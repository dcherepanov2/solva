package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import lombok.Data;

@Entity
@Table(name = "currency")
@Data
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private Currency currency;

}
