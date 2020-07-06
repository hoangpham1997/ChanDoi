package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.TIAudit;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.repository.TIAuditRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class TIAuditRepositoryImpl implements TIAuditRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Override
//    public List<ToolsDetailsTiAuditConvertDTO> getTIAuditDetails(UUID org, String date, String checkPeriodic) {
//        StringBuilder sql = new StringBuilder();
//        List<ToolsDetailsTiAuditConvertDTO> tools = new ArrayList<>();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("select ts.id toolsID, " +
//            "       ts.ToolName toolsName, " +
//            "       ts.ToolCode toolsCode, " +
//            "       ts.UnitID unitID, " +
//            "       tsdtl.ObjectID departmentID, " +
//            "       tsdtl.ExpenseItemID, " +
//            "       tsdtl.StatisticsCodeID, " +
//            "       tsdtl.Quantity, " +
//            "       tsdtl.CostAccount " +
//            "from ToolsDetail tsdtl " +
//            " left join tools ts on tsdtl.ToolsID = ts.ID " +
//            "where COALESCE((select count(*) " +
//            "                from ToolLedger tl " +
//            "                where tl.TypeID = 530 " +
//            "                  and tl.ToolsID = ts.ID " +
//            "                  and (convert(nvarchar, tl.PostedDate, 112) <= convert(nvarchar, :currentDate, 112))), 0) <= 0 " +
//            "  and ((ts.DeclareType = 1 and  (convert(nvarchar, PostedDate, 112) <= convert(nvarchar, :currentDate, 112))) OR ((ts.DeclareType = 0 OR ts.DeclareType is null) AND (COALESCE((select count(*) " +
//            "                                                                                            from ToolLedger tl1 " +
//            "                                                                                            where tl1.TypeID = 520 " +
//            "                                                                                              and (convert(nvarchar, tl1.PostedDate, 112) <= " +
//            "                                                                                                   convert(nvarchar, :currentDate, 112)) " +
//            "                                                                                              and tl1.ToolsID = ts.ID), " +
//            "                                                                                           0) > 0))) " +
//            "  and ts.CompanyID = :companyID ");
//        params.put("companyID", org);
//        params.put("currentDate", date);
////        params.put("checkPeriodic", checkPeriodic);
////        int numberDate = Common.getNumberOfDayInMonth(month, year);
////        params.put("numberDate", numberDate);
////        String monthConvert = month < 10 ? "0" + month : month.toString();
////        params.put("currentDate", year + monthConvert + (date != null ? date :numberDate));
//        Query query = entityManager.createNativeQuery( sql.toString(), "ToolsDetailsTiAuditConvertDTO");
//        Common.setParams(query, params);
//        tools = query.getResultList();
//        return tools;
//    }

    @Override
    public List<ToolsDetailsTiAuditConvertDTO> getTIAuditDetails(UUID org, String date, String checkPeriodic, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<ToolsDetailsTiAuditConvertDTO> tools = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select b.toolsID,  " +
            "       b.toolsCode,  " +
            "       b.toolsName,  " +
            "       b.UnitID,  " +
            "       b.DepartmentID,  " +
            "       b.ExpenseItemID,  " +
            "       b.StatisticsCodeID,  " +
            "       b.quantityRest quantity,  " +
            "       b.CostAccount  " +
            "from (  " +
            "         select a.toolsID,  " +
            "                a.toolsCode,  " +
            "                a.toolsName,  " +
            "                a.UnitID,  " +
            "                a.DepartmentID,  " +
            "                a.ExpenseItemID,  " +
            "                a.StatisticsCodeID,  " +
            "                case a.DeclareType  " +
            "                    when 1 then  " +
            "                        (a.soluongdaghitang + a.Quantity - a.soluongdieuchuyentu)  " +
            "                    else (a.soluongdaghitang -  " +
            "                          a.soluongdieuchuyentu +  " +
            "                          a.soluongdieuchuyenden) end quantityRest,  " +
            "                a.CostAccount,  " +
            "                a.OrganizationUnitCode  " +
            "         from (select eb.OrganizationUnitCode,  " +
            "                      t.DeclareType,  " +
            "                      t.id                                     toolsID,  " +
            "                      t.ToolCode                               toolsCode,  " +
            "                      t.ToolName                               toolsName,  " +
            "                      t.UnitID,  " +
            "                      tl.DepartmentID,  " +
            "                      ts.ExpenseItemID,  " +
            "                      ts.StatisticsCodeID,  " +
            "                      ts.CostAccount,  " +
            "                      (select sum(tld.quantity)  " +
            "                       from ToolsDetail tld  " +
            "                       where tld.ToolsID = tl.ToolsID  " +
            "                         and tld.ObjectID = tl.DepartmentID)   Quantity,  " +
            "                      ts.id                                    iddetal,  " +
            "                      COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0))  " +
            "                                from ToolLedger tl2  " +
            "                                where tl2.TypeLedger in (:currentBook, 2)  " +
            "                                  and tl.DepartmentID = tl2.DepartmentID  " +
            "                                  and tl.ToolsID = tl2.ToolsID  " +
            "                                  and convert(nvarchar, tl2.PostedDate, 112) <= convert(nvarchar, :postDate, 112)  " +
            "                                  and tl2.TypeID = 520  " +
            "                                group by tl2.DepartmentID), 0) soluongdaghitang,  " +
            "                      COALESCE((select sum(COALESCE(tl2.DecrementQuantity, 0))  " +
            "                                from ToolLedger tl2  " +
            "                                where tl2.TypeLedger in (:currentBook, 2)  " +
            "                                  and tl.DepartmentID = tl2.DepartmentID  " +
            "                                  and tl.ToolsID = tl2.ToolsID  " +
            "                                  and convert(nvarchar, tl2.PostedDate, 112) <=  " +
            "                                      convert(nvarchar, :postDate, 112)  " +
            "                                  and tl2.TypeID in (530, 540, 570)  " +
            "                                group by tl2.DepartmentID), 0) soluongdieuchuyentu,  " +
            "                      COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0))  " +
            "                                from ToolLedger tl2  " +
            "                                where tl2.TypeLedger in (:currentBook, 2)  " +
            "                                  and tl.DepartmentID = tl2.DepartmentID  " +
            "                                  and tl.ToolsID = tl2.ToolsID  " +
            "                                  and convert(nvarchar, tl2.PostedDate, 112) <= convert(nvarchar, :postDate, 112)  " +
            "                                  and tl2.TypeID in (530, 540, 570)  " +
            "                                group by tl2.DepartmentID), 0) soluongdieuchuyenden  " +
            "  " +
            "               from ToolLedger tl  " +
            "                        left join EbOrganizationUnit eb on eb.id = tl.DepartmentID  " +
            "                        left join ToolsDetail ts on ts.id = tl.ReferenceID  " +
            "                        left join Tools t on t.ID = tl.ToolsID  " +
            "               where tl.TypeID in (500, 520, 530, 540, 570) AND tl.CompanyID = :companyID and tl.TypeLedger in (:currentBook, 2)  " +
            "               group by eb.OrganizationUnitCode, eb.OrganizationUnitName, tl.DepartmentID, tl.ToolsID, eb.UnitType,  " +
            "                        eb.AccType,  " +
            "                        t.declaretype, ts.id, ts.Quantity, t.ToolCode, t.ToolName, t.id, t.UnitID, ts.ExpenseItemID,  " +
            "                        ts.StatisticsCodeID,  " +
            "                        ts.CostAccount) a) b  " +
            "where b.quantityRest > 0  " +
            "order by b.toolsCode, b.OrganizationUnitCode ");
        params.put("companyID", org);
        params.put("postDate", date);
        params.put("currentBook", currentBook);
