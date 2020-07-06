package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;

import java.util.List;
import java.util.UUID;

public interface PPServiceDetailRepositoryCustom {
    List<PPServiceDetailDTO> findAllPPServiceDetailByPPServiceId(UUID ppServiceId);
    List<PPServiceDetailDTO> findAllPPServiceDetailByPaymentVoucherID(UUID paymentVoucherID);
    void saveReceiveBillPPService(ReceiveBill receiveBill,Integer vATRate);

    List<UUID> findPaymentVoucherIDByPPServiceID(List<UUID> ids);
}
