package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Item;

import java.util.List;

public interface IItemService {
    List<Item> findAll();
    List<Item> findAll(String searchTerm);
    void save(Item theItem);
    void deleteById(long theId);
}
