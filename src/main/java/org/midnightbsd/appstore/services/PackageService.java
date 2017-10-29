package org.midnightbsd.appstore.services;

import groovy.util.logging.Slf4j;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "pkg")
@Slf4j
@Service
public class PackageService implements AppService<Package> {

    private final PackageRepository packageRepository;
    private final CategoryService categoryService;

    @Autowired
    public PackageService(final PackageRepository packageRepository, CategoryService categoryService) {
        this.packageRepository = packageRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Package> list() {
        return packageRepository.findAll();
    }

    @Override
    public Page<Package> get(final Pageable page) {
        return packageRepository.findAll(page);
    }

    @Cacheable(unless = "#result == null", key = "#id.toString()")
    @Override
    public Package get(final int id) {
        return packageRepository.findOne(id);
    }

    @Cacheable(key="#p0.concat('-byname')")
    public Package getByName(final String name) {
        return packageRepository.findOneByName(name);
    }

    @Cacheable(key="#p0.concat('-bycatname')")
    public List<Package> getByCategoryName(final String name) {
      final Category category = categoryService.getByName(name);
      return packageRepository.findByCategories(category);
    }

    public Page<Package> getByOsAndArch(final String os, final String arch, Pageable page) {
        return packageRepository.findByOsAndArch(os, arch, page);
    }

    public Page<Package> getByLicense(final String license, Pageable page) {
          return packageRepository.findByLicense(license, page);
      }

    @Transactional
    @CacheEvict(allEntries = true)
    public Package save(Package pkg) {
        org.midnightbsd.appstore.model.Package p = packageRepository.findOne(pkg.getId());
        if (p == null) {
            // new package
            return packageRepository.saveAndFlush(pkg);
        }
        
        p.setCategories(pkg.getCategories());
        p.setDescription(pkg.getDescription());
        p.setUrl(pkg.getUrl());
        p = packageRepository.saveAndFlush(p);

        return p;
    }
}
