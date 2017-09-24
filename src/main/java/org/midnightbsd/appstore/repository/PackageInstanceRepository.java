package org.midnightbsd.appstore.repository;

import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.PackageInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Repository
public interface PackageInstanceRepository extends JpaRepository<PackageInstance, Integer> {
    List<PackageInstance> findByPkgAndOperatingSystemAndArchitecture(@Param("pkg") Package pkg,
                                                                         @Param("operatingSystem") OperatingSystem operatingSystem,
                                                                         @Param("architecture") Architecture architecture);

    PackageInstance findByPkgAndOperatingSystemAndArchitectureAndVersion(@Param("pkg") Package pkg,
                                                                                   @Param("operatingSystem") OperatingSystem operatingSystem,
                                                                                   @Param("architecture") Architecture architecture,
                                                                                   @Param("version") String version);
}