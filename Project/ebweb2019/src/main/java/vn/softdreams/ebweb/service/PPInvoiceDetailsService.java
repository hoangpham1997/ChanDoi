package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PPInvoiceDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPInvoiceDetails.
 */
public interface PPInvoiceDetailsService {

    /**
     * Save a pPInvoiceDetails.
     *
     * @param pPInvoiceDetails the entity to save
     * @return the persisted entity
     */
    PPInvoiceDetails save(PPInvoiceDetails pPInvoiceDetails);

    void saveReceiveBillPPInvoice(ReceiveBill receiveBill);
    void saveAllReceiveBill(ReceiveBill receiveBill);
    /**
     * Get all the pPInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPInvoiceDetails> findAll(Pageable pageable);


    /**
     * Get the "id" pPInvoiceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPInvoiceDetails> findOne(UUID id);

    /**
     * Delete the "id" pPInvoiceDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * author Huy Xoăn
     * @param materialGoodsID
     */
    List<LotNoDTO> getListLotNo(UUID materialGoodsID);

    Page<PPInvoiceConvertDTO> getPPInvoiceDetailsGetLicense(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, List<UUID> listID);

    ResultDTO getPPOrderNoById(UUID id);
}
