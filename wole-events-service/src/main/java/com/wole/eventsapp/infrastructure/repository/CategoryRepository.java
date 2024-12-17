package com.wole.eventsapp.infrastructure.repository;

import com.wole.eventsapp.infrastructure.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
