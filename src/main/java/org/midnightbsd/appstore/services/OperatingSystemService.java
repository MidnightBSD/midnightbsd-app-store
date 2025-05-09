package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.repository.OperatingSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Transactional(readOnly = true)
@Slf4j
@Service
public class OperatingSystemService implements AppService<OperatingSystem> {
    private final OperatingSystemRepository repository;

    @Autowired
    public OperatingSystemService(final OperatingSystemRepository repository) {
        this.repository = repository;
    }

    public List<OperatingSystem> list() {
        return repository.findAllByOrderByVersionAsc();
    }

    public Page<OperatingSystem> get(final Pageable page) {
        return repository.findAll(page);
    }

    public OperatingSystem get(final int id) {
        return repository.findById(id).orElse(null);
    }

    public OperatingSystem getByNameAndVersion(final String name, final String version) {
        return repository.findByNameAndVersion(name, version);
    }

    @Transactional
    public OperatingSystem save(OperatingSystem operatingSystem) {
        OperatingSystem existing = repository.findById(operatingSystem.getId()).orElse(null);
        if (existing == null) {
            return repository.saveAndFlush(operatingSystem);
        }

        existing.setName(operatingSystem.getName());
        existing.setVersion(operatingSystem.getVersion());
        return repository.saveAndFlush(existing);
    }

    @Transactional
    public OperatingSystem createIfNotExists(final String name, final String version) {
        final OperatingSystem os =  getByNameAndVersion(name, version);
        if (os != null)
            return os;
        return save(OperatingSystem.builder().name(name).version(version).build());
    }
}
