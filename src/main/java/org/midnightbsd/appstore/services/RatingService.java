package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.model.RatingAverage;
import org.midnightbsd.appstore.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class RatingService implements AppService<Rating> {
    private final RatingRepository ratingRepository;
    private EntityManager entityManager;

    @Autowired
    public RatingService(final RatingRepository ratingRepository, EntityManager entityManager) {
        this.ratingRepository = ratingRepository;
        this.entityManager = entityManager;
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

    public RatingAverage getAverage(String pkgName) {
        Query q = entityManager.createQuery("SELECT AVG(x.score) FROM Rating x join Package p where x.pkg = p and p.name = :name");
        q.setParameter ("name", pkgName);
        Number result = (Number) q.getSingleResult();

        return new RatingAverage(result, pkgName);
    }
}
