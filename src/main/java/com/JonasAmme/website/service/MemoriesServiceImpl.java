package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Memories;
import com.JonasAmme.website.repository.MemoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoriesServiceImpl implements MemoriesService {

    @Autowired
    MemoriesRepository memoriesRepository;

    @Override
    public void insertMemory(Memories memories){
        memoriesRepository.save(memories);
    }

    @Override
    public List<Memories> getAllMemories(){
        return memoriesRepository.findAll();
    }

    @Override
    public Memories getMemoriesFromId(Long ID){
        return memoriesRepository.findByID(ID);
    }

    @Override
    public void deleteMemoriesFromId(Long ID){
        memoriesRepository.deleteByID(ID);
    }
}
