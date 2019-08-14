package com.illud.transport.repository.search;

import com.illud.transport.domain.Reply;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reply entity.
 */
public interface ReplySearchRepository extends ElasticsearchRepository<Reply, Long> {
}
