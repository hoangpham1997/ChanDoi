package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.IARegisterInvoice;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.service.IARegisterInvoiceService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
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
 * REST controller for managing IARegisterInvoice.
 */
@RestController
@RequestMapping("/api")
public class IARegisterInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(IARegisterInvoiceResource.class);

    private static final String ENTITY_NAME = "iARegisterInvoice";

    private final IARegisterInvoiceService iARegisterInvoiceService;

    public IARegisterInvoiceResource(IARegisterInvoiceService iARegisterInvoiceService) {
        this.iARegisterInvoiceService = iARegisterInvoiceService;
    }

    /**
     * POST  /ia-register-invoices : Create a new iARegisterInvoice.
     *
     * @param iARegisterInvoice the iARegisterInvoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iARegisterInvoice, or with status 400 (Bad Request) if the iARegisterInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-register-invoices")
    @Timed
    public ResponseEntity<IARegisterInvoice> createIARegisterInvoice(@RequestBody IARegisterInvoice iARegisterInvoice) throws URISyntaxException {
        log.debug("REST request to save IARegisterInvoice : {}", iARegisterInvoice);
        if (iARegisterInvoice.getId() != null) {
            throw new BadRequestAlertException("A new iARegisterInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IARegisterInvoice result = iARegisterInvoiceService.save(iARegisterInvoice);
        return ResponseEntity.created(new URI("/api/ia-register-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-register-invoices : Updates an existing iARegisterInvoice.
     *
     * @param iARegisterInvoice the iARegisterInvoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iARegisterInvoice,
     * or with status 400 (Bad Request) if the iARegisterInvoice is not valid,
     * or with status 500 (Internal Server Error) if the iARegisterInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-register-invoices")
    @Timed
    public ResponseEntity<IARegisterInvoice> updateIARegisterInvoice(@RequestBody IARegisterInvoice iARegisterInvoice) throws URISyntaxException {
        log.debug("REST request to update IARegisterInvoice : {}", iARegisterInvoice);
        if (iARegisterInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IARegisterInvoice result = iARegisterInvoiceService.save(iARegisterInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iARegisterInvoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-register-invoices : get all the iARegisterInvoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of iARegisterInvoices in body
     */
    @GetMapping("/ia-register-invoices")
    @Timed
    public ResponseEntity<List<IARegisterInvoice>> getAllIARegisterInvoices(Pageable pageable) {
        log.debug("REST request to get a page of IARegisterInvoices");
        Page<IARegisterInvoice> page = iARegisterInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ia-register-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ia-register-invoices/:id : get the "id" iARegisterInvoice.
     *
     * @param id the id of the iARegisterInvoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iARegisterInvoice, or with status 404 (Not Found)
     */
    @GetMapping("/ia-register-invoices/{id}")
    @Timed
    public ResponseEntity<IARegisterInvoice> getIARegisterInvoice(@PathVariable UUID id) {
        log.debug("REST request to get IARegisterInvoice : {}", id);
        Optional<IARegisterInvoice> iARegisterInvoice = iARegisterInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iARegisterInvoice);
    }

    /**
     * DELETE  /ia-register-invoices/:id : delete the "id" iARegisterInvoice.
     *
     * @param id the id of the iARegisterInvoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-register-invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteIARegisterInvoice(@PathVariable UUID id) {
        log.debug("REST request to delete IARegisterInvoice : {}", id);
        iARegisterInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * xuất pdf đăng ký sử dụng hóa đơn
     */
    @GetMapping("/ia-register-invoices/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf() {
        byte[] export = iARegisterInvoiceService.exportPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * xuất excell đăng ký sử dụng hóa đơn
     */
    @GetMapping("/ia-register-invoices/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel() {
        byte[] export = iARegisterInvoiceService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * tải file đính kèm
     */
    @GetMapping("/ia-register-invoices/{id}/attach-file")
    @Timed
    public ResponseEntity<byte[]> downloadAttachFile(@PathVariable UUID id) {
        return iARegisterInvoiceService.downloadAttachFile(id);
    }

    @PostMapping("/ia-register-invoices/multi-delete-ia-register-invoice")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteIARegisterInvoice(@Valid @RequestBody List<IARegisterInvoice> iaRegisterInvoices) {
        log.debug("REST request to closeBook : {}", iaRegisterInvoices);
        HandlingResultDTO responeCloseBookDTO = iARegisterInvoiceService.multiDelete(iaRegisterInvoices);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
