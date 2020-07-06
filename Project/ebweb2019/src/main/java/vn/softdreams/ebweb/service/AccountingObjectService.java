package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountingObject.
 */
public interface AccountingObjectService {

    /**
     * Save a accountingObject.
     *
     * @param accountingObject the entity to save
     * @return the persisted entity
     */
    AccountingObjectSaveDTO save(AccountingObject accountingObject);

    /**
     * Get all the accountingObjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountingObject> findAll(Pageable pageable);

    Page<AccountingObject> findAll1(Pageable pageable, Integer typeAccountingObject, SearchVoucherAccountingObjects searchVoucherAccountingObjects);

    Page<AccountingObject> findAllEmloyee1(Pageable pageable, SearchVoucherEmployee searchVoucherEmployee);

    Page<AccountingObject> pageableAllAccountingObjects(Pageable pageable, Integer typeAccountingObject);


    /**
     * add by namnh
     *
     * @return
     */
    Page<AccountingObject> findAll();


    /**
     * Get the "id" accountingObject.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountingObject> findOne(UUID id);
    Optional<AccountingObject> findOneWithPageable(Pageable pageable, UUID accID);
    Page<AccountingObject> findVoucherByAccountingObjectID(Pageable pageable, UUID id);

    /**
     * Delete the "id" accountingObject.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    HandlingResultDTO delete(List<UUID> uuids);
    HandlingResultDTO deleteEmployee(List<UUID> uuids);

    List<AccountingObject> getAllAccountingObjectsActive();
    List<AccountingObject> getByAllAccountingObjectsActive();

    /**
     * @return lấy ra nhân viên thực hiện cho combobox
     */
    List<AccountingObject> getAllAccountingObjectsEmployee();

    List<AccountingObject> getAccountingObjectsForProvider();

    List<AccountingObject> getAccountingObjectsForEmployee();

    List<AccountingObject> getAccountingObjectsForEmployees();

    List<AccountingObjectDTO> getAccountingObjectsByGroupID(UUID id, Boolean branch, UUID companyID);
    /**
     * Hautv
     * @param isObjectType12
     * @return
     */
    List<AccountingObjectDTO> findAllDTO(Boolean isObjectType12);

    /***
     * @author jsp
     * @param taskMethod 0: mua dich vu
     */
    List<AccountingObjectDTO> findAllDTO(Integer taskMethod);

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     */
    Page<PPPayVendorDTO> getPPPayVendorByDate(String fromDate, String toDate);

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     * @param accountObjectID : mã nhà cung cấp
     */
    Page<PPPayVendorBillDTO> getPPPayVendorBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID);

    List<AccountingObject> getAccountingObjectsActive();

    List<AccountingObject> getAccountingObjectsTransferActive();

    List<AccountingObject> getAccountingObjectsActiveCustom();

    Page<SAReceiptDebitDTO> getSAReceiptDebitByDate(String fromDate, String toDate);

    Page<SAReceiptDebitBillDTO> getSAReceiptDebitBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID);
    List<AccountingObject> findAllAccountingObjectByCompanyID();

    List<AccountingObject> getAccountingObjectsActiveForRSInwardOutward();

    List<AccountingObject> getAllAccountingObjectsForRSInwardOutward();

    List<AccountingObject> getAccountingObjectsRSOutward();

    List<AccountingObject> getAllAccountingObjectsRSOutward();

    List<AccountingObjectDTO> getAccountingObjectByGroupIDKH(UUID id, Boolean similarBranch, UUID companyID);

    List<AccountingObjectDTO> findAllDTOAll(Integer taskMethod);

    List<AccountingObjectDTO> findAllAccountingObjectDTO(Boolean isObjectType12);

    List<AccountingObject> getAccountingObjectActiveByReportRequest(RequestReport requestReport);

    List<AccountingObject> getAccountingObjectsRSTransfer();

    List<AccountingObjectDTO> getAccountingObjectsByGroupIDSimilarBranch(UUID id, Boolean similarBranch, UUID companyID);

    List<AccountingObject> getAccountingObjectByGroupIDKHSimilarBranch(UUID id, Boolean similarBranch, UUID companyID);

    List<AccountingObject> getAllAccountingObjectsEmployeeSimilarBranch(Boolean similarBranch, UUID companyID);

    List<AccountingObject> getAllAccountingObjectsByCompanyID(UUID orgID, boolean isDependent);

    List<AccountingObjectDTO> findAllAccountingObjectByCompany(UUID companyID, Boolean dependent);
}
