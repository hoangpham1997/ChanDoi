package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.Report.*;
import vn.softdreams.ebweb.service.dto.Report.SoTheTinhGiaThanhDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;
import javax.swing.border.Border;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTKNH;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTheTD;

@Service
@Transactional(readOnly = true)
public class ReportBusinessServiceImpl implements ReportBusinessService {
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
    private MBDepositDetailsRepository mbDepositDetailsRepository;
    private MBCreditCardRepository mbCreditCardRepository;
    private MBCreditCardDetailsRepository mbCreditCardDetailsRepository;
    private UserRepository userRepository;
    private SaReturnRepository saReturnRepository;
    private SAQuoteRepository saQuoteRepository;
    private IaPublishInvoiceRepository iaPublishInvoiceRepository;
    private IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;
    private InvoiceTypeRepository invoiceTypeRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final TypeRepository typeRepository;
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
    private final ReportBusinessRepository reportBusinessRepository;
    private final AccountListRepository accountListRepository;
    private final EInvoiceService eInvoiceService;
    private final EbUserPackageRepository ebUserPackageRepository;
    private final EbPackageRepository ebPackageRepository;
    private final PPInvoiceRepository pPInvoiceRepository;
    private final UtilsService utilsService;
    private final AccountListService accountListService;
    private final CPPeriodRepository cpPeriodRepository;
    private final StatisticsCodeRepository statisticsCodeRepository;
    private final ExpenseItemRepository expenseItemRepository;

    public ReportBusinessServiceImpl(UnitRepository unitRepository,
                                     MCReceiptRepository mcReceiptRepository,
                                     MBDepositRepository mbDepositRepository,
                                     MBDepositDetailsRepository mbDepositDetailsRepository,
                                     MBTellerPaperRepository mBTellerPaperRepository,
                                     MCPaymentRepository mcPaymentRepository,
                                     MBTellerPaperDetailsRepository mBTellerPaperDetailsRepository,
                                     MBCreditCardRepository mbCreditCardRepository,
                                     MBCreditCardDetailsRepository mbCreditCardDetailsRepository,
                                     SystemOptionRepository systemOptionRepository,
                                     UserRepository userRepository,
                                     OrganizationUnitRepository organizationUnitRepository,
                                     BankAccountDetailsRepository bankAccountDetailsRepository,
                                     OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository,
                                     UserService userService, PPDiscountReturnRepository ppDiscountReturnRepository,
                                     PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository, SAQuoteRepository saQuoteRepository,
                                     SAOrderRepository saOrderRepository, PPInvoiceRepository ppInvoiceRepository,
                                     SAInvoiceRepository saInvoiceRepository,
                                     MaterialGoodsRepository materialGoodsRepository,
                                     RSInwardOutwardRepository rsInwardOutwardRepository,
                                     SaReturnRepository saReturnRepository,
                                     MCAuditRepository mcAuditRepository, PporderRepository pporderRepository,
                                     PPServiceRepository ppServiceRepository, IaPublishInvoiceRepository iaPublishInvoiceRepository,
                                     IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository, InvoiceTypeRepository invoiceTypeRepository,
                                     SaBillRepository sABillRepository, GOtherVoucherRepository gOtherVoucherRepository,
                                     GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository,
                                     ReportBusinessRepository reportBusinessRepository,
                                     EInvoiceService eInvoiceService, AccountListRepository accountListRepository,
                                     TypeRepository typeRepository, EbUserPackageRepository ebUserPackageRepository,
                                     EbPackageRepository ebPackageRepository, PPInvoiceRepository pPInvoiceRepository,
                                     AccountListService accountListService,
                                     UtilsService utilsService, CPPeriodRepository cpPeriodRepository,
                                     StatisticsCodeRepository statisticsCodeRepository,
									 ExpenseItemRepository expenseItemRepository) {
        this.unitRepository = unitRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mbDepositRepository = mbDepositRepository;
        this.mbDepositDetailsRepository = mbDepositDetailsRepository;
        this.mBTellerPaperRepository = mBTellerPaperRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mBTellerPaperDetailsRepository = mBTellerPaperDetailsRepository;
        this.mbCreditCardRepository = mbCreditCardRepository;
        this.mbCreditCardDetailsRepository = mbCreditCardDetailsRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.userRepository = userRepository;
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
        this.materialGoodsRepository = materialGoodsRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.iaPublishInvoiceRepository = iaPublishInvoiceRepository;
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.sABillRepository = sABillRepository;
        this.gOtherVoucherRepository = gOtherVoucherRepository;
        this.gOtherVoucherDetailsRepository = gOtherVoucherDetailsRepository;
        this.reportBusinessRepository = reportBusinessRepository;
        this.eInvoiceService = eInvoiceService;
        this.accountListRepository = accountListRepository;
        this.typeRepository = typeRepository;
        this.ebUserPackageRepository = ebUserPackageRepository;
        this.ebPackageRepository = ebPackageRepository;
        this.pPInvoiceRepository = pPInvoiceRepository;
        this.utilsService = utilsService;
        this.accountListService = accountListService;
        this.cpPeriodRepository = cpPeriodRepository;
        this.statisticsCodeRepository = statisticsCodeRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    byte[] getBangKeMuaBan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "";
        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
            reportName = "bangKeBanRa";
        } else {
            reportName = "bangKeMuaVao";
        }

        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<Type> types = typeRepository.findAllByIsActive();
        List<BangKeMuaBanDTO> bangKeMuaBanDTOS = reportBusinessRepository.getBangKeMuaBan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getSimilarSum(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            phienSoLamViec,
            requestReport.getTypeReport(),
            requestReport.getBill(),
            requestReport.getDependent());
        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA) && requestReport.getSimilarSum()) {
            Map<Integer, Map<UUID, Map<String, BangKeMuaBanDTO>>> similarSumList = new HashMap<>();
            for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
                if (similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()) == null) {
                    Map<UUID, Map<String, BangKeMuaBanDTO>> dtos = new HashMap<>();
                    Map<String, BangKeMuaBanDTO> detail = new HashMap<>();
                    detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                    dtos.put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    similarSumList.put(bangKeMuaBanDTOS.get(i).getVatRate().intValue(), dtos);
                } else {
                    Map<String, BangKeMuaBanDTO> detail = similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).get(bangKeMuaBanDTOS.get(i).getId());
                    if (detail == null) {
                        detail = new HashMap<>();
                        detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    } else {
                        BangKeMuaBanDTO bangKeMuaBanDTO = detail.get(bangKeMuaBanDTOS.get(i).getVatAccount());
                        if (bangKeMuaBanDTO == null) {
                            detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        } else {
                            bangKeMuaBanDTO.setAmount(bangKeMuaBanDTO.getAmount().add(bangKeMuaBanDTOS.get(i).getAmount()));
                            bangKeMuaBanDTO.setVatAmount(bangKeMuaBanDTO.getVatAmount().add(bangKeMuaBanDTOS.get(i).getVatAmount()));
                        }
                    }
                }
                bangKeMuaBanDTOS.get(i).setLinkRef(getRefLink(bangKeMuaBanDTOS.get(i).getTypeID(), bangKeMuaBanDTOS.get(i).getId(), types, null, null, null, null));
            }
            List<BangKeMuaBanDTO> bangKeMuaBanDTOSNew = new ArrayList<>();
            for (Map<UUID, Map<String, BangKeMuaBanDTO>> uuidBangKeMuaBanDTOMap : similarSumList.values()) {
                for (Map<String, BangKeMuaBanDTO> dtoMap : uuidBangKeMuaBanDTOMap.values()) {
                    bangKeMuaBanDTOSNew.addAll(dtoMap.values());
                }
            }
            bangKeMuaBanDTOS = bangKeMuaBanDTOSNew;
        }

        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("TYPE_LEDGER", phienSoLamViec);
        parameter.put("isBill", requestReport.getBill());
        parameter.put("detailSize", bangKeMuaBanDTOS.size());
        int so0SauDauPhay = Integer.parseInt(userDTO.getSystemOption()
            .stream()
            .filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_TienVND))
            .findAny()
            .get()
            .getData());
        String so_0 = net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.leftPad("", so0SauDauPhay, '0');
        parameter.put("so0", so_0);
        String nganCachHangDonVi = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangDVi)).findAny().get().getData();
        String nganCachHangNghin = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangNghin)).findAny().get().getData();
        parameter.put("hangDonVi", Character.toString(nganCachHangDonVi.toCharArray()[0]));
        parameter.put("hangNghin", Character.toString(nganCachHangNghin.toCharArray()[0]));
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalVATAmount = BigDecimal.ZERO;
        int hienThiSoAm = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_SoAm)).findAny().get().getData());
        parameter.put("parentheses", hienThiSoAm == 0);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", "rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")");
        for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
            bangKeMuaBanDTOS.get(i).setAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            bangKeMuaBanDTOS.get(i).setVatAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
            bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
            bangKeMuaBanDTOS.get(i).setVatRateString(getDescriptionVATRate(bangKeMuaBanDTOS.get(i).getVatRate(), requestReport.getTypeReport()));
            if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
                bangKeMuaBanDTOS.get(i).setGoodsServicePurchaseCode(bangKeMuaBanDTOS.get(i).getVatRate().intValue());
            }
            if (bangKeMuaBanDTOS.get(i).getAmount().compareTo(BigDecimal.ZERO) < 0) {
                bangKeMuaBanDTOS.get(i).setTextColor("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")");
            }
            if (bangKeMuaBanDTOS.get(i).getVatAmount().compareTo(BigDecimal.ZERO) < 0) {
                bangKeMuaBanDTOS.get(i).setTextColorVAT("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")");
            }
            totalAmount = totalAmount.add(bangKeMuaBanDTOS.get(i).getAmount());
            totalVATAmount = totalVATAmount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
        }
        parameter.put("totalAmount", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            parameter.put("totalAmountColor", "rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")");
        }

        parameter.put("totalVATAmount", Utils.formatTien(totalVATAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (totalVATAmount.compareTo(BigDecimal.ZERO) < 0) {
            parameter.put("totalVATAmountColor", "rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")");
        }
        // FOOTER
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
            bangKeMuaBanDTOS = bangKeMuaBanDTOS
                .stream()
                .sorted(Comparator
                    .comparingInt(BangKeMuaBanDTO::getGoodsServicePurchaseCode)
                    .thenComparing(BangKeMuaBanDTO::getInvoiceDate, Comparator
                        .nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(BangKeMuaBanDTO::getInvoiceNo)
                    .thenComparing(BangKeMuaBanDTO::getOrderPriority)

                ).collect(Collectors.toList());
        }

        if (bangKeMuaBanDTOS.isEmpty()) {
            bangKeMuaBanDTOS.add(new BangKeMuaBanDTO());
            bangKeMuaBanDTOS.add(new BangKeMuaBanDTO());
            bangKeMuaBanDTOS.add(new BangKeMuaBanDTO());
            parameter.put("detailSize", bangKeMuaBanDTOS.size());
            parameter.put("REPORT_MAX_COUNT", bangKeMuaBanDTOS.size());
            parameter.put("totalAmount", "");
            parameter.put("totalVATAmount", "");
        }
        parameter.put("REPORT_MAX_COUNT", bangKeMuaBanDTOS.size());
        result = ReportUtils.generateReportPDF(bangKeMuaBanDTOS, parameter, jasperReport);

        return result;
    }

    private String getDescriptionVATRate(BigDecimal vatRate, String reportType) {
        if (vatRate == null) {
            return "";
        }
        if (reportType.equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
            switch (vatRate.intValue()) {
                case 0:
                    return "Hàng hóa dịch vụ chịu 0% thuế GTGT";
                case 1:
                    return "Hàng hóa dịch vụ chịu 5% thuế GTGT";
                case 2:
                    return "Hàng hóa dịch vụ chịu 10% thuế GTGT";
                case 3:
                    return "Hàng hóa dịch vụ không tính thuế GTGT";
                case 4:
                    return "Hàng hóa dịch vụ không chịu thuế GTGT";
                default:
                    return "";
            }
        } else {
            switch (vatRate.intValue()) {
                case 3:
                    return "KTT";
                case 4:
                    return "KCT";
                case 0:
                    return "0 %";
                case 1:
                    return "5 %";
                case 2:
                    return "10 %";
                default:
                    return "";
            }
        }
    }

    byte[] getSoNhatThuChi(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoNhatKyThuTien";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<Type> types = typeRepository.findAllByIsActive();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SAInvoiceViewDTO> saInvoiceDTOS = saInvoiceRepository.getAllSAInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<SaReturnDTO> saReturnDTOS = saReturnRepository.getAllSAReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<ViewPPInvoiceDTO> ppInvoiceDTOS = ppInvoiceRepository.getAllPPInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<PPDiscountReturnDTO> ppDiscountReturnDTOS = ppDiscountReturnRepository.getAllPPDiscountReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);

        UUID bankAccountID = bankAccountDetailsRepository.findIdByBankAccount(requestReport.getBankAccountDetail(), userDTO.getOrganizationUnit().getId());
        List<SoNhatKyThuTienDTO> nhatKyThuTienDTOS = reportBusinessRepository.getSoNhatKyThuChi(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getSimilarSum(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            bankAccountID,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getDependent(),
            requestReport.getTypeReport(), requestReport.getGetAmountOriginal());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        int so0SauDauPhay = Integer.parseInt(userDTO.getSystemOption()
            .stream()
            .filter(n -> n.getCode().equals(typeAmount))
            .findAny()
            .get()
            .getData());
        String so_0 = net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.leftPad("", so0SauDauPhay, '0');
        parameter.put("so0", so_0);
        String nganCachHangDonVi = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangDVi)).findAny().get().getData();
        String nganCachHangNghin = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangNghin)).findAny().get().getData();
        parameter.put("hangDonVi", String.valueOf(nganCachHangDonVi.toCharArray()[0]));
        parameter.put("hangNghin", String.valueOf(nganCachHangNghin.toCharArray()[0]));
        parameter.put("accountDebit", "");
        parameter.put("account2", "");
        parameter.put("account3", "");
        parameter.put("account4", "");
        parameter.put("account5", "");
        if (nhatKyThuTienDTOS.size() > 0) {
            String[] accountList = nhatKyThuTienDTOS.get(0).getAccountNumberList().split(",");
            parameter.put("accountDebit", accountList[0]);
            if (accountList.length > 1) {
                parameter.put("account2", accountList[1]);
            }
            if (accountList.length > 2) {
                parameter.put("account3", accountList[2]);
            }

            if (accountList.length > 3) {
                parameter.put("account4", accountList[3]);
            }
            if (accountList.length > 4) {
                parameter.put("account5", accountList[4]);
            }
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        parameter.put("detailSize", nhatKyThuTienDTOS.size());
        for (int i = 0; i < nhatKyThuTienDTOS.size(); i++) {
            nhatKyThuTienDTOS.get(i).setAmountString(Utils.formatTien(nhatKyThuTienDTOS.get(i).getAmount(), typeAmount, userDTO));
            totalAmount = totalAmount.add(nhatKyThuTienDTOS.get(i).getAmount());
            nhatKyThuTienDTOS.get(i).setCol2String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol2(), typeAmount, userDTO));
            totalCol2 = totalCol2.add(nhatKyThuTienDTOS.get(i).getCol2());
            nhatKyThuTienDTOS.get(i).setCol3String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol3(), typeAmount, userDTO));
            totalCol3 = totalCol3.add(nhatKyThuTienDTOS.get(i).getCol3());
            nhatKyThuTienDTOS.get(i).setCol4String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol4(), typeAmount, userDTO));
            totalCol4 = totalCol4.add(nhatKyThuTienDTOS.get(i).getCol4());
            nhatKyThuTienDTOS.get(i).setCol5String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol5(), typeAmount, userDTO));
            totalCol5 = totalCol5.add(nhatKyThuTienDTOS.get(i).getCol5());
            nhatKyThuTienDTOS.get(i).setCol6String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol6(), typeAmount, userDTO));
            totalCol6 = totalCol6.add(nhatKyThuTienDTOS.get(i).getCol6());
            nhatKyThuTienDTOS.get(i).setDateString(convertDate(nhatKyThuTienDTOS.get(i).getDate()));
            nhatKyThuTienDTOS.get(i).setPostedDateString(convertDate(nhatKyThuTienDTOS.get(i).getPostedDate()));
            nhatKyThuTienDTOS.get(i).setBreakPage(false);
            nhatKyThuTienDTOS.get(i).setLinkRef(getRefLink(nhatKyThuTienDTOS.get(i).getRefType(),
                nhatKyThuTienDTOS.get(i).getRefID(),
                types, saInvoiceDTOS, saReturnDTOS, ppInvoiceDTOS, ppDiscountReturnDTOS));
        }
//        parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, typeAmount, userDTO));
//        parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, typeAmount, userDTO));
        parameter.put("totalAmount", Utils.formatTien(totalAmount, typeAmount, userDTO));
        parameter.put("totalCol2", Utils.formatTien(totalCol2, typeAmount, userDTO));
        parameter.put("totalCol3", Utils.formatTien(totalCol3, typeAmount, userDTO));
        parameter.put("totalCol4", Utils.formatTien(totalCol4, typeAmount, userDTO));
        parameter.put("totalCol5", Utils.formatTien(totalCol5, typeAmount, userDTO));
        parameter.put("totalCol6", Utils.formatTien(totalCol6, typeAmount, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (nhatKyThuTienDTOS.isEmpty()) {
            nhatKyThuTienDTOS.add(new SoNhatKyThuTienDTO());
            nhatKyThuTienDTOS.add(new SoNhatKyThuTienDTO());
            nhatKyThuTienDTOS.add(new SoNhatKyThuTienDTO());
            parameter.put("detailSize", nhatKyThuTienDTOS.size());
            parameter.put("REPORT_MAX_COUNT", nhatKyThuTienDTOS.size());
            parameter.put("totalAmount", "");
            parameter.put("totalCol2", "");
            parameter.put("totalCol3", "");
            parameter.put("totalCol4", "");
            parameter.put("totalCol5", "");
            parameter.put("totalCol6", "");
        }
        parameter.put("REPORT_MAX_COUNT", nhatKyThuTienDTOS.size());
        result = ReportUtils.generateReportPDF(nhatKyThuTienDTOS, parameter, jasperReport);

        return result;
    }

    /**
     * @param requestReport
     * @return
     * @AUthor Hautv
     */
    @Override
    public byte[] getReportBusiness(RequestReport requestReport) throws JRException {
        byte[] result = null;
        switch (requestReport.getTypeReport()) {
            case TypeConstant.BAO_CAO.TONG_HOP.SO_NHAT_KY_CHUNG:
                result = getSoNhatKyChung(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.BANG_CAN_DOI_KE_TOAN:
                result = getBangCanDoiKeToan(requestReport);
                break;
            case TypeConstant.BAO_CAO.KHO.SO_CHI_TIET_VAT_LIEU:
                result = getSoChiTietVatLieu(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_CAI_HT_NHAT_KY_CHUNG:
                result = getSoCaiHTNhatKyChung(requestReport);
                break;
            case TypeConstant.BAO_CAO.QUY.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT:
                result = getSoKeToanChiTietQuyTienMat(requestReport);
                break;
            case TypeConstant.BAO_CAO.QUY.SO_QUY_TIEN_MAT:
                result = getSoQuyTienMat(requestReport);
                break;
            case TypeConstant.BAO_CAO.SO_NHAT_KY_THU_TIEN:
            case TypeConstant.BAO_CAO.SO_NHAT_KY_CHI_TIEN:
                result = getSoNhatThuChi(requestReport);
                break;
            case TypeConstant.BAO_CAO.BANG_KE_MUA_VAO:
            case TypeConstant.BAO_CAO.BANG_KE_BAN_RA:
                result = getBangKeMuaBan(requestReport);
                break;
            case TypeConstant.BAO_CAO.NGAN_HANG.BANG_KE_SO_DU_NGAN_HANG:
                result = getBangKeSoDuNganHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.NGAN_HANG.SO_TIEN_GUI_NGAN_HANG:
                result = getSoTienGuiNganHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.BANG_CAN_DOI_TAI_KHOAN:
                result = getBangCanDoiTaiKhoan(requestReport);
                break;
            case TypeConstant.BAO_CAO.KHO.THE_KHO:
                result = getTheKho(requestReport);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.TONG_HOP_CONG_NO_PHAI_TRA:
                result = getTongHopCongNoPhaiTra(requestReport);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.CHI_TIET_CONG_NO_PHAI_TRA:
                result = getChiTietCongNoPhaiTra(requestReport);
                break;
            case TypeConstant.BAO_CAO.KHO.TONG_HOP_CHI_TIET_VAT_LIEU:
                result = getTongHopChiTietVatLieu(requestReport);
                break;
            case TypeConstant.BAO_CAO.KHO.TONG_HOP_TON_KHO:
                result = getTongHopTonKho(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.KET_QUA_HOAT_DONG_KINH_DOANH:
                result = getKetQuaHDKD(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.LUU_CHUYEN_TIEN_TE_TT:
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.LUU_CHUYEN_TIEN_TE_GT:
                result = getLuuChuyenTienTeTT(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.THUYET_MINH_BAO_CAO_TAI_CHINH:
                result = getBaoCaoTaiChinh(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_CHI_TIET_CAC_TAI_KHOAN:
                result = getSoChiTietTaiKhoan(requestReport);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.SO_NHAT_KY_MUA_HANG:
                result = getSoNhatKyMuaHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.SO_CHI_TIET_MUA_HANG:
                result = getSoChiTietMuaHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_NHAT_KY_BAN_HANG:
                result = getSoNhatKyBanHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_CHI_TIET_BAN_HANG:
                result = getSoChiTietBanHang(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.CHI_TIET_CONG_NO_PHAI_THU:
                result = getSoChiTietCongNoPhaiThu(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.TONG_HOP_CONG_NO_PHAI_THU:
                result = getSoTongHopCongNoPhaiThu(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.PHAN_BO_CHI_PHI_TRA_TRUOC:
                result = getPhanBoChiPhiTraTruoc(requestReport);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.THE_TINH_GIA_THANH:
                result = getTheTinhGiaThanh(requestReport);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN:
                result = getSoTheoDoiDoiTuongTHCPTheoTaiKhoan(requestReport);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI:
                result = getSoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN:
                result = getSoTheoDoiMaThongKeTheoTaiKhoan(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI:
                result = getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI:
                result = getTongHopCPTheoKMCP(requestReport);
                break;
            case TypeConstant.BAO_CAO.CCDC.SO_CONG_CU_DUNG_CU:
                result = getSoCCDC(requestReport);
                break;
            case TypeConstant.BAO_CAO.CCDC.SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG:
                result = getSoTheoDoiCCDCTaiNoiSD(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_THEO_DOI_LAI_LO_THEO_HOA_DON:
                result = getSoTheoDoiLaiLoTheoHoaDon(requestReport);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON:
                result = getSoTheoDoiCongNoPhaiThuTheoHoaDon(requestReport);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE:
                result = getSoTheoDoiThanhToanBangNgoaiTe(requestReport);
                break;
        }
        return result;
    }

    private byte[] getSoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        JasperReport jasperReport;
        String expenseItemID = ",";
        String costSetID = ",";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<ExpenseItem> expenseItems = expenseItemRepository.findAllByListIDAndOrg(requestReport.getListExpenseItems(), currentUserLoginAndOrg.get().getOrg());
        expenseItems = getAllChildExpenseItems(expenseItems, requestReport.getCompanyID());
        for (int i = 0; i < expenseItems.size(); i++) {
            expenseItemID += Common.revertUUID(expenseItems.get(i).getId()) + ",";
        }
        for (UUID item : requestReport.getListCostSetID()) {
            costSetID += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> details = reportBusinessRepository.getSoTheoDoiTHCPTheoChiPhi(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            costSetID, expenseItemID);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        List<Type> types = typeRepository.findAllByIsActive();
        if (details.size() > 0) {
            for (Integer i = 0; i < details.size(); i++) {
                BigDecimal totalSoDauKy = BigDecimal.ZERO;
                BigDecimal totalSoPhatSinh = BigDecimal.ZERO;
                BigDecimal totalLuyKeCuoiKy = BigDecimal.ZERO;
                if (details.get(i).getSoDauky() == null || details.get(i).getSoDauky().doubleValue() == 0) {
                    details.get(i).setSoDauKyString("");
                } else {
                    details.get(i).setSoDauKyString(Utils.formatTien(details.get(i).getSoDauky(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (details.get(i).getSoPhatSinh() == null || details.get(i).getSoPhatSinh().doubleValue() == 0) {
                    details.get(i).setSoPhatSinhString("");
                } else {
                    details.get(i).setSoPhatSinhString(Utils.formatTien(details.get(i).getSoPhatSinh(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (details.get(i).getLuyKeCuoiKy() == null || details.get(i).getLuyKeCuoiKy().doubleValue() == 0) {
                    details.get(i).setLuyKeCuoiKyString("");
                } else {
                    details.get(i).setLuyKeCuoiKyString(Utils.formatTien(details.get(i).getLuyKeCuoiKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                for (SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO temp : details) {
                    if (details.get(i).getCostSetCode().equals(temp.getCostSetCode()) &&
                        details.get(i).getCostSetName().equals(temp.getCostSetName())) {
                        totalSoDauKy = totalSoDauKy.add(temp.getSoDauky());
                        totalSoPhatSinh = totalSoPhatSinh.add(temp.getSoPhatSinh());
                        totalLuyKeCuoiKy = totalLuyKeCuoiKy.add(temp.getLuyKeCuoiKy());
                    }
                }
                if (totalSoDauKy.doubleValue() == 0) {
                    details.get(i).setTotalsoDauKy("");
                } else {
                    details.get(i).setTotalsoDauKy(Utils.formatTien(totalSoDauKy, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (totalSoPhatSinh.doubleValue() == 0) {
                    details.get(i).setTotalSoPhatSinh("");
                } else {
                    details.get(i).setTotalSoPhatSinh(Utils.formatTien(totalSoPhatSinh, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (totalLuyKeCuoiKy.doubleValue() == 0) {
                    details.get(i).setTotalLuyKeCuoiKy("");
                } else {
                    details.get(i).setTotalLuyKeCuoiKy(Utils.formatTien(totalLuyKeCuoiKy, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
            }
        } else {
            details.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
            details.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
            details.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }

        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    private byte[] getSoTheoDoiDoiTuongTHCPTheoTaiKhoan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiDoiTuongTHCPTheoTaiKhoan";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        int phienLamViec= Utils.PhienSoLamViec(userDTO);
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        JasperReport jasperReport;
        String accountNumber = ",";
        String costSetID = ",";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> accountLists = accountListRepository.findAllByListAccountNumberAndOrg(requestReport.getAccountList(),
            currentUserLoginAndOrg.get().getOrgGetData());
        accountLists = getAllChildAccount(accountLists, currentUserLoginAndOrg.get().getOrgGetData());
        for (int i = 0; i < accountLists.size(); i++) {
            accountNumber += accountLists.get(i).getAccountNumber() + ",";
        }
        for (String item : requestReport.getAccountList()) {
            accountNumber += item + ",";
        }
        for (UUID item : requestReport.getListCostSetID()) {
            costSetID += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> details = reportBusinessRepository.getSoTheoDoiTHCP(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            costSetID,
            accountNumber,
            phienLamViec);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        List<Type> types = typeRepository.findAllByIsActive();
        if (details.size() > 0) {
            for (Integer i = 0; i < details.size(); i++) {
                BigDecimal totalDebitAmount = BigDecimal.ZERO;
                BigDecimal totalCreditAmount = BigDecimal.ZERO;
                details.get(i).setCostSetCodeString(details.get(i).getCostSetCode());
                details.get(i).setCostSetNameString(details.get(i).getCostSetName());
                details.get(i).setDateString(convertDate(details.get(i).getDate()));
                details.get(i).setDateHTString(convertDate(details.get(i).getDateHT()));
                details.get(i).setNoString(details.get(i).getNo());
                details.get(i).setReasonString(details.get(i).getReason());
                details.get(i).setAccountString(details.get(i).getAccount());
                details.get(i).setAccountCorrespondingString(details.get(i).getAccountCorresponding());
                if (details.get(i).getDebitAmount().doubleValue() == 0 || details.get(i).getDebitAmount() == null) {
                    details.get(i).setDebitAmountString("");
                } else {
                    details.get(i).setDebitAmountString(Utils.formatTien(details.get(i).getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (details.get(i).getCreditAmount() == null || details.get(i).getCreditAmount().doubleValue() == 0) {
                    details.get(i).setCreditAmountString("");
                } else {
                    details.get(i).setCreditAmountString(Utils.formatTien(details.get(i).getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                for (SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO temp : details) {
                    if (details.get(i).getCostSetCode().equals(temp.getCostSetCode()) &&
                        details.get(i).getCostSetName().equals(temp.getCostSetName())) {
                        totalDebitAmount = totalDebitAmount.add(temp.getDebitAmount());
                        totalCreditAmount = totalCreditAmount.add(temp.getCreditAmount());
                    }
                }
                if (totalCreditAmount.doubleValue() == 0) {
                    details.get(i).setTotalCreditAmountString("");
                } else {
                    details.get(i).setTotalCreditAmountString(Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (totalDebitAmount.doubleValue() == 0) {
                    details.get(i).setTotalDebitAmountString("");
                } else {
                    details.get(i).setTotalDebitAmountString(Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                details.get(i).setLinkRef(getRefLink(details.get(i).getTypeID(), details.get(i).getRefID(), types, null, null, null, null));
            }
        }else  {
            details.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
            details.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
            details.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }

        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);
        return result;
    }

    @Override
    public byte[] getInstructionPDF() throws JRException, IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        byte[] fileContent = Files.readAllBytes(Paths.get(currentDirectory.getAbsolutePath() + "/help/huongdansudung.pdf"));
        return fileContent;
    }

    @Override
    public byte[] getExcelReportBusiness(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        switch (requestReport.getTypeReport()) {
            case TypeConstant.BAO_CAO.TONG_HOP.SO_NHAT_KY_CHUNG:
                result = getExcelSoNhatKyChung(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.BANG_CAN_DOI_KE_TOAN:
                result = getExcelBangCanDoiKeToan(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.KHO.SO_CHI_TIET_VAT_LIEU:
                result = getExcelSoChiTietVatLieu(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_CAI_HT_NHAT_KY_CHUNG:
                result = getExcelSoCaiHTNhatKyChung(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.QUY.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT:
                result = getExcelSoKeToanChiTietQuyTienMat(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.QUY.SO_QUY_TIEN_MAT:
                result = getExcelSoQuyTienMat(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.SO_NHAT_KY_THU_TIEN:
            case TypeConstant.BAO_CAO.SO_NHAT_KY_CHI_TIEN:
                result = getExcelSoNhatKyThuChi(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BANG_KE_MUA_VAO:
                result = getExcelBangKeMuaVao(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BANG_KE_BAN_RA:
                result = getExcelBangKeBanRa(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.NGAN_HANG.BANG_KE_SO_DU_NGAN_HANG:
                result = getExcelBangKeSoDuNganHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.NGAN_HANG.SO_TIEN_GUI_NGAN_HANG:
                result = getExcelSoTienGuiNganHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.BANG_CAN_DOI_TAI_KHOAN:
                result = getExcelBangCanDoiTaiKhoan(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.KHO.THE_KHO:
                result = getExcelTheKho(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.TONG_HOP_CONG_NO_PHAI_TRA:
                result = getExcelTongHopCongNoPhaiTra(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.CHI_TIET_CONG_NO_PHAI_TRA:
                result = getExcelChiTietCongNoPhaiTra(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.KHO.TONG_HOP_CHI_TIET_VAT_LIEU:
                result = getExcelTongHopChiTietVatLieu(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.KHO.TONG_HOP_TON_KHO:
                result = getExcelTongHopTonKho(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.KET_QUA_HOAT_DONG_KINH_DOANH:
                result = getExcelKetQuaHDKD(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.LUU_CHUYEN_TIEN_TE_TT:
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.LUU_CHUYEN_TIEN_TE_GT:
                result = getExcelLuuChuyenTienTeTT(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAO_CAO_TAI_CHINH.THUYET_MINH_BAO_CAO_TAI_CHINH:
                result = getExcelBaoCaoTaiChinh(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_CHI_TIET_CAC_TAI_KHOAN:
                result = getExcelSoChiTietCacTaiKhoan(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.SO_NHAT_KY_MUA_HANG:
                result = getExcelSoNhatKyMuaHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.MUA_HANG.SO_CHI_TIET_MUA_HANG:
                result = getExcelSoChiTietMuaHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_NHAT_KY_BAN_HANG:
                result = getExcelSoNhatKyBanHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_CHI_TIET_BAN_HANG:
                result = getExcelSoChiTietBanHang(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.CHI_TIET_CONG_NO_PHAI_THU:
                result = getExcelChiTietCongNoPhaiThu(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.TONG_HOP_CONG_NO_PHAI_THU:
                result = getExcelTongHopCongNoPhaiThu(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.PHAN_BO_CHI_PHI_TRA_TRUOC:
                result = getExcelPhanBoChiPhiTraTruoc(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN:
                result = getExcelSoTheoDoiDoiTuongTHCPTheoTaiKhoan(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI:
                result = getExcelSoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN:
                result = getExcelSoTheoDoiMaThongKeTheoTaiKhoan(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI:
                result = getExcelSoTheoDoiMaThongKeTheoKhoanMucChiPhi(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI:
                result = getExcelTongHopCPTheoKMCP(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.SO_CHI_PHI_SAN_XUAT_KINH_DOANH:
                result = getExcelSoChiPhiSXKD(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.GIA_THANH.THE_TINH_GIA_THANH:
                result = getExcelTheTinhGiaThanh(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.CCDC.SO_CONG_CU_DUNG_CU:
                result = getExcelSoCongCuDungCu(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.CCDC.SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG:
                result = getExcelSoTheoDoiCCDCTaiNoiSuDung(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_THEO_DOI_LAI_LO_THEO_HOA_DON:
                result = getExcelSoTheoDoiLaiLoTheoHoaDon(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.BAN_HANG.SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON:
                result = getExcelSoTheoDoiCongNoPhaiThuTheoHoaDon(requestReport, path);
                break;
            case TypeConstant.BAO_CAO.TONG_HOP.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE:
                result = getExcelSoTheoDoiThanhToanBangNgoaiTe(requestReport, path);
                break;
        }
        return result;
    }

    private byte[] getExcelSoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi(RequestReport requestReport, String path) {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        String costSetID = ",";
        String expenseItemID = ",";
        for (UUID item : requestReport.getListCostSetID()) {
            costSetID += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<ExpenseItem> expenseItems = expenseItemRepository.findAllByListIDAndOrg(requestReport.getListExpenseItems(), currentUserLoginAndOrg.get().getOrg());
        expenseItems = getAllChildExpenseItems(expenseItems, requestReport.getCompanyID());
        for (int i = 0; i < expenseItems.size(); i++) {
            expenseItemID += Common.revertUUID(expenseItems.get(i).getId()) + ",";
        }
        List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> details = reportBusinessRepository.getSoTheoDoiTHCPTheoChiPhi(
            requestReport.getFromDate(), requestReport.getToDate(), costSetID, expenseItemID);
        List<List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO>> listDetails = new ArrayList<>();
        List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> temp = new ArrayList<>();
        if (details.size() > 0) {
            for (int i = 0; i < details.size(); i++) {
                temp.add(details.get(i));
                if (i < details.size() - 1 && details.get(i).getCostSetCode() != null &&
                    !details.get(i).getCostSetCode().equals(details.get(i + 1).getCostSetCode()) || (i == details.size() - 1)) {
                    listDetails.add(temp);
                    temp = new ArrayList<>();
                }
            }
        } else {
            temp.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
            temp.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
            temp.add(new SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO());
            listDetails.add(temp);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
//            style.setVerticalAlignment(VerticalAlignment.CENTER);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);

            for (int i = 1; i < listDetails.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < listDetails.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (listDetails.get(i).size() - 1 > 0) {
                    sheet.shiftRows(11, sheet.getLastRowNum(), listDetails.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 10;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(5).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(7).getCell(0).setCellValue("Đối tượng THCP: " + (listDetails.get(i).get(0).getCostSetCode() == null ? "" : listDetails.get(i).get(0).getCostSetCode()) + " - " + (listDetails.get(i).get(0).getCostSetName() == null ? "" : listDetails.get(i).get(0).getCostSetName()));
                BigDecimal totalSoDauKy = BigDecimal.ZERO;
                BigDecimal totalSoPhatSinh = BigDecimal.ZERO;
                BigDecimal totalLuyKeCuoiKy = BigDecimal.ZERO;
                for (SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO item : listDetails.get(i)) {
                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue(item.getExpenseItemCode() == null ? "" : item.getExpenseItemCode());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getExpenseItemName() == null ? "" : item.getExpenseItemName());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    if (item.getSoDauky() == null || item.getSoDauky().doubleValue() == 0) {
                        cell3.setCellValue("");
                    } else {
                        cell3.setCellValue(item.getSoDauky().doubleValue());
                    }

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                    if (item.getSoPhatSinh() == null || item.getSoPhatSinh().doubleValue() == 0) {
                        cell4.setCellValue("");
                    } else {
                        cell4.setCellValue(item.getSoPhatSinh().doubleValue());
                    }

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    if (item.getLuyKeCuoiKy() == null || item.getLuyKeCuoiKy().doubleValue() == 0) {
                        cell5.setCellValue("");
                    } else {
                        cell5.setCellValue(item.getLuyKeCuoiKy().doubleValue());
                    }

                    start++;
                    if (item.getSoDauky() != null || item.getSoPhatSinh() != null || item.getLuyKeCuoiKy() != null) {
                        totalSoDauKy = totalSoDauKy.add(item.getSoDauky());
                        totalSoPhatSinh = totalSoPhatSinh.add(item.getSoPhatSinh());
                        totalLuyKeCuoiKy = totalLuyKeCuoiKy.add(item.getLuyKeCuoiKy());
                    } else {
                        totalSoDauKy = null;
                        totalSoPhatSinh = null;
                        totalLuyKeCuoiKy = null;
                    }
                }
                Row sumRow = sheet.createRow(10 + listDetails.get(i).size());
                Cell cell0 = sumRow.createCell(0);
                cell0.setCellStyle(styleB);
                CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                cell0.setCellValue("Cộng");

                Cell cell11 = sumRow.createCell(1);
                cell11.setCellStyle(styleB);

                Cell cell1 = sumRow.createCell(2);
                cell1.setCellStyle(styleB);
                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                if (totalSoDauKy == null || totalSoDauKy.doubleValue() == 0) {
                    cell1.setCellValue("");
                } else {
                    cell1.setCellValue(totalSoDauKy.doubleValue());
                }

                Cell cell2 = sumRow.createCell(3);
                cell2.setCellStyle(styleB);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                if (totalSoPhatSinh == null || totalSoPhatSinh.doubleValue() == 0) {
                    cell2.setCellValue("");
                } else {
                    cell2.setCellValue(totalSoPhatSinh.doubleValue());
                }

                Cell cell3 = sumRow.createCell(4);
                cell3.setCellStyle(styleB);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                if (totalLuyKeCuoiKy == null || totalLuyKeCuoiKy.doubleValue() == 0) {
                    cell3.setCellValue("");
                } else {
                    cell3.setCellValue(totalLuyKeCuoiKy.doubleValue());
                }

                SetFooterExcel(workbook, sheet, userDTO, (12 + listDetails.get(i).size()), 3, 0, 1, 3);
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getExcelSoTheoDoiDoiTuongTHCPTheoTaiKhoan(RequestReport requestReport, String path) {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienLamviec = Utils.PhienSoLamViec(userDTO);
        String costSetID = ",";
        String account = ",";
        for (UUID item : requestReport.getListCostSetID()) {
            costSetID += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        for (String item : requestReport.getAccountList()) {
            account += item + ",";
        }
        List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> details = reportBusinessRepository.getSoTheoDoiTHCP(
            requestReport.getFromDate(), requestReport.getToDate(), costSetID, account, phienLamviec);
        List<List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO>> listDetails = new ArrayList<>();
        List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> temp = new ArrayList<>();
        if (details.size() > 0) {
            for (int i = 0; i < details.size(); i++) {
                temp.add(details.get(i));
                if (i < details.size() - 1 && details.get(i).getCostSetCode() != null &&
                    !details.get(i).getCostSetCode().equals(details.get(i + 1).getCostSetCode()) || (i == details.size() - 1)) {
                    listDetails.add(temp);
                    temp = new ArrayList<>();
                }
            }
        } else {
            temp.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
            temp.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
            temp.add(new SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO());
            listDetails.add(temp);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
//            style.setVerticalAlignment(VerticalAlignment.CENTER);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);

            for (int i = 1; i < listDetails.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < listDetails.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (listDetails.get(i).size() - 1 > 0) {
                    sheet.shiftRows(11, sheet.getLastRowNum(), listDetails.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 10;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(5).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(7).getCell(0).setCellValue("Đối tượng THCP: " + (listDetails.get(i).get(0).getCostSetCode() == null ? "" : listDetails.get(i).get(0).getCostSetCode()) + " - " + (listDetails.get(i).get(0).getCostSetName() == null ? "" : listDetails.get(i).get(0).getCostSetName()));
                BigDecimal totalDebitAmount = BigDecimal.ZERO;
                BigDecimal totalCreditAmount = BigDecimal.ZERO;
                for (SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO item : listDetails.get(i)) {
                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue((item.getDate() == null ? "" : convertDate(item.getDate())));

                    Cell cell10 = row.createCell(1);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.CENTER);
                    cell10.setCellValue((item.getDate() == null ? "" : convertDate(item.getDateHT())));

                    Cell cell2 = row.createCell(2);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue(item.getNo() == null ? "" : item.getNo());

                    Cell cell3 = row.createCell(3);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getReason() == null ? "" : item.getReason());

                    Cell cell4 = row.createCell(4);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getAccount() == null ? "" : item.getAccount());

                    Cell cell5 = row.createCell(5);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                    cell5.setCellValue(item.getAccountCorresponding() == null ? "" : item.getAccountCorresponding());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    if (item.getDebitAmount() == null || item.getDebitAmount().doubleValue() == 0) {
                        cell6.setCellValue("");
                    } else {
                        cell6.setCellValue(item.getDebitAmount().doubleValue());
                    }

                    Cell cell7 = row.createCell(7);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    if (item.getCreditAmount() == null || item.getCreditAmount().doubleValue() == 0) {
                        cell7.setCellValue("");
                    } else {
                        cell7.setCellValue(item.getCreditAmount().doubleValue());
                    }

                    start++;
                    if (item.getDebitAmount() != null || item.getCreditAmount() != null) {
                        totalCreditAmount = totalCreditAmount.add(item.getCreditAmount());
                        totalDebitAmount = totalDebitAmount.add(item.getDebitAmount());
                    } else {
                        totalDebitAmount = null;
                        totalCreditAmount = null;
                    }
                }
                Row sumRow = sheet.createRow(10 + listDetails.get(i).size());
                Cell cell0 = sumRow.createCell(0);
                cell0.setCellStyle(styleB);
                CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                cell0.setCellValue("Cộng");

                Cell cell11 = sumRow.createCell(1);
                cell11.setCellStyle(styleB);
                Cell cell12 = sumRow.createCell(2);
                cell12.setCellStyle(styleB);
                Cell cell13 = sumRow.createCell(3);
                cell13.setCellStyle(styleB);
                Cell cell14 = sumRow.createCell(4);
                cell14.setCellStyle(styleB);
                Cell cell15 = sumRow.createCell(5);
                cell15.setCellStyle(styleB);

                Cell cell1 = sumRow.createCell(6);
                cell1.setCellStyle(styleB);
                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                if (totalDebitAmount == null || totalDebitAmount.doubleValue() == 0) {
                    cell1.setCellValue("");
                } else {
                    cell1.setCellValue(totalDebitAmount.doubleValue());
                }

                Cell cell2 = sumRow.createCell(7);
                cell2.setCellStyle(styleB);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                if (totalCreditAmount == null || totalCreditAmount.doubleValue() == 0) {
                    cell2.setCellValue("");
                } else {
                    cell2.setCellValue(totalCreditAmount.doubleValue());
                }

                SetFooterExcel(workbook, sheet, userDTO, (12 + listDetails.get(i).size()), 6, 0, 3, 6);
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getExcelTongHopCongNoPhaiThu(RequestReport requestReport, String path) {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        List<TongHopCongNoPhaiThuDTO> tongHopCongNoPhaiThuDTOS = reportBusinessRepository.getTongHopCongNoPhaiThu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(currentBook),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        if (tongHopCongNoPhaiThuDTOS.size() == 0) {
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());
        }
//        TongHopCongNoPhaiThuDTO.remove(0);
//        TongHopCongNoPhaiThuDTO.remove(63);
//        for (TongHopCongNoPhaiThuDTO snk : tongHopCongNoPhaiThuDTOS) {
////            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
////            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
////            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
////            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
//            Sheet sheet1 = workbook.getSheet("adfsaf");
            sheet.shiftRows(10, sheet.getLastRowNum(), tongHopCongNoPhaiThuDTOS.size());
            // fill dữ liệu vào file
            int i = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setFont(fontDf);
            BigDecimal totalDebitAmount = BigDecimal.ZERO;
            BigDecimal totalCreditAmount = BigDecimal.ZERO;
            BigDecimal totalOpenningCreditAmount = BigDecimal.ZERO;
            BigDecimal totalOpenningDebitAmount = BigDecimal.ZERO;
            BigDecimal totalCloseCreditAmount = BigDecimal.ZERO;
            BigDecimal totalCloseDebitAmount = BigDecimal.ZERO;
//            Cell cellHeader = sheet.getRow(0).getCell(0)
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + requestReport.getAccountNumber());
            sheet.getRow(7).getCell(7).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
            for (TongHopCongNoPhaiThuDTO item : tongHopCongNoPhaiThuDTOS) {
                Row row = sheet.createRow(i);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue(item.getAccountingObjectCode());

                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue(item.getAccountingObjectName());

                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getAccountNumber());

                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue(item.getOpenningDebitAmount() != null ? item.getOpenningDebitAmount().doubleValue() : 0);

                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(item.getOpenningCreditAmount() != null ? item.getOpenningCreditAmount().doubleValue() : 0);

                Cell cell5 = row.createCell(5);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(item.getDebitAmount() != null ? item.getDebitAmount().doubleValue() : 0);

                Cell cell6 = row.createCell(6);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(item.getCreditAmount() != null ? item.getCreditAmount().doubleValue() : 0);

                Cell cell7 = row.createCell(7);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(item.getCloseDebitAmount() != null ? item.getCloseDebitAmount().doubleValue() : 0);

                Cell cell8 = row.createCell(8);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getCloseCreditAmount() != null ? item.getCloseCreditAmount().doubleValue() : 0);

                if (item.getOpenningCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getOpenningCreditAmount()) != 0) {
                    item.setOpenningCreditAmountToString(Utils.formatTien(item.getOpenningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningCreditAmount = totalOpenningCreditAmount.add(item.getOpenningCreditAmount());
                } else {
                    item.setOpenningCreditAmountToString(null);
                }
                if (item.getOpenningDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getOpenningDebitAmount()) != 0) {
                    item.setOpenningDebitAmountToString(Utils.formatTien(item.getOpenningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningDebitAmount = totalOpenningDebitAmount.add(item.getOpenningDebitAmount());
                } else {
                    item.setOpenningDebitAmountToString(null);
                }
                if (item.getDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getDebitAmount()) != 0) {
                    item.setDebitAmountToString(Utils.formatTien(item.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalDebitAmount = totalDebitAmount.add(item.getDebitAmount());
                } else {
                    item.setDebitAmountToString(null);
                }
                if (item.getCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getCreditAmount()) != 0) {
                    item.setCreditAmountToString(Utils.formatTien(item.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCreditAmount = totalCreditAmount.add(item.getCreditAmount());
                } else {
                    item.setCreditAmountToString(null);
                }
                if (item.getCloseCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getCloseCreditAmount()) != 0) {
                    item.setCloseCreditAmountToString(Utils.formatTien(item.getCloseCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseCreditAmount = totalCloseCreditAmount.add(item.getCloseCreditAmount());
                } else {
                    item.setCloseCreditAmountToString(null);
                }
                if (item.getCloseDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getCloseDebitAmount()) != 0) {
                    item.setCloseDebitAmountToString(Utils.formatTien(item.getCloseDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseDebitAmount = totalCloseDebitAmount.add(item.getCloseDebitAmount());
                } else {
                    item.setCloseDebitAmountToString(null);
                }
                i++;
            }
            Row row = sheet.createRow(10 + tongHopCongNoPhaiThuDTOS.size());
            sheet.addMergedRegion(new CellRangeAddress(10 + tongHopCongNoPhaiThuDTOS.size(), 10 + tongHopCongNoPhaiThuDTOS.size(), 0, 2));
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            CellStyle style2 = style;
            style2.setFont(fontB);

            cell0.setCellStyle(style2);
            cell1.setCellStyle(style2);
            cell2.setCellStyle(style2);
            CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
            cell0.setCellValue("Tổng cộng");

            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.setCellValue(totalOpenningDebitAmount != null ? totalOpenningDebitAmount.doubleValue() : 0);

            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.setCellValue(totalOpenningCreditAmount != null ? totalOpenningCreditAmount.doubleValue() : 0);

            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.setCellValue(totalDebitAmount != null ? totalDebitAmount.doubleValue() : 0);

            Cell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalCreditAmount != null ? totalCreditAmount.doubleValue() : 0);

            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalCloseDebitAmount != null ? totalCloseDebitAmount.doubleValue() : 0);

            Cell cell8 = row.createCell(8);
            cell8.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalCloseCreditAmount != null ? totalCloseCreditAmount.doubleValue() : 0);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
//            Row rowFooter = sheet.createRow(12 + tongHopCongNoPhaiThuDTOS.size());
//            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter.getCell(0).setCellStyle(styleHeader);
//            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");
//
//            Row rowFooter2 = sheet.createRow(13 + tongHopCongNoPhaiThuDTOS.size());
//            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter2.getCell(0).setCellStyle(styleHeader);
//            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
//            SetFooterExcel(workbook, sheet, userDTO, (14 + tongHopCongNoPhaiThuDTOS.size()), 7, 1, 4, 7);
//            sheet.getRow((15 + tongHopCongNoPhaiThuDTOS.size())).getCell(1).setCellValue("Người Lập");
            SetFooterAdditionalExcel(workbook, sheet, userDTO, (12 + tongHopCongNoPhaiThuDTOS.size()), 7, 1, 4, 7, "Người lập", "Kế toán trưởng", "Giám đốc");
//            SetFooterExcel(workbook, sheet, userDTO, (12 + tongHopCongNoPhaiThuDTOS.size()), 7, 1, 4, 7);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getExcelBangKeBanRa(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<BangKeMuaBanDTO> bangKeMuaBanDTOS = reportBusinessRepository.getBangKeMuaBan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getSimilarSum(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            phienSoLamViec,
            requestReport.getTypeReport(),
            requestReport.getBill(),
            requestReport.getDependent());

        if (bangKeMuaBanDTOS.size() == 0) {
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int rows = 6;
                sheet.shiftRows(9, sheet.getLastRowNum(), rows);
                Font fontDf = workbook.createFont();
                fontDf.setFontName("Times New Roman");
                fontDf.setBold(false);
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(5).getCell(2).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                Row row = sheet.createRow(9);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                CellRangeAddress cellMerge = new CellRangeAddress(9, 9, 0, 7);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV:");
                for (int i = 10; i < 13; i++) {
                    row = sheet.createRow(i);
                    Cell emptyCell1 = row.createCell(0);
                    Cell emptyCell2 = row.createCell(1);
                    Cell emptyCell3 = row.createCell(2);
                    Cell emptyCell4 = row.createCell(3);
                    Cell emptyCell5 = row.createCell(4);
                    Cell emptyCell6 = row.createCell(5);
                    Cell emptyCell7 = row.createCell(6);
                    Cell emptyCell8 = row.createCell(7);
                    emptyCell1.setCellStyle(style);
                    emptyCell2.setCellStyle(style);
                    emptyCell3.setCellStyle(style);
                    emptyCell4.setCellStyle(style);
                    emptyCell5.setCellStyle(style);
                    emptyCell6.setCellStyle(style);
                    emptyCell7.setCellStyle(style);
                    emptyCell8.setCellStyle(style);
                    emptyCell1.setCellValue("");
                    emptyCell2.setCellValue("");
                    emptyCell3.setCellValue("");
                    emptyCell4.setCellValue("");
                    emptyCell5.setCellValue("");
                    emptyCell6.setCellValue("");
                    emptyCell7.setCellValue("");
                    emptyCell8.setCellValue("");
                }
                row = sheet.createRow(13);
                cellMerge = new CellRangeAddress(13, 13, 0, 5);
                try {
                    sheet.addMergedRegion(cellMerge);
                } catch (Exception e) {

                }
                Cell cellSum = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cellSum.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cellSum, HorizontalAlignment.LEFT);
                cellSum.setCellValue("Cộng nhóm:");
                Cell cellSum1 = row.createCell(6);
                Cell cellSum2 = row.createCell(7);
                cellSum1.setCellStyle(style);
                cellSum2.setCellStyle(style);
                cellSum1.setCellValue("");
                cellSum2.setCellValue("");

                row = sheet.createRow(14);
                cellMerge = new CellRangeAddress(14, 14, 0, 4);
                try {
                    sheet.addMergedRegion(cellMerge);
                } catch (Exception e) {

                }
                Cell cellTotal = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cellTotal.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cellTotal, HorizontalAlignment.LEFT);
                cellTotal.setCellValue("Tổng cộng:");
                Cell cellTotal1 = row.createCell(5);
                Cell cellTotal2 = row.createCell(6);
                Cell cellTotal3 = row.createCell(7);
                cellTotal1.setCellStyle(style);
                cellTotal2.setCellStyle(style);
                cellTotal3.setCellStyle(style);
                cellTotal1.setCellValue("");
                cellTotal2.setCellValue("");
                cellTotal3.setCellValue("");

                try {
                    SetFooterExcel(workbook, sheet, userDTO, 16, 6, 0, 3, 6);
                    Row rowReporter = sheet.getRow(17);
                    rowReporter.getCell(0).setCellValue("Nguời lập");
                } catch (NullPointerException e) {

                }
                workbook.write(bos);
                workbook.close();
            } catch (Exception e) {

            }
            return bos.toByteArray();
        }

        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA) && requestReport.getSimilarSum()) {
            Map<Integer, Map<UUID, Map<String, BangKeMuaBanDTO>>> similarSumList = new HashMap<>();
            for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
                if (similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()) == null) {
                    Map<UUID, Map<String, BangKeMuaBanDTO>> dtos = new HashMap<>();
                    Map<String, BangKeMuaBanDTO> detail = new HashMap<>();
                    detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                    dtos.put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    similarSumList.put(bangKeMuaBanDTOS.get(i).getVatRate().intValue(), dtos);
                } else {
                    Map<String, BangKeMuaBanDTO> detail = similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).get(bangKeMuaBanDTOS.get(i).getId());
                    if (detail == null) {
                        detail = new HashMap<>();
                        detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    } else {
                        BangKeMuaBanDTO bangKeMuaBanDTO = detail.get(bangKeMuaBanDTOS.get(i).getVatAccount());
                        if (bangKeMuaBanDTO == null) {
                            detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        } else {
                            bangKeMuaBanDTO.setAmount(bangKeMuaBanDTO.getAmount().add(bangKeMuaBanDTOS.get(i).getAmount()));
                            bangKeMuaBanDTO.setVatAmount(bangKeMuaBanDTO.getVatAmount().add(bangKeMuaBanDTOS.get(i).getVatAmount()));
                        }
                    }
                }
            }
            List<BangKeMuaBanDTO> bangKeMuaBanDTOSNew = new ArrayList<>();
            for (Map<UUID, Map<String, BangKeMuaBanDTO>> uuidBangKeMuaBanDTOMap : similarSumList.values()) {
                for (Map<String, BangKeMuaBanDTO> dtoMap : uuidBangKeMuaBanDTOMap.values()) {
                    bangKeMuaBanDTOSNew.addAll(dtoMap.values());
                }
            }
            bangKeMuaBanDTOS = bangKeMuaBanDTOSNew;
        }
        // Ban ra
        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
            bangKeMuaBanDTOS = bangKeMuaBanDTOS
                .stream()
                .sorted(Comparator
                    .comparing(BangKeMuaBanDTO::getInvoiceDate, Comparator
                        .nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(BangKeMuaBanDTO::getInvoiceNo)
                    .thenComparing(BangKeMuaBanDTO::getOrderPriority)
                ).collect(Collectors.toList());
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalVATAmount = BigDecimal.ZERO;
            BigDecimal totalAmountVat0 = BigDecimal.ZERO;
            BigDecimal totalVat0Amount = BigDecimal.ZERO;
            BigDecimal totalAmountVat5 = BigDecimal.ZERO;
            BigDecimal totalVat5Amount = BigDecimal.ZERO;
            BigDecimal totalAmountVat10 = BigDecimal.ZERO;
            BigDecimal totalVat10Amount = BigDecimal.ZERO;
            BigDecimal totalAmountVatKct = BigDecimal.ZERO;
            BigDecimal totalVatKctAmount = BigDecimal.ZERO;
            BigDecimal totalAmountVatKtt = BigDecimal.ZERO;
            BigDecimal totalVatKttAmount = BigDecimal.ZERO;
            List<BangKeMuaBanDTO> vat0 = new ArrayList<>();
            List<BangKeMuaBanDTO> vat5 = new ArrayList<>();
            List<BangKeMuaBanDTO> vat10 = new ArrayList<>();
            List<BangKeMuaBanDTO> kct = new ArrayList<>();
            List<BangKeMuaBanDTO> ktt = new ArrayList<>();
            int rowAdd = 0;
            for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
                bangKeMuaBanDTOS.get(i).setAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                bangKeMuaBanDTOS.get(i).setVatAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
                bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
                bangKeMuaBanDTOS.get(i).setVatRateString(getDescriptionVATRate(bangKeMuaBanDTOS.get(i).getVatRate(), requestReport.getTypeReport()));
                if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
                    bangKeMuaBanDTOS.get(i).setGoodsServicePurchaseCode(bangKeMuaBanDTOS.get(i).getVatRate().intValue());
                }
                totalAmount = totalAmount.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVATAmount = totalVATAmount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                if (bangKeMuaBanDTOS.get(i).getVatRate().intValue() == 0) {
                    vat0.add(bangKeMuaBanDTOS.get(i));
                    totalAmountVat0 = totalAmountVat0.add(bangKeMuaBanDTOS.get(i).getAmount());
                    totalVat0Amount = totalVat0Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                } else if (bangKeMuaBanDTOS.get(i).getVatRate().intValue() == 1) {
                    vat5.add(bangKeMuaBanDTOS.get(i));
                    totalAmountVat5 = totalAmountVat5.add(bangKeMuaBanDTOS.get(i).getAmount());
                    totalVat5Amount = totalVat5Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                } else if (bangKeMuaBanDTOS.get(i).getVatRate().intValue() == 2) {
                    vat10.add(bangKeMuaBanDTOS.get(i));
                    totalAmountVat10 = totalAmountVat10.add(bangKeMuaBanDTOS.get(i).getAmount());
                    totalVat10Amount = totalVat10Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                } else if (bangKeMuaBanDTOS.get(i).getVatRate().intValue() == 3) {
                    kct.add(bangKeMuaBanDTOS.get(i));
                    totalAmountVatKct = totalAmountVatKct.add(bangKeMuaBanDTOS.get(i).getAmount());
                    totalVatKctAmount = totalVatKctAmount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                } else if (bangKeMuaBanDTOS.get(i).getVatRate().intValue() == 4) {
                    ktt.add(bangKeMuaBanDTOS.get(i));
                    totalAmountVatKtt = totalAmountVatKtt.add(bangKeMuaBanDTOS.get(i).getAmount());
                    totalVatKttAmount = totalVatKttAmount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
                }
            }
            if (vat0.size() > 0) {
                rowAdd += 2;
            }
            if (vat5.size() > 0) {
                rowAdd += 2;
            }
            if (vat10.size() > 0) {
                rowAdd += 2;
            }
            if (kct.size() > 0) {
                rowAdd += 2;
            }
            if (ktt.size() > 0) {
                rowAdd += 2;
            }
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int rows = bangKeMuaBanDTOS.size();
                sheet.shiftRows(9, sheet.getLastRowNum(), rows + rowAdd + 1);
                // fill dữ liệu vào file
                Font fontDf = workbook.createFont();
                fontDf.setFontName("Times New Roman");
                fontDf.setBold(false);
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(5).getCell(2).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

                int addRow = 0;
                if (vat0.size() > 0) {
                    Row row = sheet.createRow(9);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }

                    CellRangeAddress cellMerge = new CellRangeAddress(9, 9, 0, 7);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    fontB.setBold(true);
                    cell0.setCellValue("Nhóm HHDV : Hàng hóa dịch vụ chịu 0% thuế GTGT");
                    addRow++;
                    int i = 10;
                    for (BangKeMuaBanDTO item : vat0) {
                        if (Boolean.TRUE.equals(item.getBold())) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                        row = sheet.createRow(i);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                        cell1.setCellValue(item.getInvoiceNo());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                        cell2.setCellValue(item.getInvoiceDateString());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                        cell3.setCellValue(item.getAccountingObjectName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                        cell4.setCellValue(item.getCompanyTaxCode());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                        cell5.setCellValue(item.getDescription());

                        Cell cell6 = row.createCell(5);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                        cell6.setCellValue(item.getAmount().doubleValue());

                        Cell cell7 = row.createCell(6);
                        cell7.setCellStyle(style);
                        CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                        cell7.setCellValue(item.getVatAmount().doubleValue());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellStyle(style);
                        CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                        cell8.setCellValue(item.getVatAccount());
                        i++;
                    }
                    row = sheet.createRow(vat0.size() + 9 + addRow);
                    cellMerge = new CellRangeAddress(vat0.size() + 9 + addRow, vat0.size() + 9 + addRow, 0, 4);
                    try {
                        sheet.addMergedRegion(cellMerge);
                    } catch (Exception e) {

                    }
                    Cell cell1 = row.createCell(0);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    cell1.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue("Cộng nhóm: Hàng hóa dịch vụ chịu 0% thuế GTGT");
                    addRow++;
                    Cell cell2 = row.createCell(5);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                    cell2.setCellValue(totalAmountVat0.doubleValue());

                    Cell cell3 = row.createCell(6);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(totalVat0Amount.doubleValue());

                    Cell cell4 = row.createCell(7);
                    cell4.setCellStyle(style);
                    cell4.setCellValue("");
                }
                if (vat5.size() > 0) {
                    Row row = sheet.createRow(vat0.size() + 9 + addRow);
                    CellRangeAddress cellMerge = new CellRangeAddress(vat0.size() + 9 + addRow, vat0.size() + 9 + addRow, 0, 7);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell0 = row.createCell(0);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    cell0.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue("Nhóm HHDV : Hàng hóa dịch vụ chịu 5% thuế GTGT");
                    addRow++;
                    int i = 9 + vat0.size() + addRow;
                    for (BangKeMuaBanDTO item : vat5) {
                        if (Boolean.TRUE.equals(item.getBold())) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                        row = sheet.createRow(i);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                        cell1.setCellValue(item.getInvoiceNo());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                        cell2.setCellValue(item.getInvoiceDateString());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                        cell3.setCellValue(item.getAccountingObjectName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                        cell4.setCellValue(item.getCompanyTaxCode());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                        cell5.setCellValue(item.getDescription());

                        Cell cell6 = row.createCell(5);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                        cell6.setCellValue(item.getAmount().doubleValue());

                        Cell cell7 = row.createCell(6);
                        cell7.setCellStyle(style);
                        CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                        cell7.setCellValue(item.getVatAmount().doubleValue());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellStyle(style);
                        CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                        cell8.setCellValue(item.getVatAccount());
                        i++;
                    }
                    int indexTotal = vat0.size() + vat5.size() + addRow + 9;
                    row = sheet.createRow(indexTotal);
                    cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                    try {
                        sheet.addMergedRegion(cellMerge);
                    } catch (Exception e) {

                    }
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue("Cộng nhóm: Hàng hóa dịch vụ chịu 5% thuế GTGT");
                    addRow++;
                    Cell cell2 = row.createCell(5);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                    cell2.setCellValue(totalAmountVat5.doubleValue());

                    Cell cell3 = row.createCell(6);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(totalVat5Amount.doubleValue());

                    Cell cell4 = row.createCell(7);
                    cell4.setCellStyle(style);
                    cell4.setCellValue("");
                }
                if (vat10.size() > 0) {
                    Row row = sheet.createRow(vat0.size() + vat5.size() + 9 + addRow);
                    CellRangeAddress cellMerge = new CellRangeAddress(vat0.size() + vat5.size() + 9 + addRow,
                        vat0.size() + vat5.size() + 9 + addRow, 0, 7);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue("Nhóm HHDV : Hàng hóa dịch vụ chịu 10% thuế GTGT");
                    addRow++;
                    int i = 9 + vat0.size() + vat5.size() + addRow;
                    for (BangKeMuaBanDTO item : vat10) {
                        if (Boolean.TRUE.equals(item.getBold())) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                        row = sheet.createRow(i);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                        cell1.setCellValue(item.getInvoiceNo());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                        cell2.setCellValue(item.getInvoiceDateString());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                        cell3.setCellValue(item.getAccountingObjectName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                        cell4.setCellValue(item.getCompanyTaxCode());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                        cell5.setCellValue(item.getDescription());

                        Cell cell6 = row.createCell(5);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                        cell6.setCellValue(item.getAmount().doubleValue());

                        Cell cell7 = row.createCell(6);
                        cell7.setCellStyle(style);
                        CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                        cell7.setCellValue(item.getVatAmount().doubleValue());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellStyle(style);
                        CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                        cell8.setCellValue(item.getVatAccount());
                        i++;
                    }
                    int indexTotal = vat0.size() + vat5.size() + vat10.size() + addRow + 9;
                    row = sheet.createRow(indexTotal);
                    cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue("Cộng nhóm: Hàng hóa dịch vụ chịu 10% thuế GTGT");
                    addRow++;
                    Cell cell2 = row.createCell(5);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                    cell2.setCellValue(totalAmountVat10.doubleValue());

                    Cell cell3 = row.createCell(6);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(totalVat10Amount.doubleValue());

                    Cell cell4 = row.createCell(7);
                    cell4.setCellStyle(style);
                    cell4.setCellValue("");
                }
                if (kct.size() > 0) {
                    Row row = sheet.createRow(vat0.size() + vat5.size() + vat10.size() + 9 + addRow);
                    CellRangeAddress cellMerge = new CellRangeAddress(vat0.size() + vat5.size() + vat10.size() + 9 + addRow,
                        vat0.size() + vat5.size() + vat10.size() + 9 + addRow, 0, 7);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue("Nhóm HHDV : Hàng hóa dịch vụ không chịu thuế GTGT");
                    addRow++;
                    int i = 9 + vat0.size() + vat5.size() + vat10.size() + addRow;
                    for (BangKeMuaBanDTO item : kct) {
                        if (Boolean.TRUE.equals(item.getBold())) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                        row = sheet.createRow(i);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                        cell1.setCellValue(item.getInvoiceNo());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                        cell2.setCellValue(item.getInvoiceDateString());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                        cell3.setCellValue(item.getAccountingObjectName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                        cell4.setCellValue(item.getCompanyTaxCode());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                        cell5.setCellValue(item.getDescription());

                        Cell cell6 = row.createCell(5);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                        cell6.setCellValue(item.getAmount().doubleValue());

                        Cell cell7 = row.createCell(6);
                        cell7.setCellStyle(style);
                        CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                        cell7.setCellValue(item.getVatAmount().doubleValue());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellStyle(style);
                        CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                        cell8.setCellValue(item.getVatAccount());
                        i++;
                    }
                    int indexTotal = vat0.size() + vat5.size() + vat10.size() + kct.size() + addRow + 9;
                    row = sheet.createRow(indexTotal);
                    cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue("Cộng nhóm: Hàng hóa dịch vụ không chịu thuế GTGT");
                    addRow++;
                    Cell cell2 = row.createCell(5);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                    cell2.setCellValue(totalAmountVatKct.doubleValue());

                    Cell cell3 = row.createCell(6);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(totalVatKctAmount.doubleValue());

                    Cell cell4 = row.createCell(7);
                    cell4.setCellStyle(style);
                    cell4.setCellValue("");
                }
                if (ktt.size() > 0) {
                    Row row = sheet.createRow(vat0.size() + vat5.size() + vat10.size() + kct.size() + 9 + addRow);
                    CellRangeAddress cellMerge = new CellRangeAddress(vat0.size() + vat5.size() + vat10.size() + kct.size() + 9 + addRow,
                        vat0.size() + vat5.size() + vat10.size() + kct.size() + 9 + addRow, 0, 7);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue("Nhóm HHDV : Hàng hóa dịch vụ không tính thuế GTGT");
                    addRow++;
                    int i = 9 + vat0.size() + vat5.size() + vat10.size() + kct.size() + addRow;
                    for (BangKeMuaBanDTO item : ktt) {
                        if (Boolean.TRUE.equals(item.getBold())) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                        row = sheet.createRow(i);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                        cell1.setCellValue(item.getInvoiceNo());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                        cell2.setCellValue(item.getInvoiceDateString());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                        cell3.setCellValue(item.getAccountingObjectName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                        cell4.setCellValue(item.getCompanyTaxCode());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                        CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                        cell5.setCellValue(item.getDescription());

                        Cell cell6 = row.createCell(5);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                        cell6.setCellValue(item.getAmount().doubleValue());

                        Cell cell7 = row.createCell(6);
                        cell7.setCellStyle(style);
                        CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                        cell7.setCellValue(item.getVatAmount().doubleValue());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellStyle(style);
                        CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                        CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                        cell8.setCellValue(item.getVatAccount());
                        i++;
                    }
                    int indexTotal = vat0.size() + vat5.size() + vat10.size() + kct.size() + ktt.size() + addRow + 9;
                    row = sheet.createRow(indexTotal);
                    cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                    sheet.addMergedRegion(cellMerge);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    if (Boolean.TRUE.equals(fontB.getBold())) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue("Cộng nhóm: Hàng hóa dịch vụ không tính thuế GTGT");
                    addRow++;
                    Cell cell2 = row.createCell(5);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                    cell2.setCellValue(totalAmountVatKtt.doubleValue());

                    Cell cell3 = row.createCell(6);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(totalVatKttAmount.doubleValue());

                    Cell cell4 = row.createCell(7);
                    cell4.setCellStyle(style);
                    cell4.setCellValue("");
                }
                int indexTotal = vat0.size() + vat5.size() + vat10.size() + kct.size() + ktt.size() + addRow + 9;
                CellRangeAddress cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                Row row = sheet.createRow(indexTotal);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                fontB.setBold(true);
                cell0.setCellValue("Tổng cộng");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                cell2.setCellValue(totalAmount.doubleValue());

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                cell7.setCellValue(totalVATAmount.doubleValue());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                cell8.setCellValue("");
                try {
                    SetFooterExcel(workbook, sheet, userDTO, (addRow + 10 + bangKeMuaBanDTOS.size()), 6, 0, 3, 6);
                    Row rowReporter = sheet.getRow(addRow + 11 + bangKeMuaBanDTOS.size());
                    rowReporter.getCell(0).setCellValue("Nguời lập");
                } catch (NullPointerException e) {

                }
                workbook.write(bos);
                workbook.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return bos.toByteArray();
        }

        return bos.toByteArray();
    }

    private byte[] getExcelBangKeMuaVao(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<BangKeMuaBanDTO> bangKeMuaBanDTOS = reportBusinessRepository.getBangKeMuaBan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getSimilarSum(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            phienSoLamViec,
            requestReport.getTypeReport(),
            requestReport.getBill(),
            requestReport.getDependent());

        if (bangKeMuaBanDTOS.size() == 0) {
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int rows = 6;
                sheet.shiftRows(9, sheet.getLastRowNum(), rows);
                Font fontDf = workbook.createFont();
                fontDf.setFontName("Times New Roman");
                fontDf.setBold(false);
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(5).getCell(2).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                Row row = sheet.createRow(9);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                CellRangeAddress cellMerge = new CellRangeAddress(9, 9, 0, 7);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                Cell cell1 = row.createCell(8);
                cell0.setCellStyle(style);
                cell1.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào:");
                for (int i = 10; i < 13; i++) {
                    row = sheet.createRow(i);
                    Cell emptyCell1 = row.createCell(0);
                    Cell emptyCell2 = row.createCell(1);
                    Cell emptyCell3 = row.createCell(2);
                    Cell emptyCell4 = row.createCell(3);
                    Cell emptyCell5 = row.createCell(4);
                    Cell emptyCell6 = row.createCell(5);
                    Cell emptyCell7 = row.createCell(6);
                    Cell emptyCell8 = row.createCell(7);
                    Cell emptyCell9 = row.createCell(8);
                    emptyCell1.setCellStyle(style);
                    emptyCell2.setCellStyle(style);
                    emptyCell3.setCellStyle(style);
                    emptyCell4.setCellStyle(style);
                    emptyCell5.setCellStyle(style);
                    emptyCell6.setCellStyle(style);
                    emptyCell7.setCellStyle(style);
                    emptyCell8.setCellStyle(style);
                    emptyCell9.setCellStyle(style);
                }
                row = sheet.createRow(13);
                cellMerge = new CellRangeAddress(13, 13, 0, 5);
                try {
                    sheet.addMergedRegion(cellMerge);
                } catch (Exception e) {

                }
                Cell cellSum = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cellSum.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cellSum, HorizontalAlignment.LEFT);
                cellSum.setCellValue("Cộng nhóm:");
                Cell cellSum1 = row.createCell(6);
                Cell cellSum2 = row.createCell(7);
                Cell cellSum3 = row.createCell(8);
                cellSum1.setCellStyle(style);
                cellSum2.setCellStyle(style);
                cellSum3.setCellStyle(style);

                row = sheet.createRow(14);
                cellMerge = new CellRangeAddress(14, 14, 0, 4);
                try {
                    sheet.addMergedRegion(cellMerge);
                } catch (Exception e) {

                }
                Cell cellTotal = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cellTotal.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cellTotal, HorizontalAlignment.LEFT);
                cellTotal.setCellValue("Tổng cộng:");
                Cell cellTotal1 = row.createCell(5);
                Cell cellTotal2 = row.createCell(6);
                Cell cellTotal3 = row.createCell(7);
                Cell cellTotal4 = row.createCell(8);
                cellTotal1.setCellStyle(style);
                cellTotal2.setCellStyle(style);
                cellTotal3.setCellStyle(style);
                cellTotal4.setCellStyle(style);

                try {
                    SetFooterExcel(workbook, sheet, userDTO, 16, 7, 0, 3, 7);
                    Row rowReporter = sheet.getRow(17);
                    rowReporter.getCell(0).setCellValue("Nguời lập");
                } catch (NullPointerException e) {

                }
                workbook.write(bos);
                workbook.close();
            } catch (Exception e) {

            }
            return bos.toByteArray();
        }

        if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA) && requestReport.getSimilarSum()) {
            Map<Integer, Map<UUID, Map<String, BangKeMuaBanDTO>>> similarSumList = new HashMap<>();
            for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
                if (similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()) == null) {
                    Map<UUID, Map<String, BangKeMuaBanDTO>> dtos = new HashMap<>();
                    Map<String, BangKeMuaBanDTO> detail = new HashMap<>();
                    detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                    dtos.put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    similarSumList.put(bangKeMuaBanDTOS.get(i).getVatRate().intValue(), dtos);
                } else {
                    Map<String, BangKeMuaBanDTO> detail = similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).get(bangKeMuaBanDTOS.get(i).getId());
                    if (detail == null) {
                        detail = new HashMap<>();
                        detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        similarSumList.get(bangKeMuaBanDTOS.get(i).getVatRate().intValue()).put(bangKeMuaBanDTOS.get(i).getId(), detail);
                    } else {
                        BangKeMuaBanDTO bangKeMuaBanDTO = detail.get(bangKeMuaBanDTOS.get(i).getVatAccount());
                        if (bangKeMuaBanDTO == null) {
                            detail.put(bangKeMuaBanDTOS.get(i).getVatAccount(), bangKeMuaBanDTOS.get(i));
                        } else {
                            bangKeMuaBanDTO.setAmount(bangKeMuaBanDTO.getAmount().add(bangKeMuaBanDTOS.get(i).getAmount()));
                            bangKeMuaBanDTO.setVatAmount(bangKeMuaBanDTO.getVatAmount().add(bangKeMuaBanDTOS.get(i).getVatAmount()));
                        }
                    }
                }
            }
            List<BangKeMuaBanDTO> bangKeMuaBanDTOSNew = new ArrayList<>();
            for (Map<UUID, Map<String, BangKeMuaBanDTO>> uuidBangKeMuaBanDTOMap : similarSumList.values()) {
                for (Map<String, BangKeMuaBanDTO> dtoMap : uuidBangKeMuaBanDTOMap.values()) {
                    bangKeMuaBanDTOSNew.addAll(dtoMap.values());
                }
            }
            bangKeMuaBanDTOS = bangKeMuaBanDTOSNew;
        }

        //Mua vao
        bangKeMuaBanDTOS = bangKeMuaBanDTOS
            .stream()
            .sorted(Comparator
                .comparing(BangKeMuaBanDTO::getInvoiceDate, Comparator
                    .nullsFirst(Comparator.naturalOrder()))
                .thenComparing(BangKeMuaBanDTO::getInvoiceNo)
                .thenComparing(BangKeMuaBanDTO::getOrderPriority)
            ).collect(Collectors.toList());
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalVATAmount = BigDecimal.ZERO;
        BigDecimal totalAmountVatGroup1 = BigDecimal.ZERO;
        BigDecimal totalVatGroup1Amount = BigDecimal.ZERO;
        BigDecimal totalAmountVatGroup2 = BigDecimal.ZERO;
        BigDecimal totalVatGroup2Amount = BigDecimal.ZERO;
        BigDecimal totalAmountVatGroup3 = BigDecimal.ZERO;
        BigDecimal totalVatGroup3Amount = BigDecimal.ZERO;
        BigDecimal totalAmountVatGroup4 = BigDecimal.ZERO;
        BigDecimal totalVatGroup4Amount = BigDecimal.ZERO;
        BigDecimal totalAmountVatGroup5 = BigDecimal.ZERO;
        BigDecimal totalVatGroup5Amount = BigDecimal.ZERO;
        List<BangKeMuaBanDTO> group1 = new ArrayList<>();
        List<BangKeMuaBanDTO> group2 = new ArrayList<>();
        List<BangKeMuaBanDTO> group3 = new ArrayList<>();
        List<BangKeMuaBanDTO> group4 = new ArrayList<>();
        List<BangKeMuaBanDTO> group5 = new ArrayList<>();
        int rowAdd = 0;
        for (int i = 0; i < bangKeMuaBanDTOS.size(); i++) {
            bangKeMuaBanDTOS.get(i).setAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            bangKeMuaBanDTOS.get(i).setVatAmountString(Utils.formatTien(bangKeMuaBanDTOS.get(i).getVatAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
            bangKeMuaBanDTOS.get(i).setInvoiceDateString(convertDate(bangKeMuaBanDTOS.get(i).getInvoiceDate()));
            bangKeMuaBanDTOS.get(i).setVatRateString(getDescriptionVATRate(bangKeMuaBanDTOS.get(i).getVatRate(), requestReport.getTypeReport()));
            if (requestReport.getTypeReport().equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
                bangKeMuaBanDTOS.get(i).setGoodsServicePurchaseCode(bangKeMuaBanDTOS.get(i).getVatRate().intValue());
            }
            totalAmount = totalAmount.add(bangKeMuaBanDTOS.get(i).getAmount());
            totalVATAmount = totalVATAmount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            if (bangKeMuaBanDTOS.get(i).getGoodsServicePurchaseCode().intValue() == 1) {
                group1.add(bangKeMuaBanDTOS.get(i));
                totalAmountVatGroup1 = totalAmountVatGroup1.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVatGroup1Amount = totalVatGroup1Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            } else if (bangKeMuaBanDTOS.get(i).getGoodsServicePurchaseCode().intValue() == 2) {
                group2.add(bangKeMuaBanDTOS.get(i));
                totalAmountVatGroup2 = totalAmountVatGroup2.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVatGroup2Amount = totalVatGroup2Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            } else if (bangKeMuaBanDTOS.get(i).getGoodsServicePurchaseCode().intValue() == 3) {
                group3.add(bangKeMuaBanDTOS.get(i));
                totalAmountVatGroup3 = totalAmountVatGroup3.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVatGroup3Amount = totalVatGroup3Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            } else if (bangKeMuaBanDTOS.get(i).getGoodsServicePurchaseCode().intValue() == 4) {
                group4.add(bangKeMuaBanDTOS.get(i));
                totalAmountVatGroup4 = totalAmountVatGroup4.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVatGroup4Amount = totalVatGroup4Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            } else if (bangKeMuaBanDTOS.get(i).getGoodsServicePurchaseCode().intValue() == 5) {
                group5.add(bangKeMuaBanDTOS.get(i));
                totalAmountVatGroup5 = totalAmountVatGroup5.add(bangKeMuaBanDTOS.get(i).getAmount());
                totalVatGroup5Amount = totalVatGroup5Amount.add(bangKeMuaBanDTOS.get(i).getVatAmount());
            }
        }
        if (group1.size() > 0) {
            rowAdd += 2;
        }
        if (group2.size() > 0) {
            rowAdd += 2;
        }
        if (group3.size() > 0) {
            rowAdd += 2;
        }
        if (group4.size() > 0) {
            rowAdd += 2;
        }
        if (group5.size() > 0) {
            rowAdd += 2;
        }
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = bangKeMuaBanDTOS.size();
            sheet.shiftRows(9, sheet.getLastRowNum(), rows + rowAdd + 1);
            // fill dữ liệu vào file
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(5).getCell(2).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            int addRow = 0;
            if (group1.size() > 0) {
                Row row = sheet.createRow(9);
                style.setFont(fontB);
                CellRangeAddress cellMerge = new CellRangeAddress(9, 9, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào: 1.Hàng hoá, dịch vụ dùng riêng cho SXKD chịu thuế GTGT và" +
                    " sử dụng cho các hoạt động cung cấp hàng hoá, dịch vụ không kê khai, nộp thuế GTGT đủ điều kiện khấu trừ thuế");
                addRow++;
                int i = 10;
                for (BangKeMuaBanDTO item : group1) {
                    row = sheet.createRow(i);
                    style.setFont(fontDf);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                    cell1.setCellValue(item.getInvoiceNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                    cell2.setCellValue(item.getInvoiceDateString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                    cell3.setCellValue(item.getAccountingObjectName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                    cell4.setCellValue(item.getCompanyTaxCode());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                    cell5.setCellValue(item.getDescription());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                    cell6.setCellValue(item.getAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                    cell7.setCellValue(item.getVatRateString());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                    cell8.setCellValue(item.getVatAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                    cell9.setCellValue(item.getVatAccount());
                    i++;
                }
                row = sheet.createRow(group1.size() + 9 + addRow);
                cellMerge = new CellRangeAddress(group1.size() + 9 + addRow, group1.size() + 9 + addRow, 0, 4);
                try {
                    sheet.addMergedRegion(cellMerge);
                } catch (Exception e) {

                }
                Cell cell1 = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cell1.setCellStyle(style);
                Row currRow = sheet.getRow(group1.size() + 9 + addRow);
                currRow.setHeight((short) (currRow.getHeight() * 2));
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue("Cộng nhóm: Hàng hoá, dịch vụ dùng riêng cho SXKD chịu thuế GTGT và sử dụng" +
                    " cho các hoạt động cung cấp hàng hoá, dịch vụ không kê khai, nộp thuế GTGT đủ điều kiện khấu trừ thuế");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                cell2.setCellValue(totalAmountVatGroup1.doubleValue());

                Cell cell3 = row.createCell(6);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                cell3.setCellValue("");

                Cell cell4 = row.createCell(7);
                cell4.setCellStyle(style);
                cell4.setCellValue(totalVatGroup1Amount.doubleValue());

                Cell cell5 = row.createCell(8);
                cell5.setCellStyle(style);
                cell5.setCellValue("");
            }
            if (group2.size() > 0) {
                Row row = sheet.createRow(group1.size() + 9 + addRow);
                CellRangeAddress cellMerge = new CellRangeAddress(group1.size() + 9 + addRow, group1.size() + 9 + addRow, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào: 2.Hàng hóa, dịch vụ dùng chung cho SXKD chịu thuế và không chịu thuế đủ điều kiện khấu trừ thuế");
                addRow++;
                int i = 9 + group1.size() + addRow;
                for (BangKeMuaBanDTO item : group2) {
                    style.setFont(fontDf);

                    row = sheet.createRow(i);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                    cell1.setCellValue(item.getInvoiceNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                    cell2.setCellValue(item.getInvoiceDateString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                    cell3.setCellValue(item.getAccountingObjectName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                    cell4.setCellValue(item.getCompanyTaxCode());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                    cell5.setCellValue(item.getDescription());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                    cell6.setCellValue(item.getAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                    cell7.setCellValue(item.getVatRateString());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                    cell8.setCellValue(item.getVatAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                    cell9.setCellValue(item.getVatAccount());
                    i++;
                }
                int indexTotal = group1.size() + group2.size() + addRow + 9;
                row = sheet.createRow(indexTotal);
                cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                sheet.addMergedRegion(cellMerge);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue("Cộng nhóm: Hàng hóa, dịch vụ dùng chung cho SXKD chịu thuế và không chịu thuế đủ điều kiện khấu trừ thuế");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                cell2.setCellValue(totalAmountVatGroup2.doubleValue());

                Cell cell3 = row.createCell(6);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue("");

                Cell cell4 = row.createCell(7);
                cell4.setCellStyle(style);
                cell4.setCellValue(totalVatGroup2Amount.doubleValue());

                Cell cell5 = row.createCell(8);
                cell5.setCellStyle(style);
                cell5.setCellValue("");

            }
            if (group3.size() > 0) {
                Row row = sheet.createRow(group1.size() + group2.size() + 9 + addRow);
                CellRangeAddress cellMerge = new CellRangeAddress(group1.size() + group2.size() + 9 + addRow,
                    group1.size() + group2.size() + 9 + addRow, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào: 3.Hàng hóa, dịch vụ dùng cho dự án đầu tư đủ điều kiện khấu trừ thuế");
                addRow++;
                int i = 9 + group1.size() + group2.size() + addRow;
                for (BangKeMuaBanDTO item : group3) {
                    style.setFont(fontDf);
                    row = sheet.createRow(i);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                    cell1.setCellValue(item.getInvoiceNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                    cell2.setCellValue(item.getInvoiceDateString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                    cell3.setCellValue(item.getAccountingObjectName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                    cell4.setCellValue(item.getCompanyTaxCode());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                    cell5.setCellValue(item.getDescription());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                    cell6.setCellValue(item.getAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                    cell7.setCellValue(item.getVatRateString());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                    cell8.setCellValue(item.getVatAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                    cell9.setCellValue(item.getVatAccount());
                    i++;
                }
                int indexTotal = group1.size() + group2.size() + group3.size() + addRow + 9;
                row = sheet.createRow(indexTotal);
                cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                sheet.addMergedRegion(cellMerge);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue("Cộng nhóm: Hàng hóa, dịch vụ dùng cho dự án đầu tư đủ điều kiện khấu trừ thuế");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                cell2.setCellValue(totalAmountVatGroup3.doubleValue());

                Cell cell3 = row.createCell(6);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue("");

                Cell cell4 = row.createCell(7);
                cell4.setCellStyle(style);
                cell4.setCellValue(totalVatGroup3Amount.doubleValue());

                Cell cell5 = row.createCell(8);
                cell5.setCellStyle(style);
                cell5.setCellValue("");
            }
            if (group4.size() > 0) {
                Row row = sheet.createRow(group1.size() + group2.size() + group3.size() + 9 + addRow);
                CellRangeAddress cellMerge = new CellRangeAddress(group1.size() + group2.size() + group3.size() + 9 + addRow,
                    group1.size() + group2.size() + group3.size() + 9 + addRow, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào: 4.Hàng hóa, dịch vụ không đủ điều kiện khấu trừ");
                addRow++;
                int i = 9 + group1.size() + group2.size() + group3.size() + addRow;
                for (BangKeMuaBanDTO item : group4) {
                    style.setFont(fontDf);
                    row = sheet.createRow(i);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                    cell1.setCellValue(item.getInvoiceNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                    cell2.setCellValue(item.getInvoiceDateString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                    cell3.setCellValue(item.getAccountingObjectName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                    cell4.setCellValue(item.getCompanyTaxCode());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                    cell5.setCellValue(item.getDescription());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                    cell6.setCellValue(item.getAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                    cell7.setCellValue(item.getVatRateString());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                    cell8.setCellValue(item.getVatAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                    cell9.setCellValue(item.getVatAccount());
                    i++;
                }
                int indexTotal = group1.size() + group2.size() + group3.size() + group4.size() + addRow + 9;
                row = sheet.createRow(indexTotal);
                cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue("Cộng nhóm: Hàng hóa, dịch vụ không đủ điều kiện khấu trừ");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                cell2.setCellValue(totalAmountVatGroup4.doubleValue());

                Cell cell3 = row.createCell(6);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue("");

                Cell cell4 = row.createCell(7);
                cell4.setCellStyle(style);
                cell4.setCellValue(totalVatGroup4Amount.doubleValue());

                Cell cell5 = row.createCell(8);
                cell5.setCellStyle(style);
                cell5.setCellValue("");
            }
            if (group5.size() > 0) {
                Row row = sheet.createRow(group1.size() + group2.size() + group3.size() + group4.size() + 9 + addRow);
                CellRangeAddress cellMerge = new CellRangeAddress(group1.size() + group2.size() + group3.size() + group4.size() + 9 + addRow,
                    group1.size() + group2.size() + group3.size() + group4.size() + 9 + addRow, 0, 8);
                sheet.addMergedRegion(cellMerge);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Nhóm HHDV mua vào: 5.Hàng hóa, dịch vụ không phải tổng hợp trên tờ khai 01/GTGT");
                addRow++;
                int i = 9 + group1.size() + group2.size() + group3.size() + group4.size() + addRow;
                for (BangKeMuaBanDTO item : group5) {
                    style.setFont(fontDf);
                    row = sheet.createRow(i);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                    cell1.setCellValue(item.getInvoiceNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                    cell2.setCellValue(item.getInvoiceDateString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                    cell3.setCellValue(item.getAccountingObjectName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                    cell4.setCellValue(item.getCompanyTaxCode());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                    cell5.setCellValue(item.getDescription());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                    cell6.setCellValue(item.getAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                    cell7.setCellValue(item.getVatRateString());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                    cell8.setCellValue(item.getVatAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                    cell9.setCellValue(item.getVatAccount());
                    i++;
                }
                int indexTotal = group1.size() + group2.size() + group3.size() + group4.size() + group5.size() + addRow + 9;
                row = sheet.createRow(indexTotal);
                cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
                sheet.addMergedRegion(cellMerge);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue("Cộng nhóm: Hàng hóa, dịch vụ không phải tổng hợp trên tờ khai 01/GTGT");
                addRow++;
                Cell cell2 = row.createCell(5);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                cell2.setCellValue(totalAmountVatGroup5.doubleValue());

                Cell cell3 = row.createCell(6);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue("");

                Cell cell4 = row.createCell(7);
                cell4.setCellStyle(style);
                cell4.setCellValue(totalVatGroup5Amount.doubleValue());

                Cell cell5 = row.createCell(8);
                cell5.setCellStyle(style);
                cell5.setCellValue("");
            }

            int indexTotal = group1.size() + group2.size() + group3.size() + group4.size() + group5.size() + addRow + 9;
            Row row = sheet.createRow(indexTotal);
                /*Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                row.createCell(0).setCellValue("Tổng cộng");*/
            CellRangeAddress cellMerge = new CellRangeAddress(indexTotal, indexTotal, 0, 4);
            sheet.addMergedRegion(cellMerge);
            Cell cell0 = row.createCell(0);
            cell0.setCellStyle(style);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellMerge, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellMerge, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellMerge, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellMerge, sheet);
            CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
            fontB.setBold(true);
            cell0.setCellValue("Tổng cộng");
            addRow++;
            Cell cell2 = row.createCell(5);
            cell2.setCellStyle(style);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            cell2.setCellValue(totalAmount.doubleValue());

            Cell cell3 = row.createCell(6);
            cell3.setCellStyle(style);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.setCellValue("");

            Cell cell4 = row.createCell(7);
            cell4.setCellStyle(style);
            cell4.setCellValue(totalVATAmount.doubleValue());

            Cell cell5 = row.createCell(8);
            cell5.setCellStyle(style);
            cell5.setCellValue("");
            try {
                SetFooterExcel(workbook, sheet, userDTO, (addRow + 10 + bangKeMuaBanDTOS.size()), 7, 0, 3, 7);
                Row rowReporter = sheet.getRow(addRow + 11 + bangKeMuaBanDTOS.size() + 1);
                rowReporter.getCell(0).setCellValue("Nguời lập");
            } catch (NullPointerException e) {

            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getExcelSoNhatKyThuChi(RequestReport requestReport, String path) {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<Type> types = typeRepository.findAllByIsActive();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SAInvoiceViewDTO> saInvoiceDTOS = saInvoiceRepository.getAllSAInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<SaReturnDTO> saReturnDTOS = saReturnRepository.getAllSAReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<ViewPPInvoiceDTO> ppInvoiceDTOS = ppInvoiceRepository.getAllPPInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        List<PPDiscountReturnDTO> ppDiscountReturnDTOS = ppDiscountReturnRepository.getAllPPDiscountReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), phienSoLamViec);
        UUID bankAccountID = bankAccountDetailsRepository.findIdByBankAccount(requestReport.getBankAccountDetail(), userDTO.getOrganizationUnit().getId());
        List<SoNhatKyThuTienDTO> nhatKyThuTienDTOS = reportBusinessRepository.getSoNhatKyThuChi(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getSimilarSum(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            bankAccountID,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getDependent(),
            requestReport.getTypeReport(), requestReport.getGetAmountOriginal());
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        for (int i = 0; i < nhatKyThuTienDTOS.size(); i++) {
            nhatKyThuTienDTOS.get(i).setAmountString(Utils.formatTien(nhatKyThuTienDTOS.get(i).getAmount(), typeAmount, userDTO));
            totalAmount = totalAmount.add(nhatKyThuTienDTOS.get(i).getAmount());
            nhatKyThuTienDTOS.get(i).setCol2String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol2(), typeAmount, userDTO));
            totalCol2 = totalCol2.add(nhatKyThuTienDTOS.get(i).getCol2());
            nhatKyThuTienDTOS.get(i).setCol3String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol3(), typeAmount, userDTO));
            totalCol3 = totalCol3.add(nhatKyThuTienDTOS.get(i).getCol3());
            nhatKyThuTienDTOS.get(i).setCol4String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol4(), typeAmount, userDTO));
            totalCol4 = totalCol4.add(nhatKyThuTienDTOS.get(i).getCol4());
            nhatKyThuTienDTOS.get(i).setCol5String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol5(), typeAmount, userDTO));
            totalCol5 = totalCol5.add(nhatKyThuTienDTOS.get(i).getCol5());
            nhatKyThuTienDTOS.get(i).setCol6String(Utils.formatTien(nhatKyThuTienDTOS.get(i).getCol6(), typeAmount, userDTO));
            totalCol6 = totalCol6.add(nhatKyThuTienDTOS.get(i).getCol6());
            nhatKyThuTienDTOS.get(i).setDateString(convertDate(nhatKyThuTienDTOS.get(i).getDate()));
            nhatKyThuTienDTOS.get(i).setPostedDateString(convertDate(nhatKyThuTienDTOS.get(i).getPostedDate()));
            nhatKyThuTienDTOS.get(i).setBreakPage(false);
            nhatKyThuTienDTOS.get(i).setLinkRef(getRefLink(nhatKyThuTienDTOS.get(i).getRefType(),
                nhatKyThuTienDTOS.get(i).getRefID(),
                types, saInvoiceDTOS, saReturnDTOS, ppInvoiceDTOS, ppDiscountReturnDTOS));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (nhatKyThuTienDTOS.size() == 0) {
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                try {
                    SetFooterExcel(workbook, sheet, userDTO, (19 + nhatKyThuTienDTOS.size()), 9, 0, 4, 9);
                    CellStyle styleFooter = workbook.createCellStyle();
                    styleFooter.setWrapText(true);
                    Font fontB = workbook.createFont();
                    fontB.setFontName("Times New Roman");
                    fontB.setBold(true);
                    styleFooter.setFont(fontB);
                    sheet.getRow(19 + nhatKyThuTienDTOS.size() + 1).getCell(0).setCellValue("Người ghi sổ");
                    sheet.getRow(19 + nhatKyThuTienDTOS.size() + 3).getCell(0).setCellValue("");
                    sheet.getRow(19 + nhatKyThuTienDTOS.size() + 3).getCell(4).setCellValue("");
                    sheet.getRow(19 + nhatKyThuTienDTOS.size() + 3).getCell(9).setCellValue("");
                    Row row = sheet.createRow(19 + nhatKyThuTienDTOS.size() + 5);
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                        Cell cellRecorder = row.createCell(0);
                        cellRecorder.setCellStyle(styleFooter);
                        CellUtil.setAlignment(cellRecorder, HorizontalAlignment.CENTER);
                        Cell cellChiefAccountant = row.createCell(4);
                        cellChiefAccountant.setCellStyle(styleFooter);
                        CellUtil.setAlignment(cellChiefAccountant, HorizontalAlignment.CENTER);
                        Cell cellDirector = row.createCell(9);
                        cellDirector.setCellStyle(styleFooter);
                        CellUtil.setAlignment(cellDirector, HorizontalAlignment.CENTER);
                        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                            cellRecorder.setCellValue(userDTO.getFullName());
                        } else {
                            cellRecorder.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                        }
                        cellChiefAccountant.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                        cellDirector.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                    }
                } catch (Exception e) {

                }
                workbook.write(bos);
                workbook.close();
            } catch (Exception e) {

            }
            return bos.toByteArray();
        }
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = nhatKyThuTienDTOS.size();
            sheet.shiftRows(13, sheet.getLastRowNum(), rows - 1);
            // fill dữ liệu vào file
            int i = 13;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            CellStyle style1 = workbook.createCellStyle();
            style.setWrapText(true);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            String accDebit;
            sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            if (nhatKyThuTienDTOS.size() > 0) {
                String[] accountList = nhatKyThuTienDTOS.get(0).getAccountNumberList().split(",");
                String accountDebit = accountList[0];
                if (accountList.length > 1) {
                    String account2 = accountList[1];
                    sheet.getRow(10).getCell(5).setCellValue(account2);
                }
                if (accountList.length > 2) {
                    String account3 = accountList[2];
                    sheet.getRow(10).getCell(6).setCellValue(account3);
                }
                if (accountList.length > 3) {
                    String account4 = accountList[3];
                    sheet.getRow(10).getCell(7).setCellValue(account4);
                }
                if (accountList.length > 4) {
                    String account5 = accountList[4];
                    sheet.getRow(10).getCell(8).setCellValue(account5);
                }
            }
            sheet.getRow(9).getCell(4).setCellValue("Ghi có TK" + requestReport.getAccountNumber());

            //
            for (SoNhatKyThuTienDTO item : nhatKyThuTienDTOS) {
                if (Boolean.TRUE.equals(fontB.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                Row row = sheet.createRow(i);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                style.setFont(fontDf);
                CellUtil.setVerticalAlignment(cell1, VerticalAlignment.TOP);
                cell1.setCellValue(item.getPostedDateString());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                CellUtil.setVerticalAlignment(cell2, VerticalAlignment.TOP);
                cell2.setCellValue(item.getNo());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                CellUtil.setVerticalAlignment(cell3, VerticalAlignment.TOP);
                cell3.setCellValue(item.getDateString());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                CellUtil.setVerticalAlignment(cell4, VerticalAlignment.TOP);
                cell4.setCellValue(item.getDescription());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell5, VerticalAlignment.TOP);
                cell5.setCellValue(item.getAmount().doubleValue());

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell6, VerticalAlignment.TOP);
                cell6.setCellValue(item.getCol2().doubleValue());

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell7, VerticalAlignment.TOP);
                cell7.setCellValue(item.getCol3().doubleValue());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell8, VerticalAlignment.TOP);
                cell8.setCellValue(item.getCol4().doubleValue());

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell9, VerticalAlignment.TOP);
                cell9.setCellValue(item.getCol5().doubleValue());

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell10, VerticalAlignment.TOP);
                cell10.setCellValue(item.getCol6().doubleValue());

                Cell cell11 = row.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                CellUtil.setVerticalAlignment(cell11, VerticalAlignment.TOP);
                cell11.setCellValue(item.getColOtherAccount());
                i++;
            }
            if (Boolean.TRUE.equals(fontB.getBold())) {
                style.setFont(fontB);
            } else {
                style.setFont(fontDf);
            }
            Row rowSum = sheet.createRow(13 + nhatKyThuTienDTOS.size());
            rowSum.createCell(0);
            rowSum.getCell(0).setCellStyle(style);
            rowSum.getCell(0).setCellValue("Cộng");

            rowSum.createCell(1);
            rowSum.getCell(1).setCellStyle(style);
            rowSum.createCell(2);
            rowSum.getCell(2).setCellStyle(style);
            rowSum.createCell(3);
            rowSum.getCell(3).setCellStyle(style);

            Cell cell5 = rowSum.createCell(4);
            cell5.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.setCellValue(totalAmount.doubleValue());

            Cell cell6 = rowSum.createCell(5);
            cell6.setCellStyle(style);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalCol2.doubleValue());

            Cell cell7 = rowSum.createCell(6);
            cell7.setCellStyle(style);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalCol3.doubleValue());

            Cell cell8 = rowSum.createCell(7);
            cell8.setCellStyle(style);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalCol4.doubleValue());

            Cell cell9 = rowSum.createCell(8);
            cell9.setCellStyle(style);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            cell9.setCellValue(totalCol5.doubleValue());

            Cell cell10 = rowSum.createCell(9);
            cell10.setCellStyle(style);
            CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
            cell10.setCellValue(totalCol6.doubleValue());

            Cell cell11 = rowSum.createCell(10);
            cell11.setCellStyle(style);
            CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
            cell11.setCellValue("");

            Row rowSummary = sheet.createRow(15 + nhatKyThuTienDTOS.size());
            style1.setFont(fontDf);
            Cell cellSum = rowSummary.createCell(0);
            cellSum.setCellStyle(style1);
            cellSum.setCellValue("Số này có:.....trang, đánh số từ số 01 đến trang số.....");

            Row rowExportDate = sheet.createRow(16 + nhatKyThuTienDTOS.size());
            Cell cellDate = rowExportDate.createCell(0);
            cellDate.setCellStyle(style1);
            rowExportDate.getCell(0).setCellValue("Ngày mở sổ:.......");
            try {
                SetFooterExcel(workbook, sheet, userDTO, (18 + nhatKyThuTienDTOS.size()), 9, 0, 4, 9);
                CellStyle styleFooter = workbook.createCellStyle();
                styleFooter.setWrapText(true);
                styleFooter.setFont(fontB);
                sheet.getRow(18 + nhatKyThuTienDTOS.size() + 1).getCell(0).setCellValue("Người ghi sổ");
                sheet.getRow(18 + nhatKyThuTienDTOS.size() + 3).getCell(0).setCellValue("");
                sheet.getRow(18 + nhatKyThuTienDTOS.size() + 3).getCell(4).setCellValue("");
                sheet.getRow(18 + nhatKyThuTienDTOS.size() + 3).getCell(9).setCellValue("");
                Row row = sheet.createRow(18 + nhatKyThuTienDTOS.size() + 5);
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                    Cell cellRecorder = row.createCell(0);
                    cellRecorder.setCellStyle(styleFooter);
                    CellUtil.setAlignment(cellRecorder, HorizontalAlignment.CENTER);
                    Cell cellChiefAccountant = row.createCell(4);
                    cellChiefAccountant.setCellStyle(styleFooter);
                    CellUtil.setAlignment(cellChiefAccountant, HorizontalAlignment.CENTER);
                    Cell cellDirector = row.createCell(9);
                    cellDirector.setCellStyle(styleFooter);
                    CellUtil.setAlignment(cellDirector, HorizontalAlignment.CENTER);
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                        cellRecorder.setCellValue(userDTO.getFullName());
                    } else {
                        cellRecorder.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                    }
                    cellChiefAccountant.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                    cellDirector.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                }
            } catch (Exception e) {

            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return bos.toByteArray();
    }

    private byte[] getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiMaThongKeTheoKhoanMucChiPhi";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        StringBuilder statisticsCodesString = new StringBuilder();
        StringBuilder expenseItemsString = new StringBuilder();
        for (UUID item : requestReport.getStatisticsCodes()) {
            statisticsCodesString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        for (UUID item : requestReport.getExpenseItems()) {
            expenseItemsString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<TheoDoiMaThongKeTheoKhoanMucChiPhiDTO> theoDoiMaThongKeTheoKhoanMucChiPhiDTOS = reportBusinessRepository.getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            statisticsCodesString.toString(),
            expenseItemsString.toString(),
            requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec);
        if (theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.size() == 0) {
            if (requestReport.getStatisticsCodes().size() == 1) {
                statisticsCodeRepository.findById(requestReport.getStatisticsCodes().get(0)).ifPresent(mg -> {
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                });
            } else {
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
            }
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        checkEbPackage(userDTO, parameter);
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.size());

        List<Type> types = typeRepository.findAllByIsActive();

        if (theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.size() > 0) {
            for (TheoDoiMaThongKeTheoKhoanMucChiPhiDTO snk : theoDoiMaThongKeTheoKhoanMucChiPhiDTOS) {
                if (snk.getSoDauKy() != null && BigDecimal.ZERO.compareTo(snk.getSoDauKy()) != 0) {
                    snk.setSoDauKyString(Utils.formatTien(snk.getSoDauKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoDauKyString("");
                }
                if (snk.getSoPhatSinh() != null && BigDecimal.ZERO.compareTo(snk.getSoPhatSinh()) != 0) {
                    snk.setSoPhatSinhString(Utils.formatTien(snk.getSoPhatSinh(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoPhatSinhString("");
                }
                if (snk.getLuyKeCuoiKy() != null && BigDecimal.ZERO.compareTo(snk.getLuyKeCuoiKy()) != 0) {
                    snk.setLuyKeCuoiKyString(Utils.formatTien(snk.getLuyKeCuoiKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setLuyKeCuoiKyString("");
                }
                if (snk.getTongSoDauKy() != null && BigDecimal.ZERO.compareTo(snk.getTongSoDauKy()) != 0) {
                    snk.setTongSoDauKyString(Utils.formatTien(snk.getTongSoDauKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongSoDauKyString("");
                }
                if (snk.getTongSoPhatSinh() != null && BigDecimal.ZERO.compareTo(snk.getTongSoPhatSinh()) != 0) {
                    snk.setTongSoPhatSinhString(Utils.formatTien(snk.getTongSoPhatSinh(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongSoPhatSinhString("");
                }
                if (snk.getTongLuyKeCuoiKy() != null && BigDecimal.ZERO.compareTo(snk.getTongLuyKeCuoiKy()) != 0) {
                    snk.setTongLuyKeCuoiKyString(Utils.formatTien(snk.getTongLuyKeCuoiKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongLuyKeCuoiKyString("");
                }
            }
        }

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
//        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
//        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(theoDoiMaThongKeTheoKhoanMucChiPhiDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoTheoDoiMaThongKeTheoKhoanMucChiPhi(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        StringBuilder statisticsCodesString = new StringBuilder();
        StringBuilder expenseItemsString = new StringBuilder();
        for (UUID item : requestReport.getStatisticsCodes()) {
            statisticsCodesString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        for (UUID item : requestReport.getExpenseItems()) {
            expenseItemsString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<TheoDoiMaThongKeTheoKhoanMucChiPhiDTO> theoDoiMaThongKeTheoKhoanMucChiPhiDTOS = reportBusinessRepository.getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            statisticsCodesString.toString(),
            expenseItemsString.toString(),
            requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec);
        if (theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.size() == 0) {
            if (requestReport.getStatisticsCodes().size() == 1) {
                statisticsCodeRepository.findById(requestReport.getStatisticsCodes().get(0)).ifPresent(mg -> {
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                });
            } else {
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
                theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.add(new TheoDoiMaThongKeTheoKhoanMucChiPhiDTO());
            }
        }
        List<String> listStatisticsCode = new ArrayList<>();
            for (TheoDoiMaThongKeTheoKhoanMucChiPhiDTO snk : theoDoiMaThongKeTheoKhoanMucChiPhiDTOS) {
                if (snk.getSoDauKy() != null && BigDecimal.ZERO.compareTo(snk.getSoDauKy()) != 0) {
                    snk.setSoDauKyString(Utils.formatTien(snk.getSoDauKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoDauKyString("");
                }
                if (snk.getSoPhatSinh() != null && BigDecimal.ZERO.compareTo(snk.getSoPhatSinh()) != 0) {
                    snk.setSoPhatSinhString(Utils.formatTien(snk.getSoPhatSinh(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoPhatSinhString("");
                }
                if (snk.getLuyKeCuoiKy() != null && BigDecimal.ZERO.compareTo(snk.getLuyKeCuoiKy()) != 0) {
                    snk.setLuyKeCuoiKyString(Utils.formatTien(snk.getLuyKeCuoiKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setLuyKeCuoiKyString("");
                }
                if (snk.getTongSoDauKy() != null && BigDecimal.ZERO.compareTo(snk.getTongSoDauKy()) != 0) {
                    snk.setTongSoDauKyString(Utils.formatTien(snk.getTongSoDauKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongSoDauKyString("");
                }
                if (snk.getTongSoPhatSinh() != null && BigDecimal.ZERO.compareTo(snk.getTongSoPhatSinh()) != 0) {
                    snk.setTongSoPhatSinhString(Utils.formatTien(snk.getTongSoPhatSinh(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongSoPhatSinhString("");
                }
                if (snk.getTongLuyKeCuoiKy() != null && BigDecimal.ZERO.compareTo(snk.getTongLuyKeCuoiKy()) != 0) {
                    snk.setTongLuyKeCuoiKyString(Utils.formatTien(snk.getTongLuyKeCuoiKy(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongLuyKeCuoiKyString("");
                }
            if (!listStatisticsCode.contains(snk.getStatisticsCode())) {
                listStatisticsCode.add(snk.getStatisticsCode());
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            for (int index = 0; index < listStatisticsCode.size(); index++) {
                String statisticsCode = listStatisticsCode.get(index);
                Sheet sheet = workbook.cloneSheet(0);
                sheet.shiftRows(8, sheet.getLastRowNum(), theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.size());
                List<TheoDoiMaThongKeTheoKhoanMucChiPhiDTO> theoDoiMaThongKeByStatisticsCode = new ArrayList<>();
                if (statisticsCode != null) {
                    theoDoiMaThongKeTheoKhoanMucChiPhiDTOS.stream().filter(n -> n.getStatisticsCode().equals(statisticsCode)).
                        forEach(theoDoiMaThongKeByStatisticsCode::add);
                } else {
                    theoDoiMaThongKeByStatisticsCode.addAll(theoDoiMaThongKeTheoKhoanMucChiPhiDTOS);
                }

                // fill dữ liệu vào file
                int i = 8;
                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                Font fontI = workbook.createFont();
                fontI.setItalic(true);
                fontI.setFontName("Times New Roman");
                CellStyle cellStyleB = workbook.createCellStyle();
                cellStyleB.setBorderBottom(BorderStyle.THIN);
                cellStyleB.setBorderTop(BorderStyle.THIN);
                cellStyleB.setBorderRight(BorderStyle.THIN);
                cellStyleB.setBorderLeft(BorderStyle.THIN);
                cellStyleB.setFont(fontB);
                cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Cell cellHeader0 = sheet.getRow(0).getCell(0);
                Cell cellHeader1 = sheet.getRow(1).getCell(0);
                Cell cellHeader2 = sheet.getRow(2).getCell(0);
                // CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
                CellStyle styleHeaderI = workbook.createCellStyle();
                styleHeaderI.setWrapText(true);
                styleHeaderI.setFont(fontI);

                CellStyle styleHeaderB = workbook.createCellStyle();
                styleHeaderB.setWrapText(true);
                styleHeaderB.setFont(fontB);

                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                cellHeader0.setCellStyle(styleHeaderB);
                cellHeader1.setCellStyle(styleHeaderI);
                cellHeader2.setCellStyle(styleHeaderI);

                Cell cellDate = sheet.getRow(4).getCell(0);
                CellStyle styleI = workbook.createCellStyle();
                styleI.setFont(fontI);
                cellDate.setCellStyle(styleI);
                CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
                cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

                String code = theoDoiMaThongKeByStatisticsCode.get(0).getStatisticsCode();
                String name = theoDoiMaThongKeByStatisticsCode.get(0).getStatisticsCodeName();
                Cell cellSP = sheet.getRow(5).getCell(0);
                CellUtil.setVerticalAlignment(cellSP, VerticalAlignment.CENTER);
                StringBuilder codeName = new StringBuilder();
                codeName.append("Mã thống kê: ");
                if (code != null) {
                    codeName.append(code);
                    if (name != null) {
                        codeName.append(" - " + name);
                    }
                } else {
                    codeName.append("....-....");
                }
                cellSP.setCellValue(codeName.toString());

                for (TheoDoiMaThongKeTheoKhoanMucChiPhiDTO item : theoDoiMaThongKeByStatisticsCode) {
                    Row row = sheet.createRow(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getExpenseItemCode());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getExpenseItemName());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    //cell3.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (item.getSoDauKy() != null && item.getSoDauKy().doubleValue() > 0) {
                        cell3.setCellValue(item.getSoDauKy().doubleValue());
                    }

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                    //cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (item.getSoPhatSinh() != null && item.getSoPhatSinh().doubleValue() > 0) {
                        cell4.setCellValue(item.getSoPhatSinh().doubleValue());
                    }

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    //cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (item.getLuyKeCuoiKy() != null && item.getLuyKeCuoiKy().doubleValue() > 0) {
                        cell5.setCellValue(item.getLuyKeCuoiKy().doubleValue());
                    }

                    i++;
                }
                int j = 8 + theoDoiMaThongKeByStatisticsCode.size();
                Row row = sheet.createRow(j);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                cell0.setCellValue("Cộng");
                if (code != null) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(j, j, 0, 1);
                    sheet.addMergedRegion(cellRangeAddress);
                }

                Cell cell1 = row.createCell(2);
                cell1.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                //cell1.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (theoDoiMaThongKeByStatisticsCode.get(0).getTongSoDauKy() != null
                    && theoDoiMaThongKeByStatisticsCode.get(0).getTongSoDauKy().doubleValue() > 0) {
                    cell1.setCellValue(theoDoiMaThongKeByStatisticsCode.get(0).getTongSoDauKy().doubleValue());
                }

                Cell cell2 = row.createCell(3);
                cell2.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                //cell2.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (theoDoiMaThongKeByStatisticsCode.get(0).getTongSoPhatSinh() != null
                    && theoDoiMaThongKeByStatisticsCode.get(0).getTongSoPhatSinh().doubleValue() > 0) {
                    cell2.setCellValue(theoDoiMaThongKeByStatisticsCode.get(0).getTongSoPhatSinh().doubleValue());
                }

                Cell cell3 = row.createCell(4);
                cell3.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                //cell3.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (theoDoiMaThongKeByStatisticsCode.get(0).getTongLuyKeCuoiKy() != null
                    && theoDoiMaThongKeByStatisticsCode.get(0).getTongLuyKeCuoiKy().doubleValue() > 0) {
                    cell3.setCellValue(theoDoiMaThongKeByStatisticsCode.get(0).getTongLuyKeCuoiKy().doubleValue());
                }
                veBorder(row, style, Stream.of(1).collect(Collectors.toList()));

                SetFooterAdditionalExcel(workbook, sheet, userDTO, (9 + theoDoiMaThongKeByStatisticsCode.size()), 4, 0, 2, 4, "Người ghi sổ", "Kế toán", "Giám đốc");
            }
            workbook.removeSheetAt(0);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getSoTheoDoiMaThongKeTheoTaiKhoan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiMaThongKeTheoTaiKhoan";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        StringBuilder statisticsCodesString = new StringBuilder();
        for (UUID item : requestReport.getStatisticsCodes()) {
            statisticsCodesString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<TheoDoiMaThongKeTheoTaiKhoanDTO> theoDoiMaThongKeTheoTaiKhoanDTOS = reportBusinessRepository.getSoTheoDoiMaThongKeTheoTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            statisticsCodesString.toString(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec);
        if (theoDoiMaThongKeTheoTaiKhoanDTOS.size() == 0) {
            if (requestReport.getStatisticsCodes().size() == 1) {
                statisticsCodeRepository.findById(requestReport.getStatisticsCodes().get(0)).ifPresent(mg -> {
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                });
            } else {
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
            }
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        checkEbPackage(userDTO, parameter);
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", theoDoiMaThongKeTheoTaiKhoanDTOS.size());

        List<Type> types = typeRepository.findAllByIsActive();

        if (theoDoiMaThongKeTheoTaiKhoanDTOS.size() > 0) {
            for (TheoDoiMaThongKeTheoTaiKhoanDTO snk : theoDoiMaThongKeTheoTaiKhoanDTOS) {
                if (snk.getSoTienNo() != null && BigDecimal.ZERO.compareTo(snk.getSoTienNo()) != 0) {
                    snk.setSoTienNoString(Utils.formatTien(snk.getSoTienNo(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoTienNoString("");
                }
                if (snk.getSoTienCo() != null && BigDecimal.ZERO.compareTo(snk.getSoTienCo()) != 0) {
                    snk.setSoTienCoString(Utils.formatTien(snk.getSoTienCo(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setSoTienCoString("");
                }
                if (snk.getTongNo() != null && BigDecimal.ZERO.compareTo(snk.getTongNo()) != 0) {
                    snk.setTongNoString(Utils.formatTien(snk.getTongNo(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongNoString("");
                }
                if (snk.getTongCo() != null && BigDecimal.ZERO.compareTo(snk.getTongCo()) != 0) {
                    snk.setTongCoString(Utils.formatTien(snk.getTongCo(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setTongCoString("");
                }
                snk.setNgayChungTuString(convertDate(snk.getNgayChungTu()));
                snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));
                snk.setLinkRef(getRefLink(snk.getRefType(), snk.getRefID(), types, null, null, null, null));
            }
        }

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
//        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
//        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(theoDoiMaThongKeTheoTaiKhoanDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoTheoDoiMaThongKeTheoTaiKhoan(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        StringBuilder statisticsCodesString = new StringBuilder();
        for (UUID item : requestReport.getStatisticsCodes()) {
            statisticsCodesString.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<TheoDoiMaThongKeTheoTaiKhoanDTO> theoDoiMaThongKeTheoTaiKhoanDTOS = reportBusinessRepository.getSoTheoDoiMaThongKeTheoTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            statisticsCodesString.toString(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec);
        if (theoDoiMaThongKeTheoTaiKhoanDTOS.size() == 0) {
            if (requestReport.getStatisticsCodes().size() == 1) {
                statisticsCodeRepository.findById(requestReport.getStatisticsCodes().get(0)).ifPresent(mg -> {
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                    theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO(mg.getStatisticsCode(), mg.getStatisticsCodeName()));
                });
            } else {
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
                theoDoiMaThongKeTheoTaiKhoanDTOS.add(new TheoDoiMaThongKeTheoTaiKhoanDTO());
            }
        }
        List<String> listStatisticsCode = new ArrayList<>();
        Boolean isShow = false;
        for (TheoDoiMaThongKeTheoTaiKhoanDTO snk : theoDoiMaThongKeTheoTaiKhoanDTOS) {
            if (snk.getSoTienNo() != null && BigDecimal.ZERO.compareTo(snk.getSoTienNo()) != 0) {
                snk.setSoTienNoString(Utils.formatTien(snk.getSoTienNo(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setSoTienNoString("");
            }
            if (snk.getSoTienCo() != null && BigDecimal.ZERO.compareTo(snk.getSoTienCo()) != 0) {
                snk.setSoTienCoString(Utils.formatTien(snk.getSoTienCo(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setSoTienCoString("");
            }
            if (snk.getTongNo() != null && BigDecimal.ZERO.compareTo(snk.getTongNo()) != 0) {
                snk.setTongNoString(Utils.formatTien(snk.getTongNo(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setTongNoString("");
            }
            if (snk.getTongCo() != null && BigDecimal.ZERO.compareTo(snk.getTongCo()) != 0) {
                snk.setTongCoString(Utils.formatTien(snk.getTongCo(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setTongCoString("");
            }
            snk.setNgayChungTuString(convertDate(snk.getNgayChungTu()));
            snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));

            if (!listStatisticsCode.contains(snk.getStatisticsCode())) {
                listStatisticsCode.add(snk.getStatisticsCode());
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            for (int index = 0; index < listStatisticsCode.size(); index++) {
                String statisticsCode = listStatisticsCode.get(index);
                Sheet sheet = workbook.cloneSheet(0);
                sheet.shiftRows(8, sheet.getLastRowNum(), theoDoiMaThongKeTheoTaiKhoanDTOS.size());
                List<TheoDoiMaThongKeTheoTaiKhoanDTO> theoDoiMaThongKeByStatisticsCode = new ArrayList<>();
                if (statisticsCode != null) {
                    theoDoiMaThongKeTheoTaiKhoanDTOS.stream().filter(n -> n.getStatisticsCode().equals(statisticsCode)).
                        forEach(theoDoiMaThongKeByStatisticsCode::add);
                } else {
                    theoDoiMaThongKeByStatisticsCode.addAll(theoDoiMaThongKeTheoTaiKhoanDTOS);
                }

                // fill dữ liệu vào file
                int i = 8;
                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                Font fontI = workbook.createFont();
                fontI.setItalic(true);
                fontI.setFontName("Times New Roman");
                CellStyle cellStyleB = workbook.createCellStyle();
                cellStyleB.setBorderBottom(BorderStyle.THIN);
                cellStyleB.setBorderTop(BorderStyle.THIN);
                cellStyleB.setBorderRight(BorderStyle.THIN);
                cellStyleB.setBorderLeft(BorderStyle.THIN);
                cellStyleB.setFont(fontB);
                cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // set header
                Cell cellHeader0 = sheet.getRow(0).getCell(0);
                Cell cellHeader1 = sheet.getRow(1).getCell(0);
                Cell cellHeader2 = sheet.getRow(2).getCell(0);
                // CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
                CellStyle styleHeaderI = workbook.createCellStyle();
                styleHeaderI.setWrapText(true);
                styleHeaderI.setFont(fontI);

                CellStyle styleHeaderB = workbook.createCellStyle();
                styleHeaderB.setWrapText(true);
                styleHeaderB.setFont(fontB);

                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                cellHeader0.setCellStyle(styleHeaderB);
                cellHeader1.setCellStyle(styleHeaderI);
                cellHeader2.setCellStyle(styleHeaderI);

                Cell cellDate = sheet.getRow(4).getCell(0);
                CellStyle styleI = workbook.createCellStyle();
                styleI.setFont(fontI);
                cellDate.setCellStyle(styleI);
                CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
                cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

                String code = theoDoiMaThongKeByStatisticsCode.get(0).getStatisticsCode();
                String name = theoDoiMaThongKeByStatisticsCode.get(0).getStatisticsCodeName();
                Cell cellSP = sheet.getRow(5).getCell(0);
                CellUtil.setVerticalAlignment(cellSP, VerticalAlignment.CENTER);
                StringBuilder codeName = new StringBuilder();
                codeName.append("Mã thống kê: ");
                if (code != null) {
                    codeName.append(code);
                    if (name != null) {
                        codeName.append(" - " + name);
                    }
                } else {
                    codeName.append("....-....");
                }
                cellSP.setCellValue(codeName.toString());

                for (TheoDoiMaThongKeTheoTaiKhoanDTO item : theoDoiMaThongKeByStatisticsCode) {
                    Row row = sheet.createRow(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getNgayChungTuString());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue(item.getNgayHachToanString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                    cell3.setCellValue(item.getSoChungTu());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(item.getDienGiai());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                    cell5.setCellValue(item.getTk());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                    cell6.setCellValue(item.getTkDoiUng());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    //cell7.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (item.getSoTienNo() != null && item.getSoTienNo().doubleValue() > 0) {
                        cell7.setCellValue(item.getSoTienNo().doubleValue());
                    }

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    //cell8.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (item.getSoTienCo() != null && item.getSoTienCo().doubleValue() > 0) {
                        cell8.setCellValue(item.getSoTienCo().doubleValue());
                    }

                    i++;
                }
                int j = 8 + theoDoiMaThongKeByStatisticsCode.size();
                Row row = sheet.createRow(j);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                cell0.setCellValue("Cộng");
                if (code != null) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(j + 1, j + 1, 0, 5);
                    sheet.addMergedRegion(cellRangeAddress);
                }

                Cell cell1 = row.createCell(6);
                cell1.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                //cell1.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (theoDoiMaThongKeByStatisticsCode.get(0).getTongNo() != null
                    && theoDoiMaThongKeByStatisticsCode.get(0).getTongNo().doubleValue() > 0) {
                    cell1.setCellValue(theoDoiMaThongKeByStatisticsCode.get(0).getTongNo().doubleValue());
                }

                Cell cell2 = row.createCell(7);
                cell2.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                //cell2.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (theoDoiMaThongKeByStatisticsCode.get(0).getTongCo() != null
                    && theoDoiMaThongKeByStatisticsCode.get(0).getTongCo().doubleValue() > 0) {
                    cell2.setCellValue(theoDoiMaThongKeByStatisticsCode.get(0).getTongCo().doubleValue());
                }
                veBorder(row, style, Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList()));

                SetFooterAdditionalExcel(workbook, sheet, userDTO, (9 + theoDoiMaThongKeByStatisticsCode.size()), 7, 1, 4, 7, "Người lập", "Kế toán", "Giám đốc");
            }
            workbook.removeSheetAt(0);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getPhanBoChiPhiTraTruoc(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "PhanBoChiPhiTraTruoc";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<PhanBoChiPhiTraTruocDTO> phanBoChiPhiTraTruocDTOS = reportBusinessRepository.getPhanBoChiPhiTraTruoc(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        checkEbPackage(userDTO, parameter);
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalAllocationTime = BigDecimal.ZERO;
        BigDecimal totalAllocatedPeriod = BigDecimal.ZERO;
        BigDecimal totalAllocatedAmount = BigDecimal.ZERO;
        BigDecimal totalAllocatedPeriodAfter = BigDecimal.ZERO;
        BigDecimal totalAllocatedPeriodRest = BigDecimal.ZERO;
        BigDecimal totalAccumulated = BigDecimal.ZERO;
        BigDecimal totalAmountRest = BigDecimal.ZERO;
        if (phanBoChiPhiTraTruocDTOS.size() > 0) {
            for (PhanBoChiPhiTraTruocDTO snk : phanBoChiPhiTraTruocDTOS) {
                if (snk.getAmount() != null && BigDecimal.ZERO.compareTo(snk.getAmount()) != 0) {
                    snk.setAmountToString(Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAmount = totalAmount.add(snk.getAmount());
                } else {
                    snk.setAmountToString(null);
                }
                if (snk.getAllocationTime() != null && BigDecimal.ZERO.compareTo(snk.getAllocationTime()) != 0) {
                    snk.setAllocationTimeToString(snk.getAllocationTime() != null ? Utils.formatTien(snk.getAllocationTime(), Constants.SystemOption.DDSo_TienVND, userDTO) : null);
                    totalAllocationTime = totalAllocationTime.add(snk.getAllocationTime());
                } else {
                    snk.setAllocationTimeToString(null);
                }
                if (snk.getAllocatedPeriod() != null && BigDecimal.ZERO.compareTo(snk.getAllocatedPeriod()) != 0) {
                    snk.setAllocatedPeriodToString(Utils.formatTien(snk.getAllocatedPeriod(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllocatedPeriod = totalAllocatedPeriod.add(snk.getAllocatedPeriod());
                } else {
                    snk.setAllocatedPeriodToString(null);
                }
                if (snk.getAllocatedAmount() != null && BigDecimal.ZERO.compareTo(snk.getAllocatedAmount()) != 0) {
                    snk.setAllocatedAmountToString(Utils.formatTien(snk.getAllocatedAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllocatedAmount = totalAllocatedAmount.add(snk.getAllocatedAmount());
                } else {
                    snk.setAllocatedAmountToString(null);
                }
                if (snk.getAllocatedPeriodAfter() != null && BigDecimal.ZERO.compareTo(snk.getAllocatedPeriodAfter()) != 0) {
                    snk.setAllocatedPeriodAfterToString(Utils.formatTien(snk.getAllocatedPeriodAfter(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllocatedPeriodAfter = totalAllocatedPeriodAfter.add(snk.getAllocatedPeriodAfter());
                } else {
                    snk.setAllocatedPeriodAfterToString(null);
                }
                if (snk.getAllocatedPeriodRest() != null && BigDecimal.ZERO.compareTo(snk.getAllocatedPeriodRest()) != 0) {
                    snk.setAllocatedPeriodRestToString(Utils.formatTien(snk.getAllocatedPeriodRest(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllocatedPeriodRest = totalAllocatedPeriodRest.add(snk.getAllocatedPeriodRest());
                } else {
                    snk.setAllocatedPeriodRestToString(null);
                }
                if (snk.getAccumulated() != null && BigDecimal.ZERO.compareTo(snk.getAccumulated()) != 0) {
                    snk.setAccumulatedToString(Utils.formatTien(snk.getAccumulated(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAccumulated = totalAccumulated.add(snk.getAccumulated());
                } else {
                    snk.setAccumulatedToString(null);
                }
                if (snk.getAmountRest() != null && BigDecimal.ZERO.compareTo(snk.getAmountRest()) != 0) {
                    snk.setAmountRestToString(Utils.formatTien(snk.getAmountRest(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAmountRest = totalAmountRest.add(snk.getAmountRest());
                } else {
                    snk.setAmountRestToString(null);
                }

            }
        } else {
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
            totalAmount = null;
            totalAllocationTime = null;
            totalAllocatedPeriod = null;
            totalAllocatedAmount = null;
            totalAllocatedPeriodAfter = null;
            totalAllocatedPeriodRest = null;
            totalAccumulated = null;
            totalAmountRest = null;
        }
        parameter.put("REPORT_MAX_COUNT", phanBoChiPhiTraTruocDTOS.size());
        parameter.put("totalAmount", Utils.formatTien(totalAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllocationTime", Utils.formatTien(totalAllocationTime, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllocatedPeriod", Utils.formatTien(totalAllocatedPeriod, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllocatedAmount", Utils.formatTien(totalAllocatedAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllocatedPeriodRest", Utils.formatTien(totalAllocatedPeriodRest, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAccumulated", Utils.formatTien(totalAccumulated, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAmountRest", Utils.formatTien(totalAmountRest, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllocatedPeriodAfter", Utils.formatTien(totalAllocatedPeriodAfter, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(phanBoChiPhiTraTruocDTOS, parameter, jasperReport);

        return result;
    }

    private byte[] getSoChiTietBanHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoChiTietBanHang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;

        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        String accountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoChiTietBanHangDTO> soChiTietBanHangDTOS = reportBusinessRepository.getSoChiTietBanHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountingObjects,
            materialGoods,
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soChiTietBanHangDTOS.size() == 0) {
            if (requestReport.getListMaterialGoods().size() == 1) {
                materialGoodsRepository.findById(requestReport.getListMaterialGoods().get(0)).ifPresent(mg -> {
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                });
            } else {
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
            }
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));

        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", soChiTietBanHangDTOS.size());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());

        List<Type> types = typeRepository.findAllByIsActive();
        for (SoChiTietBanHangDTO snk : soChiTietBanHangDTOS) {
            if (snk.getSoLuong() != null && BigDecimal.ZERO.compareTo(snk.getSoLuong()) != 0) {
                snk.setSoLuongString(Utils.formatTien(snk.getSoLuong(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            } else {
                snk.setSoLuongString("");
            }

            if (snk.getDonGia() != null && BigDecimal.ZERO.compareTo(snk.getDonGia()) != 0) {
                snk.setDonGiaString(Utils.formatTien(snk.getDonGia(), Constants.SystemOption.DDSo_DonGia, userDTO));
            } else {
                snk.setDonGiaString("");
            }

            if (snk.getThanhTien() != null && BigDecimal.ZERO.compareTo(snk.getThanhTien()) != 0) {
                snk.setThanhTienString(Utils.formatTien(snk.getThanhTien(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setThanhTienString("");
            }

            if (snk.getThue() != null && BigDecimal.ZERO.compareTo(snk.getThue()) != 0) {
                snk.setThueString(Utils.formatTien(snk.getThue(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setThueString("");
            }

            if (snk.getKhac() != null && BigDecimal.ZERO.compareTo(snk.getKhac()) != 0) {
                snk.setKhacString(Utils.formatTien(snk.getKhac(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setKhacString("");
            }

            if (snk.getGiaVon() != null && BigDecimal.ZERO.compareTo(snk.getGiaVon()) != 0) {
                snk.setGiaVonString(Utils.formatTien(snk.getGiaVon(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setGiaVonString("");
            }

            if (snk.getTongSoLuong() != null && BigDecimal.ZERO.compareTo(snk.getTongSoLuong()) != 0) {
                snk.setTongSoLuongString(Utils.formatTien(snk.getTongSoLuong(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            } else {
                snk.setTongSoLuongString("");
            }

            if (snk.getTongThanhTien() != null && BigDecimal.ZERO.compareTo(snk.getTongThanhTien()) != 0) {
                snk.setTongThanhTienString(Utils.formatTien(snk.getTongThanhTien(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setTongThanhTienString("");
            }

            if (snk.getTongThue() != null && BigDecimal.ZERO.compareTo(snk.getTongThue()) != 0) {
                snk.setTongThueString(Utils.formatTien(snk.getTongThue(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setTongThueString("");
            }

            if (snk.getTongKhac() != null && BigDecimal.ZERO.compareTo(snk.getTongKhac()) != 0) {
                snk.setTongKhacString(Utils.formatTien(snk.getTongKhac(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setTongKhacString("");
            }

            if (snk.getDoanhThuThuan() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuThuan()) != 0) {
                snk.setDoanhThuThuanString(Utils.formatTien(snk.getDoanhThuThuan(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setDoanhThuThuanString("");
            }

            if (snk.getLaiGop() != null && BigDecimal.ZERO.compareTo(snk.getLaiGop()) != 0) {
                snk.setLaiGopString(Utils.formatTien(snk.getLaiGop(), Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                snk.setLaiGopString("");
            }

            snk.setLinkRef(getRefLink(snk.getTypeID(), snk.getRefID(), types, null, null, null, null));
            snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));
            snk.setNgayHoaDonString(convertDate(snk.getNgayHoaDon()));
        }
//        parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
//        parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soChiTietBanHangDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelSoChiTietBanHang(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        String accountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        List<SoChiTietBanHangDTO> soChiTietBanHangDTOS = reportBusinessRepository.getSoChiTietBanHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountingObjects,
            materialGoods,
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soChiTietBanHangDTOS.size() == 0) {
            if (requestReport.getListMaterialGoods().size() == 1) {
                materialGoodsRepository.findById(requestReport.getListMaterialGoods().get(0)).ifPresent(mg -> {
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                    soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO(mg.getMaterialGoodsName(), mg.getMaterialGoodsCode()));
                });
            } else {
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
                soChiTietBanHangDTOS.add(new SoChiTietBanHangDTO());
            }
        }
        List<String> listMaterialCode = new ArrayList<>();
        Boolean isShow = false;
        for (SoChiTietBanHangDTO snk : soChiTietBanHangDTOS) {
            snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));
            snk.setNgayHoaDonString(convertDate(snk.getNgayHoaDon()));

            if (!listMaterialCode.contains(snk.getMaterialGoodsCode())) {
                listMaterialCode.add(snk.getMaterialGoodsCode());
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            for (int index = 0; index < listMaterialCode.size(); index++) {
                String materialCode = listMaterialCode.get(index);
                Sheet sheet = workbook.cloneSheet(0);
                sheet.shiftRows(10, sheet.getLastRowNum(), soChiTietBanHangDTOS.size());
                List<SoChiTietBanHangDTO> soChiTietBanHangDTOSbyMaterialCode = new ArrayList<>();
                if (materialCode != null) {
                    soChiTietBanHangDTOS.stream().filter(n -> n.getMaterialGoodsCode().equals(materialCode)).
                        forEach(soChiTietBanHangDTOSbyMaterialCode::add);
                } else {
                    soChiTietBanHangDTOSbyMaterialCode.addAll(soChiTietBanHangDTOS);
                }

                // fill dữ liệu vào file
                int i = 10;
                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle cellStyleB = workbook.createCellStyle();
                cellStyleB.setBorderBottom(BorderStyle.THIN);
                cellStyleB.setBorderTop(BorderStyle.THIN);
                cellStyleB.setBorderRight(BorderStyle.THIN);
                cellStyleB.setBorderLeft(BorderStyle.THIN);
                cellStyleB.setFont(fontB);
                cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setWrapText(true);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

                Cell cellDate = sheet.getRow(4).getCell(0);
                CellStyle styleI = workbook.createCellStyle();
                Font fontI = workbook.createFont();
                styleI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                styleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                fontI.setItalic(true);
                fontI.setFontName("Times New Roman");
                styleI.setFont(fontI);
                cellDate.setCellStyle(styleI);
                CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
                cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

                String code = soChiTietBanHangDTOSbyMaterialCode.get(0).getMaterialGoodsCode();
                String name = soChiTietBanHangDTOSbyMaterialCode.get(0).getMaterialGoodsName();
                String codeName = "";
                if (code != null) {
                    codeName += code;
                    if (name != null) {
                        codeName += " - " + name;
                    }
                }
                Cell cellSP = sheet.getRow(5).getCell(0);
                CellUtil.setVerticalAlignment(cellSP, VerticalAlignment.CENTER);
                cellSP.setCellValue(codeName);

                for (SoChiTietBanHangDTO item : soChiTietBanHangDTOSbyMaterialCode) {
                    Row row = sheet.createRow(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getNgayHachToanString());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getSoHieu());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                    cell3.setCellValue(item.getNgayCTuString());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(item.getDienGiai());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    cell5.setCellValue(item.getTkDoiUng());

                    Cell cell6 = row.createCell(5);
                    //cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    if (item.getSoLuong() != null && item.getSoLuong().doubleValue() != 0) {
                        showThapPhan(item.getSoLuong().doubleValue(), workbook, cell6, false);
                    } else {
                        cell6.setCellStyle(style);
                    }

                    Cell cell7 = row.createCell(6);
                    //cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    if (item.getDonGia() != null && item.getDonGia().doubleValue() != 0) {
                        showThapPhan(item.getDonGia().doubleValue(), workbook, cell7, false);
                    } else {
                        cell7.setCellStyle(style);
                    }

                    Cell cell8 = row.createCell(7);
                    //cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    if (item.getThanhTien() != null && item.getThanhTien().doubleValue() != 0) {
                        showThapPhan(item.getThanhTien().doubleValue(), workbook, cell8, false);
                    } else {
                        cell8.setCellStyle(style);
                    }

                    Cell cell9 = row.createCell(8);
                    //cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    if (item.getThue() != null && item.getThue().doubleValue() != 0) {
                        showThapPhan(item.getThue().doubleValue(), workbook, cell9, false);
                    } else {
                        cell9.setCellStyle(style);
                    }

                    Cell cell10 = row.createCell(9);
                    //cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    if (item.getKhac() != null && item.getKhac().doubleValue() != 0) {
                        showThapPhan(item.getKhac().doubleValue(), workbook, cell10, false);
                    } else {
                        cell10.setCellStyle(style);
                    }

                    i++;
                }
                int j = 10 + soChiTietBanHangDTOSbyMaterialCode.size();
                Row row = sheet.createRow(j);
                Cell cell0 = row.createCell(3);
                cell0.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue("Cộng số phát sinh");

                Cell cell1 = row.createCell(5);
                //cell1.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                if (soChiTietBanHangDTOSbyMaterialCode.get(0).getTongSoLuong() != null
                        && soChiTietBanHangDTOSbyMaterialCode.get(0).getTongSoLuong().doubleValue() != 0) {
                    showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getTongSoLuong().doubleValue(), workbook, cell1, true);
                } else {
                    cell1.setCellStyle(cellStyleB);
                }

                Cell cell2 = row.createCell(7);
                //cell2.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                if (soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThanhTien() != null
                        && soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThanhTien().doubleValue() != 0) {
                    showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThanhTien().doubleValue(), workbook, cell2, true);
                } else {
                    cell2.setCellStyle(cellStyleB);
                }

                Cell cell3 = row.createCell(8);
                //cell3.setCellStyle(cellStyleB);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                if (soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThue() != null
                    && soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThue().doubleValue() != 0) {
                    showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getTongThue().doubleValue(), workbook, cell3, true);
                } else {
                    cell3.setCellStyle(cellStyleB);
                }

                Cell cell4 = row.createCell(9);
                //cell4.setCellStyle(cellStyleB);
//                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                if (soChiTietBanHangDTOSbyMaterialCode.get(0).getTongKhac() != null
                        && soChiTietBanHangDTOSbyMaterialCode.get(0).getTongKhac().doubleValue() != 0) {
                    showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getTongKhac().doubleValue(), workbook, cell4, true);
                } else {
                    cell4.setCellStyle(cellStyleB);
                }
                veBorder(row, style, Stream.of(0, 1, 2, 4, 6).collect(Collectors.toList()));

                for (int k = j + 1; k < j + 4; k++) {
                    Row row1 = sheet.createRow(k);
                    Cell cell5 = row1.createCell(3);
                    cell5.setCellStyle(cellStyleB);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);

                    Cell cell6 = row1.createCell(7);
                    //cell6.setCellStyle(cellStyleB);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);

                    if (j == k - 1) {
                        cell5.setCellValue(" - Doanh thu thuần");
                        if (soChiTietBanHangDTOSbyMaterialCode.get(0).getDoanhThuThuan() != null
                             && soChiTietBanHangDTOSbyMaterialCode.get(0).getDoanhThuThuan().doubleValue() != 0) {
                            showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getDoanhThuThuan().doubleValue(), workbook, cell6, true);
                        } else {
                            cell6.setCellStyle(cellStyleB);
                        }
                    } else if (j == k - 2) {
                        cell5.setCellValue(" - Giá vốn hàng bán");
                        if (soChiTietBanHangDTOSbyMaterialCode.get(0).getGiaVon() != null &&
                            soChiTietBanHangDTOSbyMaterialCode.get(0).getGiaVon().doubleValue() != 0) {
                            showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getGiaVon().doubleValue(), workbook, cell6, true);
                        } else {
                            cell6.setCellStyle(cellStyleB);
                        }
                    } else if (j == k - 3) {
                        cell5.setCellValue(" - Lãi gộp");
                        if (soChiTietBanHangDTOSbyMaterialCode.get(0).getLaiGop() != null &&
                            soChiTietBanHangDTOSbyMaterialCode.get(0).getLaiGop().doubleValue() != 0) {
                            showThapPhan(soChiTietBanHangDTOSbyMaterialCode.get(0).getLaiGop().doubleValue(), workbook, cell6, true);
                        } else {
                            cell6.setCellStyle(cellStyleB);
                        }
                    }
                    veBorder(row1, style, Stream.of(0, 1, 2, 4, 5, 6, 8, 9).collect(Collectors.toList()));
                }
                SetFooterAdditionalExcel(workbook, sheet, userDTO, (14 + soChiTietBanHangDTOSbyMaterialCode.size()), 8, 1, 4, 8, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            }
            workbook.removeSheetAt(0);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getSoChiTietMuaHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoChiTietMuaHang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;

        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        String accountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoChiTietMuaHangDTO> soChiTietMuaHangDTOS = reportBusinessRepository.getSoChiTietMuaHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountingObjects,
            materialGoods,
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getEmployeeID(),
            requestReport.getDependent());
        if (soChiTietMuaHangDTOS.size() == 0) {
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", soChiTietMuaHangDTOS.size());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());

        BigDecimal tongSoLuongMua = BigDecimal.ZERO;
        BigDecimal tongGiaTriMua = BigDecimal.ZERO;
        BigDecimal tongChietKhau = BigDecimal.ZERO;
        BigDecimal tongSoLuongTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriGiamGia = BigDecimal.ZERO;

        List<Type> types = typeRepository.findAllByIsActive();
        for (SoChiTietMuaHangDTO snk : soChiTietMuaHangDTOS) {
            if (snk.getSoLuongMua() != null && BigDecimal.ZERO.compareTo(snk.getSoLuongMua()) != 0) {
                snk.setSoLuongMuaString(Utils.formatTien(snk.getSoLuongMua(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                tongSoLuongMua = tongSoLuongMua.add(snk.getSoLuongMua());
            } else {
                snk.setSoLuongMuaString("");
            }

            if (snk.getDonGia() != null && BigDecimal.ZERO.compareTo(snk.getDonGia()) != 0) {
                snk.setDonGiaString(Utils.formatTien(snk.getDonGia(), Constants.SystemOption.DDSo_DonGia, userDTO));

            } else {
                snk.setDonGiaString("");
            }

            if (snk.getGiaTriMua() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriMua()) != 0) {
                snk.setGiaTriMuaString(Utils.formatTien(snk.getGiaTriMua(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriMua = tongGiaTriMua.add(snk.getGiaTriMua());
            } else {
                snk.setGiaTriMuaString("");
            }

            if (snk.getChietKhau() != null && BigDecimal.ZERO.compareTo(snk.getChietKhau()) != 0) {
                snk.setChietKhauString(Utils.formatTien(snk.getChietKhau(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongChietKhau = tongChietKhau.add(snk.getChietKhau());
            } else {
                snk.setChietKhauString("");
            }

            if (snk.getSoLuongTraLai() != null && BigDecimal.ZERO.compareTo(snk.getSoLuongTraLai()) != 0) {
                snk.setSoLuongTraLaiString(Utils.formatTien(snk.getSoLuongTraLai(), Constants.SystemOption.DDSo_SoLuong, userDTO));
                tongSoLuongTraLai = tongSoLuongTraLai.add(snk.getSoLuongTraLai());
            } else {
                snk.setSoLuongTraLaiString("");
            }

            if (snk.getGiaTriTraLai() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriTraLai()) != 0) {
                snk.setGiaTriTraLaiString(Utils.formatTien(snk.getGiaTriTraLai(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriTraLai = tongGiaTriTraLai.add(snk.getGiaTriTraLai());
            } else {
                snk.setGiaTriTraLaiString("");
            }

            if (snk.getGiaTriGiamGia() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriGiamGia()) != 0) {
                snk.setGiaTriGiamGiaString(Utils.formatTien(snk.getGiaTriGiamGia(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriGiamGia = tongGiaTriGiamGia.add(snk.getGiaTriGiamGia());
            } else {
                snk.setGiaTriGiamGiaString("");
            }

            snk.setLinkRef(getRefLink(snk.getTypeID(), snk.getRefID(), types, null, null, null, null));
            snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));
            snk.setNgayHoaDonString(convertDate(snk.getNgayHoaDon()));
        }

        parameter.put("tongSoLuongMua", Utils.formatTien(tongSoLuongMua, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongGiaTriMua", Utils.formatTien(tongGiaTriMua, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongChietKhau", Utils.formatTien(tongChietKhau, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongSoLuongTraLai", Utils.formatTien(tongSoLuongTraLai, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongGiaTriTraLai", Utils.formatTien(tongGiaTriTraLai, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongGiaTriGiamGia", Utils.formatTien(tongGiaTriGiamGia, Constants.SystemOption.DDSo_TienVND, userDTO));

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soChiTietMuaHangDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoChiTietMuaHang(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        DecimalFormat df = new DecimalFormat("0");
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        String accountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }

        BigDecimal tongSoLuongMua = BigDecimal.ZERO;
        BigDecimal tongGiaTriMua = BigDecimal.ZERO;
        BigDecimal tongChietKhau = BigDecimal.ZERO;
        BigDecimal tongSoLuongTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriGiamGia = BigDecimal.ZERO;
        List<SoChiTietMuaHangDTO> soChiTietMuaHangDTOS = reportBusinessRepository.getSoChiTietMuaHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountingObjects,
            materialGoods,
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getEmployeeID(),
            requestReport.getDependent());
        if (soChiTietMuaHangDTOS.size() == 0) {
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
            soChiTietMuaHangDTOS.add(new SoChiTietMuaHangDTO());
        }
        Boolean isShow = false;
        for (SoChiTietMuaHangDTO snk : soChiTietMuaHangDTOS) {
            snk.setNgayHachToanString(convertDate(snk.getNgayHachToan()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));
            snk.setNgayHoaDonString(convertDate(snk.getNgayHoaDon()));

            if (snk.getNgayCTuString() != null && !isShow) {
                isShow = true;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(8, sheet.getLastRowNum(), soChiTietMuaHangDTOS.size());
            // fill dữ liệu vào file
            int i = 8;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(4).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            styleI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            for (SoChiTietMuaHangDTO item : soChiTietMuaHangDTOS) {
                Row row = sheet.createRow(i);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getNgayHachToanString());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                cell2.setCellValue(item.getNgayCTuString());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                cell3.setCellValue(item.getSoCTu());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                cell4.setCellValue(item.getNgayHoaDonString());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(item.getSoHoaDon());

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.LEFT);
                cell6.setCellValue(item.getMahang());

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.LEFT);
                cell7.setCellValue(item.getTenhang());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.LEFT);
                cell8.setCellValue(item.getDvt());

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                if (item.getSoLuongMua() != null && item.getSoLuongMua().doubleValue() != 0) {
                    cell9.setCellValue(item.getSoLuongMua().doubleValue());
                }

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                if (item.getDonGia() != null && item.getDonGia().doubleValue() != 0) {
                    cell10.setCellValue(item.getDonGia().doubleValue());
                }

                Cell cell11 = row.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                if (item.getGiaTriMua() != null && item.getGiaTriMua().doubleValue() != 0) {
                    cell11.setCellValue(item.getGiaTriMua().doubleValue());
                }

                Cell cell12 = row.createCell(11);
                cell12.setCellStyle(style);
                CellUtil.setAlignment(cell12, HorizontalAlignment.RIGHT);
                if (item.getChietKhau() != null && item.getChietKhau().doubleValue() != 0) {
                    cell12.setCellValue(item.getChietKhau().doubleValue());
                }

                Cell cell13 = row.createCell(12);
                cell13.setCellStyle(style);
                CellUtil.setAlignment(cell13, HorizontalAlignment.RIGHT);
                if (item.getSoLuongTraLai() != null && item.getSoLuongTraLai().doubleValue() != 0) {
                    cell13.setCellValue(item.getSoLuongTraLai().doubleValue());
                }

                Cell cell14 = row.createCell(13);
                cell14.setCellStyle(style);
                CellUtil.setAlignment(cell14, HorizontalAlignment.RIGHT);
                if (item.getGiaTriTraLai() != null && item.getGiaTriTraLai().doubleValue() != 0) {
                    cell14.setCellValue(item.getGiaTriTraLai().doubleValue());
                }

                Cell cell15 = row.createCell(14);
                cell15.setCellStyle(style);
                CellUtil.setAlignment(cell15, HorizontalAlignment.RIGHT);
                if (item.getGiaTriGiamGia() != null && item.getGiaTriGiamGia().doubleValue() != 0) {
                    cell15.setCellValue(item.getGiaTriGiamGia().doubleValue());
                }

                i++;
            }

            Row rowTotal = sheet.getRow(8 + soChiTietMuaHangDTOS.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Tổng cộng");

            Cell cell1 = rowTotal.createCell(8);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            if (tongSoLuongMua.doubleValue() != 0) {
                cell1.setCellValue(tongSoLuongMua.doubleValue());
            }

            Cell cell2 = rowTotal.createCell(10);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            if (tongGiaTriMua.doubleValue() != 0) {
                cell2.setCellValue(tongGiaTriMua.doubleValue());
            }

            Cell cell3 = rowTotal.createCell(11);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            if (tongChietKhau.doubleValue() != 0) {
                cell3.setCellValue(tongChietKhau.doubleValue());
            }

            Cell cell4 = rowTotal.createCell(12);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            if (tongSoLuongTraLai.doubleValue() != 0) {
                cell4.setCellValue(tongSoLuongTraLai.doubleValue());
            }

            Cell cell5 = rowTotal.createCell(13);
            cell5.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            if (tongGiaTriTraLai.doubleValue() != 0) {
                cell5.setCellValue(tongGiaTriTraLai.doubleValue());
            }

            Cell cell6 = rowTotal.createCell(14);
            cell6.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            if (tongGiaTriGiamGia.doubleValue() != 0) {
                cell6.setCellValue(tongGiaTriGiamGia.doubleValue());
            }

            veBorder(rowTotal, style, Stream.of(1, 2, 3, 4, 5, 6, 7, 9).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (9 + soChiTietMuaHangDTOS.size()), 12, 2, 6, 12, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getSoNhatKyBanHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoNhatKyBanHang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoNhatKyBanHangDTO> soNhatKyBanHangDTOS = reportBusinessRepository.getSoNhatKyBanHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soNhatKyBanHangDTOS.size() == 0) {
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));

        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", soNhatKyBanHangDTOS.size());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal tongTien = BigDecimal.ZERO;
        BigDecimal tongHangHoa = BigDecimal.ZERO;
        BigDecimal tongThanhPham = BigDecimal.ZERO;
        BigDecimal tongDichVu = BigDecimal.ZERO;
        BigDecimal tongTroCap = BigDecimal.ZERO;
        BigDecimal tongBDSDautu = BigDecimal.ZERO;
        BigDecimal tongKhac = BigDecimal.ZERO;
        BigDecimal tongChietKhau = BigDecimal.ZERO;
        BigDecimal tongGiaTriTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriGiamGia = BigDecimal.ZERO;
        BigDecimal tongDoanhThuThuan = BigDecimal.ZERO;

        List<Type> types = typeRepository.findAllByIsActive();
        for (SoNhatKyBanHangDTO snk : soNhatKyBanHangDTOS) {
            if (snk.getDoanhThuHangHoa() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuHangHoa()) != 0) {
                snk.setDoanhThuHangHoaString(Utils.formatTien(snk.getDoanhThuHangHoa(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongHangHoa = tongHangHoa.add(snk.getDoanhThuHangHoa());
            } else {
                snk.setDoanhThuHangHoaString("");
            }
            if (snk.getDoanhThuThanhPham() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuThanhPham()) != 0) {
                snk.setDoanhThuThanhPhamString(Utils.formatTien(snk.getDoanhThuThanhPham(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongThanhPham = tongThanhPham.add(snk.getDoanhThuThanhPham());
            } else {
                snk.setDoanhThuThanhPhamString("");
            }
            if (snk.getDoanhThuDichVu() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuDichVu()) != 0) {
                snk.setDoanhThuDichVuString(Utils.formatTien(snk.getDoanhThuDichVu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongDichVu = tongDichVu.add(snk.getDoanhThuDichVu());
            } else {
                snk.setDoanhThuDichVuString("");
            }
            if (snk.getDoanhThuTroCap() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuTroCap()) != 0) {
                snk.setDoanhThuTroCapString(Utils.formatTien(snk.getDoanhThuTroCap(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongTroCap = tongTroCap.add(snk.getDoanhThuTroCap());
            } else {
                snk.setDoanhThuTroCapString("");
            }
            if (snk.getDoanhThuBDSDautu() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuBDSDautu()) != 0) {
                snk.setDoanhThuBDSDautuString(Utils.formatTien(snk.getDoanhThuBDSDautu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongBDSDautu = tongBDSDautu.add(snk.getDoanhThuBDSDautu());
            } else {
                snk.setDoanhThuBDSDautuString("");
            }
            if (snk.getDoanhThuKhac() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuKhac()) != 0) {
                snk.setDoanhThuKhacString(Utils.formatTien(snk.getDoanhThuKhac(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongKhac = tongKhac.add(snk.getDoanhThuKhac());
            } else {
                snk.setDoanhThuKhacString("");
            }
            if (snk.getChietKhau() != null && BigDecimal.ZERO.compareTo(snk.getChietKhau()) != 0) {
                snk.setChietKhauString(Utils.formatTien(snk.getChietKhau(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongChietKhau = tongChietKhau.add(snk.getChietKhau());
            } else {
                snk.setChietKhauString("");
            }
            if (snk.getGiaTriTraLai() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriTraLai()) != 0) {
                snk.setGiaTriTraLaiString(Utils.formatTien(snk.getGiaTriTraLai(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriTraLai = tongGiaTriTraLai.add(snk.getGiaTriTraLai());
            } else {
                snk.setGiaTriTraLaiString("");
            }
            if (snk.getGiaTriGiamGia() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriGiamGia()) != 0) {
                snk.setGiaTriGiamGiaString(Utils.formatTien(snk.getGiaTriGiamGia(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriGiamGia = tongGiaTriGiamGia.add(snk.getGiaTriGiamGia());
            } else {
                snk.setGiaTriGiamGiaString("");
            }

            if (snk.getSum() != null && BigDecimal.ZERO.compareTo(snk.getSum()) != 0) {
                snk.setSumString(Utils.formatTien(snk.getSum(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongTien = tongTien.add(snk.getSum());
            } else {
                snk.setSumString("");
            }
            if (snk.getDoanhThuThuan() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuThuan()) != 0) {
                snk.setDoanhThuThuanString(Utils.formatTien(snk.getDoanhThuThuan(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongDoanhThuThuan = tongDoanhThuThuan.add(snk.getDoanhThuThuan());
            } else {
                snk.setDoanhThuThuanString("");
            }

            snk.setLinkRef(getRefLink(snk.getTypeID(), snk.getRefID(), types, null, null, null, null));
            snk.setPostedDateString(convertDate(snk.getPostedDate()));
            snk.setRefDateString(convertDate(snk.getRefDate()));
            snk.setInvoiceDateString(convertDate(snk.getInvoiceDate()));
        }
        parameter.put("tongTien", Utils.formatTien(tongTien, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongHangHoa", Utils.formatTien(tongHangHoa, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongThanhPham", Utils.formatTien(tongThanhPham, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongDichVu", Utils.formatTien(tongDichVu, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongTroCap", Utils.formatTien(tongTroCap, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongBDSDautu", Utils.formatTien(tongBDSDautu, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongKhac", Utils.formatTien(tongKhac, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongChietKhau", Utils.formatTien(tongChietKhau, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongGiaTriTraLai", Utils.formatTien(tongGiaTriTraLai, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongGiaTriGiamGia", Utils.formatTien(tongGiaTriGiamGia, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongDoanhThuThuan", Utils.formatTien(tongDoanhThuThuan, Constants.SystemOption.DDSo_TienVND, userDTO));

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soNhatKyBanHangDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoNhatKyBanHang(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        DecimalFormat df = new DecimalFormat("0");

        List<SoNhatKyBanHangDTO> soNhatKyBanHangDTOS = reportBusinessRepository.getSoNhatKyBanHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soNhatKyBanHangDTOS.size() == 0) {
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
            soNhatKyBanHangDTOS.add(new SoNhatKyBanHangDTO());
        }

        BigDecimal tongTien = BigDecimal.ZERO;
        BigDecimal tongHangHoa = BigDecimal.ZERO;
        BigDecimal tongThanhPham = BigDecimal.ZERO;
        BigDecimal tongDichVu = BigDecimal.ZERO;
        BigDecimal tongTroCap = BigDecimal.ZERO;
        BigDecimal tongBDSDautu = BigDecimal.ZERO;
        BigDecimal tongKhac = BigDecimal.ZERO;
        BigDecimal tongChietKhau = BigDecimal.ZERO;
        BigDecimal tongGiaTriTraLai = BigDecimal.ZERO;
        BigDecimal tongGiaTriGiamGia = BigDecimal.ZERO;
        BigDecimal tongDoanhThuThuan = BigDecimal.ZERO;
        Boolean isShow = false;
        for (SoNhatKyBanHangDTO snk : soNhatKyBanHangDTOS) {
            if (snk.getDoanhThuHangHoa() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuHangHoa()) != 0) {
                snk.setDoanhThuHangHoaString(Utils.formatTien(snk.getDoanhThuHangHoa(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongHangHoa = tongHangHoa.add(snk.getDoanhThuHangHoa());
            } else {
                snk.setDoanhThuHangHoaString("");
            }
            if (snk.getDoanhThuThanhPham() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuThanhPham()) != 0) {
                snk.setDoanhThuThanhPhamString(Utils.formatTien(snk.getDoanhThuThanhPham(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongThanhPham = tongThanhPham.add(snk.getDoanhThuThanhPham());
            } else {
                snk.setDoanhThuThanhPhamString("");
            }
            if (snk.getDoanhThuDichVu() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuDichVu()) != 0) {
                snk.setDoanhThuDichVuString(Utils.formatTien(snk.getDoanhThuDichVu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongDichVu = tongDichVu.add(snk.getDoanhThuDichVu());
            } else {
                snk.setDoanhThuDichVuString("");
            }
            if (snk.getDoanhThuTroCap() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuTroCap()) != 0) {
                snk.setDoanhThuTroCapString(Utils.formatTien(snk.getDoanhThuTroCap(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongTroCap = tongTroCap.add(snk.getDoanhThuTroCap());
            } else {
                snk.setDoanhThuTroCapString("");
            }
            if (snk.getDoanhThuBDSDautu() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuBDSDautu()) != 0) {
                snk.setDoanhThuBDSDautuString(Utils.formatTien(snk.getDoanhThuBDSDautu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongBDSDautu = tongBDSDautu.add(snk.getDoanhThuBDSDautu());
            } else {
                snk.setDoanhThuBDSDautuString("");
            }
            if (snk.getDoanhThuKhac() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuKhac()) != 0) {
                snk.setDoanhThuKhacString(Utils.formatTien(snk.getDoanhThuKhac(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongKhac = tongKhac.add(snk.getDoanhThuKhac());
            } else {
                snk.setDoanhThuKhacString("");
            }
            if (snk.getChietKhau() != null && BigDecimal.ZERO.compareTo(snk.getChietKhau()) != 0) {
                snk.setChietKhauString(Utils.formatTien(snk.getChietKhau(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongChietKhau = tongChietKhau.add(snk.getChietKhau());
            } else {
                snk.setChietKhauString("");
            }
            if (snk.getGiaTriTraLai() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriTraLai()) != 0) {
                snk.setGiaTriTraLaiString(Utils.formatTien(snk.getGiaTriTraLai(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriTraLai = tongGiaTriTraLai.add(snk.getGiaTriTraLai());
            } else {
                snk.setGiaTriTraLaiString("");
            }
            if (snk.getGiaTriGiamGia() != null && BigDecimal.ZERO.compareTo(snk.getGiaTriGiamGia()) != 0) {
                snk.setGiaTriGiamGiaString(Utils.formatTien(snk.getGiaTriGiamGia(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongGiaTriGiamGia = tongGiaTriGiamGia.add(snk.getGiaTriGiamGia());
            } else {
                snk.setGiaTriGiamGiaString("");
            }

            if (snk.getSum() != null && BigDecimal.ZERO.compareTo(snk.getSum()) != 0) {
                snk.setSumString(Utils.formatTien(snk.getSum(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongTien = tongTien.add(snk.getSum());
            } else {
                snk.setSumString("");
            }
            if (snk.getDoanhThuThuan() != null && BigDecimal.ZERO.compareTo(snk.getDoanhThuThuan()) != 0) {
                snk.setDoanhThuThuanString(Utils.formatTien(snk.getDoanhThuThuan(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongDoanhThuThuan = tongDoanhThuThuan.add(snk.getDoanhThuThuan());
            } else {
                snk.setDoanhThuThuanString("");
            }

            snk.setPostedDateString(convertDate(snk.getPostedDate()));
            snk.setRefDateString(convertDate(snk.getRefDate()));
            snk.setInvoiceDateString(convertDate(snk.getInvoiceDate()));

            if (snk.getRefDateString() != null && !isShow) {
                isShow = true;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(8, sheet.getLastRowNum(), soNhatKyBanHangDTOS.size());
            // fill dữ liệu vào file
            int i = 8;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
//            Cell cellHeader = sheet.getRow(0).getCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
//            CellStyle styleHeader = workbook.createCellStyle();
//            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(4).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            styleI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            for (SoNhatKyBanHangDTO item : soNhatKyBanHangDTOS) {
                Row row = sheet.createRow(i);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getPostedDateString());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getRefDateString());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(item.getRefNo());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                cell4.setCellValue(item.getInvoiceDateString());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                cell5.setCellValue(item.getInvoiceNo());

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.LEFT);
                cell6.setCellValue(item.getDescription());

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                if (item.getSum() != null && item.getSum().doubleValue() != 0) {
                    cell7.setCellValue(item.getSum().doubleValue());
                }

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuHangHoa() != null && item.getDoanhThuHangHoa().doubleValue() != 0) {
                    cell8.setCellValue(item.getDoanhThuHangHoa().doubleValue());
                }

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuThanhPham() != null && item.getDoanhThuThanhPham().doubleValue() != 0) {
                    cell9.setCellValue(item.getDoanhThuThanhPham().doubleValue());
                }

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuDichVu() != null && item.getDoanhThuDichVu().doubleValue() != 0) {
                    cell10.setCellValue(item.getDoanhThuDichVu().doubleValue());
                }

                Cell cell11 = row.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuTroCap() != null && item.getDoanhThuTroCap().doubleValue() != 0) {
                    cell11.setCellValue(item.getDoanhThuTroCap().doubleValue());
                }

                Cell cell12 = row.createCell(11);
                cell12.setCellStyle(style);
                CellUtil.setAlignment(cell12, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuBDSDautu() != null && item.getDoanhThuBDSDautu().doubleValue() != 0) {
                    cell12.setCellValue(item.getDoanhThuBDSDautu().doubleValue());
                }

                Cell cell13 = row.createCell(12);
                cell13.setCellStyle(style);
                CellUtil.setAlignment(cell13, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuKhac() != null && item.getDoanhThuKhac().doubleValue() != 0) {
                    cell13.setCellValue(item.getDoanhThuKhac().doubleValue());
                }

                Cell cell14 = row.createCell(13);
                cell14.setCellStyle(style);
                CellUtil.setAlignment(cell14, HorizontalAlignment.RIGHT);
                if (item.getChietKhau() != null && item.getChietKhau().doubleValue() != 0) {
                    cell14.setCellValue(item.getChietKhau().doubleValue());
                }

                Cell cell15 = row.createCell(14);
                cell15.setCellStyle(style);
                CellUtil.setAlignment(cell15, HorizontalAlignment.RIGHT);
                if (item.getGiaTriTraLai() != null && item.getGiaTriTraLai().doubleValue() != 0) {
                    cell15.setCellValue(item.getGiaTriTraLai().doubleValue());
                }

                Cell cell16 = row.createCell(15);
                cell16.setCellStyle(style);
                CellUtil.setAlignment(cell16, HorizontalAlignment.RIGHT);
                if (item.getGiaTriGiamGia() != null && item.getGiaTriGiamGia().doubleValue() != 0) {
                    cell16.setCellValue(item.getGiaTriGiamGia().doubleValue());
                }

                Cell cell17 = row.createCell(16);
                cell17.setCellStyle(style);
                CellUtil.setAlignment(cell17, HorizontalAlignment.RIGHT);
                if (item.getDoanhThuThuan() != null && item.getDoanhThuThuan().doubleValue() != 0) {
                    cell17.setCellValue(item.getDoanhThuThuan().doubleValue());
                }

                Cell cell18 = row.createCell(17);
                cell18.setCellStyle(style);
                CellUtil.setAlignment(cell18, HorizontalAlignment.LEFT);
                cell18.setCellValue(item.getNameCustomer());
                i++;
            }

            Row rowTotal = sheet.getRow(8 + soNhatKyBanHangDTOS.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Tổng cộng");

            Cell cell1 = rowTotal.createCell(6);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            if (tongTien.doubleValue() != 0) {
                cell1.setCellValue(tongTien.doubleValue());
            }

            Cell cell2 = rowTotal.createCell(7);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            if (tongHangHoa.doubleValue() != 0) {
                cell2.setCellValue(tongHangHoa.doubleValue());
            }


            Cell cell3 = rowTotal.createCell(8);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            if (tongThanhPham.doubleValue() != 0) {
                cell3.setCellValue(tongThanhPham.doubleValue());
            }

            Cell cell4 = rowTotal.createCell(9);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            if (tongDichVu.doubleValue() != 0) {
                cell4.setCellValue(tongDichVu.doubleValue());
            }

            Cell cell5 = rowTotal.createCell(10);
            cell5.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            if (tongTroCap.doubleValue() != 0) {
                cell5.setCellValue(tongTroCap.doubleValue());
            }

            Cell cell6 = rowTotal.createCell(11);
            cell6.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            if (tongBDSDautu.doubleValue() != 0) {
                cell6.setCellValue(tongBDSDautu.doubleValue());
            }

            Cell cell7 = rowTotal.createCell(12);
            cell7.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            if (tongKhac.doubleValue() != 0) {
                cell7.setCellValue(tongKhac.doubleValue());
            }

            Cell cell8 = rowTotal.createCell(13);
            cell8.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            if (tongChietKhau.doubleValue() != 0) {
                cell8.setCellValue(tongChietKhau.doubleValue());
            }

            Cell cell9 = rowTotal.createCell(14);
            cell9.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            if (tongGiaTriTraLai.doubleValue() != 0) {
                cell9.setCellValue(tongGiaTriTraLai.doubleValue());
            }

            Cell cell10 = rowTotal.createCell(15);
            cell10.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
            if (tongGiaTriGiamGia.doubleValue() != 0) {
                cell10.setCellValue(tongGiaTriGiamGia.doubleValue());
            }

            Cell cell11 = rowTotal.createCell(16);
            cell11.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
            if (tongDoanhThuThuan.doubleValue() != 0) {
                cell11.setCellValue(tongDoanhThuThuan.doubleValue());
            }

            veBorder(rowTotal, style, Stream.of(1, 2, 3, 4, 5, 17).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (9 + soNhatKyBanHangDTOS.size()), 14, 2, 7, 14, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private byte[] getSoNhatKyMuaHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoNhatKyMuaHang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoNhatKyMuaHangDTO> soNhatKyMuaHangDTOS = reportBusinessRepository.getSoNhatKyMuaHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soNhatKyMuaHangDTOS.size() == 0) {
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
        }
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));

        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", soNhatKyMuaHangDTOS.size());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal tongHangHoa = BigDecimal.ZERO;
        BigDecimal tongPhaitraNguoiBan = BigDecimal.ZERO;
        BigDecimal tongNguyenVatLieu = BigDecimal.ZERO;
        BigDecimal tongSoTien = BigDecimal.ZERO;

        List<Type> types = typeRepository.findAllByIsActive();
        for (SoNhatKyMuaHangDTO snk : soNhatKyMuaHangDTOS) {
            if (snk.getHangHoa() != null && BigDecimal.ZERO.compareTo(snk.getHangHoa()) != 0) {
                snk.setHangHoaString(Utils.formatTien(snk.getHangHoa(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongHangHoa = tongHangHoa.add(snk.getHangHoa());
            } else {
                snk.setHangHoaString("");
            }
            if (snk.getPhaiTraNguoiBan() != null && BigDecimal.ZERO.compareTo(snk.getPhaiTraNguoiBan()) != 0) {
                snk.setPhaiTraNguoiBanString(Utils.formatTien(snk.getPhaiTraNguoiBan(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongPhaitraNguoiBan = tongPhaitraNguoiBan.add(snk.getPhaiTraNguoiBan());
            } else {
                snk.setPhaiTraNguoiBanString("");
            }
            if (snk.getNguyenVatLieu() != null && BigDecimal.ZERO.compareTo(snk.getNguyenVatLieu()) != 0) {
                snk.setNguyenVatLieuString(Utils.formatTien(snk.getNguyenVatLieu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongNguyenVatLieu = tongNguyenVatLieu.add(snk.getNguyenVatLieu());
            } else {
                snk.setNguyenVatLieuString("");
            }
            if (snk.getSoTien() != null && BigDecimal.ZERO.compareTo(snk.getSoTien()) != 0) {
                snk.setSoTienString(Utils.formatTien(snk.getSoTien(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongSoTien = tongSoTien.add(snk.getSoTien());
            } else {
                snk.setSoTienString("");
            }

            snk.setLinkRef(getRefLink(snk.getTypeID(), snk.getReferenceID(), types, null, null, null, null));
            snk.setNgayGhiSoString(convertDate(snk.getNgayGhiSo()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));

        }
        parameter.put("tongHangHoa", Utils.formatTien(tongHangHoa, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongPhaitraNguoiBan", Utils.formatTien(tongPhaitraNguoiBan, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongNguyenVatLieu", Utils.formatTien(tongNguyenVatLieu, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongSoTien", Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soNhatKyMuaHangDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoNhatKyMuaHang(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        DecimalFormat df = new DecimalFormat("0");

        List<SoNhatKyMuaHangDTO> soNhatKyMuaHangDTOS = reportBusinessRepository.getSoNhatKyMuaHang(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (soNhatKyMuaHangDTOS.size() == 0) {
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
            soNhatKyMuaHangDTOS.add(new SoNhatKyMuaHangDTO());
        }

        BigDecimal tongHangHoa = BigDecimal.ZERO;
        BigDecimal tongPhaitraNguoiBan = BigDecimal.ZERO;
        BigDecimal tongNguyenVatLieu = BigDecimal.ZERO;
        BigDecimal tongSoTien = BigDecimal.ZERO;
        Boolean isShow = false;
        for (SoNhatKyMuaHangDTO snk : soNhatKyMuaHangDTOS) {
            if (snk.getHangHoa() != null && BigDecimal.ZERO.compareTo(snk.getHangHoa()) != 0) {
                snk.setHangHoaString(Utils.formatTien(snk.getHangHoa(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongHangHoa = tongHangHoa.add(snk.getHangHoa());
            } else {
                snk.setHangHoaString("");
            }
            if (snk.getPhaiTraNguoiBan() != null && BigDecimal.ZERO.compareTo(snk.getPhaiTraNguoiBan()) != 0) {
                snk.setPhaiTraNguoiBanString(Utils.formatTien(snk.getPhaiTraNguoiBan(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongPhaitraNguoiBan = tongPhaitraNguoiBan.add(snk.getPhaiTraNguoiBan());
            } else {
                snk.setPhaiTraNguoiBanString("");
            }
            if (snk.getNguyenVatLieu() != null && BigDecimal.ZERO.compareTo(snk.getNguyenVatLieu()) != 0) {
                snk.setNguyenVatLieuString(Utils.formatTien(snk.getNguyenVatLieu(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongNguyenVatLieu = tongNguyenVatLieu.add(snk.getNguyenVatLieu());
            } else {
                snk.setNguyenVatLieuString("");
            }
            if (snk.getSoTien() != null && BigDecimal.ZERO.compareTo(snk.getSoTien()) != 0) {
                snk.setSoTienString(Utils.formatTien(snk.getSoTien(), Constants.SystemOption.DDSo_TienVND, userDTO));
                tongSoTien = tongSoTien.add(snk.getSoTien());
            } else {
                snk.setSoTienString("");
            }

            snk.setNgayGhiSoString(convertDate(snk.getNgayGhiSo()));
            snk.setNgayCTuString(convertDate(snk.getNgayCTu()));

            if (snk.getNgayCTuString() != null && !isShow) {
                isShow = true;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(10, sheet.getLastRowNum(), soNhatKyMuaHangDTOS.size());
            // fill dữ liệu vào file
            int i = 10;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(5).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            styleI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

            for (SoNhatKyMuaHangDTO item : soNhatKyMuaHangDTOS) {
                Row row = sheet.createRow(i);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getNgayGhiSoString());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getSoCTu());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                cell3.setCellValue(item.getNgayCTuString());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(item.getDienGiai());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                if (item.getHangHoa() != null && item.getHangHoa().doubleValue() != 0) {
                    cell5.setCellValue(item.getHangHoa().doubleValue());
                }

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                if (item.getNguyenVatLieu() != null && item.getNguyenVatLieu().doubleValue() != 0) {
                    cell6.setCellValue(item.getNguyenVatLieu().doubleValue());
                }

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.CENTER);
                cell7.setCellValue(item.getAccount());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                if (item.getSoTien() != null && item.getSoTien().doubleValue() != 0) {
                    cell8.setCellValue(item.getSoTien().doubleValue());
                }

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                if (item.getPhaiTraNguoiBan() != null && item.getPhaiTraNguoiBan().doubleValue() != 0) {
                    cell9.setCellValue(item.getPhaiTraNguoiBan().doubleValue());
                }

                i++;
            }

            Row rowTotal = sheet.getRow(10 + soNhatKyMuaHangDTOS.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Tổng cộng");

            Cell cell1 = rowTotal.createCell(4);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            if (tongHangHoa.doubleValue() != 0) {
                cell1.setCellValue(tongHangHoa.doubleValue());
            }

            Cell cell2 = rowTotal.createCell(5);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            if (tongNguyenVatLieu.doubleValue() != 0) {
                cell2.setCellValue(tongNguyenVatLieu.doubleValue());
            }

            Cell cell3 = rowTotal.createCell(7);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            if (tongSoTien.doubleValue() != 0) {
                cell3.setCellValue(tongSoTien.doubleValue());
            }


            Cell cell4 = rowTotal.createCell(8);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            if (tongPhaitraNguoiBan.doubleValue() != 0) {
                cell4.setCellValue(tongPhaitraNguoiBan.doubleValue());
            }

            veBorder(rowTotal, style, Stream.of(1, 2, 3, 6).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (11 + soNhatKyMuaHangDTOS.size()), 7, 1, 3, 7, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private void veBorder(Row row, CellStyle cellStyle, List<Integer> lstCellCustom) {
        lstCellCustom.stream().forEach(n -> {
            Cell cell = row.createCell(n);
            cell.setCellStyle(cellStyle);
        });
    }

    private void showThapPhan(Double so, Workbook workbook, Cell cell, Boolean bold) {
        BigDecimal soBig = BigDecimal.valueOf(so);
        Integer amountDecimal = soBig.scale();
        cell.setCellValue(soBig.doubleValue());
        DataFormat df = workbook.createDataFormat();

        Boolean checkThapPhan = (so - Math.floor(so)) != 0 ;

        Font font = workbook.createFont();
        font.setBold(bold);
        font.setFontName("Times New Roman");
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if (!checkThapPhan) {
            style.setDataFormat(df.getFormat("0"));
        } else if (amountDecimal == 1) {
            style.setDataFormat(df.getFormat("0.0"));
        } else if (amountDecimal == 2) {
            style.setDataFormat(df.getFormat("0.00"));
        } else if (amountDecimal == 3) {
            style.setDataFormat(df.getFormat("0.000"));
        } else if (amountDecimal == 4) {
            style.setDataFormat(df.getFormat("0.0000"));
        } else if (amountDecimal == 5) {
            style.setDataFormat(df.getFormat("0.00000"));
        } else if (amountDecimal == 6) {
            style.setDataFormat(df.getFormat("0.000000"));
        } else if (amountDecimal == 7) {
            style.setDataFormat(df.getFormat("0.0000000"));
        }
        cell.setCellStyle(style);
    }

    private byte[] getSoTongHopCongNoPhaiThu(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "TongHopCongNoPhaiThu";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<User> user = userService.getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        checkEbPackage(userDTO, parameter);
        List<TongHopCongNoPhaiThuDTO> tongHopCongNoPhaiThuDTOS = reportBusinessRepository.getTongHopCongNoPhaiThu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        parameter.put("account", requestReport.getAccountNumber());
        parameter.put("currencyCode", requestReport.getCurrencyID());
        parameter.put("year", requestReport.getFromDate().getYear());
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        BigDecimal totalOpenningCreditAmount = BigDecimal.ZERO;
        BigDecimal totalOpenningDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCloseCreditAmount = BigDecimal.ZERO;
        BigDecimal totalCloseDebitAmount = BigDecimal.ZERO;
        if (tongHopCongNoPhaiThuDTOS.size() > 0) {
            for (TongHopCongNoPhaiThuDTO snk : tongHopCongNoPhaiThuDTOS) {
                if (snk.getOpenningCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpenningCreditAmount()) != 0) {
                    snk.setOpenningCreditAmountToString(Utils.formatTien(snk.getOpenningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningCreditAmount = totalOpenningCreditAmount.add(snk.getOpenningCreditAmount());
                } else {
                    snk.setOpenningCreditAmountToString(null);
                }
                if (snk.getOpenningDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpenningDebitAmount()) != 0) {
                    snk.setOpenningDebitAmountToString(Utils.formatTien(snk.getOpenningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningDebitAmount = totalOpenningDebitAmount.add(snk.getOpenningDebitAmount());
                } else {
                    snk.setOpenningDebitAmountToString(null);
                }
                if (snk.getDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmount()) != 0) {
                    snk.setDebitAmountToString(Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalDebitAmount = totalDebitAmount.add(snk.getDebitAmount());
                } else {
                    snk.setDebitAmountToString(null);
                }
                if (snk.getCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmount()) != 0) {
                    snk.setCreditAmountToString(Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCreditAmount = totalCreditAmount.add(snk.getCreditAmount());
                } else {
                    snk.setCreditAmountToString(null);
                }
                if (snk.getCloseCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCloseCreditAmount()) != 0) {
                    snk.setCloseCreditAmountToString(Utils.formatTien(snk.getCloseCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseCreditAmount = totalCloseCreditAmount.add(snk.getCloseCreditAmount());
                } else {
                    snk.setCloseCreditAmountToString(null);
                }
                if (snk.getCloseDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getCloseDebitAmount()) != 0) {
                    snk.setCloseDebitAmountToString(Utils.formatTien(snk.getCloseDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseDebitAmount = totalCloseDebitAmount.add(snk.getCloseDebitAmount());
                } else {
                    snk.setCloseDebitAmountToString(null);
                }

//            if (snk.getOpenningCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getOpenningCreditAmountOC()) != 0) {
//                snk.setOpenningCreditAmountOCToString(Utils.formatTien(snk.getOpenningCreditAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setOpenningCreditAmountOCToString(null);
//            }
//            if (snk.getOpenningDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getOpenningDebitAmountOC()) != 0) {
//                snk.setOpenningDebitAmountOCToString(Utils.formatTien(snk.getOpenningDebitAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setOpenningDebitAmountOCToString(null);
//            }
//            if (snk.getDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmountOC()) != 0) {
//                snk.setDebitAmountOCToString(Utils.formatTien(snk.getDebitAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setDebitAmountOCToString(null);
//            }
//            if (snk.getCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmountOC()) != 0) {
//                snk.setCreditAmountOCToString(Utils.formatTien(snk.getCreditAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setCreditAmountOCToString(null);
//            }
//            if (snk.getCloseCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getCloseCreditAmountOC()) != 0) {
//                snk.setCloseCreditAmountOCToString(Utils.formatTien(snk.getCloseCreditAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setCloseCreditAmountOCToString(null);
//            }
//            if (snk.getCloseDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getCloseDebitAmountOC()) != 0) {
//                snk.setCloseDebitAmountOCToString(Utils.formatTien(snk.getCloseDebitAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
//            } else {
//                snk.setCloseDebitAmountOCToString(null);
//            }
            }
        } else {
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());
            tongHopCongNoPhaiThuDTOS.add(new TongHopCongNoPhaiThuDTO());

        }
        parameter.put("REPORT_MAX_COUNT", tongHopCongNoPhaiThuDTOS.size());
        parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalOpenningCreditAmount", Utils.formatTien(totalOpenningCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalOpenningDebitAmount", Utils.formatTien(totalOpenningDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCloseCreditAmount", Utils.formatTien(totalCloseCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCloseDebitAmount", Utils.formatTien(totalCloseDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongHopCongNoPhaiThuDTOS, parameter, jasperReport);

        return result;
    }

    private byte[] getSoChiTietCongNoPhaiThu(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "ChiTietCongNoPhaiThu";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<User> user = userService.getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<ChiTietCongNoPhaiThuDTO> tongChiTietNoPhaiThuDTOS = reportBusinessRepository.getChiTietCongNoPhaiThu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        parameter.put("account", requestReport.getAccountNumber());
        parameter.put("currencyCode", requestReport.getCurrencyID());
        parameter.put("year", String.valueOf(LocalDate.now().getYear()));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal totalAllDebitAmount = BigDecimal.ZERO;
        BigDecimal totalAllCreditAmount = BigDecimal.ZERO;
        BigDecimal totalAllClosingCreditAmount = BigDecimal.ZERO;
        BigDecimal totalAllClosingDebitAmount = BigDecimal.ZERO;
        if (tongChiTietNoPhaiThuDTOS.size() > 0) {
            HashMap mapAccountObjectCode = new HashMap();
            List<Type> types = typeRepository.findAllByIsActive();
            for (ChiTietCongNoPhaiThuDTO snk : tongChiTietNoPhaiThuDTOS) {
                if (snk.getClosingCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getClosingCreditAmountOC()) != 0) {
//                    totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(snk.getClosingCreditAmountOC());
                    snk.setClosingCreditAmountToString(Utils.formatTien(snk.getClosingCreditAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setClosingCreditAmountToString(null);
                }
                if (snk.getCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmountOC()) != 0) {
                    totalAllCreditAmount = totalAllCreditAmount.add(snk.getCreditAmountOC());
                    snk.setCreditAmountToString(Utils.formatTien(snk.getCreditAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setCreditAmountToString(null);
                }
                if (snk.getDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmountOC()) != 0) {
                    totalAllDebitAmount = totalAllDebitAmount.add(snk.getDebitAmountOC());
                    snk.setDebitAmountToString(Utils.formatTien(snk.getDebitAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setDebitAmountToString(null);
                }
                if (snk.getClosingDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk.getClosingDebitAmountOC()) != 0) {
//                    totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(snk.getClosingDebitAmountOC());
                    snk.setClosingDebitAmountToString(Utils.formatTien(snk.getClosingDebitAmountOC(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setClosingDebitAmountToString(null);
                }
                BigDecimal totalDebitAmount = BigDecimal.ZERO;
                BigDecimal totalCreditAmount = BigDecimal.ZERO;
                BigDecimal totalClosingCreditAmount = BigDecimal.ZERO;
                BigDecimal totalClosingDebitAmount = BigDecimal.ZERO;
                for (ChiTietCongNoPhaiThuDTO snk1 : tongChiTietNoPhaiThuDTOS) {
                    if (snk.getAccountObjectCode().equals(snk1.getAccountObjectCode())) {
                        if (snk1.getDebitAmountOC() != null && BigDecimal.ZERO.compareTo(snk1.getDebitAmountOC()) != 0) {
                            totalDebitAmount = totalDebitAmount.add(snk1.getDebitAmount());
                        }
                        if (snk1.getCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk1.getCreditAmountOC()) != 0) {
                            totalCreditAmount = totalCreditAmount.add(snk1.getCreditAmountOC());
                        }
//                        if (snk1.getClosingCreditAmount() != null && BigDecimal.ZERO.compareTo(snk1.getClosingCreditAmount()) != 0) {
//                            totalClosingCreditAmount = snk1.getClosingCreditAmount();
//                        }
//                        if (snk1.getClosingDebitAmount() != null && BigDecimal.ZERO.compareTo(snk1.getClosingDebitAmount()) != 0) {
//                            totalClosingDebitAmount = snk1.getClosingDebitAmount();
//                        }
                        if (snk1.getClosingCreditAmountOC() != null && BigDecimal.ZERO.compareTo(snk1.getClosingCreditAmountOC()) != 0) {
                            totalClosingDebitAmount = null;
                            totalClosingCreditAmount = snk1.getClosingCreditAmountOC();
                        } else {
                            totalClosingCreditAmount = null;
                            totalClosingDebitAmount = snk1.getClosingDebitAmountOC();
                        }
                    }
                }
                snk.setLinkRef(getRefLink(snk.getRefType(), snk.getRefID(), types, null, null, null, null));
                snk.setTotalDebitAmountToString(Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setTotalCreditAmountToString(Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setTotalClosingCreditAmountToString(Utils.formatTien(totalClosingCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setTotalClosingDebitAmountToString(Utils.formatTien(totalClosingDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                if (mapAccountObjectCode.isEmpty()) {
                    mapAccountObjectCode.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
                    totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(totalClosingCreditAmount != null ? totalClosingCreditAmount : BigDecimal.ZERO);
                    totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(totalClosingDebitAmount != null ? totalClosingDebitAmount : BigDecimal.ZERO);
                } else {
                    if (mapAccountObjectCode.get(snk.getAccountObjectCode()) == null) {
                        mapAccountObjectCode.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
                        totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(totalClosingCreditAmount != null ? totalClosingCreditAmount : BigDecimal.ZERO);
                        totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(totalClosingDebitAmount != null ? totalClosingDebitAmount : BigDecimal.ZERO);
                    }
                }
            }
        } else {
            tongChiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());
            tongChiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());
            tongChiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());

        }
        parameter.put("REPORT_MAX_COUNT", tongChiTietNoPhaiThuDTOS.size());
        parameter.put("totalAllCreditAmount", Utils.formatTien(totalAllCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllDebitAmount", Utils.formatTien(totalAllDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllClosingCreditAmount", Utils.formatTien(totalAllClosingCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalAllClosingDebitAmount", Utils.formatTien(totalAllClosingDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
//        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongChiTietNoPhaiThuDTOS, parameter, jasperReport);

        return result;
    }

    private byte[] getTongHopCongNoPhaiTra(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "TongHopCongNoPhaiTra";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<User> user = userService.getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<TongHopCongNoPhaiTraDTO> tongHopCongNoPhaiTraDTOS = reportBusinessRepository.getTongHopCongNoPhaiTra(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        parameter.put("account", requestReport.getAccountNumber());
        parameter.put("currencyCode", requestReport.getCurrencyID());
        parameter.put("year", String.valueOf(requestReport.getFromDate().getYear()));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        BigDecimal totalClosingCreditAmount = BigDecimal.ZERO;
        BigDecimal totalClosingDebitAmount = BigDecimal.ZERO;
        BigDecimal totalOpeningCreditAmount = BigDecimal.ZERO;
        BigDecimal totalOpeningDebitAmount = BigDecimal.ZERO;
        if (tongHopCongNoPhaiTraDTOS.size() > 0) {
            for (TongHopCongNoPhaiTraDTO snk : tongHopCongNoPhaiTraDTOS) {
                if (snk.getCloseCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCloseCreditAmount()) != 0) {
                    snk.setCloseCreditAmountToString(Utils.formatTien(snk.getCloseCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setCloseCreditAmountToString(null);
                }
                if (snk.getCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmount()) != 0) {
                    snk.setCreditAmountToString(Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setCreditAmountToString(null);
                }
                if (snk.getDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmount()) != 0) {
                    snk.setDebitAmountToString(Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setDebitAmountToString(null);
                }
                if (snk.getCloseDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getCloseDebitAmount()) != 0) {
                    snk.setCloseDebitAmountToString(Utils.formatTien(snk.getCloseDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setCloseDebitAmountToString(null);
                }
                if (snk.getOpeningCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpeningCreditAmount()) != 0) {
                    snk.setOpeningCreditAmountToString(Utils.formatTien(snk.getOpeningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setOpeningCreditAmountToString(null);
                }
                if (snk.getOpeningDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpeningDebitAmount()) != 0) {
                    snk.setOpeningDebitAmountToString(Utils.formatTien(snk.getOpeningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setOpeningDebitAmountToString(null);
                }
                totalDebitAmount = totalDebitAmount.add(snk.getDebitAmount());
                totalCreditAmount = totalCreditAmount.add(snk.getCreditAmount());
                totalClosingCreditAmount = totalClosingCreditAmount.add(snk.getCloseCreditAmount());
                totalClosingDebitAmount = totalClosingDebitAmount.add(snk.getCloseDebitAmount());
                totalOpeningCreditAmount = totalOpeningCreditAmount.add(snk.getOpeningCreditAmount());
                totalOpeningDebitAmount = totalOpeningDebitAmount.add(snk.getOpeningDebitAmount());
            }
        } else {
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());

        }
        parameter.put("REPORT_MAX_COUNT", tongHopCongNoPhaiTraDTOS.size());
        parameter.put("sumDebitAmountToString", !BigDecimal.ZERO.equals(totalDebitAmount) ? Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("sumCreditAmountToString", !BigDecimal.ZERO.equals(totalCreditAmount) ? Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("sumClosingCreditAmountToString", !BigDecimal.ZERO.equals(totalClosingCreditAmount) ? Utils.formatTien(totalClosingCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("sumClosingDebitAmountToString", !BigDecimal.ZERO.equals(totalClosingDebitAmount) ? Utils.formatTien(totalClosingDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("sumOpeningCreditAmountToString", !BigDecimal.ZERO.equals(totalOpeningCreditAmount) ? Utils.formatTien(totalOpeningCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("sumOpeningDebitAmountToString", !BigDecimal.ZERO.equals(totalOpeningDebitAmount) ? Utils.formatTien(totalOpeningDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("Reporter", userDTO.getFullName());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }

        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongHopCongNoPhaiTraDTOS, parameter, jasperReport);

        return result;
    }

    private byte[] getChiTietCongNoPhaiTra(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "ChiTietCongNoPhaiTra";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<User> user = userService.getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        BigDecimal totalAllDebitAmount = BigDecimal.ZERO;
        BigDecimal totalAllCreditAmount = BigDecimal.ZERO;
        BigDecimal totalAllClosingCreditAmount = BigDecimal.ZERO;
        BigDecimal totalAllClosingDebitAmount = BigDecimal.ZERO;
        BigDecimal totalAllOpeningCreditAmount = BigDecimal.ZERO;
        BigDecimal totalAllOpeningDebitAmount = BigDecimal.ZERO;
        List<ChiTietCongNoPhaiTraDTO> chiTietCongNoPhaiTraDTOS = reportBusinessRepository.getChiTietCongNoPhaiTra(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("account", requestReport.getAccountNumber());
        parameter.put("currencyCode", requestReport.getCurrencyID());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        if (requestReport.getTimeLineVoucher() != null) {
            parameter.put("quarter", String.valueOf(requestReport.getTimeLineVoucher()));
        }
        List<Type> types = typeRepository.findAllByIsActive();
        if (chiTietCongNoPhaiTraDTOS.size() > 0) {
            HashMap mapAccountObjectCode = new HashMap();
            for (ChiTietCongNoPhaiTraDTO snk : chiTietCongNoPhaiTraDTOS) {
                if (snk.getClosingCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getClosingCreditAmount()) != 0) {
                    snk.setClosingCreditAmountToString(Utils.formatTien(snk.getClosingCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//                    totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(snk.getClosingCreditAmount());
                } else {
                    snk.setClosingCreditAmountToString(null);
                }
                if (snk.getCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmount()) != 0) {
                    snk.setCreditAmountToString(Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllCreditAmount = totalAllCreditAmount.add(snk.getCreditAmount());
                } else {
                    snk.setCreditAmountToString(null);
                }
                if (snk.getDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmount()) != 0) {
                    snk.setDebitAmountToString(Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllDebitAmount = totalAllDebitAmount.add(snk.getDebitAmount());
                } else {
                    snk.setDebitAmountToString(null);
                }
                if (snk.getClosingDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getClosingDebitAmount()) != 0) {
                    snk.setClosingDebitAmountToString(Utils.formatTien(snk.getClosingDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//                    totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(snk.getClosingDebitAmount());
                } else {
                    snk.setClosingDebitAmountToString(null);
                }
                if (snk.getOpeningCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpeningCreditAmount()) != 0) {
                    snk.setOpeningCreditAmountToString(Utils.formatTien(snk.getOpeningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllOpeningCreditAmount = totalAllOpeningCreditAmount.add(snk.getOpeningCreditAmount());
                } else {
                    snk.setOpeningCreditAmountToString(null);
                }
                if (snk.getOpeningDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getOpeningDebitAmount()) != 0) {
                    snk.setOpeningDebitAmountToString(Utils.formatTien(snk.getOpeningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalAllOpeningDebitAmount = totalAllOpeningDebitAmount.add(snk.getOpeningDebitAmount());
                } else {
                    snk.setOpeningDebitAmountToString(null);
                }
                BigDecimal totalDebitAmount = BigDecimal.ZERO;
                BigDecimal totalCreditAmount = BigDecimal.ZERO;
                BigDecimal totalClosingCreditAmount = BigDecimal.ZERO;
                BigDecimal totalClosingDebitAmount = BigDecimal.ZERO;
                for (ChiTietCongNoPhaiTraDTO snk1 : chiTietCongNoPhaiTraDTOS) {
                    if (snk.getAccountObjectCode().equals(snk1.getAccountObjectCode())) {
                        if (snk1.getDebitAmount() != null && BigDecimal.ZERO.compareTo(snk1.getDebitAmount()) != 0) {
                            totalDebitAmount = totalDebitAmount.add(snk1.getDebitAmount());
                        }
                        if (snk1.getCreditAmount() != null && BigDecimal.ZERO.compareTo(snk1.getCreditAmount()) != 0) {
                            totalCreditAmount = totalCreditAmount.add(snk1.getCreditAmount());
                        }
                        if (snk1.getClosingCreditAmount() != null && BigDecimal.ZERO.compareTo(snk1.getClosingCreditAmount()) != 0) {
                            totalClosingDebitAmount = null;
                            totalClosingCreditAmount = snk1.getClosingCreditAmount();
                        } else {
                            totalClosingCreditAmount = null;
                            totalClosingDebitAmount = snk1.getClosingDebitAmount();
                        }
//                        if (snk1.getClosingDebitAmount() == null && BigDecimal.ZERO.compareTo(snk1.()) == 0) {
//
//                            totalClosingCreditAmount = snk1.getClosingCreditAmount();
//                        }
                    }
                }
                snk.setLinkRef(getRefLink(snk.getRefType(), snk.getReferenceID(), types, null, null, null, null));
                snk.setSumDebitAmountToString(Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setSumCreditAmountToString(Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setSumClosingCreditAmountToString(Utils.formatTien(totalClosingCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                snk.setSumClosingDebitAmountToString(Utils.formatTien(totalClosingDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
                if (mapAccountObjectCode.isEmpty()) {
                    mapAccountObjectCode.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
                    totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(totalClosingCreditAmount != null ? totalClosingCreditAmount : BigDecimal.ZERO);
                    totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(totalClosingDebitAmount != null ? totalClosingDebitAmount : BigDecimal.ZERO);
                } else {
                    if (mapAccountObjectCode.get(snk.getAccountObjectCode()) == null) {
                        mapAccountObjectCode.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
                        totalAllClosingCreditAmount = totalAllClosingCreditAmount.add(totalClosingCreditAmount != null ? totalClosingCreditAmount : BigDecimal.ZERO);
                        totalAllClosingDebitAmount = totalAllClosingDebitAmount.add(totalClosingDebitAmount != null ? totalClosingDebitAmount : BigDecimal.ZERO);
                    }
                }
            }
        } else {
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());

        }
        parameter.put("REPORT_MAX_COUNT", chiTietCongNoPhaiTraDTOS.size());
        parameter.put("totalAllDebitAmount", !BigDecimal.ZERO.equals(totalAllDebitAmount) ? Utils.formatTien(totalAllDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("totalAllCreditAmount", !BigDecimal.ZERO.equals(totalAllCreditAmount) ? Utils.formatTien(totalAllCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("totalAllClosingCreditAmount", !BigDecimal.ZERO.equals(totalAllClosingCreditAmount) ? Utils.formatTien(totalAllClosingCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        parameter.put("totalAllClosingDebitAmount", !BigDecimal.ZERO.equals(totalAllClosingDebitAmount) ? Utils.formatTien(totalAllClosingDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO) : null);
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(chiTietCongNoPhaiTraDTOS, parameter, jasperReport);

        return result;
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Hautv
     */
    byte[] getSoNhatKyChung(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoNhatKyChung";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoNhatKyChungDTO> soNhatKyChungDTOS = reportBusinessRepository.getSoNhatKyChung(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getGroupTheSameItem(),
            requestReport.getShowAccumAmount(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        List<Type> types = typeRepository.findAllByIsActive();
        if (soNhatKyChungDTOS.size() > 1 || (soNhatKyChungDTOS.size() == 1 && !requestReport.getShowAccumAmount())) {
            for (SoNhatKyChungDTO snk : soNhatKyChungDTOS) {
                if (snk.getDebitAmount() != null && BigDecimal.ZERO.compareTo(snk.getDebitAmount()) != 0) {
                    snk.setDebitAmountString(Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setDebitAmountString("");
                }
                if (snk.getCreditAmount() != null && BigDecimal.ZERO.compareTo(snk.getCreditAmount()) != 0) {
                    snk.setCreditAmountString(Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                } else {
                    snk.setCreditAmountString("");
                }
                snk.setDateString(convertDate(snk.getDate()));
                snk.setPostedDateString(convertDate(snk.getPostedDate()));
                if (snk.getDebitAmount() != null) {
                    totalDebitAmount = totalDebitAmount.add(snk.getDebitAmount());
                }
                if (snk.getCreditAmount() != null) {
                    totalCreditAmount = totalCreditAmount.add(snk.getCreditAmount());
                }
                snk.setLinkRef(getRefLink(snk.getTypeID(), snk.getReferenceID(), types, null, null, null, null));
            }
            parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
            parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        } else {
            soNhatKyChungDTOS.clear();
            for (int i = 0; i <= 2; i++) {
                SoNhatKyChungDTO soNhatKyChungDTO = new SoNhatKyChungDTO();
                soNhatKyChungDTO.setDateString("");
                soNhatKyChungDTO.setCreditAmountString("");
                soNhatKyChungDTO.setPostedDateString("");
                soNhatKyChungDTO.setAccountNumber("");
                soNhatKyChungDTO.setCorrespondingAccountNumber("");
                soNhatKyChungDTO.setDebitAmountString("");
                soNhatKyChungDTOS.add(soNhatKyChungDTO);
            }
            requestReport.setShowAccumAmount(false);
        }
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("REPORT_MAX_COUNT", soNhatKyChungDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soNhatKyChungDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoNhatKyChung(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<SoNhatKyChungDTO> soNhatKyChungDTOS = reportBusinessRepository.getSoNhatKyChung(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getGroupTheSameItem(),
            requestReport.getShowAccumAmount(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (Boolean.TRUE.equals(requestReport.getShowAccumAmount())) {
            if (soNhatKyChungDTOS.size() == 1) {
                soNhatKyChungDTOS.clear();
            }
        }
        if (soNhatKyChungDTOS.size() == 0 || (soNhatKyChungDTOS.size() == 1 && requestReport.getShowAccumAmount())) {
            for (int i = 0; i <= 2; i++) {
                SoNhatKyChungDTO soNhatKyChungDTO = new SoNhatKyChungDTO();
                soNhatKyChungDTO.setDateString("");
                soNhatKyChungDTO.setCreditAmountString("");
                soNhatKyChungDTO.setPostedDateString("");
                soNhatKyChungDTO.setAccountNumber("");
                soNhatKyChungDTO.setCorrespondingAccountNumber("");
                soNhatKyChungDTO.setDebitAmountString("");
                soNhatKyChungDTOS.add(soNhatKyChungDTO);
            }
            requestReport.setShowAccumAmount(false);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (soNhatKyChungDTOS.size() - 1 > 0) {
                sheet.shiftRows(11, sheet.getLastRowNum(), soNhatKyChungDTOS.size() - 1);
            }
            // fill dữ liệu vào file
            int i = 9;

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);

            CellStyle styleDf = workbook.createCellStyle();
            styleDf.setFont(fontDf);
            styleDf.setWrapText(true);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);

            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
//            sheet.getRow(8).getCell(7).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
            //
            BigDecimal totalDebitAmount = BigDecimal.ZERO;
            BigDecimal totalCreditAmount = BigDecimal.ZERO;
            for (SoNhatKyChungDTO item : soNhatKyChungDTOS) {
                Row row = sheet.createRow(i);
                if (Boolean.TRUE.equals(requestReport.getShowAccumAmount())) {
                    if (i == 9) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                }
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(Utils.getValueString(Utils.convertDate(item.getDate())));

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(Utils.getValueString(Utils.convertDate(item.getPostedDate())));

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(Utils.getValueString(item.getNo()));

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(Utils.getValueString(item.getDescription()));

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                cell5.setCellValue(Utils.getValueString(item.getAccountNumber()));

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                cell6.setCellValue(Utils.getValueString(item.getCorrespondingAccountNumber()));

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell7, item.getDebitAmount());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell8, item.getCreditAmount());
                i++;
                if (item.getDebitAmount() != null) {
                    totalDebitAmount = totalDebitAmount.add(item.getDebitAmount());
                }
                if (item.getCreditAmount() != null) {
                    totalCreditAmount = totalCreditAmount.add(item.getCreditAmount());
                }
            }

            Row rowSum = sheet.createRow(9 + soNhatKyChungDTOS.size());
            rowSum.createCell(0);
            rowSum.getCell(0).setCellStyle(styleB);

            rowSum.createCell(1);
            rowSum.getCell(1).setCellStyle(styleB);
            rowSum.createCell(2);
            rowSum.getCell(2).setCellStyle(styleB);
            rowSum.createCell(3);
            rowSum.getCell(3).setCellStyle(styleB);
            rowSum.getCell(3).setCellValue("Cộng số phát sinh");
            rowSum.createCell(4);
            rowSum.getCell(4).setCellStyle(styleB);

            Cell cell6 = rowSum.createCell(5);
            cell6.setCellStyle(style);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);

            Cell cell7 = rowSum.createCell(6);
            cell7.setCellStyle(styleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            Utils.setValueCellDouble(cell7, totalDebitAmount);

            Cell cell8 = rowSum.createCell(7);
            cell8.setCellStyle(styleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            Utils.setValueCellDouble(cell8, totalCreditAmount);

            sheet.createRow(11 + soNhatKyChungDTOS.size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
            sheet.createRow(12 + soNhatKyChungDTOS.size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
            sheet.getRow(11 + soNhatKyChungDTOS.size()).getCell(0).setCellStyle(styleDf);
            sheet.getRow(12 + soNhatKyChungDTOS.size()).getCell(0).setCellStyle(styleDf);

            SetFooterExcel(workbook, sheet, userDTO, (14 + soNhatKyChungDTOS.size()), 7, 0, 3, 7);
            sheet.getRow(15 + soNhatKyChungDTOS.size()).getCell(0).setCellValue("Người ghi sổ");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelBangCanDoiKeToan(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getBangCanDoiKeToan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        if (canDoiKeToanDTOS.size() == 0) {
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
        }
        canDoiKeToanDTOS.remove(0);
        canDoiKeToanDTOS.remove(63);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(14, sheet.getLastRowNum(), canDoiKeToanDTOS.size());
            // fill dữ liệu vào file
            int i = 13;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(9).getCell(4).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
            //
            for (BangCanDoiKeToanDTO item : canDoiKeToanDTOS) {
                Row row = sheet.createRow(i);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(item.getItemName());

                Cell cell2 = row.createCell(1);
                cell2.setCellValue(item.getItemCode() == null ? "" : item.getItemCode());

                Cell cell3 = row.createCell(2);
                cell3.setCellValue(item.getDescription() == null ? "" : item.getDescription());

                Cell cell4 = row.createCell(3);
                cell4.setCellValue(item.getAmount().doubleValue());

                Cell cell5 = row.createCell(4);
                cell5.setCellValue(item.getPrevAmount().doubleValue());

                if (Boolean.TRUE.equals(item.getBold())) {
                    cell1.setCellStyle(styleB);
                    cell2.setCellStyle(styleB);
                    cell3.setCellStyle(styleB);
                    cell4.setCellStyle(styleB);
                    cell5.setCellStyle(styleB);
                } else {
                    cell1.setCellStyle(style);
                    cell2.setCellStyle(style);
                    cell3.setCellStyle(style);
                    cell4.setCellStyle(style);
                    cell5.setCellStyle(style);
                }
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                i++;
            }
            SetFooterExcel(workbook, sheet, userDTO, (15 + canDoiKeToanDTOS.size()), 3, 0, 1, 3);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getBangCanDoiKeToan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "CanDoiKeToan";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getBangCanDoiKeToan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        if (canDoiKeToanDTOS.size() == 0) {
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
            canDoiKeToanDTOS.add(new BangCanDoiKeToanDTO(BigDecimal.ZERO, BigDecimal.ZERO));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", canDoiKeToanDTOS.size());
        parameter.put("Ngay", "" + requestReport.getCreateDate().getDayOfMonth());
        parameter.put("Thang", "" + requestReport.getCreateDate().getMonthValue());
        parameter.put("Nam", "" + requestReport.getCreateDate().getYear());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        canDoiKeToanDTOS.remove(0);
        canDoiKeToanDTOS.remove(63);
        for (BangCanDoiKeToanDTO snk : canDoiKeToanDTOS) {
            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        parameter.put("REPORT_MAX_COUNT", canDoiKeToanDTOS.size());
        checkEbPackage(userDTO, parameter);
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(canDoiKeToanDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelBangCanDoiTaiKhoan(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        double sumNoDK = 0;
        double sumCoDK = 0;
        double sumNoPS = 0;
        double sumCoPS = 0;
        double sumNoCK = 0;
        double sumCoCK = 0;
        List<BangCanDoiTaiKhoanDTO> canDoiTaiKhoanDTOS = reportBusinessRepository.getBangCanDoiTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getGrade(),
            currentBook, requestReport.getDependent());
        for (BangCanDoiTaiKhoanDTO snk : canDoiTaiKhoanDTOS) {
            if (snk.getGrade().intValue() == Constants.Account.GRADE_PARENT) {
                sumNoDK += snk.getOpeningDebitAmount().doubleValue();
                sumCoDK += snk.getOpeningCreditAmount().doubleValue();
                sumNoPS += snk.getDebitAmount().doubleValue();
                sumCoPS += snk.getCreditAmount().doubleValue();
                sumNoCK += snk.getClosingDebitAmount().doubleValue();
                sumCoCK += snk.getClosingCreditAmount().doubleValue();
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (canDoiTaiKhoanDTOS.size() > 0) {
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                sheet.shiftRows(13, sheet.getLastRowNum(), canDoiTaiKhoanDTOS.size() - 1);
                // fill dữ liệu vào file
                int i = 12;
                Font fontDf = workbook.createFont();
                fontDf.setFontName("Times New Roman");
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(fontDf);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle styleB = workbook.createCellStyle();
                styleB.setWrapText(true);
                styleB.setBorderBottom(BorderStyle.THIN);
                styleB.setBorderTop(BorderStyle.THIN);
                styleB.setBorderRight(BorderStyle.THIN);
                styleB.setBorderLeft(BorderStyle.THIN);
                styleB.setFont(fontB);
                styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(8).getCell(7).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
                //
                for (BangCanDoiTaiKhoanDTO item : canDoiTaiKhoanDTOS) {
                    Row row = sheet.createRow(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    cell1.setCellValue(item.getAccountNumber());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    cell2.setCellValue(item.getAccountName());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                    cell3.setCellValue(item.getOpeningDebitAmount().doubleValue());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                    cell4.setCellValue(item.getOpeningCreditAmount().doubleValue());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    cell5.setCellValue(item.getDebitAmount().doubleValue());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getCreditAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getClosingDebitAmount().doubleValue());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getClosingCreditAmount().doubleValue());
                    i++;
                }

                Row rowSum = sheet.createRow(12 + canDoiTaiKhoanDTOS.size());
                Cell cell0 = rowSum.createCell(0);
                cell0.setCellStyle(styleB);
                Cell cell1 = rowSum.createCell(1);
                cell1.setCellStyle(styleB);
                cell0.setCellValue("Cộng");

                Cell cell3 = rowSum.createCell(2);
                cell3.setCellStyle(styleB);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue(sumNoDK);

                Cell cell4 = rowSum.createCell(3);
                cell4.setCellStyle(styleB);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(sumCoDK);

                Cell cell5 = rowSum.createCell(4);
                cell5.setCellStyle(styleB);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(sumNoPS);

                Cell cell6 = rowSum.createCell(5);
                cell6.setCellStyle(styleB);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(sumCoPS);

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(sumNoCK);

                Cell cell8 = rowSum.createCell(7);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(sumCoCK);

                SetFooterExcel(workbook, sheet, userDTO, (14 + canDoiTaiKhoanDTOS.size()), 6, 0, 2, 6);
                workbook.write(bos);
                workbook.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO());
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO());
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO());
            try {
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                sheet.shiftRows(13, sheet.getLastRowNum(), canDoiTaiKhoanDTOS.size() - 1);
                // fill dữ liệu vào file
                int i = 12;
                Font fontDf = workbook.createFont();
                fontDf.setFontName("Times New Roman");
                CellStyle style = workbook.createCellStyle();
                style.setWrapText(true);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(fontDf);
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font fontB = workbook.createFont();
                fontB.setFontName("Times New Roman");
                fontB.setBold(true);
                CellStyle styleB = workbook.createCellStyle();
                styleB.setWrapText(true);
                styleB.setBorderBottom(BorderStyle.THIN);
                styleB.setBorderTop(BorderStyle.THIN);
                styleB.setBorderRight(BorderStyle.THIN);
                styleB.setBorderLeft(BorderStyle.THIN);
                styleB.setFont(fontB);
                styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(8).getCell(7).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
                //
                for (BangCanDoiTaiKhoanDTO item : canDoiTaiKhoanDTOS) {
                    Row row = sheet.createRow(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    i++;
                }

                Row rowSum = sheet.createRow(12 + canDoiTaiKhoanDTOS.size());
                Cell cell0 = rowSum.createCell(0);
                cell0.setCellStyle(styleB);
                Cell cell1 = rowSum.createCell(1);
                cell1.setCellStyle(styleB);
                cell0.setCellValue("Cộng");

                Cell cell3 = rowSum.createCell(2);
                cell3.setCellStyle(styleB);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);

                Cell cell4 = rowSum.createCell(3);
                cell4.setCellStyle(styleB);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);

                Cell cell5 = rowSum.createCell(4);
                cell5.setCellStyle(styleB);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);

                Cell cell6 = rowSum.createCell(5);
                cell6.setCellStyle(styleB);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);

                Cell cell8 = rowSum.createCell(7);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);

                SetFooterExcel(workbook, sheet, userDTO, (14 + canDoiTaiKhoanDTOS.size()), 6, 0, 2, 6);
                workbook.write(bos);
                workbook.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return bos.toByteArray();
    }

    byte[] getExcelSoKeToanChiTietQuyTienMat(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String header4 = "";
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        header4 = "Tài khoản: ";
        for (int i = 0; i < requestReport.getAccountList().size(); i++) {
            if (i == 0) {
                header4 += requestReport.getAccountList().get(i);
            } else {
                header4 += "," + requestReport.getAccountList().get(i);
            }
        }
        header4 += "; " + "Loại quỹ: " +
            requestReport.getCurrencyID() + "; " + Period;
        List<SoKeToanChiTietQuyTienMatDTO> soKeToanChiTietQuyTienMatDTOS = reportBusinessRepository.getSoKeToanChiTietQuyTienMat(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            phienSoLamViec,
            requestReport.getAccountList(),
            requestReport.getGroupTheSameItem(),
            requestReport.getDependent(), requestReport.getTypeShowCurrency());
        if (soKeToanChiTietQuyTienMatDTOS.size() <= 1) {
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(14, sheet.getLastRowNum(), soKeToanChiTietQuyTienMatDTOS.size() - 2);
            // fill dữ liệu vào file
            int i = 12;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleDVT = workbook.createCellStyle();
            styleDVT.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleDVT.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleDVT.setWrapText(true);
            styleDVT.setFont(fontDf);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setWrapText(true);
            sheet.getRow(6).getCell(0).setCellValue(header4);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(4).setCellValue(header4);
            sheet.getRow(7).getCell(9).setCellStyle(styleDVT);
            sheet.getRow(7).getCell(9).setCellValue("Đơn vị tính: " + requestReport.getCurrencyID());
            BigDecimal sumNo = BigDecimal.ZERO;
            BigDecimal sumCo = BigDecimal.ZERO;
            BigDecimal sumTon = BigDecimal.ZERO;
            //
            for (SoKeToanChiTietQuyTienMatDTO item : soKeToanChiTietQuyTienMatDTOS) {
                Row row = sheet.createRow(i);
                if (item.getPositionOrder() == 0 || item.getPositionOrder() == 2) {
                    if (item.getPositionOrder() == 2) {
                        sumTon = sumTon.add((item.getSoTon() != null && item.getSoTon() != BigDecimal.ZERO) ? item.getSoTon() : BigDecimal.ZERO);
                    }
                    style.setFont(fontB);
                } else {
                    sumNo = sumNo.add((item.getPhatSinhNo() != null && item.getPhatSinhNo() != BigDecimal.ZERO) ? item.getPhatSinhNo() : BigDecimal.ZERO);
                    sumCo = sumCo.add((item.getPhatSinhCo() != null && item.getPhatSinhCo() != BigDecimal.ZERO) ? item.getPhatSinhCo() : BigDecimal.ZERO);
                    style.setFont(fontDf);
                }
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getDate() != null ? convertDate(item.getDate()) : "");

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getPostedDate() != null ? convertDate(item.getPostedDate()) : "");

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(item.getReceiptRefNo() != null ? item.getReceiptRefNo() : "");

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(item.getPaymentRefNo() != null ? item.getPaymentRefNo() : "");

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                cell5.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                cell6.setCellValue(item.getAccount() != null ? item.getAccount() : "");

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.CENTER);
                cell7.setCellValue(item.getAccountCorresponding() != null ? item.getAccountCorresponding() : "");

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getPhatSinhNo() != null ? item.getPhatSinhNo().doubleValue() : 0);

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(item.getPhatSinhCo() != null ? item.getPhatSinhCo().doubleValue() : 0);

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.setCellValue(item.getSoTon() != null ? item.getSoTon().doubleValue() : 0);

                Cell cell11 = row.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                cell11.setCellValue(item.getNote() != null ? item.getNote() : "");
                i++;
            }
            //Lấy dòng tổng cộng
            CellStyle styleSum = workbook.createCellStyle();
            styleSum.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleSum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleSum.setWrapText(true);
            styleSum.setBorderBottom(BorderStyle.THIN);
            styleSum.setBorderTop(BorderStyle.THIN);
            styleSum.setBorderRight(BorderStyle.THIN);
            styleSum.setBorderLeft(BorderStyle.THIN);
            styleSum.setFont(fontB);
            Row rowSum = sheet.createRow(i);
            CellUtil.setAlignment(rowSum.createCell(0), HorizontalAlignment.LEFT);
            rowSum.getCell(0).setCellStyle(styleSum);
            rowSum.getCell(0).setCellValue("Tổng cộng");
            rowSum.createCell(1);
            rowSum.createCell(2);
            rowSum.createCell(3);
            rowSum.createCell(4);
            rowSum.createCell(5);
            rowSum.createCell(6);
            rowSum.getCell(1).setCellStyle(styleSum);
            rowSum.getCell(2).setCellStyle(styleSum);
            rowSum.getCell(3).setCellStyle(styleSum);
            rowSum.getCell(4).setCellStyle(styleSum);
            rowSum.getCell(5).setCellStyle(styleSum);
            rowSum.getCell(6).setCellStyle(styleSum);

            CellUtil.setAlignment(rowSum.createCell(7), HorizontalAlignment.RIGHT);
            rowSum.getCell(7).setCellStyle(styleSum);
            rowSum.getCell(7).setCellValue(sumNo.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(8), HorizontalAlignment.RIGHT);
            rowSum.getCell(8).setCellStyle(styleSum);
            rowSum.getCell(8).setCellValue(sumCo.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(9), HorizontalAlignment.RIGHT);
            rowSum.getCell(9).setCellStyle(styleSum);
            rowSum.getCell(9).setCellValue(sumTon.doubleValue());

            rowSum.createCell(10);
            rowSum.getCell(10).setCellStyle(styleSum);

            Row rowFooter = sheet.createRow(i + 2);
            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
            rowFooter.getCell(0).setCellStyle(styleHeader);
            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");

            Row rowFooter2 = sheet.createRow(i + 3);
            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
            rowFooter2.getCell(0).setCellStyle(styleHeader);
            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
            SetFooterExcel(workbook, sheet, userDTO, (16 + soKeToanChiTietQuyTienMatDTOS.size()), 8, 1, 4, 8);
            CellStyle cellStyleFooter = workbook.createCellStyle();
            cellStyleFooter.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleFooter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyleFooter.setFont(fontDf);
            cellStyleFooter.setAlignment(HorizontalAlignment.CENTER);
            sheet.getRow(16 + soKeToanChiTietQuyTienMatDTOS.size() + 1).getCell(1).setCellValue("Người ghi sổ");
//            sheet.getRow(13 + soTienGuiNganHangDTOS.size() + 3).getCell(1).setCellValue("");
//            sheet.getRow(13 + soTienGuiNganHangDTOS.size() + 3).getCell(3).setCellValue("");
//            sheet.getRow(13 + soTienGuiNganHangDTOS.size() + 3).getCell(6).setCellValue("");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelSoTienGuiNganHang(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SystemOption> systemOptions = systemOptionRepository.findAllSystemOptions(currentUserLoginAndOrg.get().getOrg());
        String data = systemOptions.stream().filter(a -> a.getCode().equals(TCKHAC_SDDMTKNH)).findFirst().get().getData();
        List<ComboboxBankAccountDetailDTO> bankAccountDetailsList = new ArrayList<>();
        List<UUID> listCompanyIDTKNH = systemOptionRepository.getAllCompanyByCompanyIdAndCode(requestReport.getCompanyID(), TCKHAC_SDDMTKNH);
        List<UUID> listCompanyIDTTD = systemOptionRepository.getAllCompanyByCompanyIdAndCode(requestReport.getCompanyID(), TCKHAC_SDDMTheTD);
        if (data.equals("1") && Boolean.TRUE.equals(requestReport.getDependent())) {
            bankAccountDetailsList = bankAccountDetailsRepository.findAllForAccType(currentUserLoginAndOrg.get().getOrg(),currentUserLoginAndOrg.get().getOrgGetData());
        } else {
            bankAccountDetailsList = bankAccountDetailsRepository.findAllByIsActive(listCompanyIDTKNH, listCompanyIDTTD,currentUserLoginAndOrg.get().getOrgGetData());
        }
        String listBankAccountDetail = ",";
        String header4 = "";
        Optional<BankAccountDetails> bankAccountDetails = Optional.of(new BankAccountDetails());
        if (requestReport.getBankAccountDetailID() != null) {
            listBankAccountDetail += Utils.uuidConvertToGUID(requestReport.getBankAccountDetailID()) + ",";
            bankAccountDetails = bankAccountDetailsRepository.findById(requestReport.getBankAccountDetailID());
            if (bankAccountDetails.isPresent()) {
                header4 += "TK ngân hàng: " + bankAccountDetails.get().getBankAccount();
            } else {
                header4 += "TK ngân hàng: ";
            }
        } else {
            if (bankAccountDetailsList.size() > 0) {
                for (int i = 0; i < bankAccountDetailsList.size(); i++) {
                    listBankAccountDetail += Utils.uuidConvertToGUID(bankAccountDetailsList.get(i).getId()) + ",";
                }
            }
            header4 += "TK ngân hàng: Tất cả; ";
        }
        String listAccountNumber = "";
        List<AccountList> accountLists = accountListRepository.getAccountStartWith112Except112(currentUserLoginAndOrg.get().getOrgGetData());
        if (requestReport.getAccountNumber().equals("112") && accountLists.size() > 0) {
            for (int i = 0; i < accountLists.size(); i++) {
                listAccountNumber += "," + accountLists.get(i).getAccountNumber();
            }
        } else {
            listAccountNumber += "," + requestReport.getAccountNumber();
        }
        listAccountNumber += ",";
        /*Header*/
        String CurrencyType = "";
        if (requestReport.getCurrencyID() != null) {
            CurrencyType = "Loại tiền: " + requestReport.getCurrencyID() + "; ";
        }
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        header4 += "Tài khoản: " + requestReport.getAccountNumber() + "; ";
        header4 += CurrencyType;
        header4 += Period;
        List<SoTienGuiNganHangDTO> soTienGuiNganHangDTOS = reportBusinessRepository.getSoTienGuiNganHang(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), listAccountNumber, requestReport.getCurrencyID(), listBankAccountDetail, requestReport.getGroupTheSameItem(), phienSoLamViec, requestReport.getDependent(), requestReport.getTypeShowCurrency());
        if (soTienGuiNganHangDTOS.size() == 0) {
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(9, sheet.getLastRowNum(), soTienGuiNganHangDTOS.size());
            // fill dữ liệu vào file
            int i = 9;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setFont(fontDf);
            styleHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            Font fontBI = workbook.createFont();
            CellStyle styleHeader2 = workbook.createCellStyle();
            styleHeader2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleHeader2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            fontBI.setBold(true);
            fontBI.setItalic(true);
            fontBI.setFontName("Times New Roman");
            styleHeader2.setFont(fontBI);
            sheet.getRow(5).getCell(0).setCellValue(header4);
            BigDecimal sumThu = BigDecimal.ZERO;
            BigDecimal sumChi = BigDecimal.ZERO;
            BigDecimal sumTon = BigDecimal.ZERO;
            for (SoTienGuiNganHangDTO item : soTienGuiNganHangDTOS) {
                Row row = sheet.createRow(i);
                if (item.getPositionOrder() == 0) {
                    style.setFont(fontB);
                    Cell cell0 = row.createCell(0);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellStyle(style);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellStyle(style);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellStyle(style);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellStyle(style);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellStyle(style);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellStyle(style);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellStyle(style);
                    cell0.setCellStyle(style);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 7));
                } else if (item.getPositionOrder() == 3) {
                    sumThu = sumThu.add(item.getSoTon() != null && item.getSoThu() != BigDecimal.ZERO ? item.getSoThu() : BigDecimal.ZERO);
                    sumChi = sumChi.add(item.getSoChi() != null && item.getSoChi() != BigDecimal.ZERO ? item.getSoChi() : BigDecimal.ZERO);
                    sumTon = sumTon.add(item.getSoTon() != null && item.getSoTon() != BigDecimal.ZERO ? item.getSoTon() : BigDecimal.ZERO);
                    style.setFont(fontB);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                    cell0.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");
                    Cell cell1 = row.createCell(1);
                    cell1.setCellStyle(style);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellStyle(style);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellStyle(style);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellStyle(style);
                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getSoThu() != null ? item.getSoThu().doubleValue() : 0);

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getSoChi() != null ? item.getSoChi().doubleValue() : 0);

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getSoTon() != null ? item.getSoTon().doubleValue() : 0);
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
                } else if (item.getPositionOrder() == 1) {
                    style.setFont(fontB);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");
                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getSoTon() != null ? item.getSoTon().doubleValue() : 0);
                } else {
                    style.setFont(fontDf);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getDate() != null ? convertDate(item.getDate()) : "");

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue(item.getPostedDate() != null ? convertDate(item.getPostedDate()) : "");

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getNo() != null ? item.getNo() : "");

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                    cell5.setCellValue(item.getAccountCorresponding() != null ? item.getAccountCorresponding() : "");

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getSoThu() != null ? item.getSoThu().doubleValue() : 0);

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getSoChi() != null ? item.getSoChi().doubleValue() : 0);

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getSoTon() != null ? item.getSoTon().doubleValue() : 0);
                }
                i++;
            }

            //Lấy dòng tổng cộng
            CellStyle styleSum = workbook.createCellStyle();
            styleSum.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleSum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleSum.setWrapText(true);
            styleSum.setBorderBottom(BorderStyle.THIN);
            styleSum.setBorderTop(BorderStyle.THIN);
            styleSum.setBorderRight(BorderStyle.THIN);
            styleSum.setBorderLeft(BorderStyle.THIN);
            styleSum.setFont(fontB);
            Row rowSum = sheet.createRow(i);
            CellUtil.setAlignment(rowSum.createCell(0), HorizontalAlignment.LEFT);
            rowSum.getCell(0).setCellStyle(styleSum);
            rowSum.getCell(0).setCellValue("Tổng cộng");
            rowSum.createCell(1);
            rowSum.createCell(2);
            rowSum.createCell(3);
            rowSum.createCell(4);
            rowSum.getCell(1).setCellStyle(styleSum);
            rowSum.getCell(2).setCellStyle(styleSum);
            rowSum.getCell(3).setCellStyle(styleSum);
            rowSum.getCell(4).setCellStyle(styleSum);

            CellUtil.setAlignment(rowSum.createCell(5), HorizontalAlignment.RIGHT);
            rowSum.getCell(5).setCellStyle(styleSum);
            rowSum.getCell(5).setCellValue(sumThu.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(6), HorizontalAlignment.RIGHT);
            rowSum.getCell(6).setCellStyle(styleSum);
            rowSum.getCell(6).setCellValue(sumChi.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(7), HorizontalAlignment.RIGHT);
            rowSum.getCell(7).setCellStyle(styleSum);
            rowSum.getCell(7).setCellValue(sumTon.doubleValue());
            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));

            Row rowFooter = sheet.createRow(i + 2);
            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
            rowFooter.getCell(0).setCellStyle(styleHeader);
            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");

            Row rowFooter2 = sheet.createRow(i + 3);
            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
            rowFooter2.getCell(0).setCellStyle(styleHeader);
            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
            sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 0, 4));

            SetFooterExcel(workbook, sheet, userDTO, (14 + soTienGuiNganHangDTOS.size()), 5, 1, 3, 5);
            sheet.getRow(14 + soTienGuiNganHangDTOS.size() + 1).getCell(1).setCellValue("Người ghi sổ");
            sheet.getRow(14 + soTienGuiNganHangDTOS.size() + 3).getCell(1).setCellValue("");
            sheet.getRow(14 + soTienGuiNganHangDTOS.size() + 3).getCell(3).setCellValue("");
            sheet.getRow(14 + soTienGuiNganHangDTOS.size() + 3).getCell(5).setCellValue("");
            Row row = sheet.createRow(14 + soTienGuiNganHangDTOS.size() + 5);
            CellStyle styleFooter = workbook.createCellStyle();
            styleFooter.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleFooter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleFooter.setWrapText(true);
            styleFooter.setFont(fontB);

            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                Cell cellRecorder = row.createCell(1);
                cellRecorder.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellRecorder, HorizontalAlignment.CENTER);
                Cell cellChiefAccountant = row.createCell(3);
                cellChiefAccountant.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellChiefAccountant, HorizontalAlignment.CENTER);
                Cell cellDirector = row.createCell(5);
                cellDirector.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellDirector, HorizontalAlignment.CENTER);
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                    cellRecorder.setCellValue(userDTO.getFullName());
                } else {
                    cellRecorder.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                }
                cellChiefAccountant.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                cellDirector.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getBangCanDoiTaiKhoan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "CanDoiTaiKhoan";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<BangCanDoiTaiKhoanDTO> canDoiTaiKhoanDTOS = reportBusinessRepository.getBangCanDoiTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getGrade(),
            currentBook, requestReport.getDependent());
        if (canDoiTaiKhoanDTOS.size() == 0) {
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
                , BigDecimal.ZERO, BigDecimal.ZERO, 0));
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
                , BigDecimal.ZERO, BigDecimal.ZERO, 0));
            canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
                , BigDecimal.ZERO, BigDecimal.ZERO, 0));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", canDoiTaiKhoanDTOS.size());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        checkEbPackage(userDTO, parameter);
        BigDecimal sumNoDK = BigDecimal.ZERO;
        BigDecimal sumCoDK = BigDecimal.ZERO;
        BigDecimal sumNoPS = BigDecimal.ZERO;
        BigDecimal sumCoPS = BigDecimal.ZERO;
        BigDecimal sumNoCK = BigDecimal.ZERO;
        BigDecimal sumCoCK = BigDecimal.ZERO;
        for (BangCanDoiTaiKhoanDTO snk : canDoiTaiKhoanDTOS) {
            snk.setCoDauKy(BigDecimal.ZERO.compareTo(snk.getOpeningCreditAmount()) == 0 ? "" : Utils.formatTien(snk.getOpeningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setNoDauKy(BigDecimal.ZERO.compareTo(snk.getOpeningDebitAmount()) == 0 ? "" : Utils.formatTien(snk.getOpeningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setCoPhatSinh(BigDecimal.ZERO.compareTo(snk.getCreditAmount()) == 0 ? "" : Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setNoPhatSinh(BigDecimal.ZERO.compareTo(snk.getDebitAmount()) == 0 ? "" : Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setCoCuoiKy(BigDecimal.ZERO.compareTo(snk.getClosingCreditAmount()) == 0 ? "" : Utils.formatTien(snk.getClosingCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setNoCuoiKy(BigDecimal.ZERO.compareTo(snk.getClosingDebitAmount()) == 0 ? "" : Utils.formatTien(snk.getClosingDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            if (snk.getGrade().intValue() == Constants.Account.GRADE_PARENT) {
                sumNoDK = sumNoDK.add(snk.getOpeningDebitAmount());
                sumCoDK = sumCoDK.add(snk.getOpeningCreditAmount());
                sumNoPS = sumNoPS.add(snk.getDebitAmount());
                sumCoPS = sumCoPS.add(snk.getCreditAmount());
                sumNoCK = sumNoCK.add(snk.getClosingDebitAmount());
                sumCoCK = sumCoCK.add(snk.getClosingCreditAmount());
            }
        }
        parameter.put("sumNoDK", BigDecimal.ZERO.compareTo(sumNoDK) == 0 ? "" : Utils.formatTien(sumNoDK, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("sumCoDK", BigDecimal.ZERO.compareTo(sumCoDK) == 0 ? "" : Utils.formatTien(sumCoDK, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("sumNoPS", BigDecimal.ZERO.compareTo(sumNoPS) == 0 ? "" : Utils.formatTien(sumNoPS, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("sumCoPS", BigDecimal.ZERO.compareTo(sumCoPS) == 0 ? "" : Utils.formatTien(sumCoPS, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("sumNoCK", BigDecimal.ZERO.compareTo(sumNoCK) == 0 ? "" : Utils.formatTien(sumNoCK, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("sumCoCK", BigDecimal.ZERO.compareTo(sumCoCK) == 0 ? "" : Utils.formatTien(sumCoCK, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(canDoiTaiKhoanDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelSoChiTietVatLieu(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoChiTietVatLieuDTO> soChiTietVatLieuDTOS = reportBusinessRepository.getSoChiTietVatLieu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            requestReport.getRepositoryID(),
            materialGoods,
            currentBook, requestReport.getDependent());
        if (soChiTietVatLieuDTOS.size() == 0) {
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
        }
        List<List<SoChiTietVatLieuDTO>> lstChiTietVatLieu = new ArrayList<>();
        List<SoChiTietVatLieuDTO> child = new ArrayList<>();
        for (int i = 0; i < soChiTietVatLieuDTOS.size(); i++) {
            child.add(soChiTietVatLieuDTOS.get(i));
            if ((i < soChiTietVatLieuDTOS.size() - 1 && soChiTietVatLieuDTOS.get(i).getMaterialGoodsID() != null &&
                (!soChiTietVatLieuDTOS.get(i).getMaterialGoodsID().equals(soChiTietVatLieuDTOS.get(i + 1).getMaterialGoodsID()) ||
                    !soChiTietVatLieuDTOS.get(i).getRepositoryID().equals(soChiTietVatLieuDTOS.get(i + 1).getRepositoryID()))) || (i == soChiTietVatLieuDTOS.size() - 1)) {
                lstChiTietVatLieu.add(child);
                child = new ArrayList<>();
            }
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);
            CellStyle styleI = workbook.createCellStyle();
            styleI.setFont(fontI);
            styleI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 1; i < lstChiTietVatLieu.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < lstChiTietVatLieu.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (lstChiTietVatLieu.get(i).size() - 1 > 0) {
                    sheet.shiftRows(13, sheet.getLastRowNum(), lstChiTietVatLieu.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 12;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(7).getCell(2).setCellValue("Kho: " +
                    (lstChiTietVatLieu.get(i).get(0).getRepositoryCode() != null ? lstChiTietVatLieu.get(i).get(0).getRepositoryCode() : "")
                    + (lstChiTietVatLieu.get(i).get(0).getRepositoryName() != null ? (" - " + lstChiTietVatLieu.get(i).get(0).getRepositoryName()) : ""));
                sheet.getRow(6).getCell(2).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(8).getCell(0).setCellValue("Mã hàng: " + (lstChiTietVatLieu.get(i).get(0).getMaterialGoodsCode() != null ? lstChiTietVatLieu.get(i).get(0).getMaterialGoodsCode() : ""));
                sheet.getRow(8).getCell(3).setCellValue("Tên Hàng: " + (lstChiTietVatLieu.get(i).get(0).getMaterialGoodsName() != null ? lstChiTietVatLieu.get(i).get(0).getMaterialGoodsName() : ""));
                sheet.getRow(8).getCell(11).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
                sheet.getRow(8).getCell(11).setCellStyle(styleI);
                //
                BigDecimal tongSLNhap = BigDecimal.ZERO;
                BigDecimal tongTienNhap = BigDecimal.ZERO;
                BigDecimal tongSLXuat = BigDecimal.ZERO;
                BigDecimal tongTienXuat = BigDecimal.ZERO;
                BigDecimal tongSLTon = BigDecimal.ZERO;
                BigDecimal tongTienTon = BigDecimal.ZERO;
                for (SoChiTietVatLieuDTO item : lstChiTietVatLieu.get(i)) {
                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue(item.getRefNo());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue((item.getRefDate() != null ? item.getRefDate().format(formatters) : ""));

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getReason() != null ? item.getReason() : "");

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getCorrespondingAccountNumber() != null ? item.getCorrespondingAccountNumber() : "");

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    cell5.setCellValue(item.getUnitName());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getUnitPrice().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getInwardQuantity().doubleValue());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getInwardAmount().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    cell9.setCellValue(item.getOutwardQuantity().doubleValue());

                    Cell cell10 = row.createCell(9);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    cell10.setCellValue(item.getOutwardAmount().doubleValue());

                    Cell cell11 = row.createCell(10);
                    cell11.setCellStyle(style);
                    CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                    cell11.setCellValue(item.getClosingQuantity().doubleValue());

                    Cell cell12 = row.createCell(11);
                    cell12.setCellStyle(style);
                    CellUtil.setAlignment(cell12, HorizontalAlignment.RIGHT);
                    cell12.setCellValue(item.getClosingAmount().doubleValue());

                    Cell cell13 = row.createCell(12);
                    cell13.setCellStyle(style);
                    CellUtil.setAlignment(cell13, HorizontalAlignment.RIGHT);
                    cell13.setCellValue("");
                    start++;

                    tongSLNhap = tongSLNhap.add(item.getInwardQuantity());
                    tongTienNhap = tongTienNhap.add(item.getInwardAmount());
                    tongSLXuat = tongSLXuat.add(item.getOutwardQuantity());
                    tongTienXuat = tongTienXuat.add(item.getOutwardAmount());
                    tongSLTon = item.getClosingQuantity();
                    tongTienTon = item.getClosingAmount();
                }
                Row rowSum = sheet.createRow(12 + lstChiTietVatLieu.get(i).size());
                rowSum.createCell(0);
                rowSum.getCell(0).setCellStyle(styleB);
                rowSum.getCell(0).setCellValue("Cộng");

                rowSum.createCell(1);
                rowSum.getCell(1).setCellStyle(styleB);
                rowSum.createCell(2);
                rowSum.getCell(2).setCellStyle(styleB);
                rowSum.createCell(3);
                rowSum.getCell(3).setCellStyle(styleB);
                rowSum.createCell(4);
                rowSum.getCell(4).setCellStyle(styleB);

                Cell cell6 = rowSum.createCell(5);
                cell6.setCellStyle(styleB);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue("");

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(tongSLNhap.doubleValue());

                Cell cell8 = rowSum.createCell(7);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(tongTienNhap.doubleValue());

                Cell cell9 = rowSum.createCell(8);
                cell9.setCellStyle(styleB);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(tongSLXuat.doubleValue());

                Cell cell10 = rowSum.createCell(9);
                cell10.setCellStyle(styleB);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.setCellValue(tongTienXuat.doubleValue());

                Cell cell11 = rowSum.createCell(10);
                cell11.setCellStyle(styleB);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                cell11.setCellValue(tongSLTon.doubleValue());

                Cell cell12 = rowSum.createCell(11);
                cell12.setCellStyle(styleB);
                CellUtil.setAlignment(cell12, HorizontalAlignment.RIGHT);
                cell12.setCellValue(tongTienTon.doubleValue());

                Cell cell13 = rowSum.createCell(12);
                cell13.setCellStyle(styleB);
                CellUtil.setAlignment(cell13, HorizontalAlignment.RIGHT);
                cell13.setCellValue("");

                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                CellStyle styleDf = workbook.createCellStyle();
                styleDf.setFont(font);
                sheet.createRow(14 + lstChiTietVatLieu.get(i).size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
                sheet.createRow(15 + lstChiTietVatLieu.get(i).size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
                sheet.getRow(14 + lstChiTietVatLieu.get(i).size()).getCell(0).setCellStyle(styleDf);
                sheet.getRow(15 + lstChiTietVatLieu.get(i).size()).getCell(0).setCellStyle(styleDf);
                SetFooterExcel(workbook, sheet, userDTO, (17 + lstChiTietVatLieu.get(i).size()), 10, 0, 4, 10);
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getSoChiTietVatLieu(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoChiTietVatLieu";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<Type> types = typeRepository.findAllByIsActive();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SAInvoiceViewDTO> saInvoiceDTOS = saInvoiceRepository.getAllSAInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<SaReturnDTO> saReturnDTOS = saReturnRepository.getAllSAReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<ViewPPInvoiceDTO> ppInvoiceDTOS = ppInvoiceRepository.getAllPPInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<PPDiscountReturnDTO> ppDiscountReturnDTOS = ppDiscountReturnRepository.getAllPPDiscountReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<SoChiTietVatLieuDTO> soChiTietVatLieuDTOS = reportBusinessRepository.getSoChiTietVatLieu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            requestReport.getRepositoryID(),
            materialGoods,
            currentBook, requestReport.getDependent());
        if (soChiTietVatLieuDTOS.size() == 0) {
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
            soChiTietVatLieuDTOS.add(new SoChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("maKho", soChiTietVatLieuDTOS.size() > 0 ? soChiTietVatLieuDTOS.get(0).getRepositoryCode() : "");
        parameter.put("tenKho", soChiTietVatLieuDTOS.size() > 0 ? soChiTietVatLieuDTOS.get(0).getRepositoryName() : "");
        parameter.put("REPORT_MAX_COUNT", soChiTietVatLieuDTOS.size());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        checkEbPackage(userDTO, parameter);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Integer soThuTu = 1;
        for (int i = 0; i < soChiTietVatLieuDTOS.size(); i++) {
            BigDecimal tongSLNhap = BigDecimal.ZERO;
            BigDecimal tongTienNhap = BigDecimal.ZERO;
            BigDecimal tongSLXuat = BigDecimal.ZERO;
            BigDecimal tongTienXuat = BigDecimal.ZERO;
            BigDecimal tongSLTon = BigDecimal.ZERO;
            BigDecimal tongTienTon = BigDecimal.ZERO;
            soChiTietVatLieuDTOS.get(i).setNgayChungTu(soChiTietVatLieuDTOS.get(i).getRefDate() == null ? "" : soChiTietVatLieuDTOS.get(i).getRefDate().format(formatters));
            soChiTietVatLieuDTOS.get(i).setDonGia(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getUnitPrice()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getUnitPrice(), Constants.SystemOption.DDSo_DonGia, userDTO));
            soChiTietVatLieuDTOS.get(i).setSlNhap(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getInwardQuantity()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getInwardQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setSlXuat(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getOutwardQuantity()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getOutwardQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setSlTon(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getClosingQuantity()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getClosingQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setTienNhap(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getInwardAmount()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getInwardAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soChiTietVatLieuDTOS.get(i).setTienXuat(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getOutwardAmount()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getOutwardAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soChiTietVatLieuDTOS.get(i).setTienTon(BigDecimal.ZERO.compareTo(soChiTietVatLieuDTOS.get(i).getClosingAmount()) == 0 ? "" : Utils.formatTien(soChiTietVatLieuDTOS.get(i).getClosingAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            for (SoChiTietVatLieuDTO item : soChiTietVatLieuDTOS) {
                if (item.getMaterialGoodsID() != null && item.getRepositoryID() != null && soChiTietVatLieuDTOS.get(i).getMaterialGoodsID().equals(item.getMaterialGoodsID()) && soChiTietVatLieuDTOS.get(i).getRepositoryID().equals(item.getRepositoryID())) {
                    tongSLNhap = tongSLNhap.add(item.getInwardQuantity());
                    tongTienNhap = tongTienNhap.add(item.getInwardAmount());
                    tongSLXuat = tongSLXuat.add(item.getOutwardQuantity());
                    tongTienXuat = tongTienXuat.add(item.getOutwardAmount());
                    tongSLTon = item.getClosingQuantity();
                    tongTienTon = item.getClosingAmount();
                }
            }
            soChiTietVatLieuDTOS.get(i).setLinkRef(getRefLink(soChiTietVatLieuDTOS.get(i).getRefType(), soChiTietVatLieuDTOS.get(i).getRefID(), types, saInvoiceDTOS, saReturnDTOS, ppInvoiceDTOS, ppDiscountReturnDTOS));
            soChiTietVatLieuDTOS.get(i).setStt((soThuTu++));
            soChiTietVatLieuDTOS.get(i).setTongSLNhap(BigDecimal.ZERO.compareTo(tongSLNhap) == 0 ? "" : Utils.formatTien(tongSLNhap, Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setTongSLXuat(BigDecimal.ZERO.compareTo(tongSLXuat) == 0 ? "" : Utils.formatTien(tongSLXuat, Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setTongSLTon(BigDecimal.ZERO.compareTo(tongSLTon) == 0 ? "" : Utils.formatTien(tongSLTon, Constants.SystemOption.DDSo_SoLuong, userDTO));
            soChiTietVatLieuDTOS.get(i).setTongTienNhap(BigDecimal.ZERO.compareTo(tongTienNhap) == 0 ? "" : Utils.formatTien(tongTienNhap, Constants.SystemOption.DDSo_TienVND, userDTO));
            soChiTietVatLieuDTOS.get(i).setTongTienXuat(BigDecimal.ZERO.compareTo(tongTienXuat) == 0 ? "" : Utils.formatTien(tongTienXuat, Constants.SystemOption.DDSo_TienVND, userDTO));
            soChiTietVatLieuDTOS.get(i).setTongTienTon(BigDecimal.ZERO.compareTo(tongTienTon) == 0 ? "" : Utils.formatTien(tongTienTon, Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soChiTietVatLieuDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelTheKho(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<TheKhoDTO> theKhoDTOS = reportBusinessRepository.getTheKho(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            requestReport.getRepositoryID(),
            materialGoods,
            currentBook, requestReport.getDependent());
        if (theKhoDTOS.size() == 0) {
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        List<List<TheKhoDTO>> lstTheKho = new ArrayList<>();
        List<TheKhoDTO> child = new ArrayList<>();
        Integer soThuTu = 1;
        for (int i = 0; i < theKhoDTOS.size(); i++) {
            theKhoDTOS.get(i).setStt((soThuTu++));
            if (theKhoDTOS.get(i).getRefType() != null && (theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO) == 0 || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_BAN_HANG) == 0 || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_DIEU_CHINH) == 0
                || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI) == 0)) {
                theKhoDTOS.get(i).setNoXK(theKhoDTOS.get(i).getRefNo());
            } else {
                theKhoDTOS.get(i).setNoNK(theKhoDTOS.get(i).getRefNo());
            }
            child.add(theKhoDTOS.get(i));
            if ((i < theKhoDTOS.size() - 1 && theKhoDTOS.get(i).getMaterialGoodsID() != null &&
                !theKhoDTOS.get(i).getMaterialGoodsID().equals(theKhoDTOS.get(i + 1).getMaterialGoodsID())) || (i == theKhoDTOS.size() - 1)) {
                lstTheKho.add(child);
                child = new ArrayList<>();
                soThuTu = 1;
            }
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 1; i < lstTheKho.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < lstTheKho.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (lstTheKho.get(i).size() - 1 > 0) {
                    sheet.shiftRows(14, sheet.getLastRowNum(), lstTheKho.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 13;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(6).getCell(1).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(8).getCell(0).setCellValue("Tên, nhãn hiệu, quy cách, vât tư: " +
                    (lstTheKho.get(i).get(0).getMaterialGoodsCode() != null ? lstTheKho.get(i).get(0).getMaterialGoodsCode() : "") +
                    (lstTheKho.get(i).get(0).getMaterialGoodsName() != null ? (" - " + lstTheKho.get(i).get(0).getMaterialGoodsName()) : ""));
                sheet.getRow(9).getCell(0).setCellValue("Đơn vị tính: " + (lstTheKho.get(i).get(0).getUnitName() != null ? lstTheKho.get(i).get(0).getUnitName() : ""));
                //
                BigDecimal tongSLNhap = BigDecimal.ZERO;
                BigDecimal tongSLXuat = BigDecimal.ZERO;
                BigDecimal tongSLTon = BigDecimal.ZERO;

                for (TheKhoDTO item : lstTheKho.get(i)) {
                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getStt());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue((item.getRefDate() != null ? item.getRefDate().format(formatters) : ""));

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                    cell3.setCellValue(item.getNoNK() != null ? item.getNoNK() : "");

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getNoXK() != null ? item.getNoXK() : "");

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    cell5.setCellValue(item.getReason() != null ? item.getReason() : "");

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                    cell6.setCellValue((item.getPostedDate() != null ? item.getPostedDate().format(formatters) : ""));

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getInwardQuantity().doubleValue());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getOutwardQuantity().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    cell9.setCellValue(item.getClosingQuantity().doubleValue());

                    Cell cell10 = row.createCell(9);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    cell10.setCellValue("");
                    start++;

                    tongSLNhap = tongSLNhap.add(item.getInwardQuantity());
                    tongSLXuat = tongSLXuat.add(item.getOutwardQuantity());
                    tongSLTon = item.getClosingQuantity();
                }

                Row rowSum = sheet.createRow(13 + lstTheKho.get(i).size());
                rowSum.createCell(0);
                rowSum.getCell(0).setCellStyle(styleB);
                rowSum.getCell(0).setCellValue("Cộng cuối kỳ");
                rowSum.createCell(1);
                rowSum.getCell(1).setCellStyle(styleB);
                rowSum.createCell(2);
                rowSum.getCell(2).setCellStyle(styleB);
                rowSum.createCell(3);
                rowSum.getCell(3).setCellStyle(styleB);
                rowSum.createCell(4);
                rowSum.getCell(4).setCellStyle(styleB);
                rowSum.createCell(5);
                rowSum.getCell(5).setCellStyle(styleB);

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(tongSLNhap.doubleValue());

                Cell cell8 = rowSum.createCell(7);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(tongSLXuat.doubleValue());

                Cell cell9 = rowSum.createCell(8);
                cell9.setCellStyle(styleB);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(tongSLTon.doubleValue());

                Cell cell10 = rowSum.createCell(9);
                cell10.setCellStyle(styleB);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.setCellValue("");

                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                CellStyle styleDf = workbook.createCellStyle();
                styleDf.setFont(font);
                styleDf.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                styleDf.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                sheet.createRow(15 + lstTheKho.get(i).size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
                sheet.createRow(16 + lstTheKho.get(i).size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
                sheet.getRow(15 + lstTheKho.get(i).size()).getCell(0).setCellStyle(styleDf);
                sheet.getRow(16 + lstTheKho.get(i).size()).getCell(0).setCellStyle(styleDf);
                SetFooterExcel(workbook, sheet, userDTO, (18 + lstTheKho.get(i).size()), 8, 0, 4, 8);
                sheet.getRow((19 + lstTheKho.get(i).size())).getCell(0).setCellValue("Người ghi sổ");
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getTheKho(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "TheKho";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<Type> types = typeRepository.findAllByIsActive();
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SAInvoiceViewDTO> saInvoiceDTOS = saInvoiceRepository.getAllSAInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<SaReturnDTO> saReturnDTOS = saReturnRepository.getAllSAReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<ViewPPInvoiceDTO> ppInvoiceDTOS = ppInvoiceRepository.getAllPPInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<PPDiscountReturnDTO> ppDiscountReturnDTOS = ppDiscountReturnRepository.getAllPPDiscountReturnHasRSID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        List<TheKhoDTO> theKhoDTOS = reportBusinessRepository.getTheKho(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getOption(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            requestReport.getRepositoryID(),
            materialGoods,
            currentBook, requestReport.getDependent());
        if (theKhoDTOS.size() == 0) {
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            theKhoDTOS.add(new TheKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", theKhoDTOS.size());
        checkEbPackage(userDTO, parameter);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Integer soThuTu = 1;
        for (int i = 0; i < theKhoDTOS.size(); i++) {
            BigDecimal tongSLNhap = BigDecimal.ZERO;
            BigDecimal tongSLXuat = BigDecimal.ZERO;
            BigDecimal tongSLTon = BigDecimal.ZERO;
            theKhoDTOS.get(i).setNgayChungTu(theKhoDTOS.get(i).getRefDate() == null ? "" : theKhoDTOS.get(i).getRefDate().format(formatters));
            theKhoDTOS.get(i).setNgayHachToan(theKhoDTOS.get(i).getPostedDate() == null ? "" : theKhoDTOS.get(i).getPostedDate().format(formatters));
            theKhoDTOS.get(i).setSlNhap(BigDecimal.ZERO.compareTo(theKhoDTOS.get(i).getInwardQuantity()) == 0 ? "" : Utils.formatTien(theKhoDTOS.get(i).getInwardQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            theKhoDTOS.get(i).setSlXuat(BigDecimal.ZERO.compareTo(theKhoDTOS.get(i).getOutwardQuantity()) == 0 ? "" : Utils.formatTien(theKhoDTOS.get(i).getOutwardQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            theKhoDTOS.get(i).setSlTon(BigDecimal.ZERO.compareTo(theKhoDTOS.get(i).getClosingQuantity()) == 0 ? "" : Utils.formatTien(theKhoDTOS.get(i).getClosingQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            for (TheKhoDTO item : theKhoDTOS) {
                if (item.getMaterialGoodsID() != null && theKhoDTOS.get(i).getMaterialGoodsID().equals(item.getMaterialGoodsID())) {
                    theKhoDTOS.get(i).setUnitName(item.getUnitName());
                    tongSLNhap = tongSLNhap.add(item.getInwardQuantity());
                    tongSLXuat = tongSLXuat.add(item.getOutwardQuantity());
                    tongSLTon = item.getClosingQuantity();
                }
            }
            theKhoDTOS.get(i).setStt((soThuTu++));
            theKhoDTOS.get(i).setTongSLNhap(BigDecimal.ZERO.compareTo(tongSLNhap) == 0 ? "" : Utils.formatTien(tongSLNhap, Constants.SystemOption.DDSo_SoLuong, userDTO));
            theKhoDTOS.get(i).setTongSLXuat(BigDecimal.ZERO.compareTo(tongSLXuat) == 0 ? "" : Utils.formatTien(tongSLXuat, Constants.SystemOption.DDSo_SoLuong, userDTO));
            theKhoDTOS.get(i).setTongSLTon(BigDecimal.ZERO.compareTo(tongSLTon) == 0 ? "" : Utils.formatTien(tongSLTon, Constants.SystemOption.DDSo_SoLuong, userDTO));
            if (theKhoDTOS.get(i).getRefType() != null && (theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO) == 0 || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_BAN_HANG) == 0 || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_DIEU_CHINH) == 0
                || theKhoDTOS.get(i).getRefType().compareTo(TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI) == 0)) {
                theKhoDTOS.get(i).setNoXK(theKhoDTOS.get(i).getRefNo());
            } else {
                theKhoDTOS.get(i).setNoNK(theKhoDTOS.get(i).getRefNo());
            }
            if ((i < theKhoDTOS.size() - 1 && theKhoDTOS.get(i).getMaterialGoodsID() != null &&
                !theKhoDTOS.get(i).getMaterialGoodsID().equals(theKhoDTOS.get(i + 1).getMaterialGoodsID())) || (i == theKhoDTOS.size() - 1)) {
                soThuTu = 1;
            }
            theKhoDTOS.get(i).setLinkRef(getRefLink(theKhoDTOS.get(i).getRefType(), theKhoDTOS.get(i).getRefID(), types, saInvoiceDTOS, saReturnDTOS, ppInvoiceDTOS, ppDiscountReturnDTOS));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(theKhoDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelTongHopChiTietVatLieu(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String accountNum = ",";
        for (String item : requestReport.getAccountList()) {
            accountNum += item.toString() + ",";
        }
        List<TongHopChiTietVatLieuDTO> tongHopChiTietVatLieuDTOS = reportBusinessRepository.getTongHopChiTietVatLieu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountNum,
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        if (tongHopChiTietVatLieuDTOS.size() == 0) {
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        Integer soThuTu = 1;
        List<List<TongHopChiTietVatLieuDTO>> lstTongHopTonKho = new ArrayList<>();
        List<TongHopChiTietVatLieuDTO> child = new ArrayList<>();
        for (int i = 0; i < tongHopChiTietVatLieuDTOS.size(); i++) {
            tongHopChiTietVatLieuDTOS.get(i).setStt((soThuTu++));
            child.add(tongHopChiTietVatLieuDTOS.get(i));
            if ((i < tongHopChiTietVatLieuDTOS.size() - 1 && tongHopChiTietVatLieuDTOS.get(i).getAccount() != null &&
                !tongHopChiTietVatLieuDTOS.get(i).getAccount().equals(tongHopChiTietVatLieuDTOS.get(i + 1).getAccount())) || (i == tongHopChiTietVatLieuDTOS.size() - 1)) {
                lstTongHopTonKho.add(child);
                child = new ArrayList<>();
                soThuTu = 1;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 1; i < lstTongHopTonKho.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < lstTongHopTonKho.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (lstTongHopTonKho.get(i).size() - 1 > 0) {
                    sheet.shiftRows(12, sheet.getLastRowNum(), lstTongHopTonKho.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 11;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(6).getCell(0).setCellValue("Tài khoản: " + (lstTongHopTonKho.get(i).get(0).getAccount() != null ? lstTongHopTonKho.get(i).get(0).getAccount() : ""));
                sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                //
                BigDecimal tongTienDK = BigDecimal.ZERO;
                BigDecimal tongTienNK = BigDecimal.ZERO;
                BigDecimal tongTienXK = BigDecimal.ZERO;
                BigDecimal tongTienCK = BigDecimal.ZERO;
                for (TongHopChiTietVatLieuDTO item : lstTongHopTonKho.get(i)) {
                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getStt());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getMaterialGoodsCode());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getMaterialGoodsName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                    cell4.setCellValue(item.getAmountOpening().doubleValue());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    cell5.setCellValue(item.getiWAmount().doubleValue());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getoWAmount().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getAmountClosing().doubleValue());
                    start++;
                    tongTienDK = tongTienDK.add(item.getAmountOpening());
                    tongTienNK = tongTienNK.add(item.getiWAmount());
                    tongTienXK = tongTienXK.add(item.getoWAmount());
                    tongTienCK = tongTienCK.add(item.getAmountClosing());
                }

                Row rowSum = sheet.createRow(11 + lstTongHopTonKho.get(i).size());
                rowSum.createCell(0);
                rowSum.getCell(0).setCellStyle(styleB);
                rowSum.getCell(0).setCellValue("Cộng");
                rowSum.createCell(1);
                rowSum.getCell(1).setCellStyle(styleB);
                rowSum.createCell(2);
                rowSum.getCell(2).setCellStyle(styleB);

                Cell cell4 = rowSum.createCell(3);
                cell4.setCellStyle(styleB);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(tongTienDK.doubleValue());

                Cell cell5 = rowSum.createCell(4);
                cell5.setCellStyle(styleB);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(tongTienNK.doubleValue());

                Cell cell6 = rowSum.createCell(5);
                cell6.setCellStyle(styleB);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(tongTienXK.doubleValue());

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(tongTienCK.doubleValue());

                Font font = workbook.createFont();
                font.setFontName("Times New Roman");
                CellStyle styleDf = workbook.createCellStyle();
                styleDf.setFont(font);
                sheet.createRow(13 + lstTongHopTonKho.get(i).size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
                sheet.createRow(14 + lstTongHopTonKho.get(i).size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
                sheet.getRow(13 + lstTongHopTonKho.get(i).size()).getCell(0).setCellStyle(styleDf);
                sheet.getRow(14 + lstTongHopTonKho.get(i).size()).getCell(0).setCellStyle(styleDf);
                SetFooterExcel(workbook, sheet, userDTO, (16 + lstTongHopTonKho.get(i).size()), 6, 0, 3, 6);
                sheet.getRow((17 + lstTongHopTonKho.get(i).size())).getCell(0).setCellValue("Người ghi sổ");
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getTongHopChiTietVatLieu(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "TongHopChiTietVatLieu";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        String accountNum = ",";
        for (String item : requestReport.getAccountList()) {
            accountNum += item.toString() + ",";
        }
        List<TongHopChiTietVatLieuDTO> tongHopChiTietVatLieuDTOS = reportBusinessRepository.getTongHopChiTietVatLieu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accountNum,
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        if (tongHopChiTietVatLieuDTOS.size() == 0) {
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopChiTietVatLieuDTOS.add(new TongHopChiTietVatLieuDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", tongHopChiTietVatLieuDTOS.size());
        checkEbPackage(userDTO, parameter);
        Integer soThuTu = 1;
        for (int i = 0; i < tongHopChiTietVatLieuDTOS.size(); i++) {
            BigDecimal tongTienDK = BigDecimal.ZERO;
            BigDecimal tongTienNK = BigDecimal.ZERO;
            BigDecimal tongTienXK = BigDecimal.ZERO;
            BigDecimal tongTienCK = BigDecimal.ZERO;
            tongHopChiTietVatLieuDTOS.get(i).setTienDauKy(BigDecimal.ZERO.compareTo(tongHopChiTietVatLieuDTOS.get(i).getAmountOpening()) == 0 ? "" : Utils.formatTien(tongHopChiTietVatLieuDTOS.get(i).getAmountOpening(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTienNhap(BigDecimal.ZERO.compareTo(tongHopChiTietVatLieuDTOS.get(i).getiWAmount()) == 0 ? "" : Utils.formatTien(tongHopChiTietVatLieuDTOS.get(i).getiWAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTienXuat(BigDecimal.ZERO.compareTo(tongHopChiTietVatLieuDTOS.get(i).getoWAmount()) == 0 ? "" : Utils.formatTien(tongHopChiTietVatLieuDTOS.get(i).getoWAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTienTon(BigDecimal.ZERO.compareTo(tongHopChiTietVatLieuDTOS.get(i).getAmountClosing()) == 0 ? "" : Utils.formatTien(tongHopChiTietVatLieuDTOS.get(i).getAmountClosing(), Constants.SystemOption.DDSo_TienVND, userDTO));
            for (TongHopChiTietVatLieuDTO item : tongHopChiTietVatLieuDTOS) {
                if (item.getAccount() != null && tongHopChiTietVatLieuDTOS.get(i).getAccount().equals(item.getAccount())) {
                    tongTienDK = tongTienDK.add(item.getAmountOpening());
                    tongTienNK = tongTienNK.add(item.getiWAmount());
                    tongTienXK = tongTienXK.add(item.getoWAmount());
                    tongTienCK = tongTienCK.add(item.getAmountClosing());
                }
            }
            if (i == 0 || (tongHopChiTietVatLieuDTOS.get(i).getAccount() != null && !tongHopChiTietVatLieuDTOS.get(i).getAccount().equals(tongHopChiTietVatLieuDTOS.get(i - 1).getAccount()))) {
                soThuTu = 1;
            }
            tongHopChiTietVatLieuDTOS.get(i).setStt((soThuTu++));
            tongHopChiTietVatLieuDTOS.get(i).setTongTienDauKy(BigDecimal.ZERO.compareTo(tongTienDK) == 0 ? "" : Utils.formatTien(tongTienDK, Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTongTienNhap(BigDecimal.ZERO.compareTo(tongTienNK) == 0 ? "" : Utils.formatTien(tongTienNK, Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTongTienXuat(BigDecimal.ZERO.compareTo(tongTienXK) == 0 ? "" : Utils.formatTien(tongTienXK, Constants.SystemOption.DDSo_TienVND, userDTO));
            tongHopChiTietVatLieuDTOS.get(i).setTongTienTon(BigDecimal.ZERO.compareTo(tongTienCK) == 0 ? "" : Utils.formatTien(tongTienCK, Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongHopChiTietVatLieuDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelTongHopTonKho(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String repositoryIDs = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            repositoryIDs += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<TongHopTonKhoDTO> tongHopTonKhoDTOS = reportBusinessRepository.getTongHopTonKho(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            repositoryIDs,
            currentBook,
            requestReport.getMaterialGoodsCategoryID(), requestReport.getDependent());
        if (tongHopTonKhoDTOS.size() == 0) {
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        List<List<TongHopTonKhoDTO>> lstTongHopTonKho = new ArrayList<>();
        List<TongHopTonKhoDTO> child = new ArrayList<>();
        double totalAllSLDK = 0;
        double totalAllTienDK = 0;
        double totalAllSLNhap = 0;
        double totalAllTienNhap = 0;
        double totalAllSLXuat = 0;
        double totalAllTienXuat = 0;
        double totalAllSLTon = 0;
        double totalAllTienTon = 0;
        for (int i = 0; i < tongHopTonKhoDTOS.size(); i++) {
            totalAllSLDK += tongHopTonKhoDTOS.get(i).getOpeningQuantity().doubleValue();
            totalAllTienDK += tongHopTonKhoDTOS.get(i).getOpeningAmount().doubleValue();
            totalAllSLNhap += tongHopTonKhoDTOS.get(i).getIwQuantity().doubleValue();
            totalAllTienNhap += tongHopTonKhoDTOS.get(i).getIwAmount().doubleValue();
            totalAllSLXuat += tongHopTonKhoDTOS.get(i).getOwQuantity().doubleValue();
            totalAllTienXuat += tongHopTonKhoDTOS.get(i).getOwAmount().doubleValue();
            totalAllSLTon += tongHopTonKhoDTOS.get(i).getClosingQuantity().doubleValue();
            totalAllTienTon += tongHopTonKhoDTOS.get(i).getClosingAmount().doubleValue();
            child.add(tongHopTonKhoDTOS.get(i));
            if ((i < tongHopTonKhoDTOS.size() - 1 && tongHopTonKhoDTOS.get(i).getRepositoryID() != null &&
                !tongHopTonKhoDTOS.get(i).getRepositoryID().equals(tongHopTonKhoDTOS.get(i + 1).getRepositoryID())) || (i == tongHopTonKhoDTOS.size() - 1)) {
                lstTongHopTonKho.add(child);
                child = new ArrayList<>();
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            // fill du lieu sheet 0
            Sheet sheet = workbook.getSheetAt(0);
            if (tongHopTonKhoDTOS.size() - 1 > 0) {
                sheet.shiftRows(11, sheet.getLastRowNum(), (tongHopTonKhoDTOS.size() + (lstTongHopTonKho.size() * 2) - 1));
            }
            int start = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            int rowCout = 0;
            for (int i = 0; i < lstTongHopTonKho.size(); i++) {
                Row rowGroup1 = sheet.getRow(start + rowCout);
                if (rowGroup1 == null) {
                    rowGroup1 = sheet.createRow(start + rowCout);
                }
                sheet.addMergedRegion(new CellRangeAddress(start + rowCout, start + rowCout, 0, 10));
                rowGroup1.createCell(0);
                rowGroup1.createCell(1);
                rowGroup1.createCell(2);
                rowGroup1.createCell(10);
                rowGroup1.getCell(10).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellValue("Tên kho: " + (lstTongHopTonKho.get(i).get(0).getRepositoryName() != null ? lstTongHopTonKho.get(i).get(0).getRepositoryName() : ""));
                rowCout++;

                double totalSLDK = 0;
                double totalTienDK = 0;
                double totalSLNhap = 0;
                double totalTienNhap = 0;
                double totalSLXuat = 0;
                double totalTienXuat = 0;
                double totalSLTon = 0;
                double totalTienTon = 0;

                for (TongHopTonKhoDTO item : lstTongHopTonKho.get(i)) {
                    Row row = sheet.getRow(start + rowCout);
                    if (row == null) {
                        row = sheet.createRow(start + rowCout);
                    }
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue(item.getMaterialGoodsCode());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue((item.getMaterialGoodsName() != null ? item.getMaterialGoodsName() : ""));

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getUnitName() != null ? item.getUnitName() : "");

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                    cell4.setCellValue(item.getOpeningQuantity().doubleValue());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    cell5.setCellValue(item.getOpeningAmount().doubleValue());

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getIwQuantity().doubleValue());

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getIwAmount().doubleValue());

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getOwQuantity().doubleValue());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    cell9.setCellValue(item.getOwAmount().doubleValue());

                    Cell cell10 = row.createCell(9);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    cell10.setCellValue(item.getClosingQuantity().doubleValue());

                    Cell cell11 = row.createCell(10);
                    cell11.setCellStyle(style);
                    CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                    cell11.setCellValue(item.getClosingAmount().doubleValue());

                    totalSLDK += item.getOpeningQuantity().doubleValue();
                    totalTienDK += item.getOpeningAmount().doubleValue();
                    totalSLNhap += item.getIwQuantity().doubleValue();
                    totalTienNhap += item.getIwAmount().doubleValue();
                    totalSLXuat += item.getOwQuantity().doubleValue();
                    totalTienXuat += item.getOwAmount().doubleValue();
                    totalSLTon += item.getClosingQuantity().doubleValue();
                    totalTienTon += item.getClosingAmount().doubleValue();

                    rowCout++;
                }
                Row rowGroup2 = sheet.createRow(start + rowCout);
                sheet.addMergedRegion(new CellRangeAddress(start + rowCout, start + rowCout, 0, 2));
                rowGroup2.createCell(0);
                rowGroup2.createCell(1);
                rowGroup2.getCell(1).setCellStyle(styleB);
                rowGroup2.createCell(2);
                rowGroup2.getCell(2).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellValue("Cộng nhóm: " + (lstTongHopTonKho.get(i).get(0).getRepositoryName() != null ? lstTongHopTonKho.get(i).get(0).getRepositoryName() : ""));

                Cell cell4 = rowGroup2.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(totalSLDK);

                Cell cell5 = rowGroup2.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(totalTienDK);

                Cell cell6 = rowGroup2.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(totalSLNhap);

                Cell cell7 = rowGroup2.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(totalTienNhap);

                Cell cell8 = rowGroup2.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(totalSLXuat);

                Cell cell9 = rowGroup2.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(totalTienXuat);

                Cell cell10 = rowGroup2.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.setCellValue(totalSLTon);

                Cell cell11 = rowGroup2.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                cell11.setCellValue(totalTienTon);
                rowCout++;
            }
            Row rowSum = sheet.createRow(start + rowCout);
            rowSum.createCell(0);
            rowSum.createCell(1);
            rowSum.getCell(1).setCellStyle(styleB);
            rowSum.createCell(2);
            rowSum.getCell(2).setCellStyle(styleB);
            rowSum.getCell(0).setCellStyle(styleB);
            rowSum.getCell(0).setCellValue("Tổng cộng");

            Cell cell4 = rowSum.createCell(3);
            cell4.setCellStyle(styleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.setCellValue(totalAllSLDK);

            Cell cell5 = rowSum.createCell(4);
            cell5.setCellStyle(styleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.setCellValue(totalAllTienDK);

            Cell cell6 = rowSum.createCell(5);
            cell6.setCellStyle(styleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalAllSLNhap);

            Cell cell7 = rowSum.createCell(6);
            cell7.setCellStyle(styleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalAllTienNhap);

            Cell cell8 = rowSum.createCell(7);
            cell8.setCellStyle(styleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalAllSLXuat);

            Cell cell9 = rowSum.createCell(8);
            cell9.setCellStyle(styleB);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            cell9.setCellValue(totalAllTienXuat);

            Cell cell10 = rowSum.createCell(9);
            cell10.setCellStyle(styleB);
            CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
            cell10.setCellValue(totalAllSLTon);

            Cell cell11 = rowSum.createCell(10);
            cell11.setCellStyle(styleB);
            CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
            cell11.setCellValue(totalAllTienTon);
            rowCout++;
            SetFooterExcel(workbook, sheet, userDTO, (11 + rowCout), 9, 0, 4, 9);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getTongHopTonKho(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "TongHopTonKho";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        String repositoryIDs = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            repositoryIDs += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<TongHopTonKhoDTO> tongHopTonKhoDTOS = reportBusinessRepository.getTongHopTonKho(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            requestReport.getUnitType(),
            repositoryIDs,
            currentBook,
            requestReport.getMaterialGoodsCategoryID(), requestReport.getDependent());
        if (tongHopTonKhoDTOS.size() == 0) {
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            tongHopTonKhoDTOS.add(new TongHopTonKhoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", tongHopTonKhoDTOS.size());
        checkEbPackage(userDTO, parameter);
        BigDecimal tongSLDauKyAll = BigDecimal.ZERO;
        BigDecimal tongTienDauKyAll = BigDecimal.ZERO;
        BigDecimal tongSLNhapAll = BigDecimal.ZERO;
        BigDecimal tongTienNhapAll = BigDecimal.ZERO;
        BigDecimal tongSLXuatAll = BigDecimal.ZERO;
        BigDecimal tongTienXuatAll = BigDecimal.ZERO;
        BigDecimal tongSLTonAll = BigDecimal.ZERO;
        BigDecimal tongTienTonAll = BigDecimal.ZERO;
        for (TongHopTonKhoDTO snk : tongHopTonKhoDTOS) {
            snk.setLoop(true);
            BigDecimal tongSLDauKy = BigDecimal.ZERO;
            BigDecimal tongTienDauKy = BigDecimal.ZERO;
            BigDecimal tongSLNhap = BigDecimal.ZERO;
            BigDecimal tongTienNhap = BigDecimal.ZERO;
            BigDecimal tongSLXuat = BigDecimal.ZERO;
            BigDecimal tongTienXuat = BigDecimal.ZERO;
            BigDecimal tongSLTon = BigDecimal.ZERO;
            BigDecimal tongTienTon = BigDecimal.ZERO;
            snk.setSlDauKy(BigDecimal.ZERO.compareTo(snk.getOpeningQuantity()) == 0 ? "" : Utils.formatTien(snk.getOpeningQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTienDauKy(BigDecimal.ZERO.compareTo(snk.getOpeningAmount()) == 0 ? "" : Utils.formatTien(snk.getOpeningAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setSlNhap(BigDecimal.ZERO.compareTo(snk.getIwQuantity()) == 0 ? "" : Utils.formatTien(snk.getIwQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setSlXuat(BigDecimal.ZERO.compareTo(snk.getOwQuantity()) == 0 ? "" : Utils.formatTien(snk.getOwQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setSlTon(BigDecimal.ZERO.compareTo(snk.getClosingQuantity()) == 0 ? "" : Utils.formatTien(snk.getClosingQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTienNhap(BigDecimal.ZERO.compareTo(snk.getIwAmount()) == 0 ? "" : Utils.formatTien(snk.getIwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setTienXuat(BigDecimal.ZERO.compareTo(snk.getOwAmount()) == 0 ? "" : Utils.formatTien(snk.getOwAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setTienTon(BigDecimal.ZERO.compareTo(snk.getClosingAmount()) == 0 ? "" : Utils.formatTien(snk.getClosingAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            for (TongHopTonKhoDTO item : tongHopTonKhoDTOS) {
                if (item.getRepositoryID() != null && snk.getRepositoryID().equals(item.getRepositoryID())) {
                    tongSLDauKy = tongSLDauKy.add(item.getOpeningQuantity());
                    tongTienDauKy = tongTienDauKy.add(item.getOpeningAmount());
                    tongSLNhap = tongSLNhap.add(item.getIwQuantity());
                    tongTienNhap = tongTienNhap.add(item.getIwAmount());
                    tongSLXuat = tongSLXuat.add(item.getOwQuantity());
                    tongTienXuat = tongTienXuat.add(item.getOwAmount());
                    tongSLTon = tongSLTon.add(item.getClosingQuantity());
                    tongTienTon = tongTienTon.add(item.getClosingAmount());
                }
            }
            snk.setTongSLDauKy(BigDecimal.ZERO.compareTo(tongSLDauKy) == 0 ? "" : Utils.formatTien(tongSLDauKy, Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTongTienDauKy(BigDecimal.ZERO.compareTo(tongTienDauKy) == 0 ? "" : Utils.formatTien(tongTienDauKy, Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setTongSLNhap(BigDecimal.ZERO.compareTo(tongSLNhap) == 0 ? "" : Utils.formatTien(tongSLNhap, Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTongSLXuat(BigDecimal.ZERO.compareTo(tongSLXuat) == 0 ? "" : Utils.formatTien(tongSLXuat, Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTongSLTon(BigDecimal.ZERO.compareTo(tongSLTon) == 0 ? "" : Utils.formatTien(tongSLTon, Constants.SystemOption.DDSo_SoLuong, userDTO));
            snk.setTongTienNhap(BigDecimal.ZERO.compareTo(tongTienNhap) == 0 ? "" : Utils.formatTien(tongTienNhap, Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setTongTienXuat(BigDecimal.ZERO.compareTo(tongTienXuat) == 0 ? "" : Utils.formatTien(tongTienXuat, Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setTongTienTon(BigDecimal.ZERO.compareTo(tongTienTon) == 0 ? "" : Utils.formatTien(tongTienTon, Constants.SystemOption.DDSo_TienVND, userDTO));
            tongSLDauKyAll = tongSLDauKyAll.add(snk.getOpeningQuantity());
            tongTienDauKyAll = tongTienDauKyAll.add(snk.getOpeningAmount());
            tongSLNhapAll = tongSLNhapAll.add(snk.getIwQuantity());
            tongTienNhapAll = tongTienNhapAll.add(snk.getIwAmount());
            tongSLXuatAll = tongSLXuatAll.add(snk.getOwQuantity());
            tongTienXuatAll = tongTienXuatAll.add(snk.getOwAmount());
            tongSLTonAll = tongSLTonAll.add(snk.getClosingQuantity());
            tongTienTonAll = tongTienTonAll.add(snk.getClosingAmount());
        }
        tongHopTonKhoDTOS.get(tongHopTonKhoDTOS.size() - 1).setLoop(false);
        parameter.put("tongSLDauKyAll", BigDecimal.ZERO.compareTo(tongSLDauKyAll) == 0 ? "" : Utils.formatTien(tongSLDauKyAll, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongTienDauKyAll", BigDecimal.ZERO.compareTo(tongTienDauKyAll) == 0 ? "" : Utils.formatTien(tongTienDauKyAll, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongSLNhapAll", BigDecimal.ZERO.compareTo(tongSLNhapAll) == 0 ? "" : Utils.formatTien(tongSLNhapAll, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongTienNhapAll", BigDecimal.ZERO.compareTo(tongTienNhapAll) == 0 ? "" : Utils.formatTien(tongTienNhapAll, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongSLXuatAll", BigDecimal.ZERO.compareTo(tongSLXuatAll) == 0 ? "" : Utils.formatTien(tongSLXuatAll, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongTienXuatAll", BigDecimal.ZERO.compareTo(tongTienXuatAll) == 0 ? "" : Utils.formatTien(tongTienXuatAll, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("tongSLTonAll", BigDecimal.ZERO.compareTo(tongSLTonAll) == 0 ? "" : Utils.formatTien(tongSLTonAll, Constants.SystemOption.DDSo_SoLuong, userDTO));
        parameter.put("tongTienTonAll", BigDecimal.ZERO.compareTo(tongTienTonAll) == 0 ? "" : Utils.formatTien(tongTienTonAll, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongHopTonKhoDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelKetQuaHDKD(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getKetQuaHDKD(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(13, sheet.getLastRowNum(), canDoiKeToanDTOS.size());
            // fill dữ liệu vào file
            int i = 12;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(7).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(8).getCell(4).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
            //
            for (BangCanDoiKeToanDTO item : canDoiKeToanDTOS) {
                Row row = sheet.createRow(i);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                cell1.setCellValue(item.getItemName());

                Cell cell2 = row.createCell(1);
                cell2.setCellValue(item.getItemCode() == null ? "" : item.getItemCode());

                Cell cell3 = row.createCell(2);
                cell3.setCellValue(item.getDescription() == null ? "" : item.getDescription());

                Cell cell4 = row.createCell(3);
                cell4.setCellValue(item.getAmount().doubleValue());

                Cell cell5 = row.createCell(4);
                cell5.setCellValue(item.getPrevAmount().doubleValue());

                if (Boolean.TRUE.equals(item.getBold())) {
                    cell1.setCellStyle(styleB);
                    cell2.setCellStyle(styleB);
                    cell3.setCellStyle(styleB);
                    cell4.setCellStyle(styleB);
                    cell5.setCellStyle(styleB);
                } else {
                    cell1.setCellStyle(style);
                    cell2.setCellStyle(style);
                    cell3.setCellStyle(style);
                    cell4.setCellStyle(style);
                    cell5.setCellStyle(style);
                }
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                i++;
            }
            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);
            CellStyle styleFooter = workbook.createCellStyle();
            styleFooter.setFont(fontI);
            Row row = sheet.createRow(15 + canDoiKeToanDTOS.size());
            Cell cell1 = row.createCell(0);
            cell1.setCellStyle(styleFooter);
            cell1.setCellValue("Ghi chú: (*) Chỉ áp dụng tại công ty cổ phần");
            SetFooterExcel(workbook, sheet, userDTO, (15 + canDoiKeToanDTOS.size()), 3, 0, 1, 3);
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getKetQuaHDKD(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "HoatDongKinhDoanh";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getKetQuaHDKD(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getDependent());
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", canDoiKeToanDTOS.size());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        checkEbPackage(userDTO, parameter);
        for (BangCanDoiKeToanDTO snk : canDoiKeToanDTOS) {
            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(canDoiKeToanDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelLuuChuyenTienTeTT(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getLuuChuyenTienTe(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getOption(), requestReport.getDependent());
        for (BangCanDoiKeToanDTO snk : canDoiKeToanDTOS) {
            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(14, sheet.getLastRowNum(), canDoiKeToanDTOS.size());
            // fill dữ liệu vào file
            int i = 13;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(8).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(9).getCell(4).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
            //
            for (BangCanDoiKeToanDTO item : canDoiKeToanDTOS) {
                Row row = sheet.createRow(i);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(item.getItemName());

                Cell cell2 = row.createCell(1);
                cell2.setCellValue(item.getItemCode() == null ? "" : item.getItemCode());

                Cell cell3 = row.createCell(2);
                cell3.setCellValue(item.getDescription() == null ? "" : item.getDescription());

                Cell cell4 = row.createCell(3);
                cell4.setCellValue(item.getAmount().doubleValue());

                Cell cell5 = row.createCell(4);
                cell5.setCellValue(item.getPrevAmount().doubleValue());

                if (Boolean.TRUE.equals(item.getBold())) {
                    cell1.setCellStyle(styleB);
                    cell2.setCellStyle(styleB);
                    cell3.setCellStyle(styleB);
                    cell4.setCellStyle(styleB);
                    cell5.setCellStyle(styleB);
                } else {
                    cell1.setCellStyle(style);
                    cell2.setCellStyle(style);
                    cell3.setCellStyle(style);
                    cell4.setCellStyle(style);
                    cell5.setCellStyle(style);
                }
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                i++;
            }
            CellStyle styleFooter = workbook.createCellStyle();
            styleFooter.setFont(fontDf);
            Row rowFooter1 = sheet.createRow(15 + canDoiKeToanDTOS.size());
            Cell cellFooter1 = rowFooter1.createCell(0);
            cellFooter1.setCellStyle(styleFooter);
            cellFooter1.setCellValue("Ghi chú: Các chỉ tiêu không có số liệu thì doanh nghiệp không phải trình bày nhưng không được đánh lại “Mã số”chỉ tiêu");
            CellUtil.setVerticalAlignment(cellFooter1, VerticalAlignment.CENTER);

            SetFooterExcel(workbook, sheet, userDTO, (18 + canDoiKeToanDTOS.size()), 3, 0, 1, 3);

            Row rowFooter2 = sheet.createRow(26 + canDoiKeToanDTOS.size());
            Cell cellFooter2 = rowFooter2.createCell(0);
            cellFooter2.setCellStyle(styleFooter);
            cellFooter2.setCellValue("- Số chứng chỉ hành nghề");

            Row rowFooter3 = sheet.createRow(27 + canDoiKeToanDTOS.size());
            Cell cellFooter3 = rowFooter3.createCell(0);
            cellFooter3.setCellStyle(styleFooter);
            cellFooter3.setCellValue("- Đơn vị cung cấp dịch vụ kế toán");

            Row rowFooter4 = sheet.createRow(28 + canDoiKeToanDTOS.size());
            Cell cellFooter4 = rowFooter4.createCell(0);
            CellStyle styleFooter4 = workbook.createCellStyle();
            styleFooter4.setFont(fontI);
            styleFooter4.setWrapText(true);
            cellFooter4.setCellStyle(styleFooter4);
            cellFooter4.setCellValue("Đối với người lập biểu là các đơn vị dịch vụ kế toán phải ghi rõ Số chứng chỉ hành nghề, tên và địa chỉ Đơn vị cung cấp dịch vụ kế toán. Người lập biểu là cá nhân ghi rõ Số chứng chỉ hành nghề.");
            CellUtil.setVerticalAlignment(cellFooter1, VerticalAlignment.CENTER);

            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getLuuChuyenTienTeTT(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "LuuChuyenTienTeTT";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<BangCanDoiKeToanDTO> canDoiKeToanDTOS = reportBusinessRepository.getLuuChuyenTienTe(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID(),
            currentBook, requestReport.getOption(), requestReport.getDependent());
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        if (Boolean.FALSE.equals(requestReport.getOption())) {
            parameter.put("isTrucTiep", false);
        } else {
            parameter.put("isTrucTiep", true);
        }
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", canDoiKeToanDTOS.size());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        checkEbPackage(userDTO, parameter);
        for (BangCanDoiKeToanDTO snk : canDoiKeToanDTOS) {
            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(canDoiKeToanDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelBaoCaoTaiChinh(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            //
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(20).getCell(0).setCellValue("1-Kỳ kế toán năm " + Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            Font fontValue = workbook.createFont();
            fontValue.setBold(true);
            fontValue.setFontName("Times New Roman");
            CellStyle cellStyleDf = workbook.createCellStyle();
            cellStyleDf.setFont(fontValue);
            cellStyleDf.setAlignment(HorizontalAlignment.CENTER);
            cellStyleDf.setVerticalAlignment(VerticalAlignment.CENTER);
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                Row rowFooterValue = sheet.getRow(904);
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                    rowFooterValue.createCell(0).setCellValue(userDTO.getFullName());
                    rowFooterValue.getCell(0).setCellStyle(cellStyleDf);
                } else {
                    rowFooterValue.createCell(0).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                    rowFooterValue.getCell(0).setCellStyle(cellStyleDf);
                }

                rowFooterValue.createCell(4).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                rowFooterValue.getCell(4).setCellStyle(cellStyleDf);

                rowFooterValue.createCell(8).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
                rowFooterValue.getCell(8).setCellStyle(cellStyleDf);
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getBaoCaoTaiChinh(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "ThuyetMinhBCTC";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<BangCanDoiTaiKhoanDTO> canDoiTaiKhoans = reportBusinessRepository.getBangCanDoiTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            true,
            requestReport.getCompanyID(),
            accountListRepository.getGradeMaxAccount(currentUserLoginAndOrg.get().getOrg()),
            currentBook, requestReport.getDependent());

        List<BangCanDoiTaiKhoanDTO> canDoiTaiKhoanDTOS = new ArrayList<>();
        canDoiTaiKhoanDTOS.add(new BangCanDoiTaiKhoanDTO());
        // khai báo key
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        String fromDate = requestReport.getFromDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String toDate = requestReport.getToDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        parameter.put("fromDate", fromDate);
        parameter.put("toDate", toDate);
        parameter.put("REPORT_MAX_COUNT", canDoiTaiKhoanDTOS.size());
        parameter.put("DonViTinh", organizationUnitRepository.findById(requestReport.getCompanyID()).get().getCurrencyID());
        checkEbPackage(userDTO, parameter);
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(canDoiTaiKhoanDTOS, parameter, jasperReport);
        return result;
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Hautv
     */
    byte[] getSoCaiHTNhatKyChung(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoCai_NhatKyChung";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoCaiHTNhatKyChungDTO> soCaiHTNhatKyChungDTOS = reportBusinessRepository.getSoCaiHTNhatKyChung(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getGroupTheSameItem(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            0,
            userDTO,
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_MAX_COUNT", soCaiHTNhatKyChungDTOS.size());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        if (soCaiHTNhatKyChungDTOS.size() > 0) {
            parameter.put("FirstAccountNumber", soCaiHTNhatKyChungDTOS.get(0).getAccountNumber());
        }else{
            soCaiHTNhatKyChungDTOS.add( new SoCaiHTNhatKyChungDTO());
            soCaiHTNhatKyChungDTOS.add( new SoCaiHTNhatKyChungDTO());
            soCaiHTNhatKyChungDTOS.add( new SoCaiHTNhatKyChungDTO());
            soCaiHTNhatKyChungDTOS.get(0).setAccountNumber("111");
            soCaiHTNhatKyChungDTOS.get(1).setAccountNumber("111");
            soCaiHTNhatKyChungDTOS.get(2).setAccountNumber("111");
            soCaiHTNhatKyChungDTOS.get(0).setHeaderDetail(false);
            soCaiHTNhatKyChungDTOS.get(1).setHeaderDetail(false);
            soCaiHTNhatKyChungDTOS.get(2).setHeaderDetail(false);
        }
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        List<Type> types = typeRepository.findAllByIsActive();
        for (SoCaiHTNhatKyChungDTO snk : soCaiHTNhatKyChungDTOS) {
            if (snk.getDebitAmount() == null || BigDecimal.ZERO.compareTo(snk.getDebitAmount()) == 0) {
                snk.setDebitAmountString("");
            } else {
                snk.setDebitAmountString(Utils.formatTien(snk.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getCreditAmount() == null || BigDecimal.ZERO.compareTo(snk.getCreditAmount()) == 0) {
                snk.setCreditAmountString("");
            } else {
                snk.setCreditAmountString(Utils.formatTien(snk.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getOpenningDebitAmount() == null || BigDecimal.ZERO.compareTo(snk.getOpenningDebitAmount()) == 0) {
                snk.setOpenningDebitAmountString("");
            } else {
                snk.setOpenningDebitAmountString(Utils.formatTien(snk.getOpenningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getOpenningCreditAmount() == null || BigDecimal.ZERO.compareTo(snk.getOpenningCreditAmount()) == 0) {
                snk.setOpenningCreditAmountString("");
            } else {
                snk.setOpenningCreditAmountString(Utils.formatTien(snk.getOpenningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getClosingDebitAmount() == null || BigDecimal.ZERO.compareTo(snk.getClosingDebitAmount()) == 0) {
                snk.setClosingDebitAmountString("");
            } else {
                snk.setClosingDebitAmountString(Utils.formatTien(snk.getClosingDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getClosingCreditAmount() == null || BigDecimal.ZERO.compareTo(snk.getClosingCreditAmount()) == 0) {
                snk.setClosingCreditAmountString("");
            } else {
                snk.setClosingCreditAmountString(Utils.formatTien(snk.getClosingCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getAccumDebitAmount() == null || BigDecimal.ZERO.compareTo(snk.getAccumDebitAmount()) == 0) {
                snk.setAccumDebitAmountString("");
            } else {
                snk.setAccumDebitAmountString(Utils.formatTien(snk.getAccumDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }
            if (snk.getAccumCreditAmount() == null || BigDecimal.ZERO.compareTo(snk.getAccumCreditAmount()) == 0) {
                snk.setAccumCreditAmountString("");
            } else {
                snk.setAccumCreditAmountString(Utils.formatTien(snk.getAccumCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
            }

            snk.setDateString(convertDate(snk.getDate()));
            snk.setPostedDateString(convertDate(snk.getPostedDate()));
            if (snk.getDebitAmount() != null) {
                totalDebitAmount = totalDebitAmount.add(snk.getDebitAmount());
            }
            if (snk.getCreditAmount() != null) {
                totalCreditAmount = totalCreditAmount.add(snk.getCreditAmount());
            }
            snk.setLinkRef(getRefLink(snk.getRefType(), snk.getReferenceID(), types, null, null, null, null));
        }
        parameter.put("totalDebitAmount", Utils.formatTien(totalDebitAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCreditAmount", Utils.formatTien(totalCreditAmount, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soCaiHTNhatKyChungDTOS, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoCaiHTNhatKyChung(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<SoCaiHTNhatKyChungDTO> soCaiHTNhatKyChungDTOS = reportBusinessRepository.getSoCaiHTNhatKyChung(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getGroupTheSameItem(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            0,
            userDTO,
            requestReport.getDependent());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);

            CellStyle styleDf = workbook.createCellStyle();
            styleDf.setFont(fontDf);
//            styleDf.setWrapText(true);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);

            CellStyle cellStyleGroup = workbook.createCellStyle();
            cellStyleGroup.setFont(fontB);

            List<List<SoCaiHTNhatKyChungDTO>> lstSoCaiHTNhatKyChungDTO = new ArrayList<>();
            List<SoCaiHTNhatKyChungDTO> child = new ArrayList<>();
            for (int i = 0; i < soCaiHTNhatKyChungDTOS.size(); i++) {
                child.add(soCaiHTNhatKyChungDTOS.get(i));
                if (Boolean.TRUE.equals(soCaiHTNhatKyChungDTOS.get(i).isBreakPage())) {
                    lstSoCaiHTNhatKyChungDTO.add(child);
                    child = new ArrayList<>();
                }
            }
            for (int i = 1; i < lstSoCaiHTNhatKyChungDTO.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int j = 0; j < lstSoCaiHTNhatKyChungDTO.size(); j++) {
                Sheet sheet = workbook.getSheetAt(j);
                List<SoCaiHTNhatKyChungDTO> lstSoCai = lstSoCaiHTNhatKyChungDTO.get(j);
                List<SoCaiHTNhatKyChungDTO> lstSoCai1 = lstSoCai.subList(1, lstSoCai.size());
                if (lstSoCai1.size() > 0) {
                    sheet.shiftRows(14, sheet.getLastRowNum(), lstSoCai1.size());
                }
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + lstSoCai.get(0).getAccountNumber() + "-" + lstSoCai.get(0).getAccountName());
                sheet.getRow(7).getCell(0).setCellStyle(cellStyleGroup);
//            sheet.getRow(8).getCell(7).setCellValue("Đơn vị tính: " + userDTO.getOrganizationUnit().getCurrencyID());
                //
                // fill dữ liệu vào file
                int i = 13;
                BigDecimal totalDebitAmount = BigDecimal.ZERO;
                BigDecimal totalCreditAmount = BigDecimal.ZERO;
                for (SoCaiHTNhatKyChungDTO item : lstSoCai1) {
                    Row row = sheet.createRow(i);
                    /*if (Boolean.TRUE.equals(requestReport.getShowAccumAmount())) {
                        if (i == 10) {
                            style.setFont(fontB);
                        } else {
                            style.setFont(fontDf);
                        }
                    }*/
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(Utils.getValueString(Utils.convertDate(item.getPostedDate())));

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(Utils.getValueString(item.getNo()));

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                    cell3.setCellValue(Utils.getValueString(Utils.convertDate(item.getDate())));

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(Utils.getValueString(item.getJournalMemo()));

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.CENTER);
                    cell7.setCellValue(Utils.getValueString(item.getCorrespondingAccountNumber()));

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    Utils.setValueCellDouble(cell8, item.getDebitAmount());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    Utils.setValueCellDouble(cell9, item.getCreditAmount());

                    i++;
                    if (item.getDebitAmount() != null) {
                        totalDebitAmount = totalDebitAmount.add(item.getDebitAmount());
                    }
                    if (item.getCreditAmount() != null) {
                        totalCreditAmount = totalCreditAmount.add(item.getCreditAmount());
                    }
                }

                Utils.setValueCellDouble(sheet.getRow(11).getCell(7), lstSoCai.get(0).getOpenningDebitAmount());
                sheet.getRow(11).getCell(7).setCellStyle(styleB);
                Utils.setValueCellDouble(sheet.getRow(11).getCell(8), lstSoCai.get(0).getOpenningCreditAmount());
                sheet.getRow(11).getCell(8).setCellStyle(styleB);

                Utils.setValueCellDouble(sheet.getRow(12).getCell(7), lstSoCai.get(0).getAccumDebitAmount());
                sheet.getRow(12).getCell(7).setCellStyle(styleB);
                Utils.setValueCellDouble(sheet.getRow(12).getCell(8), lstSoCai.get(0).getAccumCreditAmount());
                sheet.getRow(12).getCell(8).setCellStyle(styleB);

                Row rowCSPS = sheet.createRow(13 + lstSoCai1.size());
                Cell cell0_CSPS = rowCSPS.createCell(0);
                cell0_CSPS.setCellStyle(styleB);
                Cell cell1_CSPS = rowCSPS.createCell(1);
                cell1_CSPS.setCellStyle(styleB);
                Cell cell2_CSPS = rowCSPS.createCell(2);
                cell2_CSPS.setCellStyle(styleB);

                Cell cell3_CSPS = rowCSPS.createCell(3);
                cell3_CSPS.setCellStyle(styleB);
                CellUtil.setAlignment(cell3_CSPS, HorizontalAlignment.LEFT);
                cell3_CSPS.setCellValue(" - Cộng số phát sinh");

                Cell cell4_CSPS = rowCSPS.createCell(4);
                cell4_CSPS.setCellStyle(styleB);
                Cell cell5_CSPS = rowCSPS.createCell(5);
                cell5_CSPS.setCellStyle(styleB);
                Cell cell6_CSPS = rowCSPS.createCell(6);
                cell6_CSPS.setCellStyle(styleB);

                Cell cell7_CSPS = rowCSPS.createCell(7);
                cell7_CSPS.setCellStyle(styleB);
                CellUtil.setAlignment(cell7_CSPS, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell7_CSPS, lstSoCai.get(0).getCongSoPhatSinhNoAm());

                Cell cell8_CSPS = rowCSPS.createCell(8);
                cell8_CSPS.setCellStyle(styleB);
                CellUtil.setAlignment(cell8_CSPS, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell8_CSPS, lstSoCai.get(0).getCongSoPhatSinhCoAm());

                Row rowSDCK = sheet.createRow(14 + lstSoCai1.size());
                Cell cell0_SDCK = rowSDCK.createCell(0);
                cell0_SDCK.setCellStyle(styleB);
                Cell cell1_SDCK = rowSDCK.createCell(1);
                cell1_SDCK.setCellStyle(styleB);
                Cell cell2_SDCK = rowSDCK.createCell(2);
                cell2_SDCK.setCellStyle(styleB);

                Cell cell3_SDCK = rowSDCK.createCell(3);
                cell3_SDCK.setCellStyle(styleB);
                CellUtil.setAlignment(cell3_SDCK, HorizontalAlignment.LEFT);
                cell3_SDCK.setCellValue(" - Số dư cuối kỳ");

                Cell cell4_SDCK = rowSDCK.createCell(4);
                cell4_SDCK.setCellStyle(styleB);
                Cell cell5_SDCK = rowSDCK.createCell(5);
                cell5_SDCK.setCellStyle(styleB);
                Cell cell6_SDCK = rowSDCK.createCell(6);
                cell6_SDCK.setCellStyle(styleB);

                Cell cell7_SDCK = rowSDCK.createCell(7);
                cell7_SDCK.setCellStyle(styleB);
                CellUtil.setAlignment(cell7_SDCK, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell7_SDCK, lstSoCai.get(0).getSoDuCuoiKyNoAm());

                Cell cell8_SDCK = rowSDCK.createCell(8);
                cell8_SDCK.setCellStyle(styleB);
                CellUtil.setAlignment(cell8_SDCK, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell8_SDCK, lstSoCai.get(0).getSoDuCuoiKyCoAm());

                Row rowCLK = sheet.createRow(15 + lstSoCai1.size());

                Cell cell0_CLK = rowCLK.createCell(0);
                cell0_CLK.setCellStyle(styleB);
                Cell cell1_CLK = rowCLK.createCell(1);
                cell1_CLK.setCellStyle(styleB);
                Cell cell2_CLK = rowCLK.createCell(2);
                cell2_CLK.setCellStyle(styleB);

                Cell cell3_CLK = rowCLK.createCell(3);
                cell3_CLK.setCellStyle(styleB);
                CellUtil.setAlignment(cell3_CLK, HorizontalAlignment.LEFT);
                cell3_CLK.setCellValue(" - Cộng lũy kế");

                Cell cell4_CLK = rowCLK.createCell(4);
                cell4_CLK.setCellStyle(styleB);
                Cell cell5_CLK = rowCLK.createCell(5);
                cell5_CLK.setCellStyle(styleB);
                Cell cell6_CLK = rowCLK.createCell(6);
                cell6_CLK.setCellStyle(styleB);

                Cell cell7_CLK = rowCLK.createCell(7);
                cell7_CLK.setCellStyle(styleB);
                CellUtil.setAlignment(cell7_CLK, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell7_CLK, lstSoCai.get(0).getCongLuyKeNoAm());

                Cell cell8_CLK = rowCLK.createCell(8);
                cell8_CLK.setCellStyle(styleB);
                CellUtil.setAlignment(cell8_CLK, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell8_CLK, lstSoCai.get(0).getCongLuyKeCoAm());

                sheet.createRow(17 + lstSoCai1.size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
                sheet.createRow(18 + lstSoCai1.size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
                sheet.getRow(17 + lstSoCai1.size()).getCell(0).setCellStyle(styleDf);
                sheet.getRow(18 + lstSoCai1.size()).getCell(0).setCellStyle(styleDf);
                SetFooterExcel(workbook, sheet, userDTO, (19 + lstSoCai1.size()), 7, 0, 3, 7);
                sheet.getRow(20 + lstSoCai1.size()).getCell(0).setCellValue("Người ghi sổ");
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelSoChiTietCacTaiKhoan(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<SoChiTietTaiKhoanDTO> soChiTietTaiKhoanDTOS = reportBusinessRepository.getSoChiTietTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getGroupTheSameItem(),
            requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            0,
            requestReport.getDependent(),
            requestReport.getGetAmountOriginal()
        );
        if (soChiTietTaiKhoanDTOS.size() == 0) {
            for (int i = 0; i <= 2; i++) {
                SoChiTietTaiKhoanDTO soChiTietTaiKhoanDTO = new SoChiTietTaiKhoanDTO();
                soChiTietTaiKhoanDTO.setJournalMemo("");
                soChiTietTaiKhoanDTO.setClosingCreditAmountOriginalToString("");
                soChiTietTaiKhoanDTO.setClosingDebitAmountOriginalToString("");
                soChiTietTaiKhoanDTO.setAccountNumber("");
                soChiTietTaiKhoanDTO.setClosingCreditAmountToString("");
                soChiTietTaiKhoanDTO.setClosingDebitAmountToString("");
                soChiTietTaiKhoanDTO.setDebitAmountOriginalToString("");
                soChiTietTaiKhoanDTOS.add(soChiTietTaiKhoanDTO);
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (soChiTietTaiKhoanDTOS.size() > 0) {
                sheet.shiftRows(10, sheet.getLastRowNum(), soChiTietTaiKhoanDTOS.size());
            }
            // fill dữ liệu vào file
            int i = 10;

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);

            CellStyle styleDf = workbook.createCellStyle();
            styleDf.setFont(fontDf);
            styleDf.setWrapText(true);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);

            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(7).getCell(0).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
            //
            for (SoChiTietTaiKhoanDTO item : soChiTietTaiKhoanDTOS) {
                Row row = sheet.createRow(i);
                if (Boolean.TRUE.equals(item.getBold())) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(Utils.getValueString(Utils.convertDate(item.getDate())));

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(Utils.getValueString(Utils.convertDate(item.getPostedDate())));

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(Utils.getValueString(item.getNo()));

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(Utils.getValueString(item.getJournalMemo()));

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                cell5.setCellValue(Utils.getValueString(item.getAccountNumber()));

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                cell6.setCellValue(Utils.getValueString(item.getAccountCorresponding()));

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell7, item.getDebitAmount());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell8, item.getCreditAmount());

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell9, item.getClosingDebitAmount());

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                Utils.setValueCellDouble(cell10, item.getClosingCreditAmount());

                i++;
            }

            sheet.createRow(11 + soChiTietTaiKhoanDTOS.size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
            sheet.createRow(12 + soChiTietTaiKhoanDTOS.size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
            sheet.getRow(11 + soChiTietTaiKhoanDTOS.size()).getCell(0).setCellStyle(styleDf);
            sheet.getRow(12 + soChiTietTaiKhoanDTOS.size()).getCell(0).setCellStyle(styleDf);

            SetFooterExcel(workbook, sheet, userDTO, (14 + soChiTietTaiKhoanDTOS.size()), 8, 0, 4, 8);
            sheet.getRow(15 + soChiTietTaiKhoanDTOS.size()).getCell(0).setCellValue("Người ghi sổ");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    private String getDataFromListString(List<String> integers) {
        return String.join(",", Lists.transform(integers, Functions.toStringFunction()));
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getSoQuyTienMat(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "So_Quy_Tien_Mat";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoQuyTienMatDTO> soQuyTienMatDTOS = reportBusinessRepository.getSoQuyTienMat(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            phienSoLamViec,
            requestReport.getDependent(),
            requestReport.getTypeShowCurrency());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = "";
        header4 = "Loại tiền: " +
            requestReport.getCurrencyID() + "; " + Period;
        parameter.put("header4", header4);
        BigDecimal tongThu = BigDecimal.ZERO;
        BigDecimal tongChi = BigDecimal.ZERO;
        BigDecimal tongTon = BigDecimal.ZERO;
        List<Type> types = typeRepository.findAllByIsActive();
        String mainCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        if (soQuyTienMatDTOS.size() > 0) {
            for (int i = 1; i < soQuyTienMatDTOS.size(); i++) {
                soQuyTienMatDTOS.get(i).setDateToString(convertDate(soQuyTienMatDTOS.get(i).getDate()));
                soQuyTienMatDTOS.get(i).setPostedDateToString(convertDate(soQuyTienMatDTOS.get(i).getPostedDate()));
                soQuyTienMatDTOS.get(i).setTotalPaymentFBCurrencyIDToString(Utils.formatTien(soQuyTienMatDTOS.get(i).getTotalPaymentFBCurrencyID(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                soQuyTienMatDTOS.get(i).setTotalReceiptFBCurrencyIDToString(Utils.formatTien(soQuyTienMatDTOS.get(i).getTotalReceiptFBCurrencyID(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                soQuyTienMatDTOS.get(i).setClosingFBCurrencyIDToString(Utils.formatTien(soQuyTienMatDTOS.get(i).getClosingFBCurrencyID(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                if (soQuyTienMatDTOS.get(i).getTotalReceiptFBCurrencyID() != null) {
                    tongThu = tongThu.add(soQuyTienMatDTOS.get(i).getTotalReceiptFBCurrencyID());
                } else {
                    tongChi = tongChi.add(soQuyTienMatDTOS.get(i).getTotalPaymentFBCurrencyID());
                }
                soQuyTienMatDTOS.get(i).setLinkRef(getRefLink(soQuyTienMatDTOS.get(i).getTypeID(), soQuyTienMatDTOS.get(i).getRefID(), types, null, null, null, null));
            }
            soQuyTienMatDTOS.get(0).setClosingFBCurrencyIDToString(Utils.formatTien(soQuyTienMatDTOS.get(0).getClosingFBCurrencyID(), Constants.SystemOption.DDSo_TienVND, userDTO));
            tongTon = soQuyTienMatDTOS.get(soQuyTienMatDTOS.size() - 1).getClosingFBCurrencyID();
            parameter.put("TongThu", Utils.formatTien(tongThu, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongChi", Utils.formatTien(tongChi, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongTon", Utils.formatTien(tongTon, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
        } else {
            if (soQuyTienMatDTOS.size() == 0) {
                soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
                soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
                soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
            }
        }
        parameter.put("REPORT_MAX_COUNT", soQuyTienMatDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soQuyTienMatDTOS, parameter, jasperReport);
        return result;
    }


    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getSoKeToanChiTietQuyTienMat(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "So_Ke_Toan_Chi_Tiet_Quy_Tien_Mat";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoKeToanChiTietQuyTienMatDTO> soKeToanChiTietQuyTienMatDTOS = reportBusinessRepository.getSoKeToanChiTietQuyTienMat(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            phienSoLamViec,
            requestReport.getAccountList(),
            requestReport.getGroupTheSameItem(),
            requestReport.getDependent(),
            requestReport.getTypeShowCurrency());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String header4 = "";
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        List<Type> types = typeRepository.findAllByIsActive();
        header4 = "Tài khoản: ";
        for (int i = 0; i < requestReport.getAccountList().size(); i++) {
            if (i == 0) {
                header4 += requestReport.getAccountList().get(i);
            } else {
                header4 += "," + requestReport.getAccountList().get(i);
            }
        }
        header4 += "; " + "Loại tiền: " +
            requestReport.getCurrencyID() + "; " + Period;
        parameter.put("header4", header4);
//        parameter.put("CurrencyType", "Đơn vị tính: " + requestReport.getCurrencyID());
        BigDecimal tongThu = BigDecimal.ZERO;
        BigDecimal tongChi = BigDecimal.ZERO;
        BigDecimal tongTon = BigDecimal.ZERO;
        if (soKeToanChiTietQuyTienMatDTOS.size() <= 1) {
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
            soKeToanChiTietQuyTienMatDTOS.add(new SoKeToanChiTietQuyTienMatDTO(null, null, null, null, null, null, null, null, null, null, null, null, 1, null));
        }
        String mainCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        for (int i = 0; i < soKeToanChiTietQuyTienMatDTOS.size(); i++) {
            soKeToanChiTietQuyTienMatDTOS.get(i).setDateToString(convertDate(soKeToanChiTietQuyTienMatDTOS.get(i).getDate()));
            soKeToanChiTietQuyTienMatDTOS.get(i).setPostedDateToString(convertDate(soKeToanChiTietQuyTienMatDTOS.get(i).getPostedDate()));
            soKeToanChiTietQuyTienMatDTOS.get(i).setPhatSinhNoToString(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhNo() != null ? Utils.formatTien(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhNo(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
            soKeToanChiTietQuyTienMatDTOS.get(i).setPhatSinhCoToString(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhCo() != null ? Utils.formatTien(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhCo(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
            soKeToanChiTietQuyTienMatDTOS.get(i).setSoTonToString(soKeToanChiTietQuyTienMatDTOS.get(i).getSoTon() != null ? Utils.formatTien(soKeToanChiTietQuyTienMatDTOS.get(i).getSoTon(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
            if (soKeToanChiTietQuyTienMatDTOS.get(i).getPositionOrder() == 2) {
                tongThu = tongThu.add(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhNo() != null ? soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhNo() : BigDecimal.ZERO);
                tongChi = tongChi.add(soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhCo() != null ? soKeToanChiTietQuyTienMatDTOS.get(i).getPhatSinhCo() : BigDecimal.ZERO);
                tongTon = tongTon.add(soKeToanChiTietQuyTienMatDTOS.get(i).getSoTon() != null ? soKeToanChiTietQuyTienMatDTOS.get(i).getSoTon() : BigDecimal.ZERO);
            }
            soKeToanChiTietQuyTienMatDTOS.get(i).setLinkRef(getRefLink(soKeToanChiTietQuyTienMatDTOS.get(i).getTypeID(), soKeToanChiTietQuyTienMatDTOS.get(i).getRefID(), types, null, null, null, null));
        }
        parameter.put("tongThu", !tongThu.equals(BigDecimal.ZERO) ? Utils.formatTien(tongThu, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
        parameter.put("tongChi", !tongChi.equals(BigDecimal.ZERO) ? Utils.formatTien(tongChi, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
        parameter.put("tongTon", !tongTon.equals(BigDecimal.ZERO) ? Utils.formatTien(tongTon, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO) : "");
        parameter.put("REPORT_MAX_COUNT", soKeToanChiTietQuyTienMatDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soKeToanChiTietQuyTienMatDTOS, parameter, jasperReport);
        return result;
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getBangKeSoDuNganHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "Bang_Ke_So_Du_Ngan_Hang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String listAccountNumber = "";
        List<AccountList> accountLists = accountListRepository.getAccountStartWith112Except112(currentUserLoginAndOrg.get().getOrg());
        if (requestReport.getAccountNumber().equals("112") && accountLists.size() > 0) {
            for (int i = 0; i < accountLists.size(); i++) {
                listAccountNumber += "," + accountLists.get(i).getAccountNumber();
            }
        } else {
            listAccountNumber += "," + requestReport.getAccountNumber();
        }
        listAccountNumber += ",";
        List<BangKeSoDuNganHangDTO> bangKeSoDuNganHangDTOS = reportBusinessRepository.getBangKeSoDuNganHang(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), requestReport.getCurrencyID(), phienSoLamViec, listAccountNumber, requestReport.getDependent(), requestReport.getTypeShowCurrency());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = "Tài khoản: " + requestReport.getAccountNumber() + "; Loại tiền: " + requestReport.getCurrencyID() + "; " + Period;
        parameter.put("header4", header4);
        BigDecimal TongSoDuDauKy = BigDecimal.ZERO;
        BigDecimal TongPhatSinhNo = BigDecimal.ZERO;
        BigDecimal TongPhatSinhCo = BigDecimal.ZERO;
        BigDecimal TongSoDuCuoiKy = BigDecimal.ZERO;
        String mainCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        if (bangKeSoDuNganHangDTOS.size() > 0) {
            for (int i = 0; i < bangKeSoDuNganHangDTOS.size(); i++) {
                bangKeSoDuNganHangDTOS.get(i).setSoDuDauKyToString(Utils.formatTien(bangKeSoDuNganHangDTOS.get(i).getSoDuDauKy(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                bangKeSoDuNganHangDTOS.get(i).setPhatSinhNoToString(Utils.formatTien(bangKeSoDuNganHangDTOS.get(i).getPhatSinhNo(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                bangKeSoDuNganHangDTOS.get(i).setPhatSinhCoToString(Utils.formatTien(bangKeSoDuNganHangDTOS.get(i).getPhatSinhCo(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                bangKeSoDuNganHangDTOS.get(i).setSoDuCuoiKyToString(Utils.formatTien(bangKeSoDuNganHangDTOS.get(i).getSoDuCuoiKy(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                TongSoDuDauKy = TongSoDuDauKy.add(bangKeSoDuNganHangDTOS.get(i).getSoDuDauKy() != null ? bangKeSoDuNganHangDTOS.get(i).getSoDuDauKy() : BigDecimal.ZERO);
                TongPhatSinhNo = TongPhatSinhNo.add(bangKeSoDuNganHangDTOS.get(i).getPhatSinhNo() != null ? bangKeSoDuNganHangDTOS.get(i).getPhatSinhNo() : BigDecimal.ZERO);
                TongPhatSinhCo = TongPhatSinhCo.add(bangKeSoDuNganHangDTOS.get(i).getPhatSinhCo() != null ? bangKeSoDuNganHangDTOS.get(i).getPhatSinhCo() : BigDecimal.ZERO);
                TongSoDuCuoiKy = TongSoDuCuoiKy.add(bangKeSoDuNganHangDTOS.get(i).getSoDuCuoiKy() != null ? bangKeSoDuNganHangDTOS.get(i).getSoDuCuoiKy() : BigDecimal.ZERO);
            }
            parameter.put("TongSoDuDauKy", Utils.formatTien(TongSoDuDauKy, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongPhatSinhNo", Utils.formatTien(TongPhatSinhNo, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongPhatSinhCo", Utils.formatTien(TongPhatSinhCo, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongSoDuCuoiKy", Utils.formatTien(TongSoDuCuoiKy, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
        } else {
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(0, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(0, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(0, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        parameter.put("REPORT_MAX_COUNT", bangKeSoDuNganHangDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(bangKeSoDuNganHangDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelBangKeSoDuNganHang(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String listAccountNumber = "";
        List<AccountList> accountLists = accountListRepository.getAccountStartWith112Except112(currentUserLoginAndOrg.get().getOrg());
        if (requestReport.getAccountNumber().equals("112") && accountLists.size() > 0) {
            for (int i = 0; i < accountLists.size(); i++) {
                listAccountNumber += "," + accountLists.get(i).getAccountNumber();
            }
        } else {
            listAccountNumber += "," + requestReport.getAccountNumber();
        }
        listAccountNumber += ",";
        List<BangKeSoDuNganHangDTO> bangKeSoDuNganHangDTOS = reportBusinessRepository.getBangKeSoDuNganHang(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), requestReport.getCurrencyID(), phienSoLamViec, listAccountNumber, requestReport.getDependent(), requestReport.getTypeShowCurrency());
        if(bangKeSoDuNganHangDTOS.size()==0)
        {
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(1, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(2, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            bangKeSoDuNganHangDTOS.add(new BangKeSoDuNganHangDTO(3, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (bangKeSoDuNganHangDTOS.size() > 0) {
                sheet.shiftRows(9, sheet.getLastRowNum(), bangKeSoDuNganHangDTOS.size());
            }

            // fill dữ liệu vào file
            int i = 9;

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 2, 3, 0);
            String tklt = "Tài khoản: "+requestReport.getAccountNumber()+";"+"Loại tiền: "+requestReport.getCurrencyID()+";";
            sheet.getRow(6).getCell(0).setCellValue(tklt+Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));

            BigDecimal totalSoDuDauKy = BigDecimal.ZERO;
            BigDecimal totalPhatSinhNo = BigDecimal.ZERO;
            BigDecimal totalPhatSinhCo = BigDecimal.ZERO;
            BigDecimal totalSoDuCuoiKy = BigDecimal.ZERO;
            for (BangKeSoDuNganHangDTO item : bangKeSoDuNganHangDTOS) {
                Row row = sheet.createRow(i);
                if (Boolean.TRUE.equals(requestReport.getShowAccumAmount())) {
                    if (i == 9) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                }

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getRowNum());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                cell2.setCellValue(item.getTaiKhoanNganHang());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(item.getTenNganHang());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(item.getChiNhanh());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                if (item.getSoDuDauKy() != null)
                    cell5.setCellValue(item.getSoDuDauKy().doubleValue());
                else
                    cell5.setCellValue(0);

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                if (item.getPhatSinhNo() != null)
                        cell6.setCellValue(item.getPhatSinhNo().doubleValue());
                else
                    cell6.setCellValue(0);

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                if (item.getPhatSinhCo() != null)
                    cell7.setCellValue(item.getPhatSinhCo().doubleValue());
                else
                    cell7.setCellValue(0);

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                if (item.getSoDuCuoiKy() != null)
                    cell8.setCellValue(item.getSoDuCuoiKy().doubleValue());
                else
                    cell8.setCellValue(0);

                i++;
                if (item.getSoDuDauKy() != null) {
                    totalSoDuDauKy = totalSoDuDauKy.add(item.getSoDuDauKy());
                }
                if (item.getPhatSinhNo() != null) {
                    totalPhatSinhNo = totalPhatSinhNo.add(item.getPhatSinhNo());
                }
                if (item.getPhatSinhCo() != null) {
                    totalPhatSinhCo = totalPhatSinhCo.add(item.getPhatSinhCo());
                }
                if (item.getSoDuCuoiKy() != null) {
                    totalSoDuCuoiKy = totalSoDuCuoiKy.add(item.getSoDuCuoiKy());
                }
            }

            Row rowSum = sheet.createRow(9 + bangKeSoDuNganHangDTOS.size());

            rowSum.createCell(0);
            rowSum.getCell(0).setCellStyle(styleB);
            rowSum.getCell(0).setCellValue("Tổng Cộng");

            rowSum.createCell(1);
            rowSum.getCell(1).setCellStyle(styleB);

            rowSum.createCell(2);
            rowSum.getCell(2).setCellStyle(styleB);

            rowSum.createCell(3);
            rowSum.getCell(3).setCellStyle(styleB);

            Cell cell1 = rowSum.createCell(4);
            cell1.setCellStyle(styleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            if (totalSoDuDauKy != null)
                cell1.setCellValue(totalSoDuDauKy.doubleValue());
            else
                cell1.setCellValue(0);

            Cell cell2 = rowSum.createCell(5);
            cell2.setCellStyle(styleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            if (totalPhatSinhNo != null)
                cell2.setCellValue(totalPhatSinhNo.doubleValue());
            else
                cell2.setCellValue(0);

            Cell cell3 = rowSum.createCell(6);
            cell3.setCellStyle(styleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            if (totalPhatSinhCo != null)
                cell3.setCellValue(totalPhatSinhCo.doubleValue());
            else
                cell3.setCellValue(0);

            Cell cell4 = rowSum.createCell(7);
            cell4.setCellStyle(styleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            if (totalSoDuCuoiKy != null)
                cell4.setCellValue(totalSoDuCuoiKy.doubleValue());
            else
                cell4.setCellValue(0);

            sheet.createRow(11 + bangKeSoDuNganHangDTOS.size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
            sheet.createRow(12 + bangKeSoDuNganHangDTOS.size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");

            SetFooterExcel(workbook, sheet, userDTO, (14 + bangKeSoDuNganHangDTOS.size()), 7, 0, 4, 7);
            sheet.getRow(15 + bangKeSoDuNganHangDTOS.size()).getCell(0).setCellValue("Người ghi sổ");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getSoTienGuiNganHang(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "So_Tien_Gui_Ngan_Hang";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<SystemOption> systemOptions = systemOptionRepository.findAllSystemOptions(currentUserLoginAndOrg.get().getOrg());
        String data = systemOptions.stream().filter(a -> a.getCode().equals(TCKHAC_SDDMTKNH)).findFirst().get().getData();
        JasperReport jasperReport;
        List<ComboboxBankAccountDetailDTO> bankAccountDetailsList = new ArrayList<>();
        List<UUID> listCompanyIDTKNH = systemOptionRepository.getAllCompanyByCompanyIdAndCode(requestReport.getCompanyID(), TCKHAC_SDDMTKNH);
        List<UUID> listCompanyIDTTD = systemOptionRepository.getAllCompanyByCompanyIdAndCode(requestReport.getCompanyID(), TCKHAC_SDDMTheTD);
        if (data.equals("1") && Boolean.TRUE.equals(requestReport.getDependent())) {
            bankAccountDetailsList = bankAccountDetailsRepository.findAllForAccType(currentUserLoginAndOrg.get().getOrg(),currentUserLoginAndOrg.get().getOrgGetData());
        } else {
            bankAccountDetailsList = bankAccountDetailsRepository.findAllByIsActive(listCompanyIDTKNH, listCompanyIDTTD,currentUserLoginAndOrg.get().getOrgGetData());
        }
        String listBankAccountDetail = ",";
        String header4 = "";
        if (requestReport.getBankAccountDetailID() != null) {
            listBankAccountDetail += Utils.uuidConvertToGUID(requestReport.getBankAccountDetailID()) + ",";
            Optional<ComboboxBankAccountDetailDTO> bankAccountDetails = bankAccountDetailsList.stream().filter(a -> a.getId().equals(requestReport.getBankAccountDetailID())).findFirst();
            header4 += "TK ngân hàng: " + bankAccountDetails.get().getBankAccount();
        } else {
            if (bankAccountDetailsList.size() > 0) {
                for (int i = 0; i < bankAccountDetailsList.size(); i++) {
                    UUID tmp = Utils.uuidConvertToGUID(bankAccountDetailsList.get(i).getId());
                    listBankAccountDetail += tmp + ",";
                }
            }
            header4 += "TK ngân hàng: Tất cả; ";
        }
        checkEbPackage(userDTO, parameter);
        String listAccountNumber = "";
        List<AccountList> accountLists = accountListRepository.getAccountStartWith112Except112(currentUserLoginAndOrg.get().getOrgGetData());
        if (requestReport.getAccountNumber().equals("112") && accountLists.size() > 0) {
            for (int i = 0; i < accountLists.size(); i++) {
                listAccountNumber += "," + accountLists.get(i).getAccountNumber();
            }
        } else {
            listAccountNumber += "," + requestReport.getAccountNumber();
        }
        listAccountNumber += ",";
        List<SoTienGuiNganHangDTO> soTienGuiNganHangDTOS = reportBusinessRepository.getSoTienGuiNganHang(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), listAccountNumber, requestReport.getCurrencyID(), listBankAccountDetail, requestReport.getGroupTheSameItem(), phienSoLamViec, requestReport.getDependent(), requestReport.getTypeShowCurrency());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        String CurrencyType = "";
        if (requestReport.getCurrencyID() != null) {
            CurrencyType = "Loại tiền: " + requestReport.getCurrencyID() + "; ";
        }
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        header4 += "Tài khoản: " + requestReport.getAccountNumber() + ";";
        header4 += CurrencyType;
        header4 += Period;
        parameter.put("header4", header4);
        String mainCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        List<Type> types = typeRepository.findAllByIsActive();
        if (soTienGuiNganHangDTOS.size() > 0) {
            BigDecimal TongThu = BigDecimal.ZERO;
            BigDecimal TongChi = BigDecimal.ZERO;
            BigDecimal TongTon = BigDecimal.ZERO;
            for (int i = 0; i < soTienGuiNganHangDTOS.size(); i++) {
                soTienGuiNganHangDTOS.get(i).setDateToString(convertDate(soTienGuiNganHangDTOS.get(i).getDate()));
                soTienGuiNganHangDTOS.get(i).setPostedDateToString(convertDate(soTienGuiNganHangDTOS.get(i).getPostedDate()));
                soTienGuiNganHangDTOS.get(i).setSoThuToString(Utils.formatTien(soTienGuiNganHangDTOS.get(i).getSoThu(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                soTienGuiNganHangDTOS.get(i).setSoChiToString(Utils.formatTien(soTienGuiNganHangDTOS.get(i).getSoChi(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                soTienGuiNganHangDTOS.get(i).setSoTonToString(Utils.formatTien(soTienGuiNganHangDTOS.get(i).getSoTon(), requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                if (soTienGuiNganHangDTOS.get(i).getPositionOrder() == 3) {
                    TongThu = TongThu.add(soTienGuiNganHangDTOS.get(i).getSoThu() != null ? soTienGuiNganHangDTOS.get(i).getSoThu() : BigDecimal.ZERO);
                    TongChi = TongChi.add(soTienGuiNganHangDTOS.get(i).getSoChi() != null ? soTienGuiNganHangDTOS.get(i).getSoChi() : BigDecimal.ZERO);
                    TongTon = TongTon.add(soTienGuiNganHangDTOS.get(i).getSoTon() != null ? soTienGuiNganHangDTOS.get(i).getSoTon() : BigDecimal.ZERO);
                }
                soTienGuiNganHangDTOS.get(i).setLinkRef(getRefLink(soTienGuiNganHangDTOS.get(i).getTypeID(), soTienGuiNganHangDTOS.get(i).getRefID(), types, null, null, null, null));
            }
            parameter.put("TongThu", Utils.formatTien(TongThu, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongChi", Utils.formatTien(TongChi, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
            parameter.put("TongTon", Utils.formatTien(TongTon, requestReport.getCurrencyID().equals(mainCurrency) ? Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe, userDTO));
        } else {
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
            soTienGuiNganHangDTOS.add(new SoTienGuiNganHangDTO(0, null, "", "", null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 2, null, null));
        }
        parameter.put("REPORT_MAX_COUNT", soTienGuiNganHangDTOS.size());

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soTienGuiNganHangDTOS.size() > 0) {
            result = ReportUtils.generateReportPDF(soTienGuiNganHangDTOS, parameter, jasperReport);
        } else {
            return JasperRunManager.runReportToPdf(jasperReport, parameter, new JREmptyDataSource());
        }
        return result;
    }

    byte[] getSoChiTietTaiKhoan(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoChiTietCacTaiKhoan";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoChiTietTaiKhoanDTO> soChiTietTaiKhoanDTOS = reportBusinessRepository.getSoChiTietTaiKhoan(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            getDataFromListString(requestReport.getAccountList()),
            requestReport.getGroupTheSameItem(),
            requestReport.getCompanyID(),
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            0,
            requestReport.getDependent(),
            requestReport.getGetAmountOriginal()
        );
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
        checkEbPackage(userDTO, parameter);
        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
            .findAny()
            .get()
            .getData();
        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("CurrencyID", requestReport.getCurrencyID());
        parameter.put("Period", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        List<Type> types = typeRepository.findAllByIsActive();
        String formatTien = "";
        if (requestReport.getGetAmountOriginal()) {
            formatTien = Constants.SystemOption.DDSo_NgoaiTe;
        } else {
            formatTien = Constants.SystemOption.DDSo_TienVND;
        }
        if (soChiTietTaiKhoanDTOS.size() > 0) {
            for (SoChiTietTaiKhoanDTO soChiTietTaiKhoanDTO : soChiTietTaiKhoanDTOS) {
                soChiTietTaiKhoanDTO.setDateToString(convertDate(soChiTietTaiKhoanDTO.getDate()));
                soChiTietTaiKhoanDTO.setPostedDateToString(convertDate(soChiTietTaiKhoanDTO.getPostedDate()));
                if (soChiTietTaiKhoanDTO.getDebitAmount() == null || soChiTietTaiKhoanDTO.getDebitAmount().compareTo(BigDecimal.ZERO) == 0) {
                    soChiTietTaiKhoanDTO.setDebitAmountToString("");
                } else {
                    soChiTietTaiKhoanDTO.setDebitAmountToString(Utils.formatTien(soChiTietTaiKhoanDTO.getDebitAmount(), formatTien, userDTO));
                }
                if (soChiTietTaiKhoanDTO.getCreditAmount() == null || soChiTietTaiKhoanDTO.getCreditAmount().compareTo(BigDecimal.ZERO) == 0) {
                    soChiTietTaiKhoanDTO.setCreditAmountToString("");
                } else {
                    soChiTietTaiKhoanDTO.setCreditAmountToString(Utils.formatTien(soChiTietTaiKhoanDTO.getCreditAmount(), formatTien, userDTO));
                }
                if (soChiTietTaiKhoanDTO.getClosingCreditAmount() == null || soChiTietTaiKhoanDTO.getClosingCreditAmount().compareTo(BigDecimal.ZERO) == 0) {
                    soChiTietTaiKhoanDTO.setClosingCreditAmountToString("");
                } else {
                    soChiTietTaiKhoanDTO.setClosingCreditAmountToString(Utils.formatTien(soChiTietTaiKhoanDTO.getClosingCreditAmount(), formatTien, userDTO));
                }
                if (soChiTietTaiKhoanDTO.getClosingDebitAmount() == null || soChiTietTaiKhoanDTO.getClosingDebitAmount().compareTo(BigDecimal.ZERO) == 0) {
                    soChiTietTaiKhoanDTO.setClosingDebitAmountToString("");
                } else {
                    soChiTietTaiKhoanDTO.setClosingDebitAmountToString(Utils.formatTien(soChiTietTaiKhoanDTO.getClosingDebitAmount(), formatTien, userDTO));
                }
                soChiTietTaiKhoanDTO.setLinkRef(getRefLink(soChiTietTaiKhoanDTO.getTypeID(), soChiTietTaiKhoanDTO.getReferenceID(), types, null, null, null, null));
            }
        } else {
            for (int i = 0; i <= 2; i++) {
                SoChiTietTaiKhoanDTO soNhatKyChungDTO = new SoChiTietTaiKhoanDTO();
                soNhatKyChungDTO.setDateToString("");
                soNhatKyChungDTO.setPostedDateToString("");
                soNhatKyChungDTO.setClosingDebitAmountToString("");
                soNhatKyChungDTO.setBold(false);
                soNhatKyChungDTO.setCreditAmountToString("");
                soNhatKyChungDTO.setAccountNumber("");
                soNhatKyChungDTO.setAccountCorresponding("");
                soNhatKyChungDTO.setAccountNameWithAccountNumber("");
                soNhatKyChungDTO.setClosingDebitAmountToString("");
                soNhatKyChungDTO.setClosingCreditAmountToString("");
                soNhatKyChungDTO.setClosingDebitAmountOriginalToString("");
                soNhatKyChungDTO.setClosingCreditAmountOriginalToString("");
                soChiTietTaiKhoanDTOS.add(soNhatKyChungDTO);
            }
            SoChiTietTaiKhoanDTO soNhatKyChungDTO = new SoChiTietTaiKhoanDTO();
            soNhatKyChungDTO.setDateToString("");
            soNhatKyChungDTO.setPostedDateToString("");
            soNhatKyChungDTO.setClosingDebitAmountToString("");
            soNhatKyChungDTO.setBold(true);
            soNhatKyChungDTO.setJournalMemo("Cộng");
            soNhatKyChungDTO.setCreditAmountToString("");
            soNhatKyChungDTO.setAccountNumber("");
            soNhatKyChungDTO.setAccountCorresponding("");
            soNhatKyChungDTO.setAccountNameWithAccountNumber("");
            soNhatKyChungDTO.setClosingDebitAmountToString("");
            soNhatKyChungDTO.setClosingCreditAmountToString("");
            soNhatKyChungDTO.setClosingDebitAmountOriginalToString("");
            soNhatKyChungDTO.setClosingCreditAmountOriginalToString("");
            soChiTietTaiKhoanDTOS.add(soNhatKyChungDTO);
        }
        parameter.put("REPORT_MAX_COUNT", soChiTietTaiKhoanDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(soChiTietTaiKhoanDTOS, parameter, jasperReport);
        return result;
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
     * @param userDTO
     * @param parameter
     * @Author Hautv
     */
    private void setHeader(UserDTO userDTO, Map<String, Object> parameter, UUID selectOrg) {
        String header0;
        if (userDTO.getOrganizationUnit().getId().equals(selectOrg)) {
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
        } else {
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(selectOrg);
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(selectOrg);
            if (organizationUnitOptionReport != null && organizationUnit != null) {
                userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReport);
                if (organizationUnitOptionReport.getHeaderSetting() != null) {
                    if (organizationUnitOptionReport.getHeaderSetting().equals(Constants.Report.HeaderSetting0)) {
                        header0 = ((StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + organizationUnit.getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                            + (StringUtils.isEmpty(organizationUnit.getAddress()) ? "" : ("<i>" + organizationUnit.getAddress() + "</i>" + "<br>"))
                            + (StringUtils.isEmpty(organizationUnit.gettaxCode()) ? "" : ("<i>" + organizationUnit.gettaxCode())));
                        parameter.put("header0", header0);
                    } else if (organizationUnitOptionReport.getHeaderSetting().equals(Constants.Report.HeaderSetting1)) {
                        header0 = ((StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + organizationUnit.getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                            + (StringUtils.isEmpty(organizationUnit.gettaxCode()) ? "" : ("<i>" + organizationUnit.gettaxCode())));
                        parameter.put("header0", header0);
                    } else {
                        header0 = ((StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : ("<b>" + organizationUnit.getOrganizationUnitName().toUpperCase() + "</b>" + "<br>"))
                            + (StringUtils.isEmpty(organizationUnit.getAddress()) ? "" : ("<i>" + organizationUnit.getAddress() + "</i>" + "<br>")));
                        parameter.put("header0", header0);
                    }
                }
            }
        }
    }

    /**
     * @param userDTO
     * @Author chuongnv
     */
    private String setHeaderExcel(UserDTO userDTO) {
        if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport() == null) {
            userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
        }
        if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting() != null) {
            if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting0)) {
                return ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase())) + "\n" +
                    ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : userDTO.getOrganizationUnit().getAddress()) + "\n") +
                    ((StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : userDTO.getOrganizationUnit().gettaxCode()));
            } else if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting1)) {
                return ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) + "\n") +
                    (StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : userDTO.getOrganizationUnit().gettaxCode());
            } else {
                return ((StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase())) + "\n" +
                    (StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : userDTO.getOrganizationUnit().getAddress());
            }
        }
        return "";
    }

    /**
     * @Author chuongnv
     */
    private void SetHeaderExcel(UUID selectOrg, UserDTO userDTO, Workbook workbook, Sheet sheet, Integer rowCompany, Integer rowAddress,
                                Integer rowTaxCode, Integer cell) {
        if (selectOrg.equals(userDTO.getOrganizationUnit().getId())) {
            if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport() == null) {
                userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReportRepository.findByOrganizationUnitID(userDTO.getOrganizationUnit().getId()));
            }
            if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting() != null) {
                if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting0)) {
                    sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase());
                    sheet.getRow(rowAddress).getCell(cell).setCellValue(StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : userDTO.getOrganizationUnit().getAddress());
                    sheet.getRow(StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? rowAddress : rowTaxCode).getCell(cell).setCellValue((StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : userDTO.getOrganizationUnit().gettaxCode()));
//                sheet.getRow(rowAddress).setHeight((short)(sheet.getRow(rowAddress).getHeight() * 2));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowAddress).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowTaxCode).getCell(cell), CellUtil.WRAP_TEXT, true);
                } else if (userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getHeaderSetting().equals(Constants.Report.HeaderSetting1)) {
                    sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase());
                    sheet.getRow(rowAddress).getCell(cell).setCellValue((StringUtils.isEmpty(userDTO.getOrganizationUnit().gettaxCode()) ? "" : userDTO.getOrganizationUnit().gettaxCode()));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowCompany).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowTaxCode).getCell(cell), CellUtil.WRAP_TEXT, true);
                } else {
                    sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase()) ? "" : userDTO.getOrganizationUnit().getOrganizationUnitName().toUpperCase());
                    sheet.getRow(rowAddress).getCell(cell).setCellValue(StringUtils.isEmpty(userDTO.getOrganizationUnit().getAddress()) ? "" : userDTO.getOrganizationUnit().getAddress());
//                sheet.getRow(rowAddress).setHeight((short)(sheet.getRow(rowAddress).getHeight() * 2));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowCompany).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowAddress).getCell(cell), CellUtil.WRAP_TEXT, true);
                }
            }
        } else {
            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findByOrganizationUnitID(selectOrg);
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(selectOrg);
            if (organizationUnitOptionReport != null && organizationUnit != null) {
                userDTO.getOrganizationUnit().setOrganizationUnitOptionReport(organizationUnitOptionReport);
                if (organizationUnitOptionReport.getHeaderSetting() != null) {
                    if (organizationUnitOptionReport.getHeaderSetting().equals(Constants.Report.HeaderSetting0)) {
                        sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : organizationUnit.getOrganizationUnitName().toUpperCase());
                        sheet.getRow(rowAddress).getCell(cell).setCellValue(StringUtils.isEmpty(organizationUnit.getAddress()) ? "" : organizationUnit.getAddress());
                        sheet.getRow(StringUtils.isEmpty(organizationUnit.getAddress()) ? rowAddress : rowTaxCode).getCell(cell).setCellValue((StringUtils.isEmpty(organizationUnit.gettaxCode()) ? "" : organizationUnit.gettaxCode()));
//                sheet.getRow(rowAddress).setHeight((short)(sheet.getRow(rowAddress).getHeight() * 2));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowAddress).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowTaxCode).getCell(cell), CellUtil.WRAP_TEXT, true);
                    } else if (organizationUnitOptionReport.getHeaderSetting().equals(Constants.Report.HeaderSetting1)) {
                        sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : organizationUnit.getOrganizationUnitName().toUpperCase());
                        sheet.getRow(rowAddress).getCell(cell).setCellValue((StringUtils.isEmpty(organizationUnit.gettaxCode()) ? "" : organizationUnit.gettaxCode()));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowCompany).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowTaxCode).getCell(cell), CellUtil.WRAP_TEXT, true);
                    } else {
                        sheet.getRow(rowCompany).getCell(cell).setCellValue(StringUtils.isEmpty(organizationUnit.getOrganizationUnitName().toUpperCase()) ? "" : organizationUnit.getOrganizationUnitName().toUpperCase());
                        sheet.getRow(rowAddress).getCell(cell).setCellValue(StringUtils.isEmpty(organizationUnit.getAddress()) ? "" : organizationUnit.getAddress());
//                sheet.getRow(rowAddress).setHeight((short)(sheet.getRow(rowAddress).getHeight() * 2));
//                CellUtil.setCellStyleProperty(sheet.getRow(rowCompany).getCell(cell), CellUtil.WRAP_TEXT, true);
//                CellUtil.setCellStyleProperty(sheet.getRow(rowAddress).getCell(cell), CellUtil.WRAP_TEXT, true);
                    }
                }
            }
        }
    }

    /**
     * @Author chuongnv
     */
    private void SetFooterExcel(Workbook workbook, Sheet sheet, UserDTO userDTO, Integer startRow, Integer cellNgayThang,
                                Integer cellReporter, Integer cellAccountant, Integer cellManager) {
        Font fontItalic = workbook.createFont();
        fontItalic.setItalic(true);
        fontItalic.setFontName("Times New Roman");
        CellStyle cellStyleI = workbook.createCellStyle();
        cellStyleI.setFont(fontItalic);
        cellStyleI.setAlignment(HorizontalAlignment.CENTER);

        Font fontBold = workbook.createFont();
        fontBold.setBold(true);
        fontBold.setFontName("Times New Roman");
        CellStyle cellStyleB = workbook.createCellStyle();
        cellStyleB.setFont(fontBold);
        cellStyleB.setAlignment(HorizontalAlignment.CENTER);

        Font fontValue = workbook.createFont();
        fontValue.setBold(true);
        fontValue.setFontName("Times New Roman");
        CellStyle cellStyleDf = workbook.createCellStyle();
        cellStyleDf.setFont(fontValue);
        cellStyleDf.setAlignment(HorizontalAlignment.CENTER);
        cellStyleDf.setVerticalAlignment(VerticalAlignment.CENTER);

        Row rowNgayThang = sheet.getRow(startRow);
        rowNgayThang.createCell(cellNgayThang).setCellValue("Ngày......tháng......năm.....");
        rowNgayThang.getCell(cellNgayThang).setCellStyle(cellStyleI);

        Row rowFooter = sheet.getRow(startRow + 1);
        rowFooter.createCell(cellReporter).setCellValue("Người lập biểu");
        rowFooter.getCell(cellReporter).setCellStyle(cellStyleB);
        rowFooter.createCell(cellAccountant).setCellValue("Kế toán trưởng");
        rowFooter.getCell(cellAccountant).setCellStyle(cellStyleB);
        rowFooter.createCell(cellManager).setCellValue("Giám đốc");
        rowFooter.getCell(cellManager).setCellStyle(cellStyleB);

        Row rowFooter2 = sheet.getRow(startRow + 2);
        rowFooter2.createCell(cellReporter).setCellValue("(Ký, họ tên)");
        rowFooter2.getCell(cellReporter).setCellStyle(cellStyleI);
        rowFooter2.createCell(cellAccountant).setCellValue("(Ký, họ tên)");
        rowFooter2.getCell(cellAccountant).setCellStyle(cellStyleI);
        rowFooter2.createCell(cellManager).setCellValue("(Ký, họ tên, đóng dấu)");
        rowFooter2.getCell(cellManager).setCellStyle(cellStyleI);

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            Row rowFooterValue = sheet.createRow(startRow + 3);
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                rowFooterValue.createCell(cellReporter).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
                rowFooterValue.getCell(cellReporter).setCellStyle(cellStyleDf);
            } else {
                rowFooterValue.createCell(cellReporter).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
                rowFooterValue.getCell(cellReporter).setCellStyle(cellStyleDf);
            }

            rowFooterValue.createCell(cellAccountant).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
            rowFooterValue.getCell(cellAccountant).setCellStyle(cellStyleDf);

            rowFooterValue.createCell(cellManager).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector() : "");
            rowFooterValue.getCell(cellManager).setCellStyle(cellStyleDf);
        }
    }

    private void SetFooterAdditionalExcel(Workbook workbook, Sheet sheet, UserDTO userDTO, Integer startRow, Integer cellNgayThang,
                                          Integer cellReporter, Integer cellAccountant, Integer cellManager, String reporter, String accountant, String manager) {
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        Font fontItalic = workbook.createFont();
        fontItalic.setItalic(true);
        fontItalic.setFontName("Times New Roman");
        CellStyle cellStyleI = workbook.createCellStyle();
        cellStyleI.setFont(fontItalic);
        cellStyleI.setAlignment(HorizontalAlignment.CENTER);

        Font fontBold = workbook.createFont();
        fontBold.setBold(true);
        fontBold.setFontName("Times New Roman");
        CellStyle cellStyleB = workbook.createCellStyle();
        cellStyleB.setFont(fontBold);
        cellStyleB.setAlignment(HorizontalAlignment.CENTER);

        Font fontValue = workbook.createFont();
        fontValue.setBold(true);
        fontValue.setFontName("Times New Roman");
        CellStyle cellStyleDf = workbook.createCellStyle();
        cellStyleDf.setFont(fontValue);
        cellStyleDf.setAlignment(HorizontalAlignment.CENTER);
        cellStyleDf.setVerticalAlignment(VerticalAlignment.CENTER);

        Row rowSoTrang = sheet.createRow(startRow);
        rowSoTrang.createCell(0).setCellValue("   -  Sổ này có : ... trang, đánh số từ trang 01 đến trang ..");
        rowSoTrang.getCell(0).setCellStyle(cellStyle);

        Row rowNgayMoSo = sheet.createRow(startRow + 1);
        rowNgayMoSo.createCell(0).setCellValue("   -  Ngày mở sổ : ........");
        rowNgayMoSo.getCell(0).setCellStyle(cellStyle);

        Row rowNgayThang = sheet.createRow(startRow + 2);
        rowNgayThang.createCell(cellNgayThang).setCellValue("Ngày......tháng......năm...");
        rowNgayThang.getCell(cellNgayThang).setCellStyle(cellStyleI);

        Row rowFooter = sheet.createRow(startRow + 3);
        rowFooter.createCell(cellReporter).setCellValue(reporter);
        rowFooter.getCell(cellReporter).setCellStyle(cellStyleB);
        rowFooter.createCell(cellAccountant).setCellValue(accountant);
        rowFooter.getCell(cellAccountant).setCellStyle(cellStyleB);
        rowFooter.createCell(cellManager).setCellValue(manager);
        rowFooter.getCell(cellManager).setCellStyle(cellStyleB);

        Row rowFooter2 = sheet.createRow(startRow + 4);
        rowFooter2.createCell(cellReporter).setCellValue("(Ký, họ tên)");
        rowFooter2.getCell(cellReporter).setCellStyle(cellStyleI);
        rowFooter2.createCell(cellAccountant).setCellValue("(Ký, họ tên)");
        rowFooter2.getCell(cellAccountant).setCellStyle(cellStyleI);
        rowFooter2.createCell(cellManager).setCellValue("(Ký, họ tên, đóng dấu)");
        rowFooter2.getCell(cellManager).setCellStyle(cellStyleI);

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            Row rowFooterValue = sheet.createRow(startRow + 7);
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                rowFooterValue.createCell(cellReporter).setCellValue(userDTO.getFullName());
                rowFooterValue.getCell(cellReporter).setCellStyle(cellStyleB);
            } else {
                rowFooterValue.createCell(cellReporter).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                rowFooterValue.getCell(cellReporter).setCellStyle(cellStyleB);
            }

            rowFooterValue.createCell(cellAccountant).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            rowFooterValue.getCell(cellAccountant).setCellStyle(cellStyleDf);

            rowFooterValue.createCell(cellManager).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            rowFooterValue.getCell(cellManager).setCellStyle(cellStyleDf);
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

    private String getRefLink(Integer typeID, UUID refID, List<Type> types, List<SAInvoiceViewDTO> saInvoices, List<SaReturnDTO> saReturnDTOS, List<ViewPPInvoiceDTO> ppInvoiceDTOS, List<PPDiscountReturnDTO> ppDiscountReturnDTOS) {
        String url = "https://app.easybooks.vn/#/";
//        String url = "http://localhost:9000/#/";
        if (typeID != null) {
            Optional<Type> optionalType = types.stream().filter(n -> n.getId().equals(typeID)).findFirst();
            if (optionalType.isPresent()) {
                Type type = optionalType.get();
                Integer typeGroupID = type.getTypeGroupID();
                switch (typeGroupID) {
                    // Hàng bán trả lại
                    case 33:
                        url += "hang-ban/tra-lai/" + refID + "/edit/from-ref";
                        break;
                    // Giảm giá hàng bán
                    case 34:
                        url += "hang-ban/giam-gia/" + refID + "/edit/from-ref";
                        break;
                    // Xuất hóa đơn
                    case 35:
                        url += "xuat-hoa-don/" + refID + "/edit/from-ref";
                        break;
                    case 22:
                        url += "hang-mua/tra-lai/" + refID + "/edit";
                        break;
                    case 23:
                        url += "hang-mua/giam-gia/" + refID + "/edit";
                        break;
                    case 10:
                        url += "mc-receipt/" + refID + "/edit";
                        break;
                    case 16:
                        url += "mb-deposit/" + refID + "/edit";
                        break;
                    case 17:
                        url += "mb-credit-card/" + refID + "/edit";
                        break;
                    case 70:
                        url += "g-other-voucher/" + refID + "/edit";
                        break;
                    case 11:
                        url += "mc-payment/" + refID + "/edit";
                        break;
                    case 31:
                        url += "sa-order/" + refID + "/edit";
                        break;
                    case 24:
                        url += "mua-dich-vu/" + refID + "/edit/pp-service";
                        break;
                    case 40:
                        if (typeID == TypeConstant.NHAP_KHO_TU_MUA_HANG && ppInvoiceDTOS != null) {
                            List<ViewPPInvoiceDTO> ppInvoiceDTO = ppInvoiceDTOS.stream().filter(n -> n.getRsInwardOutwardID().equals(refID)).collect(Collectors.toList());
                            if (ppInvoiceDTO.size() > 0) {
                                //url += "mua-hang/qua-kho-ref/" + ppInvoiceDTO.get(0).getId() + "/edit/1";
                                url += "mua-hang/qua-kho/" + ppInvoiceDTO.get(0).getId() + "/edit";
                            }
                        } else if (typeID == TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI && saReturnDTOS != null) {
                            List<SaReturnDTO> saReturnDTO = saReturnDTOS.stream().filter(n -> n.getRsInwardOutwardID().equals(refID)).collect(Collectors.toList());
                            if (saReturnDTO.size() > 0) {
                                url += "hang-ban/tra-lai/" + saReturnDTO.get(0).getId() + "/edit/from-ref";
                            }
                        } else if (typeID == TypeConstant.NHAP_KHO_TU_DIEU_CHINH) {
                            url += "";
                        } else {
                            url += "nhap-kho/" + refID + "/edit/from-ref";
                        }
                        break;
                    case 20:
                        url += "don-mua-hang/" + refID + "/edit";
                        break;
                    case 41:
                        if (typeID == TypeConstant.XUAT_KHO_TU_BAN_HANG && saInvoices != null) {
                            List<SAInvoiceViewDTO> saInvoice = saInvoices.stream().filter(n -> n.getRsInwardOutwardID().equals(refID)).collect(Collectors.toList());
                            if (saInvoice.size() > 0) {
                                url += "chung-tu-ban-hang/" + saInvoice.get(0).getId() + "/edit/from-ref";
                            }
                        } else if (typeID == TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI && ppDiscountReturnDTOS != null) {
                            List<PPDiscountReturnDTO> ppDiscountReturnDTO = ppDiscountReturnDTOS.stream().filter(n -> n.getRsInwardOutwardID().equals(refID)).collect(Collectors.toList());
                            if (ppDiscountReturnDTO.size() > 0) {
                                url += "hang-mua/tra-lai/" + ppDiscountReturnDTO.get(0).getId() + "/view";
                            }
                        } else if (typeID == TypeConstant.XUAT_KHO_TU_DIEU_CHINH) {
                            url += "";
                        } else {
                            url += "xuat-kho/" + refID + "/edit/from-ref";
                        }
                        break;
                    case 21:
                        Boolean check = pPInvoiceRepository.getStoredInRepositoryById(refID);
                        if (check != null) {
                            if (check) {
                                url += "mua-hang/qua-kho/" + refID + "/edit";
                            } else {
                                url += "mua-hang/khong-qua-kho/" + refID + "/edit";
                            }
                        } else {
                            url += "";
                        }

                        break;
                    case 18:
                        url += "mc-audit/" + refID + "/edit";
                        break;
                    case 32:
                        url += "chung-tu-ban-hang/" + refID + "/edit/from-ref";
                        break;
                    case 30:
                        url += "sa-quote/" + refID + "/edit";
                        break;
                    case 12:
                    case 13:
                    case 14:
                        url += "mb-teller-paper/" + refID + "/edit";
                        break;
                }
            }
        }
        return url;
    }

    byte[] getExcelSoQuyTienMat(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<SoQuyTienMatDTO> soQuyTienMatDTOS = reportBusinessRepository.getSoQuyTienMat(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            phienSoLamViec,
            requestReport.getDependent(),
            requestReport.getTypeShowCurrency());
        if (soQuyTienMatDTOS.size() == 0) {
            soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
            soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
            soQuyTienMatDTOS.add(new SoQuyTienMatDTO(0, null, null, null, "", "", "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0));
        }
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = "";
        header4 = "Loại tiền: " +
            requestReport.getCurrencyID() + "; " + Period;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(13, sheet.getLastRowNum(), soQuyTienMatDTOS.size());
            // fill dữ liệu vào file
            int i = 11;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            Font fontBI = workbook.createFont();
            fontBI.setFontName("Times New Roman");
            fontBI.setBold(true);
            fontBI.setItalic(true);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setWrapText(true);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            //
            CellStyle styleTitle = workbook.createCellStyle();
            sheet.getRow(6).getCell(0).setCellValue(header4);
            BigDecimal sumTotalPaymentFBCurrencyID = BigDecimal.ZERO;
            BigDecimal sumTotalReceiptFBCurrencyID = BigDecimal.ZERO;
            BigDecimal sumClosingFBCurrencyID = BigDecimal.ZERO;
            for (SoQuyTienMatDTO item : soQuyTienMatDTOS) {
                Row row = sheet.createRow(i);
                if (item.getOrderPriority() == null) {
                    style.setFont(fontB);
                } else {
                    style.setFont(fontDf);
                }
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getDate() != null ? convertDate(item.getDate()) : "");

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getPostedDate() != null ? convertDate(item.getPostedDate()) : "");

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                cell3.setCellValue(item.getReceiptRefNo() != null ? item.getReceiptRefNo() : "");

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                cell4.setCellValue(item.getPaymentRefNo() != null ? item.getPaymentRefNo() : "");

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                cell5.setCellValue(item.getJournalMemo() != null ? item.getJournalMemo() : "");

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(item.getTotalReceiptFBCurrencyID() != null ? item.getTotalReceiptFBCurrencyID().doubleValue() : 0);

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(item.getTotalPaymentFBCurrencyID() != null ? item.getTotalPaymentFBCurrencyID().doubleValue() : 0);

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getClosingFBCurrencyID() != null ? item.getClosingFBCurrencyID().doubleValue() : 0);

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.LEFT);
                cell9.setCellValue(item.getNote() != null ? item.getNote() : "");
                sumTotalPaymentFBCurrencyID = item.getTotalPaymentFBCurrencyID() != null ? sumTotalPaymentFBCurrencyID.add(item.getTotalPaymentFBCurrencyID()) : sumTotalPaymentFBCurrencyID;
                sumTotalReceiptFBCurrencyID = item.getTotalReceiptFBCurrencyID() != null ? sumTotalReceiptFBCurrencyID.add(item.getTotalReceiptFBCurrencyID()) : sumTotalReceiptFBCurrencyID;
                i++;
            }
            sumClosingFBCurrencyID = soQuyTienMatDTOS.get(soQuyTienMatDTOS.size() - 1).getClosingFBCurrencyID();
            //Lấy dòng tổng cộn
            CellStyle styleSum = workbook.createCellStyle();
            styleSum.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleSum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleSum.setWrapText(true);
            styleSum.setBorderBottom(BorderStyle.THIN);
            styleSum.setBorderTop(BorderStyle.THIN);
            styleSum.setBorderRight(BorderStyle.THIN);
            styleSum.setBorderLeft(BorderStyle.THIN);
            styleSum.setFont(fontB);
            Row rowSum = sheet.createRow(i);
            CellUtil.setAlignment(rowSum.createCell(0), HorizontalAlignment.LEFT);
            rowSum.getCell(0).setCellStyle(styleSum);
            rowSum.getCell(0).setCellValue("Tổng cộng");
            rowSum.createCell(1);
            rowSum.createCell(2);
            rowSum.createCell(3);
            rowSum.createCell(4);
            rowSum.getCell(1).setCellStyle(styleSum);
            rowSum.getCell(2).setCellStyle(styleSum);
            rowSum.getCell(3).setCellStyle(styleSum);
            rowSum.getCell(4).setCellStyle(styleSum);

            CellUtil.setAlignment(rowSum.createCell(5), HorizontalAlignment.RIGHT);
            rowSum.getCell(5).setCellStyle(styleSum);
            rowSum.getCell(5).setCellValue(sumTotalReceiptFBCurrencyID.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(6), HorizontalAlignment.RIGHT);
            rowSum.getCell(6).setCellStyle(styleSum);
            rowSum.getCell(6).setCellValue(sumTotalPaymentFBCurrencyID.doubleValue());

            CellUtil.setAlignment(rowSum.createCell(7), HorizontalAlignment.RIGHT);
            rowSum.getCell(7).setCellStyle(styleSum);
            rowSum.getCell(7).setCellValue(sumClosingFBCurrencyID.doubleValue());

            rowSum.createCell(8);
            rowSum.getCell(8).setCellStyle(styleSum);

            Row rowFooter = sheet.createRow(i + 2);
            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
            rowFooter.getCell(0).setCellStyle(styleHeader);
            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");

            Row rowFooter2 = sheet.createRow(i + 3);
            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
            rowFooter2.getCell(0).setCellStyle(styleHeader);
            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
            sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 0, 4));
            SetFooterExcel(workbook, sheet, userDTO, (17 + soQuyTienMatDTOS.size()), 6, 1, 4, 6);
            sheet.getRow(17 + soQuyTienMatDTOS.size() + 1).getCell(1).setCellValue("Người ghi sổ");
            sheet.getRow(17 + soQuyTienMatDTOS.size() + 3).getCell(1).setCellValue("");
            sheet.getRow(17 + soQuyTienMatDTOS.size() + 3).getCell(4).setCellValue("");
            sheet.getRow(17 + soQuyTienMatDTOS.size() + 3).getCell(6).setCellValue("");
            Row row = sheet.createRow(17 + soQuyTienMatDTOS.size() + 5);
            CellStyle styleFooter = workbook.createCellStyle();
            styleFooter.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleFooter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleFooter.setWrapText(true);
            styleFooter.setFont(fontB);

            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                Cell cellRecorder = row.createCell(1);
                cellRecorder.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellRecorder, HorizontalAlignment.CENTER);
                Cell cellChiefAccountant = row.createCell(4);
                cellChiefAccountant.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellChiefAccountant, HorizontalAlignment.CENTER);
                Cell cellDirector = row.createCell(6);
                cellDirector.setCellStyle(styleFooter);
                CellUtil.setAlignment(cellDirector, HorizontalAlignment.CENTER);
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                    cellRecorder.setCellValue(userDTO.getFullName());
                } else {
                    cellRecorder.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
                }
                cellChiefAccountant.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
                cellDirector.setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author anmt
     */
    byte[] getSoCCDC(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoCongCuDungCu";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        StringBuilder toolIDs = new StringBuilder(",");
        for (UUID item : requestReport.getTools()) {
            toolIDs.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<SoCongCuDungCuDTO> soCongCuDungCuDTOs = reportBusinessRepository.getSoCongCuDungCu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            toolIDs.toString(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
        parameter.put("detailSize", soCongCuDungCuDTOs.size());
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        Integer totalCol1 = 0;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        Integer totalCol3 = 0;
        Integer totalCol4 = 0;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        BigDecimal totalCol7 = BigDecimal.ZERO;
        for (int i = 0; i < soCongCuDungCuDTOs.size(); i++) {
            soCongCuDungCuDTOs.get(i).setAmountString(Utils.formatTien(soCongCuDungCuDTOs.get(i).getAmount(), typeAmount, userDTO));
            soCongCuDungCuDTOs.get(i).setIncrementDateString(convertDate(soCongCuDungCuDTOs.get(i).getIncrementDate()));
            soCongCuDungCuDTOs.get(i).setAllocationTimesString(soCongCuDungCuDTOs.get(i).getAllocationTimes().toString());
            soCongCuDungCuDTOs.get(i).setAllocatedAmountString(Utils.formatTien(soCongCuDungCuDTOs.get(i).getAllocatedAmount(), typeAmount, userDTO));
            soCongCuDungCuDTOs.get(i).setDecrementAllocationTimeString(soCongCuDungCuDTOs.get(i).getDecrementAllocationTime().toString());
            soCongCuDungCuDTOs.get(i).setRemainingAllocationTimeString(soCongCuDungCuDTOs.get(i).getRemainingAllocationTime().toString());
            soCongCuDungCuDTOs.get(i).setDecrementAmountString(Utils.formatTien(soCongCuDungCuDTOs.get(i).getDecrementAmount(), typeAmount, userDTO));
            soCongCuDungCuDTOs.get(i).setRemainingAmountString(Utils.formatTien(soCongCuDungCuDTOs.get(i).getRemainingAmount(), typeAmount, userDTO));
            soCongCuDungCuDTOs.get(i).setPostedDateString(convertDate(soCongCuDungCuDTOs.get(i).getPostedDate()));
            totalCol1 += soCongCuDungCuDTOs.get(i).getAllocationTimes() != null ? soCongCuDungCuDTOs.get(i).getAllocationTimes() : 0;
            totalCol2 = totalCol2.add(soCongCuDungCuDTOs.get(i).getAllocatedAmount() != null ? soCongCuDungCuDTOs.get(i).getAllocatedAmount() : BigDecimal.ZERO);
            totalCol3 += soCongCuDungCuDTOs.get(i).getDecrementAllocationTime() != null ? soCongCuDungCuDTOs.get(i).getDecrementAllocationTime() : 0;
            totalCol4 += soCongCuDungCuDTOs.get(i).getRemainingAllocationTime() != null ? soCongCuDungCuDTOs.get(i).getRemainingAllocationTime() : 0;
            totalCol5 = totalCol5.add(soCongCuDungCuDTOs.get(i).getDecrementAmount() !=null ? soCongCuDungCuDTOs.get(i).getDecrementAmount() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soCongCuDungCuDTOs.get(i).getRemainingAmount() != null ? soCongCuDungCuDTOs.get(i).getRemainingAmount() : BigDecimal.ZERO);
            totalCol7 = totalCol7.add(soCongCuDungCuDTOs.get(i).getAmount() != null ? soCongCuDungCuDTOs.get(i).getAmount() : BigDecimal.ZERO);
        }
        parameter.put("totalCol1", totalCol1.toString());
        parameter.put("totalCol2", Utils.formatTien(totalCol2, typeAmount, userDTO));
        parameter.put("totalCol3", totalCol3.toString());
        parameter.put("totalCol4", totalCol4.toString());
        parameter.put("totalCol5", Utils.formatTien(totalCol5, typeAmount, userDTO));
        parameter.put("totalCol6", Utils.formatTien(totalCol6, typeAmount, userDTO));
        parameter.put("totalCol7", Utils.formatTien(totalCol7, typeAmount, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soCongCuDungCuDTOs.isEmpty()) {
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
            parameter.put("detailSize", soCongCuDungCuDTOs.size());
            parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
            parameter.put("totalCol1", "");
            parameter.put("totalCol2", "");
            parameter.put("totalCol3", "");
            parameter.put("totalCol4", "");
            parameter.put("totalCol5", "");
            parameter.put("totalCol6", "");
            parameter.put("totalCol7", "");
        }
 //       parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
        result = ReportUtils.generateReportPDF(soCongCuDungCuDTOs, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoCongCuDungCu(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        DecimalFormat df = new DecimalFormat("0");
        String materialGoods = ",";
        StringBuilder toolIDs = new StringBuilder(",");
        for (UUID item : requestReport.getTools()) {
            toolIDs.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<SoCongCuDungCuDTO> soCongCuDungCuDTOs = reportBusinessRepository.getSoCongCuDungCu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            toolIDs.toString(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        Boolean isShow = false;
        if (soCongCuDungCuDTOs.size() == 0) {
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
            soCongCuDungCuDTOs.add(new SoCongCuDungCuDTO());
        }
        Integer totalCol1 = 0;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        Integer totalCol3 = 0;
        Integer totalCol4 = 0;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        BigDecimal totalCol7 = BigDecimal.ZERO;
        for (int i = 0; i < soCongCuDungCuDTOs.size(); i++) {
            totalCol1 += soCongCuDungCuDTOs.get(i).getAllocationTimes() != null ? soCongCuDungCuDTOs.get(i).getAllocationTimes() : 0;
            totalCol2 = totalCol2.add(soCongCuDungCuDTOs.get(i).getAllocatedAmount() != null ? soCongCuDungCuDTOs.get(i).getAllocatedAmount() : BigDecimal.ZERO);
            totalCol3 += soCongCuDungCuDTOs.get(i).getDecrementAllocationTime() != null ? soCongCuDungCuDTOs.get(i).getDecrementAllocationTime() : 0;
            totalCol4 += soCongCuDungCuDTOs.get(i).getRemainingAllocationTime() != null ? soCongCuDungCuDTOs.get(i).getRemainingAllocationTime() : 0;
            totalCol5 = totalCol5.add(soCongCuDungCuDTOs.get(i).getDecrementAmount() !=null ? soCongCuDungCuDTOs.get(i).getDecrementAmount() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soCongCuDungCuDTOs.get(i).getRemainingAmount() != null ? soCongCuDungCuDTOs.get(i).getRemainingAmount() : BigDecimal.ZERO);
            totalCol7 = totalCol7.add(soCongCuDungCuDTOs.get(i).getAmount() != null ? soCongCuDungCuDTOs.get(i).getAmount() : BigDecimal.ZERO);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(6, sheet.getLastRowNum(), soCongCuDungCuDTOs.size());
            // fill dữ liệu vào file
            int i = 6;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
//            Cell cellHeader = sheet.getRow(0).getCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
//            CellStyle styleHeader = workbook.createCellStyle();
//            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(4).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            for (SoCongCuDungCuDTO item : soCongCuDungCuDTOs) {
                Row row = sheet.createRow(i);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue(item.getToolsCode());

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                cell2.setCellValue(item.getToolsName());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                cell3.setCellValue(convertDate(item.getIncrementDate()));

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell4.setCellValue(item.getAmount().doubleValue());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(item.getAllocationTimes());

                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell6.setCellValue(item.getAllocatedAmount().doubleValue());

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(item.getDecrementAllocationTime());

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getRemainingAllocationTime());

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell9.setCellValue(item.getDecrementAmount().doubleValue());

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell10.setCellValue(item.getRemainingAmount().doubleValue());

                Cell cell11 = row.createCell(10);
                cell11.setCellStyle(style);
                CellUtil.setAlignment(cell11, HorizontalAlignment.CENTER);
                cell11.setCellValue(convertDate(item.getPostedDate()));

                i++;
            }

            Row rowTotal = sheet.getRow(6 + soCongCuDungCuDTOs.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Cộng");

            Cell cell1 = rowTotal.createCell(3);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            cell1.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell1.setCellValue(totalCol7.doubleValue());

            Cell cell2 = rowTotal.createCell(4);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            cell2.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell2.setCellValue(totalCol1.doubleValue());

            Cell cell3 = rowTotal.createCell(5);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell3.setCellValue(totalCol2.doubleValue());

            Cell cell4 = rowTotal.createCell(6);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell4.setCellValue(totalCol3.doubleValue());

            Cell cell5 = rowTotal.createCell(7);
            cell5.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell5.setCellValue(totalCol4.doubleValue());

            Cell cell6 = rowTotal.createCell(8);
            cell6.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell6.setCellValue(totalCol5.doubleValue());

            Cell cell7 = rowTotal.createCell(9);
            cell7.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell7.setCellValue(totalCol6.doubleValue());

            Cell cell8 = rowTotal.createCell(10);
            cell8.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell8.setCellValue(totalCol7.doubleValue());

            veBorder(rowTotal, style, Stream.of(1, 2).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (8 + soCongCuDungCuDTOs.size()), 9, 1, 5, 9, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author anmt
     */
    byte[] getSoTheoDoiCCDCTaiNoiSD(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiCCDCTaiNoiSuDung";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        StringBuilder departmentIDs = new StringBuilder(",");
        for (UUID item : requestReport.getDepartments()) {
            departmentIDs.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<SoTheoDoiCCDCTaiNoiSuDungDTO> soTheoDoiCCDCTaiNoiSuDungDTOs = reportBusinessRepository.getSoTheoDoiCCDCTaiNoiSD(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            departmentIDs.toString(),
            phienSoLamViec);
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("REPORT_MAX_COUNT", soTheoDoiCCDCTaiNoiSuDungDTOs.size());
        parameter.put("detailSize", soTheoDoiCCDCTaiNoiSuDungDTOs.size());
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        for (int i = 0; i < soTheoDoiCCDCTaiNoiSuDungDTOs.size(); i++) {
            soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setNgayChungTuGhiTangString(convertDate(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getNgayChungTuGhiTang()));
            soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setIncrementQuantityString(
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getIncrementQuantity() != null ? soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getIncrementQuantity().toString() : null);
            soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setUnitPriceString(Utils.formatTien(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getUnitPrice(), typeAmount, userDTO));
            if (soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getIncrementAmount() != null) {
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setIncrementAmountString(Utils.formatTien(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getIncrementAmount(), typeAmount, userDTO));
            } else {
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setIncrementAmountString(null);
            }
            soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setNgayChungTuGhiGiamString(convertDate(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getNgayChungTuGhiGiam()));
            soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setDecrementQuantityString(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getDecrementQuantity() != null ?
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getDecrementQuantity().toString() : null);
            if (soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getDecrementAmount() != null) {
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setDecrementAmountString(Utils.formatTien(soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).getDecrementAmount(), typeAmount, userDTO));
            } else {
                soTheoDoiCCDCTaiNoiSuDungDTOs.get(i).setDecrementAmountString(null);
            }
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soTheoDoiCCDCTaiNoiSuDungDTOs.isEmpty()) {
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
            parameter.put("detailSize", soTheoDoiCCDCTaiNoiSuDungDTOs.size());
            parameter.put("REPORT_MAX_COUNT", soTheoDoiCCDCTaiNoiSuDungDTOs.size());
        }
        //       parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
        result = ReportUtils.generateReportPDF(soTheoDoiCCDCTaiNoiSuDungDTOs, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoTheoDoiCCDCTaiNoiSuDung(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        StringBuilder departmentIDs = new StringBuilder(",");
        for (UUID item : requestReport.getDepartments()) {
            departmentIDs.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<SoTheoDoiCCDCTaiNoiSuDungDTO> soTheoDoiCCDCTaiNoiSuDungDTOs = reportBusinessRepository.getSoTheoDoiCCDCTaiNoiSD(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            departmentIDs.toString(),
            phienSoLamViec
        );
        if (soTheoDoiCCDCTaiNoiSuDungDTOs.size() == 0) {
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
            soTheoDoiCCDCTaiNoiSuDungDTOs.add(new SoTheoDoiCCDCTaiNoiSuDungDTO());
        }
        Map<UUID, List<SoTheoDoiCCDCTaiNoiSuDungDTO>> listMapSoTheoDoiCCDCTaiNoiSD =
            soTheoDoiCCDCTaiNoiSuDungDTOs.stream().collect(Collectors.groupingBy(x -> x.getiDPhongBan()));
        // fill excel
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            // set font
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            // set cellstyle
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // clone sheet
            for (int i = 1; i < listMapSoTheoDoiCCDCTaiNoiSD.entrySet().size(); i++) {
                workbook.cloneSheet(0);
            }
            for (Map.Entry<UUID, List<SoTheoDoiCCDCTaiNoiSuDungDTO>> entry : listMapSoTheoDoiCCDCTaiNoiSD.entrySet()) {
                Sheet sheet = workbook.getSheetAt(0);
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                // set fromdate, todate
                sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                // department
                String code = entry.getValue().get(0).getMaPhongBan();
                String name = entry.getValue().get(0).getTenPhongBan();
                sheet.getRow(8).getCell(0).setCellValue("Tên đơn vị (phòng ban hoặc người sử dụng): " +
                                (!Strings.isNullOrEmpty(code) ? code : "") + (!Strings.isNullOrEmpty(name) ? " - " + name : ""));
                // set detail
                int startDetail = 13;
                for (int i = 0; i < entry.getValue().size(); i++) {
                    sheet.shiftRows(13, sheet.getLastRowNum(), entry.getValue().size());
                    Row row = sheet.createRow(startDetail);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                    cell1.setCellValue(entry.getValue().get(i).getSoChungTuGhiTang());

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue(entry.getValue().get(i).getNgayChungTuGhiTangString());

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(entry.getValue().get(i).getToolsName());

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                    cell4.setCellValue(entry.getValue().get(i).getUnitName());

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                    if (entry.getValue().get(i).getIncrementQuantity() != null) {
                        cell5.setCellValue(entry.getValue().get(i).getIncrementQuantity());
                    }

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (entry.getValue().get(i).getUnitPrice() != null) {
                        cell6.setCellValue(entry.getValue().get(i).getUnitPrice().doubleValue());
                    }

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (entry.getValue().get(i).getIncrementAmount() != null) {
                        cell7.setCellValue(entry.getValue().get(i).getIncrementAmount().doubleValue());
                    }

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.LEFT);
                    cell8.setCellValue(entry.getValue().get(i).getSoChungTuGhiGiam());

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.CENTER);
                    cell9.setCellValue(entry.getValue().get(i).getNgayChungTuGhiGiamString());

                    Cell cell10 = row.createCell(9);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.LEFT);
                    cell10.setCellValue(entry.getValue().get(i).getReason());

                    Cell cell11 = row.createCell(10);
                    cell11.setCellStyle(style);
                    CellUtil.setAlignment(cell11, HorizontalAlignment.RIGHT);
                    if (entry.getValue().get(i).getDecrementQuantity() != null) {
                        cell11.setCellValue(entry.getValue().get(i).getDecrementQuantity());
                    }
                    Cell cell12 = row.createCell(11);
                    cell12.setCellStyle(style);
                    CellUtil.setAlignment(cell12, HorizontalAlignment.RIGHT);
                    cell12.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    if (entry.getValue().get(i).getDecrementAmount() != null) {
                        cell12.setCellValue(entry.getValue().get(i).getDecrementAmount().doubleValue());
                    }

                    Cell cell13 = row.createCell(12);
                    cell13.setCellStyle(style);
                    CellUtil.setAlignment(cell13, HorizontalAlignment.LEFT);

                    // set footer
                    SetFooterAdditionalExcel(workbook, sheet, userDTO, (14 + entry.getValue().size()), 11, 1, 6, 11, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
                    startDetail++;
                }
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println("Lỗi: " + ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelChiTietCongNoPhaiTra(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        double totalAllCreditAmount = 0;
        double totalAllDebitAmount = 0;
        double totalAllClosingCreditAmount = 0;
        double totalAllClosingDebitAmount = 0;
        List<List<ChiTietCongNoPhaiTraDTO>> lstChiTietCongNoPhaiTraDTO = new ArrayList<>();
        List<ChiTietCongNoPhaiTraDTO> child = new ArrayList<>();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<ChiTietCongNoPhaiTraDTO> chiTietCongNoPhaiTraDTOS = reportBusinessRepository.getChiTietCongNoPhaiTra(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        if (chiTietCongNoPhaiTraDTOS.size() == 0) {
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());
            chiTietCongNoPhaiTraDTOS.add(new ChiTietCongNoPhaiTraDTO());
            lstChiTietCongNoPhaiTraDTO.add(chiTietCongNoPhaiTraDTOS);
        } else {
            for (int i = 0; i < chiTietCongNoPhaiTraDTOS.size(); i++) {
                totalAllCreditAmount += chiTietCongNoPhaiTraDTOS.get(i).getCreditAmount().doubleValue();
                totalAllDebitAmount += chiTietCongNoPhaiTraDTOS.get(i).getDebitAmount().doubleValue();
                totalAllClosingCreditAmount += chiTietCongNoPhaiTraDTOS.get(i).getClosingCreditAmount().doubleValue();
                totalAllClosingDebitAmount += chiTietCongNoPhaiTraDTOS.get(i).getClosingDebitAmount().doubleValue();
                child.add(chiTietCongNoPhaiTraDTOS.get(i));
                if ((i < chiTietCongNoPhaiTraDTOS.size() - 1 &&
                    !chiTietCongNoPhaiTraDTOS.get(i).getAccountObjectID().equals(chiTietCongNoPhaiTraDTOS.get(i + 1).getAccountObjectID())) || (i == chiTietCongNoPhaiTraDTOS.size() - 1)) {
                    lstChiTietCongNoPhaiTraDTO.add(child);
                    child = new ArrayList<>();
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(10, sheet.getLastRowNum(), (chiTietCongNoPhaiTraDTOS.size() + (lstChiTietCongNoPhaiTraDTO.size() * 2) - 1));
            // fill dữ liệu vào file
            int i = 10;
            int startRow = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setVerticalAlignment(VerticalAlignment.CENTER);
            // set header
//            Cell cellHeader = sheet.getRow(0).createCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + requestReport.getAccountNumber());
            sheet.getRow(7).getCell(7).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
            int rowCout = 0;
            for (int k = 0; k < lstChiTietCongNoPhaiTraDTO.size(); k++) {
                Row rowGroup1 = sheet.getRow(startRow + rowCout);
                if (rowGroup1 == null) {
                    rowGroup1 = sheet.createRow(startRow + rowCout);
                }
                sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 9));
                rowGroup1.createCell(0);
                rowGroup1.createCell(1);
                rowGroup1.createCell(2);
                if (rowGroup1.getCell(9) == null) {
                    rowGroup1.createCell(9);
                }
                rowGroup1.getCell(9).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellValue("Tên nhà cung cấp: " + (lstChiTietCongNoPhaiTraDTO.get(k).get(0).getAccountObjectName() != null ?
                    lstChiTietCongNoPhaiTraDTO.get(k).get(0).getAccountObjectName() : ""));
                rowCout++;
                double totalCreditAmount = 0;
                double totalDebitAmount = 0;
                double totalClosingCreditAmount = 0;
                double totalClosingDebitAmount = 0;
                for (ChiTietCongNoPhaiTraDTO item : lstChiTietCongNoPhaiTraDTO.get(k)) {
                    if ("SỐ DƯ ĐẦU KỲ".equals(item.getDescription() != null ? item.getDescription().toUpperCase() : "")) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    Row row = sheet.getRow(startRow + rowCout);
                    if (row == null) {
                        row = sheet.createRow(startRow + rowCout);
                    }
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                    cell0.setCellValue(item.getDate());

                    Cell cell1 = row.createCell(1);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getPostedDate());

                    Cell cell2 = row.createCell(2);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getNo());

                    Cell cell3 = row.createCell(3);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getDescription());

                    Cell cell4 = row.createCell(4);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getAccountNumber());

                    Cell cell5 = row.createCell(5);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                    cell5.setCellValue(item.getCorrespondingAccountNumber());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getDebitAmount() != null ? item.getDebitAmount().doubleValue() : 0);

                    Cell cell7 = row.createCell(7);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getCreditAmount() != null ? item.getCreditAmount().doubleValue() : 0);
//
                    Cell cell8 = row.createCell(8);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getClosingDebitAmount() != null ? item.getClosingDebitAmount().doubleValue() : 0);
//
                    Cell cell9 = row.createCell(9);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    cell9.setCellValue(item.getClosingDebitAmount() != null ? item.getClosingCreditAmount().doubleValue() : 0);
//                    i++;
                    rowCout++;
                }
                Row rowGroup2 = sheet.createRow(startRow + rowCout);
                sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 5));
                rowGroup2.createCell(0);
                rowGroup2.createCell(1);
                rowGroup2.createCell(3);
                rowGroup2.createCell(4);
                rowGroup2.createCell(5);
                rowGroup2.getCell(1).setCellStyle(styleB);
                rowGroup2.createCell(2);
                rowGroup2.getCell(2).setCellStyle(styleB);
                rowGroup2.getCell(3).setCellStyle(styleB);
                rowGroup2.getCell(4).setCellStyle(styleB);
                rowGroup2.getCell(5).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellValue("Cộng nhóm: " + (lstChiTietCongNoPhaiTraDTO.get(k).get(0).getAccountObjectName() != null ?
                    lstChiTietCongNoPhaiTraDTO.get(k).get(0).getAccountObjectName() : ""));

                Cell cell6 = rowGroup2.createCell(6);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(totalDebitAmount);

                Cell cell7 = rowGroup2.createCell(7);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(totalCreditAmount);

                Cell cell8 = rowGroup2.createCell(8);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(totalClosingDebitAmount);

                Cell cell9 = rowGroup2.createCell(9);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(totalClosingCreditAmount);
                rowCout++;
            }
            Row rowGroup3 = sheet.createRow(startRow + rowCout);
//            sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 5));
            rowGroup3.createCell(0);
            rowGroup3.createCell(1);
            rowGroup3.createCell(3);
            rowGroup3.createCell(4);
            rowGroup3.createCell(5);
            rowGroup3.getCell(1).setCellStyle(styleB);
            rowGroup3.createCell(2);
            rowGroup3.getCell(2).setCellStyle(styleB);
            rowGroup3.getCell(3).setCellStyle(styleB);
            rowGroup3.getCell(4).setCellStyle(styleB);
            rowGroup3.getCell(5).setCellStyle(styleB);
            rowGroup3.getCell(0).setCellStyle(styleB);
            rowGroup3.getCell(0).setCellValue("Tổng cộng: ");
            CellUtil.setAlignment(rowGroup3.getCell(0), HorizontalAlignment.LEFT);

            Cell cell6 = rowGroup3.createCell(6);
            cell6.setCellStyle(styleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalAllDebitAmount);

            Cell cell7 = rowGroup3.createCell(7);
            cell7.setCellStyle(styleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalAllCreditAmount);

            Cell cell8 = rowGroup3.createCell(8);
            cell8.setCellStyle(styleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalAllClosingDebitAmount);

            Cell cell9 = rowGroup3.createCell(9);
            cell9.setCellStyle(styleB);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            cell9.setCellValue(totalAllClosingCreditAmount);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
//            Row rowFooter = sheet.createRow(13 + rowCout);
//            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter.getCell(0).setCellStyle(styleHeader);
//            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");
//
//            Row rowFooter2 = sheet.createRow(14 + rowCout);
//            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter2.getCell(0).setCellStyle(styleHeader);
//            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
//            SetFooterExcel(workbook, sheet, userDTO, (15 + rowCout), 7, 1, 3, 7);
//            sheet.getRow((16 + rowCout)).getCell(1).setCellValue("Người Lập");
            SetFooterAdditionalExcel(workbook, sheet, userDTO, (12 + rowCout), 7, 1, 3, 7, "Người lập", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelChiTietCongNoPhaiThu(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        double totalAllCreditAmount = 0;
        double totalAllDebitAmount = 0;
        double totalAllClosingCreditAmount = 0;
        double totalAllClosingDebitAmount = 0;
        List<List<ChiTietCongNoPhaiThuDTO>> lstChiTietCongNoPhaiThuDTO = new ArrayList<>();
        List<ChiTietCongNoPhaiThuDTO> child = new ArrayList<>();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<ChiTietCongNoPhaiThuDTO> chiTietNoPhaiThuDTOS = reportBusinessRepository.getChiTietCongNoPhaiThu(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            requestReport.getGroupTheSameItem(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        if (chiTietNoPhaiThuDTOS.size() == 0) {
            chiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());
            chiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());
            chiTietNoPhaiThuDTOS.add(new ChiTietCongNoPhaiThuDTO());
            lstChiTietCongNoPhaiThuDTO.add(chiTietNoPhaiThuDTOS);
        } else {
            for (int i = 0; i < chiTietNoPhaiThuDTOS.size(); i++) {
                totalAllCreditAmount += chiTietNoPhaiThuDTOS.get(i).getCreditAmount().doubleValue();
                totalAllDebitAmount += chiTietNoPhaiThuDTOS.get(i).getDebitAmount().doubleValue();
                totalAllClosingCreditAmount += chiTietNoPhaiThuDTOS.get(i).getClosingCreditAmount().doubleValue();
                totalAllClosingDebitAmount += chiTietNoPhaiThuDTOS.get(i).getClosingDebitAmount().doubleValue();
                child.add(chiTietNoPhaiThuDTOS.get(i));
                if ((i < chiTietNoPhaiThuDTOS.size() - 1 &&
                    !chiTietNoPhaiThuDTOS.get(i).getAccountObjectID().equals(chiTietNoPhaiThuDTOS.get(i + 1).getAccountObjectID())) || (i == chiTietNoPhaiThuDTOS.size() - 1)) {
                    lstChiTietCongNoPhaiThuDTO.add(child);
                    child = new ArrayList<>();
                }
            }
//            Map<String, String> map = new HashMap<String, String>();
//            for (ChiTietCongNoPhaiTraDTO snk : chiTietCongNoPhaiTraDTOS) {
//                if (map.isEmpty()) {
//                    map.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
//                    ChiTietCongNoPhaiTraDTO item = new ChiTietCongNoPhaiTraDTO();
//                    item.setAccountObjectCode(snk.getAccountObjectCode());
//                    chiTietCongNoPhaiTraDTOS.add(item);
//                }
//                if (map.get(snk.getAccountObjectCode()) == null) {
//                    map.put(snk.getAccountObjectCode(), snk.getAccountObjectCode());
//                    ChiTietCongNoPhaiTraDTO dto = new ChiTietCongNoPhaiTraDTO();
//                    dto.setAccountObjectCode(snk.getAccountObjectCode());
//                    chiTietCongNoPhaiTraDTOS.add(dto);
//                }
//            }
        }
//        canDoiKeToanDTOS.remove(0);
//        canDoiKeToanDTOS.remove(63);
        Map<String, String> map = new HashMap<String, String>();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(10, sheet.getLastRowNum(), (chiTietNoPhaiThuDTOS.size() + (lstChiTietCongNoPhaiThuDTO.size() * 2) - 1));
            // fill dữ liệu vào file
            int i = 10;
            int startRow = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setVerticalAlignment(VerticalAlignment.CENTER);
            // set header
//            Cell cellHeader = sheet.getRow(0).createCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + requestReport.getAccountNumber());
            sheet.getRow(7).getCell(7).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
            int rowCout = 0;
            for (int k = 0; k < lstChiTietCongNoPhaiThuDTO.size(); k++) {
                Row rowGroup1 = sheet.getRow(startRow + rowCout);
                if (rowGroup1 == null) {
                    rowGroup1 = sheet.createRow(startRow + rowCout);
                }
                sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 9));
                rowGroup1.createCell(0);
                rowGroup1.createCell(1);
                rowGroup1.createCell(2);
                if (rowGroup1.getCell(9) == null) {
                    rowGroup1.createCell(9);
                }
                rowGroup1.getCell(9).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellStyle(styleB);
                rowGroup1.getCell(0).setCellValue("Tên khách hàng: " + (lstChiTietCongNoPhaiThuDTO.get(k).get(0).getAccountObjectName() != null ?
                    lstChiTietCongNoPhaiThuDTO.get(k).get(0).getAccountObjectName() : ""));
                rowCout++;
                double totalCreditAmount = 0;
                double totalDebitAmount = 0;
                double totalClosingCreditAmount = 0;
                double totalClosingDebitAmount = 0;
                for (ChiTietCongNoPhaiThuDTO item : lstChiTietCongNoPhaiThuDTO.get(k)) {
                    if ("SỐ DƯ ĐẦU KỲ".equals(item.getDescription() != null ? item.getDescription().toUpperCase() : "")) {
                        style.setFont(fontB);
                    } else {
                        style.setFont(fontDf);
                    }
                    Row row = sheet.getRow(startRow + rowCout);
                    if (row == null) {
                        row = sheet.createRow(startRow + rowCout);
                    }
//                    Row row = sheet.createRow(i);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style);
                    CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                    cell0.setCellValue(item.getRefDate());

                    Cell cell1 = row.createCell(1);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getPostedDate());

                    Cell cell2 = row.createCell(2);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                    cell2.setCellValue(item.getRefNo());

                    Cell cell3 = row.createCell(3);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                    cell3.setCellValue(item.getDescription());

                    Cell cell4 = row.createCell(4);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getAccountNumber());

                    Cell cell5 = row.createCell(5);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
                    cell5.setCellValue(item.getCorrespondingAccountNumber());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    cell6.setCellValue(item.getDebitAmount() != null ? item.getDebitAmount().doubleValue() : 0);

                    Cell cell7 = row.createCell(7);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    cell7.setCellValue(item.getCreditAmount() != null ? item.getCreditAmount().doubleValue() : 0);
//
                    Cell cell8 = row.createCell(8);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    cell8.setCellValue(item.getCreditAmount() != null ? item.getClosingDebitAmount().doubleValue() : 0);
//
                    Cell cell9 = row.createCell(9);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    cell9.setCellValue(item.getClosingCreditAmount() != null ? item.getClosingCreditAmount().doubleValue() : 0);
                    i++;
                    totalCreditAmount += item.getCreditAmount().doubleValue();
                    totalDebitAmount += item.getDebitAmount().doubleValue();
                    totalClosingCreditAmount += item.getClosingCreditAmount().doubleValue();
                    totalClosingDebitAmount += item.getClosingDebitAmount().doubleValue();
                    rowCout++;
                }
                Row rowGroup2 = sheet.createRow(startRow + rowCout);
                sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 5));
                rowGroup2.createCell(0);
                rowGroup2.createCell(1);
                rowGroup2.createCell(3);
                rowGroup2.createCell(4);
                rowGroup2.createCell(5);
                rowGroup2.getCell(1).setCellStyle(styleB);
                rowGroup2.createCell(2);
                rowGroup2.getCell(2).setCellStyle(styleB);
                rowGroup2.getCell(3).setCellStyle(styleB);
                rowGroup2.getCell(4).setCellStyle(styleB);
                rowGroup2.getCell(5).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellStyle(styleB);
                rowGroup2.getCell(0).setCellValue("Cộng nhóm: " + (lstChiTietCongNoPhaiThuDTO.get(k).get(0).getAccountObjectName() != null ?
                    lstChiTietCongNoPhaiThuDTO.get(k).get(0).getAccountObjectName() : ""));

                Cell cell6 = rowGroup2.createCell(6);
                cell6.setCellStyle(styleB);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(totalDebitAmount);

                Cell cell7 = rowGroup2.createCell(7);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(totalCreditAmount);

                Cell cell8 = rowGroup2.createCell(8);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(totalClosingDebitAmount);

                Cell cell9 = rowGroup2.createCell(9);
                cell9.setCellStyle(styleB);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(totalClosingCreditAmount);
                rowCout++;
            }
            // tổng cộng
            Row rowGroup3 = sheet.createRow(startRow + rowCout);
//            sheet.addMergedRegion(new CellRangeAddress(startRow + rowCout, startRow + rowCout, 0, 5));
            rowGroup3.createCell(0);
            rowGroup3.createCell(1);
            rowGroup3.createCell(3);
            rowGroup3.createCell(4);
            rowGroup3.createCell(5);
            rowGroup3.getCell(1).setCellStyle(styleB);
            rowGroup3.createCell(2);
            rowGroup3.getCell(2).setCellStyle(styleB);
            rowGroup3.getCell(3).setCellStyle(styleB);
            rowGroup3.getCell(4).setCellStyle(styleB);
            rowGroup3.getCell(5).setCellStyle(styleB);
            rowGroup3.getCell(0).setCellStyle(styleB);
            rowGroup3.getCell(0).setCellValue("Tổng cộng: ");
            CellUtil.setAlignment(rowGroup3.getCell(0), HorizontalAlignment.LEFT);

            Cell cell6 = rowGroup3.createCell(6);
            cell6.setCellStyle(styleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalAllDebitAmount);

            Cell cell7 = rowGroup3.createCell(7);
            cell7.setCellStyle(styleB);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalAllCreditAmount);

            Cell cell8 = rowGroup3.createCell(8);
            cell8.setCellStyle(styleB);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalAllClosingDebitAmount);

            Cell cell9 = rowGroup3.createCell(9);
            cell9.setCellStyle(styleB);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            cell9.setCellValue(totalAllClosingCreditAmount);
//            Row rowFooter = sheet.createRow(12 + rowCout);
//            style.setBorderBottom(BorderStyle.THIN);
//            style.setBorderTop(BorderStyle.THIN);
//            style.setBorderRight(BorderStyle.THIN);
//            style.setBorderLeft(BorderStyle.THIN);
//            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter.getCell(0).setCellStyle(styleHeader);
//            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");
//
//            Row rowFooter2 = sheet.createRow(13 + rowCout);
//            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter2.getCell(0).setCellStyle(styleHeader);
//            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
//            SetFooterExcel(workbook, sheet, userDTO, (14 + rowCout), 6, 1, 3, 7);
//            sheet.getRow((15 + rowCout)).getCell(1).setCellValue("Người Lập");
            SetFooterAdditionalExcel(workbook, sheet, userDTO, (12 + rowCout), 6, 1, 3, 7, "Người lập", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelPhanBoChiPhiTraTruoc(RequestReport requestReport, String path) throws JRException {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<PhanBoChiPhiTraTruocDTO> phanBoChiPhiTraTruocDTOS = reportBusinessRepository.getPhanBoChiPhiTraTruoc(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            Constants.TypeLedger.MANAGEMENT_BOOK.equals(phienSoLamViec),
            requestReport.getDependent());
        if (phanBoChiPhiTraTruocDTOS.size() == 0) {
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
            phanBoChiPhiTraTruocDTOS.add(new PhanBoChiPhiTraTruocDTO());
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(9, sheet.getLastRowNum(), phanBoChiPhiTraTruocDTOS.size());
            // fill dữ liệu vào file
            int i = 9;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFont(fontDf);
            // cell hetader
//            Cell cellHeader = sheet.getRow(0).getCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
//            CellStyle styleHeader = workbook.createCellStyle();
//            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalAllocationTime = BigDecimal.ZERO;
            BigDecimal totalAllocatedPeriodRest = BigDecimal.ZERO;
            BigDecimal totalAccumulated = BigDecimal.ZERO;
            BigDecimal totalAmountRest = BigDecimal.ZERO;
            BigDecimal totalAllocatedPeriodAfter = BigDecimal.ZERO;
            BigDecimal totalAllocatedAmount = BigDecimal.ZERO;
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            for (PhanBoChiPhiTraTruocDTO item : phanBoChiPhiTraTruocDTOS) {
//                if (Boolean.TRUE.equals(item.getBold())) {
//                    style.setFont(fontB);
//                } else {
//                    style.setFont(fontDf);
//                }
                Row row = sheet.createRow(i);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
                cell0.setCellValue(item.getPrepaidExpenseCode());

                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(item.getPrepaidExpenseName());

                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getDate());

                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue(item.getAmount() != null ? item.getAmount().doubleValue() : 0);
                totalAmount = totalAmount.add(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);

                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(item.getAllocationTime() != null ? item.getAllocationTime().doubleValue() : 0);
                totalAllocationTime = totalAllocationTime.add(item.getAllocationTime() != null ? item.getAllocationTime() : BigDecimal.ZERO);

                Cell cell5 = row.createCell(5);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(item.getAllocatedAmount() != null ? item.getAllocatedAmount().doubleValue() : 0);
                totalAllocatedAmount = totalAllocatedAmount.add(item.getAllocatedAmount() != null ? item.getAllocatedAmount() : BigDecimal.ZERO);

                Cell cell6 = row.createCell(6);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(item.getAllocatedPeriodAfter() != null ? item.getAllocatedPeriodAfter().doubleValue() : 0);
                totalAllocatedPeriodAfter = totalAllocatedPeriodAfter.add(item.getAllocatedPeriodAfter() != null ? item.getAllocatedPeriodAfter() : BigDecimal.ZERO);

                Cell cell7 = row.createCell(7);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(item.getAllocatedPeriodRest() != null ? item.getAllocatedPeriodRest().doubleValue() : 0);
                totalAllocatedPeriodRest = totalAllocatedPeriodRest.add(item.getAllocatedPeriodRest() != null ? item.getAllocatedPeriodRest() : BigDecimal.ZERO);

                Cell cell8 = row.createCell(8);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getAccumulated() != null ? item.getAccumulated().doubleValue() : 0);
                totalAccumulated = totalAccumulated.add(item.getAccumulated() != null ? item.getAccumulated() : BigDecimal.ZERO);

                Cell cell9 = row.createCell(9);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.setCellValue(item.getAmountRest() != null ? item.getAmountRest().doubleValue() : 0);
                totalAmountRest =  totalAmountRest.add(item.getAmountRest() != null ? item.getAmountRest() : BigDecimal.ZERO);
                i++;
            }
            Row row = sheet.createRow(i);
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            CellStyle style1 = style;
            style1.setFont(fontB);
            cell0.setCellStyle(style1);
            cell1.setCellStyle(style1);
            cell2.setCellStyle(style1);
            CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
            cell0.setCellValue("Tổng cộng");

            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.setCellValue(totalAmount != null ? totalAmount.doubleValue() : 0);

            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.setCellValue(totalAllocationTime != null ? totalAllocationTime.doubleValue() : 0);

            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.setCellValue(totalAllocatedAmount != null ? totalAllocatedAmount.doubleValue() : 0);

            Cell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalAllocatedPeriodAfter != null ? totalAllocatedPeriodAfter.doubleValue() : 0);

            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalAllocatedPeriodRest != null ? totalAllocatedPeriodRest.doubleValue() : 0);

            Cell cell8 = row.createCell(8);
            cell8.setCellStyle(style);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalAccumulated != null ? totalAccumulated.doubleValue() : 0);

            Cell cell9 = row.createCell(9);
            cell9.setCellStyle(style);
            CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
            cell9.setCellValue(totalAmountRest.doubleValue());
//            SetFooterExcel(workbook, sheet, userDTO, (11 + phanBoChiPhiTraTruocDTOS.size()), 7, 0, 3, 7);
            SetFooterAdditionalExcel(workbook, sheet, userDTO, (11 + phanBoChiPhiTraTruocDTOS.size()), 7, 1, 4, 7, "Người lập biểu", "Kế toán trưởng", "Giám đốc");

            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelTongHopCongNoPhaiTra(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(userDTO);
        String materialGoods = ",";
        for (UUID item : requestReport.getListMaterialGoods()) {
            materialGoods += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<TongHopCongNoPhaiTraDTO> tongHopCongNoPhaiTraDTOS = reportBusinessRepository.getTongHopCongNoPhaiTra(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCurrencyID(),
            requestReport.getAccountNumber(),
            materialGoods,
            Constants.TypeLedger.FINANCIAL_BOOK.equals(phienSoLamViec),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent());
        if (tongHopCongNoPhaiTraDTOS.size() == 0) {
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());
            tongHopCongNoPhaiTraDTOS.add(new TongHopCongNoPhaiTraDTO());
        }
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        BigDecimal totalOpenningCreditAmount = BigDecimal.ZERO;
        BigDecimal totalOpenningDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCloseCreditAmount = BigDecimal.ZERO;
        BigDecimal totalCloseDebitAmount = BigDecimal.ZERO;
//        canDoiKeToanDTOS.remove(0);
//        canDoiKeToanDTOS.remove(63);
//        for (TongHopCongNoPhaiTraDTO snk : tongHopCongNoPhaiTraDTOS) {
////            snk.setItemCode(snk.getItemCode() == null ? "" : snk.getItemCode());
////            snk.setDescription(snk.getDescription() == null ? "" : snk.getDescription());
////            snk.setAmountString(BigDecimal.ZERO.compareTo(snk.getAmount()) == 0 ? "" : Utils.formatTien(snk.getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
////            snk.setPrevAmountString(BigDecimal.ZERO.compareTo(snk.getPrevAmount()) == 0 ? "" : Utils.formatTien(snk.getPrevAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(11, sheet.getLastRowNum(), tongHopCongNoPhaiTraDTOS.size());
            // fill dữ liệu vào file
            int i = 10;
            int startRow = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            fontDf.setBold(false);
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFont(fontDf);
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
//            Cell cellHeader = sheet.getRow(0).createCell(0);
//            CellUtil.setVerticalAlignment(cellHeader, VerticalAlignment.CENTER);
            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setWrapText(true);
//            cellHeader.setCellValue(setHeaderExcel(userDTO));
//            cellHeader.setCellStyle(styleHeader);
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
            sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + requestReport.getAccountNumber());
            sheet.getRow(7).getCell(7).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
            for (TongHopCongNoPhaiTraDTO item : tongHopCongNoPhaiTraDTOS) {
//                if (Boolean.TRUE.equals(item.getBold())) {
//                    style.setFont(fontB);
//                } else {
//                    style.setFont(fontDf);
//                }
                Row row = sheet.createRow(i);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(style);
                CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
                cell0.setCellValue(item.getAccountObjectCode());

                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue(item.getAccountObjectName());

                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                cell2.setCellValue(item.getAccountNumber());

                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                cell3.setCellValue(item.getOpeningDebitAmount() != null ? item.getOpeningDebitAmount().doubleValue() : 0);

                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.setCellValue(item.getOpeningCreditAmount() != null ? item.getOpeningCreditAmount().doubleValue() : 0);

                Cell cell5 = row.createCell(5);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.setCellValue(item.getDebitAmount() != null ? item.getDebitAmount().doubleValue() : 0);

                Cell cell6 = row.createCell(6);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.setCellValue(item.getCreditAmount() != null ? item.getCreditAmount().doubleValue() : 0);

                Cell cell7 = row.createCell(7);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.setCellValue(item.getCloseDebitAmount() != null ? item.getCloseDebitAmount().doubleValue() : 0);

                Cell cell8 = row.createCell(8);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.setCellValue(item.getCloseCreditAmount() != null ? item.getCloseCreditAmount().doubleValue() : 0);

                if (item.getOpeningCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getOpeningCreditAmount()) != 0) {
//                    item.setOpeningCreditAmount(Utils.formatTien(item.getOpeningCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningCreditAmount = totalOpenningCreditAmount.add(item.getOpeningCreditAmount());
                } else {
                    item.setOpeningCreditAmountToString(null);
                }
                if (item.getOpeningDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getOpeningDebitAmount()) != 0) {
//                    item.setOpenningDebitAmountToString(Utils.formatTien(item.getOpenningDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalOpenningDebitAmount = totalOpenningDebitAmount.add(item.getOpeningDebitAmount());
                }
//                else {
//                    item.setOpenningDebitAmountToString(null);
//                }
                if (item.getDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getDebitAmount()) != 0) {
//                    item.setDebitAmountToString(Utils.formatTien(item.getDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalDebitAmount = totalDebitAmount.add(item.getDebitAmount());
                } else {
                    item.setDebitAmountToString(null);
                }
                if (item.getCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getCreditAmount()) != 0) {
//                    item.setCreditAmountToString(Utils.formatTien(item.getCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCreditAmount = totalCreditAmount.add(item.getCreditAmount());
                } else {
                    item.setCreditAmountToString(null);
                }
                if (item.getCloseCreditAmount() != null && BigDecimal.ZERO.compareTo(item.getCloseCreditAmount()) != 0) {
//                    item.setCloseCreditAmountToString(Utils.formatTien(item.getCloseCreditAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseCreditAmount = totalCloseCreditAmount.add(item.getCloseCreditAmount());
                } else {
                    item.setCloseCreditAmountToString(null);
                }
                if (item.getCloseDebitAmount() != null && BigDecimal.ZERO.compareTo(item.getCloseDebitAmount()) != 0) {
//                    item.setCloseDebitAmountToString(Utils.formatTien(item.getCloseDebitAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                    totalCloseDebitAmount = totalCloseDebitAmount.add(item.getCloseDebitAmount());
                } else {
                    item.setCloseDebitAmountToString(null);
                }
                i++;
            }
            Row row = sheet.createRow(startRow + tongHopCongNoPhaiTraDTOS.size());
            sheet.addMergedRegion(new CellRangeAddress(startRow + tongHopCongNoPhaiTraDTOS.size(), startRow + tongHopCongNoPhaiTraDTOS.size(), 0, 2));
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            CellStyle style1 = style;
            style1.setFont(fontB);
            cell0.setCellStyle(style1);
            cell1.setCellStyle(style1);
            cell2.setCellStyle(style1);
            CellUtil.setAlignment(cell0, HorizontalAlignment.LEFT);
            cell0.setCellValue("Tổng cộng");

            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.setCellValue(totalOpenningDebitAmount != null ? totalOpenningDebitAmount.doubleValue() : BigDecimal.ZERO.doubleValue());

            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.setCellValue(totalOpenningCreditAmount != null ? totalOpenningCreditAmount.doubleValue() : 0);

            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.setCellValue(totalDebitAmount != null ? totalDebitAmount.doubleValue() : 0);

            Cell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.setCellValue(totalCreditAmount != null ? totalCreditAmount.doubleValue() : 0);

            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
            cell7.setCellValue(totalCloseDebitAmount != null ? totalCloseDebitAmount.doubleValue() : 0);

            Cell cell8 = row.createCell(8);
            cell8.setCellStyle(style);
            CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
            cell8.setCellValue(totalCloseCreditAmount != null ? totalCloseCreditAmount.doubleValue() : 0);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
//            Row rowFooter = sheet.createRow(startRow + 2 + tongHopCongNoPhaiTraDTOS.size());
//            CellUtil.setAlignment(rowFooter.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter.getCell(0).setCellStyle(styleHeader);
//            rowFooter.getCell(0).setCellValue("- Sổ này có ... trang, đánh số từ trang 01 đến trang số ...");
//
//            Row rowFooter2 = sheet.createRow(startRow + 3 + tongHopCongNoPhaiTraDTOS.size());
//            CellUtil.setAlignment(rowFooter2.createCell(0), HorizontalAlignment.LEFT);
//            rowFooter2.getCell(0).setCellStyle(styleHeader);
//            rowFooter2.getCell(0).setCellValue("- Ngày mở sổ: ...........................");
//            SetFooterExcel(workbook, sheet, userDTO, (startRow + 4 + tongHopCongNoPhaiTraDTOS.size()), 7, 1, 4, 7);
//            sheet.getRow((startRow + 5 + tongHopCongNoPhaiTraDTOS.size())).getCell(1).setCellValue("Người Lập");
//            SetFooterExcel(workbook, sheet, userDTO, (startRow + 2 + tongHopCongNoPhaiTraDTOS.size()), 7, 1, 3, 7);
            SetFooterAdditionalExcel(workbook, sheet, userDTO, (startRow + 2 + tongHopCongNoPhaiTraDTOS.size()), 7, 1, 4, 7, "Người lập", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }


    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getTheTinhGiaThanh(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "The_Tinh_Gia_Thanh";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String listMaterialGoodsID = ",";
        String listCostSetID = ",";
        String header4 = "";
        CPPeriodDTO cpPeriod = cpPeriodRepository.getByID(requestReport.getcPPeriodID());
        if (cpPeriod != null) {
            header4 += "Từ ngày " + convertDate(cpPeriod.getFromDate()) + " đên ngày " + convertDate(cpPeriod.getToDate());
        }
        if (requestReport.getListMaterialGoods() != null && requestReport.getListMaterialGoods().size() > 0) {
            for (int i = 0; i < requestReport.getListMaterialGoods().size(); i++) {
                listMaterialGoodsID += Common.revertUUID(requestReport.getListMaterialGoods().get(i)) + ",";
            }
        }
        for (int i = 0; i < requestReport.getListCostSets().size(); i++) {
            listCostSetID += Common.revertUUID(requestReport.getListCostSets().get(i)) + ",";
        }
        checkEbPackage(userDTO, parameter);
        JasperReport jasperReport;
        List<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTOS = reportBusinessRepository.getTheTinhGiaThanh(
            requestReport.getCompanyID(), listMaterialGoodsID, listCostSetID, requestReport.getcPPeriodID(), requestReport.getTypeMethod(), requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        ;
//        String CurrencyType = "";
//        if (requestReport.getCurrencyID() != null) {
//            CurrencyType = "Loại tiền: " + requestReport.getCurrencyID() + "; ";
//        }
//        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
//        header4 += "Tài khoản: " + requestReport.getAccountNumber() + ";";
//        header4 += CurrencyType;
//        header4 += Period;
        parameter.put("header4", header4);
        String mainCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        List<Type> types = typeRepository.findAllByIsActive();
        if (soTheTinhGiaThanhDTOS.size() > 0) {
            for (int i = 0; i < soTheTinhGiaThanhDTOS.size(); i++) {
                int value = i;
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmount() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmount().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountNLVLToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountNCTTToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountCPSDMTCToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountCPSXCToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
            }
        } else {
            if (requestReport.getTypeMethod() == 0 || requestReport.getTypeMethod() == 1 || requestReport.getTypeMethod() == 2) {
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "1. Chi phí SXKD dở dang đầu kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "2. Chi phí SXKD phát sinh trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "3. Giá thành sản phẩm, dịch vụ trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "4. Chi phí SXKD dở dang cuối kỳ ", null, null, null, null, null, 0));
            }
            else{
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "1. Lũy kế chi phí phát sinh kỳ trước", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "2. Chi phí SXKD phát sinh trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "3. Tổng chi phí", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "4. Số nghiệp thu trong kỳ ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "5. Số chưa nghiệp thu cuối kỳ", null, null, null, null, null, 0));
            }
        }
        parameter.put("REPORT_MAX_COUNT", soTheTinhGiaThanhDTOS.size());
        parameter.put("typeMethod", requestReport.getTypeMethod());

        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soTheTinhGiaThanhDTOS.size() > 0) {
            result = ReportUtils.generateReportPDF(soTheTinhGiaThanhDTOS, parameter, jasperReport);
        } else {
            return JasperRunManager.runReportToPdf(jasperReport, parameter, new JREmptyDataSource());
        }
        return result;
    }

    private void checkEbPackage(UserDTO userDTO, Map<String, Object> parameter) {
        File currentDirectory = new File(new File("").getAbsolutePath());
        if (userDTO.getEbPackage() != null) {
            Boolean isCheckDemo = userDTO.getEbPackage().getPackageCode() != null && userDTO.getEbPackage().getPackageCode().contains("DEMO");
            if (Boolean.TRUE.equals(isCheckDemo)) {
                parameter.put("pathDemo", currentDirectory + "/report/demo.png");
                parameter.put("isCheckDemo", true);
            } else {
                parameter.put("isCheckDemo", false);
            }
        } else {
            parameter.put("pathDemo", currentDirectory + "/report/demo.png");
            parameter.put("isCheckDemo", true);
        }
    }


    /**
     * @param requestReport
     * @return
     * @throws JRException
     * @Author Namnh
     */
    byte[] getTongHopCPTheoKMCP(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "Tong_Hop_Chi_Phi_Theo_KMCP";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String listAccountNumber = "";
        String listExpenseItem = "";
        List<AccountList> accountLists = accountListRepository.findAllByListAccountNumberAndOrg(requestReport.getAccountList(), currentUserLoginAndOrg.get().getOrgGetData());
        accountLists = getAllChildAccount(accountLists, currentUserLoginAndOrg.get().getOrgGetData());
        List<ExpenseItem> expenseItems = expenseItemRepository.findAllByListIDAndOrg(requestReport.getListExpenseItems(), currentUserLoginAndOrg.get().getOrg());
        expenseItems = getAllChildExpenseItems(expenseItems, requestReport.getCompanyID());
        for (int i = 0; i < accountLists.size(); i++) {
            listAccountNumber += "," + accountLists.get(i).getAccountNumber();
        }
        for (int i = 0; i < expenseItems.size(); i++) {
            listExpenseItem += "," + Common.revertUUID(expenseItems.get(i).getId());
        }
        listAccountNumber += ",";
        listExpenseItem += ",";
        List<TongHopCPTheoKMCPDTO> tongHopCPTheoKMCPDTOS = reportBusinessRepository.getTongHopCPTheoKMCP(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), phienSoLamViec, listAccountNumber, listExpenseItem, requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());;
        checkEbPackage(userDTO, parameter);
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = Period;
        BigDecimal tongSoTien = BigDecimal.ZERO;
        parameter.put("header4", header4);
        Integer soThuTu = 0;
        if (tongHopCPTheoKMCPDTOS.size() > 0) {
            for (int i = 0; i < tongHopCPTheoKMCPDTOS.size(); i++) {
                tongHopCPTheoKMCPDTOS.get(i).setAmountToString((tongHopCPTheoKMCPDTOS.get(i).getAmount() != null && tongHopCPTheoKMCPDTOS.get(i).getAmount().compareTo(BigDecimal.ZERO) != 0) ? Utils.formatTien(tongHopCPTheoKMCPDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO) : null);
                for (TongHopCPTheoKMCPDTO item : tongHopCPTheoKMCPDTOS) {
                    if (item.getAccount() != null && tongHopCPTheoKMCPDTOS.get(i).getAccount().equals(item.getAccount())) {
                        tongSoTien = tongSoTien.add(item.getAmount());
                        if (item.getStt() != null && item.getStt() > soThuTu) {
                            soThuTu = item.getStt();
                        }
                    }
                }
                tongHopCPTheoKMCPDTOS.get(i).setStt(soThuTu + 1);
                tongHopCPTheoKMCPDTOS.get(i).setAmountSumToString((tongSoTien != null && tongSoTien.compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                tongSoTien = BigDecimal.ZERO;
                soThuTu = 0;
            }
        } else {
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
        }
        parameter.put("REPORT_MAX_COUNT", tongHopCPTheoKMCPDTOS.size());
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Director", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("ChiefAccountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            parameter.put("IsDisplayNameInReport", true);
        } else {
            parameter.put("IsDisplayNameInReport", false);
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
            parameter.put("Reporter", userDTO.getFullName());
        } else {
            parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(tongHopCPTheoKMCPDTOS, parameter, jasperReport);
        return result;
    }

    byte[] getExcelTongHopCPTheoKMCP(RequestReport requestReport, String path) throws JRException {
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String listAccountNumber = "";
        String listExpenseItem = "";
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> accountLists = accountListRepository.findAllByListAccountNumberAndOrg(requestReport.getAccountList(), currentUserLoginAndOrg.get().getOrgGetData());
        accountLists = getAllChildAccount(accountLists, currentUserLoginAndOrg.get().getOrgGetData());
        List<ExpenseItem> expenseItems = expenseItemRepository.findAllByListIDAndOrg(requestReport.getListExpenseItems(), currentUserLoginAndOrg.get().getOrg());
        expenseItems = getAllChildExpenseItems(expenseItems, requestReport.getCompanyID());
        for (int i = 0; i < accountLists.size(); i++) {
            listAccountNumber += "," + accountLists.get(i).getAccountNumber();
        }
        for (int i = 0; i < expenseItems.size(); i++) {
            listExpenseItem += "," + Common.revertUUID(expenseItems.get(i).getId());
        }
        listAccountNumber += ",";
        listExpenseItem += ",";
        List<TongHopCPTheoKMCPDTO> tongHopCPTheoKMCPDTOS = reportBusinessRepository.getTongHopCPTheoKMCP(
            requestReport.getCompanyID(),
            requestReport.getFromDate(),
            requestReport.getToDate(), phienSoLamViec, listAccountNumber, listExpenseItem, requestReport.getDependent());
        /*Header*/
        checkEbPackage(userDTO, parameter);
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = Period;
        Integer soThuTu = 0;
        BigDecimal tongSoTien = BigDecimal.ZERO;
        List<String> listAccount = new ArrayList<>();
        if (tongHopCPTheoKMCPDTOS.size() == 0) {
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
        } else {
            for (int i = 0; i < tongHopCPTheoKMCPDTOS.size(); i++) {
                int value = i;
                tongHopCPTheoKMCPDTOS.get(i).setAmountToString(Utils.formatTien(tongHopCPTheoKMCPDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
                for (TongHopCPTheoKMCPDTO item : tongHopCPTheoKMCPDTOS) {
                    if (item.getAccount() != null && tongHopCPTheoKMCPDTOS.get(i).getAccount().equals(item.getAccount())) {
                        tongSoTien = tongSoTien.add(item.getAmount());
                        if (item.getStt() != null && item.getStt() > soThuTu) {
                            soThuTu = item.getStt();
                        }
                    }
                }
                Optional<String> account = listAccount.stream().filter(a -> a.equals(tongHopCPTheoKMCPDTOS.get(value).getAccount())).findFirst();
                if (!account.isPresent()) {
                    listAccount.add(tongHopCPTheoKMCPDTOS.get(value).getAccount());
                }
                tongHopCPTheoKMCPDTOS.get(i).setStt(soThuTu + 1);
                tongHopCPTheoKMCPDTOS.get(i).setAmountSumToString(Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO));
                tongHopCPTheoKMCPDTOS.get(i).setAmountSumToString(Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO));
                tongSoTien = BigDecimal.ZERO;
                soThuTu = 0;
            }
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);

            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleMoney = workbook.createCellStyle();
            styleMoney.setFont(fontB);
            styleMoney.setBorderBottom(BorderStyle.THIN);
            styleMoney.setBorderTop(BorderStyle.THIN);
            styleMoney.setBorderRight(BorderStyle.THIN);
            styleMoney.setBorderLeft(BorderStyle.THIN);
            styleMoney.setWrapText(true);
            styleMoney.setAlignment(HorizontalAlignment.RIGHT);
            styleMoney.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleMoney.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontItalic = workbook.createFont();
            fontItalic.setItalic(true);
            fontItalic.setFontName("Times New Roman");
            CellStyle styleFooterItalic = workbook.createCellStyle();
            styleFooterItalic.setFont(fontItalic);
            styleFooterItalic.setAlignment(HorizontalAlignment.CENTER);

            Font fontBold = workbook.createFont();
            fontBold.setBold(true);
            fontBold.setFontName("Times New Roman");
            CellStyle styleFooterBold = workbook.createCellStyle();
            styleFooterBold.setFont(fontBold);
            styleFooterBold.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 1; i < listAccount.size(); i++) {
                workbook.cloneSheet(0);

            }
            if (listAccount.size() > 0) {
                for (int i = 0; i < listAccount.size(); i++) {
                    int value = i;
                    BigDecimal total = BigDecimal.ZERO;
                    List<TongHopCPTheoKMCPDTO> tongHopCPTheoKMCPDTOS1 = tongHopCPTheoKMCPDTOS.stream().filter(a -> a.getAccount().equals(listAccount.get(value))).collect(Collectors.toList());
                    Sheet sheet = workbook.getSheetAt(i);
                    if (tongHopCPTheoKMCPDTOS1.size() > 0) {
                        sheet.shiftRows(10, sheet.getLastRowNum(), tongHopCPTheoKMCPDTOS1.size());
                    }
                    // fill dữ liệu vào file
                    int start = 9;
                    // set header
                    SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                    sheet.getRow(5).getCell(0).setCellValue(header4);
                    sheet.getRow(7).getCell(1).setCellValue("Tài khoản: " + listAccount.get(i));
                    //

                    for (TongHopCPTheoKMCPDTO item : tongHopCPTheoKMCPDTOS1) {
                        Row row = sheet.createRow(start);
                        Cell cell1 = row.createCell(0);
                        cell1.setCellStyle(style);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                        cell1.setCellValue(item.getStt());

                        Cell cell2 = row.createCell(1);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                        cell2.setCellValue(item.getExpenseItemCode());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                        cell3.setCellValue(item.getExpenseItemName());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                        cell4.setCellValue(item.getAmount() != null ? item.getAmount().doubleValue() : 0);
                        total = total.add(item.getAmount());
                        start++;
                    }
                    Row rowSum = sheet.createRow(9 + tongHopCPTheoKMCPDTOS1.size());
                    rowSum.createCell(0);
                    rowSum.getCell(0).setCellStyle(styleB);
                    rowSum.getCell(0).setCellValue("Tổng cộng");
                    rowSum.createCell(1);
                    rowSum.getCell(1).setCellStyle(styleB);
                    rowSum.createCell(2);
                    rowSum.getCell(2).setCellStyle(styleB);
                    rowSum.createCell(3);
                    rowSum.getCell(3).setCellStyle(styleMoney);
                    rowSum.getCell(3).setCellValue(total != null ? total.doubleValue() : 0);
                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size(), 9 + tongHopCPTheoKMCPDTOS1.size(), 0, 2));

                    // tạo dòng người lập
                    Row rowDate = sheet.createRow(9 + tongHopCPTheoKMCPDTOS1.size() + 3);
                    rowDate.createCell(2);
                    rowDate.getCell(2).setCellStyle(styleFooterItalic);
                    rowDate.getCell(2).setCellValue("Ngày ..... tháng ..... năm .......");
                    rowDate.createCell(3);
                    rowDate.getCell(3).setCellStyle(styleFooterItalic);
                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 3, 9 + tongHopCPTheoKMCPDTOS1.size() + 3, 2, 3));

                    // tạo dòng người lập
                    Row rowFooter = sheet.createRow(9 + tongHopCPTheoKMCPDTOS1.size() + 4);
                    rowFooter.createCell(0);
                    rowFooter.getCell(0).setCellStyle(styleFooterBold);
                    rowFooter.getCell(0).setCellValue("Người lập");
                    rowFooter.createCell(1);
                    rowFooter.getCell(1).setCellStyle(styleFooterBold);
                    rowFooter.createCell(2);
                    rowFooter.getCell(2).setCellStyle(styleFooterBold);
                    rowFooter.getCell(2).setCellValue("Kế toán trưởng");
                    rowFooter.createCell(3);
                    rowFooter.getCell(3).setCellStyle(styleFooterBold);
                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 4, 9 + tongHopCPTheoKMCPDTOS1.size() + 4, 0, 1));
                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 4, 9 + tongHopCPTheoKMCPDTOS1.size() + 4, 2, 3));

                    // tạo dòng ký ghi họ ten
                    Row rowFooter2 = sheet.createRow(9 + tongHopCPTheoKMCPDTOS1.size() + 5);
                    rowFooter2.createCell(0);
                    rowFooter2.getCell(0).setCellStyle(styleFooterItalic);
                    rowFooter2.getCell(0).setCellValue("(Ký, họ tên)");
                    rowFooter2.createCell(1);
                    rowFooter2.getCell(1).setCellStyle(styleFooterItalic);
                    rowFooter2.createCell(2);
                    rowFooter2.getCell(2).setCellStyle(styleFooterItalic);
                    rowFooter2.getCell(2).setCellValue("(Ký, họ tên)");
                    rowFooter2.createCell(3);
                    rowFooter2.getCell(3).setCellStyle(styleFooterItalic);
                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 5, 9 + tongHopCPTheoKMCPDTOS1.size() + 5, 0, 1));
//                    sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 5, 9 + tongHopCPTheoKMCPDTOS1.size() + 5, 3, 4));

                    // tạo dòng tên người ký
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                        Row rowFooterValue = sheet.createRow(9 + tongHopCPTheoKMCPDTOS1.size() + 8);
                        rowFooterValue.createCell(1);
                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                        rowFooterValue.createCell(3);
                        rowFooterValue.getCell(3).setCellStyle(styleFooterBold);
                        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                            rowFooterValue.createCell(0).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
                            rowFooterValue.getCell(0).setCellStyle(styleFooterBold);
                        } else {
                            rowFooterValue.createCell(0).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
                            rowFooterValue.getCell(0).setCellStyle(styleFooterBold);
                        }

                        rowFooterValue.createCell(2).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
                        rowFooterValue.getCell(2).setCellStyle(styleFooterBold);
                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 8, 9 + tongHopCPTheoKMCPDTOS1.size() + 8, 0, 1));
                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 8, 9 + tongHopCPTheoKMCPDTOS1.size() + 8, 2, 3));
                    }
                }
            } else {
                Sheet sheet = workbook.getSheetAt(0);
                sheet.getRow(5).getCell(0).setCellValue(header4);
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                // tạo dòng tên người ký
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                    sheet.addMergedRegion(new CellRangeAddress(12, 12, 0, 2));
                    Row rowFooterValue = sheet.createRow(19);
                    rowFooterValue.createCell(1);
                    rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                    rowFooterValue.createCell(3);
                    rowFooterValue.getCell(3).setCellStyle(styleFooterBold);
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                        rowFooterValue.createCell(0).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
                        rowFooterValue.getCell(0).setCellStyle(styleFooterBold);
                    } else {
                        rowFooterValue.createCell(0).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
                        rowFooterValue.getCell(0).setCellStyle(styleFooterBold);
                    }

                    rowFooterValue.createCell(2).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
                    rowFooterValue.getCell(2).setCellStyle(styleFooterBold);
//                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 1, 2));
//                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 3, 4));
                }
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getExcelSoChiPhiSXKD(RequestReport requestReport, String path) throws JRException {
        Map<String, Object> parameter = new HashMap<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);

        List<AccountList> accountLists = new ArrayList<>();
        String listAccountString = ",";
        String listCostSetString = ",";
        AccountList accountList = accountListRepository.findByAccountNumberAndCompanyIDAndIsActiveTrue(requestReport.getAccountNumber(), currentUserLoginAndOrg.get().getOrgGetData());
        if (accountList != null) {
            accountLists.add(accountList);
            accountListService.getListChildAccount(accountLists, accountList.getId());
        }
        if (accountLists.size() > 1) {
            accountLists.remove(accountList);
        }
        for (int i = 0; i < accountLists.size(); i++) {
            listAccountString += accountLists.get(i).getAccountNumber() + ",";
        }
        for (int i = 0; i < requestReport.getListCostSets().size(); i++) {
            listCostSetString += Common.revertUUID(requestReport.getListCostSets().get(i)) + ",";
        }
        List<Map<String,Object>> data = reportBusinessRepository.getSoChiPhiSXKD(requestReport.getCompanyID(), requestReport.getAccountNumber(), requestReport.getFromDate(), requestReport.getToDate(), phienSoLamViec, listAccountString, listCostSetString);
        /*Header*/
        checkEbPackage(userDTO, parameter);
        String Period = Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        String header4 = Period;
        Integer soThuTu = 0;
        BigDecimal tongSoTien = BigDecimal.ZERO;
        Integer DDSo_TienVND = Integer.valueOf(systemOptionRepository.findByCode(currentUserLoginAndOrg.get().getOrg(),Constants.SystemOption.DDSo_TienVND));
        List<String> listAccount = new ArrayList<>();
//        if (data.size() == 0) {
//            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
//            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
//            tongHopCPTheoKMCPDTOS.add(new TongHopCPTheoKMCPDTO(null, null, null, null, BigDecimal.ZERO));
//        } else {
//            for (int i = 0; i < tongHopCPTheoKMCPDTOS.size(); i++) {
//                int value = i;
//                tongHopCPTheoKMCPDTOS.get(i).setAmountToString(Utils.formatTien(tongHopCPTheoKMCPDTOS.get(i).getAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
//                for (TongHopCPTheoKMCPDTO item : tongHopCPTheoKMCPDTOS) {
//                    if (item.getAccount() != null && tongHopCPTheoKMCPDTOS.get(i).getAccount().equals(item.getAccount())) {
//                        tongSoTien = tongSoTien.add(item.getAmount());
//                        if (item.getStt() != null && item.getStt() > soThuTu) {
//                            soThuTu = item.getStt();
//                        }
//                    }
//                }
//                Optional<String> account = listAccount.stream().filter(a -> a.equals(tongHopCPTheoKMCPDTOS.get(value).getAccount())).findFirst();
//                if (!account.isPresent()) {
//                    listAccount.add(tongHopCPTheoKMCPDTOS.get(value).getAccount());
//                }
//                tongHopCPTheoKMCPDTOS.get(i).setStt(soThuTu + 1);
//                tongHopCPTheoKMCPDTOS.get(i).setAmountSumToString(Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO));
//                tongHopCPTheoKMCPDTOS.get(i).setAmountSumToString(Utils.formatTien(tongSoTien, Constants.SystemOption.DDSo_TienVND, userDTO));
//                tongSoTien = BigDecimal.ZERO;
//                soThuTu = 0;
//            }
//        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);

            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleBHeader = workbook.createCellStyle();
            styleBHeader.setFont(fontB);
            styleBHeader.setBorderBottom(BorderStyle.THIN);
            styleBHeader.setBorderTop(BorderStyle.THIN);
            styleBHeader.setBorderRight(BorderStyle.THIN);
            styleBHeader.setBorderLeft(BorderStyle.THIN);
            styleBHeader.setWrapText(true);
            styleBHeader.setAlignment(HorizontalAlignment.CENTER);
            styleBHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleBHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleBoldLeftHeader = workbook.createCellStyle();
            styleBoldLeftHeader.setFont(fontB);
            styleBoldLeftHeader.setWrapText(true);
            styleBoldLeftHeader.setAlignment(HorizontalAlignment.LEFT);
            styleBoldLeftHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleBoldLeftHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleItalicLeftHeader = workbook.createCellStyle();
            styleItalicLeftHeader.setFont(fontI);
            styleItalicLeftHeader.setWrapText(true);
            styleItalicLeftHeader.setAlignment(HorizontalAlignment.LEFT);
            styleItalicLeftHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleItalicLeftHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleMoney = workbook.createCellStyle();
            styleMoney.setFont(fontB);
            styleMoney.setBorderBottom(BorderStyle.THIN);
            styleMoney.setBorderTop(BorderStyle.THIN);
            styleMoney.setBorderRight(BorderStyle.THIN);
            styleMoney.setBorderLeft(BorderStyle.THIN);
            styleMoney.setWrapText(true);
            styleMoney.setAlignment(HorizontalAlignment.RIGHT);
            styleMoney.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleMoney.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle stylePage = workbook.createCellStyle();
            stylePage.setFont(fontDf);
            stylePage.setAlignment(HorizontalAlignment.LEFT);
            stylePage.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            stylePage.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontItalic = workbook.createFont();
            fontItalic.setItalic(true);
            fontItalic.setFontName("Times New Roman");
            CellStyle styleFooterItalic = workbook.createCellStyle();
            styleFooterItalic.setFont(fontItalic);
            styleFooterItalic.setAlignment(HorizontalAlignment.CENTER);
            styleFooterItalic.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleFooterItalic.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleItalicCenterHeader = workbook.createCellStyle();
            styleItalicCenterHeader.setFont(fontItalic);
            styleItalicCenterHeader.setAlignment(HorizontalAlignment.CENTER);
            styleItalicCenterHeader.setWrapText(true);
            styleItalicCenterHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleItalicCenterHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontHeaderCustomSize = workbook.createFont();
            fontHeaderCustomSize.setBold(true);
            fontHeaderCustomSize.setFontName("Times New Roman");
            fontHeaderCustomSize.setFontHeight((short) 280);
            CellStyle styleCustomSize = workbook.createCellStyle();
            styleCustomSize.setFont(fontHeaderCustomSize);
            styleCustomSize.setWrapText(true);
            styleCustomSize.setAlignment(HorizontalAlignment.CENTER);
            styleCustomSize.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleCustomSize.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontBold = workbook.createFont();
            fontBold.setBold(true);
            fontBold.setFontName("Times New Roman");
            CellStyle styleFooterBold = workbook.createCellStyle();
            styleFooterBold.setFont(fontBold);
            styleFooterBold.setAlignment(HorizontalAlignment.CENTER);
            int indexSheet = 1;
            if (requestReport.getListCostSets().size() > 0) {
                for (int i = 0; i < requestReport.getListCostSets().size(); i++) {
                    int value = i;
                    BigDecimal total = BigDecimal.ZERO;
                    List<Map<String, Object>> data1 = data.stream().filter(a -> a.get("CostSetID").equals(Common.revertUUID(requestReport.getListCostSets().get(value)).toString().toUpperCase())).collect(Collectors.toList());
                    if (data1.size() > 0) {
                        workbook.cloneSheet(0);
                        Sheet sheet = workbook.getSheetAt(indexSheet);
                        indexSheet++;
                        sheet.shiftRows(15, sheet.getLastRowNum(), data1.size());
                        // fill dữ liệu vào file
                        int start = 13;
                        // set header
//                        SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                        sheet.getRow(11).getCell(5).setCellValue("Ghi Nợ tài khoản " + requestReport.getAccountNumber());
                        sheet.getRow(11).getCell(5).setCellStyle(styleBHeader);
                        for (int j = 0; j < accountLists.size(); j++) {
                            sheet.getRow(12).getCell(j + 6).setCellValue(accountLists.get(j).getAccountNumber());
                            sheet.getRow(12).getCell(j + 6).setCellStyle(styleBHeader);
                            sheet.getRow(11).getCell(j + 6).setCellStyle(styleBHeader);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(11, 11, 5, accountLists.size() + 5));
//                    sheet.getRow(5).getCell(0).setCellValue(header4);
//                    sheet.getRow(7).getCell(1).setCellValue("Tài khoản: " + listAccount.get(i));
                        Row rowHeader0 = sheet.createRow(0);
                        Row rowHeader1 = sheet.createRow(1);
                        Row rowHeader2 = sheet.createRow(2);
                        Row rowHeader4 = sheet.createRow(4);
                        Row rowHeader5 = sheet.createRow(5);
                        Row rowPeriod = sheet.createRow(6);
                        Row rowInfoAccount = sheet.createRow(7);
                        for (int k = 0; k < accountLists.size() + 6; k++) {
                            rowPeriod.createCell(k);
                            rowInfoAccount.createCell(k);
                            rowHeader4.createCell(k);
                            rowHeader5.createCell(k);
                            rowHeader0.createCell(k);
                            rowHeader1.createCell(k);
                            rowHeader2.createCell(k);
                        }
                        SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                        sheet.getRow(0).getCell(0).setCellStyle(styleBoldLeftHeader);
                        sheet.getRow(1).getCell(0).setCellStyle(styleItalicLeftHeader);
                        sheet.getRow(2).getCell(0).setCellStyle(styleItalicLeftHeader);
                        sheet.getRow(0).getCell(5 + accountLists.size() - 2 ).setCellValue("Mẫu số: S36-DN");
                        sheet.getRow(0).getCell(5 + accountLists.size() - 2 ).setCellStyle(styleFooterBold);
                        sheet.getRow(1).getCell(5 + accountLists.size() - 2 ).setCellStyle(styleItalicCenterHeader);
                        sheet.getRow(1).getCell(5 +accountLists.size() - 2 ).setCellValue("(Ban hành theo thông tư số 200 /2014/TT-BTC Ngày 22/12/2014 của Bộ tài chính)");
                        rowPeriod.getCell(0).setCellValue("Từ ngày: " + convertDate(requestReport.getFromDate()) + " đến ngày " + convertDate(requestReport.getToDate()));
                        rowHeader5.getCell(0).setCellValue("(Dùng cho các TK 621, 622, 623, 627, 154, 631, 641, 642, 242, 335, 632)");
                        rowHeader5.getCell(0).setCellStyle(styleFooterBold);
                        rowHeader4.getCell(0).setCellValue("SỔ CHI PHÍ SẢN XUẤT KINH DOANH");
                        rowHeader4.getCell(0).setCellStyle(styleCustomSize);
                        rowPeriod.getCell(0).setCellStyle(styleFooterItalic);
                        rowInfoAccount.getCell(0).setCellValue("Tài khoản: " + accountList.getAccountNumber() + " - " + accountList.getAccountName());
                        rowInfoAccount.getCell(0).setCellStyle(styleFooterBold);
                        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, accountLists.size() + 5));
                        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, accountLists.size() + 5));
                        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, accountLists.size() + 5));
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, accountLists.size() + 5));
                        sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, accountLists.size() + 5));
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5 + accountLists.size() - 2 , 5 + accountLists.size()));
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
                        sheet.addMergedRegion(new CellRangeAddress(1, 2, 5 + accountLists.size() - 2 , 5 + accountLists.size()));
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
                        //
                        for (Map<String, Object> item : data1) {
                            int index = 6;
                            Row row = sheet.createRow(start);
                            BigDecimal sum = BigDecimal.ZERO;
                            sheet.getRow(10).getCell(0).setCellValue("Mã đối tượng THCP: " + item.get("CostSetCode") + " - Tên đối tượng THCP: " + item.get("CostSetName"));
                            if (item.get("Row").toString().equals("3")) {
                                style.setFont(fontDf);
                            } else {
                                style.setFont(fontB);
                            }
                            for (AccountList item2 : accountLists) {
                                BigDecimal obj1 = item.get(item2.getAccountNumber()) != null ? new BigDecimal(item.get(item2.getAccountNumber()).toString()) : null;
                                Cell cell1 = row.createCell(index);
                                cell1.setCellStyle(style);
                                CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
                                if (obj1 != null) {
                                    cell1.setCellValue(Utils.round(new BigDecimal(String.valueOf(obj1)),DDSo_TienVND).doubleValue());
                                    sum = sum.add(Utils.round(new BigDecimal(String.valueOf(obj1)),DDSo_TienVND));
                                } else {
                                    cell1.setCellValue("");
                                }

                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String obj2 = item.get("PostedDate") != null ? dateFormat.format((Date) item.get("PostedDate")) : null;
                                Cell cell2 = row.createCell(0);
                                cell2.setCellStyle(style);
                                CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                                cell2.setCellValue(obj2);

                                String obj3 = item.get("No") != null ? item.get("No").toString() : "";
                                Cell cell3 = row.createCell(1);
                                cell3.setCellStyle(style);
                                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                                cell3.setCellValue(obj3);

                                String obj4 = item.get("Date") != null ? dateFormat.format((Date) item.get("Date")) : "";
                                Cell cell4 = row.createCell(2);
                                cell4.setCellStyle(style);
                                CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                                cell4.setCellValue(obj4);

                                String obj5 = item.get("Description") != null ? item.get("Description").toString() : "";
                                Cell cell5 = row.createCell(3);
                                cell5.setCellStyle(style);
                                CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                                cell5.setCellValue(obj5);

                                String obj6 = item.get("AccountCorresponding") != null ? item.get("AccountCorresponding").toString() : "";
                                Cell cell6 = row.createCell(4);
                                cell6.setCellStyle(style);
                                CellUtil.setAlignment(cell6, HorizontalAlignment.CENTER);
                                cell6.setCellValue(obj6);

                                Cell cell7 = row.createCell(5);
                                cell7.setCellStyle(style);
                                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                                if (sum != BigDecimal.ZERO) {
                                    cell7.setCellValue(sum.doubleValue());
                                } else {
                                    cell7.setCellValue("");
                                }
                                index++;
                            }
//                        sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, accountLists.size() + 6));
                            start++;
                        }

                        Row rowPage = sheet.createRow(13 + data1.size() + 1);
                        rowPage.createCell(0);
                        rowPage.getCell(0).setCellValue("- Sổ này có 01 trang, đánh số từ trang 01 đến trang số ….......");
                        rowPage.getCell(0).setCellStyle(stylePage);
                        Row rowPage2 = sheet.createRow(13 + data1.size() + 2);
                        rowPage2.createCell(0);
                        rowPage2.getCell(0).setCellValue("- Ngày mở sổ: …...................");
                        rowPage2.getCell(0).setCellStyle(stylePage);


                        Row rowDate = sheet.createRow(13 + data1.size() + 4);
                        rowDate.createCell(5 + accountLists.size() - 1);
                        rowDate.getCell(5 + accountLists.size() - 1).setCellStyle(styleFooterItalic);
                        rowDate.getCell(5 + accountLists.size() - 1).setCellValue("Ngày ..... tháng ..... năm .......");
//                    rowDate.createCell(6 + accountLists.size() - 3);
//                    rowDate.getCell(6 + accountLists.size() - 3).setCellStyle(styleFooterItalic);
//                    rowDate.createCell(6 + accountLists.size() - 2);
//                    rowDate.getCell(6 + accountLists.size() - 2).setCellStyle(styleFooterItalic);
//                    rowDate.createCell(6 + accountLists.size() - 1);
//                    rowDate.getCell(6 + accountLists.size() - 1).setCellStyle(styleFooterItalic);
//                    sheet.addMergedRegion(new CellRangeAddress(13 + data1.size() + 4, 13 + data1.size() + 4, 6 + accountLists.size() - 4, 6 + accountLists.size() - 1));

                        // tạo dòng người lập
                        Row rowFooter = sheet.createRow(13 + data1.size() + 5);
                        rowFooter.createCell(1);
                        rowFooter.getCell(1).setCellStyle(styleFooterBold);
                        rowFooter.getCell(1).setCellValue("Người lập biểu");
                        rowFooter.createCell(5 + accountLists.size() - 1);
                        rowFooter.getCell(5 + accountLists.size() - 1).setCellStyle(styleFooterBold);
                        rowFooter.getCell(5 + accountLists.size() - 1).setCellValue("Kế toán trưởng");

//                    // tạo dòng ký ghi họ ten
                        Row rowFooter2 = sheet.createRow(13 + data1.size() + 6);
                        rowFooter2.createCell(1);
                        rowFooter2.getCell(1).setCellStyle(styleFooterItalic);
                        rowFooter2.getCell(1).setCellValue("(Ký, họ tên)");
                        rowFooter2.createCell(5 + accountLists.size() - 1);
                        rowFooter2.getCell(5 + accountLists.size() - 1).setCellStyle(styleFooterItalic);
                        rowFooter2.getCell(5 + accountLists.size() - 1).setCellValue("(Ký, họ tên)");

                        // tạo dòng tên người ký
                        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                            Row rowFooterValue = sheet.createRow(13 + data1.size() + 9);
                            rowFooterValue.createCell(1);
                            rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                            rowFooterValue.createCell(5 + accountLists.size() - 1);
                            rowFooterValue.getCell(5 + accountLists.size() - 1).setCellStyle(styleFooterBold);
                            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                                rowFooterValue.createCell(1).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
                                rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                            } else {
                                rowFooterValue.createCell(1).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
                                rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                            }

                            rowFooterValue.createCell(5 + accountLists.size() - 1).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
                            rowFooterValue.getCell(5 + accountLists.size() - 1).setCellStyle(styleFooterBold);
                        }
                    }
                }
            }
            if (workbook.getNumberOfSheets() > 1) {
                workbook.removeSheetAt(0);
            } else {
                Sheet sheet = workbook.getSheetAt(0);
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(0).getCell(4).setCellStyle(styleFooterBold);
                styleFooterItalic.setWrapText(true);
                sheet.getRow(1).getCell(4).setCellStyle(styleFooterItalic);
                sheet.getRow(4).getCell(0).setCellStyle(styleCustomSize);
                sheet.getRow(5).getCell(0).setCellStyle(styleFooterBold);
                sheet.getRow(6).getCell(0).setCellStyle(styleItalicCenterHeader);
                sheet.getRow(7).getCell(0).setCellStyle(styleFooterBold);
                sheet.getRow(6).getCell(0).setCellValue("Từ ngày: " + convertDate(requestReport.getFromDate()) + " đến ngày " + convertDate(requestReport.getToDate()));
                sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + accountList.getAccountNumber() + " - " + accountList.getAccountName());
                sheet.shiftRows(15, sheet.getLastRowNum(), 2);
                sheet.addMergedRegion(new CellRangeAddress(12, 12, 5, 6));
                sheet.addMergedRegion(new CellRangeAddress(11, 11, 5, 6));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 6));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 6));
                sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 6));
                sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 6));
                sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 6));
                sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 6));
                Row rowBody = sheet.createRow(15);
                Cell cell0 = rowBody.createCell(0);
                cell0.setCellStyle(style);
                Cell cell1 = rowBody.createCell(1);
                cell1.setCellStyle(style);
                Cell cell2 = rowBody.createCell(2);
                cell2.setCellStyle(style);
                Cell cell3 = rowBody.createCell(3);
                cell3.setCellStyle(style);
                Cell cell4 = rowBody.createCell(4);
                cell4.setCellStyle(style);
                Cell cell5 = rowBody.createCell(5);
                cell5.setCellStyle(style);
                Cell cell6 = rowBody.createCell(6);
                cell6.setCellStyle(style);

                sheet.getRow(12).getCell(5).setCellStyle(styleBHeader);

                Row rowPage = sheet.createRow(13 + 4);
                rowPage.createCell(0);
                rowPage.getCell(0).setCellValue("- Sổ này có 01 trang, đánh số từ trang 01 đến trang số ….......");
                rowPage.getCell(0).setCellStyle(stylePage);
                Row rowPage2 = sheet.createRow(13 + 5);
                rowPage2.createCell(0);
                rowPage2.getCell(0).setCellValue("- Ngày mở sổ: …...................");
                rowPage2.getCell(0).setCellStyle(stylePage);


                Row rowDate = sheet.createRow(13 + 7);
                rowDate.createCell(4);
                rowDate.getCell(4).setCellStyle(styleFooterItalic);
                rowDate.getCell(4).setCellValue("Ngày ..... tháng ..... năm .......");
                sheet.addMergedRegion(new CellRangeAddress(20, 20, 4, 6));

                // tạo dòng người lập
                Row rowFooter = sheet.createRow(13 + 8);
                rowFooter.createCell(1);
                rowFooter.getCell(1).setCellStyle(styleFooterBold);
                rowFooter.getCell(1).setCellValue("Người lập biểu");
                rowFooter.createCell(5 );
                rowFooter.getCell(5).setCellStyle(styleFooterBold);
                rowFooter.getCell(5).setCellValue("Kế toán trưởng");

//                    // tạo dòng ký ghi họ ten
                Row rowFooter2 = sheet.createRow(13 + 9);
                rowFooter2.createCell(1);
                rowFooter2.getCell(1).setCellStyle(styleFooterItalic);
                rowFooter2.getCell(1).setCellValue("(Ký, họ tên)");
                rowFooter2.createCell(5);
                rowFooter2.getCell(5).setCellStyle(styleFooterItalic);
                rowFooter2.getCell(5).setCellValue("(Ký, họ tên)");

                // tạo dòng tên người ký
                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
                    Row rowFooterValue = sheet.createRow(25);
                    rowFooterValue.createCell(1);
                    rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                    rowFooterValue.createCell(5);
                    rowFooterValue.getCell(5).setCellStyle(styleFooterBold);
                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                        rowFooterValue.createCell(1).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                    } else {
                        rowFooterValue.createCell(1).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
                    }

                    rowFooterValue.createCell(5).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
                    rowFooterValue.getCell(5).setCellStyle(styleFooterBold);
                }
            }
//            } else {
//                Sheet sheet = workbook.getSheetAt(0);
//                sheet.getRow(5).getCell(0).setCellValue(header4);
//                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
//                // tạo dòng tên người ký
//                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
//                    sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 3));
//                    Row rowFooterValue = sheet.createRow(19);
//                    rowFooterValue.createCell(2);
//                    rowFooterValue.getCell(2).setCellStyle(styleFooterBold);
//                    rowFooterValue.createCell(4);
//                    rowFooterValue.getCell(4).setCellStyle(styleFooterBold);
//                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
//                        rowFooterValue.createCell(1).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
//                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
//                    } else {
//                        rowFooterValue.createCell(1).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
//                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
//                    }
//
//                    rowFooterValue.createCell(3).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
//                    rowFooterValue.getCell(3).setCellStyle(styleFooterBold);
////                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 1, 2));
////                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 3, 4));
//                }
//            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }
    byte[] getExcelTheTinhGiaThanh(RequestReport requestReport, String path) throws JRException {
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String listMaterialGoodsID = ",";
        String listCostSetID = ",";
        String header4 = "";
        CPPeriodDTO cpPeriod = cpPeriodRepository.getByID(requestReport.getcPPeriodID());
        if (cpPeriod != null) {
            header4 += "Từ ngày " + convertDate(cpPeriod.getFromDate()) + " đên ngày " + convertDate(cpPeriod.getToDate());
        }
        for (int i = 0; i < requestReport.getListMaterialGoods().size(); i++) {
            listMaterialGoodsID += Common.revertUUID(requestReport.getListMaterialGoods().get(i)) + ",";
        }
        for (int i = 0; i < requestReport.getListCostSets().size(); i++) {
            listCostSetID += Common.revertUUID(requestReport.getListCostSets().get(i)) + ",";
        }
        checkEbPackage(userDTO, parameter);
        JasperReport jasperReport;
        List<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTOS = reportBusinessRepository.getTheTinhGiaThanh(
            requestReport.getCompanyID(), listMaterialGoodsID, listCostSetID, requestReport.getcPPeriodID(), requestReport.getTypeMethod(), requestReport.getDependent());
        /*Header*/
        checkEbPackage(userDTO, parameter);
        List<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTOList = new ArrayList<>();
        if (soTheTinhGiaThanhDTOS.size() == 0) {
            if (requestReport.getTypeMethod() == 0 || requestReport.getTypeMethod() == 1 || requestReport.getTypeMethod() == 2) {
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "1. Chi phí SXKD dở dang đầu kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "2. Chi phí SXKD phát sinh trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "3. Giá thành sản phẩm, dịch vụ trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "4. Chi phí SXKD dở dang cuối kỳ ", null, null, null, null, null, 0));
            }
            else{
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "1. Lũy kế chi phí phát sinh kỳ trước", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "2. Chi phí SXKD phát sinh trong kỳ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "3. Tổng chi phí", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "4. Số nghiệp thu trong kỳ ", null, null, null, null, null, 0));
                soTheTinhGiaThanhDTOS.add(new SoTheTinhGiaThanhDTO(null, "", "", null, "", "", "5. Số chưa nghiệp thu cuối kỳ", null, null, null, null, null, 0));
            }
        } else {
            for (int i = 0; i < soTheTinhGiaThanhDTOS.size(); i++) {
                int value = i;
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmount() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmount().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmount(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountNLVLToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountNLVL(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountNCTTToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountNCTT(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountCPSDMTCToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSDMTC(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
                soTheTinhGiaThanhDTOS.get(i).setTotalAmountCPSXCToString((soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC() != null && soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC().compareTo(BigDecimal.ZERO) != 0) ? (Utils.formatTien(soTheTinhGiaThanhDTOS.get(i).getTotalAmountCPSXC(), Constants.SystemOption.DDSo_TienVND, userDTO)) : null);
//                for (SoTheTinhGiaThanhDTO item : soTheTinhGiaThanhDTOS) {
//                    if (item.getQuantumID() != null && soTheTinhGiaThanhDTOS.get(i).getQuantumID().equals(item.getQuantumID())) {
//                        if (item.getStt() != null && item.getStt() > soThuTu) {
//                            soThuTu = item.getStt();
//                        }
//                    }
//                }
                Optional<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTO = soTheTinhGiaThanhDTOList.stream().filter(a -> a.getQuantumID().equals(soTheTinhGiaThanhDTOS.get(value).getQuantumID()) && a.getCostSetID().equals(soTheTinhGiaThanhDTOS.get(value).getCostSetID())).findFirst();
                if (!soTheTinhGiaThanhDTO.isPresent()) {
                    soTheTinhGiaThanhDTOList.add(soTheTinhGiaThanhDTOS.get(value));
                }
//                soTheTinhGiaThanhDTOS.get(i).setStt(soThuTu + 1);
//                soThuTu = 0;
            }
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);

            Font fontI = workbook.createFont();
            fontI.setFontName("Times New Roman");
            fontI.setItalic(true);

            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleHeader2 = workbook.createCellStyle();
            styleHeader2.setFont(fontB);
            styleHeader2.setWrapText(true);
            styleHeader2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleHeader2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleMoney = workbook.createCellStyle();
            styleMoney.setFont(fontB);
            styleMoney.setBorderBottom(BorderStyle.THIN);
            styleMoney.setBorderTop(BorderStyle.THIN);
            styleMoney.setBorderRight(BorderStyle.THIN);
            styleMoney.setBorderLeft(BorderStyle.THIN);
            styleMoney.setWrapText(true);
            styleMoney.setAlignment(HorizontalAlignment.RIGHT);
            styleMoney.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleMoney.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontItalic = workbook.createFont();
            fontItalic.setItalic(true);
            fontItalic.setFontName("Times New Roman");
            CellStyle styleFooterItalic = workbook.createCellStyle();
            styleFooterItalic.setFont(fontItalic);
            styleFooterItalic.setAlignment(HorizontalAlignment.CENTER);

            Font fontBold = workbook.createFont();
            fontBold.setBold(true);
            fontBold.setFontName("Times New Roman");
            CellStyle styleFooterBold = workbook.createCellStyle();
            styleFooterBold.setFont(fontBold);
            styleFooterBold.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 1; i < soTheTinhGiaThanhDTOList.size(); i++) {
                workbook.cloneSheet(0);

            }
            if (soTheTinhGiaThanhDTOList.size() > 0) {
                for (int i = 0; i < soTheTinhGiaThanhDTOList.size(); i++) {
                    int value = i;
                    BigDecimal total = BigDecimal.ZERO;
                    List<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTOS1 = soTheTinhGiaThanhDTOS.stream().filter(a -> a.getQuantumID().equals(soTheTinhGiaThanhDTOList.get(value).getQuantumID()) && a.getCostSetID().equals(soTheTinhGiaThanhDTOList.get(value).getCostSetID())).collect(Collectors.toList());
                    Sheet sheet = workbook.getSheetAt(i);
                    if (soTheTinhGiaThanhDTOS1.size() > 0) {
                        sheet.shiftRows(14, sheet.getLastRowNum(), soTheTinhGiaThanhDTOS1.size());
                    }
                    // fill dữ liệu vào file
                    int start = 11;
                    // set header
                    SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 1);
                    if (requestReport.getTypeMethod() == 1 || requestReport.getTypeMethod() == 2) {
                        sheet.getRow(5).getCell(1).setCellValue(header4);
                        sheet.getRow(6).getCell(1).setCellValue(" Đối tượng tập hợp chi phí: " + soTheTinhGiaThanhDTOList.get(i).getCostSetCode() + " - " + soTheTinhGiaThanhDTOList.get(i).getCostSetName());
                        sheet.getRow(7).getCell(1).setCellValue(" Thành phẩm: " + soTheTinhGiaThanhDTOList.get(i).getQuantumCode() + " - " + soTheTinhGiaThanhDTOList.get(i).getQuantumName());
                    } else if (requestReport.getTypeMethod() == 0) {
                        sheet.getRow(5).getCell(1).setCellValue("");
                        sheet.getRow(6).getCell(1).setCellValue("");
                        sheet.getRow(7).getCell(1).setCellValue("");
                        Row row = sheet.createRow(8);
                        Cell cell = row.createCell(1);
                        cell.setCellStyle(styleHeader2);
                        cell.setCellValue(" Thành phẩm: " + soTheTinhGiaThanhDTOList.get(i).getCostSetCode() + " - " + soTheTinhGiaThanhDTOList.get(i).getCostSetName());
                    }
                    //

                    for (SoTheTinhGiaThanhDTO item : soTheTinhGiaThanhDTOS1) {
                        Row row = sheet.createRow(start);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellStyle(styleB);
                        CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                        cell1.setCellValue(item.getTarget());

                        Cell cell2 = row.createCell(2);
                        cell2.setCellStyle(style);
                        CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
                        cell2.setCellValue(item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0);

                        Cell cell3 = row.createCell(3);
                        cell3.setCellStyle(style);
                        CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
                        cell3.setCellValue(item.getTotalAmountNLVL() != null ? item.getTotalAmountNLVL().doubleValue() : 0);

                        Cell cell4 = row.createCell(4);
                        cell4.setCellStyle(style);
                        CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                        cell4.setCellValue(item.getTotalAmountNCTT() != null ? item.getTotalAmountNCTT().doubleValue() : 0);

                        Cell cell5 = row.createCell(5);
                        cell5.setCellStyle(style);
                        CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                        cell5.setCellValue(item.getTotalAmountCPSDMTC() != null ? item.getTotalAmountCPSDMTC().doubleValue() : 0);

                        Cell cell6 = row.createCell(6);
                        cell6.setCellStyle(style);
                        CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                        cell6.setCellValue(item.getTotalAmountCPSXC() != null ? item.getTotalAmountCPSXC().doubleValue() : 0);
                        start++;
                    }
                    SetFooterAdditionalExcel(workbook, sheet, userDTO, (11 + soTheTinhGiaThanhDTOS1.size()), 5, 1, 3, 5, "Người lập", "Kế toán trưởng", "Giám đốc");
                    sheet.getRow(start).getCell(0).setCellValue("");
                    sheet.getRow(start + 1).getCell(0).setCellValue("");
                }
            } else {
//                Sheet sheet = workbook.getSheetAt(0);
//                sheet.getRow(5).getCell(0).setCellValue(header4);
//                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
//                // tạo dòng tên người ký
//                if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
//                    sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 3));
//                    Row rowFooterValue = sheet.createRow(19);
//                    rowFooterValue.createCell(2);
//                    rowFooterValue.getCell(2).setCellStyle(styleFooterBold);
//                    rowFooterValue.createCell(4);
//                    rowFooterValue.getCell(4).setCellStyle(styleFooterBold);
//                    if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
//                        rowFooterValue.createCell(1).setCellValue(userDTO.getFullName() != null ? userDTO.getFullName() : "");
//                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
//                    } else {
//                        rowFooterValue.createCell(1).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter() : "");
//                        rowFooterValue.getCell(1).setCellStyle(styleFooterBold);
//                    }
//
//                    rowFooterValue.createCell(3).setCellValue(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() != null ? userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant() : "");
//                    rowFooterValue.getCell(3).setCellStyle(styleFooterBold);
////                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 1, 2));
////                        sheet.addMergedRegion(new CellRangeAddress(9 + tongHopCPTheoKMCPDTOS1.size() + 7, 9 + tongHopCPTheoKMCPDTOS1.size() + 7, 3, 4));
//                }
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }
    // De quy lay ra tat ca tai khoan con cua list tai khoan hien tai
    public List<AccountList> getAllChildAccount(List<AccountList> accountLists, UUID orgID) {
        for (int i = 0; i < accountLists.size(); i++) {
            if (Boolean.TRUE.equals(accountLists.get(i).getParentNode())) {
                List<AccountList> accountLists1 = accountListRepository.findAccountListByParentID(accountLists.get(i).getId(), orgID);
                if (accountLists1 != null && accountLists1.size() > 0) {
                    for (AccountList item : accountLists1) {
                        Optional<AccountList> accountList = accountLists.stream().filter(a -> a.getAccountNumber().equals(item.getAccountNumber())).findFirst();
                        if (!accountList.isPresent()) {
                            accountLists.add(item);
                        }
                    }
                }
            }
        }
        return accountLists;
    }
    byte[] getSoTheoDoiLaiLoTheoHoaDon(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiLaiLoTheoHoaDon";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        List<SoTheoDoiLaiLoTheoHoaDonDTO> soTheoDoiLaiLoTheoHoaDonDTOs = reportBusinessRepository.getSoTheoDoiLaiLoTheoHoaDon(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec
            );
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("REPORT_MAX_COUNT", soTheoDoiLaiLoTheoHoaDonDTOs.size());
        parameter.put("detailSize", soTheoDoiLaiLoTheoHoaDonDTOs.size());
        List<Type> types = typeRepository.findAllByIsActive();
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        BigDecimal totalCol1 = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        for (int i = 0; i < soTheoDoiLaiLoTheoHoaDonDTOs.size(); i++) {
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setLinkRef(getRefLink(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getRefType(), soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getRefID(), types, null, null, null, null));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setInvoiceDateString(convertDate(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getInvoiceDate()));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setGiaTriHHDVString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaTriHHDV(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setChietKhauString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getChietKhau(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setGiamGiaString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiamGia(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setTraLaiString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getTraLai(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setGiaVonString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaVon(), Constants.SystemOption.DDSo_TienVND, userDTO));
            soTheoDoiLaiLoTheoHoaDonDTOs.get(i).setLaiLoString(Utils.formatTien(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getLaiLo(), Constants.SystemOption.DDSo_TienVND, userDTO));

            totalCol1 = totalCol1.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaTriHHDV() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaTriHHDV() : BigDecimal.ZERO);
            totalCol2 = totalCol2.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getChietKhau() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getChietKhau() : BigDecimal.ZERO);
            totalCol3 = totalCol3.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiamGia() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiamGia() : BigDecimal.ZERO);
            totalCol4 = totalCol4.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getTraLai() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getTraLai() : BigDecimal.ZERO);
            totalCol5 = totalCol5.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaVon() !=null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaVon() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getLaiLo() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getLaiLo() : BigDecimal.ZERO);
        }
        parameter.put("totalCol1", Utils.formatTien(totalCol1, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCol2", Utils.formatTien(totalCol2, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCol3", Utils.formatTien(totalCol3, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCol4", Utils.formatTien(totalCol4, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCol5", Utils.formatTien(totalCol5, Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalCol6", Utils.formatTien(totalCol6, Constants.SystemOption.DDSo_TienVND, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soTheoDoiLaiLoTheoHoaDonDTOs.isEmpty()) {
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
            parameter.put("detailSize", soTheoDoiLaiLoTheoHoaDonDTOs.size());
            parameter.put("REPORT_MAX_COUNT", soTheoDoiLaiLoTheoHoaDonDTOs.size());
            parameter.put("totalCol1", "");
            parameter.put("totalCol2", "");
            parameter.put("totalCol3", "");
            parameter.put("totalCol4", "");
            parameter.put("totalCol5", "");
            parameter.put("totalCol6", "");
        }
        //       parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
        result = ReportUtils.generateReportPDF(soTheoDoiLaiLoTheoHoaDonDTOs, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoTheoDoiLaiLoTheoHoaDon(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<SoTheoDoiLaiLoTheoHoaDonDTO> soTheoDoiLaiLoTheoHoaDonDTOs = reportBusinessRepository.getSoTheoDoiLaiLoTheoHoaDon(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getCompanyID() == null ? userDTO.getOrganizationUnit().getId() : requestReport.getCompanyID(),
            requestReport.getDependent(),
            phienSoLamViec
        );
        if (soTheoDoiLaiLoTheoHoaDonDTOs.size() == 0) {
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
            soTheoDoiLaiLoTheoHoaDonDTOs.add(new SoTheoDoiLaiLoTheoHoaDonDTO());
        }
        BigDecimal totalCol1 = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        for (int i = 0; i < soTheoDoiLaiLoTheoHoaDonDTOs.size(); i++) {
            totalCol1 = totalCol1.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaTriHHDV() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaTriHHDV() : BigDecimal.ZERO);
            totalCol2 = totalCol2.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getChietKhau() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getChietKhau() : BigDecimal.ZERO);
            totalCol3 = totalCol3.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiamGia() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiamGia() : BigDecimal.ZERO);
            totalCol4 = totalCol4.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getTraLai() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getTraLai() : BigDecimal.ZERO);
            totalCol5 = totalCol5.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaVon() !=null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getGiaVon() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getLaiLo() != null ? soTheoDoiLaiLoTheoHoaDonDTOs.get(i).getLaiLo() : BigDecimal.ZERO);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(6, sheet.getLastRowNum(), soTheoDoiLaiLoTheoHoaDonDTOs.size());
            // fill dữ liệu vào file
            int i = 6;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(4).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            for (SoTheoDoiLaiLoTheoHoaDonDTO item : soTheoDoiLaiLoTheoHoaDonDTOs) {
                Row row = sheet.createRow(i);

                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                cell1.setCellValue(convertDate(item.getInvoiceDate()));

                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                cell2.setCellValue(item.getInvoiceNo());

                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.LEFT);
                cell3.setCellValue(item.getAccountingObjectName());

                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.LEFT);
                cell4.setCellValue(item.getReason());

                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell5.setCellValue(item.getGiaTriHHDV() != null ? item.getGiaTriHHDV().doubleValue() : 0);


                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell6.setCellValue(item.getChietKhau() != null ? item.getChietKhau().doubleValue() : 0);

                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell7.setCellValue(item.getGiamGia() != null ? item.getGiamGia().doubleValue() : 0);

                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell8.setCellValue(item.getTraLai() != null ? item.getTraLai().doubleValue() : 0);

                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell9.setCellValue(item.getGiaVon() != null ? item.getGiaVon().doubleValue() : 0);

                Cell cell10 = row.createCell(9);
                cell10.setCellStyle(style);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                cell10.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                cell10.setCellValue(item.getLaiLo() != null ? item.getLaiLo().doubleValue() : 0);

                i++;
            }

            Row rowTotal = sheet.getRow(6 + soTheoDoiLaiLoTheoHoaDonDTOs.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Cộng");

            Cell cell1 = rowTotal.createCell(4);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            cell1.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell1.setCellValue(totalCol1.doubleValue());

            Cell cell2 = rowTotal.createCell(5);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            cell2.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell2.setCellValue(totalCol2.doubleValue());

            Cell cell3 = rowTotal.createCell(6);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell3.setCellValue(totalCol3.doubleValue());

            Cell cell4 = rowTotal.createCell(7);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell4.setCellValue(totalCol4.doubleValue());

            Cell cell5 = rowTotal.createCell(8);
            cell5.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell5.setCellValue(totalCol5.doubleValue());

            Cell cell6 = rowTotal.createCell(9);
            cell6.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell6.setCellValue(totalCol6.doubleValue());

            veBorder(rowTotal, style, Stream.of(1, 2, 3).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (8 + soTheoDoiLaiLoTheoHoaDonDTOs.size()), 8, 1, 4, 8, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }

    byte[] getSoTheoDoiCongNoPhaiThuTheoHoaDon(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiCongNoPhaiThuTheoHoaDon";
        Map<String, Object> parameter = new HashMap<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        JasperReport jasperReport;
        StringBuilder accountingObjects = new StringBuilder(",");
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        String accNumber = ",";
        if (requestReport.getAccountNumber().equals("131")) {
            accNumber += "131,1311,1312" + ",";
        } else {
            accNumber += requestReport.getAccountNumber() + ",";
        }
        List<SoTheoDoiCongNoPhaiThuTheoHoaDonDTO> soTheoDoiCongNoPhaiThuTheoHoaDonDTOs = reportBusinessRepository.getSoTheoDoiCongNoPhaiThuTheoHoaDon(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            accNumber,
            requestReport.getCurrencyID(),
            accountingObjects.toString(),
            requestReport.getTypeShowCurrency(),
            currentUserLoginAndOrg.get().getOrg()
            );
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
        String fromToDate = "Tài khoản: " + requestReport.getAccountNumber() + " ; " + "Loại tiền: " + requestReport.getCurrencyID() + " ; "
            + Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate());
        parameter.put("fromDateAndToDate", fromToDate);
        parameter.put("REPORT_TYPE", requestReport.getTypeReport());
        parameter.put("ShowAccumAmount", requestReport.getShowAccumAmount());
        parameter.put("REPORT_MAX_COUNT", soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());
        parameter.put("detailSize", soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());
        List<Type> types = typeRepository.findAllByIsActive();
        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        BigDecimal totalCol1 = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        for (int i = 0; i < soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size(); i++) {
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setLinkRef(getRefLink(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getRefType(), soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getRefID(), types, null, null, null, null));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setInvoiceDateString(convertDate(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getInvoiceDate()));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setGiaTriHoaDonString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiaTriHoaDon(), typeAmount, userDTO));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setTraLaiString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getTraLai(), typeAmount, userDTO));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setGiamGiaString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiamGia(), typeAmount, userDTO));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setChietKhauTT_GiamTruKhacString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getChietKhauTT_GiamTruKhac(), typeAmount, userDTO));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setSoDaThuString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoDaThu(), typeAmount, userDTO));
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).setSoConPhaiThuString(Utils.formatTien(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoConPhaiThu(), typeAmount, userDTO));
            totalCol1 = totalCol1.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiaTriHoaDon() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiaTriHoaDon() : BigDecimal.ZERO);
            totalCol2 = totalCol2.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getTraLai() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getTraLai() : BigDecimal.ZERO);
            totalCol3 = totalCol3.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiamGia() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiamGia() : BigDecimal.ZERO);
            totalCol4 = totalCol4.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getChietKhauTT_GiamTruKhac() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getChietKhauTT_GiamTruKhac() : BigDecimal.ZERO);
            totalCol5 = totalCol5.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoDaThu() !=null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoDaThu() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoConPhaiThu() !=null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoConPhaiThu() : BigDecimal.ZERO);
        }
        parameter.put("totalCol1", Utils.formatTien(totalCol1, typeAmount, userDTO));
        parameter.put("totalCol2", Utils.formatTien(totalCol2, typeAmount, userDTO));
        parameter.put("totalCol3", Utils.formatTien(totalCol3, typeAmount, userDTO));
        parameter.put("totalCol4", Utils.formatTien(totalCol4, typeAmount, userDTO));
        parameter.put("totalCol5", Utils.formatTien(totalCol5, typeAmount, userDTO));
        parameter.put("totalCol6", Utils.formatTien(totalCol6, typeAmount, userDTO));
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        if (soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.isEmpty()) {
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
            parameter.put("detailSize", soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());
            parameter.put("REPORT_MAX_COUNT", soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());
            parameter.put("totalCol1", "");
            parameter.put("totalCol2", "");
            parameter.put("totalCol3", "");
            parameter.put("totalCol4", "");
            parameter.put("totalCol5", "");
            parameter.put("totalCol6", "");
        }
        //       parameter.put("REPORT_MAX_COUNT", soCongCuDungCuDTOs.size());
        result = ReportUtils.generateReportPDF(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs, parameter, jasperReport);

        return result;
    }

    byte[] getExcelSoTheoDoiCongNoPhaiThuTheoHoaDon(RequestReport requestReport, String path) throws JRException {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        userDTO.setEmptyIfNull(true);
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        StringBuilder accountingObjects = new StringBuilder(",");
        for (UUID item : requestReport.getAccountingObjects()) {
            accountingObjects.append(Utils.uuidConvertToGUID(item).toString()).append(",");
        }
        List<SoTheoDoiCongNoPhaiThuTheoHoaDonDTO> soTheoDoiCongNoPhaiThuTheoHoaDonDTOs = reportBusinessRepository.getSoTheoDoiCongNoPhaiThuTheoHoaDon(
            requestReport.getFromDate(),
            requestReport.getToDate(),
            requestReport.getAccountNumber(),
            requestReport.getCurrencyID(),
            accountingObjects.toString(),
            requestReport.getTypeShowCurrency(),
            currentUserLoginAndOrg.get().getOrg()
            );
        if (soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size() == 0) {
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
            soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.add(new SoTheoDoiCongNoPhaiThuTheoHoaDonDTO());
        }
        BigDecimal totalCol1 = BigDecimal.ZERO;
        BigDecimal totalCol2 = BigDecimal.ZERO;
        BigDecimal totalCol3 = BigDecimal.ZERO;
        BigDecimal totalCol4 = BigDecimal.ZERO;
        BigDecimal totalCol5 = BigDecimal.ZERO;
        BigDecimal totalCol6 = BigDecimal.ZERO;
        for (int i = 0; i < soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size(); i++) {
            totalCol1 = totalCol1.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiaTriHoaDon() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiaTriHoaDon() : BigDecimal.ZERO);
            totalCol2 = totalCol2.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getTraLai() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getTraLai() : BigDecimal.ZERO);
            totalCol3 = totalCol2.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiamGia() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getGiamGia() : BigDecimal.ZERO);
            totalCol4 = totalCol2.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getChietKhauTT_GiamTruKhac() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getChietKhauTT_GiamTruKhac() : BigDecimal.ZERO);
            totalCol5 = totalCol5.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoDaThu() !=null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoDaThu() : BigDecimal.ZERO);
            totalCol6 = totalCol6.add(soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoConPhaiThu() != null ? soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.get(i).getSoConPhaiThu() : BigDecimal.ZERO);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.shiftRows(6, sheet.getLastRowNum(), soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());
            // fill dữ liệu vào file
            int i = 6;
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle cellStyleB = workbook.createCellStyle();
            cellStyleB.setBorderBottom(BorderStyle.THIN);
            cellStyleB.setBorderTop(BorderStyle.THIN);
            cellStyleB.setBorderRight(BorderStyle.THIN);
            cellStyleB.setBorderLeft(BorderStyle.THIN);
            cellStyleB.setFont(fontB);
            cellStyleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);

            Cell cellDate = sheet.getRow(4).getCell(0);
            CellStyle styleI = workbook.createCellStyle();
            Font fontI = workbook.createFont();
            fontI.setItalic(true);
            fontI.setFontName("Times New Roman");
            styleI.setFont(fontI);
            cellDate.setCellStyle(styleI);
            CellUtil.setAlignment(cellDate, HorizontalAlignment.CENTER);
            cellDate.setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
            //
            for (SoTheoDoiCongNoPhaiThuTheoHoaDonDTO item : soTheoDoiCongNoPhaiThuTheoHoaDonDTOs) {
                Row row = sheet.createRow(i);
                Cell cell1 = row.createCell(0);
                cell1.setCellStyle(style);
                CellUtil.setAlignment(cell1, HorizontalAlignment.LEFT);
                cell1.setCellValue(convertDate(item.getInvoiceDate()));
                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(style);
                CellUtil.setAlignment(cell2, HorizontalAlignment.LEFT);
                cell2.setCellValue(item.getInvoiceNo());
                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(style);
                CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                cell3.setCellValue(item.getAccountingObjectName());
                Cell cell4 = row.createCell(3);
                cell4.setCellStyle(style);
                CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
                cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getGiaTriHoaDon() != null) {
                    cell4.setCellValue(item.getGiaTriHoaDon().doubleValue());
                }
                Cell cell5 = row.createCell(4);
                cell5.setCellStyle(style);
                CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
                cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getTraLai() != null) {
                    cell5.setCellValue(item.getTraLai().doubleValue());
                }
                Cell cell6 = row.createCell(5);
                cell6.setCellStyle(style);
                CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getGiamGia() != null) {
                    cell6.setCellValue(item.getGiamGia().doubleValue());
                }
                Cell cell7 = row.createCell(6);
                cell7.setCellStyle(style);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                cell7.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getChietKhauTT_GiamTruKhac() != null) {
                    cell7.setCellValue(item.getChietKhauTT_GiamTruKhac().doubleValue());
                }
                Cell cell8 = row.createCell(7);
                cell8.setCellStyle(style);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                cell8.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getSoDaThu() != null) {
                    cell8.setCellValue(item.getSoDaThu().doubleValue());
                }
                Cell cell9 = row.createCell(8);
                cell9.setCellStyle(style);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                cell9.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                if (item.getSoConPhaiThu() != null) {
                    cell9.setCellValue(item.getSoConPhaiThu().doubleValue());
                }
                i++;
            }

            Row rowTotal = sheet.getRow(6 + soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size());

            Cell cell0 = rowTotal.createCell(0);
            cell0.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
            cell0.setCellValue("Cộng");

            Cell cell1 = rowTotal.createCell(3);
            cell1.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell1, HorizontalAlignment.RIGHT);
            cell1.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell1.setCellValue(totalCol1.doubleValue());

            Cell cell2 = rowTotal.createCell(4);
            cell2.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell2, HorizontalAlignment.RIGHT);
            cell2.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell2.setCellValue(totalCol2.doubleValue());

            Cell cell3 = rowTotal.createCell(5);
            cell3.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell3, HorizontalAlignment.RIGHT);
            cell3.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell3.setCellValue(totalCol3.doubleValue());

            Cell cell4 = rowTotal.createCell(6);
            cell4.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
            cell4.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell4.setCellValue(totalCol4.doubleValue());

            Cell cell5 = rowTotal.createCell(7);
            cell5.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
            cell5.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell5.setCellValue(totalCol5.doubleValue());

            Cell cell6 = rowTotal.createCell(8);
            cell6.setCellStyle(cellStyleB);
            CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
            cell6.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cell6.setCellValue(totalCol6.doubleValue());

            veBorder(rowTotal, style, Stream.of(1, 2).collect(Collectors.toList()));

            SetFooterAdditionalExcel(workbook, sheet, userDTO, (8 + soTheoDoiCongNoPhaiThuTheoHoaDonDTOs.size()), 7, 1, 4, 7, "Người ghi sổ", "Kế toán trưởng", "Giám đốc");
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }
    // De quy lay ra tat ca tai khoan con cua list tai khoan hien tai
    public List<ExpenseItem> getAllChildExpenseItems(List<ExpenseItem> expenseItems, UUID orgID) {
        for (int i = 0; i < expenseItems.size(); i++) {
            if (Boolean.TRUE.equals(expenseItems.get(i).getIsParentNode())) {
                List<ExpenseItem> expenseItems1 = expenseItemRepository.findExpenseItemsByParentID(expenseItems.get(i).getId(), orgID);
                if (expenseItems1 != null && expenseItems1.size() > 0) {
                    for (ExpenseItem item : expenseItems1) {
                        Optional<ExpenseItem> expenseItem = expenseItems.stream().filter(a -> a.getId().equals(item.getId())).findFirst();
                        if (!expenseItem.isPresent()) {
                            expenseItems.add(item);
                        }
                    }
                }
            }
        }
        return expenseItems;
    }


    private byte[] getSoTheoDoiThanhToanBangNgoaiTe(RequestReport requestReport) throws JRException {
        byte[] result = null;
        String reportName = "SoTheoDoiThanhToanBangNgoaiTe";
        Map<String, Object> parameter = new HashMap<>();
        UserDTO userDTO = userService.getAccount();
        Integer phienLamViec = Utils.PhienSoLamViec(userDTO);
//        String typeAmount = userDTO.getOrganizationUnit().getCurrencyID().equalsIgnoreCase(requestReport.getCurrencyID()) ?
//            Constants.SystemOption.DDSo_TienVND : Constants.SystemOption.DDSo_NgoaiTe;
        JasperReport jasperReport;
        String listAccountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            listAccountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoTheoDoiThanhToanBangNgoaiTeDTO> detail = reportBusinessRepository.getSoTheoDoiThanhToanBangNgoaiTe(
            requestReport.getFromDate(), requestReport.getToDate(), requestReport.getCurrencyID(),
            requestReport.getAccountNumber(), listAccountingObjects, phienLamViec, requestReport.getCompanyID(),
            requestReport.getDependent());
        /*Header*/
        setHeader(userDTO, parameter, requestReport.getCompanyID());
        checkEbPackage(userDTO, parameter);
//        String color = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_MauSoAm))
//            .findAny()
//            .get()
//            .getData();
//        String rbgColor[] = color.substring(5, color.length() - 1).split(",");
//        parameter.put("textColor", ("rgb(" + rbgColor[0] + ", " + rbgColor[1] + ", " + rbgColor[2] + ")"));
        parameter.put("fromDateAndToDate", Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
        parameter.put("currency", requestReport.getCurrencyID());
        List<Type> types = typeRepository.findAllByIsActive();
        if (detail.size() >= 1) {
            for (int i = 0; i < detail.size(); i++) {
                if (detail.get(i).getIdGroup() == null && i < detail.size() - 1 && detail.get(i).getAccountingObjectID().equals(detail.get(i).getAccountingObjectID())
                    && detail.get(i).getAccount().equals(detail.get(i + 1).getAccount())){
                    detail.remove(i);
                }
                if (detail.get(i).getpSNSoTien() == null || detail.get(i).getpSNSoTien().doubleValue() == 0) {
                    detail.get(i).setPsnSotien(null);
                } else {
                    detail.get(i).setPsnSotien(Utils.formatTien(detail.get(i).getpSNSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getpSNQuyDoi() == null || detail.get(i).getpSNQuyDoi().doubleValue() == 0) {
                    detail.get(i).setPsnQuyDoi(null);
                } else {
                    detail.get(i).setPsnQuyDoi(Utils.formatTien(detail.get(i).getpSNQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (detail.get(i).getpSCSoTien() == null || detail.get(i).getpSCSoTien().doubleValue() == 0) {
                    detail.get(i).setPscSotien(null);
                } else {
                    detail.get(i).setPscSotien(Utils.formatTien(detail.get(i).getpSCSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getpSCQuyDoi() == null || detail.get(i).getpSCQuyDoi().doubleValue() == 0) {
                    detail.get(i).setPscQuyDoi(null);
                } else {
                    detail.get(i).setPscQuyDoi(Utils.formatTien(detail.get(i).getpSCQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (detail.get(i).getDuNoSoTien() == null || detail.get(i).getDuNoSoTien().doubleValue() == 0) {
                    detail.get(i).setDnSotien(null);
                } else {
                    detail.get(i).setDnSotien(Utils.formatTien(detail.get(i).getDuNoSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getDuCoSoTien() == null || detail.get(i).getDuCoSoTien().doubleValue() == 0) {
                    detail.get(i).setDcSotien(null);
                } else {
                    detail.get(i).setDcSotien(Utils.formatTien(detail.get(i).getDuCoSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getDuNoQuyDoi() == null || detail.get(i).getDuNoQuyDoi().doubleValue() == 0) {
                    detail.get(i).setDnQuyDoi(null);
                } else {
                    detail.get(i).setDnQuyDoi(Utils.formatTien(detail.get(i).getDuNoQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (detail.get(i).getDuCoQuyDoi() == null || detail.get(i).getDuCoQuyDoi().doubleValue() == 0) {
                    detail.get(i).setDcQuyDoi(null);
                } else {
                    detail.get(i).setDcQuyDoi(Utils.formatTien(detail.get(i).getDuCoQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                // Du Dau Ky
                if (detail.get(i).getsDDKSoTien() != null && detail.get(i).getsDDKSoTien().doubleValue() < 0) {
                    detail.get(i).setSoDuCoDauKySoTien(Utils.formatTien(detail.get(i).getsDDKSoTien().abs(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getsDDKSoTien() != null && detail.get(i).getsDDKSoTien().doubleValue() > 0) {
                    detail.get(i).setSoDuNoDauKySoTien(Utils.formatTien(detail.get(i).getsDDKSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getsDDKQuyDoi() != null && detail.get(i).getsDDKQuyDoi().doubleValue() < 0) {
                    detail.get(i).setSoDuCoDauKyQuyDoi(Utils.formatTien(detail.get(i).getsDDKQuyDoi().abs(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (detail.get(i).getsDDKQuyDoi() != null && detail.get(i).getsDDKQuyDoi().doubleValue() > 0) {
                    detail.get(i).setSoDuNoDauKyQuyDoi(Utils.formatTien(detail.get(i).getsDDKQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                // Du Cuoi Ky
                if (detail.get(i).getDuNoSoTien() == null || detail.get(i).getDuNoSoTien().doubleValue() == 0) {
                    detail.get(i).setDnSotien("");
                } else {
                    detail.get(i).setTotalsdnSotien(Utils.formatTien(detail.get(i).getDuNoSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getDuNoQuyDoi() == null || detail.get(i).getDuNoQuyDoi().doubleValue() == 0) {
                    detail.get(i).setDnQuyDoi("");
                } else {
                    detail.get(i).setTotalsdnQuyDoi(Utils.formatTien(detail.get(i).getDuNoQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (detail.get(i).getDuCoSoTien() == null || detail.get(i).getDuCoSoTien().doubleValue() == 0) {
                    detail.get(i).setDcSotien("");
                } else {
                    detail.get(i).setTotalsdcSotien(Utils.formatTien(detail.get(i).getDuCoSoTien(), Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (detail.get(i).getDuCoQuyDoi() == null || detail.get(i).getDuCoQuyDoi().doubleValue() == 0) {
                    detail.get(i).setDcQuyDoi("");
                } else {
                    detail.get(i).setTotalsdcQuyDoi(Utils.formatTien(detail.get(i).getDuCoQuyDoi(), Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                BigDecimal totalPsnSotien = BigDecimal.ZERO;
                BigDecimal totalPsnQuyDoi = BigDecimal.ZERO;
                BigDecimal totalPscSotien = BigDecimal.ZERO;
                BigDecimal totalPscQuyDoi = BigDecimal.ZERO;
                for (int j = 0; j < detail.size(); j++) {
                    if (detail.get(i).getAccountingObjectCode() != null && detail.get(i).getAccountingObjectCode().equals(detail.get(j).getAccountingObjectCode())
                        && detail.get(i).getAccount().equals(detail.get(j).getAccount())) {
                        if (detail.get(j).getpSNSoTien() != null) {
                            totalPsnSotien = totalPsnSotien.add(detail.get(j).getpSNSoTien());
                        }
                        if (detail.get(j).getpSNQuyDoi() != null) {
                            totalPsnQuyDoi = totalPsnQuyDoi.add(detail.get(j).getpSNQuyDoi());
                        }
                        if (detail.get(j).getpSCSoTien() != null) {
                            totalPscSotien = totalPscSotien.add(detail.get(j).getpSCSoTien());
                        }
                        if (detail.get(j).getpSCQuyDoi() != null) {
                            totalPscQuyDoi = totalPscQuyDoi.add(detail.get(j).getpSCQuyDoi());
                        }
                    }
                }
                if (totalPsnSotien.doubleValue() == 0) {
                    detail.get(i).setTotalpsnSotien("");
                } else {
                    detail.get(i).setTotalpsnSotien(Utils.formatTien(totalPsnSotien, Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (totalPsnQuyDoi.doubleValue() == 0) {
                    detail.get(i).setTotalpsnQuyDoi("");
                } else {
                    detail.get(i).setTotalpsnQuyDoi(Utils.formatTien(totalPsnQuyDoi, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (totalPscSotien.doubleValue() == 0) {
                    detail.get(i).setTotalpscSotien("");
                } else {
                    detail.get(i).setTotalpscSotien(Utils.formatTien(totalPscSotien, Constants.SystemOption.DDSo_NgoaiTe, userDTO));
                }
                if (totalPscQuyDoi.doubleValue() == 0) {
                    detail.get(i).setTotalpscQuyDoi("");
                } else {
                    detail.get(i).setTotalpscQuyDoi(Utils.formatTien(totalPscQuyDoi, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                detail.get(i).setPostedDate(convertDate(detail.get(i).getNgayChungTu()));
                detail.get(i).setDate(convertDate(detail.get(i).getNgayHoachToan()));
                detail.get(i).setTgHoiDoai(Utils.formatTien(detail.get(i).getTyGiaHoiDoai(), Constants.SystemOption.DDSo_TyGia, userDTO));
                detail.get(i).setLinkRef(getRefLink(detail.get(i).getRefType(), detail.get(i).getRefID(), types, null, null, null, null));
            }
        } else {
            detail.add(new SoTheoDoiThanhToanBangNgoaiTeDTO("",""));
            detail.add(new SoTheoDoiThanhToanBangNgoaiTeDTO("",""));
            detail.add(new SoTheoDoiThanhToanBangNgoaiTeDTO("",""));
        }
        if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayNameInReport())) {
            parameter.put("Manager", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDirector());
            parameter.put("Accountant", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getChiefAccountant());
            if (Boolean.TRUE.equals(userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getDisplayAccount())) {
                parameter.put("Reporter", userDTO.getFullName());
            } else {
                parameter.put("Reporter", userDTO.getOrganizationUnit().getOrganizationUnitOptionReport().getReporter());
            }
        }
        // tạo file báo ccáo
        jasperReport = ReportUtils.getCompiledFile("bao-cao/" + reportName + ".jasper", reportName + ".jrxml");
        // fill dữ liệu vào báo cáo, tọa file pdf và trả về mảng byte
        result = ReportUtils.generateReportPDF(detail, parameter, jasperReport);
        return result;
    }


    private byte[] getExcelSoTheoDoiThanhToanBangNgoaiTe(RequestReport requestReport, String path) {
        byte[] result = null;
        UserDTO userDTO = userService.getAccount();
        Integer phienLamViec = Utils.PhienSoLamViec(userDTO);
        String listAccountingObjects = ",";
        for (UUID item : requestReport.getAccountingObjects()) {
            listAccountingObjects += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<SoTheoDoiThanhToanBangNgoaiTeDTO> detail = reportBusinessRepository.getSoTheoDoiThanhToanBangNgoaiTe(
            requestReport.getFromDate(), requestReport.getToDate(), requestReport.getCurrencyID(),
            requestReport.getAccountNumber(), listAccountingObjects, phienLamViec, requestReport.getCompanyID(),
            requestReport.getDependent());
        List<List<SoTheoDoiThanhToanBangNgoaiTeDTO>> listDetail = new ArrayList<>();
        List<SoTheoDoiThanhToanBangNgoaiTeDTO> temp = new ArrayList<>();
        if (detail.size() <= 0) {
            temp.add(new SoTheoDoiThanhToanBangNgoaiTeDTO(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));
            temp.add(new SoTheoDoiThanhToanBangNgoaiTeDTO(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));
            temp.add(new SoTheoDoiThanhToanBangNgoaiTeDTO(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));
            listDetail.add(temp);
        } else {
            for (int i = 0; i < detail.size(); i++) {
                if (detail.get(i).getIdGroup() == null && i < detail.size() - 1 && detail.get(i).getAccountingObjectID().equals(detail.get(i).getAccountingObjectID())
                    && detail.get(i).getAccount().equals(detail.get(i + 1).getAccount())){
                    detail.remove(i);
                }
                temp.add(detail.get(i));
                if (detail.get(i).getIdGroup() == null || (i < detail.size() - 1 && !detail.get(i).getIdGroup().equals(detail.get(i + 1).getIdGroup())) || i == detail.size() - 1) {
                    listDetail.add(temp);
                    temp = new ArrayList<>();
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);

            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleDf = workbook.createCellStyle();
            styleDf.setFont(fontDf);

            for (int i = 1; i < listDetail.size(); i++) {
                workbook.cloneSheet(0);
            }
            for (int i = 0; i < listDetail.size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (listDetail.get(i).size() - 1 > 0) {
                    sheet.shiftRows(17, sheet.getLastRowNum(), listDetail.get(i).size() - 1);
                }
                // fill dữ liệu vào file
                int start = 16;
                int number = 16;
                // set header
                SetHeaderExcel(requestReport.getCompanyID(), userDTO, workbook, sheet, 0, 1, 2, 0);
                sheet.getRow(6).getCell(0).setCellValue(Utils.getPeriod(requestReport.getFromDate(), requestReport.getToDate()));
                sheet.getRow(7).getCell(0).setCellValue("Tài khoản: " + (listDetail.get(i).get(0).getAccount() == null ? "" : listDetail.get(i).get(0).getAccount()));
                sheet.getRow(8).getCell(0).setCellValue("Đối tượng: " + (listDetail.get(i).get(0).getAccountingObjectCode() == null ? "" : listDetail.get(i).get(0).getAccountingObjectCode())
                    + (listDetail.get(i).get(0).getAccountingObjectName() == null ? "" : (" - " + listDetail.get(i).get(0).getAccountingObjectName())));
                sheet.getRow(9).getCell(11).setCellValue("Loại tiền: " + requestReport.getCurrencyID());
                if (listDetail.get(i).get(0).getsDDKSoTien() != null && listDetail.get(i).get(0).getsDDKSoTien().doubleValue() < 0) {
                    sheet.getRow(14).getCell(12).setCellValue(listDetail.get(i).get(0).getsDDKSoTien().abs().doubleValue());
                }
                if (listDetail.get(i).get(0).getsDDKSoTien() != null && listDetail.get(i).get(0).getsDDKSoTien().doubleValue() > 0) {
                    sheet.getRow(14).getCell(10).setCellValue(listDetail.get(i).get(0).getsDDKSoTien().doubleValue());
                }
                if (listDetail.get(i).get(0).getsDDKQuyDoi() != null && listDetail.get(i).get(0).getsDDKQuyDoi().doubleValue() < 0) {
                    sheet.getRow(14).getCell(13).setCellValue(listDetail.get(i).get(0).getsDDKQuyDoi().abs().doubleValue());
                }
                if (listDetail.get(i).get(0).getsDDKQuyDoi() != null && listDetail.get(i).get(0).getsDDKQuyDoi().doubleValue() > 0) {
                    sheet.getRow(14).getCell(11).setCellValue(listDetail.get(i).get(0).getsDDKQuyDoi().doubleValue());
                }
                //
                BigDecimal tongPSNSoTien = BigDecimal.ZERO;
                BigDecimal tongPSNQuyDoi = BigDecimal.ZERO;
                BigDecimal tongPSCSoTien = BigDecimal.ZERO;
                BigDecimal tongPSCQuyDoi = BigDecimal.ZERO;
                BigDecimal tongDNSoTien = BigDecimal.ZERO;
                BigDecimal tongDNQuyDoi = BigDecimal.ZERO;
                BigDecimal tongDCSoTien = BigDecimal.ZERO;
                BigDecimal tongDCQuyDoi = BigDecimal.ZERO;

                for (SoTheoDoiThanhToanBangNgoaiTeDTO item : listDetail.get(i)) {
                    tongDNSoTien = item.getDuNoSoTien();
                    tongDNQuyDoi = item.getDuNoQuyDoi();
                    tongDCSoTien = item.getDuCoSoTien();
                    tongDCQuyDoi = item.getDuCoQuyDoi();

                    if (item.getpSNSoTien() == null && item.getpSNQuyDoi() == null && item.getpSCSoTien() == null && item.getpSCQuyDoi() == null){
                        number = number - 1;
                        continue;
                    }

                    Row row = sheet.createRow(start);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellStyle(style);
                    CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
                    cell1.setCellValue(item.getNgayHoachToan() == null ? "" : Utils.convertDate(item.getNgayHoachToan()));

                    Cell cell2 = row.createCell(1);
                    cell2.setCellStyle(style);
                    CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
                    cell2.setCellValue((item.getSoChungTu() == null ? "" : item.getSoChungTu()));

                    Cell cell3 = row.createCell(2);
                    cell3.setCellStyle(style);
                    CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
                    cell3.setCellValue(item.getNgayChungTu() != null ? Utils.convertDate(item.getNgayChungTu()) : "");

                    Cell cell4 = row.createCell(3);
                    cell4.setCellStyle(style);
                    CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
                    cell4.setCellValue(item.getDienGiai() != null ? item.getDienGiai() : "");

                    Cell cell5 = row.createCell(4);
                    cell5.setCellStyle(style);
                    CellUtil.setAlignment(cell5, HorizontalAlignment.LEFT);
                    cell5.setCellValue(item.gettKDoiUng() != null ? item.gettKDoiUng() : "");

                    Cell cell6 = row.createCell(5);
                    cell6.setCellStyle(style);
                    CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
                    if (item.getTyGiaHoiDoai() == null || item.getTyGiaHoiDoai().doubleValue() == 0) {
                        cell6.setCellValue("");
                    } else {
                        cell6.setCellValue(item.getTyGiaHoiDoai().doubleValue());
                    }

                    Cell cell7 = row.createCell(6);
                    cell7.setCellStyle(style);
                    CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                    if (item.getpSNSoTien() == null || item.getpSNSoTien().doubleValue() == 0 ) {
                        cell7.setCellValue("");
                    } else {
                        cell7.setCellValue(item.getpSNSoTien().doubleValue());
                    }

                    Cell cell8 = row.createCell(7);
                    cell8.setCellStyle(style);
                    CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                    if (item.getpSNQuyDoi() == null || item.getpSNQuyDoi().doubleValue() == 0) {
                        cell8.setCellValue("");
                    } else {
                        cell8.setCellValue(item.getpSNQuyDoi().doubleValue());
                    }

                    Cell cell9 = row.createCell(8);
                    cell9.setCellStyle(style);
                    CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                    if (item.getpSCSoTien() == null || item.getpSCSoTien().doubleValue() == 0) {
                        cell9.setCellValue("");
                    } else {
                        cell9.setCellValue(item.getpSCSoTien().doubleValue());
                    }

                    Cell cell10 = row.createCell(9);
                    cell10.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    if (item.getpSCQuyDoi() == null || item.getpSCQuyDoi().doubleValue() == 0) {
                        cell10.setCellValue("");
                    } else {
                        cell10.setCellValue(item.getpSCQuyDoi().doubleValue());
                    }


                    Cell cell11 = row.createCell(10);
                    cell11.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    if (item.getDuNoSoTien() == null || item.getDuNoSoTien().doubleValue() == 0) {
                        cell11.setCellValue("");
                    } else {
                        cell11.setCellValue(item.getDuNoSoTien().doubleValue());
                    }

                    Cell cell12 = row.createCell(11);
                    cell12.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    if (item.getDuNoQuyDoi() == null || item.getDuNoQuyDoi().doubleValue() == 0) {
                        cell12.setCellValue("");
                    } else {
                        cell12.setCellValue(item.getDuNoQuyDoi().doubleValue());
                    }

                    Cell cell13 = row.createCell(12);
                    cell13.setCellStyle(style);
                    CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                    if (item.getDuCoSoTien() == null || item.getDuCoSoTien().doubleValue() == 0) {
                        cell13.setCellValue("");
                    } else {
                        cell13.setCellValue(item.getDuCoSoTien().doubleValue());
                    }

                    Cell cell14 = row.createCell(13);
                    cell14.setCellStyle(style);
                    CellUtil.setAlignment(cell14, HorizontalAlignment.RIGHT);
                    if (item.getDuCoQuyDoi() == null || item.getDuCoQuyDoi().doubleValue() == 0) {
                        cell14.setCellValue("");
                    } else {
                        cell14.setCellValue(item.getDuCoQuyDoi().doubleValue());
                    }

                    start++;

                    if (item.getpSNSoTien() != null) {
                        tongPSNSoTien = tongPSNSoTien.add(item.getpSNSoTien());
                    }
                    if (item.getpSNQuyDoi() != null) {
                        tongPSNQuyDoi = tongPSNQuyDoi.add(item.getpSNQuyDoi());
                    }
                    if (item.getpSCSoTien() != null) {
                        tongPSCSoTien = tongPSCSoTien.add(item.getpSCSoTien());
                    }
                    if (item.getpSCQuyDoi() != null) {
                        tongPSCQuyDoi = tongPSCQuyDoi.add(item.getpSCQuyDoi());
                    }

                }

                Row rowSum = sheet.createRow(number + listDetail.get(i).size());
                rowSum.createCell(0);
                rowSum.getCell(0).setCellStyle(styleB);
                rowSum.createCell(1);
                rowSum.getCell(1).setCellStyle(styleB);
                rowSum.createCell(2);
                rowSum.getCell(2).setCellStyle(styleB);
                rowSum.createCell(3);
                rowSum.getCell(3).setCellStyle(styleB);
                rowSum.getCell(3).setCellValue(" - Cộng số phát sinh");
                rowSum.createCell(4);
                rowSum.getCell(4).setCellStyle(styleB);
                rowSum.createCell(5);
                rowSum.getCell(5).setCellStyle(styleB);

                Cell cell7 = rowSum.createCell(6);
                cell7.setCellStyle(styleB);
                CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
                if (tongPSNSoTien.doubleValue() == 0) {
                    cell7.setCellValue("");
                } else {
                    cell7.setCellValue(tongPSNSoTien.doubleValue());
                }

                Cell cell8 = rowSum.createCell(7);
                cell8.setCellStyle(styleB);
                CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);
                if (tongPSNQuyDoi.doubleValue() == 0) {
                    cell8.setCellValue("");
                } else {
                    cell8.setCellValue(tongPSNQuyDoi.doubleValue());
                }

                Cell cell9 = rowSum.createCell(8);
                cell9.setCellStyle(styleB);
                CellUtil.setAlignment(cell9, HorizontalAlignment.RIGHT);
                if (tongPSCSoTien.doubleValue() == 0) {
                    cell9.setCellValue("");
                } else {
                    cell9.setCellValue(tongPSCSoTien.doubleValue());
                }

                Cell cell10 = rowSum.createCell(9);
                cell10.setCellStyle(styleB);
                CellUtil.setAlignment(cell10, HorizontalAlignment.RIGHT);
                if (tongPSCQuyDoi.doubleValue() == 0) {
                    cell10.setCellValue("");
                } else {
                    cell10.setCellValue(tongPSCQuyDoi.doubleValue());
                }

                rowSum.createCell(10);
                rowSum.getCell(10).setCellStyle(styleB);
                rowSum.createCell(11);
                rowSum.getCell(11).setCellStyle(styleB);
                rowSum.createCell(12);
                rowSum.getCell(12).setCellStyle(styleB);
                rowSum.createCell(13);
                rowSum.getCell(13).setCellStyle(styleB);

                Row rowSumFooter = sheet.createRow(number + 1 + listDetail.get(i).size());
                rowSumFooter.createCell(0);
                rowSumFooter.getCell(0).setCellStyle(styleB);
                rowSumFooter.createCell(1);
                rowSumFooter.getCell(1).setCellStyle(styleB);
                rowSumFooter.createCell(2);
                rowSumFooter.getCell(2).setCellStyle(styleB);
                rowSumFooter.createCell(3);
                rowSumFooter.getCell(3).setCellStyle(styleB);
                rowSumFooter.getCell(3).setCellValue(" - Số dư cuối kỳ");
                rowSumFooter.createCell(4);
                rowSumFooter.getCell(4).setCellStyle(styleB);
                rowSumFooter.createCell(5);
                rowSumFooter.getCell(5).setCellStyle(styleB);
                rowSumFooter.createCell(6);
                rowSumFooter.getCell(6).setCellStyle(styleB);
                rowSumFooter.createCell(7);
                rowSumFooter.getCell(7).setCellStyle(styleB);
                rowSumFooter.createCell(8);
                rowSumFooter.getCell(8).setCellStyle(styleB);
                rowSumFooter.createCell(9);
                rowSumFooter.getCell(9).setCellStyle(styleB);

                Cell cell20 = rowSumFooter.createCell(10);
                cell20.setCellStyle(styleB);
                CellUtil.setAlignment(cell20, HorizontalAlignment.RIGHT);
                if (tongDNSoTien == null || tongDNSoTien.doubleValue() == 0) {
                    cell20.setCellValue("");
                } else {
                    cell20.setCellValue(tongDNSoTien.doubleValue());
                }

                Cell cell21 = rowSumFooter.createCell(11);
                cell21.setCellStyle(styleB);
                CellUtil.setAlignment(cell21, HorizontalAlignment.RIGHT);
                if (tongDNQuyDoi == null || tongDNQuyDoi.doubleValue() == 0) {
                    cell21.setCellValue("");
                } else {
                    cell21.setCellValue(tongDNQuyDoi.doubleValue());
                }

                Cell cell22 = rowSumFooter.createCell(12);
                cell22.setCellStyle(styleB);
                CellUtil.setAlignment(cell22, HorizontalAlignment.RIGHT);
                if (tongDCSoTien == null || tongDCSoTien.doubleValue() == 0) {
                    cell22.setCellValue("");
                } else {
                    cell22.setCellValue(tongDCSoTien.doubleValue());
                }

                Cell cell23 = rowSumFooter.createCell(13);
                cell23.setCellStyle(styleB);
                CellUtil.setAlignment(cell23, HorizontalAlignment.RIGHT);
                if (tongDCQuyDoi == null || tongDCQuyDoi.doubleValue() == 0) {
                    cell23.setCellValue("");
                } else {
                    cell23.setCellValue(tongDCQuyDoi.doubleValue());
                }

                sheet.createRow(number + 3 + listDetail.get(i).size()).createCell(0).setCellValue("- Sổ này có :... trang, đánh số từ trang ... đến trang ...");
                sheet.createRow(number + 4 + listDetail.get(i).size()).createCell(0).setCellValue("- Ngày mở sổ: ..........................................");
                sheet.getRow(number + 3 + listDetail.get(i).size()).getCell(0).setCellStyle(styleDf);
                sheet.getRow(number + 4 + listDetail.get(i).size()).getCell(0).setCellStyle(styleDf);
                SetFooterExcel(workbook, sheet, userDTO, (number + 5 + listDetail.get(i).size()), 10, 1, 5, 10);
                sheet.getRow((number + 6 + listDetail.get(i).size())).getCell(1).setCellValue("Người ghi sổ");
            }
            workbook.write(bos);
            workbook.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return bos.toByteArray();
    }
}
