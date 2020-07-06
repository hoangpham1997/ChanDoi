package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.CPExpenseTranferService;
import vn.softdreams.ebweb.service.dto.CPExpenseTranferSaveDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CPExpenseTranfer.
 */
@RestController
@RequestMapping("/api")
public class CPExpenseTranferResource {

    private final Logger log = LoggerFactory.getLogger(CPExpenseTranferResource.class);

    private static final String ENTITY_NAME = "cPExpenseTranfer";

    private final CPExpenseTranferService cPExpenseTranferService;

    public CPExpenseTranferResource(CPExpenseTranferService cPExpenseTranferService) {
        this.cPExpenseTranferService = cPExpenseTranferService;
    }

    /**
     * POST  /c-p-expense-tranfers : Create a new cPExpenseTranfer.
     *
     * @param cPExpenseTranfer the cPExpenseTranfer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPExpenseTranfer, or with status 400 (Bad Request) if the cPExpenseTranfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/c-p-expense-tranfers")
    @Timed
    public ResponseEntity<CPExpenseTranferSaveDTO> createCPExpenseTranfer(@RequestBody CPExpenseTranfer cPExpenseTranfer) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", cPExpenseTranfer);
        if (cPExpenseTranfer.getId() != null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPExpenseTranferSaveDTO result = new CPExpenseTranferSaveDTO();
        result = cPExpenseTranferService.saveDTO(cPExpenseTranfer);
        if (result.getCpExpenseTranfer() != null) {
            if (result.getCpExpenseTranfer().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/g-other-voucher/" + result.getCpExpenseTranfer().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCpExpenseTranfer().getId().toString()))
                    .body(result);
            }
        } else {
            return null;
        }
    }

    /**
     * PUT  /c-p-expense-tranfers : Updates an existing cPExpenseTranfer.
     *
     * @param cPExpenseTranfer the cPExpenseTranfer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPExpenseTranfer,
     * or with status 400 (Bad Request) if the cPExpenseTranfer is not valid,
     * or with status 500 (Internal Server Error) if the cPExpenseTranfer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/c-p-expense-tranfers")
    @Timed
    public ResponseEntity<CPExpenseTranferSaveDTO> updateCPExpenseTranfer(@RequestBody CPExpenseTranfer cPExpenseTranfer) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucher : {}", cPExpenseTranfer);
        if (cPExpenseTranfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPExpenseTranferSaveDTO result = new CPExpenseTranferSaveDTO();
        try {
            result = cPExpenseTranferService.saveDTO(cPExpenseTranfer);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPExpenseTranfer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /c-p-expense-tranfers : get all the cPExpenseTranfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPExpenseTranfers in body
     */
//    @GetMapping("/c-p-expense-tranfers")
//    @Timed
//    public ResponseEntity<List<CPExpenseTranfer>> getAllCPExpenseTranfers(Pageable pageable) {
//        log.debug("REST request to get a page of CPExpenseTranfers");
//        Page<CPExpenseTranfer> page = cPExpenseTranferService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-expense-tranfers");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }

    /**
     * GET  /c-p-expense-tranfers/:id : get the "id" cPExpenseTranfer.
     *
     * @param id the id of the cPExpenseTranfer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPExpenseTranfer, or with status 404 (Not Found)
     */
    @GetMapping("/c-p-expense-tranfers/{id}")
    @Timed
    public ResponseEntity<CPExpenseTranfer> getCPExpenseTranfer(@PathVariable UUID id) {
        log.debug("REST request to get CPExpenseTranfer : {}", id);
        Optional<CPExpenseTranfer> cPExpenseTranfer = cPExpenseTranferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPExpenseTranfer);
    }

    /**
     * DELETE  /c-p-expense-tranfers/:id : delete the "id" cPExpenseTranfer.
     *
     * @param id the id of the cPExpenseTranfer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-p-expense-tranfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPExpenseTranfer(@PathVariable UUID id) {
        log.debug("REST request to delete CPExpenseTranfer : {}", id);
        cPExpenseTranferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/c-p-expense-tranfers/get-all-c-p-expense-tranfer-details")
    @Timed
    public ResponseEntity<List<KetChuyenChiPhiDTO>> getCPExpenseTransferByCPPeriodID(@RequestParam(required = false) UUID cPPeriodID) {
        log.debug("REST request to get a page of Accounts");
        List<KetChuyenChiPhiDTO> page = cPExpenseTranferService.getCPExpenseTransferByCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/c-p-expense-tranfers/find-by-c-p-period-id")
    @Timed
    public ResponseEntity<Void> checkExistCPPeriodID(@RequestParam(required = false) UUID cPPeriodID) {
        log.debug("REST request to get a page of Accounts");
        cPExpenseTranferService.checkExistCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/c-p-expense-tranfers/search-all")
    @Timed
    public ResponseEntity<List<CPExpenseTranfer>> findAll(Pageable pageable,
                                                          @RequestParam(required = false) String searchVoucher) {
        log.debug("REST request to get a page of mBDeposits");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<CPExpenseTranfer> page = cPExpenseTranferService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-expense-tranfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/c-p-expense-tranfers/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPDF(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = cPExpenseTranferService.exportPDF(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/c-p-expense-tranfers/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = cPExpenseTranferService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @PostMapping("/c-p-expense-tranfers/multi-unrecord-c-p-expense-tranfers")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnrecordCPExpenseTranfer(@Valid @RequestBody List<CPExpenseTranfer> cpExpenseTranfers) {
        log.debug("REST request to closeBook : {}", cpExpenseTranfers);
        HandlingResultDTO responeCloseBookDTO = cPExpenseTranferService.multiUnRecord(cpExpenseTranfers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/c-p-expense-tranfers/multi-delete-c-p-expense-tranfers")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMBDeposit(@Valid @RequestBody List<CPExpenseTranfer> cpExpenseTranfers) {
        log.debug("REST request to closeBook : {}", cpExpenseTranfers);
        HandlingResultDTO responeCloseBookDTO = cPExpenseTranferService.multiDelete(cpExpenseTranfers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
