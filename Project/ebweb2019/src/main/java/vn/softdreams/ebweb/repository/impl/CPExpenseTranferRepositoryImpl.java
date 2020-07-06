package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.CPExpenseTranfer;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.CPExpenseTranferRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CPExpenseTranferExportDTO;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPExpenseTranferRepositoryImpl implements CPExpenseTranferRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<KetChuyenChiPhiDTO> getCPExpenseTransferByCPPeriodID(UUID org, UUID cPPeriodID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("[GetDataCPExpenseListByCPPeriodID] :org, :cPPeriodID");
        params.put("org", org);
        params.put("cPPeriodID", cPPeriodID);
        Query countQuery = entityManager.createNativeQuery(sql.toString(), "KetChuyenChiPhiDTO");
        Common.setParams(countQuery, params);
        return countQuery.getResultList();
    }

    @Override
    public Page<CPExpenseTranfer> findAll(Pageable sort, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<CPExpenseTranfer> cpExpenseTranfers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CPExpenseTranfer WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        } else {
            sql.append("AND CompanyID = null ");
        }
        if (searchVoucher.getTypeID() != null) {
            sql.append("AND TypeID = :typeID ");
            params.put("typeID", searchVoucher.getTypeID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (Boolean.TRUE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 1 ");
        } else if (Boolean.FALSE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 0 ");
        } else {
        }
//        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
//            sql.append("OR reason LIKE :searchValue ");
//            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
//        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append(" AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append(" AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                + "ORDER BY date DESC ,posteddate DESC,NOFBook DESC ", CPExpenseTranfer.class);
            Common.setParamsWithPageable(query, params, sort, total);
            cpExpenseTranfers = query.getResultList();
            Query querySum = entityManager.createNativeQuery("SELECT Sum(TotalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (cpExpenseTranfers.size() > 0) {
                cpExpenseTranfers.get(0).setTotal(totalamount);
            }
        }
        return new PageImpl<>(cpExpenseTranfers, sort, total.longValue());
    }

    @Override
    public Page<CPExpenseTranferExportDTO> getAllCPExpenseTranfers(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<CPExpenseTranferExportDTO> cpExpenseTranfers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String selectSql;
        if (Boolean.TRUE.equals(isNoMBook)) {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, " +
                "NoMBook as noBook, TypeID as typeID, null as typeIDInWord, " +
                "Reason as description, TotalAmount as totalAmount ";
        } else {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, NoFBook as noBook, " +
                "TypeID as typeID, null as typeIDInWord, " +
                "Reason as description, TotalAmount as totalAmount ";
        }
        sql.append("FROM CPExpenseTranfer WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        } else {
            sql.append("AND CompanyID = null ");
        }
        if (searchVoucher.getTypeID() != null) {
            sql.append("AND TypeID = :typeID ");
            params.put("typeID", searchVoucher.getTypeID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (Boolean.TRUE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 1 ");
        } else if (Boolean.FALSE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 0 ");
        } else {
        }
//        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
//            sql.append("OR reason LIKE :searchValue ");
//            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
//        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append(" AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append(" AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(selectSql + sql.toString()
                + "ORDER BY date DESC ,posteddate DESC,NOFBook DESC ", "CPExpenseTranferExportDTO");
            Common.setParams(query, params);
            cpExpenseTranfers = query.getResultList();
            Query querySum = entityManager.createNativeQuery("SELECT Sum(TotalAmount) " + sql.toString());
            Common.setParams(querySum, params);
        }
        return new PageImpl<>(cpExpenseTranfers);
    }

    @Override
    public void multiDeleteCPExpenseTranfer(UUID org, List<UUID> uuidList_kccp) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM CPExpenseTranfer WHERE companyID = :orgID ");
        params.put("orgID", org);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList_kccp);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteCPExpenseTranferDetails(UUID org, List<UUID> uuidList_kccp) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM CPExpenseTranferDetail ";
        sql.append(" WHERE CPExpenseTranferID IN :uuidList ");
        params.put("uuidList", uuidList_kccp);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        Common.setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void deleteByCPPeriodID(List<UUID> uuids) {
        String sql1 = "delete GeneralLedger where ReferenceID in (Select c.ID from CPExpenseTranfer c where c.CPPeriodID = ?);"+
            "delete CPExpenseTranferDetail where CPExpenseTranferID in (Select c.ID from CPExpenseTranfer c where c.CPPeriodID = ?);" +
            "delete CPExpenseTranfer where CPPeriodID = ?";
        jdbcTemplate.batchUpdate(sql1, uuids, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
        });
    }
}
