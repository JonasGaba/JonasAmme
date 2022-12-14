package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findAll();

    void deleteById(Long id);
}

