package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.VATValue;
import org.igorl.pma.backend.repository.VATValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VATValueImpl implements IVATValueService {

    private final VATValueRepository vatValueRepository;

    @Autowired
    public VATValueImpl (VATValueRepository theVATValueRepository){vatValueRepository = theVATValueRepository;}

    @Override
    public List<VATValue> findAll(){return vatValueRepository.findAll();}

    @Override
    public List<VATValue> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return vatValueRepository.findAll();
        } else {
            return vatValueRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save (VATValue theVATValue){vatValueRepository.save(theVATValue);}

    @Override
    @Transactional
    public void deleteById(long theId) {
        vatValueRepository.deleteById(theId);
    }
}
