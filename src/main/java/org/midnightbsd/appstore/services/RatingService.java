package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class RatingService implements AppService<Rating> {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(final RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> list() {
        return ratingRepository.findAll();
    }

    @Override
    public Page<Rating> get(final Pageable page) {
        return ratingRepository.findAll(page);
    }

    @Override
    public Rating get(final int id) {
        return ratingRepository.findOne(id);
    }
}
