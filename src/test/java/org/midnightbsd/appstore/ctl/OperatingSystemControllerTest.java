package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.OperatingSystemController;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.services.OperatingSystemService;
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
public class OperatingSystemControllerTest {
    @Mock
    private OperatingSystemService service;

    @InjectMocks
    private OperatingSystemController controller;

    private OperatingSystem os;

    @Before
    public void setup() {
        os = new OperatingSystem();
        os.setName("NAME");
        os.setId(1);
        os.setCreated(Calendar.getInstance().getTime());

        when(service.list()).thenReturn(Collections.singletonList(os));
        when(service.get(1)).thenReturn(os);
    }

    @Test
    public void testList() {
        final ResponseEntity<List<OperatingSystem>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGet() {
        final ResponseEntity<OperatingSystem> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }
}
