package com.illud.transport.service.mapper;

import com.illud.transport.domain.*;
import com.illud.transport.service.dto.DriverDocumentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DriverDocuments and its DTO DriverDocumentsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DriverDocumentsMapper extends EntityMapper<DriverDocumentsDTO, DriverDocuments> {



    default DriverDocuments fromId(Long id) {
        if (id == null) {
            return null;
        }
        DriverDocuments driverDocuments = new DriverDocuments();
        driverDocuments.setId(id);
        return driverDocuments;
    }
}
