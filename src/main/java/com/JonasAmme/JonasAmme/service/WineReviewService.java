package com.JonasAmme.JonasAmme.service;

import com.JonasAmme.JonasAmme.model.WineReview;

import java.util.List;

public interface WineReviewService {
    void insertWineReview(WineReview wineReview);

    List<WineReview> getAllWineReviews();

    WineReview getWineReviewFromId(Long ID);
}
