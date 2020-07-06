package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.service.PPDiscountReturnDetailsService;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailOutWardDTO;
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
 * REST controller for managing PPDiscountReturnDetails.
 */
@RestController
@RequestMapping("/api")
public class PPDiscountReturnDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PPDiscountReturnDetailsResource.class);

    private static final String ENTITY_NAME = "pPDiscountReturnDetails";

    private final PPDiscountReturnDetailsService pPDiscountReturnDetailsService;

    public PPDiscountReturnDetailsResource(PPDiscountReturnDetailsService pPDiscountReturnDetailsService) {
        this.pPDiscountReturnDetailsService = pPDiscountReturnDetailsService;
    }

    /**
     * POST  /pp-discount-return-details : Create a new pPDiscountReturnDetails.
     *
     * @param pPDiscountReturnDetails the pPDiscountReturnDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pPDiscountReturnDetails, or with status 400 (Bad Request) if the pPDiscountReturnDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pp-discount-return-details")
    @Timed
    public ResponseEntity<PPDiscountReturnDetails> createPPDiscountReturnDetails(@Valid @RequestBody PPDiscountReturnDetails pPDiscountReturnDetails) throws URISyntaxException {
        log.debug("REST request to save PPDiscountReturnDetails : {}", pPDiscountReturnDetails);
        if (pPDiscountReturnDetails.getId() != null) {
            throw new BadRequestAlertException("A new pPDiscountReturnDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPDiscountReturnDetails result = pPDiscountReturnDetailsService.save(pPDiscountReturnDetails);
        return ResponseEntity.created(new URI("/api/pp-discount-return-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pp-discount-return-details : Updates an existing pPDiscountReturnDetails.
     *
     * @param pPDiscountReturnDetails the pPDiscountReturnDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPDiscountReturnDetails,
     * or with status 400 (Bad Request) if the pPDiscountReturnDetails is not valid,
     * or with status 500 (Internal Server Error) if the pPDiscountReturnDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-discount-return-details")
    @Timed
    public ResponseEntity<PPDiscountReturnDetails> updatePPDiscountReturnDetails(@Valid @RequestBody PPDiscountReturnDetails pPDiscountReturnDetails) throws URISyntaxException {
        log.debug("REST request to update PPDiscountReturnDetails : {}", pPDiscountReturnDetails);
        if (pPDiscountReturnDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPDiscountReturnDetails result = pPDiscountReturnDetailsService.save(pPDiscountReturnDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pPDiscountReturnDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pp-discount-return-details : get all the pPDiscountReturnDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pPDiscountReturnDetails in body
     */
    @GetMapping("/pp-discount-return-details")
    @Timed
    public ResponseEntity<List<PPDiscountReturnDetails>> getAllPPDiscountReturnDetails(Pageable pageable) {
        log.debug("REST request to get a page of PPDiscountReturnDetails");
        Page<PPDiscountReturnDetails> page = pPDiscountReturnDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-discount-return-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/pp-discount-return-details/by-id")
    @Timed
    public ResponseEntity<List<PPDiscountReturnDetailConvertDTO>> getAllPPDiscountReturnDetailsByID(@RequestParam UUID ppDiscountReturnId) {
        log.debug("REST request to get a page of PPDiscountReturnDetails");
        Page<PPDiscountReturnDetailConvertDTO> page = pPDiscountReturnDetailsService.getAllPPDiscountReturnDetailsByID(ppDiscountReturnId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-discount-return-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/pp-discount-return-details/by-id-detail")
    @Timed
    public ResponseEntity<List<PPDiscountReturnDetailDTO>> getPPDiscountReturnDetailsByID(@RequestParam UUID ppDiscountReturnId) {
        log.debug("REST request to get a page of PPDiscountReturnDetails");
        List<PPDiscountReturnDetailDTO> page = pPDiscountReturnDetailsService.getPPDiscountReturnDetailsByID(ppDiscountReturnId);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-discount-return-details");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /pp-discount-return-details/:id : get the "id" pPDiscountReturnDetails.
     *
     * @param id the id of the pPDiscountReturnDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPDiscountReturnDetails, or with status 404 (Not Found)
     */
    @GetMapping("/pp-discount-return-details/{id}")
    @Timed
    public ResponseEntity<PPDiscountReturnDetails> getPPDiscountReturnDetails(@PathVariable Long id) {
        log.debug("REST request to get PPDiscountReturnDetails : {}", id);
        Optional<PPDiscountReturnDetails> pPDiscountReturnDetails = pPDiscountReturnDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPDiscountReturnDetails);
    }

    /**
     * DELETE  /pp-discount-return-details/:id : delete the "id" pPDiscountReturnDetails.
     *
     * @param id the id of the pPDiscountReturnDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pp-discount-return-details/{id}")
    @Timed
    public ResponseEntity<Void> deletePPDiscountReturnDetails(@PathVariable Long id) {
        log.debug("REST request to delete PPDiscountReturnDetails : {}", id);
        pPDiscountReturnDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/pp-discount-return-details/details")
    @Timed
    public ResponseEntity<List<PPDiscountReturnDetailOutWardDTO>> findAllDetailsById(@RequestParam List<UUID> id) {
        log.debug("REST request to get SaReturn : {}", id);
        List<PPDiscountReturnDetailOutWardDTO> details = pPDiscountReturnDetailsService.findAllDetailsById(id);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
