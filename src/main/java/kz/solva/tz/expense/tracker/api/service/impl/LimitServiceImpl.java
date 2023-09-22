package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.*;
import kz.solva.tz.expense.tracker.api.dto.LimitReqest;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.repository.*;
import kz.solva.tz.expense.tracker.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Service("limitServiceImpl")
public class LimitServiceImpl implements LimitService {
    private final AccountService accountService;

    @Qualifier("paymentCategoryServiceImpl")
    private final PaymentCategoryService paymentCategoryService;

    @Qualifier("currencyServiceImpl")
    private final CurrencyService currencyService;

    private final ExpenseLimitRepository expenseLimitRepository;

    @Qualifier("currencyServiceImpl")
    private final CurrencyRateTrackerService currencyRateTrackerService;

    @Autowired
    public LimitServiceImpl(AccountService accountService,
                            PaymentCategoryService paymentCategoryService,
                            CurrencyService currencyService,
                            ExpenseLimitRepository expenseLimitRepository,
                            CurrencyRateTrackerService currencyRateTrackerService) {
        this.accountService = accountService;
        this.paymentCategoryService = paymentCategoryService;
        this.currencyService = currencyService;
        this.expenseLimitRepository = expenseLimitRepository;
        this.currencyRateTrackerService = currencyRateTrackerService;
    }

    @Transactional(rollbackFor = Exception.class)
    public ExpenseLimit setNewLimit(LimitReqest request) throws AccountNotFoundException {
        Account byAccountNumber = accountService.findByNumber(request.getAccountNumber());
        ExpenseCategory byName = paymentCategoryService.findByName(request.getPaymentCategory());
        CurrencyEntity byCurrency = currencyService.findCurrencyByShortname(request.getCurrency());
        if (byAccountNumber == null)
            throw new AccountNotFoundException("The account number or account numbers were not found based on the request");
        ExpenseLimit limit = new ExpenseLimit();
        ExpenseLimit expenseLimit = expenseLimitRepository.findFirstByCategoryAndAccountOrderByDateTimeDesc(byName, byAccountNumber);

        if (expenseLimit != null) {
            limit.setLimitAmount(request.getLimit());
            limit.setUsedAmount(expenseLimit.getUsedAmount());
        }
        else {
            limit.setLimitAmount(request.getLimit());
            limit.setUsedAmount(new BigDecimal("0.00"));
        }
        limit.setAccount(byAccountNumber);
        limit.setCategory(byName);
        limit.setCurrency(byCurrency);
        limit.setDateTime(LocalDateTime.now());// будем хранить часовой пояс в таблице клиента и
        // и расчитывать время смены лимита на стороне клиента
        // для отображения в личном кабинете в его часовом поясе
        return expenseLimitRepository.save(limit);
    }

    public BigDecimal getRemainderByAccountForCurrencyTransaction(Account account, ExpenseCategory category) throws TwelvedataApiException {
        ExpenseLimit latestLimitForCategory = expenseLimitRepository.findFirstByCategoryAndAccountOrderByDateTimeDesc(category, account);
        Currency currencyAccount = account.getCurrency().getCurrency();
        Currency currencyLimit = latestLimitForCategory.getCurrency().getCurrency();
        CurrencyExchangeRateEntity currencyExchangeRate = currencyRateTrackerService.getCurrencyExchangeRate(currencyLimit, currencyAccount, LocalDate.now());
        BigDecimal remainder = latestLimitForCategory.getLimitAmount().subtract(latestLimitForCategory.getUsedAmount());
        BigDecimal result = remainder.multiply(currencyExchangeRate.getRate());
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    public ExpenseLimit getLimitByCategory(Account account, ExpenseCategory category) {
        return expenseLimitRepository.findFirstByCategoryAndAccountOrderByDateTimeDesc(category, account);
    }
}
