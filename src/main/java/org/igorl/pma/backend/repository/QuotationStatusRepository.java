package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationStatusRepository extends JpaRepository<QuotationStatus, Long> {
}
