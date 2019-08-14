package com.illud.transport.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RiderSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RiderSearchRepositoryMockConfiguration {

    @MockBean
    private RiderSearchRepository mockRiderSearchRepository;

}
