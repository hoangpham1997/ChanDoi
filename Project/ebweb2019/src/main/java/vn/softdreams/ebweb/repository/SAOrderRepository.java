package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SAOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the SAOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAOrderRepository extends JpaRepository<SAOrder, UUID>, SAOrderRepositoryCustom {

    @Query(value = "select count(1) from  SAInvoiceDetail where SAOrderID = ?1", nativeQuery = true)
    int checkRelateVoucherSAInvoice(UUID id);

    @Query(value = "select count(1) from  RSInwardOutwardDetail where SAOrderID = ?1", nativeQuery = true)
    int checkRelateVoucherRSInwardOutward(UUID id);

    @Modifying
    @Query(value = "delete saorder where id in ?1 ;" +
        "delete RefVoucher where RefID1 in ?1 or RefID2 in ?1 ;" +
        "UPDATE SAInvoiceDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID in ?1 ;" +
        "UPDATE RSInwardOutwardDetail set SAOrderDetailID = null, SAOrderID = null where SAOrderID in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);

    @Query(value = "select distinct a.SAOrderID from (select SAOrderID from SAInvoiceDetail where SAOrderID in ?1 " +
        "union all " +
        "select SAOrderID from RSInwardOutwardDetail where SAOrderID in ?1 ) a", nativeQuery = true)
    List<String> getIDRef(List<UUID> uuids);

    @Modifying
    @Query(value = "UPDATE SAOrder set Status = ?3 where companyID = ?1 AND ID in ?2 ", nativeQuery = true)
    void updateStatusByListID(UUID companyID, List<UUID> uuids, int status);

    @Modifying
    @Query(value = "UPDATE SAOrder set Status = ?3 where companyID = ?1 AND ID = ?2 ", nativeQuery = true)
    void updateStatusByID(UUID companyID, UUID id, int status);

    @Modifying
    @Query(value = "UPDATE SAOrder set Status = ?3 where companyID = ?1 AND ID = (select top(1) SAOrderID from SAOrderDetail where ID = ?2 ) ", nativeQuery = true)
    void updateStatusByDetailID(UUID companyID, UUID detailID, int status);

    @Modifying
    @Query(value = "EXEC [dbo].[Update_Status_SAOrder] ?1", nativeQuery = true)
    void updateStatusSAOder(String listSAOrderID);
}
