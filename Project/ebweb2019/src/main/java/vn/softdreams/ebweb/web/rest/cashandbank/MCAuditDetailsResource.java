package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCAuditDetails;
import vn.softdreams.ebweb.service.MCAuditDetailsService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MCAuditDetails.
 */
@RestController
@RequestMapping("/api")
public class MCAuditDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MCAuditDetailsResource.class);

    private static final String ENTITY_NAME = "mCAuditDetails";

    private final MCAuditDetailsService mCAuditDetailsService;

    public MCAuditDetailsResource(MCAuditDetailsService mCAuditDetailsService) {
        this.mCAuditDetailsService = mCAuditDetailsService;
    }

    /**
     * POST  /mc-audit-details : Create a new mCAuditDetails.
     *
     * @param mCAuditDetails the mCAuditDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCAuditDetails, or with status 400 (Bad Request) if the mCAuditDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mc-audit-details")
    @Timed
    public ResponseEntity<MCAuditDetails> createMCAuditDetails(@RequestBody MCAuditDetails mCAuditDetails) throws URISyntaxException {
        log.debug("REST request to save MCAuditDetails : {}", mCAuditDetails);
        if (mCAuditDetails.getId() != null) {
            throw new BadRequestAlertException("A new mCAuditDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCAuditDetails result = mCAuditDetailsService.save(mCAuditDetails);
        return ResponseEntity.created(new URI("/api/mc-audit-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mc-audit-details : Updates an existing mCAuditDetails.
     *
     * @param mCAuditDetails the mCAuditDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCAuditDetails,
     * or with status 400 (Bad Request) if the mCAuditDetails is not valid,
     * or with status 500 (Internal Server Error) if the mCAuditDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mc-audit-details")
    @Timed
    public ResponseEntity<MCAuditDetails> updateMCAuditDetails(@RequestBody MCAuditDetails mCAuditDetails) throws URISyntaxException {
        log.debug("REST request to update MCAuditDetails : {}", mCAuditDetails);
        if (mCAuditDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCAuditDetails result = mCAuditDetailsService.save(mCAuditDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCAuditDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mc-audit-details : get all the mCAuditDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCAuditDetails in body
     */
    @GetMapping("/mc-audit-details")
    @Timed
    public ResponseEntity<List<MCAuditDetails>> getAllMCAuditDetails(Pageable pageable) {
        log.debug("REST request to get a page of MCAuditDetails");
        Page<MCAuditDetails> page = mCAuditDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mc-audit-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mc-audit-details/:id : get the "id" mCAuditDetails.
     *
     * @param id the id of the mCAuditDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCAuditDetails, or with status 404 (Not Found)
     */
    @GetMapping("/mc-audit-details/{id}")
    @Timed
    public ResponseEntity<MCAuditDetails> getMCAuditDetails(@PathVariable UUID id) {
        log.debug("REST request to get MCAuditDetails : {}", id);
        Optional<MCAuditDetails> mCAuditDetails = mCAuditDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCAuditDetails);
    }

    /**
     * DELETE  /mc-audit-details/:id : delete the "id" mCAuditDetails.
     *
     * @param id the id of the mCAuditDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mc-audit-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCAuditDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MCAuditDetails : {}", id);
        mCAuditDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
