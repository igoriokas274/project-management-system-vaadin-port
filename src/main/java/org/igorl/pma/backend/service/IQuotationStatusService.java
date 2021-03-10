package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.QuotationStatus;

import java.util.List;

public interface IQuotationStatusService {
    List<QuotationStatus> findAll();
    List<QuotationStatus> findAll(String searchTerm);
    void save (QuotationStatus theQuotationStatus);
    void deleteById(long theId);
}
