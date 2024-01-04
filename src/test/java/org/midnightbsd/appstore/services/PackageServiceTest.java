package org.midnightbsd.appstore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.repository.PackageRepository;
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
class PackageServiceTest {
    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    Package obj;

    @BeforeEach
    public void setup() {
        obj = new Package();
        obj.setId(1);
        obj.setName("test");
        obj.setDescription("Foo");
        obj.setCreated(Calendar.getInstance().getTime());
    }

    @Test
    void testGetName() {
        when(packageRepository.findOneByName("test")).thenReturn(obj);
        Package obj = packageService.getByName("test");
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(packageRepository, times(1)).findOneByName(anyString());
    }

    @Test
    void testGet() {
        when(packageRepository.findById(1)).thenReturn(Optional.of(obj));
        Package obj = packageService.get(1);
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(packageRepository, times(1)).findById(1);
    }
}
