package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.service.ToolsService;
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
 * REST controller for managing Tools.
 */
@RestController
@RequestMapping("/api")
public class ToolsResource {

    private final Logger log = LoggerFactory.getLogger(ToolsResource.class);

    private static final String ENTITY_NAME = "tools";

    private final ToolsService toolsService;

    public ToolsResource(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    /**
     * POST  /tools : Create a new tools.
     *
     * @param tools the tools to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tools, or with status 400 (Bad Request) if the tools has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tools")
    @Timed
    public ResponseEntity<Tools> createTools(@Valid @RequestBody Tools tools) throws URISyntaxException {
        log.debug("REST request to save Tools : {}", tools);
        if (tools.getId() != null) {
            throw new BadRequestAlertException("A new tools cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tools result = toolsService.save(tools);
        return ResponseEntity.created(new URI("/api/tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tools : Updates an existing tools.
     *
     * @param tools the tools to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tools,
     * or with status 400 (Bad Request) if the tools is not valid,
     * or with status 500 (Internal Server Error) if the tools couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tools")
    @Timed
    public ResponseEntity<Tools> updateTools(@Valid @RequestBody Tools tools) throws URISyntaxException {
        log.debug("REST request to update Tools : {}", tools);
        if (tools.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tools result = toolsService.save(tools);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tools.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tools : get all the tools.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tools in body
     */
    @GetMapping("/tools")
    @Timed
    public ResponseEntity<List<Tools>> getAllTools(Pageable pageable) {
        log.debug("REST request to get a page of Tools");
        Page<Tools> page = toolsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/tools/get-all")
    @Timed
    public ResponseEntity<List<Tools>> getAllToolsByCompanyID(@RequestParam(required = false) UUID orgID,
                                                              @RequestParam(required = false) boolean isDependent) {
        log.debug("REST request to get a page of Tools");
        List<Tools> tools = toolsService.findAllByCompanyID(orgID, isDependent);
        return new ResponseEntity<>(tools, HttpStatus.OK);
    }

    /**
     * GET  /tools : get all the tools.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tools in body
     */
    @GetMapping("/tools/get-all-tool")
    @Timed
    public ResponseEntity<List<Tools>> getToolsActive() {
        log.debug("REST request to get a page of Tools");
        List<Tools> page = toolsService.getToolsActive();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tools");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /tools/:id : get the "id" tools.
     *
     * @param id the id of the tools to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tools, or with status 404 (Not Found)
     */
    @GetMapping("/tools/{id}")
    @Timed
    public ResponseEntity<Tools> getTools(@PathVariable UUID id) {
        log.debug("REST request to get Tools : {}", id);
        Optional<Tools> tools = toolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tools);
    }

    /**
     * DELETE  /tools/:id : delete the "id" tools.
     *
     * @param id the id of the tools to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteTools(@PathVariable UUID id) {
        log.debug("REST request to delete Tools : {}", id);
        toolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
