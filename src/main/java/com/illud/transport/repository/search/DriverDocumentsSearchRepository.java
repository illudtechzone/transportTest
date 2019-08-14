package com.illud.transport.repository.search;

import com.illud.transport.domain.DriverDocuments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DriverDocuments entity.
 */
public interface DriverDocumentsSearchRepository extends ElasticsearchRepository<DriverDocuments, Long> {
}
