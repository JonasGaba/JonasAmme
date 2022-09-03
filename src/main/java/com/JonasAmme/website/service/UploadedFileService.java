package com.JonasAmme.website.service;

import com.JonasAmme.website.model.UploadedFile;

import java.util.List;

public interface UploadedFileService {

    void insertUploadedFiles(UploadedFile uploadedFile);

    List<UploadedFile> getFilesFromIds(List<Long> ids);

    List<UploadedFile> getFilesFromIds(String ids);

    UploadedFile getFileFromId(Long id);

    List<UploadedFile> getFilesFromParent(String parentType, Long parentId);

    void deleteFilesFromParent(String parentType, Long parentId);
    void deleteFileById(Long Id);
}
