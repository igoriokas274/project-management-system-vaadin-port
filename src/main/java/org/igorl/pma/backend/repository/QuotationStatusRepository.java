package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotationStatusRepository extends JpaRepository<QuotationStatus, Long> {

    @Query("select q from QuotationStatus q where lower(q.quotationStatusName) like lower(concat('%', :searchTerm, '%'))")
    List<QuotationStatus> search(@Param("searchTerm") String searchTerm);
}
