package com.illud.transport.client.activiti_rest_api.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.illud.transport.client.activiti_rest_api.ClientConfiguration;

@FeignClient(name="${activitiRestApi.name:activitiRestApi}", url="${activitiRestApi.url}", configuration = ClientConfiguration.class)
public interface GroupsApiClient extends GroupsApi {
}