package com.JonasAmme.JonasAmme.service;

import com.JonasAmme.JonasAmme.model.UploadedFiles;
import com.JonasAmme.JonasAmme.repository.UploadedFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return uploadedFilesRepository.findAllByIDIn(IDs);
    }

    @Override
    public List<UploadedFiles> getFilesFromIDs(String IDs) {
        if(IDs == null){
            return null;
        }
        List<Long> IDsList = new ArrayList<>();
        IDs = IDs.replaceAll("\\s", "");
        for (String number : IDs.split(",")) {
            IDsList.add(Long.parseLong(number));
        }
        return getFilesFromIDs(IDsList);
    }

}
