package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.MBCreditCard;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.MBCreditCardService;
//import vn.softdreams.ebweb.service.dto.MBCreditCardDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;
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
 * REST controller for managing MBCreditCard.
 */
@RestController
@RequestMapping("/api")
public class MBCreditCardResource {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardResource.class);

    private static final String ENTITY_NAME = "mBCreditCard";

    private final MBCreditCardService mBCreditCardService;
    @Autowired
    UtilsRepository utilsRepository;

    public MBCreditCardResource(MBCreditCardService mBCreditCardService) {
        this.mBCreditCardService = mBCreditCardService;
    }

    /**
     * POST  /mb-credit-cards : Create a new mBCreditCard.
     *
     * @param mBCreditCard the mBCreditCard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBCreditCard, or with status 400 (Bad Request) if the mBCreditCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-credit-cards")
    @Timed
    public ResponseEntity<MBCreditCardSaveDTO> createMBCreditCard(@Valid @RequestBody MBCreditCard mBCreditCard) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", mBCreditCard);
        if (mBCreditCard.getId() != null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBCreditCardSaveDTO result = new MBCreditCardSaveDTO();
        if (mBCreditCard.getmBCreditCardDetails().size() > 0) {
//            for(int i = 0;i<mBDeposit.getmBDepositDetails().size();i++){
//                mBDeposit.getmBDepositDetails().
//            }
        }
        result = mBCreditCardService.saveDTO(mBCreditCard);
        if (result.getMbCreditCard() != null) {
            if (result.getMbCreditCard().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/mb-credit-cards/" + result.getMbCreditCard().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getMbCreditCard().getId().toString()))
                    .body(result);
            }
        } else {
            return null;
        }
    }

    /**
     * PUT  /mb-credit-cards : Updates an existing mBCreditCard.
     *
     * @param mBCreditCard the mBCreditCard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBCreditCard,
     * or with status 400 (Bad Request) if the mBCreditCard is not valid,
     * or with status 500 (Internal Server Error) if the mBCreditCard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-credit-cards")
    @Timed
    public ResponseEntity<MBCreditCardSaveDTO> updateMBCreditCard(@Valid @RequestBody MBCreditCard mBCreditCard) throws URISyntaxException {
        log.debug("REST request to update MCReceipt : {}", mBCreditCard);
        if (mBCreditCard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBCreditCardSaveDTO result = new MBCreditCardSaveDTO();
        try {
            result = mBCreditCardService.saveDTO(mBCreditCard);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBCreditCard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-credit-cards : get all the mBCreditCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBCreditCards in body
     */
    @GetMapping("/mb-credit-cards")
    @Timed
    public ResponseEntity<List<MBCreditCard>> getAllMBCreditCards(Pageable pageable) {
        log.debug("REST request to get a page of MBCreditCards");
        Page<MBCreditCard> page = mBCreditCardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-credit-cards/:id : get the "id" mBCreditCard.
     *
     * @param id the id of the mBCreditCard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBCreditCard, or with status 404 (Not Found)
     */
    @GetMapping("/mb-credit-cards/{id}")
    @Timed
    public ResponseEntity<MBCreditCard> getMBCreditCard(@PathVariable UUID id) {
        log.debug("REST request to get MBCreditCard : {}", id);
        Optional<MBCreditCard> mBCreditCard = mBCreditCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBCreditCard);
    }

    /**
     * DELETE  /mb-credit-cards/:id : delete the "id" mBCreditCard.
     *
     * @param id the id of the mBCreditCard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-credit-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBCreditCard(@PathVariable UUID id) {
        log.debug("REST request to delete MBCreditCard : {}", id);
        mBCreditCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/mb-credit-cards/search-all")
    @Timed
    public ResponseEntity<List<MBCreditCardViewDTO>> findAll(Pageable pageable,
                                                             @RequestParam(required = false) String searchVoucher) {
        log.debug("REST request to get a page of mBCreditCards");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<MBCreditCardViewDTO> page = mBCreditCardService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/mb-credit-cards/findByRowNum")
    @Timed
    public ResponseEntity<MBCreditCard> getMBCreditCardByRowNum(@RequestParam(required = false) String searchVoucher,
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
        MBCreditCard mbCreditCard = mBCreditCardService.findOneByRowNum(searchVoucher1, rowNum);
        return new ResponseEntity<>(mbCreditCard, HttpStatus.OK);
    }

    @GetMapping("/mb-credit-cards/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPDF(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBCreditCardService.exportPDF(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/mb-credit-cards/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = mBCreditCardService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/mb-credit-cards/getIndexRow")
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
        List<Number> lstIndex = mBCreditCardService.getIndexRow(id, searchVoucher1);
        return new ResponseEntity<>(lstIndex, HttpStatus.OK);
    }

    @PostMapping("/mb-credit-cards/multi-delete-mb-credit-cards")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteMBCreditCard(@Valid @RequestBody List<MBCreditCard> mbCreditCards) {
        log.debug("REST request to closeBook : {}", mbCreditCards);
        HandlingResultDTO responeCloseBookDTO = mBCreditCardService.multiDelete(mbCreditCards);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/mb-credit-cards/multi-unrecord-mb-credit-cards")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecordMBCreditCards(@Valid @RequestBody List<MBCreditCard> mbCreditCards) {
        log.debug("REST request to closeBook : {}", mbCreditCards);
        HandlingResultDTO responeCloseBookDTO = mBCreditCardService.multiUnRecord(mbCreditCards);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
