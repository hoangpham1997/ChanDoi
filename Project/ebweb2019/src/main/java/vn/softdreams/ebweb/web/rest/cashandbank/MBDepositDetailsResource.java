package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBDepositDetails;
import vn.softdreams.ebweb.service.MBDepositDetailsService;
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
 * REST controller for managing MBDepositDetails.
 */
@RestController
@RequestMapping("/api")
public class MBDepositDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailsResource.class);

    private static final String ENTITY_NAME = "mBDepositDetails";

    private final MBDepositDetailsService mBDepositDetailsService;

    public MBDepositDetailsResource(MBDepositDetailsService mBDepositDetailsService) {
        this.mBDepositDetailsService = mBDepositDetailsService;
    }

    /**
     * POST  /mb-deposit-details : Create a new mBDepositDetails.
     *
     * @param mBDepositDetails the mBDepositDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBDepositDetails, or with status 400 (Bad Request) if the mBDepositDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-deposit-details")
    @Timed
    public ResponseEntity<MBDepositDetails> createMBDepositDetails(@Valid @RequestBody MBDepositDetails mBDepositDetails) throws URISyntaxException {
        log.debug("REST request to save MBDepositDetails : {}", mBDepositDetails);
        if (mBDepositDetails.getId() != null) {
            throw new BadRequestAlertException("A new mBDepositDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBDepositDetails result = mBDepositDetailsService.save(mBDepositDetails);
        return ResponseEntity.created(new URI("/api/mb-deposit-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-deposit-details : Updates an existing mBDepositDetails.
     *
     * @param mBDepositDetails the mBDepositDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBDepositDetails,
     * or with status 400 (Bad Request) if the mBDepositDetails is not valid,
     * or with status 500 (Internal Server Error) if the mBDepositDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-deposit-details")
    @Timed
    public ResponseEntity<MBDepositDetails> updateMBDepositDetails(@Valid @RequestBody MBDepositDetails mBDepositDetails) throws URISyntaxException {
        log.debug("REST request to update MBDepositDetails : {}", mBDepositDetails);
        if (mBDepositDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBDepositDetails result = mBDepositDetailsService.save(mBDepositDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBDepositDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-deposit-details : get all the mBDepositDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBDepositDetails in body
     */
    @GetMapping("/mb-deposit-details")
    @Timed
    public ResponseEntity<List<MBDepositDetails>> getAllMBDepositDetails(Pageable pageable) {
        log.debug("REST request to get a page of MBDepositDetails");
        Page<MBDepositDetails> page = mBDepositDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-deposit-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-deposit-details/:id : get the "id" mBDepositDetails.
     *
     * @param id the id of the mBDepositDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBDepositDetails, or with status 404 (Not Found)
     */
    @GetMapping("/mb-deposit-details/{id}")
    @Timed
    public ResponseEntity<MBDepositDetails> getMBDepositDetails(@PathVariable UUID id) {
        log.debug("REST request to get MBDepositDetails : {}", id);
        Optional<MBDepositDetails> mBDepositDetails = mBDepositDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBDepositDetails);
    }

    /**
     * DELETE  /mb-deposit-details/:id : delete the "id" mBDepositDetails.
     *
     * @param id the id of the mBDepositDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-deposit-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBDepositDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MBDepositDetails : {}", id);
        mBDepositDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
