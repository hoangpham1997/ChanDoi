package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.jhipster.web.util.ResponseUtil;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.ViewVoucherNoService;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CloseBookDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.ResponeCloseBookDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDateClosedBook;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing InvoiceType.
 */
@RestController
@RequestMapping("/api")
public class ViewVoucherNoResource {

    private final Logger log = LoggerFactory.getLogger(ViewVoucherNoResource.class);

    private static final String ENTITY_NAME = "viewVoucherNoService";

    private final ViewVoucherNoService viewVoucherNoService;
    private final UserService userService;

    public ViewVoucherNoResource(ViewVoucherNoService viewVoucherNoService, UserService userService) {
        this.viewVoucherNoService = viewVoucherNoService;
        this.userService = userService;
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/view-voucher-no")
    @Timed
    public ResponseEntity<List<ViewVoucherNo>> getAllEInvoice(Pageable pageable) {
        log.debug("REST request to get a page of InvoiceTypes");
        Page<ViewVoucherNo> page = viewVoucherNoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-voucher-no");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/view-voucher-no/get-all-voucher-not-recorded")
    @Timed
    public ResponseEntity<List<ViewVoucherNo>> getAllVoucherNotRecorded(Pageable pageable,
                                                                        @RequestParam LocalDate postedDate,
                                                                        @RequestParam(required = false) String listBranch) {
        log.debug("REST request to get a page of get All Voucher Not Recorded");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<OrgTreeDTO> list = null;
        if (listBranch != null) {
            try {
                list = objectMapper.readValue(listBranch, new TypeReference<List<OrgTreeDTO>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (list != null) {
            String check = viewVoucherNoService.checkCloseBookForListBranch(list, postedDate);
            if (!StringUtils.isEmpty(check)) {
                throw new BadRequestAlertException(check, "", "canCloseBook");
            }
        }
        Page<ViewVoucherNo> page = viewVoucherNoService.getAllVoucherNotRecorded(pageable, postedDate);
        if (page.getTotalElements() == 0) {
            if (list != null) {
                viewVoucherNoService.updateCloseBookDateForBranch(list, postedDate);
            }
            UpdateDateClosedBook updateDateClosedBook = new UpdateDateClosedBook();
            String dBDateClosed = Utils.DBDateClosed(userService.getAccount());
            if (!StringUtils.isEmpty(dBDateClosed)) {
                LocalDate dBDateClosedOld = LocalDate.parse(dBDateClosed);
                updateDateClosedBook.setDateClosedBookOld(dBDateClosedOld);
            }
            updateDateClosedBook.setDateClosedBook(postedDate);
            updateDateClosedBook(updateDateClosedBook);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-voucher-no");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param id the id of the invoiceType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceType, or with status 404 (Not Found)
     */
    @GetMapping("/view-voucher-no/{id}")
    @Timed
    public ResponseEntity<ViewVoucherNo> getEInvoice(@PathVariable UUID id) {
        log.debug("REST request to get InvoiceType : {}", id);
        Optional<ViewVoucherNo> viewVoucherNo = viewVoucherNoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewVoucherNo);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/view-voucher-no/close-book")
    @Timed
    public ResponseEntity<HandlingResultDTO> closeBook(@Valid @RequestBody CloseBookDTO closeBook) {
        log.debug("REST request to closeBook : {}", closeBook);
        HandlingResultDTO responeCloseBookDTO = viewVoucherNoService.closeBook(closeBook);

        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/view-voucher-no/update-date-closed-book")
    @Timed
    public ResponseEntity<Boolean> updateDateClosedBook(@Valid @RequestBody UpdateDateClosedBook updateDateClosedBook) {
        log.debug("REST request to updateDateClosedBook : {}", updateDateClosedBook);
        Boolean result = viewVoucherNoService.updateDateClosedBook(updateDateClosedBook);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
