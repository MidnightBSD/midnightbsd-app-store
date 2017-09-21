package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.services.OperatingSystemService;
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
@RequestMapping("/api/os")
public class OperatingSystemController {

    @Autowired
    private OperatingSystemService operatingSystemService;

    @GetMapping
    public ResponseEntity<List<OperatingSystem>> list() {
        return ResponseEntity.ok(operatingSystemService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperatingSystem> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(operatingSystemService.get(id));
    }

    @GetMapping("/name/{name}/version/{version}")
    public ResponseEntity<OperatingSystem> get(@PathVariable("name") String name, @PathVariable("version") String version) {
        return ResponseEntity.ok(operatingSystemService.getByNameAndVersion(name, version));
    }
}
