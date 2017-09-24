package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.repository.ArchitectureRepository;
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
public class ArchitectureService implements AppService<Architecture> {

    @Autowired
    private ArchitectureRepository repository;

    public List<Architecture> list() {
        return repository.findAll();
    }

    public Page<Architecture> get(Pageable page) {
        return repository.findAll(page);
    }

    public Architecture get(int id) {
        return repository.findOne(id);
    }

    public Architecture getByName(String name) {
        return repository.findOneByName(name);
    }
}
