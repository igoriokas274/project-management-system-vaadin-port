package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Country;

import java.util.List;

public interface ICountryService {
    List<Country> findAll();
    Country findById(long theId);
    void save(Country theCountry);
    void deleteById(long theId);
}
