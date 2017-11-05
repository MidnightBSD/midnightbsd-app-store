package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@CacheConfig(cacheNames = "license")
@Transactional(readOnly = true)
@Slf4j
@Service
public class LicenseService implements AppService<License> {

    private final LicenseRepository repository;

    @Autowired
    public LicenseService(final LicenseRepository repository) {
        this.repository = repository;
    }

    @Cacheable(key = "'licenseList'", unless = "#result == null")
    public List<License> list() {
        return repository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Page<License> get(final Pageable page) {
        return repository.findAll(page);
    }

    @Cacheable(unless = "#result == null", key = "#id.toString()")
    public License get(final int id) {
        return repository.findOne(id);
    }

    @Cacheable(unless = "#result == null", key = "#name")
    public License getByName(final String name) {
        return repository.findOneByName(name);
    }

    @Transactional
    public License save(final License license) {
        License l = repository.findOneByName(license.getName());
        if (l == null) {
            l = repository.saveAndFlush(license);
            return l;
        }

        if (! l.getUrl().equals(license.getUrl()) || ! l.getDescription().equals(license.getDescription())) {
            l.setUrl(license.getUrl());
            l.setDescription(license.getDescription());
            l = repository.saveAndFlush(l);
        }

        return l;
    }

    public License createIfNotExists(final String licenseName) {
        final License license = getByName(licenseName);
        if (license != null)
            return license;

        return save(License.builder().name(licenseName).build());
    }
}
