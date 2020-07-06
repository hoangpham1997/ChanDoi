package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.service.util.Constants.PPServiceType;
import vn.softdreams.ebweb.service.util.Constants.SystemOption;
import vn.softdreams.ebweb.service.util.Constants.TypeGroupId;
import vn.softdreams.ebweb.service.util.Constants.UpdateDataDTOMessages;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PPServiceServiceImpl implements PPServiceService {

    private final Logger log = LoggerFactory.getLogger(PPServiceServiceImpl.class);

    private final PPServiceRepository ppServiceRepository;

    private final MCPaymentRepository mcPaymentRepository;

    private final MBTellerPaperRepository mbTellerPaperRepository;

    private final BankAccountDetailsRepository bankAccountDetailsRepository;

    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;

    private final AccountingObjectRepository accountingObjectRepository;

    private final MBCreditCardRepository mbCreditCardRepository;

    private final PPServiceDetailRepository ppServiceDetailRepository;

    private final GeneralLedgerRepository generalLedgerRepository;

    private final ViewVoucherNoRespository viewVoucherNoRespository;

    private final CurrencyRepository currencyRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final UtilsRepository utilsRepository;

    private final GenCodeService genCodeService;

    private final UserService userService;

    private final GeneralLedgerService generalLedgerService;

    private final PporderdetailRepository pporderdetailRepository;
    private final PporderRepository pporderRepository;

    private final UtilsService utilsService;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository;

    private final ExpenseItemRepository expenseItemRepository;

    public PPServiceServiceImpl(PPServiceRepository ppServiceRepository,
                                MCPaymentRepository mcPaymentRepository,
                                MBTellerPaperRepository mbTellerPaperRepository,
                                BankAccountDetailsRepository bankAccountDetailsRepository,
                                AccountingObjectBankAccountRepository accountingObjectBankAccountRepository,
                                AccountingObjectRepository accountingObjectRepository, MBCreditCardRepository mbCreditCardRepository,
                                PPServiceDetailRepository ppServiceDetailRepository,
                                GeneralLedgerRepository generalLedgerRepository, ViewVoucherNoRespository viewVoucherNoRespository, CurrencyRepository currencyRepository, RefVoucherRepository refVoucherRepository,
                                UtilsRepository utilsRepository, GenCodeService genCodeService, UserService userService,
                                GeneralLedgerService generalLedgerService, PporderRepository ppOrderRepository,
                                PporderdetailRepository pporderdetailRepository, PporderRepository pporderRepository, OrganizationUnitRepository organizationUnitRepository, UtilsService utilsService, PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository,
                                ExpenseItemRepository expenseItemRepository
                                ) {
        this.ppServiceRepository = ppServiceRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mbTellerPaperRepository = mbTellerPaperRepository;
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.mbCreditCardRepository = mbCreditCardRepository;
        this.ppServiceDetailRepository = ppServiceDetailRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.viewVoucherNoRespository = viewVoucherNoRespository;
        this.currencyRepository = currencyRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.utilsRepository = utilsRepository;
        this.genCodeService = genCodeService;
        this.userService = userService;
        this.generalLedgerService = generalLedgerService;
        this.pporderdetailRepository = pporderdetailRepository;
        this.pporderRepository = pporderRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
        this.ppInvoiceDetailCostRepository = ppInvoiceDetailCostRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    @Override
    public Page<PPServiceDTO> findAllPPService(Pageable pageable, Integer receiptType, String toDate, String fromDate,
                                               Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                               String freeSearch, UUID currentPPService) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!currentUserLoginAndOrg.isPresent()) {
            throw new BadRequestAlertException("Tài khoản không tồn tại", "PPService", Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        if (currentPPService != null) {
            UserDTO userDTO = userService.getAccount();
            Integer currentBook = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(SystemOption.PHIEN_SoLamViec)).findAny().get().getData());
            UpdateDataDTO updateDataDTO = ppServiceRepository.findPPServiceDTOByLocationItem(0,currentBook, currentPPService, currentUserLoginAndOrg.get().getOrg(),
                    receiptType, toDate, fromDate, hasRecord, currencyID,
                    accountingObjectID, noBookType, freeSearch);
            pageable = PageRequest.of(updateDataDTO.getRowNum()/ pageable.getPageSize(), pageable.getPageSize(), pageable.getSort());
        }
        Page<PPServiceDTO> ppServiceDTOS =  ppServiceRepository.findAllPPService(pageable, receiptType, toDate, fromDate, hasRecord, currencyID,
                accountingObjectID, noBookType, freeSearch, currentUserLoginAndOrg.get().getOrg());

        ppServiceDTOS.forEach(ppServiceDTO -> {
                    List<String> strings = ppServiceDetailRepository.findAllInvoiceNoByPPServiceId(ppServiceDTO.getId());
                    if (strings != null && strings.get(0) != null) {
                        String invoiceNo = "";
                        int size = 0;
                        for (int i = 0; i < strings.size(); i++) {
                            if (!Strings.isNullOrEmpty(strings.get(i))) {
                                invoiceNo += size == 0 ? strings.get(i) : ", " + strings.get(i);
                                size ++;
                            }
                        }
                        ppServiceDTO.setInvoiceNo(invoiceNo);
                    }
                }
            );
        return ppServiceDTOS;
    }

    @Override
    public BigDecimal countTotalResultAmountOriginal(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return ppServiceRepository.countTotalResultAmountOriginal(receiptType, toDate, fromDate, hasRecord, currencyID,
                accountingObjectID, noBookType, freeSearch, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public UpdateDataDTO savePPService(PPServiceDTO ppServiceDTO) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        MessageDTO messageDTO = new MessageDTO("");
        PPService ppService = new PPService();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        UserDTO userDTO = userService.getAccount();
        // check expensiveItem parent
        // lay ra list expensiveItem cha có con
        List<ExpenseItem> expenseItemList = expenseItemRepository.listParentHasChild(currentUserLoginAndOrg.get().getOrg());
        List<UUID> uuids = expenseItemList.stream().map(ExpenseItem::getId).collect(Collectors.toList());
        boolean isReturn = ppServiceDTO.getPpServiceDetailDTOS().stream().map(PPServiceDetailDTO::getExpenseItemId).anyMatch(uuids::contains);
        if (isReturn) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.EXPENSIVE_ITEM_HAS_CHILD);
            return updateDataDTO;
        }
        boolean isUpdateGenCode = false;
        if (ppServiceDTO.getId() != null) {
            Optional<PPService> ppServiceOptional = ppServiceRepository.findById(ppServiceDTO.getId());
            isUpdateGenCode = ppServiceOptional.isPresent();
            if (isUpdateGenCode) {
                ppService = ppServiceOptional.get();
            }
        }
        // BOTH_BOOK to One book
        if (!currentUserLoginAndOrg.isPresent()) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
            return updateDataDTO;
        }
        UpdateDataDTO genCodeData = utilsService.updateReceipt(ppService.getNoFBook(), ppService.getNoMBook(),
                ppServiceDTO.getNoBook(),
                ppService.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                ppService.getId(), TypeGroupId.PP_SERVICE_LICENSE);
        if (!Strings.isNullOrEmpty(genCodeData.getMessage())) {
            updateDataDTO.addError(genCodeData.getMessage());
        }
        ppService.setNoMBook(genCodeData.getNoMBook());
        ppService.setNoFBook(genCodeData.getNoFBook());

        ppService.setTypeLedger(ppServiceDTO.getTypeLedger());

        SecurityDTO securityDTO = currentUserLoginAndOrg.get();
        ppService.setCompanyID(securityDTO.getOrg());

        LocalDate receiptDate = DateUtil.getLocalDateFromString(ppServiceDTO.getReceiptDate(), DateUtil.C_DD_MM_YYYY);
        if (receiptDate == null) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.DATE_NULL);
            return updateDataDTO;
        }
        LocalDate postedDate = DateUtil.getLocalDateFromString(ppServiceDTO.getPostedDate(), DateUtil.C_DD_MM_YYYY);
        if (postedDate == null) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.DATE_NULL);
            return updateDataDTO;
        }
        ppService.setDate(receiptDate);

        ppService.setPostedDate(postedDate);
        ppService.setAccountingObjectID(ppServiceDTO.getAccountingObjectID());
        ppService.setAccountingObjectName(ppServiceDTO.getAccountingObjectName());
        ppService.setAccountingObjectAddress(ppServiceDTO.getAccountingObjectAddress());
        ppService.setCompanyTaxCode(ppServiceDTO.getCompanyTaxCode());
        ppService.setContactName(ppServiceDTO.getContactName());
        ppService.setCurrencyID(ppServiceDTO.getCurrencyID());
        ppService.setEmployeeID(ppServiceDTO.getEmployeeID());
        ppService.setReason(ppServiceDTO.getReason());
        ppService.setDueDate(DateUtil.getLocalDateFromString(ppServiceDTO.getDueDate(), DateUtil.C_DD_MM_YYYY));
        boolean billReceived = ppServiceDTO.getBillReceived() != null && ppServiceDTO.getBillReceived();
        ppService.setBillReceived(billReceived);
        ppService.setTotalAmount(ppServiceDTO.getTotalAmount());
        ppService.setTotalAmountOriginal(ppServiceDTO.getTotalAmountOriginal());
        ppService.setTotalDiscountAmount(ppServiceDTO.getTotalDiscountAmount());
        ppService.setTotalDiscountAmountOriginal(ppServiceDTO.getTotalDiscountAmountOriginal());
        ppService.setTotalVATAmount(ppServiceDTO.getTotalVATAmount());
        ppService.setTotalVATAmountOriginal(ppServiceDTO.getTotalVATAmountOriginal());
        ppService.setExchangeRate(ppServiceDTO.getExchangeRate());

        ppService.setNumberAttach(ppServiceDTO.getNumberAttach());
        ppService.setIsFeightService(ppServiceDTO.getPurchaseCosts() != null ? ppServiceDTO.getPurchaseCosts() : false);

        Boolean recored = ppService.getRecorded() != null ? ppService.getRecorded() : false;
        ppService.setRecorded(recored);
        // save paymentVoucher
        Integer typeId = Integer.valueOf(ppServiceDTO.getReceiptType());
        if (ppServiceDTO.getReceiptType() != null ) {
            if (ppService.getTypeID() != null && !ppService.getTypeID().equals(typeId)) {
                switch (ppService.getTypeID()) {
                    case PPServiceType.PPSERVICE_CASH:
                        mcPaymentRepository.deleteById(ppService.getPaymentVoucherID());
                        break;
                    case PPServiceType.PPSERVICE_PAYMENT_ORDER:
                    case PPServiceType.PPSERVICE_TRANSFER_SEC:
                    case PPServiceType.PPSERVICE_CASH_SEC:
                        mbTellerPaperRepository.deleteById(ppService.getPaymentVoucherID());
                        break;
                    case PPServiceType.PPSERVICE_CREDIT_CARD:
                        mbCreditCardRepository.deleteById(ppService.getPaymentVoucherID());
                        break;
                }
                ppServiceDTO.setPaymentVoucherID(null);
            }
            if (ppServiceDTO.getOtherNoBook() != null) {
                UpdateDataDTO genCodeDataOtherReceipt = new UpdateDataDTO();
                switch (typeId) {
                    case PPServiceType.PPSERVICE_CASH:
                        if (ppServiceDTO.getPaymentVoucherID() != null) {
                            genCodeDataOtherReceipt = mcPaymentRepository.getNoBookById(ppServiceDTO.getPaymentVoucherID());
                        }
                        genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                                ppServiceDTO.getOtherNoBook(),
                                genCodeDataOtherReceipt.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                                ppServiceDTO.getPaymentVoucherID(), Constants.TypeGroupId.MC_PAYMENT);
                        if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                            updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                            break;
                        }
                        MCPayment mcPayment = new MCPayment(ppService.getPaymentVoucherID(), recored,
                            PPServiceType.MC_PAYMENT, ppServiceDTO.getContactName(),
                            ppServiceDTO.getOtherNumberAttach(), ppServiceDTO.getOtherReason(), ppServiceDTO.getAccountingObjectID(),
                            ppServiceDTO.getAccountingObjectName(), ppServiceDTO.getAccountingObjectAddress(),
                            ppServiceDTO.getCompanyTaxCode(), ppServiceDTO.getEmployeeID(), ppServiceDTO.getTypeLedger(),
                                genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(), receiptDate, postedDate, securityDTO.getOrg(), ppServiceDTO.getCurrencyID(),
                            ppServiceDTO.getTotalAmount(), ppServiceDTO.getTotalAmountOriginal(), ppServiceDTO.getTotalVATAmount(),
                            ppServiceDTO.getTotalVATAmountOriginal(), ppServiceDTO.getExchangeRate());
                        ppService.setPaymentVoucherID(mcPaymentRepository.save(mcPayment).getId());
                        break;
                    case PPServiceType.PPSERVICE_PAYMENT_ORDER:
                        if (ppServiceDTO.getPaymentVoucherID() != null) {
                            genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppService.getPaymentVoucherID());
                        }
                        genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                                ppServiceDTO.getOtherNoBook(),
                                genCodeDataOtherReceipt.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                                ppServiceDTO.getPaymentVoucherID(), Constants.TypeGroupId.PAYMENT_ORDER);
                        if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                            updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                            break;
                        }
                        ppService.setPaymentVoucherID(saveMBTellerPaper(ppService, ppServiceDTO, genCodeDataOtherReceipt, receiptDate,
                            postedDate, securityDTO.getOrg(), ppServiceDTO.getCurrencyID(), recored, PPServiceType.PAYMENT_ORDER));
                        break;
                    case PPServiceType.PPSERVICE_TRANSFER_SEC:
                        if (ppServiceDTO.getPaymentVoucherID() != null) {
                            genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppService.getPaymentVoucherID());
                        }
                        genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                                ppServiceDTO.getOtherNoBook(),
                                genCodeDataOtherReceipt.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                                ppServiceDTO.getPaymentVoucherID(), Constants.TypeGroupId.TRANSFER_SEC);
                        if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                            updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                            break;
                        }
                        ppService.setPaymentVoucherID(saveMBTellerPaper(ppService, ppServiceDTO, genCodeDataOtherReceipt, receiptDate,
                            postedDate, securityDTO.getOrg(), ppServiceDTO.getCurrencyID(), recored, PPServiceType.TRANSFER_SEC));
                        break;
                    case PPServiceType.PPSERVICE_CASH_SEC:
                        if (ppServiceDTO.getPaymentVoucherID() != null) {
                            genCodeDataOtherReceipt = mbTellerPaperRepository.getNoBookById(ppService.getPaymentVoucherID());
                        }
                        genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                                ppServiceDTO.getOtherNoBook(),
                                genCodeDataOtherReceipt.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                                ppServiceDTO.getPaymentVoucherID(), Constants.TypeGroupId.CASH_SEC);
                        ppService.setPaymentVoucherID(saveMBTellerPaper(ppService, ppServiceDTO, genCodeDataOtherReceipt, receiptDate,
                            postedDate, securityDTO.getOrg(), ppServiceDTO.getCurrencyID(), recored, PPServiceType.CASH_SEC));
                        break;
                    case PPServiceType.PPSERVICE_CREDIT_CARD:
                        if (ppServiceDTO.getPaymentVoucherID() != null) {
                            genCodeDataOtherReceipt = mbCreditCardRepository.getNoBookById(ppService.getPaymentVoucherID());
                        }
                        genCodeDataOtherReceipt = utilsService.updateReceipt(genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(),
                                ppServiceDTO.getOtherNoBook(),
                                genCodeDataOtherReceipt.getTypeLedger(), ppServiceDTO.getTypeLedger(),
                                ppServiceDTO.getPaymentVoucherID(), Constants.TypeGroupId.CREDIT_CARD);
                        if (!Strings.isNullOrEmpty(genCodeDataOtherReceipt.getMessage())) {
                            updateDataDTO.addError(genCodeDataOtherReceipt.getMessage());
                            break;
                        }
                        MBCreditCard mbCreditCard = new MBCreditCard(ppService.getPaymentVoucherID(), recored, PPServiceType.CREDIT_CARD, ppServiceDTO.getCreditCardNumber(),
                            ppServiceDTO.getOtherReason(), ppServiceDTO.getAccountReceiverId(), ppServiceDTO.getAccountReceiverName(),
                            ppServiceDTO.getAccountingObjectID(), ppServiceDTO.getAccountingObjectName(),
                            ppServiceDTO.getAccountingObjectAddress(), ppService.getEmployeeID(), ppServiceDTO.getTypeLedger(),
                                genCodeDataOtherReceipt.getNoFBook(), genCodeDataOtherReceipt.getNoMBook(), receiptDate, postedDate, securityDTO.getOrg(), ppServiceDTO.getCurrencyID(),
                            ppServiceDTO.getTotalAmount(), ppServiceDTO.getTotalAmountOriginal(), ppServiceDTO.getTotalVATAmount(),
                            ppServiceDTO.getTotalVATAmountOriginal(), ppServiceDTO.getExchangeRate(), ppServiceDTO.getCreditCardId());

                        ppService.setPaymentVoucherID(mbCreditCardRepository.save(mbCreditCard).getId());
                        break;
                }
            }
        }
        if (!updateDataDTO.getErrors().isEmpty()) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.DUPLICATE);
            return updateDataDTO;
        }
        ppService.setTypeID(typeId);
        ppServiceRepository.save(ppService);
        UUID ppServiceId = ppService.getId();
        List<RefVoucher> lastRefVoucher = new ArrayList<>();
        List<RefVoucher> currentRefVoucher = new ArrayList<>();
        lastRefVoucher.addAll(refVoucherRepository.findAllByRefID1(ppService.getId()));
        if (Objects.nonNull(ppServiceDTO.getRefVouchers())) {
            currentRefVoucher.addAll(ppServiceDTO.getRefVouchers().stream().map(dto -> {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setRefID2(dto.getRefID2());
                refVoucher.setCompanyID(securityDTO.getOrg());
                refVoucher.setRefID1(ppServiceId);
                return  refVoucher;
            }).collect(Collectors.toList()));
        }
        List<RefVoucher> removeList = lastRefVoucher.stream().filter(refVoucher -> !currentRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
        removeList.addAll(lastRefVoucher.stream().filter(refVoucher -> !currentRefVoucher.stream()
                .map(RefVoucher::getRefID1).collect(Collectors.toList()).contains(refVoucher.getRefID1())).collect(Collectors.toList()));
        List<RefVoucher> addList = currentRefVoucher.stream().filter(refVoucher -> !lastRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
        refVoucherRepository.deleteInBatch(removeList);
        refVoucherRepository.saveAll(addList);

        if (Objects.nonNull(ppServiceDTO.getPpServiceDetailDTOS())) {
            Set<PPServiceDetail> serviceDetailList = ppServiceDTO.getPpServiceDetailDTOS().stream().map(ppServiceDetailDTO -> {
                PPServiceDetail ppServiceDetail = new PPServiceDetail();
                if (ppServiceDetailDTO.getId() != null) {
                    ppServiceDetail.setId(ppServiceDetailDTO.getId());
                } else {
                    ppServiceDetail.setId(UUID.randomUUID());
                }
                ppServiceDetail.setPpServiceID(ppServiceId);
                ppServiceDetail.setMaterialGoodsID(ppServiceDetailDTO.getMaterialGoodsId());
                ppServiceDetail.setDescription(ppServiceDetailDTO.getMaterialGoodsName());
                ppServiceDetail.setDebitAccount(ppServiceDetailDTO.getDebitAccount());
                ppServiceDetail.setCreditAccount(ppServiceDetailDTO.getCreditAccount());
                ppServiceDetail.setUnitID(ppServiceDetailDTO.getUnitId());
                ppServiceDetail.setQuantity(ppServiceDetailDTO.getQuantity());
                ppServiceDetail.setUnitPrice(ppServiceDetailDTO.getUnitPrice());
                ppServiceDetail.setUnitPriceOriginal(ppServiceDetailDTO.getUnitPriceOriginal());
                ppServiceDetail.setAmount(ppServiceDetailDTO.getAmount());
                ppServiceDetail.setAmountOriginal(ppServiceDetailDTO.getAmountOriginal());
                ppServiceDetail.setDiscountRate(ppServiceDetailDTO.getDiscountRate());
                ppServiceDetail.setDiscountAmount(ppServiceDetailDTO.getDiscountAmount());
                ppServiceDetail.setDiscountAmountOriginal(ppServiceDetailDTO.getDiscountAmountOriginal());
                ppServiceDetail.setDiscountAccount(ppServiceDetailDTO.getDiscountAccount());
                ppServiceDetail.setVatRate(ppServiceDetailDTO.getVatRate());
                ppServiceDetail.setVatAmount(ppServiceDetailDTO.getVatAmount());
                ppServiceDetail.setVatAmountOriginal(ppServiceDetailDTO.getVatAmountOriginal());
                ppServiceDetail.setVatAccount(ppServiceDetailDTO.getVatAccount());
                ppServiceDetail.setInvoiceDate(DateUtil.getLocalDateFromString(ppServiceDetailDTO.getInvoiceDate(), DateUtil.C_DD_MM_YYYY));
                ppServiceDetail.setInvoiceNo(ppServiceDetailDTO.getInvoiceNo());
                ppServiceDetail.setInvoiceSeries(ppServiceDetailDTO.getInvoiceSeries());
                ppServiceDetail.setGoodsServicePurchaseID(ppServiceDetailDTO.getGoodsServicePurchaseId());
                ppServiceDetail.setBudgetItemID(ppServiceDetailDTO.getBudgetItemId());
                ppServiceDetail.setCostSetID(ppServiceDetailDTO.getCostSetId());
//                ppServiceDetail.setContractID(ppServiceDetailDTO.getEmContractId());
                ppServiceDetail.setStatisticsCodeID(ppServiceDetailDTO.getStatisticsId());
                ppServiceDetail.setDepartmentID(ppServiceDetailDTO.getDepartmentId());
                ppServiceDetail.setExpenseItemID(ppServiceDetailDTO.getExpenseItemId());
                ppServiceDetail.setInvoiceTemplate(ppServiceDetailDTO.getInvoiceTemplate());
                ppServiceDetail.setOrderPriority(ppServiceDetailDTO.getOrderPriority());

                ppServiceDetail.setVatDescription(ppServiceDetailDTO.getVatDescription());
                ppServiceDetail.setDeductionDebitAccount(ppServiceDetailDTO.getDeductionDebitAccount());
                if (!Strings.isNullOrEmpty(ppServiceDetailDTO.getPpOrderNo())) {
                    ppServiceDetail.setPpOrderID(ppServiceDetailDTO.getPpOrderId());
                    ppServiceDetail.setPpOrderDetailID(ppServiceDetailDTO.getPpOrderDetailId());
                }
                ppServiceDetail.setAccountingObjectID(ppServiceDetailDTO.getPostedObjectId());
                return ppServiceDetail;
            }).collect(Collectors.toSet());
            // Get List PPOrderDetail after save PPService
            List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                    ppService.getPpServiceDetails().stream().filter(x -> x.getPpOrderDetailID() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailID(), BigDecimal.ZERO.subtract(x.getQuantity()))).collect(Collectors.toList()),
                    ppServiceDTO.getPpServiceDetailDTOS().stream().filter(x -> x.getPpOrderDetailId() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailId(), x.getQuantity())).collect(Collectors.toList()),
                    ppServiceDTO.getCheckPPOrderQuantity());
            // set new List ppService detail
            ppService.setPpServiceDetails(serviceDetailList);

            ppServiceRepository.save(ppService);
            // update PPOrderDetails
            pporderdetailRepository.saveAll(ppOrderDetails);
            pporderRepository.updateStatus(ppOrderDetails.stream()
                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
            // End update PPOrder if exits
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {

                if (generalLedgerService.record(ppService, messageDTO)) {
                    ppService.setRecorded(true);
                    ppServiceRepository.save(ppService);
                    switch (ppService.getTypeID()) {
                        case PPServiceType.PPSERVICE_CASH:
                            mcPaymentRepository.updateRecordById(ppService.getPaymentVoucherID(), true);
                            break;
                        case PPServiceType.PPSERVICE_PAYMENT_ORDER:
                        case PPServiceType.PPSERVICE_TRANSFER_SEC:
                        case PPServiceType.PPSERVICE_CASH_SEC:
                            mbTellerPaperRepository.updateRecordById(ppService.getPaymentVoucherID(), true);
                            break;
                        case PPServiceType.PPSERVICE_CREDIT_CARD:
                            mbCreditCardRepository.updateRecordById(ppService.getPaymentVoucherID(), true);
                            break;
                    }
                }
            }
            ppServiceDTO.setId(ppService.getId());
        }
        updateDataDTO.setUuid(ppService.getId());
        if (!Strings.isNullOrEmpty(messageDTO.getMsgError())) {
            updateDataDTO.setMessageRecord(messageDTO.getMsgError());
        }
        updateDataDTO.setMessages(isUpdateGenCode ? UpdateDataDTOMessages.UPDATE_SUCCESS : UpdateDataDTOMessages.SAVE_SUCCESS);

        return updateDataDTO;
    }
    private UUID saveMBTellerPaper(PPService ppService, PPServiceDTO ppServiceDTO, UpdateDataDTO updateDataDTO,
                                   LocalDate receiptDate, LocalDate postedDate, UUID companyId, String currencyCode, Boolean recored,
                                   Integer typeId) {
        AccountingObjectBankAccount accountingObjectBankAccount = null;
        if (ppServiceDTO.getAccountReceiverId() != null) {
            accountingObjectBankAccount = accountingObjectBankAccountRepository.getOne(ppServiceDTO.getAccountReceiverId());
        }
        LocalDate issueDate = null;
        if (!Strings.isNullOrEmpty(ppServiceDTO.getIssueDate())) {
            issueDate = DateUtil.getLocalDateFromString(ppServiceDTO.getIssueDate(), DateUtil.C_DD_MM_YYYY);
        }
        AccountingObject accountingObject = null;
        if (ppServiceDTO.getAccountingObjectID() != null) {
            Optional<AccountingObject> accountingObjectOptional = accountingObjectRepository.findById(ppServiceDTO.getAccountingObjectID());
            if (accountingObjectOptional.isPresent()) {
                accountingObject = accountingObjectOptional.get();
            }
        }

        AccountingObject accountingObjectEmployee = null;
        if (ppServiceDTO.getEmployeeID() != null) {
            Optional<AccountingObject> accountingObjectOptional = accountingObjectRepository.findById(ppServiceDTO.getEmployeeID());
            if (accountingObjectOptional.isPresent()) {
                accountingObjectEmployee = accountingObjectOptional.get();
            }
        }
        MBTellerPaper mbTellerPaper = new MBTellerPaper(ppService.getPaymentVoucherID(), recored,
            typeId, ppServiceDTO.getAccountPaymentId(),
            ppServiceDTO.getAccountPaymentName(), ppServiceDTO.getOtherReason(), accountingObjectBankAccount,
            ppServiceDTO.getAccountReceiverName(), accountingObject, ppServiceDTO.getAccountingObjectName(),
            ppServiceDTO.getAccountingObjectAddress(), accountingObjectEmployee, ppServiceDTO.getTypeLedger(),
            updateDataDTO.getNoFBook(), updateDataDTO.getNoMBook(), receiptDate, postedDate, companyId, currencyCode,
            ppServiceDTO.getTotalAmount(), ppServiceDTO.getTotalAmountOriginal(), ppServiceDTO.getTotalVATAmount(),
            ppServiceDTO.getTotalVATAmountOriginal(), ppServiceDTO.getExchangeRate(), ppServiceDTO.getReceiver(),
            ppServiceDTO.getIdentificationNo(), issueDate, ppServiceDTO.getIssueBy());

        return mbTellerPaperRepository.save(mbTellerPaper).getId();
    }

    @Override
    public void saveReceiveBill(List<UUID> listIDPPInvoice) {
        ppServiceRepository.updateNhanHoaDon(listIDPPInvoice);
    }

    @Override
    public UpdateDataDTO deletePPServiceById(UUID id) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        try {
            Optional<PPService> ppServiceOptional = ppServiceRepository.findById(id);
            if (!ppServiceOptional.isPresent()) {
                updateDataDTO.setMessages(UpdateDataDTOMessages.PP_SERVICE_IS_NOT_PRESET);
                return updateDataDTO;
            }
            PPService ppService = ppServiceOptional.get();
            if (ppService.getRecorded()) {
                updateDataDTO.setMessages(UpdateDataDTOMessages.HAD_RECORDED);
                return updateDataDTO;
            }

//            if (ppInvoiceDetailCostRepository.existsByPpServiceID(ppService.getId())) {
//                updateDataDTO.setMessages(UpdateDataDTOMessages.HAD_REFERENCE);
//                return updateDataDTO;
//            }
            ppInvoiceDetailCostRepository.cleanPPServiceId(ppService.getId());
            switch (ppService.getTypeID()) {
                case PPServiceType.PPSERVICE_CASH:
                    if (mcPaymentRepository.existsById(ppService.getPaymentVoucherID())) {
                        mcPaymentRepository.deleteById(ppService.getPaymentVoucherID());
                    }
                    break;
                case PPServiceType.PPSERVICE_PAYMENT_ORDER:
                case PPServiceType.PPSERVICE_TRANSFER_SEC:
                case PPServiceType.PPSERVICE_CASH_SEC:
                    if (mbTellerPaperRepository.existsById(ppService.getPaymentVoucherID())) {
                        mbTellerPaperRepository.deleteById(ppService.getPaymentVoucherID());
                    }

                    break;
                case PPServiceType.PPSERVICE_CREDIT_CARD:
                    if (mbCreditCardRepository.existsById(ppService.getPaymentVoucherID())) {
                        mbCreditCardRepository.deleteById(ppService.getPaymentVoucherID());
                    }
                    break;
            }
            refVoucherRepository.deleteByRefID1(ppService.getId());
            refVoucherRepository.deleteByRefID2(ppService.getId());
            List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                    ppService.getPpServiceDetails().stream().filter(x -> x.getPpOrderDetailID() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailID(), BigDecimal.ZERO.subtract(x.getQuantity()))).collect(Collectors.toList()),
                    new ArrayList<>(), false);
            // update PPOrderDetails
            pporderdetailRepository.saveAll(ppOrderDetails);
            pporderRepository.updateStatus(ppOrderDetails.stream()
                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
            ppServiceRepository.deleteById(ppService.getId());
            updateDataDTO.setMessages(UpdateDataDTOMessages.DELETE_SUCCESS);
        } catch (Exception e) {
            updateDataDTO.setMessages(e.getMessage());
        }
        return updateDataDTO;
    }

    @Override
    public UpdateDataDTO deletePPServiceInId(List<UUID> ids) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        updateDataDTO.setMessages(UpdateDataDTOMessages.DELETE_SUCCESS);
        if (ids.size() == 0) {
            return updateDataDTO;
        }
        try {
            ppInvoiceDetailCostRepository.cleanPPServiceIdByListID(ids);
            List<PPServiceDetail> ppServiceDetails = ppServiceDetailRepository.findAllByPpServiceIDIn(ids);
            List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                ppServiceDetails.stream().filter(x -> x.getPpOrderDetailID() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailID(), BigDecimal.ZERO.subtract(x.getQuantity()))).collect(Collectors.toList()),
                new ArrayList<>(), false);
            pporderdetailRepository.saveAll(ppOrderDetails);
            pporderRepository.updateStatus(ppOrderDetails.stream()
                .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
            viewVoucherNoRespository.deletePaymentVoucherInID(ppServiceDetailRepository.findPaymentVoucherIDByPPServiceID(ids));
            refVoucherRepository.deleteByRefID1sOrRefID2s(ids);
            ppServiceRepository.deleteByListID(ids);
            ppServiceDetailRepository.deleteByPpServiceIDIn(ids);
        } catch (Exception e) {
            updateDataDTO.setMessages(e.getMessage());
        }
        return updateDataDTO;
    }

    @Override
    public UpdateDataDTO checkHadReference(UUID uuid) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        List<String> checkList = new ArrayList<>();
        if (ppInvoiceDetailCostRepository.existsByPpServiceID(uuid)) {
            checkList.add(UpdateDataDTOMessages.HAD_REFERENCE);
        }
        if (ppServiceRepository.checkHasPaid(uuid)) {
            checkList.add(UpdateDataDTOMessages.HAD_PAID);
        }
        if (checkList.size() == 2) {
            updateDataDTO.setMessages(UpdateDataDTOMessages.HAD_REFERENCE_AND_PAID);
        } else if (checkList.size() == 1){
            updateDataDTO.setMessages(checkList.get(0));
        }
        return updateDataDTO;
    }

    @Override
    public UpdateDataDTO getPPServiceDTOByLocation(UUID ppServiceId, Integer action, Integer receiptType, String toDate, String fromDate,
                                                   Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                                   String freeSearch) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!currentUserLoginAndOrg.isPresent()) {
            throw new BadRequestAlertException("Tài khoản không tồn tại", "PPService", Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Integer currentBook = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(SystemOption.PHIEN_SoLamViec)).findAny().get().getData());

        UpdateDataDTO updateDataDTO = ppServiceRepository.findPPServiceDTOByLocationItem(action,currentBook, ppServiceId, currentUserLoginAndOrg.get().getOrg(),
                receiptType, toDate, fromDate, hasRecord, currencyID,
                accountingObjectID, noBookType, freeSearch);

        UUID newPPServiceId = updateDataDTO.getUuid();
        if (updateDataDTO.getUuid() != null) {
            PPServiceDTO ppServiceDTO = ppServiceRepository.findOneById(newPPServiceId, currentBook, currentUserLoginAndOrg.get().getOrg());
            ppServiceDTO.setPpServiceDetailDTOS(ppServiceDetailRepository.findAllPPServiceDetailByPPServiceId(ppServiceDTO.getId()));
            ppServiceDTO.setRefVouchers(refVoucherRepository.getRefViewVoucher(ppServiceId, currentBook == Constants.TypeLedger.FINANCIAL_BOOK ));
            updateDataDTO.setResult(ppServiceDTO);
        }

        return updateDataDTO;
    }

    @Override
    public Page<ReceiveBillSearchDTO>findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean isNoMBook = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
        }
        return ppServiceRepository.findAllReceiveBillSearchDTO(pageable, searchVoucher, isNoMBook, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public Page<PPServiceCostVoucherDTO> findCostVouchers(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID ppInvoiceId, Boolean isHaiQuan) {
        isHaiQuan = isHaiQuan != null ? isHaiQuan : false;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent() && userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");

            return ppServiceRepository.findCostVouchers(pageable, accountingObject, fromDate, toDate, isNoMBook, currentUserLoginAndOrg.get().getOrg(), ppInvoiceId, isHaiQuan);
        }
        return null;
    }

    @Override
    public byte[] exportPdf(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!currentUserLoginAndOrg.isPresent()) {
            throw new BadRequestAlertException("Tài khoản không tồn tại", "PPService", Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            Page<PPServiceDTO> ppService = ppServiceRepository.findAllPPService(null, receiptType, toDate, fromDate, hasRecord, currencyID,
                    accountingObjectID, noBookType, freeSearch, currentUserLoginAndOrg.get().getOrg());
            return PdfUtils.writeToFile(ppService.getContent(), ExcelConstant.PPService.HEADER, ExcelConstant.PPService.FIELD);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!currentUserLoginAndOrg.isPresent()) {
            throw new BadRequestAlertException("Tài khoản không tồn tại", "PPService", Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {

            Page<PPServiceDTO> ppService = ppServiceRepository.findAllPPService(null, receiptType, toDate, fromDate, hasRecord, currencyID,
                    accountingObjectID, noBookType, freeSearch, currentUserLoginAndOrg.get().getOrg());
            return ExcelUtils.writeToFile(ppService.getContent(), ExcelConstant.PPService.NAME, ExcelConstant.PPService.HEADER, ExcelConstant.PPService.FIELD);
        }
        return null;
    }
}
