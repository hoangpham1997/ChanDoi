package vn.softdreams.ebweb.repository;

import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.PPInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import vn.softdreams.ebweb.domain.PPInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDateAndNoBookDTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the PPInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPInvoiceRepository extends JpaRepository<PPInvoice, UUID>, PPInvoiceRepositoryCustom {

    @Query(value = "select iv.Date as ppInvoiceDate, case when :typeLedger = 0 then iv.NoFBook else iv.NoMBook end as ppInvoiceNoBook from PPInvoice iv where iv.ID = :id ", nativeQuery = true)
    PPInvoiceConvertDateAndNoBookDTO findDateAndNoBookByID(@Param("typeLedger") String typeLedger, @Param("id") UUID id);

    @Query(value = "select * from PPInvoice a where a.PaymentVoucherID = ?1 ", nativeQuery = true)
    PPInvoice getPPInvoiceByPaymentVoucherId(UUID id);

    @Query(value = "select a.StoredInRepository from PPInvoice a where a.id = ?1 ", nativeQuery = true)
    Boolean getStoredInRepositoryById(UUID id);

    @Query(value = "select * from PPInvoice where PaymentVoucherID IN ?1 ", nativeQuery = true)
    List<PPInvoice> findByListIDMBCreditCard(List<UUID> uuidList);

    @Modifying
    @Query(value = "DELETE FROM PPInvoice WHERE ID IN ?1 ", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "UPDATE PPInvoice SET Recorded = 0 WHERE ID IN ?1", nativeQuery = true)
    void updateMultiUnRecord(List<UUID> uuidList);

    @Query(value = "select ID from PPInvoice where  PaymentVoucherID IN ?1", nativeQuery = true)
    List<String> findAllIDByPaymentVoucherID(List<UUID> uuids);

    @Modifying
    @Query(value = "Update PPInvoice set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecordPPI(List<UUID> uuidList);

    @Query(value = "select ID from PPInvoice where RSInwardOutwardID IN ?1", nativeQuery = true)
    List<UUID> findAllIDByRSInwardOutwardID(List<UUID> uuids);

    @Query(value = "select distinct PPOrderDetailID from PPInvoiceDetail where id in ?1", nativeQuery = true)
    List<UUID> getPPOrderDetailIDSByListID(List<UUID> ppInvoiceDetailIDs);

    @Query(nativeQuery = true, value = "select case when  b.id is null and c.id is null and d.id is null then convert(bit, 0) else convert(bit, 1) end from PPINVOICE a " +
        "left join MCPaymentDetailVendor b on a.Id=b.PPInvoiceID " +
        "left join MBTellerPaperDetailVendor c on a.ID = c.PPInvoiceID " +
        "left join MBCreditCardDetailVendor d on a.ID= d.PPInvoiceID where a.id = ?1 ")
    boolean checkHasPaid(UUID PPInvoiceId);
//    List<PPInvoiceDetails> findByPPInvoiceIDIn(List<UUID> ppInvoiceList);
}
