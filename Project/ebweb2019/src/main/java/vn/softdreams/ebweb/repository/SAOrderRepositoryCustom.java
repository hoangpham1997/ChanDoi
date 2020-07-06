package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.SAOrderOutwardDTO;

import java.util.List;
import java.util.UUID;

public interface SAOrderRepositoryCustom {
    Page<SAOrderDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID);

    Page<SAOrderDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String currencyID, UUID objectId);

    void deleteRefSAInvoiceAndRSInwardOutward(UUID id);

    List<SAOrderDTO> findSAOrderDetailsDTO(UUID SAOrderId);

    // add by namnh
    // update lai quantity khi xoa list SAInvoiceID
    void updateQuantitySAOrder(List<UUID> uuidListSAOrderDetailID);

    void updateQuantitySAOrderBySA(List<SAInvoiceDetails> oldSADetailList);


//    void deleteByListID(List<UUID> uuids);

}
