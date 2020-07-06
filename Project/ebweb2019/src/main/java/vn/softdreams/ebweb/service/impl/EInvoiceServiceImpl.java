package vn.softdreams.ebweb.service.impl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.EInvoice;
import vn.softdreams.ebweb.domain.EInvoiceDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.EInvoiceRepository;
import vn.softdreams.ebweb.service.EInvoiceService;
import vn.softdreams.ebweb.service.SupplierService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_MIV.RequestMIV;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_MIV.RestfullApiMIV;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS.Request_SDS;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS.RestApiService;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV.RequestSIV;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV.ResponeSIV;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV.RestfullApiSIV;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.MIV.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SIV.GetListInvoiceWithDate;
import vn.softdreams.ebweb.service.dto.EInvoice.SIV.*;
import vn.softdreams.ebweb.service.dto.SABillReportDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ReportUtils;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.EInvoice.*;
import static vn.softdreams.ebweb.service.util.Constants.EInvoice.API_VIETTEL.*;
import static vn.softdreams.ebweb.service.util.Constants.EInvoice.Respone.*;
import static vn.softdreams.ebweb.service.util.Constants.EInvoice.SupplierCode.*;

/**
 * @Author Hautv
 * Service Implementation for managing InvoiceType.
 */
@Service
@Transactional
public class EInvoiceServiceImpl implements EInvoiceService {

    private final EInvoiceRepository eInvoiceRepository;
    private final UserService userService;
    private final SupplierService supplierService;

    public EInvoiceServiceImpl(EInvoiceRepository eInvoiceRepository,
                               UserService userService,
                               SupplierService supplierService
    ) {
        this.eInvoiceRepository = eInvoiceRepository;
        this.userService = userService;
        this.supplierService = supplierService;
    }

    @Override
    public Page<EInvoice> findAll(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Page<EInvoice> eInvoices = eInvoiceRepository.findAllByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
        if (eInvoices.getContent().size() > 0) {
            eInvoices.getContent().get(0).setTotal(eInvoiceRepository.sumFindAllByCompanyID(userDTO.getOrganizationUnit().getId()));
        }
        return eInvoices;
    }

    @Override
    public Page<EInvoice> findAll(Pageable pageable, SearchVoucher searchVoucher, Integer typeEInvoice) {
        UserDTO userDTO = userService.getAccount();
        return eInvoiceRepository.findAll(pageable, userDTO.getOrganizationUnit().getId(), searchVoucher, typeEInvoice);
    }

