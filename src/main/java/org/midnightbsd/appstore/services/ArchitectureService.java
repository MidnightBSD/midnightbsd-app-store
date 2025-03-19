package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.repository.ArchitectureRepository;
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
public class ArchitectureService implements AppService<Architecture> {

    private final ArchitectureRepository repository;

    @Autowired
    public ArchitectureService(final ArchitectureRepository repository) {
        this.repository = repository;
    }

    public List<Architecture> list() {
        return repository.findAll();
    }

    public Page<Architecture> get(final Pageable page) {
        return repository.findAll(page);
    }

    public Architecture get(final int id) {
        return repository.findById(id).orElse(null);
    }

    public Architecture getByName(final String name) {
        return repository.findOneByName(name);
    }

    @Transactional
    public Architecture save(final Architecture architecture) {
        final Architecture arch = repository.findOneByName(architecture.getName());
        if (arch == null) {
            return repository.saveAndFlush(architecture);
        }

        if (arch.getDescription().equals(architecture.getDescription()))
            return arch;
        
        arch.setDescription(architecture.getDescription());
        return repository.saveAndFlush(architecture);
    }

    public Architecture createIfNotExists(final String name) {
        final Architecture arch = getByName(name);
        if (arch != null)
            return arch;

        return save(Architecture.builder().name(name).build());
    }
}
