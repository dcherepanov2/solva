package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.*;
import kz.solva.tz.expense.tracker.api.dto.TransactionParams;
import kz.solva.tz.expense.tracker.api.dto.TransactionRequest;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.PaymentException;
import kz.solva.tz.expense.tracker.api.exception.PurchaseLimitExceededException;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.repository.TransactionRepository;
import kz.solva.tz.expense.tracker.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service("transactionServiceImpl")
public class TransactionServiceImpl implements TransactionService<TransactionRequest>{

    private final TransactionRepository transactionRepository;

    @Qualifier("accountServiceImpl")
    private final AccountService accountService;

    @Qualifier("limitServiceImpl")
    private final LimitService limitService;

    @Qualifier("limitExceededServiceImpl")
    private final LimitExceededService limitExceededService;

    @Qualifier("paymentCategoryServiceImpl")
    private final PaymentCategoryService paymentCategoryService;

    @Qualifier("currencyRateTrackerServiceImpl")
    private final CurrencyRateTrackerService currencyRateTrackerService;

    @Qualifier("currencyConversionServiceImpl")
    private final CurrencyConversionService currencyConversionService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountService accountService,
                                  LimitService limitService,
                                  LimitExceededService limitExceededService,
                                  PaymentCategoryService paymentCategoryService,
                                  CurrencyRateTrackerService currencyRateTrackerService,
                                  CurrencyConversionService currencyConversionService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.limitService = limitService;
        this.limitExceededService = limitExceededService;
        this.paymentCategoryService = paymentCategoryService;
        this.currencyRateTrackerService = currencyRateTrackerService;
        this.currencyConversionService = currencyConversionService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class, noRollbackFor = PurchaseLimitExceededException.class)
    @Override
    public Transaction completeTransaction(TransactionRequest request) throws AccountNotFoundException,
                                                                              TwelvedataApiException,
                                                                              PurchaseLimitExceededException,
                                                                              PaymentException {
        //TODO: придумать как упростить
        Account fromAccountNumber = accountService.findByNumber(request.getAccountFrom());
        Account toAccountNumber = accountService.findByNumber(request.getAccountTo());
        ExpenseCategory category = paymentCategoryService.findByName(request.getExpenseCategory());

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
            BigDecimal requestAmountToLimitAmount =
                    currencyRateTrackerService.convertCurrencyAmount(limit.getCurrency().getCurrency(), currencyFrom, request.getSum());
            String errorMessage = "The limit has been exceeded. Limit amount: " + limit.getLimitAmount()
                                    + ", used amount: " + limit.getUsedAmount()
                                    + ", transfer amount: " + requestAmountToLimitAmount
                                    + ", currency of limit: " + limit.getCurrency().getName();
            throw new PurchaseLimitExceededException(errorMessage);
        }

        BigDecimal newBalanceFromAccount = fromAccountNumber.getBalance().subtract(request.getSum());// списываем со счета отправителя сумму указанную в запросе
        fromAccountNumber.setBalance(newBalanceFromAccount);

        if (!currencyFrom.equals(limit.getCurrency().getCurrency())) {// высчитываем новое значение кол-ва использованных средств для лимита в валюте лимита
            BigDecimal fromCurrencyToLimitCurrency =
                    currencyRateTrackerService.convertCurrencyAmount(currencyLimit, currencyFrom, request.getSum());
            BigDecimal newUsedAmount = limit.getUsedAmount().add(fromCurrencyToLimitCurrency);
            limit.setUsedAmount(newUsedAmount);
        } else {
            BigDecimal newUsedAmount = limit.getUsedAmount().add(request.getSum());
            limit.setUsedAmount(newUsedAmount);
        }

        if (fromAccountNumber.getCurrency().equals(toAccountNumber.getCurrency())) {// совершаем перевод на счет
            BigDecimal newBalanceToAccount = toAccountNumber.getBalance().add(request.getSum());
            toAccountNumber.setBalance(newBalanceToAccount);
        } else {
            BigDecimal convertSum =
                    currencyRateTrackerService.convertCurrencyAmount(currencyTo, currencyFrom, request.getSum());
            BigDecimal newBalanceToAccount = toAccountNumber.getBalance().add(convertSum);
            toAccountNumber.setBalance(newBalanceToAccount);
        }

        accountService.saveAll(List.of(fromAccountNumber, toAccountNumber));

        LimitExceeded newLimitedExceed = limitExceededService.createNewLimitedExceed(currencyExchange, false, limit);
        Transaction transaction =
                createNewTransaction(request, fromAccountNumber, toAccountNumber, category, limit, newLimitedExceed);
        List<CurrencyConversion> currencyConversions =
                currencyConversionService.saveAllConversionForCurrencyAll(transaction);

        transaction.setCurrencyConversions(currencyConversions);
        transaction.setLimitExceeded(newLimitedExceed);

        return transaction;
    }

    @Override
    public Transaction createNewTransaction(TransactionRequest request, Account from, Account to, ExpenseCategory category, ExpenseLimit limit, LimitExceeded limitExceeded) throws TwelvedataApiException {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(from);
        transaction.setAccountTo(to);
        transaction.setAmount(request.getSum());
        transaction.setDatetime(LocalDateTime.now());
        transaction.setExpenseCategory(category);
        transaction.setLimitExceeded(limitExceeded);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactions(TransactionParams transactionParams, Pageable pageable) {
        return transactionRepository.findAllByParams(transactionParams, pageable).getContent();
    }

}
