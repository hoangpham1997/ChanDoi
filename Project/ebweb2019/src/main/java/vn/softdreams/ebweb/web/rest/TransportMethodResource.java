package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TransportMethod;
import vn.softdreams.ebweb.service.TransportMethodService;
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
 * REST controller for managing TransportMethod.
 */
@RestController
@RequestMapping("/api")
public class TransportMethodResource {

    private final Logger log = LoggerFactory.getLogger(TransportMethodResource.class);

    private static final String ENTITY_NAME = "transportMethod";

    private final TransportMethodService transportMethodService;

    public TransportMethodResource(TransportMethodService transportMethodService) {
        this.transportMethodService = transportMethodService;
    }

    /**
     * POST  /transport-methods : Create a new transportMethod.
     *
     * @param transportMethod the transportMethod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transportMethod, or with status 400 (Bad Request) if the transportMethod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transport-methods")
    @Timed
    public ResponseEntity<TransportMethod> createTransportMethod(@Valid @RequestBody TransportMethod transportMethod) throws URISyntaxException {
        log.debug("REST request to save TransportMethod : {}", transportMethod);
        if (transportMethod.getId() != null) {
            throw new BadRequestAlertException("A new transportMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransportMethod result = transportMethodService.save(transportMethod);
        return ResponseEntity.created(new URI("/api/transport-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transport-methods : Updates an existing transportMethod.
     *
     * @param transportMethod the transportMethod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transportMethod,
     * or with status 400 (Bad Request) if the transportMethod is not valid,
     * or with status 500 (Internal Server Error) if the transportMethod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transport-methods")
    @Timed
    public ResponseEntity<TransportMethod> updateTransportMethod(@Valid @RequestBody TransportMethod transportMethod) throws URISyntaxException {
        log.debug("REST request to update TransportMethod : {}", transportMethod);
        if (transportMethod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransportMethod result = transportMethodService.save(transportMethod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transportMethod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transport-methods : get all the transportMethods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transportMethods in body
     */
    @GetMapping("/transport-methods")
    @Timed
    public ResponseEntity<List<TransportMethod>> getAllTransportMethods(Pageable pageable) {
        log.debug("REST request to get a page of TransportMethods");
        Page<TransportMethod> page = transportMethodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transport-methods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transport-methods/:id : get the "id" transportMethod.
     *
     * @param id the id of the transportMethod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transportMethod, or with status 404 (Not Found)
     */
    @GetMapping("/transport-methods/{id}")
    @Timed
    public ResponseEntity<TransportMethod> getTransportMethod(@PathVariable UUID id) {
        log.debug("REST request to get TransportMethod : {}", id);
        Optional<TransportMethod> transportMethod = transportMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportMethod);
    }

    @GetMapping("/transport-methods/combobox")
    @Timed
    public ResponseEntity<List<TransportMethod>> getTransportMethodCombobox() {
        log.debug("REST request to get a page of TransportMethod");
        List<TransportMethod> page = transportMethodService.getTransportMethodCombobox();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * DELETE  /transport-methods/:id : delete the "id" transportMethod.
     *
     * @param id the id of the transportMethod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transport-methods/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransportMethod(@PathVariable UUID id) {
        log.debug("REST request to delete TransportMethod : {}", id);
        transportMethodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
