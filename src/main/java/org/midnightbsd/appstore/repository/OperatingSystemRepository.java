package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem,Integer> {

    List<OperatingSystem> findByName(@Param("name") String name);
}
