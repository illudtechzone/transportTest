package com.illud.transport.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Rider entity.
 */
public class RiderDTO implements Serializable {

    private Long id;

    private String iDPcode;

    private String firstName;

    private String lastName;

    private String mobilenumber;

    private String email;


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RiderDTO riderDTO = (RiderDTO) o;
        if (riderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), riderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RiderDTO{" +
            "id=" + getId() +
            ", iDPcode='" + getiDPcode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", mobilenumber='" + getMobilenumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
