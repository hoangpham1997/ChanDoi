package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SaReturnRepositoryCustom {
    Page<SaReturnDTO> getAllSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                         String fromDate, String toDate, Boolean status,
                                         String freeText, UUID org, Integer typeID, String soLamViec);

    SaReturnDTO getNextSaReturnDTOs(Pageable pageable, UUID accountingObjectID, String currencyID,
                                    String fromDate, String toDate, Boolean status,
                                    String freeText, UUID org, Integer rowIndex, Integer typeID, String soLamViec, UUID id);

    Page<SaReturnRSInwardDTO> findAllSaReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String soLamViec);
    List<RSInwardOutwardDetailReportDTO> getSaReturnDetails(UUID id);

    void updateUnrecord(List<UUID> uuidList);

    List<SaReturnDTO> getAllSAReturnHasRSID(UUID org, Integer currentBook);

    Page<SAInvoicePopupDTO> getAllSaReturnSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, UUID org, String currentBook, String currencyID, List<UUID> listSAReturnID);

    void updateSaBill(Set<SaBillDetails> saBillDetails, UUID id, String invoiceNo);

    List<SAInvoiceDetailPopupDTO> getSAReturnDetailBySAReturnID(List<UUID> ids, String data);

    SaReturnDetails findDetaiBySaBillID(UUID id);

}
