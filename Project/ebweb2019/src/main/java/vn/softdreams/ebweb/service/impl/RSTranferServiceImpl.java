package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * Service Implementation for managing RSTranfer.
 */
@Service
@Transactional
public class RSTranferServiceImpl implements RSTranferService {

    private final Logger log = LoggerFactory.getLogger(RSTranferServiceImpl.class);

    private final RSTransferRepository rSTranferRepository;

    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;

    private final SaBillRepository saBillRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final TypeRepository typeRepository;

    private final RepositoryLedgerRepository repositoryLedgerRepository;

    @Autowired
    UtilsService utilsService;

    @Autowired
    GenCodeService genCodeService;

    @Autowired
    UtilsRepository utilsRepository;

    @Autowired
    RefVoucherRepository refVoucherRepository;

    @Autowired
    UserService userService;

    @Autowired
    GeneralLedgerService generalLedgerService;

    public RSTranferServiceImpl(RSTransferRepository rSTranferRepository, IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository, SaBillRepository saBillRepository, OrganizationUnitRepository organizationUnitRepository, TypeRepository typeRepository, RepositoryLedgerRepository repositoryLedgerRepository) {
        this.rSTranferRepository = rSTranferRepository;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.saBillRepository = saBillRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.typeRepository = typeRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
    }

