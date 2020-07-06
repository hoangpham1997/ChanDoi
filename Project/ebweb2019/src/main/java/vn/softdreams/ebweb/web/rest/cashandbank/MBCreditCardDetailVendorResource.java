package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBCreditCardDetailVendor;
import vn.softdreams.ebweb.service.MBCreditCardDetailVendorService;
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
 * REST controller for managing MBCreditCardDetailVendor.
 */
@RestController
@RequestMapping("/api")
public class MBCreditCardDetailVendorResource {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailVendorResource.class);

    private static final String ENTITY_NAME = "mBCreditCardDetailVendor";

    private final MBCreditCardDetailVendorService mBCreditCardDetailVendorService;

    public MBCreditCardDetailVendorResource(MBCreditCardDetailVendorService mBCreditCardDetailVendorService) {
        this.mBCreditCardDetailVendorService = mBCreditCardDetailVendorService;
    }

    /**
     * POST  /mb-credit-card-detail-vendors : Create a new mBCreditCardDetailVendor.
     *
     * @param mBCreditCardDetailVendor the mBCreditCardDetailVendor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBCreditCardDetailVendor, or with status 400 (Bad Request) if the mBCreditCardDetailVendor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-credit-card-detail-vendors")
    @Timed
    public ResponseEntity<MBCreditCardDetailVendor> createMBCreditCardDetailVendor(@Valid @RequestBody MBCreditCardDetailVendor mBCreditCardDetailVendor) throws URISyntaxException {
        log.debug("REST request to save MBCreditCardDetailVendor : {}", mBCreditCardDetailVendor);
        if (mBCreditCardDetailVendor.getId() != null) {
            throw new BadRequestAlertException("A new mBCreditCardDetailVendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBCreditCardDetailVendor result = mBCreditCardDetailVendorService.save(mBCreditCardDetailVendor);
        return ResponseEntity.created(new URI("/api/mb-credit-card-detail-vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-credit-card-detail-vendors : Updates an existing mBCreditCardDetailVendor.
     *
     * @param mBCreditCardDetailVendor the mBCreditCardDetailVendor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBCreditCardDetailVendor,
     * or with status 400 (Bad Request) if the mBCreditCardDetailVendor is not valid,
     * or with status 500 (Internal Server Error) if the mBCreditCardDetailVendor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-credit-card-detail-vendors")
    @Timed
    public ResponseEntity<MBCreditCardDetailVendor> updateMBCreditCardDetailVendor(@Valid @RequestBody MBCreditCardDetailVendor mBCreditCardDetailVendor) throws URISyntaxException {
        log.debug("REST request to update MBCreditCardDetailVendor : {}", mBCreditCardDetailVendor);
        if (mBCreditCardDetailVendor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBCreditCardDetailVendor result = mBCreditCardDetailVendorService.save(mBCreditCardDetailVendor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBCreditCardDetailVendor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-credit-card-detail-vendors : get all the mBCreditCardDetailVendors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBCreditCardDetailVendors in body
     */
    @GetMapping("/mb-credit-card-detail-vendors")
    @Timed
    public ResponseEntity<List<MBCreditCardDetailVendor>> getAllMBCreditCardDetailVendors(Pageable pageable) {
        log.debug("REST request to get a page of MBCreditCardDetailVendors");
        Page<MBCreditCardDetailVendor> page = mBCreditCardDetailVendorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-credit-card-detail-vendors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-credit-card-detail-vendors/:id : get the "id" mBCreditCardDetailVendor.
     *
     * @param id the id of the mBCreditCardDetailVendor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBCreditCardDetailVendor, or with status 404 (Not Found)
     */
    @GetMapping("/mb-credit-card-detail-vendors/{id}")
    @Timed
    public ResponseEntity<MBCreditCardDetailVendor> getMBCreditCardDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to get MBCreditCardDetailVendor : {}", id);
        Optional<MBCreditCardDetailVendor> mBCreditCardDetailVendor = mBCreditCardDetailVendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBCreditCardDetailVendor);
    }

    /**
     * DELETE  /mb-credit-card-detail-vendors/:id : delete the "id" mBCreditCardDetailVendor.
     *
     * @param id the id of the mBCreditCardDetailVendor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-credit-card-detail-vendors/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBCreditCardDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to delete MBCreditCardDetailVendor : {}", id);
        mBCreditCardDetailVendorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
