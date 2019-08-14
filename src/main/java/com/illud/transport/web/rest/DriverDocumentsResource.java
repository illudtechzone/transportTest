package com.illud.transport.web.rest;
import com.illud.transport.service.DriverDocumentsService;
import com.illud.transport.web.rest.errors.BadRequestAlertException;
import com.illud.transport.web.rest.util.HeaderUtil;
import com.illud.transport.web.rest.util.PaginationUtil;
import com.illud.transport.service.dto.DriverDocumentsDTO;
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
 * REST controller for managing DriverDocuments.
 */
@RestController
@RequestMapping("/api")
public class DriverDocumentsResource {

    private final Logger log = LoggerFactory.getLogger(DriverDocumentsResource.class);

    private static final String ENTITY_NAME = "transportDriverDocuments";

    private final DriverDocumentsService driverDocumentsService;

    public DriverDocumentsResource(DriverDocumentsService driverDocumentsService) {
        this.driverDocumentsService = driverDocumentsService;
    }

    /**
     * POST  /driver-documents : Create a new driverDocuments.
     *
     * @param driverDocumentsDTO the driverDocumentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new driverDocumentsDTO, or with status 400 (Bad Request) if the driverDocuments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/driver-documents")
    public ResponseEntity<DriverDocumentsDTO> createDriverDocuments(@RequestBody DriverDocumentsDTO driverDocumentsDTO) throws URISyntaxException {
        log.debug("REST request to save DriverDocuments : {}", driverDocumentsDTO);
        if (driverDocumentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new driverDocuments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DriverDocumentsDTO result = driverDocumentsService.save(driverDocumentsDTO);
        return ResponseEntity.created(new URI("/api/driver-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /driver-documents : Updates an existing driverDocuments.
     *
     * @param driverDocumentsDTO the driverDocumentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated driverDocumentsDTO,
     * or with status 400 (Bad Request) if the driverDocumentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the driverDocumentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/driver-documents")
    public ResponseEntity<DriverDocumentsDTO> updateDriverDocuments(@RequestBody DriverDocumentsDTO driverDocumentsDTO) throws URISyntaxException {
        log.debug("REST request to update DriverDocuments : {}", driverDocumentsDTO);
        if (driverDocumentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DriverDocumentsDTO result = driverDocumentsService.save(driverDocumentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, driverDocumentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /driver-documents : get all the driverDocuments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of driverDocuments in body
     */
    @GetMapping("/driver-documents")
    public ResponseEntity<List<DriverDocumentsDTO>> getAllDriverDocuments(Pageable pageable) {
        log.debug("REST request to get a page of DriverDocuments");
        Page<DriverDocumentsDTO> page = driverDocumentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/driver-documents");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /driver-documents/:id : get the "id" driverDocuments.
     *
     * @param id the id of the driverDocumentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the driverDocumentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/driver-documents/{id}")
    public ResponseEntity<DriverDocumentsDTO> getDriverDocuments(@PathVariable Long id) {
        log.debug("REST request to get DriverDocuments : {}", id);
        Optional<DriverDocumentsDTO> driverDocumentsDTO = driverDocumentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driverDocumentsDTO);
    }

    /**
     * DELETE  /driver-documents/:id : delete the "id" driverDocuments.
     *
     * @param id the id of the driverDocumentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/driver-documents/{id}")
    public ResponseEntity<Void> deleteDriverDocuments(@PathVariable Long id) {
        log.debug("REST request to delete DriverDocuments : {}", id);
        driverDocumentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/driver-documents?query=:query : search for the driverDocuments corresponding
     * to the query.
     *
     * @param query the query of the driverDocuments search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/driver-documents")
    public ResponseEntity<List<DriverDocumentsDTO>> searchDriverDocuments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DriverDocuments for query {}", query);
        Page<DriverDocumentsDTO> page = driverDocumentsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/driver-documents");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
