package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceForMCReceiptDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.SaInvoiceDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MCReceipt entity.
 */
@SuppressWarnings("unused")
public interface MCReceiptRepositoryCustom {

    MCReceipt findByAuditID(UUID mcAuditID);

    Page<MCReceiptDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook);

    List<SAInvoiceForMCReceiptDTO> findVoucherByListSAInvoice(List<UUID> uuids, int voucherTypeID);


    void multiDeleteMCReceipt(UUID org, List<UUID> uuidListMCReceipt);

    void multiDeleteByID(UUID org, List<UUID> uuidListMCReceipt);

    void mutipleUnRecord(List<UUID> listUnRecord, UUID orgID);

}
