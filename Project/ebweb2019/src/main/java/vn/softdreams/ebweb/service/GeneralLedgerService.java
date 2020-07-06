package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CollectionVoucher;
import vn.softdreams.ebweb.domain.GLCPExpenseList;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.domain.Record;
import vn.softdreams.ebweb.service.dto.CPAllocationDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestRecordListDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GeneralLedger.
 */
public interface GeneralLedgerService {

    /**
     * Save a generalLedger.
     *
     * @param generalLedger the entity to save
     * @return the persisted entity
     */
    GeneralLedger save(GeneralLedger generalLedger);

    /**
     * Get all the generalLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeneralLedger> findAll(Pageable pageable);


    /**
     * Get the "id" generalLedger.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeneralLedger> findOne(UUID id);

    /**
     * Delete the "id" generalLedger.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    boolean record(Object object, MessageDTO msg);

    /**
     * add by Hautv
     * @param refId
     * @return
     */
    List<GeneralLedger> fillByRefID(UUID refId);

    /**
     * add by Hautv
     * @param refID
     * @return
     */
    boolean unrecord(UUID refID, UUID repositoryLedgerID);

    boolean unrecord(List<UUID> refID, Boolean isDeleteRL);

    boolean unRecordTools(UUID refID);

    boolean unRecordFixedAsset(UUID refID);

//    boolean unrecord(UUID refID, UUID repositoryLedgerID, UUID paymentVoucherId);


    BigDecimal getTotalBalanceAmount(String auditDate, String currencyID);

    List<CollectionVoucher> getCollectionVoucher(String date, String currencyID, UUID bankID);

    List<CollectionVoucher> getSpendingVoucher(String date, String currencyID, UUID bankID);

    List<CollectionVoucher> getListMatch();

    Record record(Record record);

    HandlingResultDTO record(RequestRecordListDTO requestRecordListDTO);

    Record unRecord(Record record);

    HandlingResultDTO unRecord(RequestRecordListDTO requestRecordListDTO);

    UpdateDataDTO calculatingLiabilities(UUID accountingObjectId, String postDate);

    List<GLCPExpenseList> getListForCPExpenseList();

    List<ViewGLPayExceedCashDTO> getViewGLPayExceedCash();

    Page<GiaThanhPoPupDTO> getByRatioMethod(Pageable pageable, String fromDate, String toDate, List<UUID> costSetID, Integer status);

    CPAllocationDTO getByAllocationMethod(String fromDate, String toDate, List<UUID> costSetID, Integer status);

    List<GiaThanhPoPupDTO> getExpenseList(String fromDate, String toDate, List<UUID> costSetID);

}
