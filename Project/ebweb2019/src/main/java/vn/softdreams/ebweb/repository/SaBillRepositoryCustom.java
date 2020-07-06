package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.service.dto.SABillReportDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import java.util.List;
import java.util.UUID;

public interface SaBillRepositoryCustom {

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param invoiceTemplate
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param invoiceNo
     * @param freeText
     * @param org
     * @return
     */
    Page<SaBillDTO> getAllSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                     String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo,
                                     String freeText, UUID org, String soLamViec);

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param invoiceTemplate
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param invoiceNo
     * @param freeText
     * @param org
     * @param id
     * @return
     */
    SaBillDTO getNextSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo,
                                String freeText, UUID org, Integer rowIndex, String soLamViec, UUID id);

    Page<SaBillCreatedDTO> saBillCreated(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, UUID org, String currentBook);

    List<SaBillCreatedDetailDTO> saBillCreatedDetail(List<UUID> saBillIdList);

    List<SaBill> insertBulk(List<SaBill> saBills);

    List<SABillReportDTO> findSABillDetailsDTO(UUID SABillId);

    void multiDeleteSABill(UUID org, List<UUID> uuidListSABill);

    List<SaBillDTO> getAllSaBillDTOByCompayIDs(UUID org);

}
