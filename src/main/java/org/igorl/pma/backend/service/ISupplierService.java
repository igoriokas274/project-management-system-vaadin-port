package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Supplier;

import java.util.List;

public interface ISupplierService {
    List<Supplier> findAll();
    List<Supplier> findAll(String searchTerm);
    void save(Supplier theSupplier);
    void deleteById(long theId);
}
