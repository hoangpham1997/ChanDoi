package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPOrder.
 */
public interface PporderService {

    /**
     * Save a pporder.
     *
     * @param pporder the entity to save
     * @return the persisted entity
     */
    PPOrder save(PPOrder pporder);

    /**
     * Save a pporder.
     *
     * @param pporder the entity to save
     * @return the persisted entity
     */
    PPOrderSaveDTO save(PPOrderSaveDTO pporder);

    /**
     * Get all the pporders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPOrder> findAll(Pageable pageable);


    /**
     * Get the "id" pporder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPOrder> findOne(UUID id);

    /**
     * Delete the "id" pporder.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    PPOrder findByRowNum(String searchDTO, Integer rowNum) throws IOException;

    Page<PPOrder> searchAll(Pageable pageable, String searchDTO) throws IOException;

    List<RefVoucherDTO> refVouchersByPPOrderID(UUID id);

    Page<PPOrderDTO> findAllPPOrderDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, Integer type, List<UUID> itemsSelected, String currency);

    Integer findByRowNumByID(String searchDTO, UUID id) throws IOException;

    ResultDTO validateDelete(UUID id);

    byte[] exportPdf(String searchDTO) throws IOException;

    byte[] exportExcel(String searchDTO) throws IOException;

    ResultDTO deletePPOrderAndReferences(UUID id);

    ResultDTO checkReferencesCount(UUID id);

    ResultDTO deleteReferences(UUID id);

    Number findTotalSum(Pageable pageable, String searchDTO) throws IOException;

    HandlingResultDTO multiDelete(List<PPOrder> ppOrders);

    ResultDTO validateCheckDuplicat(UUID id);
}
