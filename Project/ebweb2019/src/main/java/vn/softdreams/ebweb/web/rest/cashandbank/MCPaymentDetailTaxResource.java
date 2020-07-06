package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCPaymentDetailTax;
import vn.softdreams.ebweb.service.MCPaymentDetailTaxService;
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
 * REST controller for managing MCPaymentDetailTax.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailTaxResource.class);

    private static final String ENTITY_NAME = "mCPaymentDetailTax";

    private final MCPaymentDetailTaxService mCPaymentDetailTaxService;

    public MCPaymentDetailTaxResource(MCPaymentDetailTaxService mCPaymentDetailTaxService) {
        this.mCPaymentDetailTaxService = mCPaymentDetailTaxService;
    }

    /**
     * POST  /m-c-payment-detail-taxes : Create a new mCPaymentDetailTax.
     *
     * @param mCPaymentDetailTax the mCPaymentDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPaymentDetailTax, or with status 400 (Bad Request) if the mCPaymentDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payment-detail-taxes")
    @Timed
    public ResponseEntity<MCPaymentDetailTax> createMCPaymentDetailTax(@Valid @RequestBody MCPaymentDetailTax mCPaymentDetailTax) throws URISyntaxException {
        log.debug("REST request to save MCPaymentDetailTax : {}", mCPaymentDetailTax);
        if (mCPaymentDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new mCPaymentDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentDetailTax result = mCPaymentDetailTaxService.save(mCPaymentDetailTax);
        return ResponseEntity.created(new URI("/api/m-c-payment-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-payment-detail-taxes : Updates an existing mCPaymentDetailTax.
     *
     * @param mCPaymentDetailTax the mCPaymentDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPaymentDetailTax,
     * or with status 400 (Bad Request) if the mCPaymentDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the mCPaymentDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payment-detail-taxes")
    @Timed
    public ResponseEntity<MCPaymentDetailTax> updateMCPaymentDetailTax(@Valid @RequestBody MCPaymentDetailTax mCPaymentDetailTax) throws URISyntaxException {
        log.debug("REST request to update MCPaymentDetailTax : {}", mCPaymentDetailTax);
        if (mCPaymentDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentDetailTax result = mCPaymentDetailTaxService.save(mCPaymentDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPaymentDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payment-detail-taxes : get all the mCPaymentDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPaymentDetailTaxes in body
     */
    @GetMapping("/m-c-payment-detail-taxes")
    @Timed
    public ResponseEntity<List<MCPaymentDetailTax>> getAllMCPaymentDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MCPaymentDetailTaxes");
        Page<MCPaymentDetailTax> page = mCPaymentDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payment-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payment-detail-taxes/:id : get the "id" mCPaymentDetailTax.
     *
     * @param id the id of the mCPaymentDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPaymentDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payment-detail-taxes/{id}")
    @Timed
    public ResponseEntity<MCPaymentDetailTax> getMCPaymentDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get MCPaymentDetailTax : {}", id);
        Optional<MCPaymentDetailTax> mCPaymentDetailTax = mCPaymentDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPaymentDetailTax);
    }

    /**
     * DELETE  /m-c-payment-detail-taxes/:id : delete the "id" mCPaymentDetailTax.
     *
     * @param id the id of the mCPaymentDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payment-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPaymentDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete MCPaymentDetailTax : {}", id);
        mCPaymentDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
