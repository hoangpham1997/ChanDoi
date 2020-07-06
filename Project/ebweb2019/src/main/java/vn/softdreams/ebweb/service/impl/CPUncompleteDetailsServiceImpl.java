package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPUncompleteDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
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
 * Service Implementation for managing CPUncompleteDetails.
 */
@Service
@Transactional
public class CPUncompleteDetailsServiceImpl implements CPUncompleteDetailsService {

    private final Logger log = LoggerFactory.getLogger(CPUncompleteDetailsServiceImpl.class);

    private final CPUncompleteDetailsRepository cPUncompleteDetailsRepository;
    private final UserService userService;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final CPOPNRepository cpopnRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final CPProductQuantumRepository cpProductQuantumRepository;

    public CPUncompleteDetailsServiceImpl(CPUncompleteDetailsRepository cPUncompleteDetailsRepository, UserService userService, GeneralLedgerRepository generalLedgerRepository, CPOPNRepository cpopnRepository, RepositoryLedgerRepository repositoryLedgerRepository, CPProductQuantumRepository cpProductQuantumRepository) {
        this.cPUncompleteDetailsRepository = cPUncompleteDetailsRepository;
        this.userService = userService;
        this.generalLedgerRepository = generalLedgerRepository;
        this.cpopnRepository = cpopnRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.cpProductQuantumRepository = cpProductQuantumRepository;
    }

