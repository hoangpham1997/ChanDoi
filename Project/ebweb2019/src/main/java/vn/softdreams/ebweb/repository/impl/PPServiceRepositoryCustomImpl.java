package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.PPServiceRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class PPServiceRepositoryCustomImpl implements PPServiceRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<PPServiceDTO> findAllPPService(Pageable pageable, Integer receiptType, String toDate, String fromDate,
                                               Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                               String freeSearch, UUID org) {
        List<PPServiceDTO> ppServiceDTOS = new ArrayList<>();
        Map<String, Object> params = initParamsNativeQueryForPPServiceDTO(noBookType);
        String selectSql = generateNativeSelectQueryForPPServiceDTO();
        String fromSql = generateNativeFromQueryForPPServiceDTO();
        if (org != null) {
            params.put("org", org);
        }
        if (receiptType != null) {
            fromSql += "and t.id = :receiptType ";
            params.put("receiptType", receiptType);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            fromSql += "and CONVERT(varchar, pp.Date, 112) <= :toDate ";
            params.put("toDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(toDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            fromSql += "and CONVERT(varchar, pp.Date, 112) >= :fromDate ";
            params.put("fromDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(fromDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (hasRecord != null) {
            fromSql += "and pp.Recorded = :hasRecord ";
            params.put("hasRecord", hasRecord);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            fromSql += "and pp.CurrencyID = :currencyID ";
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            fromSql += "and pp.AccountingObjectID = :accountingObjectID ";
            params.put("accountingObjectID", accountingObjectID);
        }

        if (!Strings.isNullOrEmpty(freeSearch)) {
            fromSql += "and (";
            if (noBookType == 0) {
                fromSql += " pp.NoFBook like :freeSearch ";
            } else {
                fromSql += " pp.NoMBook like :freeSearch ";
            }
            fromSql += " or pp.AccountingObjectAddress like :freeSearch " +
                    "or CompanyTaxCode like :freeSearch " +
                    "or ao.AccountingObjectCode like :freeSearch " +
                    "or ao.AccountingObjectName like :freeSearch " +
                    "or pp.Reason like :freeSearch)";
            params.put("freeSearch", "%" + freeSearch + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + fromSql);
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            fromSql += " ORDER BY pp.date Desc, pp.PostedDate Desc, " + (noBookType == 0 ? "pp.NoFBook" : "pp.NoMBook") + " Desc";
            Query query = entityManager.createNativeQuery("SELECT " + selectSql + fromSql, "PPServiceDTO");
            params.put("noBookType", noBookType);
            if (pageable != null) {
                Utils.setParamsWithPageable(query, params, pageable, total);
            }  else {
                setParams(query, params);
                pageable = Pageable.unpaged();
            }

            ppServiceDTOS = query.getResultList();
        }
        return new PageImpl<>(ppServiceDTOS, pageable, total.longValue());
    }

    @Override
    public Page<ReceiveBillSearchDTO> findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher, Boolean isNoMBook, UUID orgID) {
        StringBuilder sql = new StringBuilder();
        StringBuilder selectSql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ReceiveBillSearchDTO> listReceiveBillSearchDTO = new ArrayList<>();
        selectSql.append(" pp.id, ppd.id as pPID, pp.TypeID, pp.Date, pp.PostedDate, pp.NoFBook, pp.NoMBook, ppd.description, ppd.Amount, ppd.VATRate, ppd.VATAccount, ppd.InvoiceTemplate, ppd.InvoiceSeries, ppd.InvoiceNo, ppd.InvoiceDate, ppd.GoodsServicePurchaseID, ppd.AccountingObjectID ");
        sql.append("FROM PPService pp INNER JOIN PPServiceDetail ppd on pp.ID = ppd.PPServiceID WHERE pp.BillReceived = 0 AND pp.CompanyID = :orgID ");
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }

        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND pp.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        params.put("orgID", orgID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery("SELECT "+ selectSql.toString() + sql.toString() + " ORDER BY Date DESC, PostedDate DESC, NoMBook DESC ", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery("SELECT "+ selectSql.toString() + sql.toString() + " ORDER BY Date DESC, PostedDate DESC, NoMBook DESC ", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            }
        }
        return new PageImpl<>(listReceiveBillSearchDTO, pageable, total.longValue());
    }

    @Override
    public void updateNhanHoaDon(List<UUID> listIDPPInvoice) {
        String sql = "UPDATE PPService SET BillReceived = 1 WHERE id = ?;";
        jdbcTemplate.batchUpdate(sql, listIDPPInvoice, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public UpdateDataDTO findPPServiceDTOByLocationItem(Integer nextItem, Integer currentBook, UUID ppServiceId, UUID org, Integer receiptType, String toDate, String fromDate,
                                                        Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                                        String freeSearch) {
        if (nextItem == null) {
            nextItem = 0;
        }
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        Map<String, Object> params = new HashMap<>();
        String noBook = "NoFBook";
        if (currentBook == 0) {
            params.put("currentBook", Arrays.asList(Constants.TypeLedger.FINANCIAL_BOOK, Constants.TypeLedger.BOTH_BOOK));
        } else {
            noBook = "NoMBook";
            params.put("currentBook", Arrays.asList(Constants.TypeLedger.MANAGEMENT_BOOK, Constants.TypeLedger.BOTH_BOOK));
        }
        params.put("cash", Constants.PPServiceType.PPSERVICE_CASH);
        params.put("paymentOrder", Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER);
        params.put("transferSec", Constants.PPServiceType.PPSERVICE_TRANSFER_SEC);
        params.put("cashSec", Constants.PPServiceType.PPSERVICE_CASH_SEC);
        params.put("creditCard", Constants.PPServiceType.PPSERVICE_CREDIT_CARD);
        String whereSql = " Where pp.TypeLedger in :currentBook and pp.companyID = :org ";
        if (receiptType != null) {
            whereSql += "and pp.TypeID = :receiptType ";
            params.put("receiptType", receiptType);
        }

        if (!Strings.isNullOrEmpty(toDate)) {
            whereSql += "and CONVERT(varchar, pp.Date, 112) <= :toDate ";
            params.put("toDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(toDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            whereSql += "and CONVERT(varchar, pp.Date, 112) >= :fromDate ";
            params.put("fromDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(fromDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (hasRecord != null) {
            whereSql += "and pp.Recorded = :hasRecord ";
            params.put("hasRecord", hasRecord);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            whereSql += "and pp.CurrencyID = :currencyID ";
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            whereSql += "and pp.AccountingObjectID = :accountingObjectID ";
            params.put("accountingObjectID", accountingObjectID);
        }

        if (!Strings.isNullOrEmpty(freeSearch)) {
            whereSql += "and (";
            if (noBookType == 0) {
                whereSql += " pp.NoFBook like :freeSearch ";
            } else {
                whereSql += " pp.NoMBook like :freeSearch ";
            }
            whereSql += " or pp.AccountingObjectAddress like :freeSearch " +
                    "or CompanyTaxCode like :freeSearch " +
                    "or ao.AccountingObjectCode like :freeSearch " +
                    "or ao.AccountingObjectName like :freeSearch " +
                    "or pp.Reason like :freeSearch)";
            params.put("freeSearch", "%" + freeSearch + "%");
        }
        String joinSQL = "left join Type t on pp.TypeID = t.ID " +
                "left join AccountingObject ao on ao.IsEmployee = 1 and ao.IsActive = 1 and ao.ID =  pp.EmployeeID " +
                "left join MCPayment mcp on pp.PaymentVoucherID = mcp.ID and pp.TypeID = :cash " +
                "left join MBTellerPaper mbt on pp.PaymentVoucherID = mbt.ID and pp.TypeID in (:paymentOrder, :transferSec, :cashSec) " +
                "left join MBCreditCard mbc on pp.PaymentVoucherID = mbc.ID and pp.TypeID = :creditCard ";
        String sql = "FROM (SELECT pp.id, ROW_NUMBER() over (ORDER BY pp.date Desc , pp.PostedDate Desc, pp." + noBook + " Desc) " +
                "rowNum FROM PPService pp " + joinSQL + whereSql + " ) a " +
                "where rowNum = (select rowNum from (SELECT pp.id, ROW_NUMBER() over (ORDER BY pp.Date Desc, pp.PostedDate Desc, pp." + noBook + " Desc) rowNum " +
                "FROM PPService pp " + joinSQL + whereSql + " ) a where id = :id) + " + nextItem + " ";

        params.put("id", ppServiceId);
        params.put("org", org);
        Query query = entityManager.createNativeQuery("SELECT id as uuid, rowNum, (select max(rowNum) from (SELECT ROW_NUMBER() over (ORDER BY pp.date Desc, pp.PostedDate Desc, pp." + noBook + " Desc) rowNum " +
                "FROM PPService pp " + joinSQL +
                whereSql + ") a) as totalRows " + sql , "UpdateDataDTO");
        setParams(query, params);
        try {
            updateDataDTO = (UpdateDataDTO) query.getSingleResult();
        } catch (Exception e) {
            updateDataDTO.setMessages(nextItem <= 0 ? Constants.UpdateDataDTOMessages.IS_FIST_ITEM : Constants.UpdateDataDTOMessages.IS_LAST_ITEM);
        }
        return updateDataDTO;
    }

    @Override
    public PPServiceDTO findOneById(UUID ppServiceId, Integer currentBook, UUID org) {
        Map<String, Object> params = initParamsNativeQueryForPPServiceDTO(currentBook);
        String selectSql = generateNativeSelectQueryForPPServiceDTO();
        String fromSql = generateNativeFromQueryForPPServiceDTO();
        fromSql += "and pp.id = :id ";
        params.put("id", ppServiceId);
        params.put("org", org);
        Query query = entityManager.createNativeQuery("SELECT " + selectSql + fromSql, "PPServiceDTO");
        params.put("noBookType", currentBook);
        setParams(query, params);
        return (PPServiceDTO) query.getSingleResult();
    }


    private Map<String, Object> initParamsNativeQueryForPPServiceDTO(Integer currentBook) {
        Map<String, Object> params = new HashMap<>();
        if (currentBook == null) {
            throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.CANNOT_FIND_CURRENT_BOOK,
                    this.getClass().getName(), Constants.UpdateDataDTOMessages.CANNOT_FIND_CURRENT_BOOK);
        }

        params.put("cash", Constants.PPServiceType.PPSERVICE_CASH);
        params.put("paymentOrder", Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER);
        params.put("transferSec", Constants.PPServiceType.PPSERVICE_TRANSFER_SEC);
        params.put("cashSec", Constants.PPServiceType.PPSERVICE_CASH_SEC);
        params.put("creditCard", Constants.PPServiceType.PPSERVICE_CREDIT_CARD);
        if (currentBook == 0) {
            params.put("currentBook", Arrays.asList(Constants.TypeLedger.FINANCIAL_BOOK, Constants.TypeLedger.BOTH_BOOK));
        } else {
            params.put("currentBook", Arrays.asList(Constants.TypeLedger.MANAGEMENT_BOOK, Constants.TypeLedger.BOTH_BOOK));
        }
        return params;
    }

    /**
     * need: Map<String, Object> params = initParamsNativeQueryForPPServiceDTO(currentBook);
     * @return
     */
    private String generateNativeSelectQueryForPPServiceDTO() {
        return new StringBuilder()
                .append(" pp.id as id, convert(varchar, pp.Date, 103) as receiptDate,")
                .append("convert(varchar, pp.PostedDate, 103) as postedDate, ")
                .append("case when :noBookType = 0 then pp.NoFBook else pp.NoMBook end as noBook, ")
                .append("case when pp.TypeID = :cash then case when :noBookType = 0 then mcp.NoFBook else mcp.NoMBook end ")
                .append("     when pp.TypeID = :creditCard then case when :noBookType = 0 then mbc.NoFBook else mbc.NoMBook end ")
                .append("     else case when :noBookType = 0 then mbt.NoFBook else mbt.NoMBook end end as otherNoBook, ")
                .append("t.TypeName as receiptType, ")
                .append("pp.AccountingObjectName as accountingObjectName, ")
                .append("pp.AccountingObjectID as accountingObjectID, ")
                .append("pp.AccountingObjectAddress as accountingObjectAddress, ")
                .append("pp.CompanyTaxCode as companyTaxCode, ")
                .append("pp.AccountingObjectContactName as contactName, ")
                .append("pp.TypeID as typeId, ")
                .append("pp.Reason as reason, ")
                .append("pp.IsFeightService as purchaseCosts, ")
                .append("pp.Recorded as recorded, ")
                .append("pp.NumberAttach as numberAttach, ")
                .append("pp.EmployeeID as employeeID, ")
                .append("ao.AccountingObjectName as employeeName, ")
                .append("pp.TotalAmount as totalAmount, ")
                .append("pp.TotalAmountOriginal as totalAmountOriginal, ")
                .append("pp.TotalDiscountAmount as totalDiscountAmount, ")
                .append("pp.TotalDiscountAmountOriginal as totalDiscountAmountOriginal, ")
                .append("pp.TotalVATAmount as totalVATAmount, ")
                .append("pp.TotalVATAmountOriginal as totalVATAmountOriginal, ")
                .append("(pp.TotalAmount - pp.TotalDiscountAmount + pp.TotalVATAmount) as resultAmount, ")
                .append("(pp.TotalAmountOriginal - pp.TotalDiscountAmountOriginal + pp.TotalVATAmountOriginal) as resultAmountOriginal, ")
                .append("pp.BillReceived as billReceived,")
                .append("convert(varchar, pp.DueDate, 103) as dueDate, ")
                .append("pp.TypeLedger as typeLedger, ")
                .append("pp.NoFBook as noFBook, ")
                .append("pp.NoMBook as noMBook, ")
                .append("pp.CurrencyID  as currencyID, ")
                .append("pp.ExchangeRate as exchangeRate, ")
                .append("mcp.Receiver as receiverUserName, ")
                .append("mcp.NumberAttach as otherNumberAttach, ")
                .append("case when pp.TypeID = :cash  then mcp.Reason ")
                .append("     when pp.TypeID = :creditCard then mbc.Reason ")
                .append("     else mbt.Reason end as otherReason, ")
                .append("mbt.BankAccountDetailID as accountPaymentId, ")
                .append("mbt.BankName as accountPaymentName, ")
                .append("case when pp.TypeID = :creditCard  then mbc.AccountingObjectBankAccountDetailID")
                .append("     else mbt.AccountingObjectBankAccount end as accountReceiverId, ")
                .append("case when pp.TypeID = :creditCard  then mbc.AccountingObjectBankName")
                .append("     else mbt.AccountingObjectBankName end as accountReceiverName, ")
                .append("mbc.CreditCardNumber as creditCardNumber, ")
                .append("pp.PaymentVoucherID as paymentVoucherID, ")
                .append("mbt.Receiver as receiver, ")
                .append("mbt.IdentificationNo as identificationNo, ")
                .append("convert(varchar, mbt.IssueDate, 103) as issueDate, ")
                .append("mbt.IssueBy as issueBy ")
                .toString();
    }

    private String generateNativeFromQueryForPPServiceDTO() {
        return new StringBuilder().append("from PPService pp ")
                .append("left join Type t on pp.TypeID = t.ID ")
                .append("left join AccountingObject ao on ao.IsEmployee = 1 and ao.IsActive = 1 and ao.ID =  pp.EmployeeID ")
                .append("left join MCPayment mcp on pp.PaymentVoucherID = mcp.ID and pp.TypeID = :cash ")
                .append("left join MBTellerPaper mbt on pp.PaymentVoucherID = mbt.ID and pp.TypeID in (:paymentOrder, :transferSec, :cashSec) ")
                .append("left join MBCreditCard mbc on pp.PaymentVoucherID = mbc.ID and pp.TypeID = :creditCard ")
                .append("where pp.TypeLedger in :currentBook and pp.companyID = :org ")
                .toString();
    }

    @Override
    public Page<PPServiceCostVoucherDTO> findCostVouchers(Pageable pageable, UUID accountingObject, String fromDate, String toDate, boolean isNoMBook, UUID org, UUID ppInvoiceId, Boolean isHaiQuan) {
        List<PPServiceCostVoucherDTO> costVoucherDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("pps.id, ");
        sql.append("pps.noFBook, ");
        sql.append("pps.noMBook, ");
        sql.append("pps.postedDate, ");
        sql.append("pps.date, ");
        sql.append("pps.accountingObjectID, ");
        sql.append("pps.accountingObjectName, ");
        sql.append("pps.reason, ");
        sql.append("pps.totalAmount, ");
        sql.append("pps.totalAmountOriginal, ");
        sql.append("pps.exchangeRate, ");
        sql.append("pps.totalDiscountAmount, ");
        sql.append("pps.totalDiscountAmountOriginal, ");
        sql.append("pps.totalVATAmount, ");
        sql.append("pps.totalVATAmountOriginal, ");
        sql.append("(SELECT sum(amount) FROM PPInvoiceDetailCost WHERE 1 = 1  ");
        sql.append("AND ppServiceId = pps.id ");
        if (ppInvoiceId != null) {
            sql.append("AND refId != :ppInvoiceId ");
            if (isHaiQuan) {
                sql.append("AND CostType = 1) as sumAmount, ");
            } else {
                sql.append("AND CostType = 0) as sumAmount, ");
            }
        } else {
            sql.append(" ) as sumAmount ");
        }

        StringBuilder sqlFrom = new StringBuilder();
        sqlFrom.append("from PPSERVICE pps ");
        sqlFrom.append(" where pps.CompanyID = :companyID ");
        params.put("companyID", org);

        sqlFrom.append("and pps.isFeightService = 1 ");

        if (isNoMBook) {
            sqlFrom.append(" and pps.TypeLedger in (1, 2) ");
        } else {
            sqlFrom.append(" and pps.TypeLedger in (0, 2) ");
        }

        if (accountingObject != null) {
            sqlFrom.append("and pps.AccountingObjectID = :accountingObject ");
            params.put("accountingObject", accountingObject);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sqlFrom.append("and CONVERT(varchar, pps.Date, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sqlFrom.append("and CONVERT(varchar, pps.Date, 112) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuery = entityManager.createNativeQuery("SELECT Count(1) " + sqlFrom.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            if (ppInvoiceId != null) {
                params.put("ppInvoiceId", ppInvoiceId);
            }
            sqlFrom.append(" ORDER BY PostedDate DESC ");
            Query query = entityManager.createNativeQuery(sql.toString() + sqlFrom.toString(), "PPServiceCostVoucherDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            costVoucherDTOS = query.getResultList();
        }
        return new PageImpl<>(costVoucherDTOS, pageable, total.longValue());
    }

    @Override
    public BigDecimal countTotalResultAmountOriginal(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch, UUID org) {
        Map<String, Object> params = initParamsNativeQueryForPPServiceDTO(noBookType);
        String fromSql = generateNativeFromQueryForPPServiceDTO();
        if (org != null) {
            params.put("org", org);
        }
        if (receiptType != null) {
            fromSql += "and t.id = :receiptType ";
            params.put("receiptType", receiptType);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            fromSql += "and CONVERT(varchar, pp.Date, 112) <= :toDate ";
            params.put("toDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(toDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            fromSql += "and CONVERT(varchar, pp.Date, 112) >= :fromDate ";
            params.put("fromDate", DateUtil.getStrByLocalDate(DateUtil.getLocalDateFromString(fromDate, DateUtil.C_DD_MM_YYYY), DateUtil.C_MMDDYYYY));
        }
        if (hasRecord != null) {
            fromSql += "and pp.Recorded = :hasRecord ";
            params.put("hasRecord", hasRecord);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            fromSql += "and pp.CurrencyID = :currencyID ";
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            fromSql += "and pp.AccountingObjectID = :accountingObjectID ";
            params.put("accountingObjectID", accountingObjectID);
        }

        if (!Strings.isNullOrEmpty(freeSearch)) {
            fromSql += "and (";
            if (noBookType == Constants.TypeLedger.FINANCIAL_BOOK) {
                fromSql += " pp.NoFBook like :freeSearch ";
            } else {
                fromSql += " pp.NoMBook like :freeSearch ";
            }
            fromSql += " or pp.AccountingObjectAddress like :freeSearch " +
                    "or CompanyTaxCode like :freeSearch " +
                    "or ao.AccountingObjectCode like :freeSearch " +
                    "or ao.AccountingObjectName like :freeSearch " +
                    "or pp.Reason like :freeSearch)";
            params.put("freeSearch", "%" + freeSearch + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT sum (pp.TotalAmountOriginal - pp.TotalDiscountAmountOriginal + pp.TotalVATAmountOriginal) " + fromSql);
        setParams(countQuerry, params);
        return  (BigDecimal) countQuerry.getSingleResult();
    }

    @Override
    public List<UUID> findAllIDByPaymentVoucherID(List<UUID> uuidsPCMuaDichVu) {
        String sql = "select ID from PPService where PaymentVoucherID IN :uuidsPCMuaDichVu";
        Map<String, Object> params = new HashMap<>();
        params.put("uuidsPCMuaDichVu", uuidsPCMuaDichVu);
        Query query = entityManager.createNativeQuery(sql, "UUIDDTO");
        setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }

    @Override
    public List<UUID> findAllPaymentVoucherIDByID(List<UUID> uuidsPCMuaDichVu) {
        String sql = "select PaymentVoucherID as uuid from PPService where ID IN :uuidsPCMuaDichVu and PaymentVoucherID is not null";
        Map<String, Object> params = new HashMap<>();
        params.put("uuidsPCMuaDichVu", uuidsPCMuaDichVu);
        Query query = entityManager.createNativeQuery(sql, "UUIDDTO");
        setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.isEmpty() ? new ArrayList<>() : updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }
}
