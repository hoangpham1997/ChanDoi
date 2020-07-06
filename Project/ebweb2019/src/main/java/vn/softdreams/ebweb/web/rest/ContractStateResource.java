package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.ContractState;
import vn.softdreams.ebweb.service.ContractStateService;
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
 * REST controller for managing ContractState.
 */
@RestController
@RequestMapping("/api")
public class ContractStateResource {

    private final Logger log = LoggerFactory.getLogger(ContractStateResource.class);

    private static final String ENTITY_NAME = "contractState";

    private final ContractStateService contractStateService;

    public ContractStateResource(ContractStateService contractStateService) {
        this.contractStateService = contractStateService;
    }

    /**
     * POST  /contract-states : Create a new contractState.
     *
     * @param contractState the contractState to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractState, or with status 400 (Bad Request) if the contractState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contract-states")
    @Timed
    public ResponseEntity<ContractState> createContractState(@Valid @RequestBody ContractState contractState) throws URISyntaxException {
        log.debug("REST request to save ContractState : {}", contractState);
        if (contractState.getId() != null) {
            throw new BadRequestAlertException("A new contractState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractState result = contractStateService.save(contractState);
        return ResponseEntity.created(new URI("/api/contract-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contract-states : Updates an existing contractState.
     *
     * @param contractState the contractState to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractState,
     * or with status 400 (Bad Request) if the contractState is not valid,
     * or with status 500 (Internal Server Error) if the contractState couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contract-states")
    @Timed
    public ResponseEntity<ContractState> updateContractState(@Valid @RequestBody ContractState contractState) throws URISyntaxException {
        log.debug("REST request to update ContractState : {}", contractState);
        if (contractState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContractState result = contractStateService.save(contractState);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contractState.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contract-states : get all the contractStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contractStates in body
     */
    @GetMapping("/contract-states")
    @Timed
    public ResponseEntity<List<ContractState>> getAllContractStates(Pageable pageable) {
        log.debug("REST request to get a page of ContractStates");
        Page<ContractState> page = contractStateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contract-states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contract-states/:id : get the "id" contractState.
     *
     * @param id the id of the contractState to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractState, or with status 404 (Not Found)
     */
    @GetMapping("/contract-states/{id}")
    @Timed
    public ResponseEntity<ContractState> getContractState(@PathVariable Long id) {
        log.debug("REST request to get ContractState : {}", id);
        Optional<ContractState> contractState = contractStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractState);
    }

    /**
     * DELETE  /contract-states/:id : delete the "id" contractState.
     *
     * @param id the id of the contractState to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contract-states/{id}")
    @Timed
    public ResponseEntity<Void> deleteContractState(@PathVariable Long id) {
        log.debug("REST request to delete ContractState : {}", id);
        contractStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
