package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.FixedAsset;
import vn.softdreams.ebweb.service.FixedAssetService;
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
 * REST controller for managing FixedAsset.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetResource.class);

    private static final String ENTITY_NAME = "fixedAsset";

    private final FixedAssetService fixedAssetService;

    public FixedAssetResource(FixedAssetService fixedAssetService) {
        this.fixedAssetService = fixedAssetService;
    }

    /**
     * POST  /fixed-assets : Create a new fixedAsset.
     *
     * @param fixedAsset the fixedAsset to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedAsset, or with status 400 (Bad Request) if the fixedAsset has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fixed-assets")
    @Timed
    public ResponseEntity<FixedAsset> createFixedAsset(@Valid @RequestBody FixedAsset fixedAsset) throws URISyntaxException {
        log.debug("REST request to save FixedAsset : {}", fixedAsset);
        if (fixedAsset.getId() != null) {
            throw new BadRequestAlertException("A new fixedAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAsset result = fixedAssetService.save(fixedAsset);
        return ResponseEntity.created(new URI("/api/fixed-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-assets : Updates an existing fixedAsset.
     *
     * @param fixedAsset the fixedAsset to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedAsset,
     * or with status 400 (Bad Request) if the fixedAsset is not valid,
     * or with status 500 (Internal Server Error) if the fixedAsset couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fixed-assets")
    @Timed
    public ResponseEntity<FixedAsset> updateFixedAsset(@Valid @RequestBody FixedAsset fixedAsset) throws URISyntaxException {
        log.debug("REST request to update FixedAsset : {}", fixedAsset);
        if (fixedAsset.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAsset result = fixedAssetService.save(fixedAsset);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fixedAsset.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-assets : get all the fixedAssets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedAssets in body
     */
    @GetMapping("/fixed-assets")
    @Timed
    public ResponseEntity<List<FixedAsset>> getAllFixedAssets(Pageable pageable) {
        log.debug("REST request to get a page of FixedAssets");
        Page<FixedAsset> page = fixedAssetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-assets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-assets/:id : get the "id" fixedAsset.
     *
     * @param id the id of the fixedAsset to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedAsset, or with status 404 (Not Found)
     */
    @GetMapping("/fixed-assets/{id}")
    @Timed
    public ResponseEntity<FixedAsset> getFixedAsset(@PathVariable UUID id) {
        log.debug("REST request to get FixedAsset : {}", id);
        Optional<FixedAsset> fixedAsset = fixedAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAsset);
    }

    /**
     * DELETE  /fixed-assets/:id : delete the "id" fixedAsset.
     *
     * @param id the id of the fixedAsset to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fixed-assets/{id}")
    @Timed
    public ResponseEntity<Void> deleteFixedAsset(@PathVariable UUID id) {
        log.debug("REST request to delete FixedAsset : {}", id);
        fixedAssetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
