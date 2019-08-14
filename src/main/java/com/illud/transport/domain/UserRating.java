package com.illud.transport.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A UserRating.
 */
@Entity
@Table(name = "user_rating")
@Document(indexName = "userrating")
public class UserRating implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "i_d_pcode")
    private String iDPcode;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "rated_on")
    private Instant ratedOn;

    @ManyToOne
    @JsonIgnoreProperties("userRatings")
    private Driver driver;

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

    public UserRating iDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
        return this;
    }

    public void setiDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
    }

    public Double getRating() {
        return rating;
    }

    public UserRating rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Instant getRatedOn() {
        return ratedOn;
    }

    public UserRating ratedOn(Instant ratedOn) {
        this.ratedOn = ratedOn;
        return this;
    }

    public void setRatedOn(Instant ratedOn) {
        this.ratedOn = ratedOn;
    }

    public Driver getDriver() {
        return driver;
    }

    public UserRating driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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
        UserRating userRating = (UserRating) o;
        if (userRating.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRating{" +
            "id=" + getId() +
            ", iDPcode='" + getiDPcode() + "'" +
            ", rating=" + getRating() +
            ", ratedOn='" + getRatedOn() + "'" +
            "}";
    }
}
