package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SaBill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the RSInwardOutward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RSInwardOutwardRepository extends JpaRepository<RSInwardOutward, UUID>, RSInwardOutwardRepositoryCustom {
    Integer countById(UUID id);

    @Query(value = "select * from RSInwardOutward where RSInwardOutward.ID = (select top 1 RSInwardOutwardID from SAInvoice where ID = ?1)", nativeQuery = true)
    Optional<RSInwardOutward> findBySaInvoiceID(UUID id);

    @Query(value = "select ID from RSInwardOutward where RSInwardOutward.ID = (select top 1 RSInwardOutwardID from SAInvoice where ID IN ?1)", nativeQuery = true)
    List<UUID> findByListSaInvoiceID(List<UUID> id);

    @Query(value = "select ID from RSInwardOutward where RSInwardOutward.ID = (select top 1 RSInwardOutwardID from PPInvoice where ID IN ?1)", nativeQuery = true)
    List<UUID> findByListPPInvoiceID(List<UUID> id);

    @Query(value = "select * from RSInwardOutward where RSInwardOutward.ID = (select top 1 RSInwardOutwardID from SAReturn where ID = ?1)", nativeQuery = true)
    Optional<RSInwardOutward> findBySaReturnID(UUID id);

    @Query(value = "Select a.id from SaReturn a where a.RSInwardOutwardID = ?1 and a.CompanyID = ?2", nativeQuery = true)
    UUID getSaReturnIDByRSInwardID(UUID rsInwardOutwardID, UUID companyID);

    @Query(value = "Select a.id from PPInvoice a where a.RSInwardOutwardID = ?1 and a.CompanyID = ?2", nativeQuery = true)
    UUID getPPInvoiceIDByRSInwardID(UUID rsInwardOutwardID, UUID companyID);

    @Modifying
    @Query(value = "delete from RSInwardOutward where id in ?1", nativeQuery = true)
    void deleteByPPDiscountReturnIds(List<UUID> id);

    @Modifying
    @Query(value = "delete from RSInwardOutward where id = ?1", nativeQuery = true)
    void deleteByPPDiscountReturnId(UUID id);

    @Modifying
    @Query(value = "delete from RSInwardOutward where id = (Select a.RSInwardOutwardID from SaReturn a where a.id = ?1)", nativeQuery = true)
    void deleteBySaReturnID(UUID id);

    @Modifying
    @Query(value = "delete from RSInwardOutward where id = (Select a.RSInwardOutwardID from SAInvoice a where a.id IN ?1)", nativeQuery = true)
    void deleteByListSAInvoiceID(List<UUID> uuidList, UUID orgID);

    @Query(value = "select count(*) from RSInwardOutward where id in ?1", nativeQuery = true)
    Long countByListId(List<UUID> rsInwardOutwardIds);

    @Modifying
    @Query(value = "Update RSInwardOutward set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecord(List<UUID> uuidList);

    @Modifying
    @Query(value = "Update RSInwardOutwardDetail set PPDiscountReturnID = null, PPDiscountReturnDetailID = null  where PPDiscountReturnID = ?1", nativeQuery = true)
    void updatePPDiscountReturnID(UUID id);

    @Query(value = "select ID FROM SAInvoice where RSInwardOutwardID = ?1", nativeQuery = true)
    UUID getIdSaInvoice(UUID id);

    @Query(value = "select ID FROM PPDiscountReturn where RSInwardOutwardID = ?1", nativeQuery = true)
    UUID getIdPPdiscountReturn(UUID id);

    @Query(value = "select ID FROM PPInvoice where RSInwardOutwardID = ?1", nativeQuery = true)
    UUID getIdPPInvoice(UUID id);

    @Query(value = "select ID FROM SAReturn where RSInwardOutwardID = ?1", nativeQuery = true)
    UUID getIdSAReturn(UUID id);
}
