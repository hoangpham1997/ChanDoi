package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GOtherVoucherDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GOtherVoucherDetails.
 */
public interface GOtherVoucherDetailsService {

    /**
     * Save a gOtherVoucherDetails.
     *
     * @param gOtherVoucherDetails the entity to save
     * @return the persisted entity
     */
    GOtherVoucherDetails save(GOtherVoucherDetails gOtherVoucherDetails);

    /**
     * Get all the gOtherVoucherDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GOtherVoucherDetails> findAll(Pageable pageable);


    /**
     * Get the "id" gOtherVoucherDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GOtherVoucherDetails> findOne(UUID id);

    /**
     * Delete the "id" gOtherVoucherDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
