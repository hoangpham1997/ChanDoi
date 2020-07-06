package vn.softdreams.ebweb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
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
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * Service Implementation for managing PPDiscountReturn.
 */
@Service
@Transactional
public class PPDiscountReturnServiceImpl implements PPDiscountReturnService {

    private final Logger log = LoggerFactory.getLogger(PPDiscountReturnServiceImpl.class);

    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;

    private final PPDiscountReturnRepository pPDiscountReturnRepository;

    private final PporderdetailRepository pporderdetailRepository;

    private final PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository;

    private final RSInwardOutwardRepository rsInwardOutwardRepository;

    private final SaBillRepository saBillRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final UserService userService;

    private final GeneralLedgerService generalLedgerService;

    private final GenCodeService genCodeService;

    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";

    private final UtilsRepository utilsRepository;

    private final PPInvoiceRepository ppInvoiceRepository;

    private final EInvoiceService eInvoiceService;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final SaBillDetailsRepository saBillDetailsRepository;

    private final UtilsService utilsService;

    private final MaterialGoodsRepository materialGoodsRepository;

    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;


    public PPDiscountReturnServiceImpl(IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository, PPDiscountReturnRepository pPDiscountReturnRepository,
                                       PporderdetailRepository pporderdetailRepository, PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository,
                                       RSInwardOutwardRepository rsInwardOutwardRepository,
                                       SaBillRepository saBillRepository,
                                       RefVoucherRepository refVoucherRepository,
                                       UserService userService,
                                       GeneralLedgerService generalLedgerService,
                                       GenCodeService genCodeService,
                                       UtilsRepository utilsRepository,
                                       PPInvoiceRepository ppInvoiceRepository,
                                       EInvoiceService eInvoiceService,
                                       OrganizationUnitRepository organizationUnitRepository, SaBillDetailsRepository saBillDetailsRepository, UtilsService utilsService, MaterialGoodsRepository materialGoodsRepository, RepositoryLedgerRepository repositoryLedgerRepository, MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.pPDiscountReturnRepository = pPDiscountReturnRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.ppDiscountReturnDetailsRepository = ppDiscountReturnDetailsRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.saBillRepository = saBillRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.generalLedgerService = generalLedgerService;
        this.genCodeService = genCodeService;
        this.utilsRepository = utilsRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.eInvoiceService = eInvoiceService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.saBillDetailsRepository = saBillDetailsRepository;
        this.utilsService = utilsService;
        this.materialGoodsRepository = materialGoodsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    /**
     * Save a pPDiscountReturn.
     *
     * @return the persisted entity
     */
    @Override
    @Transactional
    public DiscountAndBillAndRSIDTO save(DiscountAndBillAndRSIDTO discountAndBillAndRSIDTO) {
        log.debug("Request to save PPDiscountReturn : {}", discountAndBillAndRSIDTO);
//        try{
//        lấy dữ liệu 3 bảng
        List<UUID> ppInvoiceDetailIDs = new ArrayList<>();
        PPDiscountReturn ppDiscountReturn = discountAndBillAndRSIDTO.getPpDiscountReturn();
        DiscountAndBillAndRSIDTO discountAndBillAndRSIDTO1 = new DiscountAndBillAndRSIDTO();
        PPDiscountReturn ppDiscountReturns = null;
        ppDiscountReturn.setRecorded(false);
        RSInwardOutward rsInwardOutward = discountAndBillAndRSIDTO.getRsInwardOutward();
        SaBill saBill = discountAndBillAndRSIDTO.getSaBill();
        Integer typeGroupDiscountReturnID = null;
        List<PPDiscountReturnDetails> pp = new ArrayList<>(ppDiscountReturn.getPpDiscountReturnDetails());
        if (ppDiscountReturn.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }
        if (discountAndBillAndRSIDTO.getStatusPurchase()) {
            typeGroupDiscountReturnID = TypeGroupConstant.HANG_MUA_GIAM_GIA;
            ppDiscountReturn.setTypeID(MUA_HANG_GIAM_GIA);
        } else {
            typeGroupDiscountReturnID = TypeGroupConstant.HANG_MUA_TRA_LAI;
            ppDiscountReturn.setTypeID(MUA_HANG_TRA_LAI);
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            // check sổ làm việc
            UserDTO userDTO = userService.getAccount();
            if (Constants.TypeLedger.BOTH_BOOK.equals(ppDiscountReturn.getTypeLedger())) {
                if (currentBook.equals("0")) {
                    if (ppDiscountReturn.getId() == null) {
                        ppDiscountReturn.setNoMBook(genCodeService.getCodeVoucher(typeGroupDiscountReturnID, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                    if (ppDiscountReturn.getIsDeliveryVoucher() != null && ppDiscountReturn.getIsDeliveryVoucher() && rsInwardOutward.getId() == null) {
                        rsInwardOutward.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (ppDiscountReturn.getId() == null) {
                        ppDiscountReturn.setNoFBook(genCodeService.getCodeVoucher(typeGroupDiscountReturnID, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                    if (ppDiscountReturn.getIsDeliveryVoucher() != null && ppDiscountReturn.getIsDeliveryVoucher() && rsInwardOutward.getId() == null) {
                        rsInwardOutward.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            } else if (Constants.TypeLedger.MANAGEMENT_BOOK.equals(ppDiscountReturn.getTypeLedger())) {
                if (rsInwardOutward.getNoFBook() != null) {
                    rsInwardOutward.setNoFBook(null);
                }
                if (ppDiscountReturn.getNoFBook() != null) {
                    ppDiscountReturn.setNoFBook(null);
                }
            } else if (Constants.TypeLedger.FINANCIAL_BOOK.equals(ppDiscountReturn.getTypeLedger())) {
                if (rsInwardOutward.getNoMBook() != null) {
                    rsInwardOutward.setNoMBook(null);
                }
                if (ppDiscountReturn.getNoMBook() != null) {
                    ppDiscountReturn.setNoMBook(null);
                }
            }
            if (!discountAndBillAndRSIDTO.getStatusPurchase()) {
                if (ppDiscountReturn.getIsDeliveryVoucher() != null && ppDiscountReturn.getIsDeliveryVoucher()) {
                    rsInwardOutward.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                    rsInwardOutward.setTypeID(KIEM_XUAT_KHO);
                    if (!utilsRepository.checkDuplicateNoVoucher(rsInwardOutward.getNoFBook(), rsInwardOutward.getNoMBook(), rsInwardOutward.getTypeLedger(), rsInwardOutward.getId())) {
                        throw new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
                    }
                    RSInwardOutward rs = rsInwardOutwardRepository.save(rsInwardOutward);
                    ppDiscountReturn.setRsInwardOutwardID(rs.getId());
                    discountAndBillAndRSIDTO1.setRsInwardOutward(rs);
                } else {
                    if (ppDiscountReturn.getRsInwardOutwardID() != null) {
                        rsInwardOutwardRepository.deleteById(ppDiscountReturn.getRsInwardOutwardID());
                        ppDiscountReturn.setRsInwardOutwardID(null);
                    }
                }
//        tạo mới hóa đơn
                if (ppDiscountReturn.getIsBill() != null && ppDiscountReturn.getIsBill()) {
                    boolean isRequiredInvoiceNo = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream().anyMatch(item
                        -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDTichHopHDDT) && item.getData().equalsIgnoreCase("0"));
                    int count = saBillRepository.countByInvoiceNoAndCompanyID(saBill.getInvoiceNo(), saBill.getInvoiceForm(),
                        saBill.getInvoiceTemplate(), saBill.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg(),
                        saBill.getId() != null ? saBill.getId() : UUID.randomUUID());
                    if (count > 0) {
                        throw new BadRequestAlertException("Khong the xuat hoa don", "saBill", "duplicate");
                    }
                    if (saBill.getInvoiceNo() == null) {
                        if (isRequiredInvoiceNo) {
                            throw new BadRequestAlertException("So hoa don bat buoc nhap", "saBill", "invalidInvoiceNoRequired");
                        }
                    } else {
                        List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(
                            saBill.getInvoiceForm(), saBill.getInvoiceTypeID(), saBill.getInvoiceTemplate(), saBill.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg());
                        if (pTemplate.size() > 0) {
                            Boolean error = true;
                            for (IaPublishInvoiceDetails item : pTemplate) {
                                if ((item.getFromNo() == null || saBill.getInvoiceNo().compareTo(item.getFromNo()) >= 0) && (item.getToNo() == null || saBill.getInvoiceNo().compareTo(item.getToNo()) <= 0)) {
                                    error = false;
                                }
                            }
                            if (error) {
                                throw new BadRequestAlertException("So hoa don khong thuoc dai phat hanh", "saBill", "invalidInvoiceNo");
                            }
                        }
                    }
//                }
                    saBill.setTypeID(XUAT_HOA_DON_TRA_LAI);
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
                    saBill.setStatusInvoice(0);
                    saBill = saBillRepository.save(saBill);
                    discountAndBillAndRSIDTO1.setSaBill(saBill);
                    /*
                     * Add by Hautv
                     *Create EInvoice
                     *  result trả về null là không có lỗi xảy ra
                     * */
                    //region
                    if (ppDiscountReturn.getIsBill() != null && ppDiscountReturn.getIsBill() && ppDiscountReturn.getInvoiceForm().equals(0) && ppDiscountReturn.getTypeID().equals(220)) {
                        Respone_SDS result = new Respone_SDS();
                        if (saBill.getStatusInvoice() == null) {
                            saBill.setStatusInvoice(0);
                        }
                        switch (saBill.getStatusInvoice()) {
                            case 7:
                                result = eInvoiceService.createEInoviceReplaced(saBill.getId());
                                break;
                            case 8:
                                result = eInvoiceService.createEInoviceAdjusted(saBill.getId());
                                break;
                            case 6:
                            case 0:
                                result = eInvoiceService.createEInvoice(saBill.getId());
                                break;
                        }
                        if (Constants.EInvoice.Respone.Success.equals(result.getStatus())) {
                            if (Constants.EInvoice.SupplierCode.MIV.equals(Utils.getEI_IDNhaCungCapDichVu(userDTO))) {
                                if (!StringUtils.isEmpty(result.getInvoiceNo())) {
                                    ppDiscountReturns.setInvoiceNo(result.getInvoiceNo());
                                }
                            }
                        }
                    }
//                thêm dữ liệu vào ppdiscountreturndetail
                    List<SaBillDetails> saBillDetails = new ArrayList<>(saBill.getSaBillDetails());
                    int i = 0;
                    for (PPDiscountReturnDetails ppDiscountReturnDetails : ppDiscountReturn.getPpDiscountReturnDetails()) {
                        ppDiscountReturnDetails.setSaBillID(saBill.getId());
                        if (saBillDetails.get(i).getId() != null) {
                            ppDiscountReturnDetails.setSaBillDetailID(saBillDetails.get(i).getId());
                        }
                        i++;
                    }
                } else {
                    if (pp.get(0).getSaBillID() != null && ppDiscountReturn.getIsExportInvoice() != null && !ppDiscountReturn.getIsExportInvoice()) {
                        saBillRepository.deleteById(pp.get(0).getSaBillID());
                        for (PPDiscountReturnDetails ppDiscountReturnDetails : ppDiscountReturn.getPpDiscountReturnDetails()) {
                            ppDiscountReturnDetails.setSaBillID(null);
                            ppDiscountReturnDetails.setSaBillDetailID(null);
                        }
                    }
                }
//            for (PPDiscountReturnDetails ppDiscountReturnDetails : pp) {
//                if (ppDiscountReturnDetails.getPpInvoiceDetailID() != null) {
//                    if (ppDiscountReturnDetails.getPpInvoiceQuantity() != null && ppDiscountReturnDetails.getQuantity() != null) {
//                        if (ppDiscountReturnDetails.getPpInvoiceQuantity().compareTo(ppDiscountReturnDetails.getQuantity()) >= 0) {
//                            ppInvoiceRepository.updateQuantity(ppDiscountReturnDetails.getPpInvoiceDetailID(), ppDiscountReturnDetails.getQuantity(), true);
//                        } else {
//                            throw new BadRequestAlertException("minusError", "voucherExists", "");
//                        }
//                    }
//                }
//            }
            }
            ppDiscountReturn.setCompanyID(currentUserLoginAndOrg.get().getOrg());
//            ppDiscountReturn.setPpDiscountReturnDetails(new HashSet<>(pp));
            if (!utilsRepository.checkDuplicateNoVoucher(ppDiscountReturn.getNoFBook(), ppDiscountReturn.getNoMBook(), ppDiscountReturn.getTypeLedger(), ppDiscountReturn.getId())) {
                throw new BadRequestAlertException("discountreturn", "voucherExists", "");
            }
            ppDiscountReturns = pPDiscountReturnRepository.save(ppDiscountReturn);

            materialGoodsSpecificationsLedgerRepository.deleteByRefID(ppDiscountReturns.getId());
            List<PPDiscountReturnDetails> ppDiscountReturnDetailsSave = new ArrayList<>(ppDiscountReturns.getPpDiscountReturnDetails());
            List<PPDiscountReturnDetails> ppDiscountReturnDetails = new ArrayList<>(discountAndBillAndRSIDTO.getPpDiscountReturn().getPpDiscountReturnDetails());
            List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
            for (int i = 0; i < ppDiscountReturnDetails.size(); i++) {
                if (ppDiscountReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                    ppDiscountReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                    for (MaterialGoodsSpecificationsLedger item: ppDiscountReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers()) {
                        item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                        item.setDate(ppDiscountReturns.getDate());
                        item.setPostedDate(ppDiscountReturns.getPostedDate());
                        item.setTypeLedger(ppDiscountReturns.getTypeLedger());
                        item.setRefTypeID(ppDiscountReturns.getTypeID());
                        item.setReferenceID(ppDiscountReturns.getId());
                        item.setDetailID(ppDiscountReturnDetailsSave.get(i).getId());
                        item.setNoFBook(ppDiscountReturns.getNoFBook());
                        item.setNoMBook(ppDiscountReturns.getNoMBook());
                        item.setiWRepositoryID(null);
                        item.setiWQuantity(BigDecimal.ZERO);
                        item.setiWRepositoryID(ppDiscountReturnDetailsSave.get(i).getRepositoryID());
                        materialGoodsSpecificationsLedgers.add(item);
                    }
                }
            }
            if (materialGoodsSpecificationsLedgers.size() > 0) {
                materialGoodsSpecificationsLedgerRepository.saveAll(materialGoodsSpecificationsLedgers);
            }

            List<RefVoucher> refVouchers = discountAndBillAndRSIDTO.getViewVouchers();
            List<RefVoucher> refVouchersForRef = new ArrayList<>();
            for (RefVoucher item : refVouchers) {
                item.setRefID1(ppDiscountReturns.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            if (refVouchersForRef.size() > 0) {
                refVouchers.addAll(refVouchersForRef);
            }
            refVoucherRepository.deleteByRefID1(ppDiscountReturns.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            discountAndBillAndRSIDTO1.setViewVouchers(refVouchers);
            if (discountAndBillAndRSIDTO.getRecord() == null || discountAndBillAndRSIDTO.getRecord()) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                    MessageDTO msg = new MessageDTO("");
                    if (!generalLedgerService.record(ppDiscountReturns, msg)) {
                        ppDiscountReturns.setRecorded(false);
                    } else {
                        ppDiscountReturns.setRecorded(true);
                    }
                }
            }
        }
//        ppDiscountReturn.setPpDiscountReturnDetails(new HashSet<>(pp));
        discountAndBillAndRSIDTO1.setPpDiscountReturn(pPDiscountReturnRepository.save(ppDiscountReturns));

        for (PPDiscountReturnDetails ppDiscountReturnDetails : ppDiscountReturn.getPpDiscountReturnDetails()) {
            if (ppDiscountReturnDetails.getPpInvoiceDetailID() != null) {
//                pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(ppDiscountReturnDetails.getPpInvoiceDetailID());
                ppInvoiceDetailIDs.add(ppDiscountReturnDetails.getPpInvoiceDetailID());
            }
        }
        // cập nhật số lượng nhận trong bảng ppoder
        if (ppInvoiceDetailIDs != null && ppInvoiceDetailIDs.size() > 0) {
            List<UUID> ppOderDetailsID = ppInvoiceRepository.getPPOrderDetailIDSByListID(ppInvoiceDetailIDs);
            if (ppOderDetailsID != null && ppOderDetailsID.size() > 0) {
                pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(ppOderDetailsID);
            }
        }

        return discountAndBillAndRSIDTO1;
//        } catch (Exception e){
//            throw new BadRequestAlertException("lỗi2", "lỗi", "idnull");
//        }
    }

    @Override
    public PPDiscountReturn save1(PPDiscountReturn pPDiscountReturn) {
        return pPDiscountReturnRepository.save(pPDiscountReturn);
    }

    /**
     * Get all the pPDiscountReturns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPDiscountReturn> findAll(Pageable pageable) {
        log.debug("Request to get all PPDiscountReturns");
        return pPDiscountReturnRepository.findAll(pageable);
    }

    @Override
    public Optional<PPDiscountReturn> findOneByID(UUID id) {
        return Optional.empty();
    }

    /**
     * Get one pPDiscountReturn by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPDiscountReturnDTO> findOne(UUID id) {
        log.debug("Request to get PPDiscountReturn : {}", id);
        Optional<PPDiscountReturnDTO> pp = pPDiscountReturnRepository.findOneByID(id);
        UserDTO userDTO = userService.getAccount();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        Set<PPDiscountReturnDetailDTO> ppDiscountReturnDetails = new HashSet<>(ppDiscountReturnDetailsRepository.getPPDiscountReturnDetailsByID(id, currentBook));
        pp.get().setPpDiscountReturnDetails(ppDiscountReturnDetails);
//        UserDTO userDTO = userService.getAccount();
//        if (pp.get().getPpDiscountReturnDetails().size() > 0) {
//            pp.get().getPpDiscountReturnDetails().forEach(item -> {
//                if (item.getPpInvoiceID() != null) {
//                    PPInvoiceConvertDateAndNoBookDTO ppInVoice = ppInvoiceRepository.findDateAndNoBookByID(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), item.getPpInvoiceID());
//                    if (ppInVoice != null) {
//                        if (ppInVoice.getPpInVoiceDate() != null) {
//                            item.setPpInVoiceDate(ppInVoice.getPpInVoiceDate());
//                        }
//                        if (ppInVoice.getPpInVoiceNoBook() != null) {
//                            item.setPpInVoiceNoBook(ppInVoice.getPpInVoiceNoBook());
//                        }
//                    }
//                }
//            });
//        }
        return pp;
    }
//
//    /**
//     * Delete the pPDiscountReturn by id.
//     *
//     * @param ListID of the entity
//     */
//    @Override
//    public void multipleDelete(List<UUID> ListID) {
//        log.debug("Request to delete PPDiscountReturn : {}", ListID);
//        List<PPDiscountReturn> ppDiscountReturns = pPDiscountReturnRepository.findByListId(ListID);
//        List<UUID> rsInwardOutwardIds = new ArrayList<>();
//        List<UUID> saBillIds = new ArrayList<>();
//        for (PPDiscountReturn pp : ppDiscountReturns)
//        {
//            if (pp.getIsBill() != null && pp.getIsBill()) {
//                List<PPDiscountReturnDetails> ppDiscountReturnsDetails = new ArrayList<>(pp.getPpDiscountReturnDetails());
//                if (ppDiscountReturnsDetails.size() > 0 && ppDiscountReturnsDetails.get(0).getSaBillID() != null) {
//                    saBillIds.add(ppDiscountReturnsDetails.get(0).getSaBillID());
//                }
//            }
//            if (pp.getIsDeliveryVoucher() && pp.getRsInwardOutwardID() != null) {
//                rsInwardOutwardIds.add(pp.getRsInwardOutwardID());
//            }
//        }
//        if (saBillIds.size() > 0) {
//            if (saBillRepository.countByIds(saBillIds) > 0) {
//                saBillRepository.deleteByListId(saBillIds);
//                saBillDetailsRepository.deleteByListIds(saBillIds);
//            }
//        }
//        if (rsInwardOutwardIds.size() > 0) {
//            if (rsInwardOutwardRepository.countByListId(rsInwardOutwardIds) > 0) {
//                rsInwardOutwardRepository.deleteByPPDiscountReturnIds(rsInwardOutwardIds);
//            }
//        }
//        Long count = refVoucherRepository.countAllByRefID1sOrRefID2s(ListID);
//        if (count > 0) {
//            refVoucherRepository.deleteByRefID1sOrRefID2s(ListID);
//        }
//        pPDiscountReturnRepository.deleteByIds(ListID);
//        ppDiscountReturnDetailsRepository.deleteByIds(ListID);
//
//    }

    @Override
    public Long countFromRSI(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return pPDiscountReturnRepository.countFromRSI(id, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public HandlingResultDTO multiDelete(List<UUID> listID) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<UUID> ppInvoiceDetailIDs = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        List<PPDiscountReturn> ppDiscountReturns = pPDiscountReturnRepository.findByListId(listID);
        handlingResultDTO.setCountTotalVouchers(ppDiscountReturns.size());
        List<PPDiscountReturn> listDelete = ppDiscountReturns.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (PPDiscountReturn pp : ppDiscountReturns) {
            if (Boolean.TRUE.equals(pp.getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                viewVoucherNo.setPostedDate(pp.getPostedDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                if (pp.getTypeID().compareTo(HANG_BAN_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng mua trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng mua giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            } else {
                LocalDate postedDate = LocalDate.parse(pp.getPostedDate().toString());
                if (closeDateStr != null && Boolean.TRUE.equals(pp.getRecorded()) && !closeDateStr.equals("") && dateClosed != null && dateClosed.compareTo(postedDate) >= 0) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                    viewVoucherNo.setPostedDate(pp.getPostedDate());
                    viewVoucherNo.setDate(pp.getDate());
                    viewVoucherNo.setNoFBook(pp.getNoFBook());
                    viewVoucherNo.setNoMBook(pp.getNoMBook());
                    if (pp.getTypeID().compareTo(MUA_HANG_TRA_LAI) == 0) {
                        viewVoucherNo.setTypeName("Hàng mua trả lại");
                    } else {
                        viewVoucherNo.setTypeName("Hàng mua giảm giá");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(pp);
                } else if (Boolean.TRUE.equals(pp.getIsBill())) {
                    if (pp.getInvoiceNo() != null && pp.getInvoiceForm() != null && pp.getInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
                        userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
                        ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                        viewVoucherNo.setReasonFail("Hóa đơn đã cấp số !");
                        viewVoucherNo.setPostedDate(pp.getPostedDate());
                        viewVoucherNo.setDate(pp.getDate());
                        viewVoucherNo.setNoFBook(pp.getNoFBook());
                        viewVoucherNo.setNoMBook(pp.getNoMBook());
                        if (pp.getTypeID().compareTo(MUA_HANG_TRA_LAI) == 0) {
                            viewVoucherNo.setTypeName("Hàng mua trả lại");
                        } else {
                            viewVoucherNo.setTypeName("Hàng mua giảm giá");
                        }
                        viewVoucherNoListFail.add(viewVoucherNo);
                        listDelete.remove(pp);
                    }
                }
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidListSA = new ArrayList<>();
        for (PPDiscountReturn pp : listDelete) {
            if (pp.getRsInwardOutwardID() != null) {
                uuidListRS.add(pp.getRsInwardOutwardID());
            }
            if (pp.getIsBill() != null && pp.getIsBill()) {
                uuidListSA.add(new ArrayList<>(pp.getPpDiscountReturnDetails()).get(0).getSaBillID());
            }
            uuidList.add(pp.getId());
            for (PPDiscountReturnDetails ppdtl : pp.getPpDiscountReturnDetails()) {
                if (ppdtl.getPpInvoiceDetailID() != null) {
//                    pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(ppdtl.getPpInvoiceDetailID());
                ppInvoiceDetailIDs.add(ppdtl.getPpInvoiceDetailID());
                }
            }
        }
        if (uuidListSA.size() > 0) {
            if (saBillRepository.countByIds(uuidListSA) > 0) {
                saBillRepository.deleteByListId(uuidListSA);
                saBillDetailsRepository.deleteByListIds(uuidListSA);
            }
        }
        if (uuidListRS.size() > 0) {
            if (rsInwardOutwardRepository.countByListId(uuidListRS) > 0) {
                rsInwardOutwardRepository.deleteByPPDiscountReturnIds(uuidListRS);
            }
        }
        int count = 0;
        if (uuidList.size() > 0) {
            count = refVoucherRepository.countAllByRefID1sOrRefID2s(uuidList);
            if (count > 0) {
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            }
            pPDiscountReturnRepository.deleteByIds(uuidList);
            ppDiscountReturnDetailsRepository.deleteByIds(uuidList);
        }
        // cập nhật số lượng nhận trong bảng ppoder
        if (ppInvoiceDetailIDs != null && ppInvoiceDetailIDs.size() > 0) {
            List<UUID> ppOderDetailsID = ppInvoiceRepository.getPPOrderDetailIDSByListID(ppInvoiceDetailIDs);
            if (ppOderDetailsID != null && ppOderDetailsID.size() > 0) {
                pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(ppOderDetailsID);
            }
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<PPDiscountReturn> ppDiscountReturns) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(ppDiscountReturns.size());
        List<PPDiscountReturn> listDelete = ppDiscountReturns.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (PPDiscountReturn pp: ppDiscountReturns) {
            LocalDate postedDate = LocalDate.parse(pp.getPostedDate().toString());
            if (pp.getRecorded() != null && !pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ!");
                viewVoucherNo.setPostedDate(pp.getPostedDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setRefID(pp.getId());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                if (pp.getTypeID().compareTo(MUA_HANG_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng mua trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng mua giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            } else {
                if (Boolean.TRUE.equals(pp.getRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.compareTo(postedDate) >= 0) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                    viewVoucherNo.setPostedDate(pp.getPostedDate());
                    viewVoucherNo.setDate(pp.getDate());
                    viewVoucherNo.setRefID(pp.getId());
                    viewVoucherNo.setNoFBook(pp.getNoFBook());
                    viewVoucherNo.setNoMBook(pp.getNoMBook());
                    if (pp.getTypeID().compareTo(MUA_HANG_TRA_LAI) == 0) {
                        viewVoucherNo.setTypeName("Hàng mua trả lại");
                    } else {
                        viewVoucherNo.setTypeName("Hàng mua giảm giá");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(pp);
                }
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        for (PPDiscountReturn pp: listDelete) {
            if (pp.getRsInwardOutwardID() != null) {
                uuidListRS.add(pp.getRsInwardOutwardID());
            }
            uuidList.add(pp.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        if (uuidList.size() > 0) {
            pPDiscountReturnRepository.updateUnrecord(uuidList);
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.updateUnrecord(uuidList);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListRS);
            }
        }
        return handlingResultDTO;
    }

    /**
     * Delete the pPDiscountReturn by id.
     *
     * @param id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPDiscountReturn : {}", id);
        List<UUID> ppInvoiceDetailIDs = new ArrayList<>();
        Optional<PPDiscountReturn> ppDiscountReturn = pPDiscountReturnRepository.findById(id);
        UUID ppDiscountReturnId = ppDiscountReturn.get().getId();
        UUID rsInwardOutwardId = ppDiscountReturn.get().getRsInwardOutwardID();
        UUID saBillId = null;
        List<PPDiscountReturnDetails> ppDiscountReturnDetails = new ArrayList<>(ppDiscountReturn.get().getPpDiscountReturnDetails());
        if (ppDiscountReturn.get().getIsBill() != null && ppDiscountReturn.get().getIsBill()) {
            if (ppDiscountReturnDetails.size() > 0 && ppDiscountReturnDetails.get(0).getSaBillID() != null) {
                saBillId = ppDiscountReturnDetails.get(0).getSaBillID();
            }
        }

        if (saBillId != null) {
            if (saBillRepository.countById(saBillId) > 0) {
                System.out.println("sabill count:" + saBillRepository.countById(saBillId));
                saBillRepository.deleteById(saBillId);
            }
        }
        if (rsInwardOutwardId != null) {
            if (rsInwardOutwardRepository.countById(rsInwardOutwardId) > 0) {
                rsInwardOutwardRepository.deleteByPPDiscountReturnId(rsInwardOutwardId);
            }
        }
        Long count = refVoucherRepository.countAllByRefID1OrRefID2(id);
        if (count > 0) {
            refVoucherRepository.deleteByRefID1OrRefID2(id);
        }
        // Cập nhật lại sổ kho
        rsInwardOutwardRepository.updatePPDiscountReturnID(id);
        // cập nhật lại số lượng chứng từ mua hàng
//        if (ppDiscountReturnDetails.size() > 0) {
//            for (PPDiscountReturnDetails item : ppDiscountReturnDetails) {
//                if (item.getPpInvoiceDetailID() != null) {
//                    if (item.getQuantity() != null) {
//                        ppInvoiceRepository.updateQuantity(item.getPpInvoiceDetailID(), item.getQuantity(), false);
//                    }
//                }
//            }
//        }

        pPDiscountReturnRepository.deleteById(ppDiscountReturnId);
        for (PPDiscountReturnDetails pp : ppDiscountReturnDetails) {
            if (pp.getPpInvoiceDetailID() != null) {
//                pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(pp.getPpInvoiceDetailID());
                ppInvoiceDetailIDs.add(pp.getPpInvoiceDetailID());
            }
        }
        // cập nhật số lượng nhận trong bảng ppoder
        if (ppInvoiceDetailIDs != null && ppInvoiceDetailIDs.size() > 0) {
            List<UUID> ppOderDetailsID = ppInvoiceRepository.getPPOrderDetailIDSByListID(ppInvoiceDetailIDs);
            if (ppOderDetailsID != null && ppOderDetailsID.size() > 0) {
                pporderdetailRepository.updateQuantityReceiptByPPOderOrPPDiscount(ppOderDetailsID);
            }
        }
     }

    @Override
    public Page<PPDiscountReturnSearch2DTO> searchPPDiscountReturn(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

//        List<PPDiscountReturn> ppDiscountReturns = pPDiscountReturnRepository.searchPPDiscountReturn(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, currentUserLoginAndOrg.get().getOrg()).getContent();
//        if (ppDiscountReturns.size() > 0) {
//            ppDiscountReturns.forEach( item -> {
//                item.getPpDiscountReturnDetails().size();
//                item.getPpDiscountReturnDetails().forEach( item2 -> {
//
//                });
//            });
//        }
        return pPDiscountReturnRepository.searchPPDiscountReturn(pageable, accountingObjectID, currencyID, fromDate, toDate, status, statusPurchase, keySearch, currentUserLoginAndOrg.get().getOrg(), currentBook);

    }

    @Override
    public PPDiscountReturn findByRowNum(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch, Integer rownum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return pPDiscountReturnRepository.findByRowNum(pageable, accountingObjectID, currencyID, fromDate, toDate, status, statusPurchase, keySearch, rownum, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public List<RefVoucherDTO> refVouchersByPPOrderID(UUID id) {
        log.debug("Request to get refVouchersByPPDiscountReturn : {}", id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            boolean isNoMBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg()).equals('1');
            return refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<PPDiscountReturn> getPPDiscountReturnByID(UUID id) {
        return Optional.empty();
    }

    @Override
    public byte[] exportPdf(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<PPDiscountReturnSearchDTO> ppDiscountReturns = pPDiscountReturnRepository.searchPPDiscountReturnPDF(pageable, accountingObjectID, currencyID, fromDate, toDate,
                status, statusPurchase, keySearch, currentUserLoginAndOrg.get().getOrg(), currentBook);
            if (!statusPurchase) {
                return PdfUtils.writeToFile(ppDiscountReturns.getContent(), ExcelConstant.PPDiscountReturn.HEADER, ExcelConstant.PPDiscountReturn.FIELD);
            } else {
                return PdfUtils.writeToFile(ppDiscountReturns.getContent(), ExcelConstant.PPDiscountPurchase.HEADER, ExcelConstant.PPDiscountPurchase.FIELD);
            }
        }
        return null;
    }

    @Override
    public byte[] exportExcel(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<PPDiscountReturnSearchDTO> ppDiscountReturns = pPDiscountReturnRepository.searchPPDiscountReturnPDF(pageable, accountingObjectID, currencyID, fromDate, toDate,
                status, statusPurchase, keySearch, currentUserLoginAndOrg.get().getOrg(), currentBook);
            if (!statusPurchase) {
                return ExcelUtils.writeToFile(ppDiscountReturns.getContent(), ExcelConstant.PPDiscountReturn.NAME, ExcelConstant.PPDiscountReturn.HEADER, ExcelConstant.PPDiscountReturn.FIELD);
            } else {
                return ExcelUtils.writeToFile(ppDiscountReturns.getContent(), ExcelConstant.PPDiscountReturn.NAME, ExcelConstant.PPDiscountReturn.HEADER, ExcelConstant.PPDiscountReturn.FIELD);
            }
        }
        return null;
    }

    @Override
    public Page<PPDiscountReturnOutWardDTO> findAllPPDisCountReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return ppDiscountReturnDetailsRepository.findAllPPDisCountReturnDTO(pageable, accountingObject, fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), currentBook);
        }
        return null;
    }
}
