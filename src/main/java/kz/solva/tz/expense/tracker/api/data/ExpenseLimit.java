package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class ExpenseLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    private BigDecimal limitAmount;

    private BigDecimal usedAmount;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
