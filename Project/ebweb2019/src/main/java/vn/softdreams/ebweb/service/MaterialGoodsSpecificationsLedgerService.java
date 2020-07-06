package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsSpecificationsLedger.
 */
public interface MaterialGoodsSpecificationsLedgerService {

    /**
     * Save a materialGoodsSpecificationsLedger.
     *
     * @param materialGoodsSpecificationsLedger the entity to save
     * @return the persisted entity
     */
    MaterialGoodsSpecificationsLedger save(MaterialGoodsSpecificationsLedger materialGoodsSpecificationsLedger);

    /**
     * Get all the materialGoodsSpecificationsLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsSpecificationsLedger> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsSpecificationsLedger.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsSpecificationsLedger> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsSpecificationsLedger.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialGoodsSpecificationsLedgerDTO> findByMaterialGoodsID(UUID id, UUID repositoryID);

}
