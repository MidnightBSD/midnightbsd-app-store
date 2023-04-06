package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.LicenseController;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.services.LicenseService;
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
class LicenseControllerTest {
    @Mock
        private LicenseService service;

        @InjectMocks
        private LicenseController controller;

        private License license;

        @BeforeEach
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
        void testList() {
            final ResponseEntity<List<License>> result = controller.list();
            assertNotNull(result);
            assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        }

        @Test
        void testGet() {
            final ResponseEntity<License> result = controller.get(1);
            assertNotNull(result);
            assertEquals("NAME", result.getBody().getName());
            assertEquals(1, result.getBody().getId());
        }

        @Test
        void testGetByName() {
            final ResponseEntity<License> result = controller.get("NAME");
            assertNotNull(result);
            assertEquals("NAME", result.getBody().getName());
            assertEquals(1, result.getBody().getId());
        }
}
