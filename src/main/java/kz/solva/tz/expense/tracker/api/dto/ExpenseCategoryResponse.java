package kz.solva.tz.expense.tracker.api.dto;

import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import lombok.Data;

@Data
public class ExpenseCategoryResponse {

    private PaymentsCategory category;
    public ExpenseCategoryResponse(ExpenseCategory category){
        this.category = category.getName();
    }
}
