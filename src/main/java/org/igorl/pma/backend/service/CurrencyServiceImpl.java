package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Currency;
import org.igorl.pma.backend.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyServiceImpl implements ICurrencyService{

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository theCurrencyRepository) {
        currencyRepository = theCurrencyRepository;
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public List<Currency> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return currencyRepository.findAll();
        } else {
            return currencyRepository.search(stringFilter);
        }
    }

    @Override
    @Transactional
    public void save(Currency theCurrency) {
        currencyRepository.save(theCurrency);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        currencyRepository.deleteById(theId);
    }
}
