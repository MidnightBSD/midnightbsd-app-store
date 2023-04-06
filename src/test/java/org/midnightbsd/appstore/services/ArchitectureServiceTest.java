package org.midnightbsd.appstore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.repository.ArchitectureRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class ArchitectureServiceTest {

    @Mock
    private ArchitectureRepository architectureRepository;

    @InjectMocks
    private ArchitectureService architectureService;

    @BeforeEach
    public void setup() {
        Architecture architecture = new Architecture();
        architecture.setId(1);
        architecture.setName("test");
        architecture.setDescription("Foo");
        architecture.setCreated(Calendar.getInstance().getTime());

        when(architectureRepository.findOneByName("test")).thenReturn(architecture);
        when(architectureRepository.findById(1)).thenReturn(Optional.of(architecture));
        when(architectureRepository.findAll()).thenReturn(Collections.singletonList(architecture));
    }

    @Test
    void testGetName() {
        Architecture arch = architectureService.getByName("test");
        assertNotNull(arch);
        assertEquals(1, arch.getId());
        assertEquals("test", arch.getName());
        assertEquals("Foo", arch.getDescription());

        verify(architectureRepository, times(1)).findOneByName(anyString());
    }

    @Test
    void testGet() {
        Architecture arch = architectureService.get(1);
        assertNotNull(arch);
        assertEquals(1, arch.getId());
        assertEquals("test", arch.getName());
        assertEquals("Foo", arch.getDescription());

        verify(architectureRepository, times(1)).findById(1);
    }

    @Test
    void testList() {
        List<Architecture> items = architectureService.list();
        assertNotNull(items);
        assertTrue(items.size() > 0);
        verify(architectureRepository, times(1)).findAll();
    }
}
