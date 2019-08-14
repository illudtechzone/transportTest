package com.illud.transport.repository.search;

import com.illud.transport.domain.UserRating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRating entity.
 */
public interface UserRatingSearchRepository extends ElasticsearchRepository<UserRating, Long> {
}
