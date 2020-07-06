package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.PPDiscountReturn;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.repository.PPDiscountReturnRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnSearch2DTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnSearchDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

public class PPDiscountReturnRepositoryImpl implements PPDiscountReturnRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<PPDiscountReturnSearch2DTO> searchPPDiscountReturn(Pageable pageable, UUID accountingObjectID,
                                                                            String currencyID, String fromDate,
                                                                            String toDate, Boolean status, Boolean statusPurchase, String keySearch, UUID org, String currentBook) {
        Map<String, Object> params = new HashMap<>();
        List<PPDiscountReturnSearch2DTO> ppDiscountReturn = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sort = new StringBuilder();
        sort.append(" order by pp.Date desc, pp.PostedDate desc, ");
        if (currentBook.equals("0")) {
            sort.append(" pp.NoFBook desc ");
        } else if (currentBook.equals("1")) {
            sort.append(" pp.NoMBook desc ");
        }
        sqlBuilder.append(" FROM PPDiscountReturn pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and pp.TypeLedger in (:typeLedger, 2) ");
        params.put("typeLedger", currentBook);

        if (accountingObjectID != null){
            sqlBuilder.append(" and pp.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (statusPurchase != null){
            sqlBuilder.append(" and TypeID = :typeID ");
            params.put("typeID", !statusPurchase ? TypeConstant.MUA_HANG_TRA_LAI : TypeConstant.MUA_HANG_GIAM_GIA);
        }
        if (!Strings.isNullOrEmpty(currencyID)){
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (status != null){
            sqlBuilder.append(" and pp.Recorded = :recorded ");
            params.put("recorded", status);
        }

        if (fromDate != null || toDate != null){
            addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "pp.Date", "Date");
        }
        if(!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append(" and (pp.ContactName like :keySearch ");
            sqlBuilder.append(" or pp.Reason like  :keySearch ");
            sqlBuilder.append(" or pp.CompanyID like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectAddress like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectName like  :keySearch ");
            sqlBuilder.append(" or pp.ListCommonNameInventory like :keySearch ");
            sqlBuilder.append(" or pp.ListNo like :keySearch ");
            sqlBuilder.append(" or pp.EmployeeID like :keySearch ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK.toString())) {
                sqlBuilder.append(" or pp.NoFBook like :keySearch )");
            } else {
                sqlBuilder.append(" or pp.NoMBook like :keySearch )");
            }
            params.put("keySearch", "%" + keySearch + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        setParams(countQuery, params);
        Query totalAmountQuery = entityManager.createNativeQuery("SELECT  sum(ISNULL(pp.TotalAmount, 0) + ISNULL(pp.TotalVATAmount, 0)) " + sqlBuilder.toString());
        setParams(totalAmountQuery, params);
        Number totalAmount = (Number) totalAmountQuery.getSingleResult();
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * , " +
                "       case " +
                "           when pp.IsExportInvoice = 0 then (select " +
                "                                             top 1 " +
                "                                             sa.InvoiceNo " +
                "                                             from PPDiscountReturnDetail ppdtl " +
                "                                                      left join SABill sa on sa.ID = ppdtl.SABillID " +
                "                                             where ppdtl.PPDiscountReturnID = pp.id) " +
                "           else " +
                "               (select SUBSTRING((SELECT DISTINCT ', ' + sa.InvoiceNo " +
                "                                  from PPDiscountReturnDetail ppdtl " +
                "                                           left join SABill sa on sa.ID = ppdtl.SABillID " +
                "                                  where ppdtl.PPDiscountReturnID = pp.id " +
                "                                  FOR XML PATH ('')), 2, 9999)) end as sabillInvoiceNo" + sqlBuilder.toString() + sort.toString(), "PPDiscountReturnSearch2DTO");
                setParamsWithPageable(query, params, pageable, total);
            ppDiscountReturn = query.getResultList();
            ppDiscountReturn.get(0).setTotalSumAmount(Common.getBigDecimal(totalAmount));
        }
//        PPDiscountReturnSearchSumAmount2DTO result = new PPDiscountReturnSearchSumAmount2DTO();
//        result.setTotalAmount((BigDecimal) totalAmount);
//        result.setPpDiscountReturnSearch2DTO(ppDiscountReturn);
        return new PageImpl<>(ppDiscountReturn, pageable, total.longValue());
    }

    @Override
    public PPDiscountReturn findByRowNum(Pageable pageable, UUID accountingObjectID,
                                         String currencyID, String fromDate,
                                         String toDate, Boolean status, Boolean statusPurchase, String keySearch, Integer rowNum, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        PPDiscountReturn ppDiscountReturn = null;
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" FROM PPDiscountReturn pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        StringBuilder sort = new StringBuilder();
        sort.append(" order by pp.Date desc, pp.PostedDate desc, ");
        if (currentBook.equals("0")) {
            sort.append(" pp.NoFBook desc ");
        } else if (currentBook.equals("1")) {
            sort.append(" pp.NoMBook desc ");
        }
        if (statusPurchase != null){
            sqlBuilder.append(" and TypeID = :typeID ");
            params.put("typeID", !statusPurchase ? TypeConstant.MUA_HANG_TRA_LAI : TypeConstant.MUA_HANG_GIAM_GIA);
        }
        if (accountingObjectID != null){
            sqlBuilder.append(" and pp.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        sqlBuilder.append("  and (pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);
        if (!Strings.isNullOrEmpty(currencyID)){
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (status != null){
            sqlBuilder.append(" and pp.Recorded = :recorded ");
            params.put("recorded", status);
        }
        if (fromDate != null || toDate != null){
            addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "pp.Date", "Date");
        }
        if(!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append(" and (pp.ContactName like :keySearch ");
            sqlBuilder.append(" or pp.Reason like  :keySearch ");
            sqlBuilder.append(" or pp.CompanyID like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectAddress like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectName like  :keySearch ");
            sqlBuilder.append(" or pp.ListCommonNameInventory like :keySearch ");
            sqlBuilder.append(" or pp.ListNo like :keySearch ");
            sqlBuilder.append(" or pp.ListDate like :keySearch ");
            sqlBuilder.append(" or pp.EmployeeID like :keySearch ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK.toString())) {
                sqlBuilder.append(" or pp.NoFBook like :keySearch )");
            } else {
                sqlBuilder.append(" or pp.NoMBook like :keySearch )");
            }
            params.put("keySearch", "%" + keySearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (" + sort.toString() +") rownum "
                + sqlBuilder.toString() +  ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, PPDiscountReturn.class);
            setParams(query, params);
            ppDiscountReturn = (PPDiscountReturn) query.getSingleResult();
        }
        return ppDiscountReturn;
    }

    @Override
    public Optional<PPDiscountReturnDTO> findOneByID(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" from PPDiscountReturn where ID = :id ");
        params.put("id", id);
        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        PPDiscountReturnDTO ppDiscountReturn = null;
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sqlBuilder.toString(), "PPDiscountReturnDTO");
            Common.setParams(query, params);
            ppDiscountReturn = (PPDiscountReturnDTO) query.getSingleResult();
        }
        return Optional.of(ppDiscountReturn);
    }

    @Override
    public Page<PPDiscountReturnSearchDTO> searchPPDiscountReturnPDF(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch, UUID org, String currentBook) {
        Map<String, Object> params = new HashMap<>();
        List<PPDiscountReturnSearchDTO> ppDiscountReturn = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sort = new StringBuilder();
        sort.append(" order by pp.Date desc, pp.PostedDate desc, ");
        if (currentBook.equals("0")) {
            sort.append(" pp.NoFBook desc ");
        } else if (currentBook.equals("1")) {
            sort.append(" pp.NoMBook desc ");
        }
        sqlBuilder.append(" FROM PPDiscountReturn pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);

        if (accountingObjectID != null){
            sqlBuilder.append(" and pp.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (statusPurchase != null){
            sqlBuilder.append(" and TypeID = :typeID ");
            params.put("typeID", !statusPurchase ? TypeConstant.MUA_HANG_TRA_LAI : TypeConstant.MUA_HANG_GIAM_GIA);
        }
        if (!Strings.isNullOrEmpty(currencyID)){
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (status != null){
            sqlBuilder.append(" and pp.Recorded = :recorded ");
            params.put("recorded", status);
        }

        if (fromDate != null || toDate != null){
            addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "pp.Date", "Date");
        }
        if(!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append(" and (pp.ContactName like :keySearch ");
            sqlBuilder.append(" or pp.Reason like  :keySearch ");
            sqlBuilder.append(" or pp.CompanyID like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectAddress like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectName like  :keySearch ");
            sqlBuilder.append(" or pp.ListCommonNameInventory like :keySearch ");
            sqlBuilder.append(" or pp.ListNo like :keySearch ");
            sqlBuilder.append(" or pp.EmployeeID like :keySearch ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK.toString())) {
                sqlBuilder.append(" or pp.NoFBook like :keySearch )");
            } else {
                sqlBuilder.append(" or pp.NoMBook like :keySearch )");
            }
            params.put("keySearch", "%" + keySearch + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT id, " +
                "       date, " +
                "       postedDate, " +
                "       case when :typeLedger = 0 then pp.NoFBook else pp.NoMBook end as NoBook, " +
                "       accountingObjectName, " +
                "       accountingObjectAddress, " +
                "       reason, " +
                "       totalAmount, " +
                "       totalVATAmount, Recorded " + sqlBuilder.toString() + sort.toString(), "PPDiscountReturnSearchDTO");
                setParams(query, params);
            ppDiscountReturn = query.getResultList();
        }
        return new PageImpl<>(ppDiscountReturn, pageable, total.longValue());

    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update PPDiscountReturn SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<PPDiscountReturnDTO> getAllPPDiscountReturnHasRSID(UUID org, Integer currentBook) {
        Map<String, Object> params = new HashMap<>();
        List<PPDiscountReturnDTO> ppDiscountReturnDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT * ");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM PPDiscountReturn pp where pp.rsInwardOutwardID is not null and recorded = 1 and pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString() + sqlBuilder.toString(), "PPDiscountReturnDTO");
            Common.setParams(query, params);
            ppDiscountReturnDTOS = query.getResultList();
        }
        return ppDiscountReturnDTOS;
    }

    @Override
    public Page<SAInvoicePopupDTO> getAllPPDiscountReturnSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, UUID org, String currentBook, String currencyID, List<UUID> listPPDiscountReturnID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SAInvoicePopupDTO> result = new ArrayList<>();
        sql.append("FROM PPDiscountReturn a left join PPDiscountReturnDetail b on a.id = b.PPDiscountReturnID WHERE a.TypeID = 220 and ");
        if (listPPDiscountReturnID.size() > 0) {
            sql.append("a.id in :ids OR ");
            params.put("ids", listPPDiscountReturnID);
        }
        sql.append("(a.companyID = :org ");
        sql.append(" and SABillID is null and SABillDetailID is null and (a.IsBill = 0 or a.IsBill IS NULL) and (a.IsExportInvoice = 0 or a.IsExportInvoice IS NULL) and a.recorded = 1 ");
        params.put("org", org);

        if (accountingObjectID != null) {
            sql.append("AND a.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            sql.append("AND a.currencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        sql.append("and (a.typeledger = :soLamViec or a.typeledger = 2) ");
        params.put("soLamViec", currentBook);

        Common.addDateSearch(fromDate, toDate, params, sql, "posteddate");
        sql.append(" ) ");
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(distinct a.id) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT distinct a.id, a.id saInvoiceID, a.date, a.posteddate," +
                (currentBook.equalsIgnoreCase("0") ? " a.noFBook, '' noMBook, " : " '' noFBook, a.noMBook, ") +
                "a.reason, (a.totalAmount - a.TotalDiscountAmount + a.TotalVATAmount) total "
                + sql.toString() + " order by date desc, posteddate desc, " + (currentBook.equalsIgnoreCase("0") ? " a.noFBook desc " : " a.noMBook desc") , "SAInvoiceSaBillPopupDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            result = query.getResultList();
        }
        if (pageable == null) {
            return new PageImpl<>(result);
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    @Override
    public void updateSaBill(Set<SaBillDetails> saBillDetails, UUID id, String invoiceNo) {
        String sql = "UPDATE PPDiscountReturnDetail SET SaBillID = ?, saBillDeTailID = ? WHERE id = ?;" +
            "UPDATE PPDiscountReturn SET InvoiceNo = ? WHERE id = (SELECT PPDiscountReturnID FROM PPDiscountReturnDetail WHERE id = ?);";
        jdbcTemplate.batchUpdate(sql, saBillDetails, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(id).toString());
            ps.setString(2, Common.revertUUID(detail.getId()).toString());
            ps.setString(3, detail.getPpDiscountReturnDetailID() != null ? Common.revertUUID(detail.getPpDiscountReturnDetailID()).toString() : null);
            ps.setString(4, invoiceNo);
            ps.setString(5, detail.getPpDiscountReturnDetailID() != null ? Common.revertUUID(detail.getPpDiscountReturnDetailID()).toString() : null);
        });
    }

    @Override
    public List<SAInvoiceDetailPopupDTO> getPPDiscountDetailByPPDiscountID(List<UUID> ids, String data) {
        StringBuilder sql = new StringBuilder("select a.ID, " +
            "       PPDiscountReturnID  SAInvoiceID, " +
            "       MaterialGoodsID, " +
            "       IsPromotion, " +
            "       RepositoryID, " +
            "       Description, " +
            "       DebitAccount, " +
            "       CreditAccount, " +
            "       UnitID, " +
            "       quantity, " +
            "       UnitPrice, " +
            "       UnitPriceOriginal, " +
            "       MainUnitID, " +
            "       MainQuantity, " +
            "       MainUnitPrice, " +
            "       MainConvertRate, " +
            "       Formula, " +
            "       Amount, " +
            "       AmountOriginal, " +
            "       0 as DiscountRate, " +
            "       0 as DiscountAmount, " +
            "       0 as DiscountAmountOriginal, " +
            "       '' as DiscountAccount, " +
            "       VATRate, " +
            "       VATAmount, " +
            "       VATAmountOriginal, " +
            "       VATAccount, " +
            "       '' as RepositoryAccount, " +
            "       '' as CostAccount, " +
            "       0 as OWPrice, " +
            "       0 as OWAmount, " +
            "       ExpiryDate, " +
            "       LotNo, " +
            "       a.AccountingObjectID, " +
            "       DepartmentID, " +
            "       ExpenseItemID, " +
            "       BudgetItemID, " +
            "       CostSetID, " +
            "       ContractID, " +
            "       StatisticsCodeID, " +
            "       VATDescription, " +
            "       DeductionDebitAccount, " +
            "       b.date, ");
        if ("0".equalsIgnoreCase(data)) {
            sql.append("       b.noFBook, ");
            sql.append("       '' noMBook, ");
        } else {
            sql.append("       '' noFBook, ");
            sql.append("       b.noMBook, ");
        }

        sql.append("       b.accountingObjectID accountingObjectID2, b.Reason, b.typeID " +
            "from PPDiscountReturnDetail a left join PPDiscountReturn b on a.PPDiscountReturnID = b.id where b.id in (:ids) order by a.OrderPriority");
        Query query = entityManager.createNativeQuery(sql.toString(), "SAInvoiceDetailPopupDTO");
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public PPDiscountReturnDetails findDetaiBySaBillID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        PPDiscountReturnDetails saInvoiceDetails = null;
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append(" from PPDiscountReturnDetail where  SABillDetailID = :id");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( "SELECT Top(1) * " + sqlSelect.toString(), PPDiscountReturnDetails.class);
            Common.setParams(query, params);
            saInvoiceDetails = (PPDiscountReturnDetails)query.getSingleResult();
        }
        return saInvoiceDetails;
    }

    public static String addSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null)
            return "";
        for (Sort.Order order : sort) {

            orderSql.append("ORDER BY ");
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            break;
        }
        return orderSql.toString();
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }
    public void addDateSearchCustom(String fromDate, String toDate, Map<String, Object> params,
                                           StringBuilder sqlBuilder, String columnName, String param) {
        if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append("AND :from" + param + "<= convert(varchar," + columnName + "," + 112 +") AND :to" + param +
                " >= convert(varchar," + columnName + "," + 112 +") ");
            params.put("from" + param, fromDate);
            params.put("to" + param, toDate);
        } else if (!Strings.isNullOrEmpty(fromDate)) {
            sqlBuilder.append("AND :from" + param +" <= convert(varchar," + columnName + "," + 112 +") ");
            params.put("from" + param, fromDate);
        } else if (!Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append("AND :to" + param + " >= convert(varchar," + columnName + "," + 112 +") ");
            params.put("to" + param, toDate);
        }
    }
}
