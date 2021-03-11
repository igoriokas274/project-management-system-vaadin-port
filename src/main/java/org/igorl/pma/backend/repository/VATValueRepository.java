package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.VATValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VATValueRepository extends JpaRepository<VATValue, Long> {

    @Query("select v from VATValue v where lower(v.vatValue) like lower(concat('%', :searchTerm, '%'))")
    List<VATValue> search(@Param("searchTerm") String searchTerm);
}
