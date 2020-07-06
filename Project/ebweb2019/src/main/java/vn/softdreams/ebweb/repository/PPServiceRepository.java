package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPService;
import vn.softdreams.ebweb.service.dto.PPServiceDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PPServiceRepository extends JpaRepository<PPService, UUID>, PPServiceRepositoryCustom {

    @Query(nativeQuery = true, value = "select case when  b.id is null and c.id is null and d.id is null then convert(bit, 0) else convert(bit, 1) end from PPService a " +
        "left join MCPaymentDetailVendor b on a.Id=b.PPInvoiceID " +
        "left join MBTellerPaperDetailVendor c on a.ID = c.PPInvoiceID " +
        "left join MBCreditCardDetailVendor d on a.ID= d.PPInvoiceID where a.id = ?1 ")
    boolean checkHasPaid(UUID ppServiceId);

    @Query(nativeQuery = true, value = "select case when  b.id is null and c.id is null and d.id is null then convert(bit, 0) else convert(bit, 1) end from PPService a " +
        "left join MCPaymentDetailVendor b on a.Id=b.PPInvoiceID " +
        "left join MBTellerPaperDetailVendor c on a.ID = c.PPInvoiceID " +
        "left join MBCreditCardDetailVendor d on a.ID= d.PPInvoiceID where a.id In ?1 ")
    boolean checkListHasPaid(List<UUID> ppServiceId);

    @Query(value = "select * from PPService where PaymentVoucherID IN ?1 ", nativeQuery = true)
    List<PPService> findByListIDMBCreditCard(List<UUID> uuidList);

    @Modifying
    @Query(value = "DELETE FROM PPService WHERE ID IN ?1 ", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "UPDATE PPService SET Recorded = 0 WHERE ID IN ?1", nativeQuery = true)
    void updateMultiUnRecord(List<UUID> uuidList);

    @Query(value = "select ID from PPService where  PaymentVoucherID IN ?1", nativeQuery = true)
    List<String> findAllIDByPaymentVoucherIDStr(List<UUID> uuids);
}
