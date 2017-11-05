package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(
          value = "SELECT distinct p FROM Package p JOIN p.instances pi JOIN pi.operatingSystem o " +
                  "JOIN pi.architecture a WHERE o.version = :os AND a.name = :arch ORDER BY p.name")
    Page<Package> findByOsAndArch(@Param("os") String os, @Param("arch") String arch, Pageable page);

    @Query(
            value = "SELECT distinct p FROM Package p JOIN p.instances pi JOIN pi.licenses lic " +
                    "WHERE lic.name = :license ORDER BY p.name")
    Page<Package> findByLicense(@Param("license") String license, Pageable page);
}
