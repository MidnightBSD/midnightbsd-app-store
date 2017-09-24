package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.repository.LicenseRepository;
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
public class LicenseService implements AppService<License> {

    private final LicenseRepository repository;

    @Autowired
    public LicenseService(final LicenseRepository repository) {
        this.repository = repository;
    }

    public List<License> list() {
        return repository.findAll();
    }

    public Page<License> get(final Pageable page) {
        return repository.findAll(page);
    }

    public License get(final int id) {
        return repository.findOne(id);
    }

    public License getByName(final String name) {
        return repository.findOneByName(name);
    }
}
