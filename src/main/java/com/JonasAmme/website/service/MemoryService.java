package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Memory;

import java.util.List;

public interface MemoryService {
    void insertMemory(Memory memory);

    List<Memory> getAllMemories();

    Memory getMemoriesFromId(Long ID);

    void deleteMemoriesFromId(Long ID);
}
