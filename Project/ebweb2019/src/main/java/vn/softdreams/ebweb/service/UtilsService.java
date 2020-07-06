package vn.softdreams.ebweb.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.ExcelSystemFieldDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static vn.softdreams.ebweb.service.util.Constants.EbPackage.UNLIMITED_PACKAGE;

@Service
@Transactional
public class UtilsService {
    private final GenCodeService genCodeService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final UtilsRepository utilsRepository;

    private final EbPackageRepository ebPackageRepository;

    private final EbUserPackageRepository ebUserPackageRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final PporderdetailRepository pporderdetailRepository;

    private final AccountingObjectGroupService accountingObjectGroupService;

    private final AccountingObjectRepository accountingObjectRepository;
    private final SystemOptionRepository systemOptionRepository;

    private final PaymentClauseRepository paymentClauseRepository;
    private final AccountingObjectGroupRepository accountingObjectGroupRepository;
    private final BankRepository bankRepository;
    private final OrganizationUnitService organizationUnitService;

    private final MaterialGoodsRepository materialGoodsRepository;
    private final UnitRepository unitRepository;
    private final MaterialGoodsCategoryRepository materialGoodsCategoryRepository;
    private final RepositoryRepository repositoryRepository;
    private final AccountListRepository accountListRepository;
    private final MaterialGoodsSpecialTaxGroupRepository materialGoodsSpecialTaxGroupRepository;
    private final CareerGroupRepository careerGroupRepository;
    private final CurrencyRepository currencyRepository;
    private static final int MAXIMUM_SIZE_UPLOADED = 10485760;
    private static final int MAX_LENGTH_CONTACT = 25;
    private static final int MAX_CODE = 50;
    private static final int MAX_EMAIL_WEBSITE = 100;

    public UtilsService(GenCodeService genCodeService, UserService userService, UtilsRepository utilsRepository,
                        PporderdetailRepository pporderdetailRepository, EbPackageRepository ebPackageRepository,
                        MaterialGoodsRepository materialGoodsRepository, OrganizationUnitRepository organizationUnitRepository,
                        AccountingObjectGroupService accountingObjectGroupService,
                        AccountingObjectRepository accountingObjectRepository,
                        SystemOptionRepository systemOptionRepository,
                        PaymentClauseRepository paymentClauseRepository,
                        AccountingObjectGroupRepository accountingObjectGroupRepository,
                        BankRepository bankRepository,
                        EbUserPackageRepository ebUserPackageRepository,
                        OrganizationUnitService organizationUnitService,
                        UnitRepository unitRepository,
                        MaterialGoodsCategoryRepository materialGoodsCategoryRepository,
                        RepositoryRepository repositoryRepository,
                        AccountListRepository accountListRepository,
                        MaterialGoodsSpecialTaxGroupRepository materialGoodsSpecialTaxGroupRepository,
                        CareerGroupRepository careerGroupRepository,
                        CurrencyRepository currencyRepository, UserRepository userRepository) {
        this.genCodeService = genCodeService;
        this.userService = userService;
        this.utilsRepository = utilsRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.ebPackageRepository = ebPackageRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.accountingObjectGroupService = accountingObjectGroupService;
        this.accountingObjectRepository = accountingObjectRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.paymentClauseRepository = paymentClauseRepository;
        this.accountingObjectGroupRepository = accountingObjectGroupRepository;
        this.bankRepository = bankRepository;
        this.organizationUnitService = organizationUnitService;
        this.ebUserPackageRepository = ebUserPackageRepository;
        this.unitRepository = unitRepository;
        this.materialGoodsCategoryRepository = materialGoodsCategoryRepository;
        this.repositoryRepository = repositoryRepository;
        this.accountListRepository = accountListRepository;
        this.materialGoodsSpecialTaxGroupRepository = materialGoodsSpecialTaxGroupRepository;
        this.careerGroupRepository = careerGroupRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    /**
     * @param oldNoFBook    NoFBook cũ
     * @param oldNoMBook    NoMBook cũ
     * @param newNoBook     NoBook mới truyền lên từ client
     * @param oldTypeLedger TypeLedger cũ
     * @param newTypeLedger TypeLedger truyền lên từ client
     * @param receiptId     ID chứng từ nếu là là update, để null nếu là thêm mới
     * @param receiptType   Type của chứng từ để Gencode
     * @return NoFBook và NoMBook mới của chứng từ
     * utilsRepository.updateGencode Nếu đã thêm trigger thì cũng không ảnh hưởng
     * @example {@link vn.softdreams.ebweb.service.impl.PPServiceServiceImpl} region='savePPService'
     */
    public UpdateDataDTO updateReceipt(String oldNoFBook, String oldNoMBook,
                                       String newNoBook,
                                       Integer oldTypeLedger, Integer newTypeLedger,
                                       UUID receiptId, Integer receiptType) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        updateDataDTO.setNoFBook(oldNoFBook);
        updateDataDTO.setNoMBook(oldNoMBook);
        Integer currentBook = Integer.valueOf(userService.getAccount().getSystemOption()
            .stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData());
        if (oldTypeLedger != null && !oldTypeLedger.equals(newTypeLedger) && oldTypeLedger.equals(Constants.TypeLedger.BOTH_BOOK)) {
            if (newTypeLedger.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                updateDataDTO.setNoMBook(null);
            } else {
                updateDataDTO.setNoFBook(null);
            }
        }

        updateDataDTO.setUpdateReceiptNo(false);
        if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            updateDataDTO.setUpdateReceiptNo(receiptId != null && oldNoFBook != null && !oldNoFBook.equals(newNoBook));
            updateDataDTO.setNoFBook(newNoBook);
        } else {
            updateDataDTO.setUpdateReceiptNo(receiptId != null && oldNoMBook != null && !oldNoMBook.equals(newNoBook));
            updateDataDTO.setNoMBook(newNoBook);
        }

