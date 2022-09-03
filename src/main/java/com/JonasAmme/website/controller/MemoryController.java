package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.Memory;
import com.JonasAmme.website.service.MemoryService;
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
import java.util.List;
import java.util.Objects;


@Controller
public class MemoryController {
    private static final String MEMORY_PHOTOS_BASE_FOLDER = "memory_photos/";

    private static final String MEMORY_PARENT_TYPE = "MEMORY";
    @Autowired
    private MemoryService memoryService;

    @Autowired
    private UploadedFileService uploadedFileService;


    @GetMapping("/admin/addmemory")
    public String showWineReviewForm(Model model) {
        Memory memory = new Memory();
        model.addAttribute("memory", memory);


        model.addAttribute("memoryPhotos", memory.getPhotos());

        return "memories/add_memory";
    }

    @PostMapping("/admin/addmemory")
    public String submitForm(@ModelAttribute("memory") Memory memory,
                             @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {

        // Need to create it here to generate ID
        memoryService.insertMemory(memory);
        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);

        // Update with uploaded file children
        memoryService.insertMemory(memory);

        String uploadDir = MEMORY_PHOTOS_BASE_FOLDER + memory.getId();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
        }
        // Add files
        if (hasMultiPartFiles) {
            setMemoryPhotos(multipartFiles, memory);
        }
        return "memories/show_selected_memory";
    }

    @GetMapping("/memories")
    public String showMemories(Model model) {
        List<Memory> allmemories = memoryService.getAllMemories();
        model.addAttribute("allMemories", allmemories);
        return "memories/show_memories";
    }


    @GetMapping("/admin/memories/edit/{id}")
    public String editSelectedMemory(@PathVariable(value = "id") String id,
                                         Model model) {
        Long Id = Long.parseLong(id);
        Memory memory = memoryService.getMemoriesFromId(Id);
        memory.setPhotos(uploadedFileService.getFilesFromParent(MEMORY_PARENT_TYPE, Id));
        model.addAttribute("memory", memory);

        model.addAttribute("memoryPhotos", memory.getPhotos());

        return "memories/edit_selected_memory";
    }

    @PostMapping("/admin/memories/edit/{id}")
    public ModelAndView submitEditedForm(@ModelAttribute("memory") Memory memory,
                                         @RequestParam("image") MultipartFile[] multipartFiles) throws IOException {


        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles) {
            if (multipartFiles[0].isEmpty() || multipartFiles[0].getSize() == 0) {
                hasMultiPartFiles = false;
            }
        }

        String uploadDir = MEMORY_PHOTOS_BASE_FOLDER + memory.getId();

        if (hasMultiPartFiles) {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUpload.saveFile(uploadDir, fileName, multipartFile);
            }
            setMemoryPhotos(multipartFiles, memory);
        }

        // Delete photos set for deletion
        String photosToDelete = memory.getPhotosToDelete();
        if (photosToDelete != null && !photosToDelete.isEmpty()) {
            photosToDelete = photosToDelete.substring(0, photosToDelete.length() - 1);
            memory.setProfilePicture(FileUpload.deleteFilesByStringIds(photosToDelete, uploadedFileService,
                    MEMORY_PHOTOS_BASE_FOLDER, memory.getId(), MEMORY_PARENT_TYPE,
                    memory.getProfilePicture()));
        }

        memory.setDateModified(LocalDateTime.now());

        // Update with uploaded file children
        memoryService.insertMemory(memory);
        return new ModelAndView("redirect:/memories/see/" + memory.getId());
    }


    @GetMapping("/admin/memories/delete/{id}")
    public ModelAndView deleteSelectedMemory(@PathVariable(value = "id") String id,
                                                 Model model) throws IOException {
        Long Id = Long.parseLong(id);
        memoryService.deleteMemoriesFromId(Id);
        uploadedFileService.deleteFilesFromParent(MEMORY_PARENT_TYPE, Id);
        FileUtils.deleteDirectory(new File(MEMORY_PHOTOS_BASE_FOLDER + id));

        return new ModelAndView("redirect:/memories");
    }

    @GetMapping("/memories/see/{id}")
    public String showSelectedMemory(@PathVariable(value = "id") String id,
                                         Model model) {
        Long Id = Long.parseLong(id);
        Memory memory = memoryService.getMemoriesFromId(Id);
        memory.setPhotos(uploadedFileService.getFilesFromParent(MEMORY_PARENT_TYPE, Id));
        model.addAttribute("memory", memory);

        return "memories/show_selected_memory";
    }


    private void setMemoryPhotos(MultipartFile[] multipartFiles, Memory memory) {
        if (multipartFiles == null || multipartFiles.length < 1) {
            return;
        }

        String memoryProfilePicture = FileUpload.uploadFilesFromInput(multipartFiles, MEMORY_PARENT_TYPE, memory.getId(), uploadedFileService);

        // SET memory THUMBNAIL "PROFILE PIC"
        FileUpload.createThumbnailsFromFolder(MEMORY_PHOTOS_BASE_FOLDER + memory.getId() + "/");
        memory.setProfilePicture("__th__" + memoryProfilePicture);
        memory.setPhotos(uploadedFileService.getFilesFromParent(MEMORY_PARENT_TYPE, memory.getId()));

    }

}

