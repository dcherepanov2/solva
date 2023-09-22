package kz.solva.tz.expense.tracker.api.integration.fixer.api;

import lombok.Getter;

public enum URI {
    QUOTE("/quote") ;

    @Getter
    private final String value;

    URI(String value) {
        this.value = value;
    }
}
