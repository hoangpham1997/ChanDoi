package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.FixedAssetAccessories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FixedAssetAccessories.
 */
public interface FixedAssetAccessoriesService {

    /**
     * Save a fixedAssetAccessories.
     *
     * @param fixedAssetAccessories the entity to save
     * @return the persisted entity
     */
    FixedAssetAccessories save(FixedAssetAccessories fixedAssetAccessories);

    /**
     * Get all the fixedAssetAccessories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FixedAssetAccessories> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetAccessories.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FixedAssetAccessories> findOne(UUID id);

    /**
     * Delete the "id" fixedAssetAccessories.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
