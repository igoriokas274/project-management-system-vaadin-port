package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Quotation;

import java.util.List;

public interface IQuotationService {
    List<Quotation> findAll();
    List<Quotation> findAll(String searchTerm);
    void save(Quotation theQuotation);
    void deleteById(long theId);
}
