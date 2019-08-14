package com.illud.transport.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.illud.transport.client.activiti_rest_api.model.transport.DefaultInfoRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.InitiateRide;
import com.illud.transport.client.activiti_rest_api.model.transport.PaymentStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RateAndReview;
import com.illud.transport.client.activiti_rest_api.model.transport.RideStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RiderLocationInfo;
import com.illud.transport.service.TransportCommandService;

@RestController
@RequestMapping("/api")
public class TransportCommandResource {

	private final Logger log = LoggerFactory.getLogger(TransportCommandResource.class);
	
	@Autowired
	TransportCommandService transportCommandService;
	
	@PostMapping("/initiate") 
	  public String initateWorkflow() {
	  
		
	  
	  return transportCommandService.initiate();
	  
	  }
	
	@PostMapping("/collectRiderLocationDetails/{taskId}")
	public void collectRiderLocationDetails(@PathVariable String taskId, @RequestBody DefaultInfoRequest defaultInfoRequest)
	{
		transportCommandService.collectRiderLocationDetails(taskId,defaultInfoRequest);
	}
	
	@PostMapping("/initiateRide/{taskId}")
    public void initiateride(@PathVariable String taskId, @RequestBody InitiateRide initiateRide){
    	
		transportCommandService.initiateRide(taskId,initiateRide);
    }
    
    @PostMapping("/startRide/{taskId}")
    public void startRide(@PathVariable String taskId,@RequestBody RideStatus startRide) {
    	
    	transportCommandService.startRide(taskId,startRide);
    }
    
    @PostMapping("/rideComplete/{taskId}")
    public void rideComplete(@PathVariable String taskId,@RequestBody RideStatus rideComplete) {
    	
    	transportCommandService.rideComplete(taskId,rideComplete);
    }
    
    @PostMapping("/payment/{taskId}")
	 public void payment(@PathVariable String taskId, @RequestBody PaymentStatus paymentStatus) {
		 
		 transportCommandService.paymentStatus(taskId,paymentStatus);
	 }
	 
	 
	 @PostMapping("/rateAndReview/{taskId}")
	 public void rateAndReview(@PathVariable String taskId, @RequestBody RateAndReview rateAndReview) {
		 
		 transportCommandService.rateAndReview(taskId,rateAndReview);
	 }
}
