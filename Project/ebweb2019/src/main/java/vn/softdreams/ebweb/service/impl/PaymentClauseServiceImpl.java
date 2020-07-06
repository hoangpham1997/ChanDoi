package vn.softdreams.ebweb.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.BudgetItem;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.PaymentClauseService;
import vn.softdreams.ebweb.domain.PaymentClause;
import vn.softdreams.ebweb.repository.PaymentClauseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.softdreams.ebweb.service.dto.DetailDelFailCategoryDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;

/**
 * Service Implementation for managing PaymentClause.
 */
@Service
@Transactional
public class PaymentClauseServiceImpl implements PaymentClauseService {

    private final Logger log = LoggerFactory.getLogger(PaymentClauseServiceImpl.class);

    private final PaymentClauseRepository paymentClauseRepository;

    public PaymentClauseServiceImpl(PaymentClauseRepository paymentClauseRepository) {
        this.paymentClauseRepository = paymentClauseRepository;
    }

    /**
     * Save a paymentClause.
     *
     * @param paymentClause the entity to save
     * @return the persisted entity
     */

    @Override
    public PaymentClause save(PaymentClause paymentClause) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            paymentClause.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            // check duplicate paymentClause
            Integer count = this.paymentClauseRepository.checkDuplicatePaymentClause(paymentClause.getId() == null ? UUID.randomUUID() : paymentClause.getId(), currentUserLoginAndOrg.get().getOrg(), paymentClause.getPaymentClauseCode());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("","PaymentClause","pmcCodeDuplicate");
            }
        }
        log.debug("Request to save PaymentClause : {}", paymentClause);
        return paymentClauseRepository.save(paymentClause);
    }

    /**
     * Get all the paymentClauses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentClause> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentClauses");
        return paymentClauseRepository.findAll(pageable);
    }

    @Override
    public Page<PaymentClause> findAllPaymentClauses(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return paymentClauseRepository.findAllPaymentClausesByCompanyID(currentUserLoginAndOrg.get().getOrg(), pageable);
    }


    /**
     * Get one paymentClause by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentClause> findOne(UUID id) {
        log.debug("Request to get PaymentClause : {}", id);
        return paymentClauseRepository.findById(id);
    }

    /**
     * Delete the paymentClause by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PaymentClause : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Long count = null;
        count = paymentClauseRepository.checkExistedInAccountingObject(id, currentUserLoginAndOrg.get().getOrg());
        if (count != null && count > 0) {
            throw new BadRequestAlertException("", "paymentClause", "existed");
        }
        paymentClauseRepository.deleteById(id);
    }


    public List<PaymentClause> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return paymentClauseRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public HandlingResultDTO deleteMulti(List<PaymentClause> paymentClauses) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkBudgetId;
//        Collections.sort(budgetItems, (o1, o2) -> {
//            return o2.getGrade() - o1.getGrade();
//        });
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(paymentClauses.size());
        DetailDelFailCategoryDTO deleteMultiCategoryDTO;
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        List<DetailDelFailCategoryDTO> deleteMultiCategoryDTOS = new ArrayList<>();
        List<PaymentClause> listDelete = new ArrayList<>(paymentClauses);
        List<UUID> listIdDelete = new ArrayList<>();
        for (PaymentClause paymentClause : paymentClauses) {
            Long count = null;
            count = paymentClauseRepository.checkExistedInAccountingObject(paymentClause.getId(), currentUserLoginAndOrg.get().getOrg());
            if (count != null && count > 0) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan");
                BeanUtils.copyProperties(paymentClause, viewVoucherNo);
                viewVoucherNo.setAccountingObjectCode(paymentClause.getPaymentClauseCode());
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(viewVoucherNo);
            } else {
                listIdDelete.add(paymentClause.getId());
            }
        }
        paymentClauseRepository.deleteByIdIn(listIdDelete);
        handlingResultDTO.setCountSuccessVouchers(listIdDelete.size());
        handlingResultDTO.setCountTotalVouchers(paymentClauses.size());
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

//    @Override
//    public Integer checkDuplicatePaymentClause(PaymentClause paymentClause) {
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        return paymentClauseRepository.checkDuplicatePaymentClause(currentUserLoginAndOrg.get().getOrg(), paymentClause.getPaymentClauseCode());
//    }
}
