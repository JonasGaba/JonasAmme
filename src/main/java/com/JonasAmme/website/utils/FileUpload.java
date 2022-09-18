package com.JonasAmme.website.utils;


import com.JonasAmme.website.model.UploadedFile;
import com.JonasAmme.website.service.UploadedFileService;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUpload {

    public static final int thumbNailWidth = 650;
    public static final int thumbNailHeight = 350;

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

    }

    public static List<String> getFileNamesInDir(String dir) {
        File[] files = new File(dir).listFiles();
        if (files == null) {
            return null;
        }
        ArrayList<String> filePathList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                filePathList.add(file.getName());
            }
        }
        return filePathList;
    }

    public static UploadedFile insertUploadedFiles(MultipartFile multipartFile, String uploadedBy, String parentType, Long parentID) {
        // Create file entity object into UPLOADED_FILES Table
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        var uploadedFiles = new UploadedFile();
        uploadedFiles.setUploadedBy(uploadedBy);
        uploadedFiles.setImgFilename(fileName);
        uploadedFiles.setImgSize(multipartFile.getSize());
        uploadedFiles.setParentType(parentType);
        uploadedFiles.setParentId(parentID);
        return uploadedFiles;

    }

    public static boolean hasMultiPartFiles(MultipartFile[] multipartFiles) {

        boolean hasMultiPartFiles = (multipartFiles != null && multipartFiles.length > 0);
        if (hasMultiPartFiles) {
            return !multipartFiles[0].isEmpty() && multipartFiles[0].getSize() != 0;
        } else {
            return false;
        }
    }

    public static String uploadFilesFromInput(MultipartFile[] multipartFiles, String parentType, Long parentId, UploadedFileService uploadedFileService) {
        String lastFileName = null;
        for (MultipartFile multipartFile : multipartFiles) {
            UploadedFile uploadedFile = insertUploadedFiles(multipartFile, "JONAS", parentType, parentId);
            //Insert into DB
            uploadedFileService.insertUploadedFiles(uploadedFile);
            lastFileName = uploadedFile.getImgFilename();

        }
        return lastFileName;
    }

    public static String deleteFilesByStringIds(String photosToDelete, UploadedFileService uploadedFileService,
                                                String baseFolderPath, Long parentId, String parentType,
                                                String profilePictureFileName) {
        boolean profilePicWasDeleted = false;
        for (String fileIdString : photosToDelete.split(",")) {
            Long fileId = Long.parseLong(fileIdString);
            UploadedFile uploadedFile = uploadedFileService.getFileFromId(fileId);
            String fileName = uploadedFile.getImgFilename().trim();
            if (("__th__" + fileName).equals(profilePictureFileName.trim())) {
                profilePicWasDeleted = true;
            }

            File thFile = new File(baseFolderPath + parentId + "/thumbnails/__th__" + fileName);
            File file = new File(baseFolderPath + parentId + "/" + fileName);
            thFile.delete();
            file.delete();
            uploadedFileService.deleteFileById(fileId);
        }

        if (profilePicWasDeleted) {
            List<UploadedFile> uploadedFiles = uploadedFileService.getFilesFromParent(parentType, parentId);
            if (uploadedFiles == null || uploadedFiles.isEmpty()) {
                return null;
            } else {
                return "__th__" + uploadedFiles.get(0).getImgFilename();
            }
        }
        return profilePictureFileName;
    }

    public static void createThumbnailsFromFolder(String path) {

        List<String> fileNames = Arrays.stream(Objects.requireNonNull(new File(path).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName).toList();
        File folder = new File(path + "thumbnails");

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new RuntimeException(new IOException("Cannot create thumbnail folder"));
            }
        }

        for (String fileName : fileNames) {
            Image resultingImage;
            System.out.println(path + fileName);
            try {
                final BufferedImage image = ImageIO.read(new File(path + fileName));
                if (image == null) {
                    return;
                }
                resultingImage = image.getScaledInstance(thumbNailWidth, thumbNailHeight, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BufferedImage bufferedImage = new BufferedImage(thumbNailWidth, thumbNailHeight, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            File outputFile = new File(path + "thumbnails/__th__" + fileName);
            try {
                ImageIO.write(bufferedImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


    }


}
