package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class GOtherVoucherDetailsRepositoryImpl implements GOtherVoucherDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<GOtherVoucherDetailDTO> getGOtherVoucherViewID(UUID id, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailDTO> gOtherVoucherDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> paramsCount = new HashMap<>();
        sql.append("SELECT gvdtl.ID, " +
            "       gvdtl.GOtherVoucherID, " +
            "       gvdtl.Description, " +
            "       gvdtl.DebitAccount, " +
            "       gvdtl.CreditAccount, " +
            "       gvdtl.Amount, " +
            "       gvdtl.AmountOriginal, " +
            "       gvdtl.BudgetItemID, " +
            "       gvdtl.CostSetID, " +
            "       gvdtl.ContractID, " +
            "       gvdtl.DebitAccountingObjectID, " +
            "       gvdtl.CreditAccountingObjectID, " +
            "       gvdtl.EmployeeID, " +
            "       gvdtl.StatisticsCodeID, " +
            "       gvdtl.DepartmentID, " +
            "       gvdtl.BankAccountDetailID, " +
            "       gvdtl.ExpenseItemID, " +
            "       gvdtl.CashOutAmountVoucherOriginal, " +
            "       gvdtl.CashOutAmountVoucher, " +
            "       gvdtl.CashOutExchangeRateFB, " +
            "       gvdtl.CashOutAmountFB, " +
            "       gvdtl.CashOutDifferAmountFB, " +
            "       gvdtl.CashOutDifferAccountFB, " +
            "       gvdtl.CashOutExchangeRateMB, " +
            "       gvdtl.CashOutAmountMB, " +
            "       gvdtl.CashOutDifferAmountMB, " +
            "       gvdtl.CashOutDifferAccountMB, " +
            "       gvdtl.IsMatch, " +
            "       gvdtl.MatchDate, " +
            "       gvdtl.OrderPriority, " +
            "       bg.BudgetItemCode, " +
            "       cs.CostSetCode, " +
            "       acd.AccountingObjectCode DebitAccountingObjectCode, " +
            "       acc.AccountingObjectCode creditAccountingObjectCode, " +
            "       ace.AccountingObjectCode EmployeeCode, " +
            "       st.StatisticsCode StatisticsCodeCode, " +
            "       dp.organizationUnitCode DepartmentCode, " +
            "       ex.ExpenseItemCode ExpenseItemCode, " +
            "       case :typeLedger when 0 then ct.NoFBook when 1 then ct.NoMBook end contractCode " +
            " FROM GOtherVoucherDetail gvdtl " +
            "         left join GOtherVoucher gv on gvdtl.GOtherVoucherID = gv.ID " +
            "         left join BudgetItem bg on bg.ID = gvdtl.BudgetItemID " +
            "        left join CostSet cs on cs.id = gvdtl.CostSetID " +
            "        left join AccountingObject acd on acd.id = gvdtl.DebitAccountingObjectID " +
            "        left join AccountingObject acc on acc.id = gvdtl.creditAccountingObjectID " +
            "        left join AccountingObject ace on ace.id = gvdtl.EmployeeID " +
            "        left join StatisticsCode st on st.id = gvdtl.StatisticsCodeID " +
            "        left join EbOrganizationUnit dp on dp.id = gvdtl.DepartmentID " +
            "        left join ExpenseItem ex on ex.id = gvdtl.ExpenseItemID " +
            "        left join EMContract ct on ct.id = gvdtl.ContractID " +
            " where gvdtl.GOtherVoucherID = :gOtherVoucherID order by gvdtl.OrderPriority ");
        params.put("gOtherVoucherID", id);
        paramsCount.put("gOtherVoucherID", id);
        params.put("typeLedger", currentBook);
        Query countQuerry = entityManager.createNativeQuery("select count(1) from GOtherVoucherDetail where GOtherVoucherID = :gOtherVoucherID" );
        Common.setParams(countQuerry, paramsCount);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailDTO");
            Common.setParams(query, params);
            gOtherVoucherDTOS = query.getResultList();
        }
        return gOtherVoucherDTOS;
    }

}
