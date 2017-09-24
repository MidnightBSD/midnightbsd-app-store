package org.midnightbsd.appstore.services;

import groovy.util.logging.Slf4j;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class CategoryService implements AppService<Category> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> get(final Pageable page) {
        return categoryRepository.findAll(page);
    }

    @Override
    public Category get(final int id) {
        return categoryRepository.findOne(id);
    }

    public Category getByName(final String name) {
        return categoryRepository.findOneByName(name);
    }
}