        if (newTypeLedger.equals(Constants.TypeLedger.BOTH_BOOK) && (receiptId == null || !newTypeLedger.equals(oldTypeLedger))) {
            GenCode genCode = genCodeService.findWithTypeID(receiptType,
                currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK) ? Constants.TypeLedger.MANAGEMENT_BOOK : Constants.TypeLedger.FINANCIAL_BOOK);
            String codeVoucher = Utils.codeVoucher(genCode);
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                updateDataDTO.setNoMBook(codeVoucher);
            } else {
                updateDataDTO.setNoFBook(codeVoucher);
            }
            updateDataDTO.setUpdateReceiptNo(true);
        }
        if (!utilsRepository.checkDuplicateNoVoucher(updateDataDTO.getNoFBook(),
            updateDataDTO.getNoMBook(),
            newTypeLedger, receiptId)) {
            updateDataDTO.setMessage(Constants.UpdateDataDTOMessages.updateReceipt + receiptType);
        }
        return updateDataDTO;
    }

    /***
     * @author phuonghv
     * @param ppOrderDTOsOld
     * @param ppOrderDTOsNew
     * @example {@link vn.softdreams.ebweb.service.impl.PPServiceServiceImpl} region='savePPService'
     * @return
     */
    public List<PPOrderDetail> calculatingQuantityReceiptPPOrder(List<PPOrderDTO> ppOrderDTOsOld, List<PPOrderDTO> ppOrderDTOsNew, Boolean checkQuantity) {
        ppOrderDTOsNew.forEach(ppOrderDTO -> {
            boolean isNew = true;
            for (PPOrderDTO ppOrderDTOOld : ppOrderDTOsOld) {
                if (ppOrderDTO.getId().equals(ppOrderDTOOld.getId())) {
                    ppOrderDTOOld.setQuantityReceipt(ppOrderDTO.getQuantityReceipt().add(ppOrderDTOOld.getQuantityReceipt()));
                    isNew = false;
                }
            }
            if (isNew) {
                ppOrderDTOsOld.add(ppOrderDTO);
            }

        });
        return ppOrderDTOsOld.stream().map(ppOrderDTO -> {
            Optional<PPOrderDetail> ppOrderDetail = pporderdetailRepository.findById(ppOrderDTO.getId());
            if (ppOrderDetail.isPresent()) {
                PPOrderDetail detail = ppOrderDetail.get();
                detail.setQuantityReceipt(detail.getQuantityReceipt() == null ? ppOrderDTO.getQuantityReceipt() :
                    detail.getQuantityReceipt().add(ppOrderDTO.getQuantityReceipt()));
                if (checkQuantity != null && checkQuantity && detail.getQuantityReceipt().subtract(detail.getQuantity()).intValue() > 0) {
                    throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.QUANTITY_RECEIPT_GREATER_THAN_MESS,
                        "updateReceipt", Constants.UpdateDataDTOMessages.QUANTITY_RECEIPT_GREATER_THAN);
                }
                return detail;
            }
            return new PPOrderDetail();
        }).filter(x -> x.getId() != null).collect(Collectors.toList());
    }

    /**
     * @param uuid
     * @param nameColumn
     * @return Kiểm tra ràng buộc danh mục
     * true: đã được sử dụng
     * false: chưa được sử dụng
     * @Author Hautv
     */
    public Boolean checkCatalogInUsed(UUID companyID, Object uuid, String nameColumn) {
        return utilsRepository.checkCatalogInUsed(companyID, uuid, nameColumn);
    }

    public Boolean checkContraint(String nameTable, String nameColumn, UUID uuid) {
        return utilsRepository.checkContraint(nameTable, nameColumn, uuid);
    }

    /**
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumn
     * @return tất cả chứng từ phát sinh của danh mục
     * @Author Hautv
     */
    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, String nameColumn) {
        return utilsRepository.getVoucherRefCatalogDTOByID(companyID, currentBook, uuid, nameColumn);
    }

    /**
     * @param pageable
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumn
     * @return chứng từ phát sinh của danh mục phân trang
     * @Author Hautv
     */
    public Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, String nameColumn) {
        return utilsRepository.getVoucherRefCatalogDTOByID(pageable, companyID, currentBook, uuid, nameColumn);
    }

    /**
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumns
     * @return tất cả chứng từ phát sinh của danh mục
     * @Author Hautv
     */
    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns) {
        return utilsRepository.getVoucherRefCatalogDTOByID(companyID, currentBook, uuid, nameColumns);
    }

    /**
     * @param pageable
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumns
     * @return chứng từ phát sinh của danh mục phân trang
     * @Author Hautv
     */
    public Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns) {
        return utilsRepository.getVoucherRefCatalogDTOByID(pageable, companyID, currentBook, uuid, nameColumns);
    }

    /**
     * @return
     * @author anmt
     */
    public Boolean checkQuantityLimitedNoVoucher() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> userLogined = userRepository.findOneByLogin(currentUserLoginAndOrg.get().getLogin());
        Long userId = userLogined.get().getParentID() != null ? userLogined.get().getParentID() : userLogined.get().getId();
        OrganizationUnit organizationUnitLogin = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
        UUID orgId = null;
        if (organizationUnitLogin.getUserID() != null && organizationUnitLogin.getUserID().equals(userId)) {
            // neu cty loai ke toan dich vu
            orgId = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
        } else if (organizationUnitLogin.getUserID() == null) {
            // neu la chi nhanh cua cty dc tao ra tư cty KTDV
            UUID companyIdOfPackage = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
            OrganizationUnit orgPa = organizationUnitRepository.findByID(organizationUnitLogin.getParentID());
            if (orgPa != null && orgPa.getParentID() != null && orgPa.getParentID().equals(companyIdOfPackage)) {
                orgId = companyIdOfPackage;
            } else {
                orgId = organizationUnitLogin.getParentID() != null ? organizationUnitLogin.getParentID() : organizationUnitLogin.getId();
            }
        }
        EbPackage ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(userId, orgId);
        Long countNoPresent = utilsRepository.checkQuantityLimitedNoVoucher(orgId);
        if (ebPackage.getLimitedVoucher() == UNLIMITED_PACKAGE) {
            return true;
        } else {
            return ebPackage.getLimitedVoucher() > countNoPresent;
        }
    }

    public InfoPackageDTO getInforPackage() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> userLogined = userRepository.findOneByLogin(currentUserLoginAndOrg.get().getLogin());
        Long userId = userLogined.get().getParentID() != null ? userLogined.get().getParentID() : userLogined.get().getId();
        OrganizationUnit organizationUnitLogin = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
        UUID orgId = null;
        if (organizationUnitLogin.getUserID() != null && organizationUnitLogin.getUserID().equals(userId)) {
            // neu cty loai ke toan dich vu
            orgId = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
        } else if (organizationUnitLogin.getUserID() == null) {
            // neu la chi nhanh cua cty dc tao ra tư cty KTDV
            UUID companyIdOfPackage = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
            OrganizationUnit orgPa = organizationUnitRepository.findByID(organizationUnitLogin.getParentID());
            if (orgPa != null && orgPa.getParentID() != null && orgPa.getParentID().equals(companyIdOfPackage)) {
                orgId = companyIdOfPackage;
            } else {
                orgId = organizationUnitLogin.getParentID() != null ? organizationUnitLogin.getParentID() : organizationUnitLogin.getId();
            }
        }
        EbPackage ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(userId, orgId);
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserIDAndCompanyID(userId, orgId);
        Long countNoPresent = utilsRepository.checkQuantityLimitedNoVoucher(orgId);
        InfoPackageDTO infoPackageDTO = new InfoPackageDTO();
        if (ebPackage != null) {
            if (ebPackage.getLimitedVoucher() != UNLIMITED_PACKAGE && ebPackage.getLimitedVoucher() > 0) {
                if (this.checkNoQuantityPercent(countNoPresent, ebPackage.getLimitedVoucher())) {
                    infoPackageDTO.setWarningLimitedQuantity(Constants.WarningPackage.limitedNo);
                } else {
                    infoPackageDTO.setWarningLimitedQuantity(Constants.WarningPackage.notLimitedNo);
                }
            } else {
                infoPackageDTO.setWarningLimitedQuantity(Constants.WarningPackage.notLimitedNo);
            }
            if (ebPackage.getExpiredTime() != UNLIMITED_PACKAGE && ebPackage.getExpiredTime() > 0) {
                if (this.checkWarningExpiredTime(ebUserPackage.getExpriredDate())) {
                    infoPackageDTO.setWarningExpiredTime(Constants.WarningPackage.expiredTime);
                } else {
                    infoPackageDTO.setWarningExpiredTime(Constants.WarningPackage.notExpiredTime);
                }
            } else {
                infoPackageDTO.setWarningExpiredTime(Constants.WarningPackage.notExpiredTime);
            }
        } else {
            throw new BadRequestAlertException("Không có gói nào được tạo", "", "noPackage");
        }
        return infoPackageDTO;
    }

    private Boolean checkNoQuantityPercent(Long noPresent, Integer noTotal) {
        return (noPresent / noTotal) * 100 > 95;
    }

    private Boolean checkWarningExpiredTime(LocalDate expriredDate) {
        long daysBetween = DAYS.between(java.time.LocalDate.now(), expriredDate);
        return daysBetween <= 15;
    }

    public CheckQuantityExistsDTO checkQuantityExistsTest(List<CheckQuantityExistsConvertDTO> checkQuantityExistsConvertDTOS, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS) {
        HashMap mgForPPOderMap = new HashMap();
        HashMap mgForPPOderMap2 = new HashMap();
        HashMap mgForPPOderMinimumStockMap = new HashMap();
        HashMap mgForPPOderInStockMap = new HashMap();
        String textCode = "";
        String textCode2 = "";
        MathContext mc = new MathContext(2);
        for (CheckQuantityExistsConvertDTO item : checkQuantityExistsConvertDTOS) {
            MGForPPOrderConvertDTO mgs = new MGForPPOrderConvertDTO();
            if (item.getMaterialGoodsID() != null) {
                UUID id = item.getMaterialGoodsID();
                mgs = mgForPPOrderConvertDTOS.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
                if (mgs != null && mgs.getMaterialGoodsType() != 2 && mgs.getMaterialGoodsType() != 4) {
                    if (mgForPPOderMap.size() == 0) {
                        mgForPPOderMap.put(mgs.getMaterialGoodsCode(), item.getQuantity());
                    }
                    if (mgForPPOderMap.get(mgs.getMaterialGoodsCode()) != null) {
                        mgForPPOderMap.put(mgs.getMaterialGoodsCode(), item.getQuantity());
                    }
                    BigDecimal count = mgForPPOderMap.get(mgs.getMaterialGoodsCode()) != null ? (BigDecimal) mgForPPOderMap.get(mgs.getMaterialGoodsCode()) : BigDecimal.ZERO;
                    if (mgs.getMaterialGoodsInStock() != null
                        && (mgs.getMaterialGoodsInStock().subtract(count, mc).compareTo(BigDecimal.ZERO) < 0)) {
                        mgForPPOderInStockMap.put(mgs.getMaterialGoodsCode(), mgs.getMaterialGoodsCode());
                    } else {
                        mgForPPOderMap.put(mgs.getMaterialGoodsCode(), count.add(item.getQuantity()));
                    }
                    if (mgs.getMinimumStock() != null && !mgs.getMinimumStock().equals(BigDecimal.ZERO)) {
                        if (mgForPPOderMap2.size() == 0) {
                            mgForPPOderMap2.put(mgs.getMaterialGoodsCode(), item.getQuantity());
                        }
                        if (mgForPPOderMap2.get(mgs.getMaterialGoodsCode()) != null) {
                            mgForPPOderMap2.put(mgs.getMaterialGoodsCode(), item.getQuantity());
                        }
                        if (mgs.getMaterialGoodsInStock().subtract((BigDecimal) mgForPPOderMap2.get(mgs.getMaterialGoodsCode()), mc).compareTo(mgs.getMinimumStock()) < 0) {
                            mgForPPOderMinimumStockMap.put(mgs.getMaterialGoodsCode(), mgs.getMaterialGoodsCode());
                        } else {
                            mgForPPOderMap2.put(mgs.getMaterialGoodsCode(), ((BigDecimal) mgForPPOderMap2.get(mgs.getMaterialGoodsCode())).add(item.getQuantity()));
                        }
                    }
                }
            }
        }

        for (Object i : mgForPPOderMinimumStockMap.values()) {
            textCode2 += i.toString() + ", ";
        }
        for (Object i : mgForPPOderInStockMap.values()) {
            textCode += i.toString() + ", ";
        }
        return
            new CheckQuantityExistsDTO(textCode.length() > 2 ? textCode.substring(0, textCode.length() - 2) : null, textCode2.length() > 2 ? textCode2.substring(0, textCode2.length() - 2) : null);

    }

    private CheckQuantityExistsDTO convertPPDiscountReturn(PPDiscountReturn details) {
        return null;
           }

    public CheckQuantityExistsDTO checkQuantityBalance(List<CheckQuantityExistsConvertDTO> checkQuantityExistsConvertDTOS, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS) {
        String textCode = "";
        for (CheckQuantityExistsConvertDTO item : checkQuantityExistsConvertDTOS) {
            MGForPPOrderConvertDTO mgs = new MGForPPOrderConvertDTO();
            if (item.getMaterialGoodsID() != null) {
                UUID id = item.getMaterialGoodsID();
                mgs = mgForPPOrderConvertDTOS.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
                if (mgs != null && mgs.getMaterialGoodsType() != 2 && mgs.getMaterialGoodsType() != 4) {
                    BigDecimal quantityBalance = mgs.getMaterialGoodsInStock().subtract(item.getQuantity());
                    if (quantityBalance.compareTo(BigDecimal.ZERO) >= 0) {
                        mgs.setMaterialGoodsInStock(quantityBalance);
                    } else {
                        textCode += mgs.getMaterialGoodsCode() + ", ";
                    }
                }
            }
        }
        return new CheckQuantityExistsDTO(textCode.length() > 2 ? textCode.substring(0, textCode.length() - 2) : null, null);
    }

    public UpdateDataDTO getFieldsExcel(MultipartFile file) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        if (!currentUserLoginAndOrg.isPresent() || !user.isPresent()) {
            throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT,
                    this.getClass().getName(),
                    Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Workbook workbook ;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception ignore) {
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setMessage("invalidFile");
            return dto;
        }
        if (workbook.getNumberOfSheets() < 1) {
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setMessage("sheetEmpty");
            return dto;
        }

        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        Map<String, List<String>> excelFields = new HashMap<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            List<String> fields = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(i);
            // Lấy ra row đầu tiên đọc header
            try {
                Row row1 = sheet.getRow(0);
                Iterator<Cell> cellIterator = row1.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    fields.add(ExcelUtils.getCellValueString(cell));
                }
            } catch (Exception e) {

            }
            excelFields.put(workbook.getSheetAt(i).getSheetName(), fields);
        }
        updateDataDTO.setMessage(Constants.UpdateDataDTOMessages.SUCCESS);
        updateDataDTO.setExcelFields(excelFields);
        return updateDataDTO;
    }

    public UpdateDataDTO validDataFromExcel(MultipartFile file, UpdateDataDTO requestData) throws IOException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        UpdateDataDTO dto = new UpdateDataDTO();
        if (!currentUserLoginAndOrg.isPresent() || !user.isPresent()) {
            throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT,
                    this.getClass().getName(),
                    Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Workbook workbook ;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception ignore) {
            dto.setError(true);
            dto.setMessage("invalidFile");
            return dto;
        }
        if (workbook.getNumberOfSheets() < 1) {
            dto.setError(true);
            dto.setMessage("sheetEmpty");
            return dto;
        }
        Sheet sheet = workbook.getSheet(requestData.getSheetName());
        if (sheet == null) {
            dto.setError(true);
            dto.setMessage("canNotFindSheetName");
            return dto;
        }
        if (sheet.getLastRowNum() < 1) {
            dto.setError(true);
            dto.setMessage("rowEmpty");
            return dto;
        }

        Row row1 = sheet.getRow(0);
        Iterator<Cell> cellIterator = row1.cellIterator();
        Map<String, Integer> headers = new HashMap<>();
        Map<String, String> mapCodeExcelField = requestData.getExcelSystemFieldDTOS()
                .stream().filter(x -> !Strings.isNullOrEmpty(x.getExcelField()))
                .collect(Collectors.toMap(ExcelSystemFieldDTO::getExcelField, ExcelSystemFieldDTO::getCode));
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String key = ExcelUtils.getCellValue(cell).toString();
            if (mapCodeExcelField.containsKey(key)) {
                headers.put(mapCodeExcelField.get(key), i);
            }
            i++;
        }
        for (ExcelSystemFieldDTO excelSystemFieldDTO : requestData.getExcelSystemFieldDTOS()) {
            if (excelSystemFieldDTO.isRequired() && !headers.containsKey(excelSystemFieldDTO.getCode())) {
                dto.setError(true);
                dto.getErrors().add(excelSystemFieldDTO.getCode());
            }
        }

        if (file.getSize() >= MAXIMUM_SIZE_UPLOADED) {
            dto.setMessage(Constants.UpdateDataDTOMessages.FILE_TOO_LARGE);
            return dto;
        }
        if (dto.getError()) {
            dto.setMessage(Constants.UpdateDataDTOMessages.REQUIRED_ITEM);
            return dto;
        }

        requestData.setHeaders(headers);
        requestData.setCodeWithExcelField(requestData.getExcelSystemFieldDTOS()
                .stream().filter(x -> !Strings.isNullOrEmpty(x.getExcelField()))
                .collect(Collectors.toMap(ExcelSystemFieldDTO::getCode, ExcelSystemFieldDTO::getExcelField)));
        switch (requestData.getType()) {
            case Constants.ExcelDanhMucType.KH:
                return uploadKHExcel(sheet, requestData, workbook, currentUserLoginAndOrg.get());
            case Constants.ExcelDanhMucType.NCC:
                return uploadNCCExcel(sheet, requestData, workbook, currentUserLoginAndOrg.get());
            case Constants.ExcelDanhMucType.NV:
                return uploadNVExcel(sheet, requestData, workbook, currentUserLoginAndOrg.get());
            case Constants.ExcelDanhMucType.VTHH:
                return uploadVTHHExcel(sheet, requestData, workbook, currentUserLoginAndOrg.get());
        }

        return new UpdateDataDTO();
    }
    private String getCellValueString(Row row, UpdateDataDTO requestData, String code) {
        try {
            return ExcelUtils.getCellValueStringTypeString(row
                    .getCell(requestData.getHeaders().get(code)));
        } catch (Exception e) {
            return null;
        }
    }
    private BigDecimal getCellValueBigDecimal(Row row, UpdateDataDTO requestData, String code) {
        try {
            return ExcelUtils.getCellValueBigDecimal(row
                .getCell(requestData.getHeaders().get(code)));
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            requestData.setError(true);
            ExcelUtils.setComment(row.getCell(requestData.getHeaders().get(code)),
                requestData.getCodeWithExcelField().get(code)
                    + " không hợp lệ!",
                row, requestData.getHeaders().get(code));
            return null;
        }
    }
    private Object getCellValue(Row row, UpdateDataDTO requestData, String code) {
        return ExcelUtils.getCellValue(row
                .getCell(requestData.getHeaders().get(code)));
    }

    private LocalDate getCellValueLocalDate(Row row, UpdateDataDTO requestData, String code) {
        return ExcelUtils.getCellValueLocalDate(row
                .getCell(requestData.getHeaders().get(code)));
    }

    private void setComment(Row row, UpdateDataDTO requestData, String code, String message) {
        requestData.setError(true);
        try {
            ExcelUtils.setComment(row.getCell(requestData.getHeaders().get(code)), message,
                    row, requestData.getHeaders().get(code));
        } catch (Exception ignored) {
        }
    }
    private void setComment(Row row, UpdateDataDTO requestData, String code, String message, boolean isNew) {
        if (isNew) {
            setComment(row, requestData, code, message);
        }
    }

    private void removeAllComment(Row row, UpdateDataDTO requestData) {
        for (String header: requestData.getHeaders().keySet()) {
            ExcelUtils.removeComment(row.getCell(requestData.getHeaders().get(header)));
            Cell cell = row.getCell(requestData.getHeaders().get(header));
            if (cell == null) {
                row.createCell(requestData.getHeaders().get(header));
            }
        }
    }

    private boolean emailMatches(String email) {
        String regex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        return email.matches(regex);
    }

    private boolean taxRateMatches(String rate) {
        String regex = "^(\\d*\\.)?\\d+$|^[0-9]%$|^[1-9][0-9]%$|100%";
        return rate.matches(regex);
    }
    /**
     * @source: https://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java
     */
    private boolean urlMatches(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return url.matches(regex);
    }
    /**
     * @source: http://stackoverflow.com/a/20971688
     */
    private boolean phoneNumberMatches(String phone) {
        String regex = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$";
        return phone.matches(regex);
    }

    private UpdateDataDTO uploadKHExcel(Sheet sheet, UpdateDataDTO requestData, Workbook workbook, SecurityDTO securityDTO) throws IOException {
        UpdateDataDTO dto = new UpdateDataDTO();
        dto.setError(false);
        // start init map value
        Map<String, Integer> scaleTypes = new HashMap<>();
        scaleTypes.put("Tổ chức", 0);
        scaleTypes.put("Cá nhân", 1);

        Map<String, Integer> objectTypes = new HashMap<>();
        objectTypes.put("Khách hàng", 1);
        objectTypes.put("Khách hàng/Nhà cung cấp", 2);
        objectTypes.put("KH hoặc NCC", 2);
        objectTypes.put("Khác", 3);

        Map<String, Integer> gender = new HashMap<>();
        gender.put("Nữ", 0);
        gender.put("Nam", 1);

        String messageRequired = " bắt buộc nhập!";
        String hasExist = " đã tồn tại!";
        String hasDuplicate = " bị trùng!";
        String invalid = " không hợp lệ!";
        String longThan25 = " dài quá 25 ký tự!";
        /*
         * import ghi đè thì cho listAccountingObjectExist = empty
         * thì đoạn check dưới không cần sửa và k cần load dữ liệu từ db
         */

        Set<String> accountingObjectUsing = new HashSet<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            requestData.setError(false);
            removeAllComment(row, requestData);
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                accountingObjectUsing.add(accountingObjectCode);
            }
        }
        List<String> accountingObjList = new ArrayList<>(accountingObjectUsing);
        List<List<String>> smallerLists = Lists.partition(accountingObjList, 2000);
        Map<String, AccountingObject> listAccountingObjectExist = new HashMap<>();
        if (accountingObjList.size() < 2000) {
            listAccountingObjectExist = requestData.getImportType() == Constants.ImportType.importOverride ?
                new HashMap<>() :
                accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                    systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMDoiTuong), accountingObjectUsing)
                    .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));

        }

        if (accountingObjList.size() > 2000) {
            for (List<String> s : smallerLists) {
                listAccountingObjectExist = !accountingObjectUsing.isEmpty() && requestData.getImportType() == Constants.ImportType.importOverride ?
                    new HashMap<>() :
                    accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                        systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                            Constants.SystemOption.TCKHAC_SDDMDoiTuong), s)
                        .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));
            }
        }

        Map<String, PaymentClause> mapPaymentClauses = paymentClauseRepository
            .findAllByCompanyID(securityDTO.getOrg()).stream().collect(Collectors.toMap(PaymentClause::getPaymentClauseCode, x -> x));

        Map<String, AccountingObjectGroup> mapAccountingObjectGroup = accountingObjectGroupRepository.findAllAccountingObjectGroup(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong))
            .stream().collect(Collectors.toMap(AccountingObjectGroup::getAccountingObjectGroupCode, x -> x));
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Map<String, Bank> mapBankAccount = bankRepository.findAllBankByCompanyIDIn(currentUserLoginAndOrg.get().getOrgGetData())
            .stream()
            .collect(Collectors.toMap(x -> x.getBankCode().toUpperCase(), x -> x));

        // end init map value
        Map<String, AccountingObject> accountingObjects = new HashMap<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            boolean isBlankRow = true;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellValueString = ExcelUtils.getCellValueString(row.getCell(k));
                if (!Strings.isNullOrEmpty(cellValueString)) {
                    isBlankRow = false;
                    break;
                }
            }
            if (isBlankRow) {
                continue;
            }
            requestData.setError(false);
            removeAllComment(row, requestData);

            AccountingObject accountingObject = new AccountingObject();
            boolean isNewAccountingObject = true;
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                if (listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    if (requestData.getImportType() == Constants.ImportType.importUpdate) {
                        // clean Object
                        AccountingObject accountingObjectOld = listAccountingObjectExist.get(accountingObjectCode);
                        // Set lại các trường gữi nguyên
                        accountingObject.setId(accountingObjectOld.getId());
                        accountingObject.setAccountingObjectCode(accountingObjectOld.getAccountingObjectCode());
                        if (accountingObjectOld.getAccountingObjectBankAccounts() != null) {
                            accountingObject.setAccountingObjectBankAccounts(accountingObjectOld.getAccountingObjectBankAccounts());
                        }
                        isNewAccountingObject = false;
                    }
                }
                // start check bankAccount
                AccountingObjectBankAccount accountingObjectBankAccount = new AccountingObjectBankAccount();
                String bankAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.BankAccount);
                boolean isNewBankAccount = true;
                Map<String, AccountingObjectBankAccount> mapAOBankAccount = accountingObject
                    .getAccountingObjectBankAccounts().stream()
                    .collect(Collectors.toMap(AccountingObjectBankAccount::getBankAccount, x -> x));
                if (mapAOBankAccount.containsKey(bankAccount.trim())) {
                    accountingObjectBankAccount = mapAOBankAccount.get(bankAccount);
                    isNewBankAccount = false;
                }
                String bankName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.BankName);
                if (!Strings.isNullOrEmpty(bankName)) {
                    if (mapBankAccount.containsKey(bankName.toUpperCase().trim())) {
                        accountingObjectBankAccount.setBankName(mapBankAccount.get(bankName).getBankName());
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.BankName,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.BankName)
                                + invalid);
                    }
                }
                String bankBranchName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.BankBranchName);
                if (!Strings.isNullOrEmpty(bankBranchName)) {
                    accountingObjectBankAccount.setBankBranchName(bankBranchName);
                }
                String accountHolderName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountHolderName);
                if (!Strings.isNullOrEmpty(accountHolderName)) {
                    accountingObjectBankAccount.setAccountHolderName(accountHolderName);
                }

                if (isNewBankAccount) {
                    if (!Strings.isNullOrEmpty(bankAccount)) {
                        accountingObjectBankAccount.setBankAccount(bankAccount);
                        if (accountingObjects.containsKey(accountingObjectCode)) {
                            accountingObjectBankAccount.setOrderPriority(Collections.max(accountingObjects
                                .get(accountingObjectCode).getAccountingObjectBankAccounts()
                                .stream().map(AccountingObjectBankAccount::getOrderPriority)
                                .collect(Collectors.toList())) + 1);
                            accountingObjects.get(accountingObjectCode).addAccountingObjectBankAccounts(accountingObjectBankAccount);
                            continue;
                        } else {
                            accountingObjectBankAccount.setOrderPriority(0);
                            accountingObject.addAccountingObjectBankAccounts(accountingObjectBankAccount);
                        }
                    }
                }

                // end check bankAccount
                // neu la dong dau tien va trung trong he thong thi set cmt
                //requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode) - lay chu tren header dynamic
                if (isNewAccountingObject && listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode)
                            + hasExist);
                } else if (isNewAccountingObject && !accountingObjects.isEmpty() &&
                    accountingObjects.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode)
                            + hasDuplicate);
                } else if (accountingObjectCode.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode)
                            + longThan25);
                } else {
                    accountingObject.setAccountingObjectCode(accountingObjectCode);
                }
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectCode)
                        + messageRequired);
            }

            String accountingObjectName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectName);
            if (!Strings.isNullOrEmpty(accountingObjectName)) {
                accountingObject.setAccountingObjectName(accountingObjectName);
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectName,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectName)
                        + messageRequired, isNewAccountingObject);
            }

            String scaleType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ScaleType);
            if (!Strings.isNullOrEmpty(scaleType)) {
                if (scaleTypes.containsKey(scaleType.trim())) {
                    accountingObject.setScaleType(scaleTypes.get(scaleType));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ScaleType,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ScaleType)
                            + invalid, isNewAccountingObject);
                }

            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ScaleType,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ScaleType)
                        + messageRequired, isNewAccountingObject);
            }
            String objectType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ObjectType);
            if (!Strings.isNullOrEmpty(objectType)) {
                objectType = objectType.trim();
                if (objectTypes.containsKey(objectType.trim())) {
                    accountingObject.setObjectType(objectTypes.get(objectType));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ObjectType,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ObjectType)
                            + invalid, isNewAccountingObject);
                }

            } else {
                accountingObject.setObjectType(objectTypes.get("Khách hàng"));
            }
            String tel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Tel);
            if (!Strings.isNullOrEmpty(tel)) {
                if (tel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Tel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.Tel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setTel(tel);
                }
            } else {

            }

            String accountingObjectAddress = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectAddress);
            if (!Strings.isNullOrEmpty(accountingObjectAddress)) {
                accountingObject.setAccountingObjectAddress(accountingObjectAddress);
            }
            String taxCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.TaxCode);
            if (!Strings.isNullOrEmpty(taxCode)) {
                if (Utils.checkTaxCode(taxCode)) {
                    accountingObject.setTaxCode(taxCode);
                } else if (taxCode.length() > MAX_CODE){
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.TaxCode)
                            + invalid, isNewAccountingObject);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.TaxCode)
                            + invalid, isNewAccountingObject);
                }
            } else {

            }

            String email = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Email);
            if (!Strings.isNullOrEmpty(email)) {
                List<String> checkedEmails = new ArrayList<>();
                List<String> lstEmail = Arrays.asList(email.split(","));
                for (String s : lstEmail) {
                    s = s.trim();
                    if (emailMatches(s)) {
                        checkedEmails.add(s);
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Email,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.Email)
                                + invalid, isNewAccountingObject);
                    }
                }
                if (email.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Email,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.Email)
                            + invalid, isNewAccountingObject);
                } else {
                    String lastEmail = checkedEmails.stream().collect(Collectors.joining(","));
                    accountingObject.setEmail(lastEmail);
                }
            } else {

            }

            String website = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Website);
            if (!Strings.isNullOrEmpty(website)){
                if (website.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Website,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.Website)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setWebsite(website);
                }
            } else {

            }

            String fax = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Fax);
            if (!Strings.isNullOrEmpty(fax)) {
                if (fax.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Fax,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.Fax)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setFax(fax);
                }
            } else {

            }

            BigDecimal maximizaDebtAmount = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucKH.MaximizaDebtAmount);
            if (maximizaDebtAmount != null) {
                accountingObject.setMaximizaDebtAmount(maximizaDebtAmount);
            } else {

            }
            BigDecimal dueTime = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucKH.DueTime);
            if (dueTime != null) {
                accountingObject.setDueTime(dueTime.intValue());
            } else {

            }

            String paymentClauseCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.PaymentClauseCode);
            if (!Strings.isNullOrEmpty(paymentClauseCode))
                if (maximizaDebtAmount != null) {
                    if (mapPaymentClauses.containsKey(paymentClauseCode.trim())) {
                        accountingObject.setPaymentClause(mapPaymentClauses.get(paymentClauseCode));
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.PaymentClauseCode,
                            requestData.getCodeWithExcelField()
                                .get(ExcelConstant.KeyCodeDanhMucKH.PaymentClauseCode) + invalid, isNewAccountingObject);
                    }

                }
            String accountingObjectGroupCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectGroupCode);
            if (!Strings.isNullOrEmpty(accountingObjectGroupCode)) {
                if (mapAccountingObjectGroup.containsKey(accountingObjectGroupCode.toUpperCase().trim())) {
                    accountingObject.setAccountingObjectGroup(mapAccountingObjectGroup.get(accountingObjectGroupCode.toUpperCase()));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.AccountingObjectGroupCode,
                        requestData.getCodeWithExcelField()
                            .get(ExcelConstant.KeyCodeDanhMucKH.AccountingObjectGroupCode) + invalid, isNewAccountingObject);
                }
            }
            String contactName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactName);
            if (!Strings.isNullOrEmpty(contactName)) {
                accountingObject.setContactName(contactName);
            }
            String contactTitle = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactTitle);
            if (!Strings.isNullOrEmpty(contactTitle)) {
                accountingObject.setContactTitle(contactTitle);
            }
            String contactSex = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactSex);
            if (!Strings.isNullOrEmpty(contactSex)) {
                if (gender.containsKey(contactSex)) {
                    accountingObject.setContactSex(gender.get(contactSex));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactSex,
                        requestData.getCodeWithExcelField()
                            .get(ExcelConstant.KeyCodeDanhMucKH.ContactSex) + invalid, isNewAccountingObject);
                }
            }
            String contactAddress = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactAddress);
            if (!Strings.isNullOrEmpty(contactAddress)) {
                accountingObject.setContactAddress(contactAddress);
            }

            String contactMobile = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactMobile);
            if (!Strings.isNullOrEmpty(contactMobile)) {
                if (contactMobile.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactMobile,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ContactMobile)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setContactMobile(contactMobile);
                }
            } else {

            }

            String contactEmail = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactEmail);
            if (!Strings.isNullOrEmpty(contactEmail)) {
                List<String> checkedEmails = new ArrayList<>();
                List<String> lstEmail = Arrays.asList(contactEmail.split(","));
                for (String s : lstEmail) {
                    s = s.trim();
                    if (emailMatches(s)) {
                        checkedEmails.add(s);
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactEmail,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ContactEmail)
                                + invalid, isNewAccountingObject);
                    }
                }
                if (contactEmail.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactEmail,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ContactEmail)
                            + invalid, isNewAccountingObject);
                } else {
                    String lastEmail = checkedEmails.stream().collect(Collectors.joining(","));
                    accountingObject.setContactEmail(lastEmail);
                }
            } else {

            }

            String contactHomeTel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactHomeTel);
            if (!Strings.isNullOrEmpty(contactHomeTel)) {
                if (contactHomeTel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactHomeTel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ContactHomeTel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setContactHomeTel(contactHomeTel);
                }
            } else {

            }

            String contactOfficeTel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactOfficeTel);
            if (!Strings.isNullOrEmpty(contactOfficeTel)) {
                if (contactOfficeTel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.ContactOfficeTel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.ContactOfficeTel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setContactOfficeTel(contactOfficeTel);
                }
            } else {

            }

            String identificationNo = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IdentificationNo);
            if (!Strings.isNullOrEmpty(identificationNo)) {
                if (identificationNo.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IdentificationNo,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.IdentificationNo)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setIdentificationNo(identificationNo);
                }
            } else {

            }

            String issueDateStr = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IssueDate);
            LocalDate issueDate = getCellValueLocalDate(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IssueDate);
            if (!Strings.isNullOrEmpty(issueDateStr)) {
                if (issueDate != null) {
                    accountingObject.setIssueDate(issueDate);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IssueDate,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucKH.IssueDate)
                            + invalid, isNewAccountingObject);
                }
            }
            String issueBy = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucKH.IssueBy);
            if (!Strings.isNullOrEmpty(issueBy)) {
                accountingObject.setIssueBy(issueBy);
            }

            // start add object
            if (!requestData.getError()) {
                // add to list
                accountingObject.setCompanyId(securityDTO.getOrg());
                accountingObject.setisEmployee(false);
                accountingObject.setIsActive(true);
                accountingObjects.put(accountingObject.getAccountingObjectCode(), accountingObject);
            } else {
                dto.setError(true);
            }
            // end add object
        }
        if (dto.getError()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage(Constants.UpdateDataDTOMessages.FAIL);
        } else {
            switch (requestData.getImportType()) {
                case Constants.ImportType.importNew:
                    accountingObjectRepository.saveAll(accountingObjects.values());

                    break;
                case Constants.ImportType.importUpdate:
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                case Constants.ImportType.importOverride:
                    List<UUID> listDeleteAllByCompanyId = systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong);
                    accountingObjectRepository.deleteAllByCompanyIdIn(systemOptionRepository
                            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong),
                        Arrays.asList(1, 2, 3));
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                default:
                    break;
            }
            dto.setMessage(Constants.UpdateDataDTOMessages.SUCCESS);
        }
        workbook.close();
        return dto;
    }

    private UpdateDataDTO uploadNCCExcel(Sheet sheet, UpdateDataDTO requestData, Workbook workbook, SecurityDTO securityDTO) throws IOException {
        UpdateDataDTO dto = new UpdateDataDTO();
        dto.setError(false);
        // start init map value
        Map<String, Integer> scaleTypes = new HashMap<>();
        scaleTypes.put("Tổ chức", 0);
        scaleTypes.put("Cá nhân", 1);

        Map<String, Integer> objectTypes = new HashMap<>();
        objectTypes.put("Nhà cung cấp", 0);
        objectTypes.put("Khách hàng/Nhà cung cấp", 2);
        objectTypes.put("KH hoặc NCC", 2);
        objectTypes.put("Khác", 3);

        Map<String, Integer> gender = new HashMap<>();
        gender.put("Nữ", 0);
        gender.put("Nam", 1);
        /*
         * import ghi đè thì cho listAccountingObjectExist = empty
         * thì đoạn check dưới không cần sửa và k cần load dữ liệu từ db
         */

        Set<String> accountingObjectUsing = new HashSet<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                accountingObjectUsing.add(accountingObjectCode);
            }
        }

        Map<String, AccountingObject> listAccountingObjectExist = requestData.getImportType() == Constants.ImportType.importOverride ?
            new HashMap<>() :
            accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                    Constants.SystemOption.TCKHAC_SDDMDoiTuong), accountingObjectUsing)
                .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));

        Map<String, AccountingObjectGroup> mapAccountingObjectGroup = accountingObjectGroupRepository.findAllAccountingObjectGroup(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong))
            .stream().collect(Collectors.toMap(x -> x.getAccountingObjectGroupCode().toUpperCase(), x -> x));

        Map<String, Bank> mapBankAccount = bankRepository.findAllBankByCompanyIDIn(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong))
            .stream()
            .collect(Collectors.toMap(Bank::getBankCode, x -> x));
        String messageRequired = " bắt buộc nhập!";
        String hasExist = " đã tồn tại!";
        String hasDuplicate = " bị trùng!";
        String invalid = " không hợp lệ!";
        String longThan25c = " dài quá 25 kí tự!";
        // end init map value
        Map<String, AccountingObject> accountingObjects = new HashMap<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            boolean isBlankRow = true;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellValueString = ExcelUtils.getCellValueString(row.getCell(k));
                if (!Strings.isNullOrEmpty(cellValueString)) {
                    isBlankRow = false;
                    break;
                }
            }
            if (isBlankRow) {
                continue;
            }
            requestData.setError(false);
            removeAllComment(row, requestData);
            AccountingObject accountingObject = new AccountingObject();
            boolean isNewAccountingObject = true;
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                if (listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    if (requestData.getImportType() == Constants.ImportType.importUpdate) {
                        // clean Object
                        AccountingObject accountingObjectOld = listAccountingObjectExist.get(accountingObjectCode);
                        // Set lại các trường gữi nguyên
                        accountingObject.setId(accountingObjectOld.getId());
                        accountingObject.setAccountingObjectCode(accountingObjectOld.getAccountingObjectCode());
                        accountingObject.setAccountingObjectName(accountingObjectOld.getAccountingObjectName());
                        if (accountingObjectOld.getAccountingObjectBankAccounts() != null) {
                            accountingObject.setAccountingObjectBankAccounts(accountingObjectOld.getAccountingObjectBankAccounts());
                        }
                        isNewAccountingObject = false;
                    }
                }
                // start check bankAccount
                AccountingObjectBankAccount accountingObjectBankAccount = new AccountingObjectBankAccount();
                String bankAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.BankAccount);
                boolean isNewBankAccount = true;
                Map<String, AccountingObjectBankAccount> mapAOBankAccount = accountingObject
                    .getAccountingObjectBankAccounts().stream()
                    .collect(Collectors.toMap(AccountingObjectBankAccount::getBankAccount, x -> x));
                if (mapAOBankAccount.containsKey(bankAccount.trim())) {
                    accountingObjectBankAccount = mapAOBankAccount.get(bankAccount);
                    isNewBankAccount = false;
                }
                String bankName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.BankName);
                if (!Strings.isNullOrEmpty(bankName)) {
                    if (mapBankAccount.containsKey(bankName.trim())) {
                        accountingObjectBankAccount.setBankName(mapBankAccount.get(bankName).getBankName());
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.BankName,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.BankName)
                                + invalid);
                    }
                }
                String bankBranchName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.BankBranchName);
                if (!Strings.isNullOrEmpty(bankBranchName)) {
                    accountingObjectBankAccount.setBankBranchName(bankBranchName);
                }
                String accountHolderName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountHolderName);
                if (!Strings.isNullOrEmpty(accountHolderName)) {
                    accountingObjectBankAccount.setAccountHolderName(accountHolderName);
                }

                if (isNewBankAccount) {
                    if (!Strings.isNullOrEmpty(bankAccount)) {
                        accountingObjectBankAccount.setBankAccount(bankAccount);
                        if (accountingObjects.containsKey(accountingObjectCode)) {
                            accountingObjectBankAccount.setOrderPriority(Collections.max(accountingObjects
                                .get(accountingObjectCode).getAccountingObjectBankAccounts()
                                .stream().map(AccountingObjectBankAccount::getOrderPriority)
                                .collect(Collectors.toList())) + 1);
                            accountingObjects.get(accountingObjectCode).addAccountingObjectBankAccounts(accountingObjectBankAccount);
                            continue;
                        } else {
                            accountingObjectBankAccount.setOrderPriority(0);
                            accountingObject.addAccountingObjectBankAccounts(accountingObjectBankAccount);
                        }
                    }
                }

                // end check bankAccount

                if (isNewAccountingObject && listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode)
                            + hasExist);
                } else if (isNewAccountingObject && !accountingObjects.isEmpty() &&
                    accountingObjects.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode)
                            + hasDuplicate);
                } else if (isNewAccountingObject && accountingObjectCode.length() > MAX_LENGTH_CONTACT){
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode)
                            + invalid);
                } else {
                    accountingObject.setAccountingObjectCode(accountingObjectCode);
                }
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode)
                        + messageRequired);
            }

            String accountingObjectName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectName);
            if (!Strings.isNullOrEmpty(accountingObjectName)) {
                accountingObject.setAccountingObjectName(accountingObjectName);
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectName,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectName)
                        + messageRequired, isNewAccountingObject);
            }

            String scaleType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ScaleType);
            if (!Strings.isNullOrEmpty(scaleType)) {
                if (scaleTypes.containsKey(scaleType)) {
                    accountingObject.setScaleType(scaleTypes.get(scaleType));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ScaleType,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ScaleType)
                            + invalid, isNewAccountingObject);
                }

            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ScaleType,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ScaleType)
                        + messageRequired, isNewAccountingObject);
            }
            String objectType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ObjectType);
            if (!Strings.isNullOrEmpty(objectType)) {
                objectType = objectType.trim();
                if (objectTypes.containsKey(objectType)) {
                    accountingObject.setObjectType(objectTypes.get(objectType));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ObjectType,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ObjectType)
                            + invalid, isNewAccountingObject);
                }

            } else {
                accountingObject.setObjectType(objectTypes.get("Nhà cung cấp"));
            }

            String tel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Tel);
            if (!Strings.isNullOrEmpty(tel)) {
                if (tel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucKH.Tel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.Tel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setTel(tel);
                }
            } else {

            }

            String accountingObjectAddress = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectAddress);
            if (!Strings.isNullOrEmpty(accountingObjectAddress)) {
                accountingObject.setAccountingObjectAddress(accountingObjectAddress);
            }
            String taxCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.TaxCode);
            if (!Strings.isNullOrEmpty(taxCode)) {
                if (Utils.checkTaxCode(taxCode)) {
                    accountingObject.setTaxCode(taxCode);
                } else if (taxCode.length() > MAX_CODE){
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.TaxCode)
                            + invalid, isNewAccountingObject);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.TaxCode)
                            + invalid, isNewAccountingObject);
                }
            } else {

            }

            String email = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Email);
            if (!Strings.isNullOrEmpty(email)) {
                List<String> checkedEmails = new ArrayList<>();
                List<String> lstEmail = Arrays.asList(email.split(","));
                for (String s : lstEmail) {
                    s = s.trim();
                    if (emailMatches(s)) {
                        checkedEmails.add(s);
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Email,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.Email)
                                + invalid, isNewAccountingObject);
                    }
                }
                if (email.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Email,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.Email)
                            + invalid, isNewAccountingObject);
                } else {
                    String lastEmail = checkedEmails.stream().collect(Collectors.joining(","));
                    accountingObject.setEmail(lastEmail);
                }
            } else {

            }

            String website = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Website);
            if (!Strings.isNullOrEmpty(website)){
                if (website.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Website,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.Website)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setWebsite(website);
                }
            } else {

            }

            String fax = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Fax);
            if (!Strings.isNullOrEmpty(fax)) {
                if (fax.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.Fax,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.Fax)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setFax(fax);
                }
            } else {

            }

            String accountingObjectGroupCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectGroupCode);
            if (!Strings.isNullOrEmpty(accountingObjectGroupCode)) {
                if (mapAccountingObjectGroup.containsKey(accountingObjectGroupCode.toUpperCase())) {
                    accountingObject.setAccountingObjectGroup(mapAccountingObjectGroup.get(accountingObjectGroupCode.toUpperCase()));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectGroupCode,
                        requestData.getCodeWithExcelField()
                            .get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectGroupCode) + invalid, isNewAccountingObject);
                }
            }
            String contactName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactName);
            if (!Strings.isNullOrEmpty(contactName)) {
                accountingObject.setContactName(contactName);
            }
            String contactTitle = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactTitle);
            if (!Strings.isNullOrEmpty(contactTitle)) {
                accountingObject.setContactTitle(contactTitle);
            }
            String contactSex = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactSex);
            if (!Strings.isNullOrEmpty(contactSex)) {
                if (gender.containsKey(contactSex)) {
                    accountingObject.setContactSex(gender.get(contactSex));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactSex,
                        requestData.getCodeWithExcelField()
                            .get(ExcelConstant.KeyCodeDanhMucNCC.ContactSex) + invalid, isNewAccountingObject);
                }
            }
            String contactAddress = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactAddress);
            if (!Strings.isNullOrEmpty(contactAddress)) {
                accountingObject.setContactAddress(contactAddress);
            }

            String contactMobile = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactMobile);
            accountingObject.setContactMobile(contactMobile);

            String contactEmail = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactEmail);
            if (!Strings.isNullOrEmpty(contactEmail)) {
                List<String> checkedEmails = new ArrayList<>();
                List<String> lstEmail = Arrays.asList(contactEmail.split(","));
                for (String s : lstEmail) {
                    s = s.trim();
                    if (emailMatches(s)) {
                        checkedEmails.add(s);
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactEmail,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ContactEmail)
                                + invalid, isNewAccountingObject);
                    }
                }
                if (contactEmail.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactEmail,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ContactEmail)
                            + invalid, isNewAccountingObject);
                } else {
                    String lastEmail = checkedEmails.stream().collect(Collectors.joining(","));
                    accountingObject.setContactEmail(lastEmail);
                }
            } else {

            }

            String contactHomeTel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactHomeTel);
            if (!Strings.isNullOrEmpty(contactHomeTel)) {
                if (contactHomeTel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactHomeTel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ContactHomeTel)
                            + invalid, isNewAccountingObject);
                }
                else {
                    accountingObject.setContactHomeTel(contactHomeTel);
                }
            } else {

            }

            String contactOfficeTel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactOfficeTel);
            if (!Strings.isNullOrEmpty(contactOfficeTel)) {
                if (contactOfficeTel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.ContactOfficeTel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.ContactOfficeTel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setContactOfficeTel(contactOfficeTel);
                }
            } else {

            }

            String identificationNo = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IdentificationNo);
            if (!Strings.isNullOrEmpty(identificationNo)) {
                if (identificationNo.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IdentificationNo,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.IdentificationNo)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setIdentificationNo(identificationNo);
                }
            } else {

            }

            String issueDateStr = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IssueDate);
            LocalDate issueDate = getCellValueLocalDate(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IssueDate);
            if (!Strings.isNullOrEmpty(issueDateStr)) {
                if (issueDate != null) {
                    accountingObject.setIssueDate(issueDate);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IssueDate,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.IssueDate)
                            + invalid, isNewAccountingObject);
                }
            }
            String issueBy = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.IssueBy);
            if (!Strings.isNullOrEmpty(issueBy)) {
                accountingObject.setIssueBy(issueBy);
            }

            // start add object
            if (!requestData.getError()) {
                // add to list
                accountingObject.setCompanyId(securityDTO.getOrg());
                accountingObject.setisEmployee(false);
                accountingObject.setIsActive(true);
                accountingObjects.put(accountingObject.getAccountingObjectCode(), accountingObject);
            } else {
                dto.setError(true);
            }
            // end add object
        }
        if (dto.getError()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage(Constants.UpdateDataDTOMessages.FAIL);
        } else {
            switch (requestData.getImportType()) {
                case Constants.ImportType.importNew:
                    accountingObjectRepository.insertBulk(accountingObjects.values().stream().collect(Collectors.toList()));
                    break;
                case Constants.ImportType.importUpdate:
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                case Constants.ImportType.importOverride:
                    accountingObjectRepository.deleteAllByCompanyIdIn(systemOptionRepository
                            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong),
                        Arrays.asList(0, 2));
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                default:
                    break;
            }
            dto.setMessage(Constants.UpdateDataDTOMessages.SUCCESS);
        }
        workbook.close();
        return dto;
    }

    private UpdateDataDTO uploadNVExcel(Sheet sheet, UpdateDataDTO requestData, Workbook workbook, SecurityDTO securityDTO) throws IOException {
        UpdateDataDTO dto = new UpdateDataDTO();
        dto.setError(false);
        // start init map value
        Map<String, Integer> objectTypes = new HashMap<>();
        objectTypes.put("Nhà cung cấp", 0);
        objectTypes.put("Khách hàng", 1);
        objectTypes.put("Khách hàng/Nhà cung cấp", 2);
        objectTypes.put("KH hoặc NCC", 2);
        objectTypes.put("Khác", 3);

        Map<String, Integer> gender = new HashMap<>();
        gender.put("Nữ", 0);
        gender.put("Nam", 1);

        String messageRequired = " bắt buộc nhập!";
        String hasExist = " đã tồn tại!";
        String hasDuplicate = " bị trùng!";
        String invalid = " không hợp lệ!";
        /*
         * import ghi đè thì cho listAccountingObjectExist = empty
         * thì đoạn check dưới không cần sửa và k cần load dữ liệu từ db
         */
        Set<String> accountingObjectUsing = new HashSet<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                accountingObjectUsing.add(accountingObjectCode);
            }
        }

        /*Map<String, AccountingObject> listAccountingObjectExist = requestData.getImportType() == Constants.ImportType.importOverride ?
            new HashMap<>() :
            accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                    Constants.SystemOption.TCKHAC_SDDMDoiTuong), accountingObjectUsing)
                .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));
*/
        Map<String, AccountingObjectGroup> mapAccountingObjectGroup = accountingObjectGroupRepository.findAllAccountingObjectGroup(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong))
            .stream().collect(Collectors.toMap(x -> x.getAccountingObjectGroupCode().toUpperCase(), x -> x));

        Map<String, UUID> organizations = organizationUnitService.findAllActive()
            .stream().collect(Collectors.toMap(x -> x.getOrganizationUnitCode().toUpperCase(), OrganizationUnit::getId));

        List<String> accountingObjList = new ArrayList<>(accountingObjectUsing);
        List<List<String>> smallerLists = Lists.partition(accountingObjList, 2000);
        Map<String, AccountingObject> listAccountingObjectExist = new HashMap<>();
        if (accountingObjList.size() < 2000) {
            listAccountingObjectExist = requestData.getImportType() == Constants.ImportType.importOverride ?
                new HashMap<>() :
                accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                    systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMDoiTuong), accountingObjectUsing)
                    .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));

        }

        if (accountingObjList.size() > 2000) {
            for (List<String> s : smallerLists) {
                listAccountingObjectExist = !accountingObjectUsing.isEmpty() && requestData.getImportType() == Constants.ImportType.importOverride ?
                    new HashMap<>() :
                    accountingObjectRepository.getAccountingObjectCodesByCompanyId(
                        systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                            Constants.SystemOption.TCKHAC_SDDMDoiTuong), s)
                        .stream().collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, x -> x));
            }
        }
        Map<String, Bank> mapBankAccount = bankRepository.findAllBankByCompanyIDIn(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong))
            .stream()
            .collect(Collectors.toMap(Bank::getBankCode, x -> x));

        // end init map value
        Map<String, AccountingObject> accountingObjects = new HashMap<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            boolean isBlankRow = true;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellValueString = ExcelUtils.getCellValueString(row.getCell(k));
                if (!Strings.isNullOrEmpty(cellValueString)) {
                    isBlankRow = false;
                    break;
                }
            }
            if (isBlankRow) {
                continue;
            }
            requestData.setError(false);
            removeAllComment(row, requestData);
            AccountingObject accountingObject = new AccountingObject();
            boolean isNewAccountingObject = true;
            String accountingObjectCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode);
            if (!Strings.isNullOrEmpty(accountingObjectCode)) {
                if (listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    if (requestData.getImportType() == Constants.ImportType.importUpdate) {
                        // clean Object
                        AccountingObject accountingObjectOld = listAccountingObjectExist.get(accountingObjectCode);
                        // Set lại các trường gữi nguyên
                        accountingObject.setId(accountingObjectOld.getId());
                        accountingObject.setAccountingObjectCode(accountingObjectOld.getAccountingObjectCode());
                        if (accountingObjectOld.getAccountingObjectBankAccounts() != null) {
                            accountingObject.setAccountingObjectBankAccounts(accountingObjectOld.getAccountingObjectBankAccounts());
                        }
                        isNewAccountingObject = false;
                    }
                }
                // start check bankAccount
                AccountingObjectBankAccount accountingObjectBankAccount = new AccountingObjectBankAccount();
                String bankAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.BankAccount);
                boolean isNewBankAccount = true;
                Map<String, AccountingObjectBankAccount> mapAOBankAccount = accountingObject
                    .getAccountingObjectBankAccounts().stream()
                    .collect(Collectors.toMap(AccountingObjectBankAccount::getBankAccount, x -> x));
                if (mapAOBankAccount.containsKey(bankAccount)) {
                    accountingObjectBankAccount = mapAOBankAccount.get(bankAccount);
                    isNewBankAccount = false;
                }
                String bankName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.BankName);
                if (!Strings.isNullOrEmpty(bankName)) {
                    if (mapBankAccount.containsKey(bankName)) {
                        accountingObjectBankAccount.setBankName(mapBankAccount.get(bankName).getBankName());
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.BankName,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.BankName)
                                + invalid);
                    }
                }
                String bankBranchName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.BankBranchName);
                if (!Strings.isNullOrEmpty(bankBranchName)) {
                    accountingObjectBankAccount.setBankBranchName(bankBranchName);
                }
                String accountHolderName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountHolderName);
                if (!Strings.isNullOrEmpty(accountHolderName)) {
                    accountingObjectBankAccount.setAccountHolderName(accountHolderName);
                }

                if (isNewBankAccount) {
                    if (!Strings.isNullOrEmpty(bankAccount)) {
                        accountingObjectBankAccount.setBankAccount(bankAccount);
                        if (accountingObjects.containsKey(accountingObjectCode)) {
                            accountingObjectBankAccount.setOrderPriority(Collections.max(accountingObjects
                                .get(accountingObjectCode).getAccountingObjectBankAccounts()
                                .stream().map(AccountingObjectBankAccount::getOrderPriority)
                                .collect(Collectors.toList())) + 1);
                            accountingObjects.get(accountingObjectCode).addAccountingObjectBankAccounts(accountingObjectBankAccount);
                            continue;
                        } else {
                            accountingObjectBankAccount.setOrderPriority(0);
                            accountingObject.addAccountingObjectBankAccounts(accountingObjectBankAccount);
                        }
                    }
                }

                // end check bankAccount
                if (isNewAccountingObject && listAccountingObjectExist.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode)
                            + hasExist);
                } else if (isNewAccountingObject && !accountingObjects.isEmpty() &&
                    accountingObjects.containsKey(accountingObjectCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode)
                            + hasDuplicate);
                } else if (isNewAccountingObject && accountingObjectCode.length() > MAX_LENGTH_CONTACT){
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNCC.AccountingObjectCode)
                            + invalid);
                } else {
                    accountingObject.setAccountingObjectCode(accountingObjectCode);
                }
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.AccountingObjectCode)
                        + messageRequired);
            }
            String accountingObjectName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectName);
            if (!Strings.isNullOrEmpty(accountingObjectName)) {
                accountingObject.setAccountingObjectName(accountingObjectName);
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectName,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.AccountingObjectName)
                        + messageRequired, isNewAccountingObject);
            }

            String contactTitle = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ContactTitle);
            if (!Strings.isNullOrEmpty(contactTitle)) {
                accountingObject.setContactTitle(contactTitle);
            }

            String departmentCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.DepartmentCode);
            if (!Strings.isNullOrEmpty(departmentCode)) {
                if (organizations.containsKey(departmentCode.toUpperCase())) {
                    accountingObject.setDepartmentId(organizations.get(departmentCode));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.DepartmentCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.DepartmentCode)
                            + invalid, isNewAccountingObject);
                }

            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.DepartmentCode,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.DepartmentCode)
                        + messageRequired, isNewAccountingObject);
            }
            String employeeBirthdayStr = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.EmployeeBirthday);
            LocalDate employeeBirthday = getCellValueLocalDate(row, requestData, ExcelConstant.KeyCodeDanhMucNV.EmployeeBirthday);
            if (!Strings.isNullOrEmpty(employeeBirthdayStr)) {
                if (employeeBirthday != null) {
                    accountingObject.setEmployeeBirthday(employeeBirthday);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.EmployeeBirthday,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.EmployeeBirthday)
                            + invalid, isNewAccountingObject);
                }
            }

            String contactSex = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ContactSex);
            if (!Strings.isNullOrEmpty(contactSex)) {
                if (gender.containsKey(contactSex)) {
                    accountingObject.setContactSex(gender.get(contactSex));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ContactSex,
                        requestData.getCodeWithExcelField()
                            .get(ExcelConstant.KeyCodeDanhMucNV.ContactSex) + invalid, isNewAccountingObject);
                }
            }


            String taxCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.TaxCode);
            if (!Strings.isNullOrEmpty(taxCode)) {
                if (Utils.checkTaxCode(taxCode)) {
                    accountingObject.setTaxCode(taxCode);
                } else if (taxCode.length() > MAX_CODE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.TaxCode)
                            + invalid, isNewAccountingObject);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.TaxCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.TaxCode)
                            + invalid, isNewAccountingObject);
                }
            }
            BigDecimal numberOfDependent = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucNV.NumberOfDependent);
            if (numberOfDependent != null) {
                accountingObject.setNumberOfDependent(numberOfDependent.intValue());
            }

            String identificationNo = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IdentificationNo);
            if (!Strings.isNullOrEmpty(identificationNo)) {
                if (identificationNo.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IdentificationNo,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.IdentificationNo)
                            + invalid, isNewAccountingObject);
                }
                accountingObject.setIdentificationNo(identificationNo);
            } else {

            }
            String issueDateStr = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IssueDate);
            LocalDate issueDate = getCellValueLocalDate(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IssueDate);
            if (!Strings.isNullOrEmpty(issueDateStr)) {
                if (issueDate != null) {
                    accountingObject.setIssueDate(issueDate);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IssueDate,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.IssueDate)
                            + invalid, isNewAccountingObject);
                }
            }
            String issueBy = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IssueBy);
            if (!Strings.isNullOrEmpty(issueBy)) {
                accountingObject.setIssueBy(issueBy);
            }

            BigDecimal agreementSalary = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AgreementSalary);
            if (agreementSalary != null) {
                accountingObject.setAgreementSalary(agreementSalary);
            }
            BigDecimal insuranceSalary = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucNV.InsuranceSalary);
            if (insuranceSalary != null) {
                accountingObject.setInsuranceSalary(insuranceSalary);
            }
            BigDecimal salaryCoefficient = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucNV.SalaryCoefficient);
            if (insuranceSalary != null) {
                accountingObject.setSalarycoEfficient(salaryCoefficient);
            }

            String objectType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ObjectType);
            if (!Strings.isNullOrEmpty(objectType)) {
                objectType = objectType.trim();
                if (objectTypes.containsKey(objectType)) {
                    accountingObject.setObjectType(objectTypes.get(objectType));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ObjectType,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.ObjectType)
                            + invalid, isNewAccountingObject);
                }

            }
            String isUnofficialStaff = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.IsUnofficialStaff);
            accountingObject.setIsUnOfficialStaff(!Strings.isNullOrEmpty(isUnofficialStaff) && isUnofficialStaff.equals("Có"));

            String accountingObjectAddress = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.AccountingObjectAddress);
            if (!Strings.isNullOrEmpty(accountingObjectAddress)) {
                accountingObject.setAccountingObjectAddress(accountingObjectAddress);
            }

            String contactHomeTel = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ContactHomeTel);
            if (!Strings.isNullOrEmpty(contactHomeTel)) {
                if (contactHomeTel.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.ContactHomeTel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.ContactHomeTel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setContactHomeTel(contactHomeTel);
                }
            } else {

            }
            String contactMobile = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.Tel);
            if (!Strings.isNullOrEmpty(contactMobile)) {
                if (contactMobile.length() > MAX_LENGTH_CONTACT) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.Tel,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.Tel)
                            + invalid, isNewAccountingObject);
                } else {
                    accountingObject.setTel(contactMobile);
                }
            } else {

            }

            String contactEmail = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucNV.Email);
            if (!Strings.isNullOrEmpty(contactEmail)) {
                List<String> checkedEmails = new ArrayList<>();
                List<String> lstEmail = Arrays.asList(contactEmail.split(","));
                for (String s : lstEmail) {
                    s = s.trim();
                    if (emailMatches(s)) {
                        checkedEmails.add(s);
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.Email,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.Email)
                                + invalid, isNewAccountingObject);
                    }
                }
                if (contactEmail.length() > MAX_EMAIL_WEBSITE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucNV.Email,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucNV.Email)
                            + invalid, isNewAccountingObject);
                } else {
                    String lastEmail = checkedEmails.stream().collect(Collectors.joining(","));
                    accountingObject.setEmail(lastEmail);
                }
            } else {

            }

            // start add object
            if (!requestData.getError()) {
                // add to list
                accountingObject.setCompanyId(securityDTO.getOrg());
                accountingObject.setisEmployee(true);
                accountingObject.setIsActive(true);
                accountingObjects.put(accountingObject.getAccountingObjectCode(), accountingObject);
            } else {
                dto.setError(true);
            }
            // end add object
        }
        if (dto.getError()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage(Constants.UpdateDataDTOMessages.FAIL);
        } else {
            switch (requestData.getImportType()) {
                case Constants.ImportType.importNew:
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                case Constants.ImportType.importUpdate:
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                case Constants.ImportType.importOverride:
                    accountingObjectRepository.deleteAllByCompanyIdInWithEmployee(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
                    accountingObjectRepository.saveAll(accountingObjects.values());
                    break;
                default:
                    break;
            }
            dto.setMessage(Constants.UpdateDataDTOMessages.SUCCESS);
        }
        workbook.close();
        return dto;
    }

    private UpdateDataDTO uploadVTHHExcel(Sheet sheet, UpdateDataDTO requestData, Workbook workbook, SecurityDTO securityDTO) throws IOException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Map<String, Integer> materialGoodsTypeList = new HashMap<>();
        materialGoodsTypeList.put("Vật tư hàng hóa", 0);
        materialGoodsTypeList.put("VTHH lắp ráp/tháo dỡ", 1);
        materialGoodsTypeList.put("Dịch vụ", 2);
        materialGoodsTypeList.put("Thành phẩm", 3);
        materialGoodsTypeList.put("Khác", 4);
        // vatTaxRates
        Map<String, BigDecimal> vatTaxRates = new HashMap<>();
        vatTaxRates.put("0%", BigDecimal.valueOf(0));
        vatTaxRates.put("5%", BigDecimal.valueOf(1));
        vatTaxRates.put("10%", BigDecimal.valueOf(2));
        vatTaxRates.put("KCT", BigDecimal.valueOf(3));
        vatTaxRates.put("KTT", BigDecimal.valueOf(4));

        Map<String, BigDecimal> careerGroupCodeS = new HashMap<>();
        careerGroupCodeS.put("Hàng hóa dịch vụ không chịu thuế GTGT hoặc hàng hóa dịch vụ áp dụng thuế suất 0%", BigDecimal.valueOf(1));
        careerGroupCodeS.put("Phân phối, cung cấp hàng hóa áp dụng thuế suất 1%", BigDecimal.valueOf(2));
        careerGroupCodeS.put("Dịch vụ, xây dựng không bao thầu nguyên vật liệu áp dụng thuế suất 5%", BigDecimal.valueOf(3));
        careerGroupCodeS.put("Sản xuất, vận tải, dịch vụ có gắn với hàng hóa, xây dựng có bao thầu nguyên vật liệu áp dụng thuế suất 3%", BigDecimal.valueOf(4));
        careerGroupCodeS.put("Hoạt động kinh doanh khác áp dụng thuế suất 2%", BigDecimal.valueOf(5));
        // SaleDiscountPolicys
        Map<String, Integer> saleDiscountPolicys = new HashMap<>();
        saleDiscountPolicys.put("Theo %", 0);
        saleDiscountPolicys.put("Theo số tiền", 1);
        saleDiscountPolicys.put("Theo đơn giá (Số tiền CK/đơn vị SL)", 2);
        String messageRequired = " bắt buộc nhập!";
        String hasExist = " đã tồn tại!";
        String hasDuplicate = " bị trùng!";
        String invalid = " không hợp lệ!";
        String isParentNode = "Không nhập vào Nhóm HHDV chịu thuế TTĐB tổng hợp";
        String invalidQuantity = "Khoảng số lượng không được giao nhau!";
        String quantityToLessThen = "Số lượng từ không được lớn hơn số lượng đến!";
        String discountResultByRateInvalid = "Tỷ lệ chiết khấu không được lớn hơn 100%!";
        String unitCovertEqualMainUnit = "Đơn vị tính chuyển đổi không được trùng đơn vị tính chính!";
        String unitCovertDuplicate = "Các đơn vị tính chuyển đổi không được trùng nhau!";
        String mainUnitNUll = "VTHH có đơn vị tính chuyển đổi thì đơn vị tính chính không được để trống!";
        String outOfTypeMaterialGoods = "VTHH không thuộc loại lắp ráp/ tháo dỡ thì không nhập cột này!";
        String materialAssemblyEmpty = "Bạn chưa nhập dữ liệu của VTHH lắp ráp/tháo dỡ chi tiết!";
        String outOfTypeMaterialGoodsType = "Mã VTHH lắp ráp/tháo dỡ bắt buộc nhập khi có tính chất VTHH lắp ráp/tháo dỡ";

        UpdateDataDTO dto = new UpdateDataDTO();
        dto.setError(false);
        // get Material Goods Code using (Nếu dùng hàm findAll 700record mất gần 5'), vẫn phải select * để lấy tất cả các trường liên quan
        Set<String> materialGoodsCodeUsing = new HashSet<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String materialGoodsCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode);
            if (!Strings.isNullOrEmpty(materialGoodsCode)) {
                materialGoodsCodeUsing.add(materialGoodsCode);
                String materialAssemblyCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode);
                if (!Strings.isNullOrEmpty(materialGoodsCode)) {
                    materialGoodsCodeUsing.add(materialAssemblyCode);
                }
            }
        }

        List<String> materialGoodsList = new ArrayList<>(materialGoodsCodeUsing);
        List<List<String>> smallerLists = Lists.partition(materialGoodsList, 2000);
        Map<String, MaterialGoods> materialGoodsMapExist = new HashMap<>();
        if (materialGoodsList.size() < 2000) {
            materialGoodsMapExist = requestData.getImportType() == Constants.ImportType.importOverride ?
                new HashMap<>() : materialGoodsRepository
                .findAllByCompanyIDAndMaterialGoodCode(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMVTHH), materialGoodsCodeUsing).stream()
                .collect(Collectors.toMap(MaterialGoods::getMaterialGoodsCode, x -> x));

        }

        if (materialGoodsList.size() > 2000) {
            for (List<String> s : smallerLists) {
                materialGoodsMapExist = !materialGoodsCodeUsing.isEmpty() && requestData.getImportType() == Constants.ImportType.importOverride ?
                    new HashMap<>() : materialGoodsRepository
                    .findAllByCompanyIDAndMaterialGoodCode(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                            Constants.SystemOption.TCKHAC_SDDMVTHH), s).stream()
                    .collect(Collectors.toMap(MaterialGoods::getMaterialGoodsCode, x -> x));
            }
        }

        // start init map value

