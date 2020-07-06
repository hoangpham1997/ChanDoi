package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MCReceiptService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.MCReceiptSaveDTO;
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
 * REST controller for managing MCReceipt.
 */
@RestController
@RequestMapping("/api")
public class MCReceiptResource {

    private final Logger log = LoggerFactory.getLogger(MCReceiptResource.class);

    private static final String ENTITY_NAME = "mCReceipt";

    private final MCReceiptService mCReceiptService;

    public MCReceiptResource(MCReceiptService mCReceiptService) {
        this.mCReceiptService = mCReceiptService;
    }

    /**
     * POST  /m-c-receipts : Create a new mCReceipt.
     *
     * @param mCReceipt the mCReceipt to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCReceipt, or with status 400 (Bad Request) if the mCReceipt has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-receipts")
    @Timed
    public ResponseEntity<MCReceiptSaveDTO> createMCReceipt(@Valid @RequestBody MCReceipt mCReceipt) throws URISyntaxException {
        log.debug("REST request to save MCReceipt : {}", mCReceipt);
        if (mCReceipt.getId() != null) {
            throw new BadRequestAlertException("A new mCReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCReceiptSaveDTO result = new MCReceiptSaveDTO();
        result = mCReceiptService.saveDTO(mCReceipt);

        if (result.getmCReceipt().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/m-c-receipts/" + result.getmCReceipt().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getmCReceipt().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /m-c-receipts : Updates an existing mCReceipt.
     *
     * @param mCReceipt the mCReceipt to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCReceipt,
     * or with status 400 (Bad Request) if the mCReceipt is not valid,
     * or with status 500 (Internal Server Error) if the mCReceipt couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-receipts")
    @Timed
    public ResponseEntity<MCReceiptSaveDTO> updateMCReceipt(@Valid @RequestBody MCReceipt mCReceipt) throws URISyntaxException {
        log.debug("REST request to update MCReceipt : {}", mCReceipt);
        if (mCReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCReceiptSaveDTO result = new MCReceiptSaveDTO();
        result = mCReceiptService.saveDTO(mCReceipt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCReceipt.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-receipts : get all the mCReceipts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCReceipts in body
     */
    @GetMapping("/m-c-receipts")
    @Timed
    public ResponseEntity<List<MCReceipt>> getAllMCReceipts(Pageable pageable) {
        log.debug("REST request to get a page of MCReceipts");
        Page<MCReceipt> page = mCReceiptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-receipts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-receipts/:id : get the "id" mCReceipt.
     *
     * @param id the id of the mCReceipt to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCReceipt, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-receipts/{id}")
    @Timed
    public ResponseEntity<MCReceipt> getMCReceipt(@PathVariable UUID id) {
        log.debug("REST request to get MCReceipt : {}", id);
        Optional<MCReceipt> mCReceipt = mCReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCReceipt);
    }

    /**
     * DELETE  /m-c-receipts/:id : delete the "id" mCReceipt.
     *
     * @param id the id of the mCReceipt to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-receipts/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCReceipt(@PathVariable UUID id) {
        log.debug("REST request to delete MCReceipt : {}", id);
        mCReceiptService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/m-c-receiptsDTO")
    @Timed
    public ResponseEntity<List<MCReceiptDTO>> getAllMCReceiptsDTO(Pageable pageable) {
        log.debug("REST request to get a page of MCReceiptsDTO");
        Page<MCReceiptDTO> page = mCReceiptService.findAllDTOByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-receiptsDTO");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/m-c-receipts/multi-unrecord")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnrecordMCReceipt(@Valid @RequestBody List<MCReceipt> mcReceipts) {
        log.debug("REST request to multiUnrecordMCReceipt : {}", mcReceipts);
        HandlingResultDTO responeCloseBookDTO = mCReceiptService.multiUnrecord(mcReceipts);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/m-c-receipts/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMCReceipt(@Valid @RequestBody List<MCReceipt> mcReceipts) {
        log.debug("REST request to MultiDeleteMCReceipt : {}", mcReceipts);
        HandlingResultDTO responeCloseBookDTO = mCReceiptService.multiDelete(mcReceipts);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
