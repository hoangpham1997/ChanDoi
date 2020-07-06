package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SAOrder;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.SAOrderOutwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SAOrderSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAOrderViewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAOrder.
 */
public interface SAOrderService {

    /**
     * Save a sAOrder.
     *
     * @param sAOrder the entity to save
     * @return the persisted entity
     */
    SAOrder save(SAOrder sAOrder);

    /**
     * Get all the sAOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAOrder> findAll(Pageable pageable);


    /**
     * Get the "id" sAOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAOrder> findOne(UUID id);

    /**
     * Delete the "id" sAOrder.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    HandlingResultDTO delete(List<UUID> uuids);

    Page<SAOrderDTO> findAllDTOByCompanyID(Pageable pageable);

    SAOrderViewDTO findOneDTO(UUID id);

    SAOrderSaveDTO saveDTO(SAOrder saOrder);

    Page<SAOrderDTO> getViewSAOrderDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID, UUID objectId);

    Boolean checkRelateVoucher(UUID id);

    Boolean deleteRefSAInvoiceAndRSInwardOutward(UUID id);

}
