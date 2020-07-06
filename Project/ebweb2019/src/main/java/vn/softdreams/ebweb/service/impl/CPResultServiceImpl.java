package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CPPeriodDetailDTO;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CalculateCostDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositoryLedgerDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPResult.
 */
@Service
@Transactional
public class CPResultServiceImpl implements CPResultService {

    private final Logger log = LoggerFactory.getLogger(CPResultServiceImpl.class);

    private final CPResultRepository cPResultRepository;
    private final UserService userService;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final CPOPNRepository cpopnRepository;
    private final CPUncompleteDetailsRepository cPUncompleteDetailsRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final CostSetRepository costSetRepository;

    public CPResultServiceImpl(CPResultRepository cPResultRepository, UserService userService, GeneralLedgerRepository generalLedgerRepository, CPOPNRepository cpopnRepository, CPUncompleteDetailsRepository cPUncompleteDetailsRepository, RepositoryLedgerRepository repositoryLedgerRepository, CostSetRepository costSetRepository) {
        this.cPResultRepository = cPResultRepository;
        this.userService = userService;
        this.generalLedgerRepository = generalLedgerRepository;
        this.cpopnRepository = cpopnRepository;
        this.cPUncompleteDetailsRepository = cPUncompleteDetailsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.costSetRepository = costSetRepository;
    }

    /**
     * Save a cPResult.
     *
     * @param cPResult the entity to save
     * @return the persisted entity
     */
    @Override
    public CPResult save(CPResult cPResult) {
        log.debug("Request to save CPResult : {}", cPResult);        return cPResultRepository.save(cPResult);
    }

