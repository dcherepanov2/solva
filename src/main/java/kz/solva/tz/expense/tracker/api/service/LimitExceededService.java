package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.LimitExceeded;
import kz.solva.tz.expense.tracker.api.repository.LimitExceededRepository;
import org.springframework.stereotype.Service;

@Service
public class LimitExceededService {

    private final LimitExceededRepository limitExceededRepository;

    public LimitExceededService(LimitExceededRepository limitExceededRepository) {
        this.limitExceededRepository = limitExceededRepository;
    }

    public LimitExceeded createNewLimitedExceed(CurrencyExchangeRateEntity currencyExchange, Boolean limitFlag, ExpenseLimit accountLimit){
        LimitExceeded limitExceeded = new LimitExceeded();
        limitExceeded.setExchangeRateAtLimit(currencyExchange);
        limitExceeded.setLimitExceeded(limitFlag);
        limitExceeded.setAccountLimit(accountLimit);
        return limitExceededRepository.save(limitExceeded);
    }
}
