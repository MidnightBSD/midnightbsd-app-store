package org.midnightbsd.appstore.services;

import groovy.util.logging.Slf4j;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.repository.PackageInstanceRepository;
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
public class PackageInstanceService implements AppService<PackageInstance> {

    @Autowired
    private PackageInstanceRepository packageInstanceRepository;
    
    @Override
    public List<PackageInstance> list() {
        return packageInstanceRepository.findAll();
    }

    @Override
    public Page<PackageInstance> get(final Pageable page) {
        return packageInstanceRepository.findAll(page);
    }

    @Override
    public PackageInstance get(final int id) {
        return packageInstanceRepository.findOne(id);
    }
}
