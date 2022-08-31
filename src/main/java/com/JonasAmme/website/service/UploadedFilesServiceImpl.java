package com.JonasAmme.website.service;

import com.JonasAmme.website.model.UploadedFiles;
import com.JonasAmme.website.repository.UploadedFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UploadedFilesServiceImpl implements UploadedFilesService {

    @Autowired
    UploadedFilesRepository uploadedFilesRepository;

    @Override
    public void insertUploadedFiles(UploadedFiles uploadedFiles) {
        uploadedFilesRepository.save(uploadedFiles);
    }

    @Override
    public List<UploadedFiles> getFilesFromIDs(List<Long> IDs) {
        return uploadedFilesRepository.findByIDIn(IDs);
    }

    @Override
    public List<UploadedFiles> getFilesFromIDs(String IDs) {
        if (IDs == null) {
            return null;
        }
        List<Long> IDsList = new ArrayList<>();
        IDs = IDs.replaceAll("\\s", "");
        for (String number : IDs.split(",")) {
            IDsList.add(Long.parseLong(number));
        }
        return getFilesFromIDs(IDsList);
    }

    @Override
    public UploadedFiles getFileFromID(Long ID) {
        if (ID == null) {
            return null;
        }
        return uploadedFilesRepository.findByID(ID);

    }

    @Override
    public List<UploadedFiles> getFilesFromParent(String parentType, Long parentId) {
        return uploadedFilesRepository.findByParentTypeAndParentId(parentType, parentId);
    }

    @Override
    public void deleteFilesFromParent(String parentType, Long parentId) {
        uploadedFilesRepository.deleteByParentTypeAndParentId(parentType, parentId);
    }

    @Override
    public void deleteFileById(Long Id) {
        uploadedFilesRepository.deleteById(Id);
    }

}
