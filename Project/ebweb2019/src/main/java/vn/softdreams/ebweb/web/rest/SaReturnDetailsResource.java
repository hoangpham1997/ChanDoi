package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.service.SaReturnDetailsService;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDetailsDTO;
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
 * REST controller for managing SaReturnDetails.
 */
@RestController
@RequestMapping("/api")
public class SaReturnDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SaReturnDetailsResource.class);

    private static final String ENTITY_NAME = "saReturnDetails";

    private final SaReturnDetailsService saReturnDetailsService;

    public SaReturnDetailsResource(SaReturnDetailsService saReturnDetailsService) {
        this.saReturnDetailsService = saReturnDetailsService;
    }

    /**
     * GET  /sa-return-details/:id : get the "id" saReturnDetails.
     *
     * @param id the id of the saReturnDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saReturnDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sa-return-details/{id}")
    @Timed
    public ResponseEntity<SaReturnDetailsDTO> getSaReturnDetails(@PathVariable UUID id) {
        log.debug("REST request to get SaReturnDetails : {}", id);
        SaReturnDetailsDTO saReturnDetails = saReturnDetailsService.findOne(id);
        return new ResponseEntity<>(saReturnDetails, HttpStatus.OK);
    }

}
