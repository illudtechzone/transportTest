package com.illud.transport.service;

import com.illud.transport.client.activiti_rest_api.model.transport.DefaultInfoRequest;
import com.illud.transport.client.activiti_rest_api.model.transport.DriverInfo;
import com.illud.transport.client.activiti_rest_api.model.transport.PaymentStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RateAndReview;
import com.illud.transport.client.activiti_rest_api.model.transport.RideStatus;
import com.illud.transport.client.activiti_rest_api.model.transport.RiderLocationInfo;

public interface TransportCommandService {

	String initiate();
	
	void collectRiderLocationDetails(String taskId, DefaultInfoRequest defaultInfoRequest);
	
	void driverResponse(String taskId, DriverInfo driverInfo);

	void startRide(String taskId, RideStatus startRide);

	void rideComplete(String taskId, RideStatus rideComplete);

	void paymentStatus(String taskId, PaymentStatus paymentStatus);

	void rateAndReview(String taskId, RateAndReview rateAndReview);

	

}
