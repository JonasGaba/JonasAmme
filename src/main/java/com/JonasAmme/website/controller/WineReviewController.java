package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.WineReview;
import com.JonasAmme.website.service.UploadedFileService;
import com.JonasAmme.website.service.WineReviewService;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class WineReviewController {
    private static final String WINE_PHOTOS_BASE_FOLDER = "wine_review_photos/";

    private static final String WINE_REVIEW_PARENT_TYPE = "WINE_REVIEW";
    @Autowired
    private WineReviewService wineReviewService;

    @Autowired
    private UploadedFileService uploadedFileService;



    @GetMapping("/admin/addwinereview")
    public String showWineReviewForm(Model model) {
        WineReview wineReview = new WineReview();
        model.addAttribute("wineReview", wineReview);

        List<String> wineType = getWineTypes();
        model.addAttribute("wineTypes", wineType);

        model.addAttribute("winePhotos", wineReview.getPhotos());

        return "winereviews/add_wine_review";
    }

    @PostMapping("/admin/addwinereview")
    public String submitForm(@ModelAttribute("wineReview") WineReview wineReview,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        // Need to create it here to generate ID
        wineReviewService.insertWineReview(wineReview);
        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);

        // Update with uploaded file children
        wineReviewService.insertWineReview(wineReview);

        String uploadDir = WINE_PHOTOS_BASE_FOLDER + wineReview.getId();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
        }
            // Add files
        if (hasMultiPartFiles) {
            setWineReviewPhotos(multipartFiles, wineReview);
        }
        return "winereviews/show_selected_wine_review";
    }

    @GetMapping("/winereviews")
    public String showWineReviews(Model model) {
        List<WineReview> allWineReviews = wineReviewService.getAllWineReviews();
        model.addAttribute("allWineReviews", allWineReviews);
        return "winereviews/show_wine_reviews";
    }


    @GetMapping("/admin/winereviews/edit/{id}")
    public String editSelectedWineReview(@PathVariable(value = "id") String id,
                                         Model model) {
        Long Id = Long.parseLong(id);
        WineReview wineReview = wineReviewService.getWineReviewFromId(Id);
        wineReview.setPhotos(uploadedFileService.getFilesFromParent(WINE_REVIEW_PARENT_TYPE, Id));

        model.addAttribute("wineReview", wineReview);
        List<String> wineType = getWineTypes();
        model.addAttribute("wineTypes", wineType);
        model.addAttribute("winePhotos", wineReview.getPhotos());

        return "winereviews/edit_selected_wine_review";
    }

    @PostMapping("/admin/winereviews/edit/{id}")
    public ModelAndView submitEditedForm(@ModelAttribute("wineReview") WineReview wineReview,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {


        boolean hasMultiPartFiles = FileUpload.hasMultiPartFiles(multipartFiles);

        String uploadDir = WINE_PHOTOS_BASE_FOLDER + wineReview.getId();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
            setWineReviewPhotos(multipartFiles, wineReview);
        }

        // Delete photos set for deletion
        String photosToDelete = wineReview.getPhotosToDelete();
        if (photosToDelete != null && !photosToDelete.isEmpty() ){
            photosToDelete = photosToDelete.substring(0,photosToDelete.length()-1);

            wineReview.setProfilePicture(FileUpload.deleteFilesByStringIds(photosToDelete, uploadedFileService,
                    WINE_PHOTOS_BASE_FOLDER, wineReview.getId(), WINE_REVIEW_PARENT_TYPE,
                    wineReview.getProfilePicture()));
        }

        wineReview.setDateModified(LocalDateTime.now());

        // Update with uploaded file children
        wineReviewService.insertWineReview(wineReview);
        return new ModelAndView("redirect:/winereviews/see/"+wineReview.getId());
    }


    @GetMapping("/admin/winereviews/delete/{id}")
    public ModelAndView  deleteSelectedWineReview(@PathVariable(value = "id") String id,
                                         Model model) throws IOException {
        Long Id = Long.parseLong(id);
        wineReviewService.deleteWineReviewFromId(Id);
        uploadedFileService.deleteFilesFromParent(WINE_REVIEW_PARENT_TYPE,Id);
        FileUtils.deleteDirectory(new File(WINE_PHOTOS_BASE_FOLDER + id));

        return new ModelAndView("redirect:/winereviews");
    }

    @GetMapping("/winereviews/see/{id}")
    public String showSelectedWineReview(@PathVariable(value = "id") String id,
                                         Model model) {
        Long Id = Long.parseLong(id);
        WineReview wineReview = wineReviewService.getWineReviewFromId(Id);
        wineReview.setPhotos(uploadedFileService.getFilesFromParent(WINE_REVIEW_PARENT_TYPE, Id));
        model.addAttribute("wineReview", wineReview);

        return "winereviews/show_selected_wine_review";
    }

    private List<String> getWineTypes() {
        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/static/assets/misc/list-of-wine-types.txt"))) {
            result = lines.collect(Collectors.toList());
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }


    private void setWineReviewPhotos(MultipartFile[] multipartFiles, WineReview wineReview) {
        if (multipartFiles == null || multipartFiles.length<1){
            return;
        }

        String wineProfilePicture = FileUpload.uploadFilesFromInput(multipartFiles, WINE_REVIEW_PARENT_TYPE, wineReview.getId(), uploadedFileService);

        // SET WINE THUMBNAIL "PROFILE PIC"
        FileUpload.createThumbnailsFromFolder(WINE_PHOTOS_BASE_FOLDER + wineReview.getId()+"/");
        wineReview.setProfilePicture("__th__" + wineProfilePicture);
        wineReview.setPhotos(uploadedFileService.getFilesFromParent(WINE_REVIEW_PARENT_TYPE, wineReview.getId()));

    }

}
