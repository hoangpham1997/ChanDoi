package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EInvoiceService;
import vn.softdreams.ebweb.service.SaBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMDoiTuong;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;
import static vn.softdreams.ebweb.service.util.TypeConstant.XUAT_HOA_DON;

/**
 * Service Implementation for managing SaBill.
 */
@Service
@Transactional
public class SaBillServiceImpl implements SaBillService {

    private final String SA_BILL = "saBill";

    private final Logger log = LoggerFactory.getLogger(SaBillServiceImpl.class);

    private final SaBillRepository saBillRepository;

    private final AccountingObjectRepository accountingObjectRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final UserService userService;

    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;

    private final InvoiceTypeRepository invoiceTypeRepository;

    private final CurrencyRepository currencyRepository;

    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;

    private final MaterialGoodsRepository materialGoodsRepository;

    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";

    private final UnitRepository unitRepository;

    private final SystemOptionRepository systemOptionRepository;

    private final EInvoiceService eInvoiceService;

    private final OrganizationUnitRepository organizationUnitRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final SaReturnRepository saReturnRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final RepositoryRepository repositoryRepository;

    public SaBillServiceImpl(SaBillRepository saBillRepository,
                             AccountingObjectRepository accountingObjectRepository,
                             RefVoucherRepository refVoucherRepository,
                             UserService userService,
                             IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository,
                             InvoiceTypeRepository invoiceTypeRepository,
                             CurrencyRepository currencyRepository,
                             AccountingObjectBankAccountRepository accountingObjectBankAccountRepository,
                             MaterialGoodsRepository materialGoodsRepository,
                             UnitRepository unitRepository,
                             EInvoiceService eInvoiceService, OrganizationUnitRepository organizationUnitRepository,
                             SAInvoiceRepository saInvoiceRepository, SystemOptionRepository systemOptionRepository, SaReturnRepository saReturnRepository, PPDiscountReturnRepository ppDiscountReturnRepository, RepositoryRepository repositoryRepository) {
        this.saBillRepository = saBillRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.currencyRepository = currencyRepository;
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.unitRepository = unitRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.eInvoiceService = eInvoiceService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.repositoryRepository = repositoryRepository;
    }

