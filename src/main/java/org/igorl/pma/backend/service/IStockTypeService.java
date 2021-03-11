package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.StockType;

import java.util.List;

public interface IStockTypeService {

    List<StockType> findAll();
    List<StockType> findAll(String searchTerm);
    void save(StockType theStockType);
    void deleteById(long theId);
}