//        Map<String, MaterialGoods> materialGoodsMapExist = requestData.getImportType() == Constants.ImportType.importOverride ?
//            new HashMap<>() : materialGoodsRepository
//            .findAllByCompanyIDAndMaterialGoodCode(systemOptionRepository
//                .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
//                    Constants.SystemOption.TCKHAC_SDDMVTHH), materialGoodsCodeUsing).stream()
//            .collect(Collectors.toMap(MaterialGoods::getMaterialGoodsCode, x -> x));

        Map<String, MaterialGoods> materialGoods1 = materialGoodsRepository
            .findAllByCompanyID(systemOptionRepository
                .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                    Constants.SystemOption.TCKHAC_SDDMVTHH)).stream()
            .collect(Collectors.toMap(MaterialGoods::getMaterialGoodsCode, x -> x));

        List<Unit> unitsRaw = unitRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());

        Map<String, Unit> units = unitsRaw.stream()
            .collect(Collectors.toMap(x -> x.getUnitName().toUpperCase(), x -> x));
        Map<UUID, Unit> unitsByUUID = unitsRaw.stream()
            .collect(Collectors.toMap(Unit::getId, x -> x));

        List<UUID> comIds = new ArrayList<>();
        comIds.add(currentUserLoginAndOrg.get().getOrg());
        // Loại VTHH
        Map<String, UUID> materialGoodsCategories = materialGoodsCategoryRepository.getAllMaterialGoodsCategoryByCompanyID(comIds).stream()
            .collect(Collectors.toMap(x -> x.getMaterialGoodsCategoryCode().toUpperCase(),
                MaterialGoodsCategory::getId));

        Map<String, UUID> repositories = repositoryRepository.findAllRepositoryCustom(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(),
                Constants.SystemOption.TCKHAC_SDDMKho)).stream()
            .collect(Collectors.toMap(x -> x.getRepositoryCode().toUpperCase(), Repository::getId));

        // is expenseAccounts - reponsitoryAccount
        List<String> mapExpenseAccount = accountListRepository.findAllByIsActive2(currentUserLoginAndOrg.get().getOrgGetData())
            .stream().map(AccountList::getAccountNumber).collect(Collectors.toList());
        // revenueAccount
        List<String> mapRevenueAccount = accountListRepository.findAllByIsActive1(currentUserLoginAndOrg.get().getOrgGetData())
            .stream().map(AccountList::getAccountNumber).collect(Collectors.toList());
        //
        List<String> mapCurrencies = currencyRepository.findByIsActiveTrue(systemOptionRepository
            .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrgGetData(),
                Constants.SystemOption.TCKHAC_SDDMVTHH)).stream().map(Currency::getCurrencyCode).collect(Collectors.toList());

        OrganizationUnit organizationUnitLogin = organizationUnitRepository.findByID(securityDTO.getOrg());
        Integer taxCalculationMethod = organizationUnitLogin.getTaxCalculationMethod();
        // materialGoodsSpecialTaxGroup taxCalculationMethod = Constants.TaxCalculationMethod.METHOD_ABATEMENT
        // materialGoodsSpecialTaxGroup taxCalculationMethod = Constants.TaxCalculationMethod.METHOD_ABATEMENT
        Map<String, UUID> mapCareerGroups = careerGroupRepository.findAll().stream()
            .collect(Collectors.toMap(CareerGroup::getCareerGroupCode, CareerGroup::getId));

        Map<String, UUID> mapCareerGrs = careerGroupRepository.findAll().stream()
            .collect(Collectors.toMap(CareerGroup::getCareerGroupName, CareerGroup::getId));
        // materialGoodsSpecialTaxGroup taxCalculationMethod = Constants.TaxCalculationMethod.METHOD_DIRECT
        Map<String, UUID> mapMaterialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupRepository
            .findAllMaterialGoodsSpecialTaxGroupkByCompanyID(currentUserLoginAndOrg.get().getOrgGetData())
            .stream().collect(Collectors.toMap(MaterialGoodsSpecialTaxGroup::getMaterialGoodsSpecialTaxGroupCode,
                MaterialGoodsSpecialTaxGroup::getId));

        Map<String, MaterialGoodsSpecialTaxGroup> mapMaterialGoodsSpecialTaxGroup1 = materialGoodsSpecialTaxGroupRepository
            .findAllMaterialGoodsSpecialTaxGroupkByCompanyID(currentUserLoginAndOrg.get().getOrgGetData())
            .stream().collect(Collectors.toMap(MaterialGoodsSpecialTaxGroup::getMaterialGoodsSpecialTaxGroupCode,
                n -> n));

        // end init map value
        Map<String, MaterialGoods> materialGoodsMap = new HashMap<>();
        Map<String, MaterialGoods> materialGoodsMapImport = new HashMap<>();
        Map<String, Integer> headers = new HashMap<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            requestData.setError(false);
            removeAllComment(row, requestData);
            boolean isBlankRow = true;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellValueString = ExcelUtils.getCellValueString(row.getCell(k));
                if (!Strings.isNullOrEmpty(cellValueString)) {
                    isBlankRow = false;
                    break;
                }
            }
            if (isBlankRow) {
                continue;
            }
            MaterialGoods materialGoods = new MaterialGoods(UUID.randomUUID());
            boolean isNewMaterialGoods = true;
