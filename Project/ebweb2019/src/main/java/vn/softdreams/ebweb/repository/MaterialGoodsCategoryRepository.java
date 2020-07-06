package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsCategoryRepository extends JpaRepository<MaterialGoodsCategory, UUID>, MaterialGoodsCategoryRepositoryCustom {
    @Query(value = "select * from MaterialGoodsCategory b where b.CompanyID = ?1 order by MaterialGoodsCategoryCode", nativeQuery = true)
    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyID(UUID companyID);

    @Query(value = "select * from MaterialGoodsCategory b where b.CompanyID in ?1 order by MaterialGoodsCategoryCode", nativeQuery = true)
    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyID(List<UUID> companyID);

    @Query(value = "select * from MaterialGoodsCategory b where b.CompanyID = ?1 order by MaterialGoodsCategoryCode", nativeQuery = true)
    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryExceptID(UUID companyID);

    @Query(value = "select count(*) from MaterialGoodsCategory where UPPER(MaterialGoodsCategoryCode) = UPPER (?1) and CompanyID = ?2", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndMaterialGoodsCategoryCode(String materialGoodsCategoryCode, UUID org);

    @Query(value = "select * from MaterialGoodsCategory b where ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<MaterialGoodsCategory> findByParentMaterialGoodsCategoryID(UUID ParentID, UUID companyID);

    @Query(value = "select count(*) from MaterialGoodsCategory where ID <> ?1 and CompanyID = ?2  and MaterialGoodsCategoryCode = ?3 ", nativeQuery = true)
    Integer checkDuplicateMaterialGoodsCategory(UUID ID, UUID CompanyID, String materialGoodsCategoryCode);

    @Query(value = "select * from MaterialGoodsCategory where id in (select ParentID from MaterialGoodsCategory where ID = ?1)", nativeQuery = true)
    MaterialGoodsCategory findParentByChildID(UUID id);

    @Query(value = "select count(*) from MaterialGoodsCategory where ParentID =?1", nativeQuery = true)
    Integer countChildrenByID(UUID id);

    @Query(value = "select * from MaterialGoodsCategory  where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    List<MaterialGoodsCategory> listChildByParentID(UUID companyID, UUID parentID);

    @Query(value = "select count(*) from MaterialGoodsCategory where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    Integer countSiblings(UUID id, UUID ParentID);

    @Query(value = "select * from MaterialGoodsCategory b where ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<MaterialGoodsCategory> findByParentID(UUID parentAccountID, UUID companyID);
}
