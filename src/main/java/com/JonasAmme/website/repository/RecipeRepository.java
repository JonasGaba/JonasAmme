package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAll();

    void deleteById(Long id);
}

