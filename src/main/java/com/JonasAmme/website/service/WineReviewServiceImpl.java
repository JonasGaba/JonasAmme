package com.JonasAmme.website.service;

import com.JonasAmme.website.model.WineReview;
import com.JonasAmme.website.repository.WineReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WineReviewServiceImpl implements WineReviewService {

    @Autowired
    WineReviewRepository wineReviewRepository;

    @Override
    public void insertWineReview(WineReview wineReview) {
        wineReviewRepository.save(wineReview);
    }

    @Override
    public List<WineReview> getAllWineReviews() {
        return wineReviewRepository.findAll();
    }

    @Override
    public WineReview getWineReviewFromId(Long id) {
        return wineReviewRepository.findById(id).isPresent() ? wineReviewRepository.findById(id).get() : null;
    }

    @Override
    public void deleteWineReviewFromId(Long id) {
        wineReviewRepository.deleteById(id);
    }
}
