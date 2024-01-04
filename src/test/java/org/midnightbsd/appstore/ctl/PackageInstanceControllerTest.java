package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.PackageInstanceController;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.services.PackageInstanceService;
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
class PackageInstanceControllerTest {
    @Mock
         private PackageInstanceService service;

         @InjectMocks
         private PackageInstanceController controller;

         private PackageInstance instance;

         @BeforeEach
         public void setup() {
             instance = new PackageInstance();
             instance.setId(1);
             instance.setRun(2);
             instance.setArchitecture(new Architecture());
             instance.setOperatingSystem(new OperatingSystem());
             instance.setCreated(Calendar.getInstance().getTime());
         }

         @Test
         void testList() {
             when(service.list()).thenReturn(Collections.singletonList(instance));
             final ResponseEntity<List<PackageInstance>> result = controller.list();
             assertNotNull(result);
             assertEquals(1, Objects.requireNonNull(result.getBody()).size());
         }

         @Test
         void testGet() {
             when(service.get(1)).thenReturn(instance);
             final ResponseEntity<PackageInstance> result = controller.get(1);
             assertNotNull(result);
             assertEquals(2, Objects.requireNonNull(result.getBody()).getRun().intValue());
             assertEquals(1, result.getBody().getId());
         }
}
