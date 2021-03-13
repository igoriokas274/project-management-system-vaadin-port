package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.StockType;
import org.igorl.pma.backend.repository.StockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockTypeImpl implements IStockTypeService {

    private StockTypeRepository stockTypeRepository;

    @Autowired
    public void StockTypeRepository(StockTypeRepository theStockTypeRepository) {
        stockTypeRepository = theStockTypeRepository;
    }

    @Override
    public List<StockType> findAll() {
        return stockTypeRepository.findAll();
    }

    @Override
    public List<StockType> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return stockTypeRepository.findAll();
        } else {
            return stockTypeRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(StockType theStockType) {
        stockTypeRepository.save(theStockType);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        stockTypeRepository.deleteById(theId);
    }
}
