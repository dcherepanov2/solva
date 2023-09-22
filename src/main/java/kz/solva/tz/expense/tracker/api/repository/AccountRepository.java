package kz.solva.tz.expense.tracker.api.repository;

import kz.solva.tz.expense.tracker.api.data.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String number);
}
