package com.illud.transport.client.activiti_rest_api.model.transport;

public class OpenBookings {

	private String pickUp;
	private String destination;
	private String distance;
	private String trackingProcessinstanceId;
	
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
	public String getTrackingProcessinstanceId() {
		return trackingProcessinstanceId;
	}
	public void setTrackingProcessinstanceId(String trackingProcessinstanceId) {
		this.trackingProcessinstanceId = trackingProcessinstanceId;
	}
}
