package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import kz.solva.tz.expense.tracker.api.repository.PaymentCategoryRepository;
import kz.solva.tz.expense.tracker.api.service.PaymentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("paymentCategoryServiceImpl")
public class PaymentCategoryServiceImpl implements PaymentCategoryService {

    private final PaymentCategoryRepository paymentCategoryRepository;

    @Autowired
    public PaymentCategoryServiceImpl(PaymentCategoryRepository paymentCategoryRepository) {
        this.paymentCategoryRepository = paymentCategoryRepository;
    }

    @Override
    public ExpenseCategory findByName(PaymentsCategory category) {
        return paymentCategoryRepository.findByName(category);
    }
}
