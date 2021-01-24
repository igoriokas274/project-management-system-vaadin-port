package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}
