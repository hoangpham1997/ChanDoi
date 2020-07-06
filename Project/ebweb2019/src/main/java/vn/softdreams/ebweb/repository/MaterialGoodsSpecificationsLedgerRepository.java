package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsSpecificationsLedger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsSpecificationsLedgerRepository extends JpaRepository<MaterialGoodsSpecificationsLedger, UUID>, MaterialGoodsSpecificationsLedgerRepositoryCustom {

    @Modifying
    @Query(value = "DELETE MaterialGoodsSpecificationsLedger WHERE ReferenceID = ?1 ", nativeQuery = true)
    void deleteByRefID(UUID id);

    @Modifying
    @Query(value = "DELETE MaterialGoodsSpecificationsLedger WHERE ReferenceID IN ?1 ", nativeQuery = true)
    void deleteByListRefID(List<UUID> id);
}
