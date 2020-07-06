package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAllocationPost;
import vn.softdreams.ebweb.service.TIAllocationPostService;
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
 * REST controller for managing TIAllocationPost.
 */
@RestController
@RequestMapping("/api")
public class TIAllocationPostResource {

    private final Logger log = LoggerFactory.getLogger(TIAllocationPostResource.class);

    private static final String ENTITY_NAME = "tIAllocationPost";

    private final TIAllocationPostService tIAllocationPostService;

    public TIAllocationPostResource(TIAllocationPostService tIAllocationPostService) {
        this.tIAllocationPostService = tIAllocationPostService;
    }

    /**
     * POST  /t-i-allocation-posts : Create a new tIAllocationPost.
     *
     * @param tIAllocationPost the tIAllocationPost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAllocationPost, or with status 400 (Bad Request) if the tIAllocationPost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-allocation-posts")
    @Timed
    public ResponseEntity<TIAllocationPost> createTIAllocationPost(@Valid @RequestBody TIAllocationPost tIAllocationPost) throws URISyntaxException {
        log.debug("REST request to save TIAllocationPost : {}", tIAllocationPost);
        if (tIAllocationPost.getId() != null) {
            throw new BadRequestAlertException("A new tIAllocationPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAllocationPost result = tIAllocationPostService.save(tIAllocationPost);
        return ResponseEntity.created(new URI("/api/t-i-allocation-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-allocation-posts : Updates an existing tIAllocationPost.
     *
     * @param tIAllocationPost the tIAllocationPost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAllocationPost,
     * or with status 400 (Bad Request) if the tIAllocationPost is not valid,
     * or with status 500 (Internal Server Error) if the tIAllocationPost couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-allocation-posts")
    @Timed
    public ResponseEntity<TIAllocationPost> updateTIAllocationPost(@Valid @RequestBody TIAllocationPost tIAllocationPost) throws URISyntaxException {
        log.debug("REST request to update TIAllocationPost : {}", tIAllocationPost);
        if (tIAllocationPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAllocationPost result = tIAllocationPostService.save(tIAllocationPost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAllocationPost.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-allocation-posts : get all the tIAllocationPosts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAllocationPosts in body
     */
    @GetMapping("/t-i-allocation-posts")
    @Timed
    public ResponseEntity<List<TIAllocationPost>> getAllTIAllocationPosts(Pageable pageable) {
        log.debug("REST request to get a page of TIAllocationPosts");
        Page<TIAllocationPost> page = tIAllocationPostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-allocation-posts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-allocation-posts/:id : get the "id" tIAllocationPost.
     *
     * @param id the id of the tIAllocationPost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAllocationPost, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-allocation-posts/{id}")
    @Timed
    public ResponseEntity<TIAllocationPost> getTIAllocationPost(@PathVariable UUID id) {
        log.debug("REST request to get TIAllocationPost : {}", id);
        Optional<TIAllocationPost> tIAllocationPost = tIAllocationPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAllocationPost);
    }

    /**
     * DELETE  /t-i-allocation-posts/:id : delete the "id" tIAllocationPost.
     *
     * @param id the id of the tIAllocationPost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-allocation-posts/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAllocationPost(@PathVariable UUID id) {
        log.debug("REST request to delete TIAllocationPost : {}", id);
        tIAllocationPostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
