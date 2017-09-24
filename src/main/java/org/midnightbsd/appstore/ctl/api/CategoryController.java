package org.midnightbsd.appstore.ctl.api;

import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.services.CategoryService;
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
@RequestMapping("/api/category")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @GetMapping
      public ResponseEntity<List<Category>> list() {
          return ResponseEntity.ok(categoryService.list());
      }

      @GetMapping("/{id}")
      public ResponseEntity<Category> get(@PathVariable("id") int id) {
          return ResponseEntity.ok(categoryService.get(id));
      }

      @GetMapping("/name/{name}")
      public ResponseEntity<Category> get(@PathVariable("name") String name) {
          return ResponseEntity.ok(categoryService.getByName(name));
      }
}
