package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.MBCreditCard;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.MBTellerPaper;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MBTellerPaperService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.web.rest.dto.MBTellerPaperSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MBTellerPaper.
 */
@RestController
@RequestMapping("/api")
public class MBTellerPaperResource {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperResource.class);

    private static final String ENTITY_NAME = "mBTellerPaper";

    private final MBTellerPaperService mBTellerPaperService;

    public MBTellerPaperResource(MBTellerPaperService mBTellerPaperService) {
        this.mBTellerPaperService = mBTellerPaperService;
    }

    /**
     * POST  /mb-teller-papers : Create a new mBTellerPaper.
     *
     * @param mBTellerPaper the mBTellerPaper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBTellerPaper, or with status 400 (Bad Request) if the mBTellerPaper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-teller-papers")
    @Timed
    public ResponseEntity<MBTellerPaperSaveDTO> createMBTellerPaper(@Valid @RequestBody MBTellerPaper mBTellerPaper) throws URISyntaxException {
        log.debug("REST request to save MBTellerPaper : {}", mBTellerPaper);
        if (mBTellerPaper.getId() != null) {
            throw new BadRequestAlertException("A new mBTellerPaper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBTellerPaperSaveDTO result = new MBTellerPaperSaveDTO();
        result = mBTellerPaperService.saveDTO(mBTellerPaper, false);
        if (result.getmBTellerPaper().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/mb-teller-papers/" + result.getmBTellerPaper().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getmBTellerPaper().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /mb-teller-papers : Updates an existing mBTellerPaper.
     *
     * @param mBTellerPaper the mBTellerPaper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBTellerPaper,
     * or with status 400 (Bad Request) if the mBTellerPaper is not valid,
     * or with status 500 (Internal Server Error) if the mBTellerPaper couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-teller-papers")
    @Timed
    public ResponseEntity<MBTellerPaperSaveDTO> updateMBTellerPaper(@Valid @RequestBody MBTellerPaper mBTellerPaper) throws URISyntaxException {
        log.debug("REST request to update MBTellerPaper : {}", mBTellerPaper);
        if (mBTellerPaper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBTellerPaperSaveDTO result = new MBTellerPaperSaveDTO();
        try {
            result = mBTellerPaperService.saveDTO(mBTellerPaper, true);
        } catch (Exception ex) {

        }
        //result = mBTellerPaperService.findOne(result.getId()).get();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBTellerPaper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-teller-papers : get all the mBTellerPapers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBTellerPapers in body
     */
    @GetMapping("/mb-teller-papers")
    @Timed
    public ResponseEntity<List<MBTellerPaper>> getAllMBTellerPapers(Pageable pageable) {
        log.debug("REST request to get a page of MBTellerPapers");
        Page<MBTellerPaper> page = mBTellerPaperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-papers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-teller-papers/:id : get the "id" mBTellerPaper.
     *
     * @param id the id of the mBTellerPaper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaper, or with status 404 (Not Found)
     */
    @GetMapping("/mb-teller-papers/{id}")
    @Timed
    public ResponseEntity<MBTellerPaper> getMBTellerPaper(@PathVariable UUID id) {
        log.debug("REST request to get MBTellerPaper : {}", id);
        Optional<MBTellerPaper> mBTellerPaper = mBTellerPaperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBTellerPaper);
    }

    /**
     * DELETE  /mb-teller-papers/:id : delete the "id" mBTellerPaper.
     *
     * @param id the id of the mBTellerPaper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-teller-papers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBTellerPaper(@PathVariable UUID id) {
        log.debug("REST request to delete MBTellerPaper : {}", id);
        mBTellerPaperService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @param pageable
     * @return
     * @author hieugie
     */
    @GetMapping("/mb-teller-papers/search-all")
    @Timed
    public ResponseEntity<List<MBTellerPaper>> findAll(Pageable pageable,
                                                       @RequestParam(required = false) String searchVoucher
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<MBTellerPaper> page = mBTellerPaperService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-papers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-teller-papers/:id : get the "id" mBTellerPaper.
     *
     * @param searchVoucher str sort of the mBTellerPaper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaper, or with status 404 (Not Found)
     * @author mran
     */
    @GetMapping("/mb-teller-papers/findByRowNum")
    @Timed
    public ResponseEntity<MBTellerPaper> getMBTellerPaperByRowNum(@RequestParam(required = false) String searchVoucher,
                                                                  @RequestParam(required = false) Number rowNum
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MBTellerPaper mBTellerPaper = mBTellerPaperService.findOneByRowNum(searchVoucher1, rowNum);
        return new ResponseEntity<>(mBTellerPaper, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("/mb-teller-papersDTO")
    @Timed
    public ResponseEntity<List<MBTellerPaperDTO>> getAllMBTellerPaperDTO(Pageable pageable) {
        log.debug("REST request to get a page of mb-teller-papersDTO");
        Page<MBTellerPaperDTO> page = mBTellerPaperService.findAllDTOByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-papersDTO");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/mb-teller-papers/getIndexRow")
    @Timed
    public ResponseEntity<List<Number>> getIndexRow(@RequestParam(required = false) UUID id,
                                                    @RequestParam(required = false) String searchVoucher
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Number> lstIndex = mBTellerPaperService.getIndexRow(id, searchVoucher1);
        return new ResponseEntity<>(lstIndex, HttpStatus.OK);
    }

    @GetMapping(value = "/mb-teller-papers/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBTellerPaperService.exportPdf(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/mb-teller-papers/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBTellerPaperService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @PostMapping("/mb-teller-papers/multi-delete-mb-teller-papers")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteMBTellerPapers(@Valid @RequestBody List<MBTellerPaper> mbTellerPapers) {
        log.debug("REST request to closeBook : {}", mbTellerPapers);
        HandlingResultDTO responeCloseBookDTO = mBTellerPaperService.multiDelete(mbTellerPapers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/mb-teller-papers/multi-unrecord-mb-teller-papers")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecordMBTellerPapers(@Valid @RequestBody List<MBTellerPaper> mbTellerPapers) {
        log.debug("REST request to closeBook : {}", mbTellerPapers);
        HandlingResultDTO responeCloseBookDTO = mBTellerPaperService.multiUnRecord(mbTellerPapers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
