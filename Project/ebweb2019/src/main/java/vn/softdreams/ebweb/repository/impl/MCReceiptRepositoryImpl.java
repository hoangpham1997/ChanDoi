package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.repository.MCReceiptRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.SAInvoiceForMCReceiptDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.SaInvoiceDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class MCReceiptRepositoryImpl implements MCReceiptRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Add by chuongnv
     *
     * @param mcAuditID
     * @return
     */
    @Override
    public MCReceipt findByAuditID(UUID mcAuditID) {
        StringBuilder sql = new StringBuilder();
        MCReceipt mcReceipt = null;
        List<MCReceipt> mcReceipts = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT TOP 1 * FROM MCReceipt WHERE MCAuditID = :MCAuditID");
        params.put("MCAuditID", mcAuditID);
        Query query = entityManager.createNativeQuery(sql.toString(), MCReceipt.class);
        setParams(query, params);
        try {
            mcReceipts = query.getResultList();
            if (mcReceipts.size() > 0)
                mcReceipt = mcReceipts.get(0);
        } catch (Exception ex) {
            String s = ex.getMessage();
        }
        return mcReceipt;
    }

    @Override
    public Page<MCReceiptDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        List<MCReceiptDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MCReceipt WHERE CompanyID = :companyID ");
        params.put("companyID", companyID);
        String orderBy = "";
        if (displayOnBook == 0) {
//            orderBy = " order by Date DESC ,PostedDate DESC ,(case when noFbook is not null then noFbook else noMBook end) desc";
            orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2)");
        } else if (displayOnBook == 1) {
            orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {

            Query query = entityManager.createNativeQuery("SELECT *, null as TypeName " + sql.toString() + orderBy, "MCReceiptDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lst.get(0).setTotal(totalamount);
        }
        return new PageImpl<>(((List<MCReceiptDTO>) lst), pageable, total.longValue());
    }

    @Override
    public List<SAInvoiceForMCReceiptDTO> findVoucherByListSAInvoice(List<UUID> uuids, int voucherTypeID) {
        StringBuilder sql = new StringBuilder();
        List<SAInvoiceForMCReceiptDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        switch (voucherTypeID){
            case TypeConstant
                .BAN_HANG_CHUA_THU_TIEN:
                sql.append("select a.ID, a.Date, a.NoFBook, a.NoMBook, b.PaymentClauseCode, a.DueDate from ");
                sql.append("SAInvoice a left join PaymentClause b on a.PaymentClauseID = b.ID where a.ID in :listID ;");
                break;
            case TypeConstant
                .CHUNG_TU_NGHIEP_VU_KHAC:
                sql.append("select a.ID, a.Date, a.NoFBook, a.NoMBook, NULL as PaymentClauseCode, a.DueDate from ");
                sql.append("GOtherVoucher a where a.ID in :listID ;");
                break;
            case TypeConstant
                .OPENING_BLANCE_AC:
                sql.append("select a.ReferenceID as ID, a.Date, a.NoFBook, a.NoMBook, NULL as PaymentClauseCode, NULL as DueDate from ");
                sql.append("GeneralLedger a where a.ReferenceID in :listID ;");
                break;
        }
        params.put("listID", uuids);
        Query query = entityManager.createNativeQuery(sql.toString(), "ViewFromMCReceiptDTO");
        Utils.setParams(query, params);
        lst = query.getResultList();
        return lst;
    }

    @Override
    public void multiDeleteMCReceipt(UUID org, List<UUID> uuidListMCReceipt) {
        String sql1 =
            "Delete MCReceiptDetail WHERE MCReceiptID = (SELECT TOP(1) ID FROM MCReceipt WHERE MCAuditID = ?);"+
            "Delete GeneralLedger WHERE referenceID = (SELECT TOP(1) ID FROM MCReceipt WHERE MCAuditID = ?);"+
            "Delete MCReceipt WHERE MCAuditID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidListMCReceipt, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public void multiDeleteByID(UUID org, List<UUID> uuidListMCReceipt) {
        String sql1 =
            "Delete MCReceiptDetail WHERE MCReceiptID = ? ;" +
                "Delete GeneralLedger WHERE referenceID = ? and CompanyID = ? ;" +
                "Delete MCReceiptDetailCustomer WHERE MCReceiptID = ? ;" +
                "Delete MCReceiptDetailTax WHERE MCReceiptID = ? ;" +
                "Delete MCReceipt WHERE ID = ?  and CompanyID = ? ;" +
                "Delete RefVoucher WHERE (RefID1 = ? or RefID2 = ? ) and CompanyID = ?  ;";
        jdbcTemplate.batchUpdate(sql1, uuidListMCReceipt, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(org).toString());
            ps.setString(4, Common.revertUUID(detail).toString());
            ps.setString(5, Common.revertUUID(detail).toString());
            ps.setString(6, Common.revertUUID(detail).toString());
            ps.setString(7, Common.revertUUID(org).toString());
            ps.setString(8, Common.revertUUID(detail).toString());
            ps.setString(9, Common.revertUUID(detail).toString());
            ps.setString(10, Common.revertUUID(org).toString());
        });
    }

    @Override
    public void mutipleUnRecord(List<UUID> listUnRecord, UUID orgID) {
        String sql1 = "Delete GeneralLedger WHERE referenceID = ? AND CompanyID = ?;";
        jdbcTemplate.batchUpdate(sql1, listUnRecord, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Common.revertUUID(orgID).toString());
        });
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

}
