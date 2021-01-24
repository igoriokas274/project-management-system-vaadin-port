package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.QuotationStatus;
import org.igorl.pma.backend.repository.QuotationStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuotationStatusImpl implements IQuotationStatusService {

    private final QuotationStatusRepository quotationStatusRepository;

    @Autowired
    public QuotationStatusImpl (QuotationStatusRepository theQuotationStatusRepository){quotationStatusRepository = theQuotationStatusRepository;}

    @Override
    public List<QuotationStatus> findAll() {return quotationStatusRepository.findAll();}

    @Override
    public QuotationStatus findById(long theId) {

        Optional<QuotationStatus> result = quotationStatusRepository.findById(theId);
        QuotationStatus theQuotationStatus;
        if (result.isPresent()){
            theQuotationStatus = result.get();
        } else {
            throw new RuntimeException("Did not find Quotation status id" + theId);
        }
        return theQuotationStatus;
    }

    @Override
    @Transactional
    public void save(QuotationStatus theQuotationStatus){
        quotationStatusRepository.save(theQuotationStatus);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        quotationStatusRepository.deleteById(theId);
    }
}
