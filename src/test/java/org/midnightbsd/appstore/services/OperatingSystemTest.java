package org.midnightbsd.appstore.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.model.OperatingSystem;
import org.midnightbsd.appstore.repository.OperatingSystemRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class OperatingSystemTest {
    @Mock
    private OperatingSystemRepository operatingSystemRepository;

    @InjectMocks
    private OperatingSystemService operatingSystemService;

    @Before
    public void setup() {
        OperatingSystem obj = new OperatingSystem();
        obj.setId(1);
        obj.setName("test");
        obj.setVersion("1.0");
        obj.setCreated(Calendar.getInstance().getTime());

        when(operatingSystemRepository.findByNameAndVersion("test", "1.0")).thenReturn(obj);
        when(operatingSystemRepository.findById(1)).thenReturn(Optional.of(obj));
    }

    @Test
    public void testGetNameAndVersion() {
        OperatingSystem obj = operatingSystemService.getByNameAndVersion("test", "1.0");
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("1.0", obj.getVersion());

        verify(operatingSystemRepository, times(1)).findByNameAndVersion(anyString(), anyString());
    }

    @Test
    public void testGet() {
        OperatingSystem obj = operatingSystemService.get(1);
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("1.0", obj.getVersion());

        verify(operatingSystemRepository, times(1)).findById(1);
    }
}
