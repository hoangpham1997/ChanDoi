package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsCategorySaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsCategory.
 */
public interface MaterialGoodsCategoryService {

    /**
     * Save a materialGoodsCategory.
     *
     * @param materialGoodsCategory the entity to save
     * @return the persisted entity
     */
    MaterialGoodsCategory save(MaterialGoodsCategory materialGoodsCategory);

    /**
     * Get all the materialGoodsCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsCategory> findAll(Pageable pageable);
    Page<MaterialGoodsCategory> pageableAllMaterialGoodsCategories(Pageable pageable);


    /**
     * Get the "id" materialGoodsCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsCategory> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsCategory.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @author Hautv
     * @param id
     * @return
     */
    List<String> GetListOrderFixCodeParentID(UUID id);

    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyID(Boolean similarBranch);

    List<MaterialGoodsCategory> findAllExceptID(UUID id);

    List<MaterialGoodsCategory> getMaterialGoodsCategoryForReport(UUID companyID, Boolean similarBranch);


    List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(Boolean similarBranch, UUID companyID);
}
