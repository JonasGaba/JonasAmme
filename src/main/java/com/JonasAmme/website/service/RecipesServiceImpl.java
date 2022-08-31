package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Recipes;
import com.JonasAmme.website.repository.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipesServiceImpl implements RecipesService {

    @Autowired
    RecipesRepository recipesRepository;

    @Override
    public void insertRecipe(Recipes recipes){
        recipesRepository.save(recipes);
    }

    @Override
    public List<Recipes> getAllRecipes(){
        return recipesRepository.findAll();
    }

    @Override
    public Recipes getRecipesFromId(Long ID){
        return recipesRepository.findByID(ID);
    }

    @Override
    public void deleteRecipesFromId(Long ID){
        recipesRepository.deleteByID(ID);
    }
}
