package org.midnightbsd.appstore.ctl;

import org.midnightbsd.appstore.model.Architecture;
import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.search.PackageItem;
import org.midnightbsd.appstore.services.ArchitectureService;
import org.midnightbsd.appstore.services.CategoryService;
import org.midnightbsd.appstore.services.LicenseService;
import org.midnightbsd.appstore.services.OperatingSystemService;
import org.midnightbsd.appstore.services.PackageService;
import org.midnightbsd.appstore.services.PageViewService;
import org.midnightbsd.appstore.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/")
public final class HomeController {
    private final CategoryService categoryService;
    private final OperatingSystemService operatingSystemService;
    private final ArchitectureService architectureService;
    private final LicenseService licenseService;
    private final PackageService packageService;
    private final SearchService searchService;
    private final PageViewService pageViewService;

    public HomeController(final CategoryService categoryService,
                          final OperatingSystemService operatingSystemService,
                          final ArchitectureService architectureService,
                          final LicenseService licenseService,
                          final PackageService packageService,
                          final SearchService searchService,
                          final PageViewService pageViewService) {
        this.categoryService = categoryService;
        this.operatingSystemService = operatingSystemService;
        this.architectureService = architectureService;
        this.licenseService = licenseService;
        this.packageService = packageService;
        this.searchService = searchService;
        this.pageViewService = pageViewService;
    }

    @GetMapping
    public String home(final Model model) {
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("operatingSystems", operatingSystemService.list());
        model.addAttribute("architectures", architectureService.list());
        return "index";
    }

    @GetMapping("/category/{id}")
    public String category(@PathVariable("id") final int id, final Model model) {
        final Category category = categoryService.get(id);
        if (category == null) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        final List<Package> packages = packageService.getByCategoryName(category.getName());
        model.addAttribute("category", category);
        model.addAttribute("packages", packages);
        model.addAttribute("ratings", pageViewService.loadPackageRatings(pageViewService.packageNames(packages)));
        return "category";
    }

    @GetMapping("/licenses")
    public String licenses(final Model model) {
        model.addAttribute("licenses", licenseService.list());
        return "licenses";
    }

    @GetMapping("/license/{license}")
    public String license(@PathVariable("license") final String licenseName,
                          @PageableDefault(size = 50) final Pageable pageable,
                          final Model model) {
        final License license = licenseService.getByName(licenseName);
        if (license == null) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        final Page<Package> packages = packageService.getByLicense(licenseName, pageable);
        model.addAttribute("license", license);
        model.addAttribute("packages", packages);
        model.addAttribute("ratings", pageViewService.loadPackageRatings(pageViewService.packageNames(packages.getContent())));
        return "license";
    }

    @GetMapping("/os/{os}/arch/{arch}")
    public String operatingSystem(@PathVariable("os") final String osVersion,
                                  @PathVariable("arch") final String archName,
                                  @PageableDefault(size = 50) final Pageable pageable,
                                  final Model model) {
        final Architecture architecture = architectureService.getByName(archName);
        if (architecture == null) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        final Page<Package> packages = packageService.getByOsAndArch(osVersion, archName, pageable);
        model.addAttribute("osVersion", osVersion);
        model.addAttribute("architecture", architecture);
        model.addAttribute("packages", packages);
        model.addAttribute("ratings", pageViewService.loadPackageRatings(pageViewService.packageNames(packages.getContent())));
        return "os";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "term", required = false) final String term,
                         @PageableDefault(size = 10) final Pageable pageable,
                         final Model model) {
        final String searchTerm = pageViewService.sanitizeSearchTerm(term);
        final Page<PackageItem> results;
        final boolean termTooShort = !searchTerm.isEmpty() && searchTerm.length() < 3;

        if (searchTerm.isEmpty() || termTooShort) {
            results = Page.empty(pageable);
        } else {
            results = searchService.find(searchTerm, pageable);
        }

        model.addAttribute("term", searchTerm);
        model.addAttribute("termTooShort", termTooShort);
        model.addAttribute("results", results);
        model.addAttribute("ratings", pageViewService.loadPackageRatings(pageViewService.packageNamesFromSearch(results.getContent())));
        return "search";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}