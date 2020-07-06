package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;

import org.springframework.data.domain.Page;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing IaPublishInvoiceDetails.
 */
public interface IaPublishInvoiceDetailsService {

    /**
     * Save a iaPublishInvoiceDetails.
     *
     * @param iaPublishInvoiceDetails the entity to save
     * @return the persisted entity
     */
    IaPublishInvoiceDetails save(IaPublishInvoiceDetails iaPublishInvoiceDetails);

    /**
     * Get all the iaPublishInvoiceDetails.
     *
     * @return the list of entities
     */
    List<IaPublishInvoiceDetails> findAll();


    /**
     * Get the "id" iaPublishInvoiceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IaPublishInvoiceDetails> findOne(Long id);

    /**
     * Delete the "id" iaPublishInvoiceDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<IAPublishInvoiceDetailDTO> getAllByCompany();

    List<IAPublishInvoiceDetailDTO> getFollowTransferByCompany();
}
