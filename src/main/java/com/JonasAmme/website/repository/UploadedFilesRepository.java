package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.UploadedFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UploadedFilesRepository extends JpaRepository<UploadedFiles, Long> {
    List<UploadedFiles> findAll();
    List<UploadedFiles> findByIDIn(List<Long> iDs);

    UploadedFiles findByID(Long iD);
    List<UploadedFiles> findByParentTypeAndParentId(String parentType, Long parentId);
    void deleteByParentTypeAndParentId(String parentType, Long parentId);

    void deleteById(Long Id);
}


