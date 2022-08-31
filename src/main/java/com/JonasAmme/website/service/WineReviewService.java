package com.JonasAmme.website.service;

import com.JonasAmme.website.model.WineReview;

import java.util.List;

public interface WineReviewService {
    void insertWineReview(WineReview wineReview);

    List<WineReview> getAllWineReviews();

    WineReview getWineReviewFromId(Long ID);

    void deleteWineReviewFromId(Long ID);
}
