package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.repository.SaBillRepositoryCustom;
import vn.softdreams.ebweb.service.dto.SABillReportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class SaBillRepositoryImpl implements SaBillRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<SaBillDTO> getAllSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                            String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo,
                                            String freeText, UUID org, String soLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaBillDTO> result = new ArrayList<>();
        sql.append("FROM SABILL a WHERE a.companyID = :org and (a.typeledger = :soLamViec or a.typeledger = 2)");
        params.put("org", org);
        params.put("soLamViec", soLamViec);

        if (accountingObjectID != null) {
            sql.append("AND a.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(invoiceTemplate)) {
            sql.append("AND a.invoiceTemplate = :invoiceTemplate ");
            params.put("invoiceTemplate", invoiceTemplate);
        }
        Common.addDateSearch(fromInvoiceDate, toInvoiceDate, params, sql, "invoiceDate");
        if (!Strings.isNullOrEmpty(invoiceNo)) {
            sql.append("AND upper(a.invoiceNo) like :invoiceNo ");
            params.put("invoiceNo", "%" + invoiceNo.toUpperCase() + "%");
        }
        if (!Strings.isNullOrEmpty(invoiceSeries)) {
            sql.append("AND a.invoiceSeries = :invoiceSeries ");
            params.put("invoiceSeries", invoiceSeries);
        }
        if (!Strings.isNullOrEmpty(freeText)) {
            sql.append("AND (upper(a.AccountingObjectAddress) like :freeText ");
            sql.append("or upper(a.AccountingObjectName) like :freeText ");
            sql.append("or upper(a.CompanyTaxCode) like :freeText ");
            sql.append("or upper(a.AccountingObjectBankAccount) like :freeText ");
            sql.append("or upper(a.AccountingObjectBankName) like :freeText ");
            sql.append("or upper(a.ContactName) like :freeText ");
            sql.append("or upper(a.Reason) like :freeText ");
            sql.append("or upper(a.ListNo) like :freeText ");
            sql.append("or upper(a.ListDate) like :freeText ");
            sql.append("or upper(a.ListCommonNameInventory) like :freeText) ");
            params.put("freeText", "%" + freeText.toUpperCase() + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id id, a.accountingObjectName accountingObjectName," +
                " a.totalAmount totalAmount, a.totalDiscountAmount totalDiscountAmount, a.totalVATAmount totalVATAmount," +
                " a.invoiceTemplate invoiceTemplate, a.invoiceSeries invoiceSeries, a.invoiceNo invoiceNo, a.invoiceDate invoiceDate, " +
                "ROW_NUMBER() OVER (order by invoicedate desc, invoiceTemplate, invoiceSeries, invoiceno desc ) rowIndex, a.currencyID currencyID, a.invoiceForm invoiceForm "
                + sql.toString(), "SaBillDTO");
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
    public SaBillDTO getNextSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                       String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo,
                                       String freeText, UUID org, Integer rowIndex, String soLamViec, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaBillDTO> result;
        sql.append("FROM SABILL a WHERE a.companyID = :org and (a.typeledger = :soLamViec or a.typeledger = 2)");
        params.put("org", org);
        params.put("soLamViec", soLamViec);
        if (id != null) {
            sql.append("AND a.id = :id ");
            params.put("id", id);
            rowIndex = 1;
        }
        if (accountingObjectID != null) {
            sql.append("AND a.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(invoiceTemplate)) {
            sql.append("AND a.invoiceTemplate = :invoiceTemplate ");
            params.put("invoiceTemplate", invoiceTemplate);
        }
        Common.addDateSearch(fromInvoiceDate, toInvoiceDate, params, sql, "invoiceDate");
        if (!Strings.isNullOrEmpty(invoiceNo)) {
            sql.append("AND upper(a.invoiceNo) like :invoiceNo ");
            params.put("invoiceNo", "%" + invoiceNo.toUpperCase() + "%");
        }
        if (!Strings.isNullOrEmpty(invoiceSeries)) {
            sql.append("AND a.invoiceSeries = :invoiceSeries ");
            params.put("invoiceSeries", invoiceSeries);
        }
        if (!Strings.isNullOrEmpty(freeText)) {
            sql.append("AND (upper(a.AccountingObjectAddress) like :freeText ");
            sql.append("or upper(a.AccountingObjectName) like :freeText ");
            sql.append("or upper(a.CompanyTaxCode) like :freeText ");
            sql.append("or upper(a.AccountingObjectBankAccount) like :freeText ");
            sql.append("or upper(a.AccountingObjectBankName) like :freeText ");
            sql.append("or upper(a.ContactName) like :freeText ");
            sql.append("or upper(a.Reason) like :freeText ");
            sql.append("or upper(a.ListNo) like :freeText ");
            sql.append("or upper(a.ListDate) like :freeText ");
            sql.append("or upper(a.ListCommonNameInventory) like :freeText) ");
            params.put("freeText", "%" + freeText.toUpperCase() + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * FROM (SELECT a.id id, a.accountingObjectName accountingObjectName," +
                " a.totalAmount totalAmount, a.totalDiscountAmount totalDiscountAmount, a.totalVATAmount totalVATAmount," +
                " a.invoiceTemplate invoiceTemplate, a.invoiceSeries invoiceSeries, a.invoiceNo invoiceNo, a.invoiceDate invoiceDate, " +
                "ROW_NUMBER() OVER (order by invoicedate desc, invoiceTemplate, invoiceSeries, invoiceno desc ) rowIndex, currencyID, a.invoiceForm invoiceForm "
                + sql.toString() + ") a where a.rowIndex = :rowIndex", "SaBillDTO");
            params.put("rowIndex", rowIndex);
            Common.setParams(query, params);
            result = query.getResultList();
            if (result.size() > 0) {
                SaBillDTO dto = result.get(0);
                dto.setTotalRow(total.intValue());
                return dto;
            }
        }
        return null;
    }

    @Override
    public Page<SaBillCreatedDTO> saBillCreated(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, UUID org, String currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaBillCreatedDTO> result = new ArrayList<>();
        sql.append(" from SABill sa " +
            "          where CompanyID = :companyID " +
            "  and sa.ID not in (select SABillID " +
            "                    from PPDiscountReturnDetail " +
            "                    where SABillID is not null " +
            "                    union " +
            "                    select SABillID " +
            "                    from SAInvoiceDetail " +
            "                    where SABillID is not null " +
            "                    union " +
            "                    select SABillID " +
            "                    from SAReturnDetail " +
            "                    where SABillID is not null) ");
        params.put("companyID", org);
        if (accountingObjectID != null) {
            sql.append(" and sa.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (currencyCode != null) {
            sql.append(" and sa.CurrencyID = :currencyCode ");
            params.put("currencyCode", currencyCode);
        }
        if (!Strings.isNullOrEmpty(formDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(formDate, toDate, params, sql, "sa.InvoiceDate", "InvoiceDate");
        }
        sql.append(" and (sa.typeLedger = :currentBook or sa.typeLedger = 2) ");
        params.put("currentBook", currentBook);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select sa.id, " +
                "       sa.CompanyID, " +
                "       (select Type.TypeGroupID from Type where Type.ID = sa.TypeID)                typeGroupID, " +
                "       sa.id refID2, " +
                "       sa.InvoiceDate, " +
                "       sa.InvoiceTemplate, " +
                "       sa.InvoiceSeries, " +
                "       sa.InvoiceNo, " +
                "       sa.AccountingObjectName, " +
                "       (sa.TotalAmount - TotalDiscountAmount + TotalVATAmount) as totalSabill " + sql.toString() + " order by sa.InvoiceDate desc, sa.InvoiceTemplate asc, sa.InvoiceSeries, sa.InvoiceNo desc ", "SaBillCreatedDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    @Override
    public List<SaBillCreatedDetailDTO> saBillCreatedDetail(List<UUID> saBillIdList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaBillCreatedDetailDTO> result = new ArrayList<>();
        sql.append(" from SABill sa left join  SABillDetail sadtl on sa.ID = sadtl.SABillID " +
            "where sadtl.SABillID in :saBillId");
        params.put("saBillId", saBillIdList);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" order by sa.InvoiceDate, sa.InvoiceTemplate asc, sa.InvoiceSeries, sa.InvoiceNo desc, sadtl.OrderPriority ");
            Query query = entityManager.createNativeQuery( "select  " +
                "       sa.InvoiceNo,  " +
                "       sa.InvoiceDate,  " +
                "       sa.InvoiceForm,  " +
                "       (select Type.TypeGroupID\n" +
                "        from Type\n" +
                "        where Type.ID = sa.TypeID)                                typeGroupID,  " +
                "       sa.InvoiceTemplate,  " +
                "       sadtl.id," +
                "       sadtl.MaterialGoodsID,  " +
                "       sadtl.Description,  " +
                "       sadtl.DebitAccount,  " +
                "       sadtl.CreditAccount,  " +
                "       sadtl.UnitID,  " +
                "       sadtl.Quantity,  " +
                "       sadtl.UnitPrice,  " +
                "       sadtl.UnitPriceOriginal,  " +
                "       sadtl.Amount,  " +
                "       sadtl.AmountOriginal,  " +
                "       sadtl.MainUnitID,  " +
                "       sadtl.MainQuantity,  " +
                "       sadtl.MainUnitPrice,  " +
                "       sadtl.MainConvertRate,  " +
                "       sadtl.SABillID,  " +
                "       sadtl.DiscountRate,  " +
                "       sadtl.DiscountAmount,  " +
                "       sadtl.DiscountAmountOriginal,  " +
                "       sadtl.IsPromotion,  " +
                "       sadtl.Formula,  " +
                "       sadtl.ExpiryDate,  " +
                "       sadtl.LotNo,  " +
                "       sadtl.VATRate,  " +
                "       sadtl.VATAmount,  " +
                "       sadtl.VATAmountOriginal,  " +
                "       sadtl.OrderPriority,  " +
                "       sa.accountingObjectID,  " +
                "       sa.accountingObjectName,  " +
                "       sa.contactName,  " +
                "       sa.companyTaxCode  " +
                sql.toString(), "SaBillCreatedDetailDTO");
            Common.setParams(query, params);
            result = query.getResultList();
        }
        return result;
    }

    @Override
    public List<SaBill> insertBulk(List<SaBill> saBills) {
        final List<SaBill> savedEntities = new ArrayList<>(saBills.size());
        int i = 0;
        for (SaBill saBill : saBills) {
            if (save(entityManager, saBill) == 1) {
                savedEntities.add(saBill);
            }
            i++;
            if (i > Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;
    }

    @Override
    public List<SABillReportDTO> findSABillDetailsDTO(UUID SABillId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select " +
            "       b.MaterialGoodsName, " +
            "       b.MaterialGoodsCode, " +
            "       a.Quantity, " +
            "       a.UnitPrice, " +
            "       a.UnitPriceOriginal, " +
            "       c.UnitName, " +
            "       a.Amount, " +
            "       a.AmountOriginal, " +
            "       a.VATRate, " +
            "       a.VATAmount, " +
            "       a.VATAmountOriginal, " +
            "       a.DiscountAmount, " +
            "       a.DiscountAmountOriginal, " +
            "       a.DiscountRate, " +
            "       a.Description, " +
            "       d.ListCommonNameInventory, " +
            "       a.IsPromotion " +
            "        from SABillDetail a " +
            "            left join MaterialGoods b on a.MaterialGoodsID = b.ID " +
            "            left join Unit c on c.id = a.UnitID " +
            "            left join SABill d on d.ID = a.SABillID " +
            "where a.SABillID = :SABillId" + " order by a.OrderPriority");
        params.put("SABillId", SABillId);
        Query query = entityManager.createNativeQuery(sql.toString(), "SABillDTOReport");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void multiDeleteSABill(UUID org, List<UUID> uuidListSABill) {
        String sql1 =
            "Delete SABillDetail WHERE SABillID = ?;"+
                "Delete SABill WHERE ID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidListSABill, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public List<SaBillDTO> getAllSaBillDTOByCompayIDs(UUID org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<SaBillDTO> result = new ArrayList<>();
        sql.append("FROM SABILL a WHERE a.companyID = :org ");
        params.put("org", org);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id id, a.accountingObjectName accountingObjectName," +
                " a.totalAmount totalAmount, a.totalDiscountAmount totalDiscountAmount, a.totalVATAmount totalVATAmount," +
                " a.invoiceTemplate invoiceTemplate, a.invoiceSeries invoiceSeries, a.invoiceNo invoiceNo, a.invoiceDate invoiceDate, " +
                "ROW_NUMBER() OVER (order by invoicedate desc, invoiceTemplate, invoiceSeries, invoiceno desc ) rowIndex, a.currencyID currencyID, a.invoiceForm invoiceForm "
                + sql.toString(), "SaBillDTO");
            Common.setParams(query, params);
            result = query.getResultList();
        }
        return result;
    }

    private int save(EntityManager em, SaBill hisPsTbbDtl) {
        if (hisPsTbbDtl.getId() != null) {
            em.merge(hisPsTbbDtl);
        } else {
            em.persist(hisPsTbbDtl);
        }
        return 1;
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }
}
