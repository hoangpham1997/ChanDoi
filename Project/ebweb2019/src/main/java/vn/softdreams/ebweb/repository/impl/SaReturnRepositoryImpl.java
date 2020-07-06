package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.repository.SaReturnRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class SaReturnRepositoryImpl implements SaReturnRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<SaReturnDTO> getAllSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                                String fromDate, String toDate, Boolean status,
                                                String freeText, UUID org, Integer typeID, String soLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaReturnDTO> result = new ArrayList<>();
        sql.append("FROM SaReturn a WHERE a.companyID = :org and typeid = :typeID and (a.typeledger = :soLamViec or a.typeledger = 2) ");
        params.put("org", org);
        params.put("typeID", typeID);
        params.put("soLamViec", soLamViec);

        if (accountingObjectID != null) {
            sql.append("AND a.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(currencyID)) {
            sql.append("AND a.currencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        Common.addDateSearch(fromDate, toDate, params, sql, "Date");
        if (status != null) {
            sql.append("AND a.recorded = :status ");
            params.put("status", status);
        }
        if (!Strings.isNullOrEmpty(freeText)) {
            sql.append("AND (upper(a.AccountingObjectAddress) like :freeText ");
            sql.append("or upper(a.AccountingObjectName) like :freeText ");
            sql.append("or upper(a.CompanyTaxCode) like :freeText ");
            sql.append("or upper(a.ContactName) like :freeText ");
            sql.append("or upper(a.Reason) like :freeText ");
            if (soLamViec.equalsIgnoreCase("0")) {
                sql.append("or upper(a.NoFBook) like :freeText ");
            } else {
                sql.append("or upper(a.NoMBook) like :freeText ");
            }
            sql.append("or upper(a.NumberAttach) like :freeText ");
            sql.append("or upper(a.InvoiceTemplate) like :freeText ");
            sql.append("or upper(a.InvoiceSeries) like :freeText ");
            sql.append("or upper(a.InvoiceNo) like :freeText ) ");
            params.put("freeText", "%" + freeText.toUpperCase() + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id id, a.typeID typeID, a.rsInwardOutwardID rsInwardOutwardID, a.date date," +
                " a.recorded recorded, a.PostedDate PostedDate, a.NoFBook NoFBook, a.NoMBook NoMBook," +
                " a.AccountingObjectName AccountingObjectName, a.Reason Reason, a.TotalAmount TotalAmount," +
                " a.TotalDiscountAmount TotalDiscountAmount, " +
                " a.TotalVATAmount TotalVATAmount, a.TotalPaymentAmount TotalPaymentAmount, " +
                " ROW_NUMBER() OVER (order by date DESC, posteddate DESC, (case when nofbook is not null then nofbook else nombook end) DESC ) rowIndex," +
                " a.currencyID currencyID, (case when isExportInvoice = 1 then (STUFF((SELECT ',' + b.invoiceno FROM sabill b where id in (select sabillid from SAReturnDetail where SAReturnID = a.id) FOR XML PATH('')) ,1,1,'')) else a.invoiceNo end) as invoiceNo, " +
                "a.isDeliveryVoucher isDeliveryVoucher, a.invoiceForm invoiceForm "
                + sql.toString(), "SaReturnDTO");
            Query querySum = entityManager.createNativeQuery("select sum(totalAmount - totalDiscountAmount + totalVATAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            result = query.getResultList();
            if (result.size() > 0) {
                result.get(0).setTotal(totalamount);
            }
        }
        if (pageable == null) {
            return new PageImpl<>(result);
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    @Override
    public SaReturnDTO getNextSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                           String fromDate, String toDate, Boolean status,
                                           String freeText, UUID org, Integer rowIndex, Integer typeID, String soLamViec, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaReturnDTO> result;


//        if (id != null) {
//            sql.append("FROM SaReturn a WHERE a.companyID = :org ");
//            sql.append("AND a.id = :id ");
//            params.put("org", org);
//            params.put("id", id);
//            rowIndex = 1;
//        } else {
            sql.append("FROM SaReturn a WHERE a.companyID = :org and typeid = :typeID and (a.typeledger = :soLamViec or a.typeledger = 2) ");
            params.put("org", org);
            params.put("typeID", typeID);
            params.put("soLamViec", soLamViec);
            if (accountingObjectID != null) {
                sql.append("AND a.accountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", accountingObjectID);
            }
            if (!Strings.isNullOrEmpty(currencyID)) {
                sql.append("AND a.currencyID = :currencyID ");
                params.put("currencyID", currencyID);
            }
            Common.addDateSearch(fromDate, toDate, params, sql, "Date");
            if (status != null) {
                sql.append("AND a.recorded = :status ");
                params.put("status", status);
            }
            if (!Strings.isNullOrEmpty(freeText)) {
                sql.append("AND (upper(a.AccountingObjectAddress) like :freeText ");
                sql.append("or upper(a.AccountingObjectName) like :freeText ");
                sql.append("or upper(a.CompanyTaxCode) like :freeText ");
                sql.append("or upper(a.ContactName) like :freeText ");
                sql.append("or upper(a.Reason) like :freeText ");
                sql.append("or upper(a.NoFBook) like :freeText ");
                sql.append("or upper(a.NoMBook) like :freeText ");
                sql.append("or upper(a.InvoiceTemplate) like :freeText ");
                sql.append("or upper(a.NumberAttach) like :freeText ");
                sql.append("or upper(a.InvoiceSeries) like :freeText ");
                sql.append("or upper(a.InvoiceNo) like :freeText ) ");
                params.put("freeText", "%" + freeText.toUpperCase() + "%");
            }
        // }


        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select * from (SELECT a.id id, a.typeID typeID, a.rsInwardOutwardID rsInwardOutwardID, a.date \"date\"," +
                " a.recorded recorded, a.PostedDate PostedDate, a.NoFBook NoFBook, a.NoMBook NoMBook," +
                " a.AccountingObjectName AccountingObjectName, a.Reason Reason, a.TotalAmount TotalAmount, " +
                " a.TotalDiscountAmount TotalDiscountAmount, " +
                " a.TotalVATAmount TotalVATAmount, a.TotalPaymentAmount TotalPaymentAmount, " +
                "ROW_NUMBER() OVER (order by date DESC, posteddate DESC, (case when nofbook is not null then nofbook else nombook end) DESC ) rowIndex," +
                " a.currencyID currencyID, a.invoiceNo invoiceNo, a.isDeliveryVoucher isDeliveryVoucher, a.invoiceForm invoiceForm "
                + sql.toString() + ") a where a.rowIndex = :rowIndex", "SaReturnDTO");
            params.put("rowIndex", rowIndex);
            Common.setParams(query, params);
            result = query.getResultList();
            if (result.size() > 0) {
                SaReturnDTO dto = result.get(0);
                dto.setTotalRow(total.intValue());
                return dto;
            }
        }
        return null;
    }

    @Override
    public Page<SaReturnRSInwardDTO> findAllSaReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String soLamViec) {
        List<SaReturnRSInwardDTO> saReturnDTO = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select date, PostedDate, iif(:book = 0, NoFBook, NoMBook) as book, Reason, TotalAmount - TotalDiscountAmount + TotalVATAmount as total, SAReturn.id, a.TypeGroupID ");
        sql.append("from SAReturn ");
        sql.append("left join Type a on a.ID = SAReturn.TypeID ");
        sql.append("where IsDeliveryVoucher = 0 ");
        sql.append(" and typeID = 330 ");
        sql.append("  and RSInwardOutwardID is null ");
        sql.append("  and Recorded = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (accountingObject != null) {
            sql.append("and AccountingObjectID = :accountingObject ");
            params.put("accountingObject", accountingObject);
        }
        if (org != null) {
            sql.append("and companyID = :org ");
            params.put("org", org);
        }
        if (!Strings.isNullOrEmpty(soLamViec)) {
            sql.append("and typeLedger in (:book, 2) ");
            params.put("book", soLamViec);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("and CONVERT(varchar, PostedDate, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("and CONVERT(varchar, PostedDate, 112) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" order by date desc, postedDate desc, book desc ");
            Query query = entityManager.createNativeQuery(sql.toString(), "SaReturnRSInwardDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            saReturnDTO = query.getResultList();
        }
        return new PageImpl<>(saReturnDTO, pageable, total.longValue());
    }

    @Override
    public List<RSInwardOutwardDetailReportDTO> getSaReturnDetails(UUID id) {
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
            "from SAReturnDetail rsdtl " +
            "         left join Unit ut on ut.ID = rsdtl.UnitID " +
            "         left join Repository rs on rs.ID = rsdtl.RepositoryID " +
            "         left join MaterialGoods mg on mg.id = rsdtl.MaterialGoodsID " +
            "where rsdtl.SAReturnID = :id order by OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update SAReturn SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<SaReturnDTO> getAllSAReturnHasRSID(UUID org, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaReturnDTO> result = new ArrayList<>();
        sql.append("FROM SaReturn a WHERE a.rsInwardOutwardID is not null AND a.recorded = 1 and a.companyID = :org and (a.typeledger = :soLamViec or a.typeledger = 2) ");
        params.put("org", org);
        params.put("soLamViec", currentBook);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id id, a.typeID typeID, a.rsInwardOutwardID rsInwardOutwardID, a.date date," +
                " a.recorded recorded, a.PostedDate PostedDate, a.NoFBook NoFBook, a.NoMBook NoMBook," +
                " a.AccountingObjectName AccountingObjectName, a.Reason Reason, a.TotalAmount TotalAmount," +
                " a.TotalDiscountAmount TotalDiscountAmount, " +
                " a.TotalVATAmount TotalVATAmount, a.TotalPaymentAmount TotalPaymentAmount, " +
                " ROW_NUMBER() OVER (order by date DESC, posteddate DESC, (case when nofbook is not null then nofbook else nombook end) DESC ) rowIndex," +
                " a.currencyID currencyID, (case when isExportInvoice = 1 then (STUFF((SELECT ',' + b.invoiceno FROM sabill b where id in (select sabillid from SAReturnDetail where SAReturnID = a.id) FOR XML PATH('')) ,1,1,'')) else a.invoiceNo end) as invoiceNo, " +
                "a.isDeliveryVoucher isDeliveryVoucher, a.invoiceForm invoiceForm "
                + sql.toString(), "SaReturnDTO");
            Common.setParams(query, params);
            result = query.getResultList();
        }
        return result;
    }

    @Override
    public Page<SAInvoicePopupDTO> getAllSaReturnSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, UUID org, String currentBook, String currencyID, List<UUID> listSAReturnID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SAInvoicePopupDTO> result = new ArrayList<>();
        sql.append("FROM SAReturn a left join SAReturnDetail b on a.id = b.SAReturnID WHERE a.TypeID = 340 and ");
        if (listSAReturnID.size() > 0) {
            sql.append("a.id in :ids OR ");
            params.put("ids", listSAReturnID);
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
        String sql = "UPDATE SAReturnDetail SET SaBillID = ?, saBillDeTailID = ? WHERE id = ?;" +
            "UPDATE SAReturn SET InvoiceNo = ? WHERE id = (SELECT SAReturnID FROM SAReturnDetail WHERE id = ?);";
        jdbcTemplate.batchUpdate(sql, saBillDetails, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(id).toString());
            ps.setString(2, Common.revertUUID(detail.getId()).toString());
            ps.setString(3, detail.getSaReturnDetailID() != null ? Common.revertUUID(detail.getSaReturnDetailID()).toString() : null);
            ps.setString(4, invoiceNo);
            ps.setString(5, detail.getSaReturnDetailID() != null ? Common.revertUUID(detail.getSaReturnDetailID()).toString() : null);
        });
    }

    @Override
    public List<SAInvoiceDetailPopupDTO> getSAReturnDetailBySAReturnID(List<UUID> ids, String data) {
        StringBuilder sql = new StringBuilder("select a.ID, " +
            "       SAReturnID SAInvoiceID, " +
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
        if ("0".equalsIgnoreCase(data)) {
            sql.append("       b.noFBook, ");
            sql.append("       '' noMBook, ");
        } else {
            sql.append("       '' noFBook, ");
            sql.append("       b.noMBook, ");
        }

        sql.append("       b.accountingObjectID accountingObjectID2, b.Reason, b.typeID " +
            "from SAReturnDetail a left join SAReturn b on a.SAReturnID = b.id where b.id in (:ids) order by a.OrderPriority");
        Query query = entityManager.createNativeQuery(sql.toString(), "SAInvoiceDetailPopupDTO");
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public SaReturnDetails findDetaiBySaBillID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        SaReturnDetails saInvoiceDetails = null;
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append(" from SaReturnDetail where  SABillDetailID = :id");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( "SELECT Top(1) * " + sqlSelect.toString(), SaReturnDetails.class);
            Common.setParams(query, params);
            saInvoiceDetails = (SaReturnDetails)query.getSingleResult();
        }
        return saInvoiceDetails;
    }
}
