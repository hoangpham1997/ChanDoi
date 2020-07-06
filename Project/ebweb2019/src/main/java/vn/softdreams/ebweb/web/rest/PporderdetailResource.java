package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.service.PporderdetailService;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PPOrderDetail.
 */
@RestController
@RequestMapping("/api")
public class PporderdetailResource {

    private static final String               ENTITY_NAME = "pporderdetail";
    private final        Logger               log         = LoggerFactory.getLogger(PporderdetailResource.class);
    private final        PporderdetailService pporderdetailService;

    public PporderdetailResource(PporderdetailService pporderdetailService) {
        this.pporderdetailService = pporderdetailService;
    }

    /**
     * POST  /pporderdetails : Create a new pporderdetail.
     *
     * @param pporderdetail the pporderdetail to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pporderdetail, or with status 400 (Bad Request) if the pporderdetail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pporderdetails")
    @Timed
    public ResponseEntity<PPOrderDetail> createPporderdetail(@RequestBody PPOrderDetail pporderdetail) throws URISyntaxException {
        log.debug("REST request to save PPOrderDetail : {}", pporderdetail);
        if (pporderdetail.getId() != null) {
            throw new BadRequestAlertException("A new pporderdetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPOrderDetail result = pporderdetailService.save(pporderdetail);
        return ResponseEntity.created(new URI("/api/pporderdetails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pporderdetails : Updates an existing pporderdetail.
     *
     * @param pporderdetail the pporderdetail to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pporderdetail,
     * or with status 400 (Bad Request) if the pporderdetail is not valid,
     * or with status 500 (Internal Server Error) if the pporderdetail couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pporderdetails")
    @Timed
    public ResponseEntity<PPOrderDetail> updatePporderdetail(@RequestBody PPOrderDetail pporderdetail) throws URISyntaxException {
        log.debug("REST request to update PPOrderDetail : {}", pporderdetail);
        if (pporderdetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPOrderDetail result = pporderdetailService.save(pporderdetail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pporderdetail.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pporderdetails : get all the pporderdetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pporderdetails in body
     */
    @GetMapping("/pporderdetails")
    @Timed
    public ResponseEntity<List<PPOrderDetail>> getAllPporderdetails(Pageable pageable) {
        log.debug("REST request to get a page of Pporderdetails");
        Page<PPOrderDetail> page    = pporderdetailService.findAll(pageable);
        HttpHeaders         headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pporderdetails");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/pporderdetails/no-page")
    @Timed
    public ResponseEntity<List<PPOrderDetail>> getAllPporderdetailsNoPage() {
        log.debug("REST request to get a page of Pporderdetails");
        List<PPOrderDetail> page = pporderdetailService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /pporderdetails/:id : get the "id" pporderdetail.
     *
     * @param id the id of the pporderdetail to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pporderdetail, or with status 404 (Not Found)
     */
    @GetMapping("/pporderdetails/{id}")
    @Timed
    public ResponseEntity<PPOrderDetail> getPporderdetail(@PathVariable UUID id) {
        log.debug("REST request to get PPOrderDetail : {}", id);
        Optional<PPOrderDetail> pporderdetail = pporderdetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pporderdetail);
    }

    /**
     * DELETE  /pporderdetails/:id : delete the "id" pporderdetail.
     *
     * @param id the id of the pporderdetail to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pporderdetails/{id}")
    @Timed
    public ResponseEntity<Void> deletePporderdetail(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrderDetail : {}", id);
        pporderdetailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
