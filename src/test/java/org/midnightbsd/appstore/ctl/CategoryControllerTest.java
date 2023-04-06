package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.CategoryController;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.services.CategoryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController controller;

    private Category category;

    @Before
    public void setup() {
        category = new Category();
        category.setDescription("TEST Cat");
        category.setName("NAME");
        category.setId(1);
        category.setCreated(Calendar.getInstance().getTime());

        when(categoryService.list()).thenReturn(Collections.singletonList(category));
        when(categoryService.get(1)).thenReturn(category);
        when(categoryService.getByName("NAME")).thenReturn(category);
    }

    @Test
    public void testList() {
        final ResponseEntity<List<Category>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGet() {
        final ResponseEntity<Category> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    public void testGetByName() {
        final ResponseEntity<Category> result = controller.get("NAME");
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }
}
