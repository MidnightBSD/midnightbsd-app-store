package org.midnightbsd.appstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
}
