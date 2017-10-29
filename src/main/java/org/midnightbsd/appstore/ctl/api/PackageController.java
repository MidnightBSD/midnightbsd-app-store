package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.services.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/package")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(final PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping
    public ResponseEntity<List<Package>> list() {
        return ResponseEntity.ok(packageService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(packageService.get(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Package> get(@PathVariable("name") String name) {
        return ResponseEntity.ok(packageService.getByName(name));
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<Package>> getByCategoryName(@PathVariable("name") String name) {
        return ResponseEntity.ok(packageService.getByCategoryName(name));
    }

    @GetMapping("/os/{os}/arch/{arch}")
    public ResponseEntity<Page<Package>> getByOsAndArch(@PathVariable("os") String os, @PathVariable("arch") String arch, Pageable page) {
        return ResponseEntity.ok(packageService.getByOsAndArch(os, arch, page));
    }

    @GetMapping("/license/{license}")
    public ResponseEntity<Page<Package>> getByOsAndArch(@PathVariable("license") String license, Pageable page) {
        return ResponseEntity.ok(packageService.getByLicense(license, page));
    }
}
