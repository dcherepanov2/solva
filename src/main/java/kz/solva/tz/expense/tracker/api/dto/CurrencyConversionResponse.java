package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.solva.tz.expense.tracker.api.data.CurrencyConversion;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyConversionResponse {

    @Schema(description = "Валюта и ее название")
    private final CurrencyEntityResponse currency;

    @Schema(description = "Сумма")
    private final BigDecimal conversionSum;
    public CurrencyConversionResponse(CurrencyConversion currencyConversion) {
        this.currency = new CurrencyEntityResponse(currencyConversion.getCurrency());
        this.conversionSum = currencyConversion.getConversionSum();
    }
}
