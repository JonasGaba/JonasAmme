package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.Memories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoriesRepository extends JpaRepository<Memories,Long> {
    List<Memories> findAll();

    Memories findByID(Long ID);

    void deleteByID(Long ID);
}