//          boolean isContinue = false;
            String materialGoodsCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode);
            if (!Strings.isNullOrEmpty(materialGoodsCode)) {
                if (materialGoodsMapExist.containsKey(materialGoodsCode) && requestData.getImportType() == Constants.ImportType.importUpdate) {
                    MaterialGoods materialGoodsOld = materialGoodsMapExist.get(materialGoodsCode);
                    // Clean Object
                    // Gữi lại ID và code
                    materialGoods.setId(materialGoodsOld.getId());
                    materialGoods.setMaterialGoodsCode(materialGoodsOld.getMaterialGoodsCode());
                    isNewMaterialGoods = false;
                }
                if (materialGoodsMap.containsKey(materialGoodsCode)) {
                    materialGoods = materialGoodsMap.get(materialGoodsCode);
//                    isContinue = true;
                    isNewMaterialGoods = false;
                } else {
                    // UnitCode - Đơn vị tính chính
                    String unitCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCode);
                    if (!Strings.isNullOrEmpty(unitCode)) {
                        if (units.containsKey(unitCode.toUpperCase().trim())) {
                            materialGoods.setUnitID(units.get(unitCode.toUpperCase()).getId());
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCode)
                                    + invalid);
                        }
                    } else {
                    }
//                    else {
//                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCode,
//                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCode)
//                                        + messageRequired);
//                    }
                    try {
                        materialGoodsMapImport.get(materialGoodsCode).getUnitID();

                    } catch (NullPointerException e) {
                    }
                    // MaterialGoodsType - Tính chất
                    String materialGoodsType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsType);
                    if (!Strings.isNullOrEmpty(materialGoodsType)) {
                        if (materialGoodsTypeList.containsKey(materialGoodsType.trim())) {
                            materialGoods.setMaterialGoodsType(materialGoodsTypeList.get(materialGoodsType));
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsType,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsType)
                                    + invalid);
                        }
                    } else {
                        materialGoods.setMaterialGoodsType(materialGoodsTypeList.get("Vật tư hàng hóa"));
                    }
                }
                // DISCOUNT
                // Só lượng từ
                BigDecimal quantityFrom = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityFrom);
                // Số lượng đến
                BigDecimal quantityTo = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityTo);
                // Loại chiết khấu
                String discountType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountType);
                // % hoặc số tiền chiết khấu
                BigDecimal discountResult = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountResult);

                if (quantityFrom != null || quantityTo != null || !Strings.isNullOrEmpty(discountType) || discountResult != null) {
                    if (quantityFrom == null) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityFrom,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.QuantityFrom)
                                + messageRequired);
                    }
                    if (quantityTo == null) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityTo,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.QuantityTo)
                                + messageRequired);
                    }
                    if (Strings.isNullOrEmpty(discountType)) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountType,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.DiscountType)
                                + messageRequired);
                    }
                    if (discountResult == null) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountResult,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.DiscountResult)
                                + messageRequired);
                    }
                    // Nếu  tất cả 4 !null
                    if (quantityFrom != null && quantityTo != null && discountType != null && discountResult != null) {
                        SaleDiscountPolicy saleDiscountPolicy = new SaleDiscountPolicy();
                        // Nếu loại chiết khấu thuộc 3 loại chiết khấu sds
                        if (saleDiscountPolicys.containsKey(discountType.trim())) {
                            // Set loại chiết khấu cho chiết khấu bán hàng
                            saleDiscountPolicy.setDiscountType(saleDiscountPolicys.get(discountType));
                            // Nếu số lượng từ > số lượng đến
                            if (quantityFrom.compareTo(quantityTo) > 0) {
                                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityTo, quantityToLessThen);
                            } else {
                                Set<SaleDiscountPolicy> saleDiscountPolicies = materialGoods.getSaleDiscountPolicy();
//                                List<BigDecimal> quantitiesToOld = saleDiscountPolicies.stream()
//                                    .map(SaleDiscountPolicy::getQuantityTo).collect(Collectors.toList());
                                boolean isError = false;
                                for (SaleDiscountPolicy sdp : saleDiscountPolicies) {
                                    if ((quantityFrom.compareTo(sdp.getQuantityFrom()) >= 0 && quantityFrom.compareTo(sdp.getQuantityTo()) <= 0)) {
                                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityFrom, invalidQuantity);
                                        isError = true;
                                    } else if (quantityTo.compareTo(sdp.getQuantityFrom()) >= 0 && quantityTo.compareTo(sdp.getQuantityTo()) <= 0) {
                                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityTo, invalidQuantity);
                                        isError = true;
                                    }
                                }

                                if (!isError) {
                                    if (materialGoods.getSaleDiscountPolicy() != null)
                                        saleDiscountPolicy.setQuantityFrom(quantityFrom);
                                    saleDiscountPolicy.setQuantityTo(quantityTo);
                                    if (discountType.equals("Theo %") && discountResult.compareTo(BigDecimal.valueOf(100)) > 0) {
                                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountResult, discountResultByRateInvalid);
                                    } else {
                                        saleDiscountPolicy.setDiscountResult(discountResult);
                                    }
                                    saleDiscountPolicy.setMaterialGoodsID(materialGoods.getId());
                                    materialGoods.addSaleDiscountPolicy(saleDiscountPolicy);
                                }

                            }
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.DiscountType,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.DiscountType)
                                    + invalid);
                        }

                    }
                }

                // Đơn giá mua
                String currencyID = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.CurrencyID);
                String unitCodePurchasePrice = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodePurchasePrice);
                BigDecimal unitPricePurchasePrice = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitPricePurchasePrice);
                if (!Strings.isNullOrEmpty(currencyID) || !Strings.isNullOrEmpty(unitCodePurchasePrice) || unitPricePurchasePrice != null) {
                    MaterialGoodsPurchasePrice materialGoodsPurchasePrice = new MaterialGoodsPurchasePrice();
                    if (Strings.isNullOrEmpty(currencyID)) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.CurrencyID,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.CurrencyID)
                                + messageRequired);
                    } else {
                        if (mapCurrencies.contains(currencyID)) {
                            materialGoodsPurchasePrice.setCurrencyID(currencyID);
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.CurrencyID,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.CurrencyID)
                                    + invalid);
                        }
                    }
                    if (Strings.isNullOrEmpty(unitCodePurchasePrice)) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodePurchasePrice,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodePurchasePrice)
                                + messageRequired);
                    } else {
                        if (unitCodePurchasePrice == null) {
                            System.out.println("this is dau tom");
                        }
                        if (units.containsKey(unitCodePurchasePrice.toUpperCase().trim())) {
                            try {
                                materialGoodsPurchasePrice.setUnitID(units.get(unitCodePurchasePrice.toUpperCase().trim()).getId());

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodePurchasePrice,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodePurchasePrice)
                                    + invalid);
                        }
                    }
                    if (unitPricePurchasePrice == null || unitPricePurchasePrice.toString().isEmpty()) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitPricePurchasePrice,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitPricePurchasePrice)
                                + messageRequired);
                    } else {
                        materialGoodsPurchasePrice.setUnitPrice(unitPricePurchasePrice);
                    }
                    materialGoodsPurchasePrice.setMaterialGoodsID(materialGoods.getId());
                    materialGoods.addMaterialGoodsPurchasePrice(materialGoodsPurchasePrice);
                }
                // Đơn vị tính chuyển đổi
                String unitCodeConvert = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert);

                Object objConvertRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ConvertRate);
                String formula = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Formula);
                // Đơn vị tính chuyển đổi - nếu 1 trong 3 cell !null thì 2 cell còn lại phải nhập
                if (!Strings.isNullOrEmpty(unitCodeConvert) || !Strings.isNullOrEmpty(formula) || !objConvertRate.toString().isEmpty()) {
                    if (unitCodeConvert == null) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert)
                                + invalid);
                    }
                    if (objConvertRate == null || objConvertRate.toString().isEmpty()) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ConvertRate,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.ConvertRate)
                                + invalid);
                    }
                    if (formula == null) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Formula,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.Formula)
                                + invalid);
                    }
                }
                if (materialGoodsMap.containsKey(materialGoodsCode)) {
                    // Nếu mã vật tư hàng hóa đã được thêm trước đó trong file excel thì k đc trùng đơn vị tính chuyển đổi
                    try {
                        if (Boolean.TRUE.equals(materialGoodsMap.get(materialGoodsCode).getMaterialGoodsConvertUnits()
                            .stream().map(MaterialGoodsConvertUnit::getUnitID).collect(Collectors.toList()).contains(units.get(unitCodeConvert.toUpperCase()).getId()))) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert)
                                    + hasDuplicate);
                        }
                    } catch (NullPointerException e) {

                    }
                }

                if (objConvertRate instanceof Number) {
                    BigDecimal convertRate = new BigDecimal(objConvertRate.toString());
                    BigDecimal fixedSalePriceConvert = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.FixedSalePriceConvert);
                    BigDecimal salePriceConvert1 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePriceConvert1);
                    BigDecimal salePriceConvert2 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePriceConvert2);
                    BigDecimal salePriceConvert3 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePriceConvert3);
                    if (!Strings.isNullOrEmpty(unitCodeConvert) || convertRate != null || !Strings.isNullOrEmpty(formula)) {
                        MaterialGoodsConvertUnit materialGoodsConvertUnit = new MaterialGoodsConvertUnit();
                        if (!materialGoodsMapImport.containsKey(materialGoodsCode)) {
                            if (materialGoods.getUnitID() == null) {
                                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCode, mainUnitNUll);
                            }
                        }

                        if (Strings.isNullOrEmpty(unitCodeConvert)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert)
                                    + messageRequired);

                        }
                        if (Strings.isNullOrEmpty(formula)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Formula,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.Formula)
                                    + messageRequired);
                        } else {
                            try {
                                if (units.containsKey(unitCodeConvert.toUpperCase().trim())) {
                                    if (units.get(unitCodeConvert.toUpperCase()).getId().equals(materialGoods.getUnitID())) {
                                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                            unitCovertEqualMainUnit);
                                    } else {
                                        if (materialGoods.getMaterialGoodsConvertUnits()
                                            .stream().map(MaterialGoodsConvertUnit::getUnitID).collect(Collectors.toList()).contains(materialGoodsConvertUnit.getUnitID())) {
                                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                                unitCovertDuplicate);
                                            if (materialGoodsMap.containsKey(materialGoodsCode)) {
                                                // Nếu mã vật tư hàng hóa đã được thêm trước đó trong file excel thì k đc trùng đơn vị tính chuyển đổi
                                                if (materialGoodsMap.get(materialGoodsCode).getMaterialGoodsConvertUnits().toString().toUpperCase().equals(unitCodeConvert.toUpperCase())) {
                                                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                                        hasDuplicate);
                                                }
                                            }
                                        } else {
                                            materialGoodsConvertUnit.setUnitID(units.get(unitCodeConvert.toUpperCase()).getId());
                                        }
                                    }
                                } else {
                                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert,
                                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeConvert)
                                            + invalid);
                                }
                            } catch (NullPointerException e) {
                                System.out.println("abc");
                                e.printStackTrace();
                            }
                        }
                        if (convertRate == null) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ConvertRate,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.ConvertRate)
                                    + messageRequired);
                        } else {
                            materialGoodsConvertUnit.setConvertRate(convertRate);
                        }
                        if (!Strings.isNullOrEmpty(formula)) {
                            if (Arrays.asList("/", "*").contains(formula)) {
                                materialGoodsConvertUnit.setFormula(formula);
                            } else {
                                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Formula,
                                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.Formula)
                                        + invalid);
                            }
                        } else {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Formula,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.Formula)
                                    + messageRequired);
                        }

                        materialGoodsConvertUnit.setFixedSalePrice(fixedSalePriceConvert != null ? fixedSalePriceConvert : BigDecimal.ZERO);
                        materialGoodsConvertUnit.setSalePrice1(salePriceConvert1 != null ? salePriceConvert1 : BigDecimal.ZERO);
                        materialGoodsConvertUnit.setSalePrice2(salePriceConvert2 != null ? salePriceConvert2 : BigDecimal.ZERO);
                        materialGoodsConvertUnit.setSalePrice3(salePriceConvert3 != null ? salePriceConvert3 : BigDecimal.ZERO);
                        if (materialGoods.getUnitID() != null && materialGoodsConvertUnit.getUnitID() != null &&
                            materialGoodsConvertUnit.getConvertRate() != null &&
                            materialGoodsConvertUnit.getFormula() != null) {
                            if (materialGoods.getMaterialGoodsConvertUnits().size() > 0) {
                                materialGoodsConvertUnit.setOrderNumber(Collections.max(materialGoods.getMaterialGoodsConvertUnits()
                                    .stream().map(MaterialGoodsConvertUnit::getOrderNumber)
                                    .collect(Collectors.toList())) + 1);
                            } else {
                                materialGoodsConvertUnit.setOrderNumber(1);
                            }
                            if (materialGoodsConvertUnit.getFormula().equals("*")) {
                                materialGoodsConvertUnit.setDescription("1 " + unitsByUUID.get(materialGoodsConvertUnit.getUnitID()).getUnitName()
                                    + " = " + materialGoodsConvertUnit.getConvertRate() + " "
                                    + unitsByUUID.get(materialGoods.getUnitID()).getUnitName());
                            } else {
                                materialGoodsConvertUnit.setDescription("1 " + unitsByUUID.get(materialGoodsConvertUnit.getUnitID()).getUnitName()
                                    + " = 1/" + materialGoodsConvertUnit.getConvertRate() + " "
                                    + unitsByUUID.get(materialGoods.getUnitID()).getUnitName());
                            }
                            materialGoodsConvertUnit.setMaterialGoodsID(materialGoods.getId());
                            materialGoods.addMaterialGoodsConvertUnit(materialGoodsConvertUnit);
                        }
                    }
                }

                // MaterialAssembly - VTHH lắp ráp/tháo dỡ
                // Mã VTHH lắp ráp/tháo dỡ
                String materialAssemblyCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode);
                // Đơn vị tính VTHH lắp ráp/tháo dỡ
                String unitCodeAssembly = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeAssembly);
                String materialGoodsType = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsType);
                BigDecimal quantityAssembly = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityAssembly);
                BigDecimal unitPriceAssembly = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitPriceAssembly);
                BigDecimal amountAssembly = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.AmountAssembly);

