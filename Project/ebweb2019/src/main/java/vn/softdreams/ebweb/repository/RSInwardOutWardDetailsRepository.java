package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the RSInwardOutWardDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RSInwardOutWardDetailsRepository extends JpaRepository<RSInwardOutWardDetails, Long> {

    /**
     * @Author hieugie
     *
     * @param SaReturnID
     * @return
     */
    @Query(value = "Select count(1) from RSInwardOutWardDetails where SaReturnID = ?1", nativeQuery = true)
    int countBySaReturnID(UUID SaReturnID);

    /**
     * @Author hieugie
     *
     * @param SaReturnID
     * @return
     */
    @Modifying
    @Query(value = "UPDATE RSInwardOutwardDetail SET SaReturnID = null, SaReturnDetailsID = null where  SaReturnID = ?1", nativeQuery = true)
    void updateSaReturnIDAndSaReturnDetailIDToNull(UUID SaReturnID);

    @Modifying
    @Query(value = "UPDATE RSInwardOutwardDetail SET SAInvoiceID = null, SAInvoiceDetailID = null where  SAInvoiceID in ?1", nativeQuery = true)
    void UpdateSAInvoiceIDNull(List<UUID> id);
}
