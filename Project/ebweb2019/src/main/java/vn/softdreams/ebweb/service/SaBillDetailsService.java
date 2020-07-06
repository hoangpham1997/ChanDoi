package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SaBillDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.SaBillDetailDTO;

import java.util.UUID;

/**
 * Service Interface for managing SaBillDetails.
 */
public interface SaBillDetailsService {

    /**
     * Save a saBillDetails.
     *
     * @param saBillDetails the entity to save
     * @return the persisted entity
     */
    SaBillDetails save(SaBillDetails saBillDetails);

    /**
     * Get all the saBillDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaBillDetails> findAll(Pageable pageable);


    /**
     * Get the "id" saBillDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaBillDetailDTO findAll(UUID id);

    /**
     * Delete the "id" saBillDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
