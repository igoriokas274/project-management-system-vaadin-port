package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ItemToQuotation;

import java.util.List;

public interface IItemToQuotationService {

    List<ItemToQuotation> findAll();
    void save(ItemToQuotation theItemToQuotation);
    void deleteById(long theId);
}
