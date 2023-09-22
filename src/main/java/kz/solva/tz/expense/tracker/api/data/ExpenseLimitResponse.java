package kz.solva.tz.expense.tracker.api.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseLimitResponse {

    private final ExpenseCategory category;
    private final BigDecimal limitAmount;
    private final BigDecimal usedAmount;
    private final CurrencyEntity currency;
    private final LocalDateTime dateTime;

    public ExpenseLimitResponse(ExpenseLimit expenseLimit) {
        this.category = expenseLimit.getCategory();
        this.limitAmount = expenseLimit.getLimitAmount();
        this.currency = expenseLimit.getCurrency();
        this.usedAmount = expenseLimit.getUsedAmount();
        this.dateTime = expenseLimit.getDateTime();
    }
}
