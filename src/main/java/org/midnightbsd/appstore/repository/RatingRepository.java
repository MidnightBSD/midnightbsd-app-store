package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{
}
