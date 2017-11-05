package org.midnightbsd.appstore.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.repository.ArchitectureRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class ArchitectureServiceTest {

    @Mock
    private ArchitectureRepository architectureRepository;

    @InjectMocks
    private ArchitectureService architectureService;

    @Before
    public void setup() {
        Architecture architecture = new Architecture();
        architecture.setId(1);
        architecture.setName("test");
        architecture.setDescription("Foo");
        architecture.setCreated(Calendar.getInstance().getTime());

        when(architectureRepository.findOneByName("test")).thenReturn(architecture);
        when(architectureRepository.findOne(1)).thenReturn(architecture);
    }

    @Test
    public void testGetName() {
        Architecture arch = architectureService.getByName("test");
        assertNotNull(arch);
        assertEquals(1, arch.getId());
        assertEquals("test", arch.getName());
        assertEquals("Foo", arch.getDescription());

        verify(architectureRepository, times(1)).findOneByName(anyString());
    }

    @Test
    public void testGet() {
        Architecture arch = architectureService.get(1);
        assertNotNull(arch);
        assertEquals(1, arch.getId());
        assertEquals("test", arch.getName());
        assertEquals("Foo", arch.getDescription());

        verify(architectureRepository, times(1)).findOne(1);
    }
}
