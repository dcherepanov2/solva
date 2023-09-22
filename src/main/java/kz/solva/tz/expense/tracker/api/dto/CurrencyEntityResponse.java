package kz.solva.tz.expense.tracker.api.dto;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import lombok.Data;

@Data
public class CurrencyEntityResponse {
    private String name;
    private Currency currency;

    public CurrencyEntityResponse(CurrencyEntity entity) {
        this.name = entity.getName();
        this.currency = entity.getCurrency();
    }
}
