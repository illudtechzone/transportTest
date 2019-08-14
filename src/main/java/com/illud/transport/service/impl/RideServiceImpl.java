package com.illud.transport.service.impl;

import com.illud.transport.service.RideService;
import com.illud.transport.domain.Ride;
import com.illud.transport.repository.RideRepository;
import com.illud.transport.repository.search.RideSearchRepository;
import com.illud.transport.service.dto.RideDTO;
import com.illud.transport.service.mapper.RideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ride.
 */
@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final Logger log = LoggerFactory.getLogger(RideServiceImpl.class);

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    private final RideSearchRepository rideSearchRepository;

    public RideServiceImpl(RideRepository rideRepository, RideMapper rideMapper, RideSearchRepository rideSearchRepository) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
        this.rideSearchRepository = rideSearchRepository;
    }

    /**
     * Save a ride.
     *
     * @param rideDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RideDTO save(RideDTO rideDTO) {
        log.debug("Request to save Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        RideDTO result = rideMapper.toDto(ride);
        rideSearchRepository.save(ride);
        return result;
    }

    /**
     * Get all the rides.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rides");
        return rideRepository.findAll(pageable)
            .map(rideMapper::toDto);
    }


    /**
     * Get one ride by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RideDTO> findOne(Long id) {
        log.debug("Request to get Ride : {}", id);
        return rideRepository.findById(id)
            .map(rideMapper::toDto);
    }

    /**
     * Delete the ride by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ride : {}", id);
        rideRepository.deleteById(id);
        rideSearchRepository.deleteById(id);
    }

    /**
     * Search for the ride corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RideDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Rides for query {}", query);
        return rideSearchRepository.search(queryStringQuery(query), pageable)
            .map(rideMapper::toDto);
    }
}
