package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.FixedAssetAccessories;
import vn.softdreams.ebweb.service.FixedAssetAccessoriesService;
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
 * REST controller for managing FixedAssetAccessories.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetAccessoriesResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAccessoriesResource.class);

    private static final String ENTITY_NAME = "fixedAssetAccessories";

    private final FixedAssetAccessoriesService fixedAssetAccessoriesService;

    public FixedAssetAccessoriesResource(FixedAssetAccessoriesService fixedAssetAccessoriesService) {
        this.fixedAssetAccessoriesService = fixedAssetAccessoriesService;
    }

    /**
     * POST  /fixed-asset-accessories : Create a new fixedAssetAccessories.
     *
     * @param fixedAssetAccessories the fixedAssetAccessories to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedAssetAccessories, or with status 400 (Bad Request) if the fixedAssetAccessories has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fixed-asset-accessories")
    @Timed
    public ResponseEntity<FixedAssetAccessories> createFixedAssetAccessories(@Valid @RequestBody FixedAssetAccessories fixedAssetAccessories) throws URISyntaxException {
        log.debug("REST request to save FixedAssetAccessories : {}", fixedAssetAccessories);
        if (fixedAssetAccessories.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetAccessories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetAccessories result = fixedAssetAccessoriesService.save(fixedAssetAccessories);
        return ResponseEntity.created(new URI("/api/fixed-asset-accessories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-asset-accessories : Updates an existing fixedAssetAccessories.
     *
     * @param fixedAssetAccessories the fixedAssetAccessories to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedAssetAccessories,
     * or with status 400 (Bad Request) if the fixedAssetAccessories is not valid,
     * or with status 500 (Internal Server Error) if the fixedAssetAccessories couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fixed-asset-accessories")
    @Timed
    public ResponseEntity<FixedAssetAccessories> updateFixedAssetAccessories(@Valid @RequestBody FixedAssetAccessories fixedAssetAccessories) throws URISyntaxException {
        log.debug("REST request to update FixedAssetAccessories : {}", fixedAssetAccessories);
        if (fixedAssetAccessories.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAssetAccessories result = fixedAssetAccessoriesService.save(fixedAssetAccessories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fixedAssetAccessories.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-asset-accessories : get all the fixedAssetAccessories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedAssetAccessories in body
     */
    @GetMapping("/fixed-asset-accessories")
    @Timed
    public ResponseEntity<List<FixedAssetAccessories>> getAllFixedAssetAccessories(Pageable pageable) {
        log.debug("REST request to get a page of FixedAssetAccessories");
        Page<FixedAssetAccessories> page = fixedAssetAccessoriesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-asset-accessories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-asset-accessories/:id : get the "id" fixedAssetAccessories.
     *
     * @param id the id of the fixedAssetAccessories to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedAssetAccessories, or with status 404 (Not Found)
     */
    @GetMapping("/fixed-asset-accessories/{id}")
    @Timed
    public ResponseEntity<FixedAssetAccessories> getFixedAssetAccessories(@PathVariable UUID id) {
        log.debug("REST request to get FixedAssetAccessories : {}", id);
        Optional<FixedAssetAccessories> fixedAssetAccessories = fixedAssetAccessoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetAccessories);
    }

    /**
     * DELETE  /fixed-asset-accessories/:id : delete the "id" fixedAssetAccessories.
     *
     * @param id the id of the fixedAssetAccessories to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fixed-asset-accessories/{id}")
    @Timed
    public ResponseEntity<Void> deleteFixedAssetAccessories(@PathVariable UUID id) {
        log.debug("REST request to delete FixedAssetAccessories : {}", id);
        fixedAssetAccessoriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
