package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsSpecifications.
 */
public interface MaterialGoodsSpecificationsService {

    /**
     * Save a materialGoodsSpecifications.
     *
     * @param materialGoodsSpecifications the entity to save
     * @return the persisted entity
     */
    MaterialGoodsSpecifications save(MaterialGoodsSpecifications materialGoodsSpecifications);

    /**
     * Get all the materialGoodsSpecifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsSpecifications> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsSpecifications.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsSpecifications> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsSpecifications.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialGoodsSpecifications> findByMaterialGoodsID(UUID id);
}
