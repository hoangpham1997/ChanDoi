package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TypeGroup;
import vn.softdreams.ebweb.service.TypeGroupService;
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

/**
 * REST controller for managing TypeGroup.
 */
@RestController
@RequestMapping("/api")
public class TypeGroupResource {

    private final Logger log = LoggerFactory.getLogger(TypeGroupResource.class);

    private static final String ENTITY_NAME = "typeGroup";

    private final TypeGroupService typeGroupService;

    public TypeGroupResource(TypeGroupService typeGroupService) {
        this.typeGroupService = typeGroupService;
    }

    /**
     * POST  /type-groups : Create a new typeGroup.
     *
     * @param typeGroup the typeGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeGroup, or with status 400 (Bad Request) if the typeGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-groups")
    @Timed
    public ResponseEntity<TypeGroup> createTypeGroup(@RequestBody TypeGroup typeGroup) throws URISyntaxException {
        log.debug("REST request to save TypeGroup : {}", typeGroup);
        if (typeGroup.getId() != null) {
            throw new BadRequestAlertException("A new typeGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeGroup result = typeGroupService.save(typeGroup);
        return ResponseEntity.created(new URI("/api/type-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-groups : Updates an existing typeGroup.
     *
     * @param typeGroup the typeGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeGroup,
     * or with status 400 (Bad Request) if the typeGroup is not valid,
     * or with status 500 (Internal Server Error) if the typeGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-groups")
    @Timed
    public ResponseEntity<TypeGroup> updateTypeGroup(@RequestBody TypeGroup typeGroup) throws URISyntaxException {
        log.debug("REST request to update TypeGroup : {}", typeGroup);
        if (typeGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeGroup result = typeGroupService.save(typeGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-groups : get all the typeGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeGroups in body
     */
    @GetMapping("/type-groups")
    @Timed
    public ResponseEntity<List<TypeGroup>> getAllTypeGroups() {
        log.debug("REST request to get a page of TypeGroups");
        List<TypeGroup> page = typeGroupService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/type-groups/popup")
    @Timed
    public ResponseEntity<List<TypeGroup>> getAllTypeGroupsForPopup() {
        log.debug("REST request to get a page of TypeGroups");
        List<TypeGroup> page = typeGroupService.getAllTypeGroupsForPopup();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /type-groups/:id : get the "id" typeGroup.
     *
     * @param id the id of the typeGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeGroup, or with status 404 (Not Found)
     */
    @GetMapping("/type-groups/{id}")
    @Timed
    public ResponseEntity<TypeGroup> getTypeGroup(@PathVariable Long id) {
        log.debug("REST request to get TypeGroup : {}", id);
        Optional<TypeGroup> typeGroup = typeGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeGroup);
    }

    /**
     * DELETE  /type-groups/:id : delete the "id" typeGroup.
     *
     * @param id the id of the typeGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeGroup(@PathVariable Long id) {
        log.debug("REST request to delete TypeGroup : {}", id);
        typeGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
