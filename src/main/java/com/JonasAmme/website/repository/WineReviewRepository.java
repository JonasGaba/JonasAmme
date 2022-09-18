package com.JonasAmme.website.repository;

import com.JonasAmme.website.model.WineReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineReviewRepository extends JpaRepository<WineReview, Long> {
    List<WineReview> findAll();

    void deleteById(Long ID);
}

