package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
