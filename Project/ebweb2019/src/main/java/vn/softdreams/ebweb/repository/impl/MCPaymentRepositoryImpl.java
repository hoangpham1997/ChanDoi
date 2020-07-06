package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.repository.MCPaymentRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class MCPaymentRepositoryImpl implements MCPaymentRepositoryCustom {

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
    public MCPayment findByAuditID(UUID mcAuditID) {
        StringBuilder sql = new StringBuilder();
        MCPayment mcPayment = null;
        List<MCPayment> mcPayments = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT TOP 1 * FROM MCPayment WHERE MCAuditID = :MCAuditID");
        params.put("MCAuditID", mcAuditID);
        Query query = entityManager.createNativeQuery(sql.toString(), MCPayment.class);
        setParams(query, params);
        mcPayments = query.getResultList();
        if (mcPayments.size() > 0)
            mcPayment = mcPayments.get(0);
        return mcPayment;
    }

    /**
     * Hautv
     *
     * @param ID
     * @return
     */
    @Override
    public List<MCPaymentDetails> findMCPaymentDetails(UUID ID) {
        StringBuilder sql = new StringBuilder();
        List<MCPaymentDetails> mcPaymentDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM MCPaymentDetail WHERE MCPaymentID = :MCPaymentID");
        params.put("MCPaymentID", ID);
        Query query = entityManager.createNativeQuery(sql.toString(), MCPaymentDetails.class);
        setParams(query, params);
        mcPaymentDetails = query.getResultList();
        return mcPaymentDetails;
    }

    /**
     * Hautv
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<MCPaymentDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        List<MCPaymentDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MCPayment Where CompanyID = :companyID ");
        params.put("companyID", companyID);
        String orderBy = "";
        if (displayOnBook == 0) {
            orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2)");
        } else if (displayOnBook == 1) {
            orderBy = " order by Date DESC ,PostedDate DESC ,noMbook DESC";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT *, null as TypeName " + sql.toString() + " order by Date DESC ,PostedDate DESC ,(case when noFbook is not null then noFbook else noMBook end) desc ", "MCPaymentDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lst.get(0).setTotal(totalamount);
        }
        return new PageImpl<>(((List<MCPaymentDTO>) lst), pageable, total.longValue());
    }

    @Override
    public UpdateDataDTO getNoBookById(UUID paymentVoucherID) {
        String sql = "select NoFBook , NoMBook , TypeLedger from MCPayment where ID = ?1 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, paymentVoucherID);
        Object result[] = (Object[]) query.getSingleResult();
        return new UpdateDataDTO((String) result[0], (String) result[1], (Integer) result[2]);
    }

    @Override
    public Object findIDRef(UUID uuid, Integer typeID) {
        Map<String, Object> params = new HashMap<>();
        String sql = "";
        if (typeID.equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU) || typeID.equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU)) {
            sql = "select ID from PPService where PaymentVoucherID = :id";
        } else {
            sql = "select ID, StoredInRepository from PPInvoice where PaymentVoucherID = :id";
        }
        Query query = entityManager.createNativeQuery(sql);
        params.put("id", uuid);
        Common.setParams(query, params);
        return query.getSingleResult();
    }

    @Override
    public List<PPInvoiceDTO> findVoucherByListPPInvoiceID(List<UUID> uuids, int voucherTypeID) {
        StringBuilder sql = new StringBuilder();
        List<PPInvoiceDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ID, Date, NoFBook, NoMBook, DueDate from ");
        switch (voucherTypeID) {
            case TypeConstant
                .MUA_HANG_CHUA_THANH_TOAN:
                sql.append("PPInvoice where ID in :listID ;");
                break;
            case TypeConstant
                .PP_SERVICE:
                sql.append("PPService where ID in :listID ;");
                break;
            case TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC:
                sql.append("GOtherVoucher where ID in :listID ;");
                break;
            case TypeConstant
                .OPENING_BLANCE_AC:
                sql = new StringBuilder();
                sql.append("select ReferenceID as ID, Date, NoFBook, NoMBook, NULL as DueDate from GeneralLedger where ReferenceID in :listID ;");
                break;
        }
        params.put("listID", uuids);
        Query query = entityManager.createNativeQuery(sql.toString(), "ViewFromMCPaymentDTO");
        Utils.setParams(query, params);
        lst = query.getResultList();
        return lst;
    }

    @Override
    public void multiDeleteMCPayment(UUID org, List<UUID> uuidListMCPayment) {
        String sql1 =
            "Delete MCPaymentDetail WHERE MCPaymentID = (SELECT TOP(1) ID FROM MCPayment WHERE MCAuditID = ?);" +
                "Delete GeneralLedger WHERE referenceID = (SELECT TOP(1) ID FROM MCPayment WHERE MCAuditID = ?);" +
                "Delete MCPayment WHERE MCAuditID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidListMCPayment, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(detail).toString());
        });
    }

    @Modifying
    @Override
    public void multiDeleteByID(UUID org, List<UUID> uuids) {
        String sql1 =
            "Delete MCPaymentDetail WHERE MCPaymentID = ? ;" +
                "Delete GeneralLedger WHERE referenceID = ? and CompanyID = ? ;" +
                "Delete MCPaymentDetailInsurance WHERE MCPaymentID = ? ;" +
                "Delete MCPaymentDetailSalary WHERE MCPaymentID = ? ;" +
                "Delete MCPaymentDetailTax WHERE MCPaymentID = ? ;" +
                "Delete MCPaymentDetailVendor WHERE MCPaymentID = ? ;" +
                "Delete MCPayment WHERE ID = ?  and CompanyID = ? ;" +
                "Delete RefVoucher WHERE (RefID1 = ? or RefID2 = ? ) and CompanyID = ?  ;";
        jdbcTemplate.batchUpdate(sql1, uuids, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(org).toString());
            ps.setString(4, Common.revertUUID(detail).toString());
            ps.setString(5, Common.revertUUID(detail).toString());
            ps.setString(6, Common.revertUUID(detail).toString());
            ps.setString(7, Common.revertUUID(detail).toString());
            ps.setString(8, Common.revertUUID(detail).toString());
            ps.setString(9, Common.revertUUID(org).toString());
            ps.setString(10, Common.revertUUID(detail).toString());
            ps.setString(11, Common.revertUUID(detail).toString());
            ps.setString(12, Common.revertUUID(org).toString());
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
