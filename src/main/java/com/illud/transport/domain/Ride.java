package com.illud.transport.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
@Document(indexName = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "rider_id")
    private String riderId;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "address_starting_point")
    private String addressStartingPoint;

    @Column(name = "address_destination")
    private String addressDestination;

    @Column(name = "fare")
    private Double fare;

    @Column(name = "total_distance")
    private Double totalDistance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public Ride driverId(String driverId) {
        this.driverId = driverId;
        return this;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public Ride riderId(String riderId) {
        this.riderId = riderId;
        return this;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Ride startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Ride endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getAddressStartingPoint() {
        return addressStartingPoint;
    }

    public Ride addressStartingPoint(String addressStartingPoint) {
        this.addressStartingPoint = addressStartingPoint;
        return this;
    }

    public void setAddressStartingPoint(String addressStartingPoint) {
        this.addressStartingPoint = addressStartingPoint;
    }

    public String getAddressDestination() {
        return addressDestination;
    }

    public Ride addressDestination(String addressDestination) {
        this.addressDestination = addressDestination;
        return this;
    }

    public void setAddressDestination(String addressDestination) {
        this.addressDestination = addressDestination;
    }

    public Double getFare() {
        return fare;
    }

    public Ride fare(Double fare) {
        this.fare = fare;
        return this;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Ride totalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
        return this;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ride ride = (Ride) o;
        if (ride.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ride.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ride{" +
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
