package kz.solva.tz.expense.tracker.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseLimitResponse {

    private BigDecimal limitAmount;

    private CurrencyEntityResponse currencyResponse;


}
