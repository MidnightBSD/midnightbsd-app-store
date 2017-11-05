package org.midnightbsd.appstore.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.repository.CategoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Before
    public void setup() {
        Category cat = new Category();
        cat.setId(1);
        cat.setName("test");
        cat.setDescription("Foo");
        cat.setCreated(Calendar.getInstance().getTime());

        when(categoryRepository.findOneByName("test")).thenReturn(cat);
        when(categoryRepository.findOne(1)).thenReturn(cat);
    }

    @Test
    public void testGetName() {
        Category cat = categoryService.getByName("test");
        assertNotNull(cat);
        assertEquals(1, cat.getId());
        assertEquals("test", cat.getName());
        assertEquals("Foo", cat.getDescription());

        verify(categoryRepository, times(1)).findOneByName(anyString());
    }

    @Test
    public void testGet() {
        Category cat = categoryService.get(1);
        assertNotNull(cat);
        assertEquals(1, cat.getId());
        assertEquals("test", cat.getName());
        assertEquals("Foo", cat.getDescription());

        verify(categoryRepository, times(1)).findOne(1);
    }
}
