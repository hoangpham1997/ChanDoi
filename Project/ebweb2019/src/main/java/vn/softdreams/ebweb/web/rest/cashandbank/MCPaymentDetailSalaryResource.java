package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCPaymentDetailSalary;
import vn.softdreams.ebweb.service.MCPaymentDetailSalaryService;
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
 * REST controller for managing MCPaymentDetailSalary.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentDetailSalaryResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailSalaryResource.class);

    private static final String ENTITY_NAME = "mCPaymentDetailSalary";

    private final MCPaymentDetailSalaryService mCPaymentDetailSalaryService;

    public MCPaymentDetailSalaryResource(MCPaymentDetailSalaryService mCPaymentDetailSalaryService) {
        this.mCPaymentDetailSalaryService = mCPaymentDetailSalaryService;
    }

    /**
     * POST  /m-c-payment-detail-salaries : Create a new mCPaymentDetailSalary.
     *
     * @param mCPaymentDetailSalary the mCPaymentDetailSalary to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPaymentDetailSalary, or with status 400 (Bad Request) if the mCPaymentDetailSalary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payment-detail-salaries")
    @Timed
    public ResponseEntity<MCPaymentDetailSalary> createMCPaymentDetailSalary(@Valid @RequestBody MCPaymentDetailSalary mCPaymentDetailSalary) throws URISyntaxException {
        log.debug("REST request to save MCPaymentDetailSalary : {}", mCPaymentDetailSalary);
        if (mCPaymentDetailSalary.getId() != null) {
            throw new BadRequestAlertException("A new mCPaymentDetailSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentDetailSalary result = mCPaymentDetailSalaryService.save(mCPaymentDetailSalary);
        return ResponseEntity.created(new URI("/api/m-c-payment-detail-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-payment-detail-salaries : Updates an existing mCPaymentDetailSalary.
     *
     * @param mCPaymentDetailSalary the mCPaymentDetailSalary to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPaymentDetailSalary,
     * or with status 400 (Bad Request) if the mCPaymentDetailSalary is not valid,
     * or with status 500 (Internal Server Error) if the mCPaymentDetailSalary couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payment-detail-salaries")
    @Timed
    public ResponseEntity<MCPaymentDetailSalary> updateMCPaymentDetailSalary(@Valid @RequestBody MCPaymentDetailSalary mCPaymentDetailSalary) throws URISyntaxException {
        log.debug("REST request to update MCPaymentDetailSalary : {}", mCPaymentDetailSalary);
        if (mCPaymentDetailSalary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentDetailSalary result = mCPaymentDetailSalaryService.save(mCPaymentDetailSalary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPaymentDetailSalary.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payment-detail-salaries : get all the mCPaymentDetailSalaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPaymentDetailSalaries in body
     */
    @GetMapping("/m-c-payment-detail-salaries")
    @Timed
    public ResponseEntity<List<MCPaymentDetailSalary>> getAllMCPaymentDetailSalaries(Pageable pageable) {
        log.debug("REST request to get a page of MCPaymentDetailSalaries");
        Page<MCPaymentDetailSalary> page = mCPaymentDetailSalaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payment-detail-salaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payment-detail-salaries/:id : get the "id" mCPaymentDetailSalary.
     *
     * @param id the id of the mCPaymentDetailSalary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPaymentDetailSalary, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payment-detail-salaries/{id}")
    @Timed
    public ResponseEntity<MCPaymentDetailSalary> getMCPaymentDetailSalary(@PathVariable UUID id) {
        log.debug("REST request to get MCPaymentDetailSalary : {}", id);
        Optional<MCPaymentDetailSalary> mCPaymentDetailSalary = mCPaymentDetailSalaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPaymentDetailSalary);
    }

    /**
     * DELETE  /m-c-payment-detail-salaries/:id : delete the "id" mCPaymentDetailSalary.
     *
     * @param id the id of the mCPaymentDetailSalary to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payment-detail-salaries/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPaymentDetailSalary(@PathVariable UUID id) {
        log.debug("REST request to delete MCPaymentDetailSalary : {}", id);
        mCPaymentDetailSalaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
