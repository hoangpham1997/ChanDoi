package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.service.CPAllocationQuantumService;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;
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
 * REST controller for managing CPAllocationQuantum.
 */
@RestController
@RequestMapping("/api")
public class CPAllocationQuantumResource {

    private final Logger log = LoggerFactory.getLogger(CPAllocationQuantumResource.class);

    private static final String ENTITY_NAME = "cPAllocationQuantum";

    private final CPAllocationQuantumService cPAllocationQuantumService;

    public CPAllocationQuantumResource(CPAllocationQuantumService cPAllocationQuantumService) {
        this.cPAllocationQuantumService = cPAllocationQuantumService;
    }

//    /**
//     * POST  /c-p-allocation-quantums : Create a new cPAllocationQuantum.
//     *
//     * @param cPAllocationQuantum the cPAllocationQuantum to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new cPAllocationQuantum, or with status 400 (Bad Request) if the cPAllocationQuantum has already an ID
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PostMapping("/c-p-allocation-quantums")
//    @Timed
//    public ResponseEntity<CPAllocationQuantum> createCPAllocationQuantum(@RequestBody CPAllocationQuantum cPAllocationQuantum) throws URISyntaxException {
//        log.debug("REST request to save CPAllocationQuantum : {}", cPAllocationQuantum);
//        if (cPAllocationQuantum.getId() != null) {
//            throw new BadRequestAlertException("A new cPAllocationQuantum cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        CPAllocationQuantum result = cPAllocationQuantumService.save(cPAllocationQuantum);
//        return ResponseEntity.created(new URI("/api/c-p-allocation-quantums/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * PUT  /c-p-allocation-quantums : Updates an existing cPAllocationQuantum.
//     *
//     * @param cPAllocationQuantum the cPAllocationQuantum to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAllocationQuantum,
//     * or with status 400 (Bad Request) if the cPAllocationQuantum is not valid,
//     * or with status 500 (Internal Server Error) if the cPAllocationQuantum couldn't be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/c-p-allocation-quantums")
//    @Timed
//    public ResponseEntity<CPAllocationQuantum> updateCPAllocationQuantum(@RequestBody CPAllocationQuantum cPAllocationQuantum) throws URISyntaxException {
//        log.debug("REST request to update CPAllocationQuantum : {}", cPAllocationQuantum);
//        if (cPAllocationQuantum.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        CPAllocationQuantum result = cPAllocationQuantumService.save(cPAllocationQuantum);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAllocationQuantum.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /c-p-allocation-quantums : get all the cPAllocationQuantums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAllocationQuantums in body
     */
    @GetMapping("/c-p-allocation-quantums")
    @Timed
    public ResponseEntity<List<CPAllocationQuantum>> getAllCPAllocationQuantums(Pageable pageable) {
        log.debug("REST request to get a page of CPAllocationQuantums");
        Page<CPAllocationQuantum> page = cPAllocationQuantumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-allocation-quantums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /c-p-allocation-quantums/:id : get the "id" cPAllocationQuantum.
     *
     * @param id the id of the cPAllocationQuantum to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAllocationQuantum, or with status 404 (Not Found)
     */
    @GetMapping("/c-p-allocation-quantums/{id}")
    @Timed
    public ResponseEntity<CPAllocationQuantum> getCPAllocationQuantum(@PathVariable UUID id) {
        log.debug("REST request to get CPAllocationQuantum : {}", id);
        Optional<CPAllocationQuantum> cPAllocationQuantum = cPAllocationQuantumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAllocationQuantum);
    }

    /**
     * DELETE  /c-p-allocation-quantums/:id : delete the "id" cPAllocationQuantum.
     *
     * @param id the id of the cPAllocationQuantum to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-p-allocation-quantums/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAllocationQuantum(@PathVariable UUID id) {
        log.debug("REST request to delete CPAllocationQuantum : {}", id);
        cPAllocationQuantumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/c-p-allocation-quantums/find-all-c-p-allocation-quantums-active-companyid")
    @Timed
    public ResponseEntity<List<CPAllocationQuantumDTO>> getAllCPProductQuantumsActiveCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of CPAllocationQuantums");
        Page<CPAllocationQuantumDTO> page = cPAllocationQuantumService.findAllActive(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-allocation-quantums/find-all-c-p-allocation-quantums-active-companyid");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/c-p-allocation-quantums/save-all")
    @Timed
    public ResponseEntity<CPAllocationQuantum> saveAll(@RequestBody List<CPAllocationQuantum> cpProductQuantum) throws URISyntaxException {
        log.debug("REST request to update CPProductQuantum : {}", cpProductQuantum);
        cPAllocationQuantumService.saveAll(cpProductQuantum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/c-p-allocation-quantums/find-by-costset-id")
    @Timed
    public ResponseEntity<List<CPAllocationQuantum>> findByCostSetID(@RequestBody List<UUID> ids) {
        log.debug("REST request to get a page of CPProductQuantum");
        List<CPAllocationQuantum> page = cPAllocationQuantumService.findByCostSetID(ids);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
