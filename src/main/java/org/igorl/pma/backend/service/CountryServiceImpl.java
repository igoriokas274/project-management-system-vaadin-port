package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Country;
import org.igorl.pma.backend.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CountryServiceImpl implements ICountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl (CountryRepository theCountryRepository) {
        countryRepository = theCountryRepository;
    }

    @Override
    public List<Country> findAll() { return countryRepository.findAll(); }

    @Override
    public List<Country> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return countryRepository.findAll();
        } else {
            return countryRepository.search(stringFilter);
        }
    }

    @Override
    @Transactional
    public void save(Country theCountry) {
        countryRepository.save(theCountry);

    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        countryRepository.deleteById(theId);

    }
}
