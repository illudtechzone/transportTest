package com.illud.transport.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.illud.transport.client.activiti_rest_api.api.HistoryApi;
import com.illud.transport.client.activiti_rest_api.api.ProcessInstancesApi;
import com.illud.transport.client.activiti_rest_api.api.TasksApi;
import com.illud.transport.client.activiti_rest_api.model.DataResponse;
import com.illud.transport.client.activiti_rest_api.model.transport.BookingDetails;
import com.illud.transport.client.activiti_rest_api.model.transport.DefaultInfoRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.OpenBookings;
import com.illud.transport.client.activiti_rest_api.model.transport.RiderLocationInfo;
import com.illud.transport.service.TransportQueryService;

@Service
@SuppressWarnings("unchecked")
public class TransportQueryServiceImpl implements TransportQueryService {

	private final Logger log = LoggerFactory.getLogger(TransportQueryServiceImpl.class);
	
	  @Autowired
	  private TasksApi tasksApi;
	  
	  @Autowired
	  private HistoryApi historyApi;
	
	@Override
	public ResponseEntity<DataResponse> getTasks(String name, String nameLike, String description, String priority,
			String minimumPriority, String maximumPriority, String assignee, String assigneeLike, String owner,
			String ownerLike, String unassigned, String delegationState, String candidateUser, String candidateGroup,
			String candidateGroups, String involvedUser, String taskDefinitionKey, String taskDefinitionKeyLike,
			String processInstanceId, String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
			String processDefinitionId, String processDefinitionKey, String processDefinitionKeyLike,
			String processDefinitionName, String processDefinitionNameLike, String executionId, String createdOn,
			String createdBefore, String createdAfter, String dueOn, String dueBefore, String dueAfter,
			Boolean withoutDueDate, Boolean excludeSubTasks, Boolean active, Boolean includeTaskLocalVariables,
			Boolean includeProcessVariables, String tenantId, String tenantIdLike, Boolean withoutTenantId,
			String candidateOrAssigned, String category) {
		
		return tasksApi.getTasks(name, nameLike, description, priority, minimumPriority, maximumPriority, assignee, assigneeLike, owner, ownerLike, unassigned, delegationState, candidateUser, candidateGroup, candidateGroups, involvedUser, taskDefinitionKey, taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey, processInstanceBusinessKeyLike, processDefinitionId, processDefinitionKey, processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike, executionId, createdOn, createdBefore, createdAfter, dueOn, dueBefore, dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables, includeProcessVariables, tenantId, tenantIdLike, withoutTenantId, candidateOrAssigned, category);
	}

	/*
	 * @Override public List<OpenBookings> getOpenAppointments(String name, String
	 * nameLike, String description, String priority, String minimumPriority, String
	 * maximumPriority, String assignee, String assigneeLike, String owner, String
	 * ownerLike, String unassigned, String delegationState, String candidateUser,
	 * String candidateGroup, String candidateGroups, String involvedUser, String
	 * taskDefinitionKey, String taskDefinitionKeyLike, String processInstanceId,
	 * String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
	 * 
	 * @Valid String processDefinitionId, @Valid String processDefinitionKey,
	 * 
	 * @Valid String processDefinitionKeyLike, @Valid String processDefinitionName,
	 * 
	 * @Valid String processDefinitionNameLike, @Valid String executionId, @Valid
	 * String createdOn,
	 * 
	 * @Valid String createdBefore, @Valid String createdAfter, @Valid String
	 * dueOn, @Valid String dueBefore,
	 * 
	 * @Valid String dueAfter, @Valid Boolean withoutDueDate, @Valid Boolean
	 * excludeSubTasks,
	 * 
	 * @Valid Boolean active, @Valid Boolean includeTaskLocalVariables, @Valid
	 * Boolean includeProcessVariables,
	 * 
	 * @Valid String tenantId, @Valid String tenantIdLike, @Valid Boolean
	 * withoutTenantId,
	 * 
	 * @Valid String candidateOrAssigned, @Valid String category) {
	 * 
	 * ResponseEntity<DataResponse> response = tasksApi.getTasks(name, nameLike,
	 * description, priority, minimumPriority, maximumPriority, assignee,
	 * assigneeLike, owner, ownerLike, unassigned, delegationState, candidateUser,
	 * candidateGroup, candidateGroups, involvedUser, taskDefinitionKey,
	 * taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey,
	 * processInstanceBusinessKeyLike, processDefinitionId, processDefinitionKey,
	 * processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike,
	 * executionId, createdOn, createdBefore, createdAfter, dueOn, dueBefore,
	 * dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables,
	 * includeProcessVariables, tenantId, tenantIdLike, withoutTenantId,
	 * candidateOrAssigned, category);
	 * 
	 * List<LinkedHashMap<String, String>> taskResponses =
	 * (List<LinkedHashMap<String, String>>) response.getBody() .getData();
	 * 
	 * List<OpenBookings> openBookings = new ArrayList<OpenBookings>(); for
	 * (LinkedHashMap<String, String> taskResponse : taskResponses) { OpenBookings
	 * openBooking = new OpenBookings(); String taskProcessInstanceId =
	 * taskResponse.get("processInstanceId");
	 * log.info("Process Instance id of open appointment is "+taskProcessInstanceId)
	 * ;
	 * 
	 * }
	 * 
	 * return null; }
	 */

	@Override
	public DefaultInfoRequest getBookingDetails(String processInstanceId) {
		
		DefaultInfoRequest defaultInfoRequest = new DefaultInfoRequest();
		List<LinkedHashMap<String, String>> taskResponseCollectInfo = (List<LinkedHashMap<String, String>>) getHistoricTaskusingProcessInstanceIdAndName(
				processInstanceId, "collectRiderLocationDetails").getBody().getData();
		String taskId = taskResponseCollectInfo.get(0).get("id");
		log.info("Collect Informations TaskID is "+taskId);
		
		ResponseEntity<DataResponse> requestDetails = historyApi.getHistoricDetailInfo(null, processInstanceId, null, null,
				taskId.toString(), true, false);
		
		List<LinkedHashMap<String, String>> requestFormProperties = (List<LinkedHashMap<String, String>>) requestDetails
				.getBody().getData();
		
		log.info("Number of slots in the collection "+taskResponseCollectInfo.size());
		log.info("Task Id of the slot is "+taskId);
		
		for (LinkedHashMap<String, String> requestMap : requestFormProperties) {
			String distance = null;
			String pickUp = null;
			String destination = null;
			
			String propertyId = requestMap.get("propertyId");
			if (propertyId.equals("distance")) {
				distance = requestMap.get("propertyValue");
				defaultInfoRequest.setDistance(distance);
			}
			else if (propertyId.equals("pickUp")) {
				pickUp = requestMap.get("propertyValue");
				defaultInfoRequest.setPickUp(pickUp);
			}
			else if (propertyId.equals("destination")) {
				destination = requestMap.get("propertyValue");
				defaultInfoRequest.setDestination(destination);
			}
		}
		
		return defaultInfoRequest;
		
		
	}
	
	
	
	public ResponseEntity<DataResponse> getHistoricTaskusingProcessInstanceIdAndName(String processInstanceId,
			String name) {
		return historyApi.listHistoricTaskInstances(null, processInstanceId, null, null, null, null, null, null, null,
				null, null, name, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

	}
	
	
	
}
