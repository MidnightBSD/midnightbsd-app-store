package org.midnightbsd.appstore.services;

import groovy.util.logging.Slf4j;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class PackageService implements AppService<Package> {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageService(final PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public List<Package> list() {
        return packageRepository.findAll();
    }

    @Override
    public Page<Package> get(final Pageable page) {
        return packageRepository.findAll(page);
    }

    @Override
    public Package get(final int id) {
        return packageRepository.findOne(id);
    }

    public Package getByName(final String name) {
        return packageRepository.findOneByName(name);
    }
}
