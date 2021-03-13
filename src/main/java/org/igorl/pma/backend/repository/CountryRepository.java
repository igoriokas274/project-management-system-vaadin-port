package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("select c from Country c where lower(c.countryName) like lower(concat('%', :searchTerm, '%'))")
    List<Country> search(@Param("searchTerm") String searchTerm);
}
