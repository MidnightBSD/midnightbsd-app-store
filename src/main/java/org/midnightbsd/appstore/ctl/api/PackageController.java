package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.model.RatingAverage;
import org.midnightbsd.appstore.services.PackageService;
import org.midnightbsd.appstore.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

/**
 * @author Lucas Holt
 */
@RestController
@RequestMapping("/api/package")
public class PackageController {

    private final PackageService packageService;

    private final RatingService ratingService;

    @Autowired
    public PackageController(final PackageService packageService, final RatingService ratingService) {
        this.packageService = packageService;
        this.ratingService = ratingService;
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

    @GetMapping("/name/{name}/rating/avg")
    public ResponseEntity<RatingAverage> getRatingAverage(@PathVariable("name") final String name) {
        return ResponseEntity.ok(ratingService.getAverage(name));
    }

    @PostMapping(value = "/name/{name}/rating", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Rating> postRating(@PathVariable("name") final String name, @RequestBody RatingAverage ratingAvg,
                                             HttpServletRequest request) {

        Rating rating = new Rating();
        rating.setScore(ratingAvg.getAverage().intValue());

        rating.setAuthor(request.getRemoteAddr()); // TODO: switch to logins.
        rating = ratingService.save(rating, name);
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + rating.getId()))
                .body(rating);
    }
}
