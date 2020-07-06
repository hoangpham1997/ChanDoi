package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.PageImpl;
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
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PPInvoice.
 */
@Service
@Transactional
public class PPInvoiceServiceImpl implements PPInvoiceService {

    private final Logger log = LoggerFactory.getLogger(PPInvoiceServiceImpl.class);

    private final PPInvoiceRepository pPInvoiceRepository;
    private final PPInvoiceDetailsRepository ppInvoiceDetailsRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final UtilsRepository utilsRepository;
    private final GenCodeService genCodeService;
    private final MCPaymentRepository mcPaymentRepository;
    private final MBTellerPaperRepository mbTellerPaperRepository;
    private final MBCreditCardRepository mbCreditCardRepository;
    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;
    private final AccountingObjectRepository accountingObjectRepository;
    private final UserService userService;
    private final GeneralLedgerService generalLedgerService;
    private final RefVoucherRepository refVoucherRepository;
    private final PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final PporderdetailRepository pporderdetailRepository;
    private final PporderRepository pporderRepository;
    private final UtilsService utilsService;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final MCPaymentDetailVendorRepository mcPaymentDetailVendorRepository;
    private final MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository;
    private final MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;

    public PPInvoiceServiceImpl(PPInvoiceRepository pPInvoiceRepository, PPInvoiceDetailsRepository ppInvoiceDetailsRepository, RSInwardOutwardRepository rsInwardOutwardRepository, UtilsRepository utilsRepository, GenCodeService genCodeService, MCPaymentRepository mcPaymentRepository, MBTellerPaperRepository mbTellerPaperRepository, MBCreditCardRepository mbCreditCardRepository, AccountingObjectBankAccountRepository accountingObjectBankAccountRepository, AccountingObjectRepository accountingObjectRepository, UserService userService, GeneralLedgerService generalLedgerService, RefVoucherRepository refVoucherRepository, PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository, OrganizationUnitRepository organizationUnitRepository, PporderdetailRepository pporderdetailRepository, PporderRepository pporderRepository, UtilsService utilsService, RepositoryLedgerRepository repositoryLedgerRepository, MCPaymentDetailVendorRepository mcPaymentDetailVendorRepository, MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository, MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository, MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.pPInvoiceRepository = pPInvoiceRepository;
        this.ppInvoiceDetailsRepository = ppInvoiceDetailsRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.utilsRepository = utilsRepository;
        this.genCodeService = genCodeService;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mbTellerPaperRepository = mbTellerPaperRepository;
        this.mbCreditCardRepository = mbCreditCardRepository;
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.userService = userService;
        this.generalLedgerService = generalLedgerService;
        this.refVoucherRepository = refVoucherRepository;
        this.pPInvoiceDetailCostRepository = pPInvoiceDetailCostRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.pporderRepository = pporderRepository;
        this.utilsService = utilsService;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.mcPaymentDetailVendorRepository = mcPaymentDetailVendorRepository;
        this.mbTellerPaperDetailVendorRepository = mbTellerPaperDetailVendorRepository;
        this.mbCreditCardDetailVendorRepository = mbCreditCardDetailVendorRepository;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    /**
     * Save a pPInvoice.
     *
     * @param pPInvoice the entity to save
     * @return the persisted entity
     */
    @Override
    public PPInvoice save(PPInvoice pPInvoice) {
        log.debug("Request to save PPInvoice : {}", pPInvoice);
        if (pPInvoice.getId() == null) {
            pPInvoice.setId(UUID.randomUUID());
            //gan id cha cho details con
            for (PPInvoiceDetails details : pPInvoice.getPpInvoiceDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setPpInvoiceId(pPInvoice.getId());
            }
        }
        PPInvoice pPI = new PPInvoice();
        try {
            pPI = pPInvoiceRepository.save(pPInvoice);
        } catch (Exception ex) {
            log.debug("Request to save MBTellerPaper ERR : {}", ex.getMessage());
        }
        return pPI;
    }

    /**
     * Get all the pPInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPInvoice> findAll(Pageable pageable) {
        log.debug("Request to get all PPInvoices");
        return pPInvoiceRepository.findAll(pageable);
    }


    /**
     * Get one pPInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPInvoice> findOne(UUID id) {
        log.debug("Request to get PPInvoice : {}", id);
        return pPInvoiceRepository.findById(id);
    }

    /**
     * Delete the pPInvoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPInvoice : {}", id);
        pPInvoiceRepository.deleteById(id);
    }

    @Override
    public Page<ReceiveBillSearchDTO> findAllPPInvoiceReceiveBill(Pageable pageable, SearchVoucher searchVoucher, String formality) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean isNoMBook = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
        }
        return pPInvoiceRepository.findAllReceiveBillSearchDTO(pageable, searchVoucher, formality, isNoMBook, currentUserLoginAndOrg.get().getOrg());
    }


    @Override
    public Page<ReceiveBillSearchDTO> findAllReceiveBill(Pageable pageable, SearchVoucher searchVoucher) {
        Boolean isNoMBook = false;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
        }
        return pPInvoiceRepository.findAllReceiveBillDTO(pageable, searchVoucher, isNoMBook, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public void saveReceiveBill(List<UUID> listIDPPInvoice) {
        pPInvoiceRepository.updateNhanHoaDon(listIDPPInvoice);
    }

    @Override
    public Page<PPInvoice> findAll(Pageable pageable, SearchVoucher searchVoucher, String formality) {
        return pPInvoiceRepository.findAll(pageable, searchVoucher, formality);
    }

    @Override
    public UpdateDataDTO savePPInvoice(PPInvoiceDTO ppInvoiceDTO, Boolean isRSI) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        PPInvoice ppInvoice = new PPInvoice();
        RSInwardOutward rsInwardOutward = new RSInwardOutward();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean isUpdateGenCode = false;
        if (ppInvoice.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        if (ppInvoiceDTO.getId() != null) {
            Optional<PPInvoice> ppInvoiceOptional = pPInvoiceRepository.findById(ppInvoiceDTO.getId());
            if (!ppInvoiceOptional.isPresent()) {
                updateDataDTO.setMessages(Constants.PPInvoiceResult.PPINVOICE_NOT_FOUND);
                return updateDataDTO;
            }
            if (isRSI) {
                Optional<RSInwardOutward> rsInwardOutwardOptional = rsInwardOutwardRepository.findById(ppInvoiceDTO.getRsInwardOutwardId());
                if (!rsInwardOutwardOptional.isPresent()) {
                    updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.RSI_ERROR);
                    return updateDataDTO;
                }
                rsInwardOutward = rsInwardOutwardOptional.get();
            }
            isUpdateGenCode = true;
            ppInvoice = ppInvoiceOptional.get();
        }

        if (!currentUserLoginAndOrg.isPresent()) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
            return updateDataDTO;
        }

        if (Strings.isNullOrEmpty(ppInvoiceDTO.getNoFBook())) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.NO_BOOK_NULL);
            return updateDataDTO;
        }

        // thêm mới hoặc cập nhật sổ cho ppinvoice
        UpdateDataDTO genCodeData = utilsService.updateReceipt(ppInvoice.getNoFBook(), ppInvoice.getNoMBook(),
            ppInvoiceDTO.getNoFBook(),
            ppInvoice.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
            ppInvoice.getId(), Constants.TypeGroupId.PP_INVOICE);
        if (!Strings.isNullOrEmpty(genCodeData.getMessage())) {
            updateDataDTO.addError(genCodeData.getMessage());
        }
        ppInvoice.setTypeLedger(ppInvoiceDTO.getTypeLedger());
        ppInvoice.setNoMBook(genCodeData.getNoMBook());
        ppInvoice.setNoFBook(genCodeData.getNoFBook());
        // thêm mới hoặc cập nhật sổ cho kho
        if (isRSI) {
            if (Strings.isNullOrEmpty(ppInvoiceDTO.getNoFBookRs())) {
                updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.NO_BOOK_RSI_NULL);
                return updateDataDTO;
            }
            UpdateDataDTO genCodeDataRSI = utilsService.updateReceipt(rsInwardOutward.getNoFBook(), rsInwardOutward.getNoMBook(),
                ppInvoiceDTO.getNoFBookRs(),
                rsInwardOutward.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                rsInwardOutward.getId(), Constants.TypeGroupId.RS_IN_WARD_OUT_WARD);
            if (!Strings.isNullOrEmpty(genCodeDataRSI.getMessage())) {
                updateDataDTO.addError(genCodeDataRSI.getMessage());
            }
            rsInwardOutward.setNoMBook(genCodeDataRSI.getNoMBook());
            rsInwardOutward.setNoFBook(genCodeDataRSI.getNoFBook());
        }
        if (ppInvoiceDTO.getTypeId() != Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN) {
            if (Strings.isNullOrEmpty(ppInvoiceDTO.getOtherNoFBook())) {
                updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.NO_BOOK_OTHER_NULL);
                return updateDataDTO;
            }
        }

        if (!updateDataDTO.getErrors().isEmpty()) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.DUPLICATE);
            return updateDataDTO;
        }

        if (isRSI) {
            rsInwardOutward.setTypeLedger(ppInvoiceDTO.getTypeLedger());
        }

        SecurityDTO securityDTO = currentUserLoginAndOrg.get();
        ppInvoice.setCompanyId(securityDTO.getOrg());
        setPPInvoice(ppInvoiceDTO, ppInvoice);
        if (isRSI) {
            setRSInwardOutWard(ppInvoiceDTO, rsInwardOutward);
            rsInwardOutward.setCompanyID(securityDTO.getOrg());

            RSInwardOutward rsInwardOutwardSave = rsInwardOutwardRepository.save(rsInwardOutward);

            ppInvoice.setRsInwardOutwardId(rsInwardOutwardSave.getId());
            ppInvoice.setStoredInRepository(true);
        } else {
            ppInvoice.setRsInwardOutwardId(null);
            ppInvoice.setStoredInRepository(false);
        }

        if (ppInvoiceDTO.getTypeId() != null) {
//            boolean isUpdateOtherGenCode = false;
//            if (ppInvoice.getPaymentVoucherId() == null) { // check có tab phụ hay không
//                isUpdateOtherGenCode = true;
//            }
            UpdateDataDTO genCodeDataOtherReceipt = new UpdateDataDTO();
//            String otherNoFBook = null;
//            String otherNoMBook = null;
            if (ppInvoice.getTypeId() != null && !ppInvoice.getTypeId().equals(ppInvoiceDTO.getTypeId())) {
                switch (ppInvoice.getTypeId()) {
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                        mcPaymentRepository.deleteById(ppInvoice.getPaymentVoucherId());
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                        mbTellerPaperRepository.deleteById(ppInvoice.getPaymentVoucherId());
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                        mbCreditCardRepository.deleteById(ppInvoice.getPaymentVoucherId());
                        break;
                }
                ppInvoice.setPaymentVoucherId(null);
                ppInvoiceDTO.setPaymentVoucherId(null);
//                isUpdateOtherGenCode = true;
            }

            switch (ppInvoiceDTO.getTypeId()) {
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:

                    if (ppInvoiceDTO.getPaymentVoucherId() != null) {
                        genCodeDataOtherReceipt = mcPaymentRepository.getNoBookById(ppInvoiceDTO.getPaymentVoucherId());
                    }
                    genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                        ppInvoiceDTO.getOtherNoFBook(),
                        genCodeDataOtherReceipt.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                        ppInvoiceDTO.getPaymentVoucherId(), Constants.TypeGroupId.MC_PAYMENT);
                    if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                        updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                        break;
                    }
                    MCPayment mcPayment = new MCPayment(ppInvoice.getPaymentVoucherId(), ppInvoice.isRecorded(),
                        Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG, ppInvoiceDTO.getReceiverUserName(),
                        ppInvoiceDTO.getOtherNumberAttach(), ppInvoiceDTO.getOtherReason(), ppInvoiceDTO.getAccountingObjectId(),
                        ppInvoiceDTO.getAccountingObjectName(), ppInvoiceDTO.getAccountingObjectAddress(),
                        ppInvoiceDTO.getCompanyTaxCode(), ppInvoiceDTO.getEmployeeId(), ppInvoiceDTO.getTypeLedger(),
                        genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(), ppInvoiceDTO.getDate(), ppInvoiceDTO.getPostedDate(),
                        securityDTO.getOrg(), ppInvoiceDTO.getCurrencyId(), ppInvoiceDTO.getTotalAmount(),
                        ppInvoiceDTO.getTotalAmountOriginal(), ppInvoiceDTO.getTotalVATAmount(),
                        ppInvoiceDTO.getTotalVATAmountOriginal(), ppInvoiceDTO.getExchangeRate());
                    ppInvoice.setPaymentVoucherId(mcPaymentRepository.save(mcPayment).getId());
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                    if (ppInvoiceDTO.getPaymentVoucherId() != null) {
                        genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppInvoiceDTO.getPaymentVoucherId());
                    }
                    genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                        ppInvoiceDTO.getOtherNoFBook(),
                        genCodeDataOtherReceipt.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                        ppInvoiceDTO.getPaymentVoucherId(), Constants.TypeGroupId.PAYMENT_ORDER);
                    if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                        updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                        break;
                    }
                    ppInvoice.setPaymentVoucherId(saveMBTellerPaper(ppInvoice, ppInvoiceDTO, genCodeDataOtherReceipt, ppInvoiceDTO.getDate(),
                        ppInvoiceDTO.getPostedDate(), securityDTO.getOrg(), ppInvoiceDTO.getCurrencyId(), ppInvoice.isRecorded(), Constants.PPInvoiceType.TYPE_ID_UY_NHIEM_CHI_MUA_HANG));
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                    if (ppInvoiceDTO.getPaymentVoucherId() != null) {
                        genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppInvoiceDTO.getPaymentVoucherId());
                    }
                    genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                        ppInvoiceDTO.getOtherNoFBook(),
                        genCodeDataOtherReceipt.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                        ppInvoiceDTO.getPaymentVoucherId(), Constants.TypeGroupId.TRANSFER_SEC);
                    if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                        updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                        break;
                    }
                    ppInvoice.setPaymentVoucherId(saveMBTellerPaper(ppInvoice, ppInvoiceDTO, genCodeDataOtherReceipt, ppInvoiceDTO.getDate(),
                        ppInvoiceDTO.getPostedDate(), securityDTO.getOrg(), ppInvoiceDTO.getCurrencyId(), ppInvoice.isRecorded(), Constants.PPInvoiceType.TYPE_ID_SEC_CHUYEN_KHOAN_MUA_HANG));
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                    if (ppInvoiceDTO.getPaymentVoucherId() != null) {
                        genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppInvoiceDTO.getPaymentVoucherId());
                    }
                    genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                        ppInvoiceDTO.getOtherNoFBook(),
                        genCodeDataOtherReceipt.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                        ppInvoiceDTO.getPaymentVoucherId(), Constants.TypeGroupId.CASH_SEC);
                    if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                        updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                        break;
                    }
                    ppInvoice.setPaymentVoucherId(saveMBTellerPaper(ppInvoice, ppInvoiceDTO, genCodeDataOtherReceipt, ppInvoiceDTO.getDate(),
                        ppInvoiceDTO.getPostedDate(), securityDTO.getOrg(), ppInvoiceDTO.getCurrencyId(), ppInvoice.isRecorded(), Constants.PPInvoiceType.TYPE_ID_SEC_TIEN_MAT_MUA_HANG));
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                    if (ppInvoiceDTO.getPaymentVoucherId() != null) {
                        genCodeDataOtherReceipt = mbCreditCardRepository.getNoBookById(ppInvoiceDTO.getPaymentVoucherId());
                    }
                    genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                        ppInvoiceDTO.getOtherNoFBook(),
                        genCodeDataOtherReceipt.getTypeLedger(), ppInvoiceDTO.getTypeLedger(),
                        ppInvoiceDTO.getPaymentVoucherId(), Constants.TypeGroupId.CREDIT_CARD);
                    if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                        updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                        break;
                    }
                    MBCreditCard mbCreditCard = new MBCreditCard(ppInvoice.getPaymentVoucherId(), ppInvoice.isRecorded(),
                        Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG, ppInvoiceDTO.getCreditCardNumber(),
                        ppInvoiceDTO.getOtherReason(), ppInvoiceDTO.getBankAccountReceiverId(), ppInvoiceDTO.getBankAccountReceiverName(),
                        ppInvoiceDTO.getAccountingObjectId(), ppInvoiceDTO.getAccountingObjectName(),
                        ppInvoiceDTO.getAccountingObjectAddress(), ppInvoice.getEmployeeId(), ppInvoiceDTO.getTypeLedger(),
                        genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(), ppInvoiceDTO.getDate(), ppInvoiceDTO.getPostedDate(), securityDTO.getOrg(), ppInvoiceDTO.getCurrencyId(),
                        ppInvoiceDTO.getTotalAmount(), ppInvoiceDTO.getTotalAmountOriginal(), ppInvoiceDTO.getTotalVATAmount(),
                        ppInvoiceDTO.getTotalVATAmountOriginal(), ppInvoiceDTO.getExchangeRate(), ppInvoiceDTO.getCreditCardId());

                    ppInvoice.setPaymentVoucherId(mbCreditCardRepository.save(mbCreditCard).getId());
                    break;
            }
        }
        if (!updateDataDTO.getErrors().isEmpty()) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.DUPLICATE);
            return updateDataDTO;
        }
        ppInvoice.setTypeId(ppInvoiceDTO.getTypeId());

        // Get List PPOrderDetail after save PPService
        List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
            ppInvoice.getPpInvoiceDetails().stream().filter(x -> x.getPpOrderDetailId() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailId(), BigDecimal.ZERO.subtract(x.getQuantity()))).collect(Collectors.toList()),
            ppInvoiceDTO.getPpInvoiceDetails().stream().filter(x -> x.getPpOrderDetailId() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailId(), x.getQuantity())).collect(Collectors.toList()),
            ppInvoiceDTO.getCheckPPOrderQuantity());

        int orderPriority = 0;
        if (ppInvoiceDTO.getPpInvoiceDetails() != null && ppInvoiceDTO.getPpInvoiceDetails().size() > 0) {
            for (PPInvoiceDetails item : ppInvoiceDTO.getPpInvoiceDetails()) {
                item.setOrderPriority(orderPriority);
                if (ppInvoice.getId() != null) {
                    item.setPpInvoiceId(ppInvoice.getId());
                }
                orderPriority++;
            }
            ppInvoice.setPpInvoiceDetails(new HashSet<>(ppInvoiceDTO.getPpInvoiceDetails()));
        }

        if (isUpdateGenCode) {
            ppInvoice = pPInvoiceRepository.save(ppInvoice);
        } else {
            ppInvoice = save(ppInvoice);
        }

        materialGoodsSpecificationsLedgerRepository.deleteByRefID(ppInvoice.getId());
        List<PPInvoiceDetails> ppInvoiceDetailsSave = new ArrayList<>(ppInvoice.getPpInvoiceDetails());
        List<PPInvoiceDetails> ppInvoiceDetails = new ArrayList<>(ppInvoiceDTO.getPpInvoiceDetails());
        List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
        for (int i = 0; i < ppInvoiceDetails.size(); i++) {
            if (ppInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                ppInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                for (MaterialGoodsSpecificationsLedger item: ppInvoiceDetails.get(i).getMaterialGoodsSpecificationsLedgers()) {
                    item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                    item.setDate(ppInvoice.getDate());
                    item.setPostedDate(ppInvoice.getPostedDate());
                    item.setTypeLedger(ppInvoice.getTypeLedger());
                    item.setRefTypeID(ppInvoice.getTypeId());
                    item.setReferenceID(ppInvoice.getId());
                    item.setDetailID(ppInvoiceDetailsSave.get(i).getId());
                    item.setNoFBook(ppInvoice.getNoFBook());
                    item.setNoMBook(ppInvoice.getNoMBook());
                    item.setiWRepositoryID(ppInvoiceDetailsSave.get(i).getRepositoryId());
                    item.setoWQuantity(BigDecimal.ZERO);
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
        UUID ppInvoiceId = ppInvoice.getId();
        if (Objects.nonNull(ppInvoiceDTO.getRefVouchers())) {
            List<RefVoucher> lastRefVoucher = refVoucherRepository.findAllByRefID1(ppInvoice.getId());
            List<RefVoucher> currentRefVoucher = ppInvoiceDTO.getRefVouchers().stream().map(dto -> {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setRefID2(dto.getRefID2());
                refVoucher.setCompanyID(securityDTO.getOrg());
                refVoucher.setRefID1(ppInvoiceId);
                return refVoucher;
            }).collect(Collectors.toList());
            List<RefVoucher> removeList = lastRefVoucher.stream().filter(refVoucher -> !currentRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
            List<RefVoucher> addList = currentRefVoucher.stream().filter(refVoucher -> !lastRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
            refVoucherRepository.deleteInBatch(removeList);
            refVoucherRepository.saveAll(addList);
        } else {
            refVoucherRepository.deleteByRefID1(ppInvoiceId);
        }

        // ghi sổ
        UserDTO userDTO = userService.getAccount();
        MessageDTO messageDTO = new MessageDTO();
        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
            if (generalLedgerService.record(ppInvoice, messageDTO)) {
                ppInvoice.setRecorded(true);
                pPInvoiceRepository.save(ppInvoice);

                if (isRSI) {
                    rsInwardOutward.setRecorded(true);
                    rsInwardOutwardRepository.save(rsInwardOutward);
                }

                switch (ppInvoice.getTypeId()) {
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                        Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mcPaymentOptional.isPresent()) {
                            mcPaymentOptional.get().setRecorded(true);
                            mcPaymentRepository.save(mcPaymentOptional.get());
                        }
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:

                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:

                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                        Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mbTellerPaperOptional.isPresent()) {
                            mbTellerPaperOptional.get().setRecorded(true);
                            mbTellerPaperRepository.save(mbTellerPaperOptional.get());
                        }
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                        Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mbCreditCardOptional.isPresent()) {
                            mbCreditCardOptional.get().setRecorded(true);
                            mbCreditCardRepository.save(mbCreditCardOptional.get());
                        }
                        break;
                }
            } else {
                updateDataDTO.setMessageRecord(messageDTO.getMsgError());
            }
        }

        // nếu là update thì xóa phân bổ chi phí đi lưu lại
        if (ppInvoiceDTO.getId() != null) {
            pPInvoiceDetailCostRepository.deleteByRefID(ppInvoiceDTO.getId());
        }

        // lưu phân bổ chi phí
        List<PPInvoiceDetailCost> ppInvoiceDetailCosts = new ArrayList<>();
        if (ppInvoiceDTO.getPpInvoiceDetailCost().size() > 0) {
            for (PPInvoiceDetailCostDTO item : ppInvoiceDTO.getPpInvoiceDetailCost()) {
                PPInvoiceDetailCost ppInvoiceDetailCost = new PPInvoiceDetailCost();
                ppInvoiceDetailCost.setCostType(item.getCostType());
                ppInvoiceDetailCost.setRefID(ppInvoice.getId());
                ppInvoiceDetailCost.setTypeID(item.getTypeID());
                ppInvoiceDetailCost.setPpServiceID(item.getPpServiceID());
                ppInvoiceDetailCost.setAccountObjectID(item.getAccountObjectID());
                ppInvoiceDetailCost.setTotalFreightAmount(item.getTotalFreightAmount());
                ppInvoiceDetailCost.setTotalFreightAmountOriginal(item.getTotalFreightAmountOriginal());
                ppInvoiceDetailCost.setAmount(item.getAmount());
                ppInvoiceDetailCost.setAmountOriginal(item.getAmountOriginal());
                ppInvoiceDetailCost.setAccumulatedAllocateAmount(item.getAccumulatedAllocateAmount());
                ppInvoiceDetailCost.setAccumulatedAllocateAmountOriginal(item.getAccumulatedAllocateAmountOriginal());
                ppInvoiceDetailCosts.add(ppInvoiceDetailCost);
            }
        }

        if (ppInvoiceDetailCosts.size() > 0) {
            for (PPInvoiceDetailCost item : ppInvoiceDetailCosts) {
                PPServiceCostVoucherDTO ppServiceCostVoucherDTO = pPInvoiceRepository.getByPPServiceId(item.getPpServiceID());
                if (ppServiceCostVoucherDTO != null) {
                    if (item.getAmount().add((ppServiceCostVoucherDTO.getSumAmount() != null ? ppServiceCostVoucherDTO.getSumAmount() : BigDecimal.ZERO)).doubleValue()
                        > (ppServiceCostVoucherDTO.getTotalAmount() != null ? ppServiceCostVoucherDTO.getTotalAmount() : BigDecimal.ZERO).doubleValue()) {
                        throw new BadRequestAlertException("Số tiền phân bổ chi phí lần này vượt quá tổng tiền có thể phân bổ!", "", "detailCodeInvalid");
                    }
                }
            }
            pPInvoiceDetailCostRepository.saveAll(ppInvoiceDetailCosts);
        }

        updateDataDTO.setUuid(ppInvoice.getId());
        updateDataDTO.setMessages(isUpdateGenCode ? Constants.UpdateDataDTOMessages.UPDATE_SUCCESS : Constants.UpdateDataDTOMessages.SAVE_SUCCESS);
        return updateDataDTO;
    }

    private PPInvoice setPPInvoice(PPInvoiceDTO ppInvoiceDTO, PPInvoice ppInvoice) {
        ppInvoice.setAccountingObjectId(ppInvoiceDTO.getAccountingObjectId());
        ppInvoice.setAccountingObjectName(ppInvoiceDTO.getAccountingObjectName());
        ppInvoice.setAccountingObjectAddress(ppInvoiceDTO.getAccountingObjectAddress());
        ppInvoice.setContactName(ppInvoiceDTO.getContactName());
        ppInvoice.setCompanyTaxCode(ppInvoiceDTO.getCompanyTaxCode());
        ppInvoice.setDueDate(ppInvoiceDTO.getDueDate());
        ppInvoice.setReason(ppInvoiceDTO.getReason());
        ppInvoice.setEmployeeId(ppInvoiceDTO.getEmployeeId());
        ppInvoice.setNumberAttach(ppInvoiceDTO.getNumberAttach());
        ppInvoice.setDate(ppInvoiceDTO.getDate());
        ppInvoice.setPostedDate(ppInvoiceDTO.getPostedDate());
        ppInvoice.setCurrencyId(ppInvoiceDTO.getCurrencyId());
        ppInvoice.setExchangeRate(ppInvoiceDTO.getExchangeRate());
        ppInvoice.setBillReceived(ppInvoiceDTO.getBillReceived());
        ppInvoice.setTotalAmount(ppInvoiceDTO.getTotalAmount());
        ppInvoice.setTotalAmountOriginal(ppInvoiceDTO.getTotalAmountOriginal());
        ppInvoice.setTotalImportTaxAmount(ppInvoiceDTO.getTotalImportTaxAmount());
        ppInvoice.setTotalImportTaxAmountOriginal(ppInvoiceDTO.getTotalImportTaxAmountOriginal());
        ppInvoice.setTotalFreightAmount(ppInvoiceDTO.getTotalFreightAmount());
        ppInvoice.setTotalFreightAmountOriginal(ppInvoiceDTO.getTotalFreightAmountOriginal());
        ppInvoice.setTotalDiscountAmount(ppInvoiceDTO.getTotalDiscountAmount());
        ppInvoice.setTotalDiscountAmountOriginal(ppInvoiceDTO.getTotalDiscountAmountOriginal());
        ppInvoice.setTotalSpecialConsumeTaxAmount(ppInvoiceDTO.getTotalSpecialConsumeTaxAmount());
        ppInvoice.setTotalSpecialConsumeTaxAmountOriginal(ppInvoiceDTO.getTotalSpecialConsumeTaxAmountOriginal());
        ppInvoice.setTotalImportTaxExpenseAmount(ppInvoiceDTO.getTotalImportTaxExpenseAmount());
        ppInvoice.setTotalImportTaxExpenseAmountOriginal(ppInvoiceDTO.getTotalImportTaxExpenseAmountOriginal());
        ppInvoice.setTotalVATAmount(ppInvoiceDTO.getTotalVATAmount());
        ppInvoice.setTotalVATAmountOriginal(ppInvoiceDTO.getTotalVATAmountOriginal());
        ppInvoice.setTotalInwardAmount(ppInvoiceDTO.getTotalInwardAmount());
        ppInvoice.setTotalInwardAmountOriginal(ppInvoiceDTO.getTotalInwardAmountOriginal());

        Boolean record = ppInvoiceDTO.getRecorded() != null ? ppInvoiceDTO.getRecorded() : false;
        ppInvoice.setRecorded(record);
        Boolean isImportPurchase = ppInvoiceDTO.getImportPurchase() != null ? ppInvoiceDTO.getImportPurchase() : false;
        ppInvoice.setImportPurchase(isImportPurchase);

        return ppInvoice;
    }

    private RSInwardOutward setRSInwardOutWard(PPInvoiceDTO ppInvoiceDTO, RSInwardOutward rsInwardOutward) {
        rsInwardOutward.setTypeID(Constants.PPInvoiceType.TYPE_ID_NHAP_KHO_MUA_HANG);
        rsInwardOutward.setImportPurchase(ppInvoiceDTO.getImportPurchase());
        rsInwardOutward.setAccountingObjectID(ppInvoiceDTO.getAccountingObjectId());
        rsInwardOutward.setAccountingObjectName(ppInvoiceDTO.getAccountingObjectName());
        rsInwardOutward.setAccountingObjectAddress(ppInvoiceDTO.getAccountingObjectAddress());
        rsInwardOutward.setContactName(ppInvoiceDTO.getContactName());
        rsInwardOutward.setReason(ppInvoiceDTO.getReasonRs());
        rsInwardOutward.setEmployeeID(ppInvoiceDTO.getEmployeeId());
        rsInwardOutward.setNumberAttach(ppInvoiceDTO.getNumberAttach());
        rsInwardOutward.setDate(ppInvoiceDTO.getDate());
        rsInwardOutward.setPostedDate(ppInvoiceDTO.getPostedDate());
        rsInwardOutward.setCurrencyID(ppInvoiceDTO.getCurrencyId());
        rsInwardOutward.setExchangeRate(ppInvoiceDTO.getExchangeRate());
        rsInwardOutward.setTypeLedger(ppInvoiceDTO.getTypeLedger());
        rsInwardOutward.setTotalAmount(ppInvoiceDTO.getTotalAmount());
        rsInwardOutward.setTotalAmountOriginal(ppInvoiceDTO.getTotalAmountOriginal());
        // todo xem lại chỗ này
        rsInwardOutward.setExported(true);
        Boolean isImportPurchase = ppInvoiceDTO.getImportPurchase() != null ? ppInvoiceDTO.getImportPurchase() : false;
        rsInwardOutward.setImportPurchase(isImportPurchase);
        return rsInwardOutward;
    }

    @Override
    public Page<PPInvoiceSearchDTO> searchPPInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Integer status, String keySearch, UUID employeeId, Boolean isRSI) {
        isRSI = isRSI.equals(Boolean.TRUE) ? isRSI : false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return pPInvoiceRepository.searchPPInvoice(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, isNoMBook, currentUserLoginAndOrg.get().getOrg(), employeeId, isRSI);
        }
        return new PageImpl<PPInvoiceSearchDTO>(new ArrayList<>());

    }

    @Override
    public PPInvoiceDTO getPPInvoiceById(UUID id) {
        PPInvoiceDTO ppInvoiceDTO = new PPInvoiceDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            ppInvoiceDTO = pPInvoiceRepository.getPPInvoiceById(id);
            List<PPInvoiceDetails> ppInvoiceDetails = ppInvoiceDetailsRepository.findAllByppInvoiceIdOrderByOrderPriorityAsc(id);
            ppInvoiceDTO.setPpInvoiceDetails(ppInvoiceDetails);
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            ppInvoiceDTO.setRefVouchers(refVoucherRepository.getRefViewVoucher(id, isNoMBook));
            ppInvoiceDTO.setPpInvoiceDetailCost(pPInvoiceDetailCostRepository.getDetailCodeByPPInvoiceId(id));
        }
        return ppInvoiceDTO;
    }

    //            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
//                    .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
//            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1")

    @Override
    public PPInvoiceDTO getPPInvoiceNotDetailById(UUID id) {
        return pPInvoiceRepository.getPPInvoiceById(id);
    }

    @Override
    public List<PPInvoiceDetailDTO> getPPInvoiceDetailByIdPPInvoice(UUID id) {
        return pPInvoiceRepository.getPPInvoiceDetailByIdPPInvoice(id);
    }

    @Override
    public List<PPInvoiceDetailDTO> getPPInvoiceDetailByPaymentVoucherID(UUID paymentVoucherID) {
        return pPInvoiceRepository.getPPInvoiceDetailByPaymentVoucherID(paymentVoucherID);
    }

    @Override
    public PPInvoiceDTO findIdByRowNum(Pageable pageable, UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String textSearch, Integer rowNum, Boolean isRSI) {
        PPInvoiceDTO ppInvoiceDTO = new PPInvoiceDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");

            PPInvoice ppInvoice = pPInvoiceRepository.findIdByRowNum(pageable, accountingObject, status, currency, employee, fromDate, toDate, textSearch, rowNum, currentUserLoginAndOrg.get().getOrg(), isNoMBook, isRSI);
            ppInvoiceDTO = pPInvoiceRepository.getPPInvoiceById(ppInvoice.getId());
            List<PPInvoiceDetails> ppInvoiceDetails = ppInvoiceDetailsRepository.findAllByppInvoiceIdOrderByOrderPriorityAsc(ppInvoice.getId());
            ppInvoiceDTO.setPpInvoiceDetails(ppInvoiceDetails);
            ppInvoiceDTO.setRefVouchers(refVoucherRepository.getRefViewVoucher(ppInvoice.getId(), isNoMBook));
        }
        return ppInvoiceDTO;
    }

    private UpdateDataDTO updateOtherNoBook(Integer typeLedger, Integer currentBook, String otherNoBook,
                                            UUID paymentVoucherId, int typeGroupID, String otherNoFBook, String otherNoMBook, Integer oldTypeLeger) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        if (typeLedger != null) {
            boolean isUpdateNoBook = true;
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                if (otherNoMBook != null && otherNoBook.equals(otherNoFBook)) {
                    isUpdateNoBook = false;
                }
                otherNoFBook = otherNoBook;
            } else {
                if (otherNoMBook != null && otherNoBook.equals(otherNoMBook)) {
                    isUpdateNoBook = false;
                }
                otherNoMBook = otherNoBook;
            }
            if (typeLedger.equals(Constants.TypeLedger.BOTH_BOOK) && (paymentVoucherId == null || !typeLedger.equals(oldTypeLeger))) {
                GenCode genCode = genCodeService.findWithTypeID(typeGroupID, currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK) ? Constants.TypeLedger.MANAGEMENT_BOOK : Constants.TypeLedger.FINANCIAL_BOOK);
                String codeVoucher = Utils.codeVoucher(genCode);
                if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                    otherNoMBook = codeVoucher;
                } else {
                    otherNoFBook = codeVoucher;
                }
                isUpdateNoBook = true;
            }
            if (isUpdateNoBook) {
                if (!utilsRepository.checkDuplicateNoVoucher(otherNoFBook,
                    otherNoMBook,
                    typeLedger,
                    paymentVoucherId)) {
                    updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.DUPLICATE_OTHER_NO_BOOK);
                    return updateDataDTO;
                }
                if (!utilsRepository.updateGencode(otherNoFBook,
                    otherNoMBook,
                    typeGroupID,
                    typeLedger)) {
                    updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.CANNOT_UPDATE_OTHER_NO_BOOK);
                    return updateDataDTO;
                }
            }
        }
        updateDataDTO.setNoFBook(otherNoFBook);
        updateDataDTO.setNoMBook(otherNoMBook);
        return updateDataDTO;
    }

    private UUID saveMBTellerPaper(PPInvoice ppInvoice, PPInvoiceDTO ppInvoiceDTO, UpdateDataDTO updateDataDTO,
                                   LocalDate receiptDate, LocalDate postedDate, UUID companyId, String currencyCode, Boolean recored,
                                   Integer typeId) {
        AccountingObjectBankAccount accountingObjectBankAccount = null;
        if (ppInvoiceDTO.getAccountReceiverId() != null) {
            accountingObjectBankAccount = accountingObjectBankAccountRepository.getOne(ppInvoiceDTO.getAccountReceiverId());
        }
        MBTellerPaper mbTellerPaper = new MBTellerPaper(ppInvoice.getPaymentVoucherId(), recored,
            typeId, ppInvoiceDTO.getAccountPaymentId(),
            ppInvoiceDTO.getAccountPaymentName(), ppInvoiceDTO.getOtherReason(), accountingObjectBankAccount,
            ppInvoiceDTO.getAccountReceiverName(), ppInvoiceDTO.getAccountingObjectId() != null ? accountingObjectRepository.getOne(ppInvoiceDTO.getAccountingObjectId()) : null, ppInvoiceDTO.getAccountingObjectName(),
            ppInvoiceDTO.getAccountingObjectAddress(), ppInvoice.getEmployeeId() != null ? accountingObjectRepository.getOne(ppInvoice.getEmployeeId()) : null, ppInvoiceDTO.getTypeLedger(),
            updateDataDTO.getNoFBook(), updateDataDTO.getNoMBook(), receiptDate, postedDate, companyId, currencyCode,
            ppInvoiceDTO.getTotalAmount(), ppInvoiceDTO.getTotalAmountOriginal(), ppInvoiceDTO.getTotalVATAmount(),
            ppInvoiceDTO.getTotalVATAmountOriginal(), ppInvoiceDTO.getExchangeRate(), ppInvoiceDTO.getAccountReceiverFullName(),
            ppInvoiceDTO.getIdentificationNo(), ppInvoiceDTO.getIssueDate(), ppInvoiceDTO.getIssueBy());

        return mbTellerPaperRepository.save(mbTellerPaper).getId();
    }

    @Override
    public ResultDTO deleteById(UUID id) {
        Optional<PPInvoice> ppInvoiceOptional = pPInvoiceRepository.findById(id);
        if (ppInvoiceOptional.isPresent() && !ppInvoiceOptional.get().isRecorded()) {

//            // check xem có được sử dụng ở bảng khác không nếu có thì không cho xóa
//            int count = ppInvoiceDetailsRepository.checkReferences(id);
//            if (count > 0) {
//                return new ResultDTO(Constants.PPInvoiceResult.PPINVOICE_USED);
//            }
            // check xem là mua hàng qua kho hay không qua kho
            if (ppInvoiceOptional.get().isStoredInRepository() && rsInwardOutwardRepository.existsById(ppInvoiceOptional.get().getRsInwardOutwardId())) {
                rsInwardOutwardRepository.deleteById(ppInvoiceOptional.get().getRsInwardOutwardId());
            }

            // Get List PPOrderDetail after save PPService
            List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                ppInvoiceOptional.get().getPpInvoiceDetails().stream().filter(x -> x.getPpOrderDetailId() != null)
                    .map(x -> new PPOrderDTO(x.getPpOrderDetailId(), BigDecimal.ZERO.subtract(x.getQuantity())))
                    .collect(Collectors.toList()),
                new ArrayList<>(), false);

            // update PPOrderDetails
            pporderdetailRepository.saveAll(ppOrderDetails);
            pporderRepository.updateStatus(ppOrderDetails.stream()
                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
            refVoucherRepository.deleteByRefID1(id);
            // xóa phân bổ chi phí
            pPInvoiceDetailCostRepository.deleteByRefID(id);
            switch (ppInvoiceOptional.get().getTypeId()) {
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                    if (mcPaymentRepository.existsById(ppInvoiceOptional.get().getPaymentVoucherId())) {
                        mcPaymentRepository.deleteById(ppInvoiceOptional.get().getPaymentVoucherId());
                    }
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                    if (mbTellerPaperRepository.existsById(ppInvoiceOptional.get().getPaymentVoucherId())) {
                        mbTellerPaperRepository.deleteById(ppInvoiceOptional.get().getPaymentVoucherId());
                    }
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                    if (mbCreditCardRepository.existsById(ppInvoiceOptional.get().getPaymentVoucherId())) {
                        mbCreditCardRepository.deleteById(ppInvoiceOptional.get().getPaymentVoucherId());
                    }
                    break;
            }
            pPInvoiceRepository.deleteById(id);
            return new ResultDTO(Constants.PPInvoiceResult.DELETE_SUCCESS);

        }
        return new ResultDTO(Constants.PPInvoiceResult.PPINVOICE_NOT_FOUND);
    }

    @Override
    public PPInvoice getPPInvoiceByPaymentVoucherId(UUID id) {
        return pPInvoiceRepository.getPPInvoiceByPaymentVoucherId(id);
    }

    @Override
    public byte[] exportPdf(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue, Boolean isRSI) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            Page<PPInvoiceSearchDTO> ppInvoiceSearchDTOS = currentUserLoginAndOrg.map(securityDTO -> pPInvoiceRepository.searchPPInvoice(null, accountingObject, currency, fromDate, toDate, status, searchValue, isNoMBook, currentUserLoginAndOrg.get().getOrg(), employee, isRSI)).orElse(null);
            return PdfUtils.writeToFile(ppInvoiceSearchDTOS.getContent(), ExcelConstant.PPInvoice.HEADER, ExcelConstant.PPInvoice.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue, Boolean isRSI) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {

            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            Page<PPInvoiceSearchDTO> ppInvoiceSearchDTOS = currentUserLoginAndOrg.map(securityDTO -> pPInvoiceRepository.searchPPInvoice(null, accountingObject, currency, fromDate, toDate, status, searchValue, isNoMBook, currentUserLoginAndOrg.get().getOrg(), employee, isRSI)).orElse(null);
            return ExcelUtils.writeToFile(ppInvoiceSearchDTOS.getContent(), ExcelConstant.PPInvoice.NAME, ExcelConstant.PPInvoice.HEADER, ExcelConstant.PPInvoice.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public Page<ViewPPInvoiceDTO> getViewPPInvoiceDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return pPInvoiceRepository.findAllView(pageable, currentUserLoginAndOrg.get().getOrg(), accountingObjectID, fromDate, toDate, currentBook, currencyID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public ResultDTO checkUnRecord(UUID id) {
        int count = ppInvoiceDetailsRepository.checkReferences(id);
        if (count > 0) {
            return new ResultDTO(Constants.PPInvoiceResult.PPINVOICE_USED);
        }
        return new ResultDTO(Constants.PPInvoiceResult.NOTHING);
    }

    @Override
    public ResultDTO checkViaStock(UUID id) {
        Boolean check = pPInvoiceRepository.getStoredInRepositoryById(id);
        if (check != null) {
            if (check) {
                return new ResultDTO(Constants.PPInvoiceResult.STOCK_TRUE);
            }
            return new ResultDTO(Constants.PPInvoiceResult.STOCK_FALSE);
        }
        return new ResultDTO(Constants.PPInvoiceResult.ERROR);
    }

    @Override
    public ResultDTO checkReferencesCount(UUID id) {
        List<String> referenceTables = Constants.PPInvoiceDetailReferenceTable.referenceTablesPayVendor;

        int count = pPInvoiceRepository.checkReferences(id, referenceTables, Constants.PPInvoiceDetailReferenceTable.PP_INVOICE_ID);
        if (count > 0) {
            return new ResultDTO(Constants.UpdateDataDTOMessages.DUPLICATE);
        }
        return new ResultDTO(Constants.UpdateDataDTOMessages.SUCCESS);
    }

    @Override
    public HandlingResultDTO multiDelete(List<PPInvoice> ppInvoices, boolean isKho) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(ppInvoices.size());
        List<PPInvoice> listDelete;
        listDelete = new ArrayList<>(ppInvoices);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        int countSuccess = 0;
        for (PPInvoice ppi : ppInvoices) {
            if (Boolean.TRUE.equals(ppi.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                viewVoucherNo.setPostedDate(ppi.getPostedDate());
                viewVoucherNo.setDate(ppi.getDate());
                viewVoucherNo.setNoFBook(ppi.getNoFBook());
                viewVoucherNo.setNoMBook(ppi.getNoMBook());
                if (isKho) {
                    viewVoucherNo.setTypeName("Mua hàng qua kho");
                } else {
                    viewVoucherNo.setTypeName("Mua hàng không qua kho");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(ppi);
            } else {
                if (!Constants.PPInvoiceResult.DELETE_SUCCESS.equals(deleteById(ppi.getId()).getMessage())) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ không tồn tại !");
                    viewVoucherNo.setPostedDate(ppi.getPostedDate());
                    viewVoucherNo.setDate(ppi.getDate());
                    viewVoucherNo.setNoFBook(ppi.getNoFBook());
                    viewVoucherNo.setNoMBook(ppi.getNoMBook());
                    if (isKho) {
                        viewVoucherNo.setTypeName("Mua hàng qua kho");
                    } else {
                        viewVoucherNo.setTypeName("Mua hàng không qua kho");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(ppi);
                } else {
                    countSuccess += 1;
                }
            }
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(countSuccess);
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<PPInvoice> ppInvoices, boolean isKho) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(ppInvoices.size());
        List<PPInvoice> listDelete = new ArrayList<>(ppInvoices);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (closeDateStr != null && !closeDateStr.equals("")) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (PPInvoice ppi: ppInvoices) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(ppi.getPostedDate().toString());
            if (Boolean.TRUE.equals(ppi.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(ppi.getPostedDate());
                viewVoucherNo.setDate(ppi.getDate());
                viewVoucherNo.setNoFBook(ppi.getNoFBook());
                viewVoucherNo.setNoMBook(ppi.getNoMBook());
                if (isKho) {
                    viewVoucherNo.setTypeName("Mua hàng qua kho");
                } else {
                    viewVoucherNo.setTypeName("Mua hàng không qua kho");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(ppi);
            } else {
                if (!Boolean.TRUE.equals(ppi.isRecorded())) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                    viewVoucherNo.setPostedDate(ppi.getPostedDate());
                    viewVoucherNo.setDate(ppi.getDate());
                    viewVoucherNo.setNoFBook(ppi.getNoFBook());
                    viewVoucherNo.setNoMBook(ppi.getNoMBook());
                    if (isKho) {
                        viewVoucherNo.setTypeName("Mua hàng qua kho");
                    } else {
                        viewVoucherNo.setTypeName("Mua hàng không qua kho");
                    }
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(ppi);
                } else {
                    if (pPInvoiceRepository.checkReferences(ppi.getId(), Constants.PPInvoiceDetailReferenceTable.referenceTablesPayVendor, Constants.PPInvoiceDetailReferenceTable.PP_INVOICE_ID) > 0) {
                        ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                        viewVoucherNo.setReasonFail("Chứng từ này đã phát sinh nghiệp vụ Trả tiền nhà cung cấp!");
                        viewVoucherNo.setPostedDate(ppi.getPostedDate());
                        viewVoucherNo.setDate(ppi.getDate());
                        viewVoucherNo.setNoFBook(ppi.getNoFBook());
                        viewVoucherNo.setNoMBook(ppi.getNoMBook());
                        if (isKho) {
                            viewVoucherNo.setTypeName("Mua hàng qua kho");
                        } else {
                            viewVoucherNo.setTypeName("Mua hàng không qua kho");
                        }
                        viewVoucherNoListFail.add(viewVoucherNo);
                        listDelete.remove(ppi);
                    } else {
                        if (ppInvoiceDetailsRepository.checkReferences(ppi.getId()) > 0) {
                            ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                            viewVoucherNo.setReasonFail("Chứng từ này đã phát sinh nghiệp vụ liên quan!");
                            viewVoucherNo.setPostedDate(ppi.getPostedDate());
                            viewVoucherNo.setDate(ppi.getDate());
                            viewVoucherNo.setNoFBook(ppi.getNoFBook());
                            viewVoucherNo.setNoMBook(ppi.getNoMBook());
                            if (isKho) {
                                viewVoucherNo.setTypeName("Mua hàng qua kho");
                            } else {
                                viewVoucherNo.setTypeName("Mua hàng không qua kho");
                            }
                            viewVoucherNoListFail.add(viewVoucherNo);
                            listDelete.remove(ppi);
                        }
                    }
                }
            }
        }

        List<UUID> uuidListRS = new ArrayList<>();
        List<PPInvoice> ppiList = new ArrayList<>();
        List<PPInvoice> ppiListMCP = new ArrayList<>();
        List<PPInvoice> ppiListMBT = new ArrayList<>();
        List<PPInvoice> ppiListMBC = new ArrayList<>();
        for (PPInvoice ppi : listDelete) {
            if (isKho) {
                uuidListRS.add(ppi.getRsInwardOutwardId());
            }
            switch (ppi.getTypeId()) {
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
                    if (pPInvoiceRepository.checkHasPaid(ppi.getId())) {
                        mcPaymentDetailVendorRepository.findByPPInvoiceID(ppi.getId()).ifPresent(mcPaymentDetailVendor -> {
                            generalLedgerService.unrecord(mcPaymentDetailVendor.getmCPaymentID(), null);
                            mcPaymentDetailVendorRepository.delete(mcPaymentDetailVendor);
                            mcPaymentRepository.deleteById(mcPaymentDetailVendor.getmCPaymentID());
                        });
                        mbTellerPaperDetailVendorRepository.findByPPInvoiceID(ppi.getId()).ifPresent(mbTellerPaperDetailVendor -> {
                            generalLedgerService.unrecord(mbTellerPaperDetailVendor.getmBTellerPaperID(), null);
                            mbTellerPaperDetailVendorRepository.delete(mbTellerPaperDetailVendor);
                            mbTellerPaperRepository.deleteById(mbTellerPaperDetailVendor.getmBTellerPaperID());
                        });
                        mbCreditCardDetailVendorRepository.findByPPInvoiceID(ppi.getId()).ifPresent(mbCreditCardDetailVendor -> {
                            generalLedgerService.unrecord(mbCreditCardDetailVendor.getmBCreditCardID(), null);
                            mbCreditCardDetailVendorRepository.delete(mbCreditCardDetailVendor);
                            mbCreditCardRepository.deleteById(mbCreditCardDetailVendor.getmBCreditCardID());
                        });
                    }
                    ppiList.add(ppi);
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                    ppiListMCP.add(ppi);
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                    ppiListMBT.add(ppi);
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                    ppiListMBC.add(ppi);
                    break;
            }
        }

        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        if (listDelete.size() > 0) {
            if (uuidListRS.size() > 0) {
                rsInwardOutwardRepository.updateUnrecord(uuidListRS);
                repositoryLedgerRepository.deleteAllByReferenceID(uuidListRS);
            }
            if (ppiList.size() > 0) {
                pPInvoiceRepository.updateUnRecord(ppiList);
            }
            if (ppiListMCP.size() > 0) {
                pPInvoiceRepository.updateUnRecordMCP(ppiListMCP);
            }
            if (ppiListMBT.size() > 0) {
                pPInvoiceRepository.updateUnRecordMBT(ppiListMBT);
            }
            if (ppiListMBC.size() > 0) {
                pPInvoiceRepository.updateUnRecordMBC(ppiListMBC);
            }

        }
        return handlingResultDTO;
    }
}
