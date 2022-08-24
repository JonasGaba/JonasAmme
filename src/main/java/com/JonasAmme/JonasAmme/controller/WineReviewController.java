package com.JonasAmme.JonasAmme.controller;

import com.JonasAmme.JonasAmme.model.UploadedFiles;
import com.JonasAmme.JonasAmme.model.WineReview;
import com.JonasAmme.JonasAmme.service.UploadedFilesService;
import com.JonasAmme.JonasAmme.service.WineReviewService;
import com.JonasAmme.JonasAmme.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.JonasAmme.JonasAmme.utils.FileUpload.insertUploadedFiles;

@Controller
public class WineReviewController {
    private static final String WINE_PHOTOS_BASE_FOLDER = "wine_review_photos/";

    private static final String WINE_REVIEW_PARENT_TYPE = "WINE_REVIEW";
    @Autowired
    private WineReviewService wineReviewService;

    @Autowired
    private UploadedFilesService uploadedFilesService;

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

    @GetMapping("/addwinereview")
    public String showWineReviewForm(Model model) {
        WineReview wineReview = new WineReview();
        model.addAttribute("wineReview", wineReview);

        List<String> wineType = getWineTypes();
        model.addAttribute("wineTypes", wineType);

        model.addAttribute("winePhotos", wineReview.getPhotos());

        return "add_wine_review";
    }

    @PostMapping("/addwinereview")
    public String submitForm(@ModelAttribute("wineReview") WineReview wineReview,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        // Need to create it here to generate ID
        wineReviewService.insertWineReview(wineReview);
        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);

        // Update with uploaded file children
        wineReviewService.insertWineReview(wineReview);

        String uploadDir = WINE_PHOTOS_BASE_FOLDER + wineReview.getID();

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
        return "show_selected_wine_review2";
    }

    @GetMapping("/winereviews")
    public String showWineReviews(Model model) {
        List<WineReview> allWineReviews = wineReviewService.getAllWineReviews();
        model.addAttribute("allWineReviews", allWineReviews);
        return "show_wine_reviews";
    }


    @GetMapping("/winereview/edit/{id}")
    public String editSelectedWineReview(@PathVariable(value = "id") String id,
                                         Model model) {
        WineReview wineReview = wineReviewService.getWineReviewFromId(Long.parseLong(id));
        wineReview.setPhotos(uploadedFilesService.getFilesFromIDs(wineReview.getUPLOADED_FILES_IDS()));
        model.addAttribute("wineReview", wineReview);

        List<String> wineType = getWineTypes();
        model.addAttribute("wineTypes", wineType);

        model.addAttribute("winePhotos", wineReview.getPhotos());

        return "edit_selected_wine_review";
    }

    @PostMapping("/winereview/edit/{id}")
    public String submitEditedForm(@ModelAttribute("wineReview") WineReview wineReview,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles) {
            if (multipartFiles[0].isEmpty() || multipartFiles[0].getSize() == 0){
                hasMultiPartFiles = false;
            }
        }

        String uploadDir = WINE_PHOTOS_BASE_FOLDER + wineReview.getID();

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
        // Update with uploaded file children
        wineReviewService.insertWineReview(wineReview);
        return "show_selected_wine_review2";
    }

    @GetMapping("/winereview/see/{id}")
    public String showSelectedWineReview(@PathVariable(value = "id") String id,
                                         Model model) {
        WineReview wineReview = wineReviewService.getWineReviewFromId(Long.parseLong(id));
        wineReview.setPhotos(uploadedFilesService.getFilesFromIDs(wineReview.getUPLOADED_FILES_IDS()));
        model.addAttribute("wineReview", wineReview);

        return "show_selected_wine_review2";
    }


    private void setWineReviewPhotos(MultipartFile[] multipartFiles, WineReview wineReview) {
        if (multipartFiles == null || multipartFiles.length<1){
            return;
        }

        StringBuilder wineReviewFileIds = new StringBuilder();
        String wineProfilePicture = null;
        for (MultipartFile multipartFile : multipartFiles) {
            UploadedFiles uploadedFile = insertUploadedFiles(multipartFile, "JONAS", WINE_REVIEW_PARENT_TYPE, wineReview.getID());
            //Insert into DB
            uploadedFilesService.insertUploadedFiles(uploadedFile);
            // Create IDS CSV List for wine review entity
            wineReviewFileIds.append(uploadedFile.getID().toString()).append(",");
            wineProfilePicture = uploadedFile.getIMG_FILENAME();

        }
        wineReview.setUPLOADED_FILES_IDS(wineReviewFileIds.deleteCharAt(wineReviewFileIds.length() - 1).toString());

        // SET WINE THUMBNAIL "PROFILE PIC"
        FileUpload.createThumbnailsFromFolder(WINE_PHOTOS_BASE_FOLDER + wineReview.getID()+"/");
        wineReview.setPROFILE_PICTURE("__th__" + wineProfilePicture);
        wineReview.setPhotos(uploadedFilesService.getFilesFromIDs(wineReview.getUPLOADED_FILES_IDS()));

    }

    private void setWineProfilePicture(WineReview wineReview, String wineProfilePicture){
        /*Image resultingImage;
        try {
            final BufferedImage image = ImageIO.read(new File(WINE_PHOTOS_BASE_FOLDER + wineReview.getID() + "/" + wineProfilePicture));
            if (image == null){
                return;
            }
            resultingImage = image.getScaledInstance(650, 350, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage bufferedImage = new BufferedImage(650, 350, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        File outputfile = new File(WINE_PHOTOS_BASE_FOLDER + wineReview.getID() + "/__profilepicture");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        FileUpload.createThumbnailsFromFolder(WINE_PHOTOS_BASE_FOLDER + wineReview.getID()+"/");

        wineReview.setPROFILE_PICTURE("__profilepicture");
    }

}
