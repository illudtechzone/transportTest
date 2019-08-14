package com.illud.transport.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Ride entity.
 */
public class RideDTO implements Serializable {

    private Long id;

    private String driverId;

    private String riderId;

    private Instant startTime;

    private Instant endTime;

    private String addressStartingPoint;

    private String addressDestination;

    private Double fare;

    private Double totalDistance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getAddressStartingPoint() {
        return addressStartingPoint;
    }

    public void setAddressStartingPoint(String addressStartingPoint) {
        this.addressStartingPoint = addressStartingPoint;
    }

    public String getAddressDestination() {
        return addressDestination;
    }

    public void setAddressDestination(String addressDestination) {
        this.addressDestination = addressDestination;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RideDTO rideDTO = (RideDTO) o;
        if (rideDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RideDTO{" +
            "id=" + getId() +
            ", driverId='" + getDriverId() + "'" +
            ", riderId='" + getRiderId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", addressStartingPoint='" + getAddressStartingPoint() + "'" +
            ", addressDestination='" + getAddressDestination() + "'" +
            ", fare=" + getFare() +
            ", totalDistance=" + getTotalDistance() +
            "}";
    }
}
