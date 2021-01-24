package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Item;
import org.igorl.pma.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements IItemService{

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository theIemRepository) {
        itemRepository = theIemRepository;
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Item theItem) {
        itemRepository.save(theItem);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        itemRepository.deleteById(theId);
    }
}
