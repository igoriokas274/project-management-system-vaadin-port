package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Country;

import java.util.List;

public interface ICountryService {
    List<Country> findAll();
    List<Country> findAll(String stringFilter);
    void save(Country theCountry);
    void deleteById(long theId);
}
