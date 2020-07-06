package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SAInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SAInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAInvoiceRepository extends JpaRepository<SAInvoice, UUID>, SAInvoiceRepositoryCustom {

    @Modifying
    @Query(value = "UPDATE SaInvoiceDetail set saBillID = null, SaBillDetailID = null where saBillID = ?1", nativeQuery = true)
    void updateSaBillNull(UUID id);
    /**
     * @param PPInvoiceID
     * @author congnd
     */
    @Modifying
    @Query(value = "UPDATE SaInvoiceDetail SET PPInvoiceID = null, PPInvoiceDetailID = null where  PPInvoiceID = ?1", nativeQuery = true)
    void updatePPInvoiceIDAndPPInvoiceDetailIDToNull(UUID PPInvoiceID);

    @Modifying
    @Query(value = "UPDATE SaInvoiceDetail SET PPInvoiceID = null, PPInvoiceDetailID = null where  PPInvoiceID IN ?1", nativeQuery = true)
    void updateListPPInvoiceIDAndPPInvoiceDetailIDToNull(List<UUID> PPInvoiceID);

    @Query(value = "select count(1) from SAInvoice sa left join  SAInvoiceDetail sadtl on sa.ID = sadtl.SAInvoiceID where (sa.IsBill = 0 or sa.IsBill IS NULL) and (sa.IsExportInvoice = 0 or sa.IsExportInvoice IS NULL) and sadtl.SABillID IS NOT NULL and sadtl.SABillDetailID IS NOT NULL and sadtl.SAInvoiceID = ?1 and sa.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherSaBill(UUID sAInvoice, UUID org);

    @Query(value = "select count(1) from SAReturn sa left join  SAReturnDetail sadtl on sa.ID = sadtl.SAReturnID where sadtl.SAInvoiceID = ?1 and sa.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherSAReturn(UUID sAInvoice, UUID org);

    @Query(value = "select count(1) from RSInwardOutward rs left join  RSInwardOutwardDetail rsdtl on rs.ID = rsdtl.RSInwardOutwardID where rsdtl.SAInvoiceID = ?1 and rs.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherRSOutward(UUID sAInvoice, UUID org);

    @Query(value = "select TOP(1) * from SAInvoice where RSInwardOutwardID = ?1", nativeQuery = true)
    Optional<SAInvoice> findByRsInwardOutwardID(UUID id);

    @Query(value = "select sad.SAInvoiceDetailID from SAReturn sa left join SAReturnDetail sad on sa.id = sad.SAReturnID where sa.TypeID = 340 and sad.SAInvoiceDetailID IS NOT NULL", nativeQuery = true)
    List<UUID> getSAInvoiceIDFromSAReturn();

    @Query(value = "select sa.* from SAInvoice sa left join SAInvoiceDetail sad on sa.ID = sad.SAInvoiceID where (sa.IsExportInvoice = 0 OR sa.IsExportInvoice IS NULL) and (sa.IsBill = 0 OR sa.IsBill IS NULL) and sad.SABillID = ?1", nativeQuery = true)
    List<SAInvoice> getSAInvoiceIDFromSABill(UUID id);

    @Query(value = "select mc.MCReceiptID as ID from MCReceiptDetailCustomer mc where mc.SaleInvoiceID in ?1 union all select mb.MBDepositID as ID from MBDepositDetailCustomer mb where mb.SaleInvoiceID in ?1", nativeQuery = true)
    List<String> getRelateIDBySAInvoiceID(List<UUID> id);

    @Query(value = "select ID from SAInvoice where  MBDepositID IN ?1", nativeQuery = true)
    List<UUID> findListSAInvoiceID(List<UUID> uuidList);

    @Query(value = "select RSInwardOutWardID from SAInvoice where  MBDepositID IN ?1 AND RSInwardOutWardID IS NOT NULL ", nativeQuery = true)
    List<UUID> findListRSInwardOutWardIDNotNULL(List<UUID> uuidList);

    @Modifying
    @Query(value = "delete FROM SAInvoice WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteListSAInvoiceID(List<UUID> uuidList, UUID orgID);

    @Query(value = "select SAOrderDetailID from SAInvoiceDetail where  SAInvoiceID IN ?1 AND SAOrderDetailID IS NOT NULL", nativeQuery = true)
    List<String> findDetailsByListSAInvoice(List<UUID> uuidList);

    @Query(value = "select DISTINCT SAOrderID from SAOrderDetail where id in ?1 ", nativeQuery = true)
    List<String> getListIDSAorderByListDetail(List<UUID> uuidList);

    @Query(value = "select ID from SAInvoice where  MCReceiptID IN ?1", nativeQuery = true)
    List<String> findAllIDByMCReceiptID(List<UUID> uuids);

    @Query(value = "select ID from SAInvoice where  MBDepositID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    List<UUID> findAllIDByMBDepositID(List<UUID> uuids, UUID orgID);

    @Modifying
    @Query(value = "UPDATE SAInvoice SET Recorded = 0 WHERE ID IN ?1", nativeQuery = true)
    void updateUnRecordListID(List<UUID> uuids);

    @Modifying
    @Query(value = "DELETE FROM GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteUnRecordListID(List<UUID> uuids, UUID orgID);

    @Query(value = "select count(1) from SAInvoice sa where sa.RSInwardOutWardID = ?1 and sa.TypeID = 320", nativeQuery = true)
    int isBanHangChuaThuTien(UUID refID);

    @Modifying
    @Query(value = "Update SAInvoice set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecordSAI(List<UUID> uuidList);

    @Query(value = "select ID from SAInvoice where  RSInwardOutwardID IN ?1", nativeQuery = true)
    List<UUID> findAllIDByRSInwardOutwardID(List<UUID> uuids);

    @Modifying
    @Query(value = "UPDATE SaInvoice set InvoiceNo = null, isBill = 0 where ID = (select top(1) sad.SAInvoiceID from SAInvoiceDetail sad where sad.SABillID = ?1)", nativeQuery = true)
    void updateSaBillNullInvoiceNo(UUID id);
}
