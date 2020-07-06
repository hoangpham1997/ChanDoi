package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.IARegisterInvoiceDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing IARegisterInvoiceDetails.
 */
public interface IARegisterInvoiceDetailsService {

    /**
     * Save a iARegisterInvoiceDetails.
     *
     * @param iARegisterInvoiceDetails the entity to save
     * @return the persisted entity
     */
    IARegisterInvoiceDetails save(IARegisterInvoiceDetails iARegisterInvoiceDetails);

    /**
     * Get all the iARegisterInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IARegisterInvoiceDetails> findAll(Pageable pageable);


    /**
     * Get the "id" iARegisterInvoiceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IARegisterInvoiceDetails> findOne(UUID id);

    /**
     * Delete the "id" iARegisterInvoiceDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
