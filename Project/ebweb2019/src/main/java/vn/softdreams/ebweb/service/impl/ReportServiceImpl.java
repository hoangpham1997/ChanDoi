package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MCAuditReportDTO;
import vn.softdreams.ebweb.service.ReportService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.Report.TIAdjustmentDTO;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.dto.Report.TITransferDTO;
import vn.softdreams.ebweb.service.util.*;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import vn.softdreams.ebweb.web.rest.dto.*;

import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTKNH;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTheTD;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private UnitRepository unitRepository;
    private MCReceiptRepository mcReceiptRepository;
    private MCPaymentRepository mcPaymentRepository;
    private MBTellerPaperRepository mBTellerPaperRepository;
    private MBTellerPaperDetailsRepository mBTellerPaperDetailsRepository;
    private MBDepositRepository mbDepositRepository;
    private MCAuditRepository mcAuditRepository;
    private SAInvoiceRepository saInvoiceRepository;
    private MaterialGoodsRepository materialGoodsRepository;
    private RSInwardOutwardRepository rsInwardOutwardRepository;
    private RSTransferRepository rsTransferRepository;
    private MBDepositDetailsRepository mbDepositDetailsRepository;
    private SAInvoiceDetailsRepository saInvoiceDetailsRepository;
    private MBCreditCardRepository mbCreditCardRepository;
    private MBCreditCardDetailsRepository mbCreditCardDetailsRepository;
    private UserRepository userRepository;
    private SaReturnRepository saReturnRepository;
    private AccountingObjectRepository accountingObjectRepository;
    private SAQuoteRepository saQuoteRepository;
    private IaPublishInvoiceRepository iaPublishInvoiceRepository;
    private IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;
    private InvoiceTypeRepository invoiceTypeRepository;
    private  final TIAllocationDetailsRepository tiAllocationDetailsRepository;
    private final TIAuditDetailsRepository tiAuditDetailsRepository;
    private final TIAuditRepository tiAuditRepository;
    private final TIAllocationRepository tiAllocationRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository;
    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final SAOrderRepository saOrderRepository;
    private final PporderRepository pporderRepository;
    private final UserService userService;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPServiceRepository ppServiceRepository;
    private final SaBillRepository sABillRepository;
    private final GOtherVoucherRepository gOtherVoucherRepository;
    private final GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository;
    private final ReportBusinessRepository reportRepository;
    private final RepositoryRepository repositoryRepository;
    private final UtilsService utilsService;
    private final TIAllocationPostRepository tiAllocationPostRepository;
    private final TIAuditMemberDetailsRepository tiAuditMemberDetailsRepository;
    private static final List<Integer> typeMBDeposit = Arrays.asList(160, 161, 162);
    private static final List<Integer> typeMBCreditCard = Arrays.asList(170, 171, 172, 173, 174);
    private final TIAdjustmentRepository tiAdjustmentRepository;
    private final TIDecrementRepository tiDecrementRepository;
    private final TITransferRepository tiTransferRepository;


    public ReportServiceImpl(UnitRepository unitRepository,
                             MCReceiptRepository mcReceiptRepository,
                             MBDepositRepository mbDepositRepository,
                             MBDepositDetailsRepository mbDepositDetailsRepository,
                             SAInvoiceDetailsRepository saInvoiceDetailsRepository,
                             MBTellerPaperRepository mBTellerPaperRepository,
                             MCPaymentRepository mcPaymentRepository,
                             MBTellerPaperDetailsRepository mBTellerPaperDetailsRepository,
                             MBCreditCardRepository mbCreditCardRepository,
                             MBCreditCardDetailsRepository mbCreditCardDetailsRepository,
                             SystemOptionRepository systemOptionRepository,
                             UserRepository userRepository,
                             TIAllocationDetailsRepository tiAllocationDetailsRepository, TIAuditDetailsRepository tiAuditDetailsRepository, OrganizationUnitRepository organizationUnitRepository,
                             BankAccountDetailsRepository bankAccountDetailsRepository,
                             OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository,
                             UserService userService, PPDiscountReturnRepository ppDiscountReturnRepository,
                             PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository, SAQuoteRepository saQuoteRepository,
                             SAOrderRepository saOrderRepository, PPInvoiceRepository ppInvoiceRepository,
                             SAInvoiceRepository saInvoiceRepository,
                             MaterialGoodsRepository materialGoodsRepository,
                             RSInwardOutwardRepository rsInwardOutwardRepository,
                             RSTransferRepository rsTransferRepository,
                             SaReturnRepository saReturnRepository,
                             MCAuditRepository mcAuditRepository, PporderRepository pporderRepository,
                             PPServiceRepository ppServiceRepository, IaPublishInvoiceRepository iaPublishInvoiceRepository,
                             IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository, InvoiceTypeRepository invoiceTypeRepository,
                             SaBillRepository sABillRepository, GOtherVoucherRepository gOtherVoucherRepository,
                             GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository,
                             ReportBusinessRepository reportRepository, RepositoryRepository repositoryRepository,
                             AccountingObjectRepository accountingObjectRepository, TIAdjustmentRepository tiAdjustmentRepository,
                             TIDecrementRepository tiDecrementRepository, TITransferRepository tiTransferRepository,
                             TIAuditRepository tiAuditRepository,
                             TIAuditMemberDetailsRepository tiAuditMemberDetailsRepository, TIAllocationRepository tiAllocationRepository, TIAllocationPostRepository tiAllocationPostRepository,
            UtilsService utilsService) {
        this.unitRepository = unitRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mbDepositRepository = mbDepositRepository;
        this.mbDepositDetailsRepository = mbDepositDetailsRepository;
        this.saInvoiceDetailsRepository = saInvoiceDetailsRepository;
        this.mBTellerPaperRepository = mBTellerPaperRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mBTellerPaperDetailsRepository = mBTellerPaperDetailsRepository;
        this.mbCreditCardRepository = mbCreditCardRepository;
        this.mbCreditCardDetailsRepository = mbCreditCardDetailsRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.userRepository = userRepository;
        this.tiAllocationDetailsRepository = tiAllocationDetailsRepository;
        this.tiAuditDetailsRepository = tiAuditDetailsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.userService = userService;
        this.saQuoteRepository = saQuoteRepository;
        this.organizationUnitOptionReportRepository = organizationUnitOptionReportRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.ppDiscountReturnDetailsRepository = ppDiscountReturnDetailsRepository;
        this.saOrderRepository = saOrderRepository;
        this.mcAuditRepository = mcAuditRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.pporderRepository = pporderRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.rsTransferRepository = rsTransferRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.iaPublishInvoiceRepository = iaPublishInvoiceRepository;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.sABillRepository = sABillRepository;
        this.gOtherVoucherRepository = gOtherVoucherRepository;
        this.gOtherVoucherDetailsRepository = gOtherVoucherDetailsRepository;
        this.reportRepository = reportRepository;
        this.repositoryRepository = repositoryRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.utilsService = utilsService;
        this.tiAdjustmentRepository = tiAdjustmentRepository;
        this.tiDecrementRepository = tiDecrementRepository;
        this.tiTransferRepository = tiTransferRepository;

        this.tiAuditRepository = tiAuditRepository;
        this.tiAllocationRepository = tiAllocationRepository;
        this.tiAuditMemberDetailsRepository = tiAuditMemberDetailsRepository;
        this.tiAllocationPostRepository = tiAllocationPostRepository;

    }

    @Override
    public byte[] getReport(UUID id, int typeID, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        switch (typeID) {
            case Constants.MCReceipt.TYPE_PHIEU_THU:
            case TypeConstant.PHIEU_THU_TIEN_KHACH_HANG:
                return getReportMCReceipt(id, typeReport);
            case Constants.MCPayment.TYPE_PHIEU_CHI:
            case TypeConstant.PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP:
                return getReportMCPayment(id, typeReport);
            case TypeConstant.DON_DAT_HANG:
                return getReportSAOrder(id, typeReport);
            case TypeConstant.DON_MUA_HANG:
                return getReportPPOrder(id, typeReport);
            case TypeConstant.PP_SERVICE:
                return getReportPPService(id, typeReport);
            case TypeConstant.NHAP_KHO:
                return getReportNhapKho(id, typeReport);
            case TypeConstant.XUAT_KHO:
                return getReportRSOutWard(id, typeReport);
            case TypeConstant.CHUYEN_KHO:
                return getReportRSTransfer(id, typeReport);
            case TypeConstant.XUAT_HOA_DON:
            case TypeConstant.XUAT_HOA_DON_BH:
            case TypeConstant.XUAT_HOA_DON_TRA_LAI:
                return getVatReport(sABillRepository.findById(id).get(), typeReport, true);
            case TypeConstant.KET_CHUYEN_LAI_LO:
                return getReportGOtherVoucher(id, typeReport);
            case TypeConstant.PHAN_BO_CHI_PHI_TRA_TRUOC:
                return getGOtherVoucher(id, typeReport);
            case TypeConstant.THE_TIN_DUNG:
                return getReportTheTinDung(id, typeReport);
            case TypeConstant.KIEM_KE_CCDC:
                return getReportTIAudit(id, typeReport);
            case TypeConstant.PHAN_BO_CCDC:
                return getReportTIAllocation(id, typeReport);
            case TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC:
                return getReportChungTuNghiepVuKhac(id, typeReport);
        }
        if (typeID == TypeConstant.KIEM_KE_QUY) {
            return getReportMCAudit(id, typeReport);
        }
        if (typeID == TypeConstant.GHI_GIAM_CCDC ) {
            return getListTIDecrement(id , typeReport);
        }
        if (typeID == TypeConstant.DIEU_CHUYEN_CCDC) {
            return getListTITransfer( id , typeReport);
        }
        if (typeID == TypeConstant.DIEU_CHINH_CCDC) {
            return getLitsTIAdjustment(id , typeReport);
        }
        if (typeID == TypeConstant.BAN_HANG_CHUA_THU_TIEN || typeID == TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK ||
            typeID == TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM) {
            return getReportSAInvoice(id, typeReport);
        }
        if (typeID == TypeConstant.MUA_HANG_TRA_LAI || typeID == TypeConstant.MUA_HANG_GIAM_GIA) {
            return getReportPPDiscountReturn(id, typeReport);
        }
        if (typeID == TypeConstant.HANG_BAN_TRA_LAI || typeID == TypeConstant.HANG_GIAM_GIA) {
            return getReportSAReturn(id, typeReport);
        }
        if (typeID == TypeConstant.TYPE_BAO_NO_UNC || typeID == TypeConstant.TYPE_BAO_NO_SCK
            || typeID == TypeConstant.TYPE_BAO_NO_STM || typeID == TypeConstant.TYPE_BAO_NO_NCC) {
            return getReportMBTellerPaper(id, typeReport);
        } else if (typeID == TypeConstant.MUA_HANG_QUA_KHO) {
            return getReportPPInvoice(id, typeReport);
        } else if (typeMBCreditCard.indexOf(typeID) != -1) {
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            UserDTO userDTO = userService.getAccount();
            String reportName = "ChungTuKeToan";
            MBCreditCard mbCreditCard = mbCreditCardRepository.findById(id).get();
            Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
            List<MBCreditCardDetails> mbCreditCardDetails = new ArrayList<>();
            for (int i = 0; i < mbCreditCard.getmBCreditCardDetails().size(); i++) {
                mbCreditCardDetails.add(mbCreditCardDetailsRepository.findByMBCreditCardID(id).get(i));
            }
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            Integer DDSo = 0;
            if (mbCreditCard.getCurrencyID() == organizationUnit.get().getCurrencyID()) {
                Optional<SystemOption> systemOption = systemOptionRepository.findOneByUser(user.get().getId(), "DDSo_TienVND");
                DDSo = Integer.parseInt(systemOption.get().getData());
            } else {
                Optional<SystemOption> systemOption = systemOptionRepository.findOneByUser(user.get().getId(), "DDSo_NgoaiTe");
                DDSo = Integer.parseInt(systemOption.get().getData());
            }
            Map<String, Object> parameter = new HashMap<>();
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            if (userWithAuthoritiesAndSystemOption.isPresent()) {
                List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                    .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
                List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                    .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
                boolean isNoMBook = collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
                boolean isSDSoQuanTri = collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
                if (Boolean.TRUE.equals(isSDSoQuanTri)) {
                    if (Boolean.TRUE.equals(isNoMBook)) {
                        parameter.put("No", mbCreditCard.getNoMBook());
                    } else {
                        parameter.put("No", mbCreditCard.getNoFBook());
                    }
                } else {
                    parameter.put("No", mbCreditCard.getNoFBook());
                }
            }
            parameter.put("AccountingObjectName", mbCreditCard.getAccountingObjectName());
            parameter.put("AccountingObjectAddress", mbCreditCard.getAccountingObjectAddress());
            parameter.put("CurrencyID", mbCreditCard.getCurrencyID());
            parameter.put("Date", convertDate(mbCreditCard.getDate()));
            parameter.put("TotalAmountOriginal", Utils.formatTien(mbCreditCard.getTotalAmountOriginal(), Constants.SystemOption.DDSo_DonGiaNT, userDTO));
            parameter.put("TotalAmount", Utils.formatTien(mbCreditCard.getTotalAmount(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("ExchangeRate", Utils.formatTien(mbCreditCard.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
            parameter.put("Accountant", "Accountannt");
            parameter.put("Manager", "Manager");
            parameter.put("Total", Utils.formatTien(mbCreditCard.getTotalAmountOriginal(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(mbCreditCard.getTotalAmountOriginal(), mbCreditCard.getCurrencyID() == null ? "VND" : mbCreditCard.getCurrencyID(), userDTO));
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
            String conversionPair = ' ' + organizationUnit.get().getCurrencyID() + '/' + mbCreditCard.getCurrencyID();
            parameter.put("ConversionPair", conversionPair);
            for (int i = 0; i < mbCreditCardDetails.size(); i++) {
                mbCreditCardDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbCreditCardDetails.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            // tạo file báo cáo
            JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            return ReportUtils.generateReportPDF(mbCreditCardDetails, parameter, jasperReport);
        } else if (TypeConstant.LIST_TYPE_BAO_CO.indexOf(typeID) != -1) {
            return getReportMBDeposit(id, typeReport);
        } else if (typeID == TypeConstant.BAO_GIA && typeReport == 1) {
            return getReportBaoGia(id, typeReport);
        } else if (typeID == TypeConstant.BAO_GIA && typeReport == 2) {
            return getReportBaoGia(id, typeReport);
        } else if (typeID == TypeConstant.THONG_BAO_PHAT_HANH_HDDT && typeReport == 1) {
            return getThongBaoPhatHanhHDDT(id);
        } else if (typeID == TypeConstant.THONG_BAO_PHAT_HANH_HDDT && typeReport == 2) {
            return getThongBaoPhatHanhHDDT_DI_TI(id);
        } else {
            return null;
        }
    }

    private byte[] getReportTIAllocation(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case Constants.Report.ChungTuPhanBoCCDC:
                Optional<TIAllocation> tiAllocation = tiAllocationRepository.findById(id);
                if (!tiAllocation.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                result = getChungTuPhanBoCCDC(id, typeReport);
                break;
            case Constants.Report.ChungTuKeToanCCDC:
                Optional<TIAllocation> tiAllocation1 = tiAllocationRepository.findById(id);
                if (!tiAllocation1.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                result = getChungTuKeToanCCDC(id, typeReport);
                break;
        }
        return result;
    }

    private byte[] getReportTIAudit(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case Constants.Report.BienBanKiemKeCCDC:
                Optional<TIAudit> tiAudit = tiAuditRepository.findById(id);
                if (!tiAudit.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                result = getBienBanKiemKeCCDC(id, typeReport);
                break;
        }
        return result;
    }

    private byte[] getChungTuKeToanCCDC(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "ChungTuKeToan_CCDC";
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        TIAllocation tiAllocation = tiAllocationRepository.findById(id).get();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", tiAllocation.getNoMBook());
            } else {
                parameter.put("No", tiAllocation.getNoFBook());
            }
        } else {
            parameter.put("No", tiAllocation.getNoFBook());
        }
        parameter.put("postedDate", convertDate(tiAllocation.getPostedDate()));
        parameter.put("reason", tiAllocation.getReason());
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TIAllocationPostDTO> tiAllocationPostDTOS = new ArrayList<>();
        List<TIAllocationPost> tiAllocationPosts = tiAllocationPostRepository.findByTIAllocationID(id);
        for (TIAllocationPost tiAllocationPost : tiAllocationPosts) {
            TIAllocationPostDTO tempDTO = new TIAllocationPostDTO();
            tempDTO.setDescription(tiAllocationPost.getDescription());
            tempDTO.setDebitAccount(tiAllocationPost.getDebitAccount());
            tempDTO.setCreditAccount(tiAllocationPost.getCreditAccount());
            tempDTO.setAmount(Utils.formatTien(tiAllocationPost.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            totalAmount = totalAmount.add(tiAllocationPost.getAmount());
            tiAllocationPostDTOS.add(tempDTO);
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        parameter.put("totalAmount", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAmountString", Utils.GetAmountInWords(totalAmount, organizationUnit.get().getCurrencyID(), userDTO));
        parameter.put("REPORT_MAX_COUNT", tiAllocationPostDTOS.size());
        parameter.put("isDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        parameter.put("chiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        parameter.put("director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(tiAllocationPostDTOS, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuPhanBoCCDC(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "ChungTuPhanBoCCDC";
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        TIAllocation tiAllocation = tiAllocationRepository.getOne(id);
        parameter.put("reason", tiAllocation.getReason());
        BigDecimal totalAllocationAmount = BigDecimal.ZERO;
        BigDecimal totalRemainingAmount = BigDecimal.ZERO;
        BigDecimal totalAll = BigDecimal.ZERO;
        List<TIAllocationReportDTO> tiAllocationReportDTOS = new ArrayList<>();
        List<TIAllocationDetailDTO> tiAllocationDetailDTOS = tiAllocationDetailsRepository.findTIAllocationDetails(id);
        for (TIAllocationDetailDTO tiAllocationDetailDTO : tiAllocationDetailDTOS) {
            TIAllocationReportDTO tiAllocationReportDTO = new TIAllocationReportDTO();
            tiAllocationReportDTO.setToolCode(tiAllocationDetailDTO.getToolCode());
            tiAllocationReportDTO.setToolName(tiAllocationDetailDTO.getToolName());
            tiAllocationReportDTO.setAllocationAmount(Utils.formatTien(tiAllocationDetailDTO.getAllocationAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tiAllocationReportDTO.setRemainingAmount(Utils.formatTien(tiAllocationDetailDTO.getRemainingAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tiAllocationReportDTO.setTotalAllocationAmount(Utils.formatTien(tiAllocationDetailDTO.getTotalAllocationAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tiAllocationReportDTOS.add(tiAllocationReportDTO);
            totalAllocationAmount = totalAllocationAmount.add(tiAllocationDetailDTO.getAllocationAmount());
            totalRemainingAmount = totalRemainingAmount.add(tiAllocationDetailDTO.getRemainingAmount());
            totalAll = totalAll.add(tiAllocationDetailDTO.getTotalAllocationAmount());
        }
        parameter.put("totalAllocationAmount", Utils.formatTien(totalAllocationAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalRemainingAmount", Utils.formatTien(totalRemainingAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAll", Utils.formatTien(totalAll, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("REPORT_MAX_COUNT", tiAllocationReportDTOS.size());
        parameter.put("isDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().isIsDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(tiAllocationReportDTOS, parameter, jasperReport);
        return result;
    }

    private byte[] getBienBanKiemKeCCDC(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "BienBanKiemKeCCDC";
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        TIAudit tiAudit = tiAuditRepository.findById(id).get();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", tiAudit.getNoMBook());
            } else {
                parameter.put("No", tiAudit.getNoFBook());
            }
        } else {
            parameter.put("No", tiAudit.getNoFBook());
        }
        parameter.put("date", "Ngày " + tiAudit.getDate().getDayOfMonth() + " tháng " + tiAudit.getDate().getMonthValue() + " năm " + tiAudit.getDate().getYear());
        parameter.put("description", tiAudit.getDescription());
        parameter.put("inventoryResult", tiAudit.getSummary());
        BigDecimal totalQuantityONBook = BigDecimal.ZERO;
        BigDecimal totalDiffQuantity = BigDecimal.ZERO;
        BigDecimal totalQuantityInventory = BigDecimal.ZERO;
        BigDecimal totalExecuteQuantity = BigDecimal.ZERO;
        List<Object> detail = new ArrayList<>();
        int count = 0;
        TIAuditReportDTO tiAuditReportDTO = new TIAuditReportDTO();
        List<TIAuditMemberDetailDTO> tiAuditMemberDetailDTOS = tiAuditMemberDetailsRepository.findTIAuditMemberDetailDTO(id);
        parameter.put("sizeTable1", tiAuditMemberDetailDTOS.size() == 0 ? 1 : tiAuditMemberDetailDTOS.size());
        for (TIAuditMemberDetailDTO tiAuditMemberDetailDTO : tiAuditMemberDetailDTOS) {
            TIAuditReportDTO tempDTO1 = new TIAuditReportDTO();
            tempDTO1.setOrganizationUnitName("- Ông/bà: " + tiAuditMemberDetailDTO.getAccountObjectName() +
                ", Chức vụ: " + tiAuditMemberDetailDTO.getAccountingObjectTitle() +
                ", Phòng ban: " + tiAuditMemberDetailDTO.getOrganizationUnitName() + ", Đại diện: ...........");
            detail.add(tempDTO1);
            count++;
        }
        if (detail.size() == 0) {
            detail.add(tiAuditReportDTO);
        }
        List<TIAuditDetailDTO> tiAuditDetailDTOS = tiAuditDetailsRepository.findTIAuditDetailDTO(id);
        parameter.put("sizeTable2", tiAuditDetailDTOS.size() == 0 ? 1 : tiAuditDetailDTOS.size());
        for (TIAuditDetailDTO tiAuditDetailDTO : tiAuditDetailDTOS) {
            totalQuantityONBook = totalQuantityONBook.add(tiAuditDetailDTO.getQuantityONBook());
            totalDiffQuantity = totalDiffQuantity.add(tiAuditDetailDTO.getDiffQuantity());
            totalQuantityInventory = totalQuantityInventory.add(tiAuditDetailDTO.getQuantityInventory());
            totalExecuteQuantity = totalExecuteQuantity.add(tiAuditDetailDTO.getExecuteQuantity());
            TIAuditReportDTO tempDTO2 = new TIAuditReportDTO();
            tempDTO2.setToolCode(tiAuditDetailDTO.getToolCode());
            tempDTO2.setToolName(tiAuditDetailDTO.getToolName());
            tempDTO2.setUnitName(tiAuditDetailDTO.getUnitName());
            tempDTO2.setOrganizationUnitToolName(tiAuditDetailDTO.getOrganizationUnitName());
            tempDTO2.setQuantityONBook(Utils.formatTien(tiAuditDetailDTO.getQuantityONBook(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            tempDTO2.setQuantityInventory(Utils.formatTien(tiAuditDetailDTO.getQuantityInventory(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            tempDTO2.setDiffQuantity(Utils.formatTien(tiAuditDetailDTO.getDiffQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            tempDTO2.setExecuteQuantity(Utils.formatTien(tiAuditDetailDTO.getExecuteQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            if (tiAuditDetailDTO.getRecommendation() == 0) {
                tempDTO2.setRecommendation("Ghi tăng");
            }
            if (tiAuditDetailDTO.getRecommendation() == 1) {
                tempDTO2.setRecommendation("Ghi giảm");
            }
            if (tiAuditDetailDTO.getRecommendation() == 2) {
                tempDTO2.setRecommendation("Không xử lý");
            }
            detail.add(tempDTO2);
        }
        if (detail.size() == count) {
            detail.add(tiAuditReportDTO);
        }
        parameter.put("totalQuantityONBook", Utils.formatTien(totalQuantityONBook, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("totalDiffQuantity", Utils.formatTien(totalDiffQuantity, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("totalQuantityInventory", Utils.formatTien(totalQuantityInventory, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("totalExecuteQuantity", Utils.formatTien(totalExecuteQuantity, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("REPORT_MAX_COUNT", detail.size());
        parameter.put("isDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        parameter.put("chiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        parameter.put("director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(detail, parameter, jasperReport);
        return result;
    }

    private byte[] getGoodAndServReport(SaBill saBill, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "Bangke_hanghoa_dichvu";
        List<SABillReportDTO> sABillDetailsDTO = sABillRepository.findSABillDetailsDTO(saBill.getId());
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        Map<String, Object> parameter = new HashMap<>();
        BigDecimal totalVatAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        checkEbPackage(userDTO, parameter);
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        if (!StringUtils.isEmpty(saBill.getInvoiceNo())) {
            parameter.put("invoiceNo", saBill.getInvoiceNo());
        }
        parameter.put("listNo", saBill.getListNo());
        parameter.put("invoiceDate", "Ngày " + saBill.getListDate().getDayOfMonth() + " Tháng " + saBill.getListDate().getMonthValue() + " Năm " + saBill.getListDate().getYear());
        parameter.put("date", "ngày " + saBill.getInvoiceDate().getDayOfMonth() + " tháng " + saBill.getInvoiceDate().getMonthValue() + " năm " + saBill.getInvoiceDate().getYear());
        for (SABillReportDTO saBillDetailsDTO : sABillDetailsDTO) {
            if (saBillDetailsDTO.getAmountOriginal() != null) {
                totalAmountOriginal = totalAmountOriginal.add(saBillDetailsDTO.getAmountOriginal());
            }
            if (saBillDetailsDTO.getVatAmountOriginal() != null) {
                totalVatAmountOriginal = totalVatAmountOriginal.add(saBillDetailsDTO.getVatAmountOriginal());
            }
            if (saBillDetailsDTO.getQuantity() != null) {
                saBillDetailsDTO.setQuantityString(Utils.formatTien(saBillDetailsDTO.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            }
            if (saBillDetailsDTO.getUnitPrice() != null) {
                saBillDetailsDTO.setUnitPriceString(Utils.formatTien(saBillDetailsDTO.getUnitPriceOriginal(), getTypeUnitPrice(userDTO, saBill.getCurrencyID()), userDTO));
            }
            if (saBillDetailsDTO.getAmountOriginal() != null) {
                saBillDetailsDTO.setAmountString(Utils.formatTien(saBillDetailsDTO.getAmountOriginal(), getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
            }
            if (saBillDetailsDTO.getVatAmountOriginal() != null) {
                saBillDetailsDTO.setVatAmountString(Utils.formatTien(saBillDetailsDTO.getVatAmountOriginal(), getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
            }
            if (saBillDetailsDTO.getVatRate() != null) {
                saBillDetailsDTO.setVatRateString(getVatRateString(saBillDetailsDTO.getVatRate()));
            }
            saBillDetailsDTO.setTotalAmountString(Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
        }
        parameter.put("REPORT_MAX_COUNT", sABillDetailsDTO.size());
        parameter.put("totalVatAmount", Utils.formatTien(totalVatAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));

        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(sABillDetailsDTO, parameter, jasperReport);
        return result;
    }

    private byte[] getGOtherVoucher(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        GOtherVoucher gOtherVoucher = gOtherVoucherRepository.findById(id).get();
        UserDTO userDTO = userService.getAccount();
        String currentBook = String.valueOf(Utils.PhienSoLamViec(userDTO));
//        RSInwardOutward bankAccountDetails = rs.findById(ppDiscountReturn.getBankAccountDetailID()).get();
        List<GOtherVoucherDetails> details = gOtherVoucherDetailsRepository.findAllByGOtherVoucherID(gOtherVoucher.getId());
//        for (int i = 0; i < ppDiscountReturn.getPpDiscountReturnDetails().size(); i++) {
//            details.add(mBTellerPaperDetailsRepository.findByMBTellerPaperID(id).get(i));
        Map<String, Object> parameter = new HashMap<>();
        String conversionPair = "";
        JasperReport jasperReport;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String creditAccount = "";
        String debitAccount = "";
        String repository = "";
        HashMap<String, String> creditAccountMap = new HashMap<String, String>();
        HashMap<String, String> debitAccountMap = new HashMap<String, String>();
        HashMap<String, String> repositoryMap = new HashMap<String, String>();
        checkEbPackage(userDTO, parameter);
        if (!Strings.isNullOrEmpty(creditAccount)) {
            creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
        }
        if (!Strings.isNullOrEmpty(debitAccount)) {
            debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
        }
        if (!Strings.isNullOrEmpty(repository)) {
            repository = repository.substring(0, repository.length() - 2);
        }
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
            totalAmountOriginal = totalAmountOriginal.add(details.get(i).getAmountOriginal());
        }
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
                reportName = "ChungTuKeToan";
                // khai báo key
                setHeader(userDTO, parameter);
                parameter.put("Date", convertDate(gOtherVoucher.getPostedDate()));
                parameter.put("CurrencyID", gOtherVoucher.getCurrencyID());
                parameter.put("Reason", gOtherVoucher.getReason());
                parameter.put("CheckShow", true);
//                if (!StringUtils.isEmpty(gOtherVoucher.getAccountingObjectName())) {
//                    parameter.put("AccountingObjectName", gOtherVoucher.getAccountingObjectName());
//                }
//                if (!StringUtils.isEmpty(gOtherVoucher.getAccountingObjectAddress())) {
//                    parameter.put("AccountingObjectAddress", ppDiscountReturn.getAccountingObjectAddress());
//                }
                parameter.put("REPORT_MAX_COUNT", details.size());
                parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? gOtherVoucher.getNoMBook() : gOtherVoucher.getNoFBook());
//                parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
                parameter.put("Total", Utils.formatTien(gOtherVoucher.getTotalAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("TotalAmount", Utils.formatTien(gOtherVoucher.getTotalAmount(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
//                parameter.put("numberAttach", gOtherVoucher.getNumberAttach());
//                parameter.put("establishment", organizationUnitOptionReport.get());
                parameter.put("Manager", organizationUnitOptionReport.getDirector());
                parameter.put("Accountant", organizationUnitOptionReport.getChiefAccountant());
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
//                số tiền bằng chư
                parameter.put("AmountInWord", Utils.GetAmountInWords(gOtherVoucher.getTotalAmountOriginal(), gOtherVoucher.getCurrencyID() == null ? "VND" : gOtherVoucher.getCurrencyID(), userDTO));
                parameter.put("ExchangeRate", Utils.formatTien(gOtherVoucher.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + gOtherVoucher.getCurrencyID();
                parameter.put("ConversionPair", conversionPair);
//                parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(gOtherVoucher.getCurrencyID()));
                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
                break;
        }
        return result;
    }

    private byte[] getReportPPDiscountReturn(UUID id, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        String reportName = "";
        PPDiscountReturn ppDiscountReturn = ppDiscountReturnRepository.findById(id).get();
        UserDTO userDTO = userService.getAccount();
        String currentBook = String.valueOf(Utils.PhienSoLamViec(userDTO));
//        RSInwardOutward bankAccountDetails = rs.findById(ppDiscountReturn.getBankAccountDetailID()).get();
//        List<PPDiscountReturnDetailsReportConvertDTO> details = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsReportByID(ppDiscountReturn.getId(), currentBook);
//        for (int i = 0; i < ppDiscountReturn.getPpDiscountReturnDetails().size(); i++) {
//            details.add(mBTellerPaperDetailsRepository.findByMBTellerPaperID(id).get(i));
        Map<String, Object> parameter = new HashMap<>();
        String conversionPair = "";
        JasperReport jasperReport;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String creditAccount = "";
        String debitAccount = "";
        String repository = "";
        HashMap<String, String> creditAccountMap = new HashMap<String, String>();
        HashMap<String, String> debitAccountMap = new HashMap<String, String>();
        HashMap<String, String> repositoryMap = new HashMap<String, String>();
        List<PPDiscountReturnDetailsReportConvertKTDTO> detailsKT = new ArrayList<>();
        List<PPDiscountReturnDetailsReportConvertDTO> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalOriginal = BigDecimal.ZERO;
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
            case Constants.Report.GiayBaoCo:
                detailsKT = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsReportKTByID(ppDiscountReturn.getId());
                for (int i = 0; i < detailsKT.size(); i++) {
                    if (!Strings.isNullOrEmpty(detailsKT.get(i).getCreditAccount())) {
                        if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(detailsKT.get(i).getCreditAccount()))) {
                            creditAccountMap.put(detailsKT.get(i).getCreditAccount(), detailsKT.get(i).getCreditAccount());
                            creditAccount += detailsKT.get(i).getCreditAccount() + ", ";
                        }
                    }
                    if (!Strings.isNullOrEmpty(detailsKT.get(i).getDebitAccount())) {
                        if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(detailsKT.get(i).getDebitAccount()))) {
                            debitAccountMap.put(detailsKT.get(i).getDebitAccount(), detailsKT.get(i).getDebitAccount());
                            debitAccount += detailsKT.get(i).getDebitAccount() + ", ";
                        }
                    }
                    if (typeReport == Constants.Report.GiayBaoCo) {
                        if (ppDiscountReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
                            detailsKT.get(i).setAmountToString(Utils.formatTien(detailsKT.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        } else {
                            detailsKT.get(i).setAmountOriginalToString(Utils.formatTien(detailsKT.get(i).getAmountOriginal(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                            detailsKT.get(i).setAmountToString(Utils.formatTien(detailsKT.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        }
                    } else {
                        detailsKT.get(i).setAmountToString(Utils.formatTien(detailsKT.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        detailsKT.get(i).setAmountOriginalToString(Utils.formatTien(detailsKT.get(i).getAmountOriginal(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                    }
                    total = total.add(detailsKT.get(i).getAmount());
                    totalOriginal = totalOriginal.add(detailsKT.get(i).getAmountOriginal());
                }
                break;
            case Constants.Report.PhieuThu:
            case Constants.Report.PhieuThuA5:
                details = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsReportByID(ppDiscountReturn.getId(), currentBook);
                for (int i = 0; i < details.size(); i++) {
                    if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                        if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                            creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                            creditAccount += details.get(i).getCreditAccount() + ", ";
                        }
                    }
                    if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                        if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                            debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                            debitAccount += details.get(i).getDebitAccount() + ", ";
                        }
                    }
                    if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                        if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                            repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryCode());
                            repository += details.get(i).getRepositoryCode() + ", ";
                        }
                    }
                    details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                    details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                    details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                }
                break;

        }

        if (!Strings.isNullOrEmpty(creditAccount)) {
            creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
        }
        if (!Strings.isNullOrEmpty(debitAccount)) {
            debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
        }
        if (!Strings.isNullOrEmpty(repository)) {
            repository = repository.substring(0, repository.length() - 2);
        }

//        for (int i = 0; i < details.size(); i++) {
////            var.setAmountToString(Utils.formatTien(ppDiscountReturn.getTotalAmount(), Constants.SystemOption.DDSo_TienVND));
//            details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND));
//            details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
//            details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID())));
//        }
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        checkEbPackage(userDTO, parameter);
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
                reportName = "ChungTuKeToan";
                // khai báo key
                setHeader(userDTO, parameter);
                parameter.put("Date", convertDate(ppDiscountReturn.getPostedDate()));
                parameter.put("CurrencyID", ppDiscountReturn.getCurrencyID());
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectName())) {
                    parameter.put("AccountingObjectName", ppDiscountReturn.getAccountingObjectName());
                }
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectAddress())) {
                    parameter.put("AccountingObjectAddress", ppDiscountReturn.getAccountingObjectAddress());
                }
                parameter.put("REPORT_MAX_COUNT", detailsKT.size());
                parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
                parameter.put("Total", Utils.formatTien(totalOriginal, getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                parameter.put("TotalAmount", Utils.formatTien(total, Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("numberAttach", ppDiscountReturn.getNumberAttach());
                parameter.put("Reason", ppDiscountReturn.getReason());
//                parameter.put("establishment", organizationUnitOptionReport.get());
                parameter.put("Manager", organizationUnitOptionReport.getDirector());
                parameter.put("Accountant", organizationUnitOptionReport.getChiefAccountant());
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppDiscountReturn.getCurrencyID()));
//                số tiền bằng chư
                if (totalOriginal != null) {
                    parameter.put("AmountInWord", Utils.GetAmountInWords(totalOriginal, ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
                }
                parameter.put("ExchangeRate", Utils.formatTien(ppDiscountReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppDiscountReturn.getCurrencyID();
                parameter.put("ConversionPair", conversionPair);
                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                result = ReportUtils.generateReportPDF(detailsKT, parameter, jasperReport);
                break;
            case Constants.Report.ChungTuKeToanQuyDoi:
                reportName = "ChungTuKeToanQuyDoi";
                // khai báo key
                setHeader(userDTO, parameter);
                parameter.put("Date", convertDate(ppDiscountReturn.getPostedDate()));
                parameter.put("CurrencyID", ppDiscountReturn.getCurrencyID());
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectName())) {
                    parameter.put("AccountingObjectName", ppDiscountReturn.getAccountingObjectName());
                }
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectAddress())) {
                    parameter.put("AccountingObjectAddress", ppDiscountReturn.getAccountingObjectAddress());
                }
                parameter.put("REPORT_MAX_COUNT", detailsKT.size());
                parameter.put("Reason", ppDiscountReturn.getReason());
                parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
                parameter.put("TotalQD", Utils.formatTien(total, Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("Total", Utils.formatTien(totalOriginal, getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                parameter.put("numberAttach", ppDiscountReturn.getNumberAttach());
//                parameter.put("establishment", organizationUnitOptionReport.get());
                parameter.put("Manager", organizationUnitOptionReport.getDirector());
                parameter.put("Accountant", organizationUnitOptionReport.getChiefAccountant());
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppDiscountReturn.getCurrencyID()));
//                số tiền bằng chư
                if (total != null) {
                    parameter.put("AmountInWord", Utils.GetAmountInWords(total, ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
                }
                parameter.put("ExchangeRate", Utils.formatTien(ppDiscountReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppDiscountReturn.getCurrencyID();
                parameter.put("ConversionPair", conversionPair);

                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                result = ReportUtils.generateReportPDF(detailsKT, parameter, jasperReport);
                break;
            case Constants.Report.PhieuThu:
            case Constants.Report.PhieuThuA5:
                if (Constants.Report.PhieuThu == typeReport) {
                    reportName = "MCReceipt";
                } else if (Constants.Report.PhieuThuA5 == typeReport) {
                    reportName = "MCReceipt-A5";
                }
                // khai báo key
                setHeader(userDTO, parameter);
                parameter.put("Ngay", "Ngày " + ppDiscountReturn.getDate().getDayOfMonth() + " tháng " + ppDiscountReturn.getDate().getMonthValue() + " năm " + ppDiscountReturn.getDate().getYear());
                parameter.put("currencyID", ppDiscountReturn.getCurrencyID());
                parameter.put("No", creditAccount);
                parameter.put("Co", debitAccount);
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectName())) {
                    parameter.put("NguoiNop", getName(ppDiscountReturn.getAccountingObjectName(), null));
                }
                if (!StringUtils.isEmpty(ppDiscountReturn.getAccountingObjectAddress())) {
                    parameter.put("DiaChiNgNop", ppDiscountReturn.getAccountingObjectAddress());
                }
                if (!StringUtils.isEmpty(ppDiscountReturn.getReason())) {
                    parameter.put("LyDoNop", ppDiscountReturn.getReason());
                }
                if (!StringUtils.isEmpty(ppDiscountReturn.getNumberAttach())) {
                    parameter.put("KemTheo", ppDiscountReturn.getNumberAttach());
                }
                parameter.put("So", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
                parameter.put("SoTien", Utils.formatTien(ppDiscountReturn.getTotalAmountOriginal().add(ppDiscountReturn.getTotalVATAmountOriginal()), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                parameter.put("SoTien_String", Utils.GetAmountInWords(ppDiscountReturn.getTotalAmountOriginal().add(ppDiscountReturn.getTotalVATAmountOriginal()), ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
                parameter.put("TyGiaNT", ppDiscountReturn.getExchangeRate() == null ? "1" : Utils.formatTien(ppDiscountReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                parameter.put("TienQuyDoi", Utils.formatTien(ppDiscountReturn.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                    parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                    parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
//                    parameter.put("NguoiNopTien", ppDiscountReturn.getPayers());
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                        parameter.put("NguoiLapBieu", userDTO.getFullName());
                    } else {
                        parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                    }
                    parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
                }
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                parameter.put("NguoiNopTien", ppDiscountReturn.getContactName());
//                số tiền bằng chư
                parameter.put("amountInWord", Utils.GetAmountInWords(ppDiscountReturn.getTotalAmountOriginal(), ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
                parameter.put("ExchangeRate", Utils.formatTien(ppDiscountReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppDiscountReturn.getCurrencyID();
                parameter.put("ConversionPair", conversionPair);
                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                List<ExampleDTO> detail = new ArrayList<>();
                detail.add(new ExampleDTO("dataX", "dataX1"));
                result = ReportUtils.generateReportPDF(detail, parameter, jasperReport);
                break;
            case Constants.Report.GiayBaoCo:
                reportName = "GiayBaoCo";
                // khai báo key
//                parameter.put("AccountingObjectName", ppDiscountReturn.getAccountingObjectName());
                parameter.put("AccountingObjectName", !Strings.isNullOrEmpty(ppDiscountReturn.getContactName()) ?
                    ppDiscountReturn.getContactName() + (!Strings.isNullOrEmpty(ppDiscountReturn.getAccountingObjectName()) ? " - " +
                        ppDiscountReturn.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(ppDiscountReturn.getAccountingObjectName())
                    ? ppDiscountReturn.getAccountingObjectName() : "");
                setHeader(userDTO, parameter);
                parameter.put("AccountingObjectAddress", ppDiscountReturn.getAccountingObjectAddress());
                parameter.put("Reason", ppDiscountReturn.getReason());
                parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
                parameter.put("Date", convertDate(ppDiscountReturn.getDate()));
                parameter.put("BankName", ppDiscountReturn.getAccountingObjectBankName());
                parameter.put("AmountOriginal", Utils.formatTien(totalOriginal, getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                parameter.put("CurrencyID", ppDiscountReturn.getCurrencyID());
                parameter.put("ExchangeRate", Utils.formatTien(ppDiscountReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                parameter.put("Manager", "Manager");
                parameter.put("AmountInWord", Utils.GetAmountInWords(totalOriginal, ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
                if (ppDiscountReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
                    parameter.put("isShowCurrency", false);
                } else {
                    parameter.put("isShowCurrency", true);
                }
                if (ppDiscountReturn.getAccountingObjectBankAccount() != null) {
                    parameter.put("BankAccount", ppDiscountReturn.getAccountingObjectBankAccount());
                }
                if (!ppDiscountReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
                    String currencyAmountOriginal = "Số tiền nguyên tệ (" + ppDiscountReturn.getCurrencyID() + ")";
                    String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
                    parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
                    parameter.put("CurrencyInWord", currencyAmount);
                }
                parameter.put("REPORT_MAX_COUNT", detailsKT.size());
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                parameter.put("Director", organizationUnitOptionReport.getDirector());
                parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
                parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
                if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
                    parameter.put("Reporter", organizationUnitOptionReport.getReporter());
                } else {
                    parameter.put("Reporter", user.get().getFullName());
                }
                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
//                   khai báo list detail
                List<ExampleDTO> detailReport = new ArrayList<>();
                detailReport.add(new ExampleDTO("dataX", "dataX1"));
                detailReport.add(new ExampleDTO("dataY", "dataY1"));
                detailReport.add(new ExampleDTO("dataZ", "dataZ1"));
                // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details
//                        result = ReportUtils.generateReportPDF(detailReport, parameter, jasperReport);
                result = ReportUtils.generateReportPDF(detailsKT, parameter, jasperReport);
                break;
            case Constants.Report.PhieuXuatKho:
                result = getXuatKhoHangMuaTraLai(ppDiscountReturn, typeReport);
                break;
            case 10:
            case 11:
                if (ppDiscountReturn.getIsBill()) {
                    SaBill saBill = sABillRepository.findById(ppDiscountReturn.getPpDiscountReturnDetails().stream().collect(Collectors.toList()).get(0).getSaBillID()).get();
                    if (saBill != null) {
                        result = getVatReport(saBill, typeReport, ppDiscountReturn.getTypeID());
                    }
                }
                break;
            case Constants.Report.BangKeDichVu:
                if (ppDiscountReturn.getIsBill()) {
                    SaBill saBill = sABillRepository.findById(ppDiscountReturn.getPpDiscountReturnDetails().stream().collect(Collectors.toList()).get(0).getSaBillID()).get();
                    if (saBill != null) {
                        result = getGoodAndServReport(saBill, typeReport);
                    }
                }
        }
        return result;
    }

    private byte[] getXuatKhoHangMuaTraLai(PPDiscountReturn ppDiscountReturn, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        if (ppDiscountReturn != null) {
            UserDTO userDTO = userService.getAccount();
            String currentBook = String.valueOf(Utils.PhienSoLamViec(userDTO));
            List<PPDiscountReturnDetailsReportConvertDTO> details = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsReportByID(ppDiscountReturn.getId(), currentBook);
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            BigDecimal totalAmount = BigDecimal.ZERO;
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));

                // Nếu đồng tiền hoạch toán != đồng tiền QĐ thì fill đơn giá QĐ và Thành tiền QĐ
                if (!userDTO.getOrganizationUnit().getCurrencyID().equals(ppDiscountReturn.getCurrencyID())) {
                    details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPrice(), Constants.SystemOption.DDSo_DonGia, userDTO));
                    details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), getTypeUnitPrice(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
                    details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            reportName = "phieu_xuat_kho";
            // khai báo key
            setHeader(userDTO, parameter);
            checkEbPackage(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(ppDiscountReturn.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(ppDiscountReturn.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(ppDiscountReturn.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? ppDiscountReturn.getNoMBook() : ppDiscountReturn.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(ppDiscountReturn.getContactName()) ?
                ppDiscountReturn.getContactName() + (!Strings.isNullOrEmpty(ppDiscountReturn.getAccountingObjectName()) ? " - " +
                    ppDiscountReturn.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(ppDiscountReturn.getAccountingObjectName())
                ? ppDiscountReturn.getAccountingObjectName() : "");
            parameter.put("contactName", ppDiscountReturn.getContactName());
            parameter.put("accountingObjectAddress", ppDiscountReturn.getAccountingObjectAddress());
            parameter.put("description", ppDiscountReturn.getReason());
            parameter.put("numberAttach", ppDiscountReturn.getNumberAttach());
            for (int i = 0; i < details.size(); i++) {
                totalAmount = totalAmount.add(details.get(i).getAmount());
            }
            if (!userDTO.getOrganizationUnit().getCurrencyID().equals(ppDiscountReturn.getCurrencyID())) {
                parameter.put("total", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                parameter.put("total", Utils.formatTien(ppDiscountReturn.getTotalAmountOriginal(), getTypeAmount(userDTO, ppDiscountReturn.getCurrencyID()), userDTO));
            }
            parameter.put("reporter", organizationUnitOptionReport.getReporter());
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (ppDiscountReturn.getTotalAmountOriginal() != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(ppDiscountReturn.getTotalAmountOriginal(), ppDiscountReturn.getCurrencyID() == null ? "VND" : ppDiscountReturn.getCurrencyID(), userDTO));
            }
            if (!userDTO.getOrganizationUnit().getCurrencyID().equals(ppDiscountReturn.getCurrencyID()) && totalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalAmount, userDTO.getOrganizationUnit().getCurrencyID() == null ? "VND" : userDTO.getOrganizationUnit().getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getReportNhapKho(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        Optional<RSInwardOutward> rsInwardOutwardOptional = rsInwardOutwardRepository.findById(id);
        if (!rsInwardOutwardOptional.isPresent()) {
            throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
        }
        RSInwardOutward rsInwardOutward = rsInwardOutwardOptional.get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                result = getChungTuKeToan(rsInwardOutward, typeReport);
                break;
            case Constants.Report.PhieuNhapKho:
            case Constants.Report.PhieuNhapKhoA5:
                result = getPhieuNhapKho(rsInwardOutward, typeReport);
                break;
        }
        return result;
    }

    private byte[] getPhieuNhapKho(RSInwardOutward rsInwardOutward, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        if (rsInwardOutward != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsInwardOutwardRepository.getRSInwardOutWardDetails(rsInwardOutward.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), getTypeUnitPrice(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            if (typeReport == Constants.Report.PhieuNhapKho) {
                reportName = "phieu_nhap_kho";
            } else if (typeReport == Constants.Report.PhieuNhapKhoA5) {
                reportName = "phieu_nhap_kho_A5";
            }
            // khai báo key
            setHeader(userDTO, parameter);
            checkEbPackage(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            parameter.put("amountInWord", Utils.GetAmountInWords(rsInwardOutward.getTotalAmountOriginal(), userDTO.getOrganizationUnit().getCurrencyID() == null ? "VND" : userDTO.getOrganizationUnit().getCurrencyID(), userDTO));
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getXuatKho(RSInwardOutward rsInwardOutward, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        if (rsInwardOutward != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsInwardOutwardRepository.getRSInwardOutWardDetails(rsInwardOutward.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), getTypeUnitPrice(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            if (typeReport == Constants.Report.PhieuXuatKho) {
                reportName = "phieu_xuat_kho";
            } else if (typeReport == Constants.Report.PhieuXuatKhoA5) {
                reportName = "phieu_xuat_kho_A5";
            }
            // khai báo key
            setHeader(userDTO, parameter);
            checkEbPackage(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (rsInwardOutward.getTotalAmountOriginal() != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(rsInwardOutward.getTotalAmountOriginal(), rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }


    private byte[] getReportSAOrder(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        SAOrder saOrder = saOrderRepository.findById(id).get();
        switch (typeReport) {
            case Constants.Report.BieuMau:
                result = getDonDatHang(saOrder);
                break;
        }
        return result;
    }

    private byte[] getDonDatHang(SAOrder saOrder) throws JRException {
        String conversionPair = "";
        List<SAOrderDTO> saOrderDetailsDTO = saOrderRepository.findSAOrderDetailsDTO(saOrder.getId());
        Optional<   User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String reportName = "SAOrder";
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        parameter.put("AccountingObjectName", saOrder.getAccountingObjectName() == null ? "" : saOrder.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", saOrder.getAccountingObjectAddress() == null ? "" : saOrder.getAccountingObjectAddress());
        parameter.put("Description", saOrder.getReason());
        parameter.put("taxCode", organizationUnit.get().gettaxCode());
        parameter.put("Date", convertDate(saOrder.getDate()));
        parameter.put("deliveryDate", convertDate(saOrder.getDeliverDate()));
        parameter.put("deliveryPlace", saOrder.getDeliveryPlace());
        parameter.put("No", saOrder.getNo());
        parameter.put("CurrencyID", saOrder.getCurrencyID());
        parameter.put("isDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
        BigDecimal totalDiscountAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAmountDiscountedOriginal = BigDecimal.ZERO;
        BigDecimal totalVatAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SAOrderDTO saOrderDTO : saOrderDetailsDTO) {
            saOrderDTO.setAmountOriginalString(Utils.formatTien(saOrderDTO.getAmountOriginal(), getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
            saOrderDTO.setDiscountAmountOriginalString(Utils.formatTien(saOrderDTO.getDiscountAmountOriginal(), getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
            saOrderDTO.setQuantityString(Utils.formatTien(saOrderDTO.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            saOrderDTO.setUnitPriceString(Utils.formatTien(saOrderDTO.getUnitPriceOriginal(), getTypeUnitPrice(userDTO, saOrder.getCurrencyID()), userDTO));
            if (saOrderDTO.getDiscountAmountOriginal() != null) {
                totalDiscountAmountOriginal = totalDiscountAmountOriginal.add(saOrderDTO.getDiscountAmountOriginal());
            }
            if (saOrderDTO.getvATAmount() != null) {
                totalAmountOriginal = totalAmountOriginal.add(saOrderDTO.getvATAmountOriginal());
            }
            if (saOrderDTO.getvATAmountOriginal() != null) {
                totalVatAmountOriginal = totalVatAmountOriginal.add(saOrderDTO.getvATAmountOriginal());
            }
            totalAmountDiscountedOriginal = saOrder.getTotalAmountOriginal().subtract(totalDiscountAmountOriginal);
            totalAmountOriginal = totalAmountOriginal.add(saOrderDTO.getAmountOriginal().subtract(totalDiscountAmountOriginal));
            totalAmount = totalAmountOriginal.multiply(saOrder.getExchangeRate());
        }
        parameter.put("totalDiscountAmountOriginal", Utils.formatTien(totalDiscountAmountOriginal, getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
        parameter.put("totalAmountDiscountedOriginal", Utils.formatTien(totalAmountDiscountedOriginal, getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
        parameter.put("totalVatAmountOriginal", Utils.formatTien(totalVatAmountOriginal, getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
        parameter.put("totalAmountOriginal", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));
        parameter.put("totalAmount", Utils.formatTien(totalAmount, getTypeAmount(userDTO, saOrder.getCurrencyID()), userDTO));

        if (saOrderDetailsDTO.stream().map(SAOrderDTO::getDiscountRate).distinct().collect(Collectors.toList()).size() == 1) {
            BigDecimal discountRate = saOrderDetailsDTO.get(0).getDiscountRate();
            parameter.put("discountRateString", Utils.formatTien(discountRate, Constants.SystemOption.DDSo_TyLe, userDTO));
        }
        parameter.put("vATAmountString", Utils.formatTien(saOrder.getTotalVATAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalDiscountAmountString", Utils.formatTien(saOrder.getTotalDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAmountString", Utils.formatTien(saOrder.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("discountAmountString", Utils.formatTien(saOrder.getTotalDiscountAmount(), Constants.SystemOption.DDSo_DonGiaNT, userDTO));
        parameter.put("discountAmountOriginalString", Utils.formatTien(saOrder.getTotalDiscountAmountOriginal(), Constants.SystemOption.DDSo_DonGiaNT, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(saOrder.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, saOrder.getCurrencyID() == null ? "VND" : saOrder.getCurrencyID(), userDTO));
        conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + saOrder.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        BigDecimal vATRate = BigDecimal.ZERO;


        parameter.put("vATRateString", getVatRateString(saOrderDetailsDTO.get(0).getvATRate()));
        try {
            for (int i = 1; i < saOrderDetailsDTO.size(); i++) {
                if (!saOrderDetailsDTO.get(i).getvATRate().equals(saOrderDetailsDTO.get(0).getvATRate())) {
                    parameter.put("vATRateString", "/");
                }
            }
        } catch (NullPointerException e) {

        }
        parameter.put("REPORT_MAX_COUNT", saOrderDetailsDTO.size());
        parameter.put("isVisibleCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saOrder.getCurrencyID()));
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail

        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        return ReportUtils.generateReportPDF(saOrderDetailsDTO, parameter, jasperReport);
    }

    private byte[] getReportPPOrder(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        PPOrder ppOrder = pporderRepository.findById(id).get();
        String conversionPair = "";
        List<PPOrderDTO> saOrderDetailsDTO = pporderRepository.findPPOrderDetailsDTO(id);
        Collections.sort(saOrderDetailsDTO, Comparator.comparingInt(PPOrderDTO::getOrderPriority));
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        switch (typeReport) {
            case Constants.Report.BieuMau:
                String reportName = "PPOrder";
                // khai báo key
                Map<String, Object> parameter = new HashMap<>();
                UserDTO userDTO = userService.getAccount();
                userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
                /*Header*/
                setHeader(userDTO, parameter);
                checkEbPackage(userDTO, parameter);
                parameter.put("AccountingObjectName", ppOrder.getAccountingObjectName() == null ? "" : ppOrder.getAccountingObjectName());
                parameter.put("AccountingObjectAddress", ppOrder.getAccountingObjectAddress() == null ? "" : ppOrder.getAccountingObjectAddress());
                parameter.put("AccountingObjectTaxCode", ppOrder.getCompanyTaxCode() == null ? "" : ppOrder.getCompanyTaxCode());
                parameter.put("description", ppOrder.getReason());
                parameter.put("detailsSize", saOrderDetailsDTO.size());
                parameter.put("Date", convertDate(ppOrder.getDate()));
                parameter.put("deliveryDate", convertDate(ppOrder.getDeliverDate()));
                parameter.put("deliveryPlace", ppOrder.getShippingPlace());
                parameter.put("No", ppOrder.getNo());
                parameter.put("CurrencyID", ppOrder.getCurrencyId());
                parameter.put("isDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
                    parameter.put("Reporter", organizationUnitOptionReport.getReporter());
                } else {
                    parameter.put("Reporter", user.get().getFullName());
                }
                parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                BigDecimal totalDiscountAmountOriginal = BigDecimal.ZERO;
                BigDecimal totalDiscountAmount = BigDecimal.ZERO;
                BigDecimal totalAmountDiscountedOriginal = BigDecimal.ZERO;
                BigDecimal totalAmountDiscounted = BigDecimal.ZERO;
                BigDecimal totalVatAmountOriginal = BigDecimal.ZERO;
                BigDecimal totalVatAmount = BigDecimal.ZERO;
                BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (PPOrderDTO ppOrderDTO : saOrderDetailsDTO) {
                    ppOrderDTO.setAmountOriginalString(Utils.formatTien(ppOrderDTO.getAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    ppOrderDTO.setDiscountAmountOriginalString(Utils.formatTien(ppOrderDTO.getDiscountAmountOriginal(), getTypeAmount(userDTO, ppOrder.getCurrencyId()), userDTO));
                    ppOrderDTO.setQuantityString(Utils.formatTien(ppOrderDTO.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                    ppOrderDTO.setUnitPriceString(Utils.formatTien(ppOrderDTO.getUnitPriceOriginal(), getTypeUnitPrice(userDTO, ppOrder.getCurrencyId()), userDTO));
                    if (ppOrderDTO.getDiscountAmountOriginal() != null) {
                        totalDiscountAmountOriginal = totalDiscountAmountOriginal.add(ppOrderDTO.getDiscountAmountOriginal());
                    }
                    if (ppOrderDTO.getDiscountAmount() != null) {
                        totalDiscountAmount = totalDiscountAmount.add(ppOrderDTO.getDiscountAmount());
                    }
//                    if (ppOrderDTO.getvATAmount() != null) {
//                        totalAmountOriginal = totalAmountOriginal.add(ppOrderDTO.getvATAmountOriginal());
//                    }
                    if (ppOrderDTO.getvATAmountOriginal() != null) {
                        totalVatAmountOriginal = totalVatAmountOriginal.add(ppOrderDTO.getvATAmountOriginal());
                    }
                    if (ppOrderDTO.getvATAmount() != null) {
                        totalVatAmount = totalVatAmount.add(ppOrderDTO.getvATAmount());
                    }

                    totalAmountOriginal = totalAmountOriginal.add(ppOrderDTO.getAmountOriginal());
                    totalAmount = totalAmount.add(ppOrderDTO.getAmount());
//                    totalAmount = totalAmountOriginal.multiply(ppOrder.getExchangeRate());
                }
                totalAmountDiscountedOriginal = totalAmountOriginal.subtract(totalDiscountAmountOriginal);
                totalAmountDiscounted = totalAmount.subtract(totalDiscountAmount);
                parameter.put("totalDiscountAmountOriginal", Utils.formatTien(totalDiscountAmountOriginal, getTypeAmount(userDTO, ppOrder.getCurrencyId()), userDTO));
                parameter.put("totalAmountDiscountedOriginal", Utils.formatTien(totalAmountDiscountedOriginal, getTypeAmount(userDTO, ppOrder.getCurrencyId()), userDTO));
                parameter.put("totalVatAmountOriginal", Utils.formatTien(totalVatAmountOriginal, getTypeAmount(userDTO, ppOrder.getCurrencyId()), userDTO));
                parameter.put("totalAmountOriginal", Utils.formatTien(totalAmountDiscountedOriginal.add(totalVatAmountOriginal), getTypeAmount(userDTO, ppOrder.getCurrencyId()), userDTO));
                parameter.put("totalAmount", Utils.formatTien(totalAmountDiscounted.add(totalVatAmount), Constants.SystemOption.DDSo_TienVND, userDTO));
//                if (saOrderDetailsDTO.stream().filter(n -> n.getDiscountRate() != saOrderDetailsDTO.get(0).getDiscountRate()).collect(Collectors.toList()).size() == 0) {
//                    BigDecimal discountRate = saOrderDetailsDTO.get(0).getDiscountRate();
//                    parameter.put("discountRateString", Utils.formatTien(discountRate, Constants.SystemOption.DDSo_TyLe, userDTO));
//                }
                if (saOrderDetailsDTO.stream().map(PPOrderDTO::getDiscountRate).distinct().collect(Collectors.toList()).size() == 1) {
                    BigDecimal discountRate = saOrderDetailsDTO.get(0).getDiscountRate();
                    parameter.put("discountRateString", Utils.formatTien(discountRate, Constants.SystemOption.DDSo_TyLe, userDTO));
                }
                parameter.put("vATAmountString", Utils.formatTien(ppOrder.getTotalVATAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("totalDiscountAmountString", Utils.formatTien(ppOrder.getTotalDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("totalAmountString", Utils.formatTien(ppOrder.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("discountAmountString", Utils.formatTien(ppOrder.getTotalDiscountAmount(), Constants.SystemOption.DDSo_DonGiaNT, userDTO));
                parameter.put("discountAmountOriginalString", Utils.formatTien(ppOrder.getTotalDiscountAmountOriginal(), Constants.SystemOption.DDSo_DonGiaNT, userDTO));
                parameter.put("ExchangeRate", Utils.formatTien(ppOrder.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountDiscountedOriginal.add(totalVatAmountOriginal), ppOrder.getCurrencyId() == null ? "VND" : ppOrder.getCurrencyId(), userDTO));
                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppOrder.getCurrencyId();
                parameter.put("ConversionPair", conversionPair);
                BigDecimal vATRate = BigDecimal.ZERO;
                parameter.put("vATRateString", getVatRateString(saOrderDetailsDTO.get(0).getvATRate()));
                try {
                    for (int i = 1; i < saOrderDetailsDTO.size(); i++) {
                        if (!saOrderDetailsDTO.get(i).getvATRate().equals(saOrderDetailsDTO.get(0).getvATRate())) {
                            parameter.put("vATRateString", "/");
                        }
                    }
                } catch (NullPointerException e) {

                }
                parameter.put("REPORT_MAX_COUNT", saOrderDetailsDTO.size());
                parameter.put("isVisibleCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppOrder.getCurrencyId()));
                // tạo file báo cáo
                JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                // khai báo list detail

                // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
                result = ReportUtils.generateReportPDF(saOrderDetailsDTO, parameter, jasperReport);
                break;
        }
        return result;
    }

    private byte[] getReportMBTellerPaper(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        MBTellerPaper mBTellerPaper = mBTellerPaperRepository.findById(id).get();
        UserDTO userDTO = userService.getAccount();
        BankAccountDetails bankAccountDetails = new BankAccountDetails();
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String format_NguyenTe = "";
        if (mBTellerPaper.getCurrencyId().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
        }
        if (mBTellerPaper.getBankAccountDetailID() != null) {
            bankAccountDetails = bankAccountDetailsRepository.findById(mBTellerPaper.getBankAccountDetailID()).get();
        }
        List<MBTellerPaperDetails> details = new ArrayList<>(mBTellerPaper.getmBTellerPaperDetails());
        details.sort(Comparator.comparingInt(MBTellerPaperDetails::getOrderPriority));
//        for (int i = 0; i < mBTellerPaper.getmBTellerPaperDetails().size(); i++) {
//            details.add(mBTellerPaperDetailsRepository.findByMBTellerPaperID(id).get(i));
//        }
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setAmountOriginalString(Utils.formatTien(details.get(i).getAmountOriginal(), format_NguyenTe, userDTO));
            details.get(i).setAmountString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        Map<String, Object> parameter = new HashMap<>();
//        String conversionPair = "";
        JasperReport jasperReport;
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                return getChungTuKeToan(mBTellerPaper, typeReport);
            case Constants.Report.GiayBaoNo:
                reportName = "GiayBaoNo";
                // khai báo key
                parameter.put("AccountingObjectName", mBTellerPaper.getAccountingObjectName());
                parameter.put("AccountingObjectAddress", mBTellerPaper.getAccountingObjectAddress());
                parameter.put("CurrencyID", mBTellerPaper.getCurrencyId());
                Boolean currentBook = Utils.PhienSoLamViec(userDTO).equals(1);
                checkEbPackage(userDTO, parameter);
                if (Boolean.TRUE.equals(currentBook)) {
                    parameter.put("No", mBTellerPaper.getNoMBook());
                } else {
                    parameter.put("No", mBTellerPaper.getNoFBook());
                }
                parameter.put("PostedDate", convertDate(mBTellerPaper.getPostedDate()));
                parameter.put("TotalAmountOriginal", Utils.formatTien(mBTellerPaper.getTotalAmountOriginal(), format_NguyenTe, userDTO));
                parameter.put("TotalAmount", Utils.formatTien(mBTellerPaper.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("ExchangeRate", Utils.formatTien(mBTellerPaper.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                parameter.put("AmountOriginal", Utils.formatTien(mBTellerPaper.getTotalAmountOriginal(), format_NguyenTe, userDTO) + " ");
                parameter.put("Amount", Utils.formatTien(mBTellerPaper.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("BankName", mBTellerPaper.getBankName());
                if (mBTellerPaper.getBankAccountDetailID() != null) {
                    parameter.put("BankAccount", bankAccountDetails.getBankAccount());
                }
                parameter.put("Reason", mBTellerPaper.getReason());
                parameter.put("ExchangeRate", Utils.formatTien(mBTellerPaper.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                parameter.put("Manager", "Manager");
                parameter.put("Total", Utils.formatTien(mBTellerPaper.getTotalAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
                parameter.put("AmountInWord", Utils.GetAmountInWords(mBTellerPaper.getTotalAmountOriginal(), mBTellerPaper.getCurrencyId() == null ? "VND" : mBTellerPaper.getCurrencyId(), userDTO));
                parameter.put("CurrencyInWord", "Số tiền nguyên tệ (" + mBTellerPaper.getCurrencyId() + ")");
//                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mBTellerPaper.getCurrencyId();
//                parameter.put("ConversionPair", conversionPair);
                if (mBTellerPaper.getCurrencyId().equals(organizationUnit.get().getCurrencyID())) {
                    parameter.put("isShowCurrency", false);
                } else {
                    parameter.put("isShowCurrency", true);
                }
                setHeader(userDTO, parameter);
                checkEbPackage(userDTO, parameter);
                if (!mBTellerPaper.getCurrencyId().equals(organizationUnit.get().getCurrencyID())) {
                    String currencyAmountOriginal = "Số tiền nguyên tệ (" + mBTellerPaper.getCurrencyId() + ")";
                    String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
                    parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
                    parameter.put("CurrencyInWord", currencyAmount);
                    parameter.put("REPORT_MAX_COUNT", details.size());
                }
                parameter.put("REPORT_MAX_COUNT", details.size());
                parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
                parameter.put("Director", organizationUnitOptionReport.getDirector());
                parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
                parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
                if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
                    parameter.put("Reporter", organizationUnitOptionReport.getReporter());
                } else {
                    parameter.put("Reporter", user.get().getFullName());
                }
                // tạo file báo cáo
                jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
                break;
        }
        return result;
    }

    private byte[] getReportBaoGia(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case 1:
                result = getBaoGiaMauCoBan(id);
                break;
            case 2:
                result = getBaoGiaMauDayDu(id);
                break;
        }
        return result;
    }


    private byte[] getReportChungTuNghiepVuKhac(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(id);
                if (!gOtherVoucherOptional.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                GOtherVoucher gOtherVoucher = gOtherVoucherOptional.get();
                result = getChungTuKeToanCT(gOtherVoucher, typeReport);
                break;
        }
        return result;
    }

    private byte[] getChungTuKeToanCT(GOtherVoucher gOtherVoucher, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<GOtherVoucherDetails> gOtherVoucherDetails = new ArrayList<>(gOtherVoucher.getgOtherVoucherDetails());
        Collections.sort(gOtherVoucherDetails, Comparator.comparingInt(GOtherVoucherDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        checkEbPackage(userDTO, parameter);
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (!StringUtils.isEmpty("")) {
            parameter.put("AccountingObjectName", "");
        }
        if (!StringUtils.isEmpty("")) {
            parameter.put("AccountingObjectAddress", "");
        }
        parameter.put("CurrencyID", gOtherVoucher.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", gOtherVoucher.getNoMBook());
            } else {
                parameter.put("No", gOtherVoucher.getNoFBook());
            }
        } else {
            parameter.put("No", gOtherVoucher.getNoFBook());
        }
        parameter.put("Date", convertDate(gOtherVoucher.getDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(gOtherVoucher.getTotalAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(gOtherVoucher.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(gOtherVoucher.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + gOtherVoucher.getCurrencyID();
        parameter.put("Reason", gOtherVoucher.getReason());
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(gOtherVoucher.getTotalAmountOriginal(), gOtherVoucher.getCurrencyID() == null ? "VND" : gOtherVoucher.getCurrencyID(), userDTO));
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < gOtherVoucherDetails.size(); i++) {
                gOtherVoucherDetails.get(i).setAmountOriginalToString(Utils.formatTien(gOtherVoucherDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
                gOtherVoucherDetails.get(i).setAmountToString(Utils.formatTien(gOtherVoucherDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            parameter.put("Total", Utils.formatTien(gOtherVoucher.getTotalAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(gOtherVoucher.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < gOtherVoucherDetails.size(); i++) {
                gOtherVoucherDetails.get(i).setAmountOriginalToString(Utils.formatTien(gOtherVoucherDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
            }
            parameter.put("Total", Utils.formatTien(gOtherVoucher.getTotalAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(gOtherVoucher.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", gOtherVoucherDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(gOtherVoucherDetails, parameter, jasperReport);
        return result;
    }


    private byte[] getReportTheTinDung(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                Optional<MBCreditCard> mbCreditCardOptional = mbCreditCardRepository.findById(id);
                if (!mbCreditCardOptional.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                MBCreditCard mbCreditCard = mbCreditCardOptional.get();
                result = getChungTuKeToanTTD(mbCreditCard, typeReport);
                break;
        }
        return result;
    }

    private byte[] getChungTuKeToanTTD(MBCreditCard mbCreditCard, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<MBCreditCardDetails> mbCreditCardDetails = new ArrayList<>(mbCreditCard.getmBCreditCardDetails());
        Collections.sort(mbCreditCardDetails, Comparator.comparingInt(MBCreditCardDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        checkEbPackage(userDTO, parameter);
        if (!StringUtils.isEmpty(mbCreditCard.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", mbCreditCard.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(mbCreditCard.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", mbCreditCard.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", mbCreditCard.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", mbCreditCard.getNoMBook());
            } else {
                parameter.put("No", mbCreditCard.getNoFBook());
            }
        } else {
            parameter.put("No", mbCreditCard.getNoFBook());
        }
        parameter.put("Date", convertDate(mbCreditCard.getDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(mbCreditCard.getTotalAmountOriginal(), getTypeAmount(userDTO, mbCreditCard.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(mbCreditCard.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(mbCreditCard.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mbCreditCard.getCurrencyID();
        parameter.put("Reason", mbCreditCard.getReason());
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(mbCreditCard.getTotalAmountOriginal(), mbCreditCard.getCurrencyID() == null ? "VND" : mbCreditCard.getCurrencyID(), userDTO));
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < mbCreditCardDetails.size(); i++) {
                mbCreditCardDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbCreditCardDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbCreditCard.getCurrencyID()), userDTO));
                mbCreditCardDetails.get(i).setAmountToString(Utils.formatTien(mbCreditCardDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            parameter.put("Total", Utils.formatTien(mbCreditCard.getTotalAmountOriginal(), getTypeAmount(userDTO, mbCreditCard.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(mbCreditCard.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < mbCreditCardDetails.size(); i++) {
                mbCreditCardDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbCreditCardDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbCreditCard.getCurrencyID()), userDTO));
            }
            parameter.put("Total", Utils.formatTien(mbCreditCard.getTotalAmountOriginal(), getTypeAmount(userDTO, mbCreditCard.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mbCreditCard.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", mbCreditCardDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(mbCreditCardDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getReportMBDeposit(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                Optional<MBDeposit> mbDepositOptional = mbDepositRepository.findById(id);
                if (!mbDepositOptional.isPresent()) {
                    throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
                }
                MBDeposit mbDeposit = mbDepositOptional.get();
                result = getChungTuKeToanBC(mbDeposit, typeReport);
                break;
            case 2:
                result = getGiayBaoCo(id);
                break;
        }
        return result;
    }

    private byte[] getChungTuKeToanBC(MBDeposit mbDeposit, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<MBDepositDetails> mbDepositDetails = new ArrayList<>(mbDeposit.getmBDepositDetails());
        Collections.sort(mbDepositDetails, Comparator.comparingInt(MBDepositDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        checkEbPackage(userDTO, parameter);
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (!StringUtils.isEmpty(mbDeposit.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", mbDeposit.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(mbDeposit.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", mbDeposit.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", mbDeposit.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", mbDeposit.getNoMBook());
            } else {
                parameter.put("No", mbDeposit.getNoFBook());
            }
        } else {
            parameter.put("No", mbDeposit.getNoFBook());
        }
        parameter.put("Date", convertDate(mbDeposit.getDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(mbDeposit.getTotalAmountOriginal(), getTypeAmount(userDTO, mbDeposit.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(mbDeposit.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(mbDeposit.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mbDeposit.getCurrencyID();
        parameter.put("Reason", mbDeposit.getReason());
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(mbDeposit.getTotalAmountOriginal(), mbDeposit.getCurrencyID() == null ? "VND" : mbDeposit.getCurrencyID(), userDTO));
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < mbDepositDetails.size(); i++) {
                mbDepositDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbDepositDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbDeposit.getCurrencyID()), userDTO));
                mbDepositDetails.get(i).setAmountToString(Utils.formatTien(mbDepositDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            parameter.put("Total", Utils.formatTien(mbDeposit.getTotalAmountOriginal(), getTypeAmount(userDTO, mbDeposit.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(mbDeposit.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < mbDepositDetails.size(); i++) {
                mbDepositDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbDepositDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbDeposit.getCurrencyID()), userDTO));
            }
            parameter.put("Total", Utils.formatTien(mbDeposit.getTotalAmountOriginal(), getTypeAmount(userDTO, mbDeposit.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mbDeposit.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", mbDepositDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(mbDepositDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getGiayBaoCo(UUID id) throws JRException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String reportName = "GiayBaoCo";
        MBDeposit mbDeposit = mbDepositRepository.findById(id).get();
        List<MBDeposit> lstMBDeposit = new ArrayList<>();
        MBDeposit mbDeposit1 = mbDepositRepository.findById(id).get();
        lstMBDeposit.add(mbDeposit1);
        List<MBDepositDetails> mbDepositDetails = new ArrayList<>();
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        mbDepositDetails.addAll(mbDepositDetailsRepository.findByMBDepositID(id));
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String format_NguyenTe;
        String format_DonGia;
        if (mbDeposit.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
        }
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        parameter.put("AccountingObjectName", mbDeposit.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", mbDeposit.getAccountingObjectAddress());
        parameter.put("Reason", mbDeposit.getReason());
        Boolean currentBook = Utils.PhienSoLamViec(userDTO).equals(1);
        if (Boolean.TRUE.equals(currentBook)) {
            parameter.put("No", mbDeposit.getNoMBook());
        } else {
            parameter.put("No", mbDeposit.getNoFBook());
        }
        parameter.put("Date", convertDate(mbDeposit.getDate()));
        parameter.put("BankName", mbDeposit.getBankName());
        parameter.put("AmountOriginal", Utils.formatTien(mbDeposit.getTotalAmountOriginal(), format_NguyenTe, userDTO) + " " + mbDeposit.getCurrencyID());
        parameter.put("CurrencyID", mbDeposit.getCurrencyID());
        parameter.put("ExchangeRate", Utils.formatTien(mbDeposit.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("Manager", "Manager");
        parameter.put("AmountInWord", Utils.GetAmountInWords(mbDeposit.getTotalAmountOriginal(), mbDeposit.getCurrencyID() == null ? "VND" : mbDeposit.getCurrencyID(), userDTO));
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        String currencyAmountOriginal = "Số tiền nguyên tệ (" + mbDeposit.getCurrencyID() + ")";
        String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
        parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
        parameter.put("CurrencyInWord", currencyAmount);
        if (mbDeposit.getBankAccountDetailID() != null) {
            List<UUID> listCompanyIDTKNH = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH);
            List<UUID> listCompanyIDTTD = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTheTD);
            List<ComboboxBankAccountDetailDTO> comboboxBankAccountDetailDTOS = bankAccountDetailsRepository.findAllByIsActive(listCompanyIDTKNH, listCompanyIDTTD, currentUserLoginAndOrg.get().getOrgGetData());
            Optional<ComboboxBankAccountDetailDTO> comboboxBankAccountDetailDTO = comboboxBankAccountDetailDTOS.stream().filter(a->a.getId().equals(mbDeposit.getBankAccountDetailID())).findFirst();
            if(comboboxBankAccountDetailDTO.isPresent()){
                parameter.put("BankAccount", comboboxBankAccountDetailDTO.get().getBankAccount());
            }
        }
        if (mbDeposit.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("isShowCurrency", false);
        } else {
            parameter.put("isShowCurrency", true);
        }
        parameter.put("REPORT_MAX_COUNT", mbDepositDetails.size());
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("Director", organizationUnitOptionReport.getDirector());
        parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
        parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        for (int i = 0; i < mbDepositDetails.size(); i++) {
            mbDepositDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbDepositDetails.get(i).getAmountOriginal(), format_NguyenTe, userDTO));
            mbDepositDetails.get(i).setAmountToString(Utils.formatTien(mbDepositDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }

        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
//             khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        details.add(new ExampleDTO("dataY", "dataY1"));
        details.add(new ExampleDTO("dataZ", "dataZ1"));
        // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details
        return ReportUtils.generateReportPDF(mbDepositDetails, parameter, jasperReport);
    }

    private byte[] getBaoGiaMauCoBan(UUID id) throws JRException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String reportName = "BaoGia_MauCoBan";
        SAQuote saQuote = saQuoteRepository.findById(id).get();
        UserDTO userDTO = userService.getAccount();
        List<SAQuote> saQuoteList = new ArrayList<>();
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        saQuoteList.add(saQuote);
        List<SAQuoteReportDTO> saQuoteReportDTOS = saQuoteRepository.findSAQuoteDetailsReport(saQuote.getId());
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        String format_NguyenTe;
        String format_DonGia;
        if (saQuote.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
            format_DonGia = Constants.SystemOption.DDSo_DonGia;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
            format_DonGia = Constants.SystemOption.DDSo_DonGiaNT;
        }
      //  Boolean isShow = true;
        Boolean isShowTax = true;
        Boolean isShowDiscount = true;
        BigDecimal taxValue = BigDecimal.ZERO;
        BigDecimal discountValue = BigDecimal.ZERO;
        if (saQuoteReportDTOS.get(0).getvATRate() != null) {
            taxValue = saQuoteReportDTOS.get(0).getvATRate();
        }
        if (saQuoteReportDTOS.get(0).getDiscountRate() != null) {
            discountValue = saQuoteReportDTOS.get(0).getDiscountRate();
        }
        for (int i = 0; i < saQuoteReportDTOS.size(); i++) {
            saQuoteReportDTOS.get(i).setQuantityString((Utils.formatTien(saQuoteReportDTOS.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO)));
            saQuoteReportDTOS.get(i).setUnitPriceOriginalString((Utils.formatTien(saQuoteReportDTOS.get(i).getUnitPriceOriginal(), format_DonGia, userDTO)));
            saQuoteReportDTOS.get(i).setAmountOriginalString((Utils.formatTien(saQuoteReportDTOS.get(i).getAmountOriginal(), format_NguyenTe, userDTO)));
            if (saQuoteReportDTOS.get(i).getvATRate() == null || taxValue.floatValue() != saQuoteReportDTOS.get(i).getvATRate().floatValue()) {
                isShowTax = false;
            }
            if (saQuoteReportDTOS.get(i).getDiscountRate() == null || discountValue.floatValue() != saQuoteReportDTOS.get(i).getDiscountRate().floatValue()) {
                isShowDiscount = false;
            }
        }
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("AccountingObjectName", saQuote.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", saQuote.getAccountingObjectAddress());
        parameter.put("Reason", saQuote.getReason());
        parameter.put("No", saQuote.getNo());
        parameter.put("Date", convertDate(saQuote.getDate()));
        parameter.put("TotalAmount", Utils.formatTien((saQuote.getTotalAmount().add(saQuote.getTotalVATAmount())).subtract(saQuote.getTotalDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("CurrencyID", saQuote.getCurrencyID());
        parameter.put("ContactName", saQuote.getContactName());
        parameter.put("ContactMobile", saQuote.getContactMobile());
        parameter.put("ContactEmail", saQuote.getContactEmail());
        parameter.put("DiscountAmountOriginal", Utils.formatTien(saQuote.getTotalDiscountAmountOriginal(), format_NguyenTe, userDTO));
        parameter.put("TotalDiscountAmount", Utils.formatTien((saQuote.getTotalAmountOriginal()).subtract(saQuote.getTotalDiscountAmountOriginal()), format_NguyenTe, userDTO));
        parameter.put("VATAmountOriginal", Utils.formatTien(saQuote.getTotalVATAmountOriginal(), format_NguyenTe, userDTO));

//        if (saQuoteReportDTOS.size() > 1) {
//            isShow = false;
//        } else {
//            isShow = true;
//        }
        parameter.put("DiscountRate", saQuoteReportDTOS.get(0).getDiscountRate() != null ? Utils.formatTien(saQuoteReportDTOS.get(0).getDiscountRate(), Constants.SystemOption.DDSo_TyLe, userDTO) + "%" : "");
        String vATRate = "";
        if (saQuoteReportDTOS.get(0).getvATRate() != null) {
            if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 0) {
                vATRate = "0%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 1) {
                vATRate = "5%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 2) {
                vATRate = "10%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 3) {
                vATRate = "KCT";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 4) {
                vATRate = "KTT";
            }
        }
        parameter.put("VATRate", vATRate);
        parameter.put("isShowTax", isShowTax);
        parameter.put("isShowDiscount", isShowDiscount);
        parameter.put("ExchangeRate", Utils.formatTien(saQuote.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("Total", Utils.formatTien(((saQuote.getTotalAmountOriginal().subtract(saQuote.getTotalDiscountAmountOriginal())).add(saQuote.getTotalVATAmountOriginal())), format_NguyenTe, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords((saQuote.getTotalAmountOriginal().subtract(saQuote.getTotalDiscountAmountOriginal())).add(saQuote.getTotalVATAmountOriginal()), saQuote.getCurrencyID() == null ? "VND" : saQuote.getCurrencyID(), userDTO));
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        if (saQuote.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("IsShowCurrency", false);
        } else {
            parameter.put("IsShowCurrency", true);
        }
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("REPORT_MAX_COUNT", saQuoteReportDTOS.size());
        parameter.put("Director", organizationUnitOptionReport.getDirector());

        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details
        return ReportUtils.generateReportPDF(saQuoteReportDTOS, parameter, jasperReport);
    }

    private byte[] getThongBaoPhatHanhHDDT(UUID id) throws JRException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        String reportName = "Thong_bao_phat_hanh_hddt";
        IaPublishInvoice iaPublishInvoice = iaPublishInvoiceRepository.findById(id).get();
        List<IaPublishInvoiceDetails> iaPublishInvoiceDetails = iaPublishInvoiceDetailsRepository.findByIaPublishInvoiceID(id);
        for (int i = 0; i < iaPublishInvoiceDetails.size(); i++) {
            iaPublishInvoiceDetails.get(i).setInvoiceTypeName(iaPublishInvoiceDetails.get(i).getInvoiceType().getInvoiceTypeName());
            if (iaPublishInvoiceDetails.get(i).getInvoiceForm() == 0) {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn điện tử");
            } else if (iaPublishInvoiceDetails.get(i).getInvoiceForm() == 1) {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn đặt in");
            } else {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn tự in");
            }
            iaPublishInvoiceDetails.get(i).setStartUsingToString(convertDate(iaPublishInvoiceDetails.get(i).getStartUsing()));
            iaPublishInvoiceDetails.get(i).setQuantityToString(Utils.formatTien(iaPublishInvoiceDetails.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
        }
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName());
        parameter.put("OrganizationUnitAddress", organizationUnit.get().getAddress());
        parameter.put("OrganizationUnitPhoneNumber", organizationUnit.get().getPhoneNumber());
        parameter.put("OrganizationUnitTaxCode", organizationUnit.get().gettaxCode());
        parameter.put("ReceiptedTaxOffical", iaPublishInvoice.getReceiptedTaxOffical());
        parameter.put("RepresentationInLaw", iaPublishInvoice.getRepresentationInLaw());
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("REPORT_MAX_COUNT", iaPublishInvoiceDetails.size());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details

        return ReportUtils.generateReportPDF(iaPublishInvoiceDetails, parameter, jasperReport);
    }

    private byte[] getThongBaoPhatHanhHDDT_DI_TI(UUID id) throws JRException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        String reportName = "Thong_bao_phat_hanh_hddi_hdti";
        IaPublishInvoice iaPublishInvoice = iaPublishInvoiceRepository.findById(id).get();
        List<IaPublishInvoiceDetails> iaPublishInvoiceDetails = iaPublishInvoiceDetailsRepository.findByIaPublishInvoiceID(id);
        for (int i = 0; i < iaPublishInvoiceDetails.size(); i++) {
            iaPublishInvoiceDetails.get(i).setInvoiceTypeName(iaPublishInvoiceDetails.get(i).getInvoiceType().getInvoiceTypeName());
            if (iaPublishInvoiceDetails.get(i).getInvoiceForm() == 0) {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn điện tử");
            } else if (iaPublishInvoiceDetails.get(i).getInvoiceForm() == 1) {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn đặt in");
            } else {
                iaPublishInvoiceDetails.get(i).setInvoiceFormName("Hóa đơn tự in");
            }
            iaPublishInvoiceDetails.get(i).setStartUsingToString(convertDate(iaPublishInvoiceDetails.get(i).getStartUsing()));
            iaPublishInvoiceDetails.get(i).setContractDateToString(convertDate(iaPublishInvoiceDetails.get(i).getContractDate()));
            iaPublishInvoiceDetails.get(i).setQuantityToString(Utils.formatTien(iaPublishInvoiceDetails.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
        }
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName());
        parameter.put("OrganizationUnitAddress", organizationUnit.get().getAddress());
        parameter.put("OrganizationUnitPhoneNumber", organizationUnit.get().getPhoneNumber());
        parameter.put("OrganizationUnitTaxCode", organizationUnit.get().gettaxCode());
        parameter.put("ReceiptedTaxOffical", iaPublishInvoice.getReceiptedTaxOffical());
        parameter.put("RepresentationInLaw", iaPublishInvoice.getRepresentationInLaw());
        parameter.put("GoverningUnitName", organizationUnitOptionReport.getGoverningUnitName());
        parameter.put("GoverningUnitTaxCode", organizationUnitOptionReport.getGoverningUnitTaxCode());
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("REPORT_MAX_COUNT", iaPublishInvoiceDetails.size());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details

        return ReportUtils.generateReportPDF(iaPublishInvoiceDetails, parameter, jasperReport);
    }

    private byte[] getBaoGiaMauDayDu(UUID id) throws JRException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String reportName = "BaoGia_MauDayDu";
        UserDTO userDTO = userService.getAccount();
        SAQuote saQuote = saQuoteRepository.findById(id).get();
        List<SAQuote> saQuoteList = new ArrayList<>();
        saQuoteList.add(saQuote);
        List<SAQuoteReportDTO> saQuoteReportDTOS = saQuoteRepository.findSAQuoteDetailsReport(saQuote.getId());
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String format_NguyenTe;
        String format_DonGia;
        if (saQuote.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
            format_DonGia = Constants.SystemOption.DDSo_DonGia;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
            format_DonGia = Constants.SystemOption.DDSo_DonGiaNT;
        }
        Boolean isShowTax = true;
        Boolean isShowDiscount = true;
        BigDecimal taxValue = BigDecimal.ZERO;
        BigDecimal discountValue = BigDecimal.ZERO;
        if (saQuoteReportDTOS.get(0).getvATRate() != null) {
            taxValue = saQuoteReportDTOS.get(0).getvATRate();
        }
        if (saQuoteReportDTOS.get(0).getDiscountRate() != null) {
            discountValue = saQuoteReportDTOS.get(0).getDiscountRate();
        }
        for (int i = 0; i < saQuoteReportDTOS.size(); i++) {
            saQuoteReportDTOS.get(i).setQuantityString((Utils.formatTien(saQuoteReportDTOS.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO)));
            saQuoteReportDTOS.get(i).setUnitPriceOriginalString((Utils.formatTien(saQuoteReportDTOS.get(i).getUnitPriceOriginal(), format_DonGia, userDTO)));
            saQuoteReportDTOS.get(i).setAmountOriginalString((Utils.formatTien(saQuoteReportDTOS.get(i).getAmountOriginal(), format_NguyenTe, userDTO)));
            if (saQuoteReportDTOS.get(i).getvATRate() == null || taxValue.floatValue() != saQuoteReportDTOS.get(i).getvATRate().floatValue()) {
                isShowTax = false;
            }
            if (saQuoteReportDTOS.get(i).getDiscountRate() == null || discountValue.floatValue() != saQuoteReportDTOS.get(i).getDiscountRate().floatValue()) {
                isShowDiscount = false;
            }
        }
        Map<String, Object> parameter = new HashMap<>();
//        boolean isNoMBook = false;
//        boolean isSDSoQuanTri = false;
//        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
//        if (userWithAuthoritiesAndSystemOption.isPresent()) {
//            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
//                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
//            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
//                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
//            isNoMBook = !collect1.isEmpty() && collect1.get(0).getCode().equalsIgnoreCase("1");
//            isSDSoQuanTri = collect1.isEmpty() && collect1.get(0).getCode().equalsIgnoreCase("1");
//        }
        parameter.put("AccountingObjectName", saQuote.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", saQuote.getAccountingObjectAddress());
        parameter.put("Reason", saQuote.getReason());
        parameter.put("No", saQuote.getNo());
        parameter.put("Date", convertDate(saQuote.getDate()));
        parameter.put("DeliveryTime", saQuote.getDeliveryTime());
        parameter.put("GuaranteeDuration", saQuote.getGuaranteeDuration());
        if (saQuote.getPaymentClause() != null) {
            parameter.put("PaymentClause", saQuote.getPaymentClause().getPaymentClauseName());
        }
        parameter.put("FinalDate", convertDate(saQuote.getFinalDate()));
        parameter.put("TotalAmount", Utils.formatTien((saQuote.getTotalAmount().subtract(saQuote.getTotalDiscountAmount()).add(saQuote.getTotalVATAmount())), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("CurrencyID", saQuote.getCurrencyID());
        parameter.put("ContactName", saQuote.getContactName());
        parameter.put("ContactMobile", saQuote.getContactMobile());
        parameter.put("ContactEmail", saQuote.getContactEmail());
        parameter.put("DiscountAmountOriginal", Utils.formatTien(saQuote.getTotalDiscountAmountOriginal(), format_NguyenTe, userDTO));
        parameter.put("TotalDiscountAmountOriginal", Utils.formatTien(saQuote.getTotalAmountOriginal().subtract(saQuote.getTotalDiscountAmountOriginal()), format_NguyenTe, userDTO));
        parameter.put("VATAmountOriginal", Utils.formatTien(saQuote.getTotalVATAmountOriginal(), format_NguyenTe, userDTO));
        //Boolean isShow = true;
//        if (saQuoteReportDTOS.size() > 1) {
//            isShow = false;
//        } else {
//            isShow = true;
//        }
        parameter.put("DiscountRate", saQuoteReportDTOS.get(0).getDiscountRate() != null ? Utils.formatTien(saQuoteReportDTOS.get(0).getDiscountRate(), Constants.SystemOption.DDSo_TyLe, userDTO) + "%" : "");
        String vATRate = "";
        if (saQuoteReportDTOS.get(0).getvATRate() != null) {
            if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 0) {
                vATRate = "0%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 1) {
                vATRate = "5%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 2) {
                vATRate = "10%";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 3) {
                vATRate = "KCT";
            } else if (saQuoteReportDTOS.get(0).getvATRate().intValue() == 4) {
                vATRate = "KTT";
            }
        }
        parameter.put("REPORT_MAX_COUNT", saQuoteReportDTOS.size());
        parameter.put("VATRate", vATRate);
        //parameter.put("isShow", isShow);
        parameter.put("isShowTax", isShowTax);
        parameter.put("isShowDiscount", isShowDiscount);
        parameter.put("ExchangeRate", Utils.formatTien(saQuote.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TotalAmountOriginal", Utils.formatTien(((saQuote.getTotalAmountOriginal().subtract(saQuote.getTotalDiscountAmountOriginal())).add(saQuote.getTotalVATAmountOriginal())), format_NguyenTe, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords((saQuote.getTotalAmountOriginal().subtract(saQuote.getTotalDiscountAmountOriginal())).add(saQuote.getTotalVATAmountOriginal()), saQuote.getCurrencyID() == null ? "VND" : saQuote.getCurrencyID(), userDTO));
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        if (saQuote.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("isShowCurrency", false);
        } else {
            parameter.put("isShowCurrency", true);
        }
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("Director", organizationUnitOptionReport.getDirector());

        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // Hàm báo cáo này sẽ in ra 2 cái key, và 1 danh sách có trong list details
        return ReportUtils.generateReportPDF(saQuoteReportDTOS, parameter, jasperReport);
    }


    /**
     * @param id
     * @param typeReport
     * @return
     * @throws JRException
     * @Author Hautv
     */
    private byte[] getReportMCReceipt(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        MCReceipt mcReceipt = mcReceiptRepository.findById(id).get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                result = getChungTuKeToan(mcReceipt, typeReport);
                break;
            case Constants.Report.BieuMau:
                result = getBieuMauPhieuThu(mcReceipt, typeReport);
                break;
            case Constants.Report.PhieuThuTT2Lien:
                result = getBieuMauPhieuThu(mcReceipt, typeReport);
                break;
            case Constants.Report.PhieuThuA5:
                result = getBieuMauPhieuThu(mcReceipt, typeReport);
                break;
        }
        return result;
    }

    /**
     * @param id
     * @param typeReport
     * @return
     * @throws JRException
     * @Author Hautv
     */
    private byte[] getReportMCPayment(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        MCPayment mcPayment = mcPaymentRepository.findById(id).get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                result = getChungTuKeToan(mcPayment, typeReport);
                break;
            case Constants.Report.BieuMau:
                result = getBieuMauPhieuChi(mcPayment, typeReport);
                break;
            case Constants.Report.PhieuChiTT2Lien:
                result = getBieuMauPhieuChi(mcPayment, typeReport);
                break;
            case Constants.Report.PhieuChiA5:
                result = getBieuMauPhieuChi(mcPayment, typeReport);
                break;
        }
        return result;
    }

    private byte[] getBieuMauPhieuThu(MCReceipt mcReceipt, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        switch (typeReport) {
            case Constants.Report.PhieuThuTT2Lien:
                reportName = "MCReceipt-2-Lien";
                break;
            case Constants.Report.PhieuThuA5:
                reportName = "MCReceipt-A5";
                break;
            default:
                reportName = "MCReceipt";
                break;
        }

        List<MCReceiptDetails> lstDetails = mcReceipt.getMCReceiptDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(MCReceiptDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectName()) || !StringUtils.isEmpty(mcReceipt.getPayers())) {
            parameter.put("NguoiNop", getName(mcReceipt.getAccountingObjectName(), mcReceipt.getPayers()));
        }
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", mcReceipt.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(mcReceipt.getReason())) {
            parameter.put("LyDoNop", mcReceipt.getReason());
        }
        if (!StringUtils.isEmpty(mcReceipt.getNumberAttach())) {
            parameter.put("KemTheo", mcReceipt.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(mcReceipt.getTotalAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(mcReceipt.getTotalAmountOriginal(), mcReceipt.getCurrencyID() == null ? "VND" : mcReceipt.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", mcReceipt.getExchangeRate() == null ? "1" : Utils.formatTien(mcReceipt.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(mcReceipt.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mcReceipt.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcReceipt.getNoFBook());
        } else {
            parameter.put("So", mcReceipt.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNopTien", mcReceipt.getPayers());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (MCReceiptDetails mcReceiptDetails : lstDetails) {
            if (!lstCo.contains(mcReceiptDetails.getCreditAccount())) {
                lstCo.add(mcReceiptDetails.getCreditAccount());
            }
            if (!lstNo.contains(mcReceiptDetails.getDebitAccount())) {
                lstNo.add(mcReceiptDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", mcReceipt.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcReceipt.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + mcReceipt.getDate().getDayOfMonth() + " tháng " + mcReceipt.getDate().getMonthValue() + " năm " + mcReceipt.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getBieuMauPhieuChi(MCPayment mcPayment, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "mCPayment";
        switch (typeReport) {
            case Constants.Report.PhieuChiTT2Lien:
                reportName = "mCPayment-2-Lien";
                break;
            case Constants.Report.PhieuChiA5:
                reportName = "mCPayment-A5";
                break;
            default:
                reportName = "mCPayment";
                break;
        }
        List<MCPaymentDetails> lstDetails = mcPayment.getMCPaymentDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(MCPaymentDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(mcPayment.getAccountingObjectName())) {
            parameter.put("NguoiNhan", getName(mcPayment.getAccountingObjectName(), mcPayment.getReceiver()));
        }
        if (!StringUtils.isEmpty(mcPayment.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", mcPayment.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(mcPayment.getReason())) {
            parameter.put("LyDoNop", mcPayment.getReason());
        }
        if (!StringUtils.isEmpty(mcPayment.getNumberAttach())) {
            parameter.put("KemTheo", mcPayment.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(mcPayment.getTotalAmountOriginal(), getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(mcPayment.getTotalAmountOriginal(), mcPayment.getCurrencyID() == null ? "VND" : mcPayment.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", mcPayment.getExchangeRate() == null ? "1" : Utils.formatTien(mcPayment.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(mcPayment.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcPayment.getNoFBook());
        } else {
            parameter.put("So", mcPayment.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNhanTien", mcPayment.getReceiver());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (MCPaymentDetails mcPaymentDetails : lstDetails) {
            if (!lstCo.contains(mcPaymentDetails.getCreditAccount())) {
                lstCo.add(mcPaymentDetails.getCreditAccount());
            }
            if (!lstNo.contains(mcPaymentDetails.getDebitAccount())) {
                lstNo.add(mcPaymentDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", mcPayment.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcPayment.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + mcPayment.getDate().getDayOfMonth() + " tháng " + mcPayment.getDate().getMonthValue() + " năm " + mcPayment.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getBieuMauPhieuChi(PPService ppService, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "mCPayment";
        switch (typeReport) {
            case Constants.Report.PhieuChiTT2Lien:
                reportName = "mCPayment-2-Lien";
                break;
            case Constants.Report.PhieuChiA5:
                reportName = "mCPayment-A5";
                break;
            default:
                reportName = "mCPayment";
                break;
        }
        List<PPServiceDetail> lstDetails = ppService.getPpServiceDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(PPServiceDetail::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        MCPayment mcPayment = mcPaymentRepository.findById(ppService.getPaymentVoucherID()).get();
        if (!StringUtils.isEmpty(ppService.getAccountingObjectName())) {

            parameter.put("NguoiNhan", getName(mcPayment.getAccountingObjectName(), mcPayment.getReceiver()));
        }
        if (!StringUtils.isEmpty(mcPayment.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", mcPayment.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(mcPayment.getReason())) {
            parameter.put("LyDoNop", mcPayment.getReason());
        }
        if (!StringUtils.isEmpty(mcPayment.getNumberAttach())) {
            parameter.put("KemTheo", mcPayment.getNumberAttach());
        }
        BigDecimal resultAmountOriginal = ppService.getTotalAmountOriginal()
            .subtract(ppService.getTotalDiscountAmountOriginal())
            .add(ppService.getTotalVATAmountOriginal());
        parameter.put("SoTien", Utils.formatTien(resultAmountOriginal, getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(resultAmountOriginal, ppService.getCurrencyID() == null ? "VND" : ppService.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", ppService.getExchangeRate() == null ? "1" : Utils.formatTien(ppService.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(ppService.getTotalAmount().subtract(ppService.getTotalDiscountAmount()).add(ppService.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcPayment.getNoFBook());
        } else {
            parameter.put("So", mcPayment.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNhanTien", mcPayment.getReceiver());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (PPServiceDetail ppServiceDetail : lstDetails) {
            //VAT
            if (ppServiceDetail.getDeductionDebitAccount() != null && !lstCo.contains(ppServiceDetail.getDeductionDebitAccount())) {
                lstCo.add(ppServiceDetail.getDeductionDebitAccount());
            }
            if (ppServiceDetail.getVatAccount() != null && !lstNo.contains(ppServiceDetail.getVatAccount())) {
                lstNo.add(ppServiceDetail.getVatAccount());
            }

            if (ppServiceDetail.getCreditAccount() != null && !lstCo.contains(ppServiceDetail.getCreditAccount())) {
                lstCo.add(ppServiceDetail.getCreditAccount());
            }
            if (ppServiceDetail.getDebitAccount() != null && !lstNo.contains(ppServiceDetail.getDebitAccount())) {
                lstNo.add(ppServiceDetail.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", ppService.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppService.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + ppService.getDate().getDayOfMonth() + " tháng " + ppService.getDate().getMonthValue() + " năm " + ppService.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getReportPPService(UUID id, int typeReport) throws JRException, InvocationTargetException {
        byte[] result = null;
        Optional<PPService> ppServiceOptional = ppServiceRepository.findById(id);
        if (!ppServiceOptional.isPresent()) {
            throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
        }
        PPService ppService = ppServiceOptional.get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                result = getChungTuKeToan(ppService, typeReport);
                break;
            case Constants.Report.PhieuChi:
                result = getPhieuChi(ppService);
                break;
            case Constants.Report.PhieuChiTT2Lien:
                result = getBieuMauPhieuChi(ppService, typeReport);
                break;
            case Constants.Report.PhieuChiA5:
                result = getBieuMauPhieuChi(ppService, typeReport);
                break;
            case Constants.Report.GiayBaoNo:
                result = getGiayBaoNo(ppService);
                break;
        }
        return result;
    }

    private byte[] getGiayBaoNo(PPService ppService) throws JRException {
        byte[] result = null;
        String reportName = "";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        MBTellerPaper mBTellerPaper = mBTellerPaperRepository.findById(ppService.getPaymentVoucherID()).get();
        UserDTO userDTO = userService.getAccount();
        BankAccountDetails bankAccountDetails = new BankAccountDetails();
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String format_NguyenTe = "";
        JasperReport jasperReport;
        if (ppService.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
        }
        if (mBTellerPaper.getBankAccountDetailID() != null) {
            bankAccountDetails = bankAccountDetailsRepository.findById(mBTellerPaper.getBankAccountDetailID()).get();
        }
        List<PPServiceDetail> details = new ArrayList<>(ppService.getPpServiceDetails());
        Collections.sort(details, Comparator.comparingInt(PPServiceDetail::getOrderPriority));
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setAmountOriginalString(Utils.formatTien(details.get(i).getAmountOriginal().subtract(details.get(i).getDiscountAmountOriginal()), format_NguyenTe, userDTO));
            details.get(i).setAmountString(Utils.formatTien(details.get(i).getAmount().subtract(details.get(i).getDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (details.get(i).getVatAmountOriginal() != null && details.get(i).getVatAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                PPServiceDetail ppServiceDetail = new PPServiceDetail();
                ppServiceDetail.setDescription("Thuế GTGT " + materialGoodsRepository.findMaterialGoodsCodeById(details.get(i).getMaterialGoodsID()));
                ppServiceDetail.setAmountOriginalString(Utils.formatTien(details.get(i).getVatAmountOriginal(), format_NguyenTe, userDTO));
                ppServiceDetail.setAmountString(Utils.formatTien(details.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                ppServiceDetail.setDebitAccount(details.get(i).getVatAccount());
                ppServiceDetail.setCreditAccount(details.get(i).getDeductionDebitAccount());
                details.add(i + 1, ppServiceDetail);
                i++;
            }
        }
        Map<String, Object> parameter = new HashMap<>();
        setHeader(userDTO, parameter);
        reportName = "GiayBaoNo";
        checkEbPackage(userDTO, parameter);
        // khai báo key
        if (ppService.getContactName() != null && !ppService.getContactName().isEmpty()
            && ppService.getAccountingObjectName() != null && !ppService.getAccountingObjectName().isEmpty()) {
            parameter.put("AccountingObjectName", ppService.getContactName() + " - " + ppService.getAccountingObjectName());
        } else if (ppService.getContactName() != null  && !ppService.getContactName().isEmpty()) {
            parameter.put("AccountingObjectName", ppService.getContactName());
        } else if (ppService.getAccountingObjectName() != null && !ppService.getAccountingObjectName().isEmpty()) {
            parameter.put("AccountingObjectName", ppService.getAccountingObjectName());
        }
//        parameter.put("AccountingObjectName", ppService.getContactName());
//        parameter.put("AccountingObjectName2", ppService.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", ppService.getAccountingObjectAddress());
        parameter.put("CurrencyID", ppService.getCurrencyID());
        Boolean currentBook = Utils.PhienSoLamViec(userDTO).equals(1);
        if (Boolean.TRUE.equals(currentBook)) {
            parameter.put("No", mBTellerPaper.getNoMBook());
        } else {
            parameter.put("No", mBTellerPaper.getNoFBook());
        }
        parameter.put("PostedDate", convertDate(ppService.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(ppService.getTotalAmountOriginal().subtract(ppService.getTotalDiscountAmountOriginal()).add(ppService.getTotalVATAmountOriginal()), format_NguyenTe, userDTO));
        parameter.put("TotalAmount", Utils.formatTien(ppService.getTotalAmount().subtract(ppService.getTotalDiscountAmount()).add(ppService.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(ppService.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("AmountOriginal", Utils.formatTien(ppService.getTotalAmountOriginal().subtract(ppService.getTotalDiscountAmountOriginal()).add(ppService.getTotalVATAmountOriginal()), format_NguyenTe, userDTO));
        parameter.put("Amount", Utils.formatTien(ppService.getTotalAmount().subtract(ppService.getTotalDiscountAmount()).add(ppService.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("BankName", mBTellerPaper.getBankName());
        if (mBTellerPaper.getBankAccountDetailID() != null) {
            parameter.put("BankAccount", bankAccountDetails.getBankAccount());
        }
        parameter.put("Reason", ppService.getReason());
        parameter.put("ExchangeRate", Utils.formatTien(ppService.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("Manager", "Manager");
        BigDecimal amount = ppService.getTotalAmountOriginal().subtract(ppService.getTotalDiscountAmountOriginal()).add(ppService.getTotalVATAmountOriginal());

        parameter.put("Total", Utils.formatTien(amount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords(amount, ppService.getCurrencyID() == null ? "VND" : ppService.getCurrencyID(), userDTO));
        parameter.put("CurrencyInWord", "Số tiền nguyên tệ (" + ppService.getCurrencyID() + ")");
//                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mBTellerPaper.getCurrencyId();
//                parameter.put("ConversionPair", conversionPair);
        if (ppService.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("isShowCurrency", false);
        } else {
            parameter.put("isShowCurrency", true);
        }
        if (organizationUnitOptionReport.getHeaderSetting() == 0) {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().gettaxCode());
        } else {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().getAddress());
        }
        if (!ppService.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            String currencyAmountOriginal = "Số tiền nguyên tệ (" + ppService.getCurrencyID() + ")";
            String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
            parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
            parameter.put("CurrencyInWord", currencyAmount);
            parameter.put("REPORT_MAX_COUNT", details.size());
        }
        parameter.put("REPORT_MAX_COUNT", details.size());
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("Director", organizationUnitOptionReport.getDirector());
        parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
        parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuChi(PPService ppService) throws JRException {
        MCPayment mcPayment = mcPaymentRepository.findById(ppService.getPaymentVoucherID()).get();
        String reportName = "mCPayment";
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(ppService.getAccountingObjectName())) {
            parameter.put("NguoiNhan", getName(ppService.getAccountingObjectName(), mcPayment.getReceiver()));
        }
        if (!StringUtils.isEmpty(ppService.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", ppService.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(ppService.getReason())) {
            parameter.put("LyDoNop", ppService.getReason());
        }
        if (!StringUtils.isEmpty(ppService.getNumberAttach())) {
            parameter.put("KemTheo", ppService.getNumberAttach());
        }
        BigDecimal resultAmountOriginal = ppService.getTotalAmountOriginal()
            .subtract(ppService.getTotalDiscountAmountOriginal())
            .add(ppService.getTotalVATAmountOriginal());
        parameter.put("SoTien", Utils.formatTien(resultAmountOriginal, getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(resultAmountOriginal, ppService.getCurrencyID() == null ? "VND" : ppService.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", ppService.getExchangeRate() == null ? "1" : Utils.formatTien(ppService.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(ppService.getTotalAmount().subtract(ppService.getTotalDiscountAmount()).add(ppService.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcPayment.getNoFBook());
        } else {
            parameter.put("So", mcPayment.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNhanTien", mcPayment.getReceiver());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (PPServiceDetail ppServiceDetail : ppService.getPpServiceDetails().stream().sorted(Comparator.comparing(PPServiceDetail::getOrderPriority)).collect(Collectors.toList())) {
            //VAT
            if (ppServiceDetail.getDeductionDebitAccount() != null && !lstCo.contains(ppServiceDetail.getDeductionDebitAccount())) {
                lstCo.add(ppServiceDetail.getDeductionDebitAccount());
            }
            if (ppServiceDetail.getVatAccount() != null && !lstNo.contains(ppServiceDetail.getVatAccount())) {
                lstNo.add(ppServiceDetail.getVatAccount());
            }

            if (ppServiceDetail.getCreditAccount() != null && !lstCo.contains(ppServiceDetail.getCreditAccount())) {
                lstCo.add(ppServiceDetail.getCreditAccount());
            }
            if (ppServiceDetail.getDebitAccount() != null && !lstNo.contains(ppServiceDetail.getDebitAccount())) {
                lstNo.add(ppServiceDetail.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", ppService.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppService.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + ppService.getDate().getDayOfMonth() + " tháng " + ppService.getDate().getMonthValue() + " năm " + ppService.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        return ReportUtils.generateReportPDF(details, parameter, jasperReport);
    }

    /**
     * @param mcReceipt
     * @return
     * @Author Hautv
     */
    private byte[] getChungTuKeToan(MCReceipt mcReceipt, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<MCReceiptDetails> mcReceiptDetails = new ArrayList<>();
        mcReceiptDetails.addAll(mcReceipt.getMCReceiptDetails());
        Collections.sort(mcReceiptDetails, Comparator.comparingInt(MCReceiptDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
       /* boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;*/
       Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        /*Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }*/
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", mcReceipt.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", mcReceipt.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", mcReceipt.getCurrencyID());
        /*if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", mcReceipt.getNoMBook());
            } else {
                parameter.put("No", mcReceipt.getNoFBook());
            }
        } else {
            parameter.put("No", mcReceipt.getNoFBook());
        }*/
        if (Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec)) {
            parameter.put("No", mcReceipt.getNoFBook());
        } else {
            parameter.put("No", mcReceipt.getNoMBook());
        }
        parameter.put("Date", convertDate(mcReceipt.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(mcReceipt.getTotalAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(mcReceipt.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(mcReceipt.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mcReceipt.getCurrencyID();
        if (!StringUtils.isEmpty(mcReceipt.getReason())) {
            parameter.put("Reason", mcReceipt.getReason());
        }
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(mcReceipt.getTotalAmountOriginal(), mcReceipt.getCurrencyID() == null ? "VND" : mcReceipt.getCurrencyID(), userDTO));
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalQD = BigDecimal.ZERO;
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < mcReceiptDetails.size(); i++) {
                mcReceiptDetails.get(i).setAmountOriginalToString(Utils.formatTien(mcReceiptDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
                mcReceiptDetails.get(i).setAmountToString(Utils.formatTien(mcReceiptDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                if (mcReceiptDetails.get(i).getAmount() != null) {
                    totalQD = totalQD.add(mcReceiptDetails.get(i).getAmount());
                }
                if (mcReceiptDetails.get(i).getAmountOriginal() != null) {
                    total = total.add(mcReceiptDetails.get(i).getAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(total, getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalQD, Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < mcReceiptDetails.size(); i++) {
                mcReceiptDetails.get(i).setAmountOriginalToString(Utils.formatTien(mcReceiptDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
                if (mcReceiptDetails.get(i).getAmountOriginal() != null) {
                    total = total.add(mcReceiptDetails.get(i).getAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(total, getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcReceipt.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", mcReceiptDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(mcReceiptDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(PPService ppService, int typeReport) throws JRException, InvocationTargetException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<PPServiceDetail> ppServiceDetails = new ArrayList<>();
        ppServiceDetails.addAll(ppService.getPpServiceDetails());
        Collections.sort(ppServiceDetails, Comparator.comparingInt(PPServiceDetail::getOrderPriority));
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(ppService.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", ppService.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(ppService.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", ppService.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", ppService.getCurrencyID());
        parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppService.getNoMBook() : ppService.getNoFBook());
        parameter.put("Date", convertDate(ppService.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(ppService.getTotalAmountOriginal().subtract(ppService.getTotalDiscountAmountOriginal()).add(ppService.getTotalVATAmountOriginal()), getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(ppService.getTotalAmount().subtract(ppService.getTotalDiscountAmount()).add(ppService.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(ppService.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppService.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (!StringUtils.isEmpty(ppService.getReason())) {
            parameter.put("Reason", ppService.getReason());
        }
        List<PPServiceDetail> ppServiceDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < ppServiceDetails.size(); i++) {
                ppServiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(ppServiceDetails.get(i).getAmountOriginal().subtract(ppServiceDetails.get(i).getDiscountAmountOriginal()), getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
                ppServiceDetails.get(i).setAmountToString(Utils.formatTien(ppServiceDetails.get(i).getAmount().subtract(ppServiceDetails.get(i).getDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
                ppServiceDetailsList.add(ppServiceDetails.get(i));
                totalAmountOriginal = totalAmountOriginal.add(ppServiceDetails.get(i).getAmountOriginal().subtract(ppServiceDetails.get(i).getDiscountAmountOriginal()));
                totalAmount = totalAmount.add(ppServiceDetails.get(i).getAmount().subtract(ppServiceDetails.get(i).getDiscountAmount()));

                if (ppServiceDetails.get(i).getVatAmountOriginal().floatValue() > 0) {
                    PPServiceDetail ppServiceDetailVAT = new PPServiceDetail();
                    try {
                        BeanUtils.copyProperties(ppServiceDetailVAT, ppServiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ppServiceDetailVAT.setAmountOriginalToString(Utils.formatTien(ppServiceDetails.get(i).getVatAmountOriginal(), getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
                    ppServiceDetailVAT.setAmountToString(Utils.formatTien(ppServiceDetails.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    ppServiceDetailVAT.setDescription(ppServiceDetailVAT.getVatDescription());
                    ppServiceDetailVAT.setCreditAccount(ppServiceDetailVAT.getDeductionDebitAccount());
                    ppServiceDetailVAT.setDebitAccount(ppServiceDetailVAT.getVatAccount());
                    ppServiceDetailsList.add(ppServiceDetailVAT);
                    totalAmountOriginal = totalAmountOriginal.add(ppServiceDetails.get(i).getVatAmountOriginal());
                    totalAmount = totalAmount.add(ppServiceDetails.get(i).getVatAmount());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, ppService.getCurrencyID() == null ? "VND" : ppService.getCurrencyID(), userDTO));
        } else {
            for (int i = 0; i < ppServiceDetails.size(); i++) {
                ppServiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(ppServiceDetails.get(i).getAmountOriginal().subtract(ppServiceDetails.get(i).getDiscountAmountOriginal()), getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
                ppServiceDetailsList.add(ppServiceDetails.get(i));
                totalAmount = totalAmount.add(ppServiceDetails.get(i).getAmountOriginal().subtract(ppServiceDetails.get(i).getDiscountAmountOriginal()));

                if (ppServiceDetails.get(i).getVatAmountOriginal().floatValue() > 0) {
                    PPServiceDetail ppServiceDetailsVAT = new PPServiceDetail();
                    try {
                        BeanUtils.copyProperties(ppServiceDetailsVAT, ppServiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ppServiceDetailsVAT.setAmountOriginalToString(Utils.formatTien(ppServiceDetails.get(i).getVatAmountOriginal(), getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
                    ppServiceDetailsVAT.setDescription(ppServiceDetailsVAT.getVatDescription());
                    ppServiceDetailsVAT.setCreditAccount(ppServiceDetailsVAT.getDeductionDebitAccount());
                    ppServiceDetailsVAT.setDebitAccount(ppServiceDetailsVAT.getVatAccount());
                    ppServiceDetailsList.add(ppServiceDetailsVAT);
                    totalAmount = totalAmount.add(ppServiceDetails.get(i).getVatAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmount, getTypeAmount(userDTO, ppService.getCurrencyID()), userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmount, ppService.getCurrencyID() == null ? "VND" : ppService.getCurrencyID(), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppService.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", ppServiceDetailsList.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(ppServiceDetailsList, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(MBTellerPaper mbTellerPaper, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<MBTellerPaperDetails> mbTellerPaperDetails = new ArrayList<>(mbTellerPaper.getmBTellerPaperDetails());
        Collections.sort(mbTellerPaperDetails, Comparator.comparingInt(MBTellerPaperDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (!StringUtils.isEmpty(mbTellerPaper.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", mbTellerPaper.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(mbTellerPaper.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", mbTellerPaper.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", mbTellerPaper.getCurrencyId());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", mbTellerPaper.getNoMBook());
            } else {
                parameter.put("No", mbTellerPaper.getNoFBook());
            }
        } else {
            parameter.put("No", mbTellerPaper.getNoFBook());
        }
        parameter.put("Date", convertDate(mbTellerPaper.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(mbTellerPaper.getTotalAmountOriginal(), getTypeAmount(userDTO, mbTellerPaper.getCurrencyId()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(mbTellerPaper.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(mbTellerPaper.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mbTellerPaper.getCurrencyId();
        parameter.put("Reason", mbTellerPaper.getReason());
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(mbTellerPaper.getTotalAmountOriginal(), mbTellerPaper.getCurrencyId() == null ? "VND" : mbTellerPaper.getCurrencyId(), userDTO));
        BigDecimal totalOriginal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        checkEbPackage(userDTO, parameter);
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < mbTellerPaperDetails.size(); i++) {
                mbTellerPaperDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbTellerPaperDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbTellerPaper.getCurrencyId()), userDTO));
                mbTellerPaperDetails.get(i).setAmountToString(Utils.formatTien(mbTellerPaperDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                totalOriginal = totalOriginal.add(mbTellerPaperDetails.get(i).getAmountOriginal());
                total = total.add(mbTellerPaperDetails.get(i).getAmount());
            }
            parameter.put("Total", Utils.formatTien(totalOriginal, getTypeAmount(userDTO, mbTellerPaper.getCurrencyId()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(total, Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < mbTellerPaperDetails.size(); i++) {
                mbTellerPaperDetails.get(i).setAmountOriginalToString(Utils.formatTien(mbTellerPaperDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mbTellerPaper.getCurrencyId()), userDTO));
                totalOriginal = totalOriginal.add(mbTellerPaperDetails.get(i).getAmountOriginal());
            }
            parameter.put("Total", Utils.formatTien(totalOriginal, getTypeAmount(userDTO, mbTellerPaper.getCurrencyId()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mbTellerPaper.getCurrencyId()));
        parameter.put("REPORT_MAX_COUNT", mbTellerPaperDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(mbTellerPaperDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(RSInwardOutward rsInwardOutward, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<RSInwardOutWardDetails> rsInwardOutWardDetails = new ArrayList<>(rsInwardOutward.getRsInwardOutwardDetails());
        Collections.sort(rsInwardOutWardDetails, Comparator.comparingInt(RSInwardOutWardDetails::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        checkEbPackage(userDTO, parameter);
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (!StringUtils.isEmpty(rsInwardOutward.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", rsInwardOutward.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(rsInwardOutward.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", rsInwardOutward.getCurrencyID());
        parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
        parameter.put("Date", convertDate(rsInwardOutward.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(rsInwardOutward.getTotalAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(rsInwardOutward.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        if (!StringUtils.isEmpty(rsInwardOutward.getReason())) {
            parameter.put("Reason", rsInwardOutward.getReason());
        }
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + rsInwardOutward.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(rsInwardOutward.getTotalAmountOriginal(), rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < rsInwardOutWardDetails.size(); i++) {
                rsInwardOutWardDetails.get(i).setAmountOriginalToString(Utils.formatTien(rsInwardOutWardDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
                rsInwardOutWardDetails.get(i).setAmountToString(Utils.formatTien(rsInwardOutWardDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            parameter.put("Total", Utils.formatTien(rsInwardOutward.getTotalAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < rsInwardOutWardDetails.size(); i++) {
                rsInwardOutWardDetails.get(i).setAmountOriginalToString(Utils.formatTien(rsInwardOutWardDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
            }
            parameter.put("Total", Utils.formatTien(rsInwardOutward.getTotalAmountOriginal(), getTypeAmount(userDTO, rsInwardOutward.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(rsInwardOutward.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", rsInwardOutWardDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(rsInwardOutWardDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(RSTransfer rsTransfer, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        String reportName = "ChungTuKeToan";
        List<RSTransferDetail> rsTransferDetails = new ArrayList<>(rsTransfer.getRsTransferDetails());
        Collections.sort(rsTransferDetails, Comparator.comparingInt(RSTransferDetail::getOrderPriority));
        Map<String, Object> parameter = new HashMap<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        BigDecimal total = BigDecimal.ZERO;
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        checkEbPackage(userDTO, parameter);
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        if (!StringUtils.isEmpty(rsTransfer.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", rsTransfer.getAccountingObjectName());
        }
//        if (!StringUtils.isEmpty(rsTransfer.getAccountingObjectAddress())) {
//            parameter.put("AccountingObjectAddress", rsTransfer.getAccountingObjectAddress());
//        }
        parameter.put("CurrencyID", organizationUnit.get().getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", rsTransfer.getNoMBook());
            } else {
                parameter.put("No", rsTransfer.getNoFBook());
            }
        } else {
            parameter.put("No", rsTransfer.getNoFBook());
        }
        parameter.put("Date", convertDate(rsTransfer.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(rsTransfer.getTotalAmountOriginal(), getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(rsTransfer.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//        parameter.put("ExchangeRate", Utils.formatTien(rsTransfer.getExchangeRate(), Constants.SystemOption.DDSo_TyGia));
        if (!StringUtils.isEmpty(rsTransfer.getReason())) {
            parameter.put("Reason", rsTransfer.getReason());
        }
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + organizationUnit.get().getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        BigDecimal totalOWAmount = BigDecimal.ZERO;
        List<RSTransferDetail> rsTransferDetailss = new ArrayList<>();
        for (int i = 0; i < rsTransferDetails.size(); i++) {
            rsTransferDetails.get(i).setAmountOriginalToString(Utils.formatTien(rsTransferDetails.get(i).getoWAmount(), getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
            rsTransferDetailss.add(rsTransferDetails.get(i));
            totalOWAmount = totalOWAmount.add(rsTransferDetails.get(i).getoWAmount());
        }
        parameter.put("Total", Utils.formatTien(totalOWAmount, getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
        if (totalOWAmount != null) {
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalOWAmount, organizationUnit.get().getCurrencyID(), userDTO));
        }
        parameter.put("REPORT_MAX_COUNT", rsTransferDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(rsTransferDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getReportMCAudit(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        MCAudit mcAudit = mcAuditRepository.findById(id).get();
        String reportName = "mCAudit";
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        parameter.put("So", "Số: " + mcAudit.getNo());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiChiuTrachNhiem", userDTO.getFullName());
            } else {
                parameter.put("NguoiChiuTrachNhiem", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }

        String differAmountString = Utils.formatTien(mcAudit.getDifferAmount(), Constants.SystemOption.DDSo_TienVND, userDTO);
        parameter.put("differAmount", differAmountString);
        if (mcAudit.getTotalAuditAmount().compareTo(mcAudit.getTotalBalanceAmount()) > 0) {
            parameter.put("thua", differAmountString);
        } else if (mcAudit.getTotalAuditAmount().compareTo(mcAudit.getTotalBalanceAmount()) < 0) {
            parameter.put("thieu", differAmountString);
        } else {
            parameter.put("thua", differAmountString + "<br/>" + "+ Thiếu: " + differAmountString);
        }

        parameter.put("Time", "Hôm nay, vào.....giờ.... ngày " + mcAudit.getDate().getDayOfMonth() + " tháng " + mcAudit.getDate().getMonthValue() + " năm " + mcAudit.getDate().getYear());
        parameter.put("ketLuan", mcAudit.getSummary());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        List<Object> lstData = new ArrayList<>();
        parameter.put("sizeTable1", mcAudit.getMcAuditDetailMembers().size() != 0 ? mcAudit.getMcAuditDetailMembers().size() : 1);
        parameter.put("sizeTable2", mcAudit.getMcAuditDetails().size() != 0 ? mcAudit.getMcAuditDetails().size() : 1);
        List<MCAuditReportDTO> mcAuditReportDTOList = new ArrayList<>();
        MCAuditReportDTO mcAuditReportDTO1 = new MCAuditReportDTO();
        int dem = 0;
        List<MCAuditDetailMember> listMcAuditMembers = mcAudit.getMcAuditDetailMembers().stream()
            .sorted(Comparator.comparingInt(MCAuditDetailMember::getOrderPriority))
            .collect(Collectors.toList());
        for (MCAuditDetailMember mcAuditDetailMember : listMcAuditMembers) {
            MCAuditReportDTO mcAuditReportDTO = new MCAuditReportDTO();
            mcAuditReportDTO.setAccountingObjectName(mcAuditDetailMember.getAccountingObjectName());
            if (mcAuditDetailMember.getRole() != null && !mcAuditDetailMember.getRole().equals("")) {
                mcAuditReportDTO.setAccountingObjectTitle(mcAuditDetailMember.getRole());
            }
            lstData.add(mcAuditReportDTO);
            dem++;
        }
        if (lstData.size() == 0) {
            lstData.add(mcAuditReportDTO1);
            dem++;
        }
        List<MCAuditDetails> listMcAuditDetails = mcAudit.getMcAuditDetails().stream()
            .sorted(Comparator.comparingInt(MCAuditDetails::getOrderPriority))
            .collect(Collectors.toList());
        for (MCAuditDetails mcAuditDetails : listMcAuditDetails) {
            MCAuditReportDTO mcAuditReportDTO = new MCAuditReportDTO();
            mcAuditReportDTO.setQuantityString(checkFormat(mcAuditDetails.getQuantity()));
            mcAuditReportDTO.setAmountString(checkFormat(mcAuditDetails.getAmount()));
            mcAuditReportDTO.setValueOfMoneyString(checkFormat(mcAuditDetails.getValueOfMoney()));
            lstData.add(mcAuditReportDTO);
        }
        if (lstData.size() == dem) {
            lstData.add(mcAuditReportDTO1);
        }
        parameter.put("totalBalanceAmount", checkFormat(mcAudit.getTotalBalanceAmount()));
        parameter.put("totalAuditAmount", checkFormat(mcAudit.getTotalAuditAmount()));
        checkEbPackage(userDTO, parameter);
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(lstData, parameter, jasperReport);
        return result;
    }

    private String checkFormat(BigDecimal bigMoney) {
        String result = "";
        if (bigMoney.floatValue() >= 0) {
            result = new DecimalFormat("#,###").format(bigMoney).replace(',', '.');
        } else {
            bigMoney = bigMoney.multiply(BigDecimal.valueOf(-1));
            result = new DecimalFormat("(#,###)").format(bigMoney).replace(',', '.');
        }
        return result;
    }

    private String checkFormat(Integer money) {
        String result = "";
        if (money >= 0) {
            result = new DecimalFormat("#,###").format(money).replace(',', '.');
        } else {
            money *= -1;
            result = new DecimalFormat("(#,###)").format(money).replace(',', '.');
        }
        return result;
    }

    private byte[] getReportSAInvoice(UUID id, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        SAInvoice saInvoice = saInvoiceRepository.findById(id).get();
        Optional<SaBill> saBillOptional = sABillRepository.findBySaInvoiceID(saInvoice.getId());
        SaBill saBill = new SaBill();
        if (saBillOptional.isPresent()) {
            saBill = saBillOptional.get();
        }
        switch (typeReport) {
            case 1:
            case 3:
                try {
                    result = getChungTuKeToan(saInvoice, typeReport);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                result = getBaoCo(saInvoice, typeReport);
                break;
            case 0:
                RSInwardOutward rsInwardOutward = new RSInwardOutward();
                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    rsInwardOutward = rsInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID()).get();
                }

                String reportName = "xuatKhoBanHang";
                // khai báo key
                Map<String, Object> parameter = new HashMap<>();
                UserDTO userDTO = userService.getAccount();
                userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
                /*Header*/
                setHeader(userDTO, parameter);
                /*Header*/
                checkEbPackage(userDTO, parameter);
                boolean isNoMBook = false;
                boolean isSDSoQuanTri = false;
                Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
                if (userWithAuthoritiesAndSystemOption.isPresent()) {
                    List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                        .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
                    List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                        .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
                    isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
                    isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
                }

                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    if (Boolean.TRUE.equals(isSDSoQuanTri)) {
                        if (Boolean.TRUE.equals(isNoMBook)) {
                            parameter.put("So", rsInwardOutward.getNoMBook());
                        } else {
                            parameter.put("So", rsInwardOutward.getNoFBook());
                        }
                    } else {
                        parameter.put("So", rsInwardOutward.getNoFBook());
                    }
                } else {
                    if (Boolean.TRUE.equals(isSDSoQuanTri)) {
                        if (Boolean.TRUE.equals(isNoMBook)) {
                            parameter.put("So", saInvoice.getNoMBook());
                        } else {
                            parameter.put("So", saInvoice.getNoFBook());
                        }
                    } else {
                        parameter.put("So", saInvoice.getNoFBook());
                    }
                }

                if (Boolean.TRUE.equals(saInvoice.isIsDeliveryVoucher())) {
                    parameter.put("DiaChi", rsInwardOutward.getAccountingObjectAddress());
                    parameter.put("DienGiai", rsInwardOutward.getReason());
                } else {
                    parameter.put("DiaChi", saInvoice.getAccountingObjectAddress());
                    parameter.put("DienGiai", saInvoice.getReason());
                }

                parameter.put("MaSoThue", saInvoice.getCompanyTaxCode());

                List<String> lstTkNo = new ArrayList<>();
                List<String> lstTkCo = new ArrayList<>();
                for (SAInvoiceDetails saInvoiceDetails : saInvoice.getsAInvoiceDetails()) {
                    if (!lstTkNo.contains(saInvoiceDetails.getDebitAccount()))
                        lstTkNo.add(saInvoiceDetails.getDebitAccount());
                    if (!lstTkCo.contains(saInvoiceDetails.getCreditAccount()))
                        lstTkCo.add(saInvoiceDetails.getCreditAccount());
                }

                String tkNo = lstTkNo.toString();
                String tkCo = lstTkCo.toString();

                parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saInvoice.getCurrencyID()));
                parameter.put("No", tkNo.replace('[', ' ').replace(']', ' ').trim());
                parameter.put("Co", tkCo.replace('[', ' ').replace(']', ' ').trim());

                if (saInvoice.getContactName() != null && saInvoice.getAccountingObjectName() != null
                    && !saInvoice.getContactName().isEmpty() && !saInvoice.getAccountingObjectName().isEmpty()) {
                    parameter.put("TenKH", saInvoice.getContactName() + " - " + saInvoice.getAccountingObjectName());
                } else if (saInvoice.getContactName() != null && !saInvoice.getContactName().isEmpty()) {
                    parameter.put("TenKH", saInvoice.getContactName());
                } else if (saInvoice.getAccountingObjectName() != null && !saInvoice.getAccountingObjectName().isEmpty()) {
                    parameter.put("TenKH", saInvoice.getAccountingObjectName());
                } else {
                    parameter.put("TenKH", null);
                }


                parameter.put("LoaiTien", saInvoice.getCurrencyID());

                parameter.put("ExchangeRate", Utils.formatTien(saInvoice.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + saInvoice.getCurrencyID();
                parameter.put("ConversionPair", conversionPair);

                parameter.put("Time", "Ngày " + saInvoice.getDate().getDayOfMonth() + " tháng " + saInvoice.getDate().getMonthValue()
                    + " năm " + saInvoice.getDate().getYear());

                parameter.put("KemTheo", rsInwardOutward.getNumberAttach());

                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                    parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                    parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                    parameter.put("NguoiMuaHang", saInvoice.getContactName());
                }

                List<Object> lstData = new ArrayList<>();
                List<SAInvoiceDetails> saInvoiceDetailTaxs = new ArrayList<>();
                saInvoiceDetailTaxs.addAll(saInvoice.getsAInvoiceDetails());
                BigDecimal taxValue = saInvoiceDetailTaxs.get(0).getvATRate() == null ? BigDecimal.TEN : saInvoiceDetailTaxs.get(0).getvATRate();
                Boolean checkTax = true;
                List<SAInvoiceDetails> listSaInvoice = saInvoice.getsAInvoiceDetails().stream()
                    .sorted(Comparator.comparingInt(SAInvoiceDetails::getOrderPriority))
                    .collect(Collectors.toList());
                for (SAInvoiceDetails item : listSaInvoice) {
                    SAInvoiceDetails saInvoiceDetails = new SAInvoiceDetails();
                    MaterialGoods materialGoods = materialGoodsRepository.findById(item.getMaterialGoodsID()).get();
                    if (item.getUnitID() != null) {
                        Unit unit = unitRepository.findById(item.getUnitID()).get();
                        saInvoiceDetails.setUnitName(unit.getUnitName());
                    }
                    saInvoiceDetails.setMaterialGoodsName(item.getDescription());
                    saInvoiceDetails.setMaterialGoodsCode(materialGoods.getMaterialGoodsCode());
                    saInvoiceDetails.setQuanlityString(Utils.formatTien(item.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                    saInvoiceDetails.setUnitPriceString(Utils.formatTien(item.getUnitPriceOriginal(), Constants.SystemOption.DDSo_DonGia, userDTO));
                    saInvoiceDetails.setAmountString(Utils.formatTien(item.getAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                    lstData.add(saInvoiceDetails);
                    if (item.getvATRate() == null || taxValue.floatValue() != item.getvATRate().floatValue()) {
                        checkTax = false;
                    }
                }
                parameter.put("sizeTable", lstData.size());
                parameter.put("CongTien", Utils.formatTien(saInvoice.getTotalAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                parameter.put("TienThueGTGT", Utils.formatTien(saInvoice.getTotalVATAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));

                if (checkTax) {
                    if (taxValue.floatValue() == Constants.Vat.ZERO_PERCENT_VAL) {
                        parameter.put("ThueSuat", Constants.Vat.ZERO_PERCENT_NAME);
                    } else if (taxValue.floatValue() == Constants.Vat.FIVE_PERCENT_VAL) {
                        parameter.put("ThueSuat", Constants.Vat.FIVE_PERCENT_NAME);
                    } else if (taxValue.floatValue() == Constants.Vat.TEN_PERCENT_VAL) {
                        parameter.put("ThueSuat", Constants.Vat.TEN_PERCENT_NAME);
                    } else if (taxValue.floatValue() == Constants.Vat.KCT_VAL) {
                        parameter.put("ThueSuat", "/");
                    } else if (taxValue.floatValue() == Constants.Vat.KTT_VAL) {
                        parameter.put("ThueSuat", "/");
                    }
                } else {
                    parameter.put("ThueSuat", "/");
                }

                BigDecimal tongTienGoc = saInvoice.getTotalAmountOriginal().add(saInvoice.getTotalVATAmountOriginal()).subtract(saInvoice.getTotalDiscountAmountOriginal());
                parameter.put("TotalAmountOriginal", Utils.formatTien(tongTienGoc, getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));

                parameter.put("AmountInWord", Utils.GetAmountInWords(tongTienGoc, saInvoice.getCurrencyID() == null ? "VND" : saInvoice.getCurrencyID(), userDTO));
                parameter.put("TienChietKhau", Utils.formatTien(saInvoice.getTotalDiscountAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));

                BigDecimal QuyDoi = saInvoice.getTotalAmount().add(saInvoice.getTotalVATAmount()).subtract(saInvoice.getTotalDiscountAmount());
                parameter.put("TotalAmount", Utils.formatTien(QuyDoi, Constants.SystemOption.DDSo_TienVND, userDTO));
                checkEbPackage(userDTO, parameter);
                // tạo file báo cáo
                JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                // fill dữ liệu vào báo cáo, tọa    file pdf và trả về mảng byte
                result = ReportUtils.generateReportPDF(lstData, parameter, jasperReport);
                break;
            case 10:
            case 11:
                result = getVatReport(saBill, typeReport, saInvoice.getTypeID());
                break;
            case 12:
                result = getPhieuXuatKho(saInvoice, typeReport);
                break;
            case 13:
                result = getPhieuThu(saInvoice, typeReport);
                break;
            case 14:
                result = getPhieuThuA5(saInvoice, typeReport);
                break;
            case 15:
                result = getGoodAndServReport(saBill, typeReport);
                break;
            case 16:
                result = getPhieuXuatKhoA5(saInvoice, typeReport);
        }
        return result;
    }

    private byte[] getPhieuThuA5(SAInvoice saInvoice, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "MCReceipt-A5";
        MCReceipt mcReceipt = mcReceiptRepository.findById(saInvoice.getMcReceiptID()).get();
        List<SAInvoiceDetails> lstDetails = saInvoice.getsAInvoiceDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(SAInvoiceDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        checkEbPackage(userDTO, parameter);
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectName()) || !StringUtils.isEmpty(mcReceipt.getPayers())) {
            parameter.put("NguoiNop", getName(mcReceipt.getAccountingObjectName(), mcReceipt.getPayers()));
        }
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", mcReceipt.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(mcReceipt.getReason())) {
            parameter.put("LyDoNop", mcReceipt.getReason());
        }
        if (!StringUtils.isEmpty(mcReceipt.getNumberAttach())) {
            parameter.put("KemTheo", mcReceipt.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(mcReceipt.getTotalAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(mcReceipt.getTotalAmountOriginal(), mcReceipt.getCurrencyID() == null ? "VND" : saInvoice.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", mcReceipt.getExchangeRate() == null ? "1" : Utils.formatTien(mcReceipt.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(mcReceipt.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mcReceipt.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcReceipt.getNoFBook());
        } else {
            parameter.put("So", mcReceipt.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNopTien", mcReceipt.getPayers());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (SAInvoiceDetails sAInvoiceDetails : lstDetails) {
            if (!lstCo.contains(sAInvoiceDetails.getCreditAccount())) {
                lstCo.add(sAInvoiceDetails.getCreditAccount());
            }
            if (!lstNo.contains(sAInvoiceDetails.getDebitAccount())) {
                lstNo.add(sAInvoiceDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", mcReceipt.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcReceipt.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + mcReceipt.getDate().getDayOfMonth() + " tháng " + mcReceipt.getDate().getMonthValue() + " năm " + mcReceipt.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuThu(SAInvoice saInvoice, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        switch (typeReport) {
            case Constants.Report.PhieuThuTT2Lien:
                reportName = "MCReceipt-2-Lien";
                break;
            case Constants.Report.PhieuThuA5:
                reportName = "MCReceipt-A5";
                break;
            default:
                reportName = "MCReceipt";
                break;
        }
        MCReceipt mcReceipt = mcReceiptRepository.findById(saInvoice.getMcReceiptID()).get();
        List<SAInvoiceDetails> lstDetails = saInvoice.getsAInvoiceDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(SAInvoiceDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        checkEbPackage(userDTO, parameter);
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectName()) || !StringUtils.isEmpty(mcReceipt.getPayers())) {
            parameter.put("NguoiNop", getName(mcReceipt.getAccountingObjectName(), mcReceipt.getPayers()));
        }
        if (!StringUtils.isEmpty(mcReceipt.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", mcReceipt.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(mcReceipt.getReason())) {
            parameter.put("LyDoNop", mcReceipt.getReason());
        }
        if (!StringUtils.isEmpty(mcReceipt.getNumberAttach())) {
            parameter.put("KemTheo", mcReceipt.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(mcReceipt.getTotalAmountOriginal(), getTypeAmount(userDTO, mcReceipt.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(mcReceipt.getTotalAmountOriginal(), mcReceipt.getCurrencyID() == null ? "VND" : saInvoice.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", mcReceipt.getExchangeRate() == null ? "1" : Utils.formatTien(mcReceipt.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(mcReceipt.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mcReceipt.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", mcReceipt.getNoFBook());
        } else {
            parameter.put("So", mcReceipt.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNopTien", mcReceipt.getPayers());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (SAInvoiceDetails sAInvoiceDetails : lstDetails) {
            if (!lstCo.contains(sAInvoiceDetails.getCreditAccount())) {
                lstCo.add(sAInvoiceDetails.getCreditAccount());
            }
            if (!lstNo.contains(sAInvoiceDetails.getDebitAccount())) {
                lstNo.add(sAInvoiceDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", mcReceipt.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcReceipt.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + mcReceipt.getDate().getDayOfMonth() + " tháng " + mcReceipt.getDate().getMonthValue() + " năm " + mcReceipt.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuXuatKhoA5(SAInvoice saInvoice, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_xuat_kho_ban_hang_A5";
        RSInwardOutward rsInwardOutward = rsInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID()).get();
        if (saInvoice != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = saInvoiceRepository.getSaInvoiceDetails(saInvoice.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), Constants.SystemOption.DDSo_DonGia, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            reportName = "phieu_xuat_kho_ban_hang_A5";
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
            }


            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            parameter.put("reporter", organizationUnitOptionReport.getReporter());
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuXuatKhoA5(RSTransfer rsTransfer, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_xuat_kho_ban_hang_A5";
        RSTransfer rsTransfers = rsTransferRepository.findById(rsTransfer.getId()).get();
        if (rsTransfer != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsTransferRepository.getRSTransferDetails(rsTransfer.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getOwPrice(), Constants.SystemOption.DDSo_DonGia, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            reportName = "phieu_xuat_kho_ban_hang_A5";
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsTransfers.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsTransfers.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsTransfers.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsTransfers.getNoMBook() : rsTransfers.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsTransfers.getEmployeeName()) ?
                rsTransfers.getEmployeeName() + (!Strings.isNullOrEmpty(rsTransfers.getAccountingObjectName()) ? " - " +
                    rsTransfers.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsTransfers.getAccountingObjectName())
                ? rsTransfers.getAccountingObjectName() : "");
            parameter.put("contactName", rsTransfers.getEmployeeName());
//            parameter.put("accountingObjectAddress", rsTransfers.getAccountingObjectAddress());
            parameter.put("description", rsTransfers.getReason());
//            parameter.put("numberAttach", rsTransfers.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getOwAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getOwAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
            }


            parameter.put("total", Utils.formatTien(rsTransfers.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, organizationUnit.get().getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuXuatKho(SAInvoice saInvoice, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_xuat_kho_ban_hang";
        RSInwardOutward rsInwardOutward = rsInwardOutwardRepository.findById(saInvoice.getRsInwardOutwardID()).get();
        if (saInvoice != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = saInvoiceRepository.getSaInvoiceDetails(saInvoice.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), Constants.SystemOption.DDSo_DonGia, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            reportName = "phieu_xuat_kho_ban_hang";
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
            }


            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuXuatKho(RSTransfer rsTransfer, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_xuat_kho_ban_hang";
        RSTransfer rsTransfers = rsTransferRepository.findById(rsTransfer.getId()).get();
        if (rsTransfer != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsTransferRepository.getRSTransferDetails(rsTransfer.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryName())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryName()))) {
                        repositoryMap.put(details.get(i).getRepositoryName(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getOwPrice(), Constants.SystemOption.DDSo_DonGia, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());

            reportName = "phieu_xuat_kho_ban_hang";
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsTransfers.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsTransfers.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsTransfers.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsTransfers.getNoMBook() : rsTransfers.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsTransfers.getEmployeeName()) ?
                rsTransfers.getEmployeeName() + (!Strings.isNullOrEmpty(rsTransfers.getAccountingObjectName()) ? " - " +
                    rsTransfers.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsTransfers.getAccountingObjectName())
                ? rsTransfers.getAccountingObjectName() : "");
            parameter.put("contactName", rsTransfers.getEmployeeName());
//            parameter.put("accountingObjectAddress", rsTransfers.getAccountingObjectAddress());
            parameter.put("description", rsTransfers.getReason());
//            parameter.put("numberAttach", rsTransfers.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getOwAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getOwAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
            }


            parameter.put("total", Utils.formatTien(rsTransfers.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, organizationUnit.get().getCurrencyID(),userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getBaoCo(SAInvoice saInvoice, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        String reportName = "GiayBaoCo";
        MBDeposit mbDeposit = mbDepositRepository.findById(saInvoice.getMbDepositID()).get();
        List<SAInvoiceDetails> lstDetails = saInvoice.getsAInvoiceDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(SAInvoiceDetails::getOrderPriority));
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        // khai báo key
        String format_NguyenTe;
        String format_DonGia;
        if (mbDeposit.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            format_NguyenTe = Constants.SystemOption.DDSo_TienVND;
        } else {
            format_NguyenTe = Constants.SystemOption.DDSo_NgoaiTe;
        }
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        parameter.put("AccountingObjectName", mbDeposit.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", mbDeposit.getAccountingObjectAddress());
        parameter.put("Reason", mbDeposit.getReason());
        Boolean currentBook = Utils.PhienSoLamViec(userDTO).equals(1);
        if (Boolean.TRUE.equals(currentBook)) {
            parameter.put("No", mbDeposit.getNoMBook());
        } else {
            parameter.put("No", mbDeposit.getNoFBook());
        }
        parameter.put("Date", convertDate(mbDeposit.getDate()));
        parameter.put("BankName", mbDeposit.getBankName());
        parameter.put("AmountOriginal", Utils.formatTien(mbDeposit.getTotalAmountOriginal(), format_NguyenTe, userDTO));
        parameter.put("CurrencyID", mbDeposit.getCurrencyID());
        parameter.put("ExchangeRate", Utils.formatTien(mbDeposit.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("Manager", "Manager");
        parameter.put("AmountInWord", Utils.GetAmountInWords(mbDeposit.getTotalAmountOriginal(), mbDeposit.getCurrencyID() == null ? "VND" : mbDeposit.getCurrencyID(), userDTO));
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        setHeader(userDTO, parameter);
        String currencyAmountOriginal = "Số tiền nguyên tệ (" + mbDeposit.getCurrencyID() + ")";
        String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
        parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
        parameter.put("CurrencyInWord", currencyAmount);
        if (mbDeposit.getBankAccountDetailID() != null) {
            Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(mbDeposit.getBankAccountDetailID());
            parameter.put("BankAccount", bankAccountDetails.get().getBankAccount());
        }
        if (mbDeposit.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("isShowCurrency", false);
        } else {
            parameter.put("isShowCurrency", true);
        }
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("Director", organizationUnitOptionReport.getDirector());
        parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
        parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        for (int i = 0; i < lstDetails.size(); i++) {
            lstDetails.get(i).setAmountOriginalToString(Utils.formatTien(lstDetails.get(i).getAmountOriginal(), format_NguyenTe, userDTO));
            lstDetails.get(i).setAmountToString(Utils.formatTien(lstDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));

            if (lstDetails.get(i).getDiscountAmountOriginal() != null && lstDetails.get(i).getDiscountAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                SAInvoiceDetails saReturnDetails2 = new SAInvoiceDetails();
                BeanUtils.copyProperties(saReturnDetails2, lstDetails.get(i));
                saReturnDetails2.setId(UUID.randomUUID());
                saReturnDetails2.setDescription("Chiết khấu " + materialGoodsRepository.findMaterialGoodsCodeById(lstDetails.get(i).getMaterialGoodsID()));
                saReturnDetails2.setAmountOriginalToString(Utils.formatTien(lstDetails.get(i).getDiscountAmountOriginal(), format_NguyenTe, userDTO));
                saReturnDetails2.setAmountString(Utils.formatTien(lstDetails.get(i).getDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saReturnDetails2.setDebitAccount(lstDetails.get(i).getCreditAccount());
                saReturnDetails2.setCreditAccount(lstDetails.get(i).getDiscountAccount());
                i++;
                if (lstDetails.size() == i) {
                    lstDetails.add(saReturnDetails2);
                } else {
                    lstDetails.add(i, saReturnDetails2);
                }
            }

            if (lstDetails.get(i).getvATAmountOriginal() != null && lstDetails.get(i).getvATAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                SAInvoiceDetails saReturnDetails = new SAInvoiceDetails();
                BeanUtils.copyProperties(saReturnDetails, lstDetails.get(i));
                saReturnDetails.setId(UUID.randomUUID());
                saReturnDetails.setDescription("Thuế GTGT " + materialGoodsRepository.findMaterialGoodsCodeById(lstDetails.get(i).getMaterialGoodsID()));
                saReturnDetails.setAmountOriginalToString(Utils.formatTien(lstDetails.get(i).getvATAmountOriginal(), format_NguyenTe, userDTO));
                saReturnDetails.setAmountToString(Utils.formatTien(lstDetails.get(i).getvATAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saReturnDetails.setDebitAccount(lstDetails.get(i).getvATAccount());
                saReturnDetails.setCreditAccount(lstDetails.get(i).getDeductionDebitAccount());
                i++;
                if (lstDetails.size() == i) {
                    lstDetails.add(saReturnDetails);
                } else {
                    lstDetails.add(i, saReturnDetails);
                }
            }
        }
        parameter.put("REPORT_MAX_COUNT", lstDetails.size());

        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        return ReportUtils.generateReportPDF(lstDetails, parameter, jasperReport);
    }

    private byte[] getReportSAReturn(UUID id, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        SaReturn saReturn = saReturnRepository.findById(id).get();
        switch (typeReport) {
            case 1:
            case 3:
                try {
                    result = getChungTuKeToan(saReturn, typeReport);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                result = getPhieuChi(saReturn, typeReport);
                break;
            case 4:
                result = getBaoNo(saReturn, typeReport);
                break;
            case 5:
                result = getPhieuNhapKho(saReturn, typeReport);
                break;
            case 6:
                result = getPhieuNhapKhoA5(saReturn, typeReport);
                break;
            case 7:
                result = getPhieuChiA5(saReturn, typeReport);
                break;
            case 10:
            case 11:
                if (saReturn.isIsBill()) {
                    SaBill saBill = sABillRepository.findById(saReturn.getSaReturnDetails().stream().collect(Collectors.toList()).get(0).getSaBillID()).get();
                    if (saBill != null) {
                        result = getVatReport(saBill, typeReport, saReturn.getTypeID());
                    }
                }
                break;
            case 15:
                if (saReturn.isIsBill()) {
                    SaBill saBill = sABillRepository.findById(saReturn.getSaReturnDetails().stream().collect(Collectors.toList()).get(0).getSaBillID()).get();
                    if (saBill != null) {
                        result = getGoodAndServReport(saBill, typeReport);
                    }
                }
                break;
        }
        return result;
    }

    private byte[] getPhieuNhapKhoA5(SaReturn saReturn, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_nhap_kho_A5";
        RSInwardOutward rsInwardOutward = rsInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID()).get();
        if (saReturn != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = saReturnRepository.getSaReturnDetails(saReturn.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            parameter.put("amountInWord", Utils.GetAmountInWords(rsInwardOutward.getTotalAmount(), rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuNhapKhoA5(RSTransfer rsTransfer, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_nhap_kho_A5";
        RSTransfer rsTransferss = rsTransferRepository.findById(rsTransfer.getId()).get();
        if (rsTransfer != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsTransferRepository.getRSTransferDetail(rsTransfer.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getOwPrice(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsTransferss.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsTransferss.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsTransferss.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsTransferss.getNoMBook() : rsTransferss.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsTransferss.getEmployeeName()) ?
                rsTransferss.getEmployeeName() + (!Strings.isNullOrEmpty(rsTransferss.getAccountingObjectName()) ? " - " +
                    rsTransferss.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsTransferss.getAccountingObjectName())
                ? rsTransferss.getAccountingObjectName() : "");
            parameter.put("contactName", rsTransferss.getEmployeeName());
//            parameter.put("accountingObjectAddress", rsTransferss.getAccountingObjectAddress());
            parameter.put("description", rsTransferss.getReason());
//            parameter.put("numberAttach", rsTransferss.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getOwAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getOwAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
            }
            parameter.put("total", Utils.formatTien(totalCapitalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, organizationUnit.get().getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuNhapKho(SaReturn saReturn, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_nhap_kho";
        RSInwardOutward rsInwardOutward = rsInwardOutwardRepository.findById(saReturn.getRsInwardOutwardID()).get();
        if (saReturn != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = saReturnRepository.getSaReturnDetails(saReturn.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getUnitPriceOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsInwardOutward.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsInwardOutward.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsInwardOutward.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsInwardOutward.getNoMBook() : rsInwardOutward.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsInwardOutward.getContactName()) ?
                rsInwardOutward.getContactName() + (!Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName()) ? " - " +
                    rsInwardOutward.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsInwardOutward.getAccountingObjectName())
                ? rsInwardOutward.getAccountingObjectName() : "");
            parameter.put("contactName", rsInwardOutward.getContactName());
            parameter.put("accountingObjectAddress", rsInwardOutward.getAccountingObjectAddress());
            parameter.put("description", rsInwardOutward.getReason());
            parameter.put("numberAttach", rsInwardOutward.getNumberAttach());
            parameter.put("total", Utils.formatTien(rsInwardOutward.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            parameter.put("amountInWord", Utils.GetAmountInWords(rsInwardOutward.getTotalAmount(), rsInwardOutward.getCurrencyID() == null ? "VND" : rsInwardOutward.getCurrencyID(), userDTO));
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getPhieuNhapKho(RSTransfer rsTransfer, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "phieu_nhap_kho";
        RSTransfer rsTransferss = rsTransferRepository.findById(rsTransfer.getId()).get();
        if (rsTransfer != null) {
            UserDTO userDTO = userService.getAccount();
            List<RSInwardOutwardDetailReportDTO> details = rsTransferRepository.getRSTransferDetail(rsTransfer.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryName());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountToString(Utils.formatTien(details.get(i).getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setUnitPriceOriginalToString(Utils.formatTien(details.get(i).getOwPrice(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            // khai báo key
            checkEbPackage(userDTO, parameter);
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(rsTransferss.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(rsTransferss.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(rsTransferss.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            parameter.put("noBook", Utils.PhienSoLamViec(userDTO).equals(1) ? rsTransferss.getNoMBook() : rsTransferss.getNoFBook());
            parameter.put("accountingObjectContact", !Strings.isNullOrEmpty(rsTransferss.getEmployeeName()) ?
                rsTransferss.getEmployeeName() + (!Strings.isNullOrEmpty(rsTransferss.getAccountingObjectName()) ? " - " +
                    rsTransferss.getAccountingObjectName() : "") : !Strings.isNullOrEmpty(rsTransferss.getAccountingObjectName())
                ? rsTransferss.getAccountingObjectName() : "");
            parameter.put("contactName", rsTransferss.getEmployeeName());
//            parameter.put("accountingObjectAddress", rsTransferss.getAccountingObjectAddress());
            parameter.put("description", rsTransferss.getReason());
//            parameter.put("numberAttach", rsTransferss.getNumberAttach());
            BigDecimal totalCapitalAmount = BigDecimal.ZERO;
            for (RSInwardOutwardDetailReportDTO rsInwardOutwardDetailReportDTO : details) {
                if (rsInwardOutwardDetailReportDTO.getOwAmount() != null) {
                    totalCapitalAmount = totalCapitalAmount.add(rsInwardOutwardDetailReportDTO.getOwAmount());
                }
                rsInwardOutwardDetailReportDTO.setTotalToString(Utils.formatTien(totalCapitalAmount, getTypeAmount(userDTO, organizationUnit.get().getCurrencyID()), userDTO));
            }
            parameter.put("total", Utils.formatTien(totalCapitalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            if (totalCapitalAmount != null) {
                parameter.put("amountInWord", Utils.GetAmountInWords(totalCapitalAmount, organizationUnit.get().getCurrencyID(), userDTO));
            }
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getBaoNo(SaReturn saReturn, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        String reportName = "";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        String formatOriginal = "";
        JasperReport jasperReport;
        if (saReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            formatOriginal = Constants.SystemOption.DDSo_TienVND;
        } else {
            formatOriginal = Constants.SystemOption.DDSo_NgoaiTe;
        }

        List<SaReturnDetails> details = new ArrayList<>(saReturn.getSaReturnDetails());
        Collections.sort(details, Comparator.comparingInt(SaReturnDetails::getOrderPriority));
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setAmountOriginalString(Utils.formatTien(details.get(i).getAmountOriginal(), formatOriginal, userDTO));
            details.get(i).setAmountString(Utils.formatTien(details.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));

            if (details.get(i).getDiscountAmountOriginal() != null && details.get(i).getDiscountAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                SaReturnDetails saReturnDetails2 = new SaReturnDetails();
                BeanUtils.copyProperties(saReturnDetails2, details.get(i));
                saReturnDetails2.setDescription("Chiết khấu " + materialGoodsRepository.findMaterialGoodsCodeById(details.get(i).getMaterialGoodsID()));
                saReturnDetails2.setAmountOriginalString(Utils.formatTien(details.get(i).getDiscountAmountOriginal(), formatOriginal, userDTO));
                saReturnDetails2.setAmountString(Utils.formatTien(details.get(i).getDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saReturnDetails2.setDebitAccount(details.get(i).getCreditAccount());
                saReturnDetails2.setCreditAccount(details.get(i).getDiscountAccount());
                i++;
                if (details.size() == i) {
                    details.add(saReturnDetails2);
                } else {
                    details.add(i, saReturnDetails2);
                }

            }

            if (details.get(i).getVatAmountOriginal() != null && details.get(i).getVatAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {
                SaReturnDetails saReturnDetails = new SaReturnDetails();
                BeanUtils.copyProperties(saReturnDetails, details.get(i));
                saReturnDetails.setDescription("Thuế GTGT " + materialGoodsRepository.findMaterialGoodsCodeById(details.get(i).getMaterialGoodsID()));
                saReturnDetails.setAmountOriginalString(Utils.formatTien(details.get(i).getVatAmountOriginal(), formatOriginal, userDTO));
                saReturnDetails.setAmountString(Utils.formatTien(details.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saReturnDetails.setDebitAccount(details.get(i).getVatAccount());
                saReturnDetails.setCreditAccount(details.get(i).getDeductionDebitAccount());
                i++;
                if (details.size() == i) {
                    details.add(saReturnDetails);
                } else {
                    details.add(i, saReturnDetails);
                }
            }
            if (details.get(i).getVatAmountOriginal() != null && details.get(i).getVatAmountOriginal().compareTo(BigDecimal.ZERO) > 0) {

            }
        }
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        setHeader(userDTO, parameter);
        reportName = "GiayBaoNo";
        // khai báo key
        if (saReturn.getContactName() != null && !saReturn.getContactName().isEmpty()
            && saReturn.getAccountingObjectName() != null && !saReturn.getAccountingObjectName().isEmpty()) {
            parameter.put("AccountingObjectName", saReturn.getContactName() + " - " + saReturn.getAccountingObjectName());
        } else if (saReturn.getContactName() != null  && !saReturn.getContactName().isEmpty()) {
            parameter.put("AccountingObjectName", saReturn.getContactName());
        } else if (saReturn.getAccountingObjectName() != null && !saReturn.getAccountingObjectName().isEmpty()) {
            parameter.put("AccountingObjectName", saReturn.getAccountingObjectName());
        }
//        parameter.put("AccountingObjectName", saReturn.getContactName());
//        parameter.put("AccountingObjectName2", saReturn.getAccountingObjectName());
        parameter.put("AccountingObjectAddress", saReturn.getAccountingObjectAddress());
        parameter.put("CurrencyID", saReturn.getCurrencyID());
        Boolean currentBook = Utils.PhienSoLamViec(userDTO).equals(1);
        if (Boolean.TRUE.equals(currentBook)) {
            parameter.put("No", saReturn.getNoMBook());
        } else {
            parameter.put("No", saReturn.getNoFBook());
        }
        parameter.put("PostedDate", convertDate(saReturn.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(saReturn.getTotalAmountOriginal().subtract(saReturn.getTotalDiscountAmountOriginal()).add(saReturn.getTotalVATAmountOriginal()), formatOriginal, userDTO));
        parameter.put("TotalAmount", Utils.formatTien(saReturn.getTotalAmount().subtract(saReturn.getTotalDiscountAmount()).add(saReturn.getTotalVATAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(saReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("AmountOriginal", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal().subtract(saReturn.getTotalDiscountAmountOriginal())), formatOriginal, userDTO));
        parameter.put("Amount", Utils.formatTien(saReturn.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("BankName", saReturn.getAccountingObjectBankName());
        parameter.put("BankAccount", saReturn.getAccountingObjectBankAccount());
        parameter.put("Reason", saReturn.getReason());
        parameter.put("ExchangeRate", Utils.formatTien(saReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("Manager", "Manager");
        parameter.put("Total", Utils.formatTien(saReturn.getTotalAmountOriginal(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal().subtract(saReturn.getTotalDiscountAmountOriginal())), saReturn.getCurrencyID() == null ? "VND" : saReturn.getCurrencyID(), userDTO));
        parameter.put("CurrencyInWord", "Số tiền nguyên tệ (" + saReturn.getCurrencyID() + ")");
//                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mBTellerPaper.getCurrencyId();
//                parameter.put("ConversionPair", conversionPair);
        if (saReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            parameter.put("isShowCurrency", false);
        } else {
            parameter.put("isShowCurrency", true);
        }
        if (organizationUnitOptionReport.getHeaderSetting() == 0) {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().gettaxCode());
        } else {
            parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("CompanyAddress", organizationUnit.get().getAddress());
        }
        if (!saReturn.getCurrencyID().equals(organizationUnit.get().getCurrencyID())) {
            String currencyAmountOriginal = "Số tiền nguyên tệ (" + saReturn.getCurrencyID() + ")";
            String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
            parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
            parameter.put("CurrencyInWord", currencyAmount);
            parameter.put("REPORT_MAX_COUNT", details.size());
        }
        parameter.put("REPORT_MAX_COUNT", details.size());
        parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
        parameter.put("Director", organizationUnitOptionReport.getDirector());
        parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
        parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
        if (Boolean.FALSE.equals(organizationUnitOptionReport.getDisplayAccount())) {
            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
        } else {
            parameter.put("Reporter", user.get().getFullName());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuChiA5(SaReturn saReturn, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "mCPayment-A5";
        List<SaReturnDetails> lstDetails = saReturn.getSaReturnDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(SaReturnDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        setHeader(userDTO, parameter);
        checkEbPackage(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectName())) {
            parameter.put("NguoiNhan", getName(saReturn.getAccountingObjectName(), saReturn.getContactName()));
        }
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", saReturn.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(saReturn.getReason())) {
            parameter.put("LyDoNop", saReturn.getReason());
        }
        if (!StringUtils.isEmpty(saReturn.getNumberAttach())) {
            parameter.put("KemTheo", saReturn.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()), saReturn.getCurrencyID() == null ? "VND" : saReturn.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", saReturn.getExchangeRate() == null ? "1" : Utils.formatTien(saReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()).multiply(saReturn.getExchangeRate()), Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", saReturn.getNoFBook());
        } else {
            parameter.put("So", saReturn.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNhanTien", saReturn.getContactName());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (SaReturnDetails saReturnDetails : lstDetails) {
            if (!lstCo.contains(saReturnDetails.getCreditAccount())) {
                lstCo.add(saReturnDetails.getCreditAccount());
            }
            if (!lstNo.contains(saReturnDetails.getDebitAccount())) {
                lstNo.add(saReturnDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", saReturn.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saReturn.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + saReturn.getDate().getDayOfMonth() + " tháng " + saReturn.getDate().getMonthValue() + " năm " + saReturn.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuChi(SaReturn saReturn, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "mCPayment";
        switch (typeReport) {
            case Constants.Report.PhieuChiTT2Lien:
                reportName = "mCPayment-2-Lien";
                break;
            case Constants.Report.PhieuChiA5:
                reportName = "mCPayment-A5";
                break;
            default:
                reportName = "mCPayment";
                break;
        }
        List<SaReturnDetails> lstDetails = saReturn.getSaReturnDetails().stream().collect(Collectors.toList());
        Collections.sort(lstDetails, Comparator.comparingInt(SaReturnDetails::getOrderPriority));
        // khai báo key
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        checkEbPackage(userDTO, parameter);
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectName())) {
            parameter.put("NguoiNhan", getName(saReturn.getAccountingObjectName(), saReturn.getContactName()));
        }
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectAddress())) {
            parameter.put("DiaChiNgNop", saReturn.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(saReturn.getReason())) {
            parameter.put("LyDoNop", saReturn.getReason());
        }
        if (!StringUtils.isEmpty(saReturn.getNumberAttach())) {
            parameter.put("KemTheo", saReturn.getNumberAttach());
        }
        parameter.put("SoTien", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
        parameter.put("SoTien_String", Utils.GetAmountInWords(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()), saReturn.getCurrencyID() == null ? "VND" : saReturn.getCurrencyID(), userDTO));
        parameter.put("TyGiaNT", saReturn.getExchangeRate() == null ? "1" : Utils.formatTien(saReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        parameter.put("TienQuyDoi", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).subtract(saReturn.getTotalDiscountAmountOriginal()).multiply(saReturn.getExchangeRate()), Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            parameter.put("So", saReturn.getNoFBook());
        } else {
            parameter.put("So", saReturn.getNoMBook());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("NguoiNhanTien", saReturn.getContactName());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            } else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
        }
        List<String> lstCo = new ArrayList<>();
        List<String> lstNo = new ArrayList<>();
        for (SaReturnDetails saReturnDetails : lstDetails) {
            if (!lstCo.contains(saReturnDetails.getCreditAccount())) {
                lstCo.add(saReturnDetails.getCreditAccount());
            }
            if (!lstNo.contains(saReturnDetails.getDebitAccount())) {
                lstNo.add(saReturnDetails.getDebitAccount());
            }
        }
        parameter.put("No", getNoCo(lstNo));
        parameter.put("Co", getNoCo(lstCo));
        parameter.put("currencyID", saReturn.getCurrencyID());
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saReturn.getCurrencyID()));
        parameter.put("Ngay", "Ngày " + saReturn.getDate().getDayOfMonth() + " tháng " + saReturn.getDate().getMonthValue() + " năm " + saReturn.getDate().getYear());
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        // khai báo list detail
        List<ExampleDTO> details = new ArrayList<>();
        details.add(new ExampleDTO("dataX", "dataX1"));
        jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(MCPayment mcPayment, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<MCPaymentDetails> mcPaymentDetails = new ArrayList<>();
        mcPaymentDetails.addAll(mcPayment.getMCPaymentDetails());
        Collections.sort(mcPaymentDetails, Comparator.comparingInt(MCPaymentDetails::getOrderPriority));
        /*boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;*/
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        /*Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }*/
        Map<String, Object> parameter = new HashMap<>();
        checkEbPackage(userDTO, parameter);
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(mcPayment.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", mcPayment.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(mcPayment.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", mcPayment.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", mcPayment.getCurrencyID());
        if (Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec)) {
            parameter.put("No", mcPayment.getNoFBook());
        } else {
            parameter.put("No", mcPayment.getNoMBook());
        }
        parameter.put("Date", convertDate(mcPayment.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(mcPayment.getTotalAmountOriginal(), getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(mcPayment.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(mcPayment.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + mcPayment.getCurrencyID();
        if (!StringUtils.isEmpty(mcPayment.getReason())) {
            parameter.put("Reason", mcPayment.getReason());
        }
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(mcPayment.getTotalAmountOriginal(), mcPayment.getCurrencyID() == null ? "VND" : mcPayment.getCurrencyID(), userDTO));
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalQD = BigDecimal.ZERO;
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < mcPaymentDetails.size(); i++) {
                mcPaymentDetails.get(i).setAmountOriginalToString(Utils.formatTien(mcPaymentDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
                mcPaymentDetails.get(i).setAmountToString(Utils.formatTien(mcPaymentDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                if (mcPaymentDetails.get(i).getAmount() != null) {
                    totalQD = totalQD.add(mcPaymentDetails.get(i).getAmount());
                }
                if (mcPaymentDetails.get(i).getAmountOriginal() != null) {
                    total = total.add(mcPaymentDetails.get(i).getAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(total, getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalQD, Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < mcPaymentDetails.size(); i++) {
                mcPaymentDetails.get(i).setAmountOriginalToString(Utils.formatTien(mcPaymentDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
                if (mcPaymentDetails.get(i).getAmountOriginal() != null) {
                    total = total.add(mcPaymentDetails.get(i).getAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(total, getTypeAmount(userDTO, mcPayment.getCurrencyID()), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(mcPayment.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", mcPaymentDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(mcPaymentDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(SAInvoice saInvoice, int typeReport) throws JRException, InvocationTargetException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>();
        saInvoiceDetails.addAll(saInvoice.getsAInvoiceDetails());
        Collections.sort(saInvoiceDetails, Comparator.comparingInt(SAInvoiceDetails::getOrderPriority));
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(saInvoice.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", saInvoice.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(saInvoice.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", saInvoice.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", saInvoice.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", saInvoice.getNoMBook());
            } else {
                parameter.put("No", saInvoice.getNoFBook());
            }
        } else {
            parameter.put("No", saInvoice.getNoFBook());
        }
        parameter.put("Date", convertDate(saInvoice.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(saInvoice.getTotalAmountOriginal().add(saInvoice.getTotalVATAmountOriginal()).add(saInvoice.getTotalDiscountAmountOriginal()), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(saInvoice.getTotalAmount().add(saInvoice.getTotalVATAmount()).add(saInvoice.getTotalDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(saInvoice.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + saInvoice.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (!StringUtils.isEmpty(saInvoice.getReason())) {
            parameter.put("Reason", saInvoice.getReason());
        }
        List<SAInvoiceDetails> saInvoiceDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < saInvoiceDetails.size(); i++) {
                saInvoiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                saInvoiceDetails.get(i).setAmountToString(Utils.formatTien(saInvoiceDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saInvoiceDetailsList.add(saInvoiceDetails.get(i));
                totalAmountOriginal = totalAmountOriginal.add(saInvoiceDetails.get(i).getAmountOriginal());
                totalAmount = totalAmount.add(saInvoiceDetails.get(i).getAmount());

                if (saInvoiceDetails.get(i).getDiscountAmountOriginal().floatValue() > 0) {
                    SAInvoiceDetails saInvoiceDetailsDis = new SAInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(saInvoiceDetailsDis, saInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    MaterialGoods materialGoods = materialGoodsRepository.findById(saInvoiceDetailsDis.getMaterialGoodsID()).get();
                    saInvoiceDetailsDis.setDescription("Chiết khấu " + materialGoods.getMaterialGoodsCode());
                    saInvoiceDetailsDis.setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getDiscountAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                    saInvoiceDetailsDis.setAmountToString(Utils.formatTien(saInvoiceDetails.get(i).getDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    saInvoiceDetailsDis.setCreditAccount(saInvoiceDetailsDis.getDiscountAccount());
                    saInvoiceDetailsDis.setDebitAccount(saInvoiceDetails.get(i).getCreditAccount());
                    saInvoiceDetailsList.add(saInvoiceDetailsDis);
                    totalAmountOriginal = totalAmountOriginal.add(saInvoiceDetails.get(i).getDiscountAmountOriginal());
                    totalAmount = totalAmount.add(saInvoiceDetails.get(i).getDiscountAmount());
                }

                if (saInvoiceDetails.get(i).getvATAmountOriginal().floatValue() > 0) {
                    SAInvoiceDetails saInvoiceDetailsVAT = new SAInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(saInvoiceDetailsVAT, saInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    saInvoiceDetailsVAT.setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getvATAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                    saInvoiceDetailsVAT.setAmountToString(Utils.formatTien(saInvoiceDetails.get(i).getvATAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    saInvoiceDetailsVAT.setDescription(saInvoiceDetailsVAT.getvATDescription());
                    saInvoiceDetailsVAT.setDebitAccount(saInvoiceDetailsVAT.getDeductionDebitAccount());
                    saInvoiceDetailsVAT.setCreditAccount(saInvoiceDetailsVAT.getvATAccount());
                    saInvoiceDetailsList.add(saInvoiceDetailsVAT);
                    totalAmountOriginal = totalAmountOriginal.add(saInvoiceDetails.get(i).getvATAmountOriginal());
                    totalAmount = totalAmount.add(saInvoiceDetails.get(i).getvATAmount());
                }

                if (saInvoiceDetails.get(i).getExportTaxAmount().floatValue() > 0) {
                    SAInvoiceDetails saInvoiceDetailsVAT = new SAInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(saInvoiceDetailsVAT, saInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    MaterialGoods materialGoods = materialGoodsRepository.findById(saInvoiceDetailsVAT.getMaterialGoodsID()).get();
                    saInvoiceDetailsVAT.setAmountOriginalToString("");
                    saInvoiceDetailsVAT.setAmountToString(Utils.formatTien(saInvoiceDetails.get(i).getExportTaxAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    saInvoiceDetailsVAT.setDescription("Thuế XK " + materialGoods.getMaterialGoodsCode());
                    saInvoiceDetailsVAT.setDebitAccount(saInvoiceDetailsVAT.getExportTaxAccountCorresponding());
                    saInvoiceDetailsVAT.setCreditAccount(saInvoiceDetailsVAT.getExportTaxAmountAccount());
                    saInvoiceDetailsList.add(saInvoiceDetailsVAT);
                    //totalAmountOriginal = totalAmountOriginal.add(saInvoiceDetails.get(i).getExportTaxAmount());
                    totalAmount = totalAmount.add(saInvoiceDetails.get(i).getExportTaxAmount());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, saInvoice.getCurrencyID() == null ? "VND" : saInvoice.getCurrencyID(), userDTO));
        } else {
            for (int i = 0; i < saInvoiceDetails.size(); i++) {
                saInvoiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                saInvoiceDetailsList.add(saInvoiceDetails.get(i));
                totalAmount = totalAmount.add(saInvoiceDetails.get(i).getAmountOriginal());

                if (saInvoiceDetails.get(i).getDiscountAmountOriginal().floatValue() > 0) {
                    SAInvoiceDetails saInvoiceDetailsDis = new SAInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(saInvoiceDetailsDis, saInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    MaterialGoods materialGoods = materialGoodsRepository.findById(saInvoiceDetailsDis.getMaterialGoodsID()).get();
                    saInvoiceDetailsDis.setDescription("Chiết khấu " + materialGoods.getMaterialGoodsCode());
                    saInvoiceDetailsDis.setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getDiscountAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                    saInvoiceDetailsDis.setCreditAccount(saInvoiceDetailsDis.getDiscountAccount());
                    saInvoiceDetailsDis.setDebitAccount(saInvoiceDetails.get(i).getCreditAccount());
                    saInvoiceDetailsList.add(saInvoiceDetailsDis);
                    totalAmount = totalAmount.add(saInvoiceDetails.get(i).getDiscountAmountOriginal());
                }

                if (saInvoiceDetails.get(i).getvATAmountOriginal().floatValue() > 0) {
                    SAInvoiceDetails saInvoiceDetailsVAT = new SAInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(saInvoiceDetailsVAT, saInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    saInvoiceDetailsVAT.setAmountOriginalToString(Utils.formatTien(saInvoiceDetails.get(i).getvATAmountOriginal(), getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
                    saInvoiceDetailsVAT.setDescription(saInvoiceDetailsVAT.getvATDescription());
                    saInvoiceDetailsVAT.setDebitAccount(saInvoiceDetailsVAT.getDeductionDebitAccount());
                    saInvoiceDetailsVAT.setCreditAccount(saInvoiceDetailsVAT.getvATAccount());
                    saInvoiceDetailsList.add(saInvoiceDetailsVAT);
                    totalAmount = totalAmount.add(saInvoiceDetails.get(i).getvATAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmount, getTypeAmount(userDTO, saInvoice.getCurrencyID()), userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmount, saInvoice.getCurrencyID() == null ? "VND" : saInvoice.getCurrencyID(), userDTO));
        }
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saInvoice.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", saInvoiceDetailsList.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        checkEbPackage(userDTO, parameter);
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(saInvoiceDetailsList, parameter, jasperReport);
        return result;
    }

    private byte[] getChungTuKeToan(SaReturn saReturn, int typeReport) throws JRException, InvocationTargetException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<SaReturnDetails> saReturnDetails = new ArrayList<>();
        saReturnDetails.addAll(saReturn.getSaReturnDetails());
        Collections.sort(saReturnDetails, Comparator.comparingInt(SaReturnDetails::getOrderPriority));
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", saReturn.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(saReturn.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", saReturn.getAccountingObjectAddress());
        }
        parameter.put("CurrencyID", saReturn.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", saReturn.getNoMBook());
            } else {
                parameter.put("No", saReturn.getNoFBook());
            }
        } else {
            parameter.put("No", saReturn.getNoFBook());
        }
        parameter.put("Date", convertDate(saReturn.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(saReturn.getTotalAmountOriginal().add(saReturn.getTotalVATAmountOriginal()).add(saReturn.getTotalDiscountAmountOriginal()), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(saReturn.getTotalAmount().add(saReturn.getTotalVATAmount()).add(saReturn.getTotalDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(saReturn.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + saReturn.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        if (!StringUtils.isEmpty(saReturn.getReason())) {
            parameter.put("Reason", saReturn.getReason());
        }
        List<SaReturnDetails> saReturnDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < saReturnDetails.size(); i++) {
                saReturnDetails.get(i).setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                saReturnDetails.get(i).setAmountToString(Utils.formatTien(saReturnDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                saReturnDetailsList.add(saReturnDetails.get(i));
                totalAmountOriginal = totalAmountOriginal.add(saReturnDetails.get(i).getAmountOriginal());
                totalAmount = totalAmount.add(saReturnDetails.get(i).getAmount());
                if (saReturnDetails.get(i).getDiscountAmountOriginal().floatValue() > 0) {
                    SaReturnDetails saReturnDetailsDis = new SaReturnDetails();
                    try {
                        BeanUtils.copyProperties(saReturnDetailsDis, saReturnDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    MaterialGoods materialGoods = materialGoodsRepository.findById(saReturnDetailsDis.getMaterialGoodsID()).get();
                    saReturnDetailsDis.setDescription("Chiết khấu " + materialGoods.getMaterialGoodsCode());
                    saReturnDetailsDis.setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getDiscountAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                    saReturnDetailsDis.setAmountToString(Utils.formatTien(saReturnDetails.get(i).getDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    saReturnDetailsDis.setCreditAccount(saReturnDetailsDis.getDiscountAccount());
                    saReturnDetailsDis.setDebitAccount(saReturnDetails.get(i).getCreditAccount());
                    saReturnDetailsList.add(saReturnDetailsDis);
                    totalAmountOriginal = totalAmountOriginal.add(saReturnDetails.get(i).getDiscountAmountOriginal());
                    totalAmount = totalAmount.add(saReturnDetails.get(i).getDiscountAmount());
                }
                if (saReturnDetails.get(i).getVatAmountOriginal().floatValue() > 0) {
                    SaReturnDetails saReturnDetailsVAT = new SaReturnDetails();
                    try {
                        BeanUtils.copyProperties(saReturnDetailsVAT, saReturnDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    saReturnDetailsVAT.setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getVatAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                    saReturnDetailsVAT.setAmountToString(Utils.formatTien(saReturnDetails.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    saReturnDetailsVAT.setDescription(saReturnDetailsVAT.getVatDescription());
                    saReturnDetailsVAT.setDebitAccount(saReturnDetailsVAT.getVatAccount());
                    saReturnDetailsVAT.setCreditAccount(saReturnDetailsVAT.getDeductionDebitAccount());
                    saReturnDetailsList.add(saReturnDetailsVAT);
                    totalAmountOriginal = totalAmountOriginal.add(saReturnDetails.get(i).getVatAmountOriginal());
                    totalAmount = totalAmount.add(saReturnDetails.get(i).getVatAmount());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, saReturn.getCurrencyID() == null ? "VND" : saReturn.getCurrencyID(), userDTO));
        } else {
            for (int i = 0; i < saReturnDetails.size(); i++) {
                saReturnDetails.get(i).setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                saReturnDetailsList.add(saReturnDetails.get(i));
                totalAmount = totalAmount.add(saReturnDetails.get(i).getAmountOriginal());

                if (saReturnDetails.get(i).getDiscountAmountOriginal().floatValue() > 0) {
                    SaReturnDetails saReturnDetailsDis = new SaReturnDetails();
                    try {
                        BeanUtils.copyProperties(saReturnDetailsDis, saReturnDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    MaterialGoods materialGoods = materialGoodsRepository.findById(saReturnDetailsDis.getMaterialGoodsID()).get();
                    saReturnDetailsDis.setDescription("Chiết khấu " + materialGoods.getMaterialGoodsCode());
                    saReturnDetailsDis.setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getDiscountAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                    saReturnDetailsDis.setCreditAccount(saReturnDetailsDis.getDiscountAccount());
                    saReturnDetailsDis.setDebitAccount(saReturnDetails.get(i).getCreditAccount());
                    saReturnDetailsList.add(saReturnDetailsDis);
                    totalAmount = totalAmount.add(saReturnDetails.get(i).getDiscountAmountOriginal());
                }
                if (saReturnDetails.get(i).getVatAmountOriginal().floatValue() > 0) {
                    SaReturnDetails saReturnDetailsVAT = new SaReturnDetails();
                    try {
                        BeanUtils.copyProperties(saReturnDetailsVAT, saReturnDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    saReturnDetailsVAT.setAmountOriginalToString(Utils.formatTien(saReturnDetails.get(i).getVatAmountOriginal(), getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
                    saReturnDetailsVAT.setDescription(saReturnDetailsVAT.getVatDescription());
                    saReturnDetailsVAT.setDebitAccount(saReturnDetailsVAT.getVatAccount());
                    saReturnDetailsVAT.setCreditAccount(saReturnDetailsVAT.getDeductionDebitAccount());
                    saReturnDetailsList.add(saReturnDetailsVAT);
                    totalAmount = totalAmount.add(saReturnDetails.get(i).getVatAmountOriginal());
                }
            }
            parameter.put("Total", Utils.formatTien(totalAmount, getTypeAmount(userDTO, saReturn.getCurrencyID()), userDTO));
            parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmount, saReturn.getCurrencyID() == null ? "VND" : saReturn.getCurrencyID(), userDTO));
        }

        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(saReturn.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", saReturnDetailsList.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        checkEbPackage(userDTO, parameter);
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(saReturnDetailsList, parameter, jasperReport);
        return result;
    }

    private byte[] getVatReport(SaBill saBill, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        return getVatReport(saBill, typeReport, -1, false);
    }

    private byte[] getVatReport(SaBill saBill, int typeReport, boolean fromSaInvoice) throws JRException, InvocationTargetException, IllegalAccessException {
        return getVatReport(saBill, typeReport, -1, fromSaInvoice);
    }

    private byte[] getVatReport(SaBill saBill, int typeReport, Integer typeID_SAInvoice) throws JRException, InvocationTargetException, IllegalAccessException {
        return getVatReport(saBill, typeReport, typeID_SAInvoice, false);
    }

    private byte[] getVatReport(SaBill saBill, int typeReport, Integer typeID_SAInvoice, boolean fromSaInvoice) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        List<SABillReportDTO> sABillDetailsDTO = sABillRepository.findSABillDetailsDTO(saBill.getId());
        List<SABillReportDTO> anotherList = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        Map<String, Object> parameter = new HashMap<>();
        BigDecimal totalVatAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalAll = BigDecimal.ZERO;
        BigDecimal totalDiscountAmountOriginal = BigDecimal.ZERO;
        BigDecimal totalQuantities = BigDecimal.ZERO;
        checkEbPackage(userDTO, parameter);
        parameter.put("invoiceForm", saBill.getInvoiceTemplate());
        parameter.put("invoiceSerial", saBill.getInvoiceSeries());
        parameter.put("invoiceNo", saBill.getInvoiceNo());
        parameter.put("AccountingObjectName", userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase());
        parameter.put("AccountingObjectAddress", userDTO.getOrganizationUnit().getAddress());
        if (userDTO.getOrganizationUnit().gettaxCode() == null) {
            parameter.put("TaxCode", userDTO.getOrganizationUnit().gettaxCode());
        } else {
            parameter.put("TaxCode", userDTO.getOrganizationUnit().gettaxCode().replace("", " "));
        }
        parameter.put("phoneNumber", userDTO.getOrganizationUnit().getPhoneNumber());
        BankAccountDetails bankAccountDetails = new BankAccountDetails();
        UUID bankAccountDetailID = userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getBankAccountDetailID();
        if (bankAccountDetailID == null) {
            parameter.put("AccountNumber", null);
            parameter.put("AccountingObjectBankName", null);
            parameter.put("AccountingObjectEmail", null);
            parameter.put("AccountingObjectWebsite", null);
        } else {
            bankAccountDetails = bankAccountDetailsRepository.findById(bankAccountDetailID).get();
            parameter.put("AccountNumber", bankAccountDetails.getBankAccount());
            parameter.put("AccountingObjectBankName", bankAccountDetails.getBankName());
            parameter.put("AccountingObjectEmail", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getEmail());
            parameter.put("AccountingObjectWebsite", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getWebsite());
            parameter.put("AccountFaxNumber", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getFax());
        }
        parameter.put("formula", null);
        parameter.put("contactName", saBill.getContactName());
        parameter.put("companyName", saBill.getAccountingObjectName());
        parameter.put("address", saBill.getAccountingObjectAddress());
        parameter.put("accountNumber", saBill.getAccountingObjectBankAccount());
        parameter.put("taxCode", saBill.getCompanyTaxCode());
        parameter.put("paymentMethod", saBill.getPaymentMethod());
        parameter.put("invoiceDate", "Ngày(Date) " + saBill.getInvoiceDate().getDayOfMonth() + " Tháng(Month) " + saBill.getInvoiceDate().getMonthValue() + " Năm(Year) " + saBill.getInvoiceDate().getYear());

        List<BigDecimal> vatRates = new ArrayList<>();
        if (Boolean.TRUE.equals(saBill.getIsAttachList())) {
            for (SABillReportDTO saBillDetailsDTO : sABillDetailsDTO) {
                if (saBillDetailsDTO.getDiscountAmountOriginal() != null) {
                    totalDiscountAmountOriginal = totalDiscountAmountOriginal.add(saBillDetailsDTO.getDiscountAmountOriginal());
                }
                if (saBillDetailsDTO.getVatAmount() != null) {
                    totalAmountOriginal = totalAmountOriginal.add(saBillDetailsDTO.getAmountOriginal());
                }
                if (saBillDetailsDTO.getVatAmountOriginal() != null) {
                    totalVatAmountOriginal = totalVatAmountOriginal.add(saBillDetailsDTO.getVatAmountOriginal());
                }
                if (Constants.Report.HoadonGTGT == typeReport) {
                    totalAll = totalAmountOriginal.add(totalVatAmountOriginal).subtract(totalDiscountAmountOriginal);
                } else {
                    totalAll = totalAmountOriginal.subtract(totalDiscountAmountOriginal);
                }
                if (saBillDetailsDTO.getQuantity() != null) {
                    totalQuantities = totalQuantities.add(saBillDetailsDTO.getQuantity());
                }
            }
            SABillReportDTO saBillReportDTO = new SABillReportDTO();
            BeanUtils.copyProperties(saBillReportDTO, sABillDetailsDTO.get(0));
            anotherList.add(saBillReportDTO);
            checkSaBillList(anotherList);
        }
        if (Boolean.FALSE.equals(saBill.getIsAttachList()) || saBill.getIsAttachList() == null) {
            checkSaBillList(sABillDetailsDTO);
            for (SABillReportDTO saBillDetailsDTO : sABillDetailsDTO) {
                vatRates.add(saBillDetailsDTO.getVatRate());
                if (saBillDetailsDTO.getDiscountAmountOriginal() != null) {
                    totalDiscountAmountOriginal = totalDiscountAmountOriginal.add(saBillDetailsDTO.getDiscountAmountOriginal());
                }
                if (saBillDetailsDTO.getVatAmount() != null) {
                    totalAmountOriginal = totalAmountOriginal.add(saBillDetailsDTO.getAmountOriginal());
                }
                if (saBillDetailsDTO.getVatAmountOriginal() != null) {
                    totalVatAmountOriginal = totalVatAmountOriginal.add(saBillDetailsDTO.getVatAmountOriginal());
                }
                if (Constants.Report.HoadonGTGT == typeReport) {
                    totalAll = totalAmountOriginal.add(totalVatAmountOriginal).subtract(totalDiscountAmountOriginal);
                } else {
                    totalAll = totalAmountOriginal.subtract(totalDiscountAmountOriginal);
                }
                if (saBillDetailsDTO.getQuantity() != null) {
                    saBillDetailsDTO.setQuantityString(Utils.formatTien(saBillDetailsDTO.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                    totalQuantities = totalQuantities.add(saBillDetailsDTO.getQuantity());
                    saBillDetailsDTO.setTotalQuantitiesString(Utils.formatTien(totalQuantities, Constants.SystemOption.DDSo_SoLuong, userDTO));
                }
                if (saBillDetailsDTO.getUnitPrice() != null) {
                    saBillDetailsDTO.setUnitPriceString(Utils.formatTien(saBillDetailsDTO.getUnitPriceOriginal(), getTypeUnitPrice(userDTO, saBill.getCurrencyID()), userDTO));
                }
                if (saBillDetailsDTO.getAmountOriginal() != null) {
                    saBillDetailsDTO.setAmountString(Utils.formatTien(saBillDetailsDTO.getAmountOriginal(), getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                }
                if (Boolean.TRUE.equals(saBillDetailsDTO.isIsPromotion())) {
                    saBillDetailsDTO.setDescription(saBillDetailsDTO.getDescription() + " (Hàng khuyến mại)");
                }
                if ((sABillDetailsDTO.indexOf(saBillDetailsDTO) + 1) % 10 == 0) {
                    try {
                        boolean allEquals = vatRates.stream().filter(n -> n.compareTo(BigDecimal.valueOf(20)) != 0).distinct().limit(2).count() <= 1;
                        if (allEquals) {
                            saBillDetailsDTO.setVatRateString(vatRates.get(0).toString());
                            saBillDetailsDTO.setVatRateString(getVatRateString(vatRates.get(0)));
                        } else {
                            saBillDetailsDTO.setVatRateString("/");
                        }
                    } catch (NullPointerException e) {

                    }
                    saBillDetailsDTO.setTotalAmountString(Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                    saBillDetailsDTO.setTotalVatAmountOriginalString(Utils.formatTien(totalVatAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                    saBillDetailsDTO.setTotalAllString(Utils.formatTien(totalAll, getTypeAmount(userDTO, saBill.getCurrencyID()),userDTO));
                    saBillDetailsDTO.setTotalDiscountAmountOriginalString(Utils.formatTien(totalDiscountAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()),userDTO));
                    saBillDetailsDTO.setAmountInWordString(Utils.GetAmountInWords(totalAll, saBill.getCurrencyID() == null ? "VND" : saBill.getCurrencyID(), userDTO));
                    totalDiscountAmountOriginal = BigDecimal.ZERO;
                    totalAmountOriginal = BigDecimal.ZERO;
                    totalVatAmountOriginal = BigDecimal.ZERO;
                    vatRates = new ArrayList<>();
                }
            }
        }
        parameter.put("REPORT_MAX_COUNT", sABillDetailsDTO.size());
        JasperReport jasperReport;
        switch (typeReport) {
            case Constants.Report.HoadonGTGT:
                try {
                    if (Boolean.FALSE.equals(saBill.getIsAttachList()) || saBill.getIsAttachList() == null ||
                        TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK == typeID_SAInvoice ||
                        TypeConstant.MUA_HANG_TRA_LAI == typeID_SAInvoice ||
                        TypeConstant.HANG_GIAM_GIA == typeID_SAInvoice ||
                        fromSaInvoice) {
                        String reportName = "VATInvoice";
                        jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
                        result = ReportUtils.generateReportPDF(sABillDetailsDTO, parameter, jasperReport);
                    } else {
                        String reportName = "SaVATInvoice";
                        // tạo file báo cáo
                        jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
                        result = ReportUtils.generateReportPDF(sABillDetailsDTO, parameter, jasperReport);
                    }
                    if (Boolean.TRUE.equals(saBill.getIsAttachList())) {
                        String reportName = "VATGoodsAndServiceReport";
                        parameter.put("totalQuantitiesString", Utils.formatTien(totalQuantities, Constants.SystemOption.DDSo_SoLuong, userDTO));
                        parameter.put("totalAmountString", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                        parameter.put("totalVatAmountOriginalString", Utils.formatTien(totalVatAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                        parameter.put("totalAllString", Utils.formatTien(totalAll, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                        parameter.put("totalDiscountAmountOriginalString", Utils.formatTien(totalDiscountAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                        parameter.put("amountInWordString", Utils.GetAmountInWords(totalAll, saBill.getCurrencyID() == null ? "VND" : saBill.getCurrencyID(), userDTO));
                        parameter.put("listCommonNameInventory", saBill.getListCommonNameInventory());
                        parameter.put("REPORT_MAX_COUNT", anotherList.size());
                        jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                        result = ReportUtils.generateReportPDF(anotherList, parameter, jasperReport);
                    }

                } catch (NullPointerException e) {

                }
                break;
            case Constants.Report.HoadonBanHang:
                if (Boolean.TRUE.equals(saBill.getIsAttachList())) {
                    String reportName = "SaleGoodsAndServiceReport";
                    parameter.put("totalQuantitiesString", Utils.formatTien(totalQuantities, Constants.SystemOption.DDSo_SoLuong, userDTO));
                    parameter.put("totalAmountString", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                    parameter.put("totalAllString", Utils.formatTien(totalAll, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                    parameter.put("totalDiscountAmountOriginalString", Utils.formatTien(totalDiscountAmountOriginal, getTypeAmount(userDTO, saBill.getCurrencyID()), userDTO));
                    parameter.put("amountInWordString", Utils.GetAmountInWords(totalAll, saBill.getCurrencyID() == null ? "VND" : saBill.getCurrencyID(), userDTO));
                    parameter.put("listCommonNameInventory", saBill.getListCommonNameInventory());
                    parameter.put("REPORT_MAX_COUNT", anotherList.size());
                    jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                    result = ReportUtils.generateReportPDF(anotherList, parameter, jasperReport);
                } else {
                    String reportName = "SaleInvoice";
                    // tạo file báo cáo
                    jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                    // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
                    result = ReportUtils.generateReportPDF(sABillDetailsDTO, parameter, jasperReport);
                }
                break;
            case Constants.Report.BangKeDichVu:
                result = getGoodAndServReport(saBill, typeReport);
        }
        return result;
    }

    @Override
    public byte[] getReportExcel() {
        List<Unit> units = unitRepository.findAll();
        // Tên cột
        List<String> headers = Arrays.asList(ExcelConstant.Unit.NAME, ExcelConstant.Unit.DESCRIPTION);

        // Tên field của domain
        List<String> fieldNames = Arrays.asList(ExcelConstant.UnitField.NAME, ExcelConstant.UnitField.DESCRIPTION);

        return ExcelUtils.writeToFile(units, "Unit", headers, fieldNames);
    }

    // Convert PostedDate, Date
    private String convertDate(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
        }
    }

    /**
     * Hautv
     *
     * @param lstNoCo
     * @return số nợ, có
     */
    public String getNoCo(List<String> lstNoCo) {
        String result = "";
        for (String no : lstNoCo
        ) {
            if (StringUtils.isEmpty(result)) {
                result += no;
            } else {
                result += ", " + no;
            }
        }
        return result;
    }

    /**
     * @param accName
     * @param payerName
     * @return
     * @Author Hautv
     */
    public String getName(String accName, String payerName) {
        String result = "";
        if (!StringUtils.isEmpty(payerName)) {
            if (!StringUtils.isEmpty(accName)) {
                result += payerName + " - " + accName;
            } else {
                result += payerName;
            }
        } else {
            if (!StringUtils.isEmpty(accName)) {
                result += accName;
            }
        }
        return result;
    }

    /**
     * @param userDTO
     * @param parameter
     * @Author Hautv
     */
    private void setHeader(UserDTO userDTO, Map<String, Object> parameter) {
        String header0;
        if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport() == null) {
            userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        }
        if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting() != null) {
            if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting0)) {
                header0 = ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                    + (StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : ("<i>" + userDTO.getOrganizationUnit().getAddress() + "</i>" + "<br>"))
                    + (StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : ("<i>" + userDTO.getOrganizationUnit().gettaxCode())));
                parameter.put("header0", header0);
            } else if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting1)) {
                header0 = ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                    + (StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : ("<i>" + userDTO.getOrganizationUnit().gettaxCode())));
                parameter.put("header0", header0);
            } else {
                header0 = ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                    + (StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : ("<i>" + userDTO.getOrganizationUnit().getAddress() + "</i>" + "<br>")));
                parameter.put("header0", header0);
            }
        }
    }

    /**
     * @param userDTO
     * @param currency
     * @return
     * @Author Hautv
     */
    private String getTypeAmount(UserDTO userDTO, String currency) {
        if (userDTO.getOrganizationUnit().getCurrencyID().equals(currency)) {
            return Constants.SystemOption.DDSo_TienVND;
        } else {
            return Constants.SystemOption.DDSo_NgoaiTe;
        }
    }

    private String getTypeUnitPrice(UserDTO userDTO, String currency) {
        if (userDTO.getOrganizationUnit().getCurrencyID().equals(currency)) {
            return Constants.SystemOption.DDSo_DonGia;
        } else {
            return Constants.SystemOption.DDSo_NgoaiTe;
        }
    }

    private String getVatRateString(BigDecimal vatRate) {
        String result = "";
        if (vatRate != null) {
            switch (vatRate.intValue()) {
                case 0:
                    result = "0%";
                    break;
                case 1:
                    result = "5%";
                    break;
                case 2:
                    result = "10%";
                    break;
                case 3:
                case 4:
                    result = "/";
                    break;
            }
        } else {
            result = "";
        }
        return result;
    }

    private byte[] getChungTuKeToan(PPInvoice ppInvoice, int typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<PPInvoiceDetails> ppInvoiceDetails = new ArrayList<>();
        ppInvoiceDetails.addAll(ppInvoice.getPpInvoiceDetails());
        Collections.sort(ppInvoiceDetails, Comparator.comparingInt(PPInvoiceDetails::getOrderPriority));
        List<PPInvoiceDetails> ppInvoiceDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalAmountOriginal = BigDecimal.ZERO;
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
        if (!StringUtils.isEmpty(ppInvoice.getAccountingObjectName())) {
            parameter.put("AccountingObjectName", ppInvoice.getAccountingObjectName());
        }
        if (!StringUtils.isEmpty(ppInvoice.getAccountingObjectAddress())) {
            parameter.put("AccountingObjectAddress", ppInvoice.getAccountingObjectAddress());
        }
        if (!StringUtils.isEmpty(ppInvoice.getReason())) {
            parameter.put("Reason", ppInvoice.getReason());
        }
        parameter.put("CurrencyID", ppInvoice.getCurrencyId());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", ppInvoice.getNoMBook());
            } else {
                parameter.put("No", ppInvoice.getNoFBook());
            }
        } else {
            parameter.put("No", ppInvoice.getNoFBook());
        }
        parameter.put("Date", convertDate(ppInvoice.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(ppInvoice.getTotalAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(ppInvoice.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppInvoice.getCurrencyId();
        parameter.put("ConversionPair", conversionPair);
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            for (int i = 0; i < ppInvoiceDetails.size(); i++) {
                MaterialGoods materialGoods = materialGoodsRepository.findById(ppInvoiceDetails.get(i).getMaterialGoodsId()).get();
                ppInvoiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(ppInvoiceDetails.get(i).getAmountOriginal().subtract(ppInvoiceDetails.get(i).getDiscountAmountOriginal()), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                ppInvoiceDetails.get(i).setAmountToString(Utils.formatTien(ppInvoiceDetails.get(i).getAmount().subtract(ppInvoiceDetails.get(i).getDiscountAmount()), Constants.SystemOption.DDSo_TienVND, userDTO));
                ppInvoiceDetailsList.add(ppInvoiceDetails.get(i));
                totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getAmount().subtract(ppInvoiceDetails.get(i).getDiscountAmount()));
                totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getAmountOriginal().subtract(ppInvoiceDetails.get(i).getDiscountAmountOriginal()));
                if (ppInvoiceDetails.get(i).getImportTaxAmountOriginal().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsImportTax = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsImportTax, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getImportTaxAmount());
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getImportTaxAmountOriginal());
                    ppInvoiceDetailsImportTax.setAmountToString(Utils.formatTien(ppInvoiceDetailsImportTax.getImportTaxAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    ppInvoiceDetailsImportTax.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsImportTax.getImportTaxAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    ppInvoiceDetailsImportTax.setDescription("Tiền thuế nhập khẩu " + materialGoods.getMaterialGoodsCode());
                    ppInvoiceDetailsImportTax.setCreditAccount(ppInvoiceDetailsImportTax.getImportTaxAccount());
                    ppInvoiceDetailsList.add(ppInvoiceDetailsImportTax);
                }
                if (ppInvoiceDetails.get(i).getSpecialConsumeTaxAmount().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsSpecial = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsSpecial, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    ppInvoiceDetailsSpecial.setAmountToString(Utils.formatTien(ppInvoiceDetailsSpecial.getSpecialConsumeTaxAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    ppInvoiceDetailsSpecial.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsSpecial.getSpecialConsumeTaxAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getSpecialConsumeTaxAmount());
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getSpecialConsumeTaxAmountOriginal());
                    ppInvoiceDetailsSpecial.setDescription("Tiền thuế TTĐB " + materialGoods.getMaterialGoodsCode());
                    ppInvoiceDetailsSpecial.setCreditAccount(ppInvoiceDetailsSpecial.getSpecialConsumeTaxAccount());
                    ppInvoiceDetailsList.add(ppInvoiceDetailsSpecial);

                }
                if (ppInvoiceDetails.get(i).getVatAmount().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsVat = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsVat, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getVatAmount());
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getVatAmountOriginal());
                    ppInvoiceDetailsVat.setAmountToString(Utils.formatTien(ppInvoiceDetailsVat.getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    ppInvoiceDetailsVat.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsVat.getVatAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    ppInvoiceDetailsVat.setDescription("Tiền thuế GTGT " + materialGoods.getMaterialGoodsCode());
                    if (!ppInvoice.isImportPurchase()) {
                        ppInvoiceDetailsVat.setCreditAccount(ppInvoiceDetailsVat.getDeductionDebitAccount());
                        ppInvoiceDetailsVat.setDebitAccount(ppInvoiceDetailsVat.getVatAccount());
                    } else {
                        ppInvoiceDetailsVat.setCreditAccount(ppInvoiceDetailsVat.getVatAccount());
                        ppInvoiceDetailsVat.setDebitAccount(ppInvoiceDetailsVat.getDeductionDebitAccount());
                    }
                    ppInvoiceDetailsList.add(ppInvoiceDetailsVat);

                }
            }
            parameter.put("Total", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
            parameter.put("TotalQD", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            for (int i = 0; i < ppInvoiceDetails.size(); i++) {
                MaterialGoods materialGoods = materialGoodsRepository.findById(ppInvoiceDetails.get(i).getMaterialGoodsId()).get();
                ppInvoiceDetails.get(i).setAmountOriginalToString(Utils.formatTien(ppInvoiceDetails.get(i).getAmountOriginal().subtract(ppInvoiceDetails.get(i).getDiscountAmountOriginal()), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                ppInvoiceDetailsList.add(ppInvoiceDetails.get(i));
                totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getAmount().subtract(ppInvoiceDetails.get(i).getDiscountAmount()));
                totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getAmountOriginal().subtract(ppInvoiceDetails.get(i).getDiscountAmountOriginal()));
                if (ppInvoiceDetails.get(i).getImportTaxAmountOriginal().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsImportTax = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsImportTax, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getImportTaxAmount());
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getImportTaxAmountOriginal());
                    ppInvoiceDetailsImportTax.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsImportTax.getImportTaxAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    ppInvoiceDetailsImportTax.setDescription("Tiền thuế nhập khẩu " + materialGoods.getMaterialGoodsCode());
                    ppInvoiceDetailsImportTax.setCreditAccount(ppInvoiceDetailsImportTax.getImportTaxAccount());
                    ppInvoiceDetailsList.add(ppInvoiceDetailsImportTax);
                }
                if (ppInvoiceDetails.get(i).getSpecialConsumeTaxAmount().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsSpecial = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsSpecial, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getSpecialConsumeTaxAmount());
                    ppInvoiceDetailsSpecial.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsSpecial.getSpecialConsumeTaxAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getSpecialConsumeTaxAmountOriginal());
                    ppInvoiceDetailsSpecial.setDescription("Tiền thuế TTĐB " + materialGoods.getMaterialGoodsCode());
                    ppInvoiceDetailsSpecial.setCreditAccount(ppInvoiceDetailsSpecial.getSpecialConsumeTaxAccount());
                    ppInvoiceDetailsList.add(ppInvoiceDetailsSpecial);
                }
                if (ppInvoiceDetails.get(i).getVatAmount().floatValue() > 0) {
                    PPInvoiceDetails ppInvoiceDetailsVat = new PPInvoiceDetails();
                    try {
                        BeanUtils.copyProperties(ppInvoiceDetailsVat, ppInvoiceDetails.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    totalAmount = totalAmount.add(ppInvoiceDetails.get(i).getVatAmount());
                    totalAmountOriginal = totalAmountOriginal.add(ppInvoiceDetails.get(i).getVatAmountOriginal());
                    ppInvoiceDetailsVat.setAmountOriginalToString(Utils.formatTien(ppInvoiceDetailsVat.getVatAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                    ppInvoiceDetailsVat.setDescription("Tiền thuế GTGT " + materialGoods.getMaterialGoodsCode());
                    if (!ppInvoice.isImportPurchase()) {
                        ppInvoiceDetailsVat.setCreditAccount(ppInvoiceDetailsVat.getDeductionDebitAccount());
                        ppInvoiceDetailsVat.setDebitAccount(ppInvoiceDetailsVat.getVatAccount());
                    } else {
                        ppInvoiceDetailsVat.setCreditAccount(ppInvoiceDetailsVat.getVatAccount());
                        ppInvoiceDetailsVat.setDebitAccount(ppInvoiceDetailsVat.getDeductionDebitAccount());
                    }
                    ppInvoiceDetailsList.add(ppInvoiceDetailsVat);
                }
            }
                parameter.put("Total", Utils.formatTien(totalAmountOriginal, getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
        }
        parameter.put("TotalAmount", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, ppInvoice.getCurrencyId() == null ? "VND" : ppInvoice.getCurrencyId(), userDTO));
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppInvoice.getCurrencyId()));
        parameter.put("REPORT_MAX_COUNT", ppInvoiceDetailsList.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(ppInvoiceDetailsList, parameter, jasperReport);
        return result;
    }

    private byte[] getPhieuNhapKho(PPInvoice ppInvoice, int typeReports) throws JRException {
        byte[] result = null;
        String reportName = "";
        if (ppInvoice != null) {
            UserDTO userDTO = userService.getAccount();
            List<PPInvoiceDetailDTO> details = ppInvoiceRepository.getPPInvoiceDetailByIdPPInvoice(ppInvoice.getId());
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            String creditAccount = "";
            String debitAccount = "";
            String repository = "";
            HashMap<String, String> creditAccountMap = new HashMap<String, String>();
            HashMap<String, String> debitAccountMap = new HashMap<String, String>();
            HashMap<String, String> repositoryMap = new HashMap<String, String>();
            BigDecimal total = BigDecimal.ZERO;
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isQuanSoTri = currentBook.equalsIgnoreCase("1");
            Optional<RSInwardOutward> rsInwardOutward = rsInwardOutwardRepository.findById(ppInvoice.getRsInwardOutwardId());
            for (int i = 0; i < details.size(); i++) {
                if (!Strings.isNullOrEmpty(details.get(i).getCreditAccount())) {
                    if (creditAccountMap.size() <= 0 || StringUtils.isEmpty(creditAccountMap.get(details.get(i).getCreditAccount()))) {
                        creditAccountMap.put(details.get(i).getCreditAccount(), details.get(i).getCreditAccount());
                        creditAccount += details.get(i).getCreditAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getDebitAccount())) {
                    if (debitAccountMap.size() <= 0 || StringUtils.isEmpty(debitAccountMap.get(details.get(i).getDebitAccount()))) {
                        debitAccountMap.put(details.get(i).getDebitAccount(), details.get(i).getDebitAccount());
                        debitAccount += details.get(i).getDebitAccount() + ", ";
                    }
                }
                if (!Strings.isNullOrEmpty(details.get(i).getRepositoryCode())) {
                    if (repositoryMap.size() <= 0 || StringUtils.isEmpty(repositoryMap.get(details.get(i).getRepositoryCode()))) {
                        repositoryMap.put(details.get(i).getRepositoryCode(), details.get(i).getRepositoryCode());
                        repository += details.get(i).getRepositoryName() + ", ";
                    }
                }
                total = total.add(details.get(i).getInwardAmount());
                details.get(i).setQuantityToString(Utils.formatTien(details.get(i).getQuantity(), Constants.SystemOption.DDSo_TienVND, userDTO));
                details.get(i).setAmountOriginalToString(Utils.formatTien(details.get(i).getInwardAmount(), getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
                if (details.get(i).getQuantity().doubleValue() == 0) {
                    details.get(i).setUnitPriceOriginalToString(Utils.formatTien(BigDecimal.ZERO, Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    BigDecimal div = details.get(i).getInwardAmount().divide(details.get(i).getQuantity(), Utils.DDSo_DonGia(userDTO), RoundingMode.HALF_UP);
                    details.get(i).setUnitPriceOriginalToString(Utils.formatTien(div, Constants.SystemOption.DDSo_DonGia, userDTO));
                }
            }
            total.setScale(Utils.DDSo_DonGia(userDTO), RoundingMode.HALF_UP);
            if (!Strings.isNullOrEmpty(creditAccount)) {
                creditAccount = creditAccount.substring(0, (creditAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(debitAccount)) {
                debitAccount = debitAccount.substring(0, debitAccount.length() - 2);
            }
            if (!Strings.isNullOrEmpty(repository)) {
                repository = repository.substring(0, repository.length() - 2);
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            reportName = "phieu_nhap_kho";
            if (typeReports == 6) {
                reportName = "phieu_nhap_kho_A5";
            }
            // khai báo key
            setHeader(userDTO, parameter);
            parameter.put("REPORT_MAX_COUNT", details.size());
            parameter.put("taxCode", userDTO.getOrganizationUnit().gettaxCode());
            parameter.put("day", String.valueOf(ppInvoice.getDate().getDayOfMonth()));
            parameter.put("month", String.valueOf(ppInvoice.getDate().getMonth().getValue()));
            parameter.put("year", String.valueOf(ppInvoice.getDate().getYear()));
            parameter.put("creditAccount", creditAccount.toString());
            parameter.put("debitAccount", debitAccount.toString());
            parameter.put("repository", repository.toString());
            if (isQuanSoTri && rsInwardOutward.isPresent()) {
                parameter.put("noBook", rsInwardOutward.get().getNoMBook());
            } else {
                parameter.put("noBook", rsInwardOutward.get().getNoFBook());
            }
            if (!StringUtils.isEmpty(ppInvoice.getContactName())) {
                parameter.put("contactName", ppInvoice.getContactName());
                parameter.put("accountingObjectContact", ppInvoice.getContactName() + " - " + ppInvoice.getAccountingObjectName());
            }
            parameter.put("contactName", ppInvoice.getContactName());
            parameter.put("accountingObjectAddress", ppInvoice.getAccountingObjectAddress());
            parameter.put("description", ppInvoice.getReason());
            parameter.put("numberAttach", ppInvoice.getNumberAttach());
            parameter.put("total", Utils.formatTien(total, getTypeAmount(userDTO, ppInvoice.getCurrencyId()), userDTO));
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("reporter", userDTO.getFullName());
            } else {
                parameter.put("reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("stocker", organizationUnitOptionReport.getStocker());
            parameter.put("director", organizationUnitOptionReport.getDirector());
            parameter.put("chiefAccountant", organizationUnitOptionReport.getChiefAccountant());
            parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.getDisplayNameInReport());
            // số tiền bằng chữ
            parameter.put("amountInWord", Utils.GetAmountInWords(total, organizationUnit.get().getCurrencyID() == null ? "VND" : organizationUnit.get().getCurrencyID(), userDTO));
            // tạo file báo cáo
            jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
            result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
            return result;
        }
        return null;
    }

    private byte[] getReportPPInvoice(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        Optional<PPInvoice> ppInvoiceOptional = ppInvoiceRepository.findById(id);
        PPInvoiceDTO ppInvoiceDTO = ppInvoiceRepository.getPPInvoiceById(id);
        if (ppInvoiceOptional.isPresent()) {
            PPInvoice ppInvoice = ppInvoiceOptional.get();
            UserDTO userDTO = userService.getAccount();
            Map<String, Object> parameter = new HashMap<>();
            JasperReport jasperReport;
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            StringBuilder creditAccount = new StringBuilder();
            StringBuilder debitAccount = new StringBuilder();
            StringBuilder repository = new StringBuilder();

            if (!Strings.isNullOrEmpty(creditAccount.toString())) {
                creditAccount = new StringBuilder(creditAccount.substring(0, (creditAccount.length() - 2)));
            }
            if (!Strings.isNullOrEmpty(debitAccount.toString())) {
                debitAccount = new StringBuilder(debitAccount.substring(0, debitAccount.length() - 2));
            }
            if (!Strings.isNullOrEmpty(repository.toString())) {
                repository = new StringBuilder(repository.substring(0, repository.length() - 2));
            }
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            switch (typeReport) {
                case 5:
                case 7:
                    reportName = (typeReport == 5) ? "mCPayment" : "mCPayment-A5";
                    List<PPInvoiceDetailDTO> details = ppInvoiceRepository.getPPInvoiceDetailByIdPPInvoice(id);
                    for (PPInvoiceDetailDTO detail : details) {
                        if (!Strings.isNullOrEmpty(detail.getCreditAccount())) {
                            creditAccount.append(detail.getCreditAccount()).append(", ");
                        }
                        if (!Strings.isNullOrEmpty(detail.getDebitAccount())) {
                            debitAccount.append(detail.getDebitAccount()).append(", ");
                        }
                        if (!Strings.isNullOrEmpty(detail.getRepositoryCode())) {
                            repository.append(detail.getRepositoryCode()).append(", ");
                        }
//                        detail.setAmountToString(Utils.formatTien(detail.getAmount(), Constants.SystemOption.DDSo_TienVND));
//                        detail.setAmountOriginalToString(Utils.formatTien(detail.getAmountOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId())));
//                        detail.setUnitPriceOriginalToString(Utils.formatTien(detail.getUnitPriceOriginal(), getTypeAmount(userDTO, ppInvoice.getCurrencyId())));
                        detail.setAmountOriginalString(Utils.formatTien(detail.getAmountOriginal(), getTypeAmount(userDTO, ppInvoiceDTO.getCurrencyId()), userDTO));
                        detail.setAmountString(Utils.formatTien(detail.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        if (detail.getVatAmountOriginal().floatValue() > 0) {
                            detail.setAmountOriginalString(Utils.formatTien(detail.getVatAmountOriginal(), getTypeAmount(userDTO, ppInvoiceDTO.getCurrencyId()), userDTO));
                            detail.setAmountString(Utils.formatTien(detail.getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        } else if (detail.getDiscountAmount().floatValue() > 0) {
                            detail.setAmountOriginalString(Utils.formatTien(detail.getDiscountAmountOriginal(), getTypeAmount(userDTO, ppInvoiceDTO.getCurrencyId()), userDTO));
                            detail.setAmountString(Utils.formatTien(detail.getDiscountAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        }
                    }
                    // khai báo key
                    userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
                    /*Header*/
                    setHeader(userDTO, parameter);
                    /*Header*/
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getAccountingObjectName())) {
                        parameter.put("NguoiNhan", ppInvoiceDTO.getAccountingObjectName());
                    }
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getAccountingObjectAddress())) {
                        parameter.put("DiaChiNgNop", ppInvoiceDTO.getAccountingObjectAddress());
                    }
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getOtherReason())) {
                        parameter.put("LyDoNop", ppInvoiceDTO.getOtherReason());
                    }
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getNumberAttach())) {
                        parameter.put("KemTheo", ppInvoiceDTO.getNumberAttach());
                    }
                    BigDecimal amountOriginal;
                    BigDecimal amountExchange;
                    BigDecimal totalDiscountAmountOriginal = ppInvoice.getTotalDiscountAmountOriginal() != null ? ppInvoice.getTotalDiscountAmountOriginal() : BigDecimal.ZERO;
                    BigDecimal totalDiscountAmount = ppInvoice.getTotalDiscountAmount() != null ? ppInvoice.getTotalDiscountAmount() : BigDecimal.ZERO;
                    amountOriginal = ppInvoice.getTotalAmountOriginal().subtract(totalDiscountAmountOriginal);
                    amountExchange = ppInvoice.getTotalAmount().subtract(totalDiscountAmount);
                    if (!ppInvoice.isImportPurchase()) {
                        BigDecimal totalVatAmountOriginal = ppInvoice.getTotalVATAmountOriginal() != null ? ppInvoice.getTotalVATAmountOriginal() : BigDecimal.ZERO;
                        BigDecimal totalVatAmount = ppInvoice.getTotalVATAmount() != null ? ppInvoice.getTotalVATAmount() : BigDecimal.ZERO;
                        amountOriginal = amountOriginal.add(totalVatAmountOriginal);
                        amountExchange = amountExchange.add(totalVatAmount);
                    }
                    parameter.put("SoTien", Utils.formatTien(amountOriginal, getTypeAmount(userDTO, ppInvoiceDTO.getCurrencyId()), userDTO));
                    parameter.put("SoTien_String", Utils.GetAmountInWords(ppInvoiceDTO.getTotalInwardAmount(), ppInvoiceDTO.getCurrencyId() == null ? "VND" : ppInvoiceDTO.getCurrencyId(), userDTO));
                    parameter.put("TyGiaNT", ppInvoiceDTO.getExchangeRate() == null ? "1" : Utils.formatTien(ppInvoiceDTO.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
                    parameter.put("TienQuyDoi", Utils.formatTien(amountExchange, Constants.SystemOption.DDSo_TienVND, userDTO));
                    if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                        parameter.put("So", ppInvoiceDTO.getNoFBook());
                    } else {
                        parameter.put("So", ppInvoiceDTO.getNoMBook());
                    }
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getOtherNoFBook())) {
                        parameter.put("So", ppInvoiceDTO.getOtherNoFBook());
                    }
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                        parameter.put("GiamDoc", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                        parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                        parameter.put("NguoiNhanTien", ppInvoiceDTO.getAccountingObjectName());
                        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                            parameter.put("NguoiLapBieu", userDTO.getFullName());
                        } else {
                            parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                        }
                        parameter.put("ThuQuy", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getTreasurer());
                    }
                    List<String> lstCo = new ArrayList<>();
                    List<String> lstNo = new ArrayList<>();
                    for (PPInvoiceDetailDTO detail : details) {
                        if (!lstCo.contains(detail.getCreditAccount())) {
                            lstCo.add(detail.getCreditAccount());
                        }
                        if (!lstNo.contains(detail.getDebitAccount())) {
                            lstNo.add(detail.getDebitAccount());
                        }
                    }
                    if (organizationUnitOptionReport.getHeaderSetting() == 0) {
                        parameter.put("CompanyInfoName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyInfoAddress", organizationUnit.get().getAddress());
                        parameter.put("TaxCode", organizationUnit.get().gettaxCode());
                    } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
                        parameter.put("CompanyInfoName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyInfoAddress", organizationUnit.get().gettaxCode());
                    } else {
                        parameter.put("CompanyInfoName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyInfoAddress", organizationUnit.get().getAddress());
                    }
                    parameter.put("No", getNoCo(lstNo));
                    parameter.put("Co", getNoCo(lstCo));
                    parameter.put("currencyID", ppInvoiceDTO.getCurrencyId());
                    parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(ppInvoiceDTO.getCurrencyId()));
                    parameter.put("Ngay", "Ngày " + ppInvoiceDTO.getDate().getDayOfMonth() + " tháng " + ppInvoiceDTO.getDate().getMonthValue() + " năm " + ppInvoiceDTO.getDate().getYear());
                    // tạo file báo cáo
                    jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                    // khai báo list detail
                    List<ExampleDTO> examples = new ArrayList<>();
                    examples.add(new ExampleDTO("dataX", "dataX1"));
                    jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                    // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
                    result = ReportUtils.generateReportPDF(examples, parameter, jasperReport);
                    break;
                case 4:
                    reportName = "GiayBaoNo";
//                    parameter.put("AccountingObjectName", (ppInvoiceDTO.getContactName() != null ? ppInvoiceDTO.getContactName() + " - " : "") + (ppInvoiceDTO.getAccountingObjectName() != null ? ppInvoiceDTO.getAccountingObjectName() : ""));
                    if (ppInvoiceDTO.getContactName() != null && ppInvoiceDTO.getAccountingObjectName() != null) {
                        parameter.put("AccountingObjectName", ppInvoiceDTO.getContactName() + " - " + ppInvoiceDTO.getAccountingObjectName());
                    } else if (ppInvoiceDTO.getContactName() != null) {
                        parameter.put("AccountingObjectName", ppInvoiceDTO.getContactName());
                    } else if (ppInvoiceDTO.getAccountingObjectName() != null) {
                        parameter.put("AccountingObjectName", ppInvoiceDTO.getAccountingObjectName());
                    }
                    parameter.put("AccountingObjectAddress", ppInvoiceDTO.getAccountingObjectAddress());
                    parameter.put("CurrencyID", ppInvoiceDTO.getCurrencyId());
                    parameter.put("No", Utils.PhienSoLamViec(userDTO).equals(1) ? ppInvoiceDTO.getNoMBook() : ppInvoiceDTO.getNoFBook());
                    if (!StringUtils.isEmpty(ppInvoiceDTO.getOtherNoFBook())) {
                        parameter.put("No", ppInvoiceDTO.getOtherNoFBook());
                    }
//                    parameter.put("No", ppInvoiceDTO.getNoFBook() != null ? ppInvoiceDTO.getNoFBook() : ppInvoiceDTO.getNoMBook());
                    parameter.put("PostedDate", convertDate(ppInvoiceDTO.getPostedDate()));
                    String formatNguyente = ppInvoice.getCurrencyId().equals(organizationUnit.get().getCurrencyID()) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
                    BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                    List<PPInvoiceDetail1DTO> ppInvoiceDetail1DTOs = this.ppInvoiceRepository.getPPInvoiceDetail1ByPPInvoiceId(id);
                    for (PPInvoiceDetail1DTO pp : ppInvoiceDetail1DTOs) {
                        pp.setAmountOriginalString(Utils.formatTien(pp.getAmountOriginal(), getTypeAmount(userDTO, ppInvoiceDTO.getCurrencyId()), userDTO));
                        pp.setAmountString(Utils.formatTien(pp.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                        totalAmountOriginal = totalAmountOriginal.add(pp.getAmountOriginal());
                    }
//                    parameter.put("TotalAmountOriginal", Utils.formatTien(ppInvoiceDTO.getTotalAmountOriginal(), Constants.SystemOption.DDSo_NgoaiTe));
//                    parameter.put("TotalAmount", Utils.formatTien(ppInvoiceDTO.getTotalAmount(), Constants.SystemOption.DDSo_DonGiaNT));
                    parameter.put("AmountOriginal", Utils.formatTien(totalAmountOriginal, formatNguyente, userDTO));
//                    parameter.put("Amount", Utils.formatTien(ppInvoiceDTO.getTotalAmount(), Constants.SystemOption.DDSo_NgoaiTe));
                    parameter.put("BankName", ppInvoiceDTO.getAccountPaymentName());
                    if (ppInvoiceDTO.getAccountPaymentId() != null) {
                        Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findById(ppInvoiceDTO.getAccountPaymentId());
                        parameter.put("BankAccount", bankAccountDetails.get().getBankAccount());
                    }
                    parameter.put("Reason", ppInvoiceDTO.getOtherReason());
                    parameter.put("Manager", "Manager");
//                    parameter.put("Total", Utils.formatTien(ppInvoiceDTO.getTotalAmountOriginal(), Constants.SystemOption.DDSo_NgoaiTe));
                    parameter.put("AmountInWord", Utils.GetAmountInWords(totalAmountOriginal, ppInvoiceDTO.getCurrencyId() == null ? "VND" : ppInvoiceDTO.getCurrencyId(), userDTO));
//                conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + ppInvoiceDTO.getCurrencyId();
//                parameter.put("ConversionPair", conversionPair);
                    Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
                    if (ppInvoiceDTO.getCurrencyId().equals(organizationUnit.get().getCurrencyID())) {
                        parameter.put("isShowCurrency", false);
                    } else {
                        parameter.put("isShowCurrency", true);
                        String currencyAmountOriginal = "Số tiền nguyên tệ (" + ppInvoice.getCurrencyId() + ")";
                        String currencyAmount = "Số tiền (" + organizationUnit.get().getCurrencyID() + ")";
                        parameter.put("CurrencyOriginalInWord", currencyAmountOriginal);
                        parameter.put("CurrencyInWord", currencyAmount);
                        parameter.put("REPORT_MAX_COUNT", ppInvoiceDetail1DTOs.size());
                        parameter.put("ExchangeRate", Utils.formatTien(ppInvoiceDTO.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));

                    }
                    userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
                    /*Header*/
                    setHeader(userDTO, parameter);
                    if (organizationUnitOptionReport.getHeaderSetting() == 0) {
                        parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyAddress", organizationUnit.get().getAddress());
                        parameter.put("TaxCode", organizationUnit.get().gettaxCode());
                    } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
                        parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyAddress", organizationUnit.get().gettaxCode());
                    } else {
                        parameter.put("CompanyName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
                        parameter.put("CompanyAddress", organizationUnit.get().getAddress());
                    }
                    parameter.put("REPORT_MAX_COUNT", ppInvoiceDetail1DTOs.size());
                    parameter.put("IsDisplayNameInReport", organizationUnitOptionReport.isIsDisplayNameInReport());
//                    OrganizationUnitOptionReport ogr = organizationUnitOptionReport;
                    if (Boolean.TRUE.equals(organizationUnitOptionReport.isIsDisplayNameInReport())) {
                        parameter.put("Director", organizationUnitOptionReport.getDirector());
                        parameter.put("ChiefAccountant", organizationUnitOptionReport.getChiefAccountant());
                        if (Boolean.TRUE.equals(organizationUnitOptionReport.getDisplayAccount())) {
                            parameter.put("Reporter", userDTO.getFullName());
                        } else {
                            parameter.put("Reporter", organizationUnitOptionReport.getReporter());
                        }
                        parameter.put("Treasurer", organizationUnitOptionReport.getTreasurer());
                    }
                    // tạo file báo cáo
                    jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
                    jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
                    result = ReportUtils.generateReportPDF(ppInvoiceDetail1DTOs, parameter, jasperReport);
                    break;
                case 3:
                case 2:
                    result = getChungTuKeToan(ppInvoice, typeReport);
                    break;
                case 6:
                case 1:
                    result = getPhieuNhapKho(ppInvoice, typeReport);
                    break;
            }
            return result;
        }
        return null;
    }

    private byte[] getReportGOtherVoucher(UUID id, Integer typeReport) throws JRException {
        byte[] result = null;
        Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(id);
        if (!gOtherVoucherOptional.isPresent()) {
            throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
        }
        GOtherVoucher gOtherVoucher = gOtherVoucherOptional.get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
                result = getChungTuKeToan(gOtherVoucher, typeReport);
                break;
        }
        return result;
    }

    private byte[] getChungTuKeToan(GOtherVoucher gOtherVoucher, Integer typeReport) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String reportName = "ChungTuKeToan";
        if (typeReport == Constants.Report.ChungTuKeToanQuyDoi) {
            reportName = "ChungTuKeToanQuyDoi";
        }
        List<GOtherVoucherDetails> gOtherVoucherDetails = new ArrayList<>(gOtherVoucher.getgOtherVoucherDetails());
        gOtherVoucherDetails.sort(Comparator.comparingInt(GOtherVoucherDetails::getOrderPriority));
        boolean isNoMBook = false;
        boolean isSDSoQuanTri = false;
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect1 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            List<vn.softdreams.ebweb.domain.SystemOption> collect2 = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).collect(Collectors.toList());
            isNoMBook = !collect1.isEmpty() && collect1.get(0).getData().equalsIgnoreCase("1");
            isSDSoQuanTri = !collect2.isEmpty() && collect2.get(0).getData().equalsIgnoreCase("1");
        }
        Map<String, Object> parameter = new HashMap<>();
        /*Header*/
        setHeader(userDTO, parameter);
        /*Header*/
//        if (!StringUtils.isEmpty(gOtherVoucher.)) {
//            parameter.put("AccountingObjectName", ppInvoice.getAccountingObjectName());
//        }
//        if (!StringUtils.isEmpty(gOtherVoucher.)) {
//            parameter.put("AccountingObjectAddress", ppInvoice.getAccountingObjectAddress());
//        }
        if (!StringUtils.isEmpty(gOtherVoucher.getReason())) {
            parameter.put("Reason", gOtherVoucher.getReason());
        }
        parameter.put("CurrencyID", gOtherVoucher.getCurrencyID());
        if (Boolean.TRUE.equals(isSDSoQuanTri)) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                parameter.put("No", gOtherVoucher.getNoMBook());
            } else {
                parameter.put("No", gOtherVoucher.getNoFBook());
            }
        } else {
            parameter.put("No", gOtherVoucher.getNoFBook());
        }
        parameter.put("Date", convertDate(gOtherVoucher.getPostedDate()));
        parameter.put("TotalAmountOriginal", Utils.formatTien(gOtherVoucher.getTotalAmountOriginal(), getTypeAmount(userDTO, gOtherVoucher.getCurrencyID()), userDTO));
        parameter.put("TotalAmount", Utils.formatTien(gOtherVoucher.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("ExchangeRate", Utils.formatTien(gOtherVoucher.getExchangeRate(), Constants.SystemOption.DDSo_TyGia, userDTO));
        String conversionPair = ' ' + userDTO.getOrganizationUnit().getCurrencyID() + '/' + gOtherVoucher.getCurrencyID();
        parameter.put("ConversionPair", conversionPair);
        parameter.put("AmountInWord", Utils.GetAmountInWords(gOtherVoucher.getTotalAmountOriginal(), gOtherVoucher.getCurrencyID() == null ? "VND" : gOtherVoucher.getCurrencyID(), userDTO));
        for (int i = 0; i < gOtherVoucherDetails.size(); i++) {
            gOtherVoucherDetails.get(i).setAmountOriginalToString(Utils.formatTien(gOtherVoucherDetails.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        parameter.put("Total", Utils.formatTien(gOtherVoucher.getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("isForeignCurrency", userDTO.getOrganizationUnit().getCurrencyID().equals(gOtherVoucher.getCurrencyID()));
        parameter.put("REPORT_MAX_COUNT", gOtherVoucherDetails.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        // tạo file báo cáo
        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(gOtherVoucherDetails, parameter, jasperReport);
        return result;
    }

    private byte[] getReportRSOutWard(UUID id, int typeReport) throws JRException {
        byte[] result = null;
        Optional<RSInwardOutward> rsInwardOutwardOptional = rsInwardOutwardRepository.findById(id);
        if (!rsInwardOutwardOptional.isPresent()) {
            throw new BadRequestAlertException("A PPService cannot already have an ID", "PPSERVICE", "idexists");
        }
        RSInwardOutward rsInwardOutward = rsInwardOutwardOptional.get();
        switch (typeReport) {
            case Constants.Report.ChungTuKeToan:
            case Constants.Report.ChungTuKeToanQuyDoi:
                result = getChungTuKeToan(rsInwardOutward, typeReport);
                break;
            case Constants.Report.PhieuXuatKho:
            case Constants.Report.PhieuXuatKhoA5:
                result = getXuatKho(rsInwardOutward, typeReport);
                break;
        }
        return result;
    }

    private byte[] getReportRSTransfer(UUID id, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        byte[] result = null;
        RSTransfer rsTransfer = rsTransferRepository.findById(id).get();
        switch (typeReport) {
            case 1:
                result = getChungTuKeToan(rsTransfer, typeReport);
                break;
            case 2:
                result = getPhieuXuatKho(rsTransfer, typeReport);
                break;
            case 3:
                result = getPhieuXuatKhoA5(rsTransfer, typeReport);
                break;
            case 4:
                result = getPhieuNhapKho(rsTransfer, typeReport);
                break;
            case 5:
                result = getPhieuNhapKhoA5(rsTransfer, typeReport);
                break;
        }
        return result;
    }

    private void checkSaBillList(List<SABillReportDTO> saBillReportDTO) {
        int count = (saBillReportDTO.size() % 10);
        for (int i = 0; i < (10 - count); i++) {
            SABillReportDTO saBillReportDTO1 = new SABillReportDTO();
            saBillReportDTO1.setVatRate(BigDecimal.valueOf(20));
            saBillReportDTO.add(saBillReportDTO1);
        }
    }

    private void checkEbPackage(UserDTO userDTO, Map<String, Object> parameter) {
        File currentDirectory = new File(new File("").getAbsolutePath());
        if (userDTO.getEbPackage() != null) {
            Boolean isCheckDemo = userDTO.getEbPackage().getPackageCode() != null && userDTO.getEbPackage().getPackageCode().contains("DEMO");
            if (Boolean.TRUE.equals(isCheckDemo)) {
                parameter.put("pathDemo",currentDirectory + "/report/demo.png");
                parameter.put("isCheckDemo", true);
            } else {
                parameter.put("isCheckDemo", false);
            }
        } else {
            parameter.put("pathDemo",currentDirectory + "/report/demo.png");
            parameter.put("isCheckDemo", true);
        }
    }


    private byte[] getListTIDecrement(UUID id , int typeReport) throws JRException {
        byte[] result = null;
        String reportName = "GhiGiam_CCDC";
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        Map<String, Object> parameter = new HashMap<>();
        // khai báo key
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        // lấy dữ liệu từ frontend
        TIDecrement tiDecrement = tiDecrementRepository.findById(id).get();

        // fix cứng dữ liệu dưới backend để test
//        Optional<TIDecrement> tiDecrement = tiDecrementRepository.findByIdqq();
        List<TIDecrementDTO> listTiDecrementDTO = tiDecrementRepository.getDetailsTIDecrementDTO(tiDecrement.getId());
        setHeader(userDTO, parameter);
        if (organizationUnitOptionReport.getHeaderSetting() == 0) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
        }
        parameter.put("REPORT_MAX_COUNT", listTiDecrementDTO.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }
        parameter.put("Date", convertDate(tiDecrement.getDate()));
        parameter.put("Description", tiDecrement.getReason());
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.MANAGEMENT_BOOK)) {
            parameter.put("No", tiDecrement.getNoFBook());
        } else {
            parameter.put("No", tiDecrement.getNoMBook());
        }


        checkEbPackage(userDTO, parameter);

        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalDecrementQuantity = BigDecimal.ZERO;
        BigDecimal totalRemaniningDecrementAmount = BigDecimal.ZERO;
        for (int i = 0; i < listTiDecrementDTO.size(); i++) {
            totalQuantity = totalQuantity.add(listTiDecrementDTO.get(i).getQuantity());
            totalDecrementQuantity = totalDecrementQuantity.add(listTiDecrementDTO.get(i).getDecrementQuantity());
            totalRemaniningDecrementAmount = totalRemaniningDecrementAmount.add(listTiDecrementDTO.get(i).getRemainingDecrementAmount());
        }
        parameter.put("TotalQuantity", Utils.formatTien(totalQuantity, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalDecrementQuantity", Utils.formatTien(totalDecrementQuantity, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalRemaniningDecrementAmount", Utils.formatTien(totalRemaniningDecrementAmount, Constants.SystemOption.DDSo_TienVND, userDTO));

        parameter.put("IsDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            if(Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().isIsDisplayAccount())){
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            }else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }

        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(listTiDecrementDTO, parameter, jasperReport);
        return result;

    }
    private byte[] getLitsTIAdjustment(UUID id , int typeReport ) throws JRException {
        byte[] result = null;
        String reportName = "DieuChinh_CCDC";
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        Map<String, Object> parameter = new HashMap<>();
        // khai báo key
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        //lấy dữu liệu từ frontend vào
        TIAdjustment tiAdjustment = tiAdjustmentRepository.findById(id).get();

        //fix cứng dữ liệu dưới backend để lấy dữu liệu test
//        Optional<TIAdjustment> tiAdjustment = tiAdjustmentRepository.findByTIAdjustment();

        List<TIAdjustmentDTO> listTIAdjustmentDTO = tiAdjustmentRepository.getListTIAdjustmentDTO(tiAdjustment.getId());
        //check điều kiện cho header
        setHeader(userDTO, parameter);
        if (organizationUnitOptionReport.getHeaderSetting() == 0) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
        }

        parameter.put("REPORT_MAX_COUNT", listTIAdjustmentDTO.size());
        parameter.put("Date", convertDate(tiAdjustment.getDate()));
        parameter.put("Description", tiAdjustment.getReason());
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.MANAGEMENT_BOOK)) {
            parameter.put("No", tiAdjustment.getNoFBook());
        } else {
            parameter.put("No", tiAdjustment.getNoMBook());
        }
        checkEbPackage(userDTO, parameter);

        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalRemainingAmount = BigDecimal.ZERO;
        BigDecimal totalNewRemainingAmount = BigDecimal.ZERO;
        BigDecimal totalDiffRemainingAmount = BigDecimal.ZERO;
        Integer totalRemainAllocationTimes = 0;
        Integer totalNewRemainingAllocationTime = 0;
        Integer totalDifferAllocationTime = 0;

        for (int i = 0 ; i < listTIAdjustmentDTO.size() ; i++) {
            totalQuantity = totalQuantity.add(listTIAdjustmentDTO.get(i).getQuantity());
            totalRemainingAmount = totalRemainingAmount.add(listTIAdjustmentDTO.get(i).getRemainingAmount());
            totalNewRemainingAmount = totalNewRemainingAmount.add(listTIAdjustmentDTO.get(i).getNewRemainingAmount());
            totalDiffRemainingAmount = totalDiffRemainingAmount.add(listTIAdjustmentDTO.get(i).getDiffRemainingAmount());
            totalRemainAllocationTimes += listTIAdjustmentDTO.get(i).getRemainAllocationTimes();
            totalNewRemainingAllocationTime += listTIAdjustmentDTO.get(i).getNewRemainingAllocationTime();
            totalDifferAllocationTime += listTIAdjustmentDTO.get(i).getDifferAllocationTime();
        }

        parameter.put("TotalQuantity" , Utils.formatTien(totalQuantity, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalRemainingAmount" ,   Utils.formatTien(totalRemainingAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalNewRemainingAmount" ,  Utils.formatTien(totalNewRemainingAmount, Constants.SystemOption.DDSo_TienVND, userDTO) );
        parameter.put("TotalDiffRemainingAmount" ,   Utils.formatTien(totalDiffRemainingAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalRemainAllocationTimes" , totalRemainAllocationTimes);
        parameter.put("TotalNewRemainingAllocationTime" , totalNewRemainingAllocationTime);
        parameter.put("TotalDifferAllocationTime" , totalDifferAllocationTime);


        parameter.put("IsDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            if(Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().isIsDisplayAccount())){
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            }else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }

        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(listTIAdjustmentDTO, parameter, jasperReport);
        return result;
    }
    private byte[] getListTITransfer(UUID id , int typeReport)  throws JRException{
        byte[] result = null;
        String reportName = "DieuChuyen_CCDC";
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        Map<String, Object> parameter = new HashMap<>();
        // khai báo key
        UserDTO userDTO = userService.getAccount();
        userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        //lấy dữu liệu từ frontend vào
        TITransfer tiTransfer = tiTransferRepository.findById(id).get();

        //lấy dữ liệu fix cứng thử tạm thơi từ db ra
//        Optional<TITransfer> tiTransfer = tiTransferRepository.findByTITransfer();

        List<TITransferDTO> listTITransferDTO = tiTransferRepository.getDetailsTITransferDTO(tiTransfer.getId());
        //check điều kiện cho header
        setHeader(userDTO, parameter);
        if (organizationUnitOptionReport.getHeaderSetting() == 0) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else if (organizationUnitOptionReport.getHeaderSetting() == 1) {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("TaxCode", organizationUnit.get().gettaxCode());
        } else {
            parameter.put("OrganizationUnitName", organizationUnit.get().getOrganizationUnitName().toUpperCase());
            parameter.put("Address", organizationUnit.get().getAddress());
        }
        parameter.put("REPORT_MAX_COUNT", listTITransferDTO.size());
        parameter.put("Date", convertDate(tiTransfer.getDate()));
        parameter.put("Description", tiTransfer.getReason());
        if (Utils.PhienSoLamViec(userDTO).equals(Constants.TypeLedger.MANAGEMENT_BOOK)) {
            parameter.put("No", tiTransfer.getNoFBook());
        } else {
            parameter.put("No", tiTransfer.getNoMBook());
        }
        parameter.put("Transferor" , tiTransfer.getTransferor());
        parameter.put("Receiver" , tiTransfer.getReceiver());


        checkEbPackage(userDTO, parameter);

        parameter.put("IsDisplayNameInReport", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            if(Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().isIsDisplayAccount())){
                parameter.put("NguoiLapBieu", userDTO.getFullName());
            }else {
                parameter.put("NguoiLapBieu", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("KeToanTruong", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
        }

        JasperReport jasperReport = ReportUtils.getCompiledFile(reportName + ".jasper", reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(listTITransferDTO, parameter, jasperReport);
        return result;
    }

}
