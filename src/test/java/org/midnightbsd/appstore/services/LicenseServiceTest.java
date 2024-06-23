package org.midnightbsd.appstore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.repository.LicenseRepository;
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
class LicenseServiceTest {
    @Mock
    private LicenseRepository licenseRepository;

    @InjectMocks
    private LicenseService licenseService;

    License obj;

    @BeforeEach
    public void setup() {
        obj = new License();
        obj.setId(1);
        obj.setName("test");
        obj.setDescription("Foo");
        obj.setCreated(Calendar.getInstance().getTime());
    }

    @Test
    void testGetName() {
        when(licenseRepository.findOneByName("test")).thenReturn(obj);
        License lic = licenseService.getByName("test");
        assertNotNull(lic);
        assertEquals(1, lic.getId());
        assertEquals("test", lic.getName());
        assertEquals("Foo", lic.getDescription());

        verify(licenseRepository, times(1)).findOneByName(anyString());
    }

    @Test
    void testGet() {
        when(licenseRepository.findById(1)).thenReturn(Optional.of(obj));
        License lic = licenseService.get(1);
        assertNotNull(obj);
        assertEquals(1, lic.getId());
        assertEquals("test", lic.getName());
        assertEquals("Foo", lic.getDescription());

        verify(licenseRepository, times(1)).findById(1);
    }
}
