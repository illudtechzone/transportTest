package com.illud.transport.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Document(indexName = "driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "i_d_pcode")
    private String iDPcode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "driving_license_number")
    private String drivingLicenseNumber;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "total_rating")
    private Double totalRating;

    @Column(name = "status")
    private Boolean status;
    
    @GeoPointField
    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "driver")
    private Set<Review> reviews = new HashSet<>();
    @OneToMany(mappedBy = "driver")
    private Set<UserRating> userRatings = new HashSet<>();
    @OneToMany(mappedBy = "driver")
    private Set<Vehicle> vehicles = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getiDPcode() {
        return iDPcode;
    }

    public Driver iDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
        return this;
    }

    public void setiDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
    }

    public String getFirstName() {
        return firstName;
    }

    public Driver firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Driver lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public Driver mobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public Driver drivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
        return this;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public Driver image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Driver imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public Driver totalRating(Double totalRating) {
        this.totalRating = totalRating;
        return this;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public Boolean isStatus() {
        return status;
    }

    public Driver status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public Driver location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Driver reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Driver addReview(Review review) {
        this.reviews.add(review);
        review.setDriver(this);
        return this;
    }

    public Driver removeReview(Review review) {
        this.reviews.remove(review);
        review.setDriver(null);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<UserRating> getUserRatings() {
        return userRatings;
    }

    public Driver userRatings(Set<UserRating> userRatings) {
        this.userRatings = userRatings;
        return this;
    }

    public Driver addUserRating(UserRating userRating) {
        this.userRatings.add(userRating);
        userRating.setDriver(this);
        return this;
    }

    public Driver removeUserRating(UserRating userRating) {
        this.userRatings.remove(userRating);
        userRating.setDriver(null);
        return this;
    }

    public void setUserRatings(Set<UserRating> userRatings) {
        this.userRatings = userRatings;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public Driver vehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
        return this;
    }

    public Driver addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.setDriver(this);
        return this;
    }

    public Driver removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.setDriver(null);
        return this;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
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
        Driver driver = (Driver) o;
        if (driver.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), driver.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", iDPcode='" + getiDPcode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", mobileNumber=" + getMobileNumber() +
            ", drivingLicenseNumber='" + getDrivingLicenseNumber() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", totalRating=" + getTotalRating() +
            ", status='" + isStatus() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
