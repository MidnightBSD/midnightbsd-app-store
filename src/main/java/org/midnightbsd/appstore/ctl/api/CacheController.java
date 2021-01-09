package org.midnightbsd.appstore.ctl.api;

import lombok.extern.slf4j.Slf4j;
import org.midnightbsd.appstore.services.RedisCacheService;
import org.midnightbsd.appstore.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Manage cache entries
 * @author Lucas Holt
 */
@Slf4j
@RequestMapping("/api/cache")
@RestController
public class CacheController {

    private final RedisCacheService cacheService;

    @Autowired
    public CacheController(RedisCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> list() throws ServiceException {
        return new ResponseEntity<>(cacheService.list(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void delete() throws ServiceException {
        cacheService.deleteAllFromCurrentDb();
    }
}

