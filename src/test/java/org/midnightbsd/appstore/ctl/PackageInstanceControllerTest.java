package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.LicenseController;
import org.midnightbsd.appstore.ctl.api.PackageInstanceController;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.services.LicenseService;
import org.midnightbsd.appstore.services.PackageInstanceService;
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
public class PackageInstanceControllerTest {
    @Mock
         private PackageInstanceService service;

         @InjectMocks
         private PackageInstanceController controller;

         private PackageInstance instance;

         @Before
         public void setup() {
             instance = new PackageInstance();
             instance.setId(1);
             instance.setRun(2);
             instance.setArchitecture(new Architecture());
             instance.setOperatingSystem(new OperatingSystem());
             instance.setCreated(Calendar.getInstance().getTime());

             when(service.list()).thenReturn(Collections.singletonList(instance));
             when(service.get(1)).thenReturn(instance);
         }

         @Test
         public void testList() {
             final ResponseEntity<List<PackageInstance>> result = controller.list();
             assertNotNull(result);
             assertEquals(1, result.getBody().size());
         }

         @Test
         public void testGet() {
             final ResponseEntity<PackageInstance> result = controller.get(1);
             assertNotNull(result);
             assertEquals(2, result.getBody().getRun().intValue());
             assertEquals(1, result.getBody().getId());
         }
}
