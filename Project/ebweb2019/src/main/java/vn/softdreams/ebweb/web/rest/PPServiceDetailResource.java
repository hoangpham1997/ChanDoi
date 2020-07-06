package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.PPServiceDetailService;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PPServiceDetailResource {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumResource.class);

    private static final String ENTITY_NAME = "ppServiceDetail";

    private final PPServiceDetailService ppServiceDetailService;

    public PPServiceDetailResource(PPServiceDetailService ppServiceDetailService) {
        this.ppServiceDetailService = ppServiceDetailService;
    }

    @GetMapping("ppServiceDetail/find-by-id")
    @Timed
    public ResponseEntity<List<PPServiceDetailDTO>> findAllPPService(Pageable pageable, @RequestParam() UUID ppServiceId) {
        log.debug("REST request to get a page of PPServiceDTO");
        List<PPServiceDetailDTO> allPPServiceDetailByPPServiceId = ppServiceDetailService.findAllPPServiceDetailByPPServiceId(ppServiceId);
        return new ResponseEntity<>(allPPServiceDetailByPPServiceId, HttpStatus.OK);
    }


    /**
     * @Author Hautv
     * @param pageable
     * @param paymentVoucherID
     * @return
     */
    @GetMapping("ppServiceDetail/find-by-paymentvoucherid")
    @Timed
    public ResponseEntity<List<PPServiceDetailDTO>> findAllPPServiceByPaymentVoucherID(Pageable pageable, @RequestParam() UUID paymentVoucherID) {
        log.debug("REST request to get a page of PPServiceDTO");
        List<PPServiceDetailDTO> allPPServiceDetailByPPServiceId = ppServiceDetailService.findAllPPServiceDetailByPaymentVoucherID(paymentVoucherID);
        return new ResponseEntity<>(allPPServiceDetailByPPServiceId, HttpStatus.OK);
    }

    /**
     * PUT  /pp-invoices : Updates an existing pPInvoice.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoice,
     * or with status 400 (Bad Request) if the pPInvoice is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ppServiceDetail/update-receive-bill")
    @Timed
    public ResponseEntity<Void> updatePPService(@Valid @RequestBody ReceiveBill receiveBill){
        ppServiceDetailService.saveReceiveBillPPService(receiveBill);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
