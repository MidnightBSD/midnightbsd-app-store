package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.model.magus.Port;
import org.midnightbsd.appstore.model.magus.Run;
import org.midnightbsd.appstore.repository.ArchitectureRepository;
import org.midnightbsd.appstore.repository.OperatingSystemRepository;
import org.midnightbsd.appstore.repository.PackageInstanceRepository;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    private OperatingSystemRepository operatingSystemRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageInstanceRepository packageInstanceRepository;


    /**
     * Synchronize with Magus, pull new package data
     */
    @Scheduled(fixedDelay = 1000 * 60 * 120, initialDelay = 60000)
    public void sync() {
        final List<Run> runs = getFilteredRuns();
        final HashMap<String, Run> osRunMap = new HashMap<>();

        for (final Run run : runs) {
            final String os = run.getOsVersion() + "_" + run.getArch();

            // if we have a key but its less than then the current run id, ignore it
            if (osRunMap.containsKey(os) && osRunMap.get(os).getId() > run.getId())
                continue;

            osRunMap.put(os, run);
        }

        for (final Run run : osRunMap.values()) {
            Architecture arch = architectureRepository.findOneByName(run.getArch());
            if (arch == null)
            {
                arch = new Architecture();
                arch.setName(run.getArch());
                arch = architectureRepository.saveAndFlush(arch);
            }

            OperatingSystem os = operatingSystemRepository.findByNameAndVersion("MidnightBSD", run.getOsVersion());
            if (os == null) {
                os = new OperatingSystem();
                os.setName("MidnightBSD");
                os.setVersion(run.getOsVersion());
                os = operatingSystemRepository.saveAndFlush(os);
            }

           List<Port> ports = getPorts(run.getId(), MAGUS_STATUS_PASS);
           for (Port port : ports) {
               String catAndName = port.getPort();
               String name = catAndName.substring(catAndName.indexOf('/') + 1);
               org.midnightbsd.appstore.model.Package pkg = packageRepository.findByName(name);
               if (pkg == null)
                   pkg = new org.midnightbsd.appstore.model.Package();

               pkg.setName(name);
               // TODO: other metadata
               pkg = packageRepository.saveAndFlush(pkg);

               List<PackageInstance> packageInstances = packageInstanceRepository.findByPackageAndOperatingSystemAndArchitecture(pkg, os, arch);
               if (packageInstances != null &&  ! packageInstances.isEmpty())
               {
                   // reload  TODO: update?
                  packageInstanceRepository.delete(packageInstances);
               }

               PackageInstance packageInstance = new PackageInstance();
               packageInstance.setArchitecture(arch);
               packageInstance.setOperatingSystem(os);
               packageInstance.setVersion(port.getVersion());
               packageInstance.setPkg(pkg);
               packageInstance = packageInstanceRepository.saveAndFlush(packageInstance);
           }
        }
    }

    public List<Run> getFilteredRuns() {
        List<Run> runs = getRuns().stream().filter( r -> r.getBlessed() == true && (
                        r.getStatus().equalsIgnoreCase("complete") ||
                        r.getStatus().equalsIgnoreCase("active")))
                        .sorted(Comparator.comparingInt(Run::getId)).collect(Collectors.toList());
        return runs;
    }

    public List<Run> getRuns() {
        final Run[] runs = restTemplate.getForObject(magusBaseUrl + API_RUN_PATH, Run[].class);
        return Arrays.asList(runs);
    }

    public List<Port> getPorts(final int runId, final String status) {
        final String url = magusBaseUrl + API_RUN_PORTS_PATH + "?status=" + status + "&run=" + runId;
        final Port[] ports = restTemplate.getForObject(url, Port[].class);
        return Arrays.asList(ports);
    }
}
