package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.services.PackageService;
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
@RequestMapping("/api/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

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
}