    @Override
    public Optional<EInvoice> findOne(UUID id) {
        Optional<EInvoice> eInvoice = eInvoiceRepository.findById(id);
        if (eInvoice.isPresent()) {
            List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.get().getId(), eInvoice.get().getRefTable());
            eInvoice.get().seteInvoiceDetails(eInvoiceDetails);
            if (!eInvoice.get().getStatusInvoice().equals(8) && !eInvoice.get().getStatusInvoice().equals(-1) && eInvoice.get().getiDAdjustInv() != null) {
                List<EInvoiceDetails> eInvoiceDetailsAjRp = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.get().getiDAdjustInv(), eInvoice.get().getRefTable());
                eInvoice.get().seteInvoiceDetailsAjRp(eInvoiceDetailsAjRp);
            } else if (!eInvoice.get().getStatusInvoice().equals(7) && !eInvoice.get().getStatusInvoice().equals(-1) && eInvoice.get().getiDReplaceInv() != null) {
                List<EInvoiceDetails> eInvoiceDetailsAjRp = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.get().getiDReplaceInv(), eInvoice.get().getRefTable());
                eInvoice.get().seteInvoiceDetailsAjRp(eInvoiceDetailsAjRp);
            }
        }
        return eInvoice;
    }

    @Override
    public Respone_SDS createEInvoice(List<UUID> uuids, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(uuids);
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllDetailsByListID(uuids);
        for (EInvoice eInvoice : eInvoices) {
            eInvoice.seteInvoiceDetails(eInvoiceDetails.stream().filter(n -> n.geteInvoiceID().equals(eInvoice.getId())).collect(Collectors.toList()));
        }
        Map<String, List<EInvoice>> grpInvoiceTemplate =
            eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceTemplate()));
        Map<String, List<EInvoice>> grpInvoiceSeries =
            eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceSeries()));
        if (grpInvoiceTemplate.size() > 1 || grpInvoiceSeries.size() > 1) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Chỉ được tạo hóa đơn có cùng mẫu số hóa đơn và ký hiệu hóa đơn");
            return respone_sds;
        }
        Request_SDS request_sds = new Request_SDS();
        request_sds.setPattern(eInvoices.get(0).getInvoiceTemplate());
        request_sds.setSerial(eInvoices.get(0).getInvoiceSeries());
        String xml = createXMLData(eInvoices, userDTO);
        request_sds.setXmlData(xml);
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.CREATE_INVOICE_DRAFT, userDTO);

        return respone_sds;
    }

    @Override
    public Respone_SDS createEInoviceAdjusted(UUID uuid) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            Optional<EInvoice> e_Invoice = eInvoiceRepository.findById(uuid);
            if (e_Invoice.isPresent()) {
                EInvoice eInvoice = e_Invoice.get();
                switch (NCCDV) {
                    case SDS:
                        respone_sds = createEInoviceAdjustedSDS(eInvoice, userDTO);
                        break;
                    case SIV:
                        // Không có tạo nháp
                        break;
                    case MIV:
                        respone_sds = createEInoviceAdjustedMIV(eInvoice, userDTO);
                        break;
                    case VNPT:
                        respone_sds = createEInoviceAdjustedVNPT(eInvoice, userDTO);
                        break;
                }
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    private String createXMLDataEInoviceAdjusted(EInvoice eInvoice, UserDTO userDTO, List<EInvoiceDetails> eInvoiceDetails) {
        String result = null;
        Boolean currentCurrency_bool = userDTO.getOrganizationUnit().getCurrencyID().equals(eInvoice.getCurrencyID());
        AdjustInv adjustInv = new AdjustInv();
        adjustInv.setKey(Utils.uuidConvertToGUID(eInvoice.getId()));
//        adjustInv.setInvNo(eInvoice.getInvoiceNo());
        adjustInv.setCusCode(getString(eInvoice.getAccountingObjectCode()));
        adjustInv.setBuyer(getString(eInvoice.getContactName()));
        adjustInv.setCusName(getString(eInvoice.getAccountingObjectName()));
        adjustInv.setCusAddress(getString(eInvoice.getAccountingObjectAddress()));
        adjustInv.setCusBankName(getString(eInvoice.getAccountingObjectBankName()));
        adjustInv.setCusBankNo(getString(eInvoice.getAccountingObjectBankAccount()));
        adjustInv.setCusPhone(eInvoice.getContactMobile());
        adjustInv.setCusTaxCode(eInvoice.getCompanyTaxCode());
        adjustInv.setEmail(eInvoice.getAccountingObjectEmail());
        adjustInv.setPaymentMethod(getString(eInvoice.getPaymentMethod()));
        adjustInv.setArisingDate(eInvoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        adjustInv.setExchangeRate(eInvoice.getExchangeRate());
        adjustInv.setCurrencyUnit(eInvoice.getCurrencyID());
        adjustInv.setType(eInvoice.getType());
        List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool);
        adjustInv.getProducts().setProduct(lstProduct);
        if (lstProduct.size() > 0) {
            adjustInv.setVATRate(lstProduct.get(0).getVATRate());
        } else {
            adjustInv.setVATRate(-1);
        }
        BigDecimal Amount = BigDecimal.ZERO;
        BigDecimal Total = BigDecimal.ZERO;
        BigDecimal VATAmount = BigDecimal.ZERO;
        for (ProductSDS_DTO productSDS_dto : adjustInv.getProducts().getProduct()
        ) {
            Amount = Amount.add(productSDS_dto.getAmount());
            Total = Total.add(productSDS_dto.getTotal());
            VATAmount = VATAmount.add(productSDS_dto.getVATAmount());
        }
        if (currentCurrency_bool) {
            adjustInv.setAmount(Utils.round(Amount));
            adjustInv.setTotal(Utils.round(Total));
            adjustInv.setVATAmount(Utils.round(VATAmount));
        } else {
            adjustInv.setAmount(Utils.round(Amount, 2));
            adjustInv.setTotal(Utils.round(Total, 2));
            adjustInv.setVATAmount(Utils.round(VATAmount, 2));
        }
        adjustInv.setAmountInWords(Utils.GetAmountInWords(Amount, adjustInv.getCurrencyUnit(), userDTO));
//                String xml = Utils.ObjectToXML(invoicesSDS_dto);
        result = Utils.jaxbObjectToXML(adjustInv);
        return result;
    }

    private String createXMLDataEInoviceReplaced(EInvoice eInvoice, UserDTO userDTO, List<EInvoiceDetails> eInvoiceDetails) {
        String result = null;
        ReplaceInv replaceInv = new ReplaceInv();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        replaceInv.setKey(Utils.uuidConvertToGUID(eInvoice.getId()));
//        replaceInv.setInvNo(eInvoice.getInvoiceNo());
        replaceInv.setCusCode(getString(eInvoice.getAccountingObjectCode()));
        replaceInv.setBuyer(getString(eInvoice.getContactName()));
        replaceInv.setCusName(getString(eInvoice.getAccountingObjectName()));
        replaceInv.setCusAddress(getString(eInvoice.getAccountingObjectAddress()));
        replaceInv.setCusBankName(getString(eInvoice.getAccountingObjectBankName()));
        replaceInv.setCusBankNo(getString(eInvoice.getAccountingObjectBankAccount()));
        replaceInv.setCusPhone(eInvoice.getContactMobile());
        replaceInv.setCusTaxCode(eInvoice.getCompanyTaxCode());
        replaceInv.setEmail(eInvoice.getAccountingObjectEmail());
        replaceInv.setPaymentMethod(getString(eInvoice.getPaymentMethod()));
        replaceInv.setArisingDate(eInvoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        replaceInv.setExchangeRate(eInvoice.getExchangeRate());
        replaceInv.setCurrencyUnit(eInvoice.getCurrencyID());
        List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool);
        replaceInv.getProducts().setProduct(lstProduct);
        if (lstProduct.size() > 0) {
            replaceInv.setVATRate(lstProduct.get(0).getVATRate());
        } else {
            replaceInv.setVATRate(-1);
        }
        BigDecimal Amount = BigDecimal.ZERO;
        BigDecimal Total = BigDecimal.ZERO;
        BigDecimal VATAmount = BigDecimal.ZERO;
        for (ProductSDS_DTO productSDS_dto : replaceInv.getProducts().getProduct()
        ) {
            Amount = Amount.add(productSDS_dto.getAmount());
            Total = Total.add(productSDS_dto.getTotal());
            VATAmount = VATAmount.add(productSDS_dto.getVATAmount());
        }
        if (currentCurrency_bool) {
            replaceInv.setAmount(Utils.round(Amount));
            replaceInv.setTotal(Utils.round(Total));
            replaceInv.setVATAmount(Utils.round(VATAmount));
        } else {
            replaceInv.setAmount(Utils.round(Amount, 2));
            replaceInv.setTotal(Utils.round(Total, 2));
            replaceInv.setVATAmount(Utils.round(VATAmount, 2));
        }
        replaceInv.setAmountInWords(Utils.GetAmountInWords(Amount, replaceInv.getCurrencyUnit(), userDTO));
        result = Utils.jaxbObjectToXML(replaceInv);
        return result;
    }

    private Respone_SDS createEInoviceAdjustedSDS(EInvoice eInvoice, UserDTO userDTO) {
        Respone_SDS respone_sds;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        String xml = createXMLDataEInoviceAdjusted(eInvoice, userDTO, eInvoiceDetails);
        Request_SDS request_sds = new Request_SDS();
        request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDAdjustInv()));
        request_sds.setPattern(eInvoice.getInvoiceTemplate());
        request_sds.setSerial(eInvoice.getInvoiceSeries());
        request_sds.setXmlData(xml);
        String api = Constants.EInvoice.API_SDS.IMPORT_UNSIGNED_ADJUSTMENT;
        respone_sds = RestApiService.post(request_sds, api, userDTO);
        return respone_sds;
    }

    private Respone_SDS createEInoviceAdjustedMIV(EInvoice eInvoice, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        eInvoiceDetails.sort(Comparator.comparingInt(EInvoiceDetails::getOrderPriority));
        eInvoice.seteInvoiceDetails(eInvoiceDetails);
        RequestMIV requestMIV = new RequestMIV();
        setDefaultRequestMIV(requestMIV, userDTO);
        String jsonData = "";
        switch (eInvoice.getType()) {
            case Type.DieuChinhThongTin:
                requestMIV.setApi(API_MIV.DcDinhdanh);
                AdjustInformation_MIV adjustInformation_miv = createHDDCDinhDanh(eInvoice);
                jsonData = Utils.ObjectToJSON(adjustInformation_miv);
                break;
            case Type.DieuChinhTang:
                requestMIV.setApi(API_MIV.DcTang);
                AdjustIncrease_MIV adjustIncrease_miv = createHDDCTang(eInvoice);
                jsonData = Utils.ObjectToJSONIgnoreNull(adjustIncrease_miv);
                break;
            case Type.DieuChinhGiam:
                requestMIV.setApi(API_MIV.DcGiam);
                AdjustReduction_MIV adjustReduction_miv = createHDDCGiam(eInvoice);
                jsonData = Utils.ObjectToJSONIgnoreNull(adjustReduction_miv);
                break;
        }
        requestMIV.setData(jsonData);
        ResponeAdjust responeMiv = RestfullApiMIV.postAdjust(requestMIV);
        if (StringUtils.isEmpty(responeMiv.getError())) {
            respone_sds.setStatus(Success);
            String invoiceNo = responeMiv.getOk().getInv_invoiceNumber();
            respone_sds.setInvoiceNo(invoiceNo);
            UUID idMIV = responeMiv.getOk().getInv_InvoiceAuth_id();
            eInvoiceRepository.updateAfterCreateInvoiceMIV(eInvoice.getId(), responeMiv.getOk().getInv_invoiceNumber(), responeMiv.getOk().getInv_InvoiceAuth_id().toString());
        } else {
            respone_sds.setStatus(Fail);
        }
        return respone_sds;
    }

    private AdjustInformation_MIV createHDDCDinhDanh(EInvoice eInvoice) {
        AdjustInformation_MIV adjustInformation_miv = new AdjustInformation_MIV();
        EInvoice eInvoiceR = eInvoiceRepository.findById(eInvoice.getiDAdjustInv()).get();
        adjustInformation_miv.setInv_InvoiceAuth_id(Utils.uuidConvertToGUID(eInvoiceR.getiDMIV()));
        adjustInformation_miv.setInv_invoiceIssuedDate(getDateString(eInvoiceR.getInvoiceDate(), "yyyy-MM-dd"));
        adjustInformation_miv.setSovb(eInvoice.getDocumentNo());
        adjustInformation_miv.setNgayvb(getDateString(eInvoice.getDocumentDate(), "yyyy-MM-dd"));
        adjustInformation_miv.setGhi_chu(eInvoice.getDocumentNote());
        if (eInvoice.getContactName() != eInvoiceR.getContactName()) {
            adjustInformation_miv.setInv_buyerDisplayName(eInvoice.getContactName());
        }
        if (eInvoice.getAccountingObjectCode() != eInvoiceR.getAccountingObjectCode()) {
            adjustInformation_miv.setMa_dt(eInvoice.getAccountingObjectCode());
        }
        if (eInvoice.getAccountingObjectName() != eInvoiceR.getAccountingObjectName()) {
            adjustInformation_miv.setInv_buyerLegalName(eInvoice.getAccountingObjectName());
        }
        if (eInvoice.getAccountingObjectAddress() != eInvoiceR.getAccountingObjectAddress()) {
            adjustInformation_miv.setInv_buyerAddressLine(eInvoice.getAccountingObjectAddress());
        }
        if (eInvoice.getEmail() != eInvoiceR.getEmail()) {
            adjustInformation_miv.setInv_buyerEmail(eInvoice.getEmail());
        }
        if (eInvoice.getAccountingObjectBankAccount() != eInvoiceR.getAccountingObjectBankAccount()) {
            adjustInformation_miv.setInv_buyerBankAccount(eInvoice.getAccountingObjectBankAccount());
        }
        if (eInvoice.getAccountingObjectBankName() != eInvoiceR.getAccountingObjectBankName()) {
            eInvoice.setAccountingObjectBankName(eInvoice.getAccountingObjectName());
        }
        return adjustInformation_miv;
    }

    private AdjustReduction_MIV createHDDCGiam(EInvoice eInvoice) {
        AdjustReduction_MIV adjustReduction_miv = new AdjustReduction_MIV();
        EInvoice eInvoiceR = eInvoiceRepository.findById(eInvoice.getiDAdjustInv()).get();
        List<EInvoiceDetails> detailsR = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getiDAdjustInv(), eInvoice.getRefTable());
        adjustReduction_miv.setInv_InvoiceAuth_id(Utils.uuidConvertToGUID(eInvoiceR.getiDMIV()));
        adjustReduction_miv.setInv_invoiceIssuedDate(getDateString(eInvoiceR.getInvoiceDate(), "yyyy-MM-dd"));
        adjustReduction_miv.setSovb(eInvoice.getDocumentNo());
        adjustReduction_miv.setNgayvb(getDateString(eInvoice.getDocumentDate(), "yyyy-MM-dd"));
        adjustReduction_miv.setGhi_chu(eInvoice.getDocumentNote());
        String tieuthuc = "";
        for (EInvoiceDetails eInvoiceDetails : eInvoice.geteInvoiceDetails()) {
            DataAdjust dataAdjust = new DataAdjust();
            dataAdjust.setInv_itemCode(eInvoiceDetails.getMaterialGoodsCode());
            dataAdjust.setInv_quantity(eInvoiceDetails.getQuantity());
            dataAdjust.setInv_unitPrice(eInvoiceDetails.getUnitPrice());
            dataAdjust.setMa_thue(getVATRate(eInvoiceDetails.getvATRate(), null).toString());
            dataAdjust.setNv_discountPercentage(eInvoiceDetails.getDiscountRate());
            dataAdjust.setInv_discountAmount(eInvoiceDetails.getDiscountAmountOriginal());

            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getQuantity().compareTo(eInvoiceDetails.getQuantity()) != 0)) {
                tieuthuc += "Số lượng";
            }
            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getUnitPriceOriginal().compareTo(eInvoiceDetails.getUnitPriceOriginal()) != 0)) {
                if (!StringUtils.isEmpty(tieuthuc)) {
                    tieuthuc += ", đơn giá";
                } else {
                    tieuthuc += "Đơn giá";
                }
            }
            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getvATRate().compareTo(eInvoiceDetails.getvATRate()) != 0)) {
                if (!StringUtils.isEmpty(tieuthuc)) {
                    tieuthuc += ", thuế suất";
                } else {
                    tieuthuc += "Thuế suất";
                }
            }
            dataAdjust.setTieu_thuc(tieuthuc);
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("số lượng")) {
                dataAdjust.setInv_quantity(null);
            }
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("đơn giá")) {
                dataAdjust.setInv_unitPrice(null);
            }
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("thuế suất")) {
                dataAdjust.setMa_thue(null);
            }
            adjustReduction_miv.getData().add(dataAdjust);
        }
        return adjustReduction_miv;
    }

    private AdjustIncrease_MIV createHDDCTang(EInvoice eInvoice) {
        AdjustIncrease_MIV adjustIncrease_miv = new AdjustIncrease_MIV();
        EInvoice eInvoiceR = eInvoiceRepository.findById(eInvoice.getiDAdjustInv()).get();
        List<EInvoiceDetails> detailsR = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getiDAdjustInv(), eInvoice.getRefTable());
        adjustIncrease_miv.setInv_InvoiceAuth_id(Utils.uuidConvertToGUID(eInvoiceR.getiDMIV()));
        adjustIncrease_miv.setInv_invoiceIssuedDate(getDateString(eInvoiceR.getInvoiceDate(), "yyyy-MM-dd"));
        adjustIncrease_miv.setSovb(eInvoice.getDocumentNo());
        adjustIncrease_miv.setNgayvb(getDateString(eInvoice.getDocumentDate(), "yyyy-MM-dd"));
        adjustIncrease_miv.setGhi_chu(eInvoice.getDocumentNote());
        String tieuthuc = "";
        for (EInvoiceDetails eInvoiceDetails : eInvoice.geteInvoiceDetails()) {
            DataAdjust dataAdjust = new DataAdjust();
            dataAdjust.setInv_itemCode(eInvoiceDetails.getMaterialGoodsCode());
            dataAdjust.setInv_quantity(eInvoiceDetails.getQuantity());
            dataAdjust.setInv_unitPrice(eInvoiceDetails.getUnitPrice());
            dataAdjust.setMa_thue(getVATRate(eInvoiceDetails.getvATRate(), null).toString());
            dataAdjust.setNv_discountPercentage(eInvoiceDetails.getDiscountRate());
            dataAdjust.setInv_discountAmount(eInvoiceDetails.getDiscountAmountOriginal());

            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getQuantity().compareTo(eInvoiceDetails.getQuantity()) != 0)) {
                tieuthuc += "Số lượng";
            }
            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getUnitPriceOriginal().compareTo(eInvoiceDetails.getUnitPriceOriginal()) != 0)) {
                if (!StringUtils.isEmpty(tieuthuc)) {
                    tieuthuc += ", đơn giá";
                } else {
                    tieuthuc += "Đơn giá";
                }
            }
            if (detailsR.stream().anyMatch(n -> n.getMaterialGoodsCode().equals(eInvoiceDetails.getMaterialGoodsCode()) &&
                n.getvATRate().compareTo(eInvoiceDetails.getvATRate()) != 0)) {
                if (!StringUtils.isEmpty(tieuthuc)) {
                    tieuthuc += ", thuế suất";
                } else {
                    tieuthuc += "Thuế suất";
                }
            }
            dataAdjust.setTieu_thuc(tieuthuc);
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("số lượng")) {
                dataAdjust.setInv_quantity(null);
            }
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("đơn giá")) {
                dataAdjust.setInv_unitPrice(null);
            }
            if (!dataAdjust.getTieu_thuc().toLowerCase().contains("thuế suất")) {
                dataAdjust.setMa_thue(null);
            }
            adjustIncrease_miv.getData().add(dataAdjust);
        }
        return adjustIncrease_miv;
    }

    private Respone_SDS createEInoviceAdjustedVNPT(EInvoice eInvoice, UserDTO userDTO) {
        Respone_SDS respone_sds;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        String xml = createXMLDataEInoviceAdjusted(eInvoice, userDTO, eInvoiceDetails);
        Request_SDS request_sds = new Request_SDS();
        request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDAdjustInv()));
        request_sds.setPattern(eInvoice.getInvoiceTemplate());
        request_sds.setSerial(eInvoice.getInvoiceSeries());
        request_sds.setXmlData(xml);
        String api = Constants.EInvoice.API_SDS.IMPORT_UNSIGNED_ADJUSTMENT;
        respone_sds = RestApiService.post(request_sds, api, userDTO);
        return respone_sds;
    }

    @Override
    public Respone_SDS createEInoviceReplaced(UUID uuid) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Optional<EInvoice> e_Invoice = eInvoiceRepository.findById(uuid);
            if (e_Invoice.isPresent()) {
                EInvoice eInvoice = e_Invoice.get();
                List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
                String xml = createXMLDataEInoviceReplaced(eInvoice, userDTO, eInvoiceDetails);
                Request_SDS request_sds = new Request_SDS();
                request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()));
                request_sds.setPattern(eInvoice.getInvoiceTemplate());
                request_sds.setSerial(eInvoice.getInvoiceSeries());
                request_sds.setXmlData(xml);
                String api = Constants.EInvoice.API_SDS.IMPORT_UNSIGNED_REPLACEMENT;
                respone_sds = RestApiService.post(request_sds, api, userDTO);
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    @Override
    public Respone_SDS sendMail(Map<UUID, String> ikeyEmail) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Request_SDS request_sds = new Request_SDS();
            Map<UUID, String> ikeyEmailCVUUID = new HashMap<>();
            for (Map.Entry<UUID, String> entry : ikeyEmail.entrySet()) {
                ikeyEmailCVUUID.put(Utils.uuidConvertToGUID(entry.getKey()), entry.getValue());
            }
            request_sds.setIkeyEmail(ikeyEmailCVUUID);
            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.SEND_MAIL, userDTO);
            if (respone_sds.getStatus().equals(2)) {
                List<KeyInvoiceMsg> keyInvoiceMsgs = new ArrayList<>();
                for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceMsg().entrySet()) {
                    if (entry.getValue().contains("Lỗi")) {
                        KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                        keyInvoiceMsg.setId(Utils.uuidConvertToGUID(entry.getKey()));
                        keyInvoiceMsg.setMessage(entry.getValue());
                        ikeyEmail.remove(Utils.uuidConvertToGUID(entry.getKey()));
                        keyInvoiceMsgs.add(keyInvoiceMsg);
                    }
                }
                respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgs);
                if (ikeyEmail.size() > 0) {
                    eInvoiceRepository.updateSendMail(ikeyEmail);
                }
            }
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    @Override
    public InformationVoucherDTO getInformationVoucherByID(UUID id) {
        return eInvoiceRepository.getInformationVoucherByID(id);
    }

    @Override
    public String baoCaoTinhHinhSDHD(RequestReport requestReport) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setFromDate(LocalDateToString(requestReport.getFromDate()));
            request_sds.setToDate(LocalDateToString(requestReport.getToDate()));
            Respone_SDS respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.BAO_CAO.TINH_HINH_SDHD, userDTO);
            if (!respone_sds.getStatus().equals(Success)) {
                throw new BadRequestAlertException(respone_sds.getMessage(), "EInvoice", "notExist");
            }
            return respone_sds.getHtml();
        } else {
            throw new BadRequestAlertException(Constants.EInvoice.CHUA_TICH_HOP, "", "");
        }
    }

    @Override
    public String bangKeHDChungTuHHDV(RequestReport requestReport) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {

        } else {

        }
        return null;
    }

    @Override
    public String baoCaoDoanhThuTheoSP(RequestReport requestReport) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {

        } else {

        }
        return null;
    }

    @Override
    public String baoCaoTheoDoanhThuTheoBenMua(RequestReport requestReport) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {

        } else {

        }
        return null;
    }

    @Override
    public Respone_SDS loadAndUpdateDataFromMIV() {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        List<EInvoice> lsEInvoices = eInvoiceRepository.findAllByCompanyIDAndIDMIVNotNull(userDTO.getOrganizationUnit().getId());
        if (lsEInvoices.size() > 0) {
            RequestMIV requestMIV = new RequestMIV();
            setDefaultRequestMIV(requestMIV, userDTO);
            requestMIV.setApi(API_MIV.GetInfoInvoice);
            GetStatusInvoice_MIV getStatusInvoice_miv = new GetStatusInvoice_MIV();
            List<UUID> lst = lsEInvoices.stream().map(n -> Utils.uuidConvertToGUID(n.getiDMIV())).collect(Collectors.toList());
            String data = "";
            for (UUID id : lst) {
                data += "'" + id.toString() + "',";
            }
            data = data.substring(0, data.length() - 1);
            getStatusInvoice_miv.setData(data);
            String jsonData = Utils.ObjectToJSON(getStatusInvoice_miv);
            requestMIV.setData(jsonData);
            DataStatus_MIV dataStatus_miv = RestfullApiMIV.postGetStatusMIV(requestMIV);
            Map<UUID, String> keyInvoiceNoSigned = new HashMap<>();
            Map<UUID, String> keyInvoiceNoAdjusted = new HashMap<>();
            List<KeyInvoiceNo> keyInvoiceNoDTOCancelInvoice = new ArrayList<>();
            if (dataStatus_miv.getData() != null) {
                for (Data dt : dataStatus_miv.getData()) {
                    EInvoice eInvoice = lsEInvoices.stream().filter(n -> n.getiDMIV().equals(Utils.uuidConvertToGUID(dt.getInv_InvoiceAuth_id()))).findFirst().get();
                    switch (eInvoice.getStatusInvoice()) {
                        case HD_MOI_TAO_LAP:
                            if (getStatusInvoiceMIV(dt) == HD_CO_CHU_KY_SO) {
                                keyInvoiceNoSigned.put(Utils.uuidConvertToGUID(eInvoice.getId()), dt.getInv_invoiceNumber());
                            } else if (getStatusInvoiceMIV(dt) == HD_HUY) {
                                KeyInvoiceNo keyInvoiceNo1 = new KeyInvoiceNo();
                                keyInvoiceNo1.setId(eInvoice.getId());
                                keyInvoiceNo1.setNo(dt.getInv_invoiceNumber());
                                keyInvoiceNoDTOCancelInvoice.add(keyInvoiceNo1);
                            }
                            break;
                        case HD_MOI_TAO_LAP_DIEU_CHINH:
                            if (getStatusInvoiceMIV(dt) == HD_CO_CHU_KY_SO) {
                                keyInvoiceNoAdjusted.put(Utils.uuidConvertToGUID(eInvoice.getId()), dt.getInv_invoiceNumber());
                            }
                            break;
                        case HD_CO_CHU_KY_SO:
                            if (getStatusInvoiceMIV(dt) == HD_HUY) {
                                KeyInvoiceNo keyInvoiceNo1 = new KeyInvoiceNo();
                                keyInvoiceNo1.setId(eInvoice.getId());
                                keyInvoiceNo1.setNo(dt.getInv_invoiceNumber());
                                keyInvoiceNoDTOCancelInvoice.add(keyInvoiceNo1);
                            }
                            break;
                    }
                    if (keyInvoiceNoSigned.size() > 0) {
                        respone_sds = new Respone_SDS();
                        respone_sds.getData().setKeyInvoiceNo(keyInvoiceNoSigned);
                        eInvoiceRepository.updateAfterPublishInvoice(respone_sds);
                    }
                    if (keyInvoiceNoDTOCancelInvoice.size() > 0) {
                        respone_sds = new Respone_SDS();
                        respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTOCancelInvoice);
                        eInvoiceRepository.updateAfterCancelInvoice(respone_sds);
                    }
                    if (keyInvoiceNoAdjusted.size() > 0) {
                        respone_sds = new Respone_SDS();
                        respone_sds.getData().setKeyInvoiceNo(keyInvoiceNoAdjusted);
                        eInvoiceRepository.updateAfterPublishInvoiceAdjusted(respone_sds, lsEInvoices);
                    }
                    respone_sds.setStatus(Success);
                }
            }
        } else {

        }
        return respone_sds;
    }

    @Override
    public Respone_SDS loadDataToken() {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (MIV.equals(NCCDV)) {
            RequestMIV requestMIV = new RequestMIV();
            String userName = Utils.getEI_TenDangNhap(userDTO);
            String password = Utils.getEI_MatKhau(userDTO);
            String path = Utils.getEI_Path(userDTO);
            Login_MIV login_miv = new Login_MIV();
            login_miv.setUsername(userName);
            login_miv.setPassword(password);
            login_miv.setMa_dvcs("VP");
            requestMIV.setUrl(path);

            String jsonData = Utils.ObjectToJSON(login_miv);
            requestMIV.setData(jsonData);
            requestMIV.setApi(API_MIV.Login);
            Respone_MIV responeMiv = RestfullApiMIV.post(requestMIV);
            if (!StringUtils.isEmpty(responeMiv.getToken())) {
                respone_sds.setStatus(Success);
                eInvoiceRepository.updateEISystemOptionTokenMIV(userDTO.getOrganizationUnit().getId(), responeMiv.getToken());
            } else {
                respone_sds.setStatus(Constants.EInvoice.Respone.Fail);
            }
        }
        return respone_sds;
    }

    private Integer getStatusInvoiceMIV(Data data) {
        switch (data.getTrang_thai()) {
            case DataMIV.StatusInvoice.daKy:
                return HD_CO_CHU_KY_SO;
            case DataMIV.StatusInvoice.choKy:
                return HD_MOI_TAO_LAP;
            case DataMIV.StatusInvoice.xoaBo:
                return HD_HUY;
        }
        return HD_MOI_TAO_LAP;
    }


    @Override
    public Respone_SDS createEInvoice(UUID id) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            Optional<EInvoice> e_Invoice = eInvoiceRepository.findById(id);
            if (e_Invoice.isPresent()) {
                switch (NCCDV) {
                    case SDS:
                        respone_sds = createEInvoiceSDS(e_Invoice, userDTO, currentCurrency);
                        break;
                    case SIV:
//                        result = createEInvoiceSIV(e_Invoice, userDTO, currentCurrency);
                        break;
                    case MIV:
                        respone_sds = createEInvoiceMIV(e_Invoice, userDTO);
                        break;
                    case VNPT:
                        break;
                }
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    private Respone_SDS createEInvoiceSDS(Optional<EInvoice> e_Invoice, UserDTO userDTO, String currentCurrency) {
        InvoicesSDS_DTO invoicesSDS_dto = new InvoicesSDS_DTO();
        Respone_SDS respone_sds = new Respone_SDS();
        List<InvSDS_DTO> Invs = new ArrayList<>();
        EInvoice eInvoice = e_Invoice.get();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        if (eInvoiceDetails.size() > 0) {
            EInvoiceSDS_DTO eInvoiceSDS_dto = new EInvoiceSDS_DTO();
            eInvoiceSDS_dto.setKey(Utils.uuidConvertToGUID(eInvoice.getId()));
            eInvoiceSDS_dto.setInvNo(eInvoice.getInvoiceNo());
            eInvoiceSDS_dto.setCusCode(getString(eInvoice.getAccountingObjectCode()));
            eInvoiceSDS_dto.setBuyer(getString(eInvoice.getContactName()));
            eInvoiceSDS_dto.setCusName(getString(eInvoice.getAccountingObjectName()));
            eInvoiceSDS_dto.setCusAddress(getString(eInvoice.getAccountingObjectAddress()));
            eInvoiceSDS_dto.setCusBankName(getString(eInvoice.getAccountingObjectBankName()));
            eInvoiceSDS_dto.setCusBankNo(getString(eInvoice.getAccountingObjectBankAccount()));
            eInvoiceSDS_dto.setCusPhone(eInvoice.getContactMobile());
            eInvoiceSDS_dto.setCusTaxCode(eInvoice.getCompanyTaxCode());
            eInvoiceSDS_dto.setEmail(eInvoice.getAccountingObjectEmail());
            eInvoiceSDS_dto.setPaymentMethod(getString(eInvoice.getPaymentMethod()));
            eInvoiceSDS_dto.setArisingDate(eInvoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            eInvoiceSDS_dto.setExchangeRate(eInvoice.getExchangeRate());
            eInvoiceSDS_dto.setCurrencyUnit(eInvoice.getCurrencyID());
            List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool);
            eInvoiceSDS_dto.getProducts().setProduct(lstProduct);
            if (lstProduct.size() > 0) {
                eInvoiceSDS_dto.setVATRate(lstProduct.get(0).getVATRate());
            } else {
                eInvoiceSDS_dto.setVATRate(-1);
            }
            BigDecimal Amount = BigDecimal.ZERO;
            BigDecimal Total = BigDecimal.ZERO;
            BigDecimal VATAmount = BigDecimal.ZERO;
            for (ProductSDS_DTO productSDS_dto : eInvoiceSDS_dto.getProducts().getProduct()) {
                Amount = Amount.add(productSDS_dto.getAmount());
                Total = Total.add(productSDS_dto.getTotal());
                VATAmount = VATAmount.add(productSDS_dto.getVATAmount());
            }
            if (currentCurrency_bool) {
                eInvoiceSDS_dto.setAmount(Utils.round(Amount));
                eInvoiceSDS_dto.setTotal(Utils.round(Total));
                eInvoiceSDS_dto.setVATAmount(Utils.round(VATAmount));
            } else {
                eInvoiceSDS_dto.setAmount(Utils.round(Amount, 2));
                eInvoiceSDS_dto.setTotal(Utils.round(Total, 2));
                eInvoiceSDS_dto.setVATAmount(Utils.round(VATAmount, 2));
            }
            eInvoiceSDS_dto.setAmountInWords(Utils.GetAmountInWords(Amount, eInvoiceSDS_dto.getCurrencyUnit(), userDTO));
            InvSDS_DTO invSDS_dto = new InvSDS_DTO();
            invSDS_dto.setInvoice(eInvoiceSDS_dto);
            Invs.add(invSDS_dto);
            invoicesSDS_dto.setInv(Invs);
//                String xml = Utils.ObjectToXML(invoicesSDS_dto);
            String xml = Utils.jaxbObjectToXML(invoicesSDS_dto);
//                xml = xml.substring(xml.indexOf('\n') + 1); // Xóa dòng đầu <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoice.getInvoiceTemplate());
            request_sds.setSerial(eInvoice.getInvoiceSeries());
            request_sds.setXmlData(xml);
            String api = "";
            if (eInvoice.getStatusInvoice().equals(6)) {
                api = Constants.EInvoice.API_SDS.IMPORT_RESERVED_INVOICE;
            } else {
                api = Constants.EInvoice.API_SDS.CREATE_INVOICE_DRAFT;
            }
            respone_sds = RestApiService.post(request_sds, api, userDTO);
        } else {
            respone_sds.setStatus(Fail);
        }
        return respone_sds;
    }

    private String createEInvoiceSIV(Optional<EInvoice> e_Invoice, UserDTO userDTO, String currentCurrency) {
        String result = null;
        EInvoice eInvoice = e_Invoice.get();
        EInvoiceVietTel eInvoiceVietTel = new EInvoiceVietTel();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        int roundAmountOriginal = 2;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        // Thông tin người bán
        SellerInfo sellerInfo = new SellerInfo();

        // Thông tin người mua
        BuyerInfo buyerInfo = new BuyerInfo();
        buyerInfo.setBuyerCode(getString(eInvoice.getAccountingObjectCode()));
        buyerInfo.setBuyerName(getString(eInvoice.getContactName()));
        buyerInfo.setBuyerLegalName(getString(eInvoice.getAccountingObjectName()));
        buyerInfo.setBuyerAddressLine(getString(eInvoice.getAccountingObjectAddress()));
        buyerInfo.setBuyerBankName(getString(eInvoice.getAccountingObjectBankName()));
        buyerInfo.setBuyerBankAccount(getString(eInvoice.getAccountingObjectBankAccount()));
        buyerInfo.setBuyerPhoneNumber(eInvoice.getContactMobile());
        buyerInfo.setBuyerTaxCode(eInvoice.getCompanyTaxCode());
        buyerInfo.setBuyerEmail(eInvoice.getAccountingObjectEmail());
        /*buyerInfo.setBuyerIdType("1"); // Số chứng mminh
        buyerInfo.setBuyerIdNo(eInvoice.getAcc);*/

        // Thông tin hóa đơn
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setTransactionUuid(Utils.uuidConvertToGUID(eInvoice.getId()));
        invoiceInfo.setTemplateCode(eInvoice.getInvoiceTemplate());
        invoiceInfo.setInvoiceSeries(eInvoice.getInvoiceSeries());
        invoiceInfo.setInvoiceType(eInvoice.getInvoiceTypeCode());
        invoiceInfo.setCurrencyCode(eInvoice.getCurrencyID());
        invoiceInfo.setAdjustmentType("1");
        invoiceInfo.setPaymentStatus(true); // trang thái thanh toán hóa đơn
        invoiceInfo.setCusGetInvoiceRight(true); // Cho khách hàng xem hóa đơn trong Quản lý hóa đơn
        //objInvoice.certificateSerial = certificateSerial;
        invoiceInfo.setInvoiceIssuedDate(getTimeUTC(eInvoice.getRefDateTime()));
        invoiceInfo.setExchangeRate(eInvoice.getExchangeRate());

        // Thông tin sản phẩm
        List<ItemInfo> lstItemInfo = new ArrayList<>();
        List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool, SIV);
        lstItemInfo.addAll(convertListProductSDS(lstProduct));

        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        eInvoiceVietTel.setBuyerInfo(buyerInfo);
        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        Payments payments = new Payments();
        payments.setPaymentMethodName(eInvoice.getPaymentMethod());
        eInvoiceVietTel.getPayments().add(payments);
        eInvoiceVietTel.setItemInfo(lstItemInfo);

        SummarizeInfo summarizeInfo = new SummarizeInfo(); // tổng hợp tiền hàng cho toàn bộ hóa đơn
        summarizeInfo.setTaxPercentage(lstItemInfo.get(0).getTaxPercentage());
        summarizeInfo.setDiscountAmount(BigDecimal.ZERO);
        BigDecimal totalAmountWithTax = BigDecimal.ZERO;
        BigDecimal totalAmountWithTaxInWords = BigDecimal.ZERO;
        BigDecimal totalAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal sumOfTotalLineAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        for (ItemInfo itemInfo : lstItemInfo) {
            if (itemInfo.getSelection() != 3) {
                totalAmountWithTax = totalAmountWithTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax())).add(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.add(getValue(itemInfo.getTaxAmount()));
            } else {
                totalAmountWithTax = totalAmountWithTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax())).subtract(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.subtract(getValue(itemInfo.getTaxAmount()));
            }
        }
        sumOfTotalLineAmountWithoutTax = totalAmountWithoutTax;
        if (currentCurrency_bool) {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount));
        } else {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax, roundAmountOriginal));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax, roundAmountOriginal), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount, roundAmountOriginal));
        }
        eInvoiceVietTel.setSummarizeInfo(summarizeInfo);

        // Tổng thuế
        Map<Integer, List<ItemInfo>> groupTaxPercentage =
            lstItemInfo.stream().collect(Collectors.groupingBy(ItemInfo::getTaxPercentage));

        for (Map.Entry<Integer, List<ItemInfo>> listEntry : groupTaxPercentage.entrySet()) {
            if (listEntry.getKey() > 0) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.add(getValue(itemInfo.getTaxAmount()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.subtract(getValue(itemInfo.getTaxAmount()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            } else if (listEntry.getKey() == 0 || listEntry.getKey() == -1 || listEntry.getKey() == -2) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            }
        }
        String json = Utils.ObjectToJSON(eInvoiceVietTel);
        RequestSIV requestSIV = new RequestSIV();
        setDefaultRequestSIV(requestSIV, userDTO);
        requestSIV.setApi(CreateOrUpdateInvoiceDraft + requestSIV.getCodeTax());
        requestSIV.setData(json);
        RestfullApiSIV.post(requestSIV);
        return result;
    }

    private Respone_SDS createEInvoiceMIV(Optional<EInvoice> e_Invoice, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        InvoicesSDS_DTO invoicesSDS_dto = new InvoicesSDS_DTO();
        List<InvSDS_DTO> Invs = new ArrayList<>();
        EInvoice eInvoice = e_Invoice.get();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        eInvoiceDetails.sort(Comparator.comparingInt(EInvoiceDetails::getOrderPriority));
        eInvoice.seteInvoiceDetails(eInvoiceDetails);
        Invoice_MIV invoice_MIV = getEInvoiceMIV(eInvoice, userDTO);
        String jsonData = Utils.ObjectToJSON(invoice_MIV);

        RequestMIV requestMIV = new RequestMIV();
        setDefaultRequestMIV(requestMIV, userDTO);
        requestMIV.setApi(API_MIV.Save);
        requestMIV.setData(jsonData);
        Respone_MIV responeMiv = RestfullApiMIV.post(requestMIV);

        if (Boolean.TRUE.equals(responeMiv.getOk())) {
            respone_sds.setStatus(Success);
            String invoiceNo = responeMiv.getData().getInv_invoiceNumber();
            respone_sds.setInvoiceNo(invoiceNo);
            UUID idMIV = responeMiv.getData().getInv_InvoiceAuth_id();
            eInvoiceRepository.updateAfterCreateInvoiceMIV(eInvoice.getId(), invoiceNo, idMIV.toString());
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeMiv.getError());
        }
        return respone_sds;
    }

    private Invoice_MIV getEInvoiceMIV(EInvoice eInvoice, UserDTO userDTO) {
        Invoice_MIV invoice_MIV = new Invoice_MIV();
        DataInvoice_MIV dataInvoice_MIV = new DataInvoice_MIV();
        if (StringUtils.isEmpty(eInvoice.getInvoiceNo())) {
            invoice_MIV.setEditmode(DataMIV.Editmode_New);
        } else {
            invoice_MIV.setEditmode(DataMIV.Editmode_Edit);
            dataInvoice_MIV.setInv_invoiceNumber(eInvoice.getInvoiceNo());
        }
        String tab_id = "";
        if (eInvoice.getInvoiceTypeCode().equals("01GTKT")) {
            invoice_MIV.setWindowid("WIN00187");
            tab_id = "TAB00188";
        } else if (eInvoice.getInvoiceTypeCode().equals("02GTTT")) {
            invoice_MIV.setWindowid("WIN00189");
            tab_id = "TAB00192";
        }

        // Thông tin hóa đơn
        dataInvoice_MIV.setMau_hd(eInvoice.getInvoiceTemplate());
        dataInvoice_MIV.setInv_invoiceSeries(eInvoice.getInvoiceSeries());
        dataInvoice_MIV.setInv_invoiceIssuedDate(getDateString(eInvoice.getInvoiceDate(), "yyyy-MM-dd"));
        dataInvoice_MIV.setInv_currencyCode(eInvoice.getCurrencyID());
        dataInvoice_MIV.setInv_exchangeRate(eInvoice.getExchangeRate());
        // Thông tin khách hàng
        dataInvoice_MIV.setInv_buyerDisplayName(eInvoice.getContactName());
        dataInvoice_MIV.setMa_dt(eInvoice.getAccountingObjectCode());
        dataInvoice_MIV.setInv_buyerLegalName(eInvoice.getAccountingObjectName());
        dataInvoice_MIV.setInv_buyerTaxCode(eInvoice.getCompanyTaxCode());
        dataInvoice_MIV.setInv_buyerAddressLine(eInvoice.getAccountingObjectAddress());
        dataInvoice_MIV.setInv_buyerEmail(eInvoice.getAccountingObjectEmail());
        dataInvoice_MIV.setInv_buyerBankAccount(eInvoice.getAccountingObjectBankAccount());
        dataInvoice_MIV.setInv_buyerBankName(eInvoice.getAccountingObjectBankName());
        dataInvoice_MIV.setInv_paymentMethodName(eInvoice.getPaymentMethod());
        /*dataInvoice_MIV.inv_sellerBankAccount = Utils.GetBankAccount();
        dataInvoice_MIV.inv_sellerBankName = Utils.GetBankName();*/

        // Thông tin chi tiết hàng hóa
        Details_MIV details_MIV = new Details_MIV();
        for (EInvoiceDetails eInvoiceDetails : eInvoice.geteInvoiceDetails()) {
            details_MIV.setTab_id(tab_id);
            // thông tin hàng hóa
            DataDetails_MIV dataDetails_MIV = new DataDetails_MIV();
            dataDetails_MIV.setStt_rec0(StringUtils.leftPad(String.valueOf(eInvoice.geteInvoiceDetails().indexOf(eInvoiceDetails)), 8, "0"));
            dataDetails_MIV.setInv_itemCode(eInvoiceDetails.getMaterialGoodsCode());
            dataDetails_MIV.setInv_itemName(eInvoiceDetails.getDescription());
            dataDetails_MIV.setInv_unitName(eInvoiceDetails.getUnitName());
//            dataDetails_MIV.inv_unitCode = itemD.Unit;
            dataDetails_MIV.setInv_unitPrice(eInvoiceDetails.getUnitPrice()); // đơn giá
            dataDetails_MIV.setInv_quantity(eInvoiceDetails.getQuantity());
            dataDetails_MIV.setInv_TotalAmountWithoutVat(eInvoiceDetails.getAmountOriginal());
            dataDetails_MIV.setInv_vatAmount(eInvoiceDetails.getvATAmountOriginal());
            dataDetails_MIV.setInv_discountAmount(eInvoiceDetails.getDiscountAmountOriginal());
            dataDetails_MIV.setInv_discountPercentage(getValue(eInvoiceDetails.getDiscountRate()));
            dataDetails_MIV.setInv_TotalAmount(getValue(eInvoiceDetails.getAmountOriginal()).add(getValue(eInvoiceDetails.getvATAmountOriginal())).subtract(getValue(eInvoiceDetails.getDiscountAmountOriginal())));
            dataDetails_MIV.setMa_thue(getVATRate(eInvoiceDetails.getvATRate(), null).toString());
            dataDetails_MIV.setInv_promotion(eInvoiceDetails.getIsPromotion() == null ? false : eInvoiceDetails.getIsPromotion());
            details_MIV.getData().add(dataDetails_MIV);
        }
        dataInvoice_MIV.setInv_vatAmount(eInvoice.getTotalVATAmountOriginal());
        dataInvoice_MIV.setInv_discountAmount(eInvoice.getTotalDiscountAmountOriginal());
        dataInvoice_MIV.setInv_TotalAmountWithoutVat(eInvoice.getTotalAmountOriginal());
        dataInvoice_MIV.setInv_TotalAmount(getValue(eInvoice.getTotalAmountOriginal()).add(getValue(eInvoice.getTotalVATAmountOriginal())).subtract(getValue(eInvoice.getTotalDiscountAmountOriginal())));
        dataInvoice_MIV.getDetails().add(details_MIV);
        invoice_MIV.getData().add(dataInvoice_MIV);
        return invoice_MIV;
    }

    private EInvoiceVietTel getEInvoiceSIV(Optional<EInvoice> e_Invoice, UserDTO userDTO) {
        return getEInvoiceSIV(e_Invoice, userDTO, null);
    }

    private EInvoiceVietTel getEInvoiceSIV(Optional<EInvoice> e_Invoice, UserDTO userDTO, String certString) {
        EInvoice eInvoice = e_Invoice.get();
        EInvoiceVietTel eInvoiceVietTel = new EInvoiceVietTel();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        int roundAmountOriginal = 2;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        // Thông tin người bán
        SellerInfo sellerInfo = new SellerInfo();

        // Thông tin người mua
        BuyerInfo buyerInfo = new BuyerInfo();
        buyerInfo.setBuyerCode(getString(eInvoice.getAccountingObjectCode()));
        buyerInfo.setBuyerName(getString(eInvoice.getContactName()));
        buyerInfo.setBuyerLegalName(getString(eInvoice.getAccountingObjectName()));
        buyerInfo.setBuyerAddressLine(getString(eInvoice.getAccountingObjectAddress()));
        buyerInfo.setBuyerBankName(getString(eInvoice.getAccountingObjectBankName()));
        buyerInfo.setBuyerBankAccount(getString(eInvoice.getAccountingObjectBankAccount()));
        buyerInfo.setBuyerPhoneNumber(eInvoice.getContactMobile());
        buyerInfo.setBuyerTaxCode(eInvoice.getCompanyTaxCode());
        buyerInfo.setBuyerEmail(eInvoice.getAccountingObjectEmail());
        /*buyerInfo.setBuyerIdType("1"); // Số chứng mminh
        buyerInfo.setBuyerIdNo(eInvoice.getAcc);*/

        // Thông tin hóa đơn
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setTransactionUuid(Utils.uuidConvertToGUID(eInvoice.getId()));
        invoiceInfo.setTemplateCode(eInvoice.getInvoiceTemplate());
        invoiceInfo.setInvoiceSeries(eInvoice.getInvoiceSeries());
        invoiceInfo.setInvoiceType(eInvoice.getInvoiceTypeCode());
        invoiceInfo.setCurrencyCode(eInvoice.getCurrencyID());
        invoiceInfo.setAdjustmentType("1");
        invoiceInfo.setPaymentStatus(true); // trang thái thanh toán hóa đơn
        invoiceInfo.setCusGetInvoiceRight(true); // Cho khách hàng xem hóa đơn trong Quản lý hóa đơn
        //objInvoice.certificateSerial = certificateSerial;
        if (!StringUtils.isEmpty(certString)) {
            invoiceInfo.setCertificateSerial(certString);
        }
        invoiceInfo.setInvoiceIssuedDate(getTimeUTC(eInvoice.getRefDateTime()));
        invoiceInfo.setExchangeRate(eInvoice.getExchangeRate());

        // Thông tin sản phẩm
        List<ItemInfo> lstItemInfo = new ArrayList<>();
        List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool, SIV);
        lstItemInfo.addAll(convertListProductSDS(lstProduct));

        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        eInvoiceVietTel.setBuyerInfo(buyerInfo);
        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        Payments payments = new Payments();
        payments.setPaymentMethodName(eInvoice.getPaymentMethod());
        eInvoiceVietTel.getPayments().add(payments);
        eInvoiceVietTel.setItemInfo(lstItemInfo);

        SummarizeInfo summarizeInfo = new SummarizeInfo(); // tổng hợp tiền hàng cho toàn bộ hóa đơn
        summarizeInfo.setTaxPercentage(lstItemInfo.get(0).getTaxPercentage());
        summarizeInfo.setDiscountAmount(BigDecimal.ZERO);
        BigDecimal totalAmountWithTax = BigDecimal.ZERO;
        BigDecimal totalAmountWithTaxInWords = BigDecimal.ZERO;
        BigDecimal totalAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal sumOfTotalLineAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        for (ItemInfo itemInfo : lstItemInfo) {
            if (itemInfo.getSelection() != 3) {
                totalAmountWithTax = totalAmountWithTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax())).add(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.add(getValue(itemInfo.getTaxAmount()));
            } else {
                totalAmountWithTax = totalAmountWithTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax())).subtract(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.subtract(getValue(itemInfo.getTaxAmount()));
            }
        }
        sumOfTotalLineAmountWithoutTax = totalAmountWithoutTax;
        if (currentCurrency_bool) {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount));
        } else {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax, roundAmountOriginal));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax, roundAmountOriginal), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount, roundAmountOriginal));
        }
        eInvoiceVietTel.setSummarizeInfo(summarizeInfo);

        // Tổng thuế
        Map<Integer, List<ItemInfo>> groupTaxPercentage =
            lstItemInfo.stream().collect(Collectors.groupingBy(ItemInfo::getTaxPercentage));

        for (Map.Entry<Integer, List<ItemInfo>> listEntry : groupTaxPercentage.entrySet()) {
            if (listEntry.getKey() > 0) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.add(getValue(itemInfo.getTaxAmount()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.subtract(getValue(itemInfo.getTaxAmount()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            } else if (listEntry.getKey() == 0 || listEntry.getKey() == -1 || listEntry.getKey() == -2) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            }
        }
        return eInvoiceVietTel;
    }

    private EInvoiceVietTel getEInvoiceReplaceSIV(Optional<EInvoice> e_Invoice, UserDTO userDTO, String certificateSerial) {
        EInvoice eInvoice = e_Invoice.get();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        EInvoiceVietTel eInvoiceVietTel = new EInvoiceVietTel();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        int roundAmountOriginal = 2;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        // Thông tin người bán
        SellerInfo sellerInfo = new SellerInfo();

        // Thông tin người mua
        BuyerInfo buyerInfo = new BuyerInfo();
        buyerInfo.setBuyerCode(getString(eInvoice.getAccountingObjectCode()));
        buyerInfo.setBuyerName(getString(eInvoice.getContactName()));
        buyerInfo.setBuyerLegalName(getString(eInvoice.getAccountingObjectName()));
        buyerInfo.setBuyerAddressLine(getString(eInvoice.getAccountingObjectAddress()));
        buyerInfo.setBuyerBankName(getString(eInvoice.getAccountingObjectBankName()));
        buyerInfo.setBuyerBankAccount(getString(eInvoice.getAccountingObjectBankAccount()));
        buyerInfo.setBuyerPhoneNumber(eInvoice.getContactMobile());
        buyerInfo.setBuyerTaxCode(eInvoice.getCompanyTaxCode());
        buyerInfo.setBuyerEmail(eInvoice.getAccountingObjectEmail());
        /*buyerInfo.setBuyerIdType("1"); // Số chứng mminh
        buyerInfo.setBuyerIdNo(eInvoice.getAcc);*/

        // Thông tin hóa đơn
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setTransactionUuid(Utils.uuidConvertToGUID(eInvoice.getId()));
        invoiceInfo.setTemplateCode(eInvoice.getInvoiceTemplate());
        invoiceInfo.setInvoiceSeries(eInvoice.getInvoiceSeries());
        invoiceInfo.setInvoiceType(eInvoice.getInvoiceTypeCode());
        invoiceInfo.setCurrencyCode(eInvoice.getCurrencyID());
        invoiceInfo.setAdjustmentType("3");
        invoiceInfo.setPaymentStatus(true); // trang thái thanh toán hóa đơn
        invoiceInfo.setCusGetInvoiceRight(true); // Cho khách hàng xem hóa đơn trong Quản lý hóa đơn
        //objInvoice.certificateSerial = certificateSerial;
        if (!StringUtils.isEmpty(certificateSerial)) {
            invoiceInfo.setCertificateSerial(certificateSerial);
        }
        invoiceInfo.setInvoiceIssuedDate(getTimeUTC(eInvoice.getRefDateTime()));
        invoiceInfo.setExchangeRate(eInvoice.getExchangeRate());
        invoiceInfo.setOriginalInvoiceId(eInvoice.getInvoiceSeries() + eInvoice.getInvocieNoReplaced());
        invoiceInfo.setOriginalInvoiceIssueDate(getTimeUTC(eInvoice.getRefDateTimeReplaced()));
        invoiceInfo.setAdditionalReferenceDesc(eInvoice.getDocumentNo()); // Tên văn bản
        invoiceInfo.setAdditionalReferenceDate(getTimeUTC(eInvoice.getDocumentDate())); // Ngày văn bản

        // Thông tin sản phẩm
        List<ItemInfo> lstItemInfo = new ArrayList<>();
        List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool, SIV);
        lstItemInfo.addAll(convertListProductSDS(lstProduct));

        // Dòng thông tin hóa đơn thay thế
        ItemInfo itemInfoTT = new ItemInfo();
        itemInfoTT.setSelection(2);
        String nameProd = "Hóa đơn thay thế cho hóa đơn điện tử số " + invoiceInfo.getOriginalInvoiceId() + " lập ngày " + getDateString(eInvoice.getInvoiceDateReplaced());
        itemInfoTT.setItemName(nameProd);
        lstItemInfo.add(itemInfoTT);

        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        eInvoiceVietTel.setBuyerInfo(buyerInfo);
        eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
        Payments payments = new Payments();
        payments.setPaymentMethodName(eInvoice.getPaymentMethod());
        eInvoiceVietTel.getPayments().add(payments);
        eInvoiceVietTel.setItemInfo(lstItemInfo);

        SummarizeInfo summarizeInfo = new SummarizeInfo(); // tổng hợp tiền hàng cho toàn bộ hóa đơn
        summarizeInfo.setTaxPercentage(lstItemInfo.get(0).getTaxPercentage());
        summarizeInfo.setDiscountAmount(BigDecimal.ZERO);
        BigDecimal totalAmountWithTax = BigDecimal.ZERO;
        BigDecimal totalAmountWithTaxInWords = BigDecimal.ZERO;
        BigDecimal totalAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal sumOfTotalLineAmountWithoutTax = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        for (ItemInfo itemInfo : lstItemInfo) {
            if (itemInfo.getSelection() != 3) {
                totalAmountWithTax = totalAmountWithTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax())).add(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.add(getValue(itemInfo.getTaxAmount()));
            } else {
                totalAmountWithTax = totalAmountWithTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax())).subtract(getValue(itemInfo.getTaxAmount()));
                totalAmountWithoutTax = totalAmountWithoutTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                totalTaxAmount = totalTaxAmount.subtract(getValue(itemInfo.getTaxAmount()));
            }
        }
        sumOfTotalLineAmountWithoutTax = totalAmountWithoutTax;
        if (currentCurrency_bool) {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount));
        } else {
            summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax, roundAmountOriginal));
            summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax, roundAmountOriginal), invoiceInfo.getCurrencyCode(), userDTO));
            summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax, roundAmountOriginal));
            summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount, roundAmountOriginal));
        }
        eInvoiceVietTel.setSummarizeInfo(summarizeInfo);

        // Tổng thuế
        Map<Integer, List<ItemInfo>> groupTaxPercentage =
            lstItemInfo.stream().collect(Collectors.groupingBy(ItemInfo::getTaxPercentage));

        for (Map.Entry<Integer, List<ItemInfo>> listEntry : groupTaxPercentage.entrySet()) {
            if (listEntry.getKey() > 0) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.add(getValue(itemInfo.getTaxAmount()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        taxAmount = taxAmount.subtract(getValue(itemInfo.getTaxAmount()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            } else if (listEntry.getKey() == 0 || listEntry.getKey() == -1 || listEntry.getKey() == -2) {
                TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                taxBreakdowns.setTaxPercentage(listEntry.getKey());
                BigDecimal taxableAmount = BigDecimal.ZERO;
                BigDecimal taxAmount = BigDecimal.ZERO;
                for (ItemInfo itemInfo : listEntry.getValue()) {
                    if (itemInfo.getSelection() != 3) {
                        taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    } else {
                        taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    }
                }
                taxBreakdowns.setTaxableAmount(taxableAmount);
                taxBreakdowns.setTaxAmount(taxAmount);
                eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
            }
        }
        return eInvoiceVietTel;
    }

    private EInvoiceVietTel getEInvoiceAdjustedSIV(Optional<EInvoice> e_Invoice, UserDTO userDTO, String certificateSerial) {
        EInvoice eInvoice = e_Invoice.get();
        EInvoice eInvoiceOriginal = eInvoiceRepository.findById(eInvoice.getiDAdjustInv()).get();
        EInvoiceVietTel eInvoiceVietTel = new EInvoiceVietTel();
        Boolean currentCurrency_bool = userDTO.getOrganizationUnit().getCurrencyID().equals(eInvoice.getCurrencyID());
        int roundAmountOriginal = 2;
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
        // Thông tin người bán
        SellerInfo sellerInfo = new SellerInfo();

        // Thông tin người mua
        BuyerInfo buyerInfo = new BuyerInfo();
        buyerInfo.setBuyerCode(getString(eInvoice.getAccountingObjectCode()));
        buyerInfo.setBuyerName(getString(eInvoice.getContactName()));
        buyerInfo.setBuyerLegalName(getString(eInvoice.getAccountingObjectName()));
        buyerInfo.setBuyerAddressLine(getString(eInvoice.getAccountingObjectAddress()));
        buyerInfo.setBuyerBankName(getString(eInvoice.getAccountingObjectBankName()));
        buyerInfo.setBuyerBankAccount(getString(eInvoice.getAccountingObjectBankAccount()));
        buyerInfo.setBuyerPhoneNumber(eInvoice.getContactMobile());
        buyerInfo.setBuyerTaxCode(eInvoice.getCompanyTaxCode());
        buyerInfo.setBuyerEmail(eInvoice.getAccountingObjectEmail());
        /*buyerInfo.setBuyerIdType("1"); // Số chứng mminh
        buyerInfo.setBuyerIdNo(eInvoice.getAcc);*/

        // Thông tin hóa đơn
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setTransactionUuid(Utils.uuidConvertToGUID(eInvoice.getId()));
        invoiceInfo.setTemplateCode(eInvoice.getInvoiceTemplate());
        invoiceInfo.setInvoiceSeries(eInvoice.getInvoiceSeries());
        invoiceInfo.setInvoiceType(eInvoice.getInvoiceTypeCode());
        invoiceInfo.setCurrencyCode(eInvoice.getCurrencyID());
        invoiceInfo.setAdjustmentType("5");
        invoiceInfo.setPaymentStatus(true); // trang thái thanh toán hóa đơn
        invoiceInfo.setCusGetInvoiceRight(true); // Cho khách hàng xem hóa đơn trong Quản lý hóa đơn
        //objInvoice.certificateSerial = certificateSerial;
        if (!StringUtils.isEmpty(certificateSerial)) {
            invoiceInfo.setCertificateSerial(certificateSerial);
        }
        invoiceInfo.setInvoiceIssuedDate(getTimeUTC(eInvoice.getRefDateTime()));
        invoiceInfo.setExchangeRate(eInvoice.getExchangeRate());
        invoiceInfo.setOriginalInvoiceId(eInvoice.getInvoiceSeries() + eInvoice.getInvocieNoAdjusted());
        invoiceInfo.setOriginalInvoiceIssueDate(getTimeUTC(eInvoice.getInvoiceDateAdjusted()));
        invoiceInfo.setAdditionalReferenceDesc(eInvoice.getDocumentNo()); // Tên văn bản
        invoiceInfo.setAdditionalReferenceDate(getTimeUTC(eInvoice.getDocumentDate())); // Ngày văn bản

        if (eInvoice.getType().equals(Type.DieuChinhTang) || eInvoice.getType().equals(Type.DieuChinhGiam)) {
            invoiceInfo.setAdjustmentInvoiceType("1"); // Loại điều chỉnh
            // Thông tin sản phẩm
            List<ItemInfo> lstItemInfo = new ArrayList<>();
            List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool, SIV);
            lstItemInfo.addAll(convertListProductSDS(lstProduct));

            if (eInvoice.getType().equals(Type.DieuChinhTang)) {
                for (ItemInfo itemInfo : lstItemInfo) {
                    if (itemInfo.getSelection() != 3) {
                        itemInfo.setIsIncreaseItem(true);
                        itemInfo.setItemName("Điều chỉnh tăng tiền hàng, tiền thuế của hàng hóa/dịch vụ: " + getString(itemInfo.getItemName()));
                    }
                    itemInfo.setAdjustmentTaxAmount(getValue(itemInfo.getTaxAmount()));
                }
                // Dòng thông tin hóa đơn điều chỉnh
                ItemInfo itemInfoTT = new ItemInfo();
                itemInfoTT.setSelection(2);
                String nameProd = "Điều chỉnh tăng tiền hàng, tiền thuế cho hóa đơn điện tử số " + invoiceInfo.getOriginalInvoiceId() + " lập ngày " + getDateString(eInvoice.getInvoiceDateAdjusted());
                itemInfoTT.setItemName(nameProd);
                lstItemInfo.add(itemInfoTT);
            } else {
                for (ItemInfo itemInfo : lstItemInfo) {
                    if (itemInfo.getSelection() != 3) {
                        itemInfo.setIsIncreaseItem(false);
                        itemInfo.setItemName("Điều chỉnh giảm tiền hàng, tiền thuế của hàng hóa/dịch vụ: " + getString(itemInfo.getItemName()));
                    }
                    itemInfo.setAdjustmentTaxAmount(getValue(itemInfo.getTaxAmount()));
                }
                // Dòng thông tin hóa đơn điều chỉnh
                ItemInfo itemInfoTT = new ItemInfo();
                itemInfoTT.setSelection(2);
                String nameProd = "Điều chỉnh giảm tiền hàng, tiền thuế cho hóa đơn điện tử số " + invoiceInfo.getOriginalInvoiceId() + " lập ngày " + getDateString(eInvoice.getInvoiceDateAdjusted());
                itemInfoTT.setItemName(nameProd);
                lstItemInfo.add(itemInfoTT);
            }

            eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
            eInvoiceVietTel.setBuyerInfo(buyerInfo);
            eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
            Payments payments = new Payments();
            payments.setPaymentMethodName(eInvoice.getPaymentMethod());
            eInvoiceVietTel.getPayments().add(payments);
            eInvoiceVietTel.setItemInfo(lstItemInfo);

            SummarizeInfo summarizeInfo = new SummarizeInfo(); // tổng hợp tiền hàng cho toàn bộ hóa đơn
            summarizeInfo.setTaxPercentage(lstItemInfo.get(0).getTaxPercentage());
            summarizeInfo.setDiscountAmount(BigDecimal.ZERO);
            BigDecimal totalAmountWithTax = BigDecimal.ZERO;
            BigDecimal totalAmountWithTaxInWords = BigDecimal.ZERO;
            BigDecimal totalAmountWithoutTax = BigDecimal.ZERO;
            BigDecimal sumOfTotalLineAmountWithoutTax = BigDecimal.ZERO;
            BigDecimal totalTaxAmount = BigDecimal.ZERO;
            for (ItemInfo itemInfo : lstItemInfo) {
                if (itemInfo.getSelection() != 3) {
                    totalAmountWithTax = totalAmountWithTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax())).add(getValue(itemInfo.getTaxAmount()));
                    totalAmountWithoutTax = totalAmountWithoutTax.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    totalTaxAmount = totalTaxAmount.add(getValue(itemInfo.getTaxAmount()));
                } else {
                    totalAmountWithTax = totalAmountWithTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax())).subtract(getValue(itemInfo.getTaxAmount()));
                    totalAmountWithoutTax = totalAmountWithoutTax.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                    totalTaxAmount = totalTaxAmount.subtract(getValue(itemInfo.getTaxAmount()));
                }
            }
            sumOfTotalLineAmountWithoutTax = totalAmountWithoutTax;
            if (currentCurrency_bool) {
                summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax));
                summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax), invoiceInfo.getCurrencyCode(), userDTO));
                summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax));
                summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax));
                summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount));
            } else {
                summarizeInfo.setTotalAmountWithTax(Utils.round(totalAmountWithTax, roundAmountOriginal));
                summarizeInfo.setTotalAmountWithTaxInWords(Utils.GetAmountInWords(Utils.round(totalAmountWithTax, roundAmountOriginal), invoiceInfo.getCurrencyCode(), userDTO));
                summarizeInfo.setTotalAmountWithoutTax(Utils.round(totalAmountWithoutTax, roundAmountOriginal));
                summarizeInfo.setSumOfTotalLineAmountWithoutTax(Utils.round(sumOfTotalLineAmountWithoutTax, roundAmountOriginal));
                summarizeInfo.setTotalTaxAmount(Utils.round(totalTaxAmount, roundAmountOriginal));
            }
            if (eInvoice.getType().equals(Type.DieuChinhTang)) {
                summarizeInfo.setIsTotalAmountPos(true);
                summarizeInfo.setIsTotalAmtWithoutTaxPos(true);
                summarizeInfo.setIsTotalTaxAmountPos(true);
                summarizeInfo.setIsDiscountAmtPos(true);
            } else {
                summarizeInfo.setIsTotalAmountPos(false);
                summarizeInfo.setIsTotalAmtWithoutTaxPos(false);
                summarizeInfo.setIsTotalTaxAmountPos(false);
                summarizeInfo.setIsDiscountAmtPos(false);
            }
            eInvoiceVietTel.setSummarizeInfo(summarizeInfo);

            // Tổng thuế
            Map<Integer, List<ItemInfo>> groupTaxPercentage =
                lstItemInfo.stream().collect(Collectors.groupingBy(ItemInfo::getTaxPercentage));

            for (Map.Entry<Integer, List<ItemInfo>> listEntry : groupTaxPercentage.entrySet()) {
                if (listEntry.getKey() > 0) {
                    TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                    taxBreakdowns.setTaxPercentage(listEntry.getKey());
                    BigDecimal taxableAmount = BigDecimal.ZERO;
                    BigDecimal taxAmount = BigDecimal.ZERO;
                    for (ItemInfo itemInfo : listEntry.getValue()) {
                        if (itemInfo.getSelection() != 3) {
                            taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                            taxAmount = taxAmount.add(getValue(itemInfo.getTaxAmount()));
                        } else {
                            taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                            taxAmount = taxAmount.subtract(getValue(itemInfo.getTaxAmount()));
                        }
                    }
                    taxBreakdowns.setTaxableAmount(taxableAmount);
                    taxBreakdowns.setTaxAmount(taxAmount);
                    eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
                } else if (listEntry.getKey() == 0 || listEntry.getKey() == -1 || listEntry.getKey() == -2) {
                    TaxBreakdowns taxBreakdowns = new TaxBreakdowns();
                    taxBreakdowns.setTaxPercentage(listEntry.getKey());
                    BigDecimal taxableAmount = BigDecimal.ZERO;
                    BigDecimal taxAmount = BigDecimal.ZERO;
                    for (ItemInfo itemInfo : listEntry.getValue()) {
                        if (itemInfo.getSelection() != 3) {
                            taxableAmount = taxableAmount.add(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        } else {
                            taxableAmount = taxableAmount.subtract(getValue(itemInfo.getItemTotalAmountWithoutTax()));
                        }
                    }
                    taxBreakdowns.setTaxableAmount(taxableAmount);
                    taxBreakdowns.setTaxAmount(taxAmount);
                    eInvoiceVietTel.getTaxBreakdowns().add(taxBreakdowns);
                }
            }
        } else {
            invoiceInfo.setAdjustmentInvoiceType("2");
            String inforChange = "";
            if (eInvoice.getAccountingObjectCode() != eInvoiceOriginal.getAccountingObjectCode()) {
                inforChange += "Mã khách hàng: " + eInvoiceOriginal.getAccountingObjectCode() + "-->" + eInvoice.getAccountingObjectCode() + "; ";
            }
            if (eInvoice.getContactName() != eInvoiceOriginal.getContactName()) {
                inforChange += "Tên người mua: " + eInvoiceOriginal.getContactName() + "-->" + eInvoice.getContactName() + "; ";
            }
            if (eInvoice.getCompanyTaxCode() != eInvoiceOriginal.getCompanyTaxCode()) {
                inforChange += "Mã số thuế: " + eInvoiceOriginal.getCompanyTaxCode() + "-->" + eInvoice.getCompanyTaxCode() + "; ";
            }
            if (eInvoice.getEmail() != eInvoiceOriginal.getEmail()) {
                inforChange += "Địa chỉ Email: " + eInvoiceOriginal.getEmail() + "-->" + eInvoice.getEmail() + "; ";
            }
            if (eInvoice.getAccountingObjectName() != eInvoiceOriginal.getAccountingObjectName()) {
                inforChange += "Tên đơn vị: " + eInvoiceOriginal.getAccountingObjectName() + "-->" + eInvoice.getAccountingObjectName() + "; ";
            }
            if (eInvoice.getAccountingObjectAddress() != eInvoiceOriginal.getAccountingObjectAddress()) {
                inforChange += "Địa chỉ: " + eInvoiceOriginal.getAccountingObjectAddress() + "-->" + eInvoice.getAccountingObjectAddress() + "; ";
            }
            if (inforChange.length() > 0)
                inforChange = inforChange.substring(0, inforChange.length() - 2);

            List<ItemInfo> lstItemInfo = new ArrayList<>();
            // Dòng thông tin hóa đơn điều chỉnh
            ItemInfo itemInfoTT = new ItemInfo();
            itemInfoTT.setLineNumber(1);
            itemInfoTT.setSelection(1);
            String nameProd = "Điều chỉnh thông tin khách hàng cho hóa đơn điện tử số " + invoiceInfo.getOriginalInvoiceId() + " lập ngày " + getDateString(eInvoice.getInvoiceDateAdjusted()) + ": " + inforChange;
            itemInfoTT.setItemName(nameProd);
            lstItemInfo.add(itemInfoTT);

            eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
            eInvoiceVietTel.setBuyerInfo(buyerInfo);
            eInvoiceVietTel.setGeneralInvoiceInfo(invoiceInfo);
            Payments payments = new Payments();
            payments.setPaymentMethodName(eInvoice.getPaymentMethod());
            eInvoiceVietTel.getPayments().add(payments);
            eInvoiceVietTel.setItemInfo(lstItemInfo);
        }
        return eInvoiceVietTel;
    }

    List<ItemInfo> convertListProductSDS(List<ProductSDS_DTO> lstProduct) {
        List<ItemInfo> itemInfos = new ArrayList<>();
        for (ProductSDS_DTO productSDS_dto : lstProduct) {
            if (!productSDS_dto.getProdName().equals("Chiết khấu")) {
                ItemInfo itemIf = new ItemInfo();
                itemIf.setDiscount(BigDecimal.ZERO);
                itemIf.setItemCode(getString(productSDS_dto.getCode()));
                itemIf.setItemDiscount(BigDecimal.ZERO);
                itemIf.setItemName(productSDS_dto.getProdName());
                itemIf.setItemTotalAmountWithoutTax(productSDS_dto.getTotal());
                itemIf.setLineNumber(lstProduct.indexOf(productSDS_dto));
                itemIf.setQuantity(productSDS_dto.getProdQuantity());
                itemIf.setTaxAmount(productSDS_dto.getVATAmount());
                itemIf.setTaxPercentage(productSDS_dto.getVATRate());
                itemIf.setUnitName(productSDS_dto.getProdUnit());
                itemIf.setUnitPrice(productSDS_dto.getProdPrice());
                itemIf.setBatchNo(productSDS_dto.getLotNo());
                itemIf.setIsIncreaseItem(true);
                itemIf.setExpDate(getDateString(productSDS_dto.getExpiryDate()));
                itemInfos.add(itemIf);
            } else {
                ItemInfo itemIfCK = new ItemInfo();
                itemIfCK.setSelection(3); // Chiết khấu
                itemIfCK.setIsIncreaseItem(false); // giảm tiền
//                itemIfCK.discount = null;
//                itemIfCK.itemCode = null;
//                itemIfCK.itemDiscount = null;
                itemIfCK.setItemName(productSDS_dto.getProdName());
                itemIfCK.setItemTotalAmountWithoutTax(productSDS_dto.getTotal().abs());
                itemIfCK.setTaxAmount(productSDS_dto.getVATAmount().abs());
                itemIfCK.setTaxPercentage(productSDS_dto.getVATRate());
                itemIfCK.setLineNumber(lstProduct.indexOf(productSDS_dto));
//                itemIfCK.unitName = null;
                if (productSDS_dto.getTotal().compareTo(BigDecimal.ZERO) != 0) {
                    itemInfos.add(itemIfCK);
                }
            }
        }
        return itemInfos;
    }

    private String getTimeUTC(LocalDate localDate) {
        /*DateTime epochStart = new DateTime(1970, 01, 01, 0, 0, 0, DateTimeZone.UTC);
        long millis = (new DateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC)).getMillis() - epochStart.getMillis();
        String timestamp = String.valueOf(millis);*/
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
        return String.valueOf(epoch);
    }

    private String getTimeUTC(LocalDateTime localDate) {
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        ZonedDateTime zdt = localDate.atZone(zoneId);
        long epoch = zdt.toInstant().toEpochMilli();
        return String.valueOf(epoch);
    }

    private String getString(String value) {
        return value == null ? "" : value;
    }

    @Override
    public Respone_SDS getViewInvoicePDF(UUID uuids, String pattern, Integer option) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        Respone_SDS respone_sds = new Respone_SDS();
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    return getViewInvoicePdfSDS(uuids, pattern, option, userDTO);
                case SIV:
                    if (option == 0) {
                        return getViewInvoicePdfSIV(uuids, userDTO);
                    } else if (option == 1) {
                        return getViewInvoicePDFCOnvertSIV(uuids, userDTO);
                    }
                case MIV:
                    return getViewInvoicePdfMIV(uuids, userDTO);
                case VNPT:
                    break;
            }
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Chưa kết nối hóa đơn điện tử");
            throw new BadRequestAlertException(Constants.EInvoice.CHUA_TICH_HOP, "", "");
        }
        return respone_sds;
    }

    private Respone_SDS getViewInvoicePdfSDS(UUID uuid, String pattern, Integer option, UserDTO userDTO) {
        Respone_SDS respone_sds;
        Request_SDS request_sds = new Request_SDS();
        request_sds.setPattern(pattern);
        request_sds.setIkey(Utils.uuidConvertToGUID(uuid));
        request_sds.setOption(option);
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.GET_INVOCIE_PDF, userDTO);
        if (!respone_sds.getStatus().equals(2)) {
            throw new BadRequestAlertException(respone_sds.getMessage(), "EInvoice", "notExist");
        }
        return respone_sds;
    }

    private Respone_SDS getViewInvoicePdfSIV(UUID id, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        Optional<EInvoice> e_Invoice = eInvoiceRepository.findById(id);
        EInvoiceVietTel eInvoiceVietTel = new EInvoiceVietTel();
        ResponeSIV responeSIV = new ResponeSIV();
        /*try {
            respone_sds.setRawBytes(getHoaDonGTGT(eInvoiceVietTel, userDTO));
            respone_sds.setStatus(Success);
        } catch (JRException e) {
            e.printStackTrace();
        }*/
        RequestSIV requestSIV = new RequestSIV();
        setDefaultRequestSIV(requestSIV, userDTO);
        switch (e_Invoice.get().getStatusInvoice()) {
            case HD_MOI_TAO_LAP:
                requestSIV.setApi(CreateInvoiceDraftPreview + requestSIV.getCodeTax());
                eInvoiceVietTel = getEInvoiceSIV(e_Invoice, userDTO);
                requestSIV.setData(Utils.ObjectToJSON(eInvoiceVietTel));
                responeSIV = RestfullApiSIV.post(requestSIV);
                break;
            case HD_MOI_TAO_LAP_THAY_THE:
                // Không hỗ trợ
                /*requestSIV.setApi(CreateInvoiceDraftPreview + requestSIV.getCodeTax());
                eInvoiceVietTel = getEInvoiceReplaceSIV(e_Invoice, userDTO, userDTO.getOrganizationUnit().getCurrencyID());
                requestSIV.setData(Utils.ObjectToJSON(eInvoiceVietTel));
                responeSIV = RestfullApiSIV.post(requestSIV);*/
                break;
            case HD_MOI_TAO_LAP_DIEU_CHINH:
                break;
            case HD_CO_CHU_KY_SO:
            case HD_BI_THAY_THE:
            case HD_BI_DIEU_CHINH:
            case HD_HUY:
                requestSIV.setApi(GetInvoiceRepresentationFile);
                GetFileRequest getFileRequest = new GetFileRequest();
                getFileRequest.setPattern(e_Invoice.get().getInvoiceTemplate());
                getFileRequest.setSupplierTaxCode(requestSIV.getCodeTax());
                getFileRequest.setInvoiceNo(e_Invoice.get().getInvoiceSeries() + StringUtils.leftPad(e_Invoice.get().getInvoiceNo(), 7, "0"));
                getFileRequest.setTransactionUuid(Utils.uuidConvertToGUID(e_Invoice.get().getId()));
                getFileRequest.setFileType("PDF");
                requestSIV.setData(Utils.ObjectToJSON(getFileRequest));
                responeSIV = RestfullApiSIV.post(requestSIV);
                break;
        }
        if (responeSIV.getErrorCode() != null) {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
            throw new BadRequestAlertException(responeSIV.getDescription(), "", "");
        } else {
            respone_sds.setRawBytes(Base64.getDecoder().decode(responeSIV.getFileToBytes()));
            respone_sds.setStatus(Success);
        }
        return respone_sds;
    }

    private Respone_SDS getViewInvoicePdfMIV(UUID id, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        EInvoice eInvoice = eInvoiceRepository.findById(id).get();
        RequestMIV requestMIV = new RequestMIV();
        setDefaultRequestMIV(requestMIV, userDTO);
        requestMIV.setApi(API_MIV.Preview + "?id=" + Utils.uuidConvertToGUID(eInvoice.getiDMIV()));
        requestMIV.setContentType(MediaType.APPLICATION_PDF_VALUE);
        Respone_MIV responeMiv = RestfullApiMIV.get(requestMIV);
        if (StringUtils.isEmpty(responeMiv.getError())) {
            respone_sds.setRawBytes(responeMiv.getDataByteArr());
            respone_sds.setStatus(Success);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeMiv.getError());
        }

        return respone_sds;
    }

    private Respone_SDS getViewInvoicePDFCOnvertSIV(UUID id, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        EInvoice eInvoice = eInvoiceRepository.findById(id).get();
        RequestSIV requestSIV = new RequestSIV();
        setDefaultRequestSIV(requestSIV, userDTO);
        requestSIV.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        requestSIV.setApi(CreateExchangeInvoiceFile);
        ConvertInvoice convertInvoice = new ConvertInvoice();
        convertInvoice.setSupplierTaxCode(requestSIV.getCodeTax());
        convertInvoice.setStrIssueDate(getDateString(eInvoice.getInvoiceDate(), "yyyyMMddHHmmss"));
        convertInvoice.setInvoiceNo(eInvoice.getInvoiceSeries() + eInvoice.getInvoiceNo());
        String jsonData =
            "supplierTaxCode=" + URLEncoder.encode(convertInvoice.getSupplierTaxCode()) +
                "&invoiceNo=" + URLEncoder.encode(convertInvoice.getInvoiceNo()) +
                "&strIssueDate=" + URLEncoder.encode(convertInvoice.getStrIssueDate()) +
                "&exchangeUser=" + URLEncoder.encode(getString(requestSIV.getCodeTax()));
        requestSIV.setData(jsonData);
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        if (responeSIV.getErrorCode() == null) {
            respone_sds.setStatus(Success);
            respone_sds.setRawBytes(Base64.getDecoder().decode(responeSIV.getFileToBytes()));
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
        }
        return respone_sds;
    }

    byte[] getHoaDonGTGT(EInvoiceVietTel eInvoiceVietTel, UserDTO userDTO) throws JRException {
        byte[] result = null;
        String reportName = "VATInvoice";

        Map<String, Object> parameter = new HashMap<>();
        JasperReport jasperReport;
        userDTO.setEmptyIfNull(true);
        InvoiceInfo invoiceInfo = eInvoiceVietTel.getGeneralInvoiceInfo();
        BuyerInfo buyerInfo = eInvoiceVietTel.getBuyerInfo();
        /*Header*/
        parameter.put("invoiceForm", invoiceInfo.getTemplateCode());
        parameter.put("invoiceSerial", invoiceInfo.getInvoiceSeries());
//        parameter.put("invoiceNo", saBill.getInvoiceNo());

        parameter.put("contactName", buyerInfo.getBuyerName());
        parameter.put("companyName", buyerInfo.getBuyerLegalName());
        parameter.put("address", buyerInfo.getBuyerAddressLine());
        parameter.put("accountNumber", buyerInfo.getBuyerBankAccount());
        parameter.put("taxCode", buyerInfo.getBuyerTaxCode());
//        parameter.put("invoiceDate", "Ngày(Date) " + invoiceInfo.get().getDayOfMonth() + " Tháng(Month) " + saBill.getInvoiceDate().getMonthValue() + " Năm(Year) " + saBill.getInvoiceDate().getYear());


        List<ItemInfo> itemInfos = eInvoiceVietTel.getItemInfo();
        List<SABillReportDTO> details = new ArrayList<>();
        int index = 1;
        for (ItemInfo itemInfo : itemInfos) {
            SABillReportDTO saBillReportDTO = new SABillReportDTO();
            if (!itemInfo.getItemName().equals("Chiết khấu")) {
                saBillReportDTO.setIndex(index);
                index++;
            }
            saBillReportDTO.setUnitName(itemInfo.getUnitName());
            saBillReportDTO.setDescription(itemInfo.getItemName());
            saBillReportDTO.setQuantityString(Utils.formatTien(itemInfo.getQuantity(), Constants.SystemOption.DDSo_SoLuong, userDTO));
            saBillReportDTO.setUnitPriceString(Utils.formatTien(itemInfo.getUnitPrice(), Constants.SystemOption.DDSo_DonGia, userDTO));
            saBillReportDTO.setAmountString(Utils.formatTien(itemInfo.getItemTotalAmountWithoutTax(), Constants.SystemOption.DDSo_TienVND, userDTO));
            details.add(saBillReportDTO);
        }
        int size = details.size();
        if (details.size() < 10) {
            for (int i = 0; i < 10 - size; i++) {
                SABillReportDTO saBillReportDTO = new SABillReportDTO();
                details.add(saBillReportDTO);
            }
        }
        parameter.put("REPORT_MAX_COUNT", details.size());
        parameter.put("vATRate", getVatRateString(eInvoiceVietTel.getSummarizeInfo().getTaxPercentage()));
        parameter.put("totalAmount", Utils.formatTien(eInvoiceVietTel.getSummarizeInfo().getTotalAmountWithoutTax(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("totalVatAmount", Utils.formatTien(eInvoiceVietTel.getSummarizeInfo().getTotalTaxAmount(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("TotalAmountWithTax", Utils.formatTien(eInvoiceVietTel.getSummarizeInfo().getTotalAmountWithTax(), Constants.SystemOption.DDSo_TienVND, userDTO));
        parameter.put("AmountInWord", eInvoiceVietTel.getSummarizeInfo().getTotalAmountWithTaxInWords());
        // tạo file báo cáo
        jasperReport = ReportUtils.getCompiledFile("hoa-don/" + reportName + ".jasper", "hoa-don/" + reportName + ".jrxml");
        result = ReportUtils.generateReportPDF(details, parameter, jasperReport);

        return result;
    }

    private String getVatRateString(int vatRate) {
        String result = "";
        switch (vatRate) {
            case 0:
                result = "0%";
                break;
            case 5:
                result = "5%";
                break;
            case 10:
                result = "10%";
                break;
            default:
                result = "/";
                break;
        }
        return result;
    }

    @Override
    public ConnectEInvoiceDTO getConnectEInvoiceDTO() {
        ConnectEInvoiceDTO connectEInvoiceDTO = new ConnectEInvoiceDTO();
        UserDTO userDTO = userService.getAccount();
        connectEInvoiceDTO.setPath(Utils.getEI_Path(userDTO));
        connectEInvoiceDTO.setPassword(Utils.getEI_MatKhau(userDTO));
        connectEInvoiceDTO.setUserName(Utils.getEI_TenDangNhap(userDTO));
        connectEInvoiceDTO.setSupplierCode(Utils.getEI_IDNhaCungCapDichVu(userDTO));
        connectEInvoiceDTO.setUseToken(Utils.getSignType(userDTO));
        connectEInvoiceDTO.setUseInvoceWait(Utils.getCheckHDCD(userDTO));
        connectEInvoiceDTO.setSuppliers(supplierService.findAll());
        return connectEInvoiceDTO;
    }

    @Override
    public Boolean updateEISystemOption(ConnectEInvoiceDTO connectEInvoiceDTO) {
        boolean result = true;
        UserDTO userDTO = userService.getAccount();
        result = eInvoiceRepository.updateSystemOption(connectEInvoiceDTO, userDTO.getOrganizationUnit().getId());
        return result;
    }

    /**
     * @param uuids
     * @return
     * @Author Hautv
     */
    @Override
    public Respone_SDS publishInvoice(List<UUID> uuids) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (uuids.size() > 5000) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 5000");
            return respone_sds;
        }
        UserDTO userDTO = userService.getAccount();
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(uuids);
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllDetailsByListID(uuids);
        for (EInvoice eInvoice : eInvoices) {
            eInvoice.seteInvoiceDetails(eInvoiceDetails.stream().filter(n -> n.geteInvoiceID().equals(eInvoice.getId())).collect(Collectors.toList()));
        }
        Map<String, List<EInvoice>> grpInvoiceTemplate =
            eInvoices.stream().collect(Collectors.groupingBy(EInvoice::getInvoiceTemplate));
        Map<String, List<EInvoice>> grpInvoiceSeries =
            eInvoices.stream().collect(Collectors.groupingBy(EInvoice::getInvoiceSeries));
        if (grpInvoiceTemplate.size() > 1 || grpInvoiceSeries.size() > 1) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Chỉ được phát hành những hóa đơn có cùng mẫu số hóa đơn và ký hiệu hóa đơn");
            return respone_sds;
        }
        List<Integer> lstSTT = eInvoices.stream().map(EInvoice::getStatusInvoice).collect(Collectors.toList());
        if (lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE) || lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
            if (eInvoices.size() == 1) {
                if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE)) {
                    switch (NCCDV) {
                        case SDS:
                            respone_sds = publishReplacedSDS(eInvoices.get(0)); // Phát hành thay thế
                            break;
                        case SIV:
                            respone_sds = publishReplacedSIV(eInvoices.get(0), userDTO); // Phát hành thay thế
                            break;
                        case MIV:
                            break;
                        case VNPT:
                            break;
                    }
                } else if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
                    switch (NCCDV) {
                        case SDS:
                            respone_sds = publishAdjustedSDS(eInvoices.get(0)); // Phát hành điều chỉnh
                            break;
                        case SIV:
                            respone_sds = publishAdjustedSIV(eInvoices.get(0), userDTO); // Phát hành điều chỉnh
                            break;
                        case MIV:
                            break;
                        case VNPT:
                            break;
                    }
                }
            } else {
                respone_sds.setStatus(1);
                respone_sds.setMessage("Chỉ được phát hành 1 hóa đơn điều chỉnh, thay thế trong 1 lần phát hành");
                return respone_sds;
            }
        } else {
            switch (NCCDV) {
                case SDS:
                    respone_sds = publishSDS(eInvoices, userDTO);
                    break;
                case SIV:
                    respone_sds = publishSIV(eInvoices, userDTO);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }
        }
        if (respone_sds.getStatus().equals(Success)) {
            if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE)) {
                eInvoiceRepository.updateAfterPublishInvoiceReplaced(respone_sds, eInvoices.get(0));
            } else if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
                eInvoiceRepository.updateAfterPublishInvoiceAdjusted(respone_sds, eInvoices.get(0));
            } else {
                eInvoiceRepository.updateAfterPublishInvoice(respone_sds);
            }
            eInvoiceRepository.updateSendMailAfterPublish(eInvoices.stream().filter(n -> !StringUtils.isEmpty(n.getAccountingObjectEmail())).collect(Collectors.toList()));
        } else if (respone_sds.getStatus().equals(InvalidData)) {
            if (respone_sds.getData().getKeyInvoiceMsg() != null) {
                List<KeyInvoiceMsg> lstKeyInvoiceMsgs = new ArrayList<>();
                for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceMsg().entrySet()) {
                    KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                    keyInvoiceMsg.setId(Utils.uuidConvertToGUID(entry.getKey()));
                    keyInvoiceMsg.setMessage(entry.getValue());
                    lstKeyInvoiceMsgs.add(keyInvoiceMsg);
                }
                respone_sds.getData().setKeyInvoiceMsgDTO(lstKeyInvoiceMsgs);
            }
        }
        return respone_sds;
    }

    /**
     * @param uuids
     * @param certString
     * @return lấy mã hash hóa đơn
     * @Author Hautv
     */
    @Override
    public Respone_SDS getDigestData(List<UUID> uuids, String certString) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (uuids.size() > 20) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 20");
            return respone_sds;
        }
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(uuids);
        // Chia nhỏ request
        List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllDetailsByListID(uuids);
        for (EInvoice eInvoice : eInvoices) {
            eInvoice.seteInvoiceDetails(eInvoiceDetails.stream().filter(n -> n.geteInvoiceID().equals(eInvoice.getId())).collect(Collectors.toList()));
        }
        Map<String, List<EInvoice>> grpInvoiceTemplate =
            eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceTemplate()));
        Map<String, List<EInvoice>> grpInvoiceSeries =
            eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceSeries()));
        if (grpInvoiceTemplate.size() > 1 || grpInvoiceSeries.size() > 1) {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage("Chỉ được phát hành những hóa đơn có cùng mẫu số hóa đơn và ký hiệu hóa đơn");
            return respone_sds;
        }
        List<Integer> lstSTT = eInvoices.stream().map(n -> n.getStatusInvoice()).collect(Collectors.toList());
        if (lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE) || lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
            if (eInvoices.size() == 1) {
                if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE)) {
                    respone_sds = getDigestForReplaced(eInvoices.get(0), certString); // Tạo hóa đơn tạm thay thế
                } else if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
                    respone_sds = getDigestForAdjusted(eInvoices.get(0), certString); // Tạo hóa đơn tạm điều chỉnh
                }
            } else {
                respone_sds.setStatus(Fail);
                respone_sds.setMessage("Chỉ được phát hành 1 hóa đơn điều chỉnh, thay thế trong 1 lần phát hành");
                return respone_sds;
            }
        } else {
            respone_sds = getDigestForNew(eInvoices, certString);
        }
        if (respone_sds.getStatus().equals(Success) && respone_sds.getData().getDigestData() != null) {
            List<DigestData> digestData = new ArrayList<>();
            for (Map.Entry<String, String> entry : respone_sds.getData().getDigestData().entrySet()) {
                DigestData digestData1 = new DigestData();
                digestData1.setKey(entry.getKey());
                digestData1.setHashData(entry.getValue());
                digestData.add(digestData1);
            }
            respone_sds.getData().setDigestDataDTO(digestData);
        }
        return respone_sds;
    }

    /**
     * @return Ký hóa đơn với chứng thư số
     * @Author Hautv
     */
    @Override
    public Respone_SDS publishInvoiceWithCert(Request_SDS request_sds) {
        List<UUID> lstID = request_sds.getIkeys();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(lstID);
        Respone_SDS respone_sds = new Respone_SDS();
        Map<String, String> signature = new HashMap<>();
        for (DigestData digestData : request_sds.getSignatureDTO()) {
            signature.put(digestData.getKey(), digestData.getSigData());
        }
        signature = signature.entrySet().stream()
            .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        request_sds.setSignature(signature);
        UserDTO userDTO = userService.getAccount();
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        List<Integer> lstSTT = eInvoices.stream().map(n -> n.getStatusInvoice()).collect(Collectors.toList());
        if (lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE) || lstSTT.contains(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
            if (eInvoices.size() == 1) {
                if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE)) {
                    switch (NCCDV) {
                        case SDS:
                            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoices.get(0).getiDReplaceInv()));
                            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_WRAP_AND_LAUNCH_REPLACEMENT, userDTO); // Phát hành thay thế
                            break;
                        case SIV:
                            respone_sds = publishInvocieWithCertSIV(request_sds, userDTO, eInvoices);
                            break;
                        case MIV:
                            break;
                        case VNPT:
                            break;
                    }
                } else if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
                    switch (NCCDV) {
                        case SDS:
                            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoices.get(0).getiDAdjustInv()));
                            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_WRAP_AND_LAUNCH_ADJUSTMENT, userDTO); // Phát hành điều chỉnh
                            break;
                        case SIV:
                            respone_sds = publishInvocieWithCertSIV(request_sds, userDTO, eInvoices);
                            break;
                        case MIV:
                            break;
                        case VNPT:
                            break;
                    }
                }
            } else {
                respone_sds.setStatus(1);
                respone_sds.setMessage("Chỉ được phát hành 1 hóa đơn điều chỉnh, thay thế trong 1 lần phát hành");
                return respone_sds;
            }
        } else {
            switch (NCCDV) {
                case SDS:
                    respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_WRAP_AND_LAUNCH_IMPORTAION, userDTO);
                    break;
                case SIV:
                    respone_sds = publishInvocieWithCertSIV(request_sds, userDTO, eInvoices);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }
        }
        if (respone_sds.getStatus().equals(Success)) {
            if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_THAY_THE)) {
                eInvoiceRepository.updateAfterPublishInvoiceReplaced(respone_sds, eInvoices.get(0));
            } else if (eInvoices.get(0).getStatusInvoice().equals(Constants.EInvoice.HD_MOI_TAO_LAP_DIEU_CHINH)) {
                eInvoiceRepository.updateAfterPublishInvoiceAdjusted(respone_sds, eInvoices.get(0));
            } else {
                eInvoiceRepository.updateAfterPublishInvoice(respone_sds);
            }
            eInvoiceRepository.updateSendMailAfterPublish(eInvoices.stream().filter(n -> !StringUtils.isEmpty(n.getAccountingObjectEmail())).collect(Collectors.toList()));
        } else {
            if (respone_sds.getData().getKeyInvoiceMsg() != null) {
                List<KeyInvoiceMsg> lstKeyInvoiceMsgs = new ArrayList<>();
                for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceMsg().entrySet()) {
                    KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                    keyInvoiceMsg.setId(Utils.uuidConvertToGUID(entry.getKey()));
                    keyInvoiceMsg.setMessage(entry.getValue());
                    lstKeyInvoiceMsgs.add(keyInvoiceMsg);
                }
                respone_sds.getData().setKeyInvoiceMsgDTO(lstKeyInvoiceMsgs);
            }
        }
        return respone_sds;
    }

    private Respone_SDS publishInvocieWithCertSIV(Request_SDS request_sds, UserDTO userDTO, List<EInvoice> eInvoices) {
        Respone_SDS respone_sds = new Respone_SDS();
        RequestSIV requestSIV = new RequestSIV();
        setDefaultRequestSIV(requestSIV, userDTO);
        requestSIV.setApi(API_VIETTEL.CreateInvoiceUsbTokenInsertSignature);
        RequestSignature requestSignature = new RequestSignature();
        requestSignature.setSupplierTaxCode(requestSignature.getSupplierTaxCode());
        requestSignature.setTemplateCode(eInvoices.get(0).getInvoiceTemplate());
        requestSignature.setHashString(request_sds.getSignatureDTO().get(0).getHashData());
        requestSignature.setSignature(request_sds.getSignatureDTO().get(0).getSigData());
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        if (responeSIV.getErrorCode() == null) {
            Map<UUID, String> keyInvoiceNo = new HashMap<>();
            keyInvoiceNo.put(Utils.uuidConvertToGUID(eInvoices.get(0).getId()), getInvoiceNoSIV(responeSIV.getResult().getInvoiceNo(), eInvoices.get(0).getInvoiceSeries()));
            respone_sds.getData().setKeyInvoiceNo(keyInvoiceNo);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
        }
        return respone_sds;
    }

    /**
     * @param uuids
     * @return
     * @Author Hautv
     */
    @Override
    public Respone_SDS createInvoiceWaitSign(List<UUID> uuids) {
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        Respone_SDS respone_sds = new Respone_SDS();
        if (HDDT) {
            List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(uuids);
            List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllDetailsByListID(uuids);
            for (EInvoice eInvoice : eInvoices) {
                eInvoice.seteInvoiceDetails(eInvoiceDetails.stream().filter(n -> n.geteInvoiceID().equals(eInvoice.getId())).collect(Collectors.toList()));
            }
            respone_sds = crInvoiceWaitSign(eInvoices, userDTO);
            if (respone_sds.getData().getKeyInvoiceMsg() != null) {
                List<KeyInvoiceMsg> keyInvoiceMsgs = new ArrayList<>();
                for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceMsg().entrySet()) {
                    KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                    keyInvoiceMsg.setId(Utils.uuidConvertToGUID(entry.getKey()));
                    keyInvoiceMsg.setMessage(entry.getValue());
                    keyInvoiceMsgs.add(keyInvoiceMsg);
                }
                respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgs);
            }
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    /**
     * @param requestCancelInvoiceDTO
     * @return Hủy hóa đơn
     * @Author Hautv
     */
    @Override
    public Respone_SDS cancelInvoice(RequestCancelInvoiceDTO requestCancelInvoiceDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    respone_sds = cancelInvoiceSDS(requestCancelInvoiceDTO, userDTO);
                    break;
                case SIV:
                    respone_sds = cancelInvoiceSIV(requestCancelInvoiceDTO, userDTO);
                    break;
                case MIV:
                    respone_sds = cancelInvoiceMIV(requestCancelInvoiceDTO, userDTO);
                    break;
                case VNPT:
                    break;
            }
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    /**
     * @param requestCancelInvoiceDTO
     * @param userDTO
     * @return Hủy hóa đơn SDS
     * @Author Hautv
     */
    private Respone_SDS cancelInvoiceSDS(RequestCancelInvoiceDTO requestCancelInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (requestCancelInvoiceDTO.getUuidList().size() > 10) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 10");
            return respone_sds;
        }
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(requestCancelInvoiceDTO.getUuidList());
        List<Respone_SDS> lstRespone_sds = new ArrayList<>();
        // Cần check điều kiện hủy
        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        for (EInvoice eInvoice : eInvoices
        ) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoices.get(0).getInvoiceTemplate());
            request_sds.setSerial(eInvoices.get(0).getInvoiceSeries());
            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getId()));
            Respone_SDS respone_sds1 = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.CANCEL_INVOICE, userDTO);
            if (respone_sds1.getStatus().equals(2)) {
                KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
                keyInvoiceNo.setId(Utils.uuidConvertToGUID(request_sds.getIkey()));
                keyInvoiceNoDTO.add(keyInvoiceNo);
            }
            lstRespone_sds.add(respone_sds1);
        }
        respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
        eInvoiceRepository.updateAfterCancelInvoice(respone_sds);
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        for (Respone_SDS rp : lstRespone_sds
        ) {
            if (!rp.getStatus().equals(2)) {
                for (Map.Entry<UUID, String> kmess : rp.getData().getKeyInvoiceMsg().entrySet()
                ) {
                    KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                    keyInvoiceMsg.setId(Utils.uuidConvertToGUID(kmess.getKey()));
                    keyInvoiceMsg.setMessage(kmess.getValue());
                    keyInvoiceMsgDTO.add(keyInvoiceMsg);
                }
            }
        }
        respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
        respone_sds.setStatus(Success);
        return respone_sds;
    }

    /**
     * @param requestCancelInvoiceDTO
     * @param userDTO
     * @return
     * @Author Hautv
     * Hủy hóa đơn SIV
     */
    private Respone_SDS cancelInvoiceSIV(RequestCancelInvoiceDTO requestCancelInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (requestCancelInvoiceDTO.getUuidList().size() > 1) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 1");
            return respone_sds;
        } else if (StringUtils.isEmpty(requestCancelInvoiceDTO.getNameDocument()) || requestCancelInvoiceDTO.getDateDocument() == null) {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage("Không được bỏ trống các trường bắt buộc");
            return respone_sds;
        }
        EInvoice eInvoice = eInvoiceRepository.findById(requestCancelInvoiceDTO.getUuidList().get(0)).get();
        // Cần check điều kiện hủy
        RequestSIV requestSIV = new RequestSIV();
        setDefaultRequestSIV(requestSIV, userDTO);
        requestSIV.setApi(CancelTransactionInvoice);
        DestructionInvoice destructionInvoice = new DestructionInvoice();
        destructionInvoice.setSupplierTaxCode(requestSIV.getCodeTax());
        destructionInvoice.setInvoiceNo(eInvoice.getInvoiceSeries() + eInvoice.getInvoiceNo());
        destructionInvoice.setTemplateCode(eInvoice.getInvoiceTemplate());
        destructionInvoice.setStrIssueDate(getDateString(eInvoice.getInvoiceDate(), "yyyyMMddHHmmss"));
        destructionInvoice.setAdditionalReferenceDesc(requestCancelInvoiceDTO.getNameDocument());
        destructionInvoice.setAdditionalReferenceDate(getDateString(requestCancelInvoiceDTO.getDateDocument(), "yyyyMMddHHmmss"));
        String data = "supplierTaxCode=" + URLEncoder.encode(destructionInvoice.getSupplierTaxCode()) +
            "&invoiceNo=" + URLEncoder.encode(destructionInvoice.getInvoiceNo()) +
            "&strIssueDate=" + URLEncoder.encode(destructionInvoice.getStrIssueDate()) +
            "&additionalReferenceDesc=" + URLEncoder.encode(destructionInvoice.getAdditionalReferenceDesc()) +
            "&additionalReferenceDate=" + URLEncoder.encode(destructionInvoice.getAdditionalReferenceDate());
        requestSIV.setData(data);
        requestSIV.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        if (responeSIV.getErrorCode() == null) {
            respone_sds.setStatus(Success);
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(eInvoice.getId());
            keyInvoiceNoDTO.add(keyInvoiceNo);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            eInvoiceRepository.updateAfterCancelInvoice(respone_sds);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription().toString());
            KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
            keyInvoiceMsg.setId(eInvoice.getId());
            keyInvoiceMsg.setMessage(responeSIV.getDescription());
            keyInvoiceMsgDTO.add(keyInvoiceMsg);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
        }
        return respone_sds;
    }

    private Respone_SDS cancelInvoiceMIV(RequestCancelInvoiceDTO requestCancelInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (requestCancelInvoiceDTO.getUuidList().size() > 1) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 1");
            return respone_sds;
        } else if (StringUtils.isEmpty(requestCancelInvoiceDTO.getNameDocument()) || requestCancelInvoiceDTO.getDateDocument() == null) {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage("Không được bỏ trống các trường bắt buộc");
            return respone_sds;
        }
        EInvoice eInvoice = eInvoiceRepository.findById(requestCancelInvoiceDTO.getUuidList().get(0)).get();
        // Cần check điều kiện hủy

        RequestMIV requestMIV = new RequestMIV();
        setDefaultRequestMIV(requestMIV, userDTO);
        requestMIV.setApi(API_MIV.xoaboHD);

        DestructionInvoiceMIV destructionInvoiceMIV = new DestructionInvoiceMIV();
        destructionInvoiceMIV.setInv_InvoiceAuth_id(Utils.uuidConvertToGUID(eInvoice.getiDMIV()));
        destructionInvoiceMIV.setGhi_chu(requestCancelInvoiceDTO.getNameDocument());
        destructionInvoiceMIV.setNgayvb(getDateString(requestCancelInvoiceDTO.getDateDocument(), "yyyy-MM-dd"));
        destructionInvoiceMIV.setGhi_chu(requestCancelInvoiceDTO.getNote());

        String jsonData = Utils.ObjectToJSON(destructionInvoiceMIV);
        requestMIV.setData(jsonData);
        Respone_MIV responeMiv = RestfullApiMIV.post(requestMIV);

        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        if (Boolean.TRUE.equals(responeMiv.getOk())) {
            respone_sds.setStatus(Success);
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(eInvoice.getId());
            keyInvoiceNoDTO.add(keyInvoiceNo);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            eInvoiceRepository.updateAfterCancelInvoice(respone_sds);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeMiv.getError());
            KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
            keyInvoiceMsg.setId(eInvoice.getId());
            keyInvoiceMsg.setMessage(responeMiv.getError());
            keyInvoiceMsgDTO.add(keyInvoiceMsg);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
        }
        return respone_sds;
    }

    private Respone_SDS cancelInvoiceVNPT(RequestCancelInvoiceDTO requestCancelInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (requestCancelInvoiceDTO.getUuidList().size() > 1) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 1");
            return respone_sds;
        } else if (StringUtils.isEmpty(requestCancelInvoiceDTO.getNameDocument()) || requestCancelInvoiceDTO.getDateDocument() == null) {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage("Không được bỏ trống các trường bắt buộc");
            return respone_sds;
        }
        EInvoice eInvoice = eInvoiceRepository.findById(requestCancelInvoiceDTO.getUuidList().get(0)).get();
        // Cần check điều kiện hủy

        RequestMIV requestMIV = new RequestMIV();
        setDefaultRequestMIV(requestMIV, userDTO);
        requestMIV.setApi(API_MIV.xoaboHD);

        DestructionInvoiceMIV destructionInvoiceMIV = new DestructionInvoiceMIV();
        destructionInvoiceMIV.setInv_InvoiceAuth_id(Utils.uuidConvertToGUID(eInvoice.getiDMIV()));
        destructionInvoiceMIV.setGhi_chu(requestCancelInvoiceDTO.getNameDocument());
        destructionInvoiceMIV.setNgayvb(getDateString(requestCancelInvoiceDTO.getDateDocument(), "yyyy-MM-dd"));
        destructionInvoiceMIV.setGhi_chu(requestCancelInvoiceDTO.getNote());

        String jsonData = Utils.ObjectToJSON(destructionInvoiceMIV);
        requestMIV.setData(jsonData);
        Respone_MIV responeMiv = RestfullApiMIV.post(requestMIV);

        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        if (Boolean.TRUE.equals(responeMiv.getOk())) {
            respone_sds.setStatus(Success);
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(eInvoice.getId());
            keyInvoiceNoDTO.add(keyInvoiceNo);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            eInvoiceRepository.updateAfterCancelInvoice(respone_sds);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeMiv.getError());
            KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
            keyInvoiceMsg.setId(eInvoice.getId());
            keyInvoiceMsg.setMessage(responeMiv.getError());
            keyInvoiceMsgDTO.add(keyInvoiceMsg);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
        }
        return respone_sds;
    }

    /**
     * @param requestConvertInvoiceDTO
     * @return Chuyển đổi chứng minh nguồn gốc hóa đơn
     * @Author Hautv
     */
    @Override
    public Respone_SDS convertedOrigin(RequestConvertInvoiceDTO requestConvertInvoiceDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        if (requestConvertInvoiceDTO.getUuidList().size() > 10) {
            respone_sds.setStatus(1);
            respone_sds.setMessage("Số lượng không được vượt quá 10");
            return respone_sds;
        }
        UserDTO userDTO = userService.getAccount();
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    respone_sds = convertedOriginSDS(requestConvertInvoiceDTO, userDTO);
                    break;
                case SIV:
                    respone_sds = convertedOriginSIV(requestConvertInvoiceDTO, userDTO);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
        }
        return respone_sds;
    }

    private Respone_SDS convertedOriginSDS(RequestConvertInvoiceDTO requestConvertInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(requestConvertInvoiceDTO.getUuidList());
        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        List<Respone_SDS> lstResponeSds = new ArrayList<>();
        for (EInvoice eInvoice : eInvoices) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoice.getInvoiceTemplate());
            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getId()));
            request_sds.setOption(1);
            Respone_SDS respone_sds1 = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.GET_INVOCIE_PDF, userDTO);
            respone_sds1.setRawBytes(null); // Set null để trả về client nhanh
            if (respone_sds1.getStatus().equals(2)) {
                KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
                keyInvoiceNo.setId(Utils.uuidConvertToGUID(request_sds.getIkey()));
                keyInvoiceNoDTO.add(keyInvoiceNo);
            } else {
                KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                keyInvoiceMsg.setId(Utils.uuidConvertToGUID(request_sds.getIkey()));
                keyInvoiceMsgDTO.add(keyInvoiceMsg);
            }
            lstResponeSds.add(respone_sds1);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
            eInvoiceRepository.updateAfterConvertedOrigin(respone_sds);
            respone_sds.setStatus(Success);
        }
        return respone_sds;
    }

    private Respone_SDS convertedOriginSIV(RequestConvertInvoiceDTO requestConvertInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(requestConvertInvoiceDTO.getUuidList());
        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        List<ResponeSIV> lstResponeSIV = new ArrayList<>();
        for (EInvoice eInvoice : eInvoices) {
            RequestSIV requestSIV = new RequestSIV();
            setDefaultRequestSIV(requestSIV, userDTO);
            requestSIV.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            requestSIV.setApi(CreateExchangeInvoiceFile);
            ConvertInvoice convertInvoice = new ConvertInvoice();
            convertInvoice.setSupplierTaxCode(requestSIV.getCodeTax());
            convertInvoice.setExchangeUser(requestConvertInvoiceDTO.getNameUser());
            convertInvoice.setStrIssueDate(getDateString(eInvoice.getInvoiceDate(), "yyyyMMddHHmmss"));
            convertInvoice.setInvoiceNo(eInvoice.getInvoiceSeries() + eInvoice.getInvoiceNo());
            String jsonData =
                "supplierTaxCode=" + URLEncoder.encode(convertInvoice.getSupplierTaxCode()) +
                    "&invoiceNo=" + URLEncoder.encode(convertInvoice.getInvoiceNo()) +
                    "&strIssueDate=" + URLEncoder.encode(convertInvoice.getStrIssueDate()) +
                    "&exchangeUser=" + URLEncoder.encode(getString(convertInvoice.getExchangeUser()));
            requestSIV.setData(jsonData);
            ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
            if (responeSIV.getErrorCode() == null) {
                KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
                keyInvoiceNo.setId(eInvoice.getId());
                keyInvoiceNoDTO.add(keyInvoiceNo);
            } else {
                KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                keyInvoiceMsg.setId(eInvoice.getId());
                keyInvoiceMsg.setMessage(responeSIV.getDescription());
                keyInvoiceMsgDTO.add(keyInvoiceMsg);
            }
            lstResponeSIV.add(responeSIV);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
            eInvoiceRepository.updateAfterConvertedOrigin(respone_sds);
            respone_sds.setStatus(Success);
        }
        return respone_sds;
    }

    private Respone_SDS convertedOriginVNPT(RequestConvertInvoiceDTO requestConvertInvoiceDTO, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(requestConvertInvoiceDTO.getUuidList());
        List<KeyInvoiceNo> keyInvoiceNoDTO = new ArrayList<>();
        List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
        List<ResponeSIV> lstResponeSIV = new ArrayList<>();
        for (EInvoice eInvoice : eInvoices) {
            RequestSIV requestSIV = new RequestSIV();
            setDefaultRequestSIV(requestSIV, userDTO);
            requestSIV.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            requestSIV.setApi(CreateExchangeInvoiceFile);
            ConvertInvoice convertInvoice = new ConvertInvoice();
            convertInvoice.setSupplierTaxCode(requestSIV.getCodeTax());
            convertInvoice.setExchangeUser(requestConvertInvoiceDTO.getNameUser());
            convertInvoice.setStrIssueDate(getDateString(eInvoice.getInvoiceDate(), "yyyyMMddHHmmss"));
            convertInvoice.setInvoiceNo(eInvoice.getInvoiceSeries() + eInvoice.getInvoiceNo());
            String jsonData =
                "supplierTaxCode=" + URLEncoder.encode(convertInvoice.getSupplierTaxCode()) +
                    "&invoiceNo=" + URLEncoder.encode(convertInvoice.getInvoiceNo()) +
                    "&strIssueDate=" + URLEncoder.encode(convertInvoice.getStrIssueDate()) +
                    "&exchangeUser=" + URLEncoder.encode(getString(convertInvoice.getExchangeUser()));
            requestSIV.setData(jsonData);
            ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
            if (responeSIV.getErrorCode() == null) {
                KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
                keyInvoiceNo.setId(eInvoice.getId());
                keyInvoiceNoDTO.add(keyInvoiceNo);
            } else {
                KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                keyInvoiceMsg.setId(eInvoice.getId());
                keyInvoiceMsg.setMessage(responeSIV.getDescription());
                keyInvoiceMsgDTO.add(keyInvoiceMsg);
            }
            lstResponeSIV.add(responeSIV);
            respone_sds.getData().setKeyInvoiceNoDTO(keyInvoiceNoDTO);
            respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
            eInvoiceRepository.updateAfterConvertedOrigin(respone_sds);
            respone_sds.setStatus(Success);
        }
        return respone_sds;
    }

    /**
     * @param eInvoices
     * @return Tọa hóa đơn chờ
     * @Author Hautv
     */
    private Respone_SDS crInvoiceWaitSign(List<EInvoice> eInvoices, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        // region Xóa hóa đơn nháp
        List<Respone_SDS> lstRespone_sds = deleteEInovice(eInvoices.stream().map(EInvoice::getId).collect(Collectors.toList()));
        // endregion
        // region Lấy request số hóa đơn chờ ký
        Request_SDS request_sds = new Request_SDS();
        request_sds.setPattern(eInvoices.get(0).getInvoiceTemplate());
        request_sds.setSerial(eInvoices.get(0).getInvoiceSeries());
        for (EInvoice eInvoice : eInvoices
        ) {
            request_sds.getIkeyDate().put(Utils.uuidConvertToGUID(eInvoice.getId()), LocalDateToString(eInvoice.getInvoiceDate()));
        }
        // endregion
        // region Tạo đẩy dữ kiệu vào hóa đơn chờ ký
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.CREATE_INVOICE_STRIP, userDTO);
        if (respone_sds.getStatus().equals(2)) {
            //Update dữ liệu vào SABill sau khi tạo hóa đơn chờ
            eInvoiceRepository.updateAfterCreateKeyInvoiceNoWaitSign(respone_sds);
            String xml = createXMLData(eInvoices, userDTO, respone_sds.getData().getKeyInvoiceNo());
            request_sds.setIkeyDate(null);
            request_sds.setXmlData(xml);
            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.IMPORT_RESERVED_INVOICE, userDTO);
        } else {
            // Do xóa hóa đơn nháp nên phải tạo lại
            request_sds.setIkeyDate(null);
            String xml = createXMLData(eInvoices, userDTO);
            request_sds.setXmlData(xml);
            Respone_SDS respone_sdsCreateDaft = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.CREATE_INVOICE_DRAFT, userDTO);
        }
        // endregion
        return respone_sds;
    }

    @Override
    public Respone_SDS connectEInvocie(ConnectEInvoiceDTO connectEInvoiceDTO) {
        switch (connectEInvoiceDTO.getSupplierCode()) {
            case SDS:
                return connectSDS(connectEInvoiceDTO);
            case SIV:
                return connectSIV(connectEInvoiceDTO);
            case MIV:
                return connectMIV(connectEInvoiceDTO);
            case VNPT:
                return connectSDS(connectEInvoiceDTO);
        }
        return new Respone_SDS();
    }

    Respone_SDS connectSIV(ConnectEInvoiceDTO connectEInvoiceDTO) {
        Respone_SDS responeSds = new Respone_SDS();
        RequestSIV requestSIV = new RequestSIV();
        UserDTO userDTO = userService.getAccount();
//        setDefaultRequestSIV(requestSIV, userDTO);
        requestSIV.setCodeTax(userDTO.getOrganizationUnit().gettaxCode());
        requestSIV.setUserName(connectEInvoiceDTO.getUserName());
        requestSIV.setPassword(connectEInvoiceDTO.getPassword());
        requestSIV.setUrl(connectEInvoiceDTO.getPath());
        requestSIV.setApi(Constants.EInvoice.API_VIETTEL.GetListInvoiceWithDate);

        GetListInvoiceWithDate getListInvoiceWithDate = new GetListInvoiceWithDate();
        getListInvoiceWithDate.setSupplierTaxCode(requestSIV.getCodeTax());
        getListInvoiceWithDate.setFromDate("13/04/2020");
        getListInvoiceWithDate.setToDate(getDateString(LocalDate.now()));
        String dataJson = Utils.ObjectToJSON(getListInvoiceWithDate);
        requestSIV.setData(dataJson);
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);

        if (Constants.EInvoice.Respone.errorCode.equals(responeSIV.getErrorCode())) {
            responeSds.setStatus(Success);
            updateEISystemOption(connectEInvoiceDTO);
        } else {
            responeSds.setStatus(Constants.EInvoice.Respone.Fail);
        }
        return responeSds;
    }

    Respone_SDS connectMIV(ConnectEInvoiceDTO connectEInvoiceDTO) {
        Respone_SDS responeSds = new Respone_SDS();
        RequestMIV requestMIV = new RequestMIV();
        UserDTO userDTO = userService.getAccount();
        setDefaultRequestMIV(requestMIV, userDTO);
        Login_MIV login_miv = new Login_MIV();
        login_miv.setUsername(connectEInvoiceDTO.getUserName());
        login_miv.setPassword(connectEInvoiceDTO.getPassword());
        login_miv.setMa_dvcs("VP");
        requestMIV.setUrl(connectEInvoiceDTO.getPath());
        String jsonData = Utils.ObjectToJSON(login_miv);
        requestMIV.setData(jsonData);
        requestMIV.setApi(API_MIV.Login);

        Respone_MIV responeMiv = RestfullApiMIV.post(requestMIV);

        if (!StringUtils.isEmpty(responeMiv.getToken())) {
            connectEInvoiceDTO.setToken(responeMiv.getToken());
            responeSds.setStatus(Success);
            updateEISystemOption(connectEInvoiceDTO);
        } else {
            responeSds.setStatus(Constants.EInvoice.Respone.Fail);
        }
        return responeSds;
    }

    private void setDefaultRequestSIV(RequestSIV requestSIV, UserDTO userDTO) {
        String userName = Utils.getEI_TenDangNhap(userDTO);
        String password = Utils.getEI_MatKhau(userDTO);
        String path = Utils.getEI_Path(userDTO);
        String taxCode = userDTO.getOrganizationUnit().gettaxCode();

        requestSIV.setUrl(path);
        requestSIV.setPassword(password);
        requestSIV.setUserName(userName);
        requestSIV.setCodeTax(taxCode);

    }

    private void setDefaultRequestMIV(RequestMIV requestMIV, UserDTO userDTO) {
        String userName = Utils.getEI_TenDangNhap(userDTO);
        String password = Utils.getEI_MatKhau(userDTO);
        String path = Utils.getEI_Path(userDTO);
        String taxCode = userDTO.getOrganizationUnit().gettaxCode();
        String Auth = "Bear " + Utils.getEI_Token_MIV(userDTO) + ";VP";

        requestMIV.setUrl(path);
        requestMIV.setPassword(password);
        requestMIV.setUserName(userName);
        requestMIV.setAuth(Auth);
        requestMIV.setCodeTax(taxCode);

    }

    Respone_SDS connectSDS(ConnectEInvoiceDTO connectEInvoiceDTO) {
        List<EInvoice> eInvoices = eInvoiceRepository.findAll();
        Request_SDS request_sds = new Request_SDS();
        request_sds.setIkeys(eInvoices.stream().map(EInvoice::getId).collect(Collectors.toList()));
        for (UUID uuid : request_sds.getIkeys()) {
            request_sds.getIkeys().set(request_sds.getIkeys().indexOf(uuid), Utils.uuidConvertToGUID(uuid));
        }
        Respone_SDS responeSds = RestApiService.post(
            request_sds,
            Constants.EInvoice.API_SDS.GET_INVOCIES_BY_IKEYS,
            connectEInvoiceDTO.getPath(),
            connectEInvoiceDTO.getUserName(),
            connectEInvoiceDTO.getPassword()
        );

        if (responeSds.getStatus().equals(2)) {
            updateEISystemOption(connectEInvoiceDTO);
            if (responeSds.getData().getInvoices() != null && responeSds.getData().getInvoices().size() > 0) {
                eInvoiceRepository.updateSABill(responeSds.getData().getInvoices());
            }
        }
        return responeSds;
    }

    /**
     * @param uuids
     * @return Xóa hóa đơn
     * @Author Hautv
     */
    @Override
    public List<Respone_SDS> deleteEInovice(List<UUID> uuids) {
        List<Request_SDS> lstRequest_sds = new ArrayList<>();
        List<Respone_SDS> lstRespone_SDS = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        List<EInvoice> eInvoices = eInvoiceRepository.findAllByListID(uuids);
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT && eInvoices.size() > 0) {
            for (EInvoice eInvoice : eInvoices
            ) {
                Request_SDS request_sds = new Request_SDS();
            /*Map<String, List<EInvoice>> grpInvoiceTemplate =
                eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceTemplate()));
            Map<String, List<EInvoice>> grpInvoiceSeries =
                eInvoices.stream().collect(Collectors.groupingBy(w -> w.getInvoiceSeries()));*/
                request_sds.setPattern(eInvoice.getInvoiceTemplate());
                request_sds.setSerial(eInvoice.getInvoiceSeries());
//            request_sds.setIkeys(uuids.stream().map(n -> Utils.uuidConvertToGUID(n)).collect(Collectors.toList()));
                request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getId()));
                lstRequest_sds.add(request_sds);
            }
            if (lstRequest_sds.size() > 0) {
                for (Request_SDS request_sds : lstRequest_sds
                ) {
                    Respone_SDS respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.REMOVE_UNSIGNED_INVOICE, userDTO);
                    lstRespone_SDS.add(respone_sds);
                }
            }
        } else {
            Respone_SDS respone_sds = new Respone_SDS();
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);
            lstRespone_SDS.add(respone_sds);
            /*respone_sds.setStatus(5);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP);*/
        }
        return lstRespone_SDS;
    }

    @Override
    public Page<EInvoice> findAllEInvoiceWaitSign(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Page<EInvoice> eInvoices = eInvoiceRepository.findAllEInvoiceWaitSignByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
        if (eInvoices.getContent().size() > 0) {
            eInvoices.getContent().get(0).setTotal(eInvoiceRepository.sumFindAllEInvoiceWaitSignByCompanyID(userDTO.getOrganizationUnit().getId()));
        }
        return eInvoices;
    }

    @Override
    public Page<EInvoice> findAllEInvoiceCanceled(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Page<EInvoice> eInvoices = eInvoiceRepository.findAllEInvoiceCanceledByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
        if (eInvoices.getContent().size() > 0) {
            eInvoices.getContent().get(0).setTotal(eInvoiceRepository.sumFindAllEInvoiceCanceledByCompanyID(userDTO.getOrganizationUnit().getId()));
        }
        return eInvoices;
    }

    @Override
    public Page<EInvoice> findAllEInvoiceForConvert(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Page<EInvoice> eInvoices = eInvoiceRepository.findAllEInvoiceForConvertByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
        if (eInvoices.getContent().size() > 0) {
            eInvoices.getContent().get(0).setTotal(eInvoiceRepository.sumFindAllEInvoiceForConvertByCompanyID(userDTO.getOrganizationUnit().getId()));
        }
        return eInvoices;
    }

    @Override
    public Page<EInvoice> findAllEInvoiceAdjusted(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        return eInvoiceRepository.findAllEInvoiceAdjustedByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
    }

    @Override
    public Page<EInvoice> findAllEInvoiceReplaced(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        return eInvoiceRepository.findAllEInvoiceReplacedByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
    }

    private String LocalDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private Respone_SDS publishAdjustedSDS(EInvoice eInvoice) {
        Respone_SDS respone_sds = null;
        UserDTO userDTO = userService.getAccount();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoice.getInvoiceTemplate());
            request_sds.setSerial(eInvoice.getInvoiceSeries());
            String xml = createXMLDataEInoviceAdjusted(eInvoice, userDTO, eInvoice.geteInvoiceDetails());
            request_sds.setXmlData(xml);
            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDAdjustInv()));
            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.PUBLISH_INVOCIE_ADJUST_SERVER, userDTO);
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS publishAdjustedSIV(EInvoice eInvoice, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String invoiceSeries = eInvoice.getInvoiceSeries();
        if (HDDT) {
            RequestSIV requestSIV = new RequestSIV();
            setDefaultRequestSIV(requestSIV, userDTO);
            EInvoiceVietTel eInvoiceVietTel = getEInvoiceAdjustedSIV(Optional.of(eInvoice), userDTO, null);
            String jsonData = Utils.ObjectToJSON(eInvoiceVietTel);
            requestSIV.setApi(CreateInvoice + requestSIV.getCodeTax());
            requestSIV.setData(jsonData);
            ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
            if (responeSIV.getErrorCode() == null) {
                respone_sds.setStatus(Success);
                Map<UUID, String> keyAndNo = new HashMap<>();
                keyAndNo.put(eInvoiceVietTel.getGeneralInvoiceInfo().getTransactionUuid(), getInvoiceNoSIV(responeSIV.getResult().getInvoiceNo(), invoiceSeries));
                respone_sds.getData().setKeyInvoiceNo(keyAndNo);
            } else {
                respone_sds.setStatus(Fail);
                respone_sds.setMessage(responeSIV.getDescription());
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS publishReplacedSDS(EInvoice eInvoice) {
        Respone_SDS respone_sds = null;
        UserDTO userDTO = userService.getAccount();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoice.getInvoiceTemplate());
            request_sds.setSerial(eInvoice.getInvoiceSeries());
            String xml = createXMLDataEInoviceReplaced(eInvoice, userDTO, eInvoice.geteInvoiceDetails());
            request_sds.setXmlData(xml);
            request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()));
            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.PUBLISH_INVOCIE_REPLACE_SERVER, userDTO);
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS publishReplacedSIV(EInvoice eInvoice, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String invoiceSeries = eInvoice.getInvoiceSeries();
        if (HDDT) {
            RequestSIV requestSIV = new RequestSIV();
            setDefaultRequestSIV(requestSIV, userDTO);
            EInvoiceVietTel eInvoiceVietTel = getEInvoiceReplaceSIV(Optional.of(eInvoice), userDTO, null);
            String jsonData = Utils.ObjectToJSON(eInvoiceVietTel);
            requestSIV.setApi(CreateInvoice + requestSIV.getCodeTax());
            requestSIV.setData(jsonData);
            ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
            if (responeSIV.getErrorCode() == null) {
                respone_sds.setStatus(Success);
                Map<UUID, String> keyAndNo = new HashMap<>();
                keyAndNo.put(eInvoiceVietTel.getGeneralInvoiceInfo().getTransactionUuid(), getInvoiceNoSIV(responeSIV.getResult().getInvoiceNo(), invoiceSeries));
                respone_sds.getData().setKeyInvoiceNo(keyAndNo);
            } else {
                respone_sds.setStatus(Fail);
                respone_sds.setMessage(responeSIV.getDescription());
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS publishSDS(List<EInvoice> eInvoices, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        if (HDDT) {
            Request_SDS request_sds = new Request_SDS();
            request_sds.setPattern(eInvoices.get(0).getInvoiceTemplate());
            request_sds.setSerial(eInvoices.get(0).getInvoiceSeries());
            String xml = createXMLData(eInvoices, userDTO);
            request_sds.setXmlData(xml);
            respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.PUBLISH_INVOICE_SERVER, userDTO);
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS publishSIV(List<EInvoice> eInvoices, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String invoiceSeries = eInvoices.get(0).getInvoiceSeries();
        if (HDDT) {
            RequestSIV requestSIV = new RequestSIV();
            ListEInvoiceVietTel listEInvoiceVietTel = new ListEInvoiceVietTel();
            setDefaultRequestSIV(requestSIV, userDTO);
            for (EInvoice eInvoice : eInvoices) {
                EInvoiceVietTel eInvoiceVietTel = getEInvoiceSIV(Optional.of(eInvoice), userDTO);
                listEInvoiceVietTel.getCommonInvoiceInputs().add(eInvoiceVietTel);
            }
            String jsonData = Utils.ObjectToJSON(listEInvoiceVietTel);
            requestSIV.setApi(CreateBatchInvoice + requestSIV.getCodeTax());
            requestSIV.setData(jsonData);
            ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
            if (responeSIV.getErrorCode() == null) {
                respone_sds.setStatus(Success);
                Map<UUID, String> lstSucess = getKeyAndInvoiceFromResponeSIV(responeSIV, invoiceSeries);
                Map<UUID, String> lstFail = getKeyAndMessFromResponeSIV(responeSIV);
                respone_sds.getData().setKeyInvoiceNo(lstSucess);
                respone_sds.getData().setKeyInvoiceMsg(lstFail);
                List<KeyInvoiceMsg> keyInvoiceMsgDTO = new ArrayList<>();
                for (Map.Entry<UUID, String> entry : lstFail.entrySet()) {
                    KeyInvoiceMsg keyInvoiceMsg = new KeyInvoiceMsg();
                    keyInvoiceMsg.setId(entry.getKey());
                    keyInvoiceMsg.setMessage(entry.getValue());
                    keyInvoiceMsgDTO.add(keyInvoiceMsg);
                }
                respone_sds.getData().setKeyInvoiceMsgDTO(keyInvoiceMsgDTO);
            } else {
                respone_sds.setStatus(Fail);
                respone_sds.setMessage(responeSIV.getDescription());
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Map<UUID, String> getKeyAndInvoiceFromResponeSIV(ResponeSIV responeSIV, String invoiceSeries) {
        Map<UUID, String> keyInvoiceNo = new HashMap<>();
        for (ResponeCreateListInvoice invoiceInfo : responeSIV.getCreateInvoiceOutputs()) {
            if (invoiceInfo.getErrorCode() == null) {
                keyInvoiceNo.put(invoiceInfo.getTransactionUuid(), getInvoiceNoSIV(invoiceInfo.getResult().getInvoiceNo(), invoiceSeries));
            }
        }
        return keyInvoiceNo;
    }

    private Map<UUID, String> getKeyAndMessFromResponeSIV(ResponeSIV responeSIV) {
        Map<UUID, String> keyInvoiceNo = new HashMap<>();
        for (ResponeCreateListInvoice invoiceInfo : responeSIV.getCreateInvoiceOutputs()) {
            if (invoiceInfo.getErrorCode() != null) {
                keyInvoiceNo.put(invoiceInfo.getTransactionUuid(), invoiceInfo.getDescription());
            }

        }
        return keyInvoiceNo;
    }

    private String getInvoiceNoSIV(String invN, String invoiceSeries) {
        return invN.substring(invoiceSeries.length());
    }

    private Respone_SDS getDigestForAdjusted(EInvoice eInvoice, String certString) {
        Respone_SDS respone_sds = null;
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    respone_sds = getDigestForAdjustedSDS(eInvoice, certString, userDTO);
                    break;
                case SIV:
                    respone_sds = getDigestForAdjustedSIV(eInvoice, certString, userDTO);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }
        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS getDigestForAdjustedSDS(EInvoice eInvoice, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds = null;
        Request_SDS request_sds = new Request_SDS();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        request_sds.setPattern(eInvoice.getInvoiceTemplate());
        request_sds.setSerial(eInvoice.getInvoiceSeries());
        String xml = createXMLDataEInoviceAdjusted(eInvoice, userDTO, eInvoice.geteInvoiceDetails());
        request_sds.setXmlData(xml);
        request_sds.setCertString(certString);
        request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDAdjustInv()));
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_GET_DIGES_FOR_ADJUSTMENT, userDTO);
        return respone_sds;
    }

    private Respone_SDS getDigestForAdjustedSIV(EInvoice eInvoice, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        RequestSIV requestSIV = new RequestSIV();
        String invoiceSeries = eInvoice.getInvoiceSeries();
        ListEInvoiceVietTel listEInvoiceVietTel = new ListEInvoiceVietTel();
        setDefaultRequestSIV(requestSIV, userDTO);
        EInvoiceVietTel eInvoiceVietTel = getEInvoiceAdjustedSIV(Optional.of(eInvoice), userDTO, certString);
        String jsonData = Utils.ObjectToJSON(eInvoiceVietTel);
        requestSIV.setApi(API_VIETTEL.CreateInvoiceUsbTokenGetHash + requestSIV.getCodeTax());
        requestSIV.setData(jsonData);
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        if (responeSIV.getErrorCode() == null) {
            respone_sds.setStatus(Success);
            Map<String, String> stringStringMap = new HashMap<>();
            stringStringMap.put(Utils.uuidConvertToGUID(eInvoice.getId()).toString(), responeSIV.getResult().getHashString());
            respone_sds.getData().setDigestData(stringStringMap);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
        }
        return respone_sds;
    }

    private Respone_SDS getDigestForReplaced(EInvoice eInvoice, String certString) {
        Respone_SDS respone_sds = null;
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    respone_sds = getDigestForReplacedSDS(eInvoice, certString, userDTO);
                    break;
                case SIV:
                    respone_sds = getDigestForReplacedSIV(eInvoice, certString, userDTO);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }

        } else {
            respone_sds.setStatus(1);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS getDigestForReplacedSDS(EInvoice eInvoice, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds;
        Request_SDS request_sds = new Request_SDS();
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
        request_sds.setPattern(eInvoice.getInvoiceTemplate());
        request_sds.setSerial(eInvoice.getInvoiceSeries());
        String xml = createXMLDataEInoviceReplaced(eInvoice, userDTO, eInvoice.geteInvoiceDetails());
        request_sds.setXmlData(xml);
        request_sds.setCertString(certString);
        request_sds.setIkey(Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()));
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_GET_DIGES_FOR_REPLACEMENT, userDTO);
        return respone_sds;
    }

    private Respone_SDS getDigestForReplacedSIV(EInvoice eInvoice, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        RequestSIV requestSIV = new RequestSIV();
        String invoiceSeries = eInvoice.getInvoiceSeries();
        ListEInvoiceVietTel listEInvoiceVietTel = new ListEInvoiceVietTel();
        setDefaultRequestSIV(requestSIV, userDTO);
       /* for (EInvoice eInvoice : eInvoices) {
            EInvoiceVietTel eInvoiceVietTel = getEInvoiceSIV(Optional.of(eInvoice), userDTO, certString);
            listEInvoiceVietTel.getCommonInvoiceInputs().add(eInvoiceVietTel);
        }*/
        EInvoiceVietTel eInvoiceVietTel = getEInvoiceReplaceSIV(Optional.of(eInvoice), userDTO, certString);
        String jsonData = Utils.ObjectToJSON(eInvoiceVietTel);
        requestSIV.setApi(API_VIETTEL.CreateInvoiceUsbTokenGetHash + requestSIV.getCodeTax());
        requestSIV.setData(jsonData);
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        if (responeSIV.getErrorCode() == null) {
            respone_sds.setStatus(Success);
            Map<String, String> stringStringMap = new HashMap<>();
            stringStringMap.put(Utils.uuidConvertToGUID(eInvoice.getId()).toString(), responeSIV.getResult().getHashString());
            respone_sds.getData().setDigestData(stringStringMap);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
        }
        return respone_sds;
    }

    private Respone_SDS getDigestForNew(List<EInvoice> eInvoices, String certString) {
        Respone_SDS respone_sds = null;
        UserDTO userDTO = userService.getAccount();
        Boolean HDDT = Utils.getTCKHAC_SDTichHopHDDT(userDTO);
        String NCCDV = Utils.getEI_IDNhaCungCapDichVu(userDTO);
        if (HDDT) {
            switch (NCCDV) {
                case SDS:
                    respone_sds = getDigestForNewSDS(eInvoices, certString, userDTO);
                    break;
                case SIV:
                    respone_sds = getDigestForNewSIV(eInvoices, certString, userDTO);
                    break;
                case MIV:
                    break;
                case VNPT:
                    break;
            }
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(Constants.EInvoice.CHUA_TICH_HOP); // chưa tích hợp hóa đơn điện tử
        }
        return respone_sds;
    }

    private Respone_SDS getDigestForNewSDS(List<EInvoice> eInvoices, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds = null;
        Request_SDS request_sds = new Request_SDS();
        request_sds.setPattern(eInvoices.get(0).getInvoiceTemplate());
        request_sds.setSerial(eInvoices.get(0).getInvoiceSeries());
        String xml = createXMLData(eInvoices, userDTO);
        request_sds.setXmlData(xml);
        request_sds.setCertString(certString);
        respone_sds = RestApiService.post(request_sds, Constants.EInvoice.API_SDS.EXTERNAL_GET_DIGES_FOR_IMPORTATION, userDTO);
        return respone_sds;
    }

    private Respone_SDS getDigestForNewSIV(List<EInvoice> eInvoices, String certString, UserDTO userDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        RequestSIV requestSIV = new RequestSIV();
        String invoiceSeries = eInvoices.get(0).getInvoiceSeries();
        ListEInvoiceVietTel listEInvoiceVietTel = new ListEInvoiceVietTel();
        setDefaultRequestSIV(requestSIV, userDTO);
       /* for (EInvoice eInvoice : eInvoices) {
            EInvoiceVietTel eInvoiceVietTel = getEInvoiceSIV(Optional.of(eInvoice), userDTO, certString);
            listEInvoiceVietTel.getCommonInvoiceInputs().add(eInvoiceVietTel);
        }*/
        EInvoiceVietTel eInvoiceVietTel = getEInvoiceSIV(Optional.of(eInvoices.get(0)), userDTO, certString);
        String jsonData = Utils.ObjectToJSON(eInvoiceVietTel);
        requestSIV.setApi(API_VIETTEL.CreateInvoiceUsbTokenGetHash + requestSIV.getCodeTax());
        requestSIV.setData(jsonData);
        ResponeSIV responeSIV = RestfullApiSIV.post(requestSIV);
        if (responeSIV.getErrorCode() == null) {
            respone_sds.setStatus(Success);
            Map<String, String> stringStringMap = new HashMap<>();
            stringStringMap.put(Utils.uuidConvertToGUID(eInvoices.get(0).getId()).toString(), responeSIV.getResult().getHashString());
            respone_sds.getData().setDigestData(stringStringMap);
        } else {
            respone_sds.setStatus(Fail);
            respone_sds.setMessage(responeSIV.getDescription());
        }
        return respone_sds;
    }

    private String createXMLData(List<EInvoice> eInvoices, UserDTO userDTO) {
        return createXMLData(eInvoices, userDTO, null);
    }

    private String createXMLData(List<EInvoice> eInvoices, UserDTO userDTO, Map<UUID, String> keyInvoiceNo) {
        String result = null;
        String currentCurrency = userDTO.getOrganizationUnit().getCurrencyID();
        InvoicesSDS_DTO invoicesSDS_dto = new InvoicesSDS_DTO();
        List<InvSDS_DTO> Invs = new ArrayList<>();
        for (EInvoice eInvoice : eInvoices
        ) {
            Boolean currentCurrency_bool = currentCurrency.equals(eInvoice.getCurrencyID());
            List<EInvoiceDetails> eInvoiceDetails = eInvoiceRepository.findAllEInvoiceDetailsByID(eInvoice.getId(), eInvoice.getRefTable());
            if (eInvoiceDetails.size() > 0) {
                EInvoiceSDS_DTO eInvoiceSDS_dto = new EInvoiceSDS_DTO();
                eInvoiceSDS_dto.setKey(Utils.uuidConvertToGUID(eInvoice.getId()));
                if (keyInvoiceNo != null) {
                    eInvoiceSDS_dto.setInvNo(StringUtils.leftPad(keyInvoiceNo.get(eInvoiceSDS_dto.getKey()), 7, '0'));
                }
                eInvoiceSDS_dto.setCusCode(getString(eInvoice.getAccountingObjectCode()));
                eInvoiceSDS_dto.setBuyer(getString(eInvoice.getContactName()));
                eInvoiceSDS_dto.setCusName(getString(eInvoice.getAccountingObjectName()));
                eInvoiceSDS_dto.setCusAddress(getString(eInvoice.getAccountingObjectAddress()));
                eInvoiceSDS_dto.setCusBankName(getString(eInvoice.getAccountingObjectBankName()));
                eInvoiceSDS_dto.setCusBankNo(getString(eInvoice.getAccountingObjectBankAccount()));
                eInvoiceSDS_dto.setCusPhone(eInvoice.getContactMobile());
                eInvoiceSDS_dto.setCusTaxCode(eInvoice.getCompanyTaxCode());
                eInvoiceSDS_dto.setEmail(eInvoice.getAccountingObjectEmail());
                eInvoiceSDS_dto.setPaymentMethod(getString(eInvoice.getPaymentMethod()));
                eInvoiceSDS_dto.setArisingDate(eInvoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                eInvoiceSDS_dto.setExchangeRate(eInvoice.getExchangeRate());
                eInvoiceSDS_dto.setCurrencyUnit(eInvoice.getCurrencyID());
                List<ProductSDS_DTO> lstProduct = getProduct(eInvoiceDetails, currentCurrency_bool);
                eInvoiceSDS_dto.getProducts().setProduct(lstProduct);
                if (lstProduct.size() > 0) {
                    eInvoiceSDS_dto.setVATRate(lstProduct.get(0).getVATRate());
                } else {
                    eInvoiceSDS_dto.setVATRate(-1);
                }
                BigDecimal Amount = BigDecimal.ZERO;
                BigDecimal Total = BigDecimal.ZERO;
                BigDecimal VATAmount = BigDecimal.ZERO;
                for (ProductSDS_DTO productSDS_dto : eInvoiceSDS_dto.getProducts().getProduct()
                ) {
                    Amount = Amount.add(productSDS_dto.getAmount());
                    Total = Total.add(productSDS_dto.getTotal());
                    VATAmount = VATAmount.add(productSDS_dto.getVATAmount());
                }
                if (currentCurrency_bool) {
                    eInvoiceSDS_dto.setAmount(Utils.round(Amount));
                    eInvoiceSDS_dto.setTotal(Utils.round(Total));
                    eInvoiceSDS_dto.setVATAmount(Utils.round(VATAmount));
                } else {
                    eInvoiceSDS_dto.setAmount(Utils.round(Amount, 2));
                    eInvoiceSDS_dto.setTotal(Utils.round(Total, 2));
                    eInvoiceSDS_dto.setVATAmount(Utils.round(VATAmount, 2));
                }
                eInvoiceSDS_dto.setAmountInWords(Utils.GetAmountInWords(Amount, eInvoiceSDS_dto.getCurrencyUnit(), userDTO));
                InvSDS_DTO invSDS_dto = new InvSDS_DTO();
                invSDS_dto.setInvoice(eInvoiceSDS_dto);
                Invs.add(invSDS_dto);
//                String xml = Utils.ObjectToXML(invoicesSDS_dto);
//                xml = xml.substring(xml.indexOf('\n') + 1); // Xóa dòng đầu <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            }
        }
        invoicesSDS_dto.setInv(Invs);
        result = Utils.jaxbObjectToXML(invoicesSDS_dto);
        return result;
    }

    private Object cloneObject(Object obj) {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    private List<ProductSDS_DTO> getProduct(List<EInvoiceDetails> lstEInvoiceDetails, Boolean currentCurrency_bool) {
        List<ProductSDS_DTO> lstResult = new ArrayList<>();
        if (lstEInvoiceDetails.get(0).getOrderPriority() != null) {
            lstEInvoiceDetails.sort(Comparator.comparingInt(EInvoiceDetails::getOrderPriority));
        }
        for (EInvoiceDetails eInvoiceDetails : lstEInvoiceDetails
        ) {
            lstResult.addAll(getProduct(eInvoiceDetails, currentCurrency_bool));
        }
        return lstResult;
    }

    private List<ProductSDS_DTO> getProduct(List<EInvoiceDetails> lstEInvoiceDetails, Boolean currentCurrency_bool, String nCCDV) {
        List<ProductSDS_DTO> lstResult = new ArrayList<>();
        lstEInvoiceDetails.sort(Comparator.comparingInt(EInvoiceDetails::getOrderPriority));
        for (EInvoiceDetails eInvoiceDetails : lstEInvoiceDetails) {
            lstResult.addAll(getProduct(eInvoiceDetails, currentCurrency_bool, nCCDV));
        }
        return lstResult;
    }

    private List<ProductSDS_DTO> getProduct(EInvoiceDetails eInvoiceDetails, Boolean currentCurrency_bool) {
        return getProduct(eInvoiceDetails, currentCurrency_bool, null);
    }

    private List<ProductSDS_DTO> getProduct(EInvoiceDetails eInvoiceDetails, Boolean currentCurrency_bool, String nCCDV) {
        List<ProductSDS_DTO> lstResult = new ArrayList<>();
        ProductSDS_DTO productSDS_dto = new ProductSDS_DTO();
        productSDS_dto.setCode(eInvoiceDetails.getMaterialGoodsCode());
        productSDS_dto.setProdName(eInvoiceDetails.getDescription());
        productSDS_dto.setProdUnit(eInvoiceDetails.getUnitName()); // đơn vị
        productSDS_dto.setProdQuantity(eInvoiceDetails.getQuantity()); // số lượng
        if (Boolean.TRUE.equals(eInvoiceDetails.getIsPromotion())) { // Là hàng khuyến mại
            String prn = productSDS_dto.getProdName();
            prn += " (Hàng khuyến mại)";
            productSDS_dto.setProdName(prn);
        }
        productSDS_dto.setProdPrice(getValue(eInvoiceDetails.getUnitPriceOriginal()));
        if (currentCurrency_bool) {
//            productSDS_dto.setProdPrice(Utils.round(getValue(eInvoiceDetails.getUnitPriceOriginal()), 0));
            productSDS_dto.setTotal(Utils.round(getValue(eInvoiceDetails.getAmountOriginal()), 0));
        } else {
//            productSDS_dto.setProdPrice(getValue(eInvoiceDetails.getUnitPriceOriginal()));
            productSDS_dto.setTotal(getValue(eInvoiceDetails.getAmountOriginal()));
        }
        productSDS_dto.setVATRate(getVATRate(eInvoiceDetails.getvATRate(), nCCDV)); // Thuế suất
        if (productSDS_dto.getVATRate().equals(-1)) {
            productSDS_dto.setVATAmount(BigDecimal.ZERO);
        } else {
            if (currentCurrency_bool) {
                productSDS_dto.setVATAmount(Utils.round(getValue(productSDS_dto.getTotal()).multiply(toBD(productSDS_dto.getVATRate())).divide(toBD(100)), 0));
            } else {
                productSDS_dto.setVATAmount(Utils.round(getValue(productSDS_dto.getTotal()).multiply(toBD(productSDS_dto.getVATRate())).divide(toBD(100)), 2));
            }
        }
        productSDS_dto.setAmount(getValue(productSDS_dto.getTotal()).add(getValue(productSDS_dto.getVATAmount())));
        String extra = "";
        //region Số lô hạn dùng
        extra += getLotNoAndExpiryDate(eInvoiceDetails.getLotNo(), eInvoiceDetails.getExpiryDate());
        //endregion
        productSDS_dto.setExtra(extra);
        if (SIV.equals(nCCDV)) {
            productSDS_dto.setLotNo(eInvoiceDetails.getLotNo());
            productSDS_dto.setExpiryDate(eInvoiceDetails.getExpiryDate());
        }

        if (eInvoiceDetails.getAmountOriginal().compareTo(BigDecimal.ZERO) != 0) {
            ProductSDS_DTO productSDS_dto_CK = new ProductSDS_DTO();
            productSDS_dto_CK.setProdName("Chiết khấu");
            productSDS_dto_CK.setVATRate(getVATRate(eInvoiceDetails.getvATRate(), nCCDV));
            if (eInvoiceDetails.getDiscountRate() != null) {
                if (currentCurrency_bool) {
                    productSDS_dto_CK.setTotal(Utils.round(getValue(productSDS_dto.getTotal()).multiply(getValue(eInvoiceDetails.getDiscountRate())).multiply(toBD(-1)).divide(toBD(100)), 0));
                } else {
                    productSDS_dto_CK.setTotal(Utils.round(getValue(productSDS_dto.getTotal()).multiply(getValue(eInvoiceDetails.getDiscountRate())).multiply(toBD(-1)).divide(toBD(100)), 2));
                }
            } else {
                eInvoiceDetails.setDiscountRate(getValue(eInvoiceDetails.getDiscountAmountOriginal()).divide(getValue(eInvoiceDetails.getAmountOriginal()), 2, BigDecimal.ROUND_HALF_DOWN));
                if (currentCurrency_bool) {
                    productSDS_dto_CK.setTotal(Utils.round(getValue(productSDS_dto.getTotal()).multiply(getValue(eInvoiceDetails.getDiscountRate())).multiply(toBD(-1)), 0));
                } else {
                    productSDS_dto_CK.setTotal(Utils.round(getValue(productSDS_dto.getTotal()).multiply(getValue(eInvoiceDetails.getDiscountRate())).multiply(toBD(-1)), 2));
                }
            }
            if (productSDS_dto_CK.getVATRate().equals(-1)) {
                productSDS_dto_CK.setVATAmount(BigDecimal.ZERO);
            } else {
                if (currentCurrency_bool) {
                    productSDS_dto_CK.setVATAmount(Utils.round(getValue(productSDS_dto_CK.getTotal()).multiply(toBD(productSDS_dto_CK.getVATRate())).divide(toBD(100)), 0));
                } else {
                    productSDS_dto_CK.setVATAmount(Utils.round(getValue(productSDS_dto_CK.getTotal()).multiply(toBD(productSDS_dto_CK.getVATRate())).divide(toBD(100)), 2));
                }
            }
            productSDS_dto_CK.setAmount(getValue(productSDS_dto_CK.getTotal()).add(getValue(productSDS_dto_CK.getVATAmount())));
            BigDecimal sub = BigDecimal.ZERO;
            if (getValue(eInvoiceDetails.getDiscountAmountOriginal()).compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal vATAmount = BigDecimal.ZERO;
                if (currentCurrency_bool) {
                    vATAmount = Utils.round(getValue(productSDS_dto.getTotal()).add(getValue(productSDS_dto_CK.getTotal())).multiply(toBD(productSDS_dto.getVATRate())).divide(toBD(100)), 0);
                    sub = Utils.round(eInvoiceDetails.getvATAmountOriginal()).subtract(vATAmount);
                } else {
                    vATAmount = Utils.round(getValue(productSDS_dto.getTotal()).add(getValue(productSDS_dto_CK.getTotal())).multiply(toBD(productSDS_dto.getVATRate())).divide(toBD(100)), 2);
                    sub = Utils.round(eInvoiceDetails.getvATAmountOriginal(), 2).subtract(vATAmount);
                }
            } else {
                if (currentCurrency_bool) {
                    sub = Utils.round(getValue(eInvoiceDetails.getvATAmountOriginal())).subtract(getValue(productSDS_dto.getVATAmount()));
                } else {
                    sub = Utils.round(getValue(eInvoiceDetails.getvATAmountOriginal()), 2).subtract(getValue(productSDS_dto.getVATAmount()));
                }
            }
            if (sub.abs().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal vatAmontNew = productSDS_dto.getVATAmount().add(sub);
                productSDS_dto.setVATAmount(vatAmontNew);
            }
            if (currentCurrency_bool) {
                BigDecimal VATAmountCK = Utils.round(eInvoiceDetails.getvATAmountOriginal()).subtract(productSDS_dto.getVATAmount());
                productSDS_dto_CK.setVATAmount(VATAmountCK);
            } else {
                BigDecimal VATAmountCK = Utils.round(eInvoiceDetails.getvATAmountOriginal(), 2).subtract(productSDS_dto.getVATAmount());
                productSDS_dto_CK.setVATAmount(VATAmountCK);
            }
            productSDS_dto_CK.setAmount(productSDS_dto_CK.getTotal().add(productSDS_dto_CK.getVATAmount()));
            productSDS_dto.setAmount(productSDS_dto.getTotal().add(productSDS_dto.getVATAmount()));
            lstResult.add(productSDS_dto);
            if (productSDS_dto_CK.getTotal().compareTo(BigDecimal.ZERO) != 0) {
                lstResult.add(productSDS_dto_CK);
            }
        } else {
            lstResult.add(productSDS_dto);
        }
        return lstResult;
    }

    /*
     *•	-1: Không tính thuế
     * 0: Thuế suất 0%
     * 5: Thuế suất 5%
     * 10: Thuế suất 10%
     * */
    Integer getVATRate(BigDecimal vATRate, String nCCDV) {
        Integer result = -1;
        if (StringUtils.isEmpty(nCCDV)) {
            if (vATRate != null) {
                switch (vATRate.intValue()) {
                    case 0:
                        result = 0;
                        break;
                    case 1:
                        result = 5;
                        break;
                    case 2:
                        result = 10;
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            } else {
                result = -1;
            }
        } else {
            if (SIV.equals(nCCDV)) {
                if (vATRate != null) {
                    switch (vATRate.intValue()) {
                        case 0:
                            result = 0;
                            break;
                        case 1:
                            result = 5;
                            break;
                        case 2:
                            result = 10;
                            break;
                        case 3:
                            result = -2;
                            break;
                        case 4:
                            result = -1;
                            break;
                    }
                } else {
                    result = -1;
                }
            }
        }
        return result;
    }

    private BigDecimal getValue(BigDecimal bigDecimal) {
        return bigDecimal != null ? bigDecimal : BigDecimal.ZERO;
    }

    private String getLotNoAndExpiryDate(String lotNo, LocalDate expiryDate) {
        String result = "";
        if (!StringUtils.isEmpty(lotNo)) {
            result += "{ \"Lot\" : \"" + lotNo + "\", \"ExpireDate\":\"";
            if (expiryDate != null) {
                result += getDateString(expiryDate) + "\"}";
            }
        }
        return result;
    }

    String getDateString(LocalDate localDate) {
        String result = "";
        if (localDate != null) {
            result += localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return result;
    }

    String getDateString(LocalDate localDate, String pattern) {
        String result = "";
        if (localDate != null) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
            result += localDateTime.format(DateTimeFormatter.ofPattern(pattern));
        }
        return result;
    }

    private BigDecimal toBD(Long number) {
        return BigDecimal.valueOf(number);
    }

    private BigDecimal toBD(Integer number) {
        return BigDecimal.valueOf(number);
    }
}
