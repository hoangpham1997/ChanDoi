package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPProductQuantum;
import vn.softdreams.ebweb.service.CPProductQuantumService;
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
 * REST controller for managing CPProductQuantum.
 */
@RestController
@RequestMapping("/api")
public class CPProductQuantumResource {

    private final Logger log = LoggerFactory.getLogger(CPProductQuantumResource.class);

    private static final String ENTITY_NAME = "cPProductQuantum";

    private final CPProductQuantumService cPProductQuantumService;

    public CPProductQuantumResource(CPProductQuantumService cPProductQuantumService) {
        this.cPProductQuantumService = cPProductQuantumService;
    }

//    /**
//     * POST  /c-p-product-quantums : Create a new cPProductQuantum.
//     *
//     * @param cPProductQuantum the cPProductQuantum to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new cPProductQuantum, or with status 400 (Bad Request) if the cPProductQuantum has already an ID
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PostMapping("/c-p-product-quantums")
//    @Timed
//    public ResponseEntity<CPProductQuantum> createCPProductQuantum(@RequestBody CPProductQuantum cPProductQuantum) throws URISyntaxException {
//        log.debug("REST request to save CPProductQuantum : {}", cPProductQuantum);
//        if (cPProductQuantum.getId() != null) {
//            throw new BadRequestAlertException("A new cPProductQuantum cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        CPProductQuantum result = cPProductQuantumService.save(cPProductQuantum);
//        return ResponseEntity.created(new URI("/api/c-p-product-quantums/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * PUT  /c-p-product-quantums : Updates an existing cPProductQuantum.
//     *
//     * @param cPProductQuantum the cPProductQuantum to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated cPProductQuantum,
//     * or with status 400 (Bad Request) if the cPProductQuantum is not valid,
//     * or with status 500 (Internal Server Error) if the cPProductQuantum couldn't be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/c-p-product-quantums")
//    @Timed
//    public ResponseEntity<CPProductQuantum> updateCPProductQuantum(@RequestBody CPProductQuantum cPProductQuantum) throws URISyntaxException {
//        log.debug("REST request to update CPProductQuantum : {}", cPProductQuantum);
//        if (cPProductQuantum.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        CPProductQuantum result = cPProductQuantumService.save(cPProductQuantum);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPProductQuantum.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /c-p-product-quantums : get all the cPProductQuantums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPProductQuantums in body
     */
    @GetMapping("/c-p-product-quantums")
    @Timed
    public ResponseEntity<List<CPProductQuantum>> getAllCPProductQuantums(Pageable pageable) {
        log.debug("REST request to get a page of CPProductQuantums");
        Page<CPProductQuantum> page = cPProductQuantumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-product-quantums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /c-p-product-quantums/:id : get the "id" cPProductQuantum.
     *
     * @param id the id of the cPProductQuantum to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPProductQuantum, or with status 404 (Not Found)
     */
    @GetMapping("/c-p-product-quantums/{id}")
    @Timed
    public ResponseEntity<CPProductQuantum> getCPProductQuantum(@PathVariable UUID id) {
        log.debug("REST request to get CPProductQuantum : {}", id);
        Optional<CPProductQuantum> cPProductQuantum = cPProductQuantumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPProductQuantum);
    }

    /**
     * DELETE  /c-p-product-quantums/:id : delete the "id" cPProductQuantum.
     *
     * @param id the id of the cPProductQuantum to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-p-product-quantums/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPProductQuantum(@PathVariable UUID id) {
        log.debug("REST request to delete CPProductQuantum : {}", id);
        cPProductQuantumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/c-p-product-quantums/find-all-c-p-product-quantums-active-companyid")
    @Timed
    public ResponseEntity<List<CPProductQuantumDTO>> getAllCPProductQuantumsActiveCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of CPProductQuantum");
        Page<CPProductQuantumDTO> page = cPProductQuantumService.findAllActive(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-product-quantums/find-all-c-p-product-quantums-active-companyid");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/c-p-product-quantums/save-all")
    @Timed
    public ResponseEntity<CPProductQuantum> saveAll(@RequestBody List<CPProductQuantum> cpProductQuantum) throws URISyntaxException {
        log.debug("REST request to update CPProductQuantum : {}", cpProductQuantum);
        cPProductQuantumService.saveAll(cpProductQuantum);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
