package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.repository.PPInvoiceDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class PPInvoiceDetailsRepositoryImpl implements PPInvoiceDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void saveReceiveBillPPInvoice(ReceiveBill receiveBill, Integer vATRate) {
        String sql = "UPDATE ppd SET ppd.VATRate = ?, ppd.VATAccount = ? , ppd.VATAmount = CASE WHEN pp.IsImportPurchase = 0 " +
            "THEN ((ppd.Amount - ppd.DiscountAmount) + ppd.SpecialConsumeTaxAmount)*?/100 WHEN pp.IsImportPurchase = 1 " +
            "THEN ((ppd.Amount - ppd.DiscountAmount + ppd.ImportTaxExpenseAmount) + ppd.ImportTaxAmount + ppd.SpecialConsumeTaxAmount)*?/100 " +
            "ELSE 1 END ,  ppd.VATAmountOriginal = CASE WHEN pp.IsImportPurchase = 0 " +
            "THEN ((ppd.AmountOriginal - ppd.DiscountAmountOriginal) + ppd.SpecialConsumeTaxAmountOriginal)*?/100 WHEN pp.IsImportPurchase = 1 " +
            "THEN ((ppd.AmountOriginal - ppd.DiscountAmountOriginal + ppd.ImportTaxExpenseAmountOriginal) + ppd.ImportTaxAmountOriginal + ppd.SpecialConsumeTaxAmountOriginal)*?/100 " +
            "ELSE 1 END  , ppd.DeductionDebitAccount = CASE WHEN pp.IsImportPurchase = 0 THEN ppd.CreditAccount WHEN pp.IsImportPurchase = 1 THEN '1331' ELSE NULL END," +
            " ppd.InvoiceTemplate = ?, ppd.InvoiceSeries = ? , ppd.InvoiceNo = ?, ppd.InvoiceDate = ?, ppd.GoodsServicePurchaseID = ?, " +
            "ppd.AccountingObjectID = ? FROM PPInvoiceDetail AS ppd INNER JOIN PPInvoice AS pp ON ppd.PPInvoiceID = pp.ID " +
            " WHERE ppd.id = ?;" +
            "UPDATE PPInvoice SET BillReceived = 1 WHERE id = (SELECT PPInvoiceID FROM PPInvoiceDetail WHERE id = ?);";
        jdbcTemplate.batchUpdate(sql, receiveBill.getListIDPPDetail(), vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setInt(1, receiveBill.getvATRate() != null ? receiveBill.getvATRate() : 0);
            ps.setString(2, receiveBill.getvATAccount());
            ps.setInt(3, vATRate);
            ps.setInt(4, vATRate);
            ps.setInt(5, vATRate);
            ps.setInt(6, vATRate);
            ps.setString(7, receiveBill.getInvoiceTemplate());
            ps.setString(8, receiveBill.getInvoiceSeries());
            ps.setString(9, receiveBill.getInvoiceNo());
            ps.setString(10, receiveBill.getInvoiceDate().toString());
            ps.setString(11, receiveBill.getGoodsServicePurchaseID() != null ? Common.revertUUID(receiveBill.getGoodsServicePurchaseID()).toString() : null);
            ps.setString(12, receiveBill.getAccountingObjectID() != null ? Common.revertUUID(receiveBill.getAccountingObjectID()).toString() : null);
            ps.setString(13, Common.revertUUID(detail).toString());
            ps.setString(14, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public Page<PPInvoiceConvertDTO> getPPInvoiceDetailsGetLicense(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, List<UUID> listID, UUID org, String currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceConvertDTO> ppInvoiceConvertDTOS = new ArrayList<>();
        StringBuilder sort = new StringBuilder();
        sort.append(" order by pi.Date desc, ");
        if (currentBook != null) {
            if (currentBook.equals('1')) {
                sort.append(" pi.NoMBook desc ");
            } else {
                sort.append(" pi.NoFBook desc ");
            }
        }
        sql.append("  from PPInvoiceDetail pidtl " +
            "         left join PPInvoice pi on pi.ID = pidtl.PPInvoiceID " +
            "      left join MaterialGoods mg on mg.ID = pidtl.MaterialGoodsID where pi.CompanyID = :companyID and pi.Recorded = 1 and " +
            " (pidtl.Quantity - (select case when sum(prdtl.quantity) is not null then sum(prdtl.quantity) else 0 end " +
            "                        from PPDiscountReturnDetail prdtl  left join PPDiscountReturn pdr on pdr.id = prdtl.PPDiscountReturnID" +
            "                        where prdtl.PPInvoiceDetailID = pidtl.id and pdr.TypeID = 220 ");
        params.put("companyID", org);
        if (listID != null && listID.size() > 0) {
            sql.append("and pidtl.id not in :listID ");
            params.put("listID", listID);
        }
        sql.append(" )) > 0 ");
        if (currentBook != null) {
            sql.append(" and (pi.TypeLedger in (:typeLedger, 2)) ");
            params.put("typeLedger", currentBook);
        }
        if (accountingObjectID != null) {
            sql.append(" and pidtl.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (currencyCode != null) {
            sql.append(" and pi.CurrencyID = :currencyCode ");
            params.put("currencyCode", currencyCode);
        }
        if (!Strings.isNullOrEmpty(formDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(formDate, toDate, params, sql, "pi.Date", "Date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select pidtl.id                                                         ppInvoiceDetailID, " +
                "       pi.Date, " +
                "       pi.postedDate, " +
                "       (select Type.TypeGroupID from Type where Type.ID = pi.TypeID) typeGroupID, " +
                "       case when :typeLedger = 0 then pi.NoFBook else pi.NoMBook end as noMBook, " +
                "       mg.MaterialGoodsCode, " +
                "       pi.TypeLedger, " +
                "       pi.ID refID2, " +
                "       pi.Reason, " +
                "       pidtl.PPInvoiceID, " +
                "       pidtl.MaterialGoodsID, " +
                "       pidtl.RepositoryID, " +
                "       pidtl.Description, " +
                "       pidtl.DebitAccount, " +
                "       pidtl.CreditAccount, " +
                "       pidtl.UnitID, " +
                "       pidtl.Quantity - (select case when sum(prdtl.quantity) is not null then sum(prdtl.quantity) else 0 end " +
                "                         from PPDiscountReturnDetail prdtl " +
                "                                  left join PPDiscountReturn pdr on pdr.id = prdtl.PPDiscountReturnID " +
                "                         where prdtl.PPInvoiceDetailID = pidtl.id " +
                "                           and pdr.TypeID = 220)                                                              Quantity, " +
                "       pidtl.AmountOriginal - (((select case when sum(prdtl.quantity) is not null then sum(prdtl.quantity) else 0 end " +
                "                                from PPDiscountReturnDetail prdtl " +
                "                                         left join PPDiscountReturn pdr on pdr.id = prdtl.PPDiscountReturnID " +
                "                                where prdtl.PPInvoiceDetailID = pidtl.id " +
                "                                  and pdr.TypeID = 220) * pidtl.UnitPriceOriginal) + (select case " +
                "                                                                                                 when sum(prdtl1.UnitPrice * prdtl1.Quantity) is not null " +
                "                                                                                                     then sum(prdtl1.UnitPrice * prdtl1.Quantity) " +
                "                                                                                                 else 0 end " +
                "                                                                                      from PPDiscountReturnDetail prdtl1 " +
                "                                                                                               left join PPDiscountReturn pdr on pdr.id = prdtl1.PPDiscountReturnID " +
                "                                                                                      where prdtl1.PPInvoiceDetailID = pidtl.id " +
                "                                                                                        and pdr.TypeID = 230)) remainingAmount," +
                "       pidtl.UnitPrice, " +
                "       pidtl.UnitPriceOriginal, " +
                "       pidtl.Amount, " +
                "       pidtl.AmountOriginal, " +
                "       pidtl.DiscountRate, " +
                "       pidtl.DiscountAmount, " +
                "       pidtl.DiscountAmountOriginal, " +
                "       pidtl.InwardAmount, " +
                "       pidtl.InwardAmountOriginal, " +
                "       pidtl.FreightAmount, " +
                "       pidtl.FreightAmountOriginal, " +
                "       pidtl.ImportTaxExpenseAmount, " +
                "       pidtl.ImportTaxExpenseAmountOriginal, " +
                "       pidtl.ExpiryDate, " +
                "       pidtl.LotNo, " +
                "       pidtl.CustomUnitPrice, " +
                "       pidtl.VATRate, " +
                "       pidtl.VATAmount, " +
                "       pidtl.VATAmountOriginal, " +
                "       pidtl.VATAccount, " +
                "       pidtl.DeductionDebitAccount, " +
                "       pidtl.MainUnitID, " +
                "       pidtl.MainQuantity, " +
                "       pidtl.MainUnitPrice, " +
                "       pidtl.MainConvertRate, " +
                "       pidtl.Formula, " +
                "       pidtl.ImportTaxRate, " +
                "       pidtl.ImportTaxAmount, " +
                "       pidtl.ImportTaxAmountOriginal, " +
                "       pidtl.ImportTaxAccount, " +
                "       pidtl.SpecialConsumeTaxRate, " +
                "       pidtl.SpecialConsumeTaxAmount, " +
                "       pidtl.SpecialConsumeTaxAmountOriginal, " +
                "       pidtl.SpecialConsumeTaxAccount, " +
                "       pidtl.InvoiceType, " +
                "       pidtl.InvoiceDate, " +
                "       pidtl.InvoiceNo, " +
                "       pidtl.InvoiceSeries, " +
                "       pidtl.GoodsServicePurchaseID, " +
                "       pidtl.AccountingObjectID, " +
                "       pidtl.BudgetItemID, " +
                "       pidtl.CostSetID, " +
                "       pidtl.ContractID, " +
                "       pidtl.StatisticCodeID, " +
                "       pidtl.DepartmentID, " +
                "       pidtl.ExpenseItemID, " +
                "       pidtl.PPOrderID, " +
                "       pidtl.PPOrderDetailID, " +
                "       pidtl.CashOutExchangeRateFB, " +
                "       pidtl.CashOutAmountFB, " +
                "       pidtl.CashOutDifferAmountFB, " +
                "       pidtl.CashOutDifferAccountFB, " +
                "       pidtl.CashOutExchangeRateMB, " +
                "       pidtl.CashOutAmountMB, " +
                "       pidtl.CashOutDifferAmountMB, " +
                "       pidtl.CashOutDifferAccountMB, " +
                "       pidtl.CashOutVATAmountFB, " +
                "       pidtl.CashOutDifferVATAmountFB, " +
                "       pidtl.CashOutVATAmountMB, " +
                "       pidtl.CashOutDifferVATAmountMB, " +
                "       pidtl.OrderPriority, " +
                "       pidtl.VATDescription, " +
                "       pidtl.InvoiceTemplate "
                + sql.toString() + sort.toString(), "PPInvoiceConvertDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            ppInvoiceConvertDTOS = query.getResultList();
        }
        return new PageImpl<>(ppInvoiceConvertDTOS, pageable, total.longValue());
    }

    @Override
    public List<LotNoDTO> getListLotNo(UUID materialGoodsID) {
        Query lotNoQuery = entityManager.createNativeQuery("SELECT LotNo, ExpiryDate FROM PPInvoiceDetail" +
            " WHERE MaterialGoodsID = :id AND LotNo is not null AND ExpiryDate is not null", "LotNoDTO");
        lotNoQuery.setParameter("id", materialGoodsID);
        return lotNoQuery.getResultList();
    }

    @Override
    public int checkReferences(UUID id) {
        if (id == null) return 0;

        List<String> referenceTables = Constants.PPInvoiceDetailReferenceTable.referenceTables;

        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < referenceTables.size(); i++) {
            sql.append(" select count(1) as total from ")
                .append(referenceTables.get(i))
                .append(" where PPInvoiceID = ?1 ")
                .append(i == referenceTables.size() - 1 ? "" : " union ");
        }

        Query query = entityManager.createNativeQuery("select sum(total) sum from (" + sql.toString() + ") a");
        query.setParameter(1, id);
        Number sum = (Number) query.getSingleResult();
        return sum != null ? sum.intValue() : -1;
    }

    @Override
    public void updateTotalReceiveBill(List<UUID> uuidList, UUID orgID) {
        String sql = "UPDATE PPInvoice SET TotalVATAmount = (SELECT SUM(VATAmount) FROM PPInvoiceDetail WHERE PPInvoiceID = ?), " +
            " TotalVATAmountOriginal = (SELECT SUM(VATAmountOriginal) FROM PPInvoiceDetail WHERE PPInvoiceID = ?) WHERE CompanyID = ? AND ID = ?";
        jdbcTemplate.batchUpdate(sql, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(orgID).toString());
            ps.setString(4, Common.revertUUID(detail).toString());
        });
    }
}

