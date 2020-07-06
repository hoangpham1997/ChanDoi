package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.FixedAssetAllocation;
import vn.softdreams.ebweb.service.FixedAssetAllocationService;
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
 * REST controller for managing FixedAssetAllocation.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetAllocationResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAllocationResource.class);

    private static final String ENTITY_NAME = "fixedAssetAllocation";

    private final FixedAssetAllocationService fixedAssetAllocationService;

    public FixedAssetAllocationResource(FixedAssetAllocationService fixedAssetAllocationService) {
        this.fixedAssetAllocationService = fixedAssetAllocationService;
    }

    /**
     * POST  /fixed-asset-allocations : Create a new fixedAssetAllocation.
     *
     * @param fixedAssetAllocation the fixedAssetAllocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedAssetAllocation, or with status 400 (Bad Request) if the fixedAssetAllocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fixed-asset-allocations")
    @Timed
    public ResponseEntity<FixedAssetAllocation> createFixedAssetAllocation(@Valid @RequestBody FixedAssetAllocation fixedAssetAllocation) throws URISyntaxException {
        log.debug("REST request to save FixedAssetAllocation : {}", fixedAssetAllocation);
        if (fixedAssetAllocation.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetAllocation result = fixedAssetAllocationService.save(fixedAssetAllocation);
        return ResponseEntity.created(new URI("/api/fixed-asset-allocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-asset-allocations : Updates an existing fixedAssetAllocation.
     *
     * @param fixedAssetAllocation the fixedAssetAllocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedAssetAllocation,
     * or with status 400 (Bad Request) if the fixedAssetAllocation is not valid,
     * or with status 500 (Internal Server Error) if the fixedAssetAllocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fixed-asset-allocations")
    @Timed
    public ResponseEntity<FixedAssetAllocation> updateFixedAssetAllocation(@Valid @RequestBody FixedAssetAllocation fixedAssetAllocation) throws URISyntaxException {
        log.debug("REST request to update FixedAssetAllocation : {}", fixedAssetAllocation);
        if (fixedAssetAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAssetAllocation result = fixedAssetAllocationService.save(fixedAssetAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fixedAssetAllocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-asset-allocations : get all the fixedAssetAllocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedAssetAllocations in body
     */
    @GetMapping("/fixed-asset-allocations")
    @Timed
    public ResponseEntity<List<FixedAssetAllocation>> getAllFixedAssetAllocations(Pageable pageable) {
        log.debug("REST request to get a page of FixedAssetAllocations");
        Page<FixedAssetAllocation> page = fixedAssetAllocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-asset-allocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-asset-allocations/:id : get the "id" fixedAssetAllocation.
     *
     * @param id the id of the fixedAssetAllocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedAssetAllocation, or with status 404 (Not Found)
     */
    @GetMapping("/fixed-asset-allocations/{id}")
    @Timed
    public ResponseEntity<FixedAssetAllocation> getFixedAssetAllocation(@PathVariable UUID id) {
        log.debug("REST request to get FixedAssetAllocation : {}", id);
        Optional<FixedAssetAllocation> fixedAssetAllocation = fixedAssetAllocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetAllocation);
    }

    /**
     * DELETE  /fixed-asset-allocations/:id : delete the "id" fixedAssetAllocation.
     *
     * @param id the id of the fixedAssetAllocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fixed-asset-allocations/{id}")
    @Timed
    public ResponseEntity<Void> deleteFixedAssetAllocation(@PathVariable UUID id) {
        log.debug("REST request to delete FixedAssetAllocation : {}", id);
        fixedAssetAllocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
