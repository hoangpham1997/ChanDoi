package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
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
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.HANG_BAN_TRA_LAI;
import static vn.softdreams.ebweb.service.util.TypeConstant.RS_INWARD_OUTWARD;

/**
 * Service Implementation for managing SaReturn.
 */
@Service
@Transactional
public class SaReturnServiceImpl implements SaReturnService {

    private final Logger log = LoggerFactory.getLogger(SaReturnServiceImpl.class);

    private final SaReturnRepository saReturnRepository;
    private final UserService userService;
    private final RefVoucherRepository refVoucherRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final RSInwardOutWardDetailsRepository rsInwardOutwardDetailsRepository;
    private final AccountingObjectRepository accountingObjectRepository;
    private final CurrencyRepository currencyRepository;
    private final GenCodeService genCodeService;
    private final UtilsRepository utilsRepository;
    private final GeneralLedgerServiceImpl generalLedgerService;
    private final SaBillRepository saBillRepository;
    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;
    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final SAInvoiceDetailsRepository saInvoiceDetailsRepository;
    private final SAOrderDetailsRepository saOrderDetailsRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final EInvoiceService eInvoiceService;
    private final UtilsService utilsService;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;

    public SaReturnServiceImpl(SaReturnRepository saReturnRepository, UserService userService,
                               RefVoucherRepository refVoucherRepository, RSInwardOutwardRepository rsInwardOutwardRepository,
                               RSInwardOutWardDetailsRepository rsInwardOutwardDetailsRepository,
                               AccountingObjectRepository accountingObjectRepository, CurrencyRepository currencyRepository,
                               GenCodeService genCodeService, UtilsRepository utilsRepository,
                               GeneralLedgerServiceImpl generalLedgerService, SaBillRepository saBillRepository,
                               IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository,
                               SaReturnDetailsRepository saReturnDetailsRepository,
                               SAInvoiceDetailsRepository saInvoiceDetailsRepository, SAOrderDetailsRepository saOrderDetailsRepository, OrganizationUnitRepository organizationUnitRepository,
                               EInvoiceService eInvoiceService,
                               RepositoryLedgerRepository repositoryLedgerRepository,
                               UtilsService utilsService, MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.saReturnRepository = saReturnRepository;
        this.userService = userService;
        this.refVoucherRepository = refVoucherRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.currencyRepository = currencyRepository;
        this.genCodeService = genCodeService;
        this.utilsRepository = utilsRepository;
        this.generalLedgerService = generalLedgerService;
        this.saBillRepository = saBillRepository;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.saInvoiceDetailsRepository = saInvoiceDetailsRepository;
        this.saOrderDetailsRepository = saOrderDetailsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.eInvoiceService = eInvoiceService;
        this.rsInwardOutwardDetailsRepository = rsInwardOutwardDetailsRepository;
        this.utilsService = utilsService;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    /**
     * Save a saReturn.
     *
     * @param saReturnSaveDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SaReturnSaveDTO save(SaReturnSaveDTO saReturnSaveDTO) throws InvocationTargetException, IllegalAccessException {
        log.debug("Request to save SaReturn : {}", saReturnSaveDTO);
        SaReturn saReturn = saReturnSaveDTO.getSaReturn();
        if (saReturn.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        Boolean checkIsCreateNew = saReturn.getId() == null;
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        boolean isGhiSo = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0");
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            if (TypeConstant.HANG_GIAM_GIA == saReturn.getTypeID() && Boolean.TRUE.equals(saReturn.isIsBill()) && saReturn.getInvoiceTypeID() != null) {
                if (!Strings.isNullOrEmpty(saReturn.getInvoiceNo()) && saReturn.getInvoiceForm() !=  Constants.InvoiceForm.HD_DIEN_TU) {
                    int count = saReturnRepository.countByInvoiceNoAndCompanyID(saReturn.getInvoiceNo(), saReturn.getInvoiceForm(),
                        saReturn.getInvoiceTemplate(), saReturn.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg(),
                        saReturn.getId() != null ? saReturn.getId() : UUID.randomUUID());
                    if (count > 0) {
                        throw new BadRequestAlertException("Khong the xuat hoa don", "saReturn", "duplicate");
                    }
                    List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(
                        saReturn.getInvoiceForm(), saReturn.getInvoiceTypeID(), saReturn.getInvoiceTemplate(), saReturn.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg());
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
                            if (((saReturn.getInvoiceNo().compareTo(item.getFromNo()) >= 0 && item.getToNo() != null && saReturn.getInvoiceNo().compareTo(item.getToNo()) <= 0)
                            || ((item.getToNo() == null && saReturn.getInvoiceNo().compareTo(item.getFromNo()) >= 0))) && saReturn.getInvoiceDate().isBefore(item.getStartUsing())) {
                                error = true;
                            }
                        }
                        if ((minFromNo != null && saReturn.getInvoiceNo().compareTo(minFromNo) < 0) || (maxToNo != null && saReturn.getInvoiceNo().compareTo(maxToNo) > 0)) {
                            throw new BadRequestAlertException("So hoa don khong thuoc dai phat hanh", "saBill", "invalidInvoiceNo");
                        }
                        if (error) {
                            throw new BadRequestAlertException("So hoa don nho hon ngay phat hanh", "saBill", "errorInvoiceDate");
                        }
                    }
                }

            }

            saReturn.setRecorded(false);
            saReturn.setCompanyID(currentUserLoginAndOrg.get().getOrg());

            if (saReturn.getTypeLedger() == 2) {
                if (StringUtils.isEmpty(saReturn.getNoMBook())) {
                    saReturn.setNoMBook(genCodeService.getCodeVoucher(TypeConstant.HANG_BAN_TRA_LAI == saReturn.getTypeID() ? TypeGroupConstant.HANG_BAN_TRA_LAI : TypeGroupConstant.HANG_GIAM_GIA, 1));
                } else if (StringUtils.isEmpty(saReturn.getNoFBook())) {
                    saReturn.setNoFBook(genCodeService.getCodeVoucher(TypeConstant.HANG_BAN_TRA_LAI == saReturn.getTypeID() ? TypeGroupConstant.HANG_BAN_TRA_LAI : TypeGroupConstant.HANG_GIAM_GIA, 0));
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(saReturn.getNoFBook(), saReturn.getNoMBook(), saReturn.getTypeLedger(), saReturn.getId())) {
                throw new BadRequestAlertException("Trung so chung tu", "saReturn", "duplicateNoVoucher");
            }

            BigDecimal totalPaymentOriginal = BigDecimal.ZERO;
            BigDecimal totalPayment = BigDecimal.ZERO;
            BigDecimal totalAmountOriginal = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalVatAmountOriginal = BigDecimal.ZERO;
            BigDecimal totalVatAmount = BigDecimal.ZERO;
            BigDecimal totalDiscountAmountOriginal = BigDecimal.ZERO;
            BigDecimal totalDiscountAmount = BigDecimal.ZERO;
            BigDecimal totalOWAmount = BigDecimal.ZERO;
            for (SaReturnDetails detail : saReturn.getSaReturnDetails()) {
                totalPaymentOriginal = totalPaymentOriginal.add(detail.getAmountOriginal().subtract(detail.getDiscountAmountOriginal()).add(detail.getVatAmountOriginal()));
                if (detail.getAmount() != null && detail.getDiscountAmount() != null && detail.getVatAmount() != null) {
                    totalPayment = totalPayment.add(detail.getAmount().subtract(detail.getDiscountAmount()).add(detail.getVatAmount()));
                }
                if (detail.getAmount() == null) {
                    detail.setOwAmount(BigDecimal.ZERO);
                }
                if (detail.getAmountOriginal() == null) {
                    detail.setAmountOriginal(BigDecimal.ZERO);
                }
                if (detail.getDiscountAmount() == null) {
                    detail.setDiscountAmount(BigDecimal.ZERO);
                }
                if (detail.getDiscountAmountOriginal() == null) {
                    detail.setDiscountAmountOriginal(BigDecimal.ZERO);
                }
                if (detail.getVatAmount() == null) {
                    detail.setVatAmount(BigDecimal.ZERO);
                }
                if (detail.getVatAmountOriginal() == null) {
                    detail.setVatAmountOriginal(BigDecimal.ZERO);
                }
                if (detail.getUnitPrice() == null) {
                    detail.setUnitPrice(BigDecimal.ZERO);
                }
                if (detail.getUnitPriceOriginal() == null) {
                    detail.setUnitPriceOriginal(BigDecimal.ZERO);
                }
                if (detail.getQuantity() == null) {
                    detail.setQuantity(BigDecimal.ZERO);
                }
                if (detail.getMainQuantity() == null) {
                    detail.setMainQuantity(BigDecimal.ZERO);
                }
                if (detail.getOwAmount() == null) {
                    detail.setOwAmount(BigDecimal.ZERO);
                }
                if (detail.getAmountOriginal() != null) {
                    totalAmountOriginal = totalAmountOriginal.add(detail.getAmountOriginal());
                }
                if (detail.getAmount() != null) {
                    totalAmount = totalAmount.add(detail.getAmount());
                }
                if (detail.getVatAmountOriginal() != null) {
                    totalVatAmountOriginal = totalVatAmountOriginal.add(detail.getVatAmountOriginal());
                }
                if (detail.getVatAmount() != null) {
                    totalVatAmount = totalVatAmount.add(detail.getVatAmount());
                }
                if (detail.getDiscountAmountOriginal() != null) {
                    totalDiscountAmountOriginal = totalDiscountAmountOriginal.add(detail.getDiscountAmountOriginal());
                }
                if (detail.getDiscountAmount() != null) {
                    totalDiscountAmount = totalDiscountAmount.add(detail.getDiscountAmount());
                }

                if (saReturn.getTypeID().equals(HANG_BAN_TRA_LAI) && detail.getOwAmount() != null) {
                    totalOWAmount = totalOWAmount.add(detail.getOwAmount());
                }
            }

            saReturn.setTotalAmountOriginal(totalAmountOriginal);
            saReturn.setTotalAmount(totalAmount);
            saReturn.setTotalDiscountAmount(totalDiscountAmount);
            saReturn.setTotalDiscountAmountOriginal(totalDiscountAmountOriginal);
            saReturn.setTotalVATAmount(totalVatAmount);
            saReturn.setTotalVATAmountOriginal(totalVatAmountOriginal);
            saReturn.setTotalPaymentAmountOriginal(totalPaymentOriginal);
            saReturn.setTotalPaymentAmount(totalPayment);
            saReturn.setTotalOWAmount(totalOWAmount);
            RSInwardOutward rsInwardOutward = new RSInwardOutward();
            if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher())) {
                // Lưu thêm rsinwardoutward
                if (saReturn.getRsInwardOutwardID() != null && saReturn.getId() != null) {
                    rsInwardOutwardRepository.deleteById(saReturn.getRsInwardOutwardID());
                }
                try {
                    BeanUtils.copyProperties(rsInwardOutward, saReturn);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                rsInwardOutward.setId(null);
                if (saReturn.getTypeLedger() == 2) {
                    if (StringUtils.isEmpty(saReturnSaveDTO.getNoMBook())) {
                        saReturnSaveDTO.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_INWARD_OUTWARD, 1));
                    } else if (StringUtils.isEmpty(saReturnSaveDTO.getNoFBook())) {
                        saReturnSaveDTO.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_INWARD_OUTWARD, 0));
                    }
                }
                if (!utilsRepository.checkDuplicateNoVoucher(saReturnSaveDTO.getNoFBook(), saReturnSaveDTO.getNoMBook(), saReturn.getTypeLedger(), saReturn.getRsInwardOutwardID())) {
                    throw new BadRequestAlertException("Trung so chung tu", "saReturn", "duplicateNoVoucher");
                }
                rsInwardOutward.setNoFBook(saReturnSaveDTO.getNoFBook());
                rsInwardOutward.setNoMBook(saReturnSaveDTO.getNoMBook());
                rsInwardOutward.setReason(saReturnSaveDTO.getReason());
                if (saReturn.getAccountingObject() != null) {
                    rsInwardOutward.setAccountingObjectID(saReturn.getAccountingObject().getId());
                }
                if (saReturn.getEmployee() != null) {
                    rsInwardOutward.setEmployeeID(saReturn.getEmployee().getId());
                }
                rsInwardOutward.setTotalAmount(totalOWAmount);
                rsInwardOutward.setTotalAmountOriginal(totalOWAmount);
                rsInwardOutward.setTypeID(RS_INWARD_OUTWARD);
                if (isGhiSo) {
                    rsInwardOutward.setRecorded(true);
                } else {
                    rsInwardOutward.setRecorded(false);
                }
                rsInwardOutwardRepository.save(rsInwardOutward);
                saReturn.setRsInwardOutwardID(rsInwardOutward.getId());
            } else if (saReturn.getRsInwardOutwardID() != null){
                rsInwardOutwardRepository.deleteById(saReturn.getRsInwardOutwardID());
                saReturn.setRsInwardOutwardID(null);
            }


            if (saReturn.getId() != null && Boolean.TRUE.equals(saReturn.isIsBill())) {
                // trường hợp không lập kèm hóa đơn,
                // thì xem trước đây có lập kèm hóa đơn không thì xóa đi
                Optional<SaBill> oldSaBill = saBillRepository.findBySaReturnID(saReturn.getId());
                if (oldSaBill.isPresent()) {
                    saBillRepository.delete(oldSaBill.get());
                    refVoucherRepository.deleteByRefID1OrRefID2(oldSaBill.get().getId());
                }
            } else if (Boolean.FALSE.equals(saReturn.isIsBill()) && saReturn.getId() != null ){
                SaReturn saReturnOld = saReturnRepository.findById(saReturn.getId()).get();
                if (Boolean.TRUE.equals(saReturnOld.isIsBill())) {
                    // trường hợp không lập kèm hóa đơn,
                    // thì xem trước đây có lập kèm hóa đơn không thì xóa đi
                    Optional<SaBill> oldSaBill = saBillRepository.findById(saReturn.getSaReturnDetails().stream().findFirst().get().getSaBillID());
                    if (oldSaBill.isPresent()) {
                        saBillRepository.delete(oldSaBill.get());
                        refVoucherRepository.deleteByRefID1(oldSaBill.get().getId());
                    }
                    List<SaReturnDetails> saReturnDetails = new ArrayList<>(saReturn.getSaReturnDetails());
                    for (int i = 0; i < saReturnDetails.size(); i++) {
                        saReturnDetails.get(i).setSaBillDetailID(null);
                        saReturnDetails.get(i).setSaBillID(null);
                    }
                }
            }

            // Lập kèm hóa đơn
            if (Boolean.TRUE.equals(saReturn.isIsBill())) {
                // copy list detail
                List<SaBillDetails> listDestination = new ArrayList<>();
                for (SaReturnDetails source : saReturn.getSaReturnDetails()) {
                    SaBillDetails target = new SaBillDetails();
                    BeanUtils.copyProperties(target, source);
                    target.setMaterialGoods(new MaterialGoods(source.getMaterialGoodsID()));
                    target.setId(null);
                    listDestination.add(target);
                }
                // copy và save sabill
                SaBill saBill = new SaBill();
                BeanUtils.copyProperties(saBill, saReturn);
                if (saReturn.getId() != null && saReturn.getSaReturnDetails().stream().findFirst().get().getSaBillID() != null) {
                    saBill.setId(saReturn.getSaReturnDetails().stream().findFirst().get().getSaBillID());
                }
                saBill.setSaBillDetails(new HashSet<>(listDestination));
                saBill.setTypeID(TypeConstant.XUAT_HOA_DON_HBGG);
                saBill.setRefDateTime(saReturnSaveDTO.getRefDateTime());
                if (checkIsCreateNew) {
                    saBill.setStatusInvoice(Constants.EInvoice.HD_MOI_TAO_LAP);
                    saBill.setiDAdjustInv(null);
                    saBill.setiDReplaceInv(null);
                }
                saBill = saBillRepository.save(saBill);
                /*
                 * Add by Hautv
                 *Create EInvoice
                 *  result trả về null là không có lỗi xảy ra
                 * */
                //region
                if (saReturn.isIsBill() && saReturn.getInvoiceForm().equals(0) && saReturn.getTypeID().equals(340)) {
                    Respone_SDS result = eInvoiceService.createEInvoice(saBill.getId());
                    if (Constants.EInvoice.Respone.Success.equals(result.getStatus())) {
                        if (Constants.EInvoice.SupplierCode.MIV.equals(Utils.getEI_IDNhaCungCapDichVu(userDTO))) {
                            if (!StringUtils.isEmpty(result.getInvoiceNo())) {
                                saReturn.setInvoiceNo(result.getInvoiceNo());
                            }
                        }
                    }
                }
                //endregion
                // xóa sl giao của đơn đặt hàng khi là th sửa
                if (saReturn.getId() != null) {
                    Optional<SaReturn> oldSaReturn = saReturnRepository.findById(saReturn.getId());
                    List<SaReturnDetails> saReturnDetails = new ArrayList<>(oldSaReturn.get().getSaReturnDetails());
                    for (int i = 0; i < saReturnDetails.size(); i++) {
                        if (saReturnDetails.get(i).getSaInvoiceDetailID() != null) {
                            Optional<SAInvoiceDetails> saInvoiceDetails = saInvoiceDetailsRepository.findById(saReturnDetails.get(i).getSaInvoiceDetailID());
                            if (saInvoiceDetails.isPresent() && saInvoiceDetails.get().getsAOrderDetailID() != null) {
                                Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(saInvoiceDetails.get().getsAOrderDetailID());
                                if (saOrderDetails.isPresent()) {
                                    saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(saReturnDetails.get(i).getQuantity()));
                                    saOrderDetailsRepository.save(saOrderDetails.get());
                                }
                            }
                        }
                    }
                }
                // sau khi save bill thành công
                // lưu id ngược lại vào return detail
                List<SaBillDetails> saBillDetails = new ArrayList<>(saBill.getSaBillDetails());
                List<SaReturnDetails> saReturnDetails = new ArrayList<>(saReturn.getSaReturnDetails());

