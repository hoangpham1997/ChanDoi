package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPAcceptanceDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;
import vn.softdreams.ebweb.web.rest.dto.EvaluateDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositoryLedgerDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPAcceptanceDetails.
 */
@Service
@Transactional
public class CPAcceptanceDetailsServiceImpl implements CPAcceptanceDetailsService {

    private final Logger log = LoggerFactory.getLogger(CPAcceptanceDetailsServiceImpl.class);

    private final CPAcceptanceDetailsRepository cPAcceptanceDetailsRepository;
    private final UserService userService;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final CPOPNRepository cpopnRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final CPProductQuantumRepository cpProductQuantumRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final CostSetRepository costSetRepository;

    public CPAcceptanceDetailsServiceImpl(CPAcceptanceDetailsRepository cPAcceptanceDetailsRepository, UserService userService, GeneralLedgerRepository generalLedgerRepository, CPOPNRepository cpopnRepository, RepositoryLedgerRepository repositoryLedgerRepository, CPProductQuantumRepository cpProductQuantumRepository, SAInvoiceRepository saInvoiceRepository, CostSetRepository costSetRepository) {
        this.cPAcceptanceDetailsRepository = cPAcceptanceDetailsRepository;
        this.userService = userService;
        this.generalLedgerRepository = generalLedgerRepository;
        this.cpopnRepository = cpopnRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.cpProductQuantumRepository = cpProductQuantumRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.costSetRepository = costSetRepository;
    }

    /**
     * Save a cPAcceptanceDetails.
     *
     * @param cPAcceptanceDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAcceptanceDetails save(CPAcceptanceDetails cPAcceptanceDetails) {
        log.debug("Request to save CPAcceptanceDetails : {}", cPAcceptanceDetails);        return cPAcceptanceDetailsRepository.save(cPAcceptanceDetails);
    }

    /**
     * Get all the cPAcceptanceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAcceptanceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CPAcceptanceDetails");
        return cPAcceptanceDetailsRepository.findAll(pageable);
    }


    /**
     * Get one cPAcceptanceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAcceptanceDetails> findOne(UUID id) {
        log.debug("Request to get CPAcceptanceDetails : {}", id);
        return cPAcceptanceDetailsRepository.findById(id);
    }

    /**
     * Delete the cPAcceptanceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPAcceptanceDetails : {}", id);
        cPAcceptanceDetailsRepository.deleteById(id);
    }

    @Override
    public List<CPAcceptanceDetails> evaluate(EvaluateDTO evaluateDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<CPAcceptanceDetails> cpAcceptanceDetails =  new ArrayList<>();
        List<GiaThanhPoPupDTO> cpExpenseLists1 = evaluateDTO.getcPExpenseLists1();
        List<GiaThanhPoPupDTO> cpExpenseLists2 = evaluateDTO.getcPExpenseLists2();
        List<CPAllocationGeneralExpenseDetails> cpAllocationGeneralExpenseDetails = evaluateDTO.getcPAllocationGeneralExpenseDetails();
        String costSetIDs = ",";
        for (UUID item : evaluateDTO.getcPPeriodDetails().stream().map(x -> x.getCostSetID()).collect(Collectors.toList())) {
            costSetIDs += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<CPAcceptanceDetails> cpAcceptanceDetailsOld = cPAcceptanceDetailsRepository.getByCosetID(costSetIDs,
            evaluateDTO.getFromDate(), currentUserLoginAndOrg.get().getOrg(), evaluateDTO.getcPPeriodID());
        List<CPOPN> cpopns = cpopnRepository.getByCosetID(evaluateDTO.getcPPeriodDetails().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()));
        List<CostSetDTO> costSetDTOS = costSetRepository.getCostSetAndRevenueAmount(evaluateDTO.getcPPeriodDetails().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()), evaluateDTO.getFromDate().toString(), evaluateDTO.getToDate().toString());
        for (CostSetDTO item: costSetDTOS) {
            CPAcceptanceDetails cpAcceptanceDetail = new CPAcceptanceDetails();
            cpAcceptanceDetail.setCostSetID(item.getCostSetID());

            BigDecimal revanueAmountOpening = BigDecimal.ZERO;

            double totalAmountCPEX0 = cpExpenseLists1.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).mapToDouble(x -> x.getAmount().doubleValue()).sum();
            BigDecimal totalAmountCPEXL0 = new BigDecimal(totalAmountCPEX0);

            double totalAmountCPEX1 = cpExpenseLists2.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).mapToDouble(x -> x.getAmount().doubleValue()).sum();
            BigDecimal totalAmountCPEXL1 = new BigDecimal(totalAmountCPEX1);

            double totalAmountAllocation = cpAllocationGeneralExpenseDetails.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).mapToDouble(x -> x.getAllocatedAmount().doubleValue()).sum();
            BigDecimal totalAmountAllo = new BigDecimal(totalAmountAllocation);

            Optional<CPAcceptanceDetails> cpADetailsOld = cpAcceptanceDetailsOld.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
            if (cpADetailsOld.isPresent()) {
                revanueAmountOpening = cpADetailsOld.get().getAmount();
            } else {
                Optional<CPOPN> cpopn = cpopns.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (cpopn.isPresent()) {
                    revanueAmountOpening = cpopn.get().getNotAcceptedAmount() == null ? BigDecimal.ZERO : cpopn.get().getNotAcceptedAmount();
                }
            }

            BigDecimal totalAmount = revanueAmountOpening.add(totalAmountCPEXL0).add(totalAmountAllo).subtract(totalAmountCPEXL1);
            cpAcceptanceDetail.setRevenueAmount(item.getRevenueAmount());
            cpAcceptanceDetail.setAmount(totalAmount);
            cpAcceptanceDetail.setAcceptedRate(BigDecimal.valueOf(100));
            cpAcceptanceDetail.setTotalAcceptedAmount(totalAmount);

            cpAcceptanceDetails.add(cpAcceptanceDetail);
        }
        return cpAcceptanceDetails;
    }
}
