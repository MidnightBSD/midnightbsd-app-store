package org.midnightbsd.appstore.services;

import org.midnightbsd.appstore.model.Category;
import org.midnightbsd.appstore.model.License;
import org.midnightbsd.appstore.model.PackageInstance;
import org.midnightbsd.appstore.model.RatingAverage;
import org.midnightbsd.appstore.model.search.Instance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PageViewService {
    private final RatingService ratingService;

    public PageViewService(final RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public Map<String, Integer> loadPackageRatings(final Collection<String> packageNames) {
        final LinkedHashMap<String, Integer> ratings = new LinkedHashMap<>();
        if (packageNames == null) {
            return ratings;
        }

        for (final String packageName : packageNames) {
            if (packageName == null || ratings.containsKey(packageName)) {
                continue;
            }
            ratings.put(packageName, normalizeRating(ratingService.getAverage(packageName)));
        }

        return ratings;
    }

    public int normalizeRating(final RatingAverage ratingAverage) {
        if (ratingAverage == null || ratingAverage.getAverage() == null) {
            return 0;
        }

        final BigDecimal average = new BigDecimal(ratingAverage.getAverage().toString());
        return average.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public int ratingPercent(final Integer rating) {
        return Math.max(0, Math.min(5, rating == null ? 0 : rating)) * 20;
    }

    public String ratingClass(final Integer rating) {
        final int percent = ratingPercent(rating);
        if (percent < 30) {
            return "text-bg-warning";
        }
        if (percent < 70) {
            return "text-bg-info";
        }
        return "text-bg-success";
    }

    public List<PackageInstance> sortedInstances(final Set<PackageInstance> instances) {
        if (instances == null) {
            return Collections.emptyList();
        }

        return instances.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparing((PackageInstance instance) -> instance.getOperatingSystem() == null ? "" : instance.getOperatingSystem().getVersion())
                        .thenComparing(instance -> instance.getArchitecture() == null ? "" : instance.getArchitecture().getName())
                        .thenComparing(instance -> instance.getVersion() == null ? "" : instance.getVersion()))
                .toList();
    }

    public List<Instance> sortedSearchInstances(final List<Instance> instances) {
        if (instances == null) {
            return Collections.emptyList();
        }

        return instances.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparing((Instance instance) -> instance.getOsVersion() == null ? "" : instance.getOsVersion())
                        .thenComparing(instance -> instance.getArchitecture() == null ? "" : instance.getArchitecture())
                        .thenComparing(instance -> instance.getVersion() == null ? "" : instance.getVersion()))
                .toList();
    }

    public List<Category> sortedCategories(final Set<Category> categories) {
        if (categories == null) {
            return Collections.emptyList();
        }

        return categories.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(category -> category.getName() == null ? "" : category.getName()))
                .toList();
    }

    public List<License> sortedLicenses(final Set<License> licenses) {
        if (licenses == null) {
            return Collections.emptyList();
        }

        return licenses.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(license -> license.getName() == null ? "" : license.getName()))
                .toList();
    }

    public String sanitizeSearchTerm(final String term) {
        if (term == null) {
            return "";
        }
        return term.trim();
    }

    public List<String> packageNames(final List<org.midnightbsd.appstore.model.Package> packages) {
        if (packages == null) {
            return Collections.emptyList();
        }

        return packages.stream()
                .map(org.midnightbsd.appstore.model.Package::getName)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> packageNamesFromSearch(final List<org.midnightbsd.appstore.model.search.PackageItem> packages) {
        if (packages == null) {
            return Collections.emptyList();
        }

        return packages.stream()
                .map(org.midnightbsd.appstore.model.search.PackageItem::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
