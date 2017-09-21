package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.repository.OperatingSystemRepository;
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
public class OperatingSystemService {
    @Autowired
    private OperatingSystemRepository repository;

    public List<OperatingSystem> list() {
        return repository.findAll();
    }

    public Page<OperatingSystem> get(Pageable page) {
        return repository.findAll(page);
    }

    public OperatingSystem get(int id) {
        return repository.findOne(id);
    }

    public OperatingSystem getByNameAndVersion(String name, String version) {
        return repository.findByNameAndVersion(name, version);
    }
}
