package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.StockType;
import org.igorl.pma.backend.repository.StockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public StockType findById(long theId) {

        Optional<StockType> result = stockTypeRepository.findById(theId);
        StockType theStockType;
        if (result.isPresent()) {
            theStockType = result.get();
        } else {
            throw new RuntimeException("Did not find Stock type id" + theId);
        }
        return theStockType;
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
