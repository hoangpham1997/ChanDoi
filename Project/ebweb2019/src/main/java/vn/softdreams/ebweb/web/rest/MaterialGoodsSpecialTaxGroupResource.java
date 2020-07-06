package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;
import vn.softdreams.ebweb.service.MaterialGoodsSpecialTaxGroupService;
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
 * REST controller for managing MaterialGoodsSpecialTaxGroup.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsSpecialTaxGroupResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecialTaxGroupResource.class);

    private static final String ENTITY_NAME = "materialGoodsSpecialTaxGroup";

    private final MaterialGoodsSpecialTaxGroupService materialGoodsSpecialTaxGroupService;

    public MaterialGoodsSpecialTaxGroupResource(MaterialGoodsSpecialTaxGroupService materialGoodsSpecialTaxGroupService) {
        this.materialGoodsSpecialTaxGroupService = materialGoodsSpecialTaxGroupService;
    }

    /**
     * POST  /material-goods-special-tax-groups : Create a new materialGoodsSpecialTaxGroup.
     *
     * @param materialGoodsSpecialTaxGroup the materialGoodsSpecialTaxGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsSpecialTaxGroup, or with status 400 (Bad Request) if the materialGoodsSpecialTaxGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-special-tax-groups")
    @Timed
    public ResponseEntity<MaterialGoodsSpecialTaxGroup> createMaterialGoodsSpecialTaxGroup(@Valid @RequestBody MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsSpecialTaxGroup : {}", materialGoodsSpecialTaxGroup);
        if (materialGoodsSpecialTaxGroup.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsSpecialTaxGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsSpecialTaxGroup result = materialGoodsSpecialTaxGroupService.save(materialGoodsSpecialTaxGroup);
        return ResponseEntity.created(new URI("/api/material-goods-special-tax-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-special-tax-groups : Updates an existing materialGoodsSpecialTaxGroup.
     *
     * @param materialGoodsSpecialTaxGroup the materialGoodsSpecialTaxGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsSpecialTaxGroup,
     * or with status 400 (Bad Request) if the materialGoodsSpecialTaxGroup is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsSpecialTaxGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-special-tax-groups")
    @Timed
    public ResponseEntity<MaterialGoodsSpecialTaxGroup> updateMaterialGoodsSpecialTaxGroup(@Valid @RequestBody MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsSpecialTaxGroup : {}", materialGoodsSpecialTaxGroup);
        if (materialGoodsSpecialTaxGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsSpecialTaxGroup result = materialGoodsSpecialTaxGroupService.save(materialGoodsSpecialTaxGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsSpecialTaxGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-special-tax-groups : get all the materialGoodsSpecialTaxGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsSpecialTaxGroups in body
     */
    @GetMapping("/material-goods-special-tax-groups")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllMaterialGoodsSpecialTaxGroups(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroups");
        Page<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-special-tax-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/pageable-all-material-goods-special-tax-group")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> pageableAllBank(Pageable pageable) {
        log.debug("REST request to get a page of Banks");
        Page<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.pageableAllMaterialGoodsSpecialTaxGroup(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-special-tax-groups/pageable-all-material-goods-special-tax-group");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-active-company-id-except-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllAccountListActiveExceptID(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of AccountList");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAllExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-one-material-goods-special-tax-group-active-company-id-except-id")
    @Timed
    public ResponseEntity<MaterialGoodsSpecialTaxGroup> getAllAccountListActiveExceptID1(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of AccountList");
        MaterialGoodsSpecialTaxGroup page = materialGoodsSpecialTaxGroupService.findOneExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/get-material-goods-special-tax-groups/{id}")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getMaterialGoodsSpecialTaxGroupsOne(@PathVariable UUID id) {
        log.debug("REST request to get a combobox list of StatisticsCodes", id);
        List<MaterialGoodsSpecialTaxGroup> listCbx = materialGoodsSpecialTaxGroupService.findMaterialGoodsSpecialTaxGroupsOne(id);
        return new ResponseEntity<>(listCbx, HttpStatus.OK);
    }


    /**
     * GET  /material-goods-special-tax-groups/:id : get the "id" materialGoodsSpecialTaxGroup.
     *
     * @param id the id of the materialGoodsSpecialTaxGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsSpecialTaxGroup, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-special-tax-groups/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsSpecialTaxGroup> getMaterialGoodsSpecialTaxGroup(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsSpecialTaxGroup : {}", id);
        Optional<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsSpecialTaxGroup);
    }

    /**
     * DELETE  /material-goods-special-tax-groups/:id : delete the "id" materialGoodsSpecialTaxGroup.
     *
     * @param id the id of the materialGoodsSpecialTaxGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-special-tax-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsSpecialTaxGroup(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsSpecialTaxGroup : {}", id);
        materialGoodsSpecialTaxGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-by-companyid")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllMaterialGoodsSpecialTaxGroupCompanyID() {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAllMaterialGoodsSpecialTaxGroupByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-by-company-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllMaterialGoodsSpecialTaxGroupByCompanyID() {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.getAllMaterialGoodsSpecialTaxGroupByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-active-companyid")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllAccountListActiveCompanyID() {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-companyid")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllAccountListCompanyID() {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAllAccountLists();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/find-all-material-goods-special-tax-group-company-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllMaterialGoodsSpecialTaxGroupID(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> page = materialGoodsSpecialTaxGroupService.findAllExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-special-tax-groups/get-all-active")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecialTaxGroup>> getAllActiveStatisticsCodes() {
        log.debug("REST request to get a list of active MaterialGoodsSpecialTaxGroup");
        List<MaterialGoodsSpecialTaxGroup> list = materialGoodsSpecialTaxGroupService.findAllActive();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
