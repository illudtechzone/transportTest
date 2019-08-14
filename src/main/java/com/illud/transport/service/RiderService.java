package com.illud.transport.service;

import com.illud.transport.service.dto.RiderDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Rider.
 */
public interface RiderService {

    /**
     * Save a rider.
     *
     * @param riderDTO the entity to save
     * @return the persisted entity
     */
    RiderDTO save(RiderDTO riderDTO);

    /**
     * Get all the riders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RiderDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rider.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RiderDTO> findOne(Long id);

    /**
     * Delete the "id" rider.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the rider corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RiderDTO> search(String query, Pageable pageable);

	Optional<RiderDTO> createRiderIfnotExist(RiderDTO riderDTO);
}
