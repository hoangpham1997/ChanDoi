package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecifications;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger;
import vn.softdreams.ebweb.service.MaterialGoodsSpecificationsLedgerService;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;
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
import java.util.UUID;

/**
 * REST controller for managing MaterialGoodsSpecificationsLedger.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsSpecificationsLedgerResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecificationsLedgerResource.class);

    private static final String ENTITY_NAME = "materialGoodsSpecificationsLedger";

    private final MaterialGoodsSpecificationsLedgerService materialGoodsSpecificationsLedgerService;

    public MaterialGoodsSpecificationsLedgerResource(MaterialGoodsSpecificationsLedgerService materialGoodsSpecificationsLedgerService) {
        this.materialGoodsSpecificationsLedgerService = materialGoodsSpecificationsLedgerService;
    }

    /**
     * POST  /material-goods-specifications-ledgers : Create a new materialGoodsSpecificationsLedger.
     *
     * @param materialGoodsSpecificationsLedger the materialGoodsSpecificationsLedger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsSpecificationsLedger, or with status 400 (Bad Request) if the materialGoodsSpecificationsLedger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-specifications-ledgers")
    @Timed
    public ResponseEntity<MaterialGoodsSpecificationsLedger> createMaterialGoodsSpecificationsLedger(@RequestBody MaterialGoodsSpecificationsLedger materialGoodsSpecificationsLedger) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsSpecificationsLedger : {}", materialGoodsSpecificationsLedger);
        if (materialGoodsSpecificationsLedger.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsSpecificationsLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsSpecificationsLedger result = materialGoodsSpecificationsLedgerService.save(materialGoodsSpecificationsLedger);
        return ResponseEntity.created(new URI("/api/material-goods-specifications-ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-specifications-ledgers : Updates an existing materialGoodsSpecificationsLedger.
     *
     * @param materialGoodsSpecificationsLedger the materialGoodsSpecificationsLedger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsSpecificationsLedger,
     * or with status 400 (Bad Request) if the materialGoodsSpecificationsLedger is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsSpecificationsLedger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-specifications-ledgers")
    @Timed
    public ResponseEntity<MaterialGoodsSpecificationsLedger> updateMaterialGoodsSpecificationsLedger(@RequestBody MaterialGoodsSpecificationsLedger materialGoodsSpecificationsLedger) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsSpecificationsLedger : {}", materialGoodsSpecificationsLedger);
        if (materialGoodsSpecificationsLedger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsSpecificationsLedger result = materialGoodsSpecificationsLedgerService.save(materialGoodsSpecificationsLedger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsSpecificationsLedger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-specifications-ledgers : get all the materialGoodsSpecificationsLedgers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsSpecificationsLedgers in body
     */
    @GetMapping("/material-goods-specifications-ledgers")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecificationsLedger>> getAllMaterialGoodsSpecificationsLedgers(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsSpecificationsLedgers");
        Page<MaterialGoodsSpecificationsLedger> page = materialGoodsSpecificationsLedgerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-specifications-ledgers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-specifications-ledgers/:id : get the "id" materialGoodsSpecificationsLedger.
     *
     * @param id the id of the materialGoodsSpecificationsLedger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsSpecificationsLedger, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-specifications-ledgers/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsSpecificationsLedger> getMaterialGoodsSpecificationsLedger(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsSpecificationsLedger : {}", id);
        Optional<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedger = materialGoodsSpecificationsLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsSpecificationsLedger);
    }

    /**
     * DELETE  /material-goods-specifications-ledgers/:id : delete the "id" materialGoodsSpecificationsLedger.
     *
     * @param id the id of the materialGoodsSpecificationsLedger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-specifications-ledgers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsSpecificationsLedger(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsSpecificationsLedger : {}", id);
        materialGoodsSpecificationsLedgerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/material-goods-specifications-ledgers/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsSpecificationsLedgerDTO>> findByMaterialGoodsID(@RequestParam(required = false) UUID id, @RequestParam(required = false) UUID repositoryID
    ) {
        List<MaterialGoodsSpecificationsLedgerDTO> materialGoodsSpecificationsLedgerDTOS = materialGoodsSpecificationsLedgerService.findByMaterialGoodsID(id, repositoryID);
        return new ResponseEntity<>(materialGoodsSpecificationsLedgerDTOS, HttpStatus.OK);
    }
}
