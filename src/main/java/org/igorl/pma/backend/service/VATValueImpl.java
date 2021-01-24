package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.VATValue;
import org.igorl.pma.backend.repository.VATValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VATValueImpl implements IVATValueService {

    private final VATValueRepository vatValueRepository;

    @Autowired
    public VATValueImpl (VATValueRepository theVATValueRepository){vatValueRepository = theVATValueRepository;}

    @Override
    public List<VATValue> findAll(){return vatValueRepository.findAll();}

    @Override
    public VATValue findById(long theId) {

        Optional<VATValue> result = vatValueRepository.findById(theId);
        VATValue theVATValue;
        if (result.isPresent()){
            theVATValue = result.get();
        } else {
            throw new RuntimeException("Did not find VAT value id" + theId);
        }
        return theVATValue;
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
