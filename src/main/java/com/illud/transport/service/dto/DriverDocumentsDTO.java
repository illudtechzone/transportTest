package com.illud.transport.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DriverDocuments entity.
 */
public class DriverDocumentsDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DriverDocumentsDTO driverDocumentsDTO = (DriverDocumentsDTO) o;
        if (driverDocumentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), driverDocumentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DriverDocumentsDTO{" +
            "id=" + getId() +
            "}";
    }
}
