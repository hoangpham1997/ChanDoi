package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.AutoPrinciple;
import vn.softdreams.ebweb.service.AutoPrincipleService;
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
 * REST controller for managing AutoPrinciple.
 */
@RestController
@RequestMapping("/api")
public class AutoPrincipleResource {

    private final Logger log = LoggerFactory.getLogger(AutoPrincipleResource.class);

    private static final String ENTITY_NAME = "autoPrinciple";

    private final AutoPrincipleService autoPrincipleService;

    public AutoPrincipleResource(AutoPrincipleService autoPrincipleService) {
        this.autoPrincipleService = autoPrincipleService;
    }

    /**
     * POST  /auto-principles : Create a new autoPrinciple.
     *
     * @param autoPrinciple the autoPrinciple to create
     * @return the ResponseEntity with status 201 (Created) and with body the new autoPrinciple, or with status 400 (Bad Request) if the autoPrinciple has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/auto-principles")
    @Timed
    public ResponseEntity<AutoPrinciple> createAutoPrinciple(@Valid @RequestBody AutoPrinciple autoPrinciple) throws URISyntaxException {
        log.debug("REST request to save AutoPrinciple : {}", autoPrinciple);
        if (autoPrinciple.getId() != null) {
            throw new BadRequestAlertException("A new autoPrinciple cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoPrinciple result = autoPrincipleService.save(autoPrinciple);
        return ResponseEntity.created(new URI("/api/auto-principles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auto-principles : Updates an existing autoPrinciple.
     *
     * @param autoPrinciple the autoPrinciple to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated autoPrinciple,
     * or with status 400 (Bad Request) if the autoPrinciple is not valid,
     * or with status 500 (Internal Server Error) if the autoPrinciple couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/auto-principles")
    @Timed
    public ResponseEntity<AutoPrinciple> updateAutoPrinciple(@Valid @RequestBody AutoPrinciple autoPrinciple) throws URISyntaxException {
        log.debug("REST request to update AutoPrinciple : {}", autoPrinciple);
        if (autoPrinciple.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoPrinciple result = autoPrincipleService.save(autoPrinciple);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, autoPrinciple.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auto-principles : get all the autoPrinciples.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of autoPrinciples in body
     */
    @GetMapping("/auto-principles")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> getAllAutoPrinciples(Pageable pageable) {
        log.debug("REST request to get a page of AutoPrinciples");
        Page<AutoPrinciple> page = autoPrincipleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auto-principles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /auto-principles : get all the autoPrinciples.
     * add by namnnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of autoPrinciples in body
     */
    @GetMapping("/auto-principles/pageable-all-auto-principle")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> pageableAllAutoPrinciples(Pageable pageable) {
        log.debug("REST request to get a page of AutoPrinciple");
        Page<AutoPrinciple> page = autoPrincipleService.pageableAllAutoPrinciples(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auto-principles/pageable-all-auto-principle");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/auto-principles/getAllAutoPrinciples")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> getAllAutoPrinciples() {
        log.debug("REST request to get a page of AutoPrinciples");
        Page<AutoPrinciple> page = autoPrincipleService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auto-principles/getAllAutoPrinciples");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /auto-principles/:id : get the "id" autoPrinciple.
     *
     * @param id the id of the autoPrinciple to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the autoPrinciple, or with status 404 (Not Found)
     */
    @GetMapping("/auto-principles/{id}")
    @Timed
    public ResponseEntity<AutoPrinciple> getAutoPrinciple(@PathVariable UUID id) {
        log.debug("REST request to get AutoPrinciple : {}", id);
        Optional<AutoPrinciple> autoPrinciple = autoPrincipleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoPrinciple);
    }

    /**
     * DELETE  /auto-principles/:id : delete the "id" autoPrinciple.
     *
     * @param id the id of the autoPrinciple to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/auto-principles/{id}")
    @Timed
    public ResponseEntity<Void> deleteAutoPrinciple(@PathVariable UUID id) {
        log.debug("REST request to delete AutoPrinciple : {}", id);
        autoPrincipleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/auto-principles/find-all-auto-principle-active-companyid")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> getAllAutoPrincipleActiveCompanyID() {
        log.debug("REST request to get a page of AutoPrinciple");
        List<AutoPrinciple> page = autoPrincipleService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/auto-principles/find-all-auto-principle-by-companyid")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> getAllAutoPrincipleByCompanyID() {
        log.debug("REST request to get a page of AutoPrinciple");
        List<AutoPrinciple> page = autoPrincipleService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/auto-principles/type-and-companyid")
    @Timed
    public ResponseEntity<List<AutoPrinciple>> getByTypeAndCompanyID(@RequestParam(required = false) Integer type) {
        log.debug("REST request to get a page of getByTypeAndCompanyID");
        List<AutoPrinciple> page = autoPrincipleService.getByTypeAndCompanyID(type);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
