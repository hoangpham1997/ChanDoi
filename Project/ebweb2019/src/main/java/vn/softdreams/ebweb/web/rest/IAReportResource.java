package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.IARegisterInvoice;
import vn.softdreams.ebweb.domain.IAReport;
import vn.softdreams.ebweb.service.IAReportService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing IAReport.
 */
@RestController
@RequestMapping("/api")
public class IAReportResource {

    private final Logger log = LoggerFactory.getLogger(IAReportResource.class);

    private static final String ENTITY_NAME = "iAReport";

    private final IAReportService iAReportService;

    public IAReportResource(IAReportService iAReportService) {
        this.iAReportService = iAReportService;
    }

    /**
     * POST  /ia-reports : Create a new iAReport.
     *
     * @param iAReport the iAReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iAReport, or with status 400 (Bad Request) if the iAReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-reports")
    @Timed
    public ResponseEntity<IAReport> createIAReport(@Valid @RequestBody IAReport iAReport) throws URISyntaxException {
        log.debug("REST request to save IAReport : {}", iAReport);
        if (iAReport.getId() != null) {
            throw new BadRequestAlertException("A new iAReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IAReport result = iAReportService.save(iAReport);
        return ResponseEntity.created(new URI("/api/ia-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-reports : Updates an existing iAReport.
     *
     * @param iAReport the iAReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iAReport,
     * or with status 400 (Bad Request) if the iAReport is not valid,
     * or with status 500 (Internal Server Error) if the iAReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-reports")
    @Timed
    public ResponseEntity<IAReport> updateIAReport(@Valid @RequestBody IAReport iAReport) throws URISyntaxException {
        log.debug("REST request to update IAReport : {}", iAReport);
        if (iAReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IAReport result = iAReportService.save(iAReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iAReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-reports : get all the iAReports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of iAReports in body
     */
    @GetMapping("/ia-reports")
    @Timed
    public ResponseEntity<List<IAReport>> getAllIAReports(Pageable pageable, @RequestParam(required = false) Boolean isUnregistered) {
        log.debug("REST request to get a page of IAReports");
        Page<IAReport> page = iAReportService.findAll(pageable, isUnregistered);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ia-reports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/ia-reports/ia-reports-status")
    @Timed
    public ResponseEntity<List<IAReport>> getIAReportsByStatus(Pageable pageable, @RequestParam(required = false) Boolean isUnregistered, @RequestParam(required = false) Integer status ) {
        log.debug("REST request to get a page of IAReports");
        Page<IAReport> page = iAReportService.findIAReportsByStatus(pageable, isUnregistered, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ia-reports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/ia-reports/ids")
    @Timed
    public ResponseEntity<List<IAReport>> getAllIAReportsByIds(@RequestParam(required = false) List<UUID> ids) {
        log.debug("REST request to get a page of IAReports");
        List<IAReport> page = iAReportService.findAllByIds(ids);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    /**
     * @param ids danh sách mẫu hóa đơn cần check đã thông báo phát hành hay chưa
     * @return số lượng mẫu hóa đơn đã phát hành
     */
    @GetMapping("/ia-reports/check-published")
    @Timed
    public ResponseEntity<Integer> checkIsPublishedReport(@RequestParam List<UUID> ids) {
        log.debug("REST request to get a page of IAReports");
        Integer count = iAReportService.checkIsPublishedReport(ids);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * GET  /ia-reports/:id : get the "id" iAReport.
     *
     * @param id the id of the iAReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iAReport, or with status 404 (Not Found)
     */
    @GetMapping("/ia-reports/{id}")
    @Timed
    public ResponseEntity<IAReport> getIAReport(@PathVariable UUID id) {
        log.debug("REST request to get IAReport : {}", id);
        Optional<IAReport> iAReport = iAReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iAReport);
    }

    /**
     * DELETE  /ia-reports/:id : delete the "id" iAReport.
     *
     * @param id the id of the iAReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteIAReport(@PathVariable UUID id) {
        log.debug("REST request to delete IAReport : {}", id);
        iAReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * xuất pdf khỏi tạo mẫu hóa đơn
     */
    @GetMapping("/ia-reports/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf() {
        byte[] export = iAReportService.exportPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * xuất excell khỏi tạo mẫu hóa đơn
     */
    @GetMapping("/ia-reports/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel() {
        byte[] export = iAReportService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * xuất excell khỏi tạo mẫu hóa đơn
     */
    @GetMapping("/ia-reports/preview-invoice-template")
    @Timed
    public ResponseEntity<byte[]> previewInvoiceTemplate(@RequestParam String templatePath) throws JRException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        try {
            String name = templatePath + ".pdf";
            byte[] bytes = iAReportService.previewInvoiceTemplate(templatePath);
            headers.setContentDispositionFormData(name, name);
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/ia-reports/multi-delete-ia-report-invoice")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteIAReportInvoice(@Valid @RequestBody List<IAReport> iaReports) {
        log.debug("REST request to closeBook : {}", iaReports);
        HandlingResultDTO responeCloseBookDTO = iAReportService.multiDelete(iaReports);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
