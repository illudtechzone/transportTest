package com.illud.transport.repository.search;

import com.illud.transport.domain.Rider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rider entity.
 */
public interface RiderSearchRepository extends ElasticsearchRepository<Rider, Long> {
}
