package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MCPaymentService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.web.rest.dto.MCPaymentSaveDTO;
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
 * REST controller for managing MCPayment.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentResource.class);

    private static final String ENTITY_NAME = "mCPayment";

    private final MCPaymentService mCPaymentService;

    public MCPaymentResource(MCPaymentService mCPaymentService) {
        this.mCPaymentService = mCPaymentService;
    }

    /**
     * POST  /m-c-payments : Create a new mCPayment.
     *
     * @param mCPayment the mCPayment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPayment, or with status 400 (Bad Request) if the mCPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payments")
    @Timed
    public ResponseEntity<MCPaymentSaveDTO> createMCPayment(@Valid @RequestBody MCPayment mCPayment) throws URISyntaxException {
        log.debug("REST request to save MCPayment : {}", mCPayment);
        if (mCPayment.getId() != null) {
            throw new BadRequestAlertException("A new mCPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentSaveDTO result = mCPaymentService.saveDTO(mCPayment);
        if (result.getmCPayment().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/m-c-payments/" + result.getmCPayment().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getmCPayment().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /m-c-payments : Updates an existing mCPayment.
     *
     * @param mCPayment the mCPayment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPayment,
     * or with status 400 (Bad Request) if the mCPayment is not valid,
     * or with status 500 (Internal Server Error) if the mCPayment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payments")
    @Timed
    public ResponseEntity<MCPaymentSaveDTO> updateMCPayment(@Valid @RequestBody MCPayment mCPayment) throws URISyntaxException {
        log.debug("REST request to update MCPayment : {}", mCPayment);
        if (mCPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentSaveDTO result = mCPaymentService.saveDTO(mCPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payments : get all the mCPayments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPayments in body
     */
    @GetMapping("/m-c-payments")
    @Timed
    public ResponseEntity<List<MCPayment>> getAllMCPayments(Pageable pageable) {
        log.debug("REST request to get a page of MCPayments");
        Page<MCPayment> page = mCPaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payments/:id : get the "id" mCPayment.
     *
     * @param id the id of the mCPayment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPayment, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payments/{id}")
    @Timed
    public ResponseEntity<MCPayment> getMCPayment(@PathVariable UUID id) {
        log.debug("REST request to get MCPayment : {}", id);
        Optional<MCPayment> mCPayment = mCPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPayment);
    }

    /**
     * DELETE  /m-c-payments/:id : delete the "id" mCPayment.
     *
     * @param id the id of the mCPayment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payments/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPayment(@PathVariable UUID id) {
        log.debug("REST request to delete MCPayment : {}", id);
        mCPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/m-c-payments/findMCPaymentDetails")
    @Timed
    public ResponseEntity<List<MCPaymentDetails>> findMCPaymentDetails(@RequestParam(required = true) UUID id) {
        log.debug("REST request to get MCPaymentDetails : {}", id);
        List<MCPaymentDetails> mcPaymentDetails = mCPaymentService.findMCPaymentDetails(id);
        return new ResponseEntity<>(mcPaymentDetails, HttpStatus.OK);
    }

    /**
     * Hautv
     * GET  /m-c-payments : get all the mCPaymentsDTO.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPayments in body
     */
    @GetMapping("/m-c-paymentsDTO")
    @Timed
    public ResponseEntity<List<MCPaymentDTO>> getAllMCPaymentsDTO(Pageable pageable) {
        log.debug("REST request to get a page of MCPayments");
        Page<MCPaymentDTO> page = mCPaymentService.findAllDTOByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-paymentsDTO");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/m-c-payments/multi-unrecord")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnrecordMCPayment(@Valid @RequestBody List<MCPayment> mcPayments) {
        log.debug("REST request to multiUnrecordMCPayment : {}", mcPayments);
        HandlingResultDTO responeCloseBookDTO = mCPaymentService.multiUnrecord(mcPayments);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/m-c-payments/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMCPayment(@Valid @RequestBody List<MCPayment> mcPayments) {
        log.debug("REST request to MultiDeleteMCPayment : {}", mcPayments);
        HandlingResultDTO responeCloseBookDTO = mCPaymentService.multiDelete(mcPayments);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
