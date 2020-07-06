package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.SearchVoucher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.AccountListRepository;
import vn.softdreams.ebweb.repository.ReportBusinessRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.InfoPackageDTO;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.Report.BangCanDoiTaiKhoanDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.FileSizeLimitExceededException;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import java.io.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UtilsResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);

    @Autowired
    UtilsRepository utilsRepository;

    @Autowired
    GenCodeService genCodeService;

    @Autowired
    UtilsService utilsService;

    @Autowired
    UserService userService;

    @Autowired
    AccountListRepository accountListRepository;

    @Autowired
    ReportBusinessRepository reportBusinessRepository;

    @Autowired
    BackupDataService backupDataService;

    @Autowired
    ViewVoucherNoService viewVoucherNoService;

    /**
     * Hautv
     *
     * @param id
     * @param typeID
     * @param searchVoucher
     * @param isNext
     * @return
     */
    @GetMapping("/voucher/findByRowNum")
    @Timed
    public ResponseEntity<Object> getVoucherByID(@RequestParam(required = true) UUID id,
                                                 @RequestParam(required = true) Number typeID,
                                                 @RequestParam(required = false) String searchVoucher,
                                                 @RequestParam(required = false) Boolean isNext

    ) {
        log.debug("REST request to get next or pre voucher with id = " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        objectMapper.setDateFormat(df);
        if (searchVoucher != null) {
            try {
                searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Object obj = utilsRepository.findOneByRowNum(id, typeID, isNext, searchVoucher1);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    /**
     * Hautv
     *
     * @param id
     * @param searchVoucher
     * @param typeID
     * @return
     */
    @GetMapping("/voucher/getIndexVoucher")
    @Timed
    public ResponseEntity<Object> getIndexVoucherByID(@RequestParam(required = true) UUID id,
                                                      @RequestParam(required = false) String searchVoucher,
                                                      @RequestParam(required = true) Number typeID

    ) {
        log.debug("REST request to get index of voucher with id = " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        objectMapper.setDateFormat(df);
        if (searchVoucher != null) {
            try {
                searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<Integer> obj = utilsRepository.getIndexRow(id, typeID, searchVoucher1);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    /**
     * Hautv
     *
     * @param pageable
     * @param searchVoucher
     * @param typeID
     * @return
     */
    @GetMapping("/voucher/search")
    @Timed
    public ResponseEntity<List<Object>> findAll(Pageable pageable,
                                                @RequestParam(required = true) String searchVoucher,
                                                @RequestParam(required = true) int typeID) {
        log.debug("REST request to get a page of Object");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        objectMapper.setDateFormat(df);
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<Object> page = utilsRepository.findAll(pageable, searchVoucher1, typeID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/voucher/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/voucher/genCodeVoucher")
    @Timed
    public ResponseEntity<String> genCodeVoucher(@RequestParam(required = true) int typeGroupID,
                                                 @RequestParam(required = true) int displayOnBook
    ) {
        GenCode genCode = genCodeService.findWithTypeID(typeGroupID, displayOnBook);
        String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(), '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
        return new ResponseEntity<String>(codeVoucher, HttpStatus.OK);
    }

    @GetMapping(value = "/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = true) String searchVoucher,
                                            @RequestParam(required = true) int typeID) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = utilsRepository.exportPdf(searchVoucher1, typeID);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = true) String searchVoucher,
                                              @RequestParam(required = true) int typeID) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = utilsRepository.exportExcel(searchVoucher1, typeID);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    /**
     * @return
     * @author anmt
     */
    @GetMapping("/inforPackage/warning")
    @Timed
    public ResponseEntity<InfoPackageDTO> getInforPackage() {
        InfoPackageDTO infoPackageDTO = utilsService.getInforPackage();
        return new ResponseEntity<InfoPackageDTO>(infoPackageDTO, HttpStatus.OK);
    }

    /**
     * @return
     * @author hautv
     */
    @GetMapping("/get-days-backup")
    @Timed
    public ResponseEntity<Integer> getDaysBackup() {
        Integer days = backupDataService.getDaysBackup();
        return new ResponseEntity<>(days, HttpStatus.OK);
    }

    @GetMapping("trang-chu/so-du-tai-khoan")
    @Timed
    public ResponseEntity<List<BangCanDoiTaiKhoanDTO>> getSoDuTaiKhoan() {
        UserDTO userDTO = userService.getAccount();
        List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOS = reportBusinessRepository.getBangCanDoiTaiKhoan(
            LocalDate.now().atTime(LocalTime.MAX).toLocalDate(),
            LocalDate.now().atTime(LocalTime.MAX).toLocalDate(),
            true,
            userDTO.getOrganizationUnit().getId(),
            accountListRepository.getGradeMaxAccount(userDTO.getOrganizationUnit().getId()),
            Utils.PhienSoLamViec(userDTO), false
        );
        if (bangCanDoiTaiKhoanDTOS.size() == 0) {
            List<AccountList> lsAccountLists = accountListRepository.findAllAccountLists(userDTO.getOrganizationUnit().getId());
            for (AccountList accountList : lsAccountLists) {
                BangCanDoiTaiKhoanDTO bangCanDoiTaiKhoanDTO = new BangCanDoiTaiKhoanDTO();
                bangCanDoiTaiKhoanDTO.setId(accountList.getId());
                bangCanDoiTaiKhoanDTO.setAccountID(accountList.getId());
                bangCanDoiTaiKhoanDTO.setParentAccountID(accountList.getParentAccountID());
                bangCanDoiTaiKhoanDTO.setIsParentNode(accountList.getParentNode());
                bangCanDoiTaiKhoanDTO.setAccountName(accountList.getAccountName());
                bangCanDoiTaiKhoanDTO.setAccountCategoryKind(accountList.getAccountGroupKind());
                bangCanDoiTaiKhoanDTO.setAccountNumber(accountList.getAccountNumber());
                bangCanDoiTaiKhoanDTO.setGrade(accountList.getGrade());
                bangCanDoiTaiKhoanDTOS.add(bangCanDoiTaiKhoanDTO);
            }
        }
        for (BangCanDoiTaiKhoanDTO bangCanDoiTaiKhoanDTO : bangCanDoiTaiKhoanDTOS) {
            if (bangCanDoiTaiKhoanDTO.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_CO) {
                bangCanDoiTaiKhoanDTO.setClosingCreditAmountString(Utils.formatTien(bangCanDoiTaiKhoanDTO.getClosingCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else if (bangCanDoiTaiKhoanDTO.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_NO) {
                bangCanDoiTaiKhoanDTO.setClosingDebitAmountString(Utils.formatTien(bangCanDoiTaiKhoanDTO.getClosingDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                bangCanDoiTaiKhoanDTO.setClosingCreditAmountString(Utils.formatTien(bangCanDoiTaiKhoanDTO.getClosingCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                bangCanDoiTaiKhoanDTO.setClosingDebitAmountString(Utils.formatTien(bangCanDoiTaiKhoanDTO.getClosingDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
        }
        return new ResponseEntity<>(bangCanDoiTaiKhoanDTOS, HttpStatus.OK);
    }

    @GetMapping("trang-chu/suc-khoe-tai-chinh-doanh-nghiep")
    @Timed
    public ResponseEntity<List<SucKhoeTaiChinhDoanhNghiepDTO>> getSucKhoeTaiChinhDoanhNghiep(@RequestParam LocalDate fromDate,
                                                                                             @RequestParam LocalDate toDate) {
        UserDTO userDTO = userService.getAccount();
        List<SucKhoeTaiChinhDoanhNghiepDTO> sucKhoeTaiChinhDoanhNghiep = reportBusinessRepository.getSucKhoeTaiChinhDoanhNghiep(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        return new ResponseEntity<>(sucKhoeTaiChinhDoanhNghiep, HttpStatus.OK);
    }

    @GetMapping("trang-chu/bieu-do-tong-hop")
    @Timed
    public ResponseEntity<List<BieuDoTongHopDTO>> getBieuDoTongHop(@RequestParam LocalDate fromDate,
                                                                   @RequestParam LocalDate toDate) {
        UserDTO userDTO = userService.getAccount();
        List<BieuDoTongHopDTO> bieuDoTongHopDTOList = reportBusinessRepository.getBieuDoTongHop(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        return new ResponseEntity<>(bieuDoTongHopDTOList, HttpStatus.OK);
    }

    @GetMapping("trang-chu/bieu-do-doanh-thu-chi-phi")
    @Timed
    public ResponseEntity<List<BieuDoDoanhThuChiPhiDTO>> getBieuDoanhThuChiPhi(@RequestParam LocalDate fromDate,
                                                                               @RequestParam LocalDate toDate) {
        UserDTO userDTO = userService.getAccount();
        List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOS = reportBusinessRepository.getBieuDoDoanhThuChiPhi(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        return new ResponseEntity<>(bieuDoDoanhThuChiPhiDTOS, HttpStatus.OK);
    }

    @GetMapping("trang-chu/bieu-do-no-phai-thu-tra")
    @Timed
    public ResponseEntity<List<BieuDoDoanhThuChiPhiDTO>> getBieuDoNoPhaiThuTra(@RequestParam LocalDate fromDate,
                                                                               @RequestParam LocalDate toDate) {
        UserDTO userDTO = userService.getAccount();
        List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOS = reportBusinessRepository.getBieuDoNoPhaiThuTra(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        return new ResponseEntity<>(bieuDoDoanhThuChiPhiDTOS, HttpStatus.OK);
    }

    @GetMapping("trang-chu/load-data")
    @Timed
    public ResponseEntity<HomeDTO> loadTrangChu(@RequestParam LocalDate fromDate,
                                                @RequestParam LocalDate toDate) {
        HomeDTO homeDTO = new HomeDTO();
        UserDTO userDTO = userService.getAccount();
        List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOS = reportBusinessRepository.getBangCanDoiTaiKhoan(
            LocalDate.now().atTime(LocalTime.MAX).toLocalDate(),
            LocalDate.now().atTime(LocalTime.MAX).toLocalDate(),
            true,
            userDTO.getOrganizationUnit().getId(),
            accountListRepository.getGradeMaxAccount(userDTO.getOrganizationUnit().getId()),
            Utils.PhienSoLamViec(userDTO), false
        );
        List<SucKhoeTaiChinhDoanhNghiepDTO> sucKhoeTaiChinhDoanhNghiep = reportBusinessRepository.getSucKhoeTaiChinhDoanhNghiep(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        List<BieuDoTongHopDTO> bieuDoTongHopDTOList = reportBusinessRepository.getBieuDoTongHop(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOS = reportBusinessRepository.getBieuDoDoanhThuChiPhi(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        List<BieuDoDoanhThuChiPhiDTO> bieuDoNoPhaiThuTraList = reportBusinessRepository.getBieuDoNoPhaiThuTra(
            fromDate.atTime(LocalTime.MIN).toLocalDate(),
            toDate.atTime(LocalTime.MAX).toLocalDate(),
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO)
        );
        if (bangCanDoiTaiKhoanDTOS.size() == 0) {
            List<AccountList> lsAccountLists = accountListRepository.findAllAccountLists(userDTO.getOrganizationUnit().getId());
            for (AccountList accountList : lsAccountLists) {
                BangCanDoiTaiKhoanDTO bangCanDoiTaiKhoanDTO = new BangCanDoiTaiKhoanDTO();
                bangCanDoiTaiKhoanDTO.setId(accountList.getId());
                bangCanDoiTaiKhoanDTO.setAccountID(accountList.getId());
                bangCanDoiTaiKhoanDTO.setParentAccountID(accountList.getParentAccountID());
                bangCanDoiTaiKhoanDTO.setIsParentNode(accountList.getParentNode());
                bangCanDoiTaiKhoanDTO.setAccountName(accountList.getAccountName());
                bangCanDoiTaiKhoanDTO.setAccountCategoryKind(accountList.getAccountGroupKind());
                bangCanDoiTaiKhoanDTO.setAccountNumber(accountList.getAccountNumber());
                bangCanDoiTaiKhoanDTO.setGrade(accountList.getGrade());
                bangCanDoiTaiKhoanDTOS.add(bangCanDoiTaiKhoanDTO);
            }
            homeDTO.setBangCanDoiTaiKhoanDTOList(bangCanDoiTaiKhoanDTOS);
        } else {
            homeDTO.setBangCanDoiTaiKhoanDTOList(bangCanDoiTaiKhoanDTOS);
        }
        homeDTO.setSucKhoeTaiChinhDoanhNghiepDTOS(sucKhoeTaiChinhDoanhNghiep);
        homeDTO.setBieuDoTongHopDTOList(bieuDoTongHopDTOList);
        homeDTO.setBieuDoDoanhThuChiPhiDTOList(bieuDoDoanhThuChiPhiDTOS);
        homeDTO.setBieuDoNoPhaiThuTraList(bieuDoNoPhaiThuTraList);
        return new ResponseEntity<>(homeDTO, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @param id
     * @param nameColumn (nếu nhiều cột thì cách nhau bởi dấu ;)
     * @return cacs chứng từ theo ràng buộc danh mục
     * @Author Hautv
     */
    @GetMapping("catalog/get-voucher-ref")
    @Timed
    public ResponseEntity<List<VoucherRefCatalogDTO>> getVoucherRefCatalogDTOByID(Pageable pageable,
                                                                                  @RequestParam UUID id,
                                                                                  @RequestParam String nameColumn) {
        UserDTO userDTO = userService.getAccount();
        Page<VoucherRefCatalogDTO> page;
        if (nameColumn.replace(" ", "").contains(";")) {
            page = utilsRepository.getVoucherRefCatalogDTOByID(
                pageable,
                userDTO.getOrganizationUnit().getId(),
                Utils.PhienSoLamViec(userDTO),
                id,
                nameColumn
            );
        } else {
            page = utilsRepository.getVoucherRefCatalogDTOByID(
                pageable,
                userDTO.getOrganizationUnit().getId(),
                Utils.PhienSoLamViec(userDTO),
                id,
                Arrays.stream(nameColumn.replace(" ", "").split(";")).collect(Collectors.toList())
            );
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/catalog/get-voucher-ref");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /***
     * @author phuonghv
     */
    @PostMapping(value = "category/get-fields-excel", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UpdateDataDTO> getFieldsExcel(@RequestParam(required = false) MultipartFile file) {
        UpdateDataDTO updateDataDTO = utilsService.getFieldsExcel(file);
        return new ResponseEntity<>(updateDataDTO, HttpStatus.OK);
    }

    @PostMapping(value = "category/accept-data/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boolean> saveOPAccount(@RequestBody List<MaterialGoods> materialGoodsData, @PathVariable("type") Integer type) throws IOException {
        Boolean result =  utilsService.acceptedMaterialGoodsData(materialGoodsData, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "category/check-reference")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UpdateDataDTO> checkReference(@RequestParam(required = false) String type) {
        UpdateDataDTO updateDataDTO = utilsService.checkReference(type);
        return new ResponseEntity<>(updateDataDTO, HttpStatus.OK);
    }

    @PostMapping(value = "category/valid-data", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadNormalAccounting(@RequestParam(required = false) MultipartFile file,
                                                         @RequestParam String updateDataJson)
            throws IOException, FileSizeLimitExceededException {
        ObjectMapper mapper = new ObjectMapper();
        UpdateDataDTO requestData = mapper.readValue(updateDataJson, UpdateDataDTO.class);
        UpdateDataDTO updateDataDTO = utilsService.validDataFromExcel(file, requestData);

        HttpHeaders headers = new HttpHeaders();

        headers.add("isError", updateDataDTO.getError() ? "1" : "0");
        headers.add("message", updateDataDTO.getMessage());
        System.out.println(LocalDateTime.now());
        if (updateDataDTO.getMessage().equals(Constants.UpdateDataDTOMessages.SUCCESS)) {
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
        System.out.println(LocalDateTime.now());
        if (updateDataDTO.getMessage().equals(Constants.UpdateDataDTOMessages.REQUIRED_ITEM)) {
            headers.add("isError", "0");
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getErrors()), headers, HttpStatus.OK);
        }
        if (Arrays.asList("METHOD_ABATEMENT", "METHOD_DIRECT").contains(updateDataDTO.getMessage())) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        if (updateDataDTO.getMessage().equals(Constants.UpdateDataDTOMessages.FILE_TOO_LARGE)) {
            headers.add("isError", "0");
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getErrors()), headers, HttpStatus.OK);
        }
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");

        return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "category/temp/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile(@RequestParam String type) throws IOException {
        byte[] fileContent = utilsService.getFileTemp(type);
        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }
    @GetMapping("/get-dkkd")
    @Timed
    public ResultDTO getDataFromDkkD(@RequestParam String taxCode) {
        try {
            ResultDTO resultDTO = new ResultDTO();
            // First set the default cookie manager.
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            String urlprk = "https://dichvuthongtin.dkkd.gov.vn/inf/default.aspx";
            String paramKey = "ctl00_hdParameter";

            Document pageDoc = Jsoup.connect(urlprk).get();
            Element code = pageDoc.getElementById(paramKey);
            String DKKD_PARAM_KEY = "";
            if (code != null) {
                DKKD_PARAM_KEY = code.val();
            }

            java.net.URL url = new URL("https://dichvuthongtin.dkkd.gov.vn/inf/Public/Srv.aspx/GetSearch");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //headers
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write("{\"searchField\": \"" + taxCode + "\",\"h\": \"" + DKKD_PARAM_KEY + "\"}");
            osw.flush();
            osw.close();
            os.close();
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder(2048);
            for (String line; (line = br.readLine()) != null; ) {
                sb.append(line);
            }
            CookieHandler.setDefault(null);
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray objectsAray = jsonObject.getJSONArray("d");
            AccountingObjectDTO dto = new AccountingObjectDTO();
            if (objectsAray != null) {
                for (int j = 0; j < objectsAray.length(); j++) {
                    JSONObject jsonEnterprise = objectsAray.getJSONObject(j);
                    if (jsonEnterprise.get("Enterprise_Gdt_Code").equals(taxCode)) {
                        dto.setAccountingObjectName((String) jsonEnterprise.get("Name"));
                        dto.setAccountingObjectAddress((String) jsonEnterprise.get("Ho_Address"));
                        /*dto.setRepresentative((String) jsonEnterprise.get("Legal_First_Name"));
                        Long provinceId = Long.valueOf((String) jsonEnterprise.get("City_Id"));
                        Long districtId = Long.valueOf((String) jsonEnterprise.get("District_Id"));
                        Long communeId = Long.valueOf((String) jsonEnterprise.get("Ward_Id"));
                        dto.setProvinceId(provinceId);
                        dto.setDistrictId(districtId);
                        dto.setCommuneId(communeId);

                        String[] separateAddress = dto.getAddress().split(",");
                        StringBuilder address = new StringBuilder();
                        for (int i = 0; i < separateAddress.length - 4; i++) {
                            address.append(separateAddress[i]);
                        }
                        dto.setAddress(address.toString());*/
                        resultDTO.setMessage("success");
                        resultDTO.setResult(dto);
                        return resultDTO;
                    }
                }

            }
            return new ResultDTO("error01"); // mst không hợp lệ
        } catch (Exception ex) {
            String tesst = ex.getMessage();
        }
        return null;
    }

    @GetMapping(value = "utilities/reset-no/get-vouchers")
    @Timed
    public ResponseEntity<List<ViewVoucherDTO>> getVoucherByTypeGroup(Pageable pageable,
                                                               @RequestParam(required = true) Integer typeGroupID,
                                                               @RequestParam(required = false) String fromDate,
                                                               @RequestParam(required = false) String toDate
    ) {
        Page<ViewVoucherDTO> page = viewVoucherNoService.findAllByTypeGroupID(pageable, typeGroupID, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/utilities/reset-no/get-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "utilities/search-voucher")
    @Timed
    public ResponseEntity<List<ViewVoucherDTO>> searchVoucher(Pageable pageable,
                                                             @RequestParam(required = false) Integer typeSearch,
                                                             @RequestParam(required = false) Integer typeGroupID,
                                                             @RequestParam(required = false) String no,
                                                             @RequestParam(required = false) String invoiceDate,
                                                             @RequestParam(required = false) Boolean recorded,
                                                             @RequestParam(required = false) String fromDate,
                                                             @RequestParam(required = false) String toDate
    ) {
        Page<ViewVoucherDTO> page = viewVoucherNoService.searchVoucher(pageable, typeSearch, typeGroupID, no, invoiceDate, recorded, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/utilities/search-voucher");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return the ResponseEntity with status 201 (Created) and with body the new genCode, or with status 400 (Bad Request) if the genCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @Author Hautv
     */
    @PostMapping("/utilities/reset-no")
    @Timed
    public ResponseEntity<Respone_SDS> resetNo(@RequestBody RequestResetNoDTO requestResetNoDTO) {
        log.debug("REST request to Reset NoVoucher : {}", requestResetNoDTO);
        Respone_SDS respone_sds = viewVoucherNoService.resetNo(requestResetNoDTO);
        return new ResponseEntity<>(respone_sds, HttpStatus.OK);
    }

}

