package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.services.ArchitectureService;
import org.midnightbsd.appstore.services.CategoryService;
import org.midnightbsd.appstore.services.LicenseService;
import org.midnightbsd.appstore.services.OperatingSystemService;
import org.midnightbsd.appstore.services.PackageService;
import org.midnightbsd.appstore.services.PageViewService;
import org.midnightbsd.appstore.services.SearchService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ExtendedModelMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private OperatingSystemService operatingSystemService;

    @Mock
    private ArchitectureService architectureService;

    @Mock
    private LicenseService licenseService;

    @Mock
    private PackageService packageService;

    @Mock
    private SearchService searchService;

    @Mock
    private PageViewService pageViewService;

    private HomeController controller;

    @BeforeEach
    void setUp() {
        controller = new HomeController(
                categoryService,
                operatingSystemService,
                architectureService,
                licenseService,
                packageService,
                searchService,
                pageViewService
        );
    }

    @Test
    void homeLoadsListsIntoModel() {
        when(categoryService.list()).thenReturn(Collections.singletonList(new Category()));
        when(operatingSystemService.list()).thenReturn(Collections.emptyList());
        when(architectureService.list()).thenReturn(Collections.singletonList(new Architecture()));

        final ExtendedModelMap model = new ExtendedModelMap();
        final String view = controller.home(model);

        assertEquals("index", view);
        assertTrue(model.containsAttribute("categories"));
        assertTrue(model.containsAttribute("operatingSystems"));
        assertTrue(model.containsAttribute("architectures"));
        assertFalse(model.containsAttribute("licenses"));
    }

    @Test
    void licensesLoadsListsIntoModel() {
        when(licenseService.list()).thenReturn(Collections.singletonList(new License()));

        final ExtendedModelMap model = new ExtendedModelMap();
        final String view = controller.licenses(model);

        assertEquals("licenses", view);
        assertTrue(model.containsAttribute("licenses"));
    }

    @Test
    void searchSkipsElasticWhenTermTooShort() {
        when(pageViewService.sanitizeSearchTerm("ab")).thenReturn("ab");
        final ExtendedModelMap model = new ExtendedModelMap();
        final String view = controller.search("ab", PageRequest.of(0, 10), model);

        assertEquals("search", view);
        assertTrue((Boolean) model.getAttribute("termTooShort"));
        assertEquals(0L, ((Page<?>) model.getAttribute("results")).getTotalElements());
    }

    @Test
    void searchLoadsResultsAndRatings() {
        final PackageItem item = new PackageItem();
        item.setName("test-pkg");

        when(pageViewService.sanitizeSearchTerm("test")).thenReturn("test");
        when(searchService.find("test", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(item), PageRequest.of(0, 10), 1));
        when(pageViewService.packageNamesFromSearch(List.of(item))).thenReturn(List.of("test-pkg"));
        when(pageViewService.loadPackageRatings(List.of("test-pkg"))).thenReturn(Map.of("test-pkg", 4));

        final ExtendedModelMap model = new ExtendedModelMap();
        final String view = controller.search("test", PageRequest.of(0, 10), model);

        assertEquals("search", view);
        assertFalse((Boolean) model.getAttribute("termTooShort"));
        assertEquals(1L, ((Page<?>) model.getAttribute("results")).getTotalElements());
        assertEquals(4, ((Map<?, ?>) model.getAttribute("ratings")).get("test-pkg"));
    }
}
