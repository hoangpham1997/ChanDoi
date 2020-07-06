package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.eclipse.jdt.internal.compiler.codegen.FloatCache;
import vn.softdreams.ebweb.domain.PPInvoice;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.PPInvoiceDetailsService;
import vn.softdreams.ebweb.service.PPInvoiceService;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvert2DTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PPInvoiceDetails.
 */
@RestController
@RequestMapping("/api")
public class PPInvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PPInvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "pPInvoiceDetails";

    private final PPInvoiceDetailsService pPInvoiceDetailsService;
    private final PPInvoiceService ppInvoiceService;

    public PPInvoiceDetailsResource(PPInvoiceDetailsService pPInvoiceDetailsService, PPInvoiceService ppInvoiceService) {
        this.pPInvoiceDetailsService = pPInvoiceDetailsService;
        this.ppInvoiceService = ppInvoiceService;
    }

    /**
     * POST  /pp-invoice-details : Create a new pPInvoiceDetails.
     *
     * @param pPInvoiceDetails the pPInvoiceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pPInvoiceDetails, or with status 400 (Bad Request) if the pPInvoiceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pp-invoice-details")
    @Timed
    public ResponseEntity<PPInvoiceDetails> createPPInvoiceDetails(@Valid @RequestBody PPInvoiceDetails pPInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to save PPInvoiceDetails : {}", pPInvoiceDetails);
        if (pPInvoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new pPInvoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPInvoiceDetails result = pPInvoiceDetailsService.save(pPInvoiceDetails);
        return ResponseEntity.created(new URI("/api/pp-invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pp-invoice-details : Updates an existing pPInvoiceDetails.
     *
     * @param pPInvoiceDetails the pPInvoiceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoiceDetails,
     * or with status 400 (Bad Request) if the pPInvoiceDetails is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoiceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoice-details")
    @Timed
    public ResponseEntity<PPInvoiceDetails> updatePPInvoiceDetails(@Valid @RequestBody PPInvoiceDetails pPInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to update PPInvoiceDetails : {}", pPInvoiceDetails);
        if (pPInvoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPInvoiceDetails result = pPInvoiceDetailsService.save(pPInvoiceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pPInvoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pp-invoice-details : get all the pPInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pPInvoiceDetails in body
     */
    @GetMapping("/pp-invoice-details")
    @Timed
    public ResponseEntity<List<PPInvoiceDetails>> getAllPPInvoiceDetails(Pageable pageable) {
        log.debug("REST request to get a page of PPInvoiceDetails");
        Page<PPInvoiceDetails> page = pPInvoiceDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoice-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pp-invoice-details/:id : get the "id" pPInvoiceDetails.
     *
     * @param id the id of the pPInvoiceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPInvoiceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/pp-invoice-details/{id}")
    @Timed
    public ResponseEntity<PPInvoiceDetails> getPPInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to get PPInvoiceDetails : {}", id);
        Optional<PPInvoiceDetails> pPInvoiceDetails = pPInvoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPInvoiceDetails);
    }

    /**
     * hàm lấy chứng từ tham chiếu
     * @author quyennc
     * @param pageable
     * @param accountingObjectID
     * @param formDate
     * @param toDate
     * @return
     */
    @GetMapping("/pp-invoice-details/get-license")
    @Timed
    public ResponseEntity<List<PPInvoiceConvertDTO>> getPPInvoiceDetailsGetLicense(Pageable pageable,
                                                                                    @RequestParam(required = false) UUID accountingObjectID,
                                                                                    @RequestParam(required = false) String formDate,
                                                                                    @RequestParam(required = false) String toDate,
                                                                                    @RequestParam(required = false) String currencyCode,
                                                                                    @RequestParam(required = false) List<UUID> listID
                                                                                   ) {

//        log.debug("REST request to get PPInvoiceDetails : {}", id);
        Page<PPInvoiceConvertDTO> page = pPInvoiceDetailsService.getPPInvoiceDetailsGetLicense(pageable, accountingObjectID, formDate, toDate, currencyCode, listID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoice-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /pp-invoice-details/:id : delete the "id" pPInvoiceDetails.
     *
     * @param id the id of the pPInvoiceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pp-invoice-details/{id}")
    @Timed
    public ResponseEntity<Void> deletePPInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to delete PPInvoiceDetails : {}", id);
        pPInvoiceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @author Huy Xoăn
     * @param materialGoodsID
     * @return
     */
    @GetMapping("/pp-invoice-details/lot-no-array/{materialGoodsID}")
    @Timed
    public ResponseEntity<List<LotNoDTO>> getLotNoArray(Pageable pageable, @PathVariable UUID materialGoodsID) {
        log.debug("REST request to get a page of PPInvoiceDetails");
        List<LotNoDTO> LotNo = pPInvoiceDetailsService.getListLotNo(materialGoodsID);
        return new ResponseEntity<>(LotNo, HttpStatus.OK);
    }


    /**
     * PUT  /pp-invoices : Updates an existing pPInvoice.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoice,
     * or with status 400 (Bad Request) if the pPInvoice is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoices-details/update-receive-bill")
    @Timed
    public ResponseEntity<Void> updatePPInvoice(@Valid @RequestBody ReceiveBill receiveBill){
        pPInvoiceDetailsService.saveReceiveBillPPInvoice(receiveBill);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * PUT  /pp-invoices : Updates an existing pPInvoice.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoice,
     * or with status 400 (Bad Request) if the pPInvoice is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoices-details/update-all-receive-bill")
    @Timed
    public ResponseEntity<Void> updateAll(@Valid @RequestBody ReceiveBill receiveBill){
        pPInvoiceDetailsService.saveAllReceiveBill(receiveBill);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * lấy số hóa đơn từ bảng pporder
     * @author congnd
     * @return
     */
    @GetMapping("/pp-invoice-detail/get-pporderno-by-id")
    @Timed
    public ResponseEntity<ResultDTO> getPPOrderNoById(@RequestParam UUID id)
    {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = pPInvoiceDetailsService.getPPOrderNoById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
