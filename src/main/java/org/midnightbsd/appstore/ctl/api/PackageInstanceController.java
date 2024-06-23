package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.services.PackageInstanceService;
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
@RequestMapping("/api/package-instance")
public class PackageInstanceController {

    private final PackageInstanceService packageInstanceService;

    public PackageInstanceController(PackageInstanceService packageInstanceService) {
        this.packageInstanceService = packageInstanceService;
    }

    @GetMapping
    public ResponseEntity<List<PackageInstance>> list() {
        return ResponseEntity.ok(packageInstanceService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageInstance> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(packageInstanceService.get(id));
    }
}
