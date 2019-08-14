package com.illud.transport.service.mapper;

import com.illud.transport.domain.*;
import com.illud.transport.service.dto.RideDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ride and its DTO RideDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RideMapper extends EntityMapper<RideDTO, Ride> {



    default Ride fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ride ride = new Ride();
        ride.setId(id);
        return ride;
    }
}
