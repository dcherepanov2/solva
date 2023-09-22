package kz.solva.tz.expense.tracker.api.data;

import jakarta.persistence.*;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import lombok.Data;

@Entity
@Data
@Table(name="expense_category")
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentsCategory name;

}
