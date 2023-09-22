package kz.solva.tz.expense.tracker.api.repository;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRateEntity, Long> {

    @Query("SELECT cer FROM CurrencyExchangeRateEntity cer " +
            "WHERE cer.currencyFrom = :baseCurrency " +
            "AND cer.currencyTo = :toCurrency " +
            "AND cer.date = :targetDate")
    CurrencyExchangeRateEntity findByPairCurrencyAndDate(@Param("baseCurrency") CurrencyEntity baseCurrency,
                                                         @Param("toCurrency") CurrencyEntity toCurrency,
                                                         @Param("targetDate") Date targetDate);
}
