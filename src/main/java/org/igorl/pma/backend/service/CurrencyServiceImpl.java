package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Currency;
import org.igorl.pma.backend.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Currency findById(long theId) {

        Optional<Currency> result = currencyRepository.findById(theId);
        Currency theCurrency;
        if (result.isPresent()) theCurrency = result.get();
        else {
            throw new RuntimeException("Did not find currency id" + theId);
        }
        return theCurrency;
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
