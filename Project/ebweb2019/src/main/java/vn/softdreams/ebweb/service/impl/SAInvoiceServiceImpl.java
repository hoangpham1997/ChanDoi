package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Functions;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
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
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SAInvoice.
 */
@Service
@Transactional
public class SAInvoiceServiceImpl implements SAInvoiceService {

    private final Logger log = LoggerFactory.getLogger(SAInvoiceServiceImpl.class);
    private final SAInvoiceRepository sAInvoiceRepository;
    private final SAInvoiceDetailsRepository saInvoiceDetailsRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final SAOrderDetailsRepository saOrderDetailsRepository;
    private final MCReceiptRepository mcReceiptRepository;
    private final MBDepositRepository mbDepositRepository;
    private final SaBillRepository saBillRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;
    private final GeneralLedgerService generalLedgerService;
    private final EInvoiceService eInvoiceService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;
    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final SaReturnRepository saReturnRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final GenCodeService genCodeService;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final RepositoryLedgerService repositoryLedgerService;
    private final SAOrderRepository saOrderRepository;
    private final UtilsService utilsService;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;

    @Autowired
    UtilsRepository utilsRepository;

    public SAInvoiceServiceImpl(SAInvoiceRepository sAInvoiceRepository,
                                SAInvoiceDetailsService saInvoiceDetailsService, SAInvoiceDetailsRepository saInvoiceDetailsRepository, RefVoucherRepository refVoucherRepository,
                                RSInwardOutwardRepository rsInwardOutwardRepository,
                                SaBillRepository saBillRepository,
                                MCReceiptRepository mcReceiptRepository,
                                MBDepositRepository mbDepositRepository,
                                UserService userService,
                                IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository,
                                GeneralLedgerService generalLedgerService,
                                OrganizationUnitRepository organizationUnitRepository,
                                EInvoiceService eInvoiceService,
                                GenCodeService genCodeService,
                                SAOrderDetailsRepository saOrderDetailsRepository,
                                RepositoryLedgerRepository repositoryLedgerRepository,
                                UtilsService utilsService,
                                RepositoryLedgerService repositoryLedgerService,
                                SAOrderRepository saOrderRepository, RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository,
                                SaReturnDetailsRepository saReturnDetailsRepository, SaReturnRepository saReturnRepository, PPDiscountReturnRepository ppDiscountReturnRepository, MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.sAInvoiceRepository = sAInvoiceRepository;
        this.saInvoiceDetailsRepository = saInvoiceDetailsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mbDepositRepository = mbDepositRepository;
        this.saBillRepository = saBillRepository;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.generalLedgerService = generalLedgerService;
        this.eInvoiceService = eInvoiceService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.genCodeService = genCodeService;
        this.saOrderDetailsRepository = saOrderDetailsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.utilsService = utilsService;
        this.repositoryLedgerService = repositoryLedgerService;
        this.saOrderRepository = saOrderRepository;
        this.rsInwardOutWardDetailsRepository = rsInwardOutWardDetailsRepository;
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    /**
     * Save a sAInvoice.
     *
     * @param sAInvoice the entity to save
     * @return the persisted entity
     */
    @Override
    public SAInvoice save(SAInvoice sAInvoice) {
        log.debug("Request to save SAInvoice : {}", sAInvoice);
        SAInvoice saInvoiceSave = new SAInvoice();
        saInvoiceSave = sAInvoiceRepository.save(sAInvoice);
        return saInvoiceSave;
    }

    /**
     * Get all the sAInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAInvoice> findAll(Pageable pageable) {
        log.debug("Request to get all SAInvoices");
        return sAInvoiceRepository.findAll(pageable);
    }


    /**
     * Get one sAInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAInvoice> findOne(UUID id) {
        log.debug("Request to get SAInvoice : {}", id);
        return sAInvoiceRepository.findById(id);
    }

    /**
     * Delete the sAInvoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAInvoice : {}", id);
        Optional<SAInvoice> oldsaInvoice = sAInvoiceRepository.findById(id);
        Optional<RSInwardOutward> oldRsInwardOutward = rsInwardOutwardRepository.findBySaInvoiceID(id);
        Optional<MCReceipt> oldMCReceipt = mcReceiptRepository.findBySaInvoiceID(id);
        Optional<MBDeposit> oldMBDeposit = mbDepositRepository.findBySaInvoiceID(id);
        if (oldsaInvoice.isPresent()) {
            if (oldsaInvoice.get().getsAInvoiceDetails().stream().filter(c -> c.getsAOrderDetailID() != null).count() > 0) {
                List<SAInvoiceDetails> lstSAold = new ArrayList<>(oldsaInvoice.get().getsAInvoiceDetails());
                List<SAOrderDetails> lstSaOrderDetails = new ArrayList<>();
                for (int i = 0; i < lstSAold.size(); i++) {
                    if (lstSAold.get(i).getsAOrderDetailID() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstSAold.get(i).getsAOrderDetailID());
                        if (saOrderDetails.isPresent()) {
                            saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(lstSAold.get(i).getQuantity()));
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lstSaOrderDetails.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lstSaOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lstSaOrderDetails.get(0).getsAOrderID()).toString());
                }
            }
            if (Boolean.TRUE.equals(oldsaInvoice.get().isIsBill())) {
                List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>(oldsaInvoice.get().getsAInvoiceDetails());
                saBillRepository.deleteById(saInvoiceDetails.get(0).getsABillID());
                refVoucherRepository.deleteByRefID1(saInvoiceDetails.get(0).getsABillID());
                refVoucherRepository.deleteByRefID2(saInvoiceDetails.get(0).getsABillID());
            }
        }
        sAInvoiceRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
        if (oldRsInwardOutward.isPresent()) {
            rsInwardOutwardRepository.delete(oldRsInwardOutward.get());
        }
        if (oldMCReceipt.isPresent()) {
            mcReceiptRepository.delete(oldMCReceipt.get());
        }
        if (oldMBDeposit.isPresent()) {
            mbDepositRepository.delete(oldMBDeposit.get());
        }
        // Xóa hóa đơn trong trường hợp lập kèm hóa đơn
//        if (oldSaBill.isPresent()) {
//            saBillRepository.delete(oldSaBill.get());
//            refVoucherRepository.deleteByRefID1(oldSaBill.get().getId());
//            refVoucherRepository.deleteByRefID2(oldSaBill.get().getId());
//        }

    }

    @Override
    public Page<SAInvoiceViewDTO> searchSAInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = "";
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        }
        return sAInvoiceRepository.searchSAInvoice(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, currentUserLoginAndOrg.get().getOrg(), typeId, currentBook, false);
    }

    @Override
    public SAInvoice findByRowNum(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer rownum, Integer typeId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = "";
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        }
        return sAInvoiceRepository.findByRowNum(accountingObjectID, currencyID, fromDate, toDate, status, keySearch, rownum, currentUserLoginAndOrg.get().getOrg(), currentBook, typeId);
    }

    @Override
    public List<RefVoucherDTO> refVouchersBySAOrderID(UUID id) {
        log.debug("Request to get refVouchersByPPDiscountReturn : {}", id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            return refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        }
        return new ArrayList<>();
    }

    @Override
    public List<RefVoucherDTO> refVouchersByMCReceiptID(UUID id) {
        log.debug("Request to get refVouchersByPPDiscountReturn : {}", id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            return refVoucherRepository.getRefViewVoucherByMCReceiptID(id, isNoMBook);
        }
        return new ArrayList<>();
    }

    @Override
    public List<RefVoucherDTO> refVouchersByMBDepositID(UUID id) {
        log.debug("Request to get refVouchersByPPDiscountReturn : {}", id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            return refVoucherRepository.getRefVouchersByMBDepositID(id, isNoMBook);
        }
        return new ArrayList<>();
    }

    /**
     * Save a saInvoice.
     *
     * @param saInvoice the entity to save
     * @return the persisted entity
     */
    @Override
    public SaInvoiceDTO saveDTO(SAInvoice saInvoice) throws InvocationTargetException, IllegalAccessException {
        log.debug("Request to save SAInvoice : {}", saInvoice);
        if (saInvoice.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        SAInvoice saInvoiceSave = new SAInvoice();
        RSInwardOutward rsInwardOutwardSave = new RSInwardOutward();
        MCReceipt mcReceiptSave = new MCReceipt();
        MBDeposit mbDepositSave = new MBDeposit();
        SaBill saBillSave = new SaBill();
        SaInvoiceDTO saInvoiceDTOSave = new SaInvoiceDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (saInvoice.getId() != null) {
            Optional<SAInvoice> oldsaInvoice = sAInvoiceRepository.findById(saInvoice.getId());
            if (oldsaInvoice.isPresent()) {
                if (!oldsaInvoice.get().getTypeID().equals(saInvoice.getTypeID())) {
                    if (oldsaInvoice.get().getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM)) {
                        Optional<MCReceipt> oldMCReceipt = mcReceiptRepository.findBySaInvoiceID(oldsaInvoice.get().getId());
                        if (oldMCReceipt.isPresent()) {
                            mcReceiptRepository.delete(oldMCReceipt.get());
                        }
                    } else if (oldsaInvoice.get().getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK)) {
                        Optional<MBDeposit> oldMBDeposit = mbDepositRepository.findBySaInvoiceID(oldsaInvoice.get().getId());
                        if (oldMBDeposit.isPresent()) {
                            mbDepositRepository.delete(oldMBDeposit.get());
                        }
                    }
                }
            }
        }
        if (saInvoice.getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
            if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                if (StringUtils.isEmpty(saInvoice.getNoMBook())) {
                    saInvoice.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.SA_INVOICE, Constants.TypeLedger.MANAGEMENT_BOOK));
                }
                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    saInvoice.getRsInwardOutward().setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.MANAGEMENT_BOOK));
                }
            } else {
                if (StringUtils.isEmpty(saInvoice.getNoFBook())) {
                    saInvoice.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.SA_INVOICE, Constants.TypeLedger.FINANCIAL_BOOK));
                }
                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    saInvoice.getRsInwardOutward().setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.FINANCIAL_BOOK));
                }
            }
        }
        if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM)) {
            if (saInvoice.getMcReceipt().getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
                if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                    if (StringUtils.isEmpty(saInvoice.getMcReceipt().getNoMBook())) {
                        saInvoice.getMcReceipt().setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.MC_RECEIPT, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (StringUtils.isEmpty(saInvoice.getMcReceipt().getNoFBook())) {
                        saInvoice.getMcReceipt().setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.MC_RECEIPT, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            }
        } else if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK)) {
            if (saInvoice.getMbDeposit().getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
                if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                    if (StringUtils.isEmpty(saInvoice.getMbDeposit().getNoMBook())) {
                        saInvoice.getMbDeposit().setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.MB_DEPOSIT, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (StringUtils.isEmpty(saInvoice.getMbDeposit().getNoFBook())) {
                        saInvoice.getMbDeposit().setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.MB_DEPOSIT, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            }
        }
        if (!utilsRepository.checkDuplicateNoVoucher(saInvoice.getNoFBook(), saInvoice.getNoMBook(), saInvoice.getTypeLedger(), saInvoice.getId())) {
            saInvoiceDTOSave.setSaInvoice(saInvoice);
            saInvoiceDTOSave.setStatus(1);
            return saInvoiceDTOSave;
        }
        if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher()) && !utilsRepository.checkDuplicateNoVoucher(saInvoice.getRsInwardOutward().getNoFBook(), saInvoice.getRsInwardOutward().getNoMBook(), saInvoice.getRsInwardOutward().getTypeLedger(), saInvoice.getRsInwardOutward().getId())) {
            saInvoiceDTOSave.setSaInvoice(saInvoice);
            saInvoiceDTOSave.setStatus(2);
            return saInvoiceDTOSave;
        }
        if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM) && !utilsRepository.checkDuplicateNoVoucher(saInvoice.getMcReceipt().getNoFBook(), saInvoice.getMcReceipt().getNoMBook(), saInvoice.getMcReceipt().getTypeLedger(), saInvoice.getMcReceipt().getId())) {
            saInvoiceDTOSave.setSaInvoice(saInvoice);
            saInvoiceDTOSave.setStatus(3);
            return saInvoiceDTOSave;
        }
        if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK) && !utilsRepository.checkDuplicateNoVoucher(saInvoice.getMbDeposit().getNoFBook(),
            saInvoice.getMbDeposit().getNoMBook(), saInvoice.getMbDeposit().getTypeLedger(), saInvoice.getMbDeposit().getId())) {
            saInvoiceDTOSave.setSaInvoice(saInvoice);
            saInvoiceDTOSave.setStatus(4);
            return saInvoiceDTOSave;
        }
        if (Boolean.TRUE.equals(saInvoice.isIsBill()) &&
            (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("0") ||
                !saInvoice.getInvoiceForm().equals(Constants.InvoiceForm.HD_DIEN_TU))) {
            int count = saBillRepository.countByInvoiceNoAndCompanyID(saInvoice.getInvoiceNo(), saInvoice.getInvoiceForm(),
                saInvoice.getInvoiceTemplate(), saInvoice.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg(),
                saInvoice.getSaBill().getId() != null ? saInvoice.getSaBill().getId() : UUID.randomUUID());
            if (count > 0) {
                saInvoiceDTOSave.setSaInvoice(saInvoice);
                saInvoiceDTOSave.setStatus(5);
                return saInvoiceDTOSave;
            }
            List<IaPublishInvoiceDetails> pTemplate = iaPublishInvoiceDetailsRepository.findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(
                saInvoice.getInvoiceForm(), saInvoice.getInvoiceTypeID(), saInvoice.getInvoiceTemplate(), saInvoice.getInvoiceSeries(), currentUserLoginAndOrg.get().getOrg());
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
                    if (((saInvoice.getInvoiceNo().compareTo(item.getFromNo()) >= 0 && item.getToNo() != null && saInvoice.getInvoiceNo().compareTo(item.getToNo()) <= 0)
                        || ((item.getToNo() == null && saInvoice.getInvoiceNo().compareTo(item.getFromNo()) >= 0))) && saInvoice.getInvoiceDate().isBefore(item.getStartUsing())) {
                        error = true;
                    }
                }
                if ((minFromNo != null && saInvoice.getInvoiceNo().compareTo(minFromNo) < 0) || (maxToNo != null && saInvoice.getInvoiceNo().compareTo(maxToNo) > 0)) {
                    saInvoiceDTOSave.setSaInvoice(saInvoice);
                    saInvoiceDTOSave.setStatus(6);
                    return saInvoiceDTOSave;
                }
                if (error) {
                    saInvoiceDTOSave.setSaInvoice(saInvoice);
                    saInvoiceDTOSave.setStatus(10);
                    return saInvoiceDTOSave;
                }
            }
        }
        if (currentUserLoginAndOrg.isPresent()) {
            saInvoice.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (saInvoice.getId() != null && Boolean.FALSE.equals(saInvoice.isIsDeliveryVoucher()) && saInvoice.getRsInwardOutwardID() != null) {
                // trường hợp không lập kèm pxk,
                // thì xem trước đây có lập kèm pxk không thì xóa đi
                Optional<RSInwardOutward> oldRSInwardOutward = rsInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID());
                if (oldRSInwardOutward.isPresent()) {
                    rsInwardOutwardRepository.delete(oldRSInwardOutward.get());
                }
            }
            if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                saInvoice.getRsInwardOutward().setCompanyID(saInvoice.getCompanyID());
                if (saInvoice.getRsInwardOutward().getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
                    if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        if (StringUtils.isEmpty(saInvoice.getRsInwardOutward().getNoMBook())) {
                            saInvoice.getRsInwardOutward().setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.MANAGEMENT_BOOK));
                        }
                    } else {
                        if (StringUtils.isEmpty(saInvoice.getRsInwardOutward().getNoFBook())) {
                            saInvoice.getRsInwardOutward().setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.RS_OUTWARD, Constants.TypeLedger.FINANCIAL_BOOK));
                        }
                    }
                }
                if (Boolean.TRUE.equals(saInvoice.getCheckRecord()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                    saInvoice.getRsInwardOutward().setRecorded(true);
                }
                rsInwardOutwardSave = rsInwardOutwardRepository.save(saInvoice.getRsInwardOutward());
                saInvoice.setRsInwardOutwardID(rsInwardOutwardSave.getId());
            }
            if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM)) {
                saInvoice.getMcReceipt().setCompanyID(saInvoice.getCompanyID());
                if (Boolean.TRUE.equals(saInvoice.getCheckRecord()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                    saInvoice.getMcReceipt().setRecorded(true);
                }
                mcReceiptSave = mcReceiptRepository.save(saInvoice.getMcReceipt());
                saInvoice.setMcReceiptID(mcReceiptSave.getId());
            } else if (saInvoice.getTypeID().equals(TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK)) {
                saInvoice.getMbDeposit().setCompanyID(saInvoice.getCompanyID());
                if (Boolean.TRUE.equals(saInvoice.getCheckRecord()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                    saInvoice.getMbDeposit().setRecorded(true);
                }
                mbDepositSave = mbDepositRepository.save(saInvoice.getMbDeposit());
                saInvoice.setMbDepositID(mbDepositSave.getId());
            }
            if (Boolean.FALSE.equals(saInvoice.isIsBill()) && saInvoice.getId() != null ){
                SAInvoice saInvoiceOld = sAInvoiceRepository.findById(saInvoice.getId()).get();
                if (Boolean.TRUE.equals(saInvoiceOld.isIsBill())) {
                    // trường hợp không lập kèm hóa đơn,
                    // thì xem trước đây có lập kèm hóa đơn không thì xóa đi
                    Optional<SaBill> oldSaBill = saBillRepository.findById(saInvoice.getsAInvoiceDetails().stream().findFirst().get().getsABillID());
                    if (oldSaBill.isPresent()) {
                        saBillRepository.delete(oldSaBill.get());
                        refVoucherRepository.deleteByRefID1(oldSaBill.get().getId());
                    }
                    List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>(saInvoice.getsAInvoiceDetails());
                    for (int i = 0; i < saInvoiceDetails.size(); i++) {
                        saInvoiceDetails.get(i).setsABillDetailID(null);
                        saInvoiceDetails.get(i).setsABillID(null);
                    }
                }
            }
            if (Boolean.TRUE.equals(saInvoice.isIsBill())) {
                saInvoice.getSaBill().setCompanyID(saInvoice.getCompanyID());
                saBillSave = saBillRepository.save(saInvoice.getSaBill());
                List<SaBillDetails> saBillDetails = new ArrayList<>(saBillSave.getSaBillDetails());
                List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>(saInvoice.getsAInvoiceDetails());

                for (int i = 0; i < saBillDetails.size(); i++) {
                    saInvoiceDetails.get(i).setsABillDetailID(saBillDetails.get(i).getId());
                    saInvoiceDetails.get(i).setsABillID(saBillSave.getId());
                }
                saInvoice.setsAInvoiceDetails(new HashSet<>(saInvoiceDetails));
                // Copy và save chứng từ tham chiếu
                List<RefVoucherDTO> refVouchers = saInvoice.getViewVouchers();
                List<RefVoucher> refVouchersSaBill = new ArrayList<>();
                for (RefVoucherDTO source : refVouchers) {
                    RefVoucher target = new RefVoucher();
                    BeanUtils.copyProperties(target, source);
                    target.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                    target.setRefID1(saBillSave.getId());
                    refVouchersSaBill.add(target);
                }
                refVoucherRepository.deleteByRefID1(saBillSave.getId());
                refVoucherRepository.saveAll(refVouchersSaBill);
            }
            if (saInvoice.getId() == null) {
                List<SAInvoiceDetails> lstSA = new ArrayList<>(saInvoice.getsAInvoiceDetails());
                List<SAOrderDetails> lstSaOrderDetails = new ArrayList<>();
                for (int i = 0; i < lstSA.size(); i++) {
                    if (lstSA.get(i).getsAOrderDetailID() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstSA.get(i).getsAOrderDetailID());
                        if (saOrderDetails.isPresent()) {
                            if (saOrderDetails.get().getQuantityDelivery() == null) {
                                saOrderDetails.get().setQuantityDelivery(lstSA.get(i).getQuantity());
                            } else {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(lstSA.get(i).getQuantity()));
                            }
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lstSaOrderDetails.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lstSaOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lstSaOrderDetails.get(0).getsAOrderID()).toString());
                }
            } else if (saInvoice.getId() != null) {
                Optional<SAInvoice> oldsaInvoice = sAInvoiceRepository.findById(saInvoice.getId());
                List<SAOrderDetails> lstSaOrderDetails = new ArrayList<>();
                List<SAInvoiceDetails> lstSAold = new ArrayList<>(oldsaInvoice.get().getsAInvoiceDetails());
                for (int i = 0; i < lstSAold.size(); i++) {
                    if (lstSAold.get(i).getsAOrderDetailID() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstSAold.get(i).getsAOrderDetailID());
                        if (saOrderDetails.isPresent()) {
                            saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().subtract(lstSAold.get(i).getQuantity()));
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lstSaOrderDetails.add(saOrderDetails.get());
                        }
                    }
                }
                // Add by Hautv
                if (lstSaOrderDetails.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lstSaOrderDetails.get(0).getsAOrderID()).toString());
                }
                List<SAInvoiceDetails> lstSA = new ArrayList<>(saInvoice.getsAInvoiceDetails());
                List<SAOrderDetails> lstSaOrderDetails1 = new ArrayList<>();
                for (int i = 0; i < lstSA.size(); i++) {
                    if (lstSA.get(i).getsAOrderDetailID() != null) {
                        Optional<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findById(lstSA.get(i).getsAOrderDetailID());
                        if (saOrderDetails.isPresent()) {
                            if (saOrderDetails.get().getQuantityDelivery() == null) {
                                saOrderDetails.get().setQuantityDelivery(lstSA.get(i).getQuantity());
                            } else {
                                saOrderDetails.get().setQuantityDelivery(saOrderDetails.get().getQuantityDelivery().add(lstSA.get(i).getQuantity()));
                            }
                            saOrderDetailsRepository.save(saOrderDetails.get());
                            lstSaOrderDetails1.add(saOrderDetails.get());
                        }
                    }
                }
                if (lstSaOrderDetails1.size() > 0) {
                    saOrderRepository.updateStatusSAOder(Utils.uuidConvertToGUID(lstSaOrderDetails1.get(0).getsAOrderID()).toString());
                }
            }
            saInvoiceSave = sAInvoiceRepository.save(saInvoice);

            materialGoodsSpecificationsLedgerRepository.deleteByRefID(saInvoiceSave.getId());
            List<SAInvoiceDetails> saInvoiceDetailsSave = new ArrayList<>(saInvoiceSave.getsAInvoiceDetails());
            List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>(saInvoice.getsAInvoiceDetails());
            List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
            for (int i = 0; i < saInvoiceDetails.size(); i++) {
                if (saInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                    saInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                    for (MaterialGoodsSpecificationsLedger item: saInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers()) {
                        item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                        item.setDate(saInvoiceSave.getDate());
                        item.setPostedDate(saInvoiceSave.getPostedDate());
                        item.setTypeLedger(saInvoiceSave.getTypeLedger());
                        item.setRefTypeID(saInvoiceSave.getTypeID());
                        item.setReferenceID(saInvoiceSave.getId());
                        item.setDetailID(saInvoiceDetailsSave.get(i).getId());
                        item.setNoFBook(saInvoiceSave.getNoFBook());
                        item.setNoMBook(saInvoiceSave.getNoMBook());
                        item.setiWRepositoryID(null);
                        item.setiWQuantity(BigDecimal.ZERO);
                        item.setiWRepositoryID(saInvoiceDetailsSave.get(i).getRepositoryID());
                        materialGoodsSpecificationsLedgers.add(item);
                    }
                }
            }
            if (materialGoodsSpecificationsLedgers.size() > 0) {
                materialGoodsSpecificationsLedgerRepository.saveAll(materialGoodsSpecificationsLedgers);
            }

            // Sau khi lưu hóa đơn
            /*
             * Add by Hautv
             *Create EInvoice
             *  result trả về null là không có lỗi xảy ra
             * */
            //region
            if (saInvoiceSave.isIsBill() && saInvoiceSave.getInvoiceForm().equals(Constants.InvoiceForm.HD_DIEN_TU)) {
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
                if (!Constants.EInvoice.Respone.Success.equals(result.getStatus())) {
                    //Lỗi khi tạo hóa đơn
                } else {
                    if (Constants.EInvoice.SupplierCode.MIV.equals(Utils.getEI_IDNhaCungCapDichVu(userDTO))) {
                        if (!StringUtils.isEmpty(result.getInvoiceNo())) {
                            saInvoiceSave.setInvoiceNo(result.getInvoiceNo());
                        }
                    }
                }
            }
            //endregion
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : saInvoice.getViewVouchers() != null ? saInvoice.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(saInvoiceSave.getCompanyID());
                refVoucher.setRefID1(saInvoiceSave.getId());
                refVoucher.setRefID2(item.getRefID2());
                refVoucher.setAttach(item.getAttach());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(saInvoiceSave.getId());
            refVoucherRepository.saveAll(refVouchers);
            saInvoiceSave.setViewVouchers(saInvoice.getViewVouchers());
            if (Boolean.TRUE.equals(saInvoice.getCheckRecord()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(saInvoiceSave, msg)) {
                    if (Constants.MSGRecord.XUAT_QUA_SO_TON.equals(msg)) {
                        saInvoiceDTOSave.setStatus(8);
                        saInvoiceDTOSave.setSaInvoice(saInvoiceSave);
                        return saInvoiceDTOSave;
                    } else {
                        saInvoiceDTOSave.setStatus(7);
                        saInvoiceDTOSave.setSaInvoice(saInvoiceSave);
                        return saInvoiceDTOSave;
                    }
                } else {
                    saInvoiceSave.setRecorded(true);
                    for (SAInvoiceDetails sad: saInvoiceSave.getsAInvoiceDetails()) {
                        sad.setsAInvoiceId(saInvoiceSave.getId());
                    }
                    saInvoiceSave = sAInvoiceRepository.save(saInvoiceSave);
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
                        if (valCalNow.equals(Constants.CalculationMethod.TINH_NGAY_KHI_LAP_CHUNG_TU) && calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI && saInvoiceSave.isIsDeliveryVoucher()) {
                            List<UUID> listUUIDs = saInvoiceSave.getsAInvoiceDetails().stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList());
                            repositoryLedgerService.calculateOWPrice(calculationMethod, listUUIDs, saInvoiceSave.getDate().toString(), saInvoiceSave.getDate().toString());
                        } else if (valCalNow.equals(Constants.CalculationMethod.TINH_NGAY_KHI_LAP_CHUNG_TU) && calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_NHAP_TRUOC_XUAT_TRUOC) {
                            List<UUID> listUUIDs = saInvoiceSave.getsAInvoiceDetails().stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList());
                            repositoryLedgerService.calculateOWPrice(calculationMethod, listUUIDs, saInvoiceSave.getDate().toString(), saInvoiceSave.getDate().toString());
                        }
                    }
                }
            }
            saInvoiceDTOSave.setSaInvoice(saInvoiceSave);
            saInvoiceDTOSave.setStatus(0);
            return saInvoiceDTOSave;
        }
        throw new BadRequestAlertException("Không thể lưu bán hàng", "", "");
    }

    @Override
    public List<SAInvoiceDetailPopupDTO> getSaInvoiceDetail(List<SAInvoiceDetailPopupDTO> saInvoiceDTO) {
        UserDTO userDTO = userService.getAccount();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {

            SystemOption systemOption = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).findFirst().get();

            if (saInvoiceDTO.get(0).getTypeID().equals(TypeConstant.XUAT_HOA_DON)) {
                List<UUID> ids = saInvoiceDTO.stream().map(SAInvoiceDetailPopupDTO::getSaInvoiceID).collect(Collectors.toList());
                List<SAInvoiceDetailPopupDTO> saInvoiceDetail = new ArrayList<>();
                if (saInvoiceDTO.get(0).getRefTypeID().equals(TypeConstant.BAN_HANG_CHUA_THU_TIEN)) {
                    saInvoiceDetail = sAInvoiceRepository.getSaInvoiceDetailBySAInvoiceID(ids, systemOption.getData());
                } else if (saInvoiceDTO.get(0).getRefTypeID().equals(TypeConstant.HANG_GIAM_GIA)) {
                    saInvoiceDetail = saReturnRepository.getSAReturnDetailBySAReturnID(ids, systemOption.getData());
                } else if (saInvoiceDTO.get(0).getRefTypeID().equals(TypeConstant.MUA_HANG_TRA_LAI)) {
                    saInvoiceDetail = ppDiscountReturnRepository.getPPDiscountDetailByPPDiscountID(ids, systemOption.getData());
                }
                return saInvoiceDetail;
            } else {
                List<UUID> ids = saInvoiceDTO.stream().map(SAInvoiceDetailPopupDTO::getId).collect(Collectors.toList());
                List<SAInvoiceDetailPopupDTO> saInvoiceDetail = sAInvoiceRepository.getSaInvoiceDetail(ids, systemOption.getData());

                // set lai số lượng trả lại
                saInvoiceDetail.forEach(item -> saInvoiceDTO.forEach(item2 -> {
                    if (item2.getId().compareTo(item.getId()) == 0) {
                        item.setReturnQuantity(item2.getReturnQuantity());
                        if (item2.getQuantity() != null) {
                            item.setQuantity(item2.getQuantity());
                        }
                    }
                }));
                return saInvoiceDetail;
            }
        }
        return null;
    }

    @Override
    public Page<SAInvoicePopupDTO> gSaInvoiceSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, Integer typeID, String currencyID, UUID objectID, Integer createForm) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent() && userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            if (TypeConstant.XUAT_HOA_DON == typeID) {
                if (createForm == TypeConstant.BAN_HANG_CHUA_THU_TIEN) {
                    List<UUID> listSAInvoiceID = new ArrayList<>();
                    if (objectID != null) {
                        listSAInvoiceID = sAInvoiceRepository.getSAInvoiceIDFromSABill(objectID).stream().map(x -> x.getId()).collect(Collectors.toList());;
                    }
                    return sAInvoiceRepository.getAllSaInvoiceSaBillPopupDTOs(pageable, accountingObjectID, fromDate, toDate,
                        currentUserLoginAndOrg.get().getOrg(), currentBook, currencyID, listSAInvoiceID);
                } else if (createForm == TypeConstant.HANG_GIAM_GIA) {
                    List<UUID> listSAReturnID = new ArrayList<>();
                    if (objectID != null) {
                        listSAReturnID = saReturnRepository.getSAReturnIDFromSABill(objectID).stream().map(x -> x.getId()).collect(Collectors.toList());;
                    }
                    return saReturnRepository.getAllSaReturnSaBillPopupDTOs(pageable, accountingObjectID, fromDate, toDate,
                        currentUserLoginAndOrg.get().getOrg(), currentBook, currencyID, listSAReturnID);
                } else {
                    List<UUID> listPPDiscountReturnID = new ArrayList<>();
                    if (objectID != null) {
                        listPPDiscountReturnID = ppDiscountReturnRepository.getPPDiscountReturnIDFromSABill(objectID).stream().map(x -> x.getId()).collect(Collectors.toList());;
                    }
                    return ppDiscountReturnRepository.getAllPPDiscountReturnSaBillPopupDTOs(pageable, accountingObjectID, fromDate, toDate,
                        currentUserLoginAndOrg.get().getOrg(), currentBook, currencyID, listPPDiscountReturnID);
                }
            } else {
                List<UUID> listSAInvoiceID = new ArrayList<>();
                if (TypeConstant.HANG_GIAM_GIA == typeID) {
                    listSAInvoiceID = sAInvoiceRepository.getSAInvoiceIDFromSAReturn();
                }
                return sAInvoiceRepository.getAllSaInvoiceSaReturnPopupDTOs(pageable, accountingObjectID, fromDate, toDate,
                    currentUserLoginAndOrg.get().getOrg(), currentBook, currencyID, typeID, listSAInvoiceID);
            }
        }

        return null;
    }

    @Override
    public byte[] exportExcel(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<SAInvoiceViewDTO> saInvoice = currentUserLoginAndOrg.map(securityDTO ->  sAInvoiceRepository.searchSAInvoice(null, accountingObjectID, currencyID, fromDate,
                toDate, status, keySearch, currentUserLoginAndOrg.get().getOrg(), typeId, currentBook, true)).orElse(null);

            return ExcelUtils.writeToFile(saInvoice.getContent(), ExcelConstant.SAInvoice2.NAME, ExcelConstant.SAInvoice2.HEADER, ExcelConstant.SAInvoice2.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportPdf(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<SAInvoiceViewDTO> saInvoice = currentUserLoginAndOrg.map(securityDTO -> sAInvoiceRepository.searchSAInvoice(null, accountingObjectID, currencyID, fromDate,
                toDate, status, keySearch, currentUserLoginAndOrg.get().getOrg(), typeId, currentBook, true)).orElse(null);

            return PdfUtils.writeToFile(saInvoice.getContent(), ExcelConstant.SAInvoice2.HEADER, ExcelConstant.SAInvoice2.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public Integer checkRelateVoucher(UUID sAInvoice, Boolean isCheckKPXK) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkMoney = false;
        boolean checkRelate = false;
        if (currentUserLoginAndOrg.isPresent()) {
            // check saInvoice ở saBill , SaReturn, RSInwardOutward nếu có trả ra true
            List<UUID> saInvoiceIDs = new ArrayList<>();
            saInvoiceIDs.add(sAInvoice);
            if (sAInvoiceRepository.getRelateIDBySAInvoiceID(saInvoiceIDs).size() > 0) {
                checkMoney = true;
            }
            if (isCheckKPXK != null && isCheckKPXK) {
                if (sAInvoiceRepository.checkRelateVoucherRSOutward(sAInvoice, currentUserLoginAndOrg.get().getOrg()) > 0) {
                    checkRelate = true;
                }
            } else {
                if (sAInvoiceRepository.checkRelateVoucherSaBill(sAInvoice, currentUserLoginAndOrg.get().getOrg()) > 0) {
                    checkRelate = true;
                } else if(sAInvoiceRepository.checkRelateVoucherSAReturn(sAInvoice, currentUserLoginAndOrg.get().getOrg()) > 0) {
                    checkRelate = true;
                } else if(sAInvoiceRepository.checkRelateVoucherRSOutward(sAInvoice, currentUserLoginAndOrg.get().getOrg()) > 0) {
                    checkRelate = true;
                }
            }
            if (checkMoney && checkRelate) {
                return 1;
            } else if (checkMoney) {
                return 2;
            } else if (checkRelate) {
                return 3;
            }
        }
        return 0;
    }

    @Override
    public Page<SAInvoiceDTO> findAllSAInvoiceDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return saInvoiceDetailsRepository.findAllSAInvoiceDTO(pageable, accountingObject, fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), currentBook);
        }
        return null;
    }

    @Override
    public List<UUID> getSAInvoiceBySABillID(UUID saBillID) {
        List<UUID> uuids = new ArrayList<>();
        uuids = sAInvoiceRepository.getSAInvoiceIDFromSABill(saBillID).stream().map(x -> x.getId()).collect(Collectors.toList());
        if (uuids.size() == 0 || uuids == null) {
            uuids = saReturnRepository.getSAReturnIDFromSABill(saBillID).stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        if (uuids.size() == 0 || uuids == null) {
            uuids = ppDiscountReturnRepository.getPPDiscountReturnIDFromSABill(saBillID).stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        return uuids;
    }

    @Override
    public HandlingResultDTO multiDelete(List<SAInvoice> saInvoices) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(saInvoices.size());
        List<SAInvoice> listDelete = saInvoices.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        for (SAInvoice sa: saInvoices) {
            if (Boolean.TRUE.equals(sa.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoBook());
                viewVoucherNo.setNoMBook(sa.getNoBook());
                if (sa.getTypeID().compareTo(TypeConstant.BAN_HANG_CHUA_THU_TIEN) == 0) {
                    viewVoucherNo.setTypeName("Bán hàng chưa thu tiền");
                } else {
                    viewVoucherNo.setTypeName("Bán hàng thu tiền ngay");
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
                viewVoucherNo.setNoFBook(sa.getNoBook());
                viewVoucherNo.setNoMBook(sa.getNoBook());
                if (sa.getTypeID().compareTo(TypeConstant.BAN_HANG_CHUA_THU_TIEN) == 0) {
                    viewVoucherNo.setTypeName("Bán hàng chưa thu tiền");
                } else {
                    viewVoucherNo.setTypeName("Bán hàng thu tiền ngay");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidListMC = new ArrayList<>();
        List<UUID> uuidListMB = new ArrayList<>();
        for (SAInvoice sa: listDelete) {
            if (sa.getRsInwardOutwardID() != null) {
                uuidListRS.add(sa.getRsInwardOutwardID());
            }
            if (sa.getMcReceiptID() != null) {
                uuidListMC.add(sa.getMcReceiptID());
            }
            if (sa.getMbDepositID() != null) {
                uuidListMB.add(sa.getMbDepositID());
            }
            uuidList.add(sa.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        if (uuidList.size() > 0) {
            // Cap nhat lai Quantity ben SAOrder
            List<SAInvoiceDetails> oldSADetailList = sAInvoiceRepository.findSAOrderIDByListSAInvoice(uuidList);
            List<UUID> saODetailsID = oldSADetailList.stream().map(x -> x.getsAOrderDetailID()).collect(Collectors.toList());
            if (oldSADetailList.size() > 0) {
                saOrderRepository.updateQuantitySAOrderBySA(oldSADetailList);
                // Add by Hautv
                List<SAOrderDetails> saOrderDetails = saOrderDetailsRepository.findAllById(saODetailsID);
                Map<UUID, List<SAOrderDetails>> saorders =
                    saOrderDetails.stream().collect(Collectors.groupingBy(SAOrderDetails::getsAOrderID));
                if (saorders.size() > 0) {
                    List<UUID> uuidList1 = new ArrayList<>(saorders.keySet());
                    uuidList1 = uuidList1.stream().map(Utils::uuidConvertToGUID).collect(Collectors.toList());
                    String lstID = uuidList1.stream().map(Functions.toStringFunction()).collect(Collectors.joining(","));
                    saOrderRepository.updateStatusSAOder(lstID);
                }
            }
            sAInvoiceRepository.deleteListSAInvoiceID(uuidList, currentUserLoginAndOrg.get().getOrg());
            saInvoiceDetailsRepository.deleteByListSAInvoiceID(uuidList);
            List<String> listRelateID = sAInvoiceRepository.getRelateIDBySAInvoiceID(uuidList);
            if (listRelateID.size() > 0) {
                sAInvoiceRepository.DeleteRelate(listRelateID);
            }
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.deleteByListID(uuidListRS);
            }
            if (uuidListMC.size() > 0) {
                mcReceiptRepository.deleteByListID(uuidListMC);
            }
            if (uuidListMB.size() > 0) {
                mbDepositRepository.deleteByListID(uuidListMB);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<SAInvoice> saInvoices) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(saInvoices.size());
        List<SAInvoice> listDelete = saInvoices.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (SAInvoice sa: saInvoices) {
            LocalDate postedDate = LocalDate.parse(sa.getPostedDate().toString());
            if (Boolean.TRUE.equals(sa.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoBook());
                viewVoucherNo.setNoMBook(sa.getNoBook());
                if (sa.getTypeID().compareTo(TypeConstant.BAN_HANG_CHUA_THU_TIEN) == 0) {
                    viewVoucherNo.setTypeName("Bán hàng chưa thu tiền");
                } else {
                    viewVoucherNo.setTypeName("Bán hàng thu tiền ngay");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
            if (Boolean.FALSE.equals(sa.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                viewVoucherNo.setPostedDate(sa.getPostedDate());
                viewVoucherNo.setDate(sa.getDate());
                viewVoucherNo.setNoFBook(sa.getNoBook());
                viewVoucherNo.setNoMBook(sa.getNoBook());
                if (sa.getTypeID().compareTo(TypeConstant.BAN_HANG_CHUA_THU_TIEN) == 0) {
                    viewVoucherNo.setTypeName("Bán hàng chưa thu tiền");
                } else {
                    viewVoucherNo.setTypeName("Bán hàng thu tiền ngay");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(sa);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidListMC = new ArrayList<>();
        List<UUID> uuidListMB = new ArrayList<>();
        for (SAInvoice sa: listDelete) {
            if (sa.getRsInwardOutwardID() != null) {
                uuidListRS.add(sa.getRsInwardOutwardID());
            }
            if (sa.getMcReceiptID() != null) {
                uuidListMC.add(sa.getMcReceiptID());
            }
            if (sa.getMbDepositID() != null) {
                uuidListMB.add(sa.getMbDepositID());
            }
            uuidList.add(sa.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        if (uuidList.size() > 0) {
            sAInvoiceRepository.updateUnrecord(uuidList);
            rsInwardOutWardDetailsRepository.UpdateSAInvoiceIDNull(uuidList);
            saReturnDetailsRepository.UpdateSAInvoiceIDNull(uuidList);
            List<String> listRelateID = sAInvoiceRepository.getRelateIDBySAInvoiceID(uuidList);
            if (listRelateID.size() > 0) {
                sAInvoiceRepository.DeleteRelate(listRelateID);
            }
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.updateUnrecord(uuidListRS);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListRS);
            }
            if (uuidListMC.size() > 0) {
                mcReceiptRepository.updateUnrecord(uuidListMC);
            }
            if (uuidListMB.size() > 0) {
                mbDepositRepository.updateUnrecord(uuidListMB);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public Boolean isBanHangChuaThuTien(UUID refID) {
        if(sAInvoiceRepository.isBanHangChuaThuTien(refID) == 0) {
            return false;
        } else {
            return true;
        }
    }

}
