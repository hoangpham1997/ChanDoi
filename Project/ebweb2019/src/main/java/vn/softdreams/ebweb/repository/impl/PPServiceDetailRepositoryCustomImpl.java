package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.repository.PPServiceDetailRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class PPServiceDetailRepositoryCustomImpl implements PPServiceDetailRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PPServiceDetailDTO> findAllPPServiceDetailByPPServiceId(UUID ppServiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("ppServiceId", ppServiceId);
        StringBuilder sql = new StringBuilder();
        sql.append("from PPServiceDetail ppd ");
        sql.append("left join MaterialGoods mg on mg.ID = ppd.MaterialGoodsID ");
        sql.append("left join AccountingObject ao on ao.id = ppd.AccountingObjectID ");
        sql.append("left join Unit u on u.id = ppd.UnitID ");
        sql.append("left join PPOrder ppo on ppo.id = ppd.PPOrderID ");
        sql.append("left join GoodsServicePurchase gsp on gsp.id = ppd.GoodsServicePurchaseID ");
        sql.append("left join PPService pps on ppd.PPServiceID = pps.ID ");
        sql.append("left join ExpenseItem ei on ei.id = ppd.ExpenseItemID ");
        sql.append("left join CostSet cs on cs.id = ppd.CostSetID ");
        sql.append("left join EMContract emc on emc.ID = ppd.ContractID ");
        sql.append("left join BudgetItem bi on bi.id = ppd.BudgetItemID ");
        sql.append("left join EBOrganizationUnit ou on ou.ID = ppd.DepartmentID ");
        sql.append("left join StatisticsCode sc on sc.ID = ppd.StatisticsCodeID ");
        sql.append("where ppd.ppServiceId = :ppServiceId ORDER BY ppd.OrderPriority");

        StringBuilder selectSql = new StringBuilder();
        selectSql.append("ppd.id as id, ");
        selectSql.append("mg.id as materialGoodsId, ");
        selectSql.append("mg.MaterialGoodsCode as materialGoodsCode, ");
        selectSql.append("ppd.Description as materialGoodsName, ");
        selectSql.append("ppd.DebitAccount as debitAccount, ");
        selectSql.append("ppd.CreditAccount as creditAccount, ");
        selectSql.append("ao.id as postedObjectId, ");
        selectSql.append("ao.AccountingObjectCode as postedObjectCode, ");
        selectSql.append("ao.AccountingObjectName as postedObjectName, ");
        selectSql.append("u.id as unitId, ");
        selectSql.append("u.UnitName as unitName, ");
        selectSql.append("CONVERT(varchar, ppd.Quantity) as quantity, ");
        selectSql.append("ppd.UnitPrice as unitPrice, ");
        selectSql.append("ppd.Amount as amount, ");
        selectSql.append("ppd.DiscountRate as discountRate, ");
        selectSql.append("ppd.DiscountAmount as discountAmount, ");
        selectSql.append("ppd.DiscountAccount as discountAccount, ");
        selectSql.append("ppd.PPOrderID as ppOrderId, ");
        selectSql.append("ppd.PPOrderDetailID as ppOrderDetailId, ");
        selectSql.append("ppo.No as ppOrderNo, ");
        selectSql.append("ppd.UnitPriceOriginal as unitPriceOriginal, ");
        selectSql.append("ppd.AmountOriginal as amountOriginal, ");
        selectSql.append("ppd.DiscountAmountOriginal as discountAmountOriginal, ");
        selectSql.append("ppd.VATDescription as vatDescription, ");
        selectSql.append("ppd.VATRate as vatRate, ");
        selectSql.append("ppd.VATAmount as vatAmount, ");
        selectSql.append("ppd.VATAccount as vatAccount, ");
        selectSql.append("ppd.DeductionDebitAccount as deductionDebitAccount, ");
        selectSql.append("ppd.InvoiceTemplate as invoiceTemplate, ");
        selectSql.append("ppd.InvoiceSeries as invoiceSeries, ");
        selectSql.append("ppd.InvoiceNo as invoiceNo, ");
        selectSql.append("convert(varchar, ppd.InvoiceDate, 103) as invoiceDate, ");
        selectSql.append("gsp.id as goodsServicePurchaseId, ");
        selectSql.append("gsp.GoodsServicePurchaseCode as goodsServicePurchaseName, ");
        selectSql.append("CASE WHEN pps.CurrencyID = 'VND' THEN 1 ELSE 0 END as isForeignCurrency, ");
        selectSql.append("ppd.VATAmountOriginal as VATAmountOriginal, ");
        selectSql.append("ei.id as expenseItemId, ");
        selectSql.append("ei.ExpenseItemName as expenseItemCode, ");
        selectSql.append("cs.id as costSetId, ");
        selectSql.append("cs.CostSetName as costSetCode, ");
        selectSql.append("emc.id as emContractId, ");
        selectSql.append("CASE WHEN pps.TypeLedger = 0 then emc.NoFBook else emc.NoFBook end as emContractCode, ");
        selectSql.append("bi.id as budgetItemId, ");
        selectSql.append("bi.BudgetItemCode as budgetItemCode, ");
        selectSql.append("ou.id as departmentId, ");
        selectSql.append("ou.OrganizationUnitCode as departmentCode, ");
        selectSql.append("sc.id as statisticsId, ");
        selectSql.append("sc.statisticsCode as statisticsCode, ");
        selectSql.append("ppd.orderPriority as orderPriority ");

        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "PPServiceDetailDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<PPServiceDetailDTO> findAllPPServiceDetailByPaymentVoucherID(UUID paymentVoucherID) {
        Map<String, Object> params = new HashMap<>();
        params.put("ppServiceId", paymentVoucherID);
        StringBuilder sql = new StringBuilder();
        sql.append("from PPServiceDetail ppd ");
        sql.append("left join MaterialGoods mg on mg.ID = ppd.MaterialGoodsID ");
        sql.append("left join AccountingObject ao on ao.id = ppd.AccountingObjectID ");
        sql.append("left join Unit u on u.id = ppd.UnitID ");
        sql.append("left join PPOrder ppo on ppo.id = ppd.PPOrderID ");
        sql.append("left join GoodsServicePurchase gsp on gsp.id = ppd.GoodsServicePurchaseID ");
        sql.append("left join PPService pps on ppd.PPServiceID = pps.ID ");
        sql.append("left join ExpenseItem ei on ei.id = ppd.ExpenseItemID ");
        sql.append("left join CostSet cs on cs.id = ppd.CostSetID ");
        sql.append("left join EMContract emc on emc.ID = ppd.ContractID ");
        sql.append("left join BudgetItem bi on bi.id = ppd.BudgetItemID ");
        sql.append("left join EBOrganizationUnit ou on ou.ID = ppd.DepartmentID ");
        sql.append("left join StatisticsCode sc on sc.ID = ppd.StatisticsCodeID ");
        sql.append("where ppd.ppServiceId = (select ID from PPService where PaymentVoucherID = :ppServiceId ) ORDER BY ppd.OrderPriority");

        StringBuilder selectSql = new StringBuilder();
        selectSql.append("ppd.id as id, ");
        selectSql.append("mg.id as materialGoodsId, ");
        selectSql.append("mg.MaterialGoodsCode as materialGoodsCode, ");
        selectSql.append("ppd.Description as materialGoodsName, ");
        selectSql.append("ppd.DebitAccount as debitAccount, ");
        selectSql.append("ppd.CreditAccount as creditAccount, ");
        selectSql.append("ao.id as postedObjectId, ");
        selectSql.append("ao.AccountingObjectCode as postedObjectCode, ");
        selectSql.append("ao.AccountingObjectName as postedObjectName, ");
        selectSql.append("u.id as unitId, ");
        selectSql.append("u.UnitName as unitName, ");
        selectSql.append("CONVERT(varchar, ppd.Quantity) as quantity, ");
        selectSql.append("ppd.UnitPrice as unitPrice, ");
        selectSql.append("ppd.Amount as amount, ");
        selectSql.append("ppd.DiscountRate as discountRate, ");
        selectSql.append("ppd.DiscountAmount as discountAmount, ");
        selectSql.append("ppd.DiscountAccount as discountAccount, ");
        selectSql.append("ppd.PPOrderID as ppOrderId, ");
        selectSql.append("ppd.PPOrderDetailID as ppOrderDetailId, ");
        selectSql.append("ppo.No as ppOrderNo, ");
        selectSql.append("ppd.UnitPriceOriginal as unitPriceOriginal, ");
        selectSql.append("ppd.AmountOriginal as amountOriginal, ");
        selectSql.append("ppd.DiscountAmountOriginal as discountAmountOriginal, ");
        selectSql.append("ppd.VATDescription as vatDescription, ");
        selectSql.append("ppd.VATRate as vatRate, ");
        selectSql.append("ppd.VATAmount as vatAmount, ");
        selectSql.append("ppd.VATAccount as vatAccount, ");
        selectSql.append("ppd.DeductionDebitAccount as deductionDebitAccount, ");
        selectSql.append("ppd.InvoiceTemplate as invoiceTemplate, ");
        selectSql.append("ppd.InvoiceSeries as invoiceSeries, ");
        selectSql.append("ppd.InvoiceNo as invoiceNo, ");
        selectSql.append("convert(varchar, ppd.InvoiceDate, 103) as invoiceDate, ");
        selectSql.append("gsp.id as goodsServicePurchaseId, ");
        selectSql.append("gsp.GoodsServicePurchaseCode as goodsServicePurchaseName, ");
        selectSql.append("CASE WHEN pps.CurrencyID = 'VND' THEN 1 ELSE 0 END as isForeignCurrency, ");
        selectSql.append("ppd.VATAmountOriginal as VATAmountOriginal, ");
        selectSql.append("ei.id as expenseItemId, ");
        selectSql.append("ei.ExpenseItemName as expenseItemCode, ");
        selectSql.append("cs.id as costSetId, ");
        selectSql.append("cs.CostSetName as costSetCode, ");
        selectSql.append("emc.id as emContractId, ");
        selectSql.append("CASE WHEN pps.TypeLedger = 0 then emc.NoFBook else emc.NoFBook end as emContractCode, ");
        selectSql.append("bi.id as budgetItemId, ");
        selectSql.append("bi.BudgetItemCode as budgetItemCode, ");
        selectSql.append("ou.id as departmentId, ");
        selectSql.append("ou.OrganizationUnitCode as departmentCode, ");
        selectSql.append("sc.id as statisticsId, ");
        selectSql.append("sc.statisticsCode as statisticsCode, ");
        selectSql.append("ppd.orderPriority as orderPriority ");

        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "PPServiceDetailDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void saveReceiveBillPPService(ReceiveBill receiveBill, Integer vATRate) {
        String sql = "UPDATE PPServiceDetail SET VATRate = ?, VATAccount = ? ,DeductionDebitAccount = CreditAccount,  VATAmount = (Amount - DiscountAmount) *?/100, InvoiceTemplate = ?, InvoiceSeries = ? , InvoiceNo = ?, InvoiceDate = ?, GoodsServicePurchaseID = ?, AccountingObjectID = ? WHERE id = ?;" +
            "UPDATE PPService SET BillReceived = 1 WHERE id = (SELECT PPServiceID FROM PPServiceDetail WHERE id = ?);";
        jdbcTemplate.batchUpdate(sql, receiveBill.getListIDPPDetail(), vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setInt(1, receiveBill.getvATRate() != null ? receiveBill.getvATRate() : 0);
            ps.setString(2, receiveBill.getvATAccount());
            ps.setInt(3, vATRate);
            ps.setString(4, receiveBill.getInvoiceTemplate());
            ps.setString(5, receiveBill.getInvoiceSeries());
            ps.setString(6, receiveBill.getInvoiceNo());
            ps.setString(7, receiveBill.getInvoiceDate().toString());
            ps.setString(8, receiveBill.getGoodsServicePurchaseID() != null ? Common.revertUUID(receiveBill.getGoodsServicePurchaseID()).toString() : null);
            ps.setString(9, receiveBill.getAccountingObjectID() != null ? Common.revertUUID(receiveBill.getAccountingObjectID()).toString() : null);
            ps.setString(10, Common.revertUUID(detail).toString());
            ps.setString(11, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public List<UUID> findPaymentVoucherIDByPPServiceID(List<UUID> ids) {
        StringBuilder sql = new StringBuilder("select PaymentVoucherID as uuid from PPService where id in :uuidsPPService ");
        Map<String, Object> params = new HashMap<>();
        params.put("uuidsPPService", ids);
        Query query = entityManager.createNativeQuery(sql.toString(), "UUIDDTO");
        setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }
}
