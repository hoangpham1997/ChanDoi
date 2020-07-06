package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecifications;
import vn.softdreams.ebweb.service.MaterialGoodsSpecificationsService;
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
 * REST controller for managing MaterialGoodsSpecifications.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsSpecificationsResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecificationsResource.class);

    private static final String ENTITY_NAME = "materialGoodsSpecifications";

    private final MaterialGoodsSpecificationsService materialGoodsSpecificationsService;

    public MaterialGoodsSpecificationsResource(MaterialGoodsSpecificationsService materialGoodsSpecificationsService) {
        this.materialGoodsSpecificationsService = materialGoodsSpecificationsService;
    }

    /**
     * POST  /material-goods-specifications : Create a new materialGoodsSpecifications.
     *
     * @param materialGoodsSpecifications the materialGoodsSpecifications to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsSpecifications, or with status 400 (Bad Request) if the materialGoodsSpecifications has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-specifications")
    @Timed
    public ResponseEntity<MaterialGoodsSpecifications> createMaterialGoodsSpecifications(@Valid @RequestBody MaterialGoodsSpecifications materialGoodsSpecifications) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsSpecifications : {}", materialGoodsSpecifications);
        if (materialGoodsSpecifications.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsSpecifications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsSpecifications result = materialGoodsSpecificationsService.save(materialGoodsSpecifications);
        return ResponseEntity.created(new URI("/api/material-goods-specifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-specifications : Updates an existing materialGoodsSpecifications.
     *
     * @param materialGoodsSpecifications the materialGoodsSpecifications to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsSpecifications,
     * or with status 400 (Bad Request) if the materialGoodsSpecifications is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsSpecifications couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-specifications")
    @Timed
    public ResponseEntity<MaterialGoodsSpecifications> updateMaterialGoodsSpecifications(@Valid @RequestBody MaterialGoodsSpecifications materialGoodsSpecifications) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsSpecifications : {}", materialGoodsSpecifications);
        if (materialGoodsSpecifications.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsSpecifications result = materialGoodsSpecificationsService.save(materialGoodsSpecifications);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsSpecifications.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-specifications : get all the materialGoodsSpecifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsSpecifications in body
     */
    @GetMapping("/material-goods-specifications")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecifications>> getAllMaterialGoodsSpecifications(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsSpecifications");
        Page<MaterialGoodsSpecifications> page = materialGoodsSpecificationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-specifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-specifications/:id : get the "id" materialGoodsSpecifications.
     *
     * @param id the id of the materialGoodsSpecifications to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsSpecifications, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-specifications/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsSpecifications> getMaterialGoodsSpecifications(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsSpecifications : {}", id);
        Optional<MaterialGoodsSpecifications> materialGoodsSpecifications = materialGoodsSpecificationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsSpecifications);
    }

    /**
     * DELETE  /material-goods-specifications/:id : delete the "id" materialGoodsSpecifications.
     *
     * @param id the id of the materialGoodsSpecifications to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-specifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsSpecifications(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsSpecifications : {}", id);
        materialGoodsSpecificationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/material-goods-specifications/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecifications>> findByMaterialGoodsID(@RequestParam(required = false) UUID id
    ) {
        List<MaterialGoodsSpecifications> lstMaterialGoodsSpecifications = materialGoodsSpecificationsService.findByMaterialGoodsID(id);
        return new ResponseEntity<>(lstMaterialGoodsSpecifications, HttpStatus.OK);
    }
}
