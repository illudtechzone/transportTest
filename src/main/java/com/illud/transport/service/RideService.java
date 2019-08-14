package com.illud.transport.service;

import com.illud.transport.service.dto.RideDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Ride.
 */
public interface RideService {

    /**
     * Save a ride.
     *
     * @param rideDTO the entity to save
     * @return the persisted entity
     */
    RideDTO save(RideDTO rideDTO);

    /**
     * Get all the rides.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RideDTO> findAll(Pageable pageable);


    /**
     * Get the "id" ride.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RideDTO> findOne(Long id);

    /**
     * Delete the "id" ride.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ride corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RideDTO> search(String query, Pageable pageable);
}
