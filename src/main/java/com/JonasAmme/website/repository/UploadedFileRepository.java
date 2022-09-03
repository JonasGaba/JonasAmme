package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findAll();
    List<UploadedFile> findByIdIn(List<Long> iDs);

    //UploadedFile findById(Long iD);
    List<UploadedFile> findByParentTypeAndParentId(String parentType, Long parentId);
    void deleteByParentTypeAndParentId(String parentType, Long parentId);

    void deleteById(Long Id);
}


