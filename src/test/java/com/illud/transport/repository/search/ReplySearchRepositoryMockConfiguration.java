package com.illud.transport.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ReplySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReplySearchRepositoryMockConfiguration {

    @MockBean
    private ReplySearchRepository mockReplySearchRepository;

}
