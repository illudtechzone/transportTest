package com.illud.transport.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DriverDocumentsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DriverDocumentsSearchRepositoryMockConfiguration {

    @MockBean
    private DriverDocumentsSearchRepository mockDriverDocumentsSearchRepository;

}
