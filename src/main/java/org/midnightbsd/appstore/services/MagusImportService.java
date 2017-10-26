package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.*;
import org.midnightbsd.appstore.model.magus.Port;
import org.midnightbsd.appstore.model.magus.Run;
import org.midnightbsd.appstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class MagusImportService {

    private static final String API_RUN_PATH = "api/runs";
    private static final String API_RUN_PORTS_PATH = "api/run-ports-list";
    private static final String MAGUS_STATUS_PASS = "pass";

    @Value("${magus.baseUrl}")
    private String magusBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ArchitectureRepository architectureRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OperatingSystemService operatingSystemService;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageInstanceRepository packageInstanceRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SearchService searchService;

    private static final int DELAY_ONE_MINUTE = 1000 * 60;


    /**
     * Synchronize with Magus, pull new package data
     */
    @Transactional
    @Scheduled(fixedDelay = DELAY_ONE_MINUTE * 120, initialDelay = DELAY_ONE_MINUTE)
    public void sync() {
        final List<Run> runs = getFilteredRuns();
        final HashMap<String, Run> osRunMap = new HashMap<>();

        for (final Run run : runs) {
            final String os = getFilteredKey(run);
            
            // if we have a key but its less than then the current run id, ignore it
            if (osRunMap.containsKey(os) && osRunMap.get(os).getId() > run.getId())
                continue;

            osRunMap.put(os, run);
        }

        for (final Run run : osRunMap.values()) {
            Architecture arch = architectureRepository.findOneByName(run.getArch());
            if (arch == null)
            {
                log.info("Adding new architecture " + run.getArch());
                arch = new Architecture();
                arch.setName(run.getArch());
                arch = architectureRepository.saveAndFlush(arch);
            }

            OperatingSystem os = operatingSystemService.getByNameAndVersion("MidnightBSD", run.getOsVersion());
            if (os == null) {
                log.info("Adding new operating system MidnightBSD " + run.getOsVersion());
                os = new OperatingSystem();
                os.setName("MidnightBSD");
                os.setVersion(run.getOsVersion());
                os = operatingSystemService.save(os);
            }

            final List<Port> ports = getPorts(run.getId(), MAGUS_STATUS_PASS);
            log.info("Processing " + ports.size() + " ports");
            for (final Port port : ports) {
                final String catAndName = port.getPort();
                int nameSplitLoc = catAndName.indexOf('/');
                
                final String name = catAndName.substring(nameSplitLoc+ 1);
                final String category = catAndName.substring(0, nameSplitLoc);

                log.info("Attempt to find category " + category);
                Category cat = categoryService.getByName(category);
                if (cat == null) {
                    cat = new Category();
                    cat.setName(category);
                    cat.setDescription("");
                    cat = categoryService.save(cat);
                    log.info("Created new category " + cat.getName());
                }

                org.midnightbsd.appstore.model.Package pkg = packageRepository.findOneByName(name);
                if (pkg == null) {
                    pkg = new org.midnightbsd.appstore.model.Package();
                    Set<Category> s = new HashSet<>();
                    s.add(cat);
                    pkg.setCategories(s);
                }    else {
                    boolean catFound = false;
                    for (Category c : pkg.getCategories()) {
                        if (c.getName().equalsIgnoreCase(category)) {
                            catFound = true;
                            break;
                        }
                    }
                    if (!catFound)
                        pkg.getCategories().add(cat);
                }
                
                pkg.setName(name);
                pkg.setDescription(port.getDescription());
                pkg.setUrl(port.getWww());
                // TODO: other metadata
                pkg = packageRepository.saveAndFlush(pkg);
                log.info("Saved package " + pkg.getName());

                final List<PackageInstance> packageInstances = packageInstanceRepository.findByPkgAndOperatingSystemAndArchitecture(pkg, os, arch);
                if (packageInstances != null && !packageInstances.isEmpty()) {
                    // reload  TODO: update?
                    log.info("Deleting " + packageInstances.size() + " package instances");
                    packageInstanceRepository.deleteInBatch(packageInstances);
                    packageInstanceRepository.flush();
                }

                PackageInstance packageInstance = new PackageInstance();
                packageInstance.setArchitecture(arch);
                packageInstance.setOperatingSystem(os);
                packageInstance.setVersion(port.getVersion());
                packageInstance.setPkg(pkg);
                packageInstance.setRun(run.getId());
                packageInstance = packageInstanceRepository.saveAndFlush(packageInstance);

                String[] licenses = port.getLicense().split(" ");
                for (String license : licenses) {
                    if (license.isEmpty())
                        break;

                    License l = licenseRepository.findOneByName(license);
                    if (l == null) {
                        l = new License();
                        l.setName(license);
                        l = licenseRepository.saveAndFlush(l);
                    }
                    if (packageInstance.getLicenses() == null)
                        packageInstance.setLicenses(new HashSet<>());
                    packageInstance.getLicenses().add(l);

                }
                packageInstanceRepository.saveAndFlush(packageInstance); // save license link

                searchService.index(packageRepository.findOne(pkg.getId()));
            }
        }
    }

    public List<Run> getFilteredRuns() {
        log.info("Filtering runs to include only complete and active");
      return getRuns().stream().filter( r -> r.getBlessed() && (
                        r.getStatus().equalsIgnoreCase("complete") ||
                        r.getStatus().equalsIgnoreCase("active")))
                        .sorted(Comparator.comparingInt(Run::getId)).collect(Collectors.toList());
    }

    private String getFilteredKey(Run r) {
       return r.getOsVersion() + "_" + r.getArch();
    }


    public List<Run> getRuns() {
        log.info("Fetching Magus runs");
        final Run[] runs = restTemplate.getForObject(magusBaseUrl + API_RUN_PATH, Run[].class);
        return Arrays.asList(runs);
    }

    public List<Port> getPorts(final int runId, final String status) {
        log.info("Fetching Magus Ports");
        final String url = magusBaseUrl + API_RUN_PORTS_PATH + "?status=" + status + "&run=" + runId;
        final Port[] ports = restTemplate.getForObject(url, Port[].class);
        return Arrays.asList(ports);
    }
}
