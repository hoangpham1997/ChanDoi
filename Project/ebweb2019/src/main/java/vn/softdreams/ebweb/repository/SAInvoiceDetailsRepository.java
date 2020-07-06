package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the SAInvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAInvoiceDetailsRepository extends JpaRepository<SAInvoiceDetails, UUID>, SAInvoiceDetailsRepositoryCustom {

    @Query(value = "select * from SAInvoiceDetail where SAInvoiceDetail.SAInvoiceID = ?1", nativeQuery = true)
    List<SAInvoiceDetails> getSAInvoiceDetailsByID(UUID sAInvoiceID);

    @Query(value = "select * from SAInvoiceDetail where SAInvoiceDetail.SAInvoiceID = (select id from SAInvoice where MCReceiptID =?1)", nativeQuery = true)
    List<SAInvoiceDetails> getSAInvoiceDetailsByMCReceiptID(UUID mCReceiptID);

    @Query(value = "select * from SAInvoiceDetail where SAInvoiceDetail.SAInvoiceID = (select id from SAInvoice where MBDepositID =?1)", nativeQuery = true)
    List<SAInvoiceDetails> getSAInvoiceDetailsByMBDepositID(UUID mBDepositID);

    @Modifying
    @Query(value = "UPDATE SAInvoiceDetail SET SABillID = null, SABillDetailID = null where  SAInvoiceID = ?1", nativeQuery = true)
    void UpdateSABillIDNull(UUID id);

    @Query(value = "Select * from SAInvoiceDetail where SAInvoiceID in ?1", nativeQuery = true)
    List<SAInvoiceDetails> findBySaInvoiceIDOrderByOrderPriority(List<UUID> id);

    @Query(value = "SELECT * FROM SAInvoiceDetail WHERE SAInvoiceID = ?1 ", nativeQuery = true)
    List<SAInvoiceDetails> findByMBDepositID(UUID sAInvoiceId);

    @Modifying
    @Query(value = "delete FROM SAInvoiceDetail WHERE SAInvoiceID IN ?1", nativeQuery = true)
    void deleteByListSAInvoiceID(List<UUID> uuidList);
}
