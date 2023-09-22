package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.LimitExceeded;
import kz.solva.tz.expense.tracker.api.repository.LimitExceededRepository;
import kz.solva.tz.expense.tracker.api.service.LimitExceededService;
import org.springframework.stereotype.Service;

@Service("limitExceededServiceImpl")
public class LimitExceededServiceImpl implements LimitExceededService {

    private final LimitExceededRepository limitExceededRepository;

    public LimitExceededServiceImpl(LimitExceededRepository limitExceededRepository) {
        this.limitExceededRepository = limitExceededRepository;
    }

    @Override
    public LimitExceeded createNewLimitedExceed(CurrencyExchangeRateEntity currencyExchange, Boolean limitFlag, ExpenseLimit accountLimit){
        LimitExceeded limitExceeded = new LimitExceeded();
        limitExceeded.setExchangeRateAtLimit(currencyExchange);
        limitExceeded.setFlag(limitFlag);
        limitExceeded.setAccountLimit(accountLimit);
        return limitExceededRepository.save(limitExceeded);
    }
}
