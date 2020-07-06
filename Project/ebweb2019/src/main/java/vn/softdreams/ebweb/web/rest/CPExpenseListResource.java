package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPExpenseList;
import vn.softdreams.ebweb.service.CPExpenseListService;
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
 * REST controller for managing CPExpenseList.
 */
@RestController
@RequestMapping("/api")
public class CPExpenseListResource {

    private final Logger log = LoggerFactory.getLogger(CPExpenseListResource.class);

    private static final String ENTITY_NAME = "cPExpenseList";

    private final CPExpenseListService cPExpenseListService;

    public CPExpenseListResource(CPExpenseListService cPExpenseListService) {
        this.cPExpenseListService = cPExpenseListService;
    }

    /**
     * POST  /cp-expense-lists : Create a new cPExpenseList.
     *
     * @param cPExpenseList the cPExpenseList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPExpenseList, or with status 400 (Bad Request) if the cPExpenseList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-expense-lists")
    @Timed
    public ResponseEntity<CPExpenseList> createCPExpenseList(@Valid @RequestBody CPExpenseList cPExpenseList) throws URISyntaxException {
        log.debug("REST request to save CPExpenseList : {}", cPExpenseList);
        if (cPExpenseList.getId() != null) {
            throw new BadRequestAlertException("A new cPExpenseList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPExpenseList result = cPExpenseListService.save(cPExpenseList);
        return ResponseEntity.created(new URI("/api/cp-expense-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-expense-lists : Updates an existing cPExpenseList.
     *
     * @param cPExpenseList the cPExpenseList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPExpenseList,
     * or with status 400 (Bad Request) if the cPExpenseList is not valid,
     * or with status 500 (Internal Server Error) if the cPExpenseList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-expense-lists")
    @Timed
    public ResponseEntity<CPExpenseList> updateCPExpenseList(@Valid @RequestBody CPExpenseList cPExpenseList) throws URISyntaxException {
        log.debug("REST request to update CPExpenseList : {}", cPExpenseList);
        if (cPExpenseList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPExpenseList result = cPExpenseListService.save(cPExpenseList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPExpenseList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-expense-lists : get all the cPExpenseLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPExpenseLists in body
     */
    @GetMapping("/cp-expense-lists")
    @Timed
    public ResponseEntity<List<CPExpenseList>> getAllCPExpenseLists(Pageable pageable) {
        log.debug("REST request to get a page of CPExpenseLists");
        Page<CPExpenseList> page = cPExpenseListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-expense-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/cp-expense-lists/get-all-by-cPPeriodID/{cPPeriodID}")
    @Timed
    public ResponseEntity<List<CPExpenseList>> getAllCPExpenseListsByCPPeriodID(@PathVariable UUID cPPeriodID) {
        log.debug("REST request to get a page of CPExpenseLists");
        List<CPExpenseList> page = cPExpenseListService.findAllByCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /cp-expense-lists/:id : get the "id" cPExpenseList.
     *
     * @param id the id of the cPExpenseList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPExpenseList, or with status 404 (Not Found)
     */
    @GetMapping("/cp-expense-lists/{id}")
    @Timed
    public ResponseEntity<CPExpenseList> getCPExpenseList(@PathVariable UUID id) {
        log.debug("REST request to get CPExpenseList : {}", id);
        Optional<CPExpenseList> cPExpenseList = cPExpenseListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPExpenseList);
    }

    /**
     * DELETE  /cp-expense-lists/:id : delete the "id" cPExpenseList.
     *
     * @param id the id of the cPExpenseList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-expense-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPExpenseList(@PathVariable UUID id) {
        log.debug("REST request to delete CPExpenseList : {}", id);
        cPExpenseListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
