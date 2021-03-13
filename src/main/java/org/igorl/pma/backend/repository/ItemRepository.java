package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where lower(i.itemName) like lower(concat('%', :searchTerm, '%'))")
    List<Item> searchTerm(@Param("searchTerm") String searchTerm);
}
