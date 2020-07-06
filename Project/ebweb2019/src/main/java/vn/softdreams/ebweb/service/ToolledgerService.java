package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Toolledger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Toolledger.
 */
public interface ToolledgerService {

    /**
     * Save a toolledger.
     *
     * @param toolledger the entity to save
     * @return the persisted entity
     */
    Toolledger save(Toolledger toolledger);

    /**
     * Get all the toolledgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Toolledger> findAll(Pageable pageable);


    /**
     * Get the "id" toolledger.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Toolledger> findOne(UUID id);

    /**
     * Delete the "id" toolledger.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
