package com.illud.transport.service.impl;

import com.illud.transport.service.DriverService;
import com.illud.transport.domain.Driver;
import com.illud.transport.repository.DriverRepository;
import com.illud.transport.repository.search.DriverSearchRepository;
import com.illud.transport.service.dto.DriverDTO;
import com.illud.transport.service.mapper.DriverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Driver.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final DriverSearchRepository driverSearchRepository;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper, DriverSearchRepository driverSearchRepository) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.driverSearchRepository = driverSearchRepository;
    }

    /**
     * Save a driver.
     *
     * @param driverDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DriverDTO save(DriverDTO driverDTO) {
        log.debug("Request to save Driver : {}", driverDTO);
        Driver driver = driverMapper.toEntity(driverDTO);
        driver = driverRepository.save(driver);
        DriverDTO result = driverMapper.toDto(driver);
        driverSearchRepository.save(driver);
        return result;
    }

    /**
     * Get all the drivers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DriverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drivers");
        return driverRepository.findAll(pageable)
            .map(driverMapper::toDto);
    }


    /**
     * Get one driver by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        return driverRepository.findById(id)
            .map(driverMapper::toDto);
    }

    /**
     * Delete the driver by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        driverRepository.deleteById(id);
        driverSearchRepository.deleteById(id);
    }

    /**
     * Search for the driver corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DriverDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Drivers for query {}", query);
        return driverSearchRepository.search(queryStringQuery(query), pageable)
            .map(driverMapper::toDto);
    }

	@Override
	public Optional<DriverDTO> createDriverIfnotExist(DriverDTO driverDTO) {
		 Optional<Driver> driver=	driverRepository.findByiDPcode(driverDTO.getiDPcode());
		 if( driver.isPresent())
		 {
			 return driver.map(driverMapper::toDto);
		 }
		 else
		 {
			 return  Optional.of(save(driverDTO));	 
		 }
		
	}
}
