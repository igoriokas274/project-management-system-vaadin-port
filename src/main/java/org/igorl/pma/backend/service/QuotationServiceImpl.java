package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Quotation;
import org.igorl.pma.backend.repository.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuotationServiceImpl implements IQuotationService{

    private QuotationRepository quotationRepository;

    @Autowired
    public QuotationServiceImpl(QuotationRepository theQuotationRepository) {
        quotationRepository = theQuotationRepository;
    }

    @Override
    public List<Quotation> findAll() {

        return quotationRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Quotation theQuotation) {

        quotationRepository.save(theQuotation);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {

        quotationRepository.deleteById(theId);
    }
}
