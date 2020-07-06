package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;

import java.util.List;
import java.util.UUID;

public interface PPServiceDetailService {
    List<PPServiceDetailDTO> findAllPPServiceDetailByPPServiceId(UUID ppServiceId);
    List<PPServiceDetailDTO> findAllPPServiceDetailByPaymentVoucherID(UUID paymentVoucherID);
    void saveReceiveBillPPService(ReceiveBill receiveBill);
}
