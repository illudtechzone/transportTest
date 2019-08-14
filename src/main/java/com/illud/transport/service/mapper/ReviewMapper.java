package com.illud.transport.service.mapper;

import com.illud.transport.domain.*;
import com.illud.transport.service.dto.ReviewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Review and its DTO ReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {

    @Mapping(source = "driver.id", target = "driverId")
    ReviewDTO toDto(Review review);

    @Mapping(source = "driverId", target = "driver")
    @Mapping(target = "replies", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    default Review fromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}
