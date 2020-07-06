package vn.softdreams.ebweb.service.impl;

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
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * Service Implementation for managing RSInwardOutward.
 */
@Service
@Transactional
public class RSInwardOutwardServiceImpl implements RSInwardOutwardService {

    private final Logger log = LoggerFactory.getLogger(RSInwardOutwardServiceImpl.class);

    private final RSInwardOutwardRepository rSInwardOutwardRepository;

    private final PPDiscountReturnRepository ppDiscountReturnRepository;

    private final SAInvoiceRepository saInvoiceRepository;

    private final PPInvoiceRepository ppInvoiceRepository;

    private final SaReturnRepository saReturnRepository;

    private final SAOrderRepository saOrderRepository;

    private final SAOrderDetailsRepository saOrderDetailsRepository;

    private final TypeRepository typeRepository;

    private final RSInwardOutwardRepository rsInwardOutwardRepository;

    private final PPDiscountReturnServiceImpl ppDiscountReturnSeviceImpl;

    private final RepositoryLedgerRepository repositoryLedgerRepository;

    private final SAInvoiceServiceImpl saInvoiceServiceImpl;

    private final PPInvoiceServiceImpl ppInvoiceServiceImpl;

    private final SaReturnServiceImpl saReturnServiceImpl;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;

    @Autowired
    RepositoryLedgerService repositoryLedgerService;

    @Autowired
    UtilsRepository utilsRepository;

    @Autowired
    RefVoucherRepository refVoucherRepository;

    @Autowired
    UserService userService;

    @Autowired
    GeneralLedgerService generalLedgerService;

    @Autowired
    GenCodeService genCodeService;

    @Autowired
    UtilsService utilsService;

    @Autowired
    PporderdetailRepository pporderdetailRepository;

    private final PporderRepository pporderRepository;

    @Autowired
    RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;


    private final OrganizationUnitRepository organizationUnitRepository;

    public RSInwardOutwardServiceImpl(RSInwardOutwardRepository rSInwardOutwardRepository,
                                      PPDiscountReturnRepository ppDiscountReturnRepository,
                                      SAInvoiceRepository saInvoiceRepository, PPInvoiceRepository ppInvoiceRepository,
                                      SaReturnRepository saReturnRepository, SAOrderRepository saOrderRepository,
                                      SAOrderDetailsRepository saOrderDetailsRepository, TypeRepository typeRepository,
                                      RSInwardOutwardRepository rsInwardOutwardRepository,
                                      PPDiscountReturnServiceImpl ppDiscountReturnSeviceImpl,
                                      SAInvoiceServiceImpl saInvoiceServiceImpl,
                                      PPInvoiceServiceImpl ppInvoiceServiceImpl,
                                      SaReturnServiceImpl saReturnServiceImpl,
                                      RepositoryLedgerRepository repositoryLedgerRepository,
                                      MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository, OrganizationUnitRepository organizationUnitRepository,
                                      PporderRepository pporderRepository) {
        this.rSInwardOutwardRepository = rSInwardOutwardRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.saReturnRepository = saReturnRepository;
        this.saOrderRepository = saOrderRepository;
        this.saOrderDetailsRepository = saOrderDetailsRepository;
        this.typeRepository = typeRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.ppDiscountReturnSeviceImpl = ppDiscountReturnSeviceImpl;
        this.saInvoiceServiceImpl = saInvoiceServiceImpl;
        this.ppInvoiceServiceImpl = ppInvoiceServiceImpl;
        this.saReturnServiceImpl = saReturnServiceImpl;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.pporderRepository = pporderRepository;
    }

