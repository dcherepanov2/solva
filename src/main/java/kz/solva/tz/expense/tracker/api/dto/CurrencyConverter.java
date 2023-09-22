package kz.solva.tz.expense.tracker.api.dto;

import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import org.springframework.core.convert.converter.Converter;

public class CurrencyConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String source) {
        return Currency.valueOf(source.toUpperCase());
    }
}
