package com.illud.transport.web.rest;
import com.illud.transport.service.RiderService;
import com.illud.transport.web.rest.errors.BadRequestAlertException;
import com.illud.transport.web.rest.util.HeaderUtil;
import com.illud.transport.web.rest.util.PaginationUtil;
import com.illud.transport.service.dto.DriverDTO;
import com.illud.transport.service.dto.RiderDTO;
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
 * REST controller for managing Rider.
 */
@RestController
@RequestMapping("/api")
public class RiderResource {

    private final Logger log = LoggerFactory.getLogger(RiderResource.class);

    private static final String ENTITY_NAME = "transportRider";

    private final RiderService riderService;

    public RiderResource(RiderService riderService) {
        this.riderService = riderService;
    }

    /**
     * POST  /riders : Create a new rider.
     *
     * @param riderDTO the riderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new riderDTO, or with status 400 (Bad Request) if the rider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/riders")
    public ResponseEntity<RiderDTO> createRider(@RequestBody RiderDTO riderDTO) throws URISyntaxException {
        log.debug("REST request to save Rider : {}", riderDTO);
        if (riderDTO.getId() != null) {
            throw new BadRequestAlertException("A new rider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RiderDTO result = riderService.save(riderDTO);
        return ResponseEntity.created(new URI("/api/riders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /riders : Updates an existing rider.
     *
     * @param riderDTO the riderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated riderDTO,
     * or with status 400 (Bad Request) if the riderDTO is not valid,
     * or with status 500 (Internal Server Error) if the riderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/riders")
    public ResponseEntity<RiderDTO> updateRider(@RequestBody RiderDTO riderDTO) throws URISyntaxException {
        log.debug("REST request to update Rider : {}", riderDTO);
        if (riderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RiderDTO result = riderService.save(riderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, riderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /riders : get all the riders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of riders in body
     */
    @GetMapping("/riders")
    public ResponseEntity<List<RiderDTO>> getAllRiders(Pageable pageable) {
        log.debug("REST request to get a page of Riders");
        Page<RiderDTO> page = riderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/riders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /riders/:id : get the "id" rider.
     *
     * @param id the id of the riderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the riderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/riders/{id}")
    public ResponseEntity<RiderDTO> getRider(@PathVariable Long id) {
        log.debug("REST request to get Rider : {}", id);
        Optional<RiderDTO> riderDTO = riderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(riderDTO);
    }

    /**
     * DELETE  /riders/:id : delete the "id" rider.
     *
     * @param id the id of the riderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/riders/{id}")
    public ResponseEntity<Void> deleteRider(@PathVariable Long id) {
        log.debug("REST request to delete Rider : {}", id);
        riderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/riders?query=:query : search for the rider corresponding
     * to the query.
     *
     * @param query the query of the rider search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/riders")
    public ResponseEntity<List<RiderDTO>> searchRiders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Riders for query {}", query);
        Page<RiderDTO> page = riderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/riders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * POST  /riders : Create a new rider if not created yet.
     *
     * @param riderDTO the riderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new riderDTO, or with status 400 (Bad Request) if the rider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/create/riders")
    public ResponseEntity<RiderDTO> createriderIfnotExist(@RequestBody RiderDTO riderDTO) throws URISyntaxException {
        		log.debug("REST request to save rider : {}", riderDTO);
        
        		Optional<RiderDTO> result = riderService.createRiderIfnotExist(riderDTO);
        		return   ResponseUtil.wrapOrNotFound(result);
    }

}
