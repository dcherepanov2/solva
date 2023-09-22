package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<ExpenseLimit> expenseLimit;// по ТЗ не понятно с чем должен быть связан лимит, поэтому выставил счет,
                                     // т.к. сделать такую привязку логичнее всего
                                     // задавал этот вопрос hr, ответили, чтобы делал так, как считаю нужным
}
