package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimitResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Информация об аккаунте")
public class AccountResponse {
    private String accountNumber;
    private CurrencyEntityResponse currency;
    private BigDecimal balance;
    private ExpenseLimitResponse expenseLimits;

    public AccountResponse(Account account, ExpenseLimit expenseLimit){
        this.accountNumber = account.getAccountNumber();
        this.currency = new CurrencyEntityResponse(account.getCurrency());
        this.balance = account.getBalance();
        this.expenseLimits = new ExpenseLimitResponse(expenseLimit);
    }

    public AccountResponse(Account account){
        this.accountNumber = account.getAccountNumber();
        this.currency = new CurrencyEntityResponse(account.getCurrency());
        this.balance = account.getBalance();
    }
}