    /**
     * Save a rSTranfer.
     *
     * @param rSTransfer the entity to save
     * @return the persisted entity
     */
    @Override
    public RSTransferSaveDTO save(RSTransferSaveDTO rSTransfer) {
        log.debug("Request to save RSTranfer : {}", rSTransfer);
        RSTransfer rsTransfer = rSTransfer.getRsTransfer();
        int typeGroupID = 0;
        if (rsTransfer.getTypeID() == TypeConstant.CHUYEN_KHO ||
            rsTransfer.getTypeID() == TypeConstant.CHUYEN_KHO_GUI_DAI_LY ||
            rsTransfer.getTypeID() == TypeConstant.CHUYEN_KHO_CHUYEN_NOI_BO) {
            typeGroupID = TypeGroupConstant.RS_TRANSFER;
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent()) {
            rsTransfer.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            boolean isUpdateGenCode = false;
            boolean isUpdateNoBook = true;
            RSTransfer rsTransferOld = new RSTransfer();
            if (rsTransfer.getId() != null) {
                Optional<RSTransfer> rsTransferOptional = rSTranferRepository.findById(rsTransfer.getId());
                isUpdateGenCode = rsTransferOptional.isPresent();
                if (isUpdateGenCode) {
                    rsTransferOld = rsTransferOptional.get();
                }
            }

            // BOTH_BOOK to One book
            if (rsTransferOld.getTypeLedger() != null && !rsTransferOld.getTypeLedger().equals(rsTransfer.getTypeLedger())) {
                if (rsTransferOld.getTypeLedger().equals(Constants.TypeLedger.BOTH_BOOK)) {
                    if (rsTransfer.getTypeLedger().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        rsTransferOld.setNoMBook(null);
                    } else {
                        rsTransferOld.setNoFBook(null);
                    }
                }
            }

            // end
            if (rsTransfer.getTypeLedger() != null) {
                if (rSTransfer.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                    if (rsTransferOld.getNoFBook() != null && rsTransferOld.getNoFBook().equals(rsTransfer.getNoFBook())) {
                        isUpdateNoBook = false;
                    }
                    rsTransferOld.setNoFBook(rsTransfer.getNoFBook());
                } else {
                    if (rsTransferOld.getNoMBook() != null && rsTransferOld.getNoMBook().equals(rsTransfer.getNoMBook())) {
                        isUpdateNoBook = false;
                    }
                    rsTransferOld.setNoMBook(rsTransfer.getNoMBook());
                }
                if (rsTransfer.getTypeLedger().equals(Constants.TypeLedger.BOTH_BOOK) && (rsTransferOld.getId() == null || !rsTransfer.getTypeLedger().equals(rsTransferOld.getTypeLedger()))) {
                    GenCode genCode = genCodeService.findWithTypeID(typeGroupID,
                        rSTransfer.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK) ? Constants.TypeLedger.MANAGEMENT_BOOK : Constants.TypeLedger.FINANCIAL_BOOK);
                    String codeVoucher = Utils.codeVoucher(genCode);
                    if (rSTransfer.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        rsTransferOld.setNoMBook(codeVoucher);
                    } else {
                        rsTransferOld.setNoFBook(codeVoucher);
                    }
                    isUpdateNoBook = true;
                }
                rsTransferOld.setTypeLedger(rsTransfer.getTypeLedger());
                if (isUpdateNoBook) {
                    if (!utilsRepository.checkDuplicateNoVoucher(rsTransferOld.getNoFBook(),
                        rsTransferOld.getNoMBook(),
                        rsTransferOld.getTypeLedger(), rsTransfer.getId())) {
                        throw new BadRequestAlertException("Trùng số chứng từ chuyển kho", "rSTransfer", "duplicateNo");
                    }
                }
                rsTransfer.setNoFBook(rsTransferOld.getNoFBook());
                rsTransfer.setNoMBook(rsTransferOld.getNoMBook());
            }

            rsTransfer.setTotalAmountOriginal(rsTransfer.getTotalAmountOriginal());
            rsTransfer.getRsTransferDetails().forEach(item -> {
                item.setAmount(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
                item.setAmountOriginal(item.getAmount());
                item.setUnitPriceOriginal(item.getUnitPriceOriginal() != null ? item.getUnitPriceOriginal() : BigDecimal.ZERO);
                item.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
                item.setMainUnitPrice(item.getMainUnitPrice() != null ? item.getMainUnitPrice() : BigDecimal.ZERO);
//                item.setMaterialQuantumDetailID(item.getMaterialQuantumID());
                if (item.getUnit() == null) {
                    item.setMainConvertRate(new BigDecimal(1));
                    item.setFormula("*");
                    item.setMainQuantity(item.getQuantity());
                    item.setMainUnitPrice(item.getUnitPriceOriginal());
                }
            });

            boolean isRequiredInvoiceNo = user.get().getSystemOptions().stream().anyMatch(item
                -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDTichHopHDDT) && item.getData().equalsIgnoreCase("0"));

            if (!Strings.isNullOrEmpty(rsTransfer.getInvoiceNo())) {
                int count = saBillRepository.countByInvoiceNoAndCompanyID(rsTransfer.getInvoiceNo(), rsTransfer.getInvoiceForm(),
                    rsTransfer.getInvoiceTemplate(), rsTransfer.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg(),
                    rsTransfer.getId() != null ? rsTransfer.getId() : UUID.randomUUID());
                if (count > 0) {
                    throw new BadRequestAlertException("Khong the xuat hoa don", "rSInwardOutward", "duplicate");
                }
            }
            if (isRequiredInvoiceNo && rsTransfer.getInvoiceNo() == null) {
                throw new BadRequestAlertException("So hoa don bat buoc nhap", "rSInwardOutward", "invalidInvoiceNoRequired");
            } else if (rsTransfer.getInvoiceNo() != null && rsTransfer.getInvoiceForm() != null && rsTransfer.getInvoiceForm() != Constants.InvoiceForm.HD_DIEN_TU) {
                List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(
                    rsTransfer.getInvoiceForm(), rsTransfer.getInvoiceTypeID(), rsTransfer.getInvoiceTemplate(), rsTransfer.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg());
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
                        if (((rsTransfer.getInvoiceNo().compareTo(item.getFromNo()) >= 0 && item.getToNo() != null && rsTransfer.getInvoiceNo().compareTo(item.getToNo()) <= 0)
                            || ((item.getToNo() == null && rsTransfer.getInvoiceNo().compareTo(item.getFromNo()) >= 0)))) {
                            error = true;
                        }
                    }
                    if ((minFromNo != null && rsTransfer.getInvoiceNo().compareTo(minFromNo) < 0) || (maxToNo != null && rsTransfer.getInvoiceNo().compareTo(maxToNo) > 0)) {
                        throw new BadRequestAlertException("So hoa don khong thuoc dai phat hanh", "rSInwardOutward", "invalidInvoiceNo");
                    }
                }
            }

            rsTransfer = rSTranferRepository.save(rsTransfer);

