package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.SearchController;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.services.SearchService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class SearchControllerTest {
    @Mock
    private SearchService service;

    @InjectMocks
    private SearchController controller;

    private PackageItem pkg;

    @BeforeEach
    public void setup() {
        pkg = new PackageItem();
        pkg.setDescription("TEST license");
        pkg.setName("NAME");
        pkg.setId(1);

        final Page<PackageItem> page = new PageImpl<>(Collections.singletonList(pkg));
        when(service.find(anyString(), any())).thenReturn(page);
    }

    @Test
    void testFind() {
        final Page<PackageItem> result = controller.find("test", PageRequest.of(1, 1));
        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
    }
}
