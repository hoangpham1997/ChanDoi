package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.GOtherVoucherService;
import vn.softdreams.ebweb.service.dto.GOtherVoucherSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherGeneralDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherViewDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
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
 * REST controller for managing GOtherVoucher.
 */
@RestController
@RequestMapping("/api")
public class GOtherVoucherResource {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherResource.class);

    private static final String ENTITY_NAME = "gOtherVoucher";

    private final GOtherVoucherService gOtherVoucherService;

    public GOtherVoucherResource(GOtherVoucherService gOtherVoucherService) {
        this.gOtherVoucherService = gOtherVoucherService;
    }

    /**
     * POST  /g-other-vouchers : Create a new gOtherVoucher.
     *
     * @param gOtherVoucher the gOtherVoucher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gOtherVoucher, or with status 400 (Bad Request) if the gOtherVoucher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/g-other-vouchers")
    @Timed
    public ResponseEntity<GOtherVoucherSaveDTO> createGOtherVoucher(@RequestBody GOtherVoucher gOtherVoucher) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", gOtherVoucher);
        if (gOtherVoucher.getId() != null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherSaveDTO result = new GOtherVoucherSaveDTO();
        if (gOtherVoucher.getgOtherVoucherDetails().size() > 0) {
//            for(int i = 0;i<mBDeposit.getmBDepositDetails().size();i++){
//                mBDeposit.getmBDepositDetails().
//            }
        }
        result = gOtherVoucherService.saveDTO(gOtherVoucher);
        if (result.getgOtherVoucher() != null) {
            if (result.getgOtherVoucher().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/g-other-voucher/" + result.getgOtherVoucher().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getgOtherVoucher().getId().toString()))
                    .body(result);
            }
        } else {
            return null;
        }
    }

    @PostMapping("/g-other-vouchers/allocation")
    @Timed
    public ResponseEntity<GOtherVoucherGeneralDTO> createGOtherVoucherPB(@RequestBody GOtherVoucherGeneralDTO gOtherVoucherGeneralDTO) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", gOtherVoucherGeneralDTO);
        if (gOtherVoucherGeneralDTO.getgOtherVoucher().getId() != null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherGeneralDTO result = new GOtherVoucherGeneralDTO();
        result = gOtherVoucherService.saveGOtherVoucherGeneralDTO(gOtherVoucherGeneralDTO);
        if (result.getgOtherVoucher() != null) {
            if (result.getgOtherVoucher().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/g-other-voucher/" + result.getgOtherVoucher().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getgOtherVoucher().getId().toString()))
                    .body(result);
            }
        } else {
            throw new BadRequestAlertException("addNewError", ENTITY_NAME, "idexists");
        }
    }

    @PutMapping("/g-other-vouchers/allocation")
    @Timed
    public ResponseEntity<GOtherVoucherGeneralDTO> updateGOtherVoucherPB(@RequestBody GOtherVoucherGeneralDTO gOtherVoucherGeneralDTO) throws URISyntaxException {
        log.debug("REST request to save MBDeposit : {}", gOtherVoucherGeneralDTO);
        if (gOtherVoucherGeneralDTO.getgOtherVoucher().getId() == null) {
            throw new BadRequestAlertException("A new mBDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherGeneralDTO result = new GOtherVoucherGeneralDTO();
        result = gOtherVoucherService.saveGOtherVoucherGeneralDTO(gOtherVoucherGeneralDTO);
        if (result.getgOtherVoucher() != null) {
            if (result.getgOtherVoucher().getId() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.created(new URI("/api/g-other-voucher/" + result.getgOtherVoucher().getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getgOtherVoucher().getId().toString()))
                    .body(result);
            }
        } else {
            throw new BadRequestAlertException("updateError", ENTITY_NAME, "idexists");
        }
    }

    /**
     * PUT  /g-other-vouchers : Updates an existing gOtherVoucher.
     *
     * @param gOtherVoucher the gOtherVoucher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gOtherVoucher,
     * or with status 400 (Bad Request) if the gOtherVoucher is not valid,
     * or with status 500 (Internal Server Error) if the gOtherVoucher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/g-other-vouchers")
    @Timed
    public ResponseEntity<GOtherVoucherSaveDTO> updateGOtherVoucher(@RequestBody GOtherVoucher gOtherVoucher) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucher : {}", gOtherVoucher);
        if (gOtherVoucher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GOtherVoucherSaveDTO result = new GOtherVoucherSaveDTO();
        try {
            result = gOtherVoucherService.saveDTO(gOtherVoucher);
        } catch (Exception ex) {

        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gOtherVoucher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /g-other-vouchers : get all the gOtherVouchers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gOtherVouchers in body
     */
    @GetMapping("/g-other-vouchers")
    @Timed
    public ResponseEntity<List<GOtherVoucher>> getAllGOtherVouchers(Pageable pageable) {
        log.debug("REST request to get a page of GOtherVouchers");
        Page<GOtherVoucher> page = gOtherVoucherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /g-other-vouchers/:id : get the "id" gOtherVoucher.
     *
     * @param id the id of the gOtherVoucher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gOtherVoucher, or with status 404 (Not Found)
     */
    @GetMapping("/g-other-vouchers/{id}")
    @Timed
    public ResponseEntity<GOtherVoucher> getGOtherVoucher(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucher : {}", id);
        Optional<GOtherVoucher> gOtherVoucher = gOtherVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gOtherVoucher);
    }

    @GetMapping("/g-other-vouchers/detail/view/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherGeneralViewDTO> findDetailViewPB(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucher : {}", id);
        GOtherVoucherGeneralViewDTO gOtherVoucher = gOtherVoucherService.findDetailViewPB(id);
        return new ResponseEntity<>(gOtherVoucher, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/detail/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherGeneralDoubleClickDTO> findDetailPB(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucher : {}", id);
        GOtherVoucherGeneralDoubleClickDTO gOtherVoucher = gOtherVoucherService.findDetailPB(id);
        return new ResponseEntity<>(gOtherVoucher, HttpStatus.OK);
    }

    /**
     * DELETE  /g-other-vouchers/:id : delete the "id" gOtherVoucher.
     *
     * @param id the id of the gOtherVoucher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/g-other-vouchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucher(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucher : {}", id);
        gOtherVoucherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/g-other-vouchers/allocation/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucherPB(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucher : {}", id);
        gOtherVoucherService.deletePB(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/g-other-vouchers/search-all")
    @Timed
    public ResponseEntity<List<GOtherVoucherViewDTO>> findAll(Pageable pageable,
                                                              @RequestParam(required = false) String searchVoucher) {
        log.debug("REST request to get a page of mBDeposits");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<GOtherVoucherViewDTO> page = gOtherVoucherService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/findByRowNum")
    @Timed
    public ResponseEntity<GOtherVoucher> getGOtherVoucherByRowNum(@RequestParam(required = false) String searchVoucher,
                                                                  @RequestParam(required = false) Number rowNum

    ) {
        log.debug("REST request to get a page of gOtherVouchers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GOtherVoucher gOtherVoucher = gOtherVoucherService.findOneByRowNum(searchVoucher1, rowNum);
        return new ResponseEntity<>(gOtherVoucher, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/find-by-row-num-allocations")
    @Timed
    public ResponseEntity<GOtherVoucher> getGOtherVoucherByRowNumPB(@RequestParam(required = false) String fromDate,
                                                                    @RequestParam(required = false) String toDate,
                                                                    @RequestParam(required = false) String textSearch,
                                                                    @RequestParam(required = false) Integer rowNum

    ) {
        log.debug("REST request to get a page of gOtherVouchers");
        GOtherVoucher gOtherVoucher = gOtherVoucherService.findOneByRowNumPB(fromDate, toDate, textSearch, rowNum);
        return new ResponseEntity<>(gOtherVoucher, HttpStatus.OK);
    }

    @GetMapping(value = "/g-other-vouchers/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPDF(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = gOtherVoucherService.exportPDF(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/g-other-vouchers/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = gOtherVoucherService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/g-other-vouchers/export-pb/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPDFPB(Pageable pageable,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String textSearch) throws IOException {
        byte[] export = gOtherVoucherService.exportPDFPB(pageable, fromDate, toDate, textSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/g-other-vouchers/export-pb/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcelPB(Pageable pageable,
                                                @RequestParam(required = false) String fromDate,
                                                @RequestParam(required = false) String toDate,
                                                @RequestParam(required = false) String textSearch) throws IOException {

        byte[] export = gOtherVoucherService.exportExcelPB(pageable, fromDate, toDate, textSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/getIndexRow")
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
        List<Number> lstIndex = gOtherVoucherService.getIndexRow(id, searchVoucher1);
        return new ResponseEntity<>(lstIndex, HttpStatus.OK);
    }

    /**
     * lấy dữ liệu phần trên màn danh sách kết chuyễn lãi lỗ
     *
     * @param pageable
     * @param fromDate
     * @param toDate
     * @param status
     * @param keySearch
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers/search")
    @Timed
    public ResponseEntity<List<GOtherVoucherKcDsDTO>> searchPPInvoice(Pageable pageable,
                                                                      @RequestParam(required = false) String fromDate,
                                                                      @RequestParam(required = false) String toDate,
                                                                      @RequestParam(required = false) Integer status,
                                                                      @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of PPDiscountReturns");
        Page<GOtherVoucherKcDsDTO> page = gOtherVoucherService.searchGOtherVoucher(pageable, fromDate, toDate, status, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoices/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * lấy phần chi tiết ngoài màn danh sách
     *
     * @param id
     * @return
     * @author congd
     */
    @GetMapping("/g-other-vouchers/get-detail-by-gothervoucher-id")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailKcDTO>> getGOtherVoucherDetailByGOtherVoucherId(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        List<GOtherVoucherDetailKcDTO> gOtherVoucherDetailKcDTOS = gOtherVoucherService.getGOtherVoucherDetailByGOtherVoucherId(id);
        return new ResponseEntity<>(gOtherVoucherDetailKcDTOS, HttpStatus.OK);
    }

    /**
     * lấy dữ liệu mà chi tiết hoặc màn sửa
     *
     * @param id
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers/get-by-id")
    @Timed
    public ResponseEntity<GOtherVoucherKcDTO> getGOtherVoucherById(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        GOtherVoucherKcDTO gOtherVoucherKcDTO = gOtherVoucherService.getGOtherVoucherById(id);
        return new ResponseEntity<>(gOtherVoucherKcDTO, HttpStatus.OK);
    }

    /**
     * thêm mới hoặc sửa kết chuyễn lãi lỗ
     *
     * @param gOtherVoucherKc
     * @return
     * @throws URISyntaxException
     * @author congnd
     */
    @PostMapping("/g-other-vouchers-kc/save")
    @Timed
    public ResponseEntity<UpdateDataDTO> saveGOtherVoucherKc(@Valid @RequestBody GOtherVoucherKcDTO gOtherVoucherKc) throws URISyntaxException {
        UpdateDataDTO result = gOtherVoucherService.saveGOtherVoucherKc(gOtherVoucherKc);
        return ResponseEntity.created(new URI("/api/g-other-vouchers-kc/save"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.toString()))
            .body(result);
    }

    /**
     * @param id
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers-kc/delete-by-id")
    @Timed
    public ResponseEntity<ResultDTO> deleteById(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = gOtherVoucherService.deleteById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @param status
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @param rowNum
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers-kc/index")
    @Timed
    public ResponseEntity<GOtherVoucherKcDTO> findByRowNumKc(Pageable pageable,
                                                             @RequestParam(required = false) Integer status,
                                                             @RequestParam(required = false) String fromDate,
                                                             @RequestParam(required = false) String toDate,
                                                             @RequestParam(required = false) String searchValue,
                                                             @RequestParam(required = false) Integer rowNum
    ) {
        log.debug("REST request to get a page of findByRowNum");
        GOtherVoucherKcDTO gOtherVoucherKcDTO = gOtherVoucherService.findIdByRowNumKc(pageable, status, fromDate, toDate, searchValue, rowNum);
        return new ResponseEntity<>(gOtherVoucherKcDTO, HttpStatus.OK);
    }


    /**
     * @param status
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers/kc-export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdfKc(@RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String searchValue
    ) {
        byte[] export = gOtherVoucherService.exportPdfKc(status, fromDate, toDate, searchValue);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * @param status
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @return
     * @author congnd
     */
    @GetMapping("/g-other-vouchers/kc-export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcelKc(@RequestParam(required = false) Integer status,
                                                @RequestParam(required = false) String fromDate,
                                                @RequestParam(required = false) String toDate,
                                                @RequestParam(required = false) String searchValue
    ) {
        byte[] export = gOtherVoucherService.exportExcelKc(status, fromDate, toDate, searchValue);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * Lấy dữ liệu kết chuyễn lãi lỗ vs những tài khoản khác 4212, 413
     *
     * @param postDate
     * @return
     * @author congnd
     */
    @GetMapping("/view-vouchers/get-data-kc")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailDataKcDTO>> getDataKc(@RequestParam String postDate) {
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetailsList = gOtherVoucherService.getDataKc(postDate);
        return new ResponseEntity<>(gOtherVoucherDetailsList, HttpStatus.OK);
    }

    /**
     * Lấy dữ liệu kết chuyễn lãi lỗ vs những tài khoản 4212
     *
     * @param postDate
     * @return
     * @author congnd
     */
    @GetMapping("/view-vouchers/get-data-kc-diff")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailDataKcDTO>> getDataKcDiff(@RequestParam String postDate) {
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetailsList = gOtherVoucherService.getDataKcDiff(postDate);
        return new ResponseEntity<>(gOtherVoucherDetailsList, HttpStatus.OK);
    }

    /**
     * Lấy dữ liệu kết chuyễn lãi lỗ vs những tài khoản 413
     *
     * @param postDate
     * @return
     */
    @GetMapping("/view-vouchers/get-data-kc-account-special")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailDataKcDTO>> getDataAccount413(@RequestParam String postDate) {
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetailsList = gOtherVoucherService.getDataAccountSpecial(postDate);
        return new ResponseEntity<>(gOtherVoucherDetailsList, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/search-all-g-other-vouchers")
    @Timed
    public ResponseEntity<List<GOtherVoucherDTO>> searchAllPB(Pageable pageable,
                                                              @RequestParam(required = false) String fromDate,
                                                              @RequestParam(required = false) String toDate,
                                                              @RequestParam(required = false) String textSearch
    ) {
        log.debug("REST request to get a page of mBDeposits");
        Page<GOtherVoucherDTO> page = gOtherVoucherService.searchAllPB(pageable, fromDate, toDate, textSearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/row-num")
    @Timed
    public ResponseEntity<Integer> findRowNumByID(@RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  @RequestParam(required = false) String textSearch,
                                                  @RequestParam(required = false) UUID id
    ) {
        log.debug("REST request to get a page of findByRowNum");
        Integer rowNum = gOtherVoucherService.findRowNumByID(fromDate, toDate, textSearch, id);
        return new ResponseEntity<>(rowNum, HttpStatus.OK);
    }

    //    đếm số lượng chứng từ phân bổ theo tháng và năm
    @GetMapping("/g-other-vouchers/prepaid-expense-allocation-count")
    @Timed
    public ResponseEntity<Long> getPrepaidExpenseAllocationCount(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Long prepaidExpenseCodes = gOtherVoucherService.getPrepaidExpenseAllocationCount(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    @GetMapping("/g-other-vouchers/get-max-month/{id}")
    @Timed
    public ResponseEntity<LocalDate> getMaxMonth(@PathVariable UUID id) {
        log.debug("REST request to get a page of GOtherVouchers");
        LocalDate page = gOtherVoucherService.getMaxMonth(id);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-vouchers");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    // xóa nhiều dòng trong phân bổ chi phí trả trước
    @PostMapping("/g-other-vouchers/multi-delete-g-other-vouchers-allocation")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteGOtherVoucher(@RequestBody List<GOtherVoucher> gOtherVouchers) {
        log.debug("REST request to closeBook : {}", gOtherVouchers);
        HandlingResultDTO responeCloseBookDTO = gOtherVoucherService.multiDeletePB(gOtherVouchers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/g-other-vouchers/multi-delete-g-other-vouchers")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteGOtherVouchers(@Valid @RequestBody List<GOtherVoucher> gOtherVouchers) {
        log.debug("REST request to closeBook : {}", gOtherVouchers);
        HandlingResultDTO responeCloseBookDTO = gOtherVoucherService.multiDelete(gOtherVouchers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/g-other-vouchers/multi-un-record-g-other-vouchers")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecordGOtherVouchers(@Valid @RequestBody List<GOtherVoucher> gOtherVouchers) {
        log.debug("REST request to closeBook : {}", gOtherVouchers);
        HandlingResultDTO responeCloseBookDTO = gOtherVoucherService.multiUnRecord(gOtherVouchers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
