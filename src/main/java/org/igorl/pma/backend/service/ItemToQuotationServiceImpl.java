package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.ItemToQuotation;
import org.igorl.pma.backend.repository.ItemToQuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemToQuotationServiceImpl implements IItemToQuotationService{

    private ItemToQuotationRepository itemToQuotationRepository;

    @Autowired
    public ItemToQuotationServiceImpl(ItemToQuotationRepository theItemToQuotationRepository) {
        itemToQuotationRepository = theItemToQuotationRepository;
    }

    @Override
    public List<ItemToQuotation> findAll() {
        return itemToQuotationRepository.findAll();
    }

    @Override
    @Transactional
    public void save(ItemToQuotation theItemToQuotation) {
        itemToQuotationRepository.save(theItemToQuotation);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        itemToQuotationRepository.deleteById(theId);
    }
}
