package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
@Profile("!test")
public class SearchIndexer {

    @Autowired
    private SearchService searchService;

    private Date lastIndex;

    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 120000)
    public void loadNewEntries() {
        log.info("Starting search indexer - Load all packages");
        if (lastIndex == null) {
            searchService.indexAllPackages();
        } else {
            searchService.indexAllPackagesSince(lastIndex);
        }
        lastIndex = Calendar.getInstance().getTime();
    }

    /**
     * Load all public blog entries at startup into elasticsearch
     */
    @PostConstruct
    public void initialize() {
        log.info("Starting search indexer - Load all packages");

        searchService.indexAllPackages();
    }

}
