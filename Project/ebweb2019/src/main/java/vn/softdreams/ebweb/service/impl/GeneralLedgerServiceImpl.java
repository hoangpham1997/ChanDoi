package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.AccountingObjectBankAccountRepository;
import vn.softdreams.ebweb.repository.AccountingObjectRepository;
import vn.softdreams.ebweb.repository.BankAccountDetailsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.repository.GeneralLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.RequestRecordListDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.PPInvoiceType.*;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;
import static vn.softdreams.ebweb.service.util.TypeConstant.*;
import static vn.softdreams.ebweb.service.util.TypeConstant.MUA_HANG_CHUA_THANH_TOAN;

/**
 * Service Implementation for managing GeneralLedger.
 */
@Service
@Transactional
public class GeneralLedgerServiceImpl implements GeneralLedgerService {

    private final Logger log = LoggerFactory.getLogger(GeneralLedgerServiceImpl.class);
    final int TYPE_MC_RECEIPT = 100; // Phiếu thu
    final int TYPE_PHIEU_THU_TIEN_KHACH_HANG = 101; // Phiếu thu
    final List<Integer> LIST_TYPE_MC_RECEIPT = Arrays.asList(100, 101);
    final int TYPE_MC_PAYMENT = 110; // Phiếu chi
    final int TYPE_MC_AUDIT = 180;
    final int TYPE_MBDEPOSIT = 160; // Nộp tiền vào tài khoản
    final int TYPE_MCDEPOSIT = 161; // Nộp tiền từ khách hàng
    final int TYPE_MSDEPOSIT = 162; // Nộp tiền từ bán hàng
    final int UY_NHIEM_CHI = 120;
    final int NHAP_KHO = 400;
    final int NHAP_KHO_DC = 401;
    final int NHAP_KHO_MH = 402;
    final int NHAP_KHO_HBTL = 403;
    final int XUAT_KHO = 410;
    final int XUAT_KHO_BH = 411;
    final int XUAT_KHO_HMTL = 412;
    final int XUAT_KHO_DC = 413;
    final int SEC_CHUYEN_KHOAN = 130;
    final int SEC_TIEN_MAT = 140;
    final int TYPE_PPDiscountReturn = 220;
    final int TYPE_PPDiscountPurchase = 230;
    final int TYPE_MBCreditCard = 170;
    final int TYPE_GOtherVoucher = 700;
    final int TYPE_NGHIEM_THU_CONG_TRINH = 703;
    final int TYPE_CPExpense_Tranfer = 701;
    final int TYPE_GOtherVoucher_Allocations = 709;
    final int UY_NHIEM_CHI_MUA_HANG = 125;
    final int SEC_CHUYEN_KHOAN_MUA_HANG = 131;
    final int SEC_TIEN_MAT_MUA_HANG = 141;
    private final UserService userService;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final ViewVoucherNoRespository viewVoucherNoRespository;
    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final AccountingObjectRepository accountingObjectRepository;
    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final MCReceiptRepository mcReceiptRepository;
    private final MBDepositRepository mbDepositRepository;
    private final MCPaymentRepository mcPaymentRepository;
    private final MCPaymentDetailVendorRepository mcPaymentDetailVendorRepository;
    private final PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository;
    private final MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository;
    private final MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository;
    private final MBCreditCardRepository mbCreditCardRepository;
    private final MBTellerPaperRepository mbTellerPaperRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final RepositoryRepository repositoryRepository;
    private final MaterialGoodsRepository materialGoodsRepository;
    private final RSInwardOutwardRepository rSInwardOutwardRepository;
    private final RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;
    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final SaReturnRepository saReturnRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final SAInvoiceDetailsRepository saInvoiceDetailsRepository;
    private final RSTransferRepository rsTransferRepository;
    private final PPServiceRepository ppServiceRepository;
    private final GOtherVoucherRepository gOtherVoucherRepository;
    private final TIIncrementRepository tiIncrementRepository;
    private final TIAllocationRepository tiAllocationRepository;
    private final TIAuditRepository tiAuditRepository;
    private final FAIncrementRepository faIncrementRepository;
    private final PPInvoiceRepository pPInvoiceRepository;
    private final CreditCardRepository creditCardRepository;
    private final BankRepository bankRepository;
    private final RepositoryLedgerService repositoryLedgerService;
    private final ViewVoucherNoService viewVoucherNoService;
    private final UtilsService utilsService;
    private final ToolsRepository toolsRepository;
    private final ToolledgerRepository toolledgerRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final TITransferRepository tiTransferRepository;
    private final TIAdjustmentRepository tiAdjustmentRepository;
    private final TIDecrementRepository tiDecrementRepository;
    private final CPAllocationGeneralExpenseRepository cpAllocationGeneralExpenseRepository;
    private final CPExpenseTranferRepository cpExpenseTranferRepository;
    private final FixedAssetLedgerRepository fixedAssetLedgerRepository;
    private final FixedAssetRepository fixedAssetRepository;

    public GeneralLedgerServiceImpl(GeneralLedgerRepository generalLedgerRepository,
                                    UserService userService,
                                    ViewVoucherNoRespository viewVoucherNoRespository,
                                    BankAccountDetailsRepository bankAccountDetailsRepository,
                                    AccountingObjectRepository accountingObjectRepository,
                                    AccountingObjectBankAccountRepository accountingObjectBankAccountRepository,
                                    MCReceiptRepository mcReceiptRepository,
                                    MBDepositRepository mbDepositRepository,
                                    MCPaymentRepository mcPaymentRepository,
                                    MCPaymentDetailVendorRepository mcPaymentDetailVendorRepository,
                                    PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository,
                                    MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository,
                                    MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository,
                                    MBCreditCardRepository mbCreditCardRepository,
                                    MBTellerPaperRepository mbTellerPaperRepository,
                                    PPDiscountReturnRepository ppDiscountReturnRepository,
                                    RepositoryLedgerRepository repositoryLedgerRepository,
                                    RepositoryRepository repositoryRepository,
                                    MaterialGoodsRepository materialGoodsRepository,
                                    RSInwardOutwardRepository rSInwardOutwardRepository,
                                    SaReturnRepository saReturnRepository,
                                    SAInvoiceRepository saInvoiceRepository,
                                    PPServiceRepository ppServiceRepository,
                                    SAInvoiceDetailsRepository saInvoiceDetailsRepository,
                                    RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository,
                                    SaReturnDetailsRepository saReturnDetailsRepository,
                                    RSTransferRepository rsTransferRepository, GOtherVoucherRepository gOtherVoucherRepository,
                                    PPInvoiceRepository pPInvoiceRepository,
                                    TIIncrementRepository tiIncrementRepository,
                                    TIAllocationRepository tiAllocationRepository, TIAuditRepository tiAuditRepository,
                                    FAIncrementRepository faIncrementRepository, CreditCardRepository creditCardRepository,
                                    BankRepository bankRepository,
                                    RepositoryLedgerService repositoryLedgerService,
                                    ViewVoucherNoService viewVoucherNoService,
                                    TITransferRepository tiTransferRepository, TIAdjustmentRepository tiAdjustmentRepository, TIDecrementRepository tiDecrementRepository,
                                    CPAllocationGeneralExpenseRepository cpAllocationGeneralExpenseRepository,
                                    CPExpenseTranferRepository cpExpenseTranferRepository,
                                    UtilsService utilsService, ToolsRepository toolsRepository, ToolledgerRepository toolledgerRepository, SystemOptionRepository systemOptionRepository, FixedAssetLedgerRepository fixedAssetLedgerRepository, FixedAssetRepository fixedAssetRepository) {

        this.generalLedgerRepository = generalLedgerRepository;
        this.viewVoucherNoRespository = viewVoucherNoRespository;
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.userService = userService;
        this.accountingObjectRepository = accountingObjectRepository;
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mbDepositRepository = mbDepositRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mcPaymentDetailVendorRepository = mcPaymentDetailVendorRepository;
        this.ppInvoiceDetailCostRepository = ppInvoiceDetailCostRepository;
        this.mbTellerPaperDetailVendorRepository = mbTellerPaperDetailVendorRepository;
        this.mbCreditCardDetailVendorRepository = mbCreditCardDetailVendorRepository;
        this.mbCreditCardRepository = mbCreditCardRepository;
        this.mbTellerPaperRepository = mbTellerPaperRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.repositoryRepository = repositoryRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.rSInwardOutwardRepository = rSInwardOutwardRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.rsTransferRepository = rsTransferRepository;
        this.gOtherVoucherRepository = gOtherVoucherRepository;
        this.tiIncrementRepository = tiIncrementRepository;
        this.pPInvoiceRepository = pPInvoiceRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.saInvoiceDetailsRepository = saInvoiceDetailsRepository;
        this.rsInwardOutWardDetailsRepository = rsInwardOutWardDetailsRepository;
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.tiAllocationRepository = tiAllocationRepository;
        this.tiAuditRepository = tiAuditRepository;
        this.faIncrementRepository = faIncrementRepository;
        this.creditCardRepository = creditCardRepository;
        this.bankRepository = bankRepository;
        this.repositoryLedgerService = repositoryLedgerService;
        this.viewVoucherNoService = viewVoucherNoService;
        this.utilsService = utilsService;
        this.toolsRepository = toolsRepository;
        this.toolledgerRepository = toolledgerRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.cpAllocationGeneralExpenseRepository = cpAllocationGeneralExpenseRepository;
        this.tiTransferRepository = tiTransferRepository;
        this.tiAdjustmentRepository = tiAdjustmentRepository;
        this.tiDecrementRepository = tiDecrementRepository;
        this.cpExpenseTranferRepository = cpExpenseTranferRepository;
        this.fixedAssetLedgerRepository = fixedAssetLedgerRepository;
        this.fixedAssetRepository = fixedAssetRepository;
    }

    /**
     * Save a generalLedger.
     *
     * @param generalLedger the entity to save
     * @return the persisted entity
     */
    @Override
    public GeneralLedger save(GeneralLedger generalLedger) {
        log.debug("Request to save GeneralLedger : {}", generalLedger);
        return generalLedgerRepository.save(generalLedger);
    }

    /**
     * Get all the generalLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralLedger> findAll(Pageable pageable) {
        log.debug("Request to get all GeneralLedgers");
        return generalLedgerRepository.findAll(pageable);
    }


    /**
     * Get one generalLedger by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeneralLedger> findOne(UUID id) {
        log.debug("Request to get GeneralLedger : {}", id);
        return generalLedgerRepository.findById(id);
    }

    /**
     * Delete the generalLedger by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GeneralLedger : {}", id);
        generalLedgerRepository.deleteById(id);
    }

    public boolean checkErrorRecord(Object object, MessageDTO msg, UserDTO userDTO) {
        if (object.getClass() == MCReceipt.class) {
            MCReceipt mcReceipt = (MCReceipt) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(mcReceipt.getTypeLedger(), userDTO.getOrganizationUnit().getId(), mcReceipt.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (MCReceiptDetails sad : mcReceipt.getMCReceiptDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if (sad.getBankAccountDetails() != null) {
                                if ((mcReceipt.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || mcReceipt.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    sad.getBankAccountDetails().getId().equals(item.getBankAccountDetailID()) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            } else {
                                if ((mcReceipt.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || mcReceipt.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else if (object.getClass() == MCPayment.class) {
            MCPayment mcPayment = (MCPayment) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(mcPayment.getTypeLedger(), userDTO.getOrganizationUnit().getId(), mcPayment.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (MCPaymentDetails sad : mcPayment.getMCPaymentDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if ((mcPayment.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || mcPayment.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                } else {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (object.getClass() == MBDeposit.class) {

        } else if (object.getClass() == MBCreditCard.class) {
            MBCreditCard mbCreditCard = (MBCreditCard) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(mbCreditCard.getTypeLedger(), userDTO.getOrganizationUnit().getId(), mbCreditCard.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (MBCreditCardDetails sad : mbCreditCard.getmBCreditCardDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if ((mbCreditCard.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || mbCreditCard.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                } else {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (object.getClass() == MBTellerPaper.class) {
            MBTellerPaper mbTellerPaper = (MBTellerPaper) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(mbTellerPaper.getTypeLedger(), userDTO.getOrganizationUnit().getId(), mbTellerPaper.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (MBTellerPaperDetails sad : mbTellerPaper.getmBTellerPaperDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if ((mbTellerPaper.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || mbTellerPaper.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                mbTellerPaper.getBankAccountDetailID().equals(item.getBankAccountDetailID()) &&
                                sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                } else {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (object.getClass() == PPDiscountReturn.class) {

        } else if (object.getClass() == GOtherVoucher.class && msg != null && msg.getMsgError() != null && msg.getMsgError().equals(Constants.GOtherVoucher.KET_CHUYEN)) {

        } else if (object.getClass() == GOtherVoucher.class && ((GOtherVoucher) object).getTypeID() != TYPE_GOtherVoucher_Allocations) {
            GOtherVoucher gOtherVoucher = (GOtherVoucher) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(gOtherVoucher.getTypeLedger(), userDTO.getOrganizationUnit().getId(), gOtherVoucher.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (GOtherVoucherDetails sad : gOtherVoucher.getgOtherVoucherDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if (sad.getBankAccountDetailID() != null) {
                                if ((gOtherVoucher.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || gOtherVoucher.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    sad.getBankAccountDetailID().equals(item.getBankAccountDetailID()) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            } else {
                                if ((gOtherVoucher.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || gOtherVoucher.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else if (object instanceof SaReturn) {
            SaReturn saReturn = (SaReturn) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(saReturn.getTypeLedger(), userDTO.getOrganizationUnit().getId(), saReturn.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (SaReturnDetails sad : saReturn.getSaReturnDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if ((saReturn.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || saReturn.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                } else {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (object instanceof PPService) {
            PPService ppService = (PPService) object;
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(ppService.getTypeLedger(), userDTO.getOrganizationUnit().getId(), ppService.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (PPServiceDetail ppsd : ppService.getPpServiceDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            if ((ppService.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || ppService.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                ppsd.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(ppsd.getAmount()).floatValue() < 0) {
                                if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                } else {
                                    msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                }
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (object instanceof PPInvoice) {
            PPInvoice ppInvoice = (PPInvoice) object;
            // bán hàng chưa thu tiền thì ko cần check
            if (ppInvoice.getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN)) {
                return true;
            }
            SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
            if (systemOptionXQTQ.getData().equals("0")) {
                List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(ppInvoice.getTypeLedger(), userDTO.getOrganizationUnit().getId(), ppInvoice.getPostedDate());
                if (viewGLPayExceedCashAll.size() > 0) {
                    for (PPInvoiceDetails sad : ppInvoice.getPpInvoiceDetails()) {
                        for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                            BankAccountDetailDTO bankAccountDetailDTO = bankAccountDetailsRepository.getByPaymentVoucherId(ppInvoice.getPaymentVoucherId(), ppInvoice.getTypeId());
                            if (bankAccountDetailDTO != null && bankAccountDetailDTO.getId() != null) {
                                if ((ppInvoice.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || ppInvoice.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    bankAccountDetailDTO.getId().equals(item.getBankAccountDetailID()) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            } else {
                                if ((ppInvoice.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || ppInvoice.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                    sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                    if (item.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                    } else if (item.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                    } else {
                                        msg.setMsgError(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else if (object instanceof RSInwardOutward && msg != null && msg.equals("XUAT_KHO")) {

        } else if (object instanceof RSInwardOutward) {

        } else if (object instanceof SAInvoice) {
            SAInvoice saInvoice = (SAInvoice) object;
            SystemOption systemOptionTGXK = new SystemOption();
            for (SystemOption item : userDTO.getSystemOption()) {
                if (item.getCode().equals(Constants.SystemOption.VTHH_PPTinhGiaXKho)) {
                    systemOptionTGXK = item;
                }
            }
            if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher()) && Constants.CalculationMethod.PP_TINH_GIA_DICH_DANH.equals(systemOptionTGXK.getData())) {
                for (SAInvoiceDetails sad : saInvoice.getsAInvoiceDetails()) {
                    if (sad.getContractDetailID() != null) {
                        BigDecimal iwQuantity = repositoryLedgerRepository.getIwQuantityByConfrontDetailID(sad.getContractDetailID());
                        if (sad.getQuantity().floatValue() > iwQuantity.floatValue()) {
                            msg.setMsgError(Constants.MSGRecord.XUAT_QUA_SO_TON);
                            return false;
                        }
                    }
                }
            }
        } else if (object instanceof List) {

        }
        return true;
    }

    @Override
    public boolean record(Object object, MessageDTO msg) {
        log.debug("Request to record");
        UserDTO userDTO = userService.getAccount();
        if (!checkErrorRecord(object, msg, userDTO)) {
            return false;
        }
        List<GeneralLedger> lstGL = new ArrayList<>();
        List<Toolledger> lstTL = new ArrayList<>();
        List<FixedAssetLedger> lstFx = new ArrayList<>();
        if (object.getClass() == MCReceipt.class) {
            lstGL = GenGeneralLedgers((MCReceipt) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == MCPayment.class) {
            lstGL = GenGeneralLedgers((MCPayment) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == MBDeposit.class) {
            lstGL = GenGeneralLedgers((MBDeposit) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == MBCreditCard.class) {
            lstGL = GenGeneralLedgers((MBCreditCard) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == MBTellerPaper.class) {
            lstGL = GenGeneralLedgers((MBTellerPaper) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == PPDiscountReturn.class) {
            lstGL = GenGeneralLedgers((PPDiscountReturn) object);
            if (lstGL != null && lstGL.size() > 0) {
                generalLedgerRepository.saveAll(lstGL);
            }

        } else if (object.getClass() == GOtherVoucher.class && msg != null && msg.getMsgError() != null && msg.getMsgError().equals(Constants.GOtherVoucher.KET_CHUYEN)) {
            lstGL = GenGeneralLedgerGOtherVoucherKc((GOtherVoucher) object);
            if (lstGL == null) {
                msg.setMsgError(Constants.MSGRecord.KC_DU_LIEU_AM);
                return false;
            }
            generalLedgerRepository.saveAll(lstGL);
        } else if (object.getClass() == GOtherVoucher.class) {
            lstGL = GenGeneralLedgers((GOtherVoucher) object);
            generalLedgerRepository.saveAll(lstGL);
        }else if (object.getClass() == CPExpenseTranfer.class) {
            lstGL = GenGeneralLedgers((CPExpenseTranfer) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof SaReturn) {
            lstGL = genGeneralLedgers((SaReturn) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof PPService) {
            lstGL = genGeneralLedgers((PPService) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof PPInvoice) {
            lstGL = genGeneralLedgers((PPInvoice) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof RSInwardOutward && msg != null && msg.getMsgError().equals("XUAT_KHO")) {
            lstGL = genGeneralLedgerOutWard((RSInwardOutward) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof RSInwardOutward) {
            lstGL = genGeneralLedgers((RSInwardOutward) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof RSTransfer) {
            lstGL = genGeneralLedgersTransfer((RSTransfer) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof SAInvoice) {
            lstGL = genGeneralLedgers((SAInvoice) object);
            generalLedgerRepository.saveAll(lstGL);
        } else if (object instanceof TIIncrement) {
            lstTL = genGeneralLedgers((TIIncrement) object);
            toolledgerRepository.saveAll(lstTL);
        } else if (object instanceof TIAllocation) {
            lstTL = genGeneralLedgers((TIAllocation) object);
            toolledgerRepository.saveAll(lstTL);
        } else if (object instanceof TIDecrement) {
            lstTL = genGeneralLedgers((TIDecrement) object);
            toolledgerRepository.saveAll(lstTL);
        } else if (object instanceof TIAdjustment) {
            lstTL = genGeneralLedgers((TIAdjustment) object);
            toolledgerRepository.saveAll(lstTL);
        } else if (object instanceof TITransfer) {
            lstTL = genGeneralLedgers((TITransfer) object);
            toolledgerRepository.saveAll(lstTL);
        } else if (object instanceof FAIncrement) {
            lstFx = genGeneralLedgers((FAIncrement) object);
            fixedAssetLedgerRepository.saveAll(lstFx);
        } else if (object instanceof List) {
            if (!((List) object).isEmpty()) {
                if (((List) object).get(0) instanceof OPAccount) {
                    lstGL = genGeneralLedgersOPAccount((List<OPAccount>) object);
                } else if (((List) object).get(0) instanceof OPMaterialGoods) {
                    lstGL = genGeneralLedgersOPMaterialGoods((List<OPMaterialGoods>) object);
                }
                if (lstGL.size() > 0) {
                    generalLedgerRepository.deleteAllByReferenceID(lstGL.stream()
                        .map(GeneralLedger::getReferenceID)
                        .collect(Collectors.toList()));
                    viewVoucherNoRespository.saveGeneralLedger(lstGL);
                }
            }

        }
        return true;
    }

    private List<Toolledger> genGeneralLedgers(TITransfer tiTransfer) {
        List<Toolledger> listGenTemp = new ArrayList<>();
        List<TITransferDetails> lstDT = new ArrayList<>( tiTransfer.getTiTransferDetails());
//        Collections.sort(lstDT, Comparator.comparingInt(TITransferDetails::getOrderPriority));
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
//        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null) {
//            rSInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
//        }
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Tools> tool = toolsRepository.getToolsById(lstDT.get(i).getToolsID());
//          Điều chuyển giảm
            Toolledger toolledger = new Toolledger();
            if (tool.isPresent()) {
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
                toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
                toolledger.setToolCode(tool.get().getToolCode());
                toolledger.setToolName(tool.get().getToolName());
            }
            toolledger.setBranchID(null);
            toolledger.setReferenceID(tiTransfer.getId());
            toolledger.setCompanyID(tiTransfer.getCompanyID());
            toolledger.setTypeID(tiTransfer.getTypeID());
            toolledger.date(tiTransfer.getDate());
            toolledger.postedDate(tiTransfer.getDate());
            toolledger.noFBook(tiTransfer.getNoFBook());
            toolledger.noMBook(tiTransfer.getNoMBook());
            toolledger.typeLedger(tiTransfer.getTypeLedger());
            toolledger.setToolsID(lstDT.get(i).getToolsID());
            toolledger.setReason(tiTransfer.getReason());
            toolledger.setDepartmentID(lstDT.get(i).getFromDepartmentID());
            toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
            toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger.setIncrementQuantity(BigDecimal.ZERO);
            toolledger.setDecrementQuantity(lstDT.get(i).getTransferQuantity());
            toolledger.setIncrementAmount(BigDecimal.ZERO);
            toolledger.setDecrementAmount(BigDecimal.ZERO);
            toolledger.setAllocationAmount(BigDecimal.ZERO);
            toolledger.setAllocatedAmount(BigDecimal.ZERO);
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger.setToolCode(tool.get().getToolCode());
//                toolledger.setToolName(tool.get().getToolName());
//            }

            listGenTemp.add(toolledger);

            // điều chuyển tăng

            Toolledger toolledger2 = new Toolledger();
            if (tool.isPresent()) {
                toolledger2.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
                toolledger2.setAllocatedAmount(tool.get().getAllocatedAmount());
                toolledger2.setToolCode(tool.get().getToolCode());
                toolledger2.setToolName(tool.get().getToolName());
            }
            toolledger2.setBranchID(null);
            toolledger2.setReferenceID(tiTransfer.getId());
            toolledger2.setCompanyID(tiTransfer.getCompanyID());
            toolledger2.setTypeID(tiTransfer.getTypeID());
            toolledger2.date(tiTransfer.getDate());
            toolledger2.postedDate(tiTransfer.getDate());
            toolledger2.setDepartmentID(lstDT.get(i).getToDepartmentID());
            toolledger2.noFBook(tiTransfer.getNoFBook());
            toolledger2.noMBook(tiTransfer.getNoMBook());
            toolledger2.typeLedger(tiTransfer.getTypeLedger());
            toolledger2.setToolsID(lstDT.get(i).getToolsID());
            toolledger2.setReason(tiTransfer.getReason());
            toolledger2.setIncrementAllocationTime(BigDecimal.ZERO);
            toolledger2.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger2.setIncrementQuantity(lstDT.get(i).getTransferQuantity());
            toolledger2.setDecrementQuantity(BigDecimal.ZERO);
            toolledger2.setIncrementAmount(BigDecimal.ZERO);
            toolledger2.setDecrementAmount(BigDecimal.ZERO);
            toolledger2.setAllocationAmount(BigDecimal.ZERO);
            toolledger2.setAllocatedAmount(BigDecimal.ZERO);
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger2.setToolCode(tool.get().getToolCode());
//                toolledger2.setToolName(tool.get().getToolName());
//            }

            listGenTemp.add(toolledger2);
        }

        return listGenTemp;
    }

    //Điều chỉnh ccdc
    private List<Toolledger> genGeneralLedgers(TIAdjustment tiAdjustment) {
        List<Toolledger> listGenTemp = new ArrayList<>();
        List<TIAdjustmentDetails> lstDT = new ArrayList<>( tiAdjustment.getTiAdjustmentDetails());
//        Collections.sort(lstDT, Comparator.comparingInt(TIAdjustmentDetails::getOrderPriority));
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
//        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null) {
//            rSInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
//        }
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Tools> tool = toolsRepository.getToolsById(lstDT.get(i).getToolsID());
            Toolledger toolledger = new Toolledger();
            if (tool.isPresent()) {
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
                toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
                toolledger.setToolCode(tool.get().getToolCode());
                toolledger.setToolName(tool.get().getToolName());
            }
            toolledger.setBranchID(null);
            if (lstDT.get(i).getDiffRemainingAmount().compareTo(BigDecimal.ZERO) < 0) {
                toolledger.setIncrementAmount(lstDT.get(i).getDiffRemainingAmount());
            } else {
                toolledger.setDecrementAmount(lstDT.get(i).getDiffRemainingAmount());
            }
            if (lstDT.get(i).getDifferAllocationTime() < 0) {
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(lstDT.get(i).getDifferAllocationTime()));
            } else {
                toolledger.setDecrementAllocationTime(BigDecimal.valueOf(lstDT.get(i).getDifferAllocationTime()));
            }
            toolledger.setAllocatedAmount(lstDT.get(i).getAllocatedAmount());
            toolledger.setReferenceID(tiAdjustment.getId());
            toolledger.setCompanyID(tiAdjustment.getCompanyID());
            toolledger.setTypeID(tiAdjustment.getTypeID());
            toolledger.date(tiAdjustment.getDate());
            toolledger.postedDate(tiAdjustment.getDate());
            toolledger.noFBook(tiAdjustment.getNoFBook());
            toolledger.noMBook(tiAdjustment.getNoMBook());
            toolledger.typeLedger(tiAdjustment.getTypeLedger());
            toolledger.setToolsID(lstDT.get(i).getToolsID());
//            toolledger.setDescription(lstDT.get(i).get());
            toolledger.setReason(tiAdjustment.getReason());
//            toolledger.setUnitPrice(lstDT.get(i).get());
            toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
            toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger.setIncrementQuantity(lstDT.get(i).getQuantity());
            toolledger.setDecrementQuantity(BigDecimal.ZERO);
            toolledger.setIncrementAmount(tiAdjustment.getTotalAmount());
            toolledger.setDecrementAmount(BigDecimal.ZERO);
            toolledger.setAllocationAmount(BigDecimal.ZERO);
            toolledger.setAllocatedAmount(BigDecimal.ZERO);
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger.setToolCode(tool.get().getToolCode());
//                toolledger.setToolName(tool.get().getToolName());
//            }

            listGenTemp.add(toolledger);
        }

        return listGenTemp;
    }

    //Ghi giảm ccdc
    private List<Toolledger> genGeneralLedgers(TIDecrement tiDecrement) {
        List<Toolledger> listGenTemp = new ArrayList<>();
        List<TIDecrementDetails> lstDT = new ArrayList<>( tiDecrement.getTiDecrementDetails());
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Tools> tool = toolsRepository.getToolsById(lstDT.get(i).getToolsID());
            Toolledger toolledger = new Toolledger();
            if (tool.isPresent()) {
                toolledger.setUnitPrice(tool.get().getUnitPrice());
                toolledger.setToolName(tool.get().getToolName());
                toolledger.setToolCode(tool.get().getToolCode());
                toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
            }
            toolledger.setBranchID(null);
            toolledger.setReferenceID(tiDecrement.getId());
            toolledger.setCompanyID(tiDecrement.getCompanyID());
            toolledger.setTypeID(tiDecrement.getTypeID());
            toolledger.date(tiDecrement.getDate());
            toolledger.postedDate(tiDecrement.getDate());
            toolledger.noFBook(tiDecrement.getNoFBook());
            toolledger.noMBook(tiDecrement.getNoMBook());
            toolledger.typeLedger(tiDecrement.getTypeLedger());
            toolledger.setToolsID(lstDT.get(i).getToolsID());
            toolledger.setReason(tiDecrement.getReason());;
            toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
            toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger.setIncrementQuantity(BigDecimal.ZERO);
            toolledger.setDepartmentID(lstDT.get(i).getDepartmentID());
            toolledger.setDecrementQuantity(lstDT.get(i).getDecrementQuantity());
            toolledger.setIncrementAmount(BigDecimal.ZERO);
            toolledger.setDecrementAmount(BigDecimal.ZERO);
            toolledger.setAllocationAmount(BigDecimal.ZERO);
            toolledger.setOrderPriority(lstDT.get(i).getOrderPriority());
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger.setToolCode(tool.get().getToolCode());
//                toolledger.setToolName(tool.get().getToolName());
//            }
            listGenTemp.add(toolledger);
        }

        return listGenTemp;
    }

    private List<Toolledger> genGeneralLedgers(TIAllocation tiAllocation) {
        List<Toolledger> listGenTemp = new ArrayList<>();
        List<TIAllocationDetails> lstDT = new ArrayList<>( tiAllocation.getTiAllocationDetails());
//        Collections.sort(lstDT, Comparator.comparingInt(TIAllocationDetails::getOrderPriority));
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
//        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null) {
//            rSInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
//        }
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Tools> tool = toolsRepository.getToolsById(lstDT.get(i).getToolsID());
            Toolledger toolledger = new Toolledger();
            if (tool.isPresent()) {
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
                toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
            }
            toolledger.setBranchID(null);
            toolledger.setReferenceID(tiAllocation.getId());
            toolledger.setCompanyID(tiAllocation.getCompanyID());
            toolledger.setTypeID(tiAllocation.getTypeID());
            toolledger.date(tiAllocation.getDate());
            toolledger.noFBook(tiAllocation.getNoFBook());
            toolledger.noMBook(tiAllocation.getNoMBook());
            toolledger.typeLedger(tiAllocation.getTypeLedger());
            toolledger.setToolsID(lstDT.get(i).getToolsID());
            toolledger.setDescription(lstDT.get(i).getDescription());
            toolledger.setReason(tiAllocation.getReason());
            toolledger.setUnitPrice(lstDT.get(i).getAllocationAmount());
            toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
            toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger.setIncrementQuantity(lstDT.get(i).getAllocationAmount());
            toolledger.setDecrementQuantity(BigDecimal.ZERO);
            toolledger.setIncrementAmount(tiAllocation.getTotalAmount());
            toolledger.setDecrementAmount(BigDecimal.ZERO);
            toolledger.setAllocationAmount(BigDecimal.ZERO);
            toolledger.setAllocatedAmount(BigDecimal.ZERO);
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger.setToolCode(tool.get().getToolCode());
//                toolledger.setToolName(tool.get().getToolName());
//            }

            listGenTemp.add(toolledger);
        }

        return listGenTemp;
    }

    private List<FixedAssetLedger> genGeneralLedgers(FAIncrement faIncrement) {

        List<FixedAssetLedger> listGenTemp = new ArrayList<>();
        List<FAIncrementDetails> lstDT = new ArrayList<>(faIncrement.getFaIncrementDetails());
        lstDT.sort(Comparator.comparingInt(FAIncrementDetails::getOrderPriority));
        for (int i = 0; i < lstDT.size(); i++) {
            FixedAssetLedger fixedAssetLedger = new FixedAssetLedger();
            fixedAssetLedger.setBranchID(null);
            fixedAssetLedger.setReferenceID(faIncrement.getId());
            fixedAssetLedger.setCompanyID(faIncrement.getCompanyID());
            fixedAssetLedger.setTypeID(faIncrement.getTypeID());
            fixedAssetLedger.setDate(faIncrement.getDate());
            fixedAssetLedger.setNoFBook(faIncrement.getNoFBook());
            fixedAssetLedger.setNoMBook(faIncrement.getNoMBook());
            fixedAssetLedger.setTypeLedger(faIncrement.getTypeLedger());
            FixedAssetDTO fixedAsset = fixedAssetRepository.getById(lstDT.get(i).getFixedAssetID());
            if (fixedAsset != null) {
                fixedAssetLedger.setFixedAssetID(fixedAsset.getId());
                fixedAssetLedger.setFixedAssetCode(fixedAsset.getFixedAssetCode());
                fixedAssetLedger.setFixedAssetName(fixedAsset.getFixedAssetName());
                fixedAssetLedger.setUsedTime(fixedAsset.getUsedTime());
                fixedAssetLedger.setDepreciationRate(fixedAsset.getDepreciationRate());
                fixedAssetLedger.setDepreciationAmount(fixedAsset.getPurchasePrice());
                fixedAssetLedger.setDepreciationAccount(fixedAsset.getDepreciationAccount());
                fixedAssetLedger.setOriginalPrice(fixedAsset.getOriginalPrice());
                fixedAssetLedger.setOriginalPriceAccount(fixedAsset.getOriginalPriceAccount());
                fixedAssetLedger.setUsedTimeRemain(fixedAsset.getUsedTime());
                fixedAssetLedger.setDifferUsedTime(null);
                fixedAssetLedger.setMonthDepreciationRate(fixedAsset.getMonthDepreciationRate());
                fixedAssetLedger.setMonthPeriodDepreciationAmount(fixedAsset.getMonthPeriodDepreciationAmount());
                fixedAssetLedger.setAcDepreciationAmount(BigDecimal.ZERO);
                fixedAssetLedger.setRemainingAmount(fixedAsset.getPurchasePrice());
                fixedAssetLedger.setDifferOrgPrice(null);
                fixedAssetLedger.setDifferAcDepreciationAmount(null);
                fixedAssetLedger.setDifferMonthlyDepreciationAmount(null);
                fixedAssetLedger.setDifferRemainingAmount(null);
                fixedAssetLedger.setDifferDepreciationAmount(null);
            }
            fixedAssetLedger.setDescription(null);
            fixedAssetLedger.setReason(faIncrement.getReason());
            fixedAssetLedger.setDepartmentID(lstDT.get(i).getDepartmentID());

            listGenTemp.add(fixedAssetLedger);
        }
        return listGenTemp;
    }

    private List<Toolledger> genGeneralLedgers(TIIncrement tiIncrement) {

        List<Toolledger> listGenTemp = new ArrayList<>();
        List<TIIncrementDetails> lstDT = new ArrayList<>(tiIncrement.getTiIncrementDetails());
//        Collections.sort(lstDT, Comparator.comparingInt(TIIncrementDetails::getOrderPriority));
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
//        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null) {
//            rSInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
//        }
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Tools> tool = toolsRepository.getToolsById(lstDT.get(i).getToolsID());
            Toolledger toolledger = new Toolledger();
            if (tool.isPresent()) {
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
                toolledger.setToolCode(tool.get().getToolCode());
                toolledger.setToolName(tool.get().getToolName());
                toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
                toolledger.setIncrementAllocationTime(BigDecimal.valueOf(
                    tool.get().getAllocationTimes() != null ? tool.get().getAllocationTimes() : 0));
            }
            toolledger.setBranchID(null);
            toolledger.setReferenceID(tiIncrement.getId());
            toolledger.setCompanyID(tiIncrement.getCompanyID());
            toolledger.setTypeID(tiIncrement.getTypeID());
            toolledger.postedDate(tiIncrement.getDate());
            toolledger.date(tiIncrement.getDate());
            toolledger.noFBook(tiIncrement.getNoFBook());
            toolledger.noMBook(tiIncrement.getNoMBook());
            toolledger.typeLedger(tiIncrement.getTypeLedger());
            toolledger.setToolsID(lstDT.get(i).getToolsID());
            toolledger.setReason(tiIncrement.getReason());
            toolledger.setUnitPrice(lstDT.get(i).getUnitPrice());
            toolledger.setDepartmentID(lstDT.get(i).getDepartmentID());
            toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
            toolledger.setIncrementQuantity(lstDT.get(i).getQuantity());
            toolledger.setDecrementQuantity(BigDecimal.ZERO);
            toolledger.setIncrementAmount(tiIncrement.getTotalAmount());
            toolledger.setDecrementAmount(BigDecimal.ZERO);
            toolledger.setAllocationAmount(BigDecimal.ZERO);
//            Optional<Tools> tool = toolsRepository.findById(lstDT.get(i).getToolsID());
//            if (tool.isPresent()) {
//                toolledger.setToolCode(tool.get().getToolCode());
//                toolledger.setToolName(tool.get().getToolName());
//            }

            listGenTemp.add(toolledger);

        }

        return listGenTemp;
    }

    private List<GeneralLedger> genGeneralLedgersOPMaterialGoods(List<OPMaterialGoods> opMaterialGoodsList) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (OPMaterialGoods opMaterialGoods : opMaterialGoodsList) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                BeanUtils.copyProperties(generalLedger, opMaterialGoods);
                generalLedger.setReferenceID(opMaterialGoods.getId());
                generalLedger.setAccount(opMaterialGoods.getAccountNumber());
                if (opMaterialGoods.getRepositoryId() != null) {
                    generalLedger.setRepositoryID(opMaterialGoods.getRepositoryId());
                    generalLedger.setRepositoryCode(opMaterialGoods.getRepository().getRepositoryCode());
                    generalLedger.setRepositoryName(opMaterialGoods.getRepository().getRepositoryName());
                }
                if (opMaterialGoods.getTypeLedger().equals(0)) {
                    generalLedger.setNoFBook(Constants.GenCodeOpeningBalance.NO_BOOK);
                } else {
                    generalLedger.setNoMBook(Constants.GenCodeOpeningBalance.NO_BOOK);
                }
                generalLedger.setUnitID(opMaterialGoods.getUnitId());
                generalLedger.setMainUnitID(opMaterialGoods.getMainUnitId());
                generalLedger.setMainQuantity(opMaterialGoods.getMainQuantity());
                generalLedger.setMainUnitPrice(opMaterialGoods.getMainUnitPrice());
                generalLedger.setTypeID(opMaterialGoods.getTypeId());
                generalLedger.setDebitAmount(opMaterialGoods.getAmount());
                generalLedger.setDebitAmountOriginal(opMaterialGoods.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.setDate(opMaterialGoods.getPostedDate());
                generalLedger.setPostedDate(opMaterialGoods.getPostedDate());

                generalLedger.setCompanyID(opMaterialGoods.getCompanyId());
                generalLedger.setCurrencyID(opMaterialGoods.getCurrencyId());
                generalLedger.setMaterialGoodsID(opMaterialGoods.getMaterialGoodsId());
                if (opMaterialGoods.getMaterialGoodsId() != null) {
                    generalLedger.setMaterialGoodsID(opMaterialGoods.getMaterialGoodsId());
                    generalLedger.setMaterialGoodsCode(opMaterialGoods.getMaterialGoods().getMaterialGoodsCode());
                    generalLedger.setMaterialGoodsName(opMaterialGoods.getMaterialGoods().getMaterialGoodsName());
                }

                if (opMaterialGoods.getBankAccountDetailId() != null) {
                    generalLedger.setBankAccountDetailID(opMaterialGoods.getBankAccountDetailId());
                    generalLedger.setBankAccount(opMaterialGoods.getBankAccountDetails().getBankAccount());
                    generalLedger.setBankName(opMaterialGoods.getBankAccountDetails().getBankName());
                }
                generalLedger.setDepartmentID(opMaterialGoods.getDepartmentId());
                generalLedger.setExpenseItemID(opMaterialGoods.getExpenseItemId());
                generalLedger.setBudgetItemID(opMaterialGoods.getBudgetItemId());
                generalLedger.setCostSetID(opMaterialGoods.getCostSetId());
                generalLedger.setContractID(opMaterialGoods.getContractId());
                generalLedger.setStatisticsCodeID(opMaterialGoods.getStatisticsCodeId());

                listGenTemp.add(generalLedger);

                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                BeanUtils.copyProperties(repositoryLedgerItem, generalLedger);
                repositoryLedgerItem.setReferenceID(opMaterialGoods.getId());
                repositoryLedgerItem.setCompanyID(opMaterialGoods.getCompanyId());
                repositoryLedgerItem.setIwQuantity(opMaterialGoods.getQuantity());
                repositoryLedgerItem.setIwAmount(opMaterialGoods.getAmount());
                repositoryLedgerItem.setMainIWQuantity(opMaterialGoods.getMainQuantity());
                repositoryLedgerItem.setUnitPrice(opMaterialGoods.getUnitPrice());
//                repositoryLedgerItem.setAccount(aLstDT.getDebitAccount());
//                repositoryLedgerItem.setAccountCorresponding(aLstDT.getCreditAccount());
//                repositoryLedgerItem.setDetailID(aLstDT.getId());
                repositoryLedgerItem.setFormula(opMaterialGoods.getFormula());
                repositoryLedgerItem.setMaterialGoodsID(opMaterialGoods.getMaterialGoodsId());
                repositoryLedgerItem.setRepositoryID(opMaterialGoods.getRepositoryId());
//                repositoryLedgerItem.setDescription(aLstDT.getDescription());
//                repositoryLedgerItem.setReason(rsInwardOutward.getReason());
                repositoryLedgerItem.setExpiryDate(opMaterialGoods.getExpiryDate());
                repositoryLedgerItem.setUnitID(opMaterialGoods.getUnitId());
                repositoryLedgerItem.setMainUnitID(opMaterialGoods.getMainUnitId());
                repositoryLedgerItem.setMainUnitPrice(opMaterialGoods.getMainUnitPrice());
                repositoryLedgerItem.setMainConvertRate(opMaterialGoods.getMainConvertRate());
                repositoryLedgerItem.setBudgetItemID(opMaterialGoods.getBudgetItemId());
                repositoryLedgerItem.setExpenseItemID(opMaterialGoods.getExpenseItemId());
                repositoryLedgerItem.setBudgetItemID(opMaterialGoods.getBudgetItemId());
                repositoryLedgerItem.setCostSetID(opMaterialGoods.getCostSetId());
                repositoryLedgerItem.setStatisticsCodeID(opMaterialGoods.getStatisticsCodeId());
                repositoryLedgerItem.setLotNo(opMaterialGoods.getLotNo());
                // repository

                repositoryLedgers.add(repositoryLedgerItem);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.deleteAllByReferenceID(repositoryLedgers.stream()
            .map(RepositoryLedger::getReferenceID).collect(Collectors.toList()));
        viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        return listGenTemp;
    }

    private List<GeneralLedger> genGeneralLedgersOPAccount(List<OPAccount> opAccountList) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();

        for (OPAccount opAccount : opAccountList) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.setReferenceID(opAccount.getId());
            try {
                BeanUtils.copyProperties(generalLedger, opAccount);
                generalLedger.setAccount(opAccount.getAccountNumber());
                if (opAccount.getTypeLedger().equals(0)) {
                    generalLedger.setNoFBook(Constants.GenCodeOpeningBalance.NO_BOOK);
                } else {
                    generalLedger.setNoMBook(Constants.GenCodeOpeningBalance.NO_BOOK);
                }
                if (opAccount.getAccountingObjectId() != null) {
                    generalLedger.setAccountingObjectID(opAccount.getAccountingObject().getId());
                    generalLedger.setAccountingObjectCode(opAccount.getAccountingObject().getAccountingObjectCode());
                    generalLedger.setAccountingObjectName(opAccount.getAccountingObject().getAccountingObjectName());
                    generalLedger.setAccountingObjectAddress(opAccount.getAccountingObject().getAccountingObjectAddress());
                }
                generalLedger.setExchangeRate(opAccount.getExchangeRate());
                generalLedger.setTypeID(opAccount.getTypeId());
                generalLedger.debitAmount(opAccount.getDebitAmount());
                generalLedger.debitAmountOriginal(opAccount.getDebitAmountOriginal());
                generalLedger.creditAmount(opAccount.getCreditAmount());
                generalLedger.creditAmountOriginal(opAccount.getCreditAmountOriginal());
                generalLedger.setDate(opAccount.getPostedDate());
                generalLedger.setCompanyID(opAccount.getCompanyId());
                generalLedger.setCurrencyID(opAccount.getCurrencyId());
                if (opAccount.getBankAccountDetailId() != null) {
                    generalLedger.setBankAccountDetailID(opAccount.getBankAccountDetailId());
                    generalLedger.setBankAccount(opAccount.getBankAccountDetails().getBankAccount());
                    generalLedger.setBankName(opAccount.getBankAccountDetails().getBankName());
                }
                generalLedger.setDepartmentID(opAccount.getDepartmentId());
                generalLedger.setExpenseItemID(opAccount.getExpenseItemId());
                generalLedger.setBudgetItemID(opAccount.getBudgetItemId());
                generalLedger.setCostSetID(opAccount.getCostSetId());
                generalLedger.setContractID(opAccount.getContractId());
                generalLedger.setStatisticsCodeID(opAccount.getStatisticsCodeId());

                listGenTemp.add(generalLedger);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return listGenTemp;
    }

    private List<GeneralLedger> GenGeneralLedgerGOtherVoucherKc(GOtherVoucher gOtherVoucher) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<GOtherVoucherDetails> lstDT = new ArrayList<>(gOtherVoucher.getgOtherVoucherDetails());
        for (int i = 0; i < lstDT.size(); i++) {
            if (lstDT.get(i).getAmount() == null || lstDT.get(i).getAmount().doubleValue() < 0) {
                return null;
            }
        }
        lstDT.sort(Comparator.comparingInt(GOtherVoucherDetails::getOrderPriority));

        for (GOtherVoucherDetails aLstDT : lstDT) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                // cặp nợ có
                BeanUtils.copyProperties(generalLedger, gOtherVoucher);

                generalLedger.setNoFBook(gOtherVoucher.getNoFBook());
                generalLedger.setNoMBook(gOtherVoucher.getNoMBook());

                generalLedger.setTypeID(gOtherVoucher.getTypeID());
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                generalLedger.debitAmount(aLstDT.getAmount());
                generalLedger.debitAmountOriginal(aLstDT.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());
                generalLedger.setReferenceID(gOtherVoucher.getId());
                generalLedger.detailID(aLstDT.getId());
                generalLedger.refDateTime(gOtherVoucher.getDate());
                generalLedger.setCompanyID(gOtherVoucher.getCompanyID());

                // thành tiền lớn hơn 0 ms lưu
                if (aLstDT.getAmount() != null && aLstDT.getAmount().doubleValue() > 0) {
                    listGenTemp.add(generalLedger);
                }

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.setTypeID(gOtherVoucher.getTypeID());
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                generalLedgerCorresponding.creditAmount(aLstDT.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(aLstDT.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                if (aLstDT.getAmount() != null && aLstDT.getAmount().doubleValue() > 0) {
                    listGenTemp.add(generalLedgerCorresponding);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return listGenTemp;
    }

    List<GeneralLedger> genGeneralLedgersTransfer(RSTransfer rsTransfer) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<RSTransferDetail> lstDT = new ArrayList<>(rsTransfer.getRsTransferDetails());
        lstDT.sort(Comparator.comparingInt(RSTransferDetail::getOrderPriority));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (RSTransferDetail aLstDT : lstDT) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                BeanUtils.copyProperties(generalLedger, rsTransfer);
                generalLedger.setTypeID(rsTransfer.getTypeID());
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                generalLedger.debitAmount(aLstDT.getoWAmount());
                generalLedger.debitAmountOriginal(aLstDT.getoWAmount());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());

                Optional<AccountingObject> accountingObject = accountingObjectRepository.findOneById(rsTransfer.getAccountingObjectID());
                accountingObject.ifPresent(e -> {
                    generalLedger.accountingObjectCode(e.getAccountingObjectCode());
                    generalLedger.accountingObjectName(e.getAccountingObjectName());
                });
                generalLedger.setReferenceID(rsTransfer.getId());
                generalLedger.employeeID(rsTransfer.getEmployeeID());
                Optional<AccountingObject> employee = accountingObjectRepository.findOneById(rsTransfer.getEmployeeID());
                employee.ifPresent(e -> {
                    generalLedger.employeeCode(e.getAccountingObjectCode());
                    generalLedger.employeeName(e.getAccountingObjectName());
                });
                Optional<Repository> repository = repositoryRepository.findById(aLstDT.getToRepositoryID());
                // repository
                if (repository.isPresent()) {
                    generalLedger.setRepositoryCode(repository.get().getRepositoryCode());
                    generalLedger.setRepositoryName(repository.get().getRepositoryName());
                }
                generalLedger.setRepositoryID(aLstDT.getToRepositoryID());
                generalLedger.materialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                generalLedger.materialGoodsCode(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsCode() : null);
                generalLedger.materialGoodsName(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsName() : null);
                generalLedger.unitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                generalLedger.detailID(aLstDT.getId());
                generalLedger.quantity(aLstDT.getQuantity());
                generalLedger.unitPrice(aLstDT.getoWPrice());
                generalLedger.mainUnitPrice(aLstDT.getoWPrice());
                generalLedger.unitPriceOriginal(aLstDT.getoWPrice());
                generalLedger.mainQuantity(aLstDT.getMainQuantity());
                generalLedger.mainConvertRate(aLstDT.getMainConvertRate());
                generalLedger.formula(aLstDT.getFormula());
                generalLedger.departmentID(aLstDT.getDepartment() != null ? aLstDT.getDepartment().getId() : null);
                generalLedger.mainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                generalLedger.orderPriority(aLstDT.getOrderPriority());
                generalLedger.refDateTime(rsTransfer.getDate());
                generalLedger.invoiceDate(rsTransfer.getInvoiceDate());
                generalLedger.setNoFBook(rsTransfer.getNoFBook());
                generalLedger.setNoMBook(rsTransfer.getNoMBook());
                generalLedger.budgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                generalLedger.costSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                generalLedger.statisticsCodeID(aLstDT.getStatisticCode() != null ? aLstDT.getStatisticCode().getId() : null);
                generalLedger.expenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                UserDTO userDTO = userService.getAccount();
                Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    generalLedger.setNoMBook(null);
                    generalLedger.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }

                listGenTemp.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.setTypeID(rsTransfer.getTypeID());
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                generalLedgerCorresponding.creditAmount(aLstDT.getoWAmount());
                generalLedgerCorresponding.creditAmountOriginal(aLstDT.getoWAmount());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                Optional<Repository> repository3 = repositoryRepository.findById(aLstDT.getFromRepositoryID());
                // repository
                if (repository3.isPresent()) {
                    generalLedgerCorresponding.setRepositoryCode(repository3.get().getRepositoryCode());
                    generalLedgerCorresponding.setRepositoryName(repository3.get().getRepositoryName());
                }
                generalLedgerCorresponding.setRepositoryID(aLstDT.getFromRepositoryID());

                listGenTemp.add(generalLedgerCorresponding);

                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                repositoryLedgerItem.setTypeID(rsTransfer.getTypeID());
                repositoryLedgerItem.setReferenceID(rsTransfer.getId());
                repositoryLedgerItem.setCompanyID(rsTransfer.getCompanyID());
                repositoryLedgerItem.setNoFBook(rsTransfer.getNoFBook());
                repositoryLedgerItem.setNoMBook(rsTransfer.getNoMBook());
                repositoryLedgerItem.setOwQuantity(aLstDT.getQuantity());
                repositoryLedgerItem.setOwAmount(aLstDT.getoWAmount());
                repositoryLedgerItem.setMainOWQuantity(aLstDT.getMainQuantity());
                repositoryLedgerItem.setUnitPrice(aLstDT.getUnitPrice());
                repositoryLedgerItem.setAccount(aLstDT.getCreditAccount());
                repositoryLedgerItem.setAccountCorresponding(aLstDT.getDebitAccount());
                repositoryLedgerItem.setDetailID(aLstDT.getId());
                repositoryLedgerItem.setFormula(aLstDT.getFormula());
                repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                repositoryLedgerItem.setLotNo(aLstDT.getLotNo());
                repositoryLedgerItem.setReason(rsTransfer.getReason());
                repositoryLedgerItem.setDate(rsTransfer.getDate());
                repositoryLedgerItem.setPostedDate(rsTransfer.getPostedDate());
                repositoryLedgerItem.setExpiryDate(aLstDT.getExpiryDate());
                repositoryLedgerItem.setTypeLedger(rsTransfer.getTypeLedger());
                repositoryLedgerItem.setUnitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                repositoryLedgerItem.setMainUnitPrice(aLstDT.getMainUnitPrice());
                repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
                repositoryLedgerItem.setBudgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                repositoryLedgerItem.setCostSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                repositoryLedgerItem.setStatisticsCodeID(aLstDT.getStatisticCode() != null ? aLstDT.getStatisticCode().getId() : null);
                repositoryLedgerItem.setExpenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                if (aLstDT.getUnit() == null) {
                    repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice());
                    repositoryLedgerItem.setMainOWQuantity(repositoryLedgerItem.getOwQuantity());
                    repositoryLedgerItem.setMainConvertRate(new BigDecimal(1));
                    repositoryLedgerItem.setFormula("*");
                }
                Optional<Repository> repository1 = repositoryRepository.findById(aLstDT.getFromRepositoryID());
                // repository
                if (repository1.isPresent()) {
                    repositoryLedgerItem.setRepositoryCode(repository1.get().getRepositoryCode());
                    repositoryLedgerItem.setRepositoryName(repository1.get().getRepositoryName());
                }
                repositoryLedgerItem.setRepositoryID(aLstDT.getFromRepositoryID());
                // materialGoods
                Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                if (materialGoods.isPresent()) {
                    repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    repositoryLedgerItem.setNoMBook(null);
                    repositoryLedgerItem.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }
                /*Add fromRepository */
                repositoryLedgers.add(repositoryLedgerItem);

