package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.services.RatingService;
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
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> list() {
        return ResponseEntity.ok(ratingService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(ratingService.get(id));
    }
}
