package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCAuditDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCAuditDetails.
 */
public interface MCAuditDetailsService {

    /**
     * Save a mCAuditDetails.
     *
     * @param mCAuditDetails the entity to save
     * @return the persisted entity
     */
    MCAuditDetails save(MCAuditDetails mCAuditDetails);

    /**
     * Get all the mCAuditDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCAuditDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mCAuditDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCAuditDetails> findOne(UUID id);

    /**
     * Delete the "id" mCAuditDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
