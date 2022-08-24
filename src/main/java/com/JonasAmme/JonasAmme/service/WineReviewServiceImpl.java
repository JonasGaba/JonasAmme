package com.JonasAmme.JonasAmme.service;

import com.JonasAmme.JonasAmme.model.WineReview;
import com.JonasAmme.JonasAmme.repository.WineReviewRepository;
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
    public void insertWineReview(WineReview wineReview){
        wineReviewRepository.save(wineReview);
    }

    @Override
    public List<WineReview> getAllWineReviews(){
        return wineReviewRepository.findAll();
    }

    @Override
    public WineReview getWineReviewFromId(Long ID){
        return wineReviewRepository.findByID(ID);
    }

}
