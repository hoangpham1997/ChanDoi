package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.service.SaBillDetailsService;
import vn.softdreams.ebweb.web.rest.dto.SaBillDetailDTO;
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
 * REST controller for managing SaBillDetails.
 */
@RestController
@RequestMapping("/api")
public class SaBillDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SaBillDetailsResource.class);

    private static final String ENTITY_NAME = "saBillDetails";

    private final SaBillDetailsService saBillDetailsService;

    public SaBillDetailsResource(SaBillDetailsService saBillDetailsService) {
        this.saBillDetailsService = saBillDetailsService;
    }

    /**
     * POST  /sa-bill-details : Create a new saBillDetails.
     *
     * @param saBillDetails the saBillDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saBillDetails, or with status 400 (Bad Request) if the saBillDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-bill-details")
    @Timed
    public ResponseEntity<SaBillDetails> createSaBillDetails(@RequestBody SaBillDetails saBillDetails) throws URISyntaxException {
        log.debug("REST request to save SaBillDetails : {}", saBillDetails);
        if (saBillDetails.getId() != null) {
            throw new BadRequestAlertException("A new saBillDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaBillDetails result = saBillDetailsService.save(saBillDetails);
        return ResponseEntity.created(new URI("/api/sa-bill-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sa-bill-details : Updates an existing saBillDetails.
     *
     * @param saBillDetails the saBillDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saBillDetails,
     * or with status 400 (Bad Request) if the saBillDetails is not valid,
     * or with status 500 (Internal Server Error) if the saBillDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sa-bill-details")
    @Timed
    public ResponseEntity<SaBillDetails> updateSaBillDetails(@RequestBody SaBillDetails saBillDetails) throws URISyntaxException {
        log.debug("REST request to update SaBillDetails : {}", saBillDetails);
        if (saBillDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaBillDetails result = saBillDetailsService.save(saBillDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saBillDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-bill-details : get all the saBillDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saBillDetails in body
     */
    @GetMapping("/sa-bill-details")
    @Timed
    public ResponseEntity<List<SaBillDetails>> getAllSaBillDetails(Pageable pageable) {
        log.debug("REST request to get a page of SaBillDetails");
        Page<SaBillDetails> page = saBillDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-bill-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-bill-details/:id : get the "id" saBillDetails.
     *
     * @param id the id of the saBillDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saBillDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sa-bill-details/{id}")
    @Timed
    public ResponseEntity<SaBillDetailDTO> getSaBillDetails(@PathVariable UUID id) {
        log.debug("REST request to get SaBillDetails : {}", id);
        SaBillDetailDTO saBillDetails = saBillDetailsService.findAll(id);
        return new ResponseEntity<>(saBillDetails, HttpStatus.OK);
    }

    /**
     * DELETE  /sa-bill-details/:id : delete the "id" saBillDetails.
     *
     * @param id the id of the saBillDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-bill-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaBillDetails(@PathVariable UUID id) {
        log.debug("REST request to delete SaBillDetails : {}", id);
        saBillDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
