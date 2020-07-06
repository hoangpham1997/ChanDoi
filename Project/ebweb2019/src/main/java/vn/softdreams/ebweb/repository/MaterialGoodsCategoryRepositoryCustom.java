package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.Unit;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsCategoryRepositoryCustom {
    public List<String> GetListOrderFixCodeParentID(UUID id);
    Page<MaterialGoodsCategory> pageableAllMaterialGoodsCategories(Pageable sort, UUID org);

    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared);
}
