package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import vn.softdreams.ebweb.service.MaterialGoodsConvertUnitService;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;
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
 * REST controller for managing MaterialGoodsConvertUnit.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsConvertUnitResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsConvertUnitResource.class);

    private static final String ENTITY_NAME = "materialGoodsConvertUnit";

    private final MaterialGoodsConvertUnitService materialGoodsConvertUnitService;

    public MaterialGoodsConvertUnitResource(MaterialGoodsConvertUnitService materialGoodsConvertUnitService) {
        this.materialGoodsConvertUnitService = materialGoodsConvertUnitService;
    }

    /**
     * POST  /material-goods-convert-units : Create a new materialGoodsConvertUnit.
     *
     * @param materialGoodsConvertUnit the materialGoodsConvertUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsConvertUnit, or with status 400 (Bad Request) if the materialGoodsConvertUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-convert-units")
    @Timed
    public ResponseEntity<MaterialGoodsConvertUnit> createMaterialGoodsConvertUnit(@Valid @RequestBody MaterialGoodsConvertUnit materialGoodsConvertUnit) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsConvertUnit : {}", materialGoodsConvertUnit);
        if (materialGoodsConvertUnit.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsConvertUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsConvertUnit result = materialGoodsConvertUnitService.save(materialGoodsConvertUnit);
        return ResponseEntity.created(new URI("/api/material-goods-convert-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-convert-units : Updates an existing materialGoodsConvertUnit.
     *
     * @param materialGoodsConvertUnit the materialGoodsConvertUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsConvertUnit,
     * or with status 400 (Bad Request) if the materialGoodsConvertUnit is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsConvertUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-convert-units")
    @Timed
    public ResponseEntity<MaterialGoodsConvertUnit> updateMaterialGoodsConvertUnit(@Valid @RequestBody MaterialGoodsConvertUnit materialGoodsConvertUnit) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsConvertUnit : {}", materialGoodsConvertUnit);
        if (materialGoodsConvertUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsConvertUnit result = materialGoodsConvertUnitService.save(materialGoodsConvertUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsConvertUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-convert-units : get all the materialGoodsConvertUnits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsConvertUnits in body
     */
    @GetMapping("/material-goods-convert-units")
    @Timed
    public ResponseEntity<List<MaterialGoodsConvertUnit>> getAllMaterialGoodsConvertUnits(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsConvertUnits");
        Page<MaterialGoodsConvertUnit> page = materialGoodsConvertUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-convert-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-convert-units/:id : get the "id" materialGoodsConvertUnit.
     *
     * @param id the id of the materialGoodsConvertUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsConvertUnit, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-convert-units/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsConvertUnit> getMaterialGoodsConvertUnit(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsConvertUnit : {}", id);
        Optional<MaterialGoodsConvertUnit> materialGoodsConvertUnit = materialGoodsConvertUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsConvertUnit);
    }

    /**
     * DELETE  /material-goods-convert-units/:id : delete the "id" materialGoodsConvertUnit.
     *
     * @param id the id of the materialGoodsConvertUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-convert-units/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsConvertUnit(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsConvertUnit : {}", id);
        materialGoodsConvertUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * lấy tỉ lệ chuyển đổ với phép tính từ mua hàng qua kho
     * @author congnd
     * @param materialGoodsId id hàng
     * @param unitId id đơn vị tính
     * @return
     */
    @GetMapping("/material-goods-convert-units/get-by-materialgoodsid-and-unitid")
    @Timed
    public ResponseEntity<MaterialGoodsConvertUnit> getMaterialGoodsConvertUnitPPInvoice(@RequestParam UUID materialGoodsId, @RequestParam UUID unitId) {
        MaterialGoodsConvertUnit materialGoodsConvertUnit = materialGoodsConvertUnitService.getMaterialGoodsConvertUnitPPInvoice(materialGoodsId, unitId);
        return new ResponseEntity<>(materialGoodsConvertUnit, HttpStatus.OK);
    }

    /**
     * Author Hautv
     * GET  /material-goods-convert-units : get all the materialGoodsConvertUnits.
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsConvertUnits in body
     */
    @GetMapping("/material-goods-convert-units/getAll")
    @Timed
    public ResponseEntity<List<MaterialGoodsConvertUnitDTO>> getAllMaterialGoodsConvertUnits() {
        log.debug("REST request to get a page of MaterialGoodsConvertUnits");
        List<MaterialGoodsConvertUnitDTO> page = materialGoodsConvertUnitService.getAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods-convert-units/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsConvertUnit>> findByMaterialGoodsID(@RequestParam(required = false) UUID id
    ) {
        List<MaterialGoodsConvertUnit> lstMaterialGoodsConvertUnit = materialGoodsConvertUnitService.findByMaterialGoodsID(id);
        return new ResponseEntity<>(lstMaterialGoodsConvertUnit, HttpStatus.OK);
    }

    @GetMapping("/material-goods-convert-units/get-number-order")
    @Timed
    public ResponseEntity<List<Integer>> getNumberOrder(@RequestParam(required = false) UUID companyID, @RequestParam(required = false) Boolean similarBranch) {
        List<Integer> numberOrder = materialGoodsConvertUnitService.getNumberOrder(companyID, similarBranch);
        return new ResponseEntity<>(numberOrder, HttpStatus.OK);
    }
}
