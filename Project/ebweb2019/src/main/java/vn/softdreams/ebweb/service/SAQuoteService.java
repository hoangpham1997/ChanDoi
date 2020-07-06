package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SAQuote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteViewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAQuote.
 */
public interface SAQuoteService {

    /**
     * Save a sAQuote.
     *
     * @param sAQuote the entity to save
     * @return the persisted entity
     */
    SAQuoteSaveDTO save(SAQuote sAQuote, Boolean isNew);

    /**
     * Get all the sAQuotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAQuote> findAll(Pageable pageable);

    /**
     * Author Hautv
     *
     * @param pageable
     * @return
     */
    Page<ViewSAQuoteDTO> findAllView(Pageable pageable);

    Page<SAQuoteDTO> findAllData(Pageable pageable);

    /**
     * Get the "id" sAQuote.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAQuote> findOne(UUID id);

    SAQuoteViewDTO findOneByID(UUID id);

    /**
     * Delete the "id" sAQuote.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    HandlingResultDTO deleteList(List<UUID> uuids);

    /**
     * @author mran
     */
    Page<SAQuote> findAll(Pageable pageable, SearchVoucher searchVoucher);

    /**
     * @return
     * @author mran
     */
    SAQuote findOneByRowNum(SearchVoucher searchVoucher, Number rowNum);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher);

    /**
     * @return
     * @author Hautv
     */
    Page<ViewSAQuoteDTO> getViewSAQuoteDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate,String currencyID);

    byte[] exportPdf(SearchVoucher searchVoucher1);

    byte[] exportExcel(SearchVoucher searchVoucher1);

    Boolean checkRelateVoucher(UUID id);

    Boolean deleteRefSAInvoiceAndRSInwardOutward(UUID id);
}
