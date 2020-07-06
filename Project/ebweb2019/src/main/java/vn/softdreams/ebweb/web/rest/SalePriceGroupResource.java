package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SalePriceGroup;
import vn.softdreams.ebweb.service.SalePriceGroupService;
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
 * REST controller for managing SalePriceGroup.
 */
@RestController
@RequestMapping("/api")
public class SalePriceGroupResource {

    private final Logger log = LoggerFactory.getLogger(SalePriceGroupResource.class);

    private static final String ENTITY_NAME = "salePriceGroup";

    private final SalePriceGroupService salePriceGroupService;

    public SalePriceGroupResource(SalePriceGroupService salePriceGroupService) {
        this.salePriceGroupService = salePriceGroupService;
    }

    /**
     * POST  /sale-price-groups : Create a new salePriceGroup.
     *
     * @param salePriceGroup the salePriceGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salePriceGroup, or with status 400 (Bad Request) if the salePriceGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-price-groups")
    @Timed
    public ResponseEntity<SalePriceGroup> createSalePriceGroup(@Valid @RequestBody SalePriceGroup salePriceGroup) throws URISyntaxException {
        log.debug("REST request to save SalePriceGroup : {}", salePriceGroup);
        if (salePriceGroup.getId() != null) {
            throw new BadRequestAlertException("A new salePriceGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalePriceGroup result = salePriceGroupService.save(salePriceGroup);
        return ResponseEntity.created(new URI("/api/sale-price-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-price-groups : Updates an existing salePriceGroup.
     *
     * @param salePriceGroup the salePriceGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salePriceGroup,
     * or with status 400 (Bad Request) if the salePriceGroup is not valid,
     * or with status 500 (Internal Server Error) if the salePriceGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-price-groups")
    @Timed
    public ResponseEntity<SalePriceGroup> updateSalePriceGroup(@Valid @RequestBody SalePriceGroup salePriceGroup) throws URISyntaxException {
        log.debug("REST request to update SalePriceGroup : {}", salePriceGroup);
        if (salePriceGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalePriceGroup result = salePriceGroupService.save(salePriceGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salePriceGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-price-groups : get all the salePriceGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of salePriceGroups in body
     */
    @GetMapping("/sale-price-groups")
    @Timed
    public ResponseEntity<List<SalePriceGroup>> getAllSalePriceGroups(Pageable pageable) {
        log.debug("REST request to get a page of SalePriceGroups");
        Page<SalePriceGroup> page = salePriceGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-price-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sale-price-groups/:id : get the "id" salePriceGroup.
     *
     * @param id the id of the salePriceGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salePriceGroup, or with status 404 (Not Found)
     */
    @GetMapping("/sale-price-groups/{id}")
    @Timed
    public ResponseEntity<SalePriceGroup> getSalePriceGroup(@PathVariable UUID id) {
        log.debug("REST request to get SalePriceGroup : {}", id);
        Optional<SalePriceGroup> salePriceGroup = salePriceGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salePriceGroup);
    }

    /**
     * DELETE  /sale-price-groups/:id : delete the "id" salePriceGroup.
     *
     * @param id the id of the salePriceGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-price-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalePriceGroup(@PathVariable UUID id) {
        log.debug("REST request to delete SalePriceGroup : {}", id);
        salePriceGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
