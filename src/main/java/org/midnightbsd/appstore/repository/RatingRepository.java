package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByPkg(@Param("pkg") Package pkg);

}
