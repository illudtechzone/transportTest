package com.illud.transport.web.rest;
import com.illud.transport.service.RideService;
import com.illud.transport.web.rest.errors.BadRequestAlertException;
import com.illud.transport.web.rest.util.HeaderUtil;
import com.illud.transport.web.rest.util.PaginationUtil;
import com.illud.transport.service.dto.RideDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ride.
 */
@RestController
@RequestMapping("/api")
public class RideResource {

    private final Logger log = LoggerFactory.getLogger(RideResource.class);

    private static final String ENTITY_NAME = "transportRide";

    private final RideService rideService;

    public RideResource(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * POST  /rides : Create a new ride.
     *
     * @param rideDTO the rideDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rideDTO, or with status 400 (Bad Request) if the ride has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rides")
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO) throws URISyntaxException {
        log.debug("REST request to save Ride : {}", rideDTO);
        if (rideDTO.getId() != null) {
            throw new BadRequestAlertException("A new ride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RideDTO result = rideService.save(rideDTO);
        return ResponseEntity.created(new URI("/api/rides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rides : Updates an existing ride.
     *
     * @param rideDTO the rideDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rideDTO,
     * or with status 400 (Bad Request) if the rideDTO is not valid,
     * or with status 500 (Internal Server Error) if the rideDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rides")
    public ResponseEntity<RideDTO> updateRide(@RequestBody RideDTO rideDTO) throws URISyntaxException {
        log.debug("REST request to update Ride : {}", rideDTO);
        if (rideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RideDTO result = rideService.save(rideDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rideDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rides : get all the rides.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rides in body
     */
    @GetMapping("/rides")
    public ResponseEntity<List<RideDTO>> getAllRides(Pageable pageable) {
        log.debug("REST request to get a page of Rides");
        Page<RideDTO> page = rideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rides");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /rides/:id : get the "id" ride.
     *
     * @param id the id of the rideDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rideDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rides/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Long id) {
        log.debug("REST request to get Ride : {}", id);
        Optional<RideDTO> rideDTO = rideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rideDTO);
    }

    /**
     * DELETE  /rides/:id : delete the "id" ride.
     *
     * @param id the id of the rideDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rides/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        log.debug("REST request to delete Ride : {}", id);
        rideService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rides?query=:query : search for the ride corresponding
     * to the query.
     *
     * @param query the query of the ride search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rides")
    public ResponseEntity<List<RideDTO>> searchRides(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Rides for query {}", query);
        Page<RideDTO> page = rideService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rides");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
