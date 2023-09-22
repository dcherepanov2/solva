package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;

public interface PaymentCategoryService {
    ExpenseCategory findByName(PaymentsCategory category);
}