                for (int i = 0; i < saBillDetails.size(); i++) {
                    saReturnDetails.get(i).setSaBillDetailID(saBillDetails.get(i).getId());
                    saReturnDetails.get(i).setSaBillID(saBill.getId());
                    if (saReturnDetails.get(i).getSaInvoiceDetailID() != null) {
                        Optional<SAInvoiceDetails> saInvoiceDetails = saInvoiceDetailsRepository.findById(saReturnDetails.get(i).getSaInvoiceDetailID());
                        if (saInvoiceDetails.isPresent() && saInvoiceDetails.get().getsAOrderDetailID() != null) {
                            Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(saInvoiceDetails.get().getsAOrderDetailID());
                            if (saOrderDetails.isPresent()) {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(saReturnDetails.get(i).getQuantity()));
                                saOrderDetailsRepository.save(saOrderDetails.get());
                            }
                        }
                    }
                }

                saReturn.setSaReturnDetails(new HashSet<>(saReturnDetails));

                // Copy và save chứng từ tham chiếu
                List<RefVoucher> refVouchers = saReturnSaveDTO.getViewVouchers();
                List<RefVoucher> refVouchersSaBill = new ArrayList<>();
                for (RefVoucher source : refVouchers) {
                    RefVoucher target = new RefVoucher();
                    BeanUtils.copyProperties(target, source);
                    target.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                    target.setRefID1(saBill.getId());
                    refVouchersSaBill.add(target);
                }

                refVoucherRepository.deleteByRefID1(saBill.getId());
                refVouchers = refVoucherRepository.saveAll(refVouchersSaBill);
            }
            // xóa sl giao của đơn đặt hàng khi là th sửa
            if (saReturn.getTypeID().equals(HANG_BAN_TRA_LAI)) {
                if (saReturn.getId() != null) {
                    Optional<SaReturn> oldSaReturn = saReturnRepository.findById(saReturn.getId());
                    List<SaReturnDetails> saReturnDetails = new ArrayList<>(oldSaReturn.get().getSaReturnDetails());
                    for (int i = 0; i < saReturnDetails.size(); i++) {
                        if (saReturnDetails.get(i).getSaInvoiceDetailID() != null) {
                            Optional<SAInvoiceDetails> saInvoiceDetails = saInvoiceDetailsRepository.findById(saReturnDetails.get(i).getSaInvoiceDetailID());
                            if (saInvoiceDetails.isPresent() && saInvoiceDetails.get().getsAOrderDetailID() != null) {
                                Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(saInvoiceDetails.get().getsAOrderDetailID());
                                if (saOrderDetails.isPresent()) {
                                    saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(saReturnDetails.get(i).getQuantity()));
                                    saOrderDetailsRepository.save(saOrderDetails.get());
                                }
                            }
                        }
                    }
                }
                List<SaReturnDetails> saReturnDetails = new ArrayList<>(saReturn.getSaReturnDetails());
                for (int i = 0; i < saReturn.getSaReturnDetails().size(); i++) {
                    if (saReturnDetails.get(i).getSaInvoiceDetailID() != null) {
                        Optional<SAInvoiceDetails> saInvoiceDetails = saInvoiceDetailsRepository.findById(saReturnDetails.get(i).getSaInvoiceDetailID());
                        if (saInvoiceDetails.isPresent() && saInvoiceDetails.get().getsAOrderDetailID() != null) {
                            Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(saInvoiceDetails.get().getsAOrderDetailID());
                            if (saOrderDetails.isPresent()) {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(saReturnDetails.get(i).getQuantity()));
                                saOrderDetailsRepository.save(saOrderDetails.get());
                            }
                        }
                    }
                }
            }
            saReturn.getSaReturnDetails().forEach(item -> item.setId(null));
            saReturn = saReturnRepository.save(saReturn);

            materialGoodsSpecificationsLedgerRepository.deleteByRefID(saReturn.getId());
            List<SaReturnDetails> saReturnDetailsSave = new ArrayList<>(saReturn.getSaReturnDetails());
            List<SaReturnDetails> saReturnDetails = new ArrayList<>(saReturnSaveDTO.getSaReturn().getSaReturnDetails());
            List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
            for (int i = 0; i < saReturnDetails.size(); i++) {
                if (saReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                    saReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                    for (MaterialGoodsSpecificationsLedger item: saReturnDetails.get(i).getMaterialGoodsSpecificationsLedgers()) {
                        item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                        item.setDate(rsInwardOutward.getDate());
                        item.setPostedDate(rsInwardOutward.getPostedDate());
                        item.setTypeLedger(rsInwardOutward.getTypeLedger());
                        item.setRefTypeID(rsInwardOutward.getTypeID());
                        item.setReferenceID(rsInwardOutward.getId());
                        item.setDetailID(saReturnDetailsSave.get(i).getId());
                        item.setNoFBook(rsInwardOutward.getNoFBook());
                        item.setNoMBook(rsInwardOutward.getNoMBook());
                        item.setiWRepositoryID(saReturnDetailsSave.get(i).getRepositoryID());
                        item.setoWQuantity(BigDecimal.ZERO);
                        materialGoodsSpecificationsLedgers.add(item);
                    }
                }
            }
            if (materialGoodsSpecificationsLedgers.size() > 0) {
                materialGoodsSpecificationsLedgerRepository.saveAll(materialGoodsSpecificationsLedgers);
            }

            List<RefVoucher> refVouchers = saReturnSaveDTO.getViewVouchers();
            for (RefVoucher item : refVouchers) {
                item.setRefID1(saReturn.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(saReturn.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            saReturnSaveDTO.setSaReturn(saReturn);
            saReturnSaveDTO.setViewVouchers(refVouchers);

            // Tự động ghi sổ
            if (isGhiSo) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(saReturn, msg)) {
                    saReturnSaveDTO.setRecordMsg(msg.getMsgError());
                    if (isGhiSo && Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher())) {
                        rsInwardOutward.setRecorded(false);
                        rsInwardOutwardRepository.save(rsInwardOutward);
                    }
                } else {
                    saReturn.setRecorded(true);
                    saReturn = saReturnRepository.save(saReturn);
                    saReturnSaveDTO.setSaReturn(saReturn);
                }
            }
        }
        return saReturnSaveDTO;
    }

    /**
     * Get all the saReturns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaReturn> findAll(Pageable pageable) {
        log.debug("Request to get all SaReturns");
        return saReturnRepository.findAll(pageable);
    }


    /**
     * Get one saReturn by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SaReturnViewDTO findOne(UUID id) {
        SaReturnViewDTO dto = new SaReturnViewDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setViewVouchers(dtos);

            Optional<SaReturn> saReturn = saReturnRepository.findById(id);
            if (saReturn.isPresent()) {
                dto.setSaReturn(saReturn.get());
                dto.setSaReturnDetails(saReturnDetailsRepository.findViewFullByID(saReturn.get().getId(), currentBook));
                if (Boolean.TRUE.equals(saReturn.get().isIsDeliveryVoucher()) && saReturn.get().getRsInwardOutwardID() != null) {
                    Optional<RSInwardOutward> rsInwardOutward = rsInwardOutwardRepository.findById(saReturn.get().getRsInwardOutwardID());
                    if (rsInwardOutward.isPresent()) {
                        dto.setNoFBook(rsInwardOutward.get().getNoFBook());
                        dto.setNoMBook(rsInwardOutward.get().getNoMBook());
                        dto.setReason(rsInwardOutward.get().getReason());
                    }
                }
                if (Boolean.TRUE.equals(saReturn.get().isIsBill())) {
                    List<SaReturnDetails> saReturnDetails = new ArrayList<>(saReturn.get().getSaReturnDetails());
                    Optional<SaBill> saBill = saBillRepository.findById(saReturnDetails.get(0).getSaBillID());
                    if (saBill.isPresent()) {
                        dto.setRefDateTime(saBill.get().getRefDateTime());
                    }
                }
                return dto;
            }
        }

        return null;
    }

    /**
     * Delete the saReturn by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SaReturn : {}", id);
        // Trong trường hợp xóa, nhưng chứng từ được xóa lập từ RsInwardOutward thì k cho xóa
        // đối với trường hợp hàng bán trả lại
        // update lại sl đơn đặt hàng khi xóa
        Optional<SaReturn> oldSaReturn = saReturnRepository.findById(id);
        List<SaReturnDetails> saReturnDetails = new ArrayList<>(oldSaReturn.get().getSaReturnDetails());
        for (int i = 0; i < saReturnDetails.size(); i++) {
            if (saReturnDetails.get(i).getSaInvoiceDetailID() != null) {
                Optional<SAInvoiceDetails> saInvoiceDetails = saInvoiceDetailsRepository.findById(saReturnDetails.get(i).getSaInvoiceDetailID());
                if (saInvoiceDetails.isPresent() && saInvoiceDetails.get().getsAOrderDetailID() != null) {
                    Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(saInvoiceDetails.get().getsAOrderDetailID());
                    if (saOrderDetails.isPresent()) {
                        saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(saReturnDetails.get(i).getQuantity()));
                        saOrderDetailsRepository.save(saOrderDetails.get());
                    }
                }
            }
        }
        // Xóa hóa đơn trong trường hợp lập kèm hóa đơn
        Optional<SaBill> oldSaBill = saBillRepository.findBySaReturnID(id);
        if (oldSaBill.isPresent()) {
            saBillRepository.delete(oldSaBill.get());
            refVoucherRepository.deleteByRefID1(oldSaBill.get().getId());
            refVoucherRepository.deleteByRefID2(oldSaBill.get().getId());
        }
        rsInwardOutwardRepository.deleteBySaReturnID(id);
        saReturnRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
    }

    @Override
    public SaReturnViewDTO getNextSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                               String fromDate, String toDate, Boolean status,
                                               String freeText, Integer rowIndex, Integer typeID, UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            SaReturnDTO dto = saReturnRepository.getNextSaReturnDTOs(pageable, accountingObjectID, currencyID,
                fromDate, toDate, status, freeText, currentUserLoginAndOrg.get().getOrg(), rowIndex, typeID, currentBook, id);
            if (dto != null) {
                SaReturnViewDTO one = findOne(dto.getId());
                one.setTotalRow(dto.getTotalRow());
                return one;
            }
            return null;
        }

        return null;
    }

    @Override
    public Page<SaReturnDTO> getAllSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                                String fromDate, String toDate, Boolean status, String freeText, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return currentUserLoginAndOrg.map(securityDTO -> saReturnRepository.getAllSaReturnDTOs(pageable, accountingObjectID, currencyID,
                fromDate, toDate, status, freeText, securityDTO.getOrg(), typeID, currentBook)).orElse(null);
        }
        return null;
    }

    @Override
    public SaReturnSearchDTO getAllSearchData() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<AccountingObjectDTO> allAccountingObject = accountingObjectRepository.findAllDTO(SaReturn.class.getSimpleName(), currentUserLoginAndOrg.get().getOrg());
            List<CurrencyCbbDTO> currencies = currencyRepository.findAllDTO(SaReturn.class.getSimpleName(), currentUserLoginAndOrg.get().getOrg());
            return new SaReturnSearchDTO(allAccountingObject, currencies);
        }
        return null;
    }

    @Override
    public byte[] export(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean recorded, String freeText, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<SaReturnDTO> saReturnDTOS = currentUserLoginAndOrg.map(securityDTO -> saReturnRepository.getAllSaReturnDTOs(null, accountingObjectID,
                currencyID, fromDate, toDate, recorded,
                freeText, securityDTO.getOrg(), typeID, currentBook)).orElse(null);
            return ExcelUtils.writeToFile(saReturnDTOS.getContent(), typeID == TypeConstant.HANG_BAN_TRA_LAI ? ExcelConstant.SaReturn.NAME :  ExcelConstant.SaReturn.NAME2, ExcelConstant.SaReturn.HEADER, ExcelConstant.SaReturn.FIELD);
        }

        return null;
    }

    @Override
    public byte[] exportPdf(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean recorded, String freeText, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<SaReturnDTO> saReturnDTOS = currentUserLoginAndOrg.map(securityDTO -> saReturnRepository.getAllSaReturnDTOs(null, accountingObjectID,
                currencyID, fromDate, toDate, recorded,
                freeText, securityDTO.getOrg(), typeID, currentBook)).orElse(null);
            return PdfUtils.writeToFile(saReturnDTOS.getContent(), ExcelConstant.SaReturn.HEADER, ExcelConstant.SaReturn.FIELD);
        }

        return null;
    }

    @Override
    public Page<SaReturnRSInwardDTO> findAllSaReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return saReturnRepository.findAllSaReturnDTO(pageable, accountingObject, fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), currentBook);
        }
        return null;
    }

    @Override
    public List<SaReturnDetails> findAllDetailsById(List<UUID> id) {
        return saReturnDetailsRepository.findBySaReturnIDOrderByOrderPriority(id);
    }

    @Override
    public Record unrecord(Record record) {
        // check xem chứng từ này có liên kết với rsInwardOutWard không,
        // Nếu không thì bỏ ghi sổ, nếu có trả về lỗi để hỏi người dùng tiếp tục hay không
        if (record.getAnswer() == null) {
            int count = rsInwardOutwardDetailsRepository.countBySaReturnID(record.getId());
            if (count > 0) {
                throw new BadRequestAlertException("confirm bo ghi so", "saReturn", "unrecord");
            }
            saReturnRepository.unrecord(record.getId());
            boolean unrecord = generalLedgerService.unrecord(record.getId(), record.getRepositoryLedgerID());
            record.setSuccess(unrecord);
            return record;
        } else {
            // update id liên kết về null
            rsInwardOutwardDetailsRepository.updateSaReturnIDAndSaReturnDetailIDToNull(record.getId());

            saReturnRepository.unrecord(record.getId());
            // Bỏ ghi sổ
            boolean unrecord = generalLedgerService.unrecord(record.getId(), record.getRepositoryLedgerID());
            record.setSuccess(unrecord);
            return record;
        }
    }

    @Override
    public Boolean checkRelateVoucher(UUID saReturnID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            // check SARetrun ở RSInwardOutward nếu có trả ra true
            if (saReturnRepository.checkRelateVoucherRSOutward(saReturnID, currentUserLoginAndOrg.get().getOrg()) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HandlingResultDTO multiDelete(List<SaReturn> saReturns) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(saReturns.size());
        List<SaReturn> listDelete = saReturns.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        for (SaReturn sa: saReturns) {
            if (Boolean.TRUE.equals(sa.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoFBook());
                viewVoucherNo.setNoMBook(sa.getNoMBook());
                if (sa.getTypeID().compareTo(HANG_BAN_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng bán trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng bán giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
            if (sa.getInvoiceForm() != null && sa.getInvoiceNo() != null && sa.getInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
                userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Hóa đơn đã cấp số !");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoFBook());
                viewVoucherNo.setNoMBook(sa.getNoMBook());
                if (sa.getTypeID().compareTo(HANG_BAN_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng bán trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng bán giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        for (SaReturn sa: listDelete) {
            if (sa.getRsInwardOutwardID() != null) {
                uuidListRS.add(sa.getRsInwardOutwardID());
            }
            uuidList.add(sa.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        if (uuidList.size() > 0) {
            saReturnRepository.deleteByListID(uuidList);
            saReturnDetailsRepository.deleteByListID(uuidList);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.deleteByListID(uuidListRS);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<SaReturn> saReturns) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(saReturns.size());
        List<SaReturn> listDelete = saReturns.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (SaReturn sa: saReturns) {
            LocalDate postedDate = LocalDate.parse(sa.getPostedDate().toString());
            if (Boolean.TRUE.equals(sa.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoFBook());
                viewVoucherNo.setNoMBook(sa.getNoMBook());
                if (sa.getTypeID().compareTo(TypeConstant.HANG_BAN_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng bán trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng bán giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
            if (Boolean.FALSE.equals(sa.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoFBook());
                viewVoucherNo.setNoMBook(sa.getNoMBook());
                if (sa.getTypeID().compareTo(TypeConstant.HANG_BAN_TRA_LAI) == 0) {
                    viewVoucherNo.setTypeName("Hàng bán trả lại");
                } else {
                    viewVoucherNo.setTypeName("Hàng bán giảm giá");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        for (SaReturn sa: listDelete) {
            if (sa.getRsInwardOutwardID() != null) {
                uuidListRS.add(sa.getRsInwardOutwardID());
            }
            uuidList.add(sa.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        if (uuidList.size() > 0) {
            saReturnRepository.updateUnrecord(uuidList);
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.updateUnrecord(uuidList);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListRS);
            }
        }
        return handlingResultDTO;
    }
}
