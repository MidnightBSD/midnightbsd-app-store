package org.midnightbsd.appstore.services;

import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "category")
@lombok.extern.slf4j.Slf4j
@Service
public class CategoryService implements AppService<Category> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable(key = "'categoryList'", unless = "#result == null")
    @Override
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> get(final Pageable page) {
        return categoryRepository.findAll(page);
    }

    @Cacheable(unless = "#result == null", key = "#id.toString()")
    @Override
    public Category get(final int id) {
        return categoryRepository.findOne(id);
    }

    @Cacheable(unless = "#result == null", key = "#name")
    public Category getByName(final String name) {
        return categoryRepository.findOneByName(name);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Category save(final Category category) {
        final Category existing = categoryRepository.findOne(category.getId());
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
        Category cat = getByName(name);
        if (cat == null) {
            cat = new Category();
            cat.setName(name);
            cat.setDescription(description);
            return save(cat);
        }
        return cat;
    }
}
