package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
