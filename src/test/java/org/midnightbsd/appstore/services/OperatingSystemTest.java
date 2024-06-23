package org.midnightbsd.appstore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.repository.OperatingSystemRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class OperatingSystemTest {
    @Mock
    private OperatingSystemRepository operatingSystemRepository;

    @InjectMocks
    private OperatingSystemService operatingSystemService;

    OperatingSystem obj;

    @BeforeEach
    public void setup() {
        obj = new OperatingSystem();
        obj.setId(1);
        obj.setName("test");
        obj.setVersion("1.0");
        obj.setCreated(Calendar.getInstance().getTime());
    }

    @Test
    void testGetNameAndVersion() {
        when(operatingSystemRepository.findByNameAndVersion("test", "1.0")).thenReturn(obj);
        OperatingSystem operatingSystem = operatingSystemService.getByNameAndVersion("test", "1.0");
        assertNotNull(operatingSystem);
        assertEquals(1, operatingSystem.getId());
        assertEquals("test", operatingSystem.getName());
        assertEquals("1.0", operatingSystem.getVersion());

        verify(operatingSystemRepository, times(1)).findByNameAndVersion(anyString(), anyString());
    }

    @Test
    void testGet() {
        when(operatingSystemRepository.findById(1)).thenReturn(Optional.of(obj));
        OperatingSystem operatingSystem = operatingSystemService.get(1);
        assertNotNull(operatingSystem);
        assertEquals(1, operatingSystem.getId());
        assertEquals("test", operatingSystem.getName());
        assertEquals("1.0", operatingSystem.getVersion());

        verify(operatingSystemRepository, times(1)).findById(1);
    }
}
