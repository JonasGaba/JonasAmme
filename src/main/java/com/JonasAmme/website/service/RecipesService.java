package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Recipes;

import java.util.List;

public interface RecipesService {
    void insertRecipe(Recipes recipe);

    List<Recipes> getAllRecipes();

    Recipes getRecipesFromId(Long ID);

    void deleteRecipesFromId(Long ID);
}
