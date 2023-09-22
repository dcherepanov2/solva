package kz.solva.tz.expense.tracker.api.repository;

import kz.solva.tz.expense.tracker.api.data.LimitExceeded;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitExceededRepository extends JpaRepository<LimitExceeded, Long> {
}
