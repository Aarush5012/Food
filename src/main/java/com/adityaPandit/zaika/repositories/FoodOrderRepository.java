package com.adityaPandit.zaika.repositories;

import com.adityaPandit.zaika.entities.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    @EntityGraph(attributePaths = "items")
    List<FoodOrder> findByUser_Username(String username);
}