                RepositoryLedger repositoryLedgerItem1 = new RepositoryLedger();
                BeanUtils.copyProperties(repositoryLedgerItem1, repositoryLedgerItem);

                Optional<Repository> repository2 = repositoryRepository.findById(aLstDT.getToRepositoryID());
                // repository
                if (repository2.isPresent()) {
                    repositoryLedgerItem1.setRepositoryCode(repository2.get().getRepositoryCode());
                    repositoryLedgerItem1.setRepositoryName(repository2.get().getRepositoryName());
                }
                repositoryLedgerItem1.setOwQuantity(null);
                repositoryLedgerItem1.setOwAmount(null);
                repositoryLedgerItem1.setIwQuantity(aLstDT.getQuantity());
                repositoryLedgerItem1.setIwAmount(aLstDT.getoWAmount());
                repositoryLedgerItem1.setAccount(aLstDT.getDebitAccount());
                repositoryLedgerItem1.setAccountCorresponding(aLstDT.getCreditAccount());
                repositoryLedgerItem1.setRepositoryID(aLstDT.getToRepositoryID());
                repositoryLedgerItem1.setMainIWQuantity(aLstDT.getMainQuantity());
                repositoryLedgerItem1.setMainOWQuantity(null);

                /*Add fromRepository */
                repositoryLedgers.add(repositoryLedgerItem1);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.saveAll(repositoryLedgers);
        return listGenTemp;
    }



    @Override
    public List<GeneralLedger> fillByRefID(UUID refId) {
        log.debug("Request to unrecord");
        return generalLedgerRepository.findByRefID(refId);
    }

    @Override
    public boolean unrecord(UUID refID, UUID repositoryLedgerID) {
        if (repositoryLedgerID != null) {
            if (!repositoryLedgerRepository.unrecord(repositoryLedgerID)) {
                return false;
            }
        }
        return generalLedgerRepository.unrecord(refID);

    }
    @Override
    public boolean unRecordTools(UUID refID) {
        return toolledgerRepository.unrecord(refID);
    }

    @Override
    public boolean unRecordFixedAsset(UUID refID) {
        return fixedAssetLedgerRepository.unRecord(refID);
    }

    @Override
    public boolean unrecord(List<UUID> refID, Boolean isDeleteRL) {
        try {
            if (isDeleteRL) {
                repositoryLedgerRepository.deleteAllByReferenceID(refID);
            }
            generalLedgerRepository.deleteAllByReferenceID(refID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

//    @Override
//    public boolean unrecord(UUID refID, UUID repositoryLedgerID, UUID paymentVoucherId) {
//        if (repositoryLedgerID != null) {
//            if (!repositoryLedgerRepository.unrecord(repositoryLedgerID) || !generalLedgerRepository.unrecord(repositoryLedgerID)) {
//                return false;
//            }
//        }
//        if (paymentVoucherId != null) {
//            if (!generalLedgerRepository.unrecord(paymentVoucherId)) {
//                return false;
//            }
//        }
//        return generalLedgerRepository.unrecord(refID);
//    }

    List<GeneralLedger> GenGeneralLedgers(PPDiscountReturn ppDiscountReturn) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<PPDiscountReturnDetails> lstDT = new ArrayList<>(ppDiscountReturn.getPpDiscountReturnDetails().stream().
            sorted((a, b) -> a.getOrderPriority() - b.getOrderPriority()).collect(Collectors.toList()));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
        if (ppDiscountReturn.getIsDeliveryVoucher() && ppDiscountReturn.getRsInwardOutwardID() != null) {
            rSInwardOutward = rSInwardOutwardRepository.findById(ppDiscountReturn.getRsInwardOutwardID());
        }
        AccountingObject empl = null;
        if (ppDiscountReturn.getEmployeeID() != null) {
            empl = accountingObjectRepository.findById(ppDiscountReturn.getEmployeeID()).get();
        }
        for (int i = 0; i < lstDT.size(); i++) {
            Optional<Repository> repository = Optional.empty();
            if (lstDT.get(i).getRepositoryID() != null) {
                repository = repositoryRepository.findById(lstDT.get(i).getRepositoryID());
            }
            Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(lstDT.get(i).getMaterialGoodsID());
            if (lstDT.get(i).getAmountOriginal().doubleValue() != 0) {
                Optional<AccountingObject> accountingObject = Optional.empty();
                if (lstDT.get(i).getAccountingObjectID() != null) {
                    accountingObject = accountingObjectRepository.findById(lstDT.get(i).getAccountingObjectID());
                }
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setCompanyID(ppDiscountReturn.getCompanyID());
                if (ppDiscountReturn.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                    generalLedger.setInvoiceDate(ppDiscountReturn.getInvoiceDate());
                    generalLedger.setInvoiceSeries(ppDiscountReturn.getInvoiceSeries());
                    generalLedger.setInvoiceNo(ppDiscountReturn.getInvoiceNo());
                }
//            generalLedger.bankAccountDetailID(ppDiscountReturn.geta());
                generalLedger.setBankAccount(ppDiscountReturn.getAccountingObjectBankAccount());
                generalLedger.setBankName(ppDiscountReturn.getAccountingObjectBankName());
                generalLedger.setBranchID(ppDiscountReturn.getBranchID());
                generalLedger.setDate(ppDiscountReturn.getDate());
                generalLedger.setTypeLedger(ppDiscountReturn.getTypeLedger());
                generalLedger.setReferenceID(ppDiscountReturn.getId());
                generalLedger.setTypeID(ppDiscountReturn.getTypeID());
                generalLedger.postedDate(ppDiscountReturn.getPostedDate());
                generalLedger.noFBook(ppDiscountReturn.getNoFBook());
                generalLedger.noMBook(ppDiscountReturn.getNoMBook());
                generalLedger.account(lstDT.get(i).getDebitAccount());
                generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
                generalLedger.currencyID(ppDiscountReturn.getCurrencyID());
                generalLedger.exchangeRate(ppDiscountReturn.getExchangeRate());
                generalLedger.debitAmount(lstDT.get(i).getAmount() != null ? lstDT.get(i).getAmount() : BigDecimal.ZERO);
                generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal() != null ? lstDT.get(i).getAmountOriginal() : BigDecimal.ZERO);
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(ppDiscountReturn.getReason());
                generalLedger.description(lstDT.get(i).getDescription());
                generalLedger.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                if (accountingObject.isPresent()) {
                    generalLedger.accountingObjectName(accountingObject.get().getAccountingObjectName());
                    generalLedger.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                    generalLedger.contactName(accountingObject.get().getContactName());
                    generalLedger.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                }
                // repository
                generalLedger.setRepositoryID(lstDT.get(i).getRepositoryID());
                if (repository.isPresent()) {
                    generalLedger.setRepositoryCode(repository.get().getRepositoryCode());
                    generalLedger.setRepositoryName(repository.get().getRepositoryName());
                }
                generalLedger.employeeID(ppDiscountReturn.getEmployeeID());
                if (empl != null) {
                    generalLedger.employeeCode(empl.getAccountingObjectCode());
                    generalLedger.employeeName(empl.getAccountingObjectName());
                }
                generalLedger.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                if (materialGoods.isPresent()) {
                    generalLedger.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    generalLedger.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                generalLedger.budgetItemID(lstDT.get(i).getBudgetItemID());
                generalLedger.costSetID(lstDT.get(i).getCostSetID());
                generalLedger.contractID(lstDT.get(i).getContractID());
                generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
                generalLedger.detailID(lstDT.get(i).getId());
                generalLedger.refDateTime(ppDiscountReturn.getDate());
                generalLedger.expenseItemID(lstDT.get(i).getExpenseItemID());
                generalLedger.setQuantity(lstDT.get(i).getQuantity());
                generalLedger.setUnitID(lstDT.get(i).getUnitID());
                generalLedger.setUnitPriceOriginal(lstDT.get(i).getUnitPriceOriginal());
                generalLedger.setUnitPrice(lstDT.get(i).getUnitPrice());
                generalLedger.mainUnitID(lstDT.get(i).getMainUnitID());
                generalLedger.mainQuantity(lstDT.get(i).getMainQuantity());
                generalLedger.mainUnitPrice(lstDT.get(i).getMainUnitPrice());
                generalLedger.mainConvertRate(lstDT.get(i).getMainConvertRate());
                generalLedger.formula(lstDT.get(i).getFormula());
                generalLedger.departmentID(lstDT.get(i).getDepartmentID());
                generalLedger.orderPriority(lstDT.get(i).getOrderPriority());
                listGenTemp.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.setCompanyID(ppDiscountReturn.getCompanyID());
                if (ppDiscountReturn.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                    generalLedgerCorresponding.setInvoiceDate(ppDiscountReturn.getInvoiceDate());
                    generalLedgerCorresponding.setInvoiceSeries(ppDiscountReturn.getInvoiceSeries());
                    generalLedgerCorresponding.setInvoiceNo(ppDiscountReturn.getInvoiceNo());
                }
                generalLedgerCorresponding.setBankAccount(ppDiscountReturn.getAccountingObjectBankAccount());
                generalLedgerCorresponding.setBankName(ppDiscountReturn.getAccountingObjectBankName());
                generalLedgerCorresponding.setDate(ppDiscountReturn.getDate());
                generalLedgerCorresponding.setTypeLedger(ppDiscountReturn.getTypeLedger());
                generalLedgerCorresponding.setBranchID(ppDiscountReturn.getBranchID());
                generalLedgerCorresponding.setReferenceID(ppDiscountReturn.getId());
                generalLedgerCorresponding.setTypeID(ppDiscountReturn.getTypeID());
                generalLedgerCorresponding.postedDate(ppDiscountReturn.getPostedDate());
                generalLedgerCorresponding.noFBook(ppDiscountReturn.getNoFBook());
                generalLedgerCorresponding.noMBook(ppDiscountReturn.getNoMBook());
                generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
                generalLedgerCorresponding.currencyID(ppDiscountReturn.getCurrencyID());
                generalLedgerCorresponding.exchangeRate(ppDiscountReturn.getExchangeRate());
                generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount() != null ? lstDT.get(i).getAmount() : BigDecimal.ZERO);
                generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal() != null ? lstDT.get(i).getAmountOriginal() : BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.reason(ppDiscountReturn.getReason());
                generalLedgerCorresponding.description(lstDT.get(i).getDescription());
                generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                generalLedgerCorresponding.employeeID(ppDiscountReturn.getEmployeeID());
                if (empl != null) {
                    generalLedgerCorresponding.employeeCode(empl.getAccountingObjectCode());
                    generalLedgerCorresponding.employeeName(empl.getAccountingObjectName());
                }
                generalLedgerCorresponding.setRepositoryID(lstDT.get(i).getRepositoryID());
                if (repository.isPresent()) {
                    generalLedgerCorresponding.setRepositoryCode(repository.get().getRepositoryCode());
                    generalLedgerCorresponding.setRepositoryName(repository.get().getRepositoryName());
                }
                generalLedgerCorresponding.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                if (materialGoods.isPresent()) {
                    generalLedgerCorresponding.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    generalLedgerCorresponding.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                generalLedgerCorresponding.mainUnitID(lstDT.get(i).getMainUnitID());
                generalLedgerCorresponding.mainQuantity(lstDT.get(i).getMainQuantity());
                generalLedgerCorresponding.mainUnitPrice(lstDT.get(i).getMainUnitPrice());
                generalLedgerCorresponding.mainConvertRate(lstDT.get(i).getMainConvertRate());
                generalLedgerCorresponding.formula(lstDT.get(i).getFormula());
                generalLedgerCorresponding.departmentID(lstDT.get(i).getDepartmentID());
                generalLedgerCorresponding.orderPriority(lstDT.get(i).getOrderPriority());
                generalLedgerCorresponding.setUnitPriceOriginal(lstDT.get(i).getUnitPriceOriginal());
                generalLedgerCorresponding.setUnitPrice(lstDT.get(i).getUnitPrice());
                generalLedgerCorresponding.setQuantity(lstDT.get(i).getQuantity());
                generalLedgerCorresponding.setUnitID(lstDT.get(i).getUnitID());
                generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItemID());
                generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSetID());
                generalLedgerCorresponding.contractID(lstDT.get(i).getContractID());
                generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
//            generalLedgerCorresponding.contactName(ppDiscountReturn.getPayers());
                generalLedgerCorresponding.detailID(lstDT.get(i).getId());
                generalLedgerCorresponding.refDateTime(ppDiscountReturn.getDate());
//            generalLedgerCorresponding.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
                generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItemID());
                generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                if (accountingObject.isPresent()) {
                    generalLedgerCorresponding.accountingObjectName(accountingObject.get().getAccountingObjectName());
                    generalLedgerCorresponding.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                    generalLedgerCorresponding.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                    generalLedgerCorresponding.contactName(accountingObject.get().getContactName());
                }
                listGenTemp.add(generalLedgerCorresponding);
                // lưu gl thuế gtgt
                if (lstDT.get(i).getVatAccount() != null && lstDT.get(i).getDeductionDebitAccount() != null && lstDT.get(i).getVatAmountOriginal() != null && !BigDecimal.ZERO.equals(lstDT.get(i).getVatAmountOriginal())) {
                    GeneralLedger generalLedgerCorrespondingVAT = new GeneralLedger();
                    generalLedgerCorrespondingVAT.setCompanyID(ppDiscountReturn.getCompanyID());
                    if (ppDiscountReturn.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                        generalLedgerCorrespondingVAT.setInvoiceDate(ppDiscountReturn.getInvoiceDate());
                        generalLedgerCorrespondingVAT.setInvoiceSeries(ppDiscountReturn.getInvoiceSeries());
                        generalLedgerCorrespondingVAT.setInvoiceNo(ppDiscountReturn.getInvoiceNo());
                    }
                    generalLedgerCorrespondingVAT.setBankAccount(ppDiscountReturn.getAccountingObjectBankAccount());
                    generalLedgerCorrespondingVAT.setBankName(ppDiscountReturn.getAccountingObjectBankName());
                    generalLedgerCorrespondingVAT.setDate(ppDiscountReturn.getDate());
                    generalLedgerCorrespondingVAT.setTypeLedger(ppDiscountReturn.getTypeLedger());
                    generalLedgerCorrespondingVAT.setBranchID(ppDiscountReturn.getBranchID());
                    generalLedgerCorrespondingVAT.setReferenceID(ppDiscountReturn.getId());
                    generalLedgerCorrespondingVAT.setTypeID(ppDiscountReturn.getTypeID());
                    generalLedgerCorrespondingVAT.postedDate(ppDiscountReturn.getPostedDate());
                    generalLedgerCorrespondingVAT.noFBook(ppDiscountReturn.getNoFBook());
                    generalLedgerCorrespondingVAT.noMBook(ppDiscountReturn.getNoMBook());
                    generalLedgerCorrespondingVAT.account(lstDT.get(i).getDeductionDebitAccount());
                    generalLedgerCorrespondingVAT.accountCorresponding(lstDT.get(i).getVatAccount());
//            generalLedgerCorrespondingVAT.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
                    generalLedgerCorrespondingVAT.currencyID(ppDiscountReturn.getCurrencyID());
                    generalLedgerCorrespondingVAT.exchangeRate(ppDiscountReturn.getExchangeRate());
                    generalLedgerCorrespondingVAT.creditAmount(BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.debitAmount(lstDT.get(i).getVatAmount() != null ? lstDT.get(i).getVatAmount() : BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.debitAmountOriginal(lstDT.get(i).getVatAmountOriginal() != null ? lstDT.get(i).getVatAmountOriginal() : BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.reason(ppDiscountReturn.getReason());
                    generalLedgerCorrespondingVAT.description(lstDT.get(i).getVatDescription());
                    generalLedgerCorrespondingVAT.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                    generalLedgerCorrespondingVAT.employeeID(ppDiscountReturn.getEmployeeID());
                    if (empl != null) {
                        generalLedgerCorrespondingVAT.employeeCode(empl.getAccountingObjectCode());
                        generalLedgerCorrespondingVAT.employeeName(empl.getAccountingObjectName());
                    }
                    generalLedgerCorrespondingVAT.setRepositoryID(lstDT.get(i).getRepositoryID());
                    if (repository.isPresent()) {
                        generalLedgerCorrespondingVAT.setRepositoryCode(repository.get().getRepositoryCode());
                        generalLedgerCorrespondingVAT.setRepositoryName(repository.get().getRepositoryName());
                    }
                    generalLedgerCorrespondingVAT.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        generalLedgerCorrespondingVAT.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        generalLedgerCorrespondingVAT.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    generalLedgerCorrespondingVAT.mainUnitID(lstDT.get(i).getMainUnitID());
                    generalLedgerCorrespondingVAT.mainQuantity(lstDT.get(i).getMainQuantity());
                    generalLedgerCorrespondingVAT.mainUnitPrice(lstDT.get(i).getMainUnitPrice());
                    generalLedgerCorrespondingVAT.mainConvertRate(lstDT.get(i).getMainConvertRate());
                    generalLedgerCorrespondingVAT.formula(lstDT.get(i).getFormula());
                    generalLedgerCorrespondingVAT.departmentID(lstDT.get(i).getDepartmentID());
                    generalLedgerCorrespondingVAT.orderPriority(lstDT.get(i).getOrderPriority());
                    generalLedgerCorrespondingVAT.setUnitPriceOriginal(lstDT.get(i).getUnitPriceOriginal());
                    generalLedgerCorrespondingVAT.setUnitPrice(lstDT.get(i).getUnitPrice());
                    generalLedgerCorrespondingVAT.setQuantity(lstDT.get(i).getQuantity());
                    generalLedgerCorrespondingVAT.setUnitID(lstDT.get(i).getUnitID());
                    generalLedgerCorrespondingVAT.budgetItemID(lstDT.get(i).getBudgetItemID());
                    generalLedgerCorrespondingVAT.costSetID(lstDT.get(i).getCostSetID());
                    generalLedgerCorrespondingVAT.contractID(lstDT.get(i).getContractID());
                    generalLedgerCorrespondingVAT.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
//            generalLedgerCorrespondingVAT.contactName(ppDiscountReturn.getPayers());
                    generalLedgerCorrespondingVAT.detailID(lstDT.get(i).getId());
                    generalLedgerCorrespondingVAT.refDateTime(ppDiscountReturn.getDate());
//            generalLedgerCorrespondingVAT.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
                    generalLedgerCorrespondingVAT.expenseItemID(lstDT.get(i).getExpenseItemID());
                    generalLedgerCorrespondingVAT.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                    if (accountingObject.isPresent()) {
                        generalLedgerCorrespondingVAT.accountingObjectName(accountingObject.get().getAccountingObjectName());
                        generalLedgerCorrespondingVAT.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                        generalLedgerCorrespondingVAT.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                        generalLedgerCorrespondingVAT.contactName(accountingObject.get().getContactName());
                    }
                    listGenTemp.add(generalLedgerCorrespondingVAT);
                    GeneralLedger generalLedgerVAT = new GeneralLedger();
                    generalLedgerVAT.setCompanyID(ppDiscountReturn.getCompanyID());
                    if (ppDiscountReturn.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                        generalLedgerVAT.setInvoiceDate(ppDiscountReturn.getInvoiceDate());
                        generalLedgerVAT.setInvoiceSeries(ppDiscountReturn.getInvoiceSeries());
                        generalLedgerVAT.setInvoiceNo(ppDiscountReturn.getInvoiceNo());
                    }
                    generalLedgerVAT.setBankAccount(ppDiscountReturn.getAccountingObjectBankAccount());
                    generalLedgerVAT.setBankName(ppDiscountReturn.getAccountingObjectBankName());
                    generalLedgerVAT.setDate(ppDiscountReturn.getDate());
                    generalLedgerVAT.setTypeLedger(ppDiscountReturn.getTypeLedger());
                    generalLedgerVAT.setBranchID(ppDiscountReturn.getBranchID());
                    generalLedgerVAT.setReferenceID(ppDiscountReturn.getId());
                    generalLedgerVAT.setTypeID(ppDiscountReturn.getTypeID());
                    generalLedgerVAT.postedDate(ppDiscountReturn.getPostedDate());
                    generalLedgerVAT.noFBook(ppDiscountReturn.getNoFBook());
                    generalLedgerVAT.noMBook(ppDiscountReturn.getNoMBook());
                    generalLedgerVAT.account(lstDT.get(i).getVatAccount());
                    generalLedgerVAT.accountCorresponding(lstDT.get(i).getDeductionDebitAccount());
//            generalLedgerVAT.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
                    generalLedgerVAT.currencyID(ppDiscountReturn.getCurrencyID());
                    generalLedgerVAT.exchangeRate(ppDiscountReturn.getExchangeRate());
                    generalLedgerVAT.creditAmount(lstDT.get(i).getVatAmount() != null ? lstDT.get(i).getVatAmount() : BigDecimal.ZERO);
                    generalLedgerVAT.creditAmountOriginal(lstDT.get(i).getVatAmountOriginal() != null ? lstDT.get(i).getVatAmountOriginal() : BigDecimal.ZERO);
                    generalLedgerVAT.debitAmount(BigDecimal.ZERO);
                    generalLedgerVAT.debitAmountOriginal(BigDecimal.ZERO);
                    generalLedgerVAT.reason(ppDiscountReturn.getReason());
                    generalLedgerVAT.description(lstDT.get(i).getVatDescription());
                    generalLedgerVAT.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                    generalLedgerVAT.employeeID(ppDiscountReturn.getEmployeeID());
                    if (empl != null) {
                        generalLedgerVAT.employeeCode(empl.getAccountingObjectCode());
                        generalLedgerVAT.employeeName(empl.getAccountingObjectName());
                    }
                    generalLedgerVAT.setRepositoryID(lstDT.get(i).getRepositoryID());
                    if (repository.isPresent()) {
                        generalLedgerVAT.setRepositoryCode(repository.get().getRepositoryCode());
                        generalLedgerVAT.setRepositoryName(repository.get().getRepositoryName());
                    }
                    generalLedgerVAT.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        generalLedgerVAT.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        generalLedgerVAT.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    generalLedgerVAT.mainUnitID(lstDT.get(i).getMainUnitID());
                    generalLedgerVAT.mainQuantity(lstDT.get(i).getMainQuantity());
                    generalLedgerVAT.mainUnitPrice(lstDT.get(i).getMainUnitPrice());
                    generalLedgerVAT.mainConvertRate(lstDT.get(i).getMainConvertRate());
                    generalLedgerVAT.formula(lstDT.get(i).getFormula());
                    generalLedgerVAT.departmentID(lstDT.get(i).getDepartmentID());
                    generalLedgerVAT.orderPriority(lstDT.get(i).getOrderPriority());
                    generalLedgerVAT.setUnitPriceOriginal(lstDT.get(i).getUnitPriceOriginal());
                    generalLedgerVAT.setUnitPrice(lstDT.get(i).getUnitPrice());
                    generalLedgerVAT.setQuantity(lstDT.get(i).getQuantity());
                    generalLedgerVAT.setUnitID(lstDT.get(i).getUnitID());
                    generalLedgerVAT.budgetItemID(lstDT.get(i).getBudgetItemID());
                    generalLedgerVAT.costSetID(lstDT.get(i).getCostSetID());
                    generalLedgerVAT.contractID(lstDT.get(i).getContractID());
                    generalLedgerVAT.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
//            generalLedgerVAT.contactName(ppDiscountReturn.getPayers());
                    generalLedgerVAT.detailID(lstDT.get(i).getId());
                    generalLedgerVAT.refDateTime(ppDiscountReturn.getDate());
//            generalLedgerVAT.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
                    generalLedgerVAT.expenseItemID(lstDT.get(i).getExpenseItemID());
                    generalLedgerVAT.accountingObjectID(lstDT.get(i).getAccountingObjectID());
                    if (accountingObject.isPresent()) {
                        generalLedgerVAT.accountingObjectName(accountingObject.get().getAccountingObjectName());
                        generalLedgerVAT.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                        generalLedgerVAT.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                        generalLedgerVAT.contactName(accountingObject.get().getContactName());
                    }
                    listGenTemp.add(generalLedgerVAT);
                }
            }
            if (rSInwardOutward.isPresent() && ppDiscountReturn.getIsDeliveryVoucher() && ppDiscountReturn.getRsInwardOutwardID() != null) {
                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                repositoryLedgerItem.setReferenceID(rSInwardOutward.get().getId());
                repositoryLedgerItem.setCompanyID(ppDiscountReturn.getCompanyID());
                repositoryLedgerItem.setTypeID(KIEM_XUAT_KHO);
                if (rSInwardOutward.get().getNoFBook() != null) {
                    repositoryLedgerItem.setNoFBook(rSInwardOutward.get().getNoFBook());
                }
                if (rSInwardOutward.get().getNoMBook() != null) {
                    repositoryLedgerItem.setNoMBook(rSInwardOutward.get().getNoMBook());
                }
                repositoryLedgerItem.setOwQuantity(lstDT.get(i).getQuantity());
                repositoryLedgerItem.setUnitPrice(lstDT.get(i).getUnitPrice());
                repositoryLedgerItem.setOwAmount(lstDT.get(i).getAmount());
                repositoryLedgerItem.setAccount(lstDT.get(i).getCreditAccount());
                repositoryLedgerItem.setAccountCorresponding(lstDT.get(i).getDebitAccount());
                repositoryLedgerItem.setDetailID(lstDT.get(i).getId());
                repositoryLedgerItem.setFormula(lstDT.get(i).getFormula());
                repositoryLedgerItem.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                repositoryLedgerItem.setRepositoryID(lstDT.get(i).getRepositoryID());
                repositoryLedgerItem.setLotNo(lstDT.get(i).getLotNo());
                repositoryLedgerItem.setDescription(lstDT.get(i).getDescription());
                repositoryLedgerItem.setReason(rSInwardOutward.get().getReason());
                repositoryLedgerItem.setDate(rSInwardOutward.get().getDate());
                repositoryLedgerItem.setPostedDate(rSInwardOutward.get().getPostedDate());
                repositoryLedgerItem.setExpiryDate(lstDT.get(i).getExpiryDate());
                repositoryLedgerItem.setTypeLedger(rSInwardOutward.get().getTypeLedger());
                repositoryLedgerItem.setUnitID(lstDT.get(i).getUnitID());
                repositoryLedgerItem.setMainUnitID(lstDT.get(i).getMainUnitID());
                repositoryLedgerItem.setMainUnitPrice(lstDT.get(i).getMainUnitPrice());
                repositoryLedgerItem.setMainOWQuantity(lstDT.get(i).getMainQuantity());
                repositoryLedgerItem.setMainConvertRate(lstDT.get(i).getMainConvertRate());
                repositoryLedgerItem.setBudgetItemID(lstDT.get(i).getBudgetItemID());
                repositoryLedgerItem.setCostSetID(lstDT.get(i).getCostSetID());
                repositoryLedgerItem.setStatisticsCodeID(lstDT.get(i).getStatisticsCodeID());
                repositoryLedgerItem.setExpenseItemID(lstDT.get(i).getExpenseItemID());
                // repository
                if (repository.isPresent()) {
                    repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                    repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                }
                // materialGoods
                repositoryLedgerItem.setMaterialGoodsID(lstDT.get(i).getMaterialGoodsID());
                if (materialGoods.isPresent()) {
                    repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                repositoryLedgers.add(repositoryLedgerItem);
                Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                rsInwardOutward = rSInwardOutwardRepository.findById(rSInwardOutward.get().getId());
                if (rsInwardOutward.isPresent()) {
                    rsInwardOutward.get().setRecorded(true);
                    rSInwardOutwardRepository.save(rsInwardOutward.get());
                }
            }
        }
        if (repositoryLedgers != null && repositoryLedgers.size() > 0) {
            repositoryLedgerRepository.saveAll(repositoryLedgers);
        }
        return listGenTemp;
    }

    // region Gen GL Mcreceipt
    List<GeneralLedger> GenGeneralLedgers(MCReceipt mcReceipt) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<MCReceiptDetails> lstDT = new ArrayList<>(mcReceipt.getMCReceiptDetails());
//        lstDT.sort(Comparator.comparing(MCReceiptDetails::getOrderPriority));
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.setCompanyID(mcReceipt.getCompanyID());
            generalLedger.setReferenceID(mcReceipt.getId());
            generalLedger.setTypeID(mcReceipt.getTypeID());
            generalLedger.date(mcReceipt.getDate());
            generalLedger.postedDate(mcReceipt.getPostedDate());
            generalLedger.noFBook(mcReceipt.getNoFBook());
            generalLedger.noMBook(mcReceipt.getNoMBook());
            generalLedger.typeLedger(mcReceipt.getTypeLedger());
            generalLedger.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedger.accountingObjectCode(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectCode());
            generalLedger.accountingObjectName(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectName());
            generalLedger.accountingObjectAddress(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectAddress());
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
            generalLedger.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedger.bankAccount(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankAccount());
            generalLedger.bankName(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankName());
            generalLedger.currencyID(mcReceipt.getCurrencyID());
            generalLedger.exchangeRate(mcReceipt.getExchangeRate());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(mcReceipt.getReason());
            generalLedger.description(lstDT.get(i).getDescription());
            generalLedger.employeeID(mcReceipt.getEmployeeID());
            AccountingObject empl = null;
            if (mcReceipt.getEmployeeID() != null) {
                empl = accountingObjectRepository.findById(mcReceipt.getEmployeeID()).get();
                generalLedger.employeeCode(empl.getAccountingObjectCode());
                generalLedger.employeeName(empl.getAccountingObjectName());
            }
            generalLedger.budgetItemID(lstDT.get(i).getBudgetItem() == null ? null : lstDT.get(i).getBudgetItem().getId());
            generalLedger.costSetID(lstDT.get(i).getCostSet() == null ? null : lstDT.get(i).getCostSet().getId());
            generalLedger.contractID(lstDT.get(i).getContractID());
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCode() == null ? null : lstDT.get(i).getStatisticsCode().getId());
            generalLedger.contactName(mcReceipt.getPayers());
            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(mcReceipt.getDate());
            generalLedger.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItem() == null ? null : lstDT.get(i).getExpenseItem().getId());
            listGenTemp.add(generalLedger);

            /*if (!(string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
                (((lstDT[i].CreditAccount.StartsWith("133"))
                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
                genTemp.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTemp.AccountCorresponding) ?
                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTemp.AccountCorresponding).Description:null;
            }*/

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.setCompanyID(mcReceipt.getCompanyID());
            generalLedgerCorresponding.setReferenceID(mcReceipt.getId());
            generalLedgerCorresponding.setTypeID(mcReceipt.getTypeID());
            generalLedgerCorresponding.date(mcReceipt.getDate());
            generalLedgerCorresponding.postedDate(mcReceipt.getPostedDate());
            generalLedgerCorresponding.noFBook(mcReceipt.getNoFBook());
            generalLedgerCorresponding.noMBook(mcReceipt.getNoMBook());
            generalLedgerCorresponding.typeLedger(mcReceipt.getTypeLedger());
            generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedgerCorresponding.accountingObjectCode(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectCode());
            generalLedgerCorresponding.accountingObjectName(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectName());
            generalLedgerCorresponding.accountingObjectAddress(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectAddress());
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.bankAccount(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankAccount());
            generalLedgerCorresponding.bankName(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankName());
            generalLedgerCorresponding.currencyID(mcReceipt.getCurrencyID());
            generalLedgerCorresponding.exchangeRate(mcReceipt.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
            generalLedgerCorresponding.reason(mcReceipt.getReason());
            generalLedgerCorresponding.description(lstDT.get(i).getDescription());
            generalLedgerCorresponding.employeeID(mcReceipt.getEmployeeID());
            if (empl != null) {
                generalLedgerCorresponding.employeeCode(empl.getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(empl.getAccountingObjectName());
            }
            generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItem() == null ? null : lstDT.get(i).getBudgetItem().getId());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSet() == null ? null : lstDT.get(i).getCostSet().getId());
            generalLedgerCorresponding.contractID(lstDT.get(i).getContractID());
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCode() == null ? null : lstDT.get(i).getStatisticsCode().getId());
            generalLedgerCorresponding.contactName(mcReceipt.getPayers());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(mcReceipt.getDate());
            generalLedgerCorresponding.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItem() == null ? null : lstDT.get(i).getExpenseItem().getId());
            listGenTemp.add(generalLedgerCorresponding);

