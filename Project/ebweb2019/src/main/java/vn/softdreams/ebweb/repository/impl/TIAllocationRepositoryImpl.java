package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.TIAllocation;
import vn.softdreams.ebweb.repository.TIALLocationRespositoryCustom;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.tools.Tool;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class TIAllocationRepositoryImpl implements TIALLocationRespositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

//tham số: companyid, tháng, năm, ngày, loại phân bổ :0: Phân bổ đều hàng kỳ, 1: Phân bổ theo ngày ghi tăng
    @Override
    public List<TIAllocationDetailConvertDTO> getTIAllocationDetails(UUID orgID, Integer month, Integer year, Integer date, String checkPeriodic) {
        StringBuilder sql = new StringBuilder();
        List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS;
        Map<String, Object> params = new HashMap<>();
        sql.append("select ts.id                                                                                    ToolsID, " +
            "       ts.ToolName                                                                              ToolsName, " +
            "       ts.ToolCode                                                                              ToolsCode, " +
            "       ts.PostedDate, " +
            "       ts.AllocationAwaitAccount, " +
            "       case :checkPeriodic " +
            "           when 0 then ts.allocatedAmount " +
            "           else (ts.AllocatedAmount / :numberDate) * (:numberDate - DAY(ts.PostedDate) + 1) end TotalAllocationAmount, " +
            "       (COALESCE(ts.Amount, 0) - (select sum(COALESCE(AllocatedAmount, 0)) " +
            "                                  from ToolLedger " +
            "                                  where TypeID = 560 " +
            "                                    and ToolsID = ts.ID))                                       AllocationAmount, " +
            "       0                                                                                        RemainingAmount " +
            "from tools ts " +
            "where COALESCE((select count(*) " +
            "                from ToolLedger tl " +
            "                where tl.TypeID = 530 " +
            "                  and tl.ToolsID = ts.ID " +
            "                  and (convert(nvarchar, tl.PostedDate, 112) >= convert(nvarchar, :currentDate, 112))), 0) <= 0 " +
            "  and ((ts.DeclareType = 1 and  (convert(nvarchar, PostedDate, 112) <= convert(nvarchar, :currentDate, 112))) OR ((ts.DeclareType = 0 OR ts.DeclareType is null) AND (COALESCE((select count(*) " +
            "                                                                                            from ToolLedger tl1 " +
            "                                                                                            where tl1.TypeID = 520 " +
            "                                                                                              and (convert(nvarchar, tl1.PostedDate, 112) >= " +
            "                                                                                                   convert(nvarchar, :currentDate, 112)) " +
            "                                                                                              and tl1.ToolsID = ts.ID), " +
            "                                                                                           0) > 0))) " +
            "  and ts.CompanyID = :companyID ");
        params.put("companyID", orgID);
        params.put("checkPeriodic", checkPeriodic);
        int numberDate = Common.getNumberOfDayInMonth(month, year);
        params.put("numberDate", numberDate);
        String monthConvert = month < 10 ? "0" + month : month.toString();
        params.put("currentDate", year + monthConvert + (date != null ? date :numberDate));
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAllocationDetailConvertDTO");
        Common.setParams(query, params);
        tiAllocationDetailConvertDTOS = query.getResultList();
        return tiAllocationDetailConvertDTOS;
    }

    @Override
    public List<Tool> getTIAllocations(UUID org, Integer month, Integer year) {
        StringBuilder sql = new StringBuilder();
        List<Tool> tiAllocationDetailConvertDTOS;
        Map<String, Object> params = new HashMap<>();
        sql.append("select * " +
            "from tools ts " +
            "where COALESCE((select count(*) from ToolLedger tl where tl.TypeID = 530 and tl.ToolsID = ts.ID), 0) <= 0 " +
            "  and ((ts.DeclareType = 1 and  (convert(nvarchar, PostedDate, 112) <= convert(nvarchar, :currentDate, 112))) OR ((ts.DeclareType = 0 OR ts.DeclareType is null) AND (COALESCE((select count(*) " +
            "                                                                                            from ToolLedger tl " +
            "                                                                                            where tl.TypeID = 520 " +
            "                                                                                              and tl.ToolsID = ts.ID), " +
            "                                                                                           0) > 0))) " +
            "  and ts.CompanyID = :companyID");
        params.put("companyID", org);
        Query query = entityManager.createNativeQuery( sql.toString(), Tool.class);
        Common.setParams(query, params);
        tiAllocationDetailConvertDTOS = query.getResultList();
        return tiAllocationDetailConvertDTOS;
    }

    @Override
    public Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select COUNT(*)" +
            " from ToolLedger pr" +
            " where" +
            "   pr.TypeLedger in (:currentBook, 2)" +
            "   and pr.CompanyID = :companyID" +
            "   and TypeID = 560" +
            "   and MONTH(pr.Date) = :month" +
            "   and YEAR(pr.Date) = :year ");
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        params.put("month", month);
        params.put("year", year);
        Query countQuery = entityManager.createNativeQuery(sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.longValue();
    }

    @Override
    public TIAllocation getTIAllocationDuplicate(Integer month, Integer year, UUID org, String currentBook) {
            StringBuilder sqlBuilder = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sqlBuilder.append(" select top 1 * from TIAllocation where CompanyID = :companyID  and " +
                "   TypeLedger in (:currentBook, 2) and  MONTH(Date) = :month and YEAR(Date) = :year and TypeID = 560 ");
            params.put("companyID", org);
            params.put("currentBook", currentBook);
            params.put("month", month);
            params.put("year", year);
            Query countQuery = entityManager.createNativeQuery(sqlBuilder.toString(), TIAllocation.class);
            Common.setParams(countQuery, params);
            TIAllocation tiAllocation = (TIAllocation) countQuery.getSingleResult();
            return tiAllocation;
    }

    @Override
    public Page<TIAllocationConvertDTO> getAllTIAllocationSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<TIAllocationConvertDTO> tiAllocationConvertDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from TIAllocation where CompanyID = :companyID  and TypeLedger in (:currentBook, 2) ");
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
            Query query = entityManager.createNativeQuery("SELECT id, Date, PostedDate, TypeLedger, NofBook, NoMBook, case :currentBook" +
                " when 1 then NoMBook else NoFBook end noBook , Reason, TotalAmount, " +
                "TemplateID, Recorded " + sql.toString(), "TIAllocationConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            tiAllocationConvertDTOS = query.getResultList();
            tiAllocationConvertDTOS.get(0).setTotal(sum != null ? sum : BigDecimal.ZERO);
        }
        return new PageImpl<>(tiAllocationConvertDTOS, pageable, total.longValue());
    }
}
