package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBInternalTransfer;
import vn.softdreams.ebweb.service.MBInternalTransferService;
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
 * REST controller for managing MBInternalTransfer.
 */
@RestController
@RequestMapping("/api")
public class MBInternalTransferResource {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferResource.class);

    private static final String ENTITY_NAME = "mBInternalTransfer";

    private final MBInternalTransferService mBInternalTransferService;

    public MBInternalTransferResource(MBInternalTransferService mBInternalTransferService) {
        this.mBInternalTransferService = mBInternalTransferService;
    }

    /**
     * POST  /mb-internal-transfers : Create a new mBInternalTransfer.
     *
     * @param mBInternalTransfer the mBInternalTransfer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBInternalTransfer, or with status 400 (Bad Request) if the mBInternalTransfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-internal-transfers")
    @Timed
    public ResponseEntity<MBInternalTransfer> createMBInternalTransfer(@Valid @RequestBody MBInternalTransfer mBInternalTransfer) throws URISyntaxException {
        log.debug("REST request to save MBInternalTransfer : {}", mBInternalTransfer);
        if (mBInternalTransfer.getId() != null) {
            throw new BadRequestAlertException("A new mBInternalTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBInternalTransfer result = mBInternalTransferService.save(mBInternalTransfer);
        return ResponseEntity.created(new URI("/api/mb-internal-transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-internal-transfers : Updates an existing mBInternalTransfer.
     *
     * @param mBInternalTransfer the mBInternalTransfer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBInternalTransfer,
     * or with status 400 (Bad Request) if the mBInternalTransfer is not valid,
     * or with status 500 (Internal Server Error) if the mBInternalTransfer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-internal-transfers")
    @Timed
    public ResponseEntity<MBInternalTransfer> updateMBInternalTransfer(@Valid @RequestBody MBInternalTransfer mBInternalTransfer) throws URISyntaxException {
        log.debug("REST request to update MBInternalTransfer : {}", mBInternalTransfer);
        if (mBInternalTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBInternalTransfer result = mBInternalTransferService.save(mBInternalTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBInternalTransfer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-internal-transfers : get all the mBInternalTransfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBInternalTransfers in body
     */
    @GetMapping("/mb-internal-transfers")
    @Timed
    public ResponseEntity<List<MBInternalTransfer>> getAllMBInternalTransfers(Pageable pageable) {
        log.debug("REST request to get a page of MBInternalTransfers");
        Page<MBInternalTransfer> page = mBInternalTransferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-internal-transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-internal-transfers/:id : get the "id" mBInternalTransfer.
     *
     * @param id the id of the mBInternalTransfer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBInternalTransfer, or with status 404 (Not Found)
     */
    @GetMapping("/mb-internal-transfers/{id}")
    @Timed
    public ResponseEntity<MBInternalTransfer> getMBInternalTransfer(@PathVariable UUID id) {
        log.debug("REST request to get MBInternalTransfer : {}", id);
        Optional<MBInternalTransfer> mBInternalTransfer = mBInternalTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBInternalTransfer);
    }

    /**
     * DELETE  /mb-internal-transfers/:id : delete the "id" mBInternalTransfer.
     *
     * @param id the id of the mBInternalTransfer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-internal-transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBInternalTransfer(@PathVariable UUID id) {
        log.debug("REST request to delete MBInternalTransfer : {}", id);
        mBInternalTransferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
