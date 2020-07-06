package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.service.TIAllocationAllocatedService;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing TIAllocationAllocated.
 */
@RestController
@RequestMapping("/api")
public class TIAllocationAllocatedResource {

    private final Logger log = LoggerFactory.getLogger(TIAllocationAllocatedResource.class);

    private static final String ENTITY_NAME = "tIAllocationAllocated";

    private final TIAllocationAllocatedService tIAllocationAllocatedService;

    public TIAllocationAllocatedResource(TIAllocationAllocatedService tIAllocationAllocatedService) {
        this.tIAllocationAllocatedService = tIAllocationAllocatedService;
    }

    /**
     * POST  /t-i-allocation-allocateds : Create a new tIAllocationAllocated.
     *
     * @param tIAllocationAllocated the tIAllocationAllocated to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAllocationAllocated, or with status 400 (Bad Request) if the tIAllocationAllocated has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-allocation-allocateds")
    @Timed
    public ResponseEntity<TIAllocationAllocated> createTIAllocationAllocated(@Valid @RequestBody TIAllocationAllocated tIAllocationAllocated) throws URISyntaxException {
        log.debug("REST request to save TIAllocationAllocated : {}", tIAllocationAllocated);
        if (tIAllocationAllocated.getId() != null) {
            throw new BadRequestAlertException("A new tIAllocationAllocated cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAllocationAllocated result = tIAllocationAllocatedService.save(tIAllocationAllocated);
        return ResponseEntity.created(new URI("/api/t-i-allocation-allocateds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-allocation-allocateds : Updates an existing tIAllocationAllocated.
     *
     * @param tIAllocationAllocated the tIAllocationAllocated to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAllocationAllocated,
     * or with status 400 (Bad Request) if the tIAllocationAllocated is not valid,
     * or with status 500 (Internal Server Error) if the tIAllocationAllocated couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-allocation-allocateds")
    @Timed
    public ResponseEntity<TIAllocationAllocated> updateTIAllocationAllocated(@Valid @RequestBody TIAllocationAllocated tIAllocationAllocated) throws URISyntaxException {
        log.debug("REST request to update TIAllocationAllocated : {}", tIAllocationAllocated);
        if (tIAllocationAllocated.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAllocationAllocated result = tIAllocationAllocatedService.save(tIAllocationAllocated);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAllocationAllocated.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-allocation-allocateds : get all the tIAllocationAllocateds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAllocationAllocateds in body
     */
    @GetMapping("/t-i-allocation-allocateds")
    @Timed
    public ResponseEntity<List<TIAllocationAllocated>> getAllTIAllocationAllocateds(Pageable pageable) {
        log.debug("REST request to get a page of TIAllocationAllocateds");
        Page<TIAllocationAllocated> page = tIAllocationAllocatedService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-allocation-allocateds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-allocation-allocateds/:id : get the "id" tIAllocationAllocated.
     *
     * @param id the id of the tIAllocationAllocated to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAllocationAllocated, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-allocation-allocateds/{id}")
    @Timed
    public ResponseEntity<TIAllocationAllocated> getTIAllocationAllocated(@PathVariable UUID id) {
        log.debug("REST request to get TIAllocationAllocated : {}", id);
        Optional<TIAllocationAllocated> tIAllocationAllocated = tIAllocationAllocatedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAllocationAllocated);
    }

    /**
     * DELETE  /t-i-allocation-allocateds/:id : delete the "id" tIAllocationAllocated.
     *
     * @param id the id of the tIAllocationAllocated to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-allocation-allocateds/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAllocationAllocated(@PathVariable UUID id) {
        log.debug("REST request to delete TIAllocationAllocated : {}", id);
        tIAllocationAllocatedService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
