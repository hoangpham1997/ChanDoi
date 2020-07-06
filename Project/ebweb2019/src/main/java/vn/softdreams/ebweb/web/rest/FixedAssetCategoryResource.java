package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.FixedAssetCategory;
import vn.softdreams.ebweb.service.FixedAssetCategoryService;
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
 * REST controller for managing FixedAssetCategory.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetCategoryResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetCategoryResource.class);

    private static final String ENTITY_NAME = "fixedAssetCategory";

    private final FixedAssetCategoryService fixedAssetCategoryService;

    public FixedAssetCategoryResource(FixedAssetCategoryService fixedAssetCategoryService) {
        this.fixedAssetCategoryService = fixedAssetCategoryService;
    }

    /**
     * POST  /fixed-asset-categories : Create a new fixedAssetCategory.
     *
     * @param fixedAssetCategory the fixedAssetCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedAssetCategory, or with status 400 (Bad Request) if the fixedAssetCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fixed-asset-categories")
    @Timed
    public ResponseEntity<FixedAssetCategory> createFixedAssetCategory(@Valid @RequestBody FixedAssetCategory fixedAssetCategory) throws URISyntaxException {
        log.debug("REST request to save FixedAssetCategory : {}", fixedAssetCategory);
        if (fixedAssetCategory.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetCategory result = fixedAssetCategoryService.save(fixedAssetCategory);
        fixedAssetCategory.setGrade(1);
        return ResponseEntity.created(new URI("/api/fixed-asset-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-asset-categories : Updates an existing fixedAssetCategory.
     *
     * @param fixedAssetCategory the fixedAssetCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedAssetCategory,
     * or with status 400 (Bad Request) if the fixedAssetCategory is not valid,
     * or with status 500 (Internal Server Error) if the fixedAssetCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fixed-asset-categories")
    @Timed
    public ResponseEntity<FixedAssetCategory> updateFixedAssetCategory(@Valid @RequestBody FixedAssetCategory fixedAssetCategory) throws URISyntaxException {
        log.debug("REST request to update FixedAssetCategory : {}", fixedAssetCategory);
        if (fixedAssetCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAssetCategory result = fixedAssetCategoryService.save(fixedAssetCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fixedAssetCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-asset-categories : get all the fixedAssetCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedAssetCategories in body
     */
    @GetMapping("/fixed-asset-categories")
    @Timed
    public ResponseEntity<List<FixedAssetCategory>> getAllFixedAssetCategories(Pageable pageable) {
        log.debug("REST request to get a page of FixedAssetCategories");
        Page<FixedAssetCategory> page = fixedAssetCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-asset-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-asset-categories/:id : get the "id" fixedAssetCategory.
     *
     * @param id the id of the fixedAssetCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedAssetCategory, or with status 404 (Not Found)
     */
    @GetMapping("/fixed-asset-categories/{id}")
    @Timed
    public ResponseEntity<FixedAssetCategory> getFixedAssetCategory(@PathVariable UUID id) {
        log.debug("REST request to get FixedAssetCategory : {}", id);
        Optional<FixedAssetCategory> fixedAssetCategory = fixedAssetCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetCategory);
    }

    /**
     * DELETE  /fixed-asset-categories/:id : delete the "id" fixedAssetCategory.
     *
     * @param id the id of the fixedAssetCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fixed-asset-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteFixedAssetCategory(@PathVariable UUID id) {
        log.debug("REST request to delete FixedAssetCategory : {}", id);
        fixedAssetCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
