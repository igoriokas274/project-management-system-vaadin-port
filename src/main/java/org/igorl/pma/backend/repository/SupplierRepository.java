package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("select s from Supplier s where lower(s.supplierName) like lower(concat('%', :searchTerm, '%'))")
    List<Supplier> search(@Param("searchTerm") String searchTerm);
}
