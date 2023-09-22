package kz.solva.tz.expense.tracker.api.data;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "limit_exceeded")
@Data
public class LimitExceeded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exchange_rate_id_at_limit")
    private CurrencyExchangeRateEntity exchangeRateAtLimit;

    @Column(name = "limit_exceeded")
    private Boolean limitExceeded;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_limit_id")
    private ExpenseLimit accountLimit;
}
