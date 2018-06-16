package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

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
        Optional<Package> pkg = packageRepository.findById(id);
        return pkg.orElse(null);
    }

    @Cacheable(key="#p0.concat('-byname')")
    public Package getByName(final String name) {
        return packageRepository.findOneByName(name);
    }

    @Cacheable(key="#p0.concat('-bycatname')")
    public List<Package> getByCategoryName(final String name) {
      final Category category = categoryService.getByName(name);
      return packageRepository.findByCategoriesOrderByNameAsc(category);
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
        Optional<org.midnightbsd.appstore.model.Package> op = packageRepository.findById(pkg.getId());
        if (!op.isPresent()) {
            // new package
            return packageRepository.saveAndFlush(pkg);
        }
       
        org.midnightbsd.appstore.model.Package p = op.get();
        p.setCategories(pkg.getCategories());
        p.setDescription(pkg.getDescription());
        p.setUrl(pkg.getUrl());
        p = packageRepository.saveAndFlush(p);

        return p;
    }
}