//            if ((!string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTempCorresponding.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTempCorresponding.Account) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTempCorresponding.Account).
//                Description:
//                null;
//            }
        }
        return listGenTemp;
    }

    //endregion
    // region Gen GL Mcpayment
    List<GeneralLedger> GenGeneralLedgers(MCPayment mcPayment) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<MCPaymentDetails> lstDT = new ArrayList<>(mcPayment.getMCPaymentDetails());
//        lstDT.sort(Comparator.comparing(MCPaymentDetails::getOrderPriority));
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.setCompanyID(mcPayment.getCompanyID());
            generalLedger.setReferenceID(mcPayment.getId());
            generalLedger.setTypeID(mcPayment.getTypeID());
            generalLedger.date(mcPayment.getDate());
            generalLedger.postedDate(mcPayment.getPostedDate());
            generalLedger.noFBook(mcPayment.getNoFBook());
            generalLedger.noMBook(mcPayment.getNoMBook());
            generalLedger.typeLedger(mcPayment.getTypeLedger());
            generalLedger.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedger.accountingObjectCode(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectCode());
            generalLedger.accountingObjectName(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectName());
            generalLedger.accountingObjectAddress(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectAddress());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
            generalLedger.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedger.bankAccount(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankAccount());
            generalLedger.bankName(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankName());
            generalLedger.currencyID(mcPayment.getCurrencyID());
            generalLedger.exchangeRate(mcPayment.getExchangeRate());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(mcPayment.getReason());
            generalLedger.description(lstDT.get(i).getDescription());
            generalLedger.employeeID(mcPayment.getEmployeeID());
            AccountingObject empl = null;
            if (mcPayment.getEmployeeID() != null) {
                empl = accountingObjectRepository.findById(mcPayment.getEmployeeID()).get();
                generalLedger.employeeCode(empl.getAccountingObjectCode());
                generalLedger.employeeName(empl.getAccountingObjectName());
            }
            generalLedger.budgetItemID(lstDT.get(i).getBudgetItem() == null ? null : lstDT.get(i).getBudgetItem().getId());
            generalLedger.costSetID(lstDT.get(i).getCostSet() == null ? null : lstDT.get(i).getCostSet().getId());
            generalLedger.contractID(lstDT.get(i).getContractID());
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCode() == null ? null : lstDT.get(i).getStatisticsCode().getId());
            generalLedger.contactName(mcPayment.getReceiver());
            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(mcPayment.getDate());
            generalLedger.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItem() == null ? null : lstDT.get(i).getExpenseItem().getId());
            listGenTemp.add(generalLedger);

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.setCompanyID(mcPayment.getCompanyID());
            generalLedgerCorresponding.setReferenceID(mcPayment.getId());
            generalLedgerCorresponding.setTypeID(mcPayment.getTypeID());
            generalLedgerCorresponding.date(mcPayment.getDate());
            generalLedgerCorresponding.postedDate(mcPayment.getPostedDate());
            generalLedgerCorresponding.noFBook(mcPayment.getNoFBook());
            generalLedgerCorresponding.noMBook(mcPayment.getNoMBook());
            generalLedgerCorresponding.typeLedger(mcPayment.getTypeLedger());
            generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedgerCorresponding.accountingObjectCode(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectCode());
            generalLedgerCorresponding.accountingObjectName(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectName());
            generalLedgerCorresponding.accountingObjectAddress(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getAccountingObjectAddress());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.bankAccount(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankAccount());
            generalLedgerCorresponding.bankName(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getBankName());
            generalLedgerCorresponding.currencyID(mcPayment.getCurrencyID());
            generalLedgerCorresponding.exchangeRate(mcPayment.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
            generalLedgerCorresponding.reason(mcPayment.getReason());
            generalLedgerCorresponding.description(lstDT.get(i).getDescription());
            generalLedgerCorresponding.employeeID(mcPayment.getEmployeeID());
            if (empl != null) {
                generalLedgerCorresponding.employeeCode(empl.getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(empl.getAccountingObjectName());
            }
            generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItem() == null ? null : lstDT.get(i).getBudgetItem().getId());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSet() == null ? null : lstDT.get(i).getCostSet().getId());
            generalLedgerCorresponding.contractID(lstDT.get(i).getContractID());
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCode() == null ? null : lstDT.get(i).getStatisticsCode().getId());
            generalLedgerCorresponding.contactName(mcPayment.getReceiver());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(mcPayment.getDate());
            generalLedgerCorresponding.departmentID(lstDT.get(i).getOrganizationUnit() == null ? null : lstDT.get(i).getOrganizationUnit().getId());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItem() == null ? null : lstDT.get(i).getExpenseItem().getId());
            listGenTemp.add(generalLedgerCorresponding);
        }
        return listGenTemp;
    }
    //endregion

    // region Gen GL MBDeposit
    List<GeneralLedger> GenGeneralLedgers(MBDeposit mbDeposit) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<MBDepositDetails> lstDT = new ArrayList<>(mbDeposit.getmBDepositDetails());
//        lstDT.sort(Comparator.comparing(MBDepositDetails::getOrderPriority)); // sắp xếp Hautv commment
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.branchID(null);
            generalLedger.setReferenceID(mbDeposit.getId());
            generalLedger.setCompanyID(mbDeposit.getCompanyID());
            generalLedger.setTypeID(mbDeposit.getTypeID());
            generalLedger.postedDate(mbDeposit.getPostedDate());
            generalLedger.date(mbDeposit.getDate());
            generalLedger.noFBook(mbDeposit.getNoFBook());
            generalLedger.noMBook(mbDeposit.getNoMBook());
            generalLedger.typeLedger(mbDeposit.getTypeLedger());
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
//            generalLedger.bankAccountDetailID(lstDT.get);
            generalLedger.currencyID(mbDeposit.getCurrencyID());
            generalLedger.exchangeRate(mbDeposit.getExchangeRate());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.contractID(lstDT.get(i).getContractID());
            if (mbDeposit.getBankAccountDetailID() != null) {
                generalLedger.bankAccountDetailID(mbDeposit.getBankAccountDetailID());
                Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(mbDeposit.getBankAccountDetailID());
                if (bankAccountDetails.isPresent()) {
                    generalLedger.bankAccount(bankAccountDetails.get().getBankAccount());
                    generalLedger.bankName(bankAccountDetails.get().getBankName());
                } else {
                    Optional<CreditCard> creditCard = creditCardRepository.findById(mbDeposit.getBankAccountDetailID());
                    if (creditCard.isPresent()) {
                        generalLedger.bankAccount(creditCard.get().getCreditCardNumber());
                        if (creditCard.get().getBankIDIssueCard() != null) {
                            Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                            if(bank.isPresent()){
                                generalLedger.bankName(bank.get().getBankName());
                            }
                        }
                    }
                }
            }
            if (lstDT.get(i).getAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstDT.get(i).getAccountingObjectID());
                generalLedger.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedger.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedger.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                generalLedger.accountingObjectID(lstDT.get(i).getAccountingObjectID());
            }
            if (mbDeposit.getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(mbDeposit.getEmployeeID());
                generalLedger.employeeCode(employee.get().getAccountingObjectCode());
                generalLedger.employeeName(employee.get().getAccountingObjectName());
            }

//            if (lstDT.get(i).getAmountOriginal() != null)
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(mbDeposit.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedger.description(lstDT.get(i).getDescription());
//            generalLedger.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedger.employeeID(mbDeposit.getEmployeeID());
            generalLedger.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedger.costSetID(lstDT.get(i).getCostSetID());
//            generalLedger.contractID(lstDT.get(i).getContractState().getId() == null ? null : lstDT.get(i).getContractState().getId() ); comment by namnh
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
//            generalLedger.contactName(mbDeposit.pa());
            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(mbDeposit.getPostedDate());
            generalLedger.departmentID(lstDT.get(i).getDepartmentID());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItemID());
            listGenTemp.add(generalLedger);

//            if (!(string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTemp.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTemp.AccountCorresponding) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTemp.AccountCorresponding).
//                Description:
//                null;
//            }

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.branchID(null);
            generalLedgerCorresponding.setReferenceID(mbDeposit.getId());
            generalLedgerCorresponding.setCompanyID(mbDeposit.getCompanyID());
            generalLedgerCorresponding.setTypeID(mbDeposit.getTypeID());
            generalLedgerCorresponding.postedDate(mbDeposit.getPostedDate());
            generalLedgerCorresponding.date(mbDeposit.getDate());
            generalLedgerCorresponding.noFBook(mbDeposit.getNoFBook());
            generalLedgerCorresponding.noMBook(mbDeposit.getNoMBook());
            generalLedgerCorresponding.typeLedger(mbDeposit.getTypeLedger());

//            generalLedger.invoiceDate(mcReceipt)
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.currencyID(mbDeposit.getCurrencyID());
            generalLedgerCorresponding.contractID(lstDT.get(i).getContractID());
            generalLedgerCorresponding.bankAccountDetailID(mbDeposit.getBankAccountDetailID());
            if (mbDeposit.getBankAccountDetailID() != null) {
                generalLedgerCorresponding.bankAccountDetailID(mbDeposit.getBankAccountDetailID());
                Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(mbDeposit.getBankAccountDetailID());
                if (bankAccountDetails.isPresent()) {
                    generalLedgerCorresponding.bankAccount(bankAccountDetails.get().getBankAccount());
                    generalLedgerCorresponding.bankName(bankAccountDetails.get().getBankName());
                } else {
                    Optional<CreditCard> creditCard = creditCardRepository.findById(mbDeposit.getBankAccountDetailID());
                    if (creditCard.isPresent()) {
                        generalLedgerCorresponding.bankAccount(creditCard.get().getCreditCardNumber());
                        if (creditCard.get().getBankIDIssueCard() != null) {
                            Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                            if(bank.isPresent()){
                                generalLedgerCorresponding.bankName(bank.get().getBankName());
                            }
                        }
                    }
                }
            }
            if (lstDT.get(i).getAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstDT.get(i).getAccountingObjectID());
                generalLedgerCorresponding.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObjectID());
            }
            if (mbDeposit.getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(mbDeposit.getEmployeeID());
                generalLedgerCorresponding.employeeCode(employee.get().getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(employee.get().getAccountingObjectName());
            }
            generalLedgerCorresponding.exchangeRate(mbDeposit.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

            generalLedgerCorresponding.reason(mbDeposit.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedgerCorresponding.description(lstDT.get(i).getDescription());
//            generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
            generalLedgerCorresponding.employeeID(mbDeposit.getEmployeeID());
            generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSetID());
//            generalLedgerCorresponding.contractID(lstDT.get(i).getContractState().getId() == null ? null : lstDT.get(i).getContractState().getId()); comment by namnh
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
//            generalLedgerCorresponding.contactName(mbDeposit.getPayers());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(mbDeposit.getPostedDate());
            generalLedgerCorresponding.departmentID(lstDT.get(i).getDepartmentID());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItemID());
            listGenTemp.add(generalLedgerCorresponding);

//            if ((!string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTempCorresponding.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTempCorresponding.Account) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTempCorresponding.Account).
//                Description:
//                null;
//            }
        }
        return listGenTemp;
    }

    //endregion
    // region Gen GL GOtherVoucher
    List<GeneralLedger> GenGeneralLedgers(GOtherVoucher gOtherVoucher) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<GOtherVoucherDetails> lstDT = new ArrayList<>(gOtherVoucher.getgOtherVoucherDetails());
//        lstDT.sort(Comparator.comparing(GOtherVoucherDetails::getOrderPriority)); // sắp xếp Hautv commment
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.branchID(null);
            generalLedger.setReferenceID(gOtherVoucher.getId());
            generalLedger.setCompanyID(gOtherVoucher.getCompanyID());
            generalLedger.setTypeID(gOtherVoucher.getTypeID());
            generalLedger.postedDate(gOtherVoucher.getPostedDate());
            generalLedger.date(gOtherVoucher.getDate());
            generalLedger.typeLedger(gOtherVoucher.getTypeLedger());
            generalLedger.typeLedger(generalLedger.getTypeLedger());
            generalLedger.noFBook(gOtherVoucher.getNoFBook());
            generalLedger.noMBook(gOtherVoucher.getNoMBook());
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
            generalLedger.currencyID(gOtherVoucher.getCurrencyID());
