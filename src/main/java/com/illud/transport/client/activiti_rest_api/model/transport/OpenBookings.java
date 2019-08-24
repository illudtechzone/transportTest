package com.illud.transport.client.activiti_rest_api.model.transport;

public class OpenBookings {

	
	private String pickUp;
	private String destination;
	private String distance;
	private String trackingProcessinstanceId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	private String taskId;
	private String taskName;
	
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
	
	@Override
	public String toString() {
		return "OpenBookings [pickUp=" + pickUp + ", destination=" + destination + ", distance=" + distance
				+ ", trackingProcessinstanceId=" + trackingProcessinstanceId + "]";
	}
}
