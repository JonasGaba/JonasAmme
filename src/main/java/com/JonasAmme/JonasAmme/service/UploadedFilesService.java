package com.JonasAmme.JonasAmme.service;

import com.JonasAmme.JonasAmme.model.UploadedFiles;

import java.util.List;

public interface UploadedFilesService {

    void insertUploadedFiles(UploadedFiles uploadedFiles);

    List<UploadedFiles> getFilesFromIDs(List<Long> IDs);

    List<UploadedFiles> getFilesFromIDs(String IDs);
}
