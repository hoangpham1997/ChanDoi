package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsConvertUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsConvertUnitRepository extends JpaRepository<MaterialGoodsConvertUnit, UUID>, MaterialGoodsConvertUnitRepositoryCustom {

    @Query(value = "select * from MaterialGoodsConvertUnit b where MaterialGoodsID = ?1 ORDER BY OrderNumber", nativeQuery = true)
    List<MaterialGoodsConvertUnit> findByMaterialGoodsID(UUID id);

    @Query(value = "select TOP(1) * from MaterialGoodsConvertUnit where MaterialGoodsID = ?1 and unitID = ?2", nativeQuery = true)
    MaterialGoodsConvertUnit findByMaterialGoodsIDAndUnitID(UUID MaterialGoodsID, UUID unitID);

    @Query(value = "SELECT DISTINCT OrderNumber FROM MaterialGoodsConvertUnit LEFT JOIN MaterialGoods ON MaterialGoods.ID = MaterialGoodsID WHERE CompanyID in ?1 ORDER BY OrderNumber", nativeQuery = true)
    List<Integer> getNumberOrder(List<UUID> orgs);
}
