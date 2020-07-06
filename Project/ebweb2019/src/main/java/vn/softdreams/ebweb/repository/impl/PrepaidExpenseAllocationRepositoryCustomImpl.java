package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PrepaidExpense;
import vn.softdreams.ebweb.repository.PrepaidExpenseAllocationRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseAllocationConvertDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class PrepaidExpenseAllocationRepositoryCustomImpl implements PrepaidExpenseAllocationRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<PrepaidExpenseAllocationConvertDTO> findAllByExpenseListItemID(Integer month, Integer year, UUID org, String currentBook, List<UUID> listID) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseAllocationConvertDTO> prepaidExpenseAllocationConvertDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select " +
            "       pr.BranchID, " +
            "       pr.TypeLedger, " +
            "       pr.TypeExpense, " +
            "       pra.PrepaidExpenseID, " +
            "       pr.PrepaidExpenseCode, " +
            "       pr.PrepaidExpenseName, " +
            "       pr.Date, " +
            "       pr.Amount, " +
            "       pr.AllocationAmount, " +
            "       pr.AllocationTime, " +
            "       pr.AllocatedPeriod, " +
            "       pr.AllocatedAmount, " +
            "       pr.AllocationAccount, " +
            "       pr.IsActive, " +
            "       pra.AllocationObjectID, " +
            "       pra.AllocationObjectType, " +
            "       pra.AllocationObjectName, " +
            "       pra.AllocationRate, " +
            "       pra.CostAccount, " +
            "       pra.ExpenseItemID, " +
            "       case " +
            "           when pr.TypeExpense = 0 and pr.Amount <> 0 then pr.Amount " +
            "           when pr.TypeExpense = 1 and (pr.Amount - pr.allocationAmount) <> 0 " +
            "               then pr.Amount - pr.allocationAmount end                                          RemainingAmount, " +
            "       case (select Data from SystemOption where Code = 'TCKHAC_PBChiPhi' and CompanyID = :companyID) " +
            "           when 0 then pr.AllocatedAmount " +
            "           when 1 then (pr.AllocatedAmount / :numberDate) * (:numberDate - DAY(pr.Date) + 1) end AllocationAmountGo, " +
            "       pra.* " +
            "       from PrepaidExpenseAllocation pra left join PrepaidExpense pr on pra.PrepaidExpenseID = pr.ID " +
            "        where  pra.PrepaidExpenseID in :prepaidExpenseID order by pr.PrepaidExpenseCode, pr.date DESC, pra.orderPriority");
        params.put("numberDate", Common.getNumberOfDayInMonth(month, year));
        params.put("companyID", org);
        params.put("prepaidExpenseID", listID);
        Query query = entityManager.createNativeQuery( sqlBuilder.toString(), "PrepaidExpenseAllocationConvertDTO");
        Common.setParams(query, params);
        prepaidExpenseAllocationConvertDTOS = query.getResultList();
        if (prepaidExpenseAllocationConvertDTOS != null && prepaidExpenseAllocationConvertDTOS.size() > 0) {
            return prepaidExpenseAllocationConvertDTOS;
        } else {
            return null;
        }
    }

    @Override
    public Page<PrepaidExpense> getPrepaidExpenseByCurrentBookToModal(Pageable pageable, String fromDate, String toDate, Integer typeExpense, UUID org, boolean currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpense> prepaidExpenses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" from PrepaidExpense where CompanyID = :companyID and TypeLedger = :currentBook and TypeExpense = :typeExpense ");
        params.put("companyID", org);
        params.put("typeExpense", typeExpense);
        params.put("currentBook", currentBook ? 0 : 1);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "date", "date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            sqlBuilder.append("  order by PrepaidExpenseCode ");
            Query query = entityManager.createNativeQuery("SELECT * " +sqlBuilder.toString(), PrepaidExpense.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            prepaidExpenses = query.getResultList();
        }
        return new PageImpl<>(prepaidExpenses, pageable, total.longValue());
    }
}
