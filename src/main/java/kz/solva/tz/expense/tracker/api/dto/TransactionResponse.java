package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TransactionResponse {

    private AccountResponse accountFrom;

    private AccountResponse accountTo;

    @Schema(description = "Сумма транзакции")
    private BigDecimal amount;

    @Schema(description = "Время проведения транзакции")
    private LocalDateTime datetime;

    @Schema(description = "Категория")
    private ExpenseCategoryResponse expenseCategory;

    @Schema(description = "Информация о блокировке транзакции")
    private LimitExceededResponse limitExceeded;

    @Schema(description = "Информация о сумме перевода конвертированная во все остальные валюты(расчитывается по курсу на момент совершения транзакции)")
    private List<CurrencyConversionResponse> conversions;

    public TransactionResponse(Transaction transaction) {
        ExpenseLimit accountLimit = transaction.getLimitExceeded().getAccountLimit();
        this.accountFrom = new AccountResponse(transaction.getAccountFrom(), accountLimit);
        this.accountTo = new AccountResponse(transaction.getAccountTo());
        this.amount = transaction.getAmount();
        this.datetime = transaction.getDatetime();
        this.expenseCategory = new ExpenseCategoryResponse(transaction.getExpenseCategory());
        this.limitExceeded = new LimitExceededResponse(transaction.getLimitExceeded());
        this.conversions = transaction.getCurrencyConversions().stream()
                .map(CurrencyConversionResponse::new)
                .collect(Collectors.toList());
    }
}
