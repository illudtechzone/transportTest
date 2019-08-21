package com.illud.transport.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.illud.transport.client.activiti_rest_api.api.FormsApi;
import com.illud.transport.client.activiti_rest_api.api.ProcessInstancesApi;
import com.illud.transport.client.activiti_rest_api.model.ProcessInstanceCreateRequest;
import com.illud.transport.client.activiti_rest_api.model.ProcessInstanceResponse;
import com.illud.transport.client.activiti_rest_api.model.RestFormProperty;
import com.illud.transport.client.activiti_rest_api.model.RestVariable;
import com.illud.transport.client.activiti_rest_api.model.SubmitFormRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.DefaultInfoRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.DriverInfo;
import com.illud.transport.client.activiti_rest_api.model.transport.PaymentStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RateAndReview;
import com.illud.transport.client.activiti_rest_api.model.transport.RideStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RiderLocationInfo;
import com.illud.transport.service.TransportCommandService;


@Service
@SuppressWarnings("unchecked")
public class TransportCommandServiceimpl implements TransportCommandService {

	private final Logger log = LoggerFactory.getLogger(TransportCommandServiceimpl.class);
	
	@Autowired
    private FormsApi formsApi;

    
    @Autowired
    private ProcessInstancesApi processInstanceApi;
	
	
	@Override
	public String initiate() {
		
		ProcessInstanceCreateRequest processInstanceCreateRequest=new ProcessInstanceCreateRequest();
   		List<RestVariable> variables=new ArrayList<RestVariable>();
   		
   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:1:2504");
   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:3:10814");
   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:4:11267");
   		processInstanceCreateRequest.setProcessDefinitionId("illuid-work:5:13263");
   		
   		RestVariable riderRestVariable=new RestVariable();
   		riderRestVariable.setName("rider");
   		riderRestVariable.setType("string");
   		riderRestVariable.setValue("rider");
   		variables.add(riderRestVariable);
   		
   		RestVariable driverRestVariable=new RestVariable();
   		driverRestVariable.setName("driver");
   		driverRestVariable.setType("string");
   		driverRestVariable.setValue("driver");
   		
   		variables.add(driverRestVariable);
   	
   		
   		
   		log.info("*****************************************************"+variables.size());
   		processInstanceCreateRequest.setVariables(variables);
   		log.info("*****************************************************"+processInstanceCreateRequest.getVariables());
   		
   		ResponseEntity<ProcessInstanceResponse> processInstanceResponse = processInstanceApi
				.createProcessInstance(processInstanceCreateRequest);
		String processInstanceId = processInstanceResponse.getBody().getId();
		String processDefinitionId = processInstanceCreateRequest.getProcessDefinitionId();
		log.info("++++++++++++++++processDefinitionId++++++++++++++++++"+ processDefinitionId);
		log.info("++++++++++++++++ProcessInstanceId is+++++++++++++ " + processInstanceId);
		
   		processInstanceApi.createProcessInstance(processInstanceCreateRequest);
   		
		
		return processInstanceId;
		
	}
	
	

