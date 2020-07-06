package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsResourceTaxGroup;
import vn.softdreams.ebweb.service.MaterialGoodsResourceTaxGroupService;
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
 * REST controller for managing MaterialGoodsResourceTaxGroup.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsResourceTaxGroupResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsResourceTaxGroupResource.class);

    private static final String ENTITY_NAME = "materialGoodsResourceTaxGroup";

    private final MaterialGoodsResourceTaxGroupService materialGoodsResourceTaxGroupService;

    public MaterialGoodsResourceTaxGroupResource(MaterialGoodsResourceTaxGroupService materialGoodsResourceTaxGroupService) {
        this.materialGoodsResourceTaxGroupService = materialGoodsResourceTaxGroupService;
    }

    /**
     * POST  /material-goods-resource-tax-groups : Create a new materialGoodsResourceTaxGroup.
     *
     * @param materialGoodsResourceTaxGroup the materialGoodsResourceTaxGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsResourceTaxGroup, or with status 400 (Bad Request) if the materialGoodsResourceTaxGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-resource-tax-groups")
    @Timed
    public ResponseEntity<MaterialGoodsResourceTaxGroup> createMaterialGoodsResourceTaxGroup(@Valid @RequestBody MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsResourceTaxGroup : {}", materialGoodsResourceTaxGroup);
        if (materialGoodsResourceTaxGroup.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsResourceTaxGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsResourceTaxGroup result = materialGoodsResourceTaxGroupService.save(materialGoodsResourceTaxGroup);
        return ResponseEntity.created(new URI("/api/material-goods-resource-tax-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-resource-tax-groups : Updates an existing materialGoodsResourceTaxGroup.
     *
     * @param materialGoodsResourceTaxGroup the materialGoodsResourceTaxGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsResourceTaxGroup,
     * or with status 400 (Bad Request) if the materialGoodsResourceTaxGroup is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsResourceTaxGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-resource-tax-groups")
    @Timed
    public ResponseEntity<MaterialGoodsResourceTaxGroup> updateMaterialGoodsResourceTaxGroup(@Valid @RequestBody MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsResourceTaxGroup : {}", materialGoodsResourceTaxGroup);
        if (materialGoodsResourceTaxGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsResourceTaxGroup result = materialGoodsResourceTaxGroupService.save(materialGoodsResourceTaxGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsResourceTaxGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-resource-tax-groups : get all the materialGoodsResourceTaxGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsResourceTaxGroups in body
     */
    @GetMapping("/material-goods-resource-tax-groups")
    @Timed
    public ResponseEntity<List<MaterialGoodsResourceTaxGroup>> getAllMaterialGoodsResourceTaxGroups(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsResourceTaxGroups");
        Page<MaterialGoodsResourceTaxGroup> page = materialGoodsResourceTaxGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-resource-tax-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-resource-tax-groups/:id : get the "id" materialGoodsResourceTaxGroup.
     *
     * @param id the id of the materialGoodsResourceTaxGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsResourceTaxGroup, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-resource-tax-groups/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsResourceTaxGroup> getMaterialGoodsResourceTaxGroup(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsResourceTaxGroup : {}", id);
        Optional<MaterialGoodsResourceTaxGroup> materialGoodsResourceTaxGroup = materialGoodsResourceTaxGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsResourceTaxGroup);
    }

    /**
     * DELETE  /material-goods-resource-tax-groups/:id : delete the "id" materialGoodsResourceTaxGroup.
     *
     * @param id the id of the materialGoodsResourceTaxGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-resource-tax-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsResourceTaxGroup(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsResourceTaxGroup : {}", id);
        materialGoodsResourceTaxGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