    /**
     * Save a cPUncompleteDetails.
     *
     * @param cPUncompleteDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public CPUncompleteDetails save(CPUncompleteDetails cPUncompleteDetails) {
        log.debug("Request to save CPUncompleteDetails : {}", cPUncompleteDetails);        return cPUncompleteDetailsRepository.save(cPUncompleteDetails);
    }

    /**
     * Get all the cPUncompleteDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPUncompleteDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CPUncompleteDetails");
        return cPUncompleteDetailsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CPUncompleteDetails> findAllByCPPeriodID(UUID cPPeriodID) {
        log.debug("Request to get all CPUncompleteDetails");
        return cPUncompleteDetailsRepository.findAllByCPPeriodID(cPPeriodID);
    }


    /**
     * Get one cPUncompleteDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPUncompleteDetails> findOne(UUID id) {
        log.debug("Request to get CPUncompleteDetails : {}", id);
        return cPUncompleteDetailsRepository.findById(id);
    }

    /**
     * Delete the cPUncompleteDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPUncompleteDetails : {}", id);
        cPUncompleteDetailsRepository.deleteById(id);
    }

    @Override
    public List<CPUncompleteDetails> evaluate(EvaluateDTO evaluateDTO) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<CPUncompleteDetails> cpUncompleteDetails = new ArrayList<>();
        List<CPAllocationGeneralExpenseDetails> cpAllocationGeneralExpenseDetails = evaluateDTO.getcPAllocationGeneralExpenseDetails();
        List<CPUncompleteDTO> cpUncompletes = evaluateDTO.getcPUncompletes();

        if (!evaluateDTO.getMethod().equals(2)) {
//            List<GiaThanhPoPupDTO> cpExpenseLists1 = generalLedgerRepository.getExpenseList(evaluateDTO.getFromDate().toString(),
//                evaluateDTO.getToDate().toString(), evaluateDTO.getcPUncompletes().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()),
//                0, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
//            List<GiaThanhPoPupDTO> cpExpenseLists2 = generalLedgerRepository.getExpenseList(evaluateDTO.getFromDate().toString(),
//                evaluateDTO.getToDate().toString(), evaluateDTO.getcPUncompletes().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()),
//                1, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
            List<GiaThanhPoPupDTO> cpExpenseLists1 = evaluateDTO.getcPExpenseLists1();
            List<GiaThanhPoPupDTO> cpExpenseLists2 = evaluateDTO.getcPExpenseLists2();

            List<CPOPN> cpopns = cpopnRepository.getByCosetID(evaluateDTO.getcPUncompletes().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()));
            String costSetIDs = ",";
            for (UUID item : evaluateDTO.getcPUncompletes().stream().map(x -> x.getCostSetID()).collect(Collectors.toList())) {
                costSetIDs += Utils.uuidConvertToGUID(item).toString() + ",";
            }
            List<CPUncompleteDetails> cpUncompleteDetailsOld = cPUncompleteDetailsRepository.getByCosetID(costSetIDs,
                evaluateDTO.getFromDate(), currentUserLoginAndOrg.get().getOrg());
            List<RepositoryLedgerDTO> qCompletes = repositoryLedgerRepository.getByCostSetID(evaluateDTO.getcPUncompletes().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()), evaluateDTO.getFromDate().toString(), evaluateDTO.getToDate().toString());
            for (CPUncompleteDTO item: cpUncompletes) {
                CPUncompleteDetails cpUncompleteDetail = new CPUncompleteDetails();
                cpUncompleteDetail.setCostSetID(item.getCostSetID());

                BigDecimal quantityComplete = BigDecimal.ZERO;
                Optional<RepositoryLedgerDTO> qComplete = qCompletes.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (qComplete.isPresent()) {
                    quantityComplete = qComplete.get().getQuantity();
                    cpUncompleteDetail.setQuantity(quantityComplete);
                } else {
                    cpUncompleteDetail.setQuantity(BigDecimal.ZERO);
                }

                BigDecimal directMatetialAmountOPN = BigDecimal.ZERO;
                BigDecimal directLaborAmountOPN = BigDecimal.ZERO;
                BigDecimal machineMatetialAmountOPN = BigDecimal.ZERO;
                BigDecimal machineLaborAmountOPN = BigDecimal.ZERO;
                BigDecimal machineToolsAmountOPN = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmountOPN = BigDecimal.ZERO;
                BigDecimal machineServiceAmountOPN = BigDecimal.ZERO;
                BigDecimal machineGeneralAmountOPN = BigDecimal.ZERO;
                BigDecimal generalMatetialAmountOPN = BigDecimal.ZERO;
                BigDecimal generalLaborAmountOPN = BigDecimal.ZERO;
                BigDecimal generalToolsAmountOPN = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmountOPN = BigDecimal.ZERO;
                BigDecimal generalServiceAmountOPN = BigDecimal.ZERO;
                BigDecimal otherGeneralAmountOPN = BigDecimal.ZERO;

                Optional<CPUncompleteDetails> cpUDetailsOld = cpUncompleteDetailsOld.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (cpUDetailsOld.isPresent()) {
                    directMatetialAmountOPN = cpUDetailsOld.get().getDirectMatetialAmount();
                    directLaborAmountOPN = cpUDetailsOld.get().getDirectLaborAmount();
                    machineMatetialAmountOPN = cpUDetailsOld.get().getMachineMatetialAmount();
                    machineLaborAmountOPN = cpUDetailsOld.get().getMachineLaborAmount();
                    machineToolsAmountOPN = cpUDetailsOld.get().getMachineToolsAmount();
                    machineDepreciationAmountOPN = cpUDetailsOld.get().getMachineDepreciationAmount();
                    machineServiceAmountOPN = cpUDetailsOld.get().getMachineServiceAmount();
                    machineGeneralAmountOPN = cpUDetailsOld.get().getMachineGeneralAmount();
                    generalMatetialAmountOPN = cpUDetailsOld.get().getGeneralMatetialAmount();
                    generalLaborAmountOPN = cpUDetailsOld.get().getGeneralLaborAmount();
                    generalToolsAmountOPN = cpUDetailsOld.get().getGeneralToolsAmount();
                    generalDepreciationAmountOPN = cpUDetailsOld.get().getGeneralDepreciationAmount();
                    generalServiceAmountOPN = cpUDetailsOld.get().getGeneralServiceAmount();
                    otherGeneralAmountOPN = cpUDetailsOld.get().getOtherGeneralAmount();
                } else {
                    Optional<CPOPN> cpopn = cpopns.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                    if (cpopn.isPresent()) {
                        directMatetialAmountOPN = cpopn.get().getDirectMaterialAmount() != null ? cpopn.get().getDirectMaterialAmount() : BigDecimal.ZERO;
                        directLaborAmountOPN = cpopn.get().getDirectLaborAmount() != null ? cpopn.get().getDirectLaborAmount() : BigDecimal.ZERO;
                        machineMatetialAmountOPN = cpopn.get().getMachineMaterialAmount() != null ? cpopn.get().getMachineMaterialAmount() : BigDecimal.ZERO;
                        machineLaborAmountOPN = cpopn.get().getMachineLaborAmount() != null ? cpopn.get().getMachineLaborAmount() : BigDecimal.ZERO;
                        machineToolsAmountOPN = cpopn.get().getMachineToolsAmount() != null ? cpopn.get().getMachineToolsAmount() : BigDecimal.ZERO;
                        machineDepreciationAmountOPN = cpopn.get().getMachineDepreciationAmount() != null ? cpopn.get().getMachineDepreciationAmount() : BigDecimal.ZERO;
                        machineServiceAmountOPN = cpopn.get().getMachineServiceAmount() != null ? cpopn.get().getMachineServiceAmount() : BigDecimal.ZERO;
                        machineGeneralAmountOPN = cpopn.get().getMachineGeneralAmount() != null ? cpopn.get().getMachineGeneralAmount() : BigDecimal.ZERO;
                        generalMatetialAmountOPN = cpopn.get().getGeneralMaterialAmount() != null ? cpopn.get().getGeneralMaterialAmount() : BigDecimal.ZERO;
                        generalLaborAmountOPN = cpopn.get().getGeneralLaborAmount() != null ? cpopn.get().getGeneralLaborAmount() : BigDecimal.ZERO;
                        generalToolsAmountOPN = cpopn.get().getGeneralToolsAmount() != null ? cpopn.get().getGeneralToolsAmount() : BigDecimal.ZERO;
                        generalDepreciationAmountOPN = cpopn.get().getGeneralDepreciationAmount() != null ? cpopn.get().getGeneralDepreciationAmount() : BigDecimal.ZERO;
                        generalServiceAmountOPN = cpopn.get().getGeneralServiceAmount() != null ? cpopn.get().getGeneralServiceAmount() : BigDecimal.ZERO;
                        otherGeneralAmountOPN = cpopn.get().getOtherGeneralAmount() != null ? cpopn.get().getOtherGeneralAmount() : BigDecimal.ZERO;
                    }
                }

                BigDecimal directMatetialAmountEL1 = BigDecimal.ZERO;
                BigDecimal directLaborAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineMatetialAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineLaborAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineToolsAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineServiceAmountEL1 = BigDecimal.ZERO;
                BigDecimal machineGeneralAmountEL1 = BigDecimal.ZERO;
                BigDecimal generalMatetialAmountEL1 = BigDecimal.ZERO;
                BigDecimal generalLaborAmountEL1 = BigDecimal.ZERO;
                BigDecimal generalToolsAmountEL1 = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmountEL1 = BigDecimal.ZERO;
                BigDecimal generalServiceAmountEL1 = BigDecimal.ZERO;
                BigDecimal otherGeneralAmountEL1 = BigDecimal.ZERO;

                List<GiaThanhPoPupDTO> cpEL1 = cpExpenseLists1.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).collect(Collectors.toList());
                for (GiaThanhPoPupDTO cpEL: cpEL1) {
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL1 = directMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL1 = directLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL1 = machineMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL1 = machineLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL1 = machineToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL1 = machineDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL1 = machineServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL1 = machineGeneralAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL1 = generalMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL1 = generalLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL1 = generalToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL1 = generalDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL1 = generalServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
                        otherGeneralAmountEL1 = otherGeneralAmountEL1.add(cpEL.getAmount());
                    }
                }

                BigDecimal directMatetialAmountEL2 = BigDecimal.ZERO;
                BigDecimal directLaborAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineMatetialAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineLaborAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineToolsAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineServiceAmountEL2 = BigDecimal.ZERO;
                BigDecimal machineGeneralAmountEL2 = BigDecimal.ZERO;
                BigDecimal generalMatetialAmountEL2 = BigDecimal.ZERO;
                BigDecimal generalLaborAmountEL2 = BigDecimal.ZERO;
                BigDecimal generalToolsAmountEL2 = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmountEL2 = BigDecimal.ZERO;
                BigDecimal generalServiceAmountEL2 = BigDecimal.ZERO;
                BigDecimal otherGeneralAmountEL2 = BigDecimal.ZERO;

                List<GiaThanhPoPupDTO> cpEL2 = cpExpenseLists2.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).collect(Collectors.toList());
                for (GiaThanhPoPupDTO cpEL: cpEL2) {
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL2 = directMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL2 = directLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL2 = machineMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL2 = machineLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL2 = machineToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL2 = machineDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL2 = machineServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL2 = machineGeneralAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL2 = generalMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL2 = generalLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL2 = generalToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL2 = generalDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL2 = generalServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
                        otherGeneralAmountEL2 = otherGeneralAmountEL2.add(cpEL.getAmount());
                    }
                }

                BigDecimal directMatetialAmountAE = BigDecimal.ZERO;
                BigDecimal directLaborAmountAE = BigDecimal.ZERO;
                BigDecimal machineMatetialAmountAE = BigDecimal.ZERO;
                BigDecimal machineLaborAmountAE = BigDecimal.ZERO;
                BigDecimal machineToolsAmountAE = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmountAE = BigDecimal.ZERO;
                BigDecimal machineServiceAmountAE = BigDecimal.ZERO;
                BigDecimal machineGeneralAmountAE = BigDecimal.ZERO;
                BigDecimal generalMatetialAmountAE = BigDecimal.ZERO;
                BigDecimal generalLaborAmountAE = BigDecimal.ZERO;
                BigDecimal generalToolsAmountAE = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmountAE = BigDecimal.ZERO;
                BigDecimal generalServiceAmountAE = BigDecimal.ZERO;
                BigDecimal otherGeneralAmountAE = BigDecimal.ZERO;

                List<CPAllocationGeneralExpenseDetails> cpAE = cpAllocationGeneralExpenseDetails.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).collect(Collectors.toList());
                for (CPAllocationGeneralExpenseDetails cpEL: cpAE) {
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP)
                    && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("621")))) {
                        directMatetialAmountAE = directMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("622")))) {
                        directLaborAmountAE = directLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6231")))) {
                        machineMatetialAmountAE = machineMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6232")))) {
                        machineLaborAmountAE = machineLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6233")))) {
                        machineToolsAmountAE = machineToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6234")))) {
                        machineDepreciationAmountAE = machineDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6237")))) {
                        machineServiceAmountAE = machineServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6238")))) {
                        machineGeneralAmountAE = machineGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6271")))) {
                        generalMatetialAmountAE = generalMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6272")))) {
                        generalLaborAmountAE = generalLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6273")))) {
                        generalToolsAmountAE = generalToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6274")))) {
                        generalDepreciationAmountAE = generalDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6277")))) {
                        generalServiceAmountAE = generalServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6278")))) {
                        otherGeneralAmountAE = otherGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    }
                }

                BigDecimal directMatetialAmountIN = directMatetialAmountEL1.subtract(directMatetialAmountEL2).add(directMatetialAmountAE);
                BigDecimal directLaborAmountIN = directLaborAmountEL1.subtract(directLaborAmountEL2).add(directLaborAmountAE);
                BigDecimal machineMatetialAmountIN = machineMatetialAmountEL1.subtract(machineMatetialAmountEL2).add(machineMatetialAmountAE);
                BigDecimal machineLaborAmountIN = machineLaborAmountEL1.subtract(machineLaborAmountEL2).add(machineLaborAmountAE);
                BigDecimal machineToolsAmountIN = machineToolsAmountEL1.subtract(machineToolsAmountEL2).add(machineToolsAmountAE);
                BigDecimal machineDepreciationAmountIN = machineDepreciationAmountEL1.subtract(machineDepreciationAmountEL2).add(machineDepreciationAmountAE);
                BigDecimal machineServiceAmountIN = machineServiceAmountEL1.subtract(machineServiceAmountEL2).add(machineServiceAmountAE);
                BigDecimal machineGeneralAmountIN = machineGeneralAmountEL1.subtract(machineGeneralAmountEL2).add(machineGeneralAmountAE);
                BigDecimal generalMatetialAmountIN = generalMatetialAmountEL1.subtract(generalMatetialAmountEL2).add(generalMatetialAmountAE);
                BigDecimal generalLaborAmountIN = generalLaborAmountEL1.subtract(generalLaborAmountEL2).add(generalLaborAmountAE);
                BigDecimal generalToolsAmountIN = generalToolsAmountEL1.subtract(generalToolsAmountEL2).add(generalToolsAmountAE);
                BigDecimal generalDepreciationAmountIN = generalDepreciationAmountEL1.subtract(generalDepreciationAmountEL2).add(generalDepreciationAmountAE);
                BigDecimal generalServiceAmountIN = generalServiceAmountEL1.subtract(generalServiceAmountEL2).add(generalServiceAmountAE);
                BigDecimal otherGeneralAmountIN = otherGeneralAmountEL1.subtract(otherGeneralAmountEL2).add(otherGeneralAmountAE);

                BigDecimal directMatetialAmount = BigDecimal.ZERO;
                BigDecimal directLaborAmount = BigDecimal.ZERO;
                BigDecimal machineMatetialAmount = BigDecimal.ZERO;
                BigDecimal machineLaborAmount = BigDecimal.ZERO;
                BigDecimal machineToolsAmount = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmount = BigDecimal.ZERO;
                BigDecimal machineServiceAmount = BigDecimal.ZERO;
                BigDecimal machineGeneralAmount = BigDecimal.ZERO;
                BigDecimal generalMatetialAmount = BigDecimal.ZERO;
                BigDecimal generalLaborAmount = BigDecimal.ZERO;
                BigDecimal generalToolsAmount = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmount = BigDecimal.ZERO;
                BigDecimal generalServiceAmount = BigDecimal.ZERO;
                BigDecimal otherGeneralAmount = BigDecimal.ZERO;

                if (evaluateDTO.getMethod().equals(0) && item.getQuantityClosing().doubleValue() != 0 && item.getRate().doubleValue() != 0) {
                    directMatetialAmount = (directMatetialAmountOPN.add(directMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    directLaborAmount = (directLaborAmountOPN.add(directLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineMatetialAmount = (machineMatetialAmountOPN.add(machineMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineLaborAmount = (machineLaborAmountOPN.add(machineLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineToolsAmount = (machineToolsAmountOPN.add(machineToolsAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineDepreciationAmount = (machineDepreciationAmountOPN.add(machineDepreciationAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineServiceAmount = (machineServiceAmountOPN.add(machineServiceAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    machineGeneralAmount = (machineGeneralAmountOPN.add(machineGeneralAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    generalMatetialAmount = (generalMatetialAmountOPN.add(generalMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    generalLaborAmount = (generalLaborAmountOPN.add(generalLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    generalToolsAmount = (generalToolsAmountOPN.add(generalToolsAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    generalDepreciationAmount = (generalDepreciationAmountOPN.add(generalDepreciationAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    generalServiceAmount = (generalServiceAmountOPN.add(generalServiceAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                    otherGeneralAmount = (otherGeneralAmountOPN.add(otherGeneralAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing().multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64)));
                } else if (evaluateDTO.getMethod().equals(1) && item.getQuantityClosing().doubleValue() != 0 && item.getRate().doubleValue() != 0) {
                    directMatetialAmount = (directMatetialAmountOPN.add(directMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    directLaborAmount = (directLaborAmountOPN.add(directLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineMatetialAmount = (machineMatetialAmountOPN.add(machineMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineLaborAmount = (machineLaborAmountOPN.add(machineLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineToolsAmount = (machineToolsAmountOPN.add(machineToolsAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineDepreciationAmount = (machineDepreciationAmountOPN.add(machineDepreciationAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineServiceAmount = (machineServiceAmountOPN.add(machineServiceAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    machineGeneralAmount = (machineGeneralAmountOPN.add(machineGeneralAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    generalMatetialAmount = (generalMatetialAmountOPN.add(generalMatetialAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    generalLaborAmount = (generalLaborAmountOPN.add(generalLaborAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    generalToolsAmount = (generalToolsAmountOPN.add(generalToolsAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    generalDepreciationAmount = (generalDepreciationAmountOPN.add(generalDepreciationAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    generalServiceAmount = (generalServiceAmountOPN.add(generalServiceAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                    otherGeneralAmount = (otherGeneralAmountOPN.add(otherGeneralAmountIN)).
                        divide((quantityComplete.add((item.getQuantityClosing()))), MathContext.DECIMAL64).
                        multiply((item.getQuantityClosing()));
                }


                cpUncompleteDetail.setDirectMatetialAmount(directMatetialAmount);
                cpUncompleteDetail.setDirectLaborAmount(directLaborAmount);
                cpUncompleteDetail.setMachineMatetialAmount(machineMatetialAmount);
                cpUncompleteDetail.setMachineLaborAmount(machineLaborAmount);
                cpUncompleteDetail.setMachineToolsAmount(machineToolsAmount);
                cpUncompleteDetail.setMachineDepreciationAmount(machineDepreciationAmount);
                cpUncompleteDetail.setMachineServiceAmount(machineServiceAmount);
                cpUncompleteDetail.setMachineGeneralAmount(machineGeneralAmount);
                cpUncompleteDetail.setGeneralMatetialAmount(generalMatetialAmount);
                cpUncompleteDetail.setGeneralLaborAmount(generalLaborAmount);
                cpUncompleteDetail.setGeneralToolsAmount(generalToolsAmount);
                cpUncompleteDetail.setGeneralDepreciationAmount(generalDepreciationAmount);
                cpUncompleteDetail.setGeneralServiceAmount(generalServiceAmount);
                cpUncompleteDetail.setOtherGeneralAmount(otherGeneralAmount);
                cpUncompleteDetail.setTotalCostAmount(directMatetialAmount.add(directLaborAmount).add(machineMatetialAmount).add(machineLaborAmount)
                    .add(machineToolsAmount).add(machineDepreciationAmount).add(machineServiceAmount).add(machineGeneralAmount)
                    .add(generalMatetialAmount).add(generalLaborAmount).add(generalToolsAmount).add(generalDepreciationAmount)
                    .add(generalServiceAmount).add(otherGeneralAmount));
                cpUncompleteDetails.add(cpUncompleteDetail);
            }
        } else {
            List<CPProductQuantum> cpProductQuantums = cpProductQuantumRepository.getByCosetID(evaluateDTO.getcPUncompletes().stream().map(x -> x.getQuantumID()).collect(Collectors.toList()));
            for (CPUncompleteDTO item: cpUncompletes) {
                Optional<CPProductQuantum> cpProductQuantum = cpProductQuantums.stream().filter(x -> x.getId().equals(item.getQuantumID())).findFirst();
                if (cpProductQuantum.isPresent()) {
                    if (cpProductQuantum.get().getDirectMaterialAmount() == null) {
                        cpProductQuantum.get().setDirectMaterialAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getDirectLaborAmount() == null) {
                        cpProductQuantum.get().setDirectLaborAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineMaterialAmount() == null) {
                        cpProductQuantum.get().setMachineMaterialAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineLaborAmount() == null) {
                        cpProductQuantum.get().setMachineLaborAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineToolsAmount() == null) {
                        cpProductQuantum.get().setMachineToolsAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineDepreciationAmount() == null) {
                        cpProductQuantum.get().setMachineDepreciationAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineServiceAmount() == null) {
                        cpProductQuantum.get().setMachineServiceAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getMachineGeneralAmount() == null) {
                        cpProductQuantum.get().setMachineGeneralAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getGeneralMaterialAmount() == null) {
                        cpProductQuantum.get().setGeneralMaterialAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getGeneralLaborAmount() == null) {
                        cpProductQuantum.get().setGeneralLaborAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getGeneralToolsAmount() == null) {
                        cpProductQuantum.get().setGeneralToolsAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getGeneralDepreciationAmount() == null) {
                        cpProductQuantum.get().setGeneralDepreciationAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getGeneralServiceAmount() == null) {
                        cpProductQuantum.get().setGeneralServiceAmount(BigDecimal.ZERO);
                    }
                    if (cpProductQuantum.get().getOtherGeneralAmount() == null) {
                        cpProductQuantum.get().setOtherGeneralAmount(BigDecimal.ZERO);
                    }
                }
                CPUncompleteDetails cpUncompleteDetail = new CPUncompleteDetails();
                cpUncompleteDetail.setCostSetID(item.getCostSetID());

                BigDecimal directMatetialAmount = BigDecimal.ZERO;
                BigDecimal directLaborAmount = BigDecimal.ZERO;
                BigDecimal machineMatetialAmount = BigDecimal.ZERO;
                BigDecimal machineLaborAmount = BigDecimal.ZERO;
                BigDecimal machineToolsAmount = BigDecimal.ZERO;
                BigDecimal machineDepreciationAmount = BigDecimal.ZERO;
                BigDecimal machineServiceAmount = BigDecimal.ZERO;
                BigDecimal machineGeneralAmount = BigDecimal.ZERO;
                BigDecimal generalMatetialAmount = BigDecimal.ZERO;
                BigDecimal generalLaborAmount = BigDecimal.ZERO;
                BigDecimal generalToolsAmount = BigDecimal.ZERO;
                BigDecimal generalDepreciationAmount = BigDecimal.ZERO;
                BigDecimal generalServiceAmount = BigDecimal.ZERO;
                BigDecimal otherGeneralAmount = BigDecimal.ZERO;

                if (cpProductQuantum.isPresent() && item.getQuantityClosing().doubleValue() != 0 && item.getRate().doubleValue() != 0) {
                    directMatetialAmount = cpProductQuantum.get().getDirectMaterialAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    directLaborAmount = cpProductQuantum.get().getDirectLaborAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineMatetialAmount = cpProductQuantum.get().getMachineMaterialAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineLaborAmount = cpProductQuantum.get().getMachineLaborAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineToolsAmount = cpProductQuantum.get().getMachineToolsAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineDepreciationAmount = cpProductQuantum.get().getMachineDepreciationAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineServiceAmount = cpProductQuantum.get().getMachineServiceAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    machineGeneralAmount = cpProductQuantum.get().getMachineGeneralAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    generalMatetialAmount = cpProductQuantum.get().getGeneralMaterialAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    generalLaborAmount = cpProductQuantum.get().getGeneralLaborAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    generalToolsAmount = cpProductQuantum.get().getGeneralToolsAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    generalDepreciationAmount = cpProductQuantum.get().getGeneralDepreciationAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    generalServiceAmount = cpProductQuantum.get().getGeneralServiceAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                    otherGeneralAmount = cpProductQuantum.get().getOtherGeneralAmount().multiply(item.getQuantityClosing())
                        .multiply(item.getRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                }

                cpUncompleteDetail.setDirectMatetialAmount(directMatetialAmount);
                cpUncompleteDetail.setDirectLaborAmount(directLaborAmount);
                cpUncompleteDetail.setMachineMatetialAmount(machineMatetialAmount);
                cpUncompleteDetail.setMachineLaborAmount(machineLaborAmount);
                cpUncompleteDetail.setMachineToolsAmount(machineToolsAmount);
                cpUncompleteDetail.setMachineDepreciationAmount(machineDepreciationAmount);
                cpUncompleteDetail.setMachineServiceAmount(machineServiceAmount);
                cpUncompleteDetail.setMachineGeneralAmount(machineGeneralAmount);
                cpUncompleteDetail.setGeneralMatetialAmount(generalMatetialAmount);
                cpUncompleteDetail.setGeneralLaborAmount(generalLaborAmount);
                cpUncompleteDetail.setGeneralToolsAmount(generalToolsAmount);
                cpUncompleteDetail.setGeneralDepreciationAmount(generalDepreciationAmount);
                cpUncompleteDetail.setGeneralServiceAmount(generalServiceAmount);
                cpUncompleteDetail.setOtherGeneralAmount(otherGeneralAmount);
                cpUncompleteDetail.setTotalCostAmount(directMatetialAmount.add(directLaborAmount).add(machineMatetialAmount).add(machineLaborAmount)
                    .add(machineToolsAmount).add(machineDepreciationAmount).add(machineServiceAmount).add(machineGeneralAmount)
                    .add(generalMatetialAmount).add(generalLaborAmount).add(generalToolsAmount).add(generalDepreciationAmount)
                    .add(generalServiceAmount).add(otherGeneralAmount));

                cpUncompleteDetails.add(cpUncompleteDetail);
            }
        }
        return cpUncompleteDetails;
    }
}