//                if (materialGoods.getMaterialGoodsType().equals(materialGoodsTypeList.get("VTHH lắp ráp/tháo dỡ")) &&
//                    Strings.isNullOrEmpty(materialAssemblyCode) &&
//                    Strings.isNullOrEmpty(unitCodeAssembly) && quantityAssembly == null &&
//                    unitPriceAssembly == null && amountAssembly == null) {
//                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsType,
//                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
//                                + outOfTypeMaterialGoodsType);    .
//                    }
                if (!Strings.isNullOrEmpty(materialAssemblyCode) ||
                    !Strings.isNullOrEmpty(unitCodeAssembly) || quantityAssembly != null ||
                    unitPriceAssembly != null || amountAssembly != null) {
                    if (materialGoods.getMaterialGoodsType().equals(materialGoodsTypeList.get("VTHH lắp ráp/tháo dỡ"))) {
                        MaterialGoodsAssembly materialGoodsAssembly = new MaterialGoodsAssembly();
                        if (Strings.isNullOrEmpty(materialAssemblyCode) &&
                            (materialGoods.getMaterialGoodsAssembly() == null || materialGoods.getMaterialGoodsAssembly().isEmpty())) {
                            // Nếu là VTHH lắp ráp mà là dòng đầu của VTHH đó, bắt buộc phải có VTHH lắp rap
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                    + materialAssemblyEmpty);
                        } else if (Strings.isNullOrEmpty(materialAssemblyCode) && !materialGoodsMapImport.containsKey(materialGoodsCode)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                    + messageRequired);
                        } else if (!Strings.isNullOrEmpty(materialAssemblyCode) && !materialGoodsMap.containsKey(materialAssemblyCode) && !materialGoodsMapExist.containsKey(materialAssemblyCode)) {
                            // Nếu mã VTHH lắp ráp không có trong VTHH đã được thêm từ các dòng trên hoặc trong PM
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                    + invalid);
                        } else if (requestData.getImportType().equals(Constants.ImportType.importOverride) &&
                            !materialGoodsMapImport.containsKey(materialAssemblyCode)) {
                            // Nếu là import ghi đè thì kiểm tra mã VTHH phải đk thêm từ các dòng trên
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                    + invalid);
                        } else if (Strings.isNullOrEmpty(materialAssemblyCode)) {
                            if (!Strings.isNullOrEmpty(unitCodeAssembly) || quantityAssembly != null ||
                                unitPriceAssembly != null || amountAssembly != null) {
                                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                        + messageRequired);
                            }

                        } else {
                            if (materialGoodsMapExist.containsKey(materialAssemblyCode)) {
                                materialGoodsAssembly.setMaterialAssemblyID(materialGoodsMapExist.get(materialAssemblyCode).getId());
                                materialGoodsAssembly.setMaterialAssemblyDescription(materialGoodsMapExist.get(materialAssemblyCode).getMaterialGoodsName());
                            } else {
                                materialGoodsAssembly.setMaterialAssemblyID(materialGoodsMap.get(materialAssemblyCode).getId());
                                materialGoodsAssembly.setMaterialAssemblyDescription(materialGoodsMap.get(materialAssemblyCode).getMaterialGoodsName());
                            }
                        }
                        if (!Strings.isNullOrEmpty(unitCodeAssembly)) {
                            materialGoodsAssembly.setUnit(units.get(unitCodeAssembly.toUpperCase()));
                        } /*else if (!units.containsKey(unitCodeAssembly)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeAssembly,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeAssembly)
                                    + invalid);
                        } else {
                            materialGoodsAssembly.setUnit(units.get(unitCodeAssembly.toUpperCase()));
                        }*/
                        materialGoodsAssembly.setQuantity(quantityAssembly != null ? quantityAssembly : BigDecimal.ZERO);
                        materialGoodsAssembly.setUnitPrice(unitPriceAssembly != null ? unitPriceAssembly : BigDecimal.ZERO);
                        materialGoodsAssembly.setTotalAmount(amountAssembly != null ? amountAssembly : BigDecimal.ZERO);
                        /// nếu import thêm mới thì random id cho materialGoodsAssembly,
                        // nếu import update thì k cần thêm id, chỉ cần filter những mã vthh lắp ráp tháo dỡ theo mã vthh
                        // nếu trùng mã lắp ráp tháo dỡ thì chỉ update các trường còn lại, nếu k trùng thì add vào list
                        if (requestData.getImportType().equals(Constants.ImportType.importNew) || requestData.getImportType().equals(Constants.ImportType.importOverride)) {
                            //UUID materialGoodsAssemblyId = UUID.randomUUID();
                            //materialGoodsAssembly.setId(materialGoodsAssemblyId);
                            materialGoodsAssembly.setMaterialGoodsID(materialGoods.getId());
                            materialGoods.addMaterialGoodsAssembly(materialGoodsAssembly);
                        } else if (requestData.getImportType().equals(Constants.ImportType.importUpdate)) {
                            if (materialGoods1.containsKey(materialAssemblyCode)) {
                                materialGoods.addMaterialGoodsAssembly(materialGoodsAssembly);
                            } else if (materialGoodsMap.containsKey(materialAssemblyCode)) {
                                materialGoods.addMaterialGoodsAssembly(materialGoodsAssembly);
                            }
                        }
                    } else {
                        // Nếu k phải là VTHH lắp ráp mã nhập các trường VTHH lắp ráp
                        if (!Strings.isNullOrEmpty(materialAssemblyCode)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
                                    + outOfTypeMaterialGoods);
                        }
                        if (!Strings.isNullOrEmpty(unitCodeAssembly)) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeAssembly,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitCodeAssembly)
                                    + outOfTypeMaterialGoods);
                        }
                        if (quantityAssembly != null) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.QuantityAssembly,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.QuantityAssembly)
                                    + outOfTypeMaterialGoods);
                        }
                        if (unitPriceAssembly != null) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.UnitPriceAssembly,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.UnitPriceAssembly)
                                    + outOfTypeMaterialGoods);
                        }
                        if (amountAssembly != null) {
                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.AmountAssembly,
                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.AmountAssembly)
                                    + outOfTypeMaterialGoods);
                        }
                    }
                } else if (!Strings.isNullOrEmpty(materialGoodsType) && materialGoodsType.equals("VTHH lắp ráp/tháo dỡ")) {
                    if (Strings.isNullOrEmpty(materialAssemblyCode)) {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode, outOfTypeMaterialGoodsType);
                    }
                }
