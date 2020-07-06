package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Record;
import vn.softdreams.ebweb.domain.SaReturn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnViewDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing SaReturn.
 */
public interface SaReturnService {

    /**
     * Save a saReturn.
     *
     * @param saReturn the entity to save
     * @return the persisted entity
     */
    SaReturnSaveDTO save(SaReturnSaveDTO saReturn) throws InvocationTargetException, IllegalAccessException;

    /**
     * Get all the saReturns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaReturn> findAll(Pageable pageable);


    /**
     * Get the "id" saReturn.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaReturnViewDTO findOne(UUID id);

    /**
     * Delete the "id" saReturn.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param currencyID
     * @param fromDate
     * @param toDate
     * @param status
     * @param freeText
     * @param rowIndex
     * @param typeID
     * @param id
     * @return
     */
    SaReturnViewDTO getNextSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                        String fromDate, String toDate, Boolean status, String freeText,
                                        Integer rowIndex, Integer typeID, UUID id);

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param currencyID
     * @param fromDate
     * @param toDate
     * @param status
     * @param freeText
     * @param typeID
     * @return
     */
    Page<SaReturnDTO> getAllSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                         String fromDate, String toDate, Boolean status, String freeText, Integer typeID);

    /**
     *  @Author hieugie
     *
     * @return
     */
    SaReturnSearchDTO getAllSearchData();

    /**
     * @Author hieugie
     *
     * @return
     * @param accountingObjectID
     * @param currencyID
     * @param fromDate
     * @param toDate
     * @param recorded
     * @param freeText
     * @param typeID
     */
    byte[] export(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean recorded, String freeText, Integer typeID);

    /**
     * @Author hieugie
     *
     * @return
     * @param accountingObjectID
     * @param currencyID
     * @param fromDate
     * @param toDate
     * @param recorded
     * @param freeText
     * @param typeID
     */
    byte[] exportPdf(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean recorded, String freeText, Integer typeID);

    Page<SaReturnRSInwardDTO>findAllSaReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate);

    List<SaReturnDetails> findAllDetailsById(List<UUID> id);

    Record unrecord(Record record);

    Boolean checkRelateVoucher(UUID saReturnID);

    HandlingResultDTO multiDelete(List<SaReturn> saReturns);

    HandlingResultDTO multiUnrecord(List<SaReturn> saReturns);

}
