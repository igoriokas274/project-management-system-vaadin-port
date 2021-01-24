package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Currency;

import java.util.List;

public interface ICurrencyService {

    List<Currency> findAll();
    Currency findById(long theId);
    void save(Currency theCurrency);
    void deleteById(long theId);
}
