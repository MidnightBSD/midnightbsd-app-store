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
public class LicenseService {

    @Autowired
    private LicenseRepository repository;

    public List<License> list() {
        return repository.findAll();
    }

    public Page<License> get(Pageable page) {
        return repository.findAll(page);
    }

    public License get(int id) {
        return repository.findOne(id);
    }

    public License getByName(String name) {
        return repository.findOneByName(name);
    }
}
