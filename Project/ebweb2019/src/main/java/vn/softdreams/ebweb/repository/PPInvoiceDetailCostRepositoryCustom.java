package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PPInvoiceDetailCostRepositoryCustom {

    BigDecimal sumTotalAmount(UUID ppServiceId, UUID ppInvoiceId, Boolean isHaiQuan);

    List<PPInvoiceDetailCostDTO> getDetailCodeByPPInvoiceId(UUID ppInvoiceId);

    List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCostByPaymentVoucherID(UUID paymentVoucherID);
}
