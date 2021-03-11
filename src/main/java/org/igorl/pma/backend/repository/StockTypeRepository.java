package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.StockType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTypeRepository extends JpaRepository<StockType, Long> {

    @Query("select s from StockType s where lower(s.stockName) like lower(concat('%', :searchTerm, '%'))")
    List<StockType> search(@Param("searchTerm") String searchTerm);
}
