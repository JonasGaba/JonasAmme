package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Recipe;

import java.util.List;

public interface RecipeService {
    void insertRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Recipe getRecipesFromId(Long ID);

    void deleteRecipesFromId(Long ID);
}
