package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Tools;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Tools.
 */
public interface ToolsService {

    /**
     * Save a tools.
     *
     * @param tools the entity to save
     * @return the persisted entity
     */
    Tools save(Tools tools);

    /**
     * Get all the tools.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Tools> findAll(Pageable pageable);


    /**
     * Get the "id" tools.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Tools> findOne(UUID id);

    /**
     * Delete the "id" tools.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<Tools> findAllByCompanyID(UUID orgID, boolean isDependent);

    List<Tools> getToolsActive();
}
