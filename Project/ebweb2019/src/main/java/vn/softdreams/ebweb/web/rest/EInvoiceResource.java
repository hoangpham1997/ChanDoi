package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.EInvoice;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.EInvoiceService;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS.Request_SDS;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing InvoiceType.
 */
@RestController
@RequestMapping("/api")
public class EInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(EInvoiceResource.class);

    private static final String ENTITY_NAME = "eInvocie";

    private final EInvoiceService eInvoiceService;

    public EInvoiceResource(EInvoiceService eInvoiceService) {
        this.eInvoiceService = eInvoiceService;
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoice(Pageable pageable) {
        log.debug("REST request to get a page of InvoiceTypes");
        Page<EInvoice> page = eInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param id the id of the invoiceType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceType, or with status 404 (Not Found)
     */
    @GetMapping("/e-invoices/{id}")
    @Timed
    public ResponseEntity<EInvoice> getEInvoice(@PathVariable UUID id) {
        log.debug("REST request to get InvoiceType : {}", id);
        Optional<EInvoice> eInvoice = eInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eInvoice);
    }

    /**
     * GET data connectView
     *
     * @return
     */
    @GetMapping("/e-invoices/connect")
    @Timed
    public ResponseEntity<ConnectEInvoiceDTO> getConnect() {
        ConnectEInvoiceDTO connectEInvoiceDTO = eInvoiceService.getConnectEInvoiceDTO();
        return new ResponseEntity<>(connectEInvoiceDTO, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceType, or with status 404 (Not Found)
     * Hàm view hóa đơn thông thường
     */
    @GetMapping("/e-invoices/get-e-invoice-view")
    @Timed
    public ResponseEntity<byte[]> getEInvoiceView(
        @RequestParam(required = true) UUID id,
        @RequestParam(required = true) String pattern
    ) {
        log.debug("REST request to get InvoiceType : {}", id);
        Respone_SDS respone_sds = eInvoiceService.getViewInvoicePDF(id, pattern, 0);
        return new ResponseEntity<>(respone_sds.getRawBytes(), HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/publish-invoice")
    @Timed
    public ResponseEntity<Respone_SDS> publishInvoice(@Valid @RequestBody List<UUID> uuids) {
        log.debug("REST request to publish EInvoice : {}", uuids);
        Respone_SDS respone_sds = eInvoiceService.publishInvoice(uuids);

        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/get-digest-data")
    @Timed
    public ResponseEntity<Respone_SDS> getDigestData(@Valid @RequestBody Request_SDS request_sds) {
        log.debug("REST request to publish EInvoice : {}", request_sds);
        Respone_SDS respone_sds = eInvoiceService.getDigestData(request_sds.getIkeys(), request_sds.getCertString());

        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/sign-with-digest-data")
    @Timed
    public ResponseEntity<Respone_SDS> publishInvoiceWithCert(@Valid @RequestBody Request_SDS request_sds) {
        log.debug("REST request to publish EInvoice : {}", request_sds);
        Respone_SDS respone_sds = eInvoiceService.publishInvoiceWithCert(request_sds);

        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/connect")
    @Timed
    public ResponseEntity<Respone_SDS> connect(@Valid @RequestBody ConnectEInvoiceDTO connectEInvoiceDTO) {
        Respone_SDS respone_sds = eInvoiceService.connectEInvocie(connectEInvoiceDTO);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/delete")
    @Timed
    public ResponseEntity<Respone_SDS> deleteEInovice(@Valid @RequestBody List<UUID> uuids) {
//        Respone_SDS respone_sds = eInvoiceService.deleteEInovice(uuids);
        Respone_SDS respone_sds = new Respone_SDS();
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices/wait-sign")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoiceWaitSign(Pageable pageable) {
        log.debug("REST request to get a page of InvoiceTypes");
        Page<EInvoice> page = eInvoiceService.findAllEInvoiceWaitSign(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices/wait-sign");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices/canceled")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoiceCanceled(Pageable pageable) {
        log.debug("REST request to get a page of getAllEInvoiceCanceled");
        Page<EInvoice> page = eInvoiceService.findAllEInvoiceCanceled(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices/canceled");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices/convert")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoiceForConvert(Pageable pageable) {
        log.debug("REST request to get a page of getAllEInvoiceForConvert");
        Page<EInvoice> page = eInvoiceService.findAllEInvoiceForConvert(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices/convert");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices/adjusted")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoiceAdjusted(Pageable pageable) {
        log.debug("REST request to get a page of getAllEInvoiceForConvert");
        Page<EInvoice> page = eInvoiceService.findAllEInvoiceAdjusted(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices/adjusted");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/e-invoices/replaced")
    @Timed
    public ResponseEntity<List<EInvoice>> getAllEInvoiceReplaced(Pageable pageable) {
        log.debug("REST request to get a page of getAllEInvoiceForConvert");
        Page<EInvoice> page = eInvoiceService.findAllEInvoiceReplaced(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/e-invoices/replaced");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/create-invoice-wait-sign")
    @Timed
    public ResponseEntity<Respone_SDS> createInvoiceWaitSign(@Valid @RequestBody List<UUID> uuids) {
        log.debug("REST request to createInvoiceWaitSign EInvoice : {}", uuids);
        Respone_SDS respone_sds = eInvoiceService.createInvoiceWaitSign(uuids);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/cancel")
    @Timed
    public ResponseEntity<Respone_SDS> cancelInvoice(@Valid @RequestBody RequestCancelInvoiceDTO requestCancelInvoiceDTO) {
        log.debug("REST request to cancelInvoice EInvoice : {}", requestCancelInvoiceDTO);
        Respone_SDS respone_sds = eInvoiceService.cancelInvoice(requestCancelInvoiceDTO);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/converted-origin")
    @Timed
    public ResponseEntity<Respone_SDS> convertedOrigin(@Valid @RequestBody RequestConvertInvoiceDTO requestConvertInvoiceDTO) {
        log.debug("REST request to convertedOrigin EInvoice : {}", requestConvertInvoiceDTO);
        Respone_SDS respone_sds = eInvoiceService.convertedOrigin(requestConvertInvoiceDTO);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @GetMapping("/e-invoices/converted-storage")
    @Timed
    public ResponseEntity<byte[]> convertedStorage(
        @RequestParam(required = true) UUID id,
        @RequestParam(required = true) String pattern
    ) {
        log.debug("REST request to get InvoiceType : {}", id);
        Respone_SDS respone_sds = eInvoiceService.getViewInvoicePDF(id, pattern, 2);
        return new ResponseEntity<>(respone_sds.getRawBytes(), HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @GetMapping("/e-invoices/get-view-converted-origin")
    @Timed
    public ResponseEntity<byte[]> getViewConvertedOrigin(
        @RequestParam(required = true) UUID id,
        @RequestParam(required = true) String pattern
    ) {
        log.debug("REST request to get getViewConvertedOrigin : {}", id);
        Respone_SDS respone_sds = eInvoiceService.getViewInvoicePDF(id, pattern, 1);
        return new ResponseEntity<>(respone_sds.getRawBytes(), HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/e-invoices/send-mail")
    @Timed
    public ResponseEntity<Respone_SDS> sendMail(@Valid @RequestBody Map<UUID, String> ikeyEmail) {
        log.debug("REST request to sendMail EInvoice : {}", ikeyEmail);
        Respone_SDS respone_sds = eInvoiceService.sendMail(ikeyEmail);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * Hautv
     *
     * @param pageable
     * @param searchVoucher
     * @return
     */
    @GetMapping("/e-invoices/search")
    @Timed
    public ResponseEntity<List<EInvoice>> findAll(Pageable pageable,
                                                  @RequestParam(required = false) String searchVoucher,
                                                  @RequestParam() Integer typeEInvoice) {
        log.debug("REST request to get a page of Object");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<EInvoice> page = eInvoiceService.findAll(pageable, searchVoucher1, typeEInvoice);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/e-invoices/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * Hautv
     *
     * @param
     * @return
     */
    @GetMapping("/e-invoices/get-information-voucher-by-id")
    @Timed
    public ResponseEntity<InformationVoucherDTO> getInformationVoucherByID(@RequestParam() UUID id) {
        log.debug("REST request to get a page of Object");
        InformationVoucherDTO informationVoucherDTO = eInvoiceService.getInformationVoucherByID(id);
        return new ResponseEntity<>(informationVoucherDTO, HttpStatus.OK);
    }

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author Hautv
     * <p>
     * Hàm get Báo cáo
     */
    @PostMapping("/e-invoices/report")
    @Timed
    public ResponseEntity<String> getReportBusiness(@RequestBody RequestReport requestReport) throws JRException {
        HttpHeaders headers = new HttpHeaders();
        try {
            String result = null;
            switch (requestReport.getTypeReport()) {
                case TypeConstant.BAO_CAO.HDDT.BAO_CAO_TINH_HINH_SU_DUNG_HD:
                    result = eInvoiceService.baoCaoTinhHinhSDHD(requestReport);
                    break;
                case TypeConstant.BAO_CAO.HDDT.BANG_KE_HD_HHDV:
                    result = eInvoiceService.bangKeHDChungTuHHDV(requestReport);
                    break;
                case TypeConstant.BAO_CAO.HDDT.BAO_CAO_DOANH_THU_THEO_SP:
                    result = eInvoiceService.baoCaoDoanhThuTheoSP(requestReport);
                    break;
                case TypeConstant.BAO_CAO.HDDT.BAO_CAO_DOANH_THU_THEO_BEN_MUA:
                    result = eInvoiceService.baoCaoTheoDoanhThuTheoBenMua(requestReport);
                    break;
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Hautv
     *
     * @param
     * @return
     */
    @PostMapping("/e-invoices/load-data-miv")
    @Timed
    public ResponseEntity<Respone_SDS> loadDataMIV() {
        log.debug("REST request to get a page of Object");
        Respone_SDS respone_sds = eInvoiceService.loadAndUpdateDataFromMIV();
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

    /**
     * Hautv
     *
     * @param
     * @return
     */
    @PostMapping("/e-invoices/load-token-miv")
    @Timed
    public ResponseEntity<Respone_SDS> loadDataTokenMIV() {
        log.debug("REST request to get a page of Object");
        Respone_SDS respone_sds = eInvoiceService.loadDataToken();
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

}
