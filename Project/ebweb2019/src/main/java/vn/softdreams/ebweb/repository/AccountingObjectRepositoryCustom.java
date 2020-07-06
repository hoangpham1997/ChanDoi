package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.SearchVoucherAccountingObjects;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.*;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObject entity.
 */
public interface AccountingObjectRepositoryCustom {
    List<AccountingObjectDTO> findAllDTO(List<UUID> companyID, Boolean isObjectType12);

    List<AccountingObjectDTO> findAllAccountingObjectDTO(List<UUID> companyID, Boolean isObjectType12);

    /***
     * @author jsp
     * @param taskMethod 0: mua dich vu
     * @return
     */
    List<AccountingObjectDTO> findAllDTO(Integer taskMethod, List<UUID> org);

    List<AccountingObjectDTO> findAllDTO(String className, UUID org);

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     */
    Page<PPPayVendorDTO> getPPPayVendorByDate(String fromDate, String toDate, UUID companyID, Integer soLamViec);

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     * @param accountObjectID : mã nhà cung cấp
     */
    Page<PPPayVendorBillDTO> getPPPayVendorBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID, UUID companyID, Integer soLamViec);

    Page<SAReceiptDebitDTO> getSAReceiptDebitByDate(String fromDate, String toDate, UUID org, Integer soLamViec);

    Page<SAReceiptDebitBillDTO> getSAReceiptDebitBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID, UUID org, Integer soLamViec);

    Page<AccountingObject> pageableAllAccountingObjects(Pageable sort, Integer typeAccountingObject, List<UUID> org);


    Page<AccountingObject> getAllByListCompany(Pageable pageable, Integer typeAccountingObject,List<UUID> listCompanyID);

    Page<AccountingObject> findVoucherByAccountingObjectID(Pageable pageable, UUID id);

    Page<AccountingObject> findAll1(Pageable sort,Integer typeAccountingObject, SearchVoucherAccountingObjects searchVoucherAccountingObjects, List<UUID> companyID);

    Page<AccountingObject> findAllEmployee1(Pageable sort, SearchVoucherEmployee searchVoucherEmployee, List<UUID> companyID);

    List<AccountingObject> findByIsActiveCustom(List<UUID> org);

    List<AccountingObjectDTO> getAccountingObjectByGroupID(List<UUID> org, Boolean similarBranch, Boolean checkShared, UUID id);

    List<AccountingObjectDTO> getAccountingObjectByGroupIDKH(List<UUID> org, Boolean similarBranch, Boolean checkShared, UUID id);

    List<AccountingObjectDTO> findAllDTOAll(Integer taskMethod, List<UUID> allCompanyByCompanyIdAndCode);

    List<AccountingObjectDTO> getAccountingObjectsByGroupIDSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared, UUID id);

    List<AccountingObject> getAccountingObjectByGroupIDKHSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared, UUID id);

    List<AccountingObject> getAllAccountingObjectsEmployeeSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared);

    List<AccountingObject> insertBulk(List<AccountingObject> accountingObjects);

    List<AccountingObject> getAllAccountingObjectsByCompanyID(UUID orgID, boolean isDependent);

    List<AccountingObjectDTO> getAllAccountingObjectByCompany(List<UUID> comIds);
}
