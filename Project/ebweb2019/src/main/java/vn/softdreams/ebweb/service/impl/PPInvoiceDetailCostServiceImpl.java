package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPInvoiceDetailCost;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.service.PPInvoiceDetailCostService;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PPInvoiceDetailCost.
 */
@Service
@Transactional
public class PPInvoiceDetailCostServiceImpl implements PPInvoiceDetailCostService {

    private final Logger log = LoggerFactory.getLogger(PPInvoiceDetailCostServiceImpl.class);

    private final PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository;

    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";

    public PPInvoiceDetailCostServiceImpl(PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository) {
        this.pPInvoiceDetailCostRepository = pPInvoiceDetailCostRepository;
    }

    /**
     * Save a pPInvoiceDetailCost.
     *
     * @param pPInvoiceDetailCost the entity to save
     * @return the persisted entity
     */
    @Override
    public PPInvoiceDetailCost save(PPInvoiceDetailCost pPInvoiceDetailCost) {
        log.debug("Request to save PPInvoiceDetailCost : {}", pPInvoiceDetailCost);
        return pPInvoiceDetailCostRepository.save(pPInvoiceDetailCost);
    }


    /**
     * Get all the pPInvoiceDetailCost.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPInvoiceDetailCost> findAll(Pageable pageable) {
        log.debug("Request to get all PPInvoiceDetailCost");
        return pPInvoiceDetailCostRepository.findAll(pageable);
    }


    /**
     * Get one pPInvoiceDetailCost by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPInvoiceDetailCost> findOne(UUID id) {
        log.debug("Request to get PPInvoiceDetailCost : {}", id);
        return pPInvoiceDetailCostRepository.findById(id);
    }

    /**
     * Delete the pPInvoiceDetailCost by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPInvoiceDetailCost : {}", id);
        pPInvoiceDetailCostRepository.deleteById(id);
    }

    @Override
    public List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCost(UUID refId) {
        return pPInvoiceDetailCostRepository.getDetailCodeByPPInvoiceId(refId);
    }

    @Override
    public List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCostByPaymentVoucherID(UUID paymentVoucherID) {
        return pPInvoiceDetailCostRepository.getPPInvoiceDetailCostByPaymentVoucherID(paymentVoucherID);
    }

    @Override
    public ResultDTO getSumAmountByPPServiceId(UUID ppServiceId, UUID ppInvoiceId, Boolean isHaiQuan) {
        ResultDTO resultDTO = new ResultDTO();
        // todo set trạng thái success của resultDto
        resultDTO.setResult(pPInvoiceDetailCostRepository.sumTotalAmount(ppServiceId, ppInvoiceId, isHaiQuan));
        return resultDTO;
    }
}
