package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class CurrencyConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @OneToOne
    @JoinColumn(name = "currency_exchange_rate_id")
    private CurrencyExchangeRateEntity currencyExchangeRateEntity;

    @Column(name = "conversionSum")
    private BigDecimal conversionSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
