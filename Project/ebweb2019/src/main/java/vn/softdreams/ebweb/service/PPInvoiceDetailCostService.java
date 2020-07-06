package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPInvoiceDetailCost;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPInvoiceDetailCost.
 */
public interface PPInvoiceDetailCostService {

    /**
     * Save a pPInvoiceDetailCost.
     *
     * @param pPInvoiceDetailCost the entity to save
     * @return the persisted entity
     */
    PPInvoiceDetailCost save(PPInvoiceDetailCost pPInvoiceDetailCost);

    /**
     * Get all the pPInvoiceDetailCost.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPInvoiceDetailCost> findAll(Pageable pageable);


    /**
     * Get the "id" pPInvoiceDetailCost.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPInvoiceDetailCost> findOne(UUID id);

    /**
     * Delete the "id" pPInvoiceDetailCost.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCost(UUID refId);

    List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCostByPaymentVoucherID(UUID paymentVoucherID);

    ResultDTO getSumAmountByPPServiceId(UUID ppServiceId, UUID ppInvoiceId, Boolean isHaiQuan);
}
