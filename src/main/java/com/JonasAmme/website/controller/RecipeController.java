package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.Recipe;

import com.JonasAmme.website.service.RecipeService;
import com.JonasAmme.website.service.UploadedFileService;
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
public class RecipeController {
    private static final String RECIPE_PHOTOS_BASE_FOLDER = "recipe_photos/";

    private static final String RECIPE_PARENT_TYPE = "RECIPE";
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UploadedFileService uploadedFileService;


    @GetMapping("/admin/addrecipe")
    public String showRecipeForm(Model model) {
        Recipe recipe = new Recipe();
        model.addAttribute("recipe", recipe);


        model.addAttribute("recipePhotos", recipe.getPhotos());

        return "recipes/add_recipe";
    }

    @PostMapping("/admin/addrecipe")
    public String submitForm(@ModelAttribute("recipe") Recipe recipe,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        // Need to create it here to generate ID
        recipeService.insertRecipe(recipe);
        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles){
            hasMultiPartFiles = Arrays.stream(multipartFiles).anyMatch(file->file.getSize()>0);
        }

        // Update with uploaded file children
        recipeService.insertRecipe(recipe);

        String uploadDir = RECIPE_PHOTOS_BASE_FOLDER + recipe.getId();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
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
        List<Recipe> allrecipes = recipeService.getAllRecipes();
        model.addAttribute("allRecipes", allrecipes);
        return "recipes/show_recipes";
    }


    @GetMapping("/admin/recipes/edit/{id}")
    public String editSelectedRecipe(@PathVariable(value = "id") String id,
                                     Model model) {
        Long Id = Long.parseLong(id);
        Recipe recipe = recipeService.getRecipesFromId(Id);
        recipe.setPhotos(uploadedFileService.getFilesFromParent(RECIPE_PARENT_TYPE, Id));
        model.addAttribute("recipe", recipe);

        model.addAttribute("recipePhotos", recipe.getPhotos());

        return "recipes/edit_selected_recipe";
    }

    @PostMapping("/admin/recipes/edit/{id}")
    public ModelAndView submitEditedForm(@ModelAttribute("recipe") Recipe recipe,
                                         @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {


        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles) {
            if (multipartFiles[0].isEmpty() || multipartFiles[0].getSize() == 0) {
                hasMultiPartFiles = false;
            }
        }

        String uploadDir = RECIPE_PHOTOS_BASE_FOLDER + recipe.getId();

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
            recipe.setProfilePicture(FileUpload.deleteFilesByStringIds(photosToDelete, uploadedFileService,
                    RECIPE_PHOTOS_BASE_FOLDER, recipe.getId(), RECIPE_PARENT_TYPE,
                    recipe.getProfilePicture()));
        }

        recipe.setDateModified(LocalDateTime.now());

        // Update with uploaded file children
        recipeService.insertRecipe(recipe);
        return new ModelAndView("redirect:/recipes/see/" + recipe.getId());
    }


    @GetMapping("/admin/recipes/delete/{id}")
    public ModelAndView deleteSelectedRecipe(@PathVariable(value = "id") String id,
                                             Model model) throws IOException {
        Long Id = Long.parseLong(id);
        recipeService.deleteRecipesFromId(Id);
        uploadedFileService.deleteFilesFromParent(RECIPE_PARENT_TYPE, Id);
        FileUtils.deleteDirectory(new File(RECIPE_PHOTOS_BASE_FOLDER + id));

        return new ModelAndView("redirect:/recipes");
    }

    @GetMapping("/recipes/see/{id}")
    public String showSelectedRecipe(@PathVariable(value = "id") String id,
                                     Model model) {
        Long Id = Long.parseLong(id);
        Recipe recipe = recipeService.getRecipesFromId(Id);
        recipe.setPhotos(uploadedFileService.getFilesFromParent(RECIPE_PARENT_TYPE, Id));
        model.addAttribute("recipe", recipe);

        return "recipes/show_selected_recipe";
    }


    private void setRecipePhotos(MultipartFile[] multipartFiles, Recipe recipe) {
        if (multipartFiles == null || multipartFiles.length < 1) {
            return;
        }

        String recipeProfilePicture = FileUpload.uploadFilesFromInput(multipartFiles, RECIPE_PARENT_TYPE, recipe.getId(), uploadedFileService);

        // SET recipe THUMBNAIL "PROFILE PIC"
        FileUpload.createThumbnailsFromFolder(RECIPE_PHOTOS_BASE_FOLDER + recipe.getId() + "/");
        recipe.setProfilePicture("__th__" + recipeProfilePicture);
        recipe.setPhotos(uploadedFileService.getFilesFromParent(RECIPE_PARENT_TYPE, recipe.getId()));

    }

}

