package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Currency;

import java.util.List;

public interface ICurrencyService {
    List<Currency> findAll();
    List<Currency> findAll(String stringFilter);
    void save(Currency theCurrency);
    void deleteById(long theId);
}
