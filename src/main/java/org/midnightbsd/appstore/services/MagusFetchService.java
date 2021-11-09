package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.model.magus.Port;
import org.midnightbsd.appstore.model.magus.Run;
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
public class MagusFetchService {

    private static final String API_RUN_PATH = "api/runs";
    private static final String API_RUN_PORTS_PATH = "api/run-ports-list";
    private static final String MAGUS_STATUS_PASS = "pass";

    @Value("${magus.baseUrl}")
    private String magusBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final int DELAY_ONE_MINUTE = 1000 * 60;
    private static final int ONE_DAY = DELAY_ONE_MINUTE * 60 * 24;

    @Autowired
    private MagusImportService magusImportService;

    /**
     * Synchronize with Magus, pull new package data
     */
    @Scheduled(fixedDelay = ONE_DAY, initialDelay = DELAY_ONE_MINUTE)
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

        log.info("Processing " + osRunMap.values().size() + " runs");

        for (final Run run : osRunMap.values()) {
            final List<Port> ports = getPorts(run.getId(), MAGUS_STATUS_PASS);
            log.info("Processing " + ports.size() + " ports for run " + run.getId());
            magusImportService.importRun(run, ports);
        }
    }

    public List<Run> getFilteredRuns() {
        log.info("Filtering runs to include only complete and active");
        return getRuns().stream().filter(r -> r.getBlessed() && (
                r.getStatus().equalsIgnoreCase("complete") ||
                        r.getStatus().equalsIgnoreCase("active")))
                .sorted(Comparator.comparingInt(Run::getId)).collect(Collectors.toList());
    }

    private String getFilteredKey(final Run r) {
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
