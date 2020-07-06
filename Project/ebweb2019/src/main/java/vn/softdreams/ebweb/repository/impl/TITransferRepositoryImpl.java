package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.TITransferRepositoryCustom;
import vn.softdreams.ebweb.service.dto.Report.TITransferDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class TITransferRepositoryImpl implements TITransferRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<TITransferDTO> getDetailsTITransferDTO(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select" +
            " t.ToolCode ," +
            " t.ToolName ," +
            " EOU.OrganizationUnitCode  toDepartmentID," +
            " EOU1.OrganizationUnitCode  fromDepartmentID, " +
            " tid.Quantity ," +
            " tid.TransferQuantity " +
            " from Tools t " +
            " left join TITransferDetail tid on t.ID = tid.ToolsID " +
            " left join TITransfer tt on tt.ID = tid.TITransferID " +
            " left join EbOrganizationUnit EOU1 on tid.FromDepartmentID = EOU1.ID " +
            "  left join EbOrganizationUnit EOU on tid.ToDepartmentID = EOU.ID " +
            " where tt.ID = :id" + " order by tid.OrderPriority");
        params.put("id", id);
        List<TITransferDTO> tiTransferDTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "TITransferDTOList");
        Common.setParams(query, params);
        tiTransferDTOList = query.getResultList();
        return tiTransferDTOList;
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByTITransfer(UUID org, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.id,  " +
            "       ts.declaretype,  " +
            "       ts.posteddate,  " +
            "       ts.toolcode toolsCode,  " +
            "       ts.toolname toolsName,  " +
            "       ts.unitid,  " +
            "       ts.quantity,  " +
            "       ts.unitprice,  " +
            "       ts.allocationAwaitAccount  " +
            "from Tools ts  " +
            "where ts.IsActive = 1  " +
            "  and ts.CompanyID = :companyID  " +
            "  and ts.TypeLedger in (:currentBook, 2)  " +
            "  and COALESCE((select count(*) from ToolLedger tl where tl.TypeID = 530 and tl.ToolsID = ts.ID), 0) <= 0 " +
            "    AND (ts.DeclareType = 1 or ((ts.DeclareType = 0 or ts.DeclareType is not null) and (COALESCE((select count(*) " +
            "                                                                                                from ToolLedger tl1 " +
            "                                                                                                where tl1.TypeID = 520 " +
            "                                                                                                  and tl1.ToolsID = ts.ID), " +
            "                                                                                               0) > 0)))");
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        Query query = entityManager.createNativeQuery(sql.toString(), "ToolsConvertDTO");
        Common.setParams(query, params);
        List<ToolsConvertDTO> toolsConvertDTOS = query.getResultList();
        return toolsConvertDTOS;
    }

    @Override
    public Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TITransferAndTIAdjustmentConvertDTO> tiTransferAndTIAdjustmentConvertDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TITransfer where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
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
            sql.append(" or Reason like :keySearch or TotalQuantity like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            sql.append( " order by date DESC, NoBook DESC");
            Query query = entityManager.createNativeQuery("select id, " +
                "       TypeID, " +
                "       TypeLedger, " +
                "       NoFBook, " +
                "       NoMBook, " +
                "       case :currentBook " +
                "           when 1 then NoMBook " +
                "           else NoFBook end noBook, " +
                "       date, " +
                "       Reason, " +
                "        Recorded, " +
                "       TotalQuantity totalAmount " + sql.toString(), "TITransferAndTIAdjustmentConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiTransferAndTIAdjustmentConvertDTOS = query.getResultList();
        }
        return new PageImpl<>(tiTransferAndTIAdjustmentConvertDTOS, pageable, total.longValue());
    }

    @Override
    public List<TITransferDetailConvertDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select tt.id, " +
            "       tt.titransferid, " +
            "       tt.toolsid, " +
            "       tt.description, " +
            "       tt.fromdepartmentid, " +
            "       tt.todepartmentid, " +
            "       tt.quantity, " +
            "       tt.transferquantity, " +
            "       tt.costaccount, " +
            "       tt.budgetitemid, " +
            "       tt.costsetid, " +
            "       tt.statisticcodeid, " +
            "       tt.expenseitemid, " +
            "       tt.orderpriority, " +
            "       t.ToolCode                toolsCode, " +
            "       t.ToolName                toolsName, " +
            "       EOU.OrganizationUnitCode  fromdepartmentCode, " +
            "       EOU1.OrganizationUnitCode todepartmentCode, " +
            "       b.BudgetItemCode, " +
            "       c.CostSetCode, " +
            "       s.StatisticsCode, " +
            "       e.ExpenseItemCode " +
            "from TITransferDetail tt " +
            "         left join Tools t on t.id = tt.ToolsID " +
            "         left join EbOrganizationUnit EOU on tt.FromDepartmentID = EOU.id " +
            "         left join EbOrganizationUnit EOU1 on tt.ToDepartmentID = EOU1.id " +
            "         left join BudgetItem b on b.ID = tt.BudgetItemID " +
            "         left join CostSet c on c.id = tt.CostSetID " +
            "         left join StatisticsCode s on s.id = tt.StatisticCodeID " +
            "         left join ExpenseItem e on e.id = tt.ExpenseItemID " +
            "where tt.TITransferID = :id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "TITransferDetailConvertDTO");
        Common.setParams(query, params);
        List<TITransferDetailConvertDTO> toolsConvertDTOS = query.getResultList();
        return toolsConvertDTOS;

    }
}
