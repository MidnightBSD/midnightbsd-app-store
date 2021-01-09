package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.CategoryController;
import org.midnightbsd.appstore.ctl.api.LicenseController;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.services.CategoryService;
import org.midnightbsd.appstore.services.LicenseService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
public class LicenseControllerTest {
    @Mock
        private LicenseService service;

        @InjectMocks
        private LicenseController controller;

        private License license;

        @Before
        public void setup() {
            license = new License();
            license.setDescription("TEST license");
            license.setName("NAME");
            license.setId(1);
            license.setCreated(Calendar.getInstance().getTime());

            when(service.list()).thenReturn(Collections.singletonList(license));
            when(service.get(1)).thenReturn(license);
            when(service.getByName("NAME")).thenReturn(license);
        }

        @Test
        public void testList() {
            final ResponseEntity<List<License>> result = controller.list();
            assertNotNull(result);
            assertEquals(1, result.getBody().size());
        }

        @Test
        public void testGet() {
            final ResponseEntity<License> result = controller.get(1);
            assertNotNull(result);
            assertEquals("NAME", result.getBody().getName());
            assertEquals(1, result.getBody().getId());
        }

        @Test
        public void testGetByName() {
            final ResponseEntity<License> result = controller.get("NAME");
            assertNotNull(result);
            assertEquals("NAME", result.getBody().getName());
            assertEquals(1, result.getBody().getId());
        }
}
