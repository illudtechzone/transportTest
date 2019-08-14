package com.illud.transport.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserRating entity.
 */
public class UserRatingDTO implements Serializable {

    private Long id;

    private String iDPcode;

    private Double rating;

    private Instant ratedOn;


    private Long driverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getiDPcode() {
        return iDPcode;
    }

    public void setiDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Instant getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Instant ratedOn) {
        this.ratedOn = ratedOn;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRatingDTO userRatingDTO = (UserRatingDTO) o;
        if (userRatingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRatingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRatingDTO{" +
            "id=" + getId() +
            ", iDPcode='" + getiDPcode() + "'" +
            ", rating=" + getRating() +
            ", ratedOn='" + getRatedOn() + "'" +
            ", driver=" + getDriverId() +
            "}";
    }
}
