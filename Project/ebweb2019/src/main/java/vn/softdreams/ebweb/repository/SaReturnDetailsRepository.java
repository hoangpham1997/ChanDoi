package vn.softdreams.ebweb.repository;

import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDetailsViewDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the SaReturnDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaReturnDetailsRepository extends JpaRepository<SaReturnDetails, Long>, SaReturnDetailsRepositoryCustom {

    List<SaReturnDetails> findBySaReturnIDOrderByOrderPriority(UUID id);

    @Query(value = "Select * from SaReturnDetail where MaterialGoodsType in (0, 1, 3) and SaReturnID in :ids", nativeQuery = true)
    List<SaReturnDetails> findBySaReturnIDOrderByOrderPriority(@Param("ids") List<UUID> ids);

    @Query(value = "select a.ID, " +
        "       SAReturnID, " +
        "       a.MaterialGoodsID, " +
        "       MaterialGoodsCode, " +
        "       IsPromotion, " +
        "       a.RepositoryID RepositoryID, " +
        "       RepositoryCode, " +
        "       a.Description, " +
        "       DebitAccount, " +
        "       CreditAccount, " +
        "       a.UnitID UnitID, " +
        "       d.UnitName UnitName, " +
        "       Quantity, " +
        "       UnitPrice, " +
        "       UnitPriceOriginal, " +
        "       MainUnitID, " +
        "       e.UnitName mainUnitName, " +
        "       MainQuantity, " +
        "       MainUnitPrice, " +
        "       MainConvertRate, " +
        "       Formula, " +
        "       a.Amount Amount, " +
        "       a.AmountOriginal AmountOriginal, " +
        "       VATRate, " +
        "       VATAccount, " +
        "       VATAmount, " +
        "       VATAmountOriginal, " +
        "       DiscountRate, " +
        "       DiscountAmount, " +
        "       DiscountAmountOriginal, " +
        "       DiscountAccount, " +
        "       a.OwPrice OwPrice, " +
        "       a.OwAmount OwAmount, " +
        "       a.CostAccount CostAccount, " +
        "       RepositoryAccount, " +
        "       a.AccountingObjectID AccountingObjectID, " +
        "       AccountingObjectCode, " +
        "       SAInvoiceID, " +
        "       SAInvoiceDetailID, " +
        "       CONVERT(varchar, ExpiryDate, 103) ExpiryDate, " +
        "       LotNo, " +
        "       a.DepartmentID DepartmentID, " +
        "       g.OrganizationUnitCode OrganizationUnitCode, " +
        "       ExpenseItemID, " +
        "       ExpenseItemCode, " +
        "       BudgetItemID, " +
        "       BudgetItemCode, " +
        "       CostSetID, " +
        "       CostSetCode, " +
        "       ContractID, " +
        "       case when :typeLedger = 0 then m.NoFBook else m.NoMBook end as contractCode, " +
        "       StatisticsCodeID, " +
        "       StatisticsCode, " +
        "       CashOutExchangeRateFB, " +
        "       CashOutAmountFB, " +
        "       CashOutDifferAmountFB, " +
        "       CashOutDifferAccountFB, " +
        "       CashOutExchangeRateMB, " +
        "       CashOutAmountMB, " +
        "       CashOutDifferAmountMB, " +
        "       CashOutDifferAccountMB, " +
        "       CashOutVATAmountFB, " +
        "       CashOutDifferVATAmountFB, " +
        "       CashOutVATAmountMB, " +
        "       CashOutDifferVATAmountMB, " +
        "       OrderPriority, " +
        "       VATDescription, " +
        "       DeductionDebitAccount, " +
        "       SaBillDetailID, " +
        "       SaBillID, " +
        "       case when :typeLedger = 0 then o.NoFBook else o.NoMBook end as noFBook, " +
        "       CONVERT(varchar, o.Date, 103) date, " +
        "       a.CareerGroupID CareerGroupID, " +
        "       CareerGroupCode " +
        "from SAReturnDetail a " +
        "    left join MaterialGoods b on a.MaterialGoodsID = b.id " +
        "    left join Repository c on a.RepositoryID = c.id " +
        "    left join Unit d on a.UnitID = d.id " +
        "    left join Unit e on a.MainUnitID = e.id " +
        "    left join AccountingObject f on a.AccountingObjectID = f.id " +
        "    left join EbOrganizationUnit g on a.DepartmentID = g.id " +
        "    left join ExpenseItem h on a.ExpenseItemID = h.id " +
        "    left join BudgetItem i on a.BudgetItemID = i.id " +
        "    left join CostSet j on a.CostSetID = j.id " +
        "    left join EMContract m on a.ContractID = m.id " +
        "    left join CareerGroup ca on a.CareerGroupID = ca.id " +
        "    left join StatisticsCode n on a.StatisticsCodeID = n.id " +
        "    left join SAInvoice o on a.sainvoiceid = o.id " +
        "where a.SAReturnID = :saReturnID order by a.OrderPriority", nativeQuery = true)
    List<SaReturnDetailsViewDTO> findViewFullByID(@Param("saReturnID") UUID saReturnID,  @Param("typeLedger") String typeLedger);

    @Modifying
    @Query(value = "UPDATE SAReturnDetail SET SAInvoiceID = null, SAInvoiceDetailID = null where  SAInvoiceID in ?1", nativeQuery = true)
    void UpdateSAInvoiceIDNull(List<UUID> id);

    @Modifying
    @Query(value = "delete FROM SAReturnDetail WHERE SAReturnID IN ?1", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);
}
