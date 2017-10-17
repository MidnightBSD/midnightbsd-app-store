package org.midnightbsd.appstore.repository.search;

import org.midnightbsd.appstore.model.search.PackageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Lucas Holt
 */
public interface PackageSearchRepository extends ElasticsearchRepository<PackageItem, Integer> {

    Page<PackageItem> findByNameContainsOrDescriptionContainsAllIgnoreCase(String name, String description, Pageable page);
}
