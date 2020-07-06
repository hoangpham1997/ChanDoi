package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.service.MaterialGoodsCategoryService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsCategorySaveDTO;
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
 * REST controller for managing MaterialGoodsCategory.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsCategoryResource.class);

    private static final String ENTITY_NAME = "materialGoodsCategory";

    private final MaterialGoodsCategoryService materialGoodsCategoryService;

    public MaterialGoodsCategoryResource(MaterialGoodsCategoryService materialGoodsCategoryService) {
        this.materialGoodsCategoryService = materialGoodsCategoryService;
    }

    /**
     * POST  /material-goods-categories : Create a new materialGoodsCategory.
     *
     * @param materialGoodsCategory the materialGoodsCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsCategory, or with status 400 (Bad Request) if the materialGoodsCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-categories")
    @Timed
    public ResponseEntity<MaterialGoodsCategory> createMaterialGoodsCategory(@Valid @RequestBody MaterialGoodsCategory materialGoodsCategory) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsCategory : {}", materialGoodsCategory);
        if (materialGoodsCategory.getId() != null) {
            throw new BadRequestAlertException("A new statisticsCode cannot already have an ID", ENTITY_NAME, "idexists");
        }

        MaterialGoodsCategory result = materialGoodsCategoryService.save(materialGoodsCategory);
        return ResponseEntity.created(new URI("/api/material-goods-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /material-goods-categories : Updates an existing materialGoodsCategory.
     *
     * @param materialGoodsCategory the materialGoodsCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsCategory,
     * or with status 400 (Bad Request) if the materialGoodsCategory is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-categories")
    @Timed
    public ResponseEntity<MaterialGoodsCategory> updateMaterialGoodsCategory(@Valid @RequestBody MaterialGoodsCategory materialGoodsCategory) throws URISyntaxException {
        log.debug("REST request to update Material Goods Category : {}", materialGoodsCategory);
        if (materialGoodsCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsCategory result = materialGoodsCategoryService.save(materialGoodsCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-categories : get all the materialGoodsCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsCategories in body
     */
    @GetMapping("/material-goods-categories")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> getAllMaterialGoodsCategories(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsCategories");
        Page<MaterialGoodsCategory> page = materialGoodsCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-categories : get all the material-goods-categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/material-goods-categories/pageable-all-material-goods-categories")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> pageableAllMaterialGoodsCategories(Pageable pageable) {
        log.debug("REST request to get a page of AccountingObjects");
        Page<MaterialGoodsCategory> page = materialGoodsCategoryService.pageableAllMaterialGoodsCategories(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-categories/pageable-all-material-goods-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/material-goods-categories/find-all-material-goods-category-by-company-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> getAllMaterialGoodsCategoryByCompanyID(@RequestParam(required = false) Boolean similarBranch) {
        log.debug("REST request to get a page of MaterialGoodsCategory");
        List<MaterialGoodsCategory> page = materialGoodsCategoryService.getAllMaterialGoodsCategoryByCompanyID(similarBranch);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-categories/get-for-report")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> getMaterialGoodsCategoryForReport(@RequestParam(required = false) UUID companyID, @RequestParam(required = false) Boolean similarBranch) {
        log.debug("REST request to get a page of MaterialGoodsCategory");
        List<MaterialGoodsCategory> page = materialGoodsCategoryService.getMaterialGoodsCategoryForReport(companyID, similarBranch);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-categories/find-all-material-goods-categories-active-companyid-except-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> getAllAccountListActiveExceptID(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of AccountList");
        List<MaterialGoodsCategory> page = materialGoodsCategoryService.findAllExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-categories/:id : get the "id" materialGoodsCategory.
     *
     * @param id the id of the materialGoodsCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsCategory, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-categories/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsCategory> getMaterialGoodsCategory(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsCategory : {}", id);
        Optional<MaterialGoodsCategory> materialGoodsCategory = materialGoodsCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsCategory);
    }

    /**
     * DELETE  /material-goods-categories/:id : delete the "id" materialGoodsCategory.
     *
     * @param id the id of the materialGoodsCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsCategory(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsCategory : {}", id);
        materialGoodsCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @author congnd
     * @param similarBranch
     * @param companyID
     * @return
     */
    @GetMapping("/material-goods-categories/find-all-material-goods-category-by-company-id-similar-branch")
    @Timed
    public ResponseEntity<List<MaterialGoodsCategory>> getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                                                                              @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of MaterialGoodsCategory");
        List<MaterialGoodsCategory> page = materialGoodsCategoryService.getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
