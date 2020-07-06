package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAudit;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.service.TIAuditService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailAllDTO;
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
 * REST controller for managing TIAudit.
 */
@RestController
@RequestMapping("/api")
public class TIAuditResource {

    private final Logger log = LoggerFactory.getLogger(TIAuditResource.class);

    private static final String ENTITY_NAME = "tIAudit";

    private final TIAuditService tIAuditService;

    public TIAuditResource(TIAuditService tIAuditService) {
        this.tIAuditService = tIAuditService;
    }

    /**
     * POST  /t-i-audits : Create a new tIAudit.
     *
     * @param tIAudit the tIAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAudit, or with status 400 (Bad Request) if the tIAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-audits")
    @Timed
    public ResponseEntity<TIAudit> createTIAudit(@RequestBody TIAudit tIAudit) throws URISyntaxException {
        log.debug("REST request to save TIAudit : {}", tIAudit);
        if (tIAudit.getId() != null) {
            throw new BadRequestAlertException("A new tIAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAudit result = tIAuditService.save(tIAudit);
        return ResponseEntity.created(new URI("/api/t-i-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-audits : Updates an existing tIAudit.
     *
     * @param tIAudit the tIAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAudit,
     * or with status 400 (Bad Request) if the tIAudit is not valid,
     * or with status 500 (Internal Server Error) if the tIAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-audits")
    @Timed
    public ResponseEntity<TIAudit> updateTIAudit(@RequestBody TIAudit tIAudit) throws URISyntaxException {
        log.debug("REST request to update TIAudit : {}", tIAudit);
        if (tIAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAudit result = tIAuditService.save(tIAudit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-audits : get all the tIAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAudits in body
     */
    @GetMapping("/t-i-audits")
    @Timed
    public ResponseEntity<List<TIAudit>> getAllTIAudits(Pageable pageable) {
        log.debug("REST request to get a page of TIAudits");
        Page<TIAudit> page = tIAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-audits/:id : get the "id" tIAudit.
     *
     * @param id the id of the tIAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAudit, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-audits/{id}")
    @Timed
    public ResponseEntity<TIAudit> getTIAudit(@PathVariable UUID id) {
        log.debug("REST request to get TIAudit : {}", id);
        Optional<TIAudit> tIAudit = tIAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAudit);
    }

    @GetMapping("/t-i-audits/details/{id}")
    @Timed
    public ResponseEntity<TIAuditDetailAllDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get TIAudit : {}", id);
        TIAuditDetailAllDTO tIAudit = tIAuditService.findDetailsByID(id);
        return new ResponseEntity<>(tIAudit, HttpStatus.OK);
    }

    @GetMapping("/t-i-audits/get-tools-by-id/{id}")
    @Timed
    public ResponseEntity<List<ToolsDetailsTiAuditConvertDTO>> getAllToolsByTiAuditID(@PathVariable UUID id) {
        log.debug("REST request to get TIAudit : {}", id);
        List<ToolsDetailsTiAuditConvertDTO> tIAudit = tIAuditService.getAllToolsByTiAuditID(id);
        return new ResponseEntity<>(tIAudit, HttpStatus.OK);
    }

    /**
     * DELETE  /t-i-audits/:id : delete the "id" tIAudit.
     *
     * @param id the id of the tIAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-audits/relation-ship/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelationShip(@PathVariable UUID id) {
        log.debug("REST request to delete TIAudit : {}", id);
        tIAuditService.deleteRelationShip(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /t-i-audits/:id : delete the "id" tIAudit.
     *
     * xóa chứng từ ghi tăng ghi giảm phát sinh
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAudit(@PathVariable UUID id) {
        log.debug("REST request to delete TIAudit : {}", id);
        tIAuditService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/t-i-audits/ti-audit-count")
    @Timed
    public ResponseEntity<List<ToolsDetailsTiAuditConvertDTO>> getTIAudit(@RequestParam String date) {
        log.debug("REST request to get TIAudit : {}", date);
        List<ToolsDetailsTiAuditConvertDTO> tIAudit = tIAuditService.getTIAudit(date);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(tIAudit, "/api/t-i-audits");
        return new ResponseEntity<>(tIAudit, HttpStatus.OK);
    }

    //cod màn tìm kiếm
    @GetMapping("/t-i-audits/load-all")
    @Timed
    public ResponseEntity<List<TIAuditConvertDTO>> getAllTIAuditSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TIAuditConvertDTO> page = tIAuditService.getAllTIAuditSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/t-i-audits/find-by-row-num")
    @Timed
    public ResponseEntity<Optional<TIAudit>> findByRowNum(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch, Integer rowNum) {
        log.debug("REST request to get a page of TIIncrements");
        Optional<TIAudit> page = tIAuditService.findByRowNum(pageable, fromDate, toDate, keySearch, rowNum);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @param tiAudits
     * xóa nhiều dòng dữ liệu màn kiểm kê
     * @return
     */
    @PostMapping("/t-i-audits/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteTIAudit(@RequestBody List<TIAudit> tiAudits) {
        log.debug("REST request to closeBook : {}", tiAudits);
        HandlingResultDTO rsCloseBookDTO = tIAuditService.multiDelete(tiAudits);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }
}
