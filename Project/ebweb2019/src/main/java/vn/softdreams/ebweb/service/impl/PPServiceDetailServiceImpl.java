package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.repository.PPServiceDetailRepository;
import vn.softdreams.ebweb.service.PPServiceDetailService;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PPServiceDetailServiceImpl implements PPServiceDetailService {

    private final Logger log = LoggerFactory.getLogger(PPServiceServiceImpl.class);

    private final PPServiceDetailRepository ppServiceDetailRepository;

    public PPServiceDetailServiceImpl(PPServiceDetailRepository ppServiceDetailRepository) {
        this.ppServiceDetailRepository = ppServiceDetailRepository;
    }

    @Override
    public List<PPServiceDetailDTO> findAllPPServiceDetailByPPServiceId(UUID ppServiceId) {
        return ppServiceDetailRepository.findAllPPServiceDetailByPPServiceId(ppServiceId);
    }

    @Override
    public List<PPServiceDetailDTO> findAllPPServiceDetailByPaymentVoucherID(UUID paymentVoucherID) {
        return ppServiceDetailRepository.findAllPPServiceDetailByPaymentVoucherID(paymentVoucherID);
    }

    @Override
    public void saveReceiveBillPPService(ReceiveBill receiveBill) {
        Integer vATRate;
        if (receiveBill.getvATRate() == 1) {
            vATRate = 5;
        } else if (receiveBill.getvATRate() == 2) {
            vATRate = 10;
        } else {
            vATRate = 0;
        }
        ppServiceDetailRepository.saveReceiveBillPPService(receiveBill,vATRate);
    }
}
