package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.domain.PPInvoice;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceSearchDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MCPayment entity.
 */

public interface MCPaymentRepositoryCustom {

    MCPayment findByAuditID(UUID mcAuditID);

    List<MCPaymentDetails> findMCPaymentDetails(UUID ID);

    Page<MCPaymentDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook);

    UpdateDataDTO getNoBookById(UUID paymentVoucherID);

    Object findIDRef(UUID uuid, Integer typeID);

    List<PPInvoiceDTO> findVoucherByListPPInvoiceID(List<UUID> uuids, int voucherTypeID);

    void multiDeleteMCPayment(UUID org, List<UUID> uuidListMCPayment);

    void multiDeleteByID(UUID org, List<UUID> uuidListMCPayment);

}
