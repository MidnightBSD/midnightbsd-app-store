package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{

    List<Rating> findAllByPkg(Package pkg);

    @Query("Select new BigDecimal(avg(r.score)) FROM Rating r where pkg= :pkg")
    BigDecimal getAverageRatingByPkg(@Param("pkg") Package pkg);
}
