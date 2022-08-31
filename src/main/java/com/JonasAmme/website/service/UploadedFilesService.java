package com.JonasAmme.website.service;

import com.JonasAmme.website.model.UploadedFiles;

import java.util.List;

public interface UploadedFilesService {

    void insertUploadedFiles(UploadedFiles uploadedFiles);

    List<UploadedFiles> getFilesFromIDs(List<Long> IDs);

    List<UploadedFiles> getFilesFromIDs(String IDs);

    UploadedFiles getFileFromID(Long ID);

    List<UploadedFiles> getFilesFromParent(String parentType, Long parentId);

    void deleteFilesFromParent(String parentType, Long parentId);
    void deleteFileById(Long Id);
}
