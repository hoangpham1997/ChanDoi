package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsPurchasePrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsPurchasePrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsPurchasePriceRepository extends JpaRepository<MaterialGoodsPurchasePrice, UUID> {

    @Query(value = "select * from MaterialGoodsPurchasePrice b where MaterialGoodsID = ?1", nativeQuery = true)
    List<MaterialGoodsPurchasePrice> findByMaterialGoodsID(UUID id);

}
