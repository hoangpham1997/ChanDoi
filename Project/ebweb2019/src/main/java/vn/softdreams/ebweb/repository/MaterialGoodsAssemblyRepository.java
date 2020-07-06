package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsAssembly;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsAssembly entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsAssemblyRepository extends JpaRepository<MaterialGoodsAssembly, UUID> {


    @Query(value = "select * from MaterialGoodsAssembly b where MaterialGoodsID = ?1", nativeQuery = true)
    List<MaterialGoodsAssembly> findByMaterialGoodsID(UUID id);

    @Modifying
    @Query(value = "delete from MaterialGoodsAssembly where MaterialGoodsID = ?1", nativeQuery = true)
    void deleteByMaterialGoodsID(UUID id);
}
