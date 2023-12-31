package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.integration.fixer.api.RequestSender;
import kz.solva.tz.expense.tracker.api.integration.fixer.api.TwelvedataRequest;
import kz.solva.tz.expense.tracker.api.integration.fixer.api.URI;
import kz.solva.tz.expense.tracker.api.integration.fixer.api.dto.QuoteResponse;
import kz.solva.tz.expense.tracker.api.repository.CurrencyExchangeRateRepository;
import kz.solva.tz.expense.tracker.api.repository.CurrencyRepository;
import kz.solva.tz.expense.tracker.api.service.CurrencyRateTrackerService;
import kz.solva.tz.expense.tracker.api.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;

@Service("currencyRateTrackerServiceImpl")
public class CurrencyRateTrackerServiceImpl implements CurrencyRateTrackerService {
    @Qualifier("twelvedataRequestSender")
    private final RequestSender twelvedataRequestSender;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    @Qualifier("currencyServiceImpl")
    private final CurrencyService currencyService;

    @Value("${twelvedata.api.access.key}")
    private String accessKey;

    @Value("${twelvedata.api.base.url}")
    private String host;

    @Autowired
    public CurrencyRateTrackerServiceImpl(RequestSender twelvedataRequestSender, CurrencyExchangeRateRepository currencyExchangeRateRepository, CurrencyService currencyService) {
        this.twelvedataRequestSender = twelvedataRequestSender;
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
        this.currencyService = currencyService;
    }

    @Override
    public QuoteResponse fetchCurrencyPairData(Currency base, Currency to) {
        TwelvedataRequest twelvedataRequest = new TwelvedataRequest.Builder()
                .host(host)
                .uri(URI.QUOTE)
                .accessKey(accessKey)
                .interval(Duration.ofDays(1))
                .symbolParam(base, to)
                .build();
        return twelvedataRequestSender.getQuote(twelvedataRequest);
    }

    @Override
    public CurrencyExchangeRateEntity getCurrencyExchangeRate(Currency base, Currency to, LocalDate date) throws TwelvedataApiException {
        CurrencyEntity baseEntity = currencyService.findCurrencyByShortname(base);
        CurrencyEntity toEntity = currencyService.findCurrencyByShortname(to);
        CurrencyExchangeRateEntity byPairCurrency = currencyExchangeRateRepository.findByPairCurrencyAndDate(baseEntity, toEntity, Date.valueOf(date));
        if (byPairCurrency == null) {
            QuoteResponse response = fetchCurrencyPairData(base, to);
            if (response.getError() != null) {
                throw new TwelvedataApiException(response.getError().getMessage());
            }
            byPairCurrency = saveCurrencyExchange(response, baseEntity, toEntity, date);
        }
        return byPairCurrency;
    }

    @Override
    public BigDecimal convertCurrencyAmount(Currency from, Currency to, BigDecimal amount) throws TwelvedataApiException {
        CurrencyExchangeRateEntity currencyExchange = getCurrencyExchangeRate(from, to, LocalDate.now());
        return amount.divide(currencyExchange.getRate(), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }// логичнее и правельнее было бы умножать по курсу,
    // но у https://api.twelvedata.com допущена ошибка, при попытке
    // выставление фильтра KZT/USD выкидывает ошибку,
    //в тех поддержку писать у меня времени нету, поэтому пока оставлю так)

    @Override
    public CurrencyExchangeRateEntity saveCurrencyExchange(QuoteResponse response, CurrencyEntity baseEntity, CurrencyEntity toEntity, LocalDate date) {
        CurrencyExchangeRateEntity currencyExchangeRateEntity = new CurrencyExchangeRateEntity();
        currencyExchangeRateEntity.setCurrencyFrom(baseEntity);
        currencyExchangeRateEntity.setCurrencyTo(toEntity);
        currencyExchangeRateEntity.setDate(Date.valueOf(date));
        if (response.getClose() != null) {
            BigDecimal rate = new BigDecimal(response.getClose());
            currencyExchangeRateEntity.setRate(rate.setScale(6, RoundingMode.HALF_UP));
        } else {
            BigDecimal rate = new BigDecimal(response.getPreviousClose());
            currencyExchangeRateEntity.setRate(rate.setScale(6, RoundingMode.HALF_UP));
        }
        return currencyExchangeRateRepository.save(currencyExchangeRateEntity);
    }
}
