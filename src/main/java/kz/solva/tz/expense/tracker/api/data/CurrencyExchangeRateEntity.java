package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "currency_exchange_rate")
@Data
public class CurrencyExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_from_id", referencedColumnName = "id")
    private CurrencyEntity currencyFrom;

    @ManyToOne
    @JoinColumn(name = "currency_to_id", referencedColumnName = "id")
    private CurrencyEntity currencyTo;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "date")
    private Date date;
}
