package com.illud.transport.service;

import com.illud.transport.service.dto.DriverDocumentsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DriverDocuments.
 */
public interface DriverDocumentsService {

    /**
     * Save a driverDocuments.
     *
     * @param driverDocumentsDTO the entity to save
     * @return the persisted entity
     */
    DriverDocumentsDTO save(DriverDocumentsDTO driverDocumentsDTO);

    /**
     * Get all the driverDocuments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DriverDocumentsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" driverDocuments.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DriverDocumentsDTO> findOne(Long id);

    /**
     * Delete the "id" driverDocuments.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the driverDocuments corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DriverDocumentsDTO> search(String query, Pageable pageable);
}