//            // update PPOrderDetails
//            pporderdetailRepository.saveAll(ppOrderDetails);
//            pporderRepository.updateStatus(ppOrderDetails.stream()
//                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
//                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
            List<RefVoucher> refVouchers = rSTransfer.getViewVouchers();
            for (RefVoucher item : refVouchers) {
                item.setRefID1(rsTransfer.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(rsTransfer.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            rSTransfer.setRsTransfer(rsTransfer);
            rSTransfer.setViewVouchers(refVouchers);

            UserDTO userDTO = userService.getAccount();
            if (rSTransfer.getCheckRecordSLT() != null && !rSTransfer.getCheckRecordSLT()) {
                if (userDTO.getSystemOption()
                    .stream()
                    .filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo))
                    .findAny().get().getData().equals("0")) {
//                    if (typeGroupID == TypeGroupConstant.RS_INWARD_OUTWARD) {
                    if (generalLedgerService.record(rsTransfer, new MessageDTO(""))) {
                        rsTransfer.setRecorded(true);
                        rSTranferRepository.save(rsTransfer);
                    }
//                    } else if (typeGroupID == TypeGroupConstant.RS_OUTWARD) {
//                        if (generalLedgerService.record(rsInwardOutward, new MessageDTO("XUAT_KHO"))) {
//                            rsInwardOutward.setRecorded(true);
//                            rSInwardOutwardRepository.save(rsInwardOutward);
//                            String valueCal = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.VTHH_PPTinhGiaXKho)).findAny().get().getData();
//                            if ( valueCal != null && !valueCal.equals("")) {
//                                Integer calculationMethod = Integer.valueOf(valueCal);
//                                if (calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI) {
//                                    repositoryLedgerService.calculateOWPrice(calculationMethod, rsInwardOutward.getRsInwardOutwardDetails().stream().map(x -> x.getMaterialGood().getId()).collect(Collectors.toList()), rsInwardOutward.getDate().toString(), rsInwardOutward.getDate().toString());
//                                }
//                            }
//                        }
//                    }
                }
            }
            Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
            if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                rsTransfer.setNoMBook(null);
                rsTransfer.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
            }

