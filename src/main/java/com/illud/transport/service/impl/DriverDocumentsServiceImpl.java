package com.illud.transport.service.impl;

import com.illud.transport.service.DriverDocumentsService;
import com.illud.transport.domain.DriverDocuments;
import com.illud.transport.repository.DriverDocumentsRepository;
import com.illud.transport.repository.search.DriverDocumentsSearchRepository;
import com.illud.transport.service.dto.DriverDocumentsDTO;
import com.illud.transport.service.mapper.DriverDocumentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DriverDocuments.
 */
@Service
@Transactional
public class DriverDocumentsServiceImpl implements DriverDocumentsService {

    private final Logger log = LoggerFactory.getLogger(DriverDocumentsServiceImpl.class);

    private final DriverDocumentsRepository driverDocumentsRepository;

    private final DriverDocumentsMapper driverDocumentsMapper;

    private final DriverDocumentsSearchRepository driverDocumentsSearchRepository;

    public DriverDocumentsServiceImpl(DriverDocumentsRepository driverDocumentsRepository, DriverDocumentsMapper driverDocumentsMapper, DriverDocumentsSearchRepository driverDocumentsSearchRepository) {
        this.driverDocumentsRepository = driverDocumentsRepository;
        this.driverDocumentsMapper = driverDocumentsMapper;
        this.driverDocumentsSearchRepository = driverDocumentsSearchRepository;
    }

    /**
     * Save a driverDocuments.
     *
     * @param driverDocumentsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DriverDocumentsDTO save(DriverDocumentsDTO driverDocumentsDTO) {
        log.debug("Request to save DriverDocuments : {}", driverDocumentsDTO);
        DriverDocuments driverDocuments = driverDocumentsMapper.toEntity(driverDocumentsDTO);
        driverDocuments = driverDocumentsRepository.save(driverDocuments);
        DriverDocumentsDTO result = driverDocumentsMapper.toDto(driverDocuments);
        driverDocumentsSearchRepository.save(driverDocuments);
        return result;
    }

    /**
     * Get all the driverDocuments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DriverDocumentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DriverDocuments");
        return driverDocumentsRepository.findAll(pageable)
            .map(driverDocumentsMapper::toDto);
    }


    /**
     * Get one driverDocuments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDocumentsDTO> findOne(Long id) {
        log.debug("Request to get DriverDocuments : {}", id);
        return driverDocumentsRepository.findById(id)
            .map(driverDocumentsMapper::toDto);
    }

    /**
     * Delete the driverDocuments by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DriverDocuments : {}", id);
        driverDocumentsRepository.deleteById(id);
        driverDocumentsSearchRepository.deleteById(id);
    }

    /**
     * Search for the driverDocuments corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DriverDocumentsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DriverDocuments for query {}", query);
        return driverDocumentsSearchRepository.search(queryStringQuery(query), pageable)
            .map(driverDocumentsMapper::toDto);
    }
}
