package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBTellerPaperDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBTellerPaperDetailTax.
 */
public interface MBTellerPaperDetailTaxService {

    /**
     * Save a mBTellerPaperDetailTax.
     *
     * @param mBTellerPaperDetailTax the entity to save
     * @return the persisted entity
     */
    MBTellerPaperDetailTax save(MBTellerPaperDetailTax mBTellerPaperDetailTax);

    /**
     * Get all the mBTellerPaperDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBTellerPaperDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" mBTellerPaperDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBTellerPaperDetailTax> findOne(UUID id);

    /**
     * Delete the "id" mBTellerPaperDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
