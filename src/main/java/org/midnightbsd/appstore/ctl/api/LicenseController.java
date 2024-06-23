package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lucas Holt
 */
@RestController
@RequestMapping("/api/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping
    public ResponseEntity<List<License>> list() {
        return ResponseEntity.ok(licenseService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<License> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(licenseService.get(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<License> get(@PathVariable("name") String name) {
        return ResponseEntity.ok(licenseService.getByName(name));
    }
}
