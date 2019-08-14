package com.illud.transport.service.mapper;

import com.illud.transport.domain.*;
import com.illud.transport.service.dto.UserRatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRating and its DTO UserRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface UserRatingMapper extends EntityMapper<UserRatingDTO, UserRating> {

    @Mapping(source = "driver.id", target = "driverId")
    UserRatingDTO toDto(UserRating userRating);

    @Mapping(source = "driverId", target = "driver")
    UserRating toEntity(UserRatingDTO userRatingDTO);

    default UserRating fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRating userRating = new UserRating();
        userRating.setId(id);
        return userRating;
    }
}
