package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.PayTerm;
import org.igorl.pma.backend.repository.PayTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PayTermServiceImpl implements IPayTermService {

    private PayTermRepository payTermRepository;

    @Autowired
    public PayTermServiceImpl(PayTermRepository thePayTermRepository) {
        payTermRepository = thePayTermRepository;
    }

    @Override
    public List<PayTerm> findAll() {
        return payTermRepository.findAll();
    }

    @Override
    public List<PayTerm> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return payTermRepository.findAll();
        } else {
            return payTermRepository.search(stringFilter);
        }
    }

    @Override
    @Transactional
    public void save(PayTerm thePayTerm) {
        payTermRepository.save(thePayTerm);
    }

    @Override
    @Transactional
    public void deleteById(Long theId) {
        payTermRepository.deleteById(theId);
    }

}

