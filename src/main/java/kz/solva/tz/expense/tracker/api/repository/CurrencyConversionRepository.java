package kz.solva.tz.expense.tracker.api.repository;

import kz.solva.tz.expense.tracker.api.data.CurrencyConversion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {
}
