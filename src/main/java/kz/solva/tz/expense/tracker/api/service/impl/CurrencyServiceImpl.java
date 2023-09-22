package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.repository.CurrencyRepository;
import kz.solva.tz.expense.tracker.api.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("currencyServiceImpl")
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<CurrencyEntity> findAll(){
        return currencyRepository.findAll();
    }

    @Override
    public CurrencyEntity findCurrencyByShortname(Currency currency) {
        return currencyRepository.findByCurrency(currency);
    }
}
