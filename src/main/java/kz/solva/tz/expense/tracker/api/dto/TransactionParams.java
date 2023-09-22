package kz.solva.tz.expense.tracker.api.dto;

import jakarta.persistence.criteria.*;
import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.data.LimitExceeded;
import kz.solva.tz.expense.tracker.api.data.Transaction;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionParams {

    private String accountFrom;

    private Boolean limitExceeded;// потом можно будет дописать другие параметры, если когда-то понадобиться делать get запросы по несколькольким фильтрам

    public Predicate buildPredicate(CriteriaBuilder builder, Root<Transaction> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (limitExceeded != null) {
            Join<Transaction, LimitExceeded> limitExceededJoin = root.join("limitExceeded", JoinType.INNER);
            predicates.add(builder.equal(limitExceededJoin.get("flag"), limitExceeded));
        }

        if (accountFrom != null) {
            Join<Transaction, Account> accountJoin = root.join("accountFrom", JoinType.INNER);
            predicates.add(builder.equal(accountJoin.get("accountNumber"), accountFrom));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
