package org.midnightbsd.appstore.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.midnightbsd.appstore.repository.search.PackageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<PackageItem> find(String term, Pageable page) {
        return packageSearchRepository.findByNameContainsOrDescriptionContainsAllIgnoreCase(term, term, page);
    }

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

                packageSearchRepository.save(items);

                pageable = new PageRequest(i + 1, 100);
                packages = packageRepository.findAll(pageable);
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public PackageItem convert(@NonNull final org.midnightbsd.appstore.model.Package pkg) {
        final PackageItem packageItem = new PackageItem();
        HashMap<String,String> licenses = new HashMap<>();

        packageItem.setId(pkg.getId());

        packageItem.setName(pkg.getName());
        packageItem.setDescription(pkg.getDescription());
        packageItem.setUrl(pkg.getUrl());

        for (PackageInstance instance : pkg.getInstances()) {
            for (License license : instance.getLicenses()) {
               licenses.putIfAbsent(license.getName(), null);
            }
        }
        packageItem.setLicenses(new ArrayList<>(licenses.keySet()));

        final HashMap<String, Object> cats = new HashMap<String, Object>();
        for (Category c : pkg.getCategories()) {
            final String cat = c.getName();
            if (!cats.containsKey(cat))
                cats.put(cat, null);
        }

        final List<org.midnightbsd.appstore.model.search.Category> targetList = new ArrayList<org.midnightbsd.appstore.model.search.Category>();
        for (final String t : cats.keySet()) {
            final org.midnightbsd.appstore.model.search.Category category = new org.midnightbsd.appstore.model.search.Category();
            category.setName(t);
            targetList.add(category);
        }
        packageItem.setCategories(targetList);

        return packageItem;
    }
}
