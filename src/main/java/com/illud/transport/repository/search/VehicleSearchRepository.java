package com.illud.transport.repository.search;

import com.illud.transport.domain.Vehicle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vehicle entity.
 */
public interface VehicleSearchRepository extends ElasticsearchRepository<Vehicle, Long> {
}
