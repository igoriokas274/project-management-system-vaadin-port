package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.PayTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayTermRepository extends JpaRepository<PayTerm, Long> {

    @Query("select p from PayTerm p where lower(p.term) like lower(concat('%', :searchTerm, '%'))")
    List<PayTerm> search(@Param("searchTerm") String searchTerm);
}
