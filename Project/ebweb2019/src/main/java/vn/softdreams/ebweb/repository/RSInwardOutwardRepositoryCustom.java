package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.service.dto.ViewRSOutwardDTO;
import vn.softdreams.ebweb.web.rest.dto.IWVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the RSInwardOutward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RSInwardOutwardRepositoryCustom {

    Page<RSInwardOutwardSearchDTO> searchAll(Pageable pageable, UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook);

    Page<RSInwardOutwardSearchDTO> searchAllOutWard(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook);

    Integer findRowNumByID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook);

    RSInwardOutwardSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

    Integer findRowNumOutWardByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook);

    RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

    List<RSInwardOutWardDetails> getDetailsById(UUID id, Integer typeID);

    List<RSInwardOutWardDetails> getDetailsOutWardById(UUID id, Integer typeID);

    Page<ViewRSOutwardDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String soLamViec, String currencyID);

    RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook);

    void unRecordDetails(List<UUID> ids);

    void deleteByListID(List<UUID> ids);

    List<RSInwardOutwardDetailReportDTO> getRSInwardOutWardDetails(UUID id);

    Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId);

    void updateAfterDeleteMaterialQuantum(UUID id, List<MaterialQuantumDetails> materialQuantumDetails, UUID RSInwardOutwardID);

    RSInwardOutwardSearchDTO findByID(UUID id);

    void multiDeleteRSInwardOutward(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildRSInwardOutward(String tableChildName, List<UUID> uuidList);

    void updateUnrecordRS(List<UUID> uuidList);

    List<RSInwardOutwardDetailsDTO> getListUpdateUnitPrice(List<UUID> materialGoodsIDs, Integer soLamViec, LocalDate fromDate, LocalDate toDate, List<UUID> costSetIDs);

}
