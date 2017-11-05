package org.midnightbsd.appstore.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.model.search.Instance;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.midnightbsd.appstore.repository.search.PackageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class SearchService {

    @Autowired
    private PackageSearchRepository packageSearchRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Cacheable(key="#p0.concat('-').concat(#p1.getPageNumber())", value = "search")
    public Page<PackageItem> find(String term, Pageable page) {
        return packageSearchRepository.findByNameContainsOrDescriptionContainsAllIgnoreCase(term, term, page);
    }

    @CacheEvict(value = "search", allEntries = true)
    @Transactional
    @Async
    public void indexAllPackages() {
        try {
            Pageable pageable = new PageRequest(0, 100);

            Page<org.midnightbsd.appstore.model.Package> packages = packageRepository.findAll(pageable);
            for (int i = 0; i < packages.getTotalPages(); i++) {
                final ArrayList<PackageItem> items = new ArrayList<>();

                for (final org.midnightbsd.appstore.model.Package pkg : packages) {
                    items.add(convert(pkg));
                }

                log.debug("Saving a page of packages to elasticsearch. pg " + i);
                packageSearchRepository.save(items);

                pageable = new PageRequest(i + 1, 100);
                packages = packageRepository.findAll(pageable);
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @CacheEvict(value = "search", allEntries = true)
    @Transactional
    public void index(@NonNull final org.midnightbsd.appstore.model.Package pkg) {
        log.debug("Indexing package " + pkg.getName() + " id: " + pkg.getId());
        packageSearchRepository.save(convert(pkg));
    }

    public PackageItem convert(@NonNull final org.midnightbsd.appstore.model.Package pkg) {
        log.trace("Converting package " + pkg.getName() + " id: " + pkg.getId());
        
        final PackageItem packageItem = new PackageItem();
        HashMap<String,String> licenses = new HashMap<>();

        packageItem.setId(pkg.getId());

        packageItem.setName(pkg.getName());
        packageItem.setDescription(pkg.getDescription());
        packageItem.setUrl(pkg.getUrl());
        packageItem.setVersion(Calendar.getInstance().getTimeInMillis());

        List<Instance> instances = new ArrayList<>();
        if (pkg.getInstances() != null) {
            for (final PackageInstance instance : pkg.getInstances()) {
                if (instance.getLicenses() != null) {
                    for (final License license : instance.getLicenses()) {
                        licenses.putIfAbsent(license.getName(), null);
                    }
                }

                final Instance inst = new Instance();
                if (instance.getArchitecture() != null)
                    inst.setArchitecture(instance.getArchitecture().getName());
                if (instance.getOperatingSystem() != null)
                    inst.setOsVersion(instance.getOperatingSystem().getVersion());
                inst.setVersion(instance.getVersion());
                instances.add(inst);
            }
        }
        packageItem.setLicenses(new ArrayList<>(licenses.keySet()));
        packageItem.setInstances(instances);

        final HashMap<String, Object> cats = new HashMap<String, Object>();
        for (Category c : pkg.getCategories()) {
            final String cat = c.getName();
            if (!cats.containsKey(cat))
                cats.put(cat, null);
        }

        final List<org.midnightbsd.appstore.model.search.Category> targetList = new ArrayList<>();
        for (final String t : cats.keySet()) {
            final org.midnightbsd.appstore.model.search.Category category = new org.midnightbsd.appstore.model.search.Category();
            category.setName(t);
            targetList.add(category);
        }
        packageItem.setCategories(targetList);

        return packageItem;
    }
}
