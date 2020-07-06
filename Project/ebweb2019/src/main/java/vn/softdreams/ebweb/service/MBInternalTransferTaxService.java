package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBInternalTransferTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBInternalTransferTax.
 */
public interface MBInternalTransferTaxService {

    /**
     * Save a mBInternalTransferTax.
     *
     * @param mBInternalTransferTax the entity to save
     * @return the persisted entity
     */
    MBInternalTransferTax save(MBInternalTransferTax mBInternalTransferTax);

    /**
     * Get all the mBInternalTransferTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBInternalTransferTax> findAll(Pageable pageable);


    /**
     * Get the "id" mBInternalTransferTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBInternalTransferTax> findOne(UUID id);

    /**
     * Delete the "id" mBInternalTransferTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
