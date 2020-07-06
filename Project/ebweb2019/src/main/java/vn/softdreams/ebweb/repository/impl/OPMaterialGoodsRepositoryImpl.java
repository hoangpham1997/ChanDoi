package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.OPMaterialGoods;
import vn.softdreams.ebweb.repository.OPMaterialGoodsRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class OPMaterialGoodsRepositoryImpl implements OPMaterialGoodsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<OPMaterialGoodsDTO> findAllByAccountNumberAndCompanyIdAndTypeLedger(String accountNumber, UUID companyID, UUID repositoryId, Integer typeLedger) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select oa.id, ");
        sqlBuilder.append("oa.companyId, ");
        sqlBuilder.append("oa.typeId, ");
        sqlBuilder.append("oa.postedDate, ");
        sqlBuilder.append("oa.typeLedger, ");
        sqlBuilder.append("oa.accountNumber, ");
//        sqlBuilder.append("oa.accountName, ");
        sqlBuilder.append("oa.materialGoodsId, ");
        sqlBuilder.append("mg.materialGoodsName, ");
        sqlBuilder.append("mg.materialGoodsCode, ");
        sqlBuilder.append("oa.currencyId, ");
        sqlBuilder.append("oa.exchangeRate, ");
        sqlBuilder.append("oa.unitId, ");
        sqlBuilder.append("u.UnitName as unitName, ");
        sqlBuilder.append("oa.quantity, ");
        sqlBuilder.append("oa.unitPrice, ");
        sqlBuilder.append("oa.unitPriceOriginal, ");
        sqlBuilder.append("oa.mainUnitId, ");
        sqlBuilder.append("um.UnitName as mainUnitName, ");
        sqlBuilder.append("oa.mainQuantity, ");
        sqlBuilder.append("oa.mainUnitPrice, ");
        sqlBuilder.append("oa.mainConvertRate, ");
        sqlBuilder.append("oa.formula, ");
        sqlBuilder.append("oa.amount, ");
        sqlBuilder.append("oa.amountOriginal, ");
        sqlBuilder.append("oa.lotNo, ");
        sqlBuilder.append("oa.expiryDate, ");
        sqlBuilder.append("convert(varchar, oa.expiryDate, 103) as expiryDateStr, ");
        sqlBuilder.append("oa.repositoryId, ");
        sqlBuilder.append("r.repositoryCode, ");
        sqlBuilder.append("oa.bankAccountDetailId, ");
        sqlBuilder.append("bad.bankAccount, ");
        sqlBuilder.append("oa.contractId, ");
        sqlBuilder.append("IIF(oa.TypeLedger = 0, emc.NoFBook, emc.NoMBook) as noBookContract, ");
        sqlBuilder.append("oa.costSetId, ");
        sqlBuilder.append("cs.costSetCode, ");
        sqlBuilder.append("oa.expenseItemId, ");
        sqlBuilder.append("ei.expenseItemCode, ");
        sqlBuilder.append("oa.departmentId, ");
        sqlBuilder.append("ou.organizationUnitCode, ");
        sqlBuilder.append("oa.statisticsCodeId, ");
        sqlBuilder.append("sc.statisticsCode, ");
        sqlBuilder.append("oa.budgetItemId, ");
        sqlBuilder.append("bi.budgetItemCode, ");
        sqlBuilder.append("oa.orderPriority ");
        sqlBuilder.append("from OPMaterialGoods oa ");
        sqlBuilder.append("left join MaterialGoods mg on oa.MaterialGoodsID = mg.id ");
        sqlBuilder.append("left join Unit u on u.id = oa.UnitID ");
        sqlBuilder.append("left join Unit um on um.id = oa.MainUnitID ");
        sqlBuilder.append("left join Repository r on r.id = oa.repositoryId ");
        sqlBuilder.append("left join BankAccountDetail bad on bad.id = oa.BankAccountDetailID ");
        sqlBuilder.append("left join EMContract emc on emc.id = oa.ContractID ");
        sqlBuilder.append("left join CostSet cs on cs.id = oa.CostSetID ");
        sqlBuilder.append("left join ExpenseItem ei on ei.id = oa.ExpenseItemID ");
        sqlBuilder.append("left join EbOrganizationUnit ou on oa.DepartmentID = ou.ID ");
        sqlBuilder.append("left join StatisticsCode sc on sc.id = oa.StatisticsCodeID ");
        sqlBuilder.append("left join BudgetItem bi on oa.BudgetItemID = bi.ID ");
        sqlBuilder.append("where oa.CompanyID = :companyID ");
        sqlBuilder.append("and oa.accountNumber = :accountNumber ");
        sqlBuilder.append("and oa.typeLedger = :typeLedger ");
        params.put("companyID", companyID);
        params.put("accountNumber", accountNumber);
        params.put("typeLedger", typeLedger);
        if (repositoryId != null) {
            sqlBuilder.append("and oa.RepositoryID = :repositoryId ");
            params.put("repositoryId", repositoryId);
        }
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "OPMaterialGoodsDTO");
        Utils.setParams(query, params);
        return (List<OPMaterialGoodsDTO>) query.getResultList();
    }

    @Override
    public List<OPMaterialGoods> insertBulk(List<OPMaterialGoods> opMaterialGoods) {
        final List<OPMaterialGoods> savedEntities = new ArrayList<>(opMaterialGoods.size());
        int i = 0;
        for (OPMaterialGoods t : opMaterialGoods) {
            if (save(entityManager, t) == 1)
                savedEntities.add(t);
            i++;
            if (i > Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;
    }

    private int save(EntityManager em, OPMaterialGoods opMaterialGoods) {
        if (opMaterialGoods.getId() != null) {
            em.merge(opMaterialGoods);
        } else {
            em.persist(opMaterialGoods);
        }
        return 1;
    }
}
