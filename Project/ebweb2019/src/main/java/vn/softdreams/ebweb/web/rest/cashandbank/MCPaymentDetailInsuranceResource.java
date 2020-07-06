package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCPaymentDetailInsurance;
import vn.softdreams.ebweb.service.MCPaymentDetailInsuranceService;
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
 * REST controller for managing MCPaymentDetailInsurance.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentDetailInsuranceResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailInsuranceResource.class);

    private static final String ENTITY_NAME = "mCPaymentDetailInsurance";

    private final MCPaymentDetailInsuranceService mCPaymentDetailInsuranceService;

    public MCPaymentDetailInsuranceResource(MCPaymentDetailInsuranceService mCPaymentDetailInsuranceService) {
        this.mCPaymentDetailInsuranceService = mCPaymentDetailInsuranceService;
    }

    /**
     * POST  /m-c-payment-detail-insurances : Create a new mCPaymentDetailInsurance.
     *
     * @param mCPaymentDetailInsurance the mCPaymentDetailInsurance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPaymentDetailInsurance, or with status 400 (Bad Request) if the mCPaymentDetailInsurance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payment-detail-insurances")
    @Timed
    public ResponseEntity<MCPaymentDetailInsurance> createMCPaymentDetailInsurance(@Valid @RequestBody MCPaymentDetailInsurance mCPaymentDetailInsurance) throws URISyntaxException {
        log.debug("REST request to save MCPaymentDetailInsurance : {}", mCPaymentDetailInsurance);
        if (mCPaymentDetailInsurance.getId() != null) {
            throw new BadRequestAlertException("A new mCPaymentDetailInsurance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentDetailInsurance result = mCPaymentDetailInsuranceService.save(mCPaymentDetailInsurance);
        return ResponseEntity.created(new URI("/api/m-c-payment-detail-insurances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-payment-detail-insurances : Updates an existing mCPaymentDetailInsurance.
     *
     * @param mCPaymentDetailInsurance the mCPaymentDetailInsurance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPaymentDetailInsurance,
     * or with status 400 (Bad Request) if the mCPaymentDetailInsurance is not valid,
     * or with status 500 (Internal Server Error) if the mCPaymentDetailInsurance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payment-detail-insurances")
    @Timed
    public ResponseEntity<MCPaymentDetailInsurance> updateMCPaymentDetailInsurance(@Valid @RequestBody MCPaymentDetailInsurance mCPaymentDetailInsurance) throws URISyntaxException {
        log.debug("REST request to update MCPaymentDetailInsurance : {}", mCPaymentDetailInsurance);
        if (mCPaymentDetailInsurance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentDetailInsurance result = mCPaymentDetailInsuranceService.save(mCPaymentDetailInsurance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPaymentDetailInsurance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payment-detail-insurances : get all the mCPaymentDetailInsurances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPaymentDetailInsurances in body
     */
    @GetMapping("/m-c-payment-detail-insurances")
    @Timed
    public ResponseEntity<List<MCPaymentDetailInsurance>> getAllMCPaymentDetailInsurances(Pageable pageable) {
        log.debug("REST request to get a page of MCPaymentDetailInsurances");
        Page<MCPaymentDetailInsurance> page = mCPaymentDetailInsuranceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payment-detail-insurances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payment-detail-insurances/:id : get the "id" mCPaymentDetailInsurance.
     *
     * @param id the id of the mCPaymentDetailInsurance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPaymentDetailInsurance, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payment-detail-insurances/{id}")
    @Timed
    public ResponseEntity<MCPaymentDetailInsurance> getMCPaymentDetailInsurance(@PathVariable UUID id) {
        log.debug("REST request to get MCPaymentDetailInsurance : {}", id);
        Optional<MCPaymentDetailInsurance> mCPaymentDetailInsurance = mCPaymentDetailInsuranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPaymentDetailInsurance);
    }

    /**
     * DELETE  /m-c-payment-detail-insurances/:id : delete the "id" mCPaymentDetailInsurance.
     *
     * @param id the id of the mCPaymentDetailInsurance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payment-detail-insurances/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPaymentDetailInsurance(@PathVariable UUID id) {
        log.debug("REST request to delete MCPaymentDetailInsurance : {}", id);
        mCPaymentDetailInsuranceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
