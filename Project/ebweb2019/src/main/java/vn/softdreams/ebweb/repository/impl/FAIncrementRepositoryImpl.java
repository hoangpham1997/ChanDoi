package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.repository.FAIncrementRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.FAIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.FAIncrementDetailRefVoucherConvertDTO;
import vn.softdreams.ebweb.service.dto.FAIncrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class FAIncrementRepositoryImpl implements FAIncrementRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<FAIncrementConvertDTO> getAllFAIncrementsSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<FAIncrementConvertDTO> faIncrement = null;
        Map<String, Object> params = new HashMap<>();
        sql.append(" from FAIncrement where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        params.put("companyID", org);
        params.put("currentBook", currentBook ? 1 : 0);
        if (keySearch != null) {
            if (currentBook) {
                sql.append(" and (NoMBook like :keySearch ");
            } else {
                sql.append(" and (NoFBook like :keySearch ");
            }
            sql.append(" or Reason like :keySearch or TotalAmount like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Query countSum = entityManager.createNativeQuery("SELECT sum(TotalAmount) " + sql.toString());
        Common.setParams(countSum, params);
        Number total = (Number) countQuery.getSingleResult();
        BigDecimal sum = (BigDecimal) countSum.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT id, Date, TypeLedger, NofBook, NoMBook, case :currentBook" +
                " when 1 then NoMBook else NoFBook end noBook , Reason, TotalAmount, " +
                "TemplateID, Recorded " + sql.toString() + " order by Date desc, NoFbook DESC, NoMbook DESC ", "FAIncrementConvertDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            faIncrement = query.getResultList();
            faIncrement.get(0).setTotal(sum);
            if (pageable == null) {
                return new PageImpl<>(faIncrement);
            }
            return new PageImpl<>(faIncrement, pageable, total.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, total.longValue());
    }

    @Override
    public List<FAIncrementDetailsConvertDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("fa.id, ");
        sql.append("fa.faIncrementID, ");
        sql.append("fa.fixedAssetID, ");
        sql.append("fix.fixedAssetCode, ");
        sql.append("fix.fixedAssetName, ");
        sql.append("fa.description, ");
        sql.append("fa.amount, ");

        sql.append("fa.AccountingObjectID, ");
        sql.append("a.AccountingObjectCode, ");
        sql.append("fa.BudgetItemID, ");
        sql.append("b.BudgetItemCode, ");
        sql.append("fa.CostSetID, ");
        sql.append("c.CostSetCode, ");
        sql.append("fa.StatisticCodeID, ");
        sql.append("s.StatisticsCode, ");
        sql.append("fa.DepartmentID, ");
        sql.append("eb.OrganizationUnitCode, ");
        sql.append("fa.ExpenseItemID, ");
        sql.append("ex.ExpenseItemCode, ");
        sql.append("fa.OrderPriority ");
        sql.append(" from FAIncrementDetail fa ");

        sql.append(" left join FixedAsset fix on fix.id = fa.fixedAssetID ");
        sql.append(" left join AccountingObject a on a.ID = fa.AccountingObjectID ");
        sql.append(" left join BudgetItem b on b.ID = fa.BudgetItemID ");
        sql.append(" left join CostSet c on c.ID = fa.CostSetID ");
        sql.append(" left join StatisticsCode s on s.ID = fa.StatisticCodeID ");
        sql.append(" left join EbOrganizationUnit eb on eb.ID = fa.DepartmentID ");
        sql.append(" left join ExpenseItem ex on ex.ID = fa.ExpenseItemID ");
        sql.append("where fa.FAIncrementID = :id ");
        sql.append("order by fa.OrderPriority ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "faIncrementDetailsConvertDTO");
        Common.setParams(query, params);
        List<FAIncrementDetailsConvertDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS;
    }

    @Override
    public void updateUnRecord(List<UUID> uuidList) {
        String sql1 = "Update FAIncrement SET Recorded = 0 WHERE id = ?;" +
            "Delete FixedAssetLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public void deleteList(List<UUID> uuidList) {
        String sql1 = "delete FAIncrement WHERE id = ?;" +
            "delete FAIncrementDetailRefVoucher  WHERE FAIncrementID = ?;" +
            "delete FAIncrementDetail  WHERE FAIncrementID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public Optional<FAIncrement> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        FAIncrement faIncrement = null;
        Map<String, Object> params = new HashMap<>();
        sql.append(" from FAIncrement where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        params.put("companyID", org);
        params.put("currentBook", currentBook ? 1 : 0);
        if (!Strings.isNullOrEmpty(keySearch)) {
            if (currentBook) {
                sql.append(" and (NoMBook like :keySearch ");
            } else {
                sql.append(" and (NoFBook like :keySearch ");
            }
            sql.append(" or Reason like :keySearch or TotalAmount like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        StringBuilder sort = new StringBuilder();
        sort.append(" order by Date desc");
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Query countSum = entityManager.createNativeQuery("SELECT sum(TotalAmount) " + sql.toString());
        Common.setParams(countSum, params);
        Number total = (Number) countQuery.getSingleResult();
        BigDecimal sum = (BigDecimal) countSum.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (" + sort.toString() +") rownum "
                + sql.toString() +  ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, FAIncrement.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            faIncrement = (FAIncrement) query.getSingleResult();
        }
        return Optional.of(faIncrement);
    }

    @Override
    public List<FAIncrementDetailRefVoucherConvertDTO> getDataRefVouchers(UUID id, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select tidrv.ID, " +
            "       vv.Date, " +
            "       vv.PostedDate, " +
            "       case :currentBook when 1 then vv.NoMBook else vv.NoFBook end as no, " +
            "       vv.Reason, " +
            "       vv.TotalAmountOriginal, " +
            "       tidrv.FAIncrementID, " +
            "       tidrv.RefVoucherID, " +
            "       tidrv.OrderPriority, " +
            "       vv.typeGroupID " +
            "from FAIncrementDetailRefVoucher tidrv  " +
            "         left join ViewVoucherNo vv on vv.RefID = tidrv.RefVoucherID " +
            "where tidrv.FAIncrementID = :id ");
        params.put("id", id);
        params.put("currentBook", currentBook ? 1 : 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "faIncrementDetailRefVoucherConvertDTO");
        Common.setParams(query, params);
        List<FAIncrementDetailRefVoucherConvertDTO> faIncrementDetailRefVoucherConvertDTOS;
        faIncrementDetailRefVoucherConvertDTOS = (List<FAIncrementDetailRefVoucherConvertDTO>) query.getResultList();
        return faIncrementDetailRefVoucherConvertDTOS;
    }
}
