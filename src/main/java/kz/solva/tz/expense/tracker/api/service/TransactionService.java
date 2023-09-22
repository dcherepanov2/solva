package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.*;
import kz.solva.tz.expense.tracker.api.dto.TransactionParams;
import kz.solva.tz.expense.tracker.api.dto.TransactionRequest;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.PaymentException;
import kz.solva.tz.expense.tracker.api.exception.PurchaseLimitExceededException;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.repository.AccountRepository;
import kz.solva.tz.expense.tracker.api.repository.PaymentCategoryRepository;
import kz.solva.tz.expense.tracker.api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final LimitService limitService;

    private final LimitExceededService limitExceededService;

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final CurrencyRateTrackerService currencyRateTrackerService;

    private final CurrencyConversionService currencyConversionService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              AccountService accountService,
                              LimitService limitService,
                              LimitExceededService limitExceededService,
                              PaymentCategoryRepository paymentCategoryRepository,
                              CurrencyRateTrackerService currencyRateTrackerService,
                              CurrencyConversionService currencyConversionService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.limitService = limitService;
        this.limitExceededService = limitExceededService;
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.currencyRateTrackerService = currencyRateTrackerService;
        this.currencyConversionService = currencyConversionService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class, noRollbackFor = PurchaseLimitExceededException.class)
    public Transaction completeTransaction(TransactionRequest request) throws AccountNotFoundException,
                                                                              TwelvedataApiException,
                                                                              PurchaseLimitExceededException,
                                                                              PaymentException {
        //TODO: переписать метод, если останется время, применять saveCurrencyConversion при конвертации суммы валют
        //TODO: придумать как упростить
        Account fromAccountNumber = accountRepository.findByAccountNumber(request.getAccountFrom());
        Account toAccountNumber = accountRepository.findByAccountNumber(request.getAccountTo());
        ExpenseCategory category = paymentCategoryRepository.findByName(request.getExpenseCategory());

        if (fromAccountNumber == null || toAccountNumber == null) {
            throw new AccountNotFoundException("The account number or account numbers were not found based on the request");
        }
        if (fromAccountNumber.getBalance().compareTo(request.getSum()) < 0) {
            throw new PaymentException("Insufficient funds");
        }

        ExpenseLimit limit = limitService.getLimitByCategory(fromAccountNumber, category);
        Currency currencyFrom = fromAccountNumber.getCurrency().getCurrency();
        Currency currencyTo = toAccountNumber.getCurrency().getCurrency();
        Currency currencyLimit = limit.getCurrency().getCurrency();
        BigDecimal remainderByAccountForCurrencyTransaction =
                limitService.getRemainderByAccountForCurrencyTransaction(fromAccountNumber, category);
        CurrencyExchangeRateEntity currencyExchange =
                currencyRateTrackerService.getCurrencyExchangeRate(currencyFrom, currencyTo, LocalDate.now());

        if (remainderByAccountForCurrencyTransaction.compareTo(request.getSum()) < 0) { // проверяем что для перевода в нашей валюте лимит не превышен в валюте лимита
            LimitExceeded newLimitedExceed = limitExceededService.createNewLimitedExceed(currencyExchange, true, limit);
            createNewTransaction(request, fromAccountNumber, toAccountNumber, category, limit, newLimitedExceed);
            BigDecimal requestAmountToLimitAmount = currencyRateTrackerService.convertCurrencyAmount(limit.getCurrency().getCurrency(), currencyFrom, request.getSum());
            String errorMessage = "The limit has been exceeded. Limit amount: " + limit.getLimitAmount()
                    + ", used amount: " + limit.getUsedAmount() + ", transfer amount: " + requestAmountToLimitAmount + ", currency of limit: " + limit.getCurrency().getName();
            throw new PurchaseLimitExceededException(errorMessage);
        }

        BigDecimal newBalanceFromAccount = fromAccountNumber.getBalance().subtract(request.getSum());// списываем со счета отправителя сумму указанную в запросе
        newBalanceFromAccount = newBalanceFromAccount.setScale(2, RoundingMode.HALF_UP);
        fromAccountNumber.setBalance(newBalanceFromAccount);

        if (!currencyFrom.equals(limit.getCurrency().getCurrency())) {// высчитываем новое значение кол-ва использованных средств для лимита в валюте лимита
            BigDecimal test = currencyRateTrackerService.convertCurrencyAmount(currencyLimit, currencyFrom, request.getSum());
            CurrencyExchangeRateEntity currencyExchangeLimitToFrom = currencyRateTrackerService.getCurrencyExchangeRate(currencyLimit, currencyFrom, LocalDate.now());
            BigDecimal fromCurrencyToLimitCurrency = request.getSum().divide(currencyExchangeLimitToFrom.getRate(), RoundingMode.HALF_UP);
            BigDecimal newUsedAmount = limit.getUsedAmount().add(fromCurrencyToLimitCurrency);
            limit.setUsedAmount(newUsedAmount);
        } else {
            BigDecimal newUsedAmount = limit.getUsedAmount().add(request.getSum());
            limit.setUsedAmount(newUsedAmount);
        }

        if (fromAccountNumber.getCurrency().equals(toAccountNumber.getCurrency())) {// совершаем перевод на счет
            BigDecimal newBalanceToAccount = toAccountNumber.getBalance().add(request.getSum());
            newBalanceToAccount = newBalanceToAccount.setScale(2, RoundingMode.HALF_UP);
            toAccountNumber.setBalance(newBalanceToAccount);
        } else {
            BigDecimal convertSum = currencyRateTrackerService.convertCurrencyAmount(currencyTo, currencyFrom, request.getSum());
            BigDecimal newBalanceToAccount = toAccountNumber.getBalance().add(convertSum);
            newBalanceToAccount = newBalanceToAccount.setScale(2, RoundingMode.HALF_UP);
            toAccountNumber.setBalance(newBalanceToAccount);
        }

        accountService.saveAll(List.of(fromAccountNumber, toAccountNumber));

        LimitExceeded newLimitedExceed = limitExceededService.createNewLimitedExceed(currencyExchange, false, limit);
        Transaction transaction = createNewTransaction(request, fromAccountNumber, toAccountNumber, category, limit, newLimitedExceed);
        List<CurrencyConversion> currencyConversions = currencyConversionService.saveAllConversionForCurrencyAll(transaction);

        transaction.setCurrencyConversions(currencyConversions);
        transaction.setLimitExceeded(newLimitedExceed);

        return transaction;
    }

    public Transaction createNewTransaction(TransactionRequest request, Account from, Account to, ExpenseCategory category, ExpenseLimit limit, LimitExceeded limitExceeded) throws TwelvedataApiException {
        Currency currencyAccount = from.getCurrency().getCurrency();
        Currency currencyLimit = limit.getCurrency().getCurrency();
        CurrencyExchangeRateEntity exchangeRate = currencyRateTrackerService.getCurrencyExchangeRate(currencyLimit, currencyAccount, LocalDate.now());
        // логичнее и правельнее было бы умножать по курсу,
        // но у https://api.twelvedata.com допущена ошибка, при попытке
        // выставление фильтра KZT/USD выкидывает ошибку,
        //в тех поддержку писать у меня времени нету, поэтому пока оставлю так)

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(from);
        transaction.setAccountTo(to);
        transaction.setAmount(request.getSum());
        transaction.setDatetime(LocalDateTime.now());
        transaction.setExpenseCategory(category);
        transaction.setLimitExceeded(limitExceeded);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(TransactionParams transactionParams, Pageable pageable) {
        return transactionRepository.findAllByParams(transactionParams, pageable).getContent();
    }

}