//            for (RSInwardOutWardDetails rsInwardOutwardDetail : rsInwardOutward.getRsInwardOutwardDetails()) {
//                // Create From PPDiscountReturn
//                if (rsInwardOutwardDetail.getPpDiscountReturn() != null) {
//                    rsInwardOutwardDetail.setPpDiscountReturn(ppDiscountReturnRepository
//                        .findById(rsInwardOutwardDetail.getPpDiscountReturn().getId())
//                        .orElse(rsInwardOutwardDetail.getPpDiscountReturn()));
//                }
//                // Create From SaInvoice
//                if (rsInwardOutwardDetail.getSaInvoice() != null) {
//                    rsInwardOutwardDetail.setSaInvoice(saInvoiceRepository
//                        .findById(rsInwardOutwardDetail.getSaInvoice().getId())
//                        .orElse(rsInwardOutwardDetail.getSaInvoice()));
//                }
//                // Create From SaOrder
//                if (rsInwardOutwardDetail.getSaOrder() != null) {
//                    rsInwardOutwardDetail.setSaOrder(saOrderRepository
//                        .findById(rsInwardOutwardDetail.getSaOrder().getId())
//                        .orElse(rsInwardOutwardDetail.getSaOrder()));
//                }
//            }
            return rSTransfer;
        }
        throw new BadRequestAlertException("Khong the tao chuyen kho", "", "");
    }

    /**
     * Get all the rSTransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RSTransfer> findAll(Pageable pageable) {
        log.debug("Request to get all RSTransfers");
        return rSTranferRepository.findAll(pageable);
    }


    /**
     * Get one rSTransfer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RSTransfer> findOne(UUID id) {
        log.debug("Request to get RSTransfer : {}", id);
        return rSTranferRepository.findById(id);
    }

    /**
     * Delete the rSTransfer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RSTransfer : {}", id);
        rSTranferRepository.deleteById(id);
    }

    @Override
    public Page<RSTransferSearchDTO> searchAllTransfer(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSTranferRepository.searchAllTransfer(pageable, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook);
    }

    @Override
    public Page<RSTransferSearchDTO> searchAll(Pageable pageable, UUID accountingObject, UUID repository, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSTranferRepository.searchAll(pageable, accountingObject, repository, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook);
    }

    @Override
    public List<RSTransferDetailsDTO> getDetailsTransferById(UUID id, Integer typeID) {
        return rSTranferRepository.getDetailsTransferById(id, typeID);
    }

    @Override
    public RSTransferSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSTranferRepository.findReferenceTablesID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, rowNum, currentBook);
    }

    @Override
    public Integer findRowNumID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSTranferRepository.findRowNumByID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, id, currentBook);
    }

    @Override
    public byte[] exportPdf(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<RSTransferSearchDTO> rsTransfers = currentUserLoginAndOrg.map(securityDTO ->
                rSTranferRepository.searchAllTransfer(null, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsTransfers.getContent(), ExcelConstant.RSTransfer.HEADER, ExcelConstant.RSTransfer.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsTransfers.getContent(), ExcelConstant.RSTransfer.HEADER, ExcelConstant.RSTransfer.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public byte[] exportExcel(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<RSTransferSearchDTO> rsTransfers = currentUserLoginAndOrg.map(securityDTO ->
                rSTranferRepository.searchAllTransfer(null, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsTransfers.getContent(), ExcelConstant.RSTransfer.HEADER, ExcelConstant.RSTransfer.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsTransfers.getContent(), ExcelConstant.RSTransfer.HEADER, ExcelConstant.RSTransfer.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public HandlingResultDTO multiDelete(List<RSTransfer> rsTransfers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(rsTransfers.size());
        List<RSTransfer> listDelete = rsTransfers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (int i = 0; i < rsTransfers.size(); i++) {
            LocalDate postedDate = LocalDate.parse(rsTransfers.get(i).getPostedDate().toString());
            if (Boolean.TRUE.equals(rsTransfers.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                if (Boolean.TRUE.equals(rsTransfers.get(i).getRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.isAfter(postedDate)) {
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                } else {
                    viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                }
                BeanUtils.copyProperties(rsTransfers.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(rsTransfers.get(i));
            }
//            if (Boolean.TRUE.equals(rsInwardOutward.get(i).getRefIsBill())) {
//                if (rsInwardOutward.get(i).getRefInvoiceNo() != null && rsInwardOutward.get(i).getRefInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
//                    userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
//                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
//                    viewVoucherNo.setReasonFail("Hóa đơn đã cấp số !");
//                    BeanUtils.copyProperties(rsInwardOutward.get(i), viewVoucherNo);
//                    viewVoucherNoListFail.add(viewVoucherNo);
//                    listDelete.remove(rsInwardOutward.get(i));
//                }
//            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "rsInwardOutward", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_Chuyen_Kho_KNB = new ArrayList<>();
        List<UUID> uuidList_Chuyen_Kho_GDL = new ArrayList<>();
        List<UUID> uuidList_Chuyen_Kho_NB = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_CK_KNB) {
                uuidList_Chuyen_Kho_KNB.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_CK_GDL) {
                uuidList_Chuyen_Kho_GDL.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_CK_NB) {
                uuidList_Chuyen_Kho_NB.add(listDelete.get(i).getId());
            }
        }
        // Gan' TypeName
        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName(typeRepository.findTypeNameByTypeID(viewVoucherNoListFail.get(i).getTypeID()));
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu Chuyen Kho KNB, Chuyen Kho GDL, Chuyen Kho NB
        if (uuidList_Chuyen_Kho_KNB.size() > 0) {
            rSTranferRepository.multiDeleteRSTransfer(currentUserLoginAndOrg.get().getOrg(), uuidList_Chuyen_Kho_KNB);
            rSTranferRepository.multiDeleteChildRSTransfer("RSTransferDetail", uuidList_Chuyen_Kho_KNB);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_Chuyen_Kho_KNB);
        }
        if (uuidList_Chuyen_Kho_GDL.size() > 0) {
            rSTranferRepository.multiDeleteRSTransfer(currentUserLoginAndOrg.get().getOrg(), uuidList_Chuyen_Kho_GDL);
            rSTranferRepository.multiDeleteChildRSTransfer("RSTransferDetail", uuidList_Chuyen_Kho_GDL);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_Chuyen_Kho_GDL);
        }
        if (uuidList_Chuyen_Kho_NB.size() > 0) {
            rSTranferRepository.multiDeleteRSTransfer(currentUserLoginAndOrg.get().getOrg(), uuidList_Chuyen_Kho_NB);
            rSTranferRepository.multiDeleteChildRSTransfer("RSTransferDetail", uuidList_Chuyen_Kho_NB);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_Chuyen_Kho_NB);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<RSTransfer> rsTransfers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(rsTransfers.size());
        List<RSTransfer> listDelete = rsTransfers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (RSTransfer rs: rsTransfers) {
            LocalDate postedDate = LocalDate.parse(rs.getPostedDate().toString());
            if (rs.getRecorded() != null && !rs.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ!");
                viewVoucherNo.setPostedDate(rs.getPostedDate());
                viewVoucherNo.setDate(rs.getDate());
                viewVoucherNo.setNoFBook(rs.getNoFBook());
                viewVoucherNo.setNoMBook(rs.getNoMBook());
                if (rs.getTypeID() == CHUYEN_KHO) {
                    viewVoucherNo.setTypeName("Chuyển kho (Xuất kho kiêm vận chuyển nội bộ)");
                } else if (rs.getTypeID() == CHUYEN_KHO_GUI_DAI_LY) {
                    viewVoucherNo.setTypeName("Chuyển kho (Xuất gửi đại lý)");
                } else if (rs.getTypeID() == CHUYEN_KHO_CHUYEN_NOI_BO) {
                    viewVoucherNo.setTypeName("Chuyển kho (Xuất chuyển nội bộ)");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(rs);
            } else {
                if (Boolean.TRUE.equals(rs.getRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.isAfter(postedDate)) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                    viewVoucherNo.setPostedDate(rs.getPostedDate());
                    viewVoucherNo.setDate(rs.getDate());
                    viewVoucherNo.setNoFBook(rs.getNoFBook());
                    viewVoucherNo.setNoMBook(rs.getNoMBook());
                    if (rs.getTypeID() == CHUYEN_KHO) {
                        viewVoucherNo.setTypeName("Chuyển kho (Xuất kho kiêm vận chuyển nội bộ)");
                    } else if (rs.getTypeID() == CHUYEN_KHO_GUI_DAI_LY) {
                        viewVoucherNo.setTypeName("Chuyển kho (Xuất gửi đại lý)");
                    } else if (rs.getTypeID() == CHUYEN_KHO_CHUYEN_NOI_BO) {
                        viewVoucherNo.setTypeName("Chuyển kho (Xuất chuyển nội bộ)");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(rs);
                }
            }
        }

        List<UUID> uuidListCKKNB = new ArrayList<>();
        List<UUID> uuidListGDL = new ArrayList<>();
        List<UUID> uuidListNB = new ArrayList<>();
        for (RSTransfer rs: listDelete) {
            uuidListCKKNB.add(rs.getId());
            uuidListGDL.add(rs.getId());
            uuidListNB.add(rs.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListCKKNB.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        if (uuidListCKKNB.size() > 0) {
            rSTranferRepository.updateUnrecordRS(uuidListCKKNB);
            repositoryLedgerRepository.deleteAllByReferenceID(uuidListCKKNB);
        }
        if (uuidListGDL.size() > 0) {
            rSTranferRepository.updateUnrecordRS(uuidListGDL);
            repositoryLedgerRepository.deleteAllByReferenceID(uuidListGDL);
        }
        if (uuidListNB.size() > 0) {
            rSTranferRepository.updateUnrecordRS(uuidListNB);
            repositoryLedgerRepository.deleteAllByReferenceID(uuidListNB);
        }
        return handlingResultDTO;
    }
}
