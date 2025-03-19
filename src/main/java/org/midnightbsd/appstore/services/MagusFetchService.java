package org.midnightbsd.appstore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.exception.MagusFetchException;
import org.midnightbsd.appstore.model.magus.Port;
import org.midnightbsd.appstore.model.magus.Run;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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

    private final RestTemplate restTemplate;

    private static final int DELAY_ONE_MINUTE = 1000 * 60;
    private static final int ONE_DAY = DELAY_ONE_MINUTE * 60 * 24;

    private final MagusImportService magusImportService;

    public MagusFetchService(RestTemplate restTemplate, MagusImportService magusImportService) {
        this.restTemplate = restTemplate;
        this.magusImportService = magusImportService;
    }

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

        log.info("Processing " + osRunMap.size() + " runs");

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
                .sorted(Comparator.comparingInt(Run::getId)).toList();
    }

    private String getFilteredKey(final Run r) {
        return r.getOsVersion() + "_" + r.getArch();
    }


    public List<Run> getRuns() {
        var url = magusBaseUrl + API_RUN_PATH;
        log.info("Fetching Magus runs from {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String body = response.getBody();
            if (body != null && body.trim().startsWith("[")) {
                // It's likely JSON, try to parse it
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Run[] runs = mapper.readValue(body, Run[].class);
                    return Arrays.asList(runs);
                } catch (JsonProcessingException e) {
                    log.error("Error parsing JSON response", e);
                    throw new MagusFetchException("Error parsing JSON response", e);
                }
            } else {
                // It's not JSON, log the response and throw an exception
                log.error("Unexpected response format. Body: " + body);
                throw new MagusFetchException("Unexpected response format");
            }
        } else {
            log.error("Error response from server. Status: " + response.getStatusCode().value());
            throw new MagusFetchException("Error response from server");
        }
    }

    public List<Port> getPorts(final int runId, final String status) {
        log.info("Fetching Magus Ports");
        final String url = magusBaseUrl + API_RUN_PORTS_PATH + "?status=" + status + "&run=" + runId;
        final Port[] ports = restTemplate.getForObject(url, Port[].class);
        assert ports != null;
        return Arrays.asList(ports);
    }
}
