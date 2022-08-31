package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipesRepository extends JpaRepository<Recipes,Long> {
    List<Recipes> findAll();

    Recipes findByID(Long ID);

    void deleteByID(Long ID);
}

