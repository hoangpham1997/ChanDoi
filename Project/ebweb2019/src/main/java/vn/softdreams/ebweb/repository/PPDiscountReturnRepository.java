package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPDiscountReturn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the PPDiscountReturn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPDiscountReturnRepository extends JpaRepository<PPDiscountReturn, UUID>, PPDiscountReturnRepositoryCustom {

//    Optional<PPDiscountReturn> findAllById(UUID id);
//    void  deleteById(UUID id);

    /**
     * @param PPInvoiceID
     * @author congnd
     */
    @Modifying
    @Query(value = "UPDATE PPDiscountReturnDetail SET PPInvoiceID = null, PPInvoiceDetailID = null where  PPInvoiceID = ?1", nativeQuery = true)
    void updatePPInvoiceIDAndPPInvoiceDetailIDToNull(UUID PPInvoiceID);

    @Modifying
    @Query(value = "UPDATE PPDiscountReturnDetail SET PPInvoiceID = null, PPInvoiceDetailID = null where  PPInvoiceID IN ?1", nativeQuery = true)
    void updateListPPInvoiceIDAndPPInvoiceDetailIDToNull(List<UUID> PPInvoiceID);

    @Query(value = "select * from PPDiscountReturn where id in ?1", nativeQuery = true)
    List<PPDiscountReturn> findByListId(List<UUID> id);

    @Modifying
    @Query(value = "delete from PPDiscountReturn where id in ?1", nativeQuery = true)
    void deleteByIds(List<UUID> listID);

    @Query(value = "select count(*) " +
        "from RSInwardOutwardDetail rsiDTL " +
        "         left join RSInwardOutward rs on rs.ID = rsiDTL.RSInwardOutwardID " +
        "where rsiDTL.PPDiscountReturnID = ?1 and rs.CompanyID = ?2 and rs.TypeLedger in (?3, 2)", nativeQuery = true)
    Long countFromRSI(UUID id, UUID org, String currentBook);

    @Modifying
    @Query(value = "Update PPDiscountReturn set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecordPP(List<UUID> uuidList);

    @Query(value = "select ID from PPDiscountReturn where  RSInwardOutwardID IN ?1", nativeQuery = true)
    void updateUnrecordPPD(List<UUID> uuidList);

    @Query(value = "select ID from PPDiscountReturn where RSInwardOutwardID IN ?1", nativeQuery = true)
    List<UUID> findAllIDByRSInwardOutwardID(List<UUID> uuids);

    @Query(value = "select sa.* from PPDiscountReturn sa left join PPDiscountReturnDetail sad on sa.ID = sad.PPDiscountReturnID where (sa.IsExportInvoice = 0 OR sa.IsExportInvoice IS NULL) and (sa.IsBill = 0 OR sa.IsBill IS NULL) and sad.SABillID = ?1", nativeQuery = true)
    List<PPDiscountReturn> getPPDiscountReturnIDFromSABill(UUID objectID);

    @Modifying
    @Query(value = "UPDATE PPDiscountReturnDetail set saBillID = null, SaBillDetailID = null where saBillID = ?1", nativeQuery = true)
    void updateSaBillNull(UUID id);

    @Modifying
    @Query(value = "UPDATE PPDiscountReturn set InvoiceNo = null, isBill = 0 where ID = (select top(1) sad.PPDiscountReturnID from PPDiscountReturnDetail sad where sad.SABillID = ?1)", nativeQuery = true)
    void updateSaBillNullInvoiceNo(UUID id);
}
