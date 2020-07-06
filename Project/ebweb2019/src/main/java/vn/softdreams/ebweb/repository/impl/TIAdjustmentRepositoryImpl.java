package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.TIAdjustmentRepositoryCustom;
import vn.softdreams.ebweb.service.dto.Report.TIAdjustmentDTO;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailAllConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class TIAdjustmentRepositoryImpl implements TIAdjustmentRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<TIAdjustmentDTO> getListTIAdjustmentDTO(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select" +
            " t.ToolCode ," +
            " t.ToolName ," +
            " td.Quantity," +
            " td.RemainingAmount , " +
            " td.NewRemainingAmount , " +
            " td.DiffRemainingAmount , " +
            " td.RemainAllocationTimes , " +
            " td.NewRemainingAllocationTime , " +
            " td.DifferAllocationTime " +
            " from Tools t " +
            " inner join TIAdjustmentDetail td on t.ID = td.ToolsID " +
            " inner join TIAdjustment ta on td.TIAdjustmentID = ta.ID " +
            " where ta.ID = :id"  + " order by td.OrderPriority");
        params.put("id" , id);
        List<TIAdjustmentDTO>tiAdjustmentDTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "TIAdjustmentDTOList");
        Common.setParams(query, params);
        tiAdjustmentDTOList = query.getResultList();
        return tiAdjustmentDTOList;
    }

    @Override
    public Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TITransferAndTIAdjustmentConvertDTO> tiTransferAndTIAdjustmentConvertDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIAdjustment t where t.CompanyID = :companyID  and t.TypeLedger in (:currentBook, 2) ");
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        params.put("companyID", org);
        params.put("currentBook", currentBook ? 1 : 0);
        if (keySearch != null) {
            if (currentBook) {
                sql.append(" and (t.NoMBook like :keySearch ");
            } else {
                sql.append(" and (t.NoFBook like :keySearch ");
            }
            sql.append(" or t.Reason like :keySearch or t.totalAmount like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            sql.append( " order by t.date DESC, NoBook DESC");
            Query query = entityManager.createNativeQuery("select t.id, " +
                "       t.TypeID, " +
                "       t.TypeLedger, " +
                "       t.NoFBook, " +
                "       t.NoMBook, " +
                "       case :currentBook " +
                "           when 1 then t.NoMBook " +
                "           else t.NoFBook end noBook, " +
                "       t.date, " +
                "       t.Reason, " +
                "       t.recorded, " +
                "       t.TotalAmount totalAmount, " +
                "       (select sum(diffRemainingAmount) from TIAdjustmentDetail where TIAdjustmentID = t.ID) diffRemainingAmount, " +
                "        (select sum(differAllocationTime) from TIAdjustmentDetail where TIAdjustmentID = t.ID) differAllocationTime " +
                sql.toString(), "TITransferAndTIAdjustmentConvertDTO2");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiTransferAndTIAdjustmentConvertDTOS = query.getResultList();
        }
        return new PageImpl<>(tiTransferAndTIAdjustmentConvertDTOS, pageable, total.longValue());
    }

    @Override
    public List<TIAdjustmentDetailConvertDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<TIAdjustmentDetailConvertDTO> tools = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ta.id, " +
            "       ta.tiadjustmentid, " +
            "       ta.toolsid, " +
            "       ts.ToolName toolsName, " +
            "       ts.ToolCode toolsCode, " +
            "       ta.description, " +
            "       ta.quantity, " +
            "       ta.remainingamount, " +
            "       ta.newremainingamount, " +
            "       ta.diffremainingamount, " +
            "       ta.remainallocationtimes, " +
            "       ta.newremainingallocationtime, " +
            "       ta.differallocationtime, " +
            "       ta.allocatedamount, " +
            "       ta.orderpriority " +
            "from TIAdjustmentDetail ta " +
            "         left join tools ts on ts.ID = ta.ToolsID " +
            "where TIAdjustmentID = :id " +
            "order by OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAdjustmentDetailConvertDTO");
        Common.setParams(query, params);
        tools = query.getResultList();
        return tools;
    }
}
