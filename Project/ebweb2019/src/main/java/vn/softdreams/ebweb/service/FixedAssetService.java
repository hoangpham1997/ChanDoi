package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.FixedAsset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FixedAsset.
 */
public interface FixedAssetService {

    /**
     * Save a fixedAsset.
     *
     * @param fixedAsset the entity to save
     * @return the persisted entity
     */
    FixedAsset save(FixedAsset fixedAsset);

    /**
     * Get all the fixedAssets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FixedAsset> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAsset.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FixedAsset> findOne(UUID id);

    /**
     * Delete the "id" fixedAsset.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
