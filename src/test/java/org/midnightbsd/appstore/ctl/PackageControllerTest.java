package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.PackageController;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.services.PackageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class PackageControllerTest {
    @Mock
    private PackageService service;

    @InjectMocks
    private PackageController controller;

    private Package pkg;

    @BeforeEach
    public void setup() {
        pkg = new Package();
        pkg.setName("NAME");
        pkg.setId(1);
        pkg.setCreated(Calendar.getInstance().getTime());

        when(service.list()).thenReturn(Collections.singletonList(pkg));
        when(service.get(1)).thenReturn(pkg);
    }

    @Test
    void testList() {
        final ResponseEntity<List<Package>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void testGet() {
        final ResponseEntity<Package> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", Objects.requireNonNull(result.getBody()).getName());
        assertEquals(1, result.getBody().getId());
    }
}
