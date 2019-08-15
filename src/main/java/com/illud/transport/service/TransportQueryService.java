package com.illud.transport.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.illud.transport.client.activiti_rest_api.model.DataResponse;
import com.illud.transport.client.activiti_rest_api.model.transport.BookingDetails;
import com.illud.transport.client.activiti_rest_api.model.transport.DefaultInfoRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.OpenBookings;
import com.illud.transport.client.activiti_rest_api.model.transport.RiderLocationInfo;

public interface TransportQueryService {

	ResponseEntity<DataResponse> getTasks(String name, String nameLike, String description, String priority,
			String minimumPriority, String maximumPriority, String assignee, String assigneeLike, String owner,
			String ownerLike, String unassigned, String delegationState, String candidateUser, String candidateGroup,
			String candidateGroups, String involvedUser, String taskDefinitionKey, String taskDefinitionKeyLike,
			String processInstanceId, String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
			String processDefinitionId, String processDefinitionKey, String processDefinitionKeyLike,
			String processDefinitionName, String processDefinitionNameLike, String executionId, String createdOn,
			String createdBefore, String createdAfter, String dueOn, String dueBefore, String dueAfter,
			Boolean withoutDueDate, Boolean excludeSubTasks, Boolean active, Boolean includeTaskLocalVariables,
			Boolean includeProcessVariables, String tenantId, String tenantIdLike, Boolean withoutTenantId,
			String candidateOrAssigned, String category);

	/*
	 * List<OpenBookings> getOpenAppointments(String name, String nameLike, String
	 * description, String priority, String minimumPriority, String maximumPriority,
	 * String assignee, String assigneeLike, String owner, String ownerLike, String
	 * unassigned, String delegationState, String candidateUser, String
	 * candidateGroup, String candidateGroups, String involvedUser, String
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
	 * @Valid String candidateOrAssigned, @Valid String category);
	 */

	DefaultInfoRequest getBookingDetails(String processInstanceId);

	List<DefaultInfoRequest> getAllOpenBookings(String name, String nameLike, String description, String priority,
			String minimumPriority, String maximumPriority, String assignee, String assigneeLike, String owner,
			String ownerLike, String unassigned, String delegationState, String candidateUser, String candidateGroup,
			String candidateGroups, String involvedUser, String taskDefinitionKey, String taskDefinitionKeyLike,
			String processInstanceId, String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
			@Valid String processDefinitionId, @Valid String processDefinitionKey,
			@Valid String processDefinitionKeyLike, @Valid String processDefinitionName,
			@Valid String processDefinitionNameLike, @Valid String executionId, @Valid String createdOn,
			@Valid String createdBefore, @Valid String createdAfter, @Valid String dueOn, @Valid String dueBefore,
			@Valid String dueAfter, @Valid Boolean withoutDueDate, @Valid Boolean excludeSubTasks,
			@Valid Boolean active, @Valid Boolean includeTaskLocalVariables, @Valid Boolean includeProcessVariables,
			@Valid String tenantId, @Valid String tenantIdLike, @Valid Boolean withoutTenantId,
			@Valid String candidateOrAssigned, @Valid String category);

}
