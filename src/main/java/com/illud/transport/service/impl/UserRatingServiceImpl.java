package com.illud.transport.service.impl;

import com.illud.transport.service.UserRatingService;
import com.illud.transport.domain.UserRating;
import com.illud.transport.repository.UserRatingRepository;
import com.illud.transport.repository.search.UserRatingSearchRepository;
import com.illud.transport.service.dto.UserRatingDTO;
import com.illud.transport.service.mapper.UserRatingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRating.
 */
@Service
@Transactional
public class UserRatingServiceImpl implements UserRatingService {

    private final Logger log = LoggerFactory.getLogger(UserRatingServiceImpl.class);

    private final UserRatingRepository userRatingRepository;

    private final UserRatingMapper userRatingMapper;

    private final UserRatingSearchRepository userRatingSearchRepository;

    public UserRatingServiceImpl(UserRatingRepository userRatingRepository, UserRatingMapper userRatingMapper, UserRatingSearchRepository userRatingSearchRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRatingMapper = userRatingMapper;
        this.userRatingSearchRepository = userRatingSearchRepository;
    }

    /**
     * Save a userRating.
     *
     * @param userRatingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRatingDTO save(UserRatingDTO userRatingDTO) {
        log.debug("Request to save UserRating : {}", userRatingDTO);
        UserRating userRating = userRatingMapper.toEntity(userRatingDTO);
        userRating = userRatingRepository.save(userRating);
        UserRatingDTO result = userRatingMapper.toDto(userRating);
        userRatingSearchRepository.save(userRating);
        return result;
    }

    /**
     * Get all the userRatings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRatings");
        return userRatingRepository.findAll(pageable)
            .map(userRatingMapper::toDto);
    }


    /**
     * Get one userRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserRatingDTO> findOne(Long id) {
        log.debug("Request to get UserRating : {}", id);
        return userRatingRepository.findById(id)
            .map(userRatingMapper::toDto);
    }

    /**
     * Delete the userRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRating : {}", id);
        userRatingRepository.deleteById(id);
        userRatingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRatings for query {}", query);
        return userRatingSearchRepository.search(queryStringQuery(query), pageable)
            .map(userRatingMapper::toDto);
    }
}