//            generalLedger.bankAccountDetailID(lstDT.get);
            generalLedger.currencyID(gOtherVoucher.getCurrencyID());
            generalLedger.exchangeRate(gOtherVoucher.getExchangeRate());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.contractID(lstDT.get(i).getContractID());

            if (lstDT.get(i).getBankAccountDetailID() != null) {
                generalLedger.bankAccountDetailID(lstDT.get(i).getBankAccountDetailID());
                Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(lstDT.get(i).getBankAccountDetailID());
                if (bankAccountDetails.isPresent()) {
                    generalLedger.bankAccount(bankAccountDetails.get().getBankAccount());
                    generalLedger.bankName(bankAccountDetails.get().getBankName());
                } else {
                    Optional<CreditCard> creditCard = creditCardRepository.findById(lstDT.get(i).getBankAccountDetailID());
                    if (creditCard.isPresent()) {
                        generalLedger.bankAccount(creditCard.get().getCreditCardNumber());
                        if (creditCard.get().getBankIDIssueCard() != null) {
                            Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                            if(bank.isPresent()){
                                generalLedger.bankName(bank.get().getBankName());
                            }
                        }
                    }
                }
            }
            if (lstDT.get(i).getDebitAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstDT.get(i).getDebitAccountingObjectID());
                generalLedger.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedger.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedger.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                generalLedger.accountingObjectID(lstDT.get(i).getDebitAccountingObjectID());
            }
            if (lstDT.get(i).getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(lstDT.get(i).getEmployeeID());
                generalLedger.employeeCode(employee.get().getAccountingObjectCode());
                generalLedger.employeeName(employee.get().getAccountingObjectName());
            }

//            if (lstDT.get(i).getAmountOriginal() != null)
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(gOtherVoucher.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedger.description(lstDT.get(i).getDescription());
//            generalLedger.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
//            generalLedger.employeeID(gOtherVoucher.getEmployeeID());
            generalLedger.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedger.costSetID(lstDT.get(i).getCostSetID());
//            generalLedger.contractID(lstDT.get(i).getContractState().getId() == null ? null : lstDT.get(i).getContractState().getId() ); comment by namnh
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticCodeID());
            generalLedger.employeeID(lstDT.get(i).getEmployeeID());

//            generalLedger.contactName(mbDeposit.pa());
            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(gOtherVoucher.getPostedDate());
            generalLedger.departmentID(lstDT.get(i).getDepartmentID());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItemID());
            listGenTemp.add(generalLedger);

//            if (!(string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTemp.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTemp.AccountCorresponding) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTemp.AccountCorresponding).
//                Description:
//                null;
//            }

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.branchID(null);
            generalLedgerCorresponding.setReferenceID(gOtherVoucher.getId());
            generalLedgerCorresponding.setCompanyID(gOtherVoucher.getCompanyID());
            generalLedgerCorresponding.setTypeID(gOtherVoucher.getTypeID());
            generalLedgerCorresponding.postedDate(gOtherVoucher.getPostedDate());
            generalLedgerCorresponding.typeLedger(gOtherVoucher.getTypeLedger());
            generalLedgerCorresponding.date(gOtherVoucher.getDate());
            generalLedgerCorresponding.typeLedger(generalLedger.getTypeLedger());
            generalLedgerCorresponding.noFBook(gOtherVoucher.getNoFBook());
            generalLedgerCorresponding.noMBook(gOtherVoucher.getNoMBook());
            generalLedgerCorresponding.currencyID(gOtherVoucher.getCurrencyID());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.currencyID(gOtherVoucher.getCurrencyID());
            generalLedgerCorresponding.contractID(lstDT.get(i).getContractID());
            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetailID());
            generalLedgerCorresponding.employeeID(lstDT.get(i).getEmployeeID());

            if (lstDT.get(i).getBankAccountDetailID() != null) {
                generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetailID());
                Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(lstDT.get(i).getBankAccountDetailID());
                if (bankAccountDetails.isPresent()) {
                    generalLedgerCorresponding.bankAccount(bankAccountDetails.get().getBankAccount());
                    generalLedgerCorresponding.bankName(bankAccountDetails.get().getBankName());
                } else {
                    Optional<CreditCard> creditCard = creditCardRepository.findById(lstDT.get(i).getBankAccountDetailID());
                    if (creditCard.isPresent()) {
                        generalLedgerCorresponding.bankAccount(creditCard.get().getCreditCardNumber());
                        if (creditCard.get().getBankIDIssueCard() != null) {
                            Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                            if(bank.isPresent()){
                                generalLedgerCorresponding.bankName(bank.get().getBankName());
                            }
                        }
                    }
                }
            }
            if (lstDT.get(i).getCreditAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstDT.get(i).getCreditAccountingObjectID());
                generalLedgerCorresponding.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getCreditAccountingObjectID());
            }
            if (lstDT.get(i).getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(lstDT.get(i).getEmployeeID());
                generalLedgerCorresponding.employeeCode(employee.get().getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(employee.get().getAccountingObjectName());
            }
            generalLedgerCorresponding.exchangeRate(gOtherVoucher.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

            generalLedgerCorresponding.reason(gOtherVoucher.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedgerCorresponding.description(lstDT.get(i).getDescription());
//            generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObject() == null ? null : lstDT.get(i).getAccountingObject().getId());
//            generalLedgerCorresponding.employeeID(gOtherVoucher.getEmployeeID() == null ? null : gOtherVoucher.getEmployeeID());
            generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSetID());
//            generalLedgerCorresponding.contractID(lstDT.get(i).getContractState().getId() == null ? null : lstDT.get(i).getContractState().getId()); comment by namnh
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticCodeID());
//            generalLedgerCorresponding.contactName(mbDeposit.getPayers());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(gOtherVoucher.getPostedDate());
            generalLedgerCorresponding.departmentID(lstDT.get(i).getDepartmentID());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItemID());
            listGenTemp.add(generalLedgerCorresponding);

//            if ((!string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTempCorresponding.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTempCorresponding.Account) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTempCorresponding.Account).
//                Description:
//                null;
//            }
        }
        return listGenTemp;
    }
    // region Gen GL GOtherVoucher
    List<GeneralLedger> GenGeneralLedgers(CPExpenseTranfer cpExpenseTranfer) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<CPExpenseTranferDetails> lstDT = new ArrayList<>(cpExpenseTranfer.getcPExpenseTranferDetails());
//        lstDT.sort(Comparator.comparing(GOtherVoucherDetails::getOrderPriority)); // sắp xếp Hautv commment
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.branchID(null);
            generalLedger.setReferenceID(cpExpenseTranfer.getId());
            generalLedger.setCompanyID(cpExpenseTranfer.getCompanyID());
            generalLedger.setTypeID(cpExpenseTranfer.getTypeID());
            generalLedger.postedDate(cpExpenseTranfer.getPostedDate());
            generalLedger.date(cpExpenseTranfer.getDate());
            generalLedger.typeLedger(cpExpenseTranfer.getTypeLedger());
            generalLedger.typeLedger(generalLedger.getTypeLedger());
            generalLedger.noFBook(cpExpenseTranfer.getNoFBook());
            generalLedger.noMBook(cpExpenseTranfer.getNoMBook());
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(cpExpenseTranfer.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedger.description(lstDT.get(i).getDescription());
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
            generalLedger.costSetID(lstDT.get(i).getCostSetID());

            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(cpExpenseTranfer.getPostedDate());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItemID());
            listGenTemp.add(generalLedger);


            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.branchID(null);
            generalLedgerCorresponding.setReferenceID(cpExpenseTranfer.getId());
            generalLedgerCorresponding.setCompanyID(cpExpenseTranfer.getCompanyID());
            generalLedgerCorresponding.setTypeID(cpExpenseTranfer.getTypeID());
            generalLedgerCorresponding.postedDate(cpExpenseTranfer.getPostedDate());
            generalLedgerCorresponding.typeLedger(cpExpenseTranfer.getTypeLedger());
            generalLedgerCorresponding.date(cpExpenseTranfer.getDate());
            generalLedgerCorresponding.typeLedger(generalLedger.getTypeLedger());
            generalLedgerCorresponding.noFBook(cpExpenseTranfer.getNoFBook());
            generalLedgerCorresponding.noMBook(cpExpenseTranfer.getNoMBook());
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(lstDT.get(i).getBankAccountDetails() == null ? null : lstDT.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

            generalLedgerCorresponding.reason(cpExpenseTranfer.getReason());
            if (lstDT.get(i).getDescription() != null)
                generalLedgerCorresponding.description(lstDT.get(i).getDescription());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(cpExpenseTranfer.getPostedDate());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItemID());
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSetID());
            listGenTemp.add(generalLedgerCorresponding);

//            if ((!string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTempCorresponding.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTempCorresponding.Account) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTempCorresponding.Account).
//                Description:
//                null;
//            }
        }
        return listGenTemp;
    }

    //endregion
    // region Gen GL - MBCreditCard
    List<GeneralLedger> GenGeneralLedgers(MBCreditCard mbCreditCard) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<MBCreditCardDetails> lstMBCreditCardDetails = new ArrayList<>(mbCreditCard.getmBCreditCardDetails());
//        lstMBCreditCardDetails.sort(Comparator.comparing(MBCreditCardDetails::getOrderPriority)); // sắp xếp Hautv commment
        List<MBCreditCardDetailTax> lstMBCreditCardDetailTax = new ArrayList<>(mbCreditCard.getMbCreditCardDetailTax());
        for (int i = 0; i < lstMBCreditCardDetails.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            generalLedger.branchID(null);
            generalLedger.setReferenceID(mbCreditCard.getId());
            generalLedger.setCompanyID(mbCreditCard.getCompanyID());
            generalLedger.setTypeID(mbCreditCard.getTypeID());
            generalLedger.postedDate(mbCreditCard.getPostedDate());
            generalLedger.noFBook(mbCreditCard.getNoFBook());
            generalLedger.noMBook(mbCreditCard.getNoMBook());
            generalLedger.date(mbCreditCard.getDate());
            generalLedger.typeLedger(mbCreditCard.getTypeLedger());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedger.account(lstMBCreditCardDetails.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstMBCreditCardDetails.get(i).getCreditAccount());
//            generalLedger.bankAccountDetailID(lstMBCreditCardDetails.get(i).getBankAccountDetails() == null ? null : lstMBCreditCardDetails.get(i).getBankAccountDetails().getId());
            generalLedger.currencyID(mbCreditCard.getCurrencyID());
            if (mbCreditCard.getCreditCardNumber() != null) {
                Optional<CreditCard> creditCard = creditCardRepository.findByCreditCardNumberAndCompanyID(mbCreditCard.getCreditCardNumber(), currentUserLoginAndOrg.get().getOrg());
                if (creditCard.isPresent()) {
                    generalLedger.bankAccountDetailID(creditCard.get().getId());
                    generalLedger.bankAccount(creditCard.get().getCreditCardNumber());
                    if (creditCard.get().getBankIDIssueCard() != null) {
                        Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                        generalLedger.bankName(bank != null ? bank.get().getBankName() : null);
                    }
                }
            }
            generalLedger.contractID(lstMBCreditCardDetails.get(i).getContractID());
            if (lstMBCreditCardDetails  .get(i).getAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstMBCreditCardDetails.get(i).getAccountingObjectID());
                generalLedger.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedger.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedger.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
            }
            if (mbCreditCard.getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(mbCreditCard.getEmployeeID());
                generalLedger.employeeCode(employee.get().getAccountingObjectCode());
                generalLedger.employeeName(employee.get().getAccountingObjectName());
            }
            generalLedger.exchangeRate(mbCreditCard.getExchangeRate());
            generalLedger.debitAmount(lstMBCreditCardDetails.get(i).getAmount());
            generalLedger.debitAmountOriginal(lstMBCreditCardDetails.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(mbCreditCard.getReason());
            generalLedger.description(lstMBCreditCardDetails.get(i).getDescription());
            generalLedger.accountingObjectID(lstMBCreditCardDetails.get(i).getAccountingObjectID());
            generalLedger.employeeID(mbCreditCard.getEmployeeID());
            generalLedger.budgetItemID(lstMBCreditCardDetails.get(i).getBudgetItemID());
            generalLedger.costSetID(lstMBCreditCardDetails.get(i).getCostSetID());
//            generalLedger.contractID(lstMBCreditCardDetails.get(i).getContractState().getId() == null ? null : lstMBCreditCardDetails.get(i).getContractState().getId()); comment by namnh
            generalLedger.statisticsCodeID(lstMBCreditCardDetails.get(i).getStatisticsCodeID());
//            generalLedger.contactName(mbCreditCard.getPayers());
            generalLedger.detailID(lstMBCreditCardDetails.get(i).getId());
            generalLedger.refDateTime(mbCreditCard.getPostedDate());
            generalLedger.departmentID(lstMBCreditCardDetails.get(i).getDepartmentID());
            generalLedger.expenseItemID(lstMBCreditCardDetails.get(i).getExpenseItemID());
            listGenTemp.add(generalLedger);

//            if (!(string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTemp.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTemp.AccountCorresponding) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTemp.AccountCorresponding).
//                Description:
//                null;
//            }

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.branchID(null);
            generalLedgerCorresponding.date(mbCreditCard.getDate());
            generalLedgerCorresponding.typeLedger(mbCreditCard.getTypeLedger());
            generalLedgerCorresponding.setReferenceID(mbCreditCard.getId());
            generalLedgerCorresponding.setCompanyID(mbCreditCard.getCompanyID());
            generalLedgerCorresponding.setTypeID(mbCreditCard.getTypeID());
            generalLedgerCorresponding.postedDate(mbCreditCard.getPostedDate());
            generalLedgerCorresponding.noFBook(mbCreditCard.getNoFBook());
            generalLedgerCorresponding.noMBook(mbCreditCard.getNoMBook());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedgerCorresponding.account(lstMBCreditCardDetails.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstMBCreditCardDetails.get(i).getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(lstMBCreditCardDetails.get(i).getBankAccountDetails() == null ? null : lstMBCreditCardDetails.get(i).getBankAccountDetails().getId());
            generalLedgerCorresponding.currencyID(mbCreditCard.getCurrencyID());

            generalLedgerCorresponding.contractID(lstMBCreditCardDetails.get(i).getContractID());
            if (mbCreditCard.getCreditCardNumber() != null) {
                Optional<CreditCard> creditCard = creditCardRepository.findByCreditCardNumberAndCompanyID(mbCreditCard.getCreditCardNumber(), currentUserLoginAndOrg.get().getOrg());
                if (creditCard.isPresent()) {
                    generalLedgerCorresponding.bankAccountDetailID(creditCard.get().getId());
                    generalLedgerCorresponding.bankAccount(creditCard.get().getCreditCardNumber());
                    if(creditCard.get().getBankIDIssueCard() != null)
                    {
                        Optional<Bank> bank = bankRepository.findById(creditCard.get().getBankIDIssueCard());
                        generalLedgerCorresponding.bankName(bank != null ? bank.get().getBankName() : null);
                    }
                }
            }
            generalLedgerCorresponding.contractID(lstMBCreditCardDetails.get(i).getContractID());
            if (lstMBCreditCardDetails.get(i).getAccountingObjectID() != null) {
                Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(lstMBCreditCardDetails.get(i).getAccountingObjectID());
                generalLedgerCorresponding.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
            }
            if (mbCreditCard.getEmployeeID() != null) {
                Optional<AccountingObject> employee = accountingObjectRepository.findById(mbCreditCard.getEmployeeID());
                generalLedgerCorresponding.employeeCode(employee.get().getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(employee.get().getAccountingObjectName());
            }

            generalLedgerCorresponding.exchangeRate(mbCreditCard.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstMBCreditCardDetails.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstMBCreditCardDetails.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
            generalLedgerCorresponding.reason(mbCreditCard.getReason());
            generalLedgerCorresponding.description(lstMBCreditCardDetails.get(i).getDescription());
            generalLedgerCorresponding.accountingObjectID(lstMBCreditCardDetails.get(i).getAccountingObjectID());
            generalLedgerCorresponding.employeeID(mbCreditCard.getEmployeeID());
            generalLedgerCorresponding.budgetItemID(lstMBCreditCardDetails.get(i).getBudgetItemID());
            generalLedgerCorresponding.costSetID(lstMBCreditCardDetails.get(i).getCostSetID());
//            generalLedgerCorresponding.contractID(lstMBCreditCardDetails.get(i).getContractState().getId() == null ? null : lstMBCreditCardDetails.get(i).getContractState().getId()); comment by namnh
            generalLedgerCorresponding.statisticsCodeID(lstMBCreditCardDetails.get(i).getStatisticsCodeID());
//            generalLedgerCorresponding.contactName(mbCreditCard.getPayers());
            generalLedgerCorresponding.detailID(lstMBCreditCardDetails.get(i).getId());
            generalLedgerCorresponding.refDateTime(mbCreditCard.getPostedDate());
            generalLedgerCorresponding.departmentID(lstMBCreditCardDetails.get(i).getDepartmentID());
            generalLedgerCorresponding.expenseItemID(lstMBCreditCardDetails.get(i).getExpenseItemID());
            listGenTemp.add(generalLedgerCorresponding);

//            if ((!string.IsNullOrEmpty(lstDT[i].CreditAccount)) &&
//                (((lstDT[i].CreditAccount.StartsWith("133"))
//                    || lstDT[i].CreditAccount.StartsWith("33311")))) {
//                genTempCorresponding.VATDescription = mcReceipt.MCReceiptDetailTaxs.Any(x = > x.VATAccount == genTempCorresponding.Account) ?
//                mcReceipt.MCReceiptDetailTaxs.FirstOrDefault(x = > x.VATAccount == genTempCorresponding.Account).
//                Description:
//                null;
//            }
        }
        return listGenTemp;
    }
    //endregion

    // region Gen GL mBTellerPaper
    List<GeneralLedger> GenGeneralLedgers(MBTellerPaper mBTellerPaper) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<MBTellerPaperDetails> lstDT = new ArrayList<>(mBTellerPaper.getmBTellerPaperDetails());
        List<MBTellerPaperDetailTax> lstDTax = new ArrayList<>(mBTellerPaper.getmBTellerPaperDetailTaxs());
        Optional<BankAccountDetails> bankAcc = mBTellerPaper.getBankAccountDetailID() != null ? bankAccountDetailsRepository.findOneById(mBTellerPaper.getBankAccountDetailID()) : null;
        Optional<CreditCard> creditCard = mBTellerPaper.getBankAccountDetailID() != null ? creditCardRepository.findById(mBTellerPaper.getBankAccountDetailID()) : null;
        Bank bank = new Bank();
        if (creditCard.isPresent()) {
            bank = bankRepository.findById(creditCard.get().getBankIDIssueCard()).get();
        }
//        lstDT.sort(Comparator.comparing(MBTellerPaperDetails::getOrderPriority)); Hautv commment
        for (int i = 0; i < lstDT.size(); i++) {
            GeneralLedger generalLedger = new GeneralLedger();
            Optional<AccountingObject> accObj = lstDT.get(i).getAccountingObjectID() != null ? accountingObjectRepository.findOneById(lstDT.get(i).getAccountingObjectID()) : null;
            generalLedger.setCompanyID(mBTellerPaper.getCompanyId());
            generalLedger.branchID(null);
            generalLedger.setReferenceID(mBTellerPaper.getId());
            generalLedger.setTypeID(mBTellerPaper.getTypeId());
            generalLedger.postedDate(mBTellerPaper.getPostedDate());
            generalLedger.noFBook(mBTellerPaper.getNoFBook());
            generalLedger.noMBook(mBTellerPaper.getNoMBook());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedger.account(lstDT.get(i).getDebitAccount());
            generalLedger.accountCorresponding(lstDT.get(i).getCreditAccount());
            generalLedger.bankAccountDetailID(mBTellerPaper.getBankAccountDetailID());
            generalLedger.currencyID(mBTellerPaper.getCurrencyId());
            generalLedger.exchangeRate(mBTellerPaper.getExchangeRate());
            generalLedger.debitAmount(lstDT.get(i).getAmount());
            generalLedger.debitAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
            generalLedger.reason(mBTellerPaper.getReason());
            generalLedger.description(lstDT.get(i).getDescription());
            generalLedger.accountingObjectID(lstDT.get(i).getAccountingObjectID());
            generalLedger.employeeID(mBTellerPaper.getEmployeeID());
            generalLedger.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedger.costSetID(lstDT.get(i).getCostSetID());
            generalLedger.contractID(lstDT.get(i).geteMContractID());
//            generalLedger.contractID(lstDT.get(i).getContractState() == null ? null : lstDT.get(i).getContractState().getId()); comment by namnh
            generalLedger.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
            generalLedger.contactName(mBTellerPaper.getReceiver());
            generalLedger.detailID(lstDT.get(i).getId());
            generalLedger.refDateTime(mBTellerPaper.getDate());
            generalLedger.departmentID(lstDT.get(i).getDepartmentID());
            generalLedger.expenseItemID(lstDT.get(i).getExpenseItemID());
            generalLedger.bankAccount(bankAcc != null ? bankAcc.get().getBankAccount() : creditCard.get().getCreditCardNumber());
            generalLedger.bankName(bankAcc != null ? bankAcc.get().getBankName() : bank.getBankName());
            generalLedger.date(mBTellerPaper.getDate());
            generalLedger.typeLedger(mBTellerPaper.getTypeLedger());
            generalLedger.accountingObjectCode(accObj == null ? null : accObj.get().getAccountingObjectCode());
            generalLedger.accountingObjectName(accObj != null ? accObj.get().getAccountingObjectName() : null);
            generalLedger.accountingObjectAddress(accObj != null ? accObj.get().getAccountingObjectAddress() : null);
            if (mBTellerPaper.getEmployeeID() != null) {
                AccountingObject employee = accountingObjectRepository.findById(mBTellerPaper.getEmployeeID()).get();
                generalLedger.employeeCode(employee.getAccountingObjectCode());
                generalLedger.employeeName(employee.getAccountingObjectName());
            }
            listGenTemp.add(generalLedger);

            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
            generalLedgerCorresponding.setCompanyID(mBTellerPaper.getCompanyId());
            generalLedgerCorresponding.branchID(null);
            generalLedgerCorresponding.setReferenceID(mBTellerPaper.getId());
            generalLedgerCorresponding.setTypeID(mBTellerPaper.getTypeId());
            generalLedgerCorresponding.postedDate(mBTellerPaper.getPostedDate());
            generalLedgerCorresponding.noFBook(mBTellerPaper.getNoFBook());
            generalLedgerCorresponding.noMBook(mBTellerPaper.getNoMBook());
//            generalLedger.invoiceDate(mcReceipt)
            generalLedgerCorresponding.account(lstDT.get(i).getCreditAccount());
            generalLedgerCorresponding.accountCorresponding(lstDT.get(i).getDebitAccount());
            generalLedgerCorresponding.bankAccountDetailID(mBTellerPaper.getBankAccountDetailID());
            generalLedgerCorresponding.currencyID(mBTellerPaper.getCurrencyId());
            generalLedgerCorresponding.exchangeRate(mBTellerPaper.getExchangeRate());
            generalLedgerCorresponding.creditAmount(lstDT.get(i).getAmount());
            generalLedgerCorresponding.creditAmountOriginal(lstDT.get(i).getAmountOriginal());
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
            generalLedgerCorresponding.reason(mBTellerPaper.getReason());
            generalLedgerCorresponding.description(lstDT.get(i).getDescription());
            generalLedgerCorresponding.accountingObjectID(lstDT.get(i).getAccountingObjectID());
            generalLedgerCorresponding.employeeID(mBTellerPaper.getEmployeeID());
            generalLedgerCorresponding.budgetItemID(lstDT.get(i).getBudgetItemID());
            generalLedgerCorresponding.costSetID(lstDT.get(i).getCostSetID());
            generalLedgerCorresponding.contractID(lstDT.get(i).geteMContractID());
//            generalLedgerCorresponding.contractID(lstDT.get(i).getContractState() == null ? null : lstDT.get(i).getContractState().getId()); comment by namnh
            generalLedgerCorresponding.statisticsCodeID(lstDT.get(i).getStatisticsCodeID());
            generalLedgerCorresponding.contactName(mBTellerPaper.getReceiver());
            generalLedgerCorresponding.detailID(lstDT.get(i).getId());
            generalLedgerCorresponding.refDateTime(mBTellerPaper.getDate());
            generalLedgerCorresponding.departmentID(lstDT.get(i).getDepartmentID());
            generalLedgerCorresponding.expenseItemID(lstDT.get(i).getExpenseItemID());
            generalLedgerCorresponding.bankAccount(bankAcc != null ? bankAcc.get().getBankAccount() : creditCard.get().getCreditCardNumber());
            generalLedgerCorresponding.bankName(bankAcc != null ? bankAcc.get().getBankName() : bank.getBankName());
            generalLedgerCorresponding.date(mBTellerPaper.getDate());
            generalLedgerCorresponding.typeLedger(mBTellerPaper.getTypeLedger());
            generalLedgerCorresponding.accountingObjectCode(accObj == null ? null : accObj.get().getAccountingObjectCode());
            generalLedgerCorresponding.accountingObjectName(accObj != null ? accObj.get().getAccountingObjectName() : null);
            generalLedgerCorresponding.accountingObjectAddress(accObj != null ? accObj.get().getAccountingObjectAddress() : null);
            if (mBTellerPaper.getEmployeeID() != null) {
                AccountingObject employee = accountingObjectRepository.findById(mBTellerPaper.getEmployeeID()).get();
                generalLedgerCorresponding.employeeCode(employee.getAccountingObjectCode());
                generalLedgerCorresponding.employeeName(employee.getAccountingObjectName());
            }
            listGenTemp.add(generalLedgerCorresponding);
        }
        return listGenTemp;
    }
    //endregion Gen GL mBTellerPaper

    List<GeneralLedger> genGeneralLedgers(SaReturn saReturn) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<SaReturnDetails> lstDT = new ArrayList<>(saReturn.getSaReturnDetails());
        Collections.sort(lstDT, Comparator.comparingInt(SaReturnDetails::getOrderPriority));
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null) {
            rSInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
        }
        for (SaReturnDetails aLstDT : lstDT) {
            try {
                Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(aLstDT.getMaterialGoodsID());
                Optional<Repository> repository = Optional.empty();
                if (aLstDT.getRepositoryID() != null) {
                    repository = repositoryRepository.findById(aLstDT.getRepositoryID());
                }
                // tổng cộng tạo 4 cặp

                // căp amount
                genGlCouple(saReturn, listGenTemp, aLstDT, materialGoods, repository, aLstDT.getAmount(),
                    aLstDT.getAmountOriginal(), aLstDT.getDebitAccount(), aLstDT.getCreditAccount(), aLstDT.getCreditAccount(),
                    aLstDT.getDebitAccount(), 0, aLstDT.getDescription());

                // cặp tk chiết khấu
                if (aLstDT.getDiscountAmountOriginal() != null && aLstDT.getDiscountAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                    genGlCouple(saReturn, listGenTemp, aLstDT, materialGoods, repository, aLstDT.getDiscountAmount(),
                        aLstDT.getDiscountAmountOriginal(), aLstDT.getCreditAccount(), aLstDT.getDiscountAccount(),
                        aLstDT.getDiscountAccount(), aLstDT.getCreditAccount(), 1, aLstDT.getDescription());
                }


                // cặp tk thuế
                if (aLstDT.getVatAmountOriginal() != null && aLstDT.getVatAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                    genGlCouple(saReturn, listGenTemp, aLstDT, materialGoods, repository, aLstDT.getVatAmount(),
                        aLstDT.getVatAmountOriginal(), aLstDT.getVatAccount(), aLstDT.getDeductionDebitAccount(),
                        aLstDT.getDeductionDebitAccount(), aLstDT.getVatAccount(), 2, aLstDT.getVatDescription());
                }

                // cặp tiền vốn
                if (saReturn.getTypeID().equals(HANG_BAN_TRA_LAI) && aLstDT.getRepositoryID() != null && !materialGoods.get().getMaterialGoodsType().equals(Constants.MaterialGoodsType.SERVICE)) {
                    genGlCouple(saReturn, listGenTemp, aLstDT, materialGoods, repository, aLstDT.getOwAmount(),
                        aLstDT.getOwAmount(), aLstDT.getCostAccount(), aLstDT.getRepositoryAccount(),
                        aLstDT.getRepositoryAccount(), aLstDT.getCostAccount(), TypeConstant.RS_INWARD_OUTWARD, aLstDT.getDescription());
                    genRepositoryLedger(saReturn, repositoryLedgers, rSInwardOutward, aLstDT, materialGoods, repository);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        repositoryLedgerRepository.deleteAllByReferenceID(saReturn.getId());
        if (repositoryLedgers.size() > 0) {
            repositoryLedgerRepository.saveAll(repositoryLedgers);
        }

        return listGenTemp;
    }

    private void genGlCouple(SaReturn saReturn, List<GeneralLedger> listGenTemp,
                             SaReturnDetails aLstDT, Optional<MaterialGoods> materialGood, Optional<Repository> repository,
                             BigDecimal amount, BigDecimal amountOriginal, String account, String accountCorresponding,
                             String account1, String accountCorresponding1, Integer typeID, String description) throws InvocationTargetException, IllegalAccessException {
        GeneralLedger generalLedger = new GeneralLedger();

        BeanUtils.copyProperties(generalLedger, saReturn);
        if (typeID == TypeConstant.RS_INWARD_OUTWARD) {
            generalLedger.setTypeID(typeID);
            if (saReturn.getRsInwardOutwardID() != null) {
                Optional<RSInwardOutward> repositoryById = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
                if (repositoryById.isPresent()) {
                    generalLedger.setNoFBook(repositoryById.get().getNoFBook());
                    generalLedger.setNoMBook(repositoryById.get().getNoMBook());
                }
            }
        }

        // tài khoản
        generalLedger.account(account);
        generalLedger.accountCorresponding(accountCorresponding);

        // tiền
        if (typeID.equals(TypeConstant.RS_INWARD_OUTWARD)) {
            generalLedger.creditAmount(amount);
            generalLedger.creditAmountOriginal(amountOriginal);
            generalLedger.debitAmount(BigDecimal.ZERO);
            generalLedger.debitAmountOriginal(BigDecimal.ZERO);
        } else {
            generalLedger.debitAmount(amount);
            generalLedger.debitAmountOriginal(amountOriginal);
            generalLedger.creditAmount(BigDecimal.ZERO);
            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
         }


        // Lưu thông tin khác
        generalLedger.description(description);
        generalLedger.accountingObjectID(aLstDT.getAccountingObjectID());
        generalLedger.setReferenceID(saReturn.getId());
        generalLedger.employeeID(saReturn.getEmployee() != null ? saReturn.getEmployee().getId() : null);
        generalLedger.detailID(aLstDT.getId());
        generalLedger.refDateTime(saReturn.getPostedDate());

        Optional<AccountingObject> accountingObject = Optional.empty();
        if (aLstDT.getAccountingObjectID() != null) {
            accountingObject = accountingObjectRepository.findById(aLstDT.getAccountingObjectID());
            if (accountingObject.isPresent()) {
                generalLedger.setAccountingObjectCode(accountingObject.get().getAccountingObjectCode());
                generalLedger.setAccountingObjectName(accountingObject.get().getAccountingObjectName());
                generalLedger.setAccountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                generalLedger.setContactName(accountingObject.get().getContactName());
            }
        }

        if (saReturn.getEmployee() != null) {
            generalLedger.setEmployeeID(saReturn.getEmployee().getId());
            generalLedger.setEmployeeName(saReturn.getEmployee().getAccountingObjectName());
            generalLedger.setEmployeeCode(saReturn.getEmployee().getAccountingObjectCode());
        }

        generalLedger.setUnitID(aLstDT.getUnitID());
        generalLedger.setMainUnitID(aLstDT.getMainUnitID());
        generalLedger.setUnitPrice(aLstDT.getUnitPrice());
        generalLedger.setQuantity(aLstDT.getQuantity());
        generalLedger.setMainQuantity(aLstDT.getMainQuantity());
        generalLedger.setMainUnitPrice(aLstDT.getMainUnitPrice());
        generalLedger.setMainConvertRate(aLstDT.getMainConvertRate());
        generalLedger.setFormula(aLstDT.getFormula());

        if (materialGood.isPresent()) {
            generalLedger.setMaterialGoodsID(materialGood.get().getId());
            generalLedger.setMaterialGoodsName(materialGood.get().getMaterialGoodsName());
            generalLedger.setMaterialGoodsCode(materialGood.get().getMaterialGoodsCode());
        }
        if (repository.isPresent()) {
            generalLedger.setRepositoryID(repository.get().getId());
            generalLedger.setRepositoryCode(repository.get().getRepositoryCode());
            generalLedger.setRepositoryName(repository.get().getRepositoryName());
        }

        generalLedger.budgetItemID(aLstDT.getBudgetItemID());
        generalLedger.costSetID(aLstDT.getCostSetID());
        generalLedger.contractID(aLstDT.getContractID());
        generalLedger.statisticsCodeID(aLstDT.getStatisticsCodeID());
        generalLedger.expenseItemID(aLstDT.getExpenseItemID());
        generalLedger.departmentID(aLstDT.getDepartmentID());
        generalLedger.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());

//        generalLedger.setBankName(saReturn.getAccountingObjectBankName());
//        generalLedger.setBankAccount(saReturn.getAccountingObjectBankAccount());

        listGenTemp.add(generalLedger);


        GeneralLedger generalLedgerCorresponding = new GeneralLedger();
        BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
        generalLedgerCorresponding.account(account1);
        generalLedgerCorresponding.accountCorresponding(accountCorresponding1);

        if (typeID.equals(TypeConstant.RS_INWARD_OUTWARD)) {
            generalLedgerCorresponding.debitAmount(amount);
            generalLedgerCorresponding.debitAmountOriginal(amountOriginal);
            generalLedgerCorresponding.creditAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.creditAmountOriginal(BigDecimal.ZERO);
        } else {
            generalLedgerCorresponding.creditAmount(amount);
            generalLedgerCorresponding.creditAmountOriginal(amountOriginal);
            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
         }
        accountingObject.ifPresent(accountingObject1 -> generalLedgerCorresponding.setAccountingObjectCode(accountingObject1.getAccountingObjectCode()));

        listGenTemp.add(generalLedgerCorresponding);

    }

    private void genRepositoryLedger(SaReturn saReturn, List<RepositoryLedger> repositoryLedgers,
                                     Optional<RSInwardOutward> rSInwardOutward, SaReturnDetails aLstDT,
                                     Optional<MaterialGoods> materialGoods, Optional<Repository> repository) throws IllegalAccessException, InvocationTargetException {
        if (Boolean.TRUE.equals(saReturn.isIsDeliveryVoucher()) && saReturn.getRsInwardOutwardID() != null && rSInwardOutward.isPresent()) {
            RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
            BeanUtils.copyProperties(repositoryLedgerItem, aLstDT);
            repositoryLedgerItem.setTypeID(TypeConstant.RS_INWARD_OUTWARD);
            repositoryLedgerItem.setReferenceID(rSInwardOutward.get().getId());
            repositoryLedgerItem.setCompanyID(saReturn.getCompanyID());
            if (rSInwardOutward.get().getNoFBook() != null) {
                repositoryLedgerItem.setNoFBook(rSInwardOutward.get().getNoFBook());
            }
            if (rSInwardOutward.get().getNoMBook() != null) {
                repositoryLedgerItem.setNoMBook(rSInwardOutward.get().getNoMBook());
            }
            repositoryLedgerItem.setUnitPrice(aLstDT.getOwPrice());
            repositoryLedgerItem.setIwQuantity(aLstDT.getQuantity());
            repositoryLedgerItem.setIwAmount(aLstDT.getOwAmount());
            repositoryLedgerItem.setOwAmount(BigDecimal.ZERO);
            repositoryLedgerItem.setMainIWQuantity(aLstDT.getMainQuantity());
            repositoryLedgerItem.setMainOWQuantity(BigDecimal.ZERO);
            if (repositoryLedgerItem.getUnitPrice() != null && aLstDT.getMainConvertRate() != null) {
                if ("*".equalsIgnoreCase(aLstDT.getFormula()) && aLstDT.getMainConvertRate().compareTo(BigDecimal.ZERO) != 0) {
                    repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice().divide(aLstDT.getMainConvertRate(), RoundingMode.CEILING));
                } else if ("/".equalsIgnoreCase(aLstDT.getFormula())) {
                    repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice().multiply(aLstDT.getMainConvertRate()));

                }
            }

            repositoryLedgerItem.setAccount(aLstDT.getRepositoryAccount());
            repositoryLedgerItem.setAccountCorresponding(aLstDT.getCostAccount());
            repositoryLedgerItem.setDetailID(aLstDT.getId());
            repositoryLedgerItem.setReason(rSInwardOutward.get().getReason());
            repositoryLedgerItem.setDate(rSInwardOutward.get().getDate());
            repositoryLedgerItem.setPostedDate(rSInwardOutward.get().getPostedDate());
            repositoryLedgerItem.setTypeLedger(rSInwardOutward.get().getTypeLedger());
            repositoryLedgerItem.setUnitID(aLstDT.getUnitID());
            repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnitID());
            repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
            repositoryLedgerItem.setFormula(aLstDT.getFormula());
            // repository
            if (repository.isPresent()) {
                repositoryLedgerItem.setRepositoryID(repository.get().getId());
                repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
            }

            // materialGoods
            repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGoodsID());

            if (materialGoods.isPresent()) {
                repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
            }


            repositoryLedgers.add(repositoryLedgerItem);
        }
    }

    private List<GeneralLedger> genGeneralLedgers(PPService ppService) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<PPServiceDetail> lstDT = new ArrayList<>(ppService.getPpServiceDetails());
        for (PPServiceDetail aLstDT : lstDT.stream().sorted((a, b) -> a.getOrderPriority() - b.getOrderPriority()).collect(Collectors.toList())) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                // cặp nợ có
                BeanUtils.copyProperties(generalLedger, ppService);
                generalLedger.setId(null);