    /**
     * Save a saBill.
     *
     * @param saBillSaveDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SaBillSaveDTO save(SaBillSaveDTO saBillSaveDTO) {
        log.debug("Request to save SaBill : {}", saBillSaveDTO);
        SaBill saBill = saBillSaveDTO.getSaBill();
        SaBill saBillSave = new SaBill();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent()) {

            boolean isRequiredInvoiceNo = user.get().getSystemOptions().stream().anyMatch(item
                -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDTichHopHDDT) && item.getData().equalsIgnoreCase("0"));

            if (!Strings.isNullOrEmpty(saBill.getInvoiceNo())) {
                int count = saBillRepository.countByInvoiceNoAndCompanyID(saBill.getInvoiceNo(), saBill.getInvoiceForm(),
                    saBill.getInvoiceTemplate(), saBill.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg(),
                    saBill.getId() != null ? saBill.getId() : UUID.randomUUID());
                if (count > 0) {
                    throw new BadRequestAlertException("Khong the xuat hoa don", "saBill", "duplicate");
                }
            }
            if (isRequiredInvoiceNo && saBill.getInvoiceNo() == null) {
                throw new BadRequestAlertException("So hoa don bat buoc nhap", "saBill", "invalidInvoiceNoRequired");
            } else if (saBill.getInvoiceNo() != null && saBill.getInvoiceForm() != Constants.InvoiceForm.HD_DIEN_TU) {
                List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(
                    saBill.getInvoiceForm(), saBill.getInvoiceTypeID(), saBill.getInvoiceTemplate(), saBill.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg());
                if (pTemplate.size() > 0) {
                    Boolean error = false;
                    String minFromNo = null;
                    String maxToNo = null;
                    for (IaPublishInvoiceDetails item: pTemplate) {
                        if (minFromNo == null || minFromNo.compareTo(item.getFromNo()) > 0) {
                            minFromNo = item.getFromNo();
                        }
                        if (maxToNo == null  || maxToNo.compareTo(item.getToNo()) < 0) {
                            maxToNo = item.getToNo();
                        }
                        if (((saBill.getInvoiceNo().compareTo(item.getFromNo()) >= 0 && item.getToNo() != null && saBill.getInvoiceNo().compareTo(item.getToNo()) <= 0)
                            || ((item.getToNo() == null && saBill.getInvoiceNo().compareTo(item.getFromNo()) >= 0))) && saBill.getInvoiceDate().isBefore(item.getStartUsing())) {
                            error = true;
                        }
                    }
                    if ((minFromNo != null && saBill.getInvoiceNo().compareTo(minFromNo) < 0) || (maxToNo != null && saBill.getInvoiceNo().compareTo(maxToNo) > 0)) {
                        throw new BadRequestAlertException("So hoa don khong thuoc dai phat hanh", "saBill", "invalidInvoiceNo");
                    }
                    if (error) {
                        throw new BadRequestAlertException("So hoa don nho hon ngay phat hanh", "saBill", "errorInvoiceDate");
                    }
                }
            }

            saBill.setTypeID(XUAT_HOA_DON);
            saBill.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            saBill.setTotalAmount(saBill.getSaBillDetails().stream().map(SaBillDetails::getAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.setTotalAmountOriginal(saBill.getSaBillDetails().stream().map(SaBillDetails::getAmountOriginal)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.setTotalVATAmount(saBill.getSaBillDetails().stream().map(SaBillDetails::getVatAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.setTotalVATAmountOriginal(saBill.getSaBillDetails().stream().map(SaBillDetails::getVatAmountOriginal)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.setTotalDiscountAmount(saBill.getSaBillDetails().stream().map(SaBillDetails::getDiscountAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.setTotalDiscountAmountOriginal(saBill.getSaBillDetails().stream().map(SaBillDetails::getDiscountAmountOriginal)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            saBill.getSaBillDetails().forEach(item -> {
                item.setId(null);
                if (item.getUnit() != null) {
                    item.setUnitID(item.getUnit().getId());
                }
                if (item.getMainUnit() != null) {
                    item.setMainUnitID(item.getMainUnit().getId());
                }
            });
            saBillSave = saBillRepository.save(saBill);
            for (SaBillDetails saBillDetailsSave : saBillSave.getSaBillDetails()) {
                for (SaBillDetails saBillDetailsold : saBill.getSaBillDetails()) {
                    if (saBillDetailsSave.getOrderPriority().equals(saBillDetailsold.getOrderPriority())) {
                        saBillDetailsSave.setSaInvoiceDetailID(saBillDetailsold.getSaInvoiceDetailID());
                        saBillDetailsSave.setSaReturnDetailID(saBillDetailsold.getSaReturnDetailID());
                        saBillDetailsSave.setPpDiscountReturnDetailID(saBillDetailsold.getPpDiscountReturnDetailID());
                    }
                }
            }

            List<RefVoucher> refVouchers = saBillSaveDTO.getViewVouchers();
            for (RefVoucher item : refVouchers) {
                item.setRefID1(saBillSave.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(saBillSave.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            saBillSaveDTO.setSaBill(saBillSave);
            saBillSaveDTO.setViewVouchers(refVouchers);
            /*
             * Add by Hautv
             *Create EInvoice
             * */
            //region
            if (saBillSave.getInvoiceForm().equals(0)) {
                Respone_SDS result = new Respone_SDS();
                if(saBillSave.getStatusInvoice() == null){
                    saBillSave.setStatusInvoice(0);
                }
                switch (saBillSave.getStatusInvoice()){
                    case 7:
                        result = eInvoiceService.createEInoviceReplaced(saBillSave.getId());
                        break;
                    case 8:
                        result = eInvoiceService.createEInoviceAdjusted(saBillSave.getId());
                        break;
                    case 6:
                    case 0:
                        result = eInvoiceService.createEInvoice(saBillSave.getId());
                        break;
                }
                if (Constants.EInvoice.Respone.Success.equals(result.getStatus())) {
                    if (Constants.EInvoice.SupplierCode.MIV.equals(Utils.getEI_IDNhaCungCapDichVu(userService.getAccount()))) {
                        if (!StringUtils.isEmpty(result.getInvoiceNo())) {
                            saBillSaveDTO.getSaBill().setInvoiceNo(result.getInvoiceNo());
                        }
                    }
                }
            }
            //endregion
            saInvoiceRepository.updateSaBillNull(saBillSave.getId());
            saReturnRepository.updateSaBillNull(saBillSave.getId());
            ppDiscountReturnRepository.updateSaBillNull(saBillSave.getId());
            if (saBill.getSaBillDetails().stream().filter(x -> x.getSaInvoiceDetailID() != null).count() > 0) {
                saInvoiceRepository.updateSaBill(saBillSave.getSaBillDetails(), saBillSave.getId(), saBillSave.getInvoiceNo());
            } else if (saBill.getSaBillDetails().stream().filter(x -> x.getSaReturnDetailID() != null).count() > 0) {
                saReturnRepository.updateSaBill(saBillSave.getSaBillDetails(), saBillSave.getId(), saBillSave.getInvoiceNo());
            } else if (saBill.getSaBillDetails().stream().filter(x -> x.getPpDiscountReturnDetailID() != null).count() > 0) {
                ppDiscountReturnRepository.updateSaBill(saBillSave.getSaBillDetails(), saBillSave.getId(), saBillSave.getInvoiceNo());
            }
            return saBillSaveDTO;
        }

        throw new BadRequestAlertException("Khong the xuat hoa don", "", "");
    }

    /**
     * Get all the saBills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaBill> findAll(Pageable pageable) {
        log.debug("Request to get all SaBills");
        return saBillRepository.findAll(pageable);
    }


    /**
     * Get one saBill by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaBillViewDTO findOne(UUID id) {
        log.debug("Request to get SaBill : {}", id);
        SaBillViewDTO dto = new SaBillViewDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setViewVouchers(dtos);
        }
        Optional<SaBill> byId = saBillRepository.findById(id);
        if (byId.isPresent()) {
            for (SaBillDetails detail: byId.get().getSaBillDetails()) {
                SAInvoiceDetails saInvoiceDetails = saInvoiceRepository.findDetaiBySaBillID(detail.getId());
                if (saInvoiceDetails != null) {
                    detail.setSaInvoiceDetailID(saInvoiceDetails.getId());
                }
                SaReturnDetails saReturnDetails = saReturnRepository.findDetaiBySaBillID(detail.getId());
                if (saReturnDetails != null) {
                    detail.setSaReturnDetailID(saReturnDetails.getId());
                }
                PPDiscountReturnDetails ppDiscountReturnDetails = ppDiscountReturnRepository.findDetaiBySaBillID(detail.getId());
                if (ppDiscountReturnDetails != null) {
                    detail.setPpDiscountReturnDetailID(ppDiscountReturnDetails.getId());
                }
            }
            dto.setSaBill(byId.get());
            return dto;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public SaBillViewDTO findOneByID(UUID id) {
        log.debug("Request to get SaBill : {}", id);
        SaBillViewDTO dto = new SaBillViewDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setViewVouchers(dtos);
        }
        Optional<SaBill> byId = saBillRepository.findOneById(id);
        if (byId.isPresent()) {
            dto.setSaBill(byId.get());
            return dto;
        }
        return null;
    }

    @Override
    public Page<SaBillCreatedDTO> saBillCreated(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        return saBillRepository.saBillCreated(pageable, accountingObjectID, formDate, toDate, currencyCode, currentUserLoginAndOrg.get().getOrg(), userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData());
    }

    @Override
    public List<SaBillCreatedDetailDTO> saBillCreatedDetail(List<UUID> sabillIdList) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SaBillCreatedDetailDTO> saBillCreatedDetailDTOList = saBillRepository.saBillCreatedDetail(sabillIdList);
        if (saBillCreatedDetailDTOList.size() > 0) {
            for (int i = 0; i < saBillCreatedDetailDTOList.size(); i++) {
                if (saBillCreatedDetailDTOList.get(i).getMaterialGoodsID() != null) {
                    for (int j = 0; j < i; j++) {
                        if (saBillCreatedDetailDTOList.get(i).getMaterialGoodsID().equals(saBillCreatedDetailDTOList.get(j).getMaterialGoodsID())) {
                            saBillCreatedDetailDTOList.get(i).setUnits(saBillCreatedDetailDTOList.get(j).getUnits());
                            break;
                        }
                    }
                    if (saBillCreatedDetailDTOList.get(i).getUnits() == null) {
                        saBillCreatedDetailDTOList.get(i).setUnits(unitRepository.convertRateForMaterialGoodsComboboxCustom(saBillCreatedDetailDTOList.get(i).getMaterialGoodsID(),
                            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH)));
                    }
                }
            }
        }
        return saBillCreatedDetailDTOList;
    }

    /**
     * Delete the saBill by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {

        log.debug("Request to delete SaBill : {}", id);
        /* Xóa nếu là hóa đơn điện tử add by Hautv*/
        List<UUID> uuids = new ArrayList<>();
        uuids.add(id);
        List<Respone_SDS> respone_sds = eInvoiceService.deleteEInovice(uuids);
        if (respone_sds.get(0).getStatus().equals(2)) {
            // Xóa thành công
        }

        saBillRepository.deleteById(id);

        // update chứng từ bán hàng
        saInvoiceRepository.updateSaBillNullInvoiceNo(id);
        saReturnRepository.updateSaBillNullInvoiceNo(id);
        ppDiscountReturnRepository.updateSaBillNullInvoiceNo(id);
        saInvoiceRepository.updateSaBillNull(id);
        saReturnRepository.updateSaBillNull(id);
        ppDiscountReturnRepository.updateSaBillNull(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);

    }

    @Override
    public Page<SaBillDTO> getAllSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                            String fromInvoiceDate, String toInvoiceDate, String invoiceSeries,
                                            String invoiceNo, String freeText) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return currentUserLoginAndOrg.map(securityDTO -> saBillRepository.getAllSaBillDTOs(pageable, accountingObjectID,
                invoiceTemplate, fromInvoiceDate, toInvoiceDate, invoiceSeries,
                invoiceNo, freeText, securityDTO.getOrg(), currentBook)).orElse(null);
        }
        return null;
    }

    @Override
    public SaBillViewDTO getNextSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                           String fromInvoiceDate, String toInvoiceDate, String invoiceSeries,
                                           String invoiceNo, String freeText, Integer index, UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            SaBillDTO dto = saBillRepository.getNextSaBillDTOs(pageable, accountingObjectID,
                invoiceTemplate, fromInvoiceDate, toInvoiceDate, invoiceSeries,
                invoiceNo, freeText, currentUserLoginAndOrg.get().getOrg(), index, currentBook, id);
            if (dto != null) {
                SaBillViewDTO one = findOne(dto.getId());
                one.setTotalRow(dto.getTotalRow());
                return one;
            }
            return null;
        }

        return null;
    }

    @Override
    public SaBillSearchDTO getAllSearchData() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<AccountingObjectDTO> allAccountingObject = accountingObjectRepository.findAllDTO(SaBill.class.getSimpleName(), currentUserLoginAndOrg.get().getOrg());
            List<IaPublishInvoiceDetails> allTemplate = iaPublishInvoiceDetailsRepository.getAllTemplate(currentUserLoginAndOrg.get().getOrg());
            List<String> series = saBillRepository.getAllSeries(currentUserLoginAndOrg.get().getOrg());
            List<SeriDTO> collect = series.stream().map(SeriDTO::new).collect(Collectors.toList());
            return new SaBillSearchDTO(allAccountingObject, allTemplate, collect);
        }
        return null;
    }

    @Override
    public Page<SaBill> getAll() {
        return new PageImpl<>(saBillRepository.findAll());
    }

    @Override
    public UploadInvoiceDTO upload(MultipartFile file) throws IOException {
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean isError = false;
        if (!currentUserLoginAndOrg.isPresent() || !user.isPresent()) {
            throw new BadRequestAlertException(SA_BILL, SA_BILL, "login");
        }

        boolean isRequiredInvoiceNo = user.get().getSystemOptions().stream().anyMatch(item
            -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDTichHopHDDT) && item.getData().equalsIgnoreCase("0"));

        UUID org = currentUserLoginAndOrg.get().getOrg();
        Workbook workbook = null;
        String message = "";
        try {
            workbook = WorkbookFactory.create(file.getInputStream());

        } catch (Exception ignore) {
            isError = true;
            message = "invalidFile";
            UploadInvoiceDTO dto = new UploadInvoiceDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }
        Sheet sheet = workbook.getSheetAt(0);

        if (sheet.getLastRowNum() < 1) {
            isError = true;
            message = "rowEmpty";
            UploadInvoiceDTO dto = new UploadInvoiceDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }

        // Lấy ra row đầu tiên đọc header
        Row row1 = sheet.getRow(0);
        Iterator<Cell> cellIterator = row1.cellIterator();
        Map<String, Integer> headers = new HashMap<>();
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (ExcelConstant.Header.XUAT_HOA_DON.contains(ExcelUtils.getCellValue(cell).toString())
                || ExcelConstant.Header.XUAT_HOA_DON_DETAIL.contains(ExcelUtils.getCellValue(cell).toString())) {
                headers.put(ExcelUtils.getCellValue(cell).toString(), i);
            }
            i++;
        }

        // check các cột bặt buộc phải có trong file

        for (String header : ExcelConstant.Header.XUAT_HOA_DON_REQUIRED) {
            if (headers.get(header) == null) {
                isError = true;
                message = "invalidFileFormat";
                UploadInvoiceDTO dto = new UploadInvoiceDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
        }
        for (String header : ExcelConstant.Header.XUAT_HOA_DON_DETAIL_REQUIRED) {
            if (headers.get(header) == null) {
                isError = true;
                message = "invalidFileFormat";
                UploadInvoiceDTO dto = new UploadInvoiceDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
        }

        List<SaBill> saBills = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        String maHd = "";
        SaBill saBill = new SaBill();
        List<SaBillDetails> details = new ArrayList<>();
        Map<String, SaBill> mapBill = new HashMap<>();
        Map<String, List<SaBillDetails>> mapBillDetail = new HashMap<>();
        List<IaPublishInvoiceDetails> templates = iaPublishInvoiceDetailsRepository.findByIaPublishInvoiceCompanyId(org);
        List<MaterialGoodsDTO> materialGoodsDTOS = materialGoodsRepository.findAllForDTO(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
        List<Currency> currencies = currencyRepository.findByIsActiveTrue(org);
        List<InvoiceType> invoiceTypes = invoiceTypeRepository.findAllOrderByInvoiceTypeName();
        List<AccountingObjectDTO> accountingObjects = accountingObjectRepository.findAllAccountingObjectDTO(systemOptionRepository.getAllCompanyByCompanyIdAndCode(org, TCKHAC_SDDMDoiTuong), false);
        List<SaBillDTO> saBillDTOS = saBillRepository.getAllSaBillDTOByCompayIDs(org);
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);

            // Nếu mã hóa đơn cũ khác mã hóa đơn mới, tạo mới sa bill và detail
            // và chỉ đọc detail
            String newMaHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(0))));
            if (Strings.isNullOrEmpty(newMaHd)) {
//                row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(0))).setCellValue("");
//                throw new BadRequestAlertException(SA_BILL, SA_BILL, ExcelConstant.Header.XUAT_HOA_DON.get(0));
                isError = true;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(0))), "Mã hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(0)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(0))));
            }

            // Lấy mã hóa đơn, check xem mã này đã từng có dữ liệu chưa
            // nếu có lấy ra và lưu tiếp các detail
            // nếu không thì tạo mới
            boolean isNew = false;
            if (mapBill.get(newMaHd) == null) {
                isNew = true;
                if (j != 1) {
                    saBill.setSaBillDetails(new HashSet<>(details));
                    saBills.add(saBill);
                }
                saBill = new SaBill();
                if (mapBillDetail.get(newMaHd) == null) {
                    details = new ArrayList<>();
                } else {
                    details = mapBillDetail.get(newMaHd);
                }
            }

            if (isNew) {
                // Vào sổ
                String vaoSo = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(1))));
                double vaoSoParse;
                try {
                    vaoSoParse = Double.parseDouble(vaoSo);

                } catch (Exception ignore) {
                    vaoSoParse = 3D;
                }

                if (Constants.TypeLedger.SO_TAI_CHINH.equalsIgnoreCase(vaoSo) || vaoSoParse == 0) {
                    saBill.setTypeLedger(0);
                } else if (Constants.TypeLedger.SO_QUAN_TRI.equalsIgnoreCase(vaoSo) || vaoSoParse == 1) {
                    saBill.setTypeLedger(1);
                } else {
                    saBill.setTypeLedger(2);
                }

                // Hình thức hóa đơn
                String hinhThucHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2))));
                if (Strings.isNullOrEmpty(hinhThucHd)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2))), "Hình thức hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2))));
                    double hinhThucHdParse = 3D;
                    try {
                        hinhThucHdParse = Double.parseDouble(hinhThucHd);

                    } catch (Exception ignore) {
                        hinhThucHdParse = 3D;
                    }
                    if (Constants.InvoiceForm.HOA_DON_DIEN_TU.equalsIgnoreCase(hinhThucHd) || hinhThucHdParse == 0) {
                        saBill.setInvoiceForm(0);
                    } else if (Constants.InvoiceForm.HOA_DON_DAT_IN.equalsIgnoreCase(hinhThucHd) || hinhThucHdParse == 1) {
                        saBill.setInvoiceForm(1);
                    } else if (Constants.InvoiceForm.HOA_DON_TU_IN.equalsIgnoreCase(hinhThucHd) || hinhThucHdParse == 2) {
                        saBill.setInvoiceForm(2);
                    } else {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2))), "Hình thức hóa đơn không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(2)));
                    }
                }


                // Loại hóa đơn
                InvoiceType invoiceType = null;
                String loaiHD = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3))));
                if (Strings.isNullOrEmpty(loaiHD)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3))), "Loại hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3)));
                } else {
                    // invoiceType = invoiceTypeRepository.findByInvoiceTypeCode(loaiHD);
                    Optional<InvoiceType> invoiceTypeOptional = invoiceTypes.stream().filter(x -> x.getInvoiceTypeCode().equals(loaiHD)).findFirst();
                    if (invoiceTypeOptional.isPresent()) {
                        invoiceType = invoiceTypeOptional.get();
                    }
                    if (invoiceType == null) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3))), "Loại hóa đơn không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(3))));
                        saBill.setInvoiceTypeID(invoiceType.getId());
                    }
                }


                // Mẫu số hóa đơn
                String mauSoHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4))));
                if (Strings.isNullOrEmpty(mauSoHd)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4))), "Mẫu số hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4))));
                }

                // IaPublishInvoiceDetails template = iaPublishInvoiceDetailsRepository.countByInvoiceTemplateAndCompanyID(mauSoHd, org);
                IaPublishInvoiceDetails template = null;
                Optional<IaPublishInvoiceDetails> iaPublishInvoiceDetail = templates.stream().filter(x -> x.getInvoiceTemplate().equals(mauSoHd)).findFirst();
                if (iaPublishInvoiceDetail.isPresent()) {
                    template = iaPublishInvoiceDetail.get();
                }
                if (template == null) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4))), "Mẫu số hóa đơn không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4)));
                } else {
                    saBill.setTemplateID(template.getId());
                    saBill.setInvoiceTemplate(mauSoHd);
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(4))));

                }

                int count = 0;
                // Ký hiệu hóa đơn
                String kyHieuHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5))));
                if (Strings.isNullOrEmpty(kyHieuHd)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5))), "Ký hiệu hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5)));
                } else {
                    // count = iaPublishInvoiceDetailsRepository.countByInvoiceSeriesAndCompanyID(kyHieuHd, org);
                    count = (int) templates.stream().filter(x -> x.getInvoiceSeries().equals(kyHieuHd)).count();
                    if (count == 0) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5))), "Ký hiệu hóa đơn chưa được phát hành", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(5))));
                    }
                    saBill.setInvoiceSeries(kyHieuHd);
                }

                // Số hóa đơn
                boolean isErrorShd = false;
                String soHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))));
                if (!Strings.isNullOrEmpty(soHd)) {
                    // Check trùng trong db
                    // count = saBillRepository.countByInvoiceNoAndCompanyID(soHd, saBill.getInvoiceForm(), mauSoHd, kyHieuHd, org, UUID.randomUUID());
                    SaBill finalSaBill1 = saBill;
                    count = (int) saBillDTOS.stream().filter(x -> x.getInvoiceForm().equals(finalSaBill1.getInvoiceForm()) && x.getInvoiceNo().equals(soHd) &&
                        x.getInvoiceTemplate().equals(mauSoHd) && x.getInvoiceSeries().equals(kyHieuHd)).count();
                    if (count > 0) {
                        isError = true;
                        isErrorShd = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn bị trùng lặp", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))));
                        // check trùng trong list chuẩn bị insert
                        for (SaBill saBillCheck : saBills) {
                            if (!Strings.isNullOrEmpty(saBillCheck.getInvoiceNo()) && saBillCheck.getInvoiceNo().equalsIgnoreCase(soHd)
                                && saBillCheck.getInvoiceForm() != null && saBillCheck.getInvoiceForm().compareTo(saBill.getInvoiceForm()) == 0
                                && !Strings.isNullOrEmpty(saBillCheck.getInvoiceTemplate()) && saBillCheck.getInvoiceTemplate().equalsIgnoreCase(mauSoHd)
                                && !Strings.isNullOrEmpty(saBillCheck.getInvoiceSeries()) && saBillCheck.getInvoiceSeries().equalsIgnoreCase(kyHieuHd)) {
                                isError = true;
                                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn bị trùng lặp", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                            }
                        }
                    }

                    if (isRequiredInvoiceNo && saBill.getInvoiceForm() != null && saBill.getInvoiceForm() == 0) {
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn không cho phép nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                    } else if (!isErrorShd) {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))));
                    }
                }

                if (invoiceType != null) {
                    // List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(saBill.getInvoiceForm(), invoiceType.getId(), mauSoHd, kyHieuHd, org);
                    SaBill finalSaBill = saBill;
                    InvoiceType finalInvoiceType = invoiceType;
                    List<IaPublishInvoiceDetails> pTemplate = templates.stream().filter(x -> x.getInvoiceForm().equals(finalSaBill.getInvoiceForm()) && x.getInvoiceType().getId().equals(finalInvoiceType.getId()) &&
                        x.getInvoiceTemplate().equals(mauSoHd) && x.getInvoiceSeries().equals(kyHieuHd)).collect(Collectors.toList());
                    if (pTemplate.size() == 0) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn chưa được phát hành", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                    } else if (!Strings.isNullOrEmpty(soHd)) {
                        Boolean error = true;
                        for (IaPublishInvoiceDetails item: pTemplate) {
                            if ((item.getFromNo() == null || soHd.compareTo(item.getFromNo()) >= 0) && (item.getToNo() == null || soHd.compareTo(item.getToNo()) <= 0)) {
                                error = false;
                            }
                        }
                        if (error) {
                            isError = true;
                            ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn không thuộc dải hóa đơn", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                        }
                    }
                    if (!isErrorShd && !isError) {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))));
                    }
                    saBill.setInvoiceNo(soHd);

                    if (pTemplate.size() == 0) {
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))), "Ngày hóa đơn chưa được phát hành", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7)));
                    } else {
                        // ngày hóa đơn
                        String ngayHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))));
                        if (Strings.isNullOrEmpty(ngayHd)) {
                            isError = true;
                            ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))), "Ngày hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7)));
                        } else {
                            try {
                                LocalDate ngayHdLocalDate = LocalDate.parse(ngayHd, DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                                if (!ngayHdLocalDate.format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY)).equals(ngayHd)) {
                                    isError = true;
                                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))), "Ngày hóa đơn không đúng định dạng dd/MM/yyyy", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7)));
                                }
                                List<IaPublishInvoiceDetails> iaPublishInvoiceDetails = pTemplate.stream().filter(x -> (x.getFromNo() == null || soHd.compareTo(x.getFromNo()) >= 0) && (x.getToNo() == null || soHd.compareTo(x.getToNo()) <= 0)).collect(Collectors.toList());
                                if (iaPublishInvoiceDetails.size() > 0 && ngayHdLocalDate.isBefore(iaPublishInvoiceDetails.get(0).getStartUsing())) {
                                    isError = true;
                                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))), "Ngày hóa đơn nhỏ hơn ngày bắt đầu sử dụng " + iaPublishInvoiceDetails.get(0).getStartUsing().format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY)), row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7)));
                                }
                                if (!isError){
                                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))));
                                }
                                saBill.setInvoiceDate(ngayHdLocalDate);
                                saBill.setRefDateTime(ngayHdLocalDate.atTime(0,0,0));
                                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))));
                            } catch (Exception ignore) {
                                isError = true;
                                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7))), "Ngày hóa đơn không đúng định dạng dd/MM/yyyy", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(7)));
                            }
                        }

                    }
                }

                // Hình thức TT
                String hinhThucTt = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(8))));
                if (Strings.isNullOrEmpty(hinhThucTt)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(8))), "Hình thức hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(8)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(8))));
                }
                saBill.setPaymentMethod(hinhThucTt);

                // Mã loại tiền
                String currencyCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9))));
                if (Strings.isNullOrEmpty(currencyCode)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9))), "Mã loại tiền bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9)));
                } else {
                    // count = currencyRepository.countByCurrencyCodeAndIsActiveTrue(currencyCode);
                    count = (int) currencies.stream().filter(x -> x.getCurrencyCode().equals(currencyCode)).count();
                    if (count == 0) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9))), "Mã loại tiền không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(9))));
                    }
                    saBill.setCurrencyID(currencyCode);
                }


                // Tỷ giá
                String tyGia = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10))));
                if (Strings.isNullOrEmpty(tyGia)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10))), "Tỷ giá bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10)));
                } else {
                    try {
                        BigDecimal tyGiaBigDecimal = new BigDecimal(tyGia);
                        saBill.setExchangeRate(tyGiaBigDecimal);
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10))));
                    } catch (Exception ignore) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10))), "Tỷ giá không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(10)));
                    }
                }


                // Trang thái hóa đơn
                String trangThaiHd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))));

                if (Strings.isNullOrEmpty(trangThaiHd)) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))), "Trạng thái hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11)));
                } else {
                    double trangThaiHdParse;
                    try {
                        trangThaiHdParse = Double.parseDouble(trangThaiHd);

                    } catch (Exception ignore) {
                        trangThaiHdParse = -1D;
                    }
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))));
                    if (Constants.StatusInvoice.MOI_TAO_LAP.equalsIgnoreCase(trangThaiHd) || trangThaiHdParse == 0) {
                        saBill.setStatusInvoice(0);
                    } else if (Constants.StatusInvoice.CHU_KY_SO.equalsIgnoreCase(trangThaiHd) || trangThaiHdParse == 1) {
                        saBill.setStatusInvoice(1);
                    } else if (Constants.StatusInvoice.THAY_THE.equalsIgnoreCase(trangThaiHd) || trangThaiHdParse == 2) {
                        saBill.setStatusInvoice(2);
                    } else if (Constants.StatusInvoice.DIEU_CHINH.equalsIgnoreCase(trangThaiHd) || trangThaiHdParse == 3) {
                        saBill.setStatusInvoice(3);
                    } else if (Constants.StatusInvoice.HUY.equalsIgnoreCase(trangThaiHd) || trangThaiHdParse == 4) {
                        saBill.setStatusInvoice(4);
                    } else {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))), "Trạng thái hóa đơn không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11)));
                    }

