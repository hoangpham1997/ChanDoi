package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.repository.TIIncrementRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailRefVoucherConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class TIIncrementRepositoryImpl implements TIIncrementRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<TIIncrementConvertDTO> getAllTIIncrementsSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TIIncrementConvertDTO> tiIncrement = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIIncrement where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        params.put("companyID", org);
        params.put("currentBook", currentBook ? 1 : 0);
        if (keySearch != null && keySearch.trim().length() > 0) {
            if (currentBook) {
                sql.append(" and (NoMBook like :keySearch ");
            } else {
                sql.append(" and (NoFBook like :keySearch ");
            }
            sql.append(" or Reason like :keySearch or TotalAmount like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Query countSum = entityManager.createNativeQuery("SELECT sum(TotalAmount) " + sql.toString());
        Common.setParams(countSum, params);
        Number total = (Number) countQuery.getSingleResult();
        BigDecimal sum = (BigDecimal) countSum.getSingleResult();
        if (total.longValue() > 0) {
            sql.append( " order by date DESC, NoBook DESC");
            Query query = entityManager.createNativeQuery("SELECT id, Date, TypeLedger, NofBook, NoMBook, case :currentBook" +
                " when 1 then NoMBook else NoFBook end noBook , Reason, TotalAmount, " +
                "TemplateID, Recorded " + sql.toString(), "TIIncrementConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiIncrement = query.getResultList();
            tiIncrement.get(0).setTotal(sum);
        }
        return new PageImpl<>(tiIncrement, pageable, total.longValue());
    }

    @Override
    public List<TIIncrementDetailsConvertDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ttrdlt.ID, " +
            "       ttrdlt.TIIncrementID, " +
            "       ttrdlt.ToolsID, " +
            "       tl.ToolCode, " +
            "       tl.ToolName, " +
            "       ttrdlt.Description, " +
            "       ttrdlt.UnitID, " +
            "       u.UnitName, " +
            "       ttrdlt.Quantity, " +
            "       ttrdlt.UnitPrice, " +
            "       ttrdlt.Amount, " +
            "       ttrdlt.AccountingObjectID, " +
            "       a.AccountingObjectCode, " +
            "       ttrdlt.BudgetItemID, " +
            "       b.BudgetItemCode, " +
            "       ttrdlt.CostSetID, " +
            "       c.CostSetCode, " +
            "       ttrdlt.StatisticCodeID, " +
            "       s.StatisticsCode, " +
            "       ttrdlt.DepartmentID, " +
            "       eb.OrganizationUnitCode, " +
            "       ttrdlt.ExpenseItemID, " +
            "       ex.ExpenseItemCode, " +
            "       ttrdlt.OrderPriority " +
            "from TIIncrementDetail ttrdlt " +
            "         left join Tools tl on tl.id = ttrdlt.ToolsID " +
            "         left join unit u on u.ID = ttrdlt.UnitID " +
            "         left join AccountingObject a on a.ID = ttrdlt.AccountingObjectID " +
            "         left join BudgetItem b on b.ID = ttrdlt.BudgetItemID " +
            "         left join CostSet c on c.ID = ttrdlt.CostSetID " +
            "         left join StatisticsCode s on s.ID = ttrdlt.StatisticCodeID " +
            "         left join EbOrganizationUnit eb on eb.ID = ttrdlt.DepartmentID " +
            "         left join ExpenseItem ex on ex.ID = ttrdlt.ExpenseItemID " +
            "where ttrdlt.TIIncrementID = :id " +
            "order by ttrdlt.OrderPriority");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "TIIncrementDetailsConvertDTO");
        Common.setParams(query, params);
        List<TIIncrementDetailsConvertDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS;
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update TIIncrement SET Recorded = 0 WHERE id = ?;"+
            "Delete ToolLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public void deleteList(List<UUID> uuidList) {
        String sql1 = "delete TIIncrement WHERE id = ?;"+
            "delete TIIncrementDetailRefVoucher  WHERE TIIncrementID = ?;" +
            "delete TIIncrementDetail  WHERE TIIncrementID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public Optional<TIIncrement> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        TIIncrement tiIncrement = null;
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIIncrement where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
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
            Query query = entityManager.createNativeQuery(newSql, TIIncrement.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            tiIncrement = (TIIncrement) query.getSingleResult();
        }
        return Optional.of(tiIncrement);
    }

    @Override
    public List<TIIncrementDetailRefVoucherConvertDTO> getDataRefVouchers(UUID id, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select tidrv.ID, " +
            "       vv.Date, " +
            "       vv.PostedDate, " +
            "       case :currentBook when 1 then vv.NoMBook else vv.NoFBook end as no, " +
            "       vv.Reason, " +
            "       vv.TotalAmountOriginal, " +
            "       tidrv.TIIncrementID, " +
            "       tidrv.RefVoucherID, " +
            "       tidrv.OrderPriority, " +
            "       vv.typeGroupID " +
            "from TIIncrementDetailRefVoucher tidrv  " +
            "         left join ViewVoucherNo vv on vv.RefID = tidrv.RefVoucherID " +
            "where tidrv.TIIncrementID = :id ");
        params.put("id", id);
        params.put("currentBook", currentBook ? 1 : 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "TIIncrementDetailRefVoucherConvertDTO");
        Common.setParams(query, params);
        List<TIIncrementDetailRefVoucherConvertDTO> tiIncrementDetailRefVoucherConvertDTOS;
        tiIncrementDetailRefVoucherConvertDTOS = (List<TIIncrementDetailRefVoucherConvertDTO>) query.getResultList();
        return tiIncrementDetailRefVoucherConvertDTOS;
    }
}
