package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBCreditCardDetailTax;
import vn.softdreams.ebweb.service.MBCreditCardDetailTaxService;
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
 * REST controller for managing MBCreditCardDetailTax.
 */
@RestController
@RequestMapping("/api")
public class MBCreditCardDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailTaxResource.class);

    private static final String ENTITY_NAME = "mBCreditCardDetailTax";

    private final MBCreditCardDetailTaxService mBCreditCardDetailTaxService;

    public MBCreditCardDetailTaxResource(MBCreditCardDetailTaxService mBCreditCardDetailTaxService) {
        this.mBCreditCardDetailTaxService = mBCreditCardDetailTaxService;
    }

    /**
     * POST  /mb-credit-card-detail-taxes : Create a new mBCreditCardDetailTax.
     *
     * @param mBCreditCardDetailTax the mBCreditCardDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBCreditCardDetailTax, or with status 400 (Bad Request) if the mBCreditCardDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-b-credit-card-detail-taxes")
    @Timed
    public ResponseEntity<MBCreditCardDetailTax> createMBCreditCardDetailTax(@Valid @RequestBody MBCreditCardDetailTax mBCreditCardDetailTax) throws URISyntaxException {
        log.debug("REST request to save MBCreditCardDetailTax : {}", mBCreditCardDetailTax);
        if (mBCreditCardDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new mBCreditCardDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBCreditCardDetailTax result = mBCreditCardDetailTaxService.save(mBCreditCardDetailTax);
        return ResponseEntity.created(new URI("/api/mb-credit-card-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-credit-card-detail-taxes : Updates an existing mBCreditCardDetailTax.
     *
     * @param mBCreditCardDetailTax the mBCreditCardDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBCreditCardDetailTax,
     * or with status 400 (Bad Request) if the mBCreditCardDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the mBCreditCardDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-b-credit-card-detail-taxes")
    @Timed
    public ResponseEntity<MBCreditCardDetailTax> updateMBCreditCardDetailTax(@Valid @RequestBody MBCreditCardDetailTax mBCreditCardDetailTax) throws URISyntaxException {
        log.debug("REST request to update MBCreditCardDetailTax : {}", mBCreditCardDetailTax);
        if (mBCreditCardDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBCreditCardDetailTax result = mBCreditCardDetailTaxService.save(mBCreditCardDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBCreditCardDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-credit-card-detail-taxes : get all the mBCreditCardDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBCreditCardDetailTaxes in body
     */
    @GetMapping("/m-b-credit-card-detail-taxes")
    @Timed
    public ResponseEntity<List<MBCreditCardDetailTax>> getAllMBCreditCardDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MBCreditCardDetailTaxes");
        Page<MBCreditCardDetailTax> page = mBCreditCardDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-credit-card-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-credit-card-detail-taxes/:id : get the "id" mBCreditCardDetailTax.
     *
     * @param id the id of the mBCreditCardDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBCreditCardDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/m-b-credit-card-detail-taxes/{id}")
    @Timed
    public ResponseEntity<MBCreditCardDetailTax> getMBCreditCardDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get MBCreditCardDetailTax : {}", id);
        Optional<MBCreditCardDetailTax> mBCreditCardDetailTax = mBCreditCardDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBCreditCardDetailTax);
    }

    /**
     * DELETE  /mb-credit-card-detail-taxes/:id : delete the "id" mBCreditCardDetailTax.
     *
     * @param id the id of the mBCreditCardDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-b-credit-card-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBCreditCardDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete MBCreditCardDetailTax : {}", id);
        mBCreditCardDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
