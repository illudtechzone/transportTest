package com.illud.transport.service.impl;

import com.illud.transport.service.RiderService;
import com.illud.transport.domain.Driver;
import com.illud.transport.domain.Rider;
import com.illud.transport.repository.RiderRepository;
import com.illud.transport.repository.search.RiderSearchRepository;
import com.illud.transport.service.dto.RiderDTO;
import com.illud.transport.service.mapper.RiderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Rider.
 */
@Service
@Transactional
public class RiderServiceImpl implements RiderService {

    private final Logger log = LoggerFactory.getLogger(RiderServiceImpl.class);

    private final RiderRepository riderRepository;

    private final RiderMapper riderMapper;

    private final RiderSearchRepository riderSearchRepository;

    public RiderServiceImpl(RiderRepository riderRepository, RiderMapper riderMapper, RiderSearchRepository riderSearchRepository) {
        this.riderRepository = riderRepository;
        this.riderMapper = riderMapper;
        this.riderSearchRepository = riderSearchRepository;
    }

    /**
     * Save a rider.
     *
     * @param riderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RiderDTO save(RiderDTO riderDTO) {
        log.debug("Request to save Rider : {}", riderDTO);
        Rider rider = riderMapper.toEntity(riderDTO);
        rider = riderRepository.save(rider);
        RiderDTO result = riderMapper.toDto(rider);
        riderSearchRepository.save(rider);
        return result;
    }

    /**
     * Get all the riders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RiderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Riders");
        return riderRepository.findAll(pageable)
            .map(riderMapper::toDto);
    }


    /**
     * Get one rider by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RiderDTO> findOne(Long id) {
        log.debug("Request to get Rider : {}", id);
        return riderRepository.findById(id)
            .map(riderMapper::toDto);
    }

    /**
     * Delete the rider by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rider : {}", id);
        riderRepository.deleteById(id);
        riderSearchRepository.deleteById(id);
    }

    /**
     * Search for the rider corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RiderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Riders for query {}", query);
        return riderSearchRepository.search(queryStringQuery(query), pageable)
            .map(riderMapper::toDto);
    }

	@Override
	public Optional<RiderDTO> createRiderIfnotExist(RiderDTO riderDTO) {
		 Optional<Rider> rider=	riderRepository.findByiDPcode(riderDTO.getiDPcode());
		 if( rider.isPresent())
		 {
			 return rider.map(riderMapper::toDto);
		 }
		 else
		 {
			 return  Optional.of(save(riderDTO));	 
		 }
	}
}