    /**
     * Get all the cPResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPResult> findAll(Pageable pageable) {
        log.debug("Request to get all CPResults");
        return cPResultRepository.findAll(pageable);
    }


    /**
     * Get one cPResult by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPResult> findOne(UUID id) {
        log.debug("Request to get CPResult : {}", id);
        return cPResultRepository.findById(id);
    }

    /**
     * Delete the cPResult by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPResult : {}", id);
        cPResultRepository.deleteById(id);
    }

    @Override
    public List<CPResult> calculateCost(CalculateCostDTO calculateCostDTO) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer lamTronDonGia = 0;
        Integer lamTronDonGiaNT = 0;
        Integer lamTronTienVN = 0;
        Integer lamTronTienNT = 0;
        for (SystemOption sys: userDTO.getSystemOption()) {
            if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGia)) {
                lamTronDonGia = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGiaNT)) {
                lamTronDonGiaNT = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_TienVND)) {
                lamTronTienVN = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_NgoaiTe)) {
                lamTronTienNT = Integer.valueOf(sys.getData());
            }
        }
        List<CPResult> cpResults = new ArrayList<>();
        List<CPPeriodDetails> cpPeriodDetails = calculateCostDTO.getcPPeriodDetails();
        List<CPAllocationGeneralExpenseDetails> cpAllocationGeneralExpenseDetails = calculateCostDTO.getcPAllocationGeneralExpenseDetails();
        List<CPUncompleteDetails> cpUncompleteDetails = calculateCostDTO.getcPUncompleteDetails();
        List<CPAllocationRate> cpAllocationRates = calculateCostDTO.getcPAllocationRates();
//        List<GiaThanhPoPupDTO> cpExpenseLists1 = generalLedgerRepository.getExpenseList(calculateCostDTO.getFromDate().toString(),
//            calculateCostDTO.getToDate().toString(), calculateCostDTO.getcPUncompleteDetails().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()),
//            0, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
//        List<GiaThanhPoPupDTO> cpExpenseLists2 = generalLedgerRepository.getExpenseList(calculateCostDTO.getFromDate().toString(),
//            calculateCostDTO.getToDate().toString(), calculateCostDTO.getcPUncompleteDetails().stream().map(x -> x.getCostSetID()).collect(Collectors.toList()),
//            1, Utils.PhienSoLamViec(userDTO), currentUserLoginAndOrg.get().getOrg());
        List<GiaThanhPoPupDTO> cpExpenseLists1 = calculateCostDTO.getcPExpenseLists1();
        List<GiaThanhPoPupDTO> cpExpenseLists2 = calculateCostDTO.getcPExpenseLists2();

        List<CPOPN> cpopns = cpopnRepository.getByCosetID(cpPeriodDetails.stream().map(x -> x.getCostSetID()).collect(Collectors.toList()));
        String costSetIDs = ",";
        for (UUID item : cpPeriodDetails.stream().map(x -> x.getCostSetID()).collect(Collectors.toList())) {
            costSetIDs += Utils.uuidConvertToGUID(item).toString() + ",";
        }
        List<CPUncompleteDetails> cpUncompleteDetailsOld = cPUncompleteDetailsRepository.getByCosetID(costSetIDs,
            calculateCostDTO.getToDate(), currentUserLoginAndOrg.get().getOrg());

        if (cpAllocationRates == null || cpAllocationRates.size() == 0) {
            List<RepositoryLedgerDTO> qCompletes = repositoryLedgerRepository.getByCostSetID(cpPeriodDetails.stream().map(x -> x.getCostSetID()).collect(Collectors.toList()), calculateCostDTO.getFromDate().toString(), calculateCostDTO.getToDate().toString());
            for (CPPeriodDetails item: cpPeriodDetails) {
                CPResult cpResult = new CPResult();
                CPUncompleteDetails cpUncompleteDetail = null;
                Optional<CPUncompleteDetails> cpUOptional = cpUncompleteDetails.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (cpUOptional.isPresent()) {
                    cpUncompleteDetail = cpUOptional.get();
                }

                cpResult.setCostSetID(item.getCostSetID());
                BigDecimal quantityComplete = BigDecimal.ZERO;
                Optional<RepositoryLedgerDTO> qComplete = qCompletes.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (qComplete.isPresent()) {
                    quantityComplete = qComplete.get().getQuantity();
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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL1 = directMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL1 = directLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL1 = machineMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL1 = machineLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL1 = machineToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL1 = machineDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL1 = machineServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL1 = machineGeneralAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL1 = generalMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL1 = generalLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL1 = generalToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL1 = generalDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL1 = generalServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL2 = directMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL2 = directLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL2 = machineMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL2 = machineLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL2 = machineToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL2 = machineDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL2 = machineServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL2 = machineGeneralAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL2 = generalMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL2 = generalLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL2 = generalToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL2 = generalDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL2 = generalServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("621")))) {
                        directMatetialAmountAE = directMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("622")))) {
                        directLaborAmountAE = directLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6231")))) {
                        machineMatetialAmountAE = machineMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6232")))) {
                        machineLaborAmountAE = machineLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6233")))) {
                        machineToolsAmountAE = machineToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6234")))) {
                        machineDepreciationAmountAE = machineDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6237")))) {
                        machineServiceAmountAE = machineServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6238")))) {
                        machineGeneralAmountAE = machineGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6271")))) {
                        generalMatetialAmountAE = generalMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6272")))) {
                        generalLaborAmountAE = generalLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6273")))) {
                        generalToolsAmountAE = generalToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6274")))) {
                        generalDepreciationAmountAE = generalDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6277")))) {
                        generalServiceAmountAE = generalServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC)
                        && (cpEL.getAccountNumber() == null || cpEL.getAccountNumber().startsWith("6278")))) {
                        otherGeneralAmountAE = otherGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    }
                }

                BigDecimal directMatetialAmount = directMatetialAmountOPN.add(directMatetialAmountEL1).subtract(directMatetialAmountEL2).add(directMatetialAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getDirectMatetialAmount() : BigDecimal.ZERO));
                BigDecimal directLaborAmount = directLaborAmountOPN.add(directLaborAmountEL1).subtract(directLaborAmountEL2).add(directLaborAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getDirectLaborAmount() : BigDecimal.ZERO));
                BigDecimal machineMatetialAmount = machineMatetialAmountOPN.add(machineMatetialAmountEL1).subtract(machineMatetialAmountEL2).add(machineMatetialAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineMatetialAmount() : BigDecimal.ZERO));
                BigDecimal machineLaborAmount = machineLaborAmountOPN.add(machineLaborAmountEL1).subtract(machineLaborAmountEL2).add(machineLaborAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineLaborAmount() : BigDecimal.ZERO));
                BigDecimal machineToolsAmount = machineToolsAmountOPN.add(machineToolsAmountEL1).subtract(machineToolsAmountEL2).add(machineToolsAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineToolsAmount() : BigDecimal.ZERO));
                BigDecimal machineDepreciationAmount = machineDepreciationAmountOPN.add(machineDepreciationAmountEL1).subtract(machineDepreciationAmountEL2).add(machineDepreciationAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineDepreciationAmount() : BigDecimal.ZERO));
                BigDecimal machineServiceAmount = machineServiceAmountOPN.add(machineServiceAmountEL1).subtract(machineServiceAmountEL2).add(machineServiceAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineServiceAmount() : BigDecimal.ZERO));
                BigDecimal machineGeneralAmount = machineGeneralAmountOPN.add(machineGeneralAmountEL1).subtract(machineGeneralAmountEL2).add(machineGeneralAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getMachineGeneralAmount() : BigDecimal.ZERO));
                BigDecimal generalMatetialAmount = generalMatetialAmountOPN.add(generalMatetialAmountEL1).subtract(generalMatetialAmountEL2).add(generalMatetialAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getGeneralMatetialAmount() : BigDecimal.ZERO));
                BigDecimal generalLaborAmount = generalLaborAmountOPN.add(generalLaborAmountEL1).subtract(generalLaborAmountEL2).add(generalLaborAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getGeneralLaborAmount() : BigDecimal.ZERO));
                BigDecimal generalToolsAmount = generalToolsAmountOPN.add(generalToolsAmountEL1).subtract(generalToolsAmountEL2).add(generalToolsAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getGeneralToolsAmount() : BigDecimal.ZERO));
                BigDecimal generalDepreciationAmount = generalDepreciationAmountOPN.add(generalDepreciationAmountEL1).subtract(generalDepreciationAmountEL2).add(generalDepreciationAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getGeneralDepreciationAmount() : BigDecimal.ZERO));
                BigDecimal generalServiceAmount = generalServiceAmountOPN.add(generalServiceAmountEL1).subtract(generalServiceAmountEL2).add(generalServiceAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getGeneralServiceAmount() : BigDecimal.ZERO));
                BigDecimal otherGeneralAmount = otherGeneralAmountOPN.add(otherGeneralAmountEL1).subtract(otherGeneralAmountEL2).add(otherGeneralAmountAE).subtract((cpUncompleteDetail != null ? cpUncompleteDetail.getOtherGeneralAmount() : BigDecimal.ZERO));

                cpResult.setDirectMatetialAmount(directMatetialAmount);
                cpResult.setDirectLaborAmount(directLaborAmount);
                cpResult.setMachineMatetialAmount(machineMatetialAmount);
                cpResult.setMachineLaborAmount(machineLaborAmount);
                cpResult.setMachineToolsAmount(machineToolsAmount);
                cpResult.setMachineDepreciationAmount(machineDepreciationAmount);
                cpResult.setMachineServiceAmount(machineServiceAmount);
                cpResult.setMachineGeneralAmount(machineGeneralAmount);
                cpResult.setGeneralMatetialAmount(generalMatetialAmount);
                cpResult.setGeneralLaborAmount(generalLaborAmount);
                cpResult.setGeneralToolsAmount(generalToolsAmount);
                cpResult.setGeneralDepreciationAmount(generalDepreciationAmount);
                cpResult.setGeneralServiceAmount(generalServiceAmount);
                cpResult.setOtherGeneralAmount(otherGeneralAmount);
                cpResult.setTotalCostAmount(directMatetialAmount.add(directLaborAmount).add(machineMatetialAmount).add(machineLaborAmount)
                    .add(machineToolsAmount).add(machineDepreciationAmount).add(machineServiceAmount).add(machineGeneralAmount)
                    .add(generalMatetialAmount).add(generalLaborAmount).add(generalToolsAmount).add(generalDepreciationAmount)
                    .add(generalServiceAmount).add(otherGeneralAmount));
                cpResult.setTotalQuantity(quantityComplete);
                if (quantityComplete.doubleValue() == 0) {
                    cpResult.setUnitPrice(BigDecimal.ZERO);
                } else {
                    cpResult.setUnitPrice(Utils.round(cpResult.getTotalCostAmount().divide(quantityComplete, MathContext.DECIMAL64), lamTronDonGia));
                }
                cpResults.add(cpResult);
            }
        } else {
            for (CPAllocationRate item: cpAllocationRates) {
                CPResult cpResult = new CPResult();
                cpResult.setCostSetID(item.getCostSetID());
                cpResult.setMaterialGoodsID(item.getMaterialGoodsID());
                cpResult.setCoefficien(item.getAllocatedRate());
                BigDecimal quantityComplete = item.getQuantity();

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

                CPUncompleteDetails cpUDetails = null;
                Optional<CPUncompleteDetails> cpUOptional = cpUncompleteDetails.stream().filter(x -> x.getCostSetID().equals(item.getCostSetID())).findFirst();
                if (cpUOptional.isPresent()) {
                    cpUDetails = cpUOptional.get();
                }

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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL1 = directMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL1 = directLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL1 = machineMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL1 = machineLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL1 = machineToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL1 = machineDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL1 = machineServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL1 = machineGeneralAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL1 = generalMatetialAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL1 = generalLaborAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL1 = generalToolsAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL1 = generalDepreciationAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL1 = generalServiceAmountEL1.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountEL2 = directMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountEL2 = directLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountEL2 = machineMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountEL2 = machineLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountEL2 = machineToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountEL2 = machineDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountEL2 = machineServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountEL2 = machineGeneralAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountEL2 = generalMatetialAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountEL2 = generalLaborAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountEL2 = generalToolsAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountEL2 = generalDepreciationAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountEL2 = generalServiceAmountEL2.add(cpEL.getAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
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
                    if (cpEL.getAccountNumber().startsWith("621") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NGUYEN_VAT_LIEU_TRUC_TIEP))) {
                        directMatetialAmountAE = directMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("622") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.NHAN_CONG_TRUC_TIEP))) {
                        directLaborAmountAE = directLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6231") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NHAN_CONG))) {
                        machineMatetialAmountAE = machineMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6232") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU))) {
                        machineLaborAmountAE = machineLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6233") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT))) {
                        machineToolsAmountAE = machineToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6234") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY))) {
                        machineDepreciationAmountAE = machineDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6237") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI))) {
                        machineServiceAmountAE = machineServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6238") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_KHAC))) {
                        machineGeneralAmountAE = machineGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6271") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.PHI_PHI_NHAN_VIEN_PHAN_XUONG))) {
                        generalMatetialAmountAE = generalMatetialAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6272") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG))) {
                        generalLaborAmountAE = generalLaborAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6273") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG))) {
                        generalToolsAmountAE = generalToolsAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6274") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_KHAU_HAO_MAY_CHUNG))) {
                        generalDepreciationAmountAE = generalDepreciationAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6277") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG))) {
                        generalServiceAmountAE = generalServiceAmountAE.add(cpEL.getAllocatedAmount());
                    } else if (cpEL.getAccountNumber().startsWith("6278") || (cpEL.getAccountNumber().startsWith("154") && cpEL.getExpenseItemType() != null && cpEL.getExpenseItemType().equals(Constants.ExpenseType.CHI_PHI_BANG_TIEN_MAT_KHAC))) {
                        otherGeneralAmountAE = otherGeneralAmountAE.add(cpEL.getAllocatedAmount());
                    }
                }

                BigDecimal directMatetialAmount = (directMatetialAmountOPN.add(directMatetialAmountEL1).subtract(directMatetialAmountEL2).add(directMatetialAmountAE).subtract((cpUDetails != null ? cpUDetails.getDirectMatetialAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal directLaborAmount = (directLaborAmountOPN.add(directLaborAmountEL1).subtract(directLaborAmountEL2).add(directLaborAmountAE).subtract((cpUDetails != null ? cpUDetails.getDirectLaborAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineMatetialAmount = (machineMatetialAmountOPN.add(machineMatetialAmountEL1).subtract(machineMatetialAmountEL2).add(machineMatetialAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineMatetialAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineLaborAmount = (machineLaborAmountOPN.add(machineLaborAmountEL1).subtract(machineLaborAmountEL2).add(machineLaborAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineLaborAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineToolsAmount = (machineToolsAmountOPN.add(machineToolsAmountEL1).subtract(machineToolsAmountEL2).add(machineToolsAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineToolsAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineDepreciationAmount = (machineDepreciationAmountOPN.add(machineDepreciationAmountEL1).subtract(machineDepreciationAmountEL2).add(machineDepreciationAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineDepreciationAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineServiceAmount = (machineServiceAmountOPN.add(machineServiceAmountEL1).subtract(machineServiceAmountEL2).add(machineServiceAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineServiceAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal machineGeneralAmount = (machineGeneralAmountOPN.add(machineGeneralAmountEL1).subtract(machineGeneralAmountEL2).add(machineGeneralAmountAE).subtract((cpUDetails != null ? cpUDetails.getMachineGeneralAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal generalMatetialAmount = (generalMatetialAmountOPN.add(generalMatetialAmountEL1).subtract(generalMatetialAmountEL2).add(generalMatetialAmountAE).subtract((cpUDetails != null ? cpUDetails.getGeneralMatetialAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal generalLaborAmount = (generalLaborAmountOPN.add(generalLaborAmountEL1).subtract(generalLaborAmountEL2).add(generalLaborAmountAE).subtract((cpUDetails != null ? cpUDetails.getGeneralLaborAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal generalToolsAmount = (generalToolsAmountOPN.add(generalToolsAmountEL1).subtract(generalToolsAmountEL2).add(generalToolsAmountAE).subtract((cpUDetails != null ? cpUDetails.getGeneralToolsAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal generalDepreciationAmount = (generalDepreciationAmountOPN.add(generalDepreciationAmountEL1).subtract(generalDepreciationAmountEL2).add(generalDepreciationAmountAE).subtract((cpUDetails != null ? cpUDetails.getGeneralDepreciationAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal generalServiceAmount = (generalServiceAmountOPN.add(generalServiceAmountEL1).subtract(generalServiceAmountEL2).add(generalServiceAmountAE).subtract((cpUDetails != null ? cpUDetails.getGeneralServiceAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);
                BigDecimal otherGeneralAmount = (otherGeneralAmountOPN.add(otherGeneralAmountEL1).subtract(otherGeneralAmountEL2).add(otherGeneralAmountAE).subtract((cpUDetails != null ? cpUDetails.getOtherGeneralAmount() : BigDecimal.ZERO))).multiply(item.getAllocatedRate()).divide(BigDecimal.valueOf(100),  MathContext.DECIMAL64);

                cpResult.setDirectMatetialAmount(directMatetialAmount);
                cpResult.setDirectLaborAmount(directLaborAmount);
                cpResult.setMachineMatetialAmount(machineMatetialAmount);
                cpResult.setMachineLaborAmount(machineLaborAmount);
                cpResult.setMachineToolsAmount(machineToolsAmount);
                cpResult.setMachineDepreciationAmount(machineDepreciationAmount);
                cpResult.setMachineServiceAmount(machineServiceAmount);
                cpResult.setMachineGeneralAmount(machineGeneralAmount);
                cpResult.setGeneralMatetialAmount(generalMatetialAmount);
                cpResult.setGeneralLaborAmount(generalLaborAmount);
                cpResult.setGeneralToolsAmount(generalToolsAmount);
                cpResult.setGeneralDepreciationAmount(generalDepreciationAmount);
                cpResult.setGeneralServiceAmount(generalServiceAmount);
                cpResult.setOtherGeneralAmount(otherGeneralAmount);
                cpResult.setTotalCostAmount(directMatetialAmount.add(directLaborAmount).add(machineMatetialAmount).add(machineLaborAmount)
                    .add(machineToolsAmount).add(machineDepreciationAmount).add(machineServiceAmount).add(machineGeneralAmount)
                    .add(generalMatetialAmount).add(generalLaborAmount).add(generalToolsAmount).add(generalDepreciationAmount)
                    .add(generalServiceAmount).add(otherGeneralAmount));
                cpResult.setTotalQuantity(quantityComplete);
                if (quantityComplete.doubleValue() == 0) {
                    cpResult.setUnitPrice(BigDecimal.ZERO);
                } else {
                    cpResult.setUnitPrice(Utils.round(cpResult.getTotalCostAmount().divide(quantityComplete, MathContext.DECIMAL64), lamTronDonGia));
                }
                cpResults.add(cpResult);
            }
        }

        return cpResults;
    }
}
