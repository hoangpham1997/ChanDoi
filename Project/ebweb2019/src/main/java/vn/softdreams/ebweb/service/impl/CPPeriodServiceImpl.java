package vn.softdreams.ebweb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.repository.CPExpenseListRepository;
import vn.softdreams.ebweb.repository.GeneralLedgerRepository;
import vn.softdreams.ebweb.service.CPPeriodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.service.GeneralLedgerService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPPeriod.
 */
@Service
@Transactional
public class CPPeriodServiceImpl implements CPPeriodService {

    private final Logger log = LoggerFactory.getLogger(CPPeriodServiceImpl.class);

    private final CPPeriodRepository cPPeriodRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final CPExpenseListRepository cpExpenseListRepository;
    private final CPExpenseTranferRepository cpExpenseTranferRepository;
    private final GOtherVoucherRepository gOtherVoucherRepository;
    private final UserService userService;
    private final GeneralLedgerService generalLedgerService;
    private final CPAcceptanceDetailsRepository cpAcceptanceDetailsRepository;
    private final CPAcceptanceRepository cpAcceptanceRepository;
    private final CurrencyRepository currencyRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    @Autowired
    GenCodeService genCodeService;

    public CPPeriodServiceImpl(CPPeriodRepository cPPeriodRepository, GeneralLedgerRepository generalLedgerRepository, CPExpenseListRepository cpExpenseListRepository, CPExpenseTranferRepository cpExpenseTranferRepository, GOtherVoucherRepository gOtherVoucherRepository, UserService userService, GeneralLedgerService generalLedgerService, CPAcceptanceDetailsRepository cpAcceptanceDetailsRepository, CPAcceptanceRepository cpAcceptanceRepository, CurrencyRepository currencyRepository, OrganizationUnitRepository organizationUnitRepository) {
        this.cPPeriodRepository = cPPeriodRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.cpExpenseListRepository = cpExpenseListRepository;
        this.cpExpenseTranferRepository = cpExpenseTranferRepository;
        this.gOtherVoucherRepository = gOtherVoucherRepository;
        this.userService = userService;
        this.generalLedgerService = generalLedgerService;
        this.cpAcceptanceDetailsRepository = cpAcceptanceDetailsRepository;
        this.cpAcceptanceRepository = cpAcceptanceRepository;
        this.currencyRepository = currencyRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a cPPeriod.
     *
     * @param cPPeriod the entity to save
     * @return the persisted entity
     */
    @Override
    public CPPeriod save(CPPeriod cPPeriod) {
        log.debug("Request to save CPPeriod : {}", cPPeriod);
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        cPPeriod.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        cPPeriod.setTypeLedger(soLamViec);
        if (cPPeriod.getId() != null && (cPPeriod.getType().equals(Constants.GiaThanh.CTVV) || cPPeriod.getType().equals(Constants.GiaThanh.DON_HANG))) {
            List<CPAcceptance> cpAcceptances = cpAcceptanceRepository.findByCPPeriod(cPPeriod.getId());
            List<CPAcceptanceDetails> cpAcceptanceDetails = cpAcceptanceDetailsRepository.findByCPPeriod(cPPeriod.getId());
            cPPeriod.setcPAcceptances(new HashSet<>(cpAcceptances));
            cPPeriod.setcPAcceptanceDetails(new HashSet<>(cpAcceptanceDetails));
        }
        CPPeriod cPPeriodSave = cPPeriodRepository.save(cPPeriod);
        return cPPeriodSave;
    }

    /**
     * Get all the cPPeriods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPPeriod> findAll(Pageable pageable) {
        log.debug("Request to get all CPPeriods");
        return cPPeriodRepository.findAll(pageable);
    }


    /**
     * Get one cPPeriod by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPPeriod> findOne(UUID id) {
        log.debug("Request to get CPPeriod : {}", id);
        return cPPeriodRepository.findById(id);
    }

    /**
     * Delete the cPPeriod by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPPeriod : {}", id);
        List<UUID> uuids = new ArrayList<>();
        uuids.add(id);
        cpExpenseTranferRepository.deleteByCPPeriodID(uuids);
        gOtherVoucherRepository.deleteByCPPeriodID(uuids);
        cPPeriodRepository.deleteCPPeriod(uuids);
    }

    @Override
    public Page<CPPeriodDTO> findAllByType(Pageable pageable, Integer type) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        return cPPeriodRepository.findAllByType(pageable, type, currentUserLoginAndOrg.get().getOrg(), Utils.PhienSoLamViec(userDTO), false);
    }

    @Override
    public Integer checkDelete(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        int count1 = cpExpenseTranferRepository.checkExist(currentUserLoginAndOrg.get().getOrg(), id);
        if (count1 > 0) {
            return 1;
        }
        int count2 = cpAcceptanceRepository.checkExist(id);
        if (count2 > 0) {
            return 2;
        }
        return 0;
    }

    @Override
    public Boolean checkPeriod(String fromDate, String toDate, List<UUID> costSetIDs, Integer type) {
        Integer count = cPPeriodRepository.findPeriod(fromDate, toDate, costSetIDs, type);
        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<CPPeriod> getAllCPPeriod() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return cPPeriodRepository.getAllCPPeriod(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public byte[] exportExcel(Pageable pageable, Integer type) {

        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Page<CPPeriodDTO> cpPeriodDTOS = cPPeriodRepository.findAllByType(pageable, type, currentUserLoginAndOrg.get().getOrg(), Utils.PhienSoLamViec(userDTO), true);
            if (Constants.GiaThanh.GIAN_DON.equals(type) || Constants.GiaThanh.HE_SO.equals(type) || Constants.GiaThanh.TY_LE.equals(type)) {
                return ExcelUtils.writeToFile(cpPeriodDTOS.getContent(), ExcelConstant.GiaThanh1.NAME, ExcelConstant.GiaThanh1.HEADER, ExcelConstant.GiaThanh1.FIELD, userDTO);
            } else {
                return ExcelUtils.writeToFile(cpPeriodDTOS.getContent(), ExcelConstant.GiaThanh2.NAME, ExcelConstant.GiaThanh2.HEADER, ExcelConstant.GiaThanh2.FIELD, userDTO);
            }
        }
        return null;
    }

    @Override
    public byte[] exportPdf(Pageable pageable, Integer type) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Page<CPPeriodDTO> cpPeriodDTOS = cPPeriodRepository.findAllByType(pageable, type, currentUserLoginAndOrg.get().getOrg(), Utils.PhienSoLamViec(userDTO), true);
            if (Constants.GiaThanh.GIAN_DON.equals(type) || Constants.GiaThanh.HE_SO.equals(type) || Constants.GiaThanh.TY_LE.equals(type)) {
                return PdfUtils.writeToFile(cpPeriodDTOS.getContent(), ExcelConstant.GiaThanh1.HEADER, ExcelConstant.GiaThanh1.FIELD, userDTO);
            } else {
                return PdfUtils.writeToFile(cpPeriodDTOS.getContent(), ExcelConstant.GiaThanh2.HEADER, ExcelConstant.GiaThanh2.FIELD, userDTO);
            }
        }
        return null;
    }

    @Override
    public CPPeriodDTO findByID(UUID ID) {
        CPPeriodDTO cpPeriod = cPPeriodRepository.getByID(ID);
        List<CPPeriodDetailDTO> cpPeriodDetailDTOS = cPPeriodRepository.getAllCPPeriodDetailByCPPeriodID(cpPeriod.getId());
        if (cpPeriodDetailDTOS.size() > 0) {
            cpPeriod.setcPPeriodDetails(cpPeriodDetailDTOS);
        }
        List<CPExpenseListDTO> cpExpenseListDTOS = cPPeriodRepository.getAllCPExpenseListByCPPeriodID(cpPeriod.getId());
        if (cpExpenseListDTOS.size() > 0) {
            cpPeriod.setcPExpenseList(cpExpenseListDTOS);
        }
        List<CPAllocationExpenseDTO> cpAllocationExpenseDTOS = cPPeriodRepository.getAllCPAllocationExpenseByCPPeriodID(cpPeriod.getId());
        if (cpAllocationExpenseDTOS.size() > 0) {
            cpPeriod.setcPAllocationGeneralExpenses(cpAllocationExpenseDTOS);
        }
        List<CPAllocationExpenseDetailDTO> cpAllocationExpenseDetailDTOS = cPPeriodRepository.getAllCPAllocationExpenseDetailByCPPeriodID(cpPeriod.getId());
        if (cpAllocationExpenseDetailDTOS.size() > 0) {
            cpPeriod.setcPAllocationGeneralExpenseDetails(cpAllocationExpenseDetailDTOS);
        }
        if (cpPeriod.getType().equals(Constants.GiaThanh.HE_SO) || cpPeriod.getType().equals(Constants.GiaThanh.TY_LE) || cpPeriod.getType().equals(Constants.GiaThanh.GIAN_DON)) {
            List<CPUncompletesDTO> cpUncompleteDTOS = cPPeriodRepository.getAllCPUncompleteByCPPeriodID(cpPeriod.getId());
            if (cpUncompleteDTOS.size() > 0) {
                cpPeriod.setcPUncompletes(cpUncompleteDTOS);
            }
            List<CPUncompleteDetailDTO> cpUncompleteDetailDTOS = cPPeriodRepository.getAllCPUncompleteDetailByCPPeriodID(cpPeriod.getId());
            if (cpUncompleteDetailDTOS.size() > 0) {
                cpPeriod.setcPUncompleteDetails(cpUncompleteDetailDTOS);
            }
            List<CPResultDTO> cpResultDTOS = cPPeriodRepository.getAllCPResultByCPPeriodID(cpPeriod.getId());
            if (cpResultDTOS.size() > 0) {
                cpPeriod.setcPResults(cpResultDTOS);
            }
        }
        if (cpPeriod.getType().equals(Constants.GiaThanh.HE_SO) || cpPeriod.getType().equals(Constants.GiaThanh.TY_LE)) {
            List<CPAllocationRateDTO> cpAllocationRateDTOS = cPPeriodRepository.getAllCPAllocationRateByCPPeriodID(cpPeriod.getId());
            if (cpAllocationRateDTOS.size() > 0) {
                cpPeriod.setcPAllocationRates(cpAllocationRateDTOS);
            }
        }
        if (cpPeriod.getType().equals(Constants.GiaThanh.CTVV) || cpPeriod.getType().equals(Constants.GiaThanh.DON_HANG)) {
            List<CPAcceptanceDetailDTO> cpAcceptanceDetailDTOS = new ArrayList<>();
            List<CPAcceptanceDTO> cpAcceptanceDTOS = cPPeriodRepository.getAllPAcceptanceByCPPeriodID(cpPeriod.getId());
            if (cpAcceptanceDTOS.size() > 0) {
                cpPeriod.setcPAcceptances(cpAcceptanceDTOS);
                cpAcceptanceDetailDTOS = cPPeriodRepository.getAllCPAcceptanceDetailByCPPeriodIDSecond(cpPeriod.getId());
            } else {
                cpAcceptanceDetailDTOS = cPPeriodRepository.getAllCPAcceptanceDetailByCPPeriodID(cpPeriod.getId());
            }
            if (cpAcceptanceDetailDTOS.size() > 0) {
                cpPeriod.setcPAcceptanceDetails(cpAcceptanceDetailDTOS);
            }
        }
        return cpPeriod;
    }

    @Override
    public CPPeriod accepted(CPPeriod cPPeriod) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);

        List<CPAcceptanceDetails> cpAcceptanceDetails = new ArrayList<>(cPPeriod.getcPAcceptanceDetails());
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String reason = ("Nghiệm thu " + (cPPeriod.getType().equals(Constants.GiaThanh.CTVV) ? "công trình" : "đơn hàng") + " của kỳ tính giá thành từ " + cPPeriod.getFromDate().format(formatters) +  " đến " + cPPeriod.getToDate().format(formatters));

        GOtherVoucher gOtherVoucher = GetGOtherVoucherFromCPAcceptanceDetail(cpAcceptanceDetails,
            soLamViec, currentUserLoginAndOrg.get().getOrg(), cPPeriod.getCreditAccount(), cPPeriod.getDebitAccount(), reason);
        gOtherVoucher.setCurrencyID(userDTO.getOrganizationUnit().getCurrencyID());
        gOtherVoucher.setExchangeRate(currencyRepository.getExchageRateByCurencyCode(userDTO.getOrganizationUnit().getCurrencyID(), currentUserLoginAndOrg.get().getOrg()));
        GOtherVoucher gOtherVoucherSave = gOtherVoucherRepository.save(gOtherVoucher);
        MessageDTO messageDTO = new MessageDTO();
        generalLedgerService.record(gOtherVoucherSave, messageDTO);

        CPAcceptance cpAcceptance = new CPAcceptance();
        cpAcceptance.setId(UUID.randomUUID());
        cpAcceptance.setgOtherVoucherID(gOtherVoucherSave.getId());
        cpAcceptance.setcPPeriodID(cPPeriod.getId());
        cpAcceptance.setDate(LocalDate.now());
        cpAcceptance.setPostedDate(LocalDate.now());
        cpAcceptance.setTypeID(TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC_NT);
        if (soLamViec == 0) {
            cpAcceptance.setNo(gOtherVoucher.getNoFBook());
        } else {
            cpAcceptance.setNo(gOtherVoucher.getNoMBook());
        }
        cpAcceptance.setTotalAmount(gOtherVoucher.getTotalAmount());
        cpAcceptance.setTotalAmountOriginal(gOtherVoucher.getTotalAmountOriginal());

        CPAcceptance cpAcceptanceSave = cpAcceptanceRepository.save(cpAcceptance);

        for (CPAcceptanceDetails item: cPPeriod.getcPAcceptanceDetails()) {
            item.setId(null);
            item.setDescription(reason);
            item.setCreditAccount(cPPeriod.getCreditAccount());
            item.setDebitAccount(cPPeriod.getDebitAccount());
            item.setcPAcceptanceID(cpAcceptanceSave.getId());
            item.setcPPeriodID(cPPeriod.getId());
        }

        cpAcceptanceDetailsRepository.saveAll(cPPeriod.getcPAcceptanceDetails());
        return cPPeriod;
    }

    @Override
    public List<CPPeriod> getAllCPPeriodForReport(Boolean isDependent, UUID orgID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> listOrgID = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            if (Boolean.TRUE.equals(isDependent)) {
                List<UUID> uuids = organizationUnitRepository.findAllOrgAccType0(orgID);
                listOrgID.addAll(uuids);
                return cPPeriodRepository.findAllByOrgID(listOrgID);
            } else {
                listOrgID.add(orgID);
                return cPPeriodRepository.findAllByOrgID(listOrgID);
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public HandlingResultDTO multiDelete(List<CPPeriodDTO> cpPeriodDTOS) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(cpPeriodDTOS.size());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();

        // get ListID chung tu theo Type ID
        List<UUID> uuidList = cpPeriodDTOS.stream().map(x -> x.getId()).collect(Collectors.toList());

        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu
        if (uuidList.size() > 0) {
            cpExpenseTranferRepository.deleteByCPPeriodID(uuidList);
            gOtherVoucherRepository.deleteByCPPeriodID(uuidList);
            cPPeriodRepository.deleteCPPeriod(uuidList);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        return handlingResultDTO;
    }

    private GOtherVoucher GetGOtherVoucherFromCPAcceptanceDetail(List<CPAcceptanceDetails> cpAcceptanceDetails, Integer soLamViec, UUID org,
                                                                 String creditAccount, String debitAccount, String reason) {
        GOtherVoucher gOtherVoucher = new GOtherVoucher();
        gOtherVoucher.setTypeID(TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC_NT);
        gOtherVoucher.setCompanyID(org);
        gOtherVoucher.setTypeLedger(soLamViec);
        gOtherVoucher.setRecorded(true);
        gOtherVoucher.setDate(LocalDate.now());
        gOtherVoucher.setPostedDate(LocalDate.now());
        gOtherVoucher.setReason(reason);
        GenCode genCode = genCodeService.findWithTypeID(70, soLamViec == null ? 2 : soLamViec);
        String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(), '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
        if (soLamViec == 0) {
            gOtherVoucher.setNoFBook(codeVoucher);
        } else {
            gOtherVoucher.setNoMBook(codeVoucher);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CPAcceptanceDetails item: cpAcceptanceDetails) {
            GOtherVoucherDetails gOtherVoucherDetails = new GOtherVoucherDetails();
            gOtherVoucherDetails.setDescription(reason);
            gOtherVoucherDetails.setCreditAccount(creditAccount);
            gOtherVoucherDetails.setDebitAccount(debitAccount);
            gOtherVoucherDetails.setAmountOriginal(item.getTotalAcceptedAmount());
            gOtherVoucherDetails.setAmount(item.getTotalAcceptedAmount());
            gOtherVoucherDetails.setCostSetID(item.getCostSetID());
            totalAmount = totalAmount.add(item.getTotalAcceptedAmount());
            gOtherVoucher.getgOtherVoucherDetails().add(gOtherVoucherDetails);
        }
        gOtherVoucher.setTotalAmount(totalAmount);
        gOtherVoucher.setTotalAmountOriginal(totalAmount);
        return gOtherVoucher;
    }
}
