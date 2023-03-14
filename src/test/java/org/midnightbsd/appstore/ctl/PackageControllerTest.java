package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.OperatingSystemController;
import org.midnightbsd.appstore.ctl.api.PackageController;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.services.OperatingSystemService;
import org.midnightbsd.appstore.services.PackageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageControllerTest {
    @Mock
    private PackageService service;

    @InjectMocks
    private PackageController controller;

    private Package pkg;

    @Before
    public void setup() {
        pkg = new Package();
        pkg.setName("NAME");
        pkg.setId(1);
        pkg.setCreated(Calendar.getInstance().getTime());

        when(service.list()).thenReturn(Collections.singletonList(pkg));
        when(service.get(1)).thenReturn(pkg);
    }

    @Test
    public void testList() {
        final ResponseEntity<List<Package>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGet() {
        final ResponseEntity<Package> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }
}
