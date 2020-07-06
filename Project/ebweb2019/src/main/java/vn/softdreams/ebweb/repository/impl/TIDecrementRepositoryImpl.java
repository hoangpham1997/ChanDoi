package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.repository.TIDecrementRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class TIDecrementRepositoryImpl implements TIDecrementRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TIDecrementDTO> getDetailsTIDecrementDTO(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select" +
            " t.ToolCode ," +
            " t.ToolName ," +
            " Eou.OrganizationUnitCode ," +
            " tid.Quantity , " +
            " tid.DecrementQuantity ," +
            " tid.RemainingDecrementAmount " +
            " from Tools t " +
            " inner join TIDecrementDetail tid on t.ID = tid.ToolsID " +
            " inner join TIDecrement TD on tid.TIDecrementID = TD.ID " +
            " left join EbOrganizationUnit EOU on tid.DepartmentID = EOU.ID" +
            " where TD.ID = :id"  + " order by tid.OrderPriority");
        params.put("id" , id);
        List<TIDecrementDTO>tiDecrementDTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "TIDecrementDTOList");
        Common.setParams(query, params);
        tiDecrementDTOList = query.getResultList();
        return tiDecrementDTOList;
    }

    @Override
    public Page<TIDecrementConvertDTO> getAllTIDecrementSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TIDecrementConvertDTO> tiIncrement = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIDecrement where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        params.put("companyID", org);
        params.put("currentBook", currentBook ? 1 : 0);
        if (keySearch != null || keySearch.trim().length() > 0) {
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
                "TemplateID, Recorded " + sql.toString(), "TIDecrementConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiIncrement = query.getResultList();
            tiIncrement.get(0).setTotal(sum);
        }
        return new PageImpl<>(tiIncrement, pageable, total.longValue());
    }

    @Override
    public List<TIDecrementDetailsConvertDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select td.id,  " +
            "       td.tidecrementid,  " +
            "       td.toolsid,  " +
            "       ts.ToolCode              toolsCode,  " +
            "       ts.ToolName              toolsName,  " +
            "       td.description,  " +
            "       td.departmentid,  " +
            "       EOU.OrganizationUnitCode departmentCode,  " +
            "       td.quantity,  " +
            "       td.decrementquantity,  " +
            "       td.amount,  " +
            "       td.remainingdecrementamount,  " +
            "       td.tiauditid,  " +
            "       td.orderpriority  " +
            "from TIDecrementDetail td  " +
            "         left join Tools ts on td.ToolsID = ts.id  " +
            "         left join EbOrganizationUnit EOU on td.DepartmentID = EOU.id where TIDecrementID = :id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "TIDecrementDetailsConvertDTO");
        Common.setParams(query, params);
        List<TIDecrementDetailsConvertDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS;

    }

    @Override
    public void deleteList(List<UUID> uuidList) {
        String sql1 = "delete TIDecrement WHERE id = ?;"+
            "delete TIDecrementDetail  WHERE TIDecrementID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update TIDecrement SET Recorded = 0 WHERE id = ?;"+
            "Delete ToolLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }
}
