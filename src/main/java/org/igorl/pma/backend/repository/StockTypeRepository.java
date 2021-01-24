package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.StockType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTypeRepository extends JpaRepository<StockType, Long> {
}
