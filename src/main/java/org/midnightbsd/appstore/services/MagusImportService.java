package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.*;
import org.midnightbsd.appstore.model.magus.Port;
import org.midnightbsd.appstore.model.magus.Run;
import org.midnightbsd.appstore.repository.PackageInstanceRepository;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class MagusImportService {

    @Autowired
    private ArchitectureService architectureService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OperatingSystemService operatingSystemService;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageInstanceRepository packageInstanceRepository;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private PackageService packageService;


    /**
     * Synchronize with Magus, pull new package data
     */
    @CacheEvict(allEntries = true, cacheNames = {
            "category", "license", "arch", "os", "pkg"
    })
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void importRun(final Run run, final List<Port> ports) {

        final HashMap<String, Category> categoryMap = new HashMap<>();
        final HashMap<String, License> licenseMap = new HashMap<>();

        final Architecture arch = architectureService.createIfNotExists(run.getArch());
        final OperatingSystem os = operatingSystemService.createIfNotExists("MidnightBSD", run.getOsVersion());

        for (final Port port : ports) {
            try {
                final String catAndName = port.getPort();
                final int nameSplitLoc = catAndName.indexOf('/');

                final String name = catAndName.substring(nameSplitLoc + 1);
                final String category = catAndName.substring(0, nameSplitLoc);

                final Category cat;
                if (categoryMap.containsKey(category))
                    cat = categoryMap.get(category);
                else {
                    cat = categoryService.createIfNotExists(category, "");
                    categoryMap.put(category, cat);
                }

                org.midnightbsd.appstore.model.Package pkg = packageRepository.findOneByName(name);
                if (pkg == null) {
                    // new package

                    pkg = new org.midnightbsd.appstore.model.Package();
                    Set<Category> s = new HashSet<>();
                    s.add(cat);
                    pkg.setCategories(s);
                    pkg.setName(name);
                    pkg.setDescription(port.getDescription());
                    pkg.setUrl(port.getWww());
                    pkg = packageService.save(pkg);
                    log.info("Saved new package " + pkg.getName());
                } else {
                    boolean catFound = false;
                    for (final Category c : pkg.getCategories()) {
                        if (c.getName().equalsIgnoreCase(category)) {
                            catFound = true;
                            break;
                        }
                    }
                    if (!catFound)
                        pkg.getCategories().add(cat);

                    // only save if something changed.
                    if (!catFound ||
                            (port.getDescription() != null && !port.getDescription().equalsIgnoreCase(pkg.getDescription())) ||
                            (port.getWww() != null && !port.getWww().equalsIgnoreCase(pkg.getUrl()))) {
                        pkg.setDescription(port.getDescription());
                        pkg.setUrl(port.getWww());
                        pkg = packageService.save(pkg);
                        log.info("Updated package " + pkg.getName());
                    }
                }

                PackageInstance packageInstance = null;
                if (pkg.getInstances() != null) {
                    for (PackageInstance pi : pkg.getInstances()) {
                        if (pi.getOperatingSystem().getName().equals(os.getName()) &&
                                pi.getOperatingSystem().getVersion().equals(os.getVersion()) &&
                                pi.getArchitecture().getName().equals(arch.getName())) {

                            // the same exact run, do nothing.
                            if (pi.getRun().equals(run.getId())) {
                                log.info("package instance exists, do nothing for " + pkg.getName() + ": " + arch.getName() + " " + os.getVersion());
                                packageInstance = pi;
                                break;
                            }

                            // the run is different, so update it
                            pi.setRun(run.getId());
                            packageInstance = packageInstanceRepository.saveAndFlush(pi);
                            log.info("Run id updated for package instance " + pkg.getName() + ": " + arch.getName() + " " + os.getVersion());
                            break;
                        }
                    }
                }

                if (packageInstance == null) {
                    packageInstance = new PackageInstance();
                    packageInstance.setArchitecture(arch);
                    packageInstance.setOperatingSystem(os);
                    packageInstance.setVersion(port.getVersion());
                    packageInstance.setPkg(pkg);
                    packageInstance.setRun(run.getId());
                    packageInstance = packageInstanceRepository.saveAndFlush(packageInstance);
                    log.info("Package instance for " + pkg.getName() + ": " + arch.getName() + " " + os.getVersion() + " added");
                }

                boolean licenseAdded = false;
                final String[] licenses = port.getLicense().split(" ");
                for (final String license : licenses) {
                    if (license.isEmpty())
                        break;

                    final License l;
                    if (licenseMap.containsKey(license))
                        l = licenseMap.get(license);
                    else {
                        l = licenseService.createIfNotExists(license);
                        licenseMap.put(license, l);
                    }
                    if (packageInstance.getLicenses() == null)
                        packageInstance.setLicenses(new HashSet<>());

                    if (packageInstance.getLicenses().stream().noneMatch(f -> f.getName().equalsIgnoreCase(l.getName()))) {
                        packageInstance.getLicenses().add(l);
                        licenseAdded = true;
                    }
                }
                if (licenseAdded)
                    packageInstanceRepository.saveAndFlush(packageInstance); // save license link

                log.info("Indexing package " + pkg.getName());
                searchService.index(packageRepository.findById(pkg.getId()).orElse(null));
            } catch (final Exception e) {
                log.error("Error saving package " + e.getMessage(), e);
            }
        }
    }
}
