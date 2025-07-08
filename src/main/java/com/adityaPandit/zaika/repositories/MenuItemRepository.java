package com.adityaPandit.zaika.repositories;

import com.adityaPandit.zaika.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for MenuItem entity to interact with DB.
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    MenuItem findByNameIgnoreCase(String name); // Optional custom finder
}
