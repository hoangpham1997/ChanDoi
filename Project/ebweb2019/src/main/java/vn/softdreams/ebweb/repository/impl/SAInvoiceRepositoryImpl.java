package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.repository.SAInvoiceRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class SAInvoiceRepositoryImpl implements SAInvoiceRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<SAInvoiceViewDTO> searchSAInvoice(Pageable pageable, UUID accountingObjectID,
                                                  String currencyID, String fromDate,
                                                  String toDate, Boolean status, String keySearch, UUID org,
                                                  Integer typeId, String soLamViec,
                                                  Boolean isExport) {
        Map<String, Object> params = new HashMap<>();
        List<SAInvoiceViewDTO> saInvoices = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT pp.id, " +
            "       typeID, " +
            "       date, " +
            "       postedDate, " +
            "       case when :typeLedger = 0 then pp.NoFBook else pp.NoMBook end as NoBook, " +
            "       invoiceNo, " +
            "       accountingObjectName, " +
            "       accountingObjectAddress, " +
            "       reason, " +
            "       totalAmount, " +
            "       totalDiscountAmount, " +
            "       totalVATAmount, " +
            "       (totalAmount - totalDiscountAmount + totalVATAmount) as totalAllAmount, " +
            "       recorded, " +
            "       currencyID, " +
            "       exported, " +
            "       rsInwardOutwardID, " +
            "       mcReceiptID, " +
            "       mbDepositID, invoiceForm, t.TypeName typeName ");
        params.put("typeLedger", soLamViec);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM SAInvoice pp ");
        sqlBuilder.append(" left join Type t on t.ID = pp.TypeID  ");
        sqlBuilder.append(" where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", soLamViec);
        if (typeId != null) {
            sqlBuilder.append(" and pp.TypeID = :typeID ");
            params.put("typeID", typeId);
        } else {
            sqlBuilder.append(" and pp.TypeID like '32%' ");
        }

        if (accountingObjectID != null) {
            sqlBuilder.append(" and pp.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (status != null) {
            sqlBuilder.append(" and pp.Recorded = :recorded ");
            params.put("recorded", status);
        }
        if (fromDate != null && !Strings.isNullOrEmpty(fromDate)) {
            sqlBuilder.append(" and pp.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append(" and pp.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        if (!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append(" and (pp.ContactName like :keySearch ");
            sqlBuilder.append(" or pp.Reason like  :keySearch ");
            sqlBuilder.append(" or pp.CompanyID like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectAddress like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectName like  :keySearch ");
            sqlBuilder.append(" or pp.ListCommonNameInventory like :keySearch ");
            sqlBuilder.append(" or pp.ListNo like :keySearch ");
            sqlBuilder.append(" or pp.EmployeeID like :keySearch");
            sqlBuilder.append(" or pp.NoFBook like :keySearch ");
            sqlBuilder.append(" or pp.NoMBook like :keySearch ");
            sqlBuilder.append(" or pp.InvoiceNo like  :keySearch ");
            sqlBuilder.append(" or pp.TotalAmount like  :keySearch ");
            sqlBuilder.append(" or pp.TotalDiscountAmount like  :keySearch ");
            sqlBuilder.append(" or pp.TotalVATAmount like :keySearch ");
            sqlBuilder.append(" or (pp.TotalAmount + pp.TotalVATAmount - pp.TotalDiscountAmount) like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString() + sqlBuilder.toString() + " order by pp.Date desc, pp.PostedDate desc , pp.NoFBook desc ", "SAInvoiceViewDTO");

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount - totalDiscountAmount + totalVATAmount) " + sqlBuilder.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (isExport) {
                Common.setParams(query, params);
            } else {
                Common.setParamsWithPageable(query, params, pageable, total);
            }
            saInvoices = query.getResultList();
            if (saInvoices.size() > 0) {
                saInvoices.get(0).setTotal(totalamount);
            }
        }
        if (pageable == null) {
            return new PageImpl<>(saInvoices);
        }
        return new PageImpl<>(saInvoices, pageable, total.longValue());
    }

    @Override
    public SAInvoice findByRowNum(UUID accountingObjectID,
                                  String currencyID, String fromDate,
                                  String toDate, Boolean status, String keySearch, Integer rowNum, UUID org, String soLamViec, Integer typeId) {
        StringBuilder sqlBuilder = new StringBuilder();
        SAInvoice saInvoice = null;
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" FROM SAInvoice pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", soLamViec);

        if (typeId != null) {
            sqlBuilder.append(" and pp.TypeID = :typeID ");
            params.put("typeID", typeId);
        } else {
            sqlBuilder.append(" and pp.TypeID like '32%' ");
        }

        if (accountingObjectID != null) {
            sqlBuilder.append(" and pp.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (status != null) {
            sqlBuilder.append(" and pp.Recorded = :recorded ");
            params.put("recorded", status);
        }
        if (fromDate != null || toDate != null) {
            Common.addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "pp.Date", "Date");
        }
        if (!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append(" and (pp.ContactName like :keySearch ");
            sqlBuilder.append(" or pp.Reason like  :keySearch ");
            sqlBuilder.append(" or pp.CompanyID like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectAddress like  :keySearch ");
            sqlBuilder.append(" or pp.AccountingObjectName like  :keySearch ");
            sqlBuilder.append(" or pp.ListCommonNameInventory like :keySearch ");
            sqlBuilder.append(" or pp.ListNo like :keySearch ");
            sqlBuilder.append(" or pp.ListDate like :keySearch ");
            sqlBuilder.append(" or pp.EmployeeID like :keySearch ");
            params.put("keySearch", "%" + keySearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (order by pp.Date desc, pp.PostedDate desc, pp.NoFBook desc) rownum "
                + sqlBuilder.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, SAInvoice.class);
            Common.setParams(query, params);
            saInvoice = (SAInvoice) query.getSingleResult();
        }
        return saInvoice;
    }

    @Override
    public List<SAInvoiceDetailPopupDTO> getSaInvoiceDetail(List<UUID> ids, String soLamViec) {
        StringBuilder sql = new StringBuilder("select a.ID, " +
            "       SAInvoiceID, " +
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
            "       DiscountRate, " +
            "       DiscountAmount, " +
            "       DiscountAmountOriginal, " +
            "       DiscountAccount, " +
            "       VATRate, " +
            "       VATAmount, " +
            "       VATAmountOriginal, " +
            "       VATAccount, " +
            "       RepositoryAccount, " +
            "       CostAccount, " +
            "       OWPrice, " +
            "       OWAmount, " +
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
            if ("0".equalsIgnoreCase(soLamViec)) {
                sql.append("       b.noFBook, ");
                sql.append("       '' noMBook, ");
            } else {
                sql.append("       '' noFBook, ");
                sql.append("       b.noMBook, ");
            }

        sql.append("       b.accountingObjectID accountingObjectID2, b.Reason, b.typeID " +
        "from SAInvoiceDetail a left join SAInvoice b on a.SAInvoiceID = b.id where a.id in (:ids) order by b.NoFBook desc, b.NoMBook desc, a.OrderPriority");
        Query query = entityManager.createNativeQuery(sql.toString(), "SAInvoiceDetailPopupDTO");
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Page<SAInvoicePopupDTO> getAllSaInvoiceSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID,
                                                                  String fromDate, String toDate,
                                                                  UUID org, String currentBook, String currencyID, List<UUID> listSAInvoiceID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SAInvoicePopupDTO> result = new ArrayList<>();
        sql.append("FROM SaInvoice a left join SaInvoiceDetail b on a.id = b.saInvoiceID WHERE ");
        if (listSAInvoiceID.size() > 0) {
            sql.append("a.id in :ids OR ");
            params.put("ids", listSAInvoiceID);
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
    public Page<SAInvoicePopupDTO> getAllSaInvoiceSaReturnPopupDTOs(Pageable pageable, UUID accountingObjectID,
                                                                    String fromDate, String toDate,
                                                                    UUID org, String currentBook, String currencyID, Integer typeID, List<UUID> listSAInvoiceID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SAInvoicePopupDTO> result = new ArrayList<>();
        sql.append("SELECT b.id, a.id saInvoiceID, a.date, ");
        if (currentBook.equalsIgnoreCase("0")) {
            sql.append("a.noFBook, '' noMBook, ");
        } else {
            sql.append("'' noFBook, a.noMBook, ");
        }

        sql.append("c.materialgoodscode, b.description materialgoodsname, (b.Quantity - (SELECT ISNULL(SUM(Quantity), 0) from SAReturnDetail where SAInvoiceDetailID = b.id)) quantity, " +
            "(b.Quantity - (SELECT ISNULL(SUM(Quantity), 0) from SAReturnDetail where SAInvoiceDetailID = b.id)) returnQuantity, b.OrderPriority ");
        sql.append("FROM SaInvoiceDetail b left join SaInvoice a on a.id = b.saInvoiceID ");
        sql.append("left join materialGoods c on c.id = b.materialgoodsid ");
        sql.append("WHERE a.companyID = :org and a.recorded = 1 ");
        sql.append("and (a.typeledger = :soLamViec or a.typeledger = 2) ");
        params.put("soLamViec", currentBook);
        if (typeID.equals(TypeConstant.HANG_GIAM_GIA)) {
            sql.append("and (b.IsPromotion = 0 Or b.IsPromotion is null) ");
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            sql.append("AND a.currencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            sql.append("AND a.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (listSAInvoiceID.size() > 0) {
            sql.append("AND b.id not in :ids ");
            params.put("ids", listSAInvoiceID);
        }
        Common.addDateSearch(fromDate, toDate, params, sql, "date");
        params.put("org", org);

        if (typeID == TypeConstant.HANG_BAN_TRA_LAI) {
            sql.append(" ) a where a.quantity > 0 ");
        } else {
            sql.append(" ) a ");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) from ( " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id, a.sainvoiceID, " +
                " a.date, a.noFBook, a.noMBook, a.materialGoodsCode, a.materialGoodsName, " +
                " a.quantity, a.returnQuantity from ( " + sql.toString() + " order by noFBook desc, noMBook desc, date desc, orderPriority", "SAInvoiceSaReturnPopupDTO");
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
    public void updateSaBill(Set<SaBillDetails> saBillDetails, UUID saBillID, String InvoiceNo) {
        String sql = "UPDATE SaInvoiceDetail SET SaBillID = ?, saBillDeTailID = ? WHERE id = ?;" +
            "UPDATE SAInvoice SET InvoiceNo = ? WHERE id = (SELECT SAInvoiceID FROM SAInvoiceDetail WHERE id = ?);";
        jdbcTemplate.batchUpdate(sql, saBillDetails, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(saBillID).toString());
            ps.setString(2, Common.revertUUID(detail.getId()).toString());
            ps.setString(3, detail.getSaInvoiceDetailID() != null ? Common.revertUUID(detail.getSaInvoiceDetailID()).toString() : null);
            ps.setString(4, InvoiceNo);
            ps.setString(5, detail.getSaInvoiceDetailID() != null ? Common.revertUUID(detail.getSaInvoiceDetailID()).toString() : null);
        });
    }

    @Override
    public List<SAInvoiceDetailPopupDTO> getSaInvoiceDetailBySAInvoiceID(List<UUID> ids, String soLamViec) {
        StringBuilder sql = new StringBuilder("select a.ID, " +
            "       SAInvoiceID, " +
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
            "       DiscountRate, " +
            "       DiscountAmount, " +
            "       DiscountAmountOriginal, " +
            "       DiscountAccount, " +
            "       VATRate, " +
            "       VATAmount, " +
            "       VATAmountOriginal, " +
            "       VATAccount, " +
            "       RepositoryAccount, " +
            "       CostAccount, " +
            "       OWPrice, " +
            "       OWAmount, " +
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
        if ("0".equalsIgnoreCase(soLamViec)) {
            sql.append("       b.noFBook, ");
            sql.append("       '' noMBook, ");
        } else {
            sql.append("       '' noFBook, ");
            sql.append("       b.noMBook, ");
        }

        sql.append("       b.accountingObjectID accountingObjectID2, b.Reason, b.typeID " +
            "from SAInvoiceDetail a left join SAInvoice b on a.SAInvoiceID = b.id where b.id in (:ids) order by a.OrderPriority");
        Query query = entityManager.createNativeQuery(sql.toString(), "SAInvoiceDetailPopupDTO");
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public void DeleteRelate(List<String> listRelateID) {
        String sql1 = "Delete MCReceipt WHERE id = ?;"+
            "Delete MCReceiptDetail WHERE MCReceiptID = ?;"+
            "Delete MCReceiptDetailCustomer WHERE MCReceiptID = ?;"+
            "Delete MBDeposit WHERE id = ?;"+
            "Delete MBDepositDetail WHERE MBDepositID = ?;"+
            "Delete MBDepositDetailCustomer WHERE MBDepositID = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, listRelateID, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, detail);
            ps.setString(2, detail);
            ps.setString(3, detail);
            ps.setString(4, detail);
            ps.setString(5, detail);
            ps.setString(6, detail);
            ps.setString(7, detail);
        });
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update SAInvoice SET Recorded = 0 WHERE id = ? ;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<SAInvoiceViewDTO> getAllSAInvoiceHasRSID(UUID org, Integer soLamViec) {
        Map<String, Object> params = new HashMap<>();
        List<SAInvoiceViewDTO> saInvoices = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT id, " +
            "       typeID, " +
            "       date, " +
            "       postedDate, " +
            "       case when :typeLedger = 0 then pp.NoFBook else pp.NoMBook end as NoBook, " +
            "       invoiceNo, " +
            "       accountingObjectName, " +
            "       accountingObjectAddress, " +
            "       reason, " +
            "       totalAmount, " +
            "       totalDiscountAmount, " +
            "       totalVATAmount, " +
            "       (totalAmount - totalDiscountAmount + totalVATAmount) as totalAllAmount, " +
            "       recorded, " +
            "       currencyID, " +
            "       exported, " +
            "       rsInwardOutwardID, " +
            "       mcReceiptID, " +
            "       mbDepositID, invoiceForm, null typeName ");
        params.put("typeLedger", soLamViec);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM SAInvoice pp where pp.rsInwardOutwardID is not null and recorded = 1 and pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", soLamViec);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString() + sqlBuilder.toString() + " order by pp.Date desc, pp.PostedDate desc , pp.NoFBook desc ", "SAInvoiceViewDTO");
            Common.setParams(query, params);
            saInvoices = query.getResultList();
        }
        return saInvoices;
    }

    @Override
    public List<SAInvoiceDetails> findSAOrderIDByListSAInvoice(List<UUID> uuidList) {
        Map<String, Object> params = new HashMap<>();
        List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append(" from SAInvoiceDetail where  SAInvoiceID IN :ids AND SAOrderDetailID IS NOT NULL ");
        params.put("ids", uuidList);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( "SELECT * " + sqlSelect.toString(), SAInvoiceDetails.class);
            Common.setParams(query, params);
            saInvoiceDetails = query.getResultList();
        }
        return saInvoiceDetails;
    }

    @Override
    public SAInvoiceDetails findDetaiBySaBillID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        SAInvoiceDetails saInvoiceDetails = null;
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append(" from SAInvoiceDetail where  SABillDetailID = :id");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( "SELECT Top(1) * " + sqlSelect.toString(), SAInvoiceDetails.class);
            Common.setParams(query, params);
            saInvoiceDetails = (SAInvoiceDetails)query.getSingleResult();
        }
        return saInvoiceDetails;
    }

    @Override
    public List<RSInwardOutwardDetailReportDTO> getSaInvoiceDetails(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select mg.MaterialGoodsCode, " +
            "       mg.MaterialGoodsName, " +
            "       ut.UnitName, " +
            "       rsdtl.Quantity, " +
            "       rsdtl.AmountOriginal, " +
            "       rsdtl.OWAmount AS Amount, " +
            "       rsdtl.CreditAccount, " +
            "       rsdtl.DebitAccount, " +
            "       rs.RepositoryCode, " +
            "       rsdtl.OWPrice AS UnitPriceOriginal, " +
            "       rs.RepositoryName, " +
            "       rsdtl.Description " +
            "from SaInvoiceDetail rsdtl " +
            "         left join Unit ut on ut.ID = rsdtl.UnitID " +
            "         left join Repository rs on rs.ID = rsdtl.RepositoryID " +
            "         left join MaterialGoods mg on mg.id = rsdtl.MaterialGoodsID " +
            "where rsdtl.SaInvoiceID = :id order by OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }
}
