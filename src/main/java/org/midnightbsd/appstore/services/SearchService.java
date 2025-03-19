package org.midnightbsd.appstore.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.model.search.Instance;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.midnightbsd.appstore.repository.search.PackageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.BulkFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class SearchService {

    private final PackageSearchRepository packageSearchRepository;

    private final PackageRepository packageRepository;

    public SearchService(PackageSearchRepository packageSearchRepository, PackageRepository packageRepository) {
        this.packageSearchRepository = packageSearchRepository;
        this.packageRepository = packageRepository;
    }

    public Page<PackageItem> find(String term, Pageable page) {
        return packageSearchRepository.findByNameContainsOrDescriptionContainsAllIgnoreCase(term, term, page);
    }

    @CacheEvict(value = "search", allEntries = true)
    @Transactional
    @Async
    public void indexAllPackages() {
        indexPackages(packageRepository::findAll);
    }

    @CacheEvict(value = "search", allEntries = true)
    @Transactional
    @Async
    public void indexAllPackagesSince(Date since) {
        indexPackages(pageable -> packageRepository.findByLastModifiedDateGreaterThanEqual(since, pageable));
    }

    private void indexPackages(Function<Pageable, Page<Package>> fetchPackages) {
        try {
            Pageable pageable = PageRequest.of(0, 100);
            Page<org.midnightbsd.appstore.model.Package> packages;

            do {
                packages = fetchPackages.apply(pageable);
                final ArrayList<PackageItem> items = new ArrayList<>();

                for (final org.midnightbsd.appstore.model.Package pkg : packages) {
                    items.add(convert(pkg));
                }

                log.debug("Saving a page of packages to elasticsearch. pg " + pageable.getPageNumber());
                packageSearchRepository.saveAll(items);

                pageable = PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize());
            } while (packages.hasNext());

        } catch (BulkFailureException bulk) {
            log.error("Error indexing bulk items: {}", bulk.getFailedDocuments());
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
        log.trace("Converting package {} id: {}", pkg.getName(), pkg.getId());
        
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

                instances.add(createInstance(instance));
            }
        }
        packageItem.setLicenses(new ArrayList<>(licenses.keySet()));
        packageItem.setInstances(instances);

        final Map<String, Object> cats = buildCatMap(pkg.getCategories());
        packageItem.setCategories(addCats(cats));

        return packageItem;
    }

    private Instance createInstance(PackageInstance instance) {
        final Instance inst = new Instance();
        if (instance.getArchitecture() != null)
            inst.setArchitecture(instance.getArchitecture().getName());
        if (instance.getOperatingSystem() != null)
            inst.setOsVersion(instance.getOperatingSystem().getVersion());
        inst.setVersion(instance.getVersion());
        inst.setCpe(instance.getCpe());
        inst.setFlavor(instance.getFlavor());
        return inst;
    }

    private Map<String,Object> buildCatMap(Set<Category> categies) {
        final HashMap<String, Object> cats = new HashMap<>();
        for (Category c : categies) {
            final String cat = c.getName();
            if (!cats.containsKey(cat))
                cats.put(cat, null);
        }
        return cats;
    }

    private List<org.midnightbsd.appstore.model.search.Category> addCats(Map<String,Object> cats) {
        final List<org.midnightbsd.appstore.model.search.Category> targetList = new ArrayList<>();
        for (final String t : cats.keySet()) {
            final org.midnightbsd.appstore.model.search.Category category = new org.midnightbsd.appstore.model.search.Category();
            category.setName(t);
            targetList.add(category);
        }
        return targetList;
    }
}
