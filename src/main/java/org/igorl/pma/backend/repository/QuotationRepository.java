package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    @Query("select q from Quotation q where lower(q.quotationTitle) like lower(concat('%', searchTerm, '%'))")
    List<Quotation> search(@Param("searchTerm") String searchTerm);
}
