package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.FixedAssetCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FixedAssetCategory.
 */
public interface FixedAssetCategoryService {

    /**
     * Save a fixedAssetCategory.
     *
     * @param fixedAssetCategory the entity to save
     * @return the persisted entity
     */
    FixedAssetCategory save(FixedAssetCategory fixedAssetCategory);

    /**
     * Get all the fixedAssetCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FixedAssetCategory> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FixedAssetCategory> findOne(UUID id);

    /**
     * Delete the "id" fixedAssetCategory.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
