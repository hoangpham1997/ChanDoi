package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.MCPaymentRepository;
import vn.softdreams.ebweb.repository.MCReceiptRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MCAuditService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.MCAuditDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.MCAuditSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;
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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MCAudit.
 */
@RestController
@RequestMapping("/api")
public class MCAuditResource {

    private final Logger log = LoggerFactory.getLogger(MCAuditResource.class);

    private static final String ENTITY_NAME = "mCAudit";

    private final MCAuditService mCAuditService;
    @Autowired
    UtilsRepository utilsRepository;

    @Autowired
    MCPaymentRepository mcPaymentRepository;

    @Autowired
    MCReceiptRepository mcReceiptRepository;

    public MCAuditResource(MCAuditService mCAuditService) {
        this.mCAuditService = mCAuditService;
    }

    /**
     * POST  /mc-audits : Create a new mCAudit.
     *
     * @param mCAudit the mCAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCAudit, or with status 400 (Bad Request) if the mCAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mc-audits")
    @Timed
    public ResponseEntity<MCAuditSaveDTO> createMCAudit(@Valid @RequestBody MCAudit mCAudit) throws URISyntaxException {
        log.debug("REST request to save MCAudit : {}", mCAudit);
        if (mCAudit.getId() != null) {
            throw new BadRequestAlertException("A new mCAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCAuditSaveDTO result = new MCAuditSaveDTO();
        result = mCAuditService.saveDTO(mCAudit);
        if (result.getMcAudit().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/mc-audits/" + result.getMcAudit().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getMcAudit().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /mc-audits : Updates an existing mCAudit.
     *
     * @param mCAudit the mCAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCAudit,
     * or with status 400 (Bad Request) if the mCAudit is not valid,
     * or with status 500 (Internal Server Error) if the mCAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mc-audits")
    @Timed
    public ResponseEntity<MCAuditSaveDTO> updateMCAudit(@Valid @RequestBody MCAudit mCAudit) throws URISyntaxException {
        log.debug("REST request to update MCAudit : {}", mCAudit);
        if (mCAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCAuditSaveDTO result = new MCAuditSaveDTO();
        try {
            result = mCAuditService.saveDTO(mCAudit);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mc-audits : get all the mCAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCAudits in body
     */
    @GetMapping("/mc-audits")
    @Timed
    public ResponseEntity<List<MCAudit>> getAllMCAudits(Pageable pageable) {
        log.debug("REST request to get a page of MCAudits");
        Page<MCAudit> page = mCAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mc-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mc-audits/:id : get the "id" mCAudit.
     *
     * @param id the id of the mCAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCAudit, or with status 404 (Not Found)
     */
    @GetMapping("/mc-audits/{id}")
    @Timed
    public ResponseEntity<MCAudit> getMCAudit(@PathVariable UUID id) {
        log.debug("REST request to get MCAudit : {}", id);
        Optional<MCAudit> mCAudit = mCAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCAudit);
    }

    /**
     * DELETE  /mc-audits/:id : delete the "id" mCAudit.
     *
     * @param id the id of the mCAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mc-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCAudit(@PathVariable UUID id) {
        log.debug("REST request to delete MCAudit : {}", id);
        mCAuditService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /mc-audits/:id : delete the "id" mCAudit.
     *
     * @param id the id of the mCAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/mc-audits/deleteRef/{id}")
    @Timed
    public ResponseEntity<Void> editMCAudit(@PathVariable UUID id) {
        log.debug("REST request to delete MCAudit : {}", id);
        mCAuditService.edit(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/mc-audits/index")
    @Timed
    public ResponseEntity<MCAudit> findByRowNum(@RequestParam(required = false) String currencyID,
                                                  @RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  @RequestParam(required = false) String keySearch,
                                                  @RequestParam(required = false) Integer rowNum
    ) {
        log.debug("REST request to get a page of findByRowNum");
        MCAudit ppDiscountReturn = mCAuditService.findByRowNum(currencyID, fromDate,
            toDate, keySearch, rowNum);
        return new ResponseEntity<>(ppDiscountReturn, HttpStatus.OK);
    }

    @GetMapping("/mc-audit-objects-search")
    @Timed
    public ResponseEntity<List<MCAuditDTO>> searchMCAudit(Pageable pageable,
                                                                  @RequestParam(required = false) String currencyID,
                                                                  @RequestParam(required = false) String fromDate,
                                                                  @RequestParam(required = false) String toDate,
                                                                  @RequestParam(required = false) String keySearch)
    {
        log.debug("REST request to get a page of SAInvoices");
        Page<MCAuditDTO> page = mCAuditService.searchMCAudit(pageable, currencyID, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mc-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/mc-audits/findMCReceiptByMCAuditID")
    @Timed
    public ResponseEntity<MCReceipt> findMCReceiptByMCAuditID(@RequestParam(required = false) UUID ID) {
        MCReceipt mcReceipt = mcReceiptRepository.findByAuditID(ID);
        return new ResponseEntity<MCReceipt>(mcReceipt, HttpStatus.OK);
    }

    @GetMapping("/mc-audits/findMCPaymentByMCAuditID")
    @Timed
    public ResponseEntity<MCPayment> findMCPaymentByMCAuditID(@RequestParam(required = false) UUID ID) {
        MCPayment mcPayment = mcPaymentRepository.findByAuditID(ID);
        return new ResponseEntity<MCPayment>(mcPayment, HttpStatus.OK);
    }

    /**
     * chuongnv
     * GET  /mc-audits : get all the mCAuditsDTO.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPayments in body
     */
    @GetMapping("/mc-auditsDTO")
    @Timed
    public ResponseEntity<List<MCAuditDTO>> getAllMCAuditsDTO(Pageable pageable) {
        log.debug("REST request to get a page of MCAudits");
        Page<MCAuditDTO> page = mCAuditService.findAllDTOByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mc-auditsDTO");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/mc-audits/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/mcaudit.xlsx");
        byte[] fileContent = Files.readAllBytes(reportFile.toPath());
        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }

    @GetMapping(value = "/mc-audits/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(Pageable pageable,
                                              @RequestParam(required = false) String currencyID,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String textSearch) throws IOException {
        byte[] export = mCAuditService.exportExcel(pageable, currencyID, fromDate, toDate, textSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/mc-audits/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(Pageable pageable,
                                            @RequestParam(required = false) String currencyID,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String textSearch) throws IOException {
        byte[] export = mCAuditService.exportPdf(pageable, currencyID, fromDate, toDate, textSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping("/mc-audits/by-id")
    @Timed
    public ResponseEntity<MCAuditDetailDTO> getMCAuditDetailsByID(@RequestParam UUID mCAuditID) {
        log.debug("REST request to get a page of SAInvoiceDetails");
        MCAuditDetailDTO mcAuditDetailDTO = mCAuditService.getMCAuditDetailsByID(mCAuditID);
        return new ResponseEntity<>(mcAuditDetailDTO, HttpStatus.OK);
    }

    @PostMapping("/mc-audits/multi-delete-mc-audits")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMCAudit(@Valid @RequestBody List<MCAudit> mcAudits) {
        log.debug("REST request to closeBook : {}", mcAudits);
        HandlingResultDTO responeCloseBookDTO = mCAuditService.multiDelete(mcAudits);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

}
