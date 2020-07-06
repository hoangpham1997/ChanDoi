package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBDepositDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBDepositDetailTax.
 */
public interface MBDepositDetailTaxService {

    /**
     * Save a mBDepositDetailTax.
     *
     * @param mBDepositDetailTax the entity to save
     * @return the persisted entity
     */
    MBDepositDetailTax save(MBDepositDetailTax mBDepositDetailTax);

    /**
     * Get all the mBDepositDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBDepositDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" mBDepositDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBDepositDetailTax> findOne(UUID id);

    /**
     * Delete the "id" mBDepositDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
