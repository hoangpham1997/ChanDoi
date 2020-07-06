package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.IAInvoiceTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing IAInvoiceTemplate.
 */
public interface IAInvoiceTemplateService {

    /**
     * Save a iAInvoiceTemplate.
     *
     * @param iAInvoiceTemplate the entity to save
     * @return the persisted entity
     */
    IAInvoiceTemplate save(IAInvoiceTemplate iAInvoiceTemplate);

    /**
     * Get all the iAInvoiceTemplates.
     *
     * @return the list of entities
     */
    List<IAInvoiceTemplate> findAll();


    /**
     * Get the "id" iAInvoiceTemplate.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IAInvoiceTemplate> findOne(UUID id);

    /**
     * Delete the "id" iAInvoiceTemplate.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
