package org.midnightbsd.appstore.services;

import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Transactional(readOnly = true)
@lombok.extern.slf4j.Slf4j
@Service
public class CategoryService implements AppService<Category> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> list() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<Category> get(final Pageable page) {
        return categoryRepository.findAll(page);
    }

    @Override
    public Category get(final int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category getByName(final String name) {
        return categoryRepository.findOneByName(name);
    }

    @Transactional
    public Category save(final Category category) {
        final Category existing = categoryRepository.findById(category.getId()).orElse(null);
        if (existing == null) {
            log.debug("Created new category " + category.getName());
            
            // create new one
            return categoryRepository.saveAndFlush(category);
        }

        if (!category.getDescription().equals(existing.getDescription()) ||
                !category.getName().equals(existing.getName())) {
            existing.setDescription(category.getDescription());
            existing.setName(category.getName());
            return categoryRepository.saveAndFlush(existing);
        }
        return existing;
    }

    public Category createIfNotExists(final String name, final String description) {
        Category cat = categoryRepository.findOneByName(name);
        if (cat == null) {
            cat = new Category();
            cat.setName(name);
            cat.setDescription(description);
            return save(cat);
        }
        return cat;
    }
}