	@Override
	public void collectRiderLocationDetails(String taskId, DefaultInfoRequest defaultInfoRequest) {
		
		log .info("into ====================collectRiderLocationDetails()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
		
   		RestFormProperty distanceFormProperty = new RestFormProperty();
   		distanceFormProperty.setId("distance");
   		distanceFormProperty.setName("distance");
   		distanceFormProperty.setType("String");
   		distanceFormProperty.setReadable(true);
   		distanceFormProperty.setValue(defaultInfoRequest.getDistance());
   		formProperties.add(distanceFormProperty);
   		
   		RestFormProperty pickUpFormProperty = new RestFormProperty();
   		pickUpFormProperty.setId("pickUp");
   		pickUpFormProperty.setName("pickUp");
   		pickUpFormProperty.setType("String");
   		pickUpFormProperty.setReadable(true);
   		pickUpFormProperty.setValue(defaultInfoRequest.getPickUp());
   		formProperties.add(pickUpFormProperty);
   		
   		RestFormProperty destinationFormProperty = new RestFormProperty();
   		destinationFormProperty.setId("destination");
   		destinationFormProperty.setName("destination");
   		destinationFormProperty.setType("String");
   		destinationFormProperty.setReadable(true);
   		destinationFormProperty.setValue(defaultInfoRequest.getDestination());
   		formProperties.add(destinationFormProperty);
		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	
	@Override
	public void driverResponse(String taskId, DriverInfo driverInfo) {
		
		log .info("into ====================initiateRide()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
   		
   		RestFormProperty nameFormProperty = new RestFormProperty();
   		nameFormProperty.setId("name");
   		nameFormProperty.setName("name");
   		nameFormProperty.setType("String");
   		nameFormProperty.setReadable(true);
   		nameFormProperty.setValue(driverInfo.getName());
   		formProperties.add(nameFormProperty);
   		
   		RestFormProperty emailFormProperty = new RestFormProperty();
   		emailFormProperty.setId("email");
   		emailFormProperty.setName("email");
   		emailFormProperty.setType("String");
   		emailFormProperty.setReadable(true);
   		emailFormProperty.setValue(driverInfo.getEmail());
   		formProperties.add(emailFormProperty);
   		
   		
   		RestFormProperty statusFormProperty = new RestFormProperty();
   		statusFormProperty.setId("status");
   		statusFormProperty.setName("destination");
   		statusFormProperty.setType("String");
   		statusFormProperty.setReadable(true);
   		statusFormProperty.setValue(driverInfo.getStatus());
   		formProperties.add(statusFormProperty);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("destination");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(driverInfo.getTrackingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void startRide(String taskId, RideStatus startRide) {
		
		log .info("into ====================startRide()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty .setId("trackingId");
   		trackingIdFormProperty .setName("trackingId");
   		trackingIdFormProperty .setType("String");
   		trackingIdFormProperty .setReadable(true);
   		trackingIdFormProperty .setValue(startRide.getTrackingId());
   		formProperties.add(trackingIdFormProperty );
   		
   		RestFormProperty statusFormProperty = new RestFormProperty();
   		statusFormProperty.setId("status");
   		statusFormProperty.setName("status");
   		statusFormProperty.setType("String");
   		statusFormProperty.setReadable(true);
   		statusFormProperty.setValue(startRide.getStatus());
   		formProperties.add(statusFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void rideComplete(String taskId, RideStatus rideComplete) {
		
		log .info("into ====================rideComplete()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty .setId("trackingId");
   		trackingIdFormProperty .setName("trackingId");
   		trackingIdFormProperty .setType("String");
   		trackingIdFormProperty .setReadable(true);
   		trackingIdFormProperty .setValue(rideComplete.getTrackingId());
   		formProperties.add(trackingIdFormProperty );
   		
   		RestFormProperty statusFormProperty = new RestFormProperty();
   		statusFormProperty.setId("status");
   		statusFormProperty.setName("status");
   		statusFormProperty.setType("String");
   		statusFormProperty.setReadable(true);
   		statusFormProperty.setValue(rideComplete.getStatus());
   		formProperties.add(statusFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void paymentStatus(String taskId, PaymentStatus paymentStatus) {
		

		log .info("into ====================startRide()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("destination");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(paymentStatus.getTrackingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		RestFormProperty paymentStatusFormProperty = new RestFormProperty();
   		paymentStatusFormProperty.setId("trackingId");
   		paymentStatusFormProperty.setName("destination");
   		paymentStatusFormProperty.setType("String");
   		paymentStatusFormProperty.setReadable(true);
   		paymentStatusFormProperty.setValue(paymentStatus.getPaymentStatus());
   		formProperties.add(paymentStatusFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void rateAndReview(String taskId, RateAndReview rateAndReview) {
		
		log .info("into ====================startRide()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("destination");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(rateAndReview.getTrackingId());
   		formProperties.add(trackingIdFormProperty);
		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	

}
