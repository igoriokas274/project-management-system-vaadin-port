package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.VATValue;

import java.util.List;

public interface IVATValueService {

    List<VATValue> findAll();
    List<VATValue> findAll(String searchTerm);
    void save (VATValue theVATValue);
    void deleteById(long theId);
}
