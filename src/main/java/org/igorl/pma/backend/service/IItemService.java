package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Item;

import java.util.List;

public interface IItemService {

    List<Item> findAll();
    void save(Item theItem);
    void deleteById(long theId);
}
