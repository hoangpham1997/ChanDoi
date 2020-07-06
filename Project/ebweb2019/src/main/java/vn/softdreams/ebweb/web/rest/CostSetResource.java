package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.service.CostSetService;
import vn.softdreams.ebweb.service.dto.CostSetConvertDTO;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CostSetSaveDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CostSet.
 */
@RestController
@RequestMapping("/api")
public class CostSetResource {

    private final Logger log = LoggerFactory.getLogger(CostSetResource.class);

    private static final String ENTITY_NAME = "costSet";

    private final CostSetService costSetService;

    public CostSetResource(CostSetService costSetService) {
        this.costSetService = costSetService;
    }

    /**
     * POST  /cost-sets : Create a new costSet.
     *
     * @param costSet the costSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new costSet, or with status 400 (Bad Request) if the costSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cost-sets")
    @Timed
    public ResponseEntity<CostSetSaveDTO> createCostSet(@Valid @RequestBody CostSet costSet) throws URISyntaxException {
        log.debug("REST request to save CostSet : {}", costSet);
        if (costSet.getId() != null) {
            throw new BadRequestAlertException("A new costSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostSetSaveDTO result = new CostSetSaveDTO();
        try {
            result = costSetService.saveDTO(costSet);
        } catch (Exception ex) {

        }
        if (result.getCostSet().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/cost-set/" + result.getCostSet().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCostSet().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /cost-sets : Updates an existing costSet.
     *
     * @param costSet the costSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated costSet,
     * or with status 400 (Bad Request) if the costSet is not valid,
     * or with status 500 (Internal Server Error) if the costSet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cost-sets")
    @Timed
    public ResponseEntity<CostSetSaveDTO> updateCostSet(@Valid @RequestBody CostSet costSet) throws URISyntaxException {
        log.debug("REST request to update CostSet : {}", costSet);
        if (costSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostSetSaveDTO result = new CostSetSaveDTO();
        try {
            result = costSetService.saveDTO(costSet);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, costSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cost-sets : get all the costSets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of costSets in body
     */
    @GetMapping("/cost-sets")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSets(Pageable pageable) {
        log.debug("REST request to get a page of CostSets");
        Page<CostSet> page = costSetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cost-sets : get all the costSets.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of costSets in body
     */
    @GetMapping("/cost-sets/find-all-cost-sets-by-company-id")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSets() {
        log.debug("REST request to get a page of Accounts");
        List<CostSet> page = costSetService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /cost-sets/:id : get the "id" costSet.
     *
     * @param id the id of the costSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costSet, or with status 404 (Not Found)
     */
    @GetMapping("/cost-sets/{id}")
    @Timed
    public ResponseEntity<CostSet> getCostSet(@PathVariable UUID id) {
        log.debug("REST request to get CostSet : {}", id);
        Optional<CostSet> costSet = costSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costSet);
    }

    /**
     * DELETE  /cost-sets/:id : delete the "id" costSet.
     *
     * @param id the id of the costSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cost-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCostSet(@PathVariable UUID id) {
        log.debug("REST request to delete CostSet : {}", id);
        costSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /*
     * author: tungnt
     * @param:pageable
     * */
    @GetMapping("/cost-sets/search-all-active")
    @Timed
    public ResponseEntity<List<CostSet>> findAll(Pageable pageable, @RequestParam(required = false) UUID branchID,
                                                 @RequestParam(required = false) String costSetCode,
                                                 @RequestParam(required = false) String costSetName,
                                                 @RequestParam(required = false) Integer costSetType,
                                                 @RequestParam(required = false) String description,
                                                 @RequestParam(required = false) UUID parentID,
                                                 @RequestParam(required = false) Boolean isParentNode,
                                                 @RequestParam(required = false) String orderFixCode,
                                                 @RequestParam(required = false) Integer grade,
                                                 @RequestParam(required = false) Boolean isActive) {
        log.debug("REST request to get CostSet ");
        Page<CostSet> page = costSetService.findAll(pageable, branchID, costSetCode, costSetName, costSetType, description,
            parentID, isParentNode, orderFixCode, grade, isActive);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/search-all")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSetsActive() {
        log.debug("REST request to get a page of CostSets");
        Page<CostSet> page = costSetService.getAllCostSetsActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-sets/getAllCostSetsActive");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/find-all-cost-sets-active-companyid")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSetActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<CostSet> page = costSetService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/find-all-cost-sets-by-org")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSetsByOrg(@RequestParam (required = false) UUID orgID, @RequestParam (required = false) Boolean isDependent) {
        log.debug("REST request to get a page of Accounts");
        List<CostSet> page = costSetService.findAllByOrgID(orgID, isDependent);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/find-all-cost-sets-by-companyid")
    @Timed
    public ResponseEntity<List<CostSet>> getCostSetsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<CostSet> page = costSetService.getCostSetsByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/find-all-cost-set-by-companyid")
    @Timed
    public ResponseEntity<List<CostSet>> getAllCostSetByCompanyID(Pageable pageable, @RequestParam(required = false) Boolean isGetAllCompany) {
        log.debug("REST request to get a page of CostSets");
        //Page<CostSet> page = costSetService.findAllCostSetByCompanyID(pageable);
        Page<CostSet> page = costSetService.findAllCostSetByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-sets/find-all-cost-set-by-companyid");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/cost-sets/find-cost-sets-by-type")
    @Timed
    public ResponseEntity<List<CostSet>> getCostSetsByTypeRaTio(Pageable pageable, @RequestParam(required = false) Integer type) {
        log.debug("REST request to get a page of CostSets");
        Page<CostSet> page = costSetService.getCostSetsByTypeRaTio(pageable, type);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-sets/find-cost-sets-by-type");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/cost-sets/multi-delete-cost-set")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteCostSets(@Valid @RequestBody List<UUID> costSets) {
        log.debug("REST request to delete multi costSets");
        HandlingResultDTO resultDTO = costSetService.multiDelete(costSets);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/cost-sets/find-all-cost-set-dependent")
    @Timed
    public ResponseEntity<List<CostSet>> getCostSetList(@RequestParam(required = false) UUID companyID ,@RequestParam(required = false) Boolean dependent){
        log.debug("REST request to get a page of CostSets");
        List<CostSet> page = costSetService.findCostSetList(companyID, dependent);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/cost-sets/find-by-list-id-cost-set")
    @Timed
    public ResponseEntity<List<CostSetMaterialGoodsDTO>> getCostSetByListID(@RequestParam(required = false) List<UUID> uuids) {
        log.debug("REST request to get a page of Accounts");
        List<CostSetMaterialGoodsDTO> page = costSetService.getCostSetByListID(uuids);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/cost-sets/find-revenue-by-costset-id")
    @Timed
    public ResponseEntity<List<CostSetDTO>> findRevenueByCostSetID(@RequestBody CostSetDTO costSetDTO) {
        List<CostSetDTO> page = costSetService.findRevenueByCostSetID(costSetDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
