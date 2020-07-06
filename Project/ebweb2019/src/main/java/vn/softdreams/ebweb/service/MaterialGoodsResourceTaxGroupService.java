package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsResourceTaxGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsResourceTaxGroup.
 */
public interface MaterialGoodsResourceTaxGroupService {

    /**
     * Save a materialGoodsResourceTaxGroup.
     *
     * @param materialGoodsResourceTaxGroup the entity to save
     * @return the persisted entity
     */
    MaterialGoodsResourceTaxGroup save(MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup);

    /**
     * Get all the materialGoodsResourceTaxGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsResourceTaxGroup> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsResourceTaxGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsResourceTaxGroup> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsResourceTaxGroup.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
