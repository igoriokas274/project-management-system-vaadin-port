package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.PayTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayTermRepository extends JpaRepository<PayTerm, Long> {
}
