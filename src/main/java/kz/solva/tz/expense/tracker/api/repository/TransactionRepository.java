package kz.solva.tz.expense.tracker.api.repository;

import jakarta.persistence.criteria.Predicate;
import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.data.Transaction;
import kz.solva.tz.expense.tracker.api.dto.TransactionParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByAccountFromAndExpenseCategory(Account accountFrom, ExpenseCategory category);

    default Page<Transaction> findAllByParams(TransactionParams params, Pageable pageable) {
        return findAll((Specification<Transaction>) (root, query, criteriaBuilder) -> {
            Predicate predicate = params.buildPredicate(criteriaBuilder, root);
            query.where(predicate);
            return query.getRestriction();
        }, pageable);
    }
}
