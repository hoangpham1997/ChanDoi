package vn.softdreams.ebweb.repository;

import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailConvertDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the PPDiscountReturnDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPDiscountReturnDetailsRepository extends JpaRepository<PPDiscountReturnDetails, Long>, PPDiscountReturnDetailsRepositoryCustom {

//    @Query(value = "select re.PPDiscountReturnID PPDiscountReturnID, " +
//        "       re.PPInvoiceID PPInvoiceID, " +
//        "       re.PPInvoiceDetailID PPInvoiceDetailID, " +
//        "       re.Description Description, " +
//        "       re.DebitAccount DebitAccount, " +
//        "       re.CreditAccount CreditAccount, " +
//        "       re.Quantity Quantity, " +
//        "       re.UnitPrice UnitPrice, " +
//        "       re.UnitPriceOriginal UnitPriceOriginal, " +
//        "       re.Amount Amount, " +
//        "       re.AmountOriginal AmountOriginal, " +
//        "       re.MainQuantity MainQuantity, " +
//        "       re.MainUnitPrice MainUnitPrice, " +
//        "       re.MainConvertRate MainConvertRate, " +
//        "       re.Formula Formula, " +
//        "       CONVERT(varchar,re.ExpiryDate,111) ExpiryDate, " +
//        "       re.LotNo LotNo, " +
//        "       re.VATRate VATRate, " +
//        "       re.VATAmount VATAmount, " +
//        "       re.VATAmountOriginal VATAmountOriginal, " +
//        "       re.VATAccount VATAccount, " +
//        "       re.IsMatch IsMatch, " +
//        "       CONVERT(varchar,re.MatchDate,111) MatchDate, " +
//        "       re.OrderPriority OrderPriority, " +
//        "       re.VATDescription VATDescription, " +
//        "       re.DeductionDebitAccount DeductionDebitAccount, " +
//        "       re.IsPromotion IsPromotion, " +
//        "       rp.RepositoryCode     RepositoryID, " +
//        "       mg.MaterialGoodsID  MaterialGoodsID, " +
//        "       mg.MaterialGoodsCode  MaterialGoodsCode, " +
//        "       u.UnitName              UnitID, " +
//        "       u2.UnitName             MainUnitID, " +
//        "       gsc.GoodsServicePurchaseCode GoodsServicePurchaseID, " +
//        "       aco.AccountingObjectCode AccountingObjectID, " +
//        "       cs.CostSetCode CostSetID, " +
//        "       ct.NoMBook ContractNoMBook," +
//        "       ct.NoFBook ContractNoFBook, " +
//        "       stc.StatisticsCode StatisticsCodeID, " +
//        "       ou.OrganizationUnitCode DepartmentID, " +
//        "       ei.ExpenseItemCode      ExpenseItemID, " +
//        "       bi.BudgetItemCode     BudgetItemID, " +
//        "  case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as PPInvoiceNoBook," +
//        "  iv.Date as PPInvoiceDate " +
//        " from PPDiscountReturnDetail re " +
//        "         left join PPDiscountReturn pp on pp.ID = re.PPDiscountReturnID" +
//        "         left join PPInvoice iv on re.PPInvoiceID = iv.ID" +
//        "         left join PPInvoiceDetail ivdtl on re.PPInvoiceDetailID = ivdtl.ID " +
//        "         left join Repository rp on rp.ID = re.RepositoryID " +
//        "         left join MaterialGoods mg on mg.ID = re.MaterialGoodsID " +
//        "         left join Unit u on u.ID = re.UnitID " +
//        "         left join Unit u2 on u2.ID = re.MainUnitID " +
//        "         left join GoodsServicePurchase gsc on gsc.ID = re.GoodsServicePurchaseID " +
//        "         left join AccountingObject aco on aco.ID = re.AccountingObjectID " +
//        "         left join SABill sa on sa.ID = re.SABillID " +
//        "         left join SABillDetail sadtl on sadtl.ID = re.SABillDetailID " +
//        "         left join CostSet cs on cs.ID = re.CostSetID " +
//        "         left join EMContract ct on ct.ID = re.ContractID " +
//        "         left join StatisticsCode stc on stc.ID = re.StatisticsCodeID " +
//        "         left join EbOrganizationUnit ou on ou.ID = re.DepartmentID " +
//        "         left join ExpenseItem ei on ei.ID = re.ExpenseItemID " +
//        "         left join BudgetItem bi on bi.id = re.BudgetItemID " +
//        "where re.PPDiscountReturnID = :ppDiscountReturnId order by re.OrderPriority asc ", nativeQuery = true)
//    List<PPDiscountReturnDetailConvertDTO> getAllPPDiscountReturnDetailsByID(@Param("ppDiscountReturnId") UUID ppDiscountReturnId, @Param("typeLedger") String typeLedger);

    @Query(value = "Select * from PPDiscountReturnDetail ppD left join MaterialGoods mt on ppD.MaterialGoodsID = mt.ID\n" +
        "where ppD.PPDiscountReturnID in :ids and mt.MaterialGoodsType in (0,1,3)", nativeQuery = true)
    List<PPDiscountReturnDetails> findByPpDiscountReturnIDOrderByOrderPriority(@Param("ids") List<UUID> ids);

    @Modifying
    @Query(value = "delete from PPDiscountReturnDetail where PPDiscountReturnID in ?1", nativeQuery = true)
    void deleteByIds(List<UUID> listID);

    @Query(value = "select * from PPDiscountReturnDetail ppd left join PPDiscountReturn pp on ppd.PPDiscountReturnID = pp.ID where pp.Recorded = 1 and pp.TypeID = 230 and pp.PostedDate >= ?1 and pp.CompanyID = ?2 and ppd.PPInvoiceDetailID is not null and ppd.MaterialGoodsID in ?3", nativeQuery = true)
    List<PPDiscountReturnDetails> getAllPPDiscountReturnDetailsByComID(String strStartDate, UUID org, List<UUID> materialGoodsIDs);

//    @Query(value = "select ppdtl.*, case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as PPInvoiceNoBook, iv.Date  as PPInvoiceDate from PPDiscountReturnDetail ppdtl left join PPInvoice iv on ppdtl.PPInvoiceID = iv.ID where ppdtl.PPDiscountReturnID = :ppDiscountReturnId", nativeQuery = true)
//    Set<PPDiscountReturnDetails> getPPDiscountReturnDetailsByID(@Param("ppDiscountReturnId") UUID ppDiscountReturnId, @Param("typeLedger") String typeLedger);
}