//                generalLedger.setInvoiceNo(aLstDT.getInvoiceNo());
                switch (ppService.getTypeID()) {
                    case Constants.PPServiceType.PPSERVICE_CASH:
                        Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppService.getPaymentVoucherID());
                        if (mcPaymentOptional.isPresent()) {
                            generalLedger.setNoFBook(mcPaymentOptional.get().getNoFBook());
                            generalLedger.setNoMBook(mcPaymentOptional.get().getNoMBook());
                        }
                        break;
                    case Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER:
                    case Constants.PPServiceType.PPSERVICE_TRANSFER_SEC:
                    case Constants.PPServiceType.PPSERVICE_CASH_SEC:
                        Optional<MBTellerPaper> mbTellerPaperOptionalSecTm = mbTellerPaperRepository.findById(ppService.getPaymentVoucherID());
                        if (mbTellerPaperOptionalSecTm.isPresent()) {
                            generalLedger.setNoFBook(mbTellerPaperOptionalSecTm.get().getNoFBook());
                            generalLedger.setNoMBook(mbTellerPaperOptionalSecTm.get().getNoMBook());
                            generalLedger.setBankAccountDetailID(mbTellerPaperOptionalSecTm.get().getBankAccountDetailID());
                            Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(mbTellerPaperOptionalSecTm.get().getBankAccountDetailID());
                            if (bankAccountDetails.isPresent()) {
                                generalLedger.setBankAccount(bankAccountDetails.get().getBankAccount());
                                generalLedger.bankName(bankAccountDetails.get().getBankName());
                            }
                        }
                        break;
                    case Constants.PPServiceType.PPSERVICE_CREDIT_CARD:
                        Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppService.getPaymentVoucherID());
                        if (mbCreditCardOptional.isPresent()) {
                            generalLedger.setNoFBook(mbCreditCardOptional.get().getNoFBook());
                            generalLedger.setNoMBook(mbCreditCardOptional.get().getNoMBook());
                            Optional<CreditCard> creditCardOptional = creditCardRepository.findByCreditCardNumberAndCompanyID(mbCreditCardOptional.get().getCreditCardNumber(), ppService.getCompanyID());
                            if (creditCardOptional.isPresent()) {
                                Optional<Bank> bankOptional = bankRepository.findById(creditCardOptional.get().getBankIDIssueCard());
                                bankOptional.ifPresent(bank -> generalLedger.bankName(bank.getBankName()));
                                generalLedger.setBankAccountDetailID(creditCardOptional.get().getId());
                                generalLedger.setBankAccount(mbCreditCardOptional.get().getCreditCardNumber());
                            }
                        }
                        break;
                }
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                generalLedger.debitAmount(aLstDT.getAmount().subtract(aLstDT.getDiscountAmount()));
                generalLedger.debitAmountOriginal(aLstDT.getAmountOriginal().subtract(aLstDT.getDiscountAmountOriginal()));
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());

                Optional<AccountingObject> accObj = aLstDT.getAccountingObjectID() != null ? accountingObjectRepository.findOneById(aLstDT.getAccountingObjectID()) : Optional.empty();
                generalLedger.accountingObjectID(aLstDT.getAccountingObjectID());
                generalLedger.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                generalLedger.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                generalLedger.accountingObjectAddress(accObj.map(AccountingObject::getAccountingObjectAddress).orElse(null));
                generalLedger.contactName(accObj.map(AccountingObject::getContactName).orElse(null));
                generalLedger.setReferenceID(ppService.getId());
                generalLedger.employeeID(ppService.getEmployeeID());
                generalLedger.detailID(aLstDT.getId());
                generalLedger.refDateTime(ppService.getDate());
                generalLedger.budgetItemID(aLstDT.getBudgetItemID());
                generalLedger.costSetID(aLstDT.getCostSetID());
                generalLedger.contractID(aLstDT.getContractID());
                generalLedger.statisticsCodeID(aLstDT.getStatisticsCodeID());
                generalLedger.expenseItemID(aLstDT.getExpenseItemID());
                generalLedger.departmentID(aLstDT.getDepartmentID());
                generalLedger.setCompanyID(ppService.getCompanyID());

                Optional<MaterialGoods> materialGoodsOptional = aLstDT.getMaterialGoodsID() != null ? materialGoodsRepository.findById(aLstDT.getMaterialGoodsID()) : Optional.empty();
                generalLedger.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                generalLedger.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                generalLedger.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                Optional<AccountingObject> employeeOptional = ppService.getEmployeeID() != null ? accountingObjectRepository.findById(ppService.getEmployeeID()) : Optional.empty();
                generalLedger.employeeID(ppService.getEmployeeID());
                generalLedger.setEmployeeCode(employeeOptional.map(AccountingObject::getAccountingObjectCode).orElse(null));
                generalLedger.setEmployeeName(employeeOptional.map(AccountingObject::getAccountingObjectName).orElse(null));

                generalLedger.setUnitID(aLstDT.getUnitID());
                generalLedger.setCurrencyID(ppService.getCurrencyID());
                generalLedger.quantity(aLstDT.getQuantity());
                generalLedger.setUnitPrice(aLstDT.getUnitPrice());
                generalLedger.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                generalLedger.setOrderPriority(aLstDT.getOrderPriority());
                generalLedger.setInvoiceNo(null);
                generalLedger.setInvoiceDate(null);
                generalLedger.setInvoiceSeries(null);
                // thành tiền lớn hơn 0 ms lưu
                if (aLstDT.getAmount() != null && aLstDT.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                    listGenTemp.add(generalLedger);
                }

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                generalLedgerCorresponding.creditAmount(aLstDT.getAmount().subtract(aLstDT.getDiscountAmount()));
                generalLedgerCorresponding.creditAmountOriginal(aLstDT.getAmountOriginal().subtract(aLstDT.getDiscountAmountOriginal()));
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                if (aLstDT.getAmount() != null && aLstDT.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                    listGenTemp.add(generalLedgerCorresponding);
                }

                if (!Strings.isNullOrEmpty(aLstDT.getVatAccount()) && !Strings.isNullOrEmpty(aLstDT.getDeductionDebitAccount())) {
                    GeneralLedger generalLedgerVAT = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerVAT, generalLedger);
                    generalLedgerVAT.account(aLstDT.getVatAccount());
                    generalLedgerVAT.accountCorresponding(aLstDT.getDeductionDebitAccount());
                    generalLedgerVAT.creditAmount(BigDecimal.ZERO);
                    generalLedgerVAT.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedgerVAT.debitAmount(aLstDT.getVatAmount());
                    generalLedgerVAT.debitAmountOriginal(aLstDT.getVatAmountOriginal());
                    generalLedgerVAT.setDescription(aLstDT.getVatDescription());

                    listGenTemp.add(generalLedgerVAT);

                    GeneralLedger generalLedgerVATCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerVATCorresponding, generalLedgerVAT);
                    generalLedgerVATCorresponding.account(aLstDT.getDeductionDebitAccount());
                    generalLedgerVATCorresponding.accountCorresponding(aLstDT.getVatAccount());
                    generalLedgerVATCorresponding.creditAmount(aLstDT.getVatAmount());
                    generalLedgerVATCorresponding.creditAmountOriginal(aLstDT.getVatAmountOriginal());
                    generalLedgerVATCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                    listGenTemp.add(generalLedgerVATCorresponding);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return listGenTemp;
    }

    List<GeneralLedger> genGeneralLedgers(SAInvoice saInvoice) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<SAInvoiceDetails> lstDT = new ArrayList<>(saInvoice.getsAInvoiceDetails());
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
        Optional<MCReceipt> mcReceiptOptional = Optional.empty();
        Optional<MBDeposit> mbDepositOptional = Optional.empty();
        if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_TM)) {
            mcReceiptOptional = mcReceiptRepository.findById(saInvoice.getMcReceiptID());
        } else if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_CK)) {
            mbDepositOptional = mbDepositRepository.findById(saInvoice.getMbDepositID());
        }
        Optional<AccountingObject> employee = saInvoice.getEmployeeID() != null ? accountingObjectRepository.findOneById(saInvoice.getEmployeeID()) : Optional.empty();
        Optional<BankAccountDetails> accBankDetailObj = (mbDepositOptional.isPresent() && mbDepositOptional.get().getBankAccountDetailID() != null) ? bankAccountDetailsRepository.findOneById(mbDepositOptional.get().getBankAccountDetailID()) : Optional.empty();
        for (SAInvoiceDetails aLstDT : lstDT) {
            try {
                Optional<AccountingObject> accObj = aLstDT.getAccountingObjectID() != null ? accountingObjectRepository.findOneById(aLstDT.getAccountingObjectID()) : Optional.empty();
                Optional<Repository> repositoryOptional = aLstDT.getRepositoryID() != null ? repositoryRepository.findById(aLstDT.getRepositoryID()) : Optional.empty();
                Optional<MaterialGoods> materialGoodsOptional = aLstDT.getMaterialGoodsID() != null ? materialGoodsRepository.findById(aLstDT.getMaterialGoodsID()) : Optional.empty();
                if (Boolean.FALSE.equals(aLstDT.isIsPromotion()) || aLstDT.isIsPromotion() == null) {
                    GeneralLedger generalLedger = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedger, saInvoice);
                    generalLedger.setTypeID(saInvoice.getTypeID());
                    generalLedger.account(aLstDT.getDebitAccount());
                    generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                    generalLedger.debitAmount(aLstDT.getAmount());
                    generalLedger.debitAmountOriginal(aLstDT.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(aLstDT.getDescription());
                    generalLedger.accountingObjectID(aLstDT.getAccountingObjectID());
                    generalLedger.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                    generalLedger.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                    generalLedger.setReferenceID(saInvoice.getId());
                    generalLedger.employeeID(saInvoice.getEmployeeID());
                    generalLedger.setEmployeeCode(employee.map(AccountingObject::getAccountingObjectCode).orElse(null));
                    generalLedger.setEmployeeName(employee.map(AccountingObject::getAccountingObjectName).orElse(null));
                    generalLedger.detailID(aLstDT.getId());

                    generalLedger.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                    generalLedger.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                    generalLedger.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                    generalLedger.setRepositoryID(aLstDT.getRepositoryID());
                    generalLedger.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                    generalLedger.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                    generalLedger.setUnitID(aLstDT.getUnitID());
                    generalLedger.setQuantity(aLstDT.getQuantity());
                    generalLedger.setUnitPrice(aLstDT.getUnitPrice());
                    generalLedger.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                    generalLedger.setMainUnitID(aLstDT.getMainUnitID());
                    generalLedger.setMainQuantity(aLstDT.getMainQuantity());
                    generalLedger.setMainUnitPrice(aLstDT.getMainUnitPrice());
                    generalLedger.setMainConvertRate(aLstDT.getMainConvertRate());
                    generalLedger.setFormula(aLstDT.getFormula());

                    generalLedger.refDateTime(saInvoice.getDate());
                    generalLedger.budgetItemID(aLstDT.getBudgetItemID());
                    generalLedger.costSetID(aLstDT.getCostSetID());
                    generalLedger.contractID(aLstDT.getContractID());
                    generalLedger.statisticsCodeID(aLstDT.getStatisticsCodeID());
                    generalLedger.expenseItemID(aLstDT.getExpenseItemID());
                    if (mcReceiptOptional.isPresent()) {
                        generalLedger.setNoFBook(mcReceiptOptional.get().getNoFBook());
                        generalLedger.setNoMBook(mcReceiptOptional.get().getNoMBook());
                    } else if (mbDepositOptional.isPresent()) {
                        generalLedger.setNoFBook(mbDepositOptional.get().getNoFBook());
                        generalLedger.setNoMBook(mbDepositOptional.get().getNoMBook());
                        generalLedger.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                        generalLedger.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                        generalLedger.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                    }
                    listGenTemp.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.setTypeID(saInvoice.getTypeID());
                    generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(aLstDT.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(aLstDT.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                    listGenTemp.add(generalLedgerCorresponding);
                    if (aLstDT.getDiscountAmountOriginal() != null && aLstDT.getDiscountAmountOriginal().doubleValue() != 0 && aLstDT.getDiscountAccount() != null) {
                        GeneralLedger generalLedger3 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedger3, saInvoice);
                        generalLedger3.setTypeID(saInvoice.getTypeID());
                        generalLedger3.account(aLstDT.getDiscountAccount());
                        generalLedger3.accountCorresponding(aLstDT.getDebitAccount());
                        generalLedger3.debitAmount(aLstDT.getDiscountAmount());
                        generalLedger3.debitAmountOriginal(aLstDT.getDiscountAmountOriginal());
                        generalLedger3.creditAmount(BigDecimal.ZERO);
                        generalLedger3.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger3.description(aLstDT.getDescription());
                        generalLedger3.accountingObjectID(aLstDT.getAccountingObjectID());
                        generalLedger3.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger3.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger3.setReferenceID(saInvoice.getId());
                        generalLedger3.employeeID(saInvoice.getEmployeeID());
                        generalLedger3.setEmployeeCode(employee.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger3.setEmployeeName(employee.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger3.detailID(aLstDT.getId());

                        generalLedger3.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                        generalLedger3.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                        generalLedger3.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                        generalLedger3.setRepositoryID(aLstDT.getRepositoryID());
                        generalLedger3.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                        generalLedger3.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                        generalLedger3.setUnitID(aLstDT.getUnitID());
                        generalLedger3.setQuantity(aLstDT.getQuantity());
                        generalLedger3.setUnitPrice(aLstDT.getUnitPrice());
                        generalLedger3.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                        generalLedger3.setMainUnitID(aLstDT.getMainUnitID());
                        generalLedger3.setMainQuantity(aLstDT.getMainQuantity());
                        generalLedger3.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        generalLedger3.setMainConvertRate(aLstDT.getMainConvertRate());
                        generalLedger3.setFormula(aLstDT.getFormula());

                        generalLedger3.refDateTime(saInvoice.getDate());
                        generalLedger3.budgetItemID(aLstDT.getBudgetItemID());
                        generalLedger3.costSetID(aLstDT.getCostSetID());
                        generalLedger3.contractID(aLstDT.getContractID());
                        generalLedger3.statisticsCodeID(aLstDT.getStatisticsCodeID());
                        generalLedger3.expenseItemID(aLstDT.getExpenseItemID());
                        if (mcReceiptOptional.isPresent()) {
                            generalLedger3.setNoFBook(mcReceiptOptional.get().getNoFBook());
                            generalLedger3.setNoMBook(mcReceiptOptional.get().getNoMBook());
                        } else if (mbDepositOptional.isPresent()) {
                            generalLedger3.setNoFBook(mbDepositOptional.get().getNoFBook());
                            generalLedger3.setNoMBook(mbDepositOptional.get().getNoMBook());
                            generalLedger3.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                            generalLedger3.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                            generalLedger3.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                        }
                        listGenTemp.add(generalLedger3);

                        GeneralLedger generalLedgerCorresponding3 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding3, generalLedger3);
                        generalLedgerCorresponding3.setTypeID(saInvoice.getTypeID());
                        generalLedgerCorresponding3.account(aLstDT.getDebitAccount());
                        generalLedgerCorresponding3.accountCorresponding(aLstDT.getDiscountAccount());
                        generalLedgerCorresponding3.creditAmount(aLstDT.getDiscountAmount());
                        generalLedgerCorresponding3.creditAmountOriginal(aLstDT.getDiscountAmountOriginal());
                        generalLedgerCorresponding3.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding3.debitAmountOriginal(BigDecimal.ZERO);
                        listGenTemp.add(generalLedgerCorresponding3);
                    }
                }
                if (Boolean.TRUE.equals(saInvoice.isExported())) {
                    if (aLstDT.getExportTaxAmount() != null && aLstDT.getExportTaxAmount().doubleValue() != 0) {
                        GeneralLedger generalLedger1 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedger1, saInvoice);
                        generalLedger1.setTypeID(saInvoice.getTypeID());
                        generalLedger1.account(aLstDT.getExportTaxAccountCorresponding());
                        generalLedger1.accountCorresponding(aLstDT.getExportTaxAmountAccount());
                        generalLedger1.debitAmount(aLstDT.getExportTaxAmount());
                        generalLedger1.debitAmountOriginal(aLstDT.getExportTaxAmount());
                        generalLedger1.creditAmount(BigDecimal.ZERO);
                        generalLedger1.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger1.description((DESCRIPTION_VAT.EXPORT_TAX + materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null)));
                        generalLedger1.accountingObjectID(aLstDT.getAccountingObjectID());
                        generalLedger1.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger1.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger1.setReferenceID(saInvoice.getId());
                        generalLedger1.employeeID(saInvoice.getEmployeeID());
                        generalLedger1.setEmployeeCode(employee.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger1.setEmployeeName(employee.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger1.detailID(aLstDT.getId());

                        generalLedger1.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                        generalLedger1.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                        generalLedger1.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                        generalLedger1.setRepositoryID(aLstDT.getRepositoryID());
                        generalLedger1.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                        generalLedger1.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                        generalLedger1.setUnitID(aLstDT.getUnitID());
                        generalLedger1.setQuantity(aLstDT.getQuantity());
                        generalLedger1.setUnitPrice(aLstDT.getUnitPrice());
                        generalLedger1.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                        generalLedger1.setMainUnitID(aLstDT.getMainUnitID());
                        generalLedger1.setMainQuantity(aLstDT.getMainQuantity());
                        generalLedger1.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        generalLedger1.setMainConvertRate(aLstDT.getMainConvertRate());
                        generalLedger1.setFormula(aLstDT.getFormula());

                        generalLedger1.refDateTime(saInvoice.getDate());
                        generalLedger1.budgetItemID(aLstDT.getBudgetItemID());
                        generalLedger1.costSetID(aLstDT.getCostSetID());
                        generalLedger1.contractID(aLstDT.getContractID());
                        generalLedger1.statisticsCodeID(aLstDT.getStatisticsCodeID());
                        generalLedger1.expenseItemID(aLstDT.getExpenseItemID());
                        if (mcReceiptOptional.isPresent()) {
                            generalLedger1.setNoFBook(mcReceiptOptional.get().getNoFBook());
                            generalLedger1.setNoMBook(mcReceiptOptional.get().getNoMBook());
                        } else if (mbDepositOptional.isPresent()) {
                            generalLedger1.setNoFBook(mbDepositOptional.get().getNoFBook());
                            generalLedger1.setNoMBook(mbDepositOptional.get().getNoMBook());
                            generalLedger1.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                            generalLedger1.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                            generalLedger1.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                        }
                        listGenTemp.add(generalLedger1);

                        GeneralLedger generalLedgerCorresponding1 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding1, generalLedger1);
                        generalLedgerCorresponding1.setTypeID(saInvoice.getTypeID());
                        generalLedgerCorresponding1.account(aLstDT.getExportTaxAmountAccount());
                        generalLedgerCorresponding1.accountCorresponding(aLstDT.getExportTaxAccountCorresponding());
                        generalLedgerCorresponding1.creditAmount(aLstDT.getExportTaxAmount());
                        generalLedgerCorresponding1.creditAmountOriginal(aLstDT.getExportTaxAmount());
                        generalLedgerCorresponding1.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding1.debitAmountOriginal(BigDecimal.ZERO);
                        listGenTemp.add(generalLedgerCorresponding1);
                    }
                } else {
                    if (aLstDT.getvATAmountOriginal() != null && aLstDT.getvATAmountOriginal().doubleValue() != 0) {
                        GeneralLedger generalLedger2 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedger2, saInvoice);
                        generalLedger2.setTypeID(saInvoice.getTypeID());
                        generalLedger2.account(aLstDT.getDeductionDebitAccount());
                        generalLedger2.accountCorresponding(aLstDT.getvATAccount());
                        generalLedger2.debitAmount(aLstDT.getvATAmount());
                        generalLedger2.debitAmountOriginal(aLstDT.getvATAmountOriginal());
                        generalLedger2.creditAmount(BigDecimal.ZERO);
                        generalLedger2.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger2.description(aLstDT.getvATDescription());
                        generalLedger2.accountingObjectID(aLstDT.getAccountingObjectID());
                        generalLedger2.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger2.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger2.setReferenceID(saInvoice.getId());
                        generalLedger2.employeeID(saInvoice.getEmployeeID());
                        generalLedger2.setEmployeeCode(employee.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger2.setEmployeeName(employee.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger2.detailID(aLstDT.getId());

                        generalLedger2.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                        generalLedger2.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                        generalLedger2.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                        generalLedger2.setRepositoryID(aLstDT.getRepositoryID());
                        generalLedger2.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                        generalLedger2.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                        generalLedger2.setUnitID(aLstDT.getUnitID());
                        generalLedger2.setQuantity(aLstDT.getQuantity());
                        generalLedger2.setUnitPrice(aLstDT.getUnitPrice());
                        generalLedger2.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                        generalLedger2.setMainUnitID(aLstDT.getMainUnitID());
                        generalLedger2.setMainQuantity(aLstDT.getMainQuantity());
                        generalLedger2.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        generalLedger2.setMainConvertRate(aLstDT.getMainConvertRate());
                        generalLedger2.setFormula(aLstDT.getFormula());

                        generalLedger2.refDateTime(saInvoice.getDate());
                        generalLedger2.budgetItemID(aLstDT.getBudgetItemID());
                        generalLedger2.costSetID(aLstDT.getCostSetID());
                        generalLedger2.contractID(aLstDT.getContractID());
                        generalLedger2.statisticsCodeID(aLstDT.getStatisticsCodeID());
                        generalLedger2.expenseItemID(aLstDT.getExpenseItemID());
                        if (mcReceiptOptional.isPresent()) {
                            generalLedger2.setNoFBook(mcReceiptOptional.get().getNoFBook());
                            generalLedger2.setNoMBook(mcReceiptOptional.get().getNoMBook());
                        } else if (mbDepositOptional.isPresent()) {
                            generalLedger2.setNoFBook(mbDepositOptional.get().getNoFBook());
                            generalLedger2.setNoMBook(mbDepositOptional.get().getNoMBook());
                            generalLedger2.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                            generalLedger2.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                            generalLedger2.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                        }
                        listGenTemp.add(generalLedger2);

                        GeneralLedger generalLedgerCorresponding2 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding2, generalLedger2);
                        generalLedgerCorresponding2.setTypeID(saInvoice.getTypeID());
                        generalLedgerCorresponding2.account(aLstDT.getvATAccount());
                        generalLedgerCorresponding2.accountCorresponding(aLstDT.getDeductionDebitAccount());
                        generalLedgerCorresponding2.creditAmount(aLstDT.getvATAmount());
                        generalLedgerCorresponding2.creditAmountOriginal(aLstDT.getvATAmountOriginal());
                        generalLedgerCorresponding2.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding2.debitAmountOriginal(BigDecimal.ZERO);
                        listGenTemp.add(generalLedgerCorresponding2);
                    }
                }
                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    rSInwardOutward = rSInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID());
                    if (rSInwardOutward.isPresent()) {
                        GeneralLedger generalLedger4 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedger4, saInvoice);
                        generalLedger4.setTypeID(411);
                        if (rSInwardOutward.get().getNoFBook() != null) {
                            generalLedger4.setNoFBook(rSInwardOutward.get().getNoFBook());
                        }
                        if (rSInwardOutward.get().getNoMBook() != null) {
                            generalLedger4.setNoMBook(rSInwardOutward.get().getNoMBook());
                        }
                        generalLedger4.account(aLstDT.getCostAccount());
                        generalLedger4.accountCorresponding(aLstDT.getRepositoryAccount());
                        generalLedger4.debitAmount(aLstDT.getoWAmount());
                        generalLedger4.debitAmountOriginal(aLstDT.getoWAmount());
                        generalLedger4.creditAmount(BigDecimal.ZERO);
                        generalLedger4.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger4.description(aLstDT.getDescription());
                        generalLedger4.accountingObjectID(aLstDT.getAccountingObjectID());
                        generalLedger4.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger4.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger4.setReferenceID(saInvoice.getId());
                        generalLedger4.employeeID(saInvoice.getEmployeeID());
                        generalLedger4.setEmployeeCode(employee.map(AccountingObject::getAccountingObjectCode).orElse(null));
                        generalLedger4.setEmployeeName(employee.map(AccountingObject::getAccountingObjectName).orElse(null));
                        generalLedger4.detailID(aLstDT.getId());

                        generalLedger4.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                        generalLedger4.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                        generalLedger4.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                        generalLedger4.setRepositoryID(aLstDT.getRepositoryID());
                        generalLedger4.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                        generalLedger4.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                        generalLedger4.setUnitID(aLstDT.getUnitID());
                        generalLedger4.setQuantity(aLstDT.getQuantity());
                        generalLedger4.setUnitPrice(aLstDT.getUnitPrice());
                        generalLedger4.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                        generalLedger4.setMainUnitID(aLstDT.getMainUnitID());
                        generalLedger4.setMainQuantity(aLstDT.getMainQuantity());
                        generalLedger4.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        generalLedger4.setMainConvertRate(aLstDT.getMainConvertRate());
                        generalLedger4.setFormula(aLstDT.getFormula());

                        generalLedger4.refDateTime(saInvoice.getDate());
                        generalLedger4.budgetItemID(aLstDT.getBudgetItemID());
                        generalLedger4.costSetID(aLstDT.getCostSetID());
                        generalLedger4.contractID(aLstDT.getContractID());
                        generalLedger4.statisticsCodeID(aLstDT.getStatisticsCodeID());
                        generalLedger4.expenseItemID(aLstDT.getExpenseItemID());
                        listGenTemp.add(generalLedger4);

                        GeneralLedger generalLedgerCorresponding4 = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding4, generalLedger4);
                        generalLedgerCorresponding4.setTypeID(411);
                        generalLedgerCorresponding4.account(aLstDT.getRepositoryAccount());
                        generalLedgerCorresponding4.accountCorresponding(aLstDT.getCostAccount());
                        generalLedgerCorresponding4.creditAmount(aLstDT.getoWAmount());
                        generalLedgerCorresponding4.creditAmountOriginal(aLstDT.getoWAmount());
                        generalLedgerCorresponding4.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding4.debitAmountOriginal(BigDecimal.ZERO);
                        listGenTemp.add(generalLedgerCorresponding4);


                        RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                        repositoryLedgerItem.setReferenceID(rSInwardOutward.get().getId());
                        repositoryLedgerItem.setCompanyID(saInvoice.getCompanyID());
                        if (rSInwardOutward.get().getNoFBook() != null) {
                            repositoryLedgerItem.setNoFBook(rSInwardOutward.get().getNoFBook());
                        }
                        if (rSInwardOutward.get().getNoMBook() != null) {
                            repositoryLedgerItem.setNoMBook(rSInwardOutward.get().getNoMBook());
                        }
                        repositoryLedgerItem.setTypeID(rSInwardOutward.get().getTypeID());
                        repositoryLedgerItem.setOwQuantity(aLstDT.getQuantity());
                        repositoryLedgerItem.setUnitPrice(aLstDT.getoWPrice());
                        repositoryLedgerItem.setOwAmount(aLstDT.getoWAmount());
                        repositoryLedgerItem.setAccount(aLstDT.getRepositoryAccount());
                        repositoryLedgerItem.setAccountCorresponding(aLstDT.getCostAccount());
                        repositoryLedgerItem.setDetailID(aLstDT.getId());
                        repositoryLedgerItem.setFormula(aLstDT.getFormula());
                        repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGoodsID());
                        repositoryLedgerItem.setRepositoryID(aLstDT.getRepositoryID());
                        repositoryLedgerItem.setLotNo(aLstDT.getLotNo());
                        repositoryLedgerItem.setDescription(aLstDT.getDescription());
                        repositoryLedgerItem.setReason(rSInwardOutward.get().getReason());
                        repositoryLedgerItem.setDate(rSInwardOutward.get().getDate());
                        repositoryLedgerItem.setPostedDate(rSInwardOutward.get().getPostedDate());
                        repositoryLedgerItem.setExpiryDate(aLstDT.getExpiryDate());
                        repositoryLedgerItem.setTypeLedger(rSInwardOutward.get().getTypeLedger());
                        repositoryLedgerItem.setUnitID(aLstDT.getUnitID());
                        repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnitID());
                        repositoryLedgerItem.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        repositoryLedgerItem.setMainOWQuantity(aLstDT.getMainQuantity());
                        repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
                        repositoryLedgerItem.setBudgetItemID(aLstDT.getBudgetItemID());
                        repositoryLedgerItem.setCostSetID(aLstDT.getCostSetID());
                        repositoryLedgerItem.setStatisticsCodeID(aLstDT.getStatisticsCodeID());
                        repositoryLedgerItem.setExpenseItemID(aLstDT.getExpenseItemID());
                        repositoryLedgerItem.setConfrontID(aLstDT.getConfrontID());
                        repositoryLedgerItem.setConfrontDetailID(aLstDT.getConfrontDetailID());
                        if (aLstDT.getRepositoryID() != null) {
                            Optional<Repository> repository = repositoryRepository.findById(aLstDT.getRepositoryID());
                            // repository
                            if (repository.isPresent()) {
                                repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                                repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                            }
                        }
                        // materialGoods
                        Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(aLstDT.getMaterialGoodsID());
                        if (materialGoods.isPresent()) {
                            repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                            repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                        }

                        repositoryLedgers.add(repositoryLedgerItem);
                    }
                }


            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.saveAll(repositoryLedgers);
        return listGenTemp;
    }

    List<GeneralLedger> genGeneralLedgers(PPInvoice ppInvoice) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<PPInvoiceDetails> lstDT = new ArrayList<>(ppInvoice.getPpInvoiceDetails());
        lstDT.sort(Comparator.comparingInt(PPInvoiceDetails::getOrderPriority));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        Optional<RSInwardOutward> rsInwardOutwardOptional = Optional.empty();
        if (ppInvoice.isStoredInRepository() && ppInvoice.getRsInwardOutwardId() != null) {
            rsInwardOutwardOptional = rSInwardOutwardRepository.findById(ppInvoice.getRsInwardOutwardId());
        }
        for (PPInvoiceDetails aLstDT : lstDT) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                // cặp nợ có
                BeanUtils.copyProperties(generalLedger, ppInvoice);

                switch (ppInvoice.getTypeId()) {
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                        Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mcPaymentOptional.isPresent()) {
                            generalLedger.setNoFBook(mcPaymentOptional.get().getNoFBook());
                            generalLedger.setNoMBook(mcPaymentOptional.get().getNoMBook());
                        }
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                        Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mbTellerPaperOptional.isPresent()) {
                            generalLedger.setNoFBook(mbTellerPaperOptional.get().getNoFBook());
                            generalLedger.setNoMBook(mbTellerPaperOptional.get().getNoMBook());
                            generalLedger.setBankAccountDetailID(mbTellerPaperOptional.get().getBankAccountDetailID());
                            if (mbTellerPaperOptional.get().getBankAccountDetailID() != null) {
                                Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(mbTellerPaperOptional.get().getBankAccountDetailID());
                                if (bankAccountDetails.isPresent()) {
                                    generalLedger.setBankAccount(bankAccountDetails.get().getBankAccount());
                                    generalLedger.bankName(bankAccountDetails.get().getBankName());
                                }
                            }
                        }
                        break;
                    case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                        Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoice.getPaymentVoucherId());
                        if (mbCreditCardOptional.isPresent()) {
                            generalLedger.setNoFBook(mbCreditCardOptional.get().getNoFBook());
                            generalLedger.setNoMBook(mbCreditCardOptional.get().getNoMBook());
                            Optional<CreditCard> creditCardOptional = creditCardRepository.findByCreditCardNumberAndCompanyID(mbCreditCardOptional.get().getCreditCardNumber(), ppInvoice.getCompanyId());
                            if (creditCardOptional.isPresent() && creditCardOptional.get().getBankIDIssueCard() != null) {
                                Optional<Bank> bankOptional = bankRepository.findById(creditCardOptional.get().getBankIDIssueCard());
                                bankOptional.ifPresent(bank -> generalLedger.bankName(bank.getBankName()));
                                generalLedger.setBankAccountDetailID(creditCardOptional.get().getId());
                                generalLedger.setBankAccount(mbCreditCardOptional.get().getCreditCardNumber());
                            }
                        }
                        break;
                }

                generalLedger.setTypeID(ppInvoice.getTypeId());
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                BigDecimal debitAmount = null;
                if (aLstDT.getDiscountAmount() != null) {
                    debitAmount = aLstDT.getAmount().subtract(aLstDT.getDiscountAmount());
                } else {
                    debitAmount = aLstDT.getAmount();
                }
                BigDecimal debitAmountOriginal = null;
                if (aLstDT.getDiscountAmountOriginal() != null) {
                    debitAmountOriginal = aLstDT.getAmountOriginal().subtract(aLstDT.getDiscountAmountOriginal());
                } else {
                    debitAmountOriginal = aLstDT.getAmountOriginal();
                }

                generalLedger.debitAmount(debitAmount);
                generalLedger.debitAmountOriginal(debitAmountOriginal);
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());

                Optional<AccountingObject> accObj = aLstDT.getAccountingObjectId() != null ? accountingObjectRepository.findOneById(aLstDT.getAccountingObjectId()) : Optional.empty();
                generalLedger.accountingObjectID(aLstDT.getAccountingObjectId());
                generalLedger.accountingObjectName(accObj.map(AccountingObject::getAccountingObjectName).orElse(null));
                generalLedger.accountingObjectCode(accObj.map(AccountingObject::getAccountingObjectCode).orElse(null));

                generalLedger.setReferenceID(ppInvoice.getId());

                generalLedger.detailID(aLstDT.getId());
                generalLedger.refDateTime(ppInvoice.getDate());
                generalLedger.budgetItemID(aLstDT.getBudgetItemId());
                generalLedger.costSetID(aLstDT.getCostSetId());
                generalLedger.contractID(aLstDT.getContractId());
                generalLedger.statisticsCodeID(aLstDT.getStatisticCodeId());
                generalLedger.expenseItemID(aLstDT.getExpenseItemId());
                generalLedger.setCompanyID(ppInvoice.getCompanyId());

                Optional<MaterialGoods> materialGoodsOptional = aLstDT.getMaterialGoodsId() != null ? materialGoodsRepository.findById(aLstDT.getMaterialGoodsId()) : Optional.empty();
                generalLedger.setMaterialGoodsID(aLstDT.getMaterialGoodsId());
                generalLedger.setMaterialGoodsCode(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsCode).orElse(null));
                generalLedger.setMaterialGoodsName(materialGoodsOptional.map(MaterialGoods::getMaterialGoodsName).orElse(null));

                Optional<Repository> repositoryOptional = aLstDT.getRepositoryId() != null ? repositoryRepository.findById(aLstDT.getRepositoryId()) : Optional.empty();
                generalLedger.setRepositoryID(aLstDT.getRepositoryId());
                generalLedger.setRepositoryCode(repositoryOptional.map(Repository::getRepositoryCode).orElse(null));
                generalLedger.setRepositoryName(repositoryOptional.map(Repository::getRepositoryName).orElse(null));

                Optional<AccountingObject> employeeOptional = ppInvoice.getEmployeeId() != null ? accountingObjectRepository.findById(ppInvoice.getEmployeeId()) : Optional.empty();
                generalLedger.employeeID(ppInvoice.getEmployeeId());
                generalLedger.setEmployeeCode(employeeOptional.map(AccountingObject::getAccountingObjectCode).orElse(null));
                generalLedger.setEmployeeName(employeeOptional.map(AccountingObject::getAccountingObjectName).orElse(null));

                generalLedger.setUnitID(aLstDT.getUnitId());
                generalLedger.setCurrencyID(ppInvoice.getCurrencyId());
                generalLedger.quantity(aLstDT.getQuantity());
                generalLedger.setUnitPrice(aLstDT.getUnitPrice());
                generalLedger.setUnitPriceOriginal(aLstDT.getUnitPriceOriginal());
                generalLedger.setMainUnitID(aLstDT.getMainUnitId());
                generalLedger.setMainQuantity(aLstDT.getMainQuantity());
                generalLedger.setMainUnitPrice(aLstDT.getMainUnitPrice());
                generalLedger.setMainConvertRate(aLstDT.getMainConvertRate());
                generalLedger.setFormula(aLstDT.getFormula());

                generalLedger.departmentID(aLstDT.getDepartmentId());

                // thành tiền lớn hơn 0 ms lưu
                if (aLstDT.getAmount() != null && aLstDT.getAmount().doubleValue() != 0) {
                    listGenTemp.add(generalLedger);
                }

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.setTypeID(ppInvoice.getTypeId());
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());

                generalLedgerCorresponding.creditAmount(debitAmount);
                generalLedgerCorresponding.creditAmountOriginal(debitAmountOriginal);
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                if (aLstDT.getAmount() != null && aLstDT.getAmount().doubleValue() != 0) {
                    listGenTemp.add(generalLedgerCorresponding);
                }

                if (ppInvoice.isImportPurchase()) {
                    // nợ - thuế nhập khẩu
                    if (aLstDT.getImportTaxAmount() != null && aLstDT.getImportTaxAmount().doubleValue() != 0) {
                        GeneralLedger genTempVATCorrespondingImportPurchase = new GeneralLedger();
                        BeanUtils.copyProperties(genTempVATCorrespondingImportPurchase, generalLedger);
                        genTempVATCorrespondingImportPurchase.setTypeID(ppInvoice.getTypeId());
                        genTempVATCorrespondingImportPurchase.account(aLstDT.getDebitAccount());
                        genTempVATCorrespondingImportPurchase.accountCorresponding(aLstDT.getImportTaxAccount());
                        genTempVATCorrespondingImportPurchase.debitAmount(aLstDT.getImportTaxAmount());
                        genTempVATCorrespondingImportPurchase.debitAmountOriginal(aLstDT.getImportTaxAmountOriginal());
                        genTempVATCorrespondingImportPurchase.creditAmount(BigDecimal.ZERO);
                        genTempVATCorrespondingImportPurchase.creditAmountOriginal(BigDecimal.ZERO);

                        listGenTemp.add(genTempVATCorrespondingImportPurchase);

                        GeneralLedger genTempVATImportPurchase = new GeneralLedger();
                        BeanUtils.copyProperties(genTempVATImportPurchase, generalLedger);
                        genTempVATImportPurchase.setTypeID(ppInvoice.getTypeId());
                        genTempVATImportPurchase.account(aLstDT.getImportTaxAccount());
                        genTempVATImportPurchase.accountCorresponding(aLstDT.getDebitAccount());
                        genTempVATImportPurchase.debitAmount(BigDecimal.ZERO);
                        genTempVATImportPurchase.debitAmountOriginal(BigDecimal.ZERO);
                        genTempVATImportPurchase.creditAmount(aLstDT.getImportTaxAmount());
                        genTempVATImportPurchase.creditAmountOriginal(aLstDT.getImportTaxAmountOriginal());

                        listGenTemp.add(genTempVATImportPurchase);
                    }
                }

                // cặp nợ thuế tiêu thụ đặc biệt
                if (aLstDT.getSpecialConsumeTaxAmount() != null && aLstDT.getSpecialConsumeTaxAmount().doubleValue() != 0) {
                    GeneralLedger genTempTTDBCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(genTempTTDBCorresponding, generalLedger);
                    genTempTTDBCorresponding.setTypeID(ppInvoice.getTypeId());
                    genTempTTDBCorresponding.account(aLstDT.getDebitAccount());
                    genTempTTDBCorresponding.accountCorresponding(aLstDT.getSpecialConsumeTaxAccount());
                    genTempTTDBCorresponding.debitAmount(aLstDT.getSpecialConsumeTaxAmount());
                    genTempTTDBCorresponding.debitAmountOriginal(aLstDT.getSpecialConsumeTaxAmountOriginal());
                    genTempTTDBCorresponding.creditAmount(BigDecimal.ZERO);
                    genTempTTDBCorresponding.creditAmountOriginal(BigDecimal.ZERO);

                    listGenTemp.add(genTempTTDBCorresponding);

                    GeneralLedger genTempTTDB = new GeneralLedger();
                    BeanUtils.copyProperties(genTempTTDB, generalLedger);
                    genTempTTDB.setTypeID(ppInvoice.getTypeId());
                    genTempTTDB.account(aLstDT.getSpecialConsumeTaxAccount());
                    genTempTTDB.accountCorresponding(aLstDT.getDebitAccount());
                    genTempTTDB.debitAmount(BigDecimal.ZERO);
                    genTempTTDB.debitAmountOriginal(BigDecimal.ZERO);
                    genTempTTDB.creditAmount(aLstDT.getSpecialConsumeTaxAmount());
                    genTempTTDB.creditAmountOriginal(aLstDT.getSpecialConsumeTaxAmountOriginal());

                    listGenTemp.add(genTempTTDB);
                }

                // nợ - thuế giá trị gia tăng
                if (aLstDT.getVatAmount() != null && aLstDT.getVatAmount().doubleValue() != 0) {
                    GeneralLedger genTempVAT = new GeneralLedger();
                    BeanUtils.copyProperties(genTempVAT, generalLedger);
                    genTempVAT.setTypeID(ppInvoice.getTypeId());
                    if (ppInvoice.isImportPurchase()) {
                        genTempVAT.account(aLstDT.getDeductionDebitAccount());
                        genTempVAT.accountCorresponding(aLstDT.getVatAccount());
                        genTempVAT.debitAmount(aLstDT.getVatAmount());
                        genTempVAT.debitAmountOriginal(aLstDT.getVatAmountOriginal());
                        genTempVAT.creditAmount(BigDecimal.ZERO);
                        genTempVAT.creditAmountOriginal(BigDecimal.ZERO);
                    } else {
                        genTempVAT.account(aLstDT.getVatAccount());
                        genTempVAT.accountCorresponding(aLstDT.getDeductionDebitAccount());
                        genTempVAT.debitAmount(aLstDT.getVatAmount());
                        genTempVAT.debitAmountOriginal(aLstDT.getVatAmountOriginal());
                        genTempVAT.creditAmount(BigDecimal.ZERO);
                        genTempVAT.creditAmountOriginal(BigDecimal.ZERO);
                    }
                    genTempVAT.description(aLstDT.getVatDescription());
                    listGenTemp.add(genTempVAT);

                    GeneralLedger genTempVATCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(genTempVATCorresponding, generalLedger);
                    genTempVATCorresponding.setTypeID(ppInvoice.getTypeId());

                    if (ppInvoice.isImportPurchase()) {
                        genTempVATCorresponding.account(aLstDT.getVatAccount());
                        genTempVATCorresponding.accountCorresponding(aLstDT.getDeductionDebitAccount());
                        genTempVATCorresponding.debitAmount(BigDecimal.ZERO);
                        genTempVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                        genTempVATCorresponding.creditAmount(aLstDT.getVatAmount());
                        genTempVATCorresponding.creditAmountOriginal(aLstDT.getVatAmountOriginal());
                    } else {
                        genTempVATCorresponding.account(aLstDT.getDeductionDebitAccount());
                        genTempVATCorresponding.accountCorresponding(aLstDT.getVatAccount());
                        genTempVATCorresponding.debitAmount(BigDecimal.ZERO);
                        genTempVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                        genTempVATCorresponding.creditAmount(aLstDT.getVatAmount());
                        genTempVATCorresponding.creditAmountOriginal(aLstDT.getVatAmountOriginal());
                    }
                    genTempVATCorresponding.description(aLstDT.getVatDescription());

                    listGenTemp.add(genTempVATCorresponding);
                }


