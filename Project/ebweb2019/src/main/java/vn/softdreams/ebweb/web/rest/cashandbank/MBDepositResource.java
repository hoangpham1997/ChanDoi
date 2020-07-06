package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MBDepositService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.MBDepositDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.CloseBookDTO;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MBDeposit.
 */
@RestController
@RequestMapping("/api")
public class MBDepositResource {

    private final Logger log = LoggerFactory.getLogger(MBDepositResource.class);

    private static final String ENTITY_NAME = "AccounntingObject";

    private final MBDepositService mBDepositService;
    @Autowired
    UtilsRepository utilsRepository;

    public MBDepositResource(MBDepositService mBDepositService) {
        this.mBDepositService = mBDepositService;
    }

    /**
     * POST  /mb-deposits : Create a new mBDeposit.
     *
     * @param mBDeposit the mBDeposit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBDeposit, or with status 400 (Bad Request) if the mBDeposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-deposits")
    @Timed
    public ResponseEntity<MBDepositSaveDTO> createMBDeposit(@Valid @RequestBody MBDeposit mBDeposit) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", mBDeposit);
        if (mBDeposit.getId() != null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBDepositSaveDTO result = new MBDepositSaveDTO();
        if (mBDeposit.getmBDepositDetails().size() > 0) {
//            for(int i = 0;i<mBDeposit.getmBDepositDetails().size();i++){
//                mBDeposit.getmBDepositDetails().
//            }
        }
        result = mBDepositService.saveDTO(mBDeposit);
        if (result.getMbDeposit() != null) {
            if (result.getMbDeposit().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/mb-deposits/" + result.getMbDeposit().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getMbDeposit().getId().toString()))
                    .body(result);
            }
        } else {
            return null;
        }
    }

    /**
     * PUT  /mb-deposits : Updates an existing mBDeposit.
     *
     * @param mBDeposit the mBDeposit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBDeposit,
     * or with status 400 (Bad Request) if the mBDeposit is not valid,
     * or with status 500 (Internal Server Error) if the mBDeposit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-deposits")
    @Timed
    public ResponseEntity<MBDepositSaveDTO> updateMBDeposit(@Valid @RequestBody MBDeposit mBDeposit) throws URISyntaxException {
        log.debug("REST request to update MCReceipt : {}", mBDeposit);
        if (mBDeposit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBDepositSaveDTO result = new MBDepositSaveDTO();
        try {
            result = mBDepositService.saveDTO(mBDeposit);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBDeposit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-deposits : get all the mBDeposits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBDeposits in body
     */
    @GetMapping("/mb-deposits")
    @Timed
    public ResponseEntity<List<MBDeposit>> getAllMBDeposits(Pageable pageable) {
        log.debug("REST request to get a page of MBDeposits");
        Page<MBDeposit> page = mBDepositService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /mb-deposits/:id : get the "id" mBDeposit.
     *
     * @param id the id of the mBDeposit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBDeposit, or with status 404 (Not Found)
     */
    @GetMapping("/mb-deposits/{id}")
    @Timed
    public ResponseEntity<MBDeposit> getMBDeposit(@PathVariable UUID id) {
        log.debug("REST request to get MBDeposit : {}", id);
        Optional<MBDeposit> mBDeposit = mBDepositService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBDeposit);
    }

    /**
     * DELETE  /mb-deposits/:id : delete the "id" mBDeposit.
     *
     * @param id the id of the mBDeposit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-deposits/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBDeposit(@PathVariable UUID id) {
        log.debug("REST request to delete MBDeposit : {}", id);
        mBDepositService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/mb-deposits/multi-delete-mb-deposits")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMBDeposit(@Valid @RequestBody List<MBDeposit> mbDeposits) {
        log.debug("REST request to closeBook : {}", mbDeposits);
        HandlingResultDTO responeCloseBookDTO = mBDepositService.multiDelete(mbDeposits);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PutMapping("/mb-deposits/mutiple-record")
    @Timed
    public ResponseEntity<Void> mutipleRecordMBDeposit(@Valid @RequestBody MutipleRecord mutipleRecord) {
        mBDepositService.mutipleRecord(mutipleRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/mb-deposits/search-all")
    @Timed
    public ResponseEntity<List<MBDepositViewDTO>> findAll(Pageable pageable,
                                                          @RequestParam(required = false) String searchVoucher) {
        log.debug("REST request to get a page of mBDeposits");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<MBDepositViewDTO> page = mBDepositService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Author nam
     * <p>
     * Lay du lieu deposit theo id
     *
     * @param mbDepositId
     * @return
     */
    @GetMapping("/mb-deposits/get-all-data")
    @Timed
    public ResponseEntity<MBDepositDTO> getAllData(@RequestParam UUID mbDepositId) {
        log.debug("REST request to get a page of MBDeposits");
        MBDepositDTO mbDepositDTO = mBDepositService.getAllData(mbDepositId);
        return new ResponseEntity<>(mbDepositDTO, HttpStatus.OK);
    }

    @GetMapping("/mb-deposits/findByRowNum")
    @Timed
    public ResponseEntity<MBDeposit> getMBDepositByRowNum(@RequestParam(required = false) String searchVoucher,
                                                          @RequestParam(required = false) Number rowNum

    ) {
        log.debug("REST request to get a page of mBDeposits");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MBDeposit mBTellerPaper = mBDepositService.findOneByRowNum(searchVoucher1, rowNum);
        return new ResponseEntity<>(mBTellerPaper, HttpStatus.OK);
    }

    @GetMapping(value = "/mb-deposits/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPDF(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBDepositService.exportPDF(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/mb-deposits/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBDepositService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/mb-deposits/getIndexRow")
    @Timed
    public ResponseEntity<List<Number>> getIndexRow(@RequestParam(required = false) UUID id,
                                                    @RequestParam(required = false) String searchVoucher
    ) {
        log.debug("REST request to get a page of mBDeposits");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Number> lstIndex = mBDepositService.getIndexRow(id, searchVoucher1);
        return new ResponseEntity<>(lstIndex, HttpStatus.OK);
    }

    @PostMapping("/mb-deposits/multi-unrecord-mb-deposits")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnrecordMBDeposit(@Valid @RequestBody List<MBDeposit> mbDeposits) {
        log.debug("REST request to closeBook : {}", mbDeposits);
        HandlingResultDTO responeCloseBookDTO = mBDepositService.multiUnrecord(mbDeposits);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
