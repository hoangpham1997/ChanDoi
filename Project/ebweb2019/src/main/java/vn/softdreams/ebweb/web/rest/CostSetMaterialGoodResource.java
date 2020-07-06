package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.domain.CostSetMaterialGood;
import vn.softdreams.ebweb.service.CostSetMaterialGoodService;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;
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
 * REST controller for managing CostSetMaterialGood.
 */
@RestController
@RequestMapping("/api")
public class CostSetMaterialGoodResource {

    private final Logger log = LoggerFactory.getLogger(CostSetMaterialGoodResource.class);

    private static final String ENTITY_NAME = "costSetMaterialGood";

    private final CostSetMaterialGoodService costSetMaterialGoodService;

    public CostSetMaterialGoodResource(CostSetMaterialGoodService costSetMaterialGoodService) {
        this.costSetMaterialGoodService = costSetMaterialGoodService;
    }

    /**
     * POST  /cost-set-material-goods : Create a new costSetMaterialGood.
     *
     * @param costSetMaterialGood the costSetMaterialGood to create
     * @return the ResponseEntity with status 201 (Created) and with body the new costSetMaterialGood, or with status 400 (Bad Request) if the costSetMaterialGood has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cost-set-material-goods")
    @Timed
    public ResponseEntity<CostSetMaterialGood> createCostSetMaterialGood(@Valid @RequestBody CostSetMaterialGood costSetMaterialGood) throws URISyntaxException {
        log.debug("REST request to save CostSetMaterialGood : {}", costSetMaterialGood);
        if (costSetMaterialGood.getId() != null) {
            throw new BadRequestAlertException("A new costSetMaterialGood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostSetMaterialGood result = costSetMaterialGoodService.save(costSetMaterialGood);
        return ResponseEntity.created(new URI("/api/cost-set-material-goods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cost-set-material-goods : Updates an existing costSetMaterialGood.
     *
     * @param costSetMaterialGood the costSetMaterialGood to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated costSetMaterialGood,
     * or with status 400 (Bad Request) if the costSetMaterialGood is not valid,
     * or with status 500 (Internal Server Error) if the costSetMaterialGood couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cost-set-material-goods")
    @Timed
    public ResponseEntity<CostSetMaterialGood> updateCostSetMaterialGood(@Valid @RequestBody CostSetMaterialGood costSetMaterialGood) throws URISyntaxException {
        log.debug("REST request to update CostSetMaterialGood : {}", costSetMaterialGood);
        if (costSetMaterialGood.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostSetMaterialGood result = costSetMaterialGoodService.save(costSetMaterialGood);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, costSetMaterialGood.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cost-set-material-goods : get all the costSetMaterialGoods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of costSetMaterialGoods in body
     */
    @GetMapping("/cost-set-material-goods")
    @Timed
    public ResponseEntity<List<CostSetMaterialGood>> getAllCostSetMaterialGoods(Pageable pageable) {
        log.debug("REST request to get a page of CostSetMaterialGoods");
        Page<CostSetMaterialGood> page = costSetMaterialGoodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cost-set-material-goods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cost-set-material-goods/:id : get the "id" costSetMaterialGood.
     *
     * @param id the id of the costSetMaterialGood to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costSetMaterialGood, or with status 404 (Not Found)
     */
    @GetMapping("/cost-set-material-goods/{id}")
    @Timed
    public ResponseEntity<CostSetMaterialGood> getCostSetMaterialGood(@PathVariable UUID id) {
        log.debug("REST request to get CostSetMaterialGood : {}", id);
        Optional<CostSetMaterialGood> costSetMaterialGood = costSetMaterialGoodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costSetMaterialGood);
    }

    /**
     * DELETE  /cost-set-material-goods/:id : delete the "id" costSetMaterialGood.
     *
     * @param id the id of the costSetMaterialGood to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cost-set-material-goods/{id}")
    @Timed
    public ResponseEntity<Void> deleteCostSetMaterialGood(@PathVariable UUID id) {
        log.debug("REST request to delete CostSetMaterialGood : {}", id);
        costSetMaterialGoodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/cost-set-material-goods/get-all-by-company-id")
    @Timed
    public ResponseEntity<List<TheTinhGiaThanhDTO>> getAllByCompanyID(@RequestParam(required = false) Integer typeMethod) {
        log.debug("REST request to get a page of Accounts");
        List<TheTinhGiaThanhDTO> page = costSetMaterialGoodService.getAllByCompanyID(typeMethod);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cost-set-material-goods/get-all-for-report")
    @Timed
    public ResponseEntity<List<TheTinhGiaThanhDTO>> getAllForReport(@RequestParam(required = false) Integer typeMethod, @RequestParam(required = false) Boolean isDependent, @RequestParam(required = false) UUID orgID) {
        log.debug("REST request to get a page of Accounts");
        List<TheTinhGiaThanhDTO> page = costSetMaterialGoodService.getAllForReport(typeMethod, isDependent, orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
