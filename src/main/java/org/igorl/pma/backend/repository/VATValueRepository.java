package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.VATValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VATValueRepository extends JpaRepository<VATValue, Long> {
}
