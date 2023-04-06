package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.ArchitectureController;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.services.ArchitectureService;
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
 * Test architecture controller
 *
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class ArchitectureControllerTest {

    @Mock
    private ArchitectureService architectureService;

    @InjectMocks
    private ArchitectureController controller;

    private Architecture arch;

    @BeforeEach
    public void setup() {
        arch = new Architecture();
        arch.setDescription("TEST ARCH");
        arch.setName("NAME");
        arch.setId(1);
        arch.setCreated(Calendar.getInstance().getTime());

        when(architectureService.list()).thenReturn(Collections.singletonList(arch));
        when(architectureService.get(1)).thenReturn(arch);
        when(architectureService.getByName("NAME")).thenReturn(arch);
    }

    @Test
    void testList() {
        final ResponseEntity<List<Architecture>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testGet() {
        final ResponseEntity<Architecture> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    void testGetByName() {
        final ResponseEntity<Architecture> result = controller.get("NAME");
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }
}
