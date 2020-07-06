package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBDepositDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBDepositDetails.
 */
public interface MBDepositDetailsService {

    /**
     * Save a mBDepositDetails.
     *
     * @param mBDepositDetails the entity to save
     * @return the persisted entity
     */
    MBDepositDetails save(MBDepositDetails mBDepositDetails);

    /**
     * Get all the mBDepositDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBDepositDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mBDepositDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBDepositDetails> findOne(UUID id);

    /**
     * Delete the "id" mBDepositDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