//                    isRequiredInvoiceNo == 0
                    if (Strings.isNullOrEmpty(soHd) && isRequiredInvoiceNo && !Strings.isNullOrEmpty(trangThaiHd) && trangThaiHdParse != 0) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6))), "Số hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(6)));
                    }
                    if (Strings.isNullOrEmpty(saBill.getInvoiceNo())
                        && (!isRequiredInvoiceNo && saBill.getStatusInvoice() != 0)) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))), "Số hóa đơn bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(11))));
                    }
                }


                // Mã đối tượng
                String maDoiTuong = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12))));
                AccountingObject accountingObject = null;
                if (!Strings.isNullOrEmpty(maDoiTuong)) {
                    // accountingObject = accountingObjectRepository.findByAccountingObjectCodeAndCompanyId(maDoiTuong, systemOptionRepository.getAllCompanyByCompanyIdAndCode(org, TCKHAC_SDDMDoiTuong));
                    Optional<AccountingObjectDTO> accountingObjectDTO = accountingObjects.stream().filter(x -> x.getAccountingObjectCode().equals(maDoiTuong)).findFirst();
                    if (accountingObjectDTO.isPresent()) {
                        accountingObject = new AccountingObject();
                        accountingObject.setId(accountingObjectDTO.get().getId());
                        accountingObject.setAccountingObjectCode(accountingObjectDTO.get().getAccountingObjectCode());
                        accountingObject.setAccountingObjectName(accountingObjectDTO.get().getAccountingObjectName());
                    }
                    if (accountingObject == null) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12))), "Mã đối tượng không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12))));
                    }
                    saBill.setAccountingObject(accountingObject);
                } else {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12))), "Mã đối tượng bắt buộc nhập", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(12)));
                }

                // Tên đối tượng
                String tenDoiTuong = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(13))));
                if (!Strings.isNullOrEmpty(maDoiTuong)) {
                    saBill.setAccountingObjectName(tenDoiTuong);
                }

                // địa chỉ
                String diaChi = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(14))));
                if (!Strings.isNullOrEmpty(diaChi)) {
                    saBill.setAccountingObjectAddress(diaChi);
                }

                // MST
                String mst = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(15))));
                if (!Strings.isNullOrEmpty(mst)) {
                    if (!Common.isValidTaxCode(mst)) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(15))), "Mã số thuế không hợp lệ", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(15)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(15))));
                    }
                    saBill.setCompanyTaxCode(mst);
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(15))));
                }

                // Người liên hệ
                String nguoiLienHe = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(16))));
                if (!Strings.isNullOrEmpty(nguoiLienHe)) {
                    saBill.setContactName(nguoiLienHe);
                }

                // Diễn giải
                String reason = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(17))));
                if (!Strings.isNullOrEmpty(reason)) {
                    saBill.setReason(reason);
                }

                // Số tài khoản
                String soTaiKhoan = formatter.formatCellValue(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(18))));
                if (!Strings.isNullOrEmpty(soTaiKhoan)) {
//                    count = accountingObjectBankAccountRepository.countByAccountingObjectIdAndBankAccount(accountingObject.getId(), soTaiKhoan);
//                    if (count == 0) {
//                        isError = true;
//                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(18))), "Số tài khoản không hợp lệ", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(18)));
//                    } else {
//                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(18))));
//                    }
                    saBill.setAccountingObjectBankAccount(soTaiKhoan);
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(18))));
                }

                // Tên ngân hàng
                String tenNganHang = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON.get(19))));
                if (!Strings.isNullOrEmpty(tenNganHang)) {
                    saBill.setAccountingObjectBankName(tenNganHang);
                }

                saBill.setTypeID(TypeConstant.XUAT_HOA_DON);
                saBill.setCompanyID(org);
                mapBill.put(newMaHd, saBill);
            }
            // Tiếp tục check detail

            SaBillDetails saBillDetails = new SaBillDetails();

            // Mã hàng
            String maHang = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0))));
            if (Strings.isNullOrEmpty(maHang)) {
                isError = true;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0))), "Mã hàng không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0)));
            } else {
                // MaterialGoods materialGoods = materialGoodsRepository.findByCompanyIDAndMaterialGoodsCodeAndIsActiveTrue(systemOptionRepository.getAllCompanyByCompanyIdAndCode(org, TCKHAC_SDDMVTHH), maHang);
                MaterialGoods materialGoods = null;
                Optional<MaterialGoodsDTO> materialGoodsDTO = materialGoodsDTOS.stream().filter(x -> x.getMaterialGoodsCode().equals(maHang)).findFirst();
                if (materialGoodsDTO.isPresent()) {
                    materialGoods = new MaterialGoods();
                    materialGoods.setId(materialGoodsDTO.get().getId());
                    materialGoods.setMaterialGoodsCode(materialGoodsDTO.get().getMaterialGoodsCode());
                    materialGoods.setMaterialGoodsName(materialGoodsDTO.get().getMaterialGoodsName());
                }
                if (materialGoods == null) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0))), "Mã hàng không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(0))));
                    saBillDetails.setMaterialGoods(materialGoods);
                    // Đơn vị tính
                    String donViTinh = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(2))));
                    if (!Strings.isNullOrEmpty(donViTinh)) {
                        Unit unit = unitRepository.findByCompanyIdAndIsActiveTrueAndUnitName(donViTinh.toUpperCase(), materialGoods.getId());
                        if (unit == null) {
                            isError = true;
                            ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(2))), "Đơn vị tính không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(2)));
                        } else {
                            ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(2))));
                            saBillDetails.setUnitID(unit.getId());
                        }
                    }
                }
            }

            // Tên hàng
            String tenHang = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(1))));
            if (!Strings.isNullOrEmpty(tenHang)) {
                saBillDetails.setDescription(tenHang);
            }

            // Số lượng
            String soLuong = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(3))));
            if (!Strings.isNullOrEmpty(soLuong)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(3))));
                    saBillDetails.setQuantity(new BigDecimal(soLuong));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(3))), "Số lượng không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(3)));
                }
            }

            // Đơn giá
            String donGia = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(4))));
            if (!Strings.isNullOrEmpty(donGia)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(4))));
                    saBillDetails.unitPriceOriginal(new BigDecimal(donGia));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(4))), "Đơn giá không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(4)));

                }
            }

            // Đơn giá QĐ
            String donGiaQD = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(5))));
            if (!Strings.isNullOrEmpty(donGiaQD)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(5))));
                    saBillDetails.unitPrice(new BigDecimal(donGiaQD));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(5))), "Đơn giá QĐ không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(5)));
                }
            }

            // Thành tiền
            String thanhTien = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(6))));
            if (!Strings.isNullOrEmpty(thanhTien)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(6))));
                    saBillDetails.amountOriginal(new BigDecimal(thanhTien));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(6))), "Thành tiền không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(6)));
                }
            }

            // Thành tiền QD
            String thanhTienQd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(7))));
            if (!Strings.isNullOrEmpty(thanhTienQd)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(7))));
                    saBillDetails.amount(new BigDecimal(thanhTienQd));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(7))), "Thành tiền QĐ không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(7)));
                }
            }

            // Tỷ lệ ck
            String tyLeCk = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(8))));
            if (!Strings.isNullOrEmpty(tyLeCk)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(8))));
                    saBillDetails.discountRate(new BigDecimal(tyLeCk));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(8))), "Tỷ lệ CK không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(8)));
                }
            }

            // Tiền chiết khấu
            String chietKhau = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(9))));
            if (!Strings.isNullOrEmpty(chietKhau)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(9))));
                    saBillDetails.setDiscountAmountOriginal(new BigDecimal(chietKhau));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(9))), "Tiền CK không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(9)));
                }
            }

            // Tiền chiết khấu QD
            String chietKhauQd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(10))));
            if (!Strings.isNullOrEmpty(chietKhauQd)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(10))));
                    saBillDetails.setDiscountAmount(new BigDecimal(chietKhauQd));
                } catch (Exception ignore) {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(10))), "Tiền CK QĐ không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(10)));
                }
            }

            // % Thuế VAT
            String thueVat = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(11))));
            if (!Strings.isNullOrEmpty(thueVat)) {
                String real = "";
                try {
                    real = String.valueOf((new BigDecimal(thueVat)).intValue());
                } catch (Exception ignore) {

                }
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(11))));
                if (Constants.Vat.ZERO_PERCENT.equalsIgnoreCase(thueVat) || Constants.Vat.ZERO_PERCENT.equalsIgnoreCase(real)) {
                    saBillDetails.vatRate(BigDecimal.valueOf(0));
                } else if (Constants.Vat.FIVE_PERCENT.equalsIgnoreCase(thueVat) || Constants.Vat.FIVE_PERCENT.equalsIgnoreCase(real)) {
                    saBillDetails.vatRate(BigDecimal.valueOf(1));
                }  else if (Constants.Vat.TEN_PERCENT.equalsIgnoreCase(thueVat) || Constants.Vat.TEN_PERCENT.equalsIgnoreCase(real)) {
                    saBillDetails.vatRate(BigDecimal.valueOf(2));
                } else if (Constants.Vat.KCT.equalsIgnoreCase(thueVat)) {
                    saBillDetails.vatRate(BigDecimal.valueOf(3));
                } else if (Constants.Vat.KTT.equalsIgnoreCase(thueVat)) {
                    saBillDetails.vatRate(BigDecimal.valueOf(4));
                } else {
                    isError = true;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(11))), "Tỷ lệ thuế GTGT Không tồn tại", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(11)));
                }
            }

            if (saBillDetails.getVatRate().compareTo(BigDecimal.valueOf(3)) == 0 ||
                saBillDetails.getVatRate().compareTo(BigDecimal.valueOf(4)) == 0) {
                saBillDetails.setVatAmount(BigDecimal.ZERO);
                saBillDetails.setVatAmountOriginal(BigDecimal.ZERO);
            } else {
                // Thuế
                String tienThueVat = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(12))));
                if (!Strings.isNullOrEmpty(tienThueVat)) {
                    try {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(12))));
                        saBillDetails.setVatAmountOriginal(new BigDecimal(tienThueVat));
                    } catch (Exception ignore) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(12))), "Thuế GTGT không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(12)));
                    }
                }

                // Thuế qd
                String tienThueVatQd = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(13))));
                if (!Strings.isNullOrEmpty(tienThueVatQd)) {
                    try {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(13))));
                        saBillDetails.setVatAmount(new BigDecimal(tienThueVatQd));
                    } catch (Exception ignore) {
                        isError = true;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(13))), "Thuế GTGT QĐ không đúng định dạng", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(13)));
                    }
                }
            }
            // Số lô
            String soLo = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(14))));
            if (!Strings.isNullOrEmpty(soLo)) {
                try {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(14))));
                    saBillDetails.setLotNo(soLo);
                } catch (Exception ignore) {

                }
            }

            // Hạn dùng
            String hanDung = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15))));
            try {
                LocalDate hanDungLocalDate = LocalDate.parse(hanDung, DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                if (!hanDungLocalDate.format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY)).equals(hanDung)) {
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15))), "Hạn dùng không đúng định dạng dd/MM/yyyy", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15)));
                } else {
                    saBillDetails.setExpiryDate(hanDungLocalDate);
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15))));
                }
            } catch (Exception ignore) {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15))), "Hạn dùng không đúng định dạng dd/MM/yyyy", row, headers.get(ExcelConstant.Header.XUAT_HOA_DON_DETAIL.get(15)));
            }
            details.add(saBillDetails);
            mapBillDetail.put(maHd, details);
        }
        saBill.setSaBillDetails(new HashSet<>(details));
        saBills.add(saBill);
        // Nếu k có lỗi, lưu dữ liệu
        if (!isError) {
            for (SaBill bill : saBills) {
                bill.setTypeID(XUAT_HOA_DON);
                bill.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                bill.setTotalAmount(bill.getSaBillDetails().stream().map(SaBillDetails::getAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setTotalAmountOriginal(bill.getSaBillDetails().stream().map(SaBillDetails::getAmountOriginal)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setTotalVATAmount(bill.getSaBillDetails().stream().map(SaBillDetails::getVatAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setTotalVATAmountOriginal(bill.getSaBillDetails().stream().map(SaBillDetails::getVatAmountOriginal)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setTotalDiscountAmount(bill.getSaBillDetails().stream().map(SaBillDetails::getDiscountAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setTotalDiscountAmountOriginal(bill.getSaBillDetails().stream().map(SaBillDetails::getDiscountAmountOriginal)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                bill.setStatusInvoice(0);
            }

            List<SaBill> saBills1 = saBillRepository.insertBulk(saBills);
            // Add by Hautv
            List<SaBill> lstEInvoice = saBills1.stream().filter(n -> n.getStatusInvoice().equals(0)).collect(Collectors.toList());
            if (lstEInvoice.size() > 0) {
                // Đẩy hóa đơn lên hệ thống hóa đơn điện tử SDS
                UserDTO userDTO = userService.getAccount();
                Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
                String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
                if (HDDT && NCCDV.equals("SDS")) {
                    Respone_SDS respone_sds = eInvoiceService.createEInvoice(saBills1.stream().map(SaBill::getId).collect(Collectors.toList()), userDTO);
                    if (respone_sds.getStatus() == 2) {
                        // Thành công
                    } else {
                        // Có lỗi
                    }
                }
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        workbook.close();
        UploadInvoiceDTO dto = new UploadInvoiceDTO();
        dto.setError(isError);
        dto.setExcelFile(bos.toByteArray());
        dto.setMessage(message);
        return dto;
    }

    @Override
    public byte[] exportExcel(UUID accountingObjectID, String invoiceTemplate, String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo, String freeText) {

        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

            Page<SaBillDTO> saBillDTOS = currentUserLoginAndOrg.map(securityDTO -> saBillRepository.getAllSaBillDTOs(null, accountingObjectID,
                invoiceTemplate, fromInvoiceDate, toInvoiceDate, invoiceSeries,
                invoiceNo, freeText, securityDTO.getOrg(), currentBook)).orElse(null);

            return ExcelUtils.writeToFile(saBillDTOS.getContent(), ExcelConstant.SaBill.NAME, ExcelConstant.SaBill.HEADER, ExcelConstant.SaBill.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportPdf(UUID accountingObjectID, String invoiceTemplate, String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo, String freeText) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

            Page<SaBillDTO> saBillDTOS = currentUserLoginAndOrg.map(securityDTO -> saBillRepository.getAllSaBillDTOs(null, accountingObjectID,
                invoiceTemplate, fromInvoiceDate, toInvoiceDate, invoiceSeries,
                invoiceNo, freeText, securityDTO.getOrg(), currentBook)).orElse(null);

            return PdfUtils.writeToFile(saBillDTOS.getContent(), ExcelConstant.SaBill.HEADER, ExcelConstant.SaBill.FIELD, userDTO);
        }
        return null;
    }


    @Override
    public Boolean checkRelateVoucher(UUID sABillID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            // check sabill ở saInvoice , SaReturn, PPDiscountReturn nếu có trả ra true
            if (saBillRepository.checkRelateVoucherSaInvoice(sABillID, currentUserLoginAndOrg.get().getOrg()) > 0) {
                return true;
            } else if(saBillRepository.checkRelateVoucherSAReturn(sABillID, currentUserLoginAndOrg.get().getOrg()) > 0) {
                return true;
            } else if(saBillRepository.checkRelateVoucherPPDiscountReturn(sABillID, currentUserLoginAndOrg.get().getOrg()) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HandlingResultDTO multiDelete(List<SaBill> saBills) {
        UserDTO userDTO = userService.getAccount();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(saBills.size());
        List<SaBill> listDelete = saBills.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        // get ListID chung tu theo Type ID
        List<UUID> uuidListSABill = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1") &&
            listDelete.get(i).getInvoiceForm() == 0 && listDelete.get(i).getInvoiceNo() != null && !listDelete.get(i).getInvoiceNo().equals("")) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Hóa đơn đã được cấp số!");
                viewVoucherNo.setDate(listDelete.get(i).getInvoiceDate());
                viewVoucherNo.setPostedDate(listDelete.get(i).getInvoiceDate());
                viewVoucherNo.setInvoiceNo(listDelete.get(i).getInvoiceNo());
                viewVoucherNo.setTypeName("Xuất hóa đơn");
                viewVoucherNoListFail.add(viewVoucherNo);
            } else {
                uuidListSABill.add(listDelete.get(i).getId());
            }
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size() - viewVoucherNoListFail.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu
        if (uuidListSABill.size() > 0) {
            saBillRepository.multiDeleteSABill(currentUserLoginAndOrg.get().getOrg(), uuidListSABill);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidListSABill);
        }
        List<Respone_SDS> respone_sds = eInvoiceService.deleteEInovice(uuidListSABill);
        if (respone_sds.get(0).getStatus().equals(2)) {
            // Xóa thành công
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        return handlingResultDTO;
    }
}
