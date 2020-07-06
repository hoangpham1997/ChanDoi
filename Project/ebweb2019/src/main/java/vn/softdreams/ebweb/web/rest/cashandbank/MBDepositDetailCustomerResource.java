package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBDepositDetailCustomer;
import vn.softdreams.ebweb.service.MBDepositDetailCustomerService;
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
 * REST controller for managing MBDepositDetailCustomer.
 */
@RestController
@RequestMapping("/api")
public class MBDepositDetailCustomerResource {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailCustomerResource.class);

    private static final String ENTITY_NAME = "mBDepositDetailCustomer";

    private final MBDepositDetailCustomerService mBDepositDetailCustomerService;

    public MBDepositDetailCustomerResource(MBDepositDetailCustomerService mBDepositDetailCustomerService) {
        this.mBDepositDetailCustomerService = mBDepositDetailCustomerService;
    }

    /**
     * POST  /m-b-deposit-detail-customers : Create a new mBDepositDetailCustomer.
     *
     * @param mBDepositDetailCustomer the mBDepositDetailCustomer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBDepositDetailCustomer, or with status 400 (Bad Request) if the mBDepositDetailCustomer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-b-deposit-detail-customers")
    @Timed
    public ResponseEntity<MBDepositDetailCustomer> createMBDepositDetailCustomer(@Valid @RequestBody MBDepositDetailCustomer mBDepositDetailCustomer) throws URISyntaxException {
        log.debug("REST request to save MBDepositDetailCustomer : {}", mBDepositDetailCustomer);
        if (mBDepositDetailCustomer.getId() != null) {
            throw new BadRequestAlertException("A new mBDepositDetailCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBDepositDetailCustomer result = mBDepositDetailCustomerService.save(mBDepositDetailCustomer);
        return ResponseEntity.created(new URI("/api/m-b-deposit-detail-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-b-deposit-detail-customers : Updates an existing mBDepositDetailCustomer.
     *
     * @param mBDepositDetailCustomer the mBDepositDetailCustomer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBDepositDetailCustomer,
     * or with status 400 (Bad Request) if the mBDepositDetailCustomer is not valid,
     * or with status 500 (Internal Server Error) if the mBDepositDetailCustomer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-b-deposit-detail-customers")
    @Timed
    public ResponseEntity<MBDepositDetailCustomer> updateMBDepositDetailCustomer(@Valid @RequestBody MBDepositDetailCustomer mBDepositDetailCustomer) throws URISyntaxException {
        log.debug("REST request to update MBDepositDetailCustomer : {}", mBDepositDetailCustomer);
        if (mBDepositDetailCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBDepositDetailCustomer result = mBDepositDetailCustomerService.save(mBDepositDetailCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBDepositDetailCustomer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-b-deposit-detail-customers : get all the mBDepositDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBDepositDetailCustomers in body
     */
    @GetMapping("/m-b-deposit-detail-customers")
    @Timed
    public ResponseEntity<List<MBDepositDetailCustomer>> getAllMBDepositDetailCustomers(Pageable pageable) {
        log.debug("REST request to get a page of MBDepositDetailCustomers");
        Page<MBDepositDetailCustomer> page = mBDepositDetailCustomerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-b-deposit-detail-customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-b-deposit-detail-customers/:id : get the "id" mBDepositDetailCustomer.
     *
     * @param id the id of the mBDepositDetailCustomer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBDepositDetailCustomer, or with status 404 (Not Found)
     */
    @GetMapping("/m-b-deposit-detail-customers/{id}")
    @Timed
    public ResponseEntity<MBDepositDetailCustomer> getMBDepositDetailCustomer(@PathVariable UUID id) {
        log.debug("REST request to get MBDepositDetailCustomer : {}", id);
        Optional<MBDepositDetailCustomer> mBDepositDetailCustomer = mBDepositDetailCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBDepositDetailCustomer);
    }

    /**
     * DELETE  /m-b-deposit-detail-customers/:id : delete the "id" mBDepositDetailCustomer.
     *
     * @param id the id of the mBDepositDetailCustomer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-b-deposit-detail-customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBDepositDetailCustomer(@PathVariable UUID id) {
        log.debug("REST request to delete MBDepositDetailCustomer : {}", id);
        mBDepositDetailCustomerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
