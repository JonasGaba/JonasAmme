package com.JonasAmme.website.service;

import com.JonasAmme.website.model.Memories;

import java.util.List;

public interface MemoriesService {
    void insertMemory(Memories memory);

    List<Memories> getAllMemories();

    Memories getMemoriesFromId(Long ID);

    void deleteMemoriesFromId(Long ID);
}
