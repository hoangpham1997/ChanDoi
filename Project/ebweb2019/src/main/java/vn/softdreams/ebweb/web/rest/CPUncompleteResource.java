package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPUncomplete;
import vn.softdreams.ebweb.service.CPUncompleteService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CPUncomplete.
 */
@RestController
@RequestMapping("/api")
public class CPUncompleteResource {

    private final Logger log = LoggerFactory.getLogger(CPUncompleteResource.class);

    private static final String ENTITY_NAME = "cPUncomplete";

    private final CPUncompleteService cPUncompleteService;

    public CPUncompleteResource(CPUncompleteService cPUncompleteService) {
        this.cPUncompleteService = cPUncompleteService;
    }

    /**
     * POST  /cp-uncompletes : Create a new cPUncomplete.
     *
     * @param cPUncomplete the cPUncomplete to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPUncomplete, or with status 400 (Bad Request) if the cPUncomplete has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-uncompletes")
    @Timed
    public ResponseEntity<CPUncomplete> createCPUncomplete(@RequestBody CPUncomplete cPUncomplete) throws URISyntaxException {
        log.debug("REST request to save CPUncomplete : {}", cPUncomplete);
        if (cPUncomplete.getId() != null) {
            throw new BadRequestAlertException("A new cPUncomplete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPUncomplete result = cPUncompleteService.save(cPUncomplete);
        return ResponseEntity.created(new URI("/api/cp-uncompletes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-uncompletes : Updates an existing cPUncomplete.
     *
     * @param cPUncomplete the cPUncomplete to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPUncomplete,
     * or with status 400 (Bad Request) if the cPUncomplete is not valid,
     * or with status 500 (Internal Server Error) if the cPUncomplete couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-uncompletes")
    @Timed
    public ResponseEntity<CPUncomplete> updateCPUncomplete(@RequestBody CPUncomplete cPUncomplete) throws URISyntaxException {
        log.debug("REST request to update CPUncomplete : {}", cPUncomplete);
        if (cPUncomplete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPUncomplete result = cPUncompleteService.save(cPUncomplete);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPUncomplete.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-uncompletes : get all the cPUncompletes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPUncompletes in body
     */
    @GetMapping("/cp-uncompletes")
    @Timed
    public ResponseEntity<List<CPUncomplete>> getAllCPUncompletes(Pageable pageable) {
        log.debug("REST request to get a page of CPUncompletes");
        Page<CPUncomplete> page = cPUncompleteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-uncompletes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-uncompletes/:id : get the "id" cPUncomplete.
     *
     * @param id the id of the cPUncomplete to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPUncomplete, or with status 404 (Not Found)
     */
    @GetMapping("/cp-uncompletes/{id}")
    @Timed
    public ResponseEntity<CPUncomplete> getCPUncomplete(@PathVariable Long id) {
        log.debug("REST request to get CPUncomplete : {}", id);
        Optional<CPUncomplete> cPUncomplete = cPUncompleteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPUncomplete);
    }

    /**
     * DELETE  /cp-uncompletes/:id : delete the "id" cPUncomplete.
     *
     * @param id the id of the cPUncomplete to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-uncompletes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPUncomplete(@PathVariable Long id) {
        log.debug("REST request to delete CPUncomplete : {}", id);
        cPUncompleteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
