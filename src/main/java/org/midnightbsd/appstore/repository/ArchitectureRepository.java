package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Architecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lucas Holt
 */
@Repository
public interface ArchitectureRepository extends JpaRepository<Architecture, Integer> {

    Architecture findOneByName(@Param("name") String name);
}
