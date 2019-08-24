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
import org.springframework.web.bind.annotation.RequestParam;
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
	  
	  @Autowired
	  private ProcessInstancesApi processInstancesApi;
	
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
		
		return tasksApi.getTasks(name, nameLike, description, priority, minimumPriority, maximumPriority, assignee, assigneeLike, owner, ownerLike, unassigned, delegationState, candidateUser, candidateGroup, candidateGroups, involvedUser, taskDefinitionKey, taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey, processInstanceBusinessKeyLike, processDefinitionId, processDefinitionKey, processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike, executionId, createdOn, createdBefore, createdAfter, dueOn, dueBefore, dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables, includeProcessVariables, tenantId, tenantIdLike, withoutTenantId, candidateOrAssigned, category,/*pageable.getPageNumber()+""*/"0",null, "desc",/* pageable.getPageSize()+""*/"150");
	}

	
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
		
		log.info("Number of items in the collection "+taskResponseCollectInfo.size());
		log.info("Task Id of the item is "+taskId);
		
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

	
	public ResponseEntity<DataResponse> getHistoricProcessInstance(){
		return processInstancesApi.getProcessInstances(null, null, "illuid-work:5:13263", null, null, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public List<OpenBookings> getAllOpenBookings(String name,  String assignee, String assigneeLike,
			String processInstanceId,  @Valid String processDefinitionId,
			@Valid String processDefinitionKey) {
		
		ResponseEntity<DataResponse> response = tasksApi.getTasks(name, null, null, null,
				null, null, assignee, assigneeLike, null, null, null, null,
				null, null, null, null, null, null,
				processInstanceId, null, null, "illuid-work:5:13263",
				processDefinitionKey, null, null, null,
				null, null, null, null, null, null, null, null,
				null, null, null, null, null, null,
				null, null, null, /*pageable.getPageNumber()+""*/"0",null, "desc",/* pageable.getPageSize()+""*/"150");
		
		//List<LinkedHashMap<String, String>> taskResponses = (List<LinkedHashMap<String, String>>) response.getBody()
			//	.getData();
		
		//List<DataResponse> taskResponses = (List<DataResponse>) getHistoricProcessInstance().getBody().getData();
		
		
		
		List<LinkedHashMap<String, String>> taskResponses=(List<LinkedHashMap<String, String>>) getHistoricProcessInstance().getBody().getData();
		List<OpenBookings> myBookings = new ArrayList<OpenBookings>();
		log.info("/////////////////////"+taskResponses);
		
		log.info("/////////////////////"+taskResponses.size());
		
		
		  for (LinkedHashMap<String, String> taskResponse : taskResponses) {
		  OpenBookings myBooking = new OpenBookings(); 
		  String taskProcessInstanceId =taskResponse.get("id"); 
		  log.info("***************************************************Process Instance id of open appointment is "+taskProcessInstanceId);
		  
		  DefaultInfoRequest df = new DefaultInfoRequest();
		  df=getBookingDetails(taskProcessInstanceId);
		  
		  myBooking.setDistance(df.getDistance());
		  myBooking.setPickUp(df.getPickUp());
		  myBooking.setDestination(df.getDestination());
		  myBooking.setTrackingProcessinstanceId(taskProcessInstanceId);
		  
		  myBookings.add(myBooking);
		  		  
		  
		  }
		 
		return myBookings;
		
	}


	@Override
	public ResponseEntity<Void> deleteProcessInstance(String processInstanceId) {
		log.info("***************************************************Process Instance id to delete  is  "+processInstanceId);
		//log.info("***************************************************Process Instance id to delete  is  "+getAllPendingBookings(null, null, null, processInstanceId, null, null));
		return processInstancesApi.deleteProcessInstance(processInstanceId, null);
	}
	
	@Override
	public List<OpenBookings>getAllPendingBookings( String name,
			 String nameLike,
			 String assignee,
			 String assigneeLike,
			 String candidateUser,
			 String candidateGroup,
			 String candidateGroups,
			 String processInstanceId,
			 String processDefinitionId,
			 String processDefinitionKey,
			 String createdOn,
			String createdBefore,
			 String createdAfter/*Pageable pageable*/) {

		
		ResponseEntity<DataResponse> response = tasksApi.getTasks(name, nameLike, null, null, null, null, assignee,
				assigneeLike, null, null, null, null, candidateUser, candidateGroup, candidateGroups, null, null, null,
				processInstanceId, null, null, "illuid-work:5:13263", null, null, null, null, null, createdOn, createdBefore, createdAfter, null,
				null, null, null, null, null, null, null, null, null, null, null, null, /*pageable.getPageNumber()+""*/"0",null, "desc",/* pageable.getPageSize()+""*/"1500");
		List<LinkedHashMap<String, String>> myTasks = (List<LinkedHashMap<String, String>>) response.getBody()
				.getData();
		log.info("*****************************////////////////////////////////////**********"+myTasks.size());
		List<OpenBookings> bookings = new ArrayList<OpenBookings>();
		
		myTasks.forEach(task -> {
			OpenBookings openTask = new OpenBookings();
			String taskProcessInstanceId = task.get("processInstanceId");
			//log.info("***************************************************Process Instance id to delete  is  "+taskProcessInstanceId);
			String taskName = task.get("name");
			String taskId = task.get("id");
			openTask.setPickUp(getBookingDetails(taskProcessInstanceId).getPickUp());
			openTask.setDestination(getBookingDetails(taskProcessInstanceId).getDestination());
			openTask.setDistance(getBookingDetails(taskProcessInstanceId).getDistance());
			openTask.setTrackingProcessinstanceId(taskProcessInstanceId);
			openTask.setTaskId(taskId);
			openTask.setTaskName(taskName);
			
			
		
			bookings.add(openTask);
			System.out.println(
					"TaskName is " + taskName + " taskid is " + taskId + " processinstanceId " + taskProcessInstanceId);
		});
		
		return bookings;
	}


	
}