//                else
//                    if (materialGoodsMapImport.containsKey(materialGoodsCode)) {
//                        if (Strings.isNullOrEmpty(materialAssemblyCode) && materialGoodsMapImport.get(materialGoodsCode).getMaterialGoodsType().equals(materialGoodsTypeList.get("VTHH lắp ráp/tháo dỡ"))) {
//                            if ()
//                            setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode,
//                                requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialAssemblyCode)
//                                    + messageRequired);
//                        }
//                    }

                // continue
/*                if (isContinue) {
                    continue;
                }*/

                if (materialGoodsMapExist.containsKey(materialGoodsCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode)
                            + hasExist, isNewMaterialGoods);
                } else if (!materialGoodsMap.isEmpty() && materialGoodsMap.containsKey(materialGoodsCode)) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode)
                            + hasDuplicate, isNewMaterialGoods);
                } else if (materialGoodsCode.length() > MAX_CODE) {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode)
                            + invalid, isNewMaterialGoods);
                } else {
                    materialGoods.setMaterialGoodsCode(materialGoodsCode);
                }
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCode)
                        + messageRequired, isNewMaterialGoods);
            }

            String materialGoodsName = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsName);
            if (!Strings.isNullOrEmpty(materialGoodsName)) {
                materialGoods.setMaterialGoodsName(materialGoodsName);
            } else if (materialGoodsMapImport.containsKey(materialGoodsCode) && Strings.isNullOrEmpty(materialGoodsName)) {
                // Nếu cùng mã vthh đã đc thêm ở dòng trước đó, thì k báo lỗi cell tên cột vthh nữa
            } else {
                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsName,
                    requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsName)
                        + messageRequired, isNewMaterialGoods);
            }
            // MaterialGoodsCategoryCode - Loại VTHH
            String materialGoodsCategoryCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCategoryCode);
            if (!Strings.isNullOrEmpty(materialGoodsCategoryCode)) {
                if (materialGoodsCategories.containsKey(materialGoodsCategoryCode.toUpperCase().trim())) {
                    materialGoods.setMaterialGoodsCategoryID(materialGoodsCategories.get(materialGoodsCategoryCode.toUpperCase()));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCategoryCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCategoryCode)
                            + invalid, isNewMaterialGoods);
                }
            } else if (Strings.isNullOrEmpty(materialGoodsCategoryCode) && materialGoodsMapImport.containsKey(materialGoodsCode)) {

            }
