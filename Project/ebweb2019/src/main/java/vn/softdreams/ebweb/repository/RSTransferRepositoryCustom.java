package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the RSInwardOutward entity.
 */
public interface RSTransferRepositoryCustom {

    Page<RSTransferSearchDTO> searchAllTransfer(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook);

    List<RSTransferDetailsDTO> getDetailsTransferById(UUID id, Integer typeID);

    RSTransferSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

    Integer findRowNumByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook);

    Page<RSTransferSearchDTO> searchAll(Pageable pageable, UUID accountingObject, UUID repository, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook);

    List<RSInwardOutwardDetailReportDTO> getRSTransferDetails(UUID id);

    List<RSInwardOutwardDetailReportDTO> getRSTransferDetail(UUID id);

    void multiDeleteRSTransfer(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildRSTransfer(String tableChildName, List<UUID> uuidList);

    void updateUnrecordRS(List<UUID> uuidList);
//    Page<RSTransferSearchDTO> searchAllTransfer(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook);

//    Integer findRowNumByID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook);

//    RSTransferSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

//    Integer findRowNumOutWardByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook);

//    RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

//    List<RSInwardOutWardDetails> getDetailsById(UUID id, Integer typeID);

//    List<RSInwardOutWardDetails> getDetailsOutWardById(UUID id, Integer typeID);

//    Page<ViewRSOutwardDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String soLamViec, String currencyID);

//    RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

//    void unRecordDetails(List<UUID> ids);

//    void deleteByListID(List<UUID> ids);

//    List<RSInwardOutwardDetailReportDTO> getRSInwardOutWardDetails(UUID id);

//    Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId);

//    void updateAfterDeleteMaterialQuantum(UUID id, List<MaterialQuantumDetails> materialQuantumDetails, UUID RSInwardOutwardID);

//    RSInwardOutwardSearchDTO findByID(UUID id);

//    void multiDeleteRSInwardOutward(UUID orgID, List<UUID> uuidList);

//    void multiDeleteChildRSInwardOutward(String tableChildName, List<UUID> uuidList);

//    void updateUnrecordRS(List<UUID> uuidList);
}
