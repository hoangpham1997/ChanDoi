package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.repository.SAOrderRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnOutWardDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.SAOrderOutwardDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class SAOrderRepositoryImpl implements SAOrderRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<SAOrderDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<SAOrderDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAOrder Where CompanyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by Date DESC ,No DESC ", "SAOrderDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lst.get(0).setTotal(totalamount);
        }
        return new PageImpl<>(((List<SAOrderDTO>) lst), pageable, total.longValue());
    }

    @Override
    public Page<SAOrderDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String currencyID, UUID objectId) {
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlAdd = new StringBuilder();
        List<SAOrderDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> paramsAdd = new HashMap<>();
        if (objectId != null) {
            sqlAdd.append("(ISNULL(a.quantityDelivery, 0) - ISNULL((SELECT TOP (1) Quantity " +
                "                                         from SAInvoiceDetail " +
                "                                                  left join SAInvoice on SAInvoice.ID = SAInvoiceDetail.SAInvoiceID " +
                "                                         WHERE SAInvoice.ID = :objectID " +
                "           AND SAInvoiceDetail.SAOrderDetailID = a.ID), 0)) as QuantityDelivery ");
            paramsAdd.put("objectID", objectId);
        } else {
            sqlAdd.append("  ISNULL(a.quantityDelivery, 0) as QuantityDelivery ");
        }
        sql.append(" from SAOrderDetail a " +
            "         left join " +
            "     MaterialGoods ON MaterialGoods.ID = a.MaterialGoodsID " +
            "         right join SAOrder on SAOrder.ID = a.SAOrderID " +
            "         left join Type b on b.ID = SAOrder.TypeID " +
            "where a.Quantity > 0 AND (a.QuantityDelivery < a.Quantity OR a.QuantityDelivery IS NULL) AND SAOrder.CompanyID = :companyID ");
        params.put("companyID", org);
        if (!Strings.isNullOrEmpty(currencyID)) {
            sql.append("  and SAOrder.CurrencyID = :currencyID ");
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
            Query query = entityManager.createNativeQuery("select a.SAOrderID as ID," +
                "       a.ID as sAOrderDetailID," +
                "       TypeID," +
                "       SAOrder.CompanyID," +
                "       No," +
                "       Date," +
                "       SAOrder.AccountingObjectID," +
                "       SAOrder.AccountingObjectName," +
                "       SAOrder.AccountingObjectAddress," +
                "       SAOrder.Reason," +
                "       SAOrder.ContactName," +
                "       SAOrder.CompanyTaxCode," +
                "       SAOrder.EmployeeID," +
                "       a.MaterialGoodsID," +
                "       MaterialGoodsCode," +
                "       a.Description," +
                "       a.Quantity," +
                "       a.UnitPrice," +
                "       a.Amount," +
                "       a.unitID," +
                "       a.unitPriceOriginal," +
                "       a.mainQuantity," +
                "       a.mainUnitID," +
                "       a.mainUnitPrice," +
                "       a.mainConvertRate," +
                "       a.formula," +
                "       a.vATDescription," +
                "       a.amountOriginal," +
                "       a.discountRate," +
                "       a.discountAmount," +
                "       a.discountAmountOriginal," +
                "       a.vATRate," +
                "       a.vATAmount," +
                "       a.vATAmountOriginal, " +
                "       MaterialGoods.RepositoryID, " +
                "       b.TypeGroupID, " +
                sqlAdd.toString() + sql.toString() + " order by Date DESC ,No DESC", "SAOrderDTOPopup");
            setParams(query, paramsAdd);
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<SAOrderDTO>) lst), pageable, total.longValue());
    }

    @Override
    public void deleteRefSAInvoiceAndRSInwardOutward(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("UPDATE SAInvoiceDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID = :id ; ");
        sql.append("UPDATE RSInwardOutwardDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID = :id ; ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public List<SAOrderDTO> findSAOrderDetailsDTO(UUID sAOrderId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select " +
            "       a.DiscountAmount, " +
            "       a.Description, " +
            "       b.MaterialGoodsCode, " +
            "       a.Quantity, " +
            "       c.UnitName, " +
            "       a.UnitPrice, " +
            "       a.UnitPriceOriginal, " +
            "       a.Amount, " +
            "       a.AmountOriginal, " +
            "       a.DiscountAmount, " +
            "       a.DiscountAmountOriginal, " +
            "       a.DiscountRate, " +
            "       a.VATRate, " +
            "       a.VATAmount, " +
            "       a.VATAmountOriginal " +
            "        from SAOrderDetail a " +
            "            left join MaterialGoods b on a.MaterialGoodsID = b.ID " +
            "            left join Unit c on c.id = a.UnitID " +
            "where SAOrderID = :sAOrderId");
        params.put("sAOrderId", sAOrderId);
        Query query = entityManager.createNativeQuery(sql.toString(), "SAOrderDTOReport");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void updateQuantitySAOrder(List<UUID> uuidListSAOrderDetails) {
        String sql = "UPDATE SAOD SET SAOD.Quantity = (SAOD.Quantity + SAID.Quantity) FROM SAOrderDetail AS SAOD INNER JOIN SAInvoiceDetail AS SAID ON SAOD.ID = SAID.SAOrderDetailID WHERE SAOD.ID = ? ;";
        jdbcTemplate.batchUpdate(sql, uuidListSAOrderDetails, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public void updateQuantitySAOrderBySA(List<SAInvoiceDetails> oldSADetailList) {
        String sql = "UPDATE SAOD SET SAOD.QuantityDelivery = (SAOD.QuantityDelivery - (SELECT sad.Quantity FROM SAInvoiceDetail sad where ID = ?)) FROM SAOrderDetail AS SAOD WHERE SAOD.ID = ?;";
        jdbcTemplate.batchUpdate(sql, oldSADetailList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail.getId()).toString());
            ps.setString(2, Common.revertUUID(detail.getsAOrderDetailID()).toString());
        });
    }

    /*@Override
    public void deleteByListID(List<UUID> uuids) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("delete saorder where id in :id; +" +
            "        delete RefVoucher where RefID1 in :id or RefID2 in :id ; +" +
            "        UPDATE SAInvoiceDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID in :id ; +" +
            "        UPDATE RSInwardOutwardDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID in :id ;");
        params.put("id", uuids);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }*/


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
}