//            else {
//                setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCategoryCode,
//                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsCategoryCode)
//                                + messageRequired);
//            }
            // Warranty - Thời hạn bảo hành
            String warranty = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.Warranty);
            if (!Strings.isNullOrEmpty(warranty)) {
                materialGoods.setWarranty(warranty);
            }
            // MinimumStock - Số lượng tồn tối thiểu
            BigDecimal minimumStock = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MinimumStock);
            if (minimumStock != null) {
                materialGoods.setMinimumStock(minimumStock);
            } else {
                materialGoods.setMinimumStock(BigDecimal.ZERO);
            }
            // ItemSource - Xuất xứ hàng hóa
            String itemSource = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ItemSource);
            if (!Strings.isNullOrEmpty(itemSource)) {
                materialGoods.setItemSource(itemSource);
            }
            // RepositoryCode - Kho
            String repositoryCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.RepositoryCode);
            if (!Strings.isNullOrEmpty(repositoryCode)) {
                if (repositories.containsKey(repositoryCode.toUpperCase().trim())) {
                    materialGoods.setRepositoryID(repositories.get(repositoryCode.toUpperCase()));
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.RepositoryCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.RepositoryCode)
                            + invalid, isNewMaterialGoods);
                }
            }

            // ReponsitoryAccount - Tài khoản kho
            String reponsitoryAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ReponsitoryAccount);
            if (!Strings.isNullOrEmpty(reponsitoryAccount)) {
                if (mapExpenseAccount.contains(reponsitoryAccount)) {
                    materialGoods.setReponsitoryAccount(reponsitoryAccount);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ReponsitoryAccount,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.ReponsitoryAccount)
                            + invalid, isNewMaterialGoods);
                }
            }
            // ExpenseAccount - Tài khoản chi phí
            String expenseAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ExpenseAccount);
            if (!Strings.isNullOrEmpty(expenseAccount)) {
                if (mapExpenseAccount.contains(expenseAccount)) {
                    materialGoods.setExpenseAccount(expenseAccount);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ExpenseAccount,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.ExpenseAccount)
                            + invalid, isNewMaterialGoods);
                }
            }
            // RevenueAccount - Tài khoản chi phí
            String revenueAccount = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.RevenueAccount);
            if (!Strings.isNullOrEmpty(revenueAccount)) {
                if (mapRevenueAccount.contains(revenueAccount)) {
                    materialGoods.setRevenueAccount(revenueAccount);
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.RevenueAccount,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.RevenueAccount)
                            + invalid, isNewMaterialGoods);
                }
            }
            // VATTaxRate - Thuế suất thuế GTGT
            Object vatTaxRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.VATTaxRate);
            if (taxCalculationMethod == Constants.TaxCalculationMethod.METHOD_DIRECT) {
                // Nếu phương pháp tính thuế là trực tiếp thì không import thuế suất thuế GTGT
            } else if (vatTaxRate != null && !vatTaxRate.equals("")) {
                if (vatTaxRate instanceof String) {
                    if (vatTaxRates.containsKey(vatTaxRate)) {
                        materialGoods.setVatTaxRate(vatTaxRates.get(vatTaxRate));
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.VATTaxRate,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.VATTaxRate)
                                + invalid, isNewMaterialGoods);
                    }
                } else if (vatTaxRate instanceof Double) {
                    if (((Double) vatTaxRate).doubleValue() == 0) {
                        materialGoods.setVatTaxRate(vatTaxRates.get("0%"));
                    } else if (((Double) vatTaxRate).compareTo(0.05) == 0) {
                        materialGoods.setVatTaxRate(vatTaxRates.get("5%"));
                    } else if (((Double) vatTaxRate).compareTo(0.1) == 0) {
                        materialGoods.setVatTaxRate(vatTaxRates.get("10%"));
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.VATTaxRate,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.VATTaxRate)
                                + invalid, isNewMaterialGoods);
                    }
                }
            }


            /*BigDecimal importTaxRate = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ImportTaxRate);
            if (importTaxRate != null) {
                materialGoods.setImportTaxRate(importTaxRate);
            }*/

            //^[0-9]{1,3}%$
            // ImportTaxRate - Thuế suất thuế Nhập khẩu
            Object importTaxRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ImportTaxRate);
            if (importTaxRate != null) {
                BigDecimal iTaxRate = convertRate(importTaxRate, row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ImportTaxRate);
                if (iTaxRate != null) {
                    materialGoods.setImportTaxRate(iTaxRate);
                } else if (iTaxRate == null && materialGoodsMap.containsKey(materialGoodsCode)) {

                }
            } else {
                try {
                    materialGoods.setImportTaxRate((BigDecimal) importTaxRate);
                } catch (NullPointerException e) {

                }
            }

            // ExportTaxRate - Thuế suất thuế Xuất khẩu
            Object exportTaxRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ExportTaxRate);
            if (exportTaxRate != null) {
                BigDecimal eTaxRate = convertRate(exportTaxRate, row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.ExportTaxRate);
                if (eTaxRate != null) {
                    materialGoods.setExportTaxRate(eTaxRate);
                } else if (eTaxRate == null && materialGoodsMap.containsKey(materialGoodsCode)) {

                }
            } else {
                try {
                    materialGoods.setExportTaxRate((BigDecimal) exportTaxRate);
                } catch (NullPointerException e) {

                }
            }

            // SaleDiscountRate - Tỷ lệ CKBH
            Object saleDiscountRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SaleDiscountRate);
            if (saleDiscountRate != null) {
                BigDecimal sDiscountRate = convertRate(saleDiscountRate, row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SaleDiscountRate);
                if (sDiscountRate != null) {
                    materialGoods.setSaleDiscountRate(sDiscountRate);
                } else if (sDiscountRate == null && materialGoodsMap.containsKey(materialGoodsCode)) {

                }
            } else {
                try {
                    materialGoods.setSaleDiscountRate((BigDecimal) saleDiscountRate);
                } catch (NullPointerException e) {

                }
            }

            // PurchaseDiscountRate - Tỷ lệ CKMH
            Object purchaseDiscountRate = getCellValue(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.PurchaseDiscountRate);
            if (purchaseDiscountRate != null) {
                BigDecimal pDiscountRate = convertRate(saleDiscountRate, row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.PurchaseDiscountRate);
                if (pDiscountRate != null) {
                    materialGoods.setPurchaseDiscountRate(pDiscountRate);
                } else if (pDiscountRate == null && materialGoodsMap.containsKey(materialGoodsCode)) {

                }
            } else {
                try {
                    materialGoods.setPurchaseDiscountRate((BigDecimal) purchaseDiscountRate);
                } catch (NullPointerException e) {

                }
            }

            // MaterialGoodsGSTCode - Nhóm HHDV chịu thuế TTĐB
            String materialGoodsGSTCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsGSTCode);
            if (!Strings.isNullOrEmpty(materialGoodsGSTCode)) {
                if (mapMaterialGoodsSpecialTaxGroup.containsKey(materialGoodsGSTCode.trim())) {
                    if (Boolean.FALSE.equals(mapMaterialGoodsSpecialTaxGroup1.get(materialGoodsGSTCode).isIsParentNode())) {
                        materialGoods.setMaterialGoodsGSTID(mapMaterialGoodsSpecialTaxGroup.get(materialGoodsGSTCode));
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsGSTCode,
                            isParentNode, true);
                    }
                } else {
                    setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsGSTCode,
                        requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.MaterialGoodsGSTCode)
                            + invalid, isNewMaterialGoods);
                }
            }

            // MaterialGoodsGSTCode - Nhóm HHDV chịu thuế TTĐB
            String careerGroupCode = getCellValueString(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.CareerGroupCode);
            if (!Strings.isNullOrEmpty(careerGroupCode)) {
                if (taxCalculationMethod == Constants.TaxCalculationMethod.METHOD_DIRECT) {
                    if (mapCareerGroups.containsKey(careerGroupCode.trim())) {
                        materialGoods.setCareerGroupID(mapCareerGroups.get(careerGroupCode));
                        dto.setMessage("METHOD_DIRECT");
                    } else if (careerGroupCodeS.containsKey(careerGroupCode)) {
                        materialGoods.setCareerGroupID(mapCareerGrs.get(careerGroupCode));
                        dto.setMessage("METHOD_DIRECT");
                    } else {
                        setComment(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.CareerGroupCode,
                            requestData.getCodeWithExcelField().get(ExcelConstant.KeyCodeDanhMucVTHH.CareerGroupCode)
                                + invalid, isNewMaterialGoods);
                    }
                } else if (taxCalculationMethod == Constants.TaxCalculationMethod.METHOD_ABATEMENT) {
                    dto.setMessage("METHOD_ABATEMENT");
                }
            }

            // FixedSalePrice - Giá bán cố định
            BigDecimal fixedSalePrice = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.FixedSalePrice);
            if (fixedSalePrice != null) {
                if (!materialGoodsMap.containsKey(materialGoodsCode)) {
                    materialGoods.setFixedSalePrice(fixedSalePrice);
                }
            }
            // SalePrice1 - Giá bán 1
            BigDecimal salePrice1 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePrice1);
            if (salePrice1 != null) {
                if (!materialGoodsMap.containsKey(materialGoodsCode)) {
                    materialGoods.setSalePrice1(salePrice1);
                }
            }
            // SalePrice2 - Giá bán 2
            BigDecimal salePrice2 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePrice2);
            if (salePrice2 != null) {
                if (!materialGoodsMap.containsKey(materialGoodsCode)) {
                    materialGoods.setSalePrice2(salePrice2);
                }
            }
            // SalePrice3 - Giá bán 3
            BigDecimal salePrice3 = getCellValueBigDecimal(row, requestData, ExcelConstant.KeyCodeDanhMucVTHH.SalePrice3);
            if (salePrice3 != null) {
                if (!materialGoodsMap.containsKey(materialGoodsCode)) {
                    materialGoods.setSalePrice3(salePrice3);
                }
            }

            // start add object
            materialGoodsMapImport.put(materialGoods.getMaterialGoodsCode(), materialGoods);
            if (!requestData.getError()) {
                // add to list
                materialGoods.setCompanyID(securityDTO.getOrg());
                materialGoods.setIsActive(true);
                materialGoods.setIsSecurity(false);
                materialGoodsMap.put(materialGoods.getMaterialGoodsCode(), materialGoods);
            } else {
                dto.setError(true);
            }
            // end add object
        }
        if (dto.getError()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage("ERROR");
        } else {
            if (Arrays.asList("METHOD_ABATEMENT", "METHOD_DIRECT").contains(dto.getMessage())) {
                materialGoodsRepository.rollBack();
                dto.setResult(materialGoodsMap.values());
            } else {
                switch (requestData.getImportType()) {
                    case Constants.ImportType.importNew:
                        materialGoodsRepository.saveAll(materialGoodsMap.values());
                        break;
                    case Constants.ImportType.importUpdate:
                        materialGoodsRepository.saveAll(materialGoodsMap.values());
                        break;
                    case Constants.ImportType.importOverride:
                        materialGoodsRepository.deleteAllByCompanyIdIn(systemOptionRepository
                            .getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
                        materialGoodsRepository.saveAll(materialGoodsMap.values());
                        break;
                    default:
                        break;
                }
                dto.setMessage(Constants.UpdateDataDTOMessages.SUCCESS);
            }
        }
        workbook.close();
        return dto;
    }

    public UpdateDataDTO checkReference(String type) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        switch (type) {
            case Constants.ExcelDanhMucType.KH:
                updateDataDTO.setErrors(accountingObjectRepository.getAccountingObjectHasReference(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong),
                    Arrays.asList(1, 2 , 3)
                ));
                return updateDataDTO;
            case Constants.ExcelDanhMucType.NCC:
                updateDataDTO.setErrors(accountingObjectRepository.getAccountingObjectHasReference(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong),
                    Arrays.asList(0, 2, 3)
                ));
                return updateDataDTO;
            case Constants.ExcelDanhMucType.NV:
                updateDataDTO.setErrors(accountingObjectRepository.getAccountingObjectHasReferenceForEmployee(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong)
                ));
                return updateDataDTO;
            case Constants.ExcelDanhMucType.VTHH:
                updateDataDTO.setErrors(materialGoodsRepository.getMaterialGoodsCodeHasReference(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(),
                                Constants.SystemOption.TCKHAC_SDDMVTHH)));
                return updateDataDTO;
        }
        updateDataDTO.setError(false);
        return updateDataDTO;
    }
    public byte[] getFileTemp(String type) {
        try {
            File currentDirectory = new File(new File("").getAbsolutePath());
            String reportUrl = "";
            switch (type) {
                case Constants.ExcelDanhMucType.KH:
                    reportUrl = "/report/Import_KhachHang.xlsx";
                    break;
                case Constants.ExcelDanhMucType.NCC:
                    reportUrl = "/report/Import_NhaCungCap.xlsx";
                    break;
                case Constants.ExcelDanhMucType.NV:
                    reportUrl = "/report/Import_NhanVien.xlsx";
                    break;
                case Constants.ExcelDanhMucType.VTHH:
                    reportUrl = "/report/Import_VTHH.xlsx";
                    break;
                default:
                    break;
            }
            File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + reportUrl);
            return Files.readAllBytes(reportFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public Boolean acceptedMaterialGoodsData(List<MaterialGoods> materialGoodsData, Integer type) {
        switch (type) {
            case Constants.ImportType.importNew:
            case Constants.ImportType.importUpdate:
                // materialGoodsRepository.saveAll(materialGoodsData);
                materialGoodsRepository.insertBulk(materialGoodsData);
                return true;
            case Constants.ImportType.importOverride:
                Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
                materialGoodsRepository.deleteAllByCompanyIdIn(systemOptionRepository
                        .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
                materialGoodsRepository.saveAll(materialGoodsData);
                return true;
            default:
                return false;
        }
    }

    BigDecimal convertRate(Object rate, Row row, UpdateDataDTO requestData, String code) {
        String iTaxRate = rate.toString().trim();
        if (taxRateMatches(iTaxRate)) {
            iTaxRate = iTaxRate.replaceAll("%", "");
            return new BigDecimal(iTaxRate);
        } else if (iTaxRate.isEmpty()) {
            return null;
        } else {
            String invalid = " không hợp lệ!";
            setComment(row, requestData, code,
                requestData.getCodeWithExcelField().get(code)
                    + invalid);
        }
        return null;
    }
}
