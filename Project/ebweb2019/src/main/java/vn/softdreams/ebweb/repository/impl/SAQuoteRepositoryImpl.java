package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.SAQuote;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.SAQuoteReportDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.repository.SAQuoteRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class SAQuoteRepositoryImpl implements SAQuoteRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<SAQuote> findAll(Pageable pageable, SearchVoucher searchVoucher, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<SAQuote> lstSAQ = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuote WHERE 1 = 1 ");
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        if (searchVoucher.getEmployeeID() != null) {
            sql.append("AND employeeID = :employeeID ");
            params.put("employeeID", searchVoucher.getEmployeeID());
        }
        if (searchVoucher.getCurrencyID() != null) {
            sql.append("AND currencyID = :currencyID ");
            params.put("currencyID", searchVoucher.getCurrencyID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
            sql.append("AND (contactName LIKE :searchValue ");
            sql.append("OR accountingObjectAddress LIKE :searchValue ");
            sql.append("OR companyTaxCode LIKE :searchValue ");
            sql.append("OR reason LIKE :searchValue ");
            sql.append("OR description LIKE :searchValue ");
            sql.append("OR contactEmail LIKE :searchValue ");
            sql.append("OR contactMobile LIKE :searchValue ");
            sql.append("OR no LIKE :searchValue ");
            sql.append("OR guaranteeDuration LIKE :searchValue) ");
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by Date DESC, No DESC ,FinalDate DESC ", SAQuote.class);
            setParamsWithPageable(query, params, pageable, total);
            lstSAQ = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount - totalDiscountAmount + totalVATAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lstSAQ.get(0).setTotalMoney(totalamount);
        }
        return new PageImpl<>(lstSAQ, pageable, total.longValue());
    }

    @Override
    public Page<ViewSAQuoteDTO> findAllView(Pageable pageable, UUID companyID, UUID accountingObjectID, String fromDate, String toDate, String currencyID) {
        StringBuilder sql = new StringBuilder();
        List<ViewSAQuoteDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from SAQuoteDetail a " +
            "         left join " +
            "     MaterialGoods ON MaterialGoods.ID = a.MaterialGoodsID " +
            "         right join SAQuote b on b.ID = a.SAQuoteID " +
            "where b.CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (currencyID != null) {
            sql.append("  and b.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            sql.append("and AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            sql.append("and Date <= :toDate ");
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select a.SAQuoteID as ID, " +
                "       a.ID        as sAQuoteDetailID, " +
                "       TypeID, " +
                "       b.CompanyID, " +
                "       b.No, " +
                "       b.Date, " +
                "       a.MaterialGoodsID, " +
                "       MaterialGoodsCode, " +
                "       a.Description, " +
                "       a.Quantity, " +
                "       a.UnitPrice, " +
                "       a.Amount, " +
                "       a.unitID, " +
                "       a.unitPriceOriginal, " +
                "       a.mainQuantity, " +
                "       a.mainUnitID, " +
                "       a.mainUnitPrice, " +
                "       a.mainConvertRate, " +
                "       a.formula, " +
                "       a.vATDescription, " +
                "       a.amountOriginal, " +
                "       a.discountRate, " +
                "       a.discountAmount, " +
                "       a.discountAmountOriginal, " +
                "       a.vATRate, " +
                "       a.vATAmount, " +
                "       a.vATAmountOriginal, " +
                "       b.AccountingObjectID, " +
                "       b.AccountingObjectName, " +
                "       b.AccountingObjectAddress, " +
                "       b.CompanyTaxCode, " +
                "       b.ContactName, " +
                "       b.Reason as DescriptionParent, " +
                "       b.EmployeeID, " +
                "       b.DeliveryTime " +
                sql.toString() + " order by Date DESC ,No DESC", "ViewSAQuoteDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<ViewSAQuoteDTO>) lst), pageable, total.longValue());
    }

    @Override
    public Page<SAQuoteDTO> findAllData(Pageable pageable, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<SAQuoteDTO> lstSAQDto = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuote WHERE 1 = 1 ");
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by Date DESC, No DESC ,FinalDate DESC  ", "SAQuoteDTO");
            setParamsWithPageable(query, params, pageable, total);
            lstSAQDto = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount - totalDiscountAmount + totalVATAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lstSAQDto.get(0).setTotalMoney(totalamount);
        }
        return new PageImpl<>(lstSAQDto, pageable, total.longValue());
    }

    @Override
    public SAQuote findOneByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        SAQuote sAQ = new SAQuote();
        // List<SAQuote> lstSAQ = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuote WHERE 1 = 1 ");
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucher != null) {
            if (searchVoucher.getAccountingObjectID() != null) {
                sql.append("AND accountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
            }
            if (searchVoucher.getEmployeeID() != null) {
                sql.append("AND employeeID = :employeeID ");
                params.put("employeeID", searchVoucher.getEmployeeID());
            }
            if (searchVoucher.getCurrencyID() != null) {
                sql.append("AND currencyID = :currencyID ");
                params.put("currencyID", searchVoucher.getCurrencyID());
            }
            if (searchVoucher.getFromDate() != null) {
                sql.append("AND date >= :fromDate ");
                params.put("fromDate", searchVoucher.getFromDate());
            }
            if (searchVoucher.getToDate() != null) {
                sql.append("AND date <= :toDate ");
                params.put("toDate", searchVoucher.getToDate());
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND (contactName LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR companyTaxCode LIKE :searchValue ");
                sql.append("OR reason LIKE :searchValue ");
                sql.append("OR description LIKE :searchValue ");
                sql.append("OR contactEmail LIKE :searchValue ");
                sql.append("OR contactMobile LIKE :searchValue ");
                sql.append("OR no LIKE :searchValue ");
                sql.append("OR guaranteeDuration LIKE :searchValue) ");
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            // String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + addSort(sort.getSort()) + ") row_num " + sql.toString() + ") where rownum = :rowNum";
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC, No DESC ,FinalDate DESC ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, SAQuote.class);
            setParams(query, params);
            Object obj = query.getSingleResult();
            sAQ = SAQuote.class.cast(obj);
        }
        return sAQ;
    }

    @Override
    public List<Number> getIndexRow(UUID iD, SearchVoucher searchVoucher, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<Number> lstIndex = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuote WHERE 1 = 1 ");
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucher != null) {
            if (searchVoucher.getAccountingObjectID() != null) {
                sql.append("AND accountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
            }
            if (searchVoucher.getEmployeeID() != null) {
                sql.append("AND employeeID = :employeeID ");
                params.put("employeeID", searchVoucher.getEmployeeID());
            }
            if (searchVoucher.getCurrencyID() != null) {
                sql.append("AND currencyID = :currencyID ");
                params.put("currencyID", searchVoucher.getCurrencyID());
            }
            if (searchVoucher.getFromDate() != null) {
                sql.append("AND date >= :fromDate ");
                params.put("fromDate", searchVoucher.getFromDate());
            }
            if (searchVoucher.getToDate() != null) {
                sql.append("AND date <= :toDate ");
                params.put("toDate", searchVoucher.getToDate());
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND (contactName LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR companyTaxCode LIKE :searchValue ");
                sql.append("OR reason LIKE :searchValue ");
                sql.append("OR description LIKE :searchValue ");
                sql.append("OR contactEmail LIKE :searchValue ");
                sql.append("OR contactMobile LIKE :searchValue ");
                sql.append("OR no LIKE :searchValue ");
                sql.append("OR guaranteeDuration LIKE :searchValue) ");
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        Number indexRow = 0;
        if (total.longValue() > 0) {
            String newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC, No DESC ,FinalDate DESC ) rowNum " + sql.toString() + ") a where a.ID = :iD";
            params.put("iD", iD);
            Query query = entityManager.createNativeQuery(newSql);
            setParams(query, params);
            indexRow = (Number) query.getSingleResult();
        }
        lstIndex.add(indexRow);
        lstIndex.add(total);
        return lstIndex;
    }

    @Override
    public List<SAQuote> findAllForExport(SearchVoucher searchVoucher, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<SAQuote> lstSAQ = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuote WHERE 1 = 1 ");
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", org);
        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        if (searchVoucher.getEmployeeID() != null) {
            sql.append("AND employeeID = :employeeID ");
            params.put("employeeID", searchVoucher.getEmployeeID());
        }
        if (searchVoucher.getCurrencyID() != null) {
            sql.append("AND currencyID = :currencyID ");
            params.put("currencyID", searchVoucher.getCurrencyID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
            sql.append("AND (contactName LIKE :searchValue ");
            sql.append("OR accountingObjectAddress LIKE :searchValue ");
            sql.append("OR companyTaxCode LIKE :searchValue ");
            sql.append("OR reason LIKE :searchValue ");
            sql.append("OR description LIKE :searchValue ");
            sql.append("OR contactEmail LIKE :searchValue ");
            sql.append("OR contactMobile LIKE :searchValue ");
            sql.append("OR no LIKE :searchValue ");
            sql.append("OR guaranteeDuration LIKE :searchValue) ");
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by Date DESC, No DESC ,FinalDate DESC ", SAQuote.class);
            setParams(query, params);
            lstSAQ = query.getResultList();
        }
        return lstSAQ;
    }

    @Override
    public void deleteRefSAInvoiceAndRSInwardOutward(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("UPDATE SAInvoiceDetail set SAQuoteDetailID = null, SAQuoteID = null where SAQuoteID = :id ; ");
//        sql.append("UPDATE RSInwardOutwardDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID = :id ; ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public List<SAQuoteReportDTO> findSAQuoteDetailsReport(UUID sAQuoteDetailID) {
        StringBuilder sql = new StringBuilder();
        List<SAQuoteReportDTO> lstSAQ = new ArrayList<SAQuoteReportDTO>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAQuoteDetail a LEFT JOIN MaterialGoods b ON a.MaterialGoodsID = b.ID LEFT JOIN  Unit c ON a.UnitID = c.ID LEFT JOIN SAQuote d ON a.SAQuoteID = d.ID WHERE ");
        sql.append(" a.SAQuoteID = :sAQuoteDetailID ");
        params.put("sAQuoteDetailID", sAQuoteDetailID);
        String selectSQL = "SELECT b.MaterialGoodsCode as materialGoodsName, a.Description as description, c.UnitName as unitName, a.Quantity as quantity, null as quantityString,a.UnitPriceOriginal as unitPriceOriginal, a.UnitPrice as unitPrice, null as unitPriceOriginalString, a.AmountOriginal as amountOriginal, null as amountOriginalString, a.DiscountRate as discountRate, a.DiscountAmountOriginal as discountAmountOriginal, a.VATRate as vATRate, a.VATAmountOriginal as vATAmountOriginal ";
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(selectSQL + sql.toString() + " ORDER BY a.OrderPriority", "SAQuoteReportDTO");
            setParams(query, params);
            lstSAQ = query.getResultList();
        }
        return lstSAQ;
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

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    /**
     * @param sort
     * @return
     */
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

    public static String addMultiSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null) {
            return "";
        }
        orderSql.append("ORDER BY ");
        int i = 0;
        for (Sort.Order order : sort) {
            if (i > 0) {
                orderSql.append(", ");
            }
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            i++;
        }
        return orderSql.toString();
    }

}
