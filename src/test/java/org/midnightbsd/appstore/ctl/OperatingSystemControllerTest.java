package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.OperatingSystemController;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.services.OperatingSystemService;
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
class OperatingSystemControllerTest {
    @Mock
    private OperatingSystemService service;

    @InjectMocks
    private OperatingSystemController controller;

    private OperatingSystem os;

    @BeforeEach
    public void setup() {
        os = new OperatingSystem();
        os.setName("NAME");
        os.setId(1);
        os.setCreated(Calendar.getInstance().getTime());

        when(service.list()).thenReturn(Collections.singletonList(os));
        when(service.get(1)).thenReturn(os);
    }

    @Test
    void testList() {
        final ResponseEntity<List<OperatingSystem>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void testGet() {
        final ResponseEntity<OperatingSystem> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", Objects.requireNonNull(result.getBody()).getName());
        assertEquals(1, result.getBody().getId());
    }
}
