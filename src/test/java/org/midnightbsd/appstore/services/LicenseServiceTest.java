package org.midnightbsd.appstore.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.repository.LicenseRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
public class LicenseServiceTest {
    @Mock
    private LicenseRepository licenseRepository;

    @InjectMocks
    private LicenseService licenseService;

    @Before
    public void setup() {
        License obj = new License();
        obj.setId(1);
        obj.setName("test");
        obj.setDescription("Foo");
        obj.setCreated(Calendar.getInstance().getTime());

        when(licenseRepository.findOneByName("test")).thenReturn(obj);
        when(licenseRepository.findById(1)).thenReturn(Optional.of(obj));
    }

    @Test
    public void testGetName() {
        License obj = licenseService.getByName("test");
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(licenseRepository, times(1)).findOneByName(anyString());
    }

    @Test
    public void testGet() {
        License obj = licenseService.get(1);
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(licenseRepository, times(1)).findById(1);
    }
}
