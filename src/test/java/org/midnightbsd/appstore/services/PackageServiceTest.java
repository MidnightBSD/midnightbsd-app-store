package org.midnightbsd.appstore.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.repository.PackageRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageServiceTest {
    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    @Before
    public void setup() {
        Package obj = new Package();
        obj.setId(1);
        obj.setName("test");
        obj.setDescription("Foo");
        obj.setCreated(Calendar.getInstance().getTime());

        when(packageRepository.findOneByName("test")).thenReturn(obj);
        when(packageRepository.findById(1)).thenReturn(Optional.of(obj));
    }

    @Test
    public void testGetName() {
        Package obj = packageService.getByName("test");
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(packageRepository, times(1)).findOneByName(anyString());
    }

    @Test
    public void testGet() {
        Package obj = packageService.get(1);
        assertNotNull(obj);
        assertEquals(1, obj.getId());
        assertEquals("test", obj.getName());
        assertEquals("Foo", obj.getDescription());

        verify(packageRepository, times(1)).findById(1);
    }
}
