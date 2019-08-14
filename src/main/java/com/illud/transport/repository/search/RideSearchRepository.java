package com.illud.transport.repository.search;

import com.illud.transport.domain.Ride;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ride entity.
 */
public interface RideSearchRepository extends ElasticsearchRepository<Ride, Long> {
}
