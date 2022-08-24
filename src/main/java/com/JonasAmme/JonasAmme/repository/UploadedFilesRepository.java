package com.JonasAmme.JonasAmme.repository;

import com.JonasAmme.JonasAmme.model.UploadedFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UploadedFilesRepository extends JpaRepository<UploadedFiles, Long> {
    List<UploadedFiles> findAll();
    List<UploadedFiles> findAllByIDIn(List<Long> iDs);
}


