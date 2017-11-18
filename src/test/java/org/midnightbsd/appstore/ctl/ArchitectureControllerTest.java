package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.ArchitectureController;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.services.ArchitectureService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test architecture controller
 *
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class ArchitectureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArchitectureService architectureService;

    @InjectMocks
    private ArchitectureController controller;

    private Architecture arch;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        arch = new Architecture();
        arch.setDescription("TEST ARCH");
        arch.setName("NAME");
        arch.setId(1);
        arch.setCreated(Calendar.getInstance().getTime());

        when(architectureService.list()).thenReturn(Collections.singletonList(arch));
        when(architectureService.get(1)).thenReturn(arch);
    }

    @Test
    public void testList() {
        final ResponseEntity<List<Architecture>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGet() {
        final ResponseEntity<Architecture> result = controller.get(1);
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    public void testGetByName() {
        final ResponseEntity<Architecture> result = controller.get("NAME");
        assertNotNull(result);
        assertEquals("NAME", result.getBody().getName());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    public void mvcTestList() throws Exception {
        mockMvc.perform(get("/api/architecture"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
    }

    @Test
    public void mvcTestGet() throws Exception {
        mockMvc.perform(get("/api/architecture/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
    }

    @Test
    public void mvcTestGetByName() throws Exception {
        mockMvc.perform(get("/api/architecture/NAME"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
    }
}
