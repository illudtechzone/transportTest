package com.illud.transport.client.activiti_rest_api.model.transport;

import org.activiti.engine.impl.variable.ByteArrayType;

public class BookingDetails extends ByteArrayType{

	private String customerName;
	private String pickUp;
	private String destination;
	private String distance;
	
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPickUp() {
		return pickUp;
	}
	public void setPickUp(String pickUp) {
		this.pickUp = pickUp;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	
	
}
