package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GoodsServicePurchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.GoodsServicePurchaseContvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the GoodsServicePurchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsServicePurchaseRepository extends JpaRepository<GoodsServicePurchase, UUID> {

    @Query(value = "select * from GoodsServicePurchase gsp where gsp.ID =" +
        " (select eb.GoodsServicePurchaseID from EbOrganizationUnit eb where eb.id = ?1)", nativeQuery = true)
    Optional<GoodsServicePurchase> getPurchaseName(UUID companyID);

    @Query(value = "select id, GoodsServicePurchaseCode, GoodsServicePurchaseName from GoodsServicePurchase", nativeQuery = true)
    List<GoodsServicePurchaseContvertDTO> getPurchaseNameToCombobox();

    @Query(value = "select * from GoodsServicePurchase a where a.isActive = 1 order by a.GoodsServicePurchaseCode", nativeQuery = true)
    List<GoodsServicePurchase> findAllByIsActive();
}