//                if (ppInvoice.getTypeId() != Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN && ppInvoice.getPaymentVoucherId() != null) {
//                    GeneralLedger generalPaymentVoucher = new GeneralLedger();
//                    BeanUtils.copyProperties(generalPaymentVoucher, generalLedger);
//                    generalPaymentVoucher.setReferenceID(ppInvoice.getPaymentVoucherId());
//
//                    switch (ppInvoice.getTypeId()) {
//                        case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
//                            Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoice.getPaymentVoucherId());
//                            if (mcPaymentOptional.isPresent()) {
//                                generalPaymentVoucher.setNoFBook(mcPaymentOptional.get().getNoFBook());
//                                generalPaymentVoucher.setNoMBook(mcPaymentOptional.get().getNoMBook());
//                                generalPaymentVoucher.setTypeID(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG);
//                            }
//                            break;
//                        case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
//                            Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoice.getPaymentVoucherId());
//                            if (mbTellerPaperOptional.isPresent()) {
//                                generalPaymentVoucher.setNoFBook(mbTellerPaperOptional.get().getNoFBook());
//                                generalPaymentVoucher.setNoMBook(mbTellerPaperOptional.get().getNoMBook());
//                                generalPaymentVoucher.setTypeID(Constants.PPInvoiceType.TYPE_ID_UY_NHIEM_CHI_MUA_HANG);
//                            }
//                             break;
//                        case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
//                            Optional<MBTellerPaper> mbTellerPaperOptionalSecCk = mbTellerPaperRepository.findById(ppInvoice.getPaymentVoucherId());
//                            if (mbTellerPaperOptionalSecCk.isPresent()) {
//                                generalPaymentVoucher.setNoFBook(mbTellerPaperOptionalSecCk.get().getNoFBook());
//                                generalPaymentVoucher.setNoMBook(mbTellerPaperOptionalSecCk.get().getNoMBook());
//                                generalPaymentVoucher.setTypeID(Constants.PPInvoiceType.TYPE_ID_SEC_CHUYEN_KHOAN_MUA_HANG);
//                            }
//                             break;
//                        case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
//                            Optional<MBTellerPaper> mbTellerPaperOptionalSecTm = mbTellerPaperRepository.findById(ppInvoice.getPaymentVoucherId());
//                            if (mbTellerPaperOptionalSecTm.isPresent()) {
//                                generalPaymentVoucher.setNoFBook(mbTellerPaperOptionalSecTm.get().getNoFBook());
//                                generalPaymentVoucher.setNoMBook(mbTellerPaperOptionalSecTm.get().getNoMBook());
//                                generalPaymentVoucher.setTypeID(Constants.PPInvoiceType.TYPE_ID_SEC_TIEN_MAT_MUA_HANG);
//                            }
//                             break;
//                        case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
//                            Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoice.getPaymentVoucherId());
//                            if (mbCreditCardOptional.isPresent()) {
//                                generalPaymentVoucher.setNoFBook(mbCreditCardOptional.get().getNoFBook());
//                                generalPaymentVoucher.setNoMBook(mbCreditCardOptional.get().getNoMBook());
//                                generalPaymentVoucher.setTypeID(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG);
//                            }
//                            break;
//                    }
//                    listGenTemp.add(generalPaymentVoucher);
//
//                    //
//                    GeneralLedger generalLedgerCorrespondingPaymentVoucher = new GeneralLedger();
//                    BeanUtils.copyProperties(generalLedgerCorrespondingPaymentVoucher, generalPaymentVoucher);
//                    generalLedgerCorrespondingPaymentVoucher.account(aLstDT.getCreditAccount());
//                    generalLedgerCorrespondingPaymentVoucher.accountCorresponding(aLstDT.getDebitAccount());
//                    generalLedgerCorrespondingPaymentVoucher.creditAmount(aLstDT.getAmount());
//                    generalLedgerCorrespondingPaymentVoucher.creditAmountOriginal(aLstDT.getAmountOriginal());
//                    generalLedgerCorrespondingPaymentVoucher.debitAmount(BigDecimal.ZERO);
//                    generalLedgerCorrespondingPaymentVoucher.debitAmountOriginal(BigDecimal.ZERO);
//                    listGenTemp.add(generalLedgerCorrespondingPaymentVoucher);
//
//
//                }

                if (ppInvoice.isStoredInRepository() && rsInwardOutwardOptional.isPresent()) {
                    if (materialGoodsOptional.isPresent() && (materialGoodsOptional.get().getMaterialGoodsType() == null || !materialGoodsOptional.get().getMaterialGoodsType().equals(Constants.MaterialGoodsType.SERVICE)) && aLstDT.getRepositoryId() != null) {
                        RSInwardOutward rsInwardOutward = rsInwardOutwardOptional.get();
//                    //
//                    GeneralLedger generalRsi = new GeneralLedger();
//                    BeanUtils.copyProperties(generalRsi, generalLedger);
//                    generalRsi.setTypeID(rsInwardOutward.getTypeID());
//                    generalRsi.setReferenceID(rsInwardOutward.getId());
//                    generalRsi.setNoFBook(rsInwardOutward.getNoFBook());
//                    generalRsi.setNoMBook(rsInwardOutward.getNoMBook());
//
//                    listGenTemp.add(generalRsi);
//
//                    //
//                    GeneralLedger generalLedgerCorrespondingRsi = new GeneralLedger();
//                    BeanUtils.copyProperties(generalLedgerCorrespondingRsi, generalRsi);
//                    generalLedgerCorrespondingRsi.setTypeID(rsInwardOutward.getTypeID());
//                    generalLedgerCorrespondingRsi.account(aLstDT.getCreditAccount());
//                    generalLedgerCorrespondingRsi.accountCorresponding(aLstDT.getDebitAccount());
//                    generalLedgerCorrespondingRsi.creditAmount(aLstDT.getAmount());
//                    generalLedgerCorrespondingRsi.creditAmountOriginal(aLstDT.getAmountOriginal());
//                    generalLedgerCorrespondingRsi.debitAmount(BigDecimal.ZERO);
//                    generalLedgerCorrespondingRsi.debitAmountOriginal(BigDecimal.ZERO);
//                    listGenTemp.add(generalLedgerCorrespondingRsi);


                        //  lưu repository
                        RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                        repositoryLedgerItem.setReferenceID(rsInwardOutward.getId());
                        repositoryLedgerItem.setCompanyID(ppInvoice.getCompanyId());
                        if (rsInwardOutward.getNoFBook() != null) {
                            repositoryLedgerItem.setNoFBook(rsInwardOutward.getNoFBook());
                        }
                        if (rsInwardOutward.getNoMBook() != null) {
                            repositoryLedgerItem.setNoMBook(rsInwardOutward.getNoMBook());
                        }
                        repositoryLedgerItem.setIwQuantity(aLstDT.getQuantity());
                        repositoryLedgerItem.setIwAmount(aLstDT.getInwardAmount());
                        if (repositoryLedgerItem.getIwAmount() != null && repositoryLedgerItem.getIwQuantity() != null && repositoryLedgerItem.getIwQuantity().doubleValue() != 0) {
                            repositoryLedgerItem.setUnitPrice(repositoryLedgerItem.getIwAmount().divide(repositoryLedgerItem.getIwQuantity(), MathContext.DECIMAL32));
                        } else {
                            repositoryLedgerItem.setUnitPrice(BigDecimal.ZERO);
                        }

                        repositoryLedgerItem.setAccount(aLstDT.getDebitAccount());
                        repositoryLedgerItem.setAccountCorresponding(aLstDT.getCreditAccount());
                        repositoryLedgerItem.setDetailID(aLstDT.getId());
                        repositoryLedgerItem.setFormula(aLstDT.getFormula());
                        repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGoodsId());
                        repositoryLedgerItem.setRepositoryID(aLstDT.getRepositoryId());
                        repositoryLedgerItem.setLotNo(aLstDT.getLotNo());
                        repositoryLedgerItem.setDescription(aLstDT.getDescription());
                        repositoryLedgerItem.setReason(rsInwardOutward.getReason());
                        repositoryLedgerItem.setDate(rsInwardOutward.getDate());
                        repositoryLedgerItem.setPostedDate(rsInwardOutward.getPostedDate());
                        repositoryLedgerItem.setExpiryDate(aLstDT.getExpiryDate());
                        repositoryLedgerItem.setTypeLedger(rsInwardOutward.getTypeLedger());
                        repositoryLedgerItem.setUnitID(aLstDT.getUnitId());
                        repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnitId());
                        repositoryLedgerItem.setMainUnitPrice(aLstDT.getMainUnitPrice());
                        repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
                        repositoryLedgerItem.setBudgetItemID(aLstDT.getBudgetItemId());
                        repositoryLedgerItem.setCostSetID(aLstDT.getCostSetId());
                        repositoryLedgerItem.setStatisticsCodeID(aLstDT.getStatisticCodeId());
                        repositoryLedgerItem.setExpenseItemID(aLstDT.getExpenseItemId());
                        Optional<Repository> repository = aLstDT.getRepositoryId() != null ? repositoryRepository.findById(aLstDT.getRepositoryId()) : Optional.empty();
                        // repository
                        if (repository.isPresent()) {
                            repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                            repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                        }
                        // materialGoods
                        Optional<MaterialGoods> materialGoods = aLstDT.getMaterialGoodsId() != null ? materialGoodsRepository.findById(aLstDT.getMaterialGoodsId()) : Optional.empty();
                        if (materialGoods.isPresent()) {
                            repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                            repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                        }
                        repositoryLedgerItem.setMainIWQuantity(aLstDT.getMainQuantity());
                        repositoryLedgerItem.setTypeID(rsInwardOutward.getTypeID());


                        repositoryLedgers.add(repositoryLedgerItem);
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.saveAll(repositoryLedgers);
        return listGenTemp;
    }

    List<GeneralLedger> genGeneralLedgers(RSInwardOutward rsInwardOutward) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<RSInwardOutWardDetails> lstDT = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
        lstDT.sort(Comparator.comparingInt(RSInwardOutWardDetails::getOrderPriority));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (RSInwardOutWardDetails aLstDT : lstDT) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                BeanUtils.copyProperties(generalLedger, rsInwardOutward);
                generalLedger.setTypeID(rsInwardOutward.getTypeID());
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                generalLedger.debitAmount(aLstDT.getAmount());
                generalLedger.debitAmountOriginal(aLstDT.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());
                generalLedger.accountingObjectID(rsInwardOutward.getAccountingObjectID());
                generalLedger.accountingObjectName(rsInwardOutward.getAccountingObjectName());
                generalLedger.accountingObjectCode(rsInwardOutward.getAccountingObjectCode());
                generalLedger.setReferenceID(rsInwardOutward.getId());
                generalLedger.employeeID(rsInwardOutward.getEmployeeID());
                Optional<AccountingObject> employee = accountingObjectRepository.findOneById(rsInwardOutward.getEmployeeID());
                employee.ifPresent(e -> {
                    generalLedger.employeeCode(e.getAccountingObjectCode());
                    generalLedger.employeeName(e.getAccountingObjectName());
                });
                generalLedger.materialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                generalLedger.materialGoodsCode(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsCode() : null);
                generalLedger.materialGoodsName(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsName() : null);
                generalLedger.repositoryID(aLstDT.getRepository() != null ? aLstDT.getRepository().getId() : null);
                generalLedger.repositoryCode(aLstDT.getRepository() != null ? aLstDT.getRepository().getRepositoryCode() : null);
                generalLedger.repositoryName(aLstDT.getRepository() != null ? aLstDT.getRepository().getRepositoryName() : null);
                generalLedger.unitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                generalLedger.detailID(aLstDT.getId());
                generalLedger.quantity(aLstDT.getQuantity());
                generalLedger.unitPrice(aLstDT.getUnitPrice());
                generalLedger.unitPriceOriginal(aLstDT.getUnitPriceOriginal());
                generalLedger.mainUnitPrice(aLstDT.getMainUnitPrice());
                generalLedger.mainQuantity(aLstDT.getMainQuantity());
                generalLedger.mainConvertRate(aLstDT.getMainConvertRate());
                generalLedger.formula(aLstDT.getFormula());
                generalLedger.departmentID(aLstDT.getDepartment() != null ? aLstDT.getDepartment().getId() : null);
                generalLedger.mainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                generalLedger.orderPriority(aLstDT.getOrderPriority());
                generalLedger.refDateTime(rsInwardOutward.getDate());
                generalLedger.setNoFBook(rsInwardOutward.getNoFBook());
                generalLedger.setNoMBook(rsInwardOutward.getNoMBook());
                generalLedger.budgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                generalLedger.costSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                generalLedger.contractID(aLstDT.getContract() != null ? aLstDT.getContract().getId() : null);
                generalLedger.statisticsCodeID(aLstDT.getStatisticsCode() != null ? aLstDT.getStatisticsCode().getId() : null);
                generalLedger.expenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                UserDTO userDTO = userService.getAccount();
                Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    generalLedger.setNoMBook(null);
                    generalLedger.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }
                listGenTemp.add(generalLedger);


                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.setTypeID(rsInwardOutward.getTypeID());
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                generalLedgerCorresponding.creditAmount(aLstDT.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(aLstDT.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                listGenTemp.add(generalLedgerCorresponding);

                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                repositoryLedgerItem.setTypeID(rsInwardOutward.getTypeID());
                repositoryLedgerItem.setReferenceID(rsInwardOutward.getId());
                repositoryLedgerItem.setCompanyID(rsInwardOutward.getCompanyID());
                repositoryLedgerItem.setNoFBook(rsInwardOutward.getNoFBook());
                repositoryLedgerItem.setNoMBook(rsInwardOutward.getNoMBook());
                repositoryLedgerItem.setIwQuantity(aLstDT.getQuantity());
                repositoryLedgerItem.setIwAmount(aLstDT.getAmount());
                repositoryLedgerItem.setMainIWQuantity(aLstDT.getMainQuantity());
                repositoryLedgerItem.setUnitPrice(aLstDT.getUnitPriceOriginal());
                repositoryLedgerItem.setAccount(aLstDT.getDebitAccount());
                repositoryLedgerItem.setAccountCorresponding(aLstDT.getCreditAccount());
                repositoryLedgerItem.setDetailID(aLstDT.getId());
                repositoryLedgerItem.setFormula(aLstDT.getFormula());
                repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                repositoryLedgerItem.setRepositoryID(aLstDT.getRepository() != null ? aLstDT.getRepository().getId() : null);
                repositoryLedgerItem.setLotNo(aLstDT.getLotNo());
                repositoryLedgerItem.setDescription(aLstDT.getDescription());
                repositoryLedgerItem.setReason(rsInwardOutward.getReason());
                repositoryLedgerItem.setDate(rsInwardOutward.getDate());
                repositoryLedgerItem.setPostedDate(rsInwardOutward.getPostedDate());
                repositoryLedgerItem.setExpiryDate(aLstDT.getExpiryDate());
                repositoryLedgerItem.setTypeLedger(rsInwardOutward.getTypeLedger());
                repositoryLedgerItem.setUnitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                repositoryLedgerItem.setMainUnitPrice(aLstDT.getMainUnitPrice());
                repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
                repositoryLedgerItem.setBudgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                repositoryLedgerItem.setCostSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                repositoryLedgerItem.setStatisticsCodeID(aLstDT.getStatisticsCode() != null ? aLstDT.getStatisticsCode().getId() : null);
                repositoryLedgerItem.setExpenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                Optional<Repository> repository = Optional.empty();
                if (aLstDT.getRepository() != null && aLstDT.getRepository().getId() != null) {
                    repository = repositoryRepository.findById(aLstDT.getRepository().getId());
                }
                // repository
                if (repository.isPresent()) {
                    repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                    repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                }
                // materialGoods
                Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                if (materialGoods.isPresent()) {
                    repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    repositoryLedgerItem.setNoMBook(null);
                    repositoryLedgerItem.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }
                repositoryLedgers.add(repositoryLedgerItem);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.saveAll(repositoryLedgers);
        return listGenTemp;
    }

    List<GeneralLedger> genGeneralLedgerOutWard(RSInwardOutward rsInwardOutward) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        List<RSInwardOutWardDetails> lstDT = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
        lstDT.sort(Comparator.comparingInt(RSInwardOutWardDetails::getOrderPriority));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (RSInwardOutWardDetails aLstDT : lstDT) {
            GeneralLedger generalLedger = new GeneralLedger();
            try {
                BeanUtils.copyProperties(generalLedger, rsInwardOutward);
                generalLedger.setTypeID(rsInwardOutward.getTypeID());
                generalLedger.account(aLstDT.getDebitAccount());
                generalLedger.accountCorresponding(aLstDT.getCreditAccount());
                generalLedger.debitAmount(aLstDT.getAmount());
                generalLedger.debitAmountOriginal(aLstDT.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.description(aLstDT.getDescription());

                Optional<AccountingObject> accountingObject = accountingObjectRepository.findOneById(rsInwardOutward.getAccountingObjectID());
                accountingObject.ifPresent(e -> {
                    generalLedger.accountingObjectCode(e.getAccountingObjectCode());
                    generalLedger.accountingObjectName(e.getAccountingObjectName());
                });
                generalLedger.setReferenceID(rsInwardOutward.getId());
                generalLedger.employeeID(rsInwardOutward.getEmployeeID());
                Optional<AccountingObject> employee = accountingObjectRepository.findOneById(rsInwardOutward.getEmployeeID());
                employee.ifPresent(e -> {
                    generalLedger.employeeCode(e.getAccountingObjectCode());
                    generalLedger.employeeName(e.getAccountingObjectName());
                });
                generalLedger.materialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                generalLedger.materialGoodsCode(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsCode() : null);
                generalLedger.materialGoodsName(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getMaterialGoodsName() : null);
                generalLedger.repositoryID(aLstDT.getRepository() != null ? aLstDT.getRepository().getId() : null);
                generalLedger.repositoryCode(aLstDT.getRepository() != null ? aLstDT.getRepository().getRepositoryCode() : null);
                generalLedger.repositoryName(aLstDT.getRepository() != null ? aLstDT.getRepository().getRepositoryName() : null);
                generalLedger.unitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                generalLedger.detailID(aLstDT.getId());
                generalLedger.quantity(aLstDT.getQuantity());
                generalLedger.unitPrice(aLstDT.getUnitPrice());
                generalLedger.mainUnitPrice(aLstDT.getMainUnitPrice());
                generalLedger.unitPriceOriginal(aLstDT.getUnitPriceOriginal());
                generalLedger.mainQuantity(aLstDT.getMainQuantity());
                generalLedger.mainConvertRate(aLstDT.getMainConvertRate());
                generalLedger.formula(aLstDT.getFormula());
                generalLedger.departmentID(aLstDT.getDepartment() != null ? aLstDT.getDepartment().getId() : null);
                generalLedger.mainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                generalLedger.orderPriority(aLstDT.getOrderPriority());
                generalLedger.refDateTime(rsInwardOutward.getDate());
                generalLedger.setNoFBook(rsInwardOutward.getNoFBook());
                generalLedger.setNoMBook(rsInwardOutward.getNoMBook());
                generalLedger.budgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                generalLedger.costSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                generalLedger.contractID(aLstDT.getContract() != null ? aLstDT.getContract().getId() : null);
                generalLedger.statisticsCodeID(aLstDT.getStatisticsCode() != null ? aLstDT.getStatisticsCode().getId() : null);
                generalLedger.expenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                UserDTO userDTO = userService.getAccount();
                Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    generalLedger.setNoMBook(null);
                    generalLedger.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }

                listGenTemp.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                generalLedgerCorresponding.setTypeID(rsInwardOutward.getTypeID());
                generalLedgerCorresponding.account(aLstDT.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(aLstDT.getDebitAccount());
                generalLedgerCorresponding.creditAmount(aLstDT.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(aLstDT.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                listGenTemp.add(generalLedgerCorresponding);

                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                repositoryLedgerItem.setTypeID(rsInwardOutward.getTypeID());
                repositoryLedgerItem.setReferenceID(rsInwardOutward.getId());
                repositoryLedgerItem.setCompanyID(rsInwardOutward.getCompanyID());
                repositoryLedgerItem.setNoFBook(rsInwardOutward.getNoFBook());
                repositoryLedgerItem.setNoMBook(rsInwardOutward.getNoMBook());
                repositoryLedgerItem.setOwQuantity(aLstDT.getQuantity());
                repositoryLedgerItem.setOwAmount(aLstDT.getAmount());
                repositoryLedgerItem.setMainOWQuantity(aLstDT.getMainQuantity());
                repositoryLedgerItem.setUnitPrice(aLstDT.getUnitPrice());
                repositoryLedgerItem.setAccount(aLstDT.getCreditAccount());
                repositoryLedgerItem.setAccountCorresponding(aLstDT.getDebitAccount());
                repositoryLedgerItem.setDetailID(aLstDT.getId());
                repositoryLedgerItem.setFormula(aLstDT.getFormula());
                repositoryLedgerItem.setMaterialGoodsID(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                repositoryLedgerItem.setRepositoryID(aLstDT.getRepository() != null ? aLstDT.getRepository().getId() : null);
                repositoryLedgerItem.setLotNo(aLstDT.getLotNo());
                repositoryLedgerItem.setReason(rsInwardOutward.getReason());
                repositoryLedgerItem.setDate(rsInwardOutward.getDate());
                repositoryLedgerItem.setPostedDate(rsInwardOutward.getPostedDate());
                repositoryLedgerItem.setExpiryDate(aLstDT.getExpiryDate());
                repositoryLedgerItem.setTypeLedger(rsInwardOutward.getTypeLedger());
                repositoryLedgerItem.setUnitID(aLstDT.getUnit() != null ? aLstDT.getUnit().getId() : null);
                repositoryLedgerItem.setMainUnitID(aLstDT.getMainUnit() != null ? aLstDT.getMainUnit().getId() : null);
                repositoryLedgerItem.setMainUnitPrice(aLstDT.getMainUnitPrice());
                repositoryLedgerItem.setMainConvertRate(aLstDT.getMainConvertRate());
                repositoryLedgerItem.setBudgetItemID(aLstDT.getBudgetItem() != null ? aLstDT.getBudgetItem().getId() : null);
                repositoryLedgerItem.setCostSetID(aLstDT.getCostSet() != null ? aLstDT.getCostSet().getId() : null);
                repositoryLedgerItem.setStatisticsCodeID(aLstDT.getStatisticsCode() != null ? aLstDT.getStatisticsCode().getId() : null);
                repositoryLedgerItem.setExpenseItemID(aLstDT.getExpenseItem() != null ? aLstDT.getExpenseItem().getId() : null);
                repositoryLedgerItem.setConfrontID(aLstDT.getConfrontID());
                repositoryLedgerItem.setConfrontDetailID(aLstDT.getConfrontDetailID());
                if (aLstDT.getUnit() == null) {
                    repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice());
                    repositoryLedgerItem.setMainOWQuantity(repositoryLedgerItem.getOwQuantity());
                    repositoryLedgerItem.setMainConvertRate(new BigDecimal(1));
                    repositoryLedgerItem.setFormula("*");
                }
                Optional<Repository> repository = repositoryRepository.findById(aLstDT.getRepository().getId());
                // repository
                if (repository.isPresent()) {
                    repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                    repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                }
                // materialGoods
                Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(aLstDT.getMaterialGood() != null ? aLstDT.getMaterialGood().getId() : null);
                if (materialGoods.isPresent()) {
                    repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                    repositoryLedgerItem.setNoMBook(null);
                    repositoryLedgerItem.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                }
                repositoryLedgers.add(repositoryLedgerItem);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        repositoryLedgerRepository.saveAll(repositoryLedgers);
        return listGenTemp;
    }


    public static BigDecimal calculateAmountFromOriginal(BigDecimal amountOriginal, BigDecimal exchangeRate) {
//        BigDecimal amount = BigDecimal.ZERO;
//        BigDecimal rate = exchangeRate;
//        amount = amountOriginal * (rate > BigDecimal.ZERO ? rate : 1);
//        return Math.Round(amount);
        return BigDecimal.ZERO;
    }
    //endregion

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalBalanceAmount(String auditDate, String currencyID) {
        UserDTO userDTO = userService.getAccount();
        String account = currencyID.equals(userDTO.getOrganizationUnit().getCurrencyID()) ? Constants.GOtherVoucher.ACCOUNT_CURRENCY + "%" : Constants.GOtherVoucher.ACCOUNT_FOREIGNCURRENCY + "%";
        return generalLedgerRepository.getTotalBalanceAmount(auditDate, currencyID, account,
            userDTO.getOrganizationUnit().getId(),
            Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData()));
    }

    @Override
    public List<CollectionVoucher> getCollectionVoucher(String date, String currencyID, UUID bankID) {
        return generalLedgerRepository.getCollectionVoucher(date, currencyID, bankID);
    }

    @Override
    public List<CollectionVoucher> getSpendingVoucher(String date, String currencyID, UUID bankID) {
        return generalLedgerRepository.getSpendingVoucher(date, currencyID, bankID);
    }

    @Override
    public List<CollectionVoucher> getListMatch() {
        return generalLedgerRepository.getListMatch();
    }

    /**
     * Author Hautv
     *
     * @param record
     * @return
     */
    @Override
    public Record record(Record record) {
        UserDTO userDTO = userService.getAccount();
        boolean rs = false;
        MessageDTO msg = new MessageDTO("");
        switch (record.getTypeID()) {
            case TYPE_MC_RECEIPT:
            case TYPE_PHIEU_THU_TIEN_KHACH_HANG:
                Optional<MCReceipt> mc = mcReceiptRepository.findById(record.getId());
                if (mc != null) {
                    MCReceipt mcReceipt = mc.get();
                    rs = record(mcReceipt, msg);
                    if (rs) {
                        mcReceipt.setRecorded(true);
                        mcReceiptRepository.save(mcReceipt);
                    }
                }
                break;
            case TYPE_MC_PAYMENT:
            case PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP:
                Optional<MCPayment> mcP = mcPaymentRepository.findById(record.getId());
                if (mcP != null) {
                    MCPayment mcPayment = mcP.get();
                    rs = record(mcPayment, msg);
                    if (rs) {
                        mcPayment.setRecorded(true);
                        mcPaymentRepository.save(mcPayment);
                    }
                }
                break;
            case TYPE_MBDEPOSIT:
            case TYPE_MCDEPOSIT:
                Optional<MBDeposit> mbD = mbDepositRepository.findById(record.getId());
                if (mbD != null) {
                    MBDeposit mbDeposit = mbD.get();
                    rs = record(mbDeposit, msg);
                    if (rs) {
                        mbDeposit.setRecorded(true);
                        mbDepositRepository.save(mbDeposit);
                    }
                }
                break;
            case TYPE_MBCreditCard:
            case TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU:
            case TYPE_ID_THE_TIN_DUNG_MUA_HANG:
            case TYPE_ID_THE_TIN_DUNG_TRA_TIEN_NCC:
                Optional<MBCreditCard> mbC = mbCreditCardRepository.findById(record.getId());
                if (mbC != null) {
                    MBCreditCard mbCreditCard = mbC.get();
                    rs = record(mbCreditCard, msg);
                    if (rs) {
                        mbCreditCard.setRecorded(true);
                        mbCreditCardRepository.save(mbCreditCard);
                    }
                }
                break;
            case UY_NHIEM_CHI:
            case SEC_CHUYEN_KHOAN:
            case SEC_TIEN_MAT:
            case TYPE_ID_UNC_MUA_HANG:
            case TYPE_ID_SCK_MUA_HANG:
            case TYPE_ID_STM_MUA_HANG:
            case TYPE_ID_UNC_MUA_DICH_VU:
            case TYPE_ID_SCK_MUA_DICH_VU:
            case TYPE_ID_STM_MUA_DICH_VU:
            case BAO_NO_UNC_TRA_TIEN_NCC:
            case BAO_NO_SCK_TRA_TIEN_NCC:
            case BAO_NO_STM_TRA_TIEN_NCC:
                Optional<MBTellerPaper> mBTP = mbTellerPaperRepository.findById(record.getId());
                if (mBTP != null) {
                    MBTellerPaper mbTellerPaper = mBTP.get();
                    rs = record(mbTellerPaper, msg);
                    if (rs) {
                        mbTellerPaper.setRecorded(true);
                        mbTellerPaperRepository.save(mbTellerPaper);
                    }
                }
                break;
            case TYPE_PPDiscountReturn:
            case TYPE_PPDiscountPurchase:
                Optional<PPDiscountReturn> ppD = ppDiscountReturnRepository.findById(record.getId());
                if (ppD != null) {
                    PPDiscountReturn ppDiscountReturn = ppD.get();
                    rs = record(ppDiscountReturn, msg);
                    if (rs) {
                        ppDiscountReturn.setRecorded(true);
                        PPDiscountReturn pp = ppDiscountReturnRepository.save(ppDiscountReturn);
                        Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                        if (pp.getIsDeliveryVoucher() && pp.getRsInwardOutwardID() != null) {
                            rsInwardOutward = rSInwardOutwardRepository.findById(pp.getRsInwardOutwardID());
                            if (rsInwardOutward.isPresent()) {
                                rsInwardOutward.get().setRecorded(true);
                                rSInwardOutwardRepository.save(rsInwardOutward.get());
                            }
                        }
                    }
                }
                break;
            case HANG_BAN_TRA_LAI: {
                Optional<SaReturn> saReturnOptional = saReturnRepository.findById(record.getId());
                if (saReturnOptional.isPresent()) {
                    SaReturn saReturn = saReturnOptional.get();
                    rs = record(saReturn, msg);
                    if (rs) {
                        saReturn.setRecorded(true);
                        saReturnRepository.save(saReturn);
                        if (saReturn.isIsDeliveryVoucher() && saReturn.getRsInwardOutwardID() != null) {
                            Optional<RSInwardOutward> rsInwardOutward = rSInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID());
                            if (rsInwardOutward.isPresent()) {
                                rsInwardOutward.get().setRecorded(true);
                                rSInwardOutwardRepository.save(rsInwardOutward.get());
                            }
                        }
                    }
                }
            }
            break;
            case HANG_GIAM_GIA: {
                Optional<SaReturn> saReturnOptional = saReturnRepository.findById(record.getId());
                if (saReturnOptional.isPresent()) {
                    SaReturn saReturn = saReturnOptional.get();
                    rs = record(saReturn, msg);
                    if (rs) {
                        saReturn.setRecorded(true);
                    }
                }
            }
            break;
            case PP_SERVICE:
                Optional<PPService> ppServiceOptional = ppServiceRepository.findById(record.getId());
                if (ppServiceOptional.isPresent()) {
                    PPService ppService = ppServiceOptional.get();
                    rs = record(ppService, msg);
                    if (rs) {
                        ppService.setRecorded(true);
                        ppServiceRepository.save(ppService);
                        switch (ppService.getTypeID()) {
                            case Constants.PPServiceType.PPSERVICE_CASH:
                                Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppService.getPaymentVoucherID());
                                if (mcPaymentOptional.isPresent()) {
                                    mcPaymentOptional.get().setRecorded(true);
                                    mcPaymentRepository.save(mcPaymentOptional.get());
                                }
                                break;
                            case Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER:
                            case Constants.PPServiceType.PPSERVICE_TRANSFER_SEC:
                            case Constants.PPServiceType.PPSERVICE_CASH_SEC:
                                Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppService.getPaymentVoucherID());
                                if (mbTellerPaperOptional.isPresent()) {
                                    mbTellerPaperOptional.get().setRecorded(true);
                                    mbTellerPaperRepository.save(mbTellerPaperOptional.get());
                                }
                                break;
                            case Constants.PPServiceType.PPSERVICE_CREDIT_CARD:
                                Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppService.getPaymentVoucherID());
                                if (mbCreditCardOptional.isPresent()) {
                                    mbCreditCardOptional.get().setRecorded(true);
                                    mbCreditCardRepository.save(mbCreditCardOptional.get());
                                }
                                break;
                        }
                    }
                }
                break;
            case TYPE_GOtherVoucher:
            case TYPE_GOtherVoucher_Allocations:
            case TYPE_NGHIEM_THU_CONG_TRINH:
                Optional<GOtherVoucher> gOV = gOtherVoucherRepository.findById(record.getId());
                if (gOV.isPresent()) {
                    GOtherVoucher gOtherVoucher = gOV.get();
                    rs = record(gOtherVoucher, msg);
                    if (rs) {
                        gOtherVoucher.setRecorded(true);
                        gOtherVoucherRepository.save(gOtherVoucher);
                    }
                }
                break;
            case MUA_HANG_QUA_KHO:
                Optional<PPInvoice> ppInvoiceOptional = pPInvoiceRepository.findById(record.getId());
                if (ppInvoiceOptional.isPresent()) {
                    PPInvoice ppInvoice = ppInvoiceOptional.get();
                    rs = record(ppInvoice, msg);
                    if (rs) {
                        Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                        if (ppInvoiceOptional.get().isStoredInRepository()) {
                            rsInwardOutward = rSInwardOutwardRepository.findById(ppInvoiceOptional.get().getRsInwardOutwardId());
                            if (rsInwardOutward.isPresent()) {
                                rsInwardOutward.get().setRecorded(true);
                                rSInwardOutwardRepository.save(rsInwardOutward.get());
                            }
                        }
                        ppInvoice.setRecorded(true);
                        pPInvoiceRepository.save(ppInvoice);
                        switch (ppInvoiceOptional.get().getTypeId()) {
                            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                                Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoiceOptional.get().getPaymentVoucherId());
                                if (mcPaymentOptional.isPresent()) {
                                    mcPaymentOptional.get().setRecorded(true);
                                    mcPaymentRepository.save(mcPaymentOptional.get());
                                }
                                break;
                            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:

                            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:

                            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                                Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoiceOptional.get().getPaymentVoucherId());
                                if (mbTellerPaperOptional.isPresent()) {
                                    mbTellerPaperOptional.get().setRecorded(true);
                                    mbTellerPaperRepository.save(mbTellerPaperOptional.get());
                                }
                                break;
                            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                                Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoiceOptional.get().getPaymentVoucherId());
                                if (mbCreditCardOptional.isPresent()) {
                                    mbCreditCardOptional.get().setRecorded(true);
                                    mbCreditCardRepository.save(mbCreditCardOptional.get());
                                }
                                break;
                        }
                    }
                }
                break;
            case NHAP_KHO:
            case NHAP_KHO_DC:
            case NHAP_KHO_MH:
            case NHAP_KHO_HBTL:
                Optional<RSInwardOutward> rsInwardOutwardO = rSInwardOutwardRepository.findById(record.getId());
                if (rsInwardOutwardO.isPresent()) {
                    RSInwardOutward rsInwardOutward = rsInwardOutwardO.get();
                    rs = record(rsInwardOutward, msg);
                    if (rs) {
                        rsInwardOutward.setRecorded(true);
                        rSInwardOutwardRepository.save(rsInwardOutward);
                    }
                }
                break;
            case XUAT_KHO:
            case XUAT_KHO_BH:
            case XUAT_KHO_HMTL:
            case XUAT_KHO_DC:
                Optional<RSInwardOutward> rsInwardOutward1 = rSInwardOutwardRepository.findById(record.getId());
                if (rsInwardOutward1.isPresent()) {
                    RSInwardOutward rsInwardOutward = rsInwardOutward1.get();
                    rs = record(rsInwardOutward, new MessageDTO(record.getMsg()));
                    if (rs) {
                        rsInwardOutward.setRecorded(true);
                        rSInwardOutwardRepository.save(rsInwardOutward);
                        String strMethod = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.VTHH_PPTinhGiaXKho)).findAny().get().getData();
                        if (strMethod != null && !strMethod.equals("")) {
                            Integer calculationMethod = Integer.valueOf(strMethod);
                            if (calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI) {
                                repositoryLedgerService.calculateOWPrice(calculationMethod, rsInwardOutward.getRsInwardOutwardDetails().stream().map(x -> x.getMaterialGood().getId()).collect(Collectors.toList()), rsInwardOutward.getDate().toString(), rsInwardOutward.getDate().toString());
                            }
                        }
                    }
                }
                break;
            case CHUYEN_KHO:
            case CHUYEN_KHO_GUI_DAI_LY:
            case CHUYEN_KHO_CHUYEN_NOI_BO:
                Optional<RSTransfer> rsTransfers = rsTransferRepository.findById(record.getId());
                if (rsTransfers.isPresent()) {
                    RSTransfer rsTransfer = rsTransfers.get();
                    rs = record(rsTransfer, msg);
                    if (rs) {
                        rsTransfer.setRecorded(true);
                        rsTransferRepository.save(rsTransfer);
                    }
                }
                break;
            case BAN_HANG_CHUA_THU_TIEN:
            case BAN_HANG_THU_TIEN_NGAY_TM:
            case BAN_HANG_THU_TIEN_NGAY_CK:
                Optional<SAInvoice> saInvoiceOptional = saInvoiceRepository.findById(record.getId());
                if (saInvoiceOptional.isPresent()) {
                    SAInvoice saInvoice = saInvoiceOptional.get();
                    rs = record(saInvoice, msg);
                    if (rs) {
                        saInvoice.setRecorded(true);
                        saInvoiceRepository.save(saInvoice);
                        String valueCal = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.VTHH_PPTinhGiaXKho)).findAny().get().getData();
                        if (valueCal != null && !valueCal.equals("")) {
                            Integer calculationMethod = Integer.valueOf(valueCal);
                            if (calculationMethod == Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI && saInvoice.isIsDeliveryVoucher()) {
                                repositoryLedgerService.calculateOWPrice(calculationMethod, saInvoice.getsAInvoiceDetails().stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList()), saInvoice.getDate().toString(), saInvoice.getDate().toString());
                            }
                        }
                        if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_TM)) {
                            Optional<MCReceipt> mcReceiptOptional = mcReceiptRepository.findById(saInvoice.getMcReceiptID());
                            if (mcReceiptOptional.isPresent()) {
                                mcReceiptOptional.get().setRecorded(true);
                                mcReceiptRepository.save(mcReceiptOptional.get());
                            }
                        } else if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_CK)) {
                            Optional<MBDeposit> mbDepositOptional = mbDepositRepository.findById(saInvoice.getMbDepositID());
                            if (mbDepositOptional.isPresent()) {
                                mbDepositOptional.get().setRecorded(true);
                                mbDepositRepository.save(mbDepositOptional.get());
                            }
                        }
                        if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                            Optional<RSInwardOutward> rsInwardOutwardOptional = rSInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID());
                            if (rsInwardOutwardOptional.isPresent()) {
                                rsInwardOutwardOptional.get().setRecorded(true);
                                rSInwardOutwardRepository.save(rsInwardOutwardOptional.get());
                            }
                        }
                    }
                }
                break;
            case KET_CHUYEN_LAI_LO:
                Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(record.getId());
                if (gOtherVoucherOptional.isPresent()) {
                    GOtherVoucher gOtherVoucher = gOtherVoucherOptional.get();
                    msg.setMsgError(Constants.GOtherVoucher.KET_CHUYEN);
                    rs = record(gOtherVoucher, msg);
                    if (rs) {
                        gOtherVoucher.setRecorded(true);
                        gOtherVoucherRepository.save(gOtherVoucher);
                    }
                }
                break;
            case TYPE_CPExpense_Tranfer:
                Optional<CPExpenseTranfer> cPET = cpExpenseTranferRepository.findById(record.getId());
                if (cPET.isPresent()) {
                    CPExpenseTranfer cpExpenseTranfer = cPET.get();
                    rs = record(cpExpenseTranfer, msg);
                    if (rs) {
                        cpExpenseTranfer.setRecorded(true);
                        cpExpenseTranferRepository.save(cpExpenseTranfer);
                    }
                }
                break;

            case GHI_TANG_CCDC:
                Optional<TIIncrement> tiIncrement = tiIncrementRepository.findById(record.getId());
                if (tiIncrement.isPresent()) {
                    TIIncrement tiIncrement1 = tiIncrement.get();
                    msg.setMsgError("");
                    rs = record(tiIncrement1, msg);
                    if (rs) {
                        tiIncrement1.setRecorded(true);
                        tiIncrementRepository.save(tiIncrement1);
                    }
                }
                break;
            case TSCD_GHI_TANG:
                Optional<FAIncrement> faIncrement = faIncrementRepository.findById(record.getId());
                if (faIncrement.isPresent()) {
                    FAIncrement faIncrement1 = faIncrement.get();
                    msg.setMsgError("");
                    rs = record(faIncrement1, msg);
                    if (rs) {
                        faIncrement1.setRecorded(true);
                        faIncrementRepository.save(faIncrement1);
                    }
                }
                break;
            case PHAN_BO_CCDC:
                Optional<TIAllocation> tiAllocation = tiAllocationRepository.findById(record.getId());
                if (tiAllocation.isPresent()) {
                    TIAllocation tiAllocation1 = tiAllocation.get();
                    msg.setMsgError("");
                    rs = record(tiAllocation1, msg);
                    if (rs) {
                        tiAllocation1.setRecorded(true);
                        tiAllocationRepository.save(tiAllocation1);
                    }
                }
                break;
            case GHI_GIAM_CCDC:
                Optional<TIDecrement> tiDecrement = tiDecrementRepository.findById(record.getId());
                if (tiDecrement.isPresent()) {
                    TIDecrement tiDecrement1 = tiDecrement.get();
                    msg.setMsgError("");
                    rs = record(tiDecrement1, msg);
                    if (rs) {
                        tiDecrement1.setRecorded(true);
                        tiDecrementRepository.save(tiDecrement1);
                    }
                }
                break;
            case DIEU_CHINH_CCDC:
                Optional<TIAdjustment> tiAdjustment = tiAdjustmentRepository.findById(record.getId());
                if (tiAdjustment.isPresent()) {
                    TIAdjustment tiAdjustment1 = tiAdjustment.get();
                    msg.setMsgError("");
                    rs = record(tiAdjustment1, msg);
                    if (rs) {
                        tiAdjustment1.setRecorded(true);
                        tiAdjustmentRepository.save(tiAdjustment1);
                    }
                }
                break;
            case DIEU_CHUYEN_CCDC:
                Optional<TITransfer> tiTransfer = tiTransferRepository.findById(record.getId());
                if (tiTransfer.isPresent()) {
                    TITransfer tiTransfer1 = tiTransfer.get();
                    msg.setMsgError("");
                    rs = record(tiTransfer1, msg);
                    if (rs) {
                        tiTransfer1.setRecorded(true);
                        tiTransferRepository.save(tiTransfer1);
                    }
                }
                break;

        }

        record.setSuccess(rs);
        record.setMsg(msg.getMsgError());
        return record;
    }

    /**
     * Author Hautv
     *
     * @param requestRecordListDTO
     * @return
     */
    @Override
    public HandlingResultDTO record(RequestRecordListDTO requestRecordListDTO) {
        List<UUID> rcList = new ArrayList<>();
        switch (requestRecordListDTO.getTypeIDMain()) {
            case PHIEU_THU:
                return handlingRecordMCReceipt(requestRecordListDTO.getRecords());
            case PHIEU_CHI:
                return handlingRecordMCPayment(requestRecordListDTO.getRecords());
            case TYPE_BAO_NO_UNC:
                return handlingRecordMBTellerPaper(requestRecordListDTO.getRecords());
            case BAO_CO:
                return handlingRecordMBDeposit(requestRecordListDTO.getRecords());
            case THE_TIN_DUNG:
                return handlingRecordMBCreditCard(requestRecordListDTO.getRecords());
            case BAN_HANG_CHUA_THU_TIEN:
                return handlingRecordSAInvoice(requestRecordListDTO.getRecords());
            case HANG_BAN_TRA_LAI:
                return handlingRecordSAReturn(requestRecordListDTO.getRecords());
            case PHAN_BO_CHI_PHI_TRA_TRUOC:
                return handlingRecordGOtherVoucher(requestRecordListDTO.getRecords());
            case MUA_HANG_TRA_LAI:
            case MUA_HANG_GIAM_GIA:
                return handlingRecordPPDiscountReturn(requestRecordListDTO.getRecords());
            case PP_SERVICE:
                return handlingRecordPPService(requestRecordListDTO.getRecords());
            case MUA_HANG_CHUA_THANH_TOAN:
                return handlingRecordPPInvoice(requestRecordListDTO.getRecords(), Boolean.TRUE.equals(requestRecordListDTO.getKho()));
            case CHUNG_TU_NGHIEP_VU_KHAC:
                return handlingRecordGOtherVoucher(requestRecordListDTO.getRecords());
            case XUAT_KHO:
                return handlingRecordRSOutWard(requestRecordListDTO.getRecords());
            case NHAP_KHO:
                return handlingRecordRSInWard(requestRecordListDTO.getRecords());
            case GHI_TANG_CCDC:
                return handlingRecordTIIncrement(requestRecordListDTO.getRecords());
            case GHI_GIAM_CCDC:
                return handlingRecordTIDecrement(requestRecordListDTO.getRecords());
            case CHUYEN_KHO:
                return handlingRecordRSTransfer(requestRecordListDTO.getRecords());
            case KET_CHUYEN_CHI_PHI:
                return handlingRecordCPExpenseTranfer(requestRecordListDTO.getRecords());
            case TSCD_GHI_TANG:
                return handlingRecordFAIncrement(requestRecordListDTO.getRecords());
        }

        return null;
    }

    private HandlingResultDTO handlingRecordTIDecrement(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
//                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
//                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
//                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
//                    lstFail.add(viewVoucherNo);
//                    listV.remove(viewVoucherNo);
//                }
            }
        }
        List<Toolledger> ledgers = viewVoucherNoService.handleToolRecord(listV, lstFail); // Ghi sổ cái
        if (ledgers.size() > 0) {
            List<ViewVoucherNo> uuids = listV
                .stream()
                .filter(x -> !lstFail
                    .stream()
                    .map(ViewVoucherNo::getRefID)
                    .collect(Collectors.toList())
                    .contains(x.getRefID()))
                .collect(Collectors.toList());
//            viewVoucherNoRespository.saveToolLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(uuids);
            viewVoucherNoRespository.updateTableRecord(uuids);
        }

        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    private HandlingResultDTO handlingRecordFAIncrement(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<FixedAssetLedger> ledgers = viewVoucherNoService.handleFixedRecord(listV, lstFail); // Ghi sổ cái
        if (ledgers.size() > 0) {
            List<ViewVoucherNo> uuids = listV
                .stream()
                .filter(x -> !lstFail
                    .stream()
                    .map(ViewVoucherNo::getRefID)
                    .collect(Collectors.toList())
                    .contains(x.getRefID()))
                .collect(Collectors.toList());
            viewVoucherNoRespository.updateVoucherRefRecorded(uuids);
            viewVoucherNoRespository.updateTableRecord(uuids);
        }

        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    private HandlingResultDTO handlingRecordTIIncrement(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
//                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
//                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
//                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
//                    lstFail.add(viewVoucherNo);
//                    listV.remove(viewVoucherNo);
//                }
            }
        }
        List<Toolledger> ledgers = viewVoucherNoService.handleToolRecord(listV, lstFail); // Ghi sổ cái
        if (ledgers.size() > 0) {
            List<ViewVoucherNo> uuids = listV
                .stream()
                .filter(x -> !lstFail
                    .stream()
                    .map(ViewVoucherNo::getRefID)
                    .collect(Collectors.toList())
                    .contains(x.getRefID()))
                .collect(Collectors.toList());
//            viewVoucherNoRespository.saveToolLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(uuids);
            viewVoucherNoRespository.updateTableRecord(uuids);
        }

        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn mua hàng qua kho vs không qua kho
     * @Author congnd
     */
    private HandlingResultDTO handlingRecordPPInvoice(List<Record> records, boolean isKho) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());

        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }

        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái

        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        if (isKho) {
            repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        }

        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        if (lstFail.size() > 0) {
            listV.removeAll(lstFail);
        }
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (isKho && repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    private HandlingResultDTO handlingRecordPPService(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        if (ledgers.size() > 0) {
            List<ViewVoucherNo> uuids = listV
                .stream()
                .filter(x -> !lstFail
                    .stream()
                    .map(ViewVoucherNo::getRefID)
                    .collect(Collectors.toList())
                    .contains(x.getRefID()))
                .collect(Collectors.toList());
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(uuids);
            viewVoucherNoRespository.updateTableRecord(uuids);
        }

        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    private HandlingResultDTO handlingRecordPPDiscountReturn(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn phiếu thu
     * @Author Hautv
     */
    private HandlingResultDTO handlingRecordMCReceipt(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> PHIEU_THU_TU_BAN_HANG != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsFromSAInvoice = records.stream().filter(n -> PHIEU_THU_TU_BAN_HANG == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsFromSAInvoice.size() > 0) {
            lstRecord.addAll(convertToUUID(saInvoiceRepository.findAllIDByMCReceiptID(uuidsFromSAInvoice)));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = lstRecord.size() > 0 ? listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList()) : new ArrayList<>();
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        if (lstFail.size() > 0) {
            listV.removeAll(lstFail);
        }
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }


    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn Báo có
     * @Author Namnh
     */
    private HandlingResultDTO handlingRecordMBDeposit(List<Record> records) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> NOP_TIEN_TU_BAN_HANG != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsFromSAInvoice = records.stream().filter(n -> NOP_TIEN_TU_BAN_HANG == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsFromSAInvoice.size() > 0) {
            lstRecord.addAll(saInvoiceRepository.findAllIDByMBDepositID(uuidsFromSAInvoice, currentUserLoginAndOrg.get().getOrg()));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        if (lstFail.size() > 0) {
            listV.removeAll(lstFail);
        }
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn phiếu chi
     * @Author Hautv
     */
    private HandlingResultDTO handlingRecordMCPayment(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> PHIEU_CHI_MUA_DICH_VU != n.getTypeID() && PHIEU_CHI_MUA_HANG != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsPCMuaHang = records.stream().filter(n -> PHIEU_CHI_MUA_HANG == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaHang.size() > 0) {
            lstRecord.addAll(convertToUUID(pPInvoiceRepository.findAllIDByPaymentVoucherID(uuidsPCMuaHang)));
        }
        List<UUID> uuidsPCMuaDichVu = records.stream().filter(n -> PHIEU_CHI_MUA_DICH_VU == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaDichVu.size() > 0) {
            lstRecord.addAll(convertToUUID(ppServiceRepository.findAllIDByPaymentVoucherIDStr(uuidsPCMuaDichVu)));
        }
        List<ViewVoucherNo> listViewVoucherNo = lstRecord.size() > 0 ? viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord) : new ArrayList<>();
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        if (lstFail.size() > 0) {
            listV.removeAll(lstFail);
        }
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /** @Author Hautv
     * @param uuids
     * @return
     */
    List<UUID> convertToUUID(List<String> uuids) {
        List<UUID> result = new ArrayList<>();
        for (String id : uuids) {
            result.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        return result;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn thẻ tín dụng
     * @Author Namnh
     */
    private HandlingResultDTO handlingRecordMBCreditCard(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> THE_TIN_DUNG_MUA_DICH_VU != n.getTypeID() && THE_TIN_DUNG_MUA_HANG != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsPCMuaHang = records.stream().filter(n -> THE_TIN_DUNG_MUA_HANG == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaHang.size() > 0) {
            lstRecord.addAll(convertToUUID(pPInvoiceRepository.findAllIDByPaymentVoucherID(uuidsPCMuaHang)));
        }
        List<UUID> uuidsPCMuaDichVu = records.stream().filter(n -> THE_TIN_DUNG_MUA_DICH_VU == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaDichVu.size() > 0) {
            lstRecord.addAll(convertToUUID(ppServiceRepository.findAllIDByPaymentVoucherIDStr(uuidsPCMuaDichVu)));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }


    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn Báo nợ
     * @Author Namnh
     */
    private HandlingResultDTO handlingRecordMBTellerPaper(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> TYPE_ID_UNC_MUA_DICH_VU != n.getTypeID() &&
            TYPE_ID_STM_MUA_DICH_VU != n.getTypeID() && TYPE_ID_SCK_MUA_DICH_VU != n.getTypeID() &&
            UY_NHIEM_CHI_MUA_HANG != n.getTypeID() && SEC_TIEN_MAT_MUA_HANG != n.getTypeID() &&
            SEC_CHUYEN_KHOAN_MUA_HANG != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsPCMuaHang = records.stream().filter(n -> UY_NHIEM_CHI_MUA_HANG == n.getTypeID() || SEC_TIEN_MAT_MUA_HANG == n.getTypeID() ||
            SEC_CHUYEN_KHOAN_MUA_HANG == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaHang.size() > 0) {
            lstRecord.addAll(convertToUUID(pPInvoiceRepository.findAllIDByPaymentVoucherID(uuidsPCMuaHang)));
        }
        List<UUID> uuidsPCMuaDichVu = records.stream().filter(n -> TYPE_ID_UNC_MUA_DICH_VU == n.getTypeID() ||
            TYPE_ID_STM_MUA_DICH_VU == n.getTypeID() || TYPE_ID_SCK_MUA_DICH_VU == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsPCMuaDichVu.size() > 0) {
            lstRecord.addAll(convertToUUID(ppServiceRepository.findAllIDByPaymentVoucherIDStr(uuidsPCMuaDichVu)));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn bán hàng
     * @Author Chuongnv
     */
    private HandlingResultDTO handlingRecordSAInvoice(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }


    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn Chứng từ nghiệp vụ khác
     * @Author namnh
     */
    private HandlingResultDTO handlingRecordGOtherVoucher(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
//            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
//                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
//                for (ViewVoucherNoDetailDTO item: lstDT) {
//                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
//                }
//                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
//                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
//                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
//                    lstFail.add(viewVoucherNo);
//                    listV.remove(viewVoucherNo);
//                }
//            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn bán hàng
     * @Author Chuongnv
     */
    private HandlingResultDTO handlingRecordSAReturn(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (Boolean.TRUE.equals(viewVoucherNo.getDeliveryVoucher()) && userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn xuất kho
     * @Author huyxoan
     */
    private HandlingResultDTO handlingRecordRSOutWard(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().filter(n -> XUAT_KHO_BH != n.getTypeID() && XUAT_KHO_HMTL != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsBanHang = records.stream().filter(n -> XUAT_KHO_BH == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsBanHang.size() > 0) {
            lstRecord.addAll(saInvoiceRepository.findAllIDByRSInwardOutwardID(uuidsBanHang));
        }
        List<UUID> uuidsHMTL = records.stream().filter(n -> XUAT_KHO_HMTL == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsHMTL.size() > 0) {
            lstRecord.addAll(ppDiscountReturnRepository.findAllIDByRSInwardOutwardID(uuidsHMTL));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn nhập kho
     * @Author huyxoan
     */
    private HandlingResultDTO handlingRecordRSInWard(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().filter(n -> NHAP_KHO_MH != n.getTypeID() && NHAP_KHO_HBTL != n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        List<UUID> uuidsMuaHang = records.stream().filter(n -> NHAP_KHO_MH == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsMuaHang.size() > 0) {
            lstRecord.addAll(pPInvoiceRepository.findAllIDByRSInwardOutwardID(uuidsMuaHang));
        }
        List<UUID> uuidsHBTL = records.stream().filter(n -> NHAP_KHO_HBTL == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
        if (uuidsHBTL.size() > 0) {
            lstRecord.addAll(saReturnRepository.findAllIDByRSInwardOutwardID(uuidsHBTL));
        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn chuyển kho
     * @Author huyxoan
     */
    private HandlingResultDTO handlingRecordRSTransfer(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        List<UUID> lstRecord = records.stream().filter(n -> CHUYEN_KHO == n.getTypeID() || CHUYEN_KHO_GUI_DAI_LY == n.getTypeID() || CHUYEN_KHO_CHUYEN_NOI_BO == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
//        List<UUID> uuidsBanHang = records.stream().filter(n -> XUAT_KHO_BH == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
//        if (uuidsBanHang.size() > 0) {
//            lstRecord.addAll(saInvoiceRepository.findAllIDByRSInwardOutwardID(uuidsBanHang));
//        }
//        List<UUID> uuidsHMTL = records.stream().filter(n -> XUAT_KHO_HMTL == n.getTypeID()).map(Record::getId).collect(Collectors.toList());
//        if (uuidsHMTL.size() > 0) {
//            lstRecord.addAll(ppDiscountReturnRepository.findAllIDByRSInwardOutwardID(uuidsHMTL));
//        }
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(listV);
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listCheck) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0")) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : lstDT) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    viewVoucherNo.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(viewVoucherNo);
                    listV.remove(viewVoucherNo);
                }
            }
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }


    /**
     * @param records
     * @return Ghi sổ nhiều chứng từ màn Báo có
     * @Author Namnh
     */
    private HandlingResultDTO handlingRecordCPExpenseTranfer(List<Record> records) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        List<UUID> lstRecord = records.stream().map(Record::getId).collect(Collectors.toList());
        List<ViewVoucherNo> listViewVoucherNo = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), lstRecord);
        List<ViewVoucherNo> lstFail = listViewVoucherNo.stream().filter(ViewVoucherNo::getRecorded).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstFail) {
            viewVoucherNo.setReasonFail(Constants.Message.VoucherRecorded);
        }
        List<ViewVoucherNo> listV = listViewVoucherNo.stream().filter(n -> !n.getRecorded()).collect(Collectors.toList());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(listV.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : listV) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<GeneralLedger> ledgers = viewVoucherNoService.handleRecord(listV, lstFail); // Ghi sổ cái
        List<RepositoryLedger> repositoryLedgers = viewVoucherNoService.handleRepositoryLedger(listV, lstFail); // ghi sổ kho
        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        if (lstFail.size() > 0) {
            listV.removeAll(lstFail);
        }
        if (ledgers.size() > 0) {
            viewVoucherNoRespository.saveGeneralLedger(ledgers);
            viewVoucherNoRespository.updateVoucherRefRecorded(listV);
            viewVoucherNoRespository.updateTableRecord(listV);
        }
        if (repositoryLedgers.size() > 0) {
            viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers);
        }
        if (lstFail.size() > 0) {
            if (Constants.TypeLedger.FINANCIAL_BOOK.equals(Utils.PhienSoLamViec(userDTO))) {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoFBook));
            } else {
                lstFail.sort(Comparator.comparing(ViewVoucherNo::getDate).thenComparing(ViewVoucherNo::getPostedDate).thenComparing(ViewVoucherNo::getNoMBook));
            }
        }
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountTotalVouchers(listViewVoucherNo.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountSuccessVouchers(listViewVoucherNo.size() - lstFail.size());
        return handlingResultDTO;
    }

    /**
     * Author Hautv
     *
     * @param record
     * @return
     */
    @Override
    public Record unRecord(Record record) {
        boolean rs = false;
        String msg = "";
        switch (record.getTypeID()) {
            case TYPE_MC_RECEIPT:
            case TYPE_PHIEU_THU_TIEN_KHACH_HANG:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    MCReceipt mc = mcReceiptRepository.findById(record.getId()).get();
                    mc.setRecorded(false);
                    mcReceiptRepository.save(mc);
                }
                break;
            case TYPE_MC_PAYMENT:
            case PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    MCPayment mc = mcPaymentRepository.findById(record.getId()).get();
                    mc.setRecorded(false);
                    mcPaymentRepository.save(mc);
                }
                break;
            case TYPE_MBDEPOSIT:
            case TYPE_MCDEPOSIT:
            case TYPE_MSDEPOSIT:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    MBDeposit mbDeposit = mbDepositRepository.findById(record.getId()).get();
                    mbDeposit.setRecorded(false);
                    mbDepositRepository.save(mbDeposit);
                }
                break;
            case TYPE_MBCreditCard:
            case TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU:
            case TYPE_ID_THE_TIN_DUNG_MUA_HANG:
            case TYPE_ID_THE_TIN_DUNG_TRA_TIEN_NCC:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    MBCreditCard mbCreditCard = mbCreditCardRepository.findById(record.getId()).get();
                    mbCreditCard.setRecorded(false);
                    mbCreditCardRepository.save(mbCreditCard);
                }
                break;
            case UY_NHIEM_CHI:
            case SEC_CHUYEN_KHOAN:
            case SEC_TIEN_MAT:
            case TYPE_ID_UNC_MUA_HANG:
            case TYPE_ID_SCK_MUA_HANG:
            case TYPE_ID_STM_MUA_HANG:
            case TYPE_ID_UNC_MUA_DICH_VU:
            case TYPE_ID_SCK_MUA_DICH_VU:
            case TYPE_ID_STM_MUA_DICH_VU:
            case BAO_NO_UNC_TRA_TIEN_NCC:
            case BAO_NO_SCK_TRA_TIEN_NCC:
            case BAO_NO_STM_TRA_TIEN_NCC:
                rs = unrecord(record.getId(), null);
                if (rs) {
                    MBTellerPaper mBTP = mbTellerPaperRepository.findById(record.getId()).get();
                    mBTP.setRecorded(false);
                    mbTellerPaperRepository.save(mBTP);
                }
                break;
            case TYPE_PPDiscountReturn:
            case TYPE_PPDiscountPurchase:
                Optional<PPDiscountReturn> ppDiscountReturn = ppDiscountReturnRepository.findById(record.getId());
                if (ppDiscountReturn.isPresent() && ppDiscountReturn.get().getIsDeliveryVoucher() && ppDiscountReturn.get().getRsInwardOutwardID() != null) {
                    record.setRepositoryLedgerID(ppDiscountReturn.get().getRsInwardOutwardID());
                }
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
//                    PPDiscountReturn ppDiscountReturn = ppDiscountReturnRepository.findById(record.getId()).get();
                    ppDiscountReturn.get().setRecorded(false);
                    PPDiscountReturn pp = ppDiscountReturnRepository.save(ppDiscountReturn.get());
                    Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                    if (pp.getIsDeliveryVoucher() && pp.getRsInwardOutwardID() != null) {
                        rsInwardOutward = rSInwardOutwardRepository.findById(pp.getRsInwardOutwardID());
                        if (rsInwardOutward.isPresent()) {
                            rsInwardOutward.get().setRecorded(false);
                            rSInwardOutwardRepository.save(rsInwardOutward.get());
                        }
                    }
                }
                break;
            case HANG_BAN_TRA_LAI:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    Optional<SaReturn> saReturnOptional = saReturnRepository.findById(record.getId());
                    if (saReturnOptional.isPresent()) {
                        saReturnOptional.get().setRecorded(false);
                        saReturnRepository.save(saReturnOptional.get());
                        if (Boolean.TRUE.equals(saReturnOptional.get().isIsDeliveryVoucher())) {
                            Optional<RSInwardOutward> rsInwardOutwardOptional = rSInwardOutwardRepository.findById(saReturnOptional.get().getRsInwardOutwardID());
                            if (rsInwardOutwardOptional.isPresent()) {
                                rsInwardOutwardOptional.get().setRecorded(false);
                                rSInwardOutwardRepository.save(rsInwardOutwardOptional.get());
                            }
                        }
                    }
                }
                break;
            case HANG_GIAM_GIA:
                rs = unrecord(record.getId(), null);
                if (rs) {
                    Optional<SaReturn> saReturnOptional = saReturnRepository.findById(record.getId());
                    if (saReturnOptional.isPresent()) {
                        saReturnOptional.get().setRecorded(false);
                        saReturnRepository.save(saReturnOptional.get());
                    }
                }
                break;
            case PP_SERVICE:
                rs = unrecord(record.getId(), null);
                if (rs) {
                    Optional<PPService> ppServiceOptional = ppServiceRepository.findById(record.getId());
                    if (ppServiceOptional.isPresent()) {
                        ppServiceOptional.get().setRecorded(false);
                        ppServiceRepository.save(ppServiceOptional.get());
//                        if (ppInvoiceDetailCostRepository.existsByPpServiceID(ppServiceOptional.get().getId())) {
//                            ppInvoiceDetailCostRepository.removePPServiceByPpServiceID(ppServiceOptional.get().getId());
//                        }
                        if (ppServiceRepository.checkHasPaid(ppServiceOptional.get().getId())) {
                            mcPaymentDetailVendorRepository.findByPPInvoiceID(ppServiceOptional.get().getId()).ifPresent(mcPaymentDetailVendor -> {
                                unrecord(mcPaymentDetailVendor.getmCPaymentID(), null);
                                mcPaymentDetailVendorRepository.delete(mcPaymentDetailVendor);
                                mcPaymentRepository.deleteById(mcPaymentDetailVendor.getmCPaymentID());
                            });
                            mbTellerPaperDetailVendorRepository.findByPPInvoiceID(ppServiceOptional.get().getId()).ifPresent(mbTellerPaperDetailVendor -> {
                                unrecord(mbTellerPaperDetailVendor.getmBTellerPaperID(), null);
                                mbTellerPaperDetailVendorRepository.delete(mbTellerPaperDetailVendor);
                                mbTellerPaperRepository.deleteById(mbTellerPaperDetailVendor.getmBTellerPaperID());
                            });
                            mbCreditCardDetailVendorRepository.findByPPInvoiceID(ppServiceOptional.get().getId()).ifPresent(mbCreditCardDetailVendor -> {
                                unrecord(mbCreditCardDetailVendor.getmBCreditCardID(), null);
                                mbCreditCardDetailVendorRepository.delete(mbCreditCardDetailVendor);
                                mbCreditCardRepository.deleteById(mbCreditCardDetailVendor.getmBCreditCardID());
                            });
                        }
                        if (ppServiceOptional.get().getPaymentVoucherID() != null) {
                            switch (ppServiceOptional.get().getTypeID()) {
                                case Constants.PPServiceType.PPSERVICE_CASH:
                                    mcPaymentRepository.updateRecordById(ppServiceOptional.get().getPaymentVoucherID(), false);
                                    unrecord(ppServiceOptional.get().getPaymentVoucherID(), null);
                                    break;
                                case Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER:
                                case Constants.PPServiceType.PPSERVICE_TRANSFER_SEC:
                                case Constants.PPServiceType.PPSERVICE_CASH_SEC:
                                    mbTellerPaperRepository.updateRecordById(ppServiceOptional.get().getPaymentVoucherID(), false);
                                    unrecord(ppServiceOptional.get().getPaymentVoucherID(), null);
                                    break;
                                case Constants.PPServiceType.PPSERVICE_CREDIT_CARD:
                                    mbCreditCardRepository.updateRecordById(ppServiceOptional.get().getPaymentVoucherID(), false);
                                    unrecord(ppServiceOptional.get().getPaymentVoucherID(), null);
                                    break;
                            }
                        }
                    }

                }
                break;
            case TYPE_GOtherVoucher:
            case TYPE_GOtherVoucher_Allocations:
            case TYPE_NGHIEM_THU_CONG_TRINH:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    GOtherVoucher gOtherVoucher = gOtherVoucherRepository.findById(record.getId()).get();
                    gOtherVoucher.setRecorded(false);
                    gOtherVoucherRepository.save(gOtherVoucher);
                }
                break;
            case MUA_HANG_QUA_KHO:
                Optional<PPInvoice> ppInvoice = pPInvoiceRepository.findById(record.getId());
                if (ppInvoice.isPresent()) {
                    ppDiscountReturnRepository.updatePPInvoiceIDAndPPInvoiceDetailIDToNull(record.getId());
                    saInvoiceRepository.updatePPInvoiceIDAndPPInvoiceDetailIDToNull(record.getId());
                    Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                    if (ppInvoice.get().isStoredInRepository()) {
                        rsInwardOutward = rSInwardOutwardRepository.findById(ppInvoice.get().getRsInwardOutwardId());
                    }
                    if (rsInwardOutward.isPresent()) {
                        rs = unrecord(record.getId(), rsInwardOutward.get().getId());
                        if (rs) {
                            ppInvoice.get().setRecorded(false);
                            pPInvoiceRepository.save(ppInvoice.get());
                            rsInwardOutward.get().setRecorded(false);
                            rSInwardOutwardRepository.save(rsInwardOutward.get());
                            switch (ppInvoice.get().getTypeId()) {
                                case TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
                                    if (pPInvoiceRepository.checkHasPaid(ppInvoice.get().getId())) {
                                        mcPaymentDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mcPaymentDetailVendor -> {
                                            unrecord(mcPaymentDetailVendor.getmCPaymentID(), null);
                                            mcPaymentDetailVendorRepository.delete(mcPaymentDetailVendor);
                                            mcPaymentRepository.deleteById(mcPaymentDetailVendor.getmCPaymentID());
                                        });
                                        mbTellerPaperDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mbTellerPaperDetailVendor -> {
                                            unrecord(mbTellerPaperDetailVendor.getmBTellerPaperID(), null);
                                            mbTellerPaperDetailVendorRepository.delete(mbTellerPaperDetailVendor);
                                            mbTellerPaperRepository.deleteById(mbTellerPaperDetailVendor.getmBTellerPaperID());
                                        });
                                        mbCreditCardDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mbCreditCardDetailVendor -> {
                                            unrecord(mbCreditCardDetailVendor.getmBCreditCardID(), null);
                                            mbCreditCardDetailVendorRepository.delete(mbCreditCardDetailVendor);
                                            mbCreditCardRepository.deleteById(mbCreditCardDetailVendor.getmBCreditCardID());
                                        });
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                                    Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mcPaymentOptional.isPresent()) {
                                        mcPaymentOptional.get().setRecorded(false);
                                        mcPaymentRepository.save(mcPaymentOptional.get());
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:

                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:

                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                                    Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mbTellerPaperOptional.isPresent()) {
                                        mbTellerPaperOptional.get().setRecorded(false);
                                        mbTellerPaperRepository.save(mbTellerPaperOptional.get());
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                                    Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mbCreditCardOptional.isPresent()) {
                                        mbCreditCardOptional.get().setRecorded(false);
                                        mbCreditCardRepository.save(mbCreditCardOptional.get());
                                    }
                                    break;
                            }
                        }
                    } else {
                        rs = unrecord(record.getId(), null);

                        if (rs) {
                            ppInvoice.get().setRecorded(false);
                            pPInvoiceRepository.save(ppInvoice.get());
                            switch (ppInvoice.get().getTypeId()) {
                                case TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
                                    if (pPInvoiceRepository.checkHasPaid(ppInvoice.get().getId())) {
                                        mcPaymentDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mcPaymentDetailVendor -> {
                                            unrecord(mcPaymentDetailVendor.getmCPaymentID(), null);
                                            mcPaymentDetailVendorRepository.delete(mcPaymentDetailVendor);
                                            mcPaymentRepository.deleteById(mcPaymentDetailVendor.getmCPaymentID());
                                        });
                                        mbTellerPaperDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mbTellerPaperDetailVendor -> {
                                            unrecord(mbTellerPaperDetailVendor.getmBTellerPaperID(), null);
                                            mbTellerPaperDetailVendorRepository.delete(mbTellerPaperDetailVendor);
                                            mbTellerPaperRepository.deleteById(mbTellerPaperDetailVendor.getmBTellerPaperID());
                                        });
                                        mbCreditCardDetailVendorRepository.findByPPInvoiceID(ppInvoice.get().getId()).ifPresent(mbCreditCardDetailVendor -> {
                                            unrecord(mbCreditCardDetailVendor.getmBCreditCardID(), null);
                                            mbCreditCardDetailVendorRepository.delete(mbCreditCardDetailVendor);
                                            mbCreditCardRepository.deleteById(mbCreditCardDetailVendor.getmBCreditCardID());
                                        });
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                                    Optional<MCPayment> mcPaymentOptional = mcPaymentRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mcPaymentOptional.isPresent()) {
                                        mcPaymentOptional.get().setRecorded(false);
                                        mcPaymentRepository.save(mcPaymentOptional.get());
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:

                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:

                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                                    Optional<MBTellerPaper> mbTellerPaperOptional = mbTellerPaperRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mbTellerPaperOptional.isPresent()) {
                                        mbTellerPaperOptional.get().setRecorded(false);
                                        mbTellerPaperRepository.save(mbTellerPaperOptional.get());
                                    }
                                    break;
                                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                                    Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(ppInvoice.get().getPaymentVoucherId());
                                    if (mbCreditCardOptional.isPresent()) {
                                        mbCreditCardOptional.get().setRecorded(false);
                                        mbCreditCardRepository.save(mbCreditCardOptional.get());
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;
            case NHAP_KHO:
            case NHAP_KHO_DC:
            case NHAP_KHO_MH:
            case NHAP_KHO_HBTL:
                Optional<RSInwardOutward> optional = rSInwardOutwardRepository.findById(record.getId());
                if (optional.isPresent()) {
                    RSInwardOutward rsInwardOutward = optional.get();
                    rs = unrecord(record.getId(), rsInwardOutward.getId());
                    if (rs) {
                        rsInwardOutward.setRecorded(false);
                        rSInwardOutwardRepository.save(rsInwardOutward);
                    }
                }
                break;
            case BAN_HANG_CHUA_THU_TIEN:
            case BAN_HANG_THU_TIEN_NGAY_TM:
            case BAN_HANG_THU_TIEN_NGAY_CK:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    Optional<SAInvoice> saInvoiceOptional = saInvoiceRepository.findById(record.getId());
                    if (saInvoiceOptional.isPresent()) {
                        SAInvoice saInvoice = saInvoiceOptional.get();
                        saInvoice.setRecorded(false);
                        saInvoiceRepository.save(saInvoice);
                        if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_TM)) {
                            unrecord(saInvoice.getMcReceiptID(), null);
                            Optional<MCReceipt> mcReceiptOptional = mcReceiptRepository.findById(saInvoice.getMcReceiptID());
                            if (mcReceiptOptional.isPresent()) {
                                mcReceiptOptional.get().setRecorded(false);
                                mcReceiptRepository.save(mcReceiptOptional.get());
                            }
                        } else if (saInvoice.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_CK)) {
                            unrecord(saInvoice.getMbDepositID(), null);
                            Optional<MBDeposit> mbDepositOptional = mbDepositRepository.findById(saInvoice.getMbDepositID());
                            if (mbDepositOptional.isPresent()) {
                                mbDepositOptional.get().setRecorded(false);
                                mbDepositRepository.save(mbDepositOptional.get());
                            }
                        }
                        if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                            Optional<RSInwardOutward> rsInwardOutwardOptional = rSInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID());
                            if (rsInwardOutwardOptional.isPresent()) {
                                rsInwardOutwardOptional.get().setRecorded(false);
                                rSInwardOutwardRepository.save(rsInwardOutwardOptional.get());
                            }
                        }
//                    saInvoiceDetailsRepository.UpdateSABillIDNull(saInvoice.getId());
                        List<UUID> saInvoiceIDs = new ArrayList<>();
                        saInvoiceIDs.add(saInvoice.getId());
                        rsInwardOutWardDetailsRepository.UpdateSAInvoiceIDNull(saInvoiceIDs);
                        saReturnDetailsRepository.UpdateSAInvoiceIDNull(saInvoiceIDs);
                        List<String> listRelateID = saInvoiceRepository.getRelateIDBySAInvoiceID(saInvoiceIDs);
                        if (listRelateID.size() > 0) {
                            saInvoiceRepository.DeleteRelate(listRelateID);
                        }
                    }
                }
                break;
            case XUAT_KHO:
            case XUAT_KHO_BH:
            case XUAT_KHO_HMTL:
            case XUAT_KHO_DC:
                Optional<RSInwardOutward> option = rSInwardOutwardRepository.findById(record.getId());
                if (option.isPresent()) {
                    RSInwardOutward rsInwardOutward = option.get();
                    rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                    if (rs) {
                        rsInwardOutward.setRecorded(false);
                        rSInwardOutwardRepository.save(rsInwardOutward);
                    }
                }
                break;
            case CHUYEN_KHO:
            case CHUYEN_KHO_GUI_DAI_LY:
            case CHUYEN_KHO_CHUYEN_NOI_BO:
                Optional<RSTransfer> rsTransferOptional = rsTransferRepository.findById(record.getId());
                if (rsTransferOptional.isPresent()) {
                    RSTransfer rsTransfer = rsTransferOptional.get();
                    rs = unrecord(record.getId(), rsTransfer.getId());
                    if (rs) {
                        rsTransfer.setRecorded(false);
                        rsTransferRepository.save(rsTransfer);
                    }
                }
                break;
            case KET_CHUYEN_LAI_LO:
                Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(record.getId());
                if (gOtherVoucherOptional.isPresent()) {
                    GOtherVoucher gOtherVoucher = gOtherVoucherOptional.get();
                    msg = Constants.GOtherVoucher.KET_CHUYEN;
                    rs = unrecord(record.getId(), null);
                    if (rs) {
                        gOtherVoucher.setRecorded(false);
                        gOtherVoucherRepository.save(gOtherVoucher);
                    }
                }
                break;
            case TYPE_CPExpense_Tranfer:
                rs = unrecord(record.getId(), record.getRepositoryLedgerID());
                if (rs) {
                    CPExpenseTranfer cpExpenseTranfer = cpExpenseTranferRepository.findById(record.getId()).get();
                    cpExpenseTranfer.setRecorded(false);
                    cpExpenseTranferRepository.save(cpExpenseTranfer);
                }
                break;
            case GHI_TANG_CCDC:
                Optional<TIIncrement> tiIncrement = tiIncrementRepository.findById(record.getId());
                if (tiIncrement.isPresent()) {
                    TIIncrement tiIncrement1 = tiIncrement.get();
                    msg = "";
                    rs = unRecordTools(record.getId());
                    if (rs) {
                        tiIncrement1.setRecorded(false);
                        tiIncrementRepository.save(tiIncrement1);
                    }
                }
                break;
                case GHI_GIAM_CCDC:
                Optional<TIDecrement> tiDecrement = tiDecrementRepository.findById(record.getId());
                if (tiDecrement.isPresent()) {
                    TIDecrement tiDecrement1 = tiDecrement.get();
                    msg = "";
                    rs = unRecordTools(record.getId());
                    if (rs) {
                        tiDecrement1.setRecorded(false);
                        tiDecrementRepository.save(tiDecrement1);
                    }
                }
                break;
            case PHAN_BO_CCDC:
                Optional<TIAllocation> tiAllocation = tiAllocationRepository.findById(record.getId());
                if (tiAllocation.isPresent()) {
                    TIAllocation tiAllocation1 = tiAllocation.get();
                    msg = "";
                    rs = unRecordTools(record.getId());
                    if (rs) {
                        tiAllocation1.setRecorded(false);
                        tiAllocationRepository.save(tiAllocation1);
                    }
                }
                break;
                case DIEU_CHUYEN_CCDC:
                Optional<TITransfer> tiTransfer = tiTransferRepository.findById(record.getId());
                if (tiTransfer.isPresent()) {
                    TITransfer tiTransfer1 = tiTransfer.get();
                    msg = "";
                    rs = unRecordTools(record.getId());
                    if (rs) {
                        tiTransfer1.setRecorded(false);
                        tiTransferRepository.save(tiTransfer1);
                    }
                }
                break;
                case DIEU_CHINH_CCDC:
                Optional<TIAdjustment> tiAdjustment = tiAdjustmentRepository.findById(record.getId());
                if (tiAdjustment.isPresent()) {
                    TIAdjustment tiAdjustment1 = tiAdjustment.get();
                    msg = "";
                    rs = unRecordTools(record.getId());
                    if (rs) {
                        tiAdjustment1.setRecorded(false);
                        tiAdjustmentRepository.save(tiAdjustment1);
                    }
                }
                break;

            case TSCD_GHI_TANG:
                Optional<FAIncrement> faIncrement = faIncrementRepository.findById(record.getId());
                if (faIncrement.isPresent()) {
                    FAIncrement faIncrement1 = faIncrement.get();
                    msg = "";
                    rs = unRecordFixedAsset(record.getId());
                    if (rs) {
                        faIncrement1.setRecorded(false);
                        faIncrementRepository.save(faIncrement1);
                    }
                }
                break;
        }
        record.setSuccess(rs);
        record.setMsg(msg);
        return record;
    }

    @Override
    public HandlingResultDTO unRecord(RequestRecordListDTO requestRecordListDTO) {
        switch (requestRecordListDTO.getTypeIDMain()) {
            case PP_SERVICE:
                 handlingUnRecordPPService(requestRecordListDTO.getRecords());
                return new HandlingResultDTO();
            default:
                break;
        }
        return new HandlingResultDTO();
    }
    private void handlingUnRecordPPService(List<Record> records) {
        List<UUID> uuidPPService = records.stream().map(Record::getId).collect(Collectors.toList());
        viewVoucherNoRespository.deletePaymentVoucherInID(generalLedgerRepository.findAllPaymentVoucherByPPInvoiceId(uuidPPService));
        List<UUID> paymentVouchers = ppServiceRepository.findAllPaymentVoucherIDByID(uuidPPService);
        List<UUID> uuidsRecord = new ArrayList<>(uuidPPService);
        uuidsRecord.addAll(paymentVouchers);
        generalLedgerRepository.unrecordList(uuidsRecord);
        ppServiceRepository.updateMultiUnRecord(uuidPPService);
        if (!paymentVouchers.isEmpty()) {
            mcPaymentRepository.updateRecordInId(paymentVouchers, false);
            mbCreditCardRepository.updateRecordInId(paymentVouchers, false);
            mbTellerPaperRepository.updateRecordInId(paymentVouchers, false);
        }

    }
    @Override
    public UpdateDataDTO calculatingLiabilities(UUID accountingObjectId, String postDate) {
        postDate = DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(postDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY);
        return generalLedgerRepository.calculatingLiabilities(accountingObjectId, postDate);
    }

    @Override
    public List<GLCPExpenseList> getListForCPExpenseList() {
        return generalLedgerRepository.getListForCPExpenseList();
    }

    @Override
    public List<ViewGLPayExceedCashDTO> getViewGLPayExceedCash() {
        UserDTO userDTO = userService.getAccount();
        return generalLedgerRepository.getViewGLPayExceedCash(Utils.PhienSoLamViec(userDTO), userDTO.getOrganizationUnit().getId(), LocalDate.now());
    }

    @Override
    public Page<GiaThanhPoPupDTO> getByRatioMethod(Pageable pageable, String fromDate, String toDate, List<UUID> costSetID, Integer status) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return generalLedgerRepository.getByRatioMethod(pageable, fromDate, toDate, costSetID, status, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public CPAllocationDTO getByAllocationMethod(String fromDate, String toDate, List<UUID> costSetID, Integer status) {
        UserDTO userDTO = userService.getAccount();
        CPAllocationDTO cpAllocationDTO = new CPAllocationDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<GiaThanhAllocationPoPupDTO> listAllocationGeneralExpenseAll = cpAllocationGeneralExpenseRepository.getAllocationPeriod(currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, costSetID, Utils.PhienSoLamViec(userDTO));
        //List<GiaThanhAllocationPoPupDTO> listAllocationGeneralExpenseSums = cpAllocationGeneralExpenseRepository.getAllocationPeriodSum(currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, costSetID, Utils.PhienSoLamViec(userDTO));
        cpAllocationDTO.setGiaThanhAllocationPoPupDTOAll(listAllocationGeneralExpenseAll);
        //cpAllocationDTO.setGiaThanhAllocationPoPupDTOSums(listAllocationGeneralExpenseSums);
        return cpAllocationDTO;

    }

    @Override
    public List<GiaThanhPoPupDTO> getExpenseList(String fromDate, String toDate, List<UUID> costSetID) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<GiaThanhPoPupDTO>  cpExplist0 =  generalLedgerRepository.getExpenseList(fromDate, toDate, costSetID, 0, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
        List<GiaThanhPoPupDTO>  cpExplist1 =  generalLedgerRepository.getExpenseList(fromDate, toDate, costSetID, 1, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
        List<GiaThanhPoPupDTO> cpExpenseLists = new ArrayList<>();
        if (cpExplist0 != null && cpExplist0.size() > 0) {
            cpExpenseLists.addAll(cpExplist0);
        }
        if (cpExplist1 != null && cpExplist1.size() > 0) {
            cpExpenseLists.addAll(cpExplist1);
        }
        return cpExpenseLists;
    }
}
