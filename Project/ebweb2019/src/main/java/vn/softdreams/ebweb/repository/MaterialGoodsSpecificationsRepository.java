package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecifications;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsSpecifications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsSpecificationsRepository extends JpaRepository<MaterialGoodsSpecifications, UUID> {

    @Query(value = "select * from MaterialGoodsSpecifications b where MaterialGoodsID = ?1 Order By MaterialGoodsSpecificationsCode", nativeQuery = true)
    List<MaterialGoodsSpecifications> findByMaterialGoodsID(UUID id);

}
