package kz.solva.tz.expense.tracker.api.repository;

import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    ExpenseCategory findByName(PaymentsCategory name);
}
