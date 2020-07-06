package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.EMContract;
import vn.softdreams.ebweb.service.EMContractService;
import vn.softdreams.ebweb.service.dto.EMContractConvertDTO;
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
 * REST controller for managing EMContract.
 */
@RestController
@RequestMapping("/api")
public class EMContractResource {

    private final Logger log = LoggerFactory.getLogger(EMContractResource.class);

    private static final String ENTITY_NAME = "eMContract";

    private final EMContractService eMContractService;

    public EMContractResource(EMContractService eMContractService) {
        this.eMContractService = eMContractService;
    }

    /**
     * POST  /e-m-contracts : Create a new eMContract.
     *
     * @param eMContract the eMContract to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eMContract, or with status 400 (Bad Request) if the eMContract has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/e-m-contracts")
    @Timed
    public ResponseEntity<EMContract> createEMContract(@Valid @RequestBody EMContract eMContract) throws URISyntaxException {
        log.debug("REST request to save EMContract : {}", eMContract);
        if (eMContract.getId() != null) {
            throw new BadRequestAlertException("A new eMContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EMContract result = eMContractService.save(eMContract);
        return ResponseEntity.created(new URI("/api/e-m-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /e-m-contracts : Updates an existing eMContract.
     *
     * @param eMContract the eMContract to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eMContract,
     * or with status 400 (Bad Request) if the eMContract is not valid,
     * or with status 500 (Internal Server Error) if the eMContract couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/e-m-contracts")
    @Timed
    public ResponseEntity<EMContract> updateEMContract(@Valid @RequestBody EMContract eMContract) throws URISyntaxException {
        log.debug("REST request to update EMContract : {}", eMContract);
        if (eMContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EMContract result = eMContractService.save(eMContract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eMContract.getId().toString()))
            .body(result);
    }

    /**
     * GET  /e-m-contracts : get all the eMContracts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eMContracts in body
     */
    @GetMapping("/e-m-contracts")
    @Timed
    public ResponseEntity<List<EMContract>> getAllEMContracts(Pageable pageable) {
        log.debug("REST request to get a page of EMContracts");
        Page<EMContract> page = eMContractService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-m-contracts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /e-m-contracts : get all the eMContracts.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eMContracts in body
     */
    @GetMapping("/e-m-contracts/getAllEMContracts")
    @Timed
    public ResponseEntity<List<EMContract>> getAllEMContracts() {
        log.debug("REST request to get a page of EMContracts");
        Page<EMContract> page = eMContractService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-m-contracts/getAllEMContracts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /e-m-contracts/:id : get the "id" eMContract.
     *
     * @param id the id of the eMContract to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eMContract, or with status 404 (Not Found)
     */
    @GetMapping("/e-m-contracts/{id}")
    @Timed
    public ResponseEntity<EMContract> getEMContract(@PathVariable UUID id) {
        log.debug("REST request to get EMContract : {}", id);
        Optional<EMContract> eMContract = eMContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eMContract);
    }

    /**
     * DELETE  /e-m-contracts/:id : delete the "id" eMContract.
     *
     * @param id the id of the eMContract to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/e-m-contracts/{id}")
    @Timed
    public ResponseEntity<Void> deleteEMContract(@PathVariable UUID id) {
        log.debug("REST request to delete EMContract : {}", id);
        eMContractService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/e-m-contracts/active")
    @Timed
    public ResponseEntity<List<EMContract>> getAllEMContractsActive(Pageable pageable) {
        log.debug("REST request to get a page of EMContracts");
        Page<EMContract> page = eMContractService.getAllEMContractsActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-m-contracts/active");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/e-m-contracts/find-all-em-contracts-active-companyid")
    @Timed
    public ResponseEntity<List<EMContract>> getAllEMContractsActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<EMContract> page = eMContractService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/e-m-contracts/find-all-em-contracts-for-report")
    @Timed
    public ResponseEntity<List<EMContract>> getAllForReport(@RequestParam(required = false) Boolean isDependent, @RequestParam(required = false) UUID orgID) {
        log.debug("REST request to get a page of Accounts");
        List<EMContract> page = eMContractService.getAllForReport(isDependent, orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
