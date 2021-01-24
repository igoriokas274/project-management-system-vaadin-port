package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
