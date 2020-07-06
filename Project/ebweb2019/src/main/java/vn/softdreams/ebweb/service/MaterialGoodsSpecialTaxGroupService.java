package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsSpecialTaxGroup.
 */
public interface MaterialGoodsSpecialTaxGroupService {

    /**
     * Save a materialGoodsSpecialTaxGroup.
     *
     * @param materialGoodsSpecialTaxGroup the entity to save
     * @return the persisted entity
     */
    MaterialGoodsSpecialTaxGroup save(MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup);

    /**
     * Get all the materialGoodsSpecialTaxGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsSpecialTaxGroup> findAll(Pageable pageable);
    Page<MaterialGoodsSpecialTaxGroup> pageableAllMaterialGoodsSpecialTaxGroup(Pageable pageable);

    /**
     * Get the "id" materialGoodsSpecialTaxGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsSpecialTaxGroup> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsSpecialTaxGroup.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialGoodsSpecialTaxGroup> findAllMaterialGoodsSpecialTaxGroupByCompanyID();

    List<MaterialGoodsSpecialTaxGroup> getAllMaterialGoodsSpecialTaxGroupByCompanyID();

    List<MaterialGoodsSpecialTaxGroup> findAllExceptID(UUID id);

    MaterialGoodsSpecialTaxGroup findOneExceptID(UUID id);

    List<MaterialGoodsSpecialTaxGroup> findAllAccountLists();

    List<MaterialGoodsSpecialTaxGroup> findAllActive();

    List<MaterialGoodsSpecialTaxGroup> findMaterialGoodsSpecialTaxGroupsOne(UUID id);

}
