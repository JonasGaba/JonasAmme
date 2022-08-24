package com.JonasAmme.website.service;

import com.JonasAmme.website.model.UploadedFile;
import com.JonasAmme.website.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UploadedFileServiceImpl implements UploadedFileService {

    @Autowired
    UploadedFileRepository uploadedFileRepository;

    @Override
    public void insertUploadedFiles(UploadedFile uploadedFile) {
        uploadedFileRepository.save(uploadedFile);
    }

    @Override
    public List<UploadedFile> getFilesFromIds(List<Long> Ids) {
        return uploadedFileRepository.findByIdIn(Ids);
    }

    @Override
    public List<UploadedFile> getFilesFromIds(String Ids) {
        if (Ids == null) {
            return null;
        }
        List<Long> IdsList = new ArrayList<>();
        Ids = Ids.replaceAll("\\s", "");
        for (String number : Ids.split(",")) {
            IdsList.add(Long.parseLong(number));
        }
        return getFilesFromIds(IdsList);
    }

    @Override
    public UploadedFile getFileFromId(Long id) {
        if (id == null) {
            return null;
        }

        return uploadedFileRepository.findById(id).isPresent() ? uploadedFileRepository.findById(id).get():null;

    }

    @Override
    public List<UploadedFile> getFilesFromParent(String parentType, Long parentId) {
        return uploadedFileRepository.findByParentTypeAndParentId(parentType, parentId);
    }

    @Override
    public void deleteFilesFromParent(String parentType, Long parentId) {
        uploadedFileRepository.deleteByParentTypeAndParentId(parentType, parentId);
    }

    @Override
    public void deleteFileById(Long Id) {
        uploadedFileRepository.deleteById(Id);
    }

}