//        params.put("checkPeriodic", checkPeriodic);
//        int numberDate = Common.getNumberOfDayInMonth(month, year);
//        params.put("numberDate", numberDate);
//        String monthConvert = month < 10 ? "0" + month : month.toString();
//        params.put("currentDate", year + monthConvert + (date != null ? date :numberDate));
        Query query = entityManager.createNativeQuery( sql.toString(), "ToolsDetailsTiAuditConvertDTO");
        Common.setParams(query, params);
        tools = query.getResultList();
        return tools;
    }

    @Override
    public List<ToolsDetailsTiAuditConvertDTO> getAllToolsByTiAuditID(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<ToolsDetailsTiAuditConvertDTO> tools = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ts.id toolsID, ts.ToolName toolsName, ts.ToolCode toolsCode from Tools ts where ts.id in (select ToolsID from TIAuditDetail where TIAuditID = :id)");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "ToolsItemConvertDTO");
        Common.setParams(query, params);
        tools = query.getResultList();
        return tools;
    }

    @Override
    public List<TIAuditDetailByIDDTO> findDetailsByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<TIAuditDetailByIDDTO> tools = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select " +
            "tadtl.id, " +
            "tadtl.tiAuditID, " +
            "tadtl.toolsID, " +
            "ts.ToolCode toolsCode, " +
            "ts.ToolName toolsName, " +
            "ts.UnitID unitID, " +
            "u.UnitName unitName, " +
            "tadtl.departmentID, " +
            "eo.OrganizationUnitCode departmentCode, " +
            "tadtl.quantityONBook, " +
            "tadtl.quantityInventory, " +
            "tadtl.diffQuantity, " +
            "tadtl.executeQuantity, " +
            "tadtl.recommendation, " +
            "tadtl.note, " +
            "tadtl.orderPriority " +
            "from TIAuditDetail tadtl " +
            "left join Tools ts on ts.id = tadtl.ToolsID " +
            "left join Unit u on u.ID = ts.UnitID " +
            "left join EbOrganizationUnit eo on eo.ID = tadtl.DepartmentID " +
            "where tadtl.TIAuditID = :id order by tadtl.OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAuditDetailByIDDTO");
        Common.setParams(query, params);
        tools = query.getResultList();
        return tools;
    }

    @Override
    public Page<TIAuditConvertDTO> getAllTIAuditSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TIAuditConvertDTO> tiAuditConvertDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIAudit where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
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
            sql.append(" or Description like :keySearch or Summary like :keySearch )");
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
                "       Description, " +
                "       InventoryDate, " +
                "       Summary, " +
                "       TemplateID " + sql.toString(), "TIAuditConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiAuditConvertDTOS = query.getResultList();
        }
        return new PageImpl<>(tiAuditConvertDTOS, pageable, total.longValue());
    }

    @Override
    public Optional<TIAudit> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        TIAudit tiAudit = null;
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIAudit where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
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
            sql.append(" or Description like :keySearch or Summary like :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }
        StringBuilder sort = new StringBuilder();
        sort.append(" order by Date desc");
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (" + sort.toString() +") rownum "
                + sql.toString() +  ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, TIIncrement.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            tiAudit = (TIAudit) query.getSingleResult();
        }
        return Optional.of(tiAudit);
    }

    @Override
    public void deleteList(List<UUID> uuidList) {
        String sql1 = "delete TIAudit WHERE id = ?;"+
            "delete TIAuditDetail  WHERE TIAuditID = ?;" +
            "delete TIAuditMemberDetail  WHERE TIAuditID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update TIAudit SET Recorded = 0 WHERE id = ?;"+
            "Delete ToolLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }
}
