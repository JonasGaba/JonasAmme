package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.Recipes;

import com.JonasAmme.website.service.RecipesService;
import com.JonasAmme.website.service.UploadedFilesService;
import com.JonasAmme.website.utils.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Controller
public class RecipesController {
    private static final String RECIPE_PHOTOS_BASE_FOLDER = "recipes_photos/";

    private static final String RECIPE_PARENT_TYPE = "RECIPE";
    @Autowired
    private RecipesService recipesService;

    @Autowired
    private UploadedFilesService uploadedFilesService;


    @GetMapping("/addrecipe")
    public String showRecipeForm(Model model) {
        Recipes recipe = new Recipes();
        model.addAttribute("recipe", recipe);


        model.addAttribute("recipePhotos", recipe.getPhotos());

        return "recipes/add_recipe";
    }

    @PostMapping("/addrecipe")
    public String submitForm(@ModelAttribute("recipe") Recipes recipe,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        System.out.println(recipe.getINSTRUCTIONS());
        System.out.println(recipe.getINGREDIENTS());

        // Need to create it here to generate ID
        recipesService.insertRecipe(recipe);
        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles){
            hasMultiPartFiles = Arrays.stream(multipartFiles).anyMatch(file->file.getSize()>0);
        }

        // Update with uploaded file children
        recipesService.insertRecipe(recipe);

        String uploadDir = RECIPE_PHOTOS_BASE_FOLDER + recipe.getID();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                System.out.println("HÆR: " + multipartFile.getSize());
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
        }
        // Add files
        if (hasMultiPartFiles) {
            setRecipePhotos(multipartFiles, recipe);
        }

        return "recipes/show_selected_recipe";
    }

    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        List<Recipes> allrecipes = recipesService.getAllRecipes();
        model.addAttribute("allRecipes", allrecipes);
        return "recipes/show_recipes";
    }


    @GetMapping("/recipes/edit/{id}")
    public String editSelectedRecipe(@PathVariable(value = "id") String id,
                                     Model model) {
        Long Id = Long.parseLong(id);
        Recipes recipe = recipesService.getRecipesFromId(Id);
        recipe.setPhotos(uploadedFilesService.getFilesFromParent(RECIPE_PARENT_TYPE, Id));
        model.addAttribute("recipe", recipe);

        model.addAttribute("recipePhotos", recipe.getPhotos());

        return "recipes/edit_selected_recipe";
    }

    @PostMapping("/recipes/edit/{id}")
    public ModelAndView submitEditedForm(@ModelAttribute("recipe") Recipes recipe,
                                         @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {


        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles) {
            if (multipartFiles[0].isEmpty() || multipartFiles[0].getSize() == 0) {
                hasMultiPartFiles = false;
            }
        }

        String uploadDir = RECIPE_PHOTOS_BASE_FOLDER + recipe.getID();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
            setRecipePhotos(multipartFiles, recipe);
        }

        // Delete photos set for deletion
        String photosToDelete = recipe.getPhotosToDelete();
        if (photosToDelete != null && !photosToDelete.isEmpty()) {
            photosToDelete = photosToDelete.substring(0, photosToDelete.length() - 1);
            recipe.setPROFILE_PICTURE(FileUpload.deleteFilesByStringIds(photosToDelete, uploadedFilesService,
                    RECIPE_PHOTOS_BASE_FOLDER, recipe.getID(), RECIPE_PARENT_TYPE,
                    recipe.getPROFILE_PICTURE()));
        }

        recipe.setDATE_MODIFIED(LocalDateTime.now());

        // Update with uploaded file children
        recipesService.insertRecipe(recipe);
        return new ModelAndView("redirect:/recipes/see/" + recipe.getID());
    }


    @GetMapping("/recipes/delete/{id}")
    public ModelAndView deleteSelectedRecipe(@PathVariable(value = "id") String id,
                                             Model model) throws IOException {
        Long Id = Long.parseLong(id);
        recipesService.deleteRecipesFromId(Id);
        uploadedFilesService.deleteFilesFromParent(RECIPE_PARENT_TYPE, Id);
        FileUtils.deleteDirectory(new File(RECIPE_PHOTOS_BASE_FOLDER + id));

        return new ModelAndView("redirect:/recipes");
    }

    @GetMapping("/recipes/see/{id}")
    public String showSelectedRecipe(@PathVariable(value = "id") String id,
                                     Model model) {
        Long Id = Long.parseLong(id);
        Recipes recipe = recipesService.getRecipesFromId(Id);
        recipe.setPhotos(uploadedFilesService.getFilesFromParent(RECIPE_PARENT_TYPE, Id));
        model.addAttribute("recipe", recipe);

        return "recipes/show_selected_recipe";
    }


    private void setRecipePhotos(MultipartFile[] multipartFiles, Recipes recipe) {
        if (multipartFiles == null || multipartFiles.length < 1) {
            return;
        }

        String recipeProfilePicture = FileUpload.uploadFilesFromInput(multipartFiles, RECIPE_PARENT_TYPE, recipe.getID(), uploadedFilesService);

        // SET recipe THUMBNAIL "PROFILE PIC"
        FileUpload.createThumbnailsFromFolder(RECIPE_PHOTOS_BASE_FOLDER + recipe.getID() + "/");
        recipe.setPROFILE_PICTURE("__th__" + recipeProfilePicture);
        recipe.setPhotos(uploadedFilesService.getFilesFromParent(RECIPE_PARENT_TYPE, recipe.getID()));

    }

}

