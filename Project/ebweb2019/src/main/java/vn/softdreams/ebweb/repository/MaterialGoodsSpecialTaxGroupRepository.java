package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsSpecialTaxGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsSpecialTaxGroupRepository extends JpaRepository<MaterialGoodsSpecialTaxGroup, UUID>, MaterialGoodsSpecialTaxGroupRepositoryCustom {
    @Query(value = "select * from MaterialGoodsSpecialTaxGroup b where b.CompanyID = ?1  order by MaterialGoodsSpecialTaxGroupCode asc ", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> getAllMaterialGoodsSpecialTaxGroupByCompanyID(UUID companyID);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup b where b.ID = ?1 order by MaterialGoodsSpecialTaxGroupCode asc ", nativeQuery = true)
    MaterialGoodsSpecialTaxGroup getOneMaterialGoodsSpecialTaxGroupByCompanyID(UUID ID);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup b where b.CompanyID = ?1 and isActive = 1", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> findAllMaterialGoodsSpecialTaxGroupkByCompanyID(UUID companyID);

    @Query(value = "select count(*) from MaterialGoodsSpecialTaxGroup where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    Integer countSiblings(UUID id, UUID ParentID);

    @Query(value = "select count(*) from MaterialGoodsSpecialTaxGroup where ID <> ?1 and CompanyID = ?2  and MaterialGoodsSpecialTaxGroupCode = ?3 ", nativeQuery = true)
    Integer checkDuplicateMaterialGoodsSpecialTaxGroup(UUID ID, UUID CompanyID, String MaterialGoodsSpecialTaxGroupCode);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup where id in (select ParentID from MaterialGoodsSpecialTaxGroup where ID = ?1)", nativeQuery = true)
    MaterialGoodsSpecialTaxGroup findParentByChildID(UUID id);

    @Query(value = "select count(*) from MaterialGoodsSpecialTaxGroup where ParentID =?1", nativeQuery = true)
    Integer countChildrenByID(UUID id);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup  where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> listChildByParentID(UUID companyID, UUID parentID);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup b where ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> findByParentID(UUID parentAccountID, UUID companyID);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup where CompanyID =?1 AND isActive = 1 order by MaterialGoodsSpecialTaxGroupCode asc", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> findAllByIsActive(UUID companyID);

    @Query(value = "select * from MaterialGoodsSpecialTaxGroup b where b.CompanyID = ?1 ORDER BY MaterialGoodsSpecialTaxGroupCode", nativeQuery = true)
    List<MaterialGoodsSpecialTaxGroup> findAllAccountLists(UUID companyID);
}
