package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface PackageRepository extends JpaRepository<org.midnightbsd.appstore.model.Package, Integer> {
    org.midnightbsd.appstore.model.Package findOneByName(@Param("name") String name);

    List<org.midnightbsd.appstore.model.Package> findByCategories(@Param("category") Category category);
}
