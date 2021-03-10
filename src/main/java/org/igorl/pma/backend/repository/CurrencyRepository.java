package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("select c from Currency c where lower(c.currencyName) like lower(concat('%', :searchTerm, '%'))")
    List<Currency> search(@Param("searchTerm") String searchTerm);
}
