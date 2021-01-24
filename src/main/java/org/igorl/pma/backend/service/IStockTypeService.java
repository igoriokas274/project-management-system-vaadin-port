package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.StockType;

import java.util.List;

public interface IStockTypeService {

    List<StockType> findAll();
    StockType findById(long theId);
    void save (StockType theStockType);
    void deleteById(long theId);
}
