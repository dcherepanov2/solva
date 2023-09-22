package kz.solva.tz.expense.tracker.api.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {
    USD("USD"), KZT("KZT"), RUB("RUB");
    private final String value;
    Currency(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
