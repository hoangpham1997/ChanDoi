package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.UnitService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.web.rest.dto.UnitSaveDTO;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Unit.
 */
@RestController
@RequestMapping("/api")
public class UnitResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);

    private static final String ENTITY_NAME = "unit";

    private final UnitService unitService;

    public UnitResource(UnitService unitService) {
        this.unitService = unitService;
    }

    /**
     * POST  /units : Create a new unit.
     *
     * @param unit the unit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unit, or with status 400 (Bad Request) if the unit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/units")
    @Timed
    public ResponseEntity<UnitSaveDTO> createUnit(@Valid @RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to save Unit : {}", unit);
        if (unit.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitSaveDTO result = unitService.save(unit);
        if (result.getUnit().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/units/" + result.getUnit().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getUnit().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /units : Updates an existing unit.
     *
     * @param unit the unit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unit,
     * or with status 400 (Bad Request) if the unit is not valid,
     * or with status 500 (Internal Server Error) if the unit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/units")
    @Timed
    public ResponseEntity<UnitSaveDTO> updateUnit(@Valid @RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to update Unit : {}", unit);
        if (unit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnitSaveDTO result = unitService.save(unit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /units : get all the units.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of units in body
     */
    @GetMapping("/units")
    @Timed
    public ResponseEntity<List<Unit>> getAllUnits() {
        log.debug("REST request to get a page of Units");
        List<Unit> page = unitService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /unit : get all the unit.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/units/pageable-all-unit")
    @Timed
    public ResponseEntity<List<Unit>> pageableAllUnit(Pageable pageable) {
        log.debug("REST request to get a page of AccountingObjects");
        Page<Unit> page = unitService.pageableAllUnit(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/units/pageable-all-unit");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /units/:id : get the "id" unit.
     *
     * @param id the id of the unit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unit, or with status 404 (Not Found)
     */
    @GetMapping("/units/{id}")
    @Timed
    public ResponseEntity<Unit> getUnit(@PathVariable UUID id) {
        log.debug("REST request to get Unit : {}", id);
        Optional<Unit> unit = unitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unit);
    }
    @GetMapping("/units/by-id")
    @Timed
    public ResponseEntity<Unit> findOne(@RequestParam UUID id) {
        log.debug("REST request to get Unit : {}", id);
        Optional<Unit> unit = unitService.findOneByID(id);
        return ResponseUtil.wrapOrNotFound(unit);
    }
    /**
     * DELETE  /units/:id : delete the "id" unit.
     *
     * @param id the id of the unit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/units/{id}")
    @Timed
    public ResponseEntity<Void> deleteUnit(@PathVariable UUID id) {
        log.debug("REST request to delete Unit : {}", id);
        unitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/units/delete-list-unit")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerUnit(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = unitService.deleteUnit(uuids);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    /**
     * @author hieugie
     *
     * @param pageable
     * @return
     */
    @GetMapping("/units/search-all")
    @Timed
    public ResponseEntity<List<Unit>> findAll(Pageable pageable, @RequestParam(required = false) String unitName,
                                                  @RequestParam(required = false) String unitDescription,
                                                  @RequestParam(required = false) Boolean isActive) {
        log.debug("REST request to get a page of Units");
        Page<Unit> page = unitService.findAll(pageable, unitName, unitDescription, isActive);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     * @author dungvm
     */
    @GetMapping("/units/material-goods-convert-rate")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> convertRateForMaterialGoods(@RequestParam UUID materialGoodsId) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        List<UnitConvertRateDTO> convertRateForMaterialGoods = unitService.convertRateForMaterialGoods(materialGoodsId);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }
    @GetMapping("/units/material-goods-convert-rate/combobox")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> convertRateForMaterialGoodsCombobox(@RequestParam UUID materialGoodsId) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        List<UnitConvertRateDTO> convertRateForMaterialGoods = unitService.convertRateForMaterialGoodsCombobox(materialGoodsId);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }
    @GetMapping("/units/material-goods-convert-rate/combobox/custom")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> convertRateForMaterialGoodsComboboxCustom(@RequestParam(required = false) UUID materialGoodsId) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        List<UnitConvertRateDTO> convertRateForMaterialGoods = unitService.convertRateForMaterialGoodsComboboxCustom(materialGoodsId);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }

    @GetMapping("/units/material-goods-convert-rate/combobox/custom/increment")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> getUnitByITIIncrementID(@RequestParam(required = false) UUID tiIncrementID) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        List<UnitConvertRateDTO> convertRateForMaterialGoods = unitService.getUnitByITIIncrementID(tiIncrementID);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }

    @PostMapping("/units/material-goods-convert-rate/combobox/custom-list")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> convertRateForMaterialGoodsComboboxCustomList(@RequestParam(required = false) List<UUID> materialGoodsId) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        List<UnitConvertRateDTO> convertRateForMaterialGoods = unitService.convertRateForMaterialGoodsComboboxCustomList(materialGoodsId);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }

    @GetMapping("/units/main-unit-name")
    @Timed
    public UnitConvertRateDTO getMainUnitName(@RequestParam UUID materialGoodsId) {
        log.debug("REST request to get a page of convertRateForMaterialGoods");
        UnitConvertRateDTO unitConvertRateDTO = unitService.getMainUnitName(materialGoodsId);
        return unitConvertRateDTO;
    }

    /**
     * @return
     * @author dungvm
     */
    @GetMapping("/units/material-goods-unit-price")
    @Timed
    public ResponseEntity<List<BigDecimal>> unitPriceOriginalForMaterialGoods(@RequestParam UUID materialGoodsId,
                                                                              @RequestParam UUID unitId,
                                                                              @RequestParam String currencyCode) {
        log.debug("REST request to get a page of unitPriceOriginalForMaterialGoods");
        List<BigDecimal> convertRateForMaterialGoods = unitService.unitPriceOriginalForMaterialGoods(materialGoodsId, unitId, currencyCode);
        return new ResponseEntity<>(convertRateForMaterialGoods, HttpStatus.OK);
    }

    @GetMapping("/units/{id}/uuid")
    @Timed
    public ResponseEntity<Unit> getUUIDUnit(@PathVariable UUID id) {
        log.debug("REST request to get Unit : {}", id);
        Optional<Unit> unit = unitService.getUUIDUnit(id);
        return ResponseUtil.wrapOrNotFound(unit);
    }

    @GetMapping("/units/find-all-units-active-companyid")
    @Timed
    public ResponseEntity<List<Unit>> getAllUnitsActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<Unit> page = unitService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/units/find-all-units-by-company-id")
    @Timed
    public ResponseEntity<List<Unit>> getAllUnitsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<Unit> page = unitService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @Author Hautv
     * @return
     */
    @GetMapping("/units/find-all-with-convertrate")
    @Timed
    public ResponseEntity<List<UnitConvertRateDTO>> findAllWithConvertRate() {
        log.debug("REST request to get a page of Accounts");
        List<UnitConvertRateDTO> page = unitService.findAllWithConvertRate();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
