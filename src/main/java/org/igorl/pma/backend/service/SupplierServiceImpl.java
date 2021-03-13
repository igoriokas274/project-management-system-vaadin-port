package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Supplier;
import org.igorl.pma.backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements ISupplierService {

    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository theSupplierRepository) {
        supplierRepository = theSupplierRepository;
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public List<Supplier> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return supplierRepository.findAll();
        } else {
            return supplierRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(Supplier theSupplier) {
        supplierRepository.save(theSupplier);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        supplierRepository.deleteById(theId);
    }
}
