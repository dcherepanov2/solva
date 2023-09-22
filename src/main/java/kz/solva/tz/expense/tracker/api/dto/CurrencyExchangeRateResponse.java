package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Информация об курсе валютной пары")
public class CurrencyExchangeRateResponse {

    private CurrencyEntityResponse currencyFrom;

    private CurrencyEntityResponse currencyTo;

    @Schema(description = "Курс валютной пары")
    private BigDecimal rate;

    @Schema(description = "Дата")
    private LocalDate date;
    public CurrencyExchangeRateResponse(CurrencyExchangeRateEntity entity) {
        this.currencyFrom = new CurrencyEntityResponse(entity.getCurrencyFrom());
        this.currencyTo =  new CurrencyEntityResponse(entity.getCurrencyTo());
        this.rate = entity.getRate();
        this.date = entity.getDate().toLocalDate();
    }
}
