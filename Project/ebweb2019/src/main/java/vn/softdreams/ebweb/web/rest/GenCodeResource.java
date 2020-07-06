package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.service.GenCodeService;
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
 * REST controller for managing GenCode.
 */
@RestController
@RequestMapping("/api")
public class GenCodeResource {

    private final Logger log = LoggerFactory.getLogger(GenCodeResource.class);

    private static final String ENTITY_NAME = "genCode";

    private final GenCodeService genCodeService;

    public GenCodeResource(GenCodeService genCodeService) {
        this.genCodeService = genCodeService;
    }

    /**
     * POST  /gen-codes : Create a new genCode.
     *
     * @param genCode the genCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gen-codes")
    @Timed
    public ResponseEntity<GenCode> createGenCode(@Valid @RequestBody GenCode genCode) throws URISyntaxException {
        log.debug("REST request to save GenCode : {}", genCode);
        if (genCode.getId() != null) {
            throw new BadRequestAlertException("A new genCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenCode result = genCodeService.save(genCode);
        return ResponseEntity.created(new URI("/api/gen-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gen-codes : Updates an existing genCode.
     *
     * @param genCode the genCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genCode,
     * or with status 400 (Bad Request) if the genCode is not valid,
     * or with status 500 (Internal Server Error) if the genCode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gen-codes")
    @Timed
    public ResponseEntity<GenCode> updateGenCode(@Valid @RequestBody GenCode genCode) throws URISyntaxException {
        log.debug("REST request to update GenCode : {}", genCode);
        if (genCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GenCode result = genCodeService.save(genCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genCode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gen-codes : get all the genCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of genCodes in body
     */
    @GetMapping("/gen-codes")
    @Timed
    public ResponseEntity<List<GenCode>> getAllGenCodes(Pageable pageable) {
        log.debug("REST request to get a page of GenCodes");
        Page<GenCode> page = genCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gen-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gen-codes/:id : get the "id" genCode.
     *
     * @param id the id of the genCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genCode, or with status 404 (Not Found)
     */
    @GetMapping("/gen-codes/{id}")
    @Timed
    public ResponseEntity<GenCode> getGenCode(@PathVariable UUID id) {
        log.debug("REST request to get GenCode : {}", id);
        Optional<GenCode> genCode = genCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genCode);
    }

    /**
     * DELETE  /gen-codes/:id : delete the "id" genCode.
     *
     * @param id the id of the genCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gen-codes/{id}")
    @Timed
    public ResponseEntity<Void> deleteGenCode(@PathVariable UUID id) {
        log.debug("REST request to delete GenCode : {}", id);
        genCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/gen-codes/find-all-gen-codes-companyid")
    @Timed
    public ResponseEntity<List<GenCode>> getAllGenCodeForSystemOption() {
        log.debug("REST request to get a page of GenCode");
        List<GenCode> page = genCodeService.getAllGenCodeForSystemOption();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
