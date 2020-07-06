package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SaReturn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SaReturn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaReturnRepository extends JpaRepository<SaReturn, UUID>, SaReturnRepositoryCustom {

    @Query(value = "select count(1) from SaReturn a where a.invoiceNo = ?1 and a.invoiceForm = ?2 and a.invoiceTemplate = ?3 and a.invoiceSeries = ?4 and a.CompanyID = ?5 and id <> ?6", nativeQuery = true)
    int countByInvoiceNoAndCompanyID(String soHd, Integer hinhThucHd, String mauSoHd, String kyHieuHd, UUID org, UUID id);

    @Modifying
    @Query(value = "UPDATE SaReturn set recorded = 0 where id = ?1", nativeQuery = true)
    void unrecord(UUID id);

    @Query(value = "Select TOP(1) * from SAReturn sa left join SAReturnDetail sad on sa.ID = sad.SAReturnID where sad.SAInvoiceID = ?1", nativeQuery = true)
    Optional<SaReturn> findBySAInvoiceID(UUID sAInvoiceId);

    @Query(value = "select count(1) from RSInwardOutward rs left join  RSInwardOutwardDetail rsdtl on rs.ID = rsdtl.RSInwardOutwardID where rsdtl.SAReturnID = ?1 and rs.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherRSOutward(UUID saReturnID, UUID org);

    @Modifying
    @Query(value = "delete FROM SAReturn WHERE ID IN ?1", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "Update SAReturn set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecordSAR(List<UUID> uuidList);

    @Query(value = "select ID from SAReturn where RSInwardOutwardID IN ?1", nativeQuery = true)
    List<UUID> findAllIDByRSInwardOutwardID(List<UUID> uuids);

    @Query(value = "select sa.* from SAReturn sa left join SAReturnDetail sad on sa.ID = sad.SAReturnID where (sa.IsExportInvoice = 0 OR sa.IsExportInvoice IS NULL) and (sa.IsBill = 0 OR sa.IsBill IS NULL) and sad.SABillID = ?1", nativeQuery = true)
    List<SaReturn> getSAReturnIDFromSABill(UUID objectID);

    @Modifying
    @Query(value = "UPDATE SAReturnDetail set saBillID = null, SaBillDetailID = null where saBillID = ?1", nativeQuery = true)
    void updateSaBillNull(UUID id);

    @Modifying
    @Query(value = "UPDATE SAReturn set InvoiceNo = null, isBill = 0 where ID = (select top(1) sad.SAReturnID from SAReturnDetail sad where sad.SABillID = ?1)", nativeQuery = true)
    void updateSaBillNullInvoiceNo(UUID id);
}
