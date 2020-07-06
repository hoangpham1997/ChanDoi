package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.IAReport;
import vn.softdreams.ebweb.domain.IaPublishInvoice;
import vn.softdreams.ebweb.service.IaPublishInvoiceService;
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
 * REST controller for managing IaPublishInvoice.
 */
@RestController
@RequestMapping("/api")
public class IaPublishInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(IaPublishInvoiceResource.class);

    private static final String ENTITY_NAME = "iaPublishInvoice";

    private final IaPublishInvoiceService iaPublishInvoiceService;

    public IaPublishInvoiceResource(IaPublishInvoiceService iaPublishInvoiceService) {
        this.iaPublishInvoiceService = iaPublishInvoiceService;
    }

    /**
     * POST  /ia-publish-invoices : Create a new iaPublishInvoice.
     *
     * @param iaPublishInvoice the iaPublishInvoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iaPublishInvoice, or with status 400 (Bad Request) if the iaPublishInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-publish-invoices")
    @Timed
    public ResponseEntity<IaPublishInvoice> createIaPublishInvoice(@RequestBody IaPublishInvoice iaPublishInvoice) throws URISyntaxException {
        log.debug("REST request to save IaPublishInvoice : {}", iaPublishInvoice);
        if (iaPublishInvoice.getId() != null) {
            throw new BadRequestAlertException("A new iaPublishInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IaPublishInvoice result = iaPublishInvoiceService.save(iaPublishInvoice);
        return ResponseEntity.created(new URI("/api/ia-publish-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-publish-invoices : Updates an existing iaPublishInvoice.
     *
     * @param iaPublishInvoice the iaPublishInvoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iaPublishInvoice,
     * or with status 400 (Bad Request) if the iaPublishInvoice is not valid,
     * or with status 500 (Internal Server Error) if the iaPublishInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-publish-invoices")
    @Timed
    public ResponseEntity<IaPublishInvoice> updateIaPublishInvoice(@RequestBody IaPublishInvoice iaPublishInvoice) throws URISyntaxException {
        log.debug("REST request to update IaPublishInvoice : {}", iaPublishInvoice);
        if (iaPublishInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IaPublishInvoice result = iaPublishInvoiceService.save(iaPublishInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iaPublishInvoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-publish-invoices : get all the iaPublishInvoices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of iaPublishInvoices in body
     */
    @GetMapping("/ia-publish-invoices")
    @Timed
    public ResponseEntity<List<IaPublishInvoice>> getAllIaPublishInvoices() {
        log.debug("REST request to get a page of IaPublishInvoices");
        List<IaPublishInvoice> page = iaPublishInvoiceService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /ia-publish-invoices : get all the iaPublishInvoices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of iaPublishInvoices in body
     */
    @GetMapping("/ia-publish-invoices/pageable")
    @Timed
    public ResponseEntity<List<IaPublishInvoice>> getAllIaPublishInvoices(Pageable pageable) {
        log.debug("REST request to get a page of IaPublishInvoice");
        Page<IaPublishInvoice> page = iaPublishInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ia-publish-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ia-publish-invoices/:id : get the "id" iaPublishInvoice.
     *
     * @param id the id of the iaPublishInvoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iaPublishInvoice, or with status 404 (Not Found)
     */
    @GetMapping("/ia-publish-invoices/{id}")
    @Timed
    public ResponseEntity<IaPublishInvoice> getIaPublishInvoice(@PathVariable UUID id) {
        log.debug("REST request to get IaPublishInvoice : {}", id);
        Optional<IaPublishInvoice> iaPublishInvoice = iaPublishInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iaPublishInvoice);
    }

    /**
     * DELETE  /ia-publish-invoices/:id : delete the "id" iaPublishInvoice.
     *
     * @param id the id of the iaPublishInvoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-publish-invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteIaPublishInvoice(@PathVariable UUID id) {
        log.debug("REST request to delete IaPublishInvoice : {}", id);
        iaPublishInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @author dungvm
     * lấy đến số lớn nhất dựa vào IAReportID
     */
    @GetMapping("/ia-publish-invoices/max-from-no//{id}")
    @Timed
    public ResponseEntity<Long> findCurrentMaxFromNo(@PathVariable UUID id) {
        log.debug("REST request to findCurrentMaxFromNo : {}", id);
        Long current = iaPublishInvoiceService.findCurrentMaxFromNo(id);
        return ResponseEntity.ok().body(current);
    }

    /**
     * xuất pdf thông báo phát hành hóa đơn
     */
    @GetMapping("/ia-publish-invoices/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf() {
        byte[] export = iaPublishInvoiceService.exportPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * xuất excell thông báo phát hành hóa đơn
     */
    @GetMapping("/ia-publish-invoices/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel() {
        byte[] export = iaPublishInvoiceService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }
}
