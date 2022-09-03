package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Memory;
import com.JonasAmme.website.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoryServiceImpl implements MemoryService {

    @Autowired
    MemoryRepository memoryRepository;

    @Override
    public void insertMemory(Memory memory){
        memoryRepository.save(memory);
    }

    @Override
    public List<Memory> getAllMemories(){
        return memoryRepository.findAll();
    }

    @Override
    public Memory getMemoriesFromId(Long id){
        return memoryRepository.findById(id).isPresent() ? memoryRepository.findById(id).get():null;
    }

    @Override
    public void deleteMemoriesFromId(Long id){
        memoryRepository.deleteById(id);
    }
}
