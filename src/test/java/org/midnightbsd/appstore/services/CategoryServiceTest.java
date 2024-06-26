package org.midnightbsd.appstore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.repository.CategoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    Category cat;

    @BeforeEach
    public void setup() {
        cat = new Category();
        cat.setId(1);
        cat.setName("test");
        cat.setDescription("Foo");
        cat.setCreated(Calendar.getInstance().getTime());
    }

    @Test
    void testGetName() {
        when(categoryRepository.findOneByName("test")).thenReturn(cat);
        Category c = categoryService.getByName("test");
        assertNotNull(cat);
        assertEquals(1, c.getId());
        assertEquals("test", c.getName());
        assertEquals("Foo", c.getDescription());

        verify(categoryRepository, times(1)).findOneByName(anyString());
    }

    @Test
    void testGet() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(cat));
        Category c = categoryService.get(1);
        assertNotNull(cat);
        assertEquals(1, c.getId());
        assertEquals("test", c.getName());
        assertEquals("Foo", c.getDescription());

        verify(categoryRepository, times(1)).findById(1);
    }
}
