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
import java.util.Objects;

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
    }

    @Test
    void testList() {
        when(architectureService.list()).thenReturn(Collections.singletonList(arch));
        final ResponseEntity<List<Architecture>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void testGet() {
        when(architectureService.get(1)).thenReturn(arch);
        final ResponseEntity<Architecture> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", Objects.requireNonNull(result.getBody()).getName());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    void testGetByName() {
        when(architectureService.getByName("NAME")).thenReturn(arch);
        final ResponseEntity<Architecture> result = controller.get("NAME");
        assertNotNull(result);
        assertEquals("NAME", Objects.requireNonNull(result.getBody()).getName());
        assertEquals(1, result.getBody().getId());
    }
}
