package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_from_id", nullable = false)
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_to_id", nullable = false)
    private Account accountTo;

    private BigDecimal amount;

    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategory expenseCategory;

    @OneToOne
    @JoinColumn(name = "limit_exceeded_id")
    private LimitExceeded limitExceeded;

    @OneToMany(mappedBy = "transaction")
    private List<CurrencyConversion> currencyConversions;

}