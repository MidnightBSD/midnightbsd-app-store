package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.LicenseController;
import org.midnightbsd.appstore.ctl.api.SearchController;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.services.LicenseService;
import org.midnightbsd.appstore.services.SearchService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    @Mock
    private SearchService service;

    @InjectMocks
    private SearchController controller;

    private PackageItem pkg;

    @Before
    public void setup() {
        pkg = new PackageItem();
        pkg.setDescription("TEST license");
        pkg.setName("NAME");
        pkg.setId(1);

        final Page<PackageItem> page = new PageImpl<>(Collections.singletonList(pkg));
        when(service.find(anyString(), any())).thenReturn(page);
    }

    @Test
    public void testFind() {
        final Page<PackageItem> result = controller.find("test", PageRequest.of(1, 1));
        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
    }
}
