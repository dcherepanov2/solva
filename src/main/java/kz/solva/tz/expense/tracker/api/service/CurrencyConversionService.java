package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyConversion;
import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.Transaction;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.repository.CurrencyConversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyConversionService {
    private final CurrencyConversionRepository currencyConversionRepository;

    private final CurrencyRateTrackerService currencyRateTrackerService;

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyConversionService(CurrencyConversionRepository currencyConversionRepository, CurrencyRateTrackerService currencyRateTrackerService, CurrencyService currencyService) {
        this.currencyConversionRepository = currencyConversionRepository;
        this.currencyRateTrackerService = currencyRateTrackerService;
        this.currencyService = currencyService;
    }

    public List<CurrencyConversion> saveAllConversionForCurrencyAll(Transaction transaction) throws TwelvedataApiException {
        List<CurrencyConversion> allConversion = new ArrayList<>();
        List<CurrencyEntity> allCurrency = currencyService.findAll();
        for(CurrencyEntity currency: allCurrency){
            Currency currencyLocal = currency.getCurrency();
            Currency currencyAccountFrom = transaction.getAccountFrom().getCurrency().getCurrency();

            if(!currencyLocal.equals(currencyAccountFrom)){
                CurrencyExchangeRateEntity currencyExchange = currencyRateTrackerService.getCurrencyExchangeRate(currencyLocal,currencyAccountFrom, LocalDate.now());
                CurrencyConversion currencyConversion = saveCurrencyConversion(currencyExchange, transaction);
                allConversion.add(currencyConversion);
            }

        }
        return allConversion;
    }
    public CurrencyConversion saveCurrencyConversion(CurrencyExchangeRateEntity currencyExchange, Transaction transaction) throws TwelvedataApiException {
        Currency currencyFrom = currencyExchange.getCurrencyFrom().getCurrency();
        Currency currencyTo = currencyExchange.getCurrencyTo().getCurrency();
        BigDecimal amountConversion = currencyRateTrackerService.convertCurrencyAmount(currencyFrom, currencyTo, transaction.getAmount());

        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setCurrency(currencyExchange.getCurrencyFrom());
        currencyConversion.setCurrencyExchangeRateEntity(currencyExchange);
        currencyConversion.setConversionSum(amountConversion);
        currencyConversion.setTransaction(transaction);

        return currencyConversionRepository.save(currencyConversion);
    }
}
