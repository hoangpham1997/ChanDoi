package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.PPDiscountReturnDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class PPDiscountReturnDetailsRepositoryImpl implements PPDiscountReturnDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<PPDiscountReturnDetailDTO> getPPDiscountReturnDetailsByID(UUID ppDiscountReturnId, String data) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select ppdtl.*,   case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as PPInvoiceNoBook,  iv.Date as PPInvoiceDate " +
            " from PPDiscountReturnDetail ppdtl" +
            "         left join PPInvoice iv on ppdtl.PPInvoiceID = iv.ID" +
            " where ppdtl.PPDiscountReturnID = :ppDiscountReturnId ");
        params.put("typeLedger", data);
        params.put("ppDiscountReturnId", ppDiscountReturnId);
        List<PPDiscountReturnDetailDTO> ppDiscountReturnDetailDTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPDiscountReturnDetailDTO");
        Common.setParams(query, params);
        ppDiscountReturnDetailDTOS = query.getResultList();
        return ppDiscountReturnDetailDTOS;
    }

    @Override
    public Page<PPDiscountReturnOutWardDTO> findAllPPDisCountReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String currentBook) {
        List<PPDiscountReturnOutWardDTO> ppDiscountReturnOutWardDTO = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select pp.date, pp.PostedDate, iif(:book = 0, pp.NoFBook, pp.NoMBook) as book, pp.Reason, pp.TotalAmount - (COALESCE(pp.TotalDiscountAmount, 0)) + pp.TotalVATAmount as total, pp.id, a.TypeGroupID ");
        sql.append("from PPDiscountReturn pp ");
        sql.append("left join RSInwardOutwardDetail rsd on rsd.PPDiscountReturnID = pp.ID ");
        sql.append("left join Type a on a.ID = pp.TypeID ");
        sql.append("where pp.IsDeliveryVoucher = 0 ");
        sql.append(" and pp.typeID = 220 ");
        sql.append(" and pp.RSInwardOutwardID is null ");
        sql.append(" and rsd.PPDiscountReturnID is null ");
        sql.append(" and pp.Recorded = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (accountingObject != null) {
            sql.append("and AccountingObjectID = :accountingObject ");
            params.put("accountingObject", accountingObject);
        }
        if (org != null) {
            sql.append("and companyID = :org ");
            params.put("org", org);
        }
        if (!Strings.isNullOrEmpty(currentBook)) {
            sql.append("and pp.typeLedger in (:book, 2) ");
            params.put("book", currentBook);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("and CONVERT(varchar, pp.PostedDate, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("and CONVERT(varchar, pp.PostedDate, 112) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" order by pp.date desc, pp.postedDate desc, book desc ");
            Query query = entityManager.createNativeQuery(sql.toString(), "PPDiscountReturnOutWardDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            ppDiscountReturnOutWardDTO = query.getResultList();
        }
        return new PageImpl<>(ppDiscountReturnOutWardDTO, pageable, total.longValue());
    }

    @Override
    public List<PPDiscountReturnDetailOutWardDTO> findByPpDiscountReturnIDOrderByOrderPriority(List<UUID> id, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("Select ppD.*, case when :typeLedger = 0 then pp.NoFBook else pp.NoMBook end as NoBookPPDiscountReturn " +
            "from PPDiscountReturnDetail ppD " +
            "         left join MaterialGoods mt on ppD.MaterialGoodsID = mt.ID " +
            "         left join PPDiscountReturn pp on pp.ID = ppd.PPDiscountReturnID " +
            "where ppD.PPDiscountReturnID in :ids and mt.MaterialGoodsType in (0, 1, 3) and pp.TypeLedger in (2, :typeLedger) order by OrderPriority ");
        params.put("typeLedger", currentBook);
        params.put("ids", id);
        List<PPDiscountReturnDetailOutWardDTO> PPDiscountReturnDetailOutWardDTOs = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPDiscountReturnDetailOutWardDTO");
        Common.setParams(query, params);
        PPDiscountReturnDetailOutWardDTOs = query.getResultList();
        return PPDiscountReturnDetailOutWardDTOs;
    }

    @Override
    public List<PPDiscountReturnDetailsReportConvertDTO> getAllPPDiscountReturnDetailsReportByID(UUID id, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select " +
            "       re.Description Description, " +
            "       re.DebitAccount DebitAccount, " +
            "       re.CreditAccount CreditAccount, " +
            "       re.Quantity Quantity, " +
            "       re.UnitPrice UnitPrice, " +
            "       re.UnitPriceOriginal UnitPriceOriginal, " +
            "       re.Amount Amount, " +
            "       re.AmountOriginal AmountOriginal, " +
            "       re.MainQuantity MainQuantity, " +
            "       re.MainUnitPrice MainUnitPrice, " +
            "       re.MainConvertRate MainConvertRate, " +
            "       re.Formula Formula, " +
            "       CONVERT(varchar,re.ExpiryDate,111) ExpiryDate, " +
            "       re.LotNo LotNo, " +
            "       re.VATRate VATRate, " +
            "       re.VATAmount VATAmount, " +
            "       re.VATAmountOriginal VATAmountOriginal, " +
            "       re.VATAccount VATAccount, " +
            "       re.IsMatch IsMatch, " +
            "       CONVERT(varchar,re.MatchDate,111) MatchDate, " +
            "       re.OrderPriority OrderPriority, " +
            "       re.VATDescription VATDescription, " +
            "       re.DeductionDebitAccount DeductionDebitAccount, " +
            "       re.IsPromotion IsPromotion, " +
            "       rp.RepositoryCode     RepositoryCode, " +
            "       mg.MaterialGoodsCode  materialGoodsCode, " +
            "       u.UnitName              UnitCode, " +
            "       u2.UnitName             MainUnitCode, " +
            "       gsc.GoodsServicePurchaseCode GoodsServicePurchaseCode, " +
            "       aco.AccountingObjectCode AccountingObjectCode, " +
            "       cs.CostSetCode CostSetCode, " +
            "       ct.NoMBook ContractNoMBook," +
            "       ct.NoFBook ContractNoFBook, " +
            "       stc.StatisticsCode StatisticsCodeCode, " +
            "       ei.ExpenseItemCode      ExpenseItemCode, " +
            "       bi.BudgetItemCode     BudgetItemCode, " +
            "       ou.OrganizationUnitCode DepartmentCode, " +
            "  case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as PPInvoiceNoBook," +
            "  iv.Date as PPInvoiceDate, " +
            "       u.UnitName              UnitName, " +
            "       mg.MaterialGoodsName  MaterialGoodsName, " +
            "       rp.RepositoryName     RepositoryName " +
            "  from PPDiscountReturnDetail re " +
            "         left join PPDiscountReturn pp on pp.ID = re.PPDiscountReturnID" +
            "         left join PPInvoice iv on re.PPInvoiceID = iv.ID" +
            "         left join PPInvoiceDetail ivdtl on re.PPInvoiceDetailID = ivdtl.ID " +
            "         left join Repository rp on rp.ID = re.RepositoryID " +
            "         left join MaterialGoods mg on mg.ID = re.MaterialGoodsID " +
            "         left join Unit u on u.ID = re.UnitID " +
            "         left join Unit u2 on u2.ID = re.MainUnitID " +
            "         left join GoodsServicePurchase gsc on gsc.ID = re.GoodsServicePurchaseID " +
            "         left join AccountingObject aco on aco.ID = re.AccountingObjectID " +
            "         left join SABill sa on sa.ID = re.SABillID " +
            "         left join SABillDetail sadtl on sadtl.ID = re.SABillDetailID " +
            "         left join CostSet cs on cs.ID = re.CostSetID " +
            "         left join EMContract ct on ct.ID = re.ContractID " +
            "         left join StatisticsCode stc on stc.ID = re.StatisticsCodeID " +
            "         left join EbOrganizationUnit ou on ou.ID = re.DepartmentID " +
            "         left join ExpenseItem ei on ei.ID = re.ExpenseItemID " +
            "         left join BudgetItem bi on bi.id = re.BudgetItemID " +
            "where re.PPDiscountReturnID = :ppDiscountReturnId order by re.OrderPriority ");
        params.put("typeLedger", currentBook);
        params.put("ppDiscountReturnId", id);
        List<PPDiscountReturnDetailsReportConvertDTO> ppDiscountReturnDetailsReportConvertDTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPDiscountReturnDetailsReportConvertDTO");
        Common.setParams(query, params);
        ppDiscountReturnDetailsReportConvertDTOS = query.getResultList();
        return ppDiscountReturnDetailsReportConvertDTOS;
    }

    @Override
    public List<PPDiscountReturnDetailsReportConvertKTDTO> getAllPPDiscountReturnDetailsReportKTByID(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select * from ( " +
            "select ppdtl.Description, " +
            "       ppdtl.CreditAccount, " +
            "       ppdtl.DebitAccount, " +
            "       ppdtl.Amount, " +
            "       ppdtl.AmountOriginal, " +
            "       ppdtl.OrderPriority, " +
            "       pp.ExchangeRate exchangeRate, " +
            "       CAST(0 AS BIT) as checkVAT " +
            "from PPDiscountReturnDetail ppdtl " +
            "         left join PPDiscountReturn pp on pp.id = ppdtl.PPDiscountReturnID " +
            "where ppdtl.PPDiscountReturnID = :id " +
            "union " +
            "select ppdtl1.VATDescription description, " +
            "       ppdtl1.VATAccount CreditAccount, " +
            "       ppdtl1.DeductionDebitAccount DebitAccount, " +
            "       ppdtl1.VATAmount Amount, " +
            "       ppdtl1.VATAmountOriginal AmountOriginal, " +
            "       ppdtl1.OrderPriority, " +
            "       pp1.ExchangeRate exchangeRate, " +
            "       CAST(1 AS BIT) as checkVAT " +
            "from PPDiscountReturnDetail ppdtl1 " +
            "         left join PPDiscountReturn pp1 on pp1.id = ppdtl1.PPDiscountReturnID " +
            "where ppdtl1.PPDiscountReturnID = :id and COALESCE(ppdtl1.VATAmountOriginal, 0) <> 0) details " +
            "order by details.OrderPriority");
        params.put("id", id);
        List<PPDiscountReturnDetailsReportConvertKTDTO> ppDiscountReturnDetailsReportConvertDTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPDiscountReturnDetailsReportConvertKTDTO");
        Common.setParams(query, params);
        ppDiscountReturnDetailsReportConvertDTOS = query.getResultList();
        return ppDiscountReturnDetailsReportConvertDTOS;
    }

    @Override
    public List<PPDiscountReturnDetailConvertDTO> getAllPPDiscountReturnDetailsByID(UUID ppDiscountReturnId, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select re.PPDiscountReturnID                                            PPDiscountReturnID, " +
            "       re.PPInvoiceID                                                   PPInvoiceID, " +
            "       re.PPInvoiceDetailID                                             PPInvoiceDetailID, " +
            "       re.Description                                                   Description, " +
            "       re.DebitAccount                                                  DebitAccount, " +
            "       re.CreditAccount                                                 CreditAccount, " +
            "       re.Quantity                                                      Quantity, " +
            "       re.UnitPrice                                                     UnitPrice, " +
            "       re.UnitPriceOriginal                                             UnitPriceOriginal, " +
            "       re.Amount                                                        Amount, " +
            "       re.AmountOriginal                                                AmountOriginal, " +
            "       re.MainQuantity                                                  MainQuantity, " +
            "       re.MainUnitPrice                                                 MainUnitPrice, " +
            "       re.MainConvertRate                                               MainConvertRate, " +
            "       re.Formula                                                       Formula, " +
            "       CONVERT(varchar, re.ExpiryDate, 111)                             ExpiryDate, " +
            "       re.LotNo                                                         LotNo, " +
            "       re.VATRate                                                       VATRate, " +
            "       re.VATAmount                                                     VATAmount, " +
            "       re.VATAmountOriginal                                             VATAmountOriginal, " +
            "       re.VATAccount                                                    VATAccount, " +
            "       re.IsMatch                                                       IsMatch, " +
            "       CONVERT(varchar, re.MatchDate, 111)                              MatchDate, " +
            "       re.OrderPriority                                                 OrderPriority, " +
            "       re.VATDescription                                                VATDescription, " +
            "       re.DeductionDebitAccount                                         DeductionDebitAccount, " +
            "       re.IsPromotion                                                   IsPromotion, " +
            "       rp.id                                                            RepositoryID, " +
            "       rp.RepositoryCode                                                RepositoryCode, " +
            "       mg.ID                                                            MaterialGoodsID, " +
            "       mg.MaterialGoodsCode                                             MaterialGoodsCode, " +
            "       u.id                                                             UnitID, " +
            "       u.UnitName                                                       UnitName, " +
            "       u2.id                                                            MainUnitID, " +
            "       u2.UnitName                                                      MainUnitName, " +
            "       gsc.id                                                           GoodsServicePurchaseID, " +
            "       gsc.GoodsServicePurchaseCode                                     GoodsServicePurchaseCode, " +
            "       aco.id                                                           AccountingObjectID, " +
            "       aco.AccountingObjectCode                                         AccountingObjectCode, " +
            "       cs.id                                                            CostSetID, " +
            "       cs.CostSetCode                                                   CostSetCode, " +
            "       ct.NoMBook                                                       ContractNoMBook, " +
            "       ct.NoFBook                                                       ContractNoFBook, " +
            "       stc.id                                                           StatisticsCodeID, " +
            "       stc.StatisticsCode                                               StatisticsCode, " +
            "       ou.id                                                            DepartmentID, " +
            "       ou.OrganizationUnitCode                                          DepartmentCode, " +
            "       ei.id                                                            ExpenseItemID, " +
            "       ei.ExpenseItemCode                                               ExpenseItemCode, " +
            "       bi.id                                                            BudgetItemID, " +
            "       bi.BudgetItemCode                                                BudgetItemCode, " +
            "       case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as PPInvoiceNoBook, " +
            "       iv.Date                                                       as PPInvoiceDate " +
            "from PPDiscountReturnDetail re " +
            "         left join PPDiscountReturn pp on pp.ID = re.PPDiscountReturnID " +
            "         left join PPInvoice iv on re.PPInvoiceID = iv.ID " +
            "         left join PPInvoiceDetail ivdtl on re.PPInvoiceDetailID = ivdtl.ID " +
            "         left join Repository rp on rp.ID = re.RepositoryID " +
            "         left join MaterialGoods mg on mg.ID = re.MaterialGoodsID " +
            "         left join Unit u on u.ID = re.UnitID " +
            "         left join Unit u2 on u2.ID = re.MainUnitID " +
            "         left join GoodsServicePurchase gsc on gsc.ID = re.GoodsServicePurchaseID " +
            "         left join AccountingObject aco on aco.ID = re.AccountingObjectID " +
            "         left join SABill sa on sa.ID = re.SABillID " +
            "         left join SABillDetail sadtl on sadtl.ID = re.SABillDetailID " +
            "         left join CostSet cs on cs.ID = re.CostSetID " +
            "         left join EMContract ct on ct.ID = re.ContractID " +
            "         left join StatisticsCode stc on stc.ID = re.StatisticsCodeID " +
            "         left join EbOrganizationUnit ou on ou.ID = re.DepartmentID " +
            "         left join ExpenseItem ei on ei.ID = re.ExpenseItemID " +
            "         left join BudgetItem bi on bi.id = re.BudgetItemID " +
            "where re.PPDiscountReturnID = :ppDiscountReturnId order by re.OrderPriority ");
        params.put("typeLedger", currentBook);
        params.put("ppDiscountReturnId", ppDiscountReturnId);
        List<PPDiscountReturnDetailConvertDTO> ppDiscountReturnDetailsConvertDTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPDiscountReturnDetailConvertDTO");
        Common.setParams(query, params);
        ppDiscountReturnDetailsConvertDTOS = query.getResultList();
        return ppDiscountReturnDetailsConvertDTOS;
    }
}