    /**
     * Save a rSInwardOutward.
     *
     * @param rSInwardOutward the entity to save
     * @return the persisted entity
     */
    @Override
    public RSInwardOutwardSaveDTO save(RSInwardOutwardSaveDTO rSInwardOutward) {
        log.debug("Request to save PPOrderSaveDTO : {}", rSInwardOutward);
        RSInwardOutward rsInwardOutward = rSInwardOutward.getRsInwardOutward();
        int typeGroupID = 0;
        if (rsInwardOutward.getTypeID() == TypeConstant.NHAP_KHO ||
            rsInwardOutward.getTypeID() == TypeConstant.NHAP_KHO_TU_DIEU_CHINH ||
            rsInwardOutward.getTypeID() == TypeConstant.NHAP_KHO_TU_MUA_HANG ||
            rsInwardOutward.getTypeID() == TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                typeGroupID = TypeGroupConstant.RS_INWARD_OUTWARD;
        } else if (rsInwardOutward.getTypeID() == XUAT_KHO ||
            rsInwardOutward.getTypeID() == XUAT_KHO_TU_BAN_HANG ||
            rsInwardOutward.getTypeID() == XUAT_KHO_TU_HANG_MUA_TRA_LAI ||
            rsInwardOutward.getTypeID() == TypeConstant.XUAT_KHO_TU_DIEU_CHINH) {
                typeGroupID = TypeGroupConstant.RS_OUTWARD;
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            rsInwardOutward.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            boolean isUpdateGenCode = false;
            boolean isUpdateNoBook = true;
            RSInwardOutward rsInwardOutwardOld = new RSInwardOutward();
            if (rsInwardOutward.getId() != null) {
                Optional<RSInwardOutward> rsInwardOutwardOptional = rSInwardOutwardRepository.findById(rsInwardOutward.getId());
                isUpdateGenCode = rsInwardOutwardOptional.isPresent();
                if (isUpdateGenCode) {
                    rsInwardOutwardOld = rsInwardOutwardOptional.get();
                }
            }

            // BOTH_BOOK to One book
            if (rsInwardOutwardOld.getTypeLedger() != null && !rsInwardOutwardOld.getTypeLedger().equals(rsInwardOutward.getTypeLedger())) {
                if (rsInwardOutwardOld.getTypeLedger().equals(Constants.TypeLedger.BOTH_BOOK)) {
                    if (rsInwardOutward.getTypeLedger().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        rsInwardOutwardOld.setNoMBook(null);
                    } else {
                        rsInwardOutwardOld.setNoFBook(null);
                    }
                }
            }

            // end
            if (rsInwardOutward.getTypeLedger() != null) {
                if (rSInwardOutward.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                    if (rsInwardOutwardOld.getNoFBook() != null && rsInwardOutwardOld.getNoFBook().equals(rsInwardOutward.getNoFBook())) {
                        isUpdateNoBook = false;
                    }
                    rsInwardOutwardOld.setNoFBook(rsInwardOutward.getNoFBook());
                } else {
                    if (rsInwardOutwardOld.getNoMBook() != null && rsInwardOutwardOld.getNoMBook().equals(rsInwardOutward.getNoMBook())) {
                        isUpdateNoBook = false;
                    }
                    rsInwardOutwardOld.setNoMBook(rsInwardOutward.getNoMBook());
                }
                if (rsInwardOutward.getTypeLedger().equals(Constants.TypeLedger.BOTH_BOOK) && (rsInwardOutwardOld.getId() == null || !rsInwardOutward.getTypeLedger().equals(rsInwardOutwardOld.getTypeLedger()))) {
                    GenCode genCode = genCodeService.findWithTypeID(typeGroupID,
                        rSInwardOutward.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK) ? Constants.TypeLedger.MANAGEMENT_BOOK : Constants.TypeLedger.FINANCIAL_BOOK);
                    String codeVoucher = Utils.codeVoucher(genCode);
                    if (rSInwardOutward.getCurrentBook().equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        rsInwardOutwardOld.setNoMBook(codeVoucher);
                    } else {
                        rsInwardOutwardOld.setNoFBook(codeVoucher);
                    }
                    isUpdateNoBook = true;
                }
                rsInwardOutwardOld.setTypeLedger(rsInwardOutward.getTypeLedger());
                if (isUpdateNoBook) {
                    if (!utilsRepository.checkDuplicateNoVoucher(rsInwardOutwardOld.getNoFBook(),
                        rsInwardOutwardOld.getNoMBook(),
                        rsInwardOutwardOld.getTypeLedger(), rsInwardOutward.getId())) {
                        throw new BadRequestAlertException("Trùng số chứng từ nhập kho", "rSInwardOutward", "duplicateNo");
                    }
                }
                rsInwardOutward.setNoFBook(rsInwardOutwardOld.getNoFBook());
                rsInwardOutward.setNoMBook(rsInwardOutwardOld.getNoMBook());
            }

            rsInwardOutward.setTotalAmountOriginal(rsInwardOutward.getTotalAmountOriginal());
            rsInwardOutward.getRsInwardOutwardDetails().forEach(item -> {
                item.setAmount(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
                item.setAmountOriginal(item.getAmount());
                item.setUnitPriceOriginal(item.getUnitPriceOriginal() != null ? item.getUnitPriceOriginal() : BigDecimal.ZERO);
                item.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
                item.setMainUnitPrice(item.getMainUnitPrice() != null ? item.getMainUnitPrice() : BigDecimal.ZERO);
                if (item.getUnit() == null) {
                    item.setMainConvertRate(new BigDecimal(1));
                    item.setFormula("*");
                    item.setMainQuantity(item.getQuantity());
                    item.setMainUnitPrice(item.getUnitPriceOriginal());
                }
            });

            RSInwardOutward rsInwardOutwardCompare = new RSInwardOutward();
            if (rsInwardOutward.getId() != null) {
                Optional<RSInwardOutward> rsInwardOutwardOptional = rSInwardOutwardRepository.findById(rsInwardOutward.getId());
                if (rsInwardOutwardOptional.isPresent()) {
                    rsInwardOutwardCompare = rsInwardOutwardOptional.get();
                }
            }
            List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                rsInwardOutwardCompare.getRsInwardOutwardDetails()
                    .stream()
                    .filter(x -> x.getPpOrderDetail() != null && x.getPpOrderDetail().getId() != null)
                    .map(x -> new PPOrderDTO(x.getPpOrderDetail().getId(), BigDecimal.ZERO.subtract(x.getQuantity())))
                    .collect(Collectors.toList()),
                rsInwardOutward.getRsInwardOutwardDetails()
                    .stream()
                    .filter(x -> x.getPpOrderDetail() != null && x.getPpOrderDetail().getId() != null)
                    .map(x -> new PPOrderDTO(x.getPpOrderDetail().getId(), x.getQuantity()))
                    .collect(Collectors.toList()), false);

            if (rsInwardOutward.getId() == null && rsInwardOutward.getRsInwardOutwardDetails().stream().filter(a -> a.getSaOrderDetail() != null && a.getSaOrderDetail().getId() != null).count() > 0) {
                List<RSInwardOutWardDetails> lstRS = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
                List<SAOrderDetails> lsSaOrderDetails = new ArrayList<>();
                for (int i = 0; i < lstRS.size(); i++) {
                    if (lstRS.get(i).getSaOrderDetail().getId() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstRS.get(i).getSaOrderDetail().getId());
                        if (saOrderDetails.isPresent()) {
                            if (saOrderDetails.get().getQuantityDelivery() == null) {
                                saOrderDetails.get().setQuantityDelivery(lstRS.get(i).getQuantity());
                            } else {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(lstRS.get(i).getQuantity()));
                            }
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lsSaOrderDetails.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lsSaOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lsSaOrderDetails.get(0).getsAOrderID()).toString());
                }
            } else if (rsInwardOutward.getId() != null) {
                Optional<RSInwardOutward> oldRSInwardOutward = rSInwardOutwardRepository.findById(rsInwardOutward.getId());
                List<SAOrderDetails> lsSaOrderDetails = new ArrayList<>();
                if (oldRSInwardOutward.get().getRsInwardOutwardDetails().stream().filter(a -> a.getSaOrderDetail() != null && a.getSaOrderDetail().getId() != null).count() > 0) {
                    List<RSInwardOutWardDetails> lstRSOld = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
                    for (int i = 0; i < lstRSOld.size(); i++) {
                        if (lstRSOld.get(i).getSaOrderDetail().getId() != null) {
                            Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstRSOld.get(i).getSaOrderDetail().getId());
                            if (saOrderDetails.isPresent()) {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(lstRSOld.get(i).getQuantity()));
                                saOrderDetailsRepository.save(saOrderDetails.get());
                                lsSaOrderDetails.add(saOrderDetails.get());
                            }
                        }
                    }
                    // Add by Hautv
                    if (lsSaOrderDetails.size() > 0) {
                        saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lsSaOrderDetails.get(0).getsAOrderID()).toString());
                    }
                }
                List<RSInwardOutWardDetails> lstRS = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
                List<SAOrderDetails> lstSAOrderDT = new ArrayList<>();
                for (int i = 0; i < lstRS.size(); i++) {
                    if (lstRS.get(i).getSaOrderDetail() != null && lstRS.get(i).getSaOrderDetail().getId() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstRS.get(i).getSaOrderDetail().getId());
                        if (saOrderDetails.isPresent()) {
                            if (saOrderDetails.get().getQuantityDelivery() == null) {
                                saOrderDetails.get().setQuantityDelivery(lstRS.get(i).getQuantity());
                            } else {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(lstRS.get(i).getQuantity()));
                            }
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lstSAOrderDT.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lstSAOrderDT.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lstSAOrderDT.get(0).getsAOrderID()).toString());
                }
            }

            rsInwardOutward = rSInwardOutwardRepository.save(rsInwardOutward);

            materialGoodsSpecificationsLedgerRepository.deleteByRefID(rsInwardOutward.getId());
            List<RSInwardOutWardDetails> rsInwardOutWardDetailsSave = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
            List<RSInwardOutWardDetails> rsInwardOutWardDetails = new ArrayList<>(rSInwardOutward.getRsInwardOutward().getRsInwardOutwardDetails());
            List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
            for (int i = 0; i < rsInwardOutWardDetails.size(); i++) {
                if (rsInwardOutWardDetails.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                    rsInwardOutWardDetails.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                    for (MaterialGoodsSpecificationsLedger item: rsInwardOutWardDetails.get(i).getMaterialGoodsSpecificationsLedgers()) {
                        item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                        item.setDate(rsInwardOutward.getDate());
                        item.setPostedDate(rsInwardOutward.getPostedDate());
                        item.setTypeLedger(rsInwardOutward.getTypeLedger());
                        item.setRefTypeID(rsInwardOutward.getTypeID());
                        item.setReferenceID(rsInwardOutward.getId());
                        item.setDetailID(rsInwardOutWardDetailsSave.get(i).getId());
                        item.setNoFBook(rsInwardOutward.getNoFBook());
                        item.setNoMBook(rsInwardOutward.getNoMBook());
                        if (rsInwardOutward.getTypeID() == TypeConstant.NHAP_KHO) {
                            item.setiWRepositoryID(rsInwardOutWardDetailsSave.get(i).getRepository().getId());
                            item.setoWQuantity(BigDecimal.ZERO);
                        } else {
                            item.setoWRepositoryID(rsInwardOutWardDetailsSave.get(i).getRepository().getId());
                            item.setiWRepositoryID(null);
                            item.setiWQuantity(BigDecimal.ZERO);
                        }
                        materialGoodsSpecificationsLedgers.add(item);
                    }
                }
            }
            if (materialGoodsSpecificationsLedgers.size() > 0) {
                materialGoodsSpecificationsLedgerRepository.saveAll(materialGoodsSpecificationsLedgers);
            }

            // update PPOrderDetails
            pporderdetailRepository.saveAll(ppOrderDetails);
            pporderRepository.updateStatus(ppOrderDetails.stream()
                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));

            List<RefVoucher> refVouchers = new ArrayList<>();
            List<RSInwardOutWardDetails> lstRSDetails = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
            for (RefVoucher item : rSInwardOutward.getViewVouchers() != null ? rSInwardOutward.getViewVouchers() : new ArrayList<RefVoucher>()) {
                if (lstRSDetails.stream().filter(x -> x.getPpDiscountReturn() != null && x.getPpDiscountReturn().getId() != null ||
                    x.getSaOrder() != null && x.getSaOrder().getId() != null ||
                    x.getSaInvoice() != null && x.getSaInvoice().getId() != null ||
                    x.getSaReturn() != null && x.getSaReturn().getId() != null ||
                    x.getPpOrder() != null && x.getPpOrder().getId() != null).count() > 0) {
                    RefVoucher refVoucherForRef = new RefVoucher();
                    refVoucherForRef.setCompanyID(rsInwardOutward.getCompanyID());
                    refVoucherForRef.setRefID1(item.getRefID2());
                    refVoucherForRef.setRefID2(rsInwardOutward.getId());
                    refVouchers.add(refVoucherForRef);
                }
            }
            refVoucherRepository.deleteByRefID1(rsInwardOutward.getId());
            refVoucherRepository.saveAll(refVouchers);
            rSInwardOutward.setRsInwardOutward(rsInwardOutward);
            rSInwardOutward.setViewVouchers(rSInwardOutward.getViewVouchers());

            List<RefVoucher> refVoucher = rSInwardOutward.getViewVouchers();
            for (RefVoucher item : refVoucher) {
                item.setRefID1(rsInwardOutward.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(rsInwardOutward.getId());
            refVoucher = refVoucherRepository.saveAll(refVoucher);
            rSInwardOutward.setRsInwardOutward(rsInwardOutward);
            rSInwardOutward.setViewVouchers(refVoucher);

            UserDTO userDTO = userService.getAccount();
            if (rSInwardOutward.getCheckRecordSLT() != null && !rSInwardOutward.getCheckRecordSLT()) {
                if (userDTO.getSystemOption()
                    .stream()
                    .filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo))
                    .findAny().get().getData().equals("0")) {
                    if (typeGroupID == TypeGroupConstant.RS_INWARD_OUTWARD) {
                        if (generalLedgerService.record(rsInwardOutward, new MessageDTO(""))) {
                            rsInwardOutward.setRecorded(true);
                            rSInwardOutwardRepository.save(rsInwardOutward);
                        }
                    } else if (typeGroupID == TypeGroupConstant.RS_OUTWARD) {
                        if (generalLedgerService.record(rsInwardOutward, new MessageDTO("XUAT_KHO"))) {
                            rsInwardOutward.setRecorded(true);
                            for (RSInwardOutWardDetails sad: rsInwardOutward.getRsInwardOutwardDetails()) {
                                sad.setRsInwardOutwardID(rsInwardOutward.getId());
                            }
                            rSInwardOutwardRepository.save(rsInwardOutward);
                            String valueCal = "";
                            String valCalNow = "";
                            for (SystemOption sys: userDTO.getSystemOption()) {
                                if (sys.getCode().equals(Constants.SystemOption.VTHH_PPTinhGiaXKho)) {
                                    valueCal = sys.getData();
                                }
                                if (sys.getCode().equals(Constants.SystemOption.VTHH_TDTGia_NgayLapCTu)) {
                                    valCalNow = sys.getData();
                                }
                            }
                            if (valueCal != null && !valueCal.equals("") && valCalNow != null && !valCalNow.equals("")) {
                                Integer calculationMethod = Integer.valueOf(valueCal);
                                if (valCalNow.equals(Constants.CalculationMethod.TINH_NGAY_KHI_LAP_CHUNG_TU) && calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI) {
                                    List<UUID> listUUIDs = rsInwardOutward.getRsInwardOutwardDetails().stream().map(x -> x.getMaterialGood().getId()).collect(Collectors.toList());
                                    repositoryLedgerService.calculateOWPrice(calculationMethod, listUUIDs, rsInwardOutward.getDate().toString(), rsInwardOutward.getDate().toString());
                                } else if (valCalNow.equals(Constants.CalculationMethod.TINH_NGAY_KHI_LAP_CHUNG_TU) && calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_NHAP_TRUOC_XUAT_TRUOC) {
                                    List<UUID> listUUIDs = rsInwardOutward.getRsInwardOutwardDetails().stream().map(x -> x.getMaterialGood().getId()).collect(Collectors.toList());
                                    repositoryLedgerService.calculateOWPrice(calculationMethod, listUUIDs, rsInwardOutward.getDate().toString(), rsInwardOutward.getDate().toString());
                                }
                            }
                        }
                    }
                }
            }
            Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
            if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                rsInwardOutward.setNoMBook(null);
                rsInwardOutward.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
            }

            for (RSInwardOutWardDetails rsInwardOutwardDetail : rsInwardOutward.getRsInwardOutwardDetails()) {
                // Create From PPDiscountReturn
                if (rsInwardOutwardDetail.getPpDiscountReturn() != null) {
                    rsInwardOutwardDetail.setPpDiscountReturn(ppDiscountReturnRepository
                        .findById(rsInwardOutwardDetail.getPpDiscountReturn().getId())
                        .orElse(rsInwardOutwardDetail.getPpDiscountReturn()));
                }
                // Create From SaInvoice
                if (rsInwardOutwardDetail.getSaInvoice() != null) {
                    rsInwardOutwardDetail.setSaInvoice(saInvoiceRepository
                        .findById(rsInwardOutwardDetail.getSaInvoice().getId())
                        .orElse(rsInwardOutwardDetail.getSaInvoice()));
                }
                // Create From SaOrder
                if (rsInwardOutwardDetail.getSaOrder() != null) {
                    rsInwardOutwardDetail.setSaOrder(saOrderRepository
                        .findById(rsInwardOutwardDetail.getSaOrder().getId())
                        .orElse(rsInwardOutwardDetail.getSaOrder()));
                }
                // Create From SaReturn
                if (rsInwardOutwardDetail.getSaReturn() != null) {
                    rsInwardOutwardDetail.setSaReturn(saReturnRepository
                        .findById(rsInwardOutwardDetail.getSaReturn().getId())
                        .orElse(rsInwardOutwardDetail.getSaReturn()));
                }
                // Create From PpOrder
                if (rsInwardOutwardDetail.getPpOrder() != null) {
                    rsInwardOutwardDetail.setPpOrder(pporderRepository
                        .findById(rsInwardOutwardDetail.getPpOrder().getId())
                        .orElse(rsInwardOutwardDetail.getPpOrder()));
                }
            }
            return rSInwardOutward;
        }
        throw new BadRequestAlertException("Khong the tao nhap kho", "", "");
    }

    /**
     * Get all the rSInwardOutwards.ff
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RSInwardOutward> findAll(Pageable pageable) {
        log.debug("Request to get all RSInwardOutwards");
        return rSInwardOutwardRepository.findAll(pageable);
    }


    /**
     * Get one rSInwardOutward by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RSInwardOutward> findOne(UUID id) {
        log.debug("Request to get RSInwardOutward : {}", id);
        return rSInwardOutwardRepository.findById(id);
    }

    /**
     * Delete the rSInwardOutward by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RSInwardOutward : {}", id);
        Optional<RSInwardOutward> oldRSInwardOutward = rSInwardOutwardRepository.findById(id);
        if (oldRSInwardOutward.isPresent()) {
            if (oldRSInwardOutward.get().getRsInwardOutwardDetails().stream().filter(a -> a.getSaOrderDetail() != null && a.getSaOrderDetail().getId() != null).count() > 0) {
                List<RSInwardOutWardDetails> lstRSOld = new ArrayList<>(oldRSInwardOutward.get().getRsInwardOutwardDetails());
                List<SAOrderDetails> lsSaOrderDetails = new ArrayList<>();
                for (int i = 0; i < lstRSOld.size(); i++) {
                    if (lstRSOld.get(i).getSaOrderDetail().getId() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstRSOld.get(i).getSaOrderDetail().getId());
                        if (saOrderDetails.isPresent()) {
                            saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(lstRSOld.get(i).getQuantity()));
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lsSaOrderDetails.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lsSaOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lsSaOrderDetails.get(0).getsAOrderID()).toString());
                }
            }
            if (oldRSInwardOutward.get().getRsInwardOutwardDetails().stream().filter(a -> a.getPpOrderDetail() != null && a.getPpOrderDetail().getId() != null).count() > 0) {
                List<RSInwardOutWardDetails> lstRSOld = new ArrayList<>(oldRSInwardOutward.get().getRsInwardOutwardDetails());
                List<PPOrderDetail> lsPpOrderDetails = new ArrayList<>();
                for (int i = 0; i < lstRSOld.size(); i++) {
                    if (lstRSOld.get(i).getPpOrderDetail() != null && lstRSOld.get(i).getPpOrderDetail().getId() != null) {
                        Optional<PPOrderDetail> ppOrderDetails = pporderdetailRepository.findById(lstRSOld.get(i).getPpOrderDetail().getId());
                        if (ppOrderDetails.isPresent()) {
                            ppOrderDetails.get().setQuantityReceipt(ppOrderDetails.get().getQuantityReceipt().subtract(lstRSOld.get(i).getQuantity()));
                            pporderdetailRepository.save(ppOrderDetails.get());
                            lsPpOrderDetails.add(ppOrderDetails.get());
                        }
                    }
                }
                if (lsPpOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lsPpOrderDetails.get(0).getpPOrderID()).toString());
                }
            }
        }
        rSInwardOutwardRepository.deleteById(id);
    }

    @Override
    public Page<RSInwardOutwardSearchDTO> searchAll(Pageable pageable, UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.searchAll(pageable, accountingObject, status, fromDate, toDate, searchValue, noType, currentBook);
    }

    @Override
    public Page<RSInwardOutwardSearchDTO> searchAllOutWard(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.searchAllOutWard(pageable, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook);
    }

    @Override
    public byte[] exportPdf(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

            Page<RSInwardOutwardSearchDTO> rsInwardOutwards = currentUserLoginAndOrg.map(securityDTO ->
                rSInwardOutwardRepository.searchAll(null, accountingObject, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public byte[] exportExcel(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();

        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<RSInwardOutwardSearchDTO> rsInwardOutwards = currentUserLoginAndOrg.map(securityDTO ->
                rSInwardOutwardRepository.searchAll(null, accountingObject, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public byte[] exportPdfOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

            Page<RSInwardOutwardSearchDTO> rsInwardOutwards = currentUserLoginAndOrg.map(securityDTO ->
                rSInwardOutwardRepository.searchAllOutWard(null, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public byte[] exportExcelOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();

        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<RSInwardOutwardSearchDTO> rsInwardOutwards = currentUserLoginAndOrg.map(securityDTO ->
                rSInwardOutwardRepository.searchAllOutWard(null, accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, Integer.parseInt(currentBook, 10))).orElse(null);

            UserDTO userDTO = userService.getAccount();
            Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
            if (phienSoLamViec.equals(0)) {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOFBOOK, userDTO);
            } else {
                return PdfUtils.writeToFile(rsInwardOutwards.getContent(), ExcelConstant.RSInwardOutward.HEADER, ExcelConstant.RSInwardOutward.FIELD_NOMBOOK, userDTO);
            }
        }
        return null;
    }

    @Override
    public List<RSInwardOutWardDetails> getDetailsById(UUID id, Integer typeID) {
        return rSInwardOutwardRepository.getDetailsById(id, typeID);
    }

    @Override
    public List<RSInwardOutWardDetails> getDetailsOutWardById(UUID id, Integer typeID) {
        return rSInwardOutwardRepository.getDetailsOutWardById(id, typeID);
    }

    public UUID getReferenceIDByRSInwardID(UUID id, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID uuid = null;
        if (currentUserLoginAndOrg.isPresent()) {
            if (typeID == TypeConstant.NHAP_KHO_TU_MUA_HANG)
                uuid = rSInwardOutwardRepository.getPPInvoiceIDByRSInwardID(id, currentUserLoginAndOrg.get().getOrg());
            else if (typeID == TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI)
                uuid = rSInwardOutwardRepository.getSaReturnIDByRSInwardID(id, currentUserLoginAndOrg.get().getOrg());
            else if (typeID == TypeConstant.NHAP_KHO_TU_DIEU_CHINH)
                // TODO: Nhập kho từ điều chỉnh
                return null;
        }
        return uuid;
    }

    @Override
    public Page<ViewRSOutwardDTO> getViewRSOutwardDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return rSInwardOutwardRepository.findAllView(pageable, currentUserLoginAndOrg.get().getOrg(), accountingObjectID, fromDate, toDate, currentBook, currencyID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.findReferenceTableID(accountingObject, status, fromDate, toDate, noType, searchValue, rowNum, currentBook);
    }

    /**
     * @author dungvm
     *
     * Sau khi bỏ ghi sổ, xóa id của các trường liên kết
     *
     * @param ids id của những rsinwardoutwarddetail lập từ màn khác
     */
    @Override
    public void unRecordDetails(List<UUID> ids) {
        rSInwardOutwardRepository.unRecordDetails(ids);
    }

    @Override
    public RSInwardOutwardSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.findReferenceTablesID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, rowNum, currentBook);
    }

    @Override
    public Integer findRowNumByID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.findRowNumByID(accountingObject, status, fromDate, toDate, noType, searchValue, id, currentBook);
    }

    @Override
    public RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.findByRowNumOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, rowNum, currentBook);
    }

    @Override
    public Integer findRowNumOutWardByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id) {
        UserDTO userDTO = userService.getAccount();
        int currentBook = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData(), 10);
        return rSInwardOutwardRepository.findRowNumOutWardByID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, id, currentBook);
    }

    @Override
    public HandlingResultDTO multiDelete(List<RSInwardOutward> rsInwardOutward) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(rsInwardOutward.size());
        List<RSInwardOutward> listDelete = rsInwardOutward.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (int i = 0; i < rsInwardOutward.size(); i++) {
            LocalDate postedDate = LocalDate.parse(rsInwardOutward.get(i).getPostedDate().toString());
            if (Boolean.TRUE.equals(rsInwardOutward.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                if (Boolean.TRUE.equals(rsInwardOutward.get(i).getRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.isAfter(postedDate)) {
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                } else {
                    viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                }
                BeanUtils.copyProperties(rsInwardOutward.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(rsInwardOutward.get(i));
            }
            if (Boolean.TRUE.equals(rsInwardOutward.get(i).getRefIsBill())) {
                if (rsInwardOutward.get(i).getRefInvoiceNo() != null && rsInwardOutward.get(i).getRefInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
                    userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Hóa đơn đã cấp số !");
                    BeanUtils.copyProperties(rsInwardOutward.get(i), viewVoucherNo);
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(rsInwardOutward.get(i));
                }
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "rsInwardOutward", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_XK = new ArrayList<>();
        List<UUID> uuidList_XK_HMTL = new ArrayList<>();
        List<UUID> uuidList_XK_BH = new ArrayList<>();
        List<UUID> uuidList_NK = new ArrayList<>();
        List<UUID> uuidList_NK_MH = new ArrayList<>();
        List<UUID> uuidList_NK_HBTL = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_XK) {
                uuidList_XK.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_XK_HMTL) {
                uuidList_XK_HMTL.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_XK_BH) {
                uuidList_XK_BH.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_NK) {
                uuidList_NK.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_NK_MH) {
                uuidList_NK_MH.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == Constants.MultiDelete_TypeID.Type_NK_HBTL) {
                uuidList_NK_HBTL.add(listDelete.get(i).getId());
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
        // Xoa chung tu Xuat Kho
        if (uuidList_XK.size() > 0) {
            rSInwardOutwardRepository.multiDeleteRSInwardOutward(currentUserLoginAndOrg.get().getOrg(), uuidList_XK);
            rSInwardOutwardRepository.multiDeleteChildRSInwardOutward("RSInwardOutwardDetail", uuidList_XK);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_XK);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu xuat kho tu ban hang
        if (uuidList_XK_BH.size() > 0) {
            for (RSInwardOutward inwardOutward : listDelete) {
                UUID idSaInvoice =  rsInwardOutwardRepository.getIdSaInvoice(inwardOutward.getId());
                if (idSaInvoice != null) {
                    idSaInvoice = Utils.uuidConvertToGUID(idSaInvoice);
                    saInvoiceServiceImpl.delete(idSaInvoice);
                }
            }
            rsInwardOutwardRepository.deleteByListID(uuidList_XK_BH);
        }
        // Xoa chung tu xuat kho tu hang mua tra lai
        if (uuidList_XK_HMTL.size() > 0) {
            for (RSInwardOutward inwardOutward : listDelete) {
                UUID idPPDiscountReturn =  rsInwardOutwardRepository.getIdPPdiscountReturn(inwardOutward.getId());
                if (idPPDiscountReturn != null) {
                    idPPDiscountReturn = Utils.uuidConvertToGUID(idPPDiscountReturn);
                    ppDiscountReturnSeviceImpl.delete(idPPDiscountReturn);
                }
            }
            rsInwardOutwardRepository.deleteByListID(uuidList_XK_HMTL);
        }
        // Xoa chung tu Nhap Kho
        if (uuidList_NK.size() > 0) {
            rSInwardOutwardRepository.multiDeleteRSInwardOutward(currentUserLoginAndOrg.get().getOrg(), uuidList_NK);
            rSInwardOutwardRepository.multiDeleteChildRSInwardOutward("RSInwardOutwardDetail", uuidList_NK);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_NK);
        }
        // Xoa chung tu Nhap Kho tu mua hang
        if (uuidList_NK_MH.size() > 0) {
            for (RSInwardOutward inwardOutward : listDelete) {
                UUID idPPInvoice =  rsInwardOutwardRepository.getIdPPInvoice(inwardOutward.getId());
                if (idPPInvoice != null) {
                    idPPInvoice = Utils.uuidConvertToGUID(idPPInvoice);
                    ppInvoiceServiceImpl.delete(idPPInvoice);
                }
            }
            rsInwardOutwardRepository.deleteByListID(uuidList_NK_MH);
        }
        // Xoa chung tu Nhap Kho tu hang ban tra lai
        if (uuidList_NK_HBTL.size() > 0) {
            for (RSInwardOutward inwardOutward : listDelete) {
                UUID idSAReturn =  rsInwardOutwardRepository.getIdSAReturn(inwardOutward.getId());
                if (idSAReturn != null) {
                    idSAReturn = Utils.uuidConvertToGUID(idSAReturn);
                    saReturnServiceImpl.delete(idSAReturn);
                }
            }
            rsInwardOutwardRepository.deleteByListID(uuidList_NK_HBTL);
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<RSInwardOutward> rsInwardOutward) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(rsInwardOutward.size());
        List<RSInwardOutward> listDelete = rsInwardOutward.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (RSInwardOutward rs: rsInwardOutward) {
            LocalDate postedDate = LocalDate.parse(rs.getPostedDate().toString());
            if (rs.getRecorded() != null && !rs.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ!");
                viewVoucherNo.setPostedDate(rs.getPostedDate());
                viewVoucherNo.setDate(rs.getDate());
                viewVoucherNo.setNoFBook(rs.getNoFBook());
                viewVoucherNo.setNoMBook(rs.getNoMBook());
                if (rs.getTypeID() == XUAT_KHO) {
                    viewVoucherNo.setTypeName("Xuất kho");
                } else if (rs.getTypeID() == XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                    viewVoucherNo.setTypeName("Xuất kho từ hàng mua trả lại");
                } else if (rs.getTypeID() == XUAT_KHO_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Xuất kho từ bán hàng");
                } else if (rs.getTypeID() == NHAP_KHO) {
                    viewVoucherNo.setTypeName("Nhập kho");
                } else if (rs.getTypeID() == NHAP_KHO_TU_MUA_HANG) {
                    viewVoucherNo.setTypeName("Nhập kho từ mua hàng");
                } else if (rs.getTypeID() == NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                    viewVoucherNo.setTypeName("Nhập kho từ hàng bán trả lại");
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
                    if (rs.getTypeID() == XUAT_KHO) {
                        viewVoucherNo.setTypeName("Xuất kho");
                    } else if (rs.getTypeID() == XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                        viewVoucherNo.setTypeName("Xuất kho từ hàng mua trả lại");
                    } else if (rs.getTypeID() == XUAT_KHO_TU_BAN_HANG) {
                        viewVoucherNo.setTypeName("Xuất kho từ bán hàng");
                    } else if (rs.getTypeID() == NHAP_KHO) {
                        viewVoucherNo.setTypeName("Nhập kho");
                    } else if (rs.getTypeID() == NHAP_KHO_TU_MUA_HANG) {
                        viewVoucherNo.setTypeName("Nhập kho từ mua hàng");
                    } else if (rs.getTypeID() == NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                        viewVoucherNo.setTypeName("Nhập kho từ hàng bán trả lại");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(rs);
                }
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListPPD = new ArrayList<>();
        List<UUID> uuidListSAI = new ArrayList<>();
        List<UUID> uuidListPPI = new ArrayList<>();
        List<UUID> uuidListSAR = new ArrayList<>();
        for (RSInwardOutward rs: listDelete) {
            UUID idPPDiscountReturn =  rsInwardOutwardRepository.getIdPPdiscountReturn(rs.getId());
            UUID idSAInvoice =  rsInwardOutwardRepository.getIdSaInvoice(rs.getId());
            UUID idPPInvoice =  rsInwardOutwardRepository.getIdPPInvoice(rs.getId());
            UUID idSAReturn =  rsInwardOutwardRepository.getIdSAReturn(rs.getId());
            if (idPPDiscountReturn != null) {
                idPPDiscountReturn = Utils.uuidConvertToGUID(idPPDiscountReturn);
                uuidListPPD.add(idPPDiscountReturn);
            }
            if (idSAInvoice != null) {
                idSAInvoice = Utils.uuidConvertToGUID(idSAInvoice);
                uuidListSAI.add(idSAInvoice);
            }
            if (idPPInvoice != null) {
                idPPInvoice = Utils.uuidConvertToGUID(idPPInvoice);
                uuidListPPI.add(idPPInvoice);
            }
            if (idSAReturn != null) {
                idSAReturn = Utils.uuidConvertToGUID(idSAReturn);
                uuidListSAR.add(idSAReturn);
            }
            uuidList.add(rs.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        if (uuidList.size() > 0) {
            rsInwardOutwardRepository.updateUnrecordRS(uuidList);
            repositoryLedgerRepository.deleteAllByReferenceID(uuidList);
            if (uuidListPPD.size() > 0) {
                ppDiscountReturnRepository.updateUnrecord(uuidListPPD);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListPPD);
            }
            if (uuidListSAI.size() > 0) {
                saInvoiceRepository.updateUnrecord(uuidListSAI);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListSAI);
            }
            if (uuidListPPI.size() > 0) {
                ppInvoiceRepository.updateUnrecord(uuidListPPI);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListPPI);
            }
            if (uuidListSAR.size() > 0) {
                saReturnRepository.updateUnrecord(uuidListSAR);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListSAR);
            }
        }
        return handlingResultDTO;
    }
}
