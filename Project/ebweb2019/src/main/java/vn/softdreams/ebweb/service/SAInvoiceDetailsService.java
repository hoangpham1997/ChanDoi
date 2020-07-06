package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SAInvoiceDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAInvoiceDetails.
 */
public interface SAInvoiceDetailsService {

    /**
     * Save a sAInvoiceDetails.
     *
     * @param sAInvoiceDetails the entity to save
     * @return the persisted entity
     */
    SAInvoiceDetails save(SAInvoiceDetails sAInvoiceDetails);

    /**
     * Get all the sAInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAInvoiceDetails> findAll(Pageable pageable);


    /**
     * Get the "id" sAInvoiceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAInvoiceDetails> findOne(UUID id);

    /**
     * Delete the "id" sAInvoiceDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<SAInvoiceDetails> getSAInvoiceDetailsByID(UUID sAInvoiceID);

    List<SAInvoiceDetails> getSAInvoiceDetailsByMCReceiptID(UUID mCReceiptID);
    List<SAInvoiceDetails> getSAInvoiceDetailsByMCDepositID(UUID mBDepositID);

    List<SAInvoiceDetailsOutWardDTO> findAllDetailsById(List<UUID> id);
}
