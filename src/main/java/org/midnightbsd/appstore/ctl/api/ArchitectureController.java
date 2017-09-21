package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.services.ArchitectureService;
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
@RequestMapping("/api/architecture")
public class ArchitectureController {

    @Autowired
    private ArchitectureService architectureService;

    @GetMapping
    public ResponseEntity<List<Architecture>> list() {
        return ResponseEntity.ok(architectureService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Architecture> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(architectureService.get(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Architecture> get(@PathVariable("name") String name) {
        return ResponseEntity.ok(architectureService.getByName(name));
    }
}
