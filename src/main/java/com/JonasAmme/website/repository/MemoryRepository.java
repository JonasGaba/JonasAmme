package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory,Long> {
    List<Memory> findAll();

    //Memory findById(Long id);

    void deleteById(Long id);
}

