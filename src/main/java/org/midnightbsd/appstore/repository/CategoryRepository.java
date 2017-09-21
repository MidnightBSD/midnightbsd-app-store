package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findOneByName(@Param("name") String name);
}