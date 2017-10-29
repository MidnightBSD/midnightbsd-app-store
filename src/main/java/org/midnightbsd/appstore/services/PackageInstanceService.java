package org.midnightbsd.appstore.services;

import groovy.util.logging.Slf4j;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.repository.PackageInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class PackageInstanceService implements AppService<PackageInstance> {

    private final PackageInstanceRepository packageInstanceRepository;

    @Autowired
    public PackageInstanceService(final PackageInstanceRepository packageInstanceRepository) {
        this.packageInstanceRepository = packageInstanceRepository;
    }

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

    @Transactional
    public PackageInstance save(PackageInstance packageInstance) {
        PackageInstance pi = packageInstanceRepository.findOne(packageInstance.getId());
        if (pi == null) {
            return packageInstanceRepository.saveAndFlush(packageInstance);
        }

        // TODO: arch/os/pkg update
        if (! packageInstance.getVersion().equals(pi.getVersion()) || ! packageInstance.getRun().equals(pi.getRun())) {
            pi.setVersion(packageInstance.getVersion());
            pi.setRun(packageInstance.getRun());
            return packageInstanceRepository.saveAndFlush(pi);
        }
        
        return pi;
    }
}
