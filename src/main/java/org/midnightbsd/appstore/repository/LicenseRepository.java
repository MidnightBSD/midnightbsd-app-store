package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {
    License findOneByName(@Param("name") String name);
}