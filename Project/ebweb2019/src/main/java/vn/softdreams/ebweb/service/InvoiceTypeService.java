package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.InvoiceType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing InvoiceType.
 */
public interface InvoiceTypeService {

    /**
     * Save a invoiceType.
     *
     * @param invoiceType the entity to save
     * @return the persisted entity
     */
    InvoiceType save(InvoiceType invoiceType);

    /**
     * Get all the invoiceTypes.
     *
     * @return the list of entities
     */
    List<InvoiceType> findAll();


    /**
     * Get the "id" invoiceType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InvoiceType> findOne(UUID id);

    /**
     * Delete the "id" invoiceType.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
