package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.solva.tz.expense.tracker.api.data.LimitExceeded;
import lombok.Data;

@Data
@Schema(description = "Информация об блокировке")
public class LimitExceededResponse {

    @Schema(description = "True, если заблокирована. False, если не заблокирована")
    private Boolean flag;

    private CurrencyExchangeRateResponse exchangeRateAtLimit;

    public LimitExceededResponse(LimitExceeded limitExceeded){
        this.exchangeRateAtLimit = new CurrencyExchangeRateResponse(limitExceeded.getExchangeRateAtLimit());
        this.flag = limitExceeded.getFlag();
    }
}
