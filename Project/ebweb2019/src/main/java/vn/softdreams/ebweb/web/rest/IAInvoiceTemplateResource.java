package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.IAInvoiceTemplate;
import vn.softdreams.ebweb.service.IAInvoiceTemplateService;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing IAInvoiceTemplate.
 */
@RestController
@RequestMapping("/api")
public class IAInvoiceTemplateResource {

    private final Logger log = LoggerFactory.getLogger(IAInvoiceTemplateResource.class);

    private static final String ENTITY_NAME = "iAInvoiceTemplate";

    private final IAInvoiceTemplateService iAInvoiceTemplateService;

    public IAInvoiceTemplateResource(IAInvoiceTemplateService iAInvoiceTemplateService) {
        this.iAInvoiceTemplateService = iAInvoiceTemplateService;
    }

    /**
     * POST  /ia-invoice-templates : Create a new iAInvoiceTemplate.
     *
     * @param iAInvoiceTemplate the iAInvoiceTemplate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iAInvoiceTemplate, or with status 400 (Bad Request) if the iAInvoiceTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-invoice-templates")
    @Timed
    public ResponseEntity<IAInvoiceTemplate> createIAInvoiceTemplate(@Valid @RequestBody IAInvoiceTemplate iAInvoiceTemplate) throws URISyntaxException {
        log.debug("REST request to save IAInvoiceTemplate : {}", iAInvoiceTemplate);
        if (iAInvoiceTemplate.getId() != null) {
            throw new BadRequestAlertException("A new iAInvoiceTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IAInvoiceTemplate result = iAInvoiceTemplateService.save(iAInvoiceTemplate);
        return ResponseEntity.created(new URI("/api/ia-invoice-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-invoice-templates : Updates an existing iAInvoiceTemplate.
     *
     * @param iAInvoiceTemplate the iAInvoiceTemplate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iAInvoiceTemplate,
     * or with status 400 (Bad Request) if the iAInvoiceTemplate is not valid,
     * or with status 500 (Internal Server Error) if the iAInvoiceTemplate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-invoice-templates")
    @Timed
    public ResponseEntity<IAInvoiceTemplate> updateIAInvoiceTemplate(@Valid @RequestBody IAInvoiceTemplate iAInvoiceTemplate) throws URISyntaxException {
        log.debug("REST request to update IAInvoiceTemplate : {}", iAInvoiceTemplate);
        if (iAInvoiceTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IAInvoiceTemplate result = iAInvoiceTemplateService.save(iAInvoiceTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iAInvoiceTemplate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-invoice-templates : get all the iAInvoiceTemplates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of iAInvoiceTemplates in body
     */
    @GetMapping("/ia-invoice-templates")
    @Timed
    public List<IAInvoiceTemplate> getAllIAInvoiceTemplates() {
        log.debug("REST request to get all IAInvoiceTemplates");
        return iAInvoiceTemplateService.findAll();
    }

    /**
     * GET  /ia-invoice-templates/:id : get the "id" iAInvoiceTemplate.
     *
     * @param id the id of the iAInvoiceTemplate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iAInvoiceTemplate, or with status 404 (Not Found)
     */
    @GetMapping("/ia-invoice-templates/{id}")
    @Timed
    public ResponseEntity<IAInvoiceTemplate> getIAInvoiceTemplate(@PathVariable UUID id) {
        log.debug("REST request to get IAInvoiceTemplate : {}", id);
        Optional<IAInvoiceTemplate> iAInvoiceTemplate = iAInvoiceTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iAInvoiceTemplate);
    }

    /**
     * DELETE  /ia-invoice-templates/:id : delete the "id" iAInvoiceTemplate.
     *
     * @param id the id of the iAInvoiceTemplate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-invoice-templates/{id}")
    @Timed
    public ResponseEntity<Void> deleteIAInvoiceTemplate(@PathVariable UUID id) {
        log.debug("REST request to delete IAInvoiceTemplate : {}", id);
        iAInvoiceTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
