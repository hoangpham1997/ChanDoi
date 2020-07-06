package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.CPProductQuantum;
import vn.softdreams.ebweb.repository.CPProductQuantumRepository;
import vn.softdreams.ebweb.repository.RSInwardOutWardDetailsRepository;
import vn.softdreams.ebweb.repository.RepositoryLedgerRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPAllocationRateService;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.repository.CPAllocationRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositoryLedgerDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPAllocationRate.
 */
@Service
@Transactional
public class CPAllocationRateServiceImpl implements CPAllocationRateService {

    private final Logger log = LoggerFactory.getLogger(CPAllocationRateServiceImpl.class);

    private final CPAllocationRateRepository cPAllocationRateRepository;
    private final CPProductQuantumRepository cPProductQuantumRepository;
    private final RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;

    public CPAllocationRateServiceImpl(CPAllocationRateRepository cPAllocationRateRepository, CPProductQuantumRepository cPProductQuantumRepository, RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository, RepositoryLedgerRepository repositoryLedgerRepository) {
        this.cPAllocationRateRepository = cPAllocationRateRepository;
        this.cPProductQuantumRepository = cPProductQuantumRepository;
        this.rsInwardOutWardDetailsRepository = rsInwardOutWardDetailsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
    }

    /**
     * Save a cPAllocationRate.
     *
     * @param cPAllocationRate the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAllocationRate save(CPAllocationRate cPAllocationRate) {
        log.debug("Request to save CPAllocationRate : {}", cPAllocationRate);
        return cPAllocationRateRepository.save(cPAllocationRate);
    }

    /**
     * Get all the cPAllocationRates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAllocationRate> findAll(Pageable pageable) {
        log.debug("Request to get all CPAllocationRates");
        return cPAllocationRateRepository.findAll(pageable);
    }


    /**
     * Get one cPAllocationRate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAllocationRate> findOne(UUID id) {
        log.debug("Request to get CPAllocationRate : {}", id);
        return cPAllocationRateRepository.findById(id);
    }

    /**
     * Delete the cPAllocationRate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPAllocationRate : {}", id);
        cPAllocationRateRepository.deleteById(id);
    }

    public List<CPAllocationRate> findAllByCPPeriodID(UUID cPPeriodID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return cPAllocationRateRepository.findAllByCPPeriodID(currentUserLoginAndOrg.get().getOrg(), cPPeriodID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<CPAllocationRateDTO> findAllByListCostSets(List<UUID> uuids, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<CPAllocationRateDTO> cpAllocationRateDTOS = new ArrayList<CPAllocationRateDTO>();
            cpAllocationRateDTOS = cPAllocationRateRepository.findAllByListCostSets(uuids, currentUserLoginAndOrg.get().getOrg());
            List<CPProductQuantum> cpProductQuantums = cPProductQuantumRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
            List<RepositoryLedgerDTO> qCompletes = repositoryLedgerRepository.getByCostSetIDAndMaterialGoods(uuids, cpAllocationRateDTOS.stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList()), fromDate, toDate);
            for (CPAllocationRateDTO item: cpAllocationRateDTOS) {
                Optional<RepositoryLedgerDTO> qComplete = qCompletes.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID()) && x.getMaterialGoodsID().equals(item.getMaterialGoodsID())).findFirst();
                if (qComplete.isPresent()) {
                    item.setQuantity(qComplete.get().getQuantity());
                } else {
                    item.setQuantity(BigDecimal.ZERO);
                }
                Optional<CPProductQuantum> cpProductQuantum = cpProductQuantums.stream().filter(a -> a.getId().equals(item.getMaterialGoodsID())).findFirst();
                if (cpProductQuantum.isPresent()) {
                    item.setPriceQuantum(cpProductQuantum.get().getTotalCostAmount() != null ? cpProductQuantum.get().getTotalCostAmount() : BigDecimal.ZERO);
                } else {
                    item.setPriceQuantum(BigDecimal.ZERO);
                }
            }
            return cpAllocationRateDTOS;
        }
        throw new BadRequestAlertException("", "", "");
    }
}
