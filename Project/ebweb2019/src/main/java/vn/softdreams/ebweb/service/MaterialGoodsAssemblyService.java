package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsAssembly;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsAssembly.
 */
public interface MaterialGoodsAssemblyService {

    /**
     * Save a materialGoodsAssembly.
     *
     * @param materialGoodsAssembly the entity to save
     * @return the persisted entity
     */
    MaterialGoodsAssembly save(MaterialGoodsAssembly materialGoodsAssembly);

    /**
     * Get all the materialGoodsAssemblies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsAssembly> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsAssembly.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsAssembly> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsAssembly.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialGoodsAssembly> findByMaterialGoodsID(UUID id);


}
