package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Toolledger;
import vn.softdreams.ebweb.service.ToolledgerService;
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
 * REST controller for managing Toolledger.
 */
@RestController
@RequestMapping("/api")
public class ToolledgerResource {

    private final Logger log = LoggerFactory.getLogger(ToolledgerResource.class);

    private static final String ENTITY_NAME = "toolledger";

    private final ToolledgerService toolledgerService;

    public ToolledgerResource(ToolledgerService toolledgerService) {
        this.toolledgerService = toolledgerService;
    }

    /**
     * POST  /toolledgers : Create a new toolledger.
     *
     * @param toolledger the toolledger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new toolledger, or with status 400 (Bad Request) if the toolledger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/toolledgers")
    @Timed
    public ResponseEntity<Toolledger> createToolledger(@Valid @RequestBody Toolledger toolledger) throws URISyntaxException {
        log.debug("REST request to save Toolledger : {}", toolledger);
        if (toolledger.getId() != null) {
            throw new BadRequestAlertException("A new toolledger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Toolledger result = toolledgerService.save(toolledger);
        return ResponseEntity.created(new URI("/api/toolledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /toolledgers : Updates an existing toolledger.
     *
     * @param toolledger the toolledger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated toolledger,
     * or with status 400 (Bad Request) if the toolledger is not valid,
     * or with status 500 (Internal Server Error) if the toolledger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/toolledgers")
    @Timed
    public ResponseEntity<Toolledger> updateToolledger(@Valid @RequestBody Toolledger toolledger) throws URISyntaxException {
        log.debug("REST request to update Toolledger : {}", toolledger);
        if (toolledger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Toolledger result = toolledgerService.save(toolledger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, toolledger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /toolledgers : get all the toolledgers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of toolledgers in body
     */
    @GetMapping("/toolledgers")
    @Timed
    public ResponseEntity<List<Toolledger>> getAllToolledgers(Pageable pageable) {
        log.debug("REST request to get a page of Toolledgers");
        Page<Toolledger> page = toolledgerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/toolledgers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /toolledgers/:id : get the "id" toolledger.
     *
     * @param id the id of the toolledger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the toolledger, or with status 404 (Not Found)
     */
    @GetMapping("/toolledgers/{id}")
    @Timed
    public ResponseEntity<Toolledger> getToolledger(@PathVariable UUID id) {
        log.debug("REST request to get Toolledger : {}", id);
        Optional<Toolledger> toolledger = toolledgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolledger);
    }

    /**
     * DELETE  /toolledgers/:id : delete the "id" toolledger.
     *
     * @param id the id of the toolledger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/toolledgers/{id}")
    @Timed
    public ResponseEntity<Void> deleteToolledger(@PathVariable UUID id) {
        log.debug("REST request to delete Toolledger : {}", id);
        toolledgerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
