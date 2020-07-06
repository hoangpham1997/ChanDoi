package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.repository.GOtherVoucherRepository;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.service.GOtherVoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.cashandbank.GOtherVoucherExportDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherGeneralDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GOtherVoucher.
 */
@Service
@Transactional
public class GOtherVoucherServiceImpl implements GOtherVoucherService {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherServiceImpl.class);

    private final GOtherVoucherRepository gOtherVoucherRepository;
    private final PrepaidExpenseRepository prepaidExpenseRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";
    private final GenCodeService genCodeService;
    private final GeneralLedgerService generalLedgerService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final Integer TYPE_GOTHERVOUCHER = 700;
    private final Integer TYPE_FORWARDING_EXPENSE = 701;
    private final Integer TYPE_FORWARDING_PROFITS_LOSSES = 702;
    private final Integer TYPE_ACCEPTANCE_OF_WORKS_ORDERS_CONTRACTS = 703;
    private final Integer TYPE_ACCOUNTING_OF_DEPRECIATION_EXPENSES_OF_FIXED = 704;
    private final Integer TYPE_ACCOUNTING_OF_EXPENSES_ALLOCATED_FOR_TOOLS = 705;
    private final Integer TYPE_EXCHANGE_RATE_DIFFERENCES_FROM_THE_EXIT_RATE = 706;
    private final GOtherVoucherDetailExpenseRepository gOtherVoucherDetailExpenseRepository;
    private final GOtherVoucherDetailExpenseAllocationRepository gOtherVoucherDetailExpenseAllocationRepository;
    private final GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository;
    private final UtilsService utilsService;
    private final TypeRepository typeRepository;
    private final CPAcceptanceDetailsRepository cpAcceptanceDetailsRepository;

    //    private final Integer TYPE_GOTHERVOUCHER = 707;
//    private final Integer TYPE_GOTHERVOUCHER = 708;
//    private final Integer TYPE_GOTHERVOUCHER = 709;
//    private final Integer TYPE_GOTHERVOUCHER = 710;
    @Autowired
    UtilsRepository utilsRepository;

    public GOtherVoucherServiceImpl(GOtherVoucherRepository gOtherVoucherRepository,
                                    PrepaidExpenseRepository prepaidExpenseRepository, RefVoucherRepository refVoucherRepository,
                                    UserService userService,
                                    GenCodeService genCodeService,
                                    GeneralLedgerService generalLedgerService,
                                    OrganizationUnitRepository organizationUnitRepository,
                                    GOtherVoucherDetailExpenseRepository gOtherVoucherDetailExpenseRepository,
                                    GOtherVoucherDetailExpenseAllocationRepository gOtherVoucherDetailExpenseAllocationRepository,
                                    GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository,
                                    UtilsService utilsService, TypeRepository typeRepository, CPAcceptanceDetailsRepository cpAcceptanceDetailsRepository) {
        this.gOtherVoucherRepository = gOtherVoucherRepository;
        this.prepaidExpenseRepository = prepaidExpenseRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.gOtherVoucherDetailExpenseRepository = gOtherVoucherDetailExpenseRepository;
        this.gOtherVoucherDetailExpenseAllocationRepository = gOtherVoucherDetailExpenseAllocationRepository;
        this.gOtherVoucherDetailsRepository = gOtherVoucherDetailsRepository;
        this.utilsService = utilsService;
        this.typeRepository = typeRepository;
        this.cpAcceptanceDetailsRepository = cpAcceptanceDetailsRepository;
    }

    /**
     * Save a gOtherVoucher.
     *
     * @param gOtherVoucher the entity to save
     * @return the persisted entity
     */
    @Override
    public GOtherVoucher save(GOtherVoucher gOtherVoucher) {
        log.debug("Request to save MBDeposit : {}", gOtherVoucher);
        GOtherVoucher gOV = new GOtherVoucher();
        if (gOtherVoucher.getId() == null) {
            gOtherVoucher.setId(UUID.randomUUID());
            //gan id cha cho details con
            for (GOtherVoucherDetails details : gOtherVoucher.getgOtherVoucherDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setgOtherVoucherID(gOtherVoucher.getId());
            }
//            for (MBDepositDetailTax detailTax : mBDeposit.getmBDepositDetailTax()) {
//                if (detailTax.getId() != null) {
//                    detailTax.setId(null);
//                }
//                detailTax.setmBDepositID(mBDeposit.getId());
//            }
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            gOtherVoucher.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            gOV = gOtherVoucherRepository.save(gOtherVoucher);
//            utilsRepository.updateGencode(gOV.getNoFBook(), gOV.getNoMBook(), 70, gOV.getTypeLedger() == null ? 0 : gOV.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : gOtherVoucher.getViewVouchers() != null ? gOtherVoucher.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(gOV.getCompanyID());
                refVoucher.setRefID1(gOV.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(gOV.getId());
            refVoucherRepository.deleteByRefID2(gOV.getId());
            refVoucherRepository.saveAll(refVouchers);
            gOV.setViewVouchers(gOtherVoucher.getViewVouchers());
            return gOV;
        }
        throw new BadRequestAlertException("Không thể lưu báo có ! ", "", "");
    }

    /**
     * Get all the gOtherVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GOtherVoucher> findAll(Pageable pageable) {
        log.debug("Request to get all GOtherVouchers");
        return gOtherVoucherRepository.findAll(pageable);
    }


    /**
     * Get one gOtherVoucher by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GOtherVoucher> findOne(UUID id) {
        log.debug("Request to get MCPayment : {}", id);
        Optional<GOtherVoucher> gOtherVoucher = gOtherVoucherRepository.findById(id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            gOtherVoucher.get().setViewVouchers(dtos);
        }
        return gOtherVoucher;
    }

    /**
     * Delete the gOtherVoucher by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GOtherVoucher : {}", id);
        List<UUID> uuids = new ArrayList<>();
        uuids.add(id);
        cpAcceptanceDetailsRepository.deleteByGOtherVoucherID(uuids);
        gOtherVoucherRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);

    }

    @Override
    public Page<GOtherVoucherViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return gOtherVoucherRepository.findAll(pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    @Transactional(readOnly = true)
    public GOtherVoucher findOneByRowNum(SearchVoucher searchVoucher, Number rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return gOtherVoucherRepository.findByRowNum(searchVoucher, rowNum, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public GOtherVoucherSaveDTO saveDTO(GOtherVoucher gOtherVoucher) {
        log.debug("Request to save GOtherVoucher : {}", gOtherVoucher);
        GOtherVoucher gOV = new GOtherVoucher();
        GOtherVoucherSaveDTO gOtherVoucherSaveDTO = new GOtherVoucherSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (gOtherVoucher.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
                }
            }
            UserDTO userDTO = userService.getAccount();
            if (gOtherVoucher.getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(gOtherVoucher.getNoMBook())) {
                        gOtherVoucher.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (StringUtils.isEmpty(gOtherVoucher.getNoFBook())) {
                        gOtherVoucher.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(gOtherVoucher.getNoFBook(), gOtherVoucher.getNoMBook(), gOtherVoucher.getTypeLedger(), gOtherVoucher.getId())) {
                gOtherVoucherSaveDTO.setgOtherVoucher(gOtherVoucher);
                gOtherVoucherSaveDTO.setStatus(1);
                return gOtherVoucherSaveDTO;
            }
            gOtherVoucher.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            gOtherVoucher.getgOtherVoucherDetails().sort(Comparator.comparingInt(GOtherVoucherDetails::getOrderPriority));
            gOV = gOtherVoucherRepository.save(gOtherVoucher);
//            utilsRepository.updateGencode(gOV.getNoFBook(), gOV.getNoMBook(), 70, gOV.getTypeLedger() == null ? 2 : gOV.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : gOtherVoucher.getViewVouchers() != null ? gOtherVoucher.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(gOV.getCompanyID());
                refVoucher.setRefID1(gOV.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(gOV.getId());
            refVoucherRepository.deleteByRefID2(gOV.getId());
            refVoucherRepository.saveAll(refVouchers);
            gOV.setViewVouchers(gOtherVoucher.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(gOV, messageDTO)) {
                    gOtherVoucherSaveDTO.setStatus(2);
                    gOtherVoucherSaveDTO.setMsg(messageDTO.getMsgError());
                    gOV.setRecorded(false);
                    gOtherVoucherRepository.save(gOV);
                } else {
                    gOtherVoucherSaveDTO.setStatus(0);
                    gOV.setRecorded(true);
                    gOtherVoucherRepository.save(gOV);
                }
            } else {
                gOtherVoucherSaveDTO.setStatus(0);
                if (gOV.getRecorded() == null) {
                    gOV.setRecorded(false);
                    gOtherVoucherRepository.save(gOV);
                }
//                mcP.setRecorded(false);
            }
            gOtherVoucherSaveDTO.setgOtherVoucher(gOV);
            return gOtherVoucherSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    @Override
    public byte[] exportPDF(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<Type> types = typeRepository.findAllByIsActive();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<GOtherVoucherExportDTO> gOtherVoucherExportDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.getAllGOtherVouchers(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < gOtherVoucherExportDTOS.getContent().size(); i++) {
            Integer value = i;
            Optional<Type> type = types.stream().filter(a -> a.getId().equals(gOtherVoucherExportDTOS.getContent().get(value).getTypeID())).findFirst();
            if (type.isPresent()) {
                gOtherVoucherExportDTOS.getContent().get(value).setTypeIDInWord(type.get().getTypeName());
            }
        }
        return PdfUtils.writeToFile(gOtherVoucherExportDTOS.getContent(), ExcelConstant.GOtherVoucher.HEADER, ExcelConstant.GOtherVoucher.FIELD);
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<Type> types = typeRepository.findAllByIsActive();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<GOtherVoucherExportDTO> gOtherVoucherExportDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.getAllGOtherVouchers(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < gOtherVoucherExportDTOS.getContent().size(); i++) {
            Integer value = i;
            Optional<Type> type = types.stream().filter(a -> a.getId().equals(gOtherVoucherExportDTOS.getContent().get(value).getTypeID())).findFirst();
            if (type.isPresent()) {
                gOtherVoucherExportDTOS.getContent().get(value).setTypeIDInWord(type.get().getTypeName());
            }
        }
        return ExcelUtils.writeToFile(gOtherVoucherExportDTOS.getContent(), ExcelConstant.GOtherVoucher.NAME, ExcelConstant.GOtherVoucher.HEADER, ExcelConstant.GOtherVoucher.FIELD);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return gOtherVoucherRepository.getIndexRow(id, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public Page<GOtherVoucherKcDsDTO> searchGOtherVoucher(Pageable pageable, String fromDate, String toDate, Integer status, String keySearch) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return gOtherVoucherRepository.searchGOtherVoucher(pageable, fromDate, toDate, status, keySearch, isNoMBook, currentUserLoginAndOrg.get().getOrg());
        }
        return new PageImpl<GOtherVoucherKcDsDTO>(new ArrayList<>());
    }

    @Override
    public List<GOtherVoucherDetailKcDTO> getGOtherVoucherDetailByGOtherVoucherId(UUID id) {
        return gOtherVoucherRepository.getGOtherVoucherDetailByGOtherVoucherId(id);
    }

    public GOtherVoucherGeneralDTO saveGOtherVoucherGeneralDTO(GOtherVoucherGeneralDTO gOtherVoucherGeneralDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
//        Boolean check = gOtherVoucherGeneralDTO.getgOtherVoucher().getId() == null;
//        UUID gOtherVoucherID = gOtherVoucherGeneralDTO.getgOtherVoucher().getId();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            GOtherVoucher gOtherVoucher = gOtherVoucherGeneralDTO.getgOtherVoucher();
            if (gOtherVoucher.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("", "voucherExists", "");
                }
            }
            List<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses = gOtherVoucherGeneralDTO.getgOtherVoucherDetailExpenses();
//            List<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations = gOtherVoucherGeneralDTO.getgOtherVoucherDetailExpenseAllocations();
//            List<GOtherVoucherDetails> gOtherVoucherDetails = gOtherVoucherGeneralDTO.getgOtherVoucherDetails();
            gOtherVoucher.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            gOtherVoucher.setTypeLedger(Integer.parseInt(currentBook));
            UserDTO userDTO = userService.getAccount();
            gOtherVoucher.setTypeID(TypeConstant.PHAN_BO_CHI_PHI_TRA_TRUOC);
            if (Objects.equals(gOtherVoucher.getTypeLedger(), Constants.TypeLedger.BOTH_BOOK)) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(gOtherVoucher.getNoMBook())) {
                        gOtherVoucher.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                } else {
                    if (StringUtils.isEmpty(gOtherVoucher.getNoFBook())) {
                        gOtherVoucher.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(gOtherVoucher.getNoFBook(), gOtherVoucher.getNoMBook(), gOtherVoucher.getTypeLedger(), gOtherVoucher.getId())) {
                throw new BadRequestAlertException("noBook", "gOtherVoucher", "DUPLICATE_NO_BOOK_RS");
            }
            Set<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses1 = new HashSet<>();
            gOtherVoucherDetailExpenses1.addAll(gOtherVoucherGeneralDTO.getgOtherVoucherDetailExpenses());
            // lưu id vào các detail liên kết
            gOtherVoucher.setgOtherVoucherDetailExpenses(gOtherVoucherDetailExpenses1);
            Set<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations = new HashSet<>();
            gOtherVoucherDetailExpenseAllocations.addAll(gOtherVoucherGeneralDTO.getgOtherVoucherDetailExpenseAllocations());
            gOtherVoucher.setgOtherVoucherDetailExpenseAllocations(gOtherVoucherDetailExpenseAllocations);
            List<GOtherVoucherDetails> gOtherVoucherDetails = new ArrayList<>();
            gOtherVoucherDetails.addAll(gOtherVoucherGeneralDTO.getgOtherVoucherDetails());
            gOtherVoucher.setgOtherVoucherDetails(gOtherVoucherDetails);
            // ghi sổ
            gOtherVoucher.setRecorded(false);
            gOtherVoucherGeneralDTO.setgOtherVoucher(this.gOtherVoucherRepository.save(gOtherVoucher));
            refVoucherRepository.deleteByRefID1(gOtherVoucher.getId());
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(gOtherVoucher, new MessageDTO(""))) {
                    gOtherVoucher.setRecorded(false);
                } else {
                    gOtherVoucher.setRecorded(true);
                }
            }
            // cập nhật chi phí trả trước
//            List<UUID> listID = new ArrayList<>();
//            for (GOtherVoucherDetailExpense gOtherVoucherDetailExpense : gOtherVoucherDetailExpenses) {
//                listID.add(gOtherVoucherDetailExpense.getPrepaidExpenseID());
//            }
//            if (check) {
//                prepaidExpenseRepository.updateAllocatedPeriod(true, listID);
//            } else {
//                List<UUID> pre = gOtherVoucherRepository.getAllByGOtherVoucherID(gOtherVoucherID, listID);
//                if (pre != null && pre.size() > 0) {
//                    prepaidExpenseRepository.updateAllocatedPeriod(false, pre);
//                }
//            }
            gOtherVoucherGeneralDTO.setgOtherVoucher(this.gOtherVoucherRepository.save(gOtherVoucher));
            List<RefVoucher> refVouchers = gOtherVoucherGeneralDTO.getViewVouchers();
            if (refVouchers != null && refVouchers.size() > 0) {
                for (RefVoucher item : refVouchers) {
                    item.setRefID1(gOtherVoucherGeneralDTO.getgOtherVoucher().getId());
                    item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                }
                refVoucherRepository.deleteByRefID1(gOtherVoucherGeneralDTO.getgOtherVoucher().getId());
                refVoucherRepository.saveAll(refVouchers);
            }
//            lưu id vào detail
//            for (int i = 0; i < gOtherVoucherDetailExpenseAllocations.size(); i++) {
//                if (i < gOtherVoucherDetailExpenses.size()) {
//                    if (gOtherVoucherDetailExpenses.get(i) != null) {
//                        gOtherVoucherDetailExpenses.get(i).setgOtherVoucherID(gOtherVoucherGeneralDTO.getgOtherVoucher().getId());
//                    }
//                }
//                if (gOtherVoucherDetails.get(i) != null) {
//                    gOtherVoucherDetails.get(i).setgOtherVoucherID(gOtherVoucherGeneralDTO.getgOtherVoucher().getId());
//                }
//                if (gOtherVoucherDetailExpenseAllocations.get(i) != null) {
//                    gOtherVoucherDetailExpenseAllocations.get(i).setgOtherVoucherID(gOtherVoucherGeneralDTO.getgOtherVoucher().getId());
//                }
//            }
//            gOtherVoucherGeneralDTO.setgOtherVoucherDetailExpenses(this.gOtherVoucherDetailExpenseRepository.saveAll(gOtherVoucherDetailExpenses));
//            gOtherVoucherGeneralDTO.setgOtherVoucherDetailExpenseAllocations(this.gOtherVoucherDetailExpenseAllocationRepository.saveAll(gOtherVoucherDetailExpenseAllocations));
//            if (gOtherVoucherDetails != null) {
//                List<GOtherVoucherDetails> a = this.gOtherVoucherDetailsRepository.saveAll(gOtherVoucherDetails);
//                gOtherVoucherGeneralDTO.setgOtherVoucherDetails(a);
//            }
        }
        return gOtherVoucherGeneralDTO;
    }

    @Override
    public GOtherVoucherKcDTO getGOtherVoucherById(UUID id) {
        GOtherVoucherKcDTO gOtherVoucherKcDTO = new GOtherVoucherKcDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            gOtherVoucherKcDTO = gOtherVoucherRepository.getGOtherVoucherById(id);
            List<GOtherVoucherDetails> gOtherVoucherDetailsList = gOtherVoucherDetailsRepository.findAllBygOtherVoucherIDOrderByOrderPriorityAsc(id);
            gOtherVoucherKcDTO.setgOtherVoucherDetails(gOtherVoucherDetailsList);
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            gOtherVoucherKcDTO.setRefVouchers(refVoucherRepository.getRefViewVoucher(id, isNoMBook));
        }
        return gOtherVoucherKcDTO;
    }

    @Override
    public UpdateDataDTO saveGOtherVoucherKc(GOtherVoucherKcDTO gOtherVoucherKcDTO) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        GOtherVoucher gOtherVoucher = new GOtherVoucher();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean isUpdateGenCode = false;
        if (gOtherVoucherKcDTO.getId() != null) {
            Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(gOtherVoucherKcDTO.getId());
            if (!gOtherVoucherOptional.isPresent()) {
                updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.NOT_FOUND);
                return updateDataDTO;
            }
            isUpdateGenCode = true;
            gOtherVoucher = gOtherVoucherOptional.get();
        }

        if (!currentUserLoginAndOrg.isPresent()) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
            return updateDataDTO;
        }

        if (Strings.isNullOrEmpty(gOtherVoucherKcDTO.getNoFBook())) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.NO_BOOK_NULL);
            return updateDataDTO;
        }
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (!userWithAuthoritiesAndSystemOption.isPresent()) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
            return updateDataDTO;
        }

        Instant maxDate = null;

        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        boolean isNoMBook = currentBook.equalsIgnoreCase("1");

        if (gOtherVoucherKcDTO.getId() != null) {
            maxDate = gOtherVoucherRepository.getMaxDateNotId(currentUserLoginAndOrg.get().getOrg(), currentBook, gOtherVoucherKcDTO.getId());
        } else {
             maxDate = gOtherVoucherRepository.getMaxDate(currentUserLoginAndOrg.get().getOrg(), currentBook);
        }

        if (maxDate != null && gOtherVoucherKcDTO.getPostedDate().isBefore(maxDate.atZone(ZoneId.systemDefault()).toLocalDate())) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.POSTDATE_INVALID);
            updateDataDTO.setResult(Utils.convertDate(maxDate.atZone(ZoneId.systemDefault()).toLocalDate()));

            return updateDataDTO;
        }

        if (!utilsRepository.checkDuplicateNoVoucher(gOtherVoucherKcDTO.getNoFBook(),
            gOtherVoucherKcDTO.getNoFBook(),
            isNoMBook ? 1 : 0, gOtherVoucherKcDTO.getId())) {
            throw new BadRequestAlertException("Trùng số chứng từ", "updateReceipt" + Constants.TypeGroupId.G_OTHER_VOUCHER_KC, Constants.UpdateDataDTOMessages.DUPLICATE_NO_BOOK);
        }

        if (isNoMBook) {
            gOtherVoucher.setTypeLedger(1);
            gOtherVoucher.setNoFBook(null);
            gOtherVoucher.setNoMBook(gOtherVoucherKcDTO.getNoFBook());
        } else {
            gOtherVoucher.setTypeLedger(0);
            gOtherVoucher.setNoFBook(gOtherVoucherKcDTO.getNoFBook());
            gOtherVoucher.setNoMBook(null);
        }
        SecurityDTO securityDTO = currentUserLoginAndOrg.get();
        gOtherVoucher.setCompanyID(securityDTO.getOrg());

        setGOtherVoucher(gOtherVoucherKcDTO, gOtherVoucher);

        gOtherVoucher.setgOtherVoucherDetails(gOtherVoucherKcDTO.getgOtherVoucherDetails());

        if (gOtherVoucher.getgOtherVoucherDetails() != null && gOtherVoucher.getgOtherVoucherDetails().size() > 0) {
            for (GOtherVoucherDetails item : gOtherVoucher.getgOtherVoucherDetails()) {
                if (gOtherVoucher.getId() != null) {
                    item.setgOtherVoucherID(gOtherVoucher.getId());
                }
            }
        }

        if (isUpdateGenCode) {
            gOtherVoucher = gOtherVoucherRepository.save(gOtherVoucher);
        } else {
            gOtherVoucher = saveKc(gOtherVoucher);
        }

        UUID gOtherVoucherId = gOtherVoucher.getId();
        if (Objects.nonNull(gOtherVoucherKcDTO.getRefVouchers())) {
            List<RefVoucher> lastRefVoucher = refVoucherRepository.findAllByRefID1(gOtherVoucher.getId());
            List<RefVoucher> currentRefVoucher = gOtherVoucherKcDTO.getRefVouchers().stream().map(dto -> {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setRefID2(dto.getRefID2());
                refVoucher.setCompanyID(securityDTO.getOrg());
                refVoucher.setRefID1(gOtherVoucherId);
                return refVoucher;
            }).collect(Collectors.toList());
            List<RefVoucher> removeList = lastRefVoucher.stream().filter(refVoucher -> !currentRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
            List<RefVoucher> addList = currentRefVoucher.stream().filter(refVoucher -> !lastRefVoucher.stream()
                .map(RefVoucher::getRefID2).collect(Collectors.toList()).contains(refVoucher.getRefID2())).collect(Collectors.toList());
            refVoucherRepository.deleteInBatch(removeList);
            refVoucherRepository.saveAll(addList);
        } else {
            refVoucherRepository.deleteByRefID1(gOtherVoucherId);
        }

        // ghi sổ
        UserDTO userDTO = userService.getAccount();
        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
            if (generalLedgerService.record(gOtherVoucher, new MessageDTO(Constants.GOtherVoucher.KET_CHUYEN))) {
                gOtherVoucher.setRecorded(true);
                gOtherVoucherRepository.save(gOtherVoucher);
            }
        }

        updateDataDTO.setUuid(gOtherVoucher.getId());
        updateDataDTO.setMessages(isUpdateGenCode ? Constants.UpdateDataDTOMessages.UPDATE_SUCCESS : Constants.UpdateDataDTOMessages.SAVE_SUCCESS);
        return updateDataDTO;
    }

    private void setGOtherVoucher(GOtherVoucherKcDTO gOtherVoucherKcDTO, GOtherVoucher gOtherVoucher) {
        gOtherVoucher.setTypeID(Constants.TypeId.TYPE_G_OTHER_VOUCHER_KC);
        gOtherVoucher.setDate(gOtherVoucherKcDTO.getDate());
        gOtherVoucher.setPostedDate(gOtherVoucherKcDTO.getPostedDate());
        gOtherVoucher.setCurrencyID(gOtherVoucherKcDTO.getCurrencyID());
        gOtherVoucher.setExchangeRate(gOtherVoucherKcDTO.getExchangeRate());
        gOtherVoucher.setReason(gOtherVoucherKcDTO.getReason());
        gOtherVoucher.setTotalAmount(gOtherVoucherKcDTO.getTotalAmount());
        gOtherVoucher.setTotalAmountOriginal(gOtherVoucherKcDTO.getTotalAmountOriginal());
        Boolean record = gOtherVoucherKcDTO.getRecorded() != null ? gOtherVoucherKcDTO.getRecorded() : false;
        gOtherVoucher.setRecorded(record);
    }

    public GOtherVoucher saveKc(GOtherVoucher gOtherVoucher) {
        log.debug("Request to save PPInvoice : {}", gOtherVoucher);
        if (gOtherVoucher.getId() == null) {
            gOtherVoucher.setId(UUID.randomUUID());
            //gan id cha cho details con
            for (GOtherVoucherDetails details : gOtherVoucher.getgOtherVoucherDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setgOtherVoucherID(gOtherVoucher.getId());
            }
        }
        GOtherVoucher gOt = new GOtherVoucher();
        try {
            gOt = gOtherVoucherRepository.save(gOtherVoucher);
        } catch (Exception ex) {
            log.debug("Request to save MBTellerPaper ERR : {}", ex.getMessage());
        }
        return gOt;
    }

    @Override
    public ResultDTO deleteById(UUID id) {
        Optional<GOtherVoucher> gOtherVoucherOptional = gOtherVoucherRepository.findById(id);
        if (gOtherVoucherOptional.isPresent() && !gOtherVoucherOptional.get().isRecorded()) {
            refVoucherRepository.deleteByRefID1(id);
            gOtherVoucherRepository.deleteById(id);
            return new ResultDTO(Constants.PPInvoiceResult.DELETE_SUCCESS);
        }
        return new ResultDTO(Constants.UpdateDataDTOMessages.NOT_FOUND);
    }

    @Override
    public GOtherVoucherKcDTO findIdByRowNumKc(Pageable pageable, Integer status, String fromDate, String toDate, String searchValue, Integer rowNum) {
        GOtherVoucherKcDTO gOtherVoucherKcDTO = new GOtherVoucherKcDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");

            GOtherVoucher gOtherVoucher = gOtherVoucherRepository.findIdByRowNumKc(pageable, status, fromDate, toDate, searchValue, rowNum, currentUserLoginAndOrg.get().getOrg(), isNoMBook);
            gOtherVoucherKcDTO = gOtherVoucherRepository.getGOtherVoucherById(gOtherVoucher.getId());
            List<GOtherVoucherDetails> gOtherVoucherDetailsList = gOtherVoucherDetailsRepository.findAllBygOtherVoucherIDOrderByOrderPriorityAsc(gOtherVoucher.getId());
            gOtherVoucherKcDTO.setgOtherVoucherDetails(gOtherVoucherDetailsList);
            gOtherVoucherKcDTO.setRefVouchers(refVoucherRepository.getRefViewVoucher(gOtherVoucher.getId(), isNoMBook));
        }
        return gOtherVoucherKcDTO;
    }

    public Page<GOtherVoucherDTO> searchAllPB(Pageable pageable, String fromDate, String toDate, String textSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return gOtherVoucherRepository.searchAllPB(pageable, fromDate, toDate, textSearch, currentUserLoginAndOrg.get().getOrg(), currentBook, false);
    }

    @Override
    public GOtherVoucherGeneralViewDTO findDetailViewPB(UUID id) {
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        GOtherVoucherGeneralViewDTO gOtherVoucherGeneralViewDTO = new GOtherVoucherGeneralViewDTO();
        List<GOtherVoucherDetailDTO> gOtherVoucherDetails = gOtherVoucherDetailsRepository.getGOtherVoucherViewID(id, currentBook);
        if (gOtherVoucherDetails != null && gOtherVoucherDetails.size() > 0) {
            gOtherVoucherGeneralViewDTO.setgOtherVoucherDetails(gOtherVoucherDetails);
        }
        List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocationRepository.findAllByGOtherVoucherViewID(id);
        if (gOtherVoucherDetailExpenseAllocations != null && gOtherVoucherDetailExpenseAllocations.size() > 0) {
            gOtherVoucherGeneralViewDTO.setgOtherVoucherDetailExpenseAllocations(gOtherVoucherDetailExpenseAllocations);
        }
        List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenseRepository.findAllByGOtherVoucherViewID(id);
        if (gOtherVoucherDetailExpenseAllocations != null && gOtherVoucherDetailExpenseAllocations.size() > 0) {
            gOtherVoucherGeneralViewDTO.setgOtherVoucherDetailExpenses(gOtherVoucherDetailExpenses);
        }
        boolean isNoMBook = currentBook.equals(1);
        List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        gOtherVoucherGeneralViewDTO.setRefVoucherDTOS(dtos);
        return gOtherVoucherGeneralViewDTO;
    }

    @Override
    public void deletePB(UUID id) {
        List<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenseRepository.findAllByGOtherVoucherID(id);
        gOtherVoucherRepository.deleteById(id);
        gOtherVoucherDetailExpenseAllocationRepository.deleteByIdPB(id);
        gOtherVoucherDetailExpenseRepository.deleteByIdPB(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
        List<UUID> listID = new ArrayList<>();
        for (GOtherVoucherDetailExpense gOtherVoucherDetailExpense : gOtherVoucherDetailExpenses) {
            listID.add(gOtherVoucherDetailExpense.getPrepaidExpenseID());
        }
//        if (listID != null && listID.size() > 0) {
//            prepaidExpenseRepository.updateAllocatedPeriod(false, listID);
//        }
    }

    @Override
    public GOtherVoucher findOneByRowNumPB(String fromDate, String toDate, String textSearch, Integer rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        GOtherVoucher gOtherVoucher = gOtherVoucherRepository.findOneByRowNumPB(fromDate, toDate, textSearch, rowNum, currentUserLoginAndOrg.get().getOrg(), currentBook);
        return gOtherVoucher;
    }

    @Override
    public byte[] exportExcelPB(Pageable pageable, String fromDate, String toDate, String textSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        Page<GOtherVoucherDTO> gOtherVoucherExportDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.searchAllPB(pageable, fromDate, toDate, textSearch, currentUserLoginAndOrg.get().getOrg(), currentBook, true)).orElse(null);
        return ExcelUtils.writeToFile(gOtherVoucherExportDTOS.getContent(), ExcelConstant.GOtherVoucherPB.NAME, ExcelConstant.GOtherVoucherPB.HEADER, ExcelConstant.GOtherVoucherPB.FIELD);

    }

    @Override
    public byte[] exportPDFPB(Pageable pageable, String fromDate, String toDate, String textSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        Page<GOtherVoucherDTO> gOtherVoucherExportDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.searchAllPB(pageable, fromDate, toDate, textSearch, currentUserLoginAndOrg.get().getOrg(), currentBook, true)).orElse(null);
        return PdfUtils.writeToFile(gOtherVoucherExportDTOS.getContent(), ExcelConstant.GOtherVoucherPB.HEADER, ExcelConstant.GOtherVoucherPB.FIELD);

    }

    @Override
    public Integer findRowNumByID(String fromDate, String toDate, String textSearch, UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return gOtherVoucherRepository.findRowNumByID(fromDate, toDate, textSearch, id, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    public byte[] exportPdfKc(Integer status, String fromDate, String toDate, String searchValue) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            Page<GOtherVoucherKcDsDTO> gOtherVoucherKcDsDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.searchGOtherVoucher(null, fromDate, toDate, status, searchValue, isNoMBook, currentUserLoginAndOrg.get().getOrg())).orElse(null);
            return PdfUtils.writeToFile(gOtherVoucherKcDsDTOS.getContent(), ExcelConstant.GOtherVoucherKc.HEADER, ExcelConstant.GOtherVoucherKc.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportExcelKc(Integer status, String fromDate, String toDate, String searchValue) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            Page<GOtherVoucherKcDsDTO> gOtherVoucherKcDsDTOS = currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.searchGOtherVoucher(null, fromDate, toDate, status, searchValue, isNoMBook, currentUserLoginAndOrg.get().getOrg())).orElse(null);
            return ExcelUtils.writeToFile(gOtherVoucherKcDsDTOS.getContent(), ExcelConstant.GOtherVoucherKc.NAME, ExcelConstant.GOtherVoucherKc.HEADER, ExcelConstant.GOtherVoucherKc.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataKc(String postDate) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.getDataKc(postDate, securityDTO.getOrg(), isNoMBook)).orElse(null);
        }
        return null;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataKcDiff(String postDate) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.getDataKcDiff(postDate, securityDTO.getOrg(), isNoMBook)).orElse(null);
        }
        return null;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataAccountSpecial(String postDate) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return currentUserLoginAndOrg.map(securityDTO -> gOtherVoucherRepository.getDataAccountSpecial(postDate, securityDTO.getOrg(), isNoMBook)).orElse(null);
        }
        return null;
    }

    public Long getPrepaidExpenseAllocationCount(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
//        đếm số lượng chi phí hợp lệ
        Long count = prepaidExpenseRepository.countAllByMonthAndYear(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
        if (count != null && count < 0) {
            throw new BadRequestAlertException("Invalid id", "GOtherVoucher", "countError");
        }
        return gOtherVoucherRepository.countAllByMonthAndYear(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public LocalDate getMaxMonth(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        Instant instant = gOtherVoucherRepository.getMaxMonth(id, currentBook);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public HandlingResultDTO multiDeletePB(List<GOtherVoucher> gOtherVouchers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(gOtherVouchers.size());
        List<GOtherVoucher> listDelete = gOtherVouchers.stream().sorted(Comparator.comparing(GOtherVoucher::getDate)).collect(Collectors.toList());
        Collections.reverse(listDelete);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        List<UUID> uuidListDelete = new ArrayList<>();
        UserDTO user = userService.getAccount();
        String currentBook = Utils.PhienSoLamViec(user).toString();
        int dem = 0;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Instant instant = gOtherVoucherRepository.getMaxMonthByCompanyID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        int maxMonth = listDelete.get(0).getDate().getMonthValue();
        Boolean check = false; //check xem có chứng từ nào phân bổ sau tháng lớn nhất của list không

        if (instant != null) {
            int checkMonth = instant.atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            if (maxMonth == checkMonth) {
                check = true;
            }
        } else  {
            check = false;
        }
        for (int i = 0; i < listDelete.size(); i++) {
            if (Boolean.TRUE.equals(listDelete.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(gOtherVouchers.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(listDelete.get(i));
                i--;
                dem++;
            } else {
                if (check && (maxMonth  == listDelete.get(i).getDate().getMonthValue())) {
                    if (i+1 < listDelete.size()) {
                        maxMonth = listDelete.get(i + 1).getDate().getMonthValue();
                    }
                } else {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đã tồn tại chứng từ phân bổ sau kỳ phân bổ này!");
                    BeanUtils.copyProperties(listDelete.get(i), viewVoucherNo);
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(listDelete.get(i));
                    i--;
//                    i--;
                }
                //            check  chứng từ Đã tồn tại chứng từ phân bổ sau kỳ phân bổ này
//                LocalDate date = gOtherVoucherRepository.getMaxMonth(gOtherVouchers.get(i).getId(), currentBook).atZone(ZoneId.systemDefault()).toLocalDate();
//                if (date != null && (date.compareTo(gOtherVouchers.get(i).getDate()) > 0)) {
//                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
//                    viewVoucherNo.setReasonFail("Chứng từ đã tồn tại chứng từ phân bổ sau kỳ phân bổ này!");
//                    BeanUtils.copyProperties(gOtherVouchers.get(i), viewVoucherNo);
//                    viewVoucherNoListFail.add(viewVoucherNo);
//                    listDelete.remove(gOtherVouchers.get(i));
//                }

            }
        }

        if (gOtherVouchers.size() == dem) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBDeposit", "errorDeleteList");
        }
        List<UUID> listID = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            listID.add(listDelete.get(i).getId());
        }
        if (listID != null && listID.size() > 0) {
            gOtherVoucherRepository.deleteByIdIn(listID);
            refVoucherRepository.deleteByRefID1sOrRefID2s(listID);
        }

        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

    @Override
    public GOtherVoucherGeneralDoubleClickDTO findDetailPB(UUID id) {
        // lấy dữ liệu phân bổ chi phí trả trước khi doubleclick
        GOtherVoucherGeneralDoubleClickDTO GOtherVoucherGeneralDTO = new GOtherVoucherGeneralDoubleClickDTO();
        GOtherVoucher gOtherVoucher = gOtherVoucherRepository.getOneGOtherVoucher(id);
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        if (gOtherVoucher != null) {
            GOtherVoucherGeneralDTO.setgOtherVoucher(gOtherVoucher);
        }
        List<GOtherVoucherDetails> gOtherVoucherDetails = gOtherVoucherDetailsRepository.findAllByGOtherVoucherID(id);
        if (gOtherVoucherDetails != null && gOtherVoucherDetails.size() > 0) {
            GOtherVoucherGeneralDTO.setgOtherVoucherDetails(gOtherVoucherDetails);
        }
        List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocationRepository.findAllByGOtherVoucherViewID(id);
        if (gOtherVoucherDetailExpenseAllocations != null && gOtherVoucherDetailExpenseAllocations.size() > 0) {
            GOtherVoucherGeneralDTO.setgOtherVoucherDetailExpenseAllocations(gOtherVoucherDetailExpenseAllocations);
        }
        List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenseRepository.findAllByGOtherVoucherViewID(id);
        if (gOtherVoucherDetailExpenseAllocations != null && gOtherVoucherDetailExpenseAllocations.size() > 0) {
            GOtherVoucherGeneralDTO.setgOtherVoucherDetailExpenses(gOtherVoucherDetailExpenses);
        }
        boolean isNoMBook = currentBook.equals(1);
        List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        GOtherVoucherGeneralDTO.setRefVoucherDTOS(dtos);
        return GOtherVoucherGeneralDTO;
    }


    @Override
    public HandlingResultDTO multiDelete(List<GOtherVoucher> gOtherVouchers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(gOtherVouchers.size());
        UserDTO userDTO = userService.getAccount();
        List<GOtherVoucher> listDeleteCTNVK = gOtherVouchers.stream().filter(a->!a.getTypeID().equals(709)).collect(Collectors.toList());
        List<GOtherVoucher> listDelete = gOtherVouchers.stream().filter(a->!a.getTypeID().equals(709)).collect(Collectors.toList());
        List<GOtherVoucher> listDeletePBCPTT = gOtherVouchers.stream().filter(a->a.getTypeID().equals(709)).collect(Collectors.toList());
        List<GOtherVoucher> listPBCPTT = gOtherVouchers.stream().filter(a->a.getTypeID().equals(709)).collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        List<UUID> uuidListDelete = new ArrayList<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String currentBook = Utils.PhienSoLamViec(userDTO).toString();
        Instant instant = gOtherVoucherRepository.getMaxMonthByCompanyID(currentUserLoginAndOrg.get().getOrg(), currentBook);
        if (listDeletePBCPTT.size() > 0) {
            int maxMonth = listDeletePBCPTT.get(0).getDate().getMonthValue();
            Boolean check = false; //check xem có chứng từ nào phân bổ sau tháng lớn nhất của list không
            if (instant != null) {
                int checkMonth = instant.atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
                if (maxMonth == checkMonth) {
                    check = true;
                }
            } else {
                check = false;
            }
            for (int i = 0; i < listPBCPTT.size(); i++) {
                if (Boolean.TRUE.equals(listPBCPTT.get(i).getRecorded())) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                    BeanUtils.copyProperties(listPBCPTT.get(i), viewVoucherNo);
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDeletePBCPTT.remove(listPBCPTT.get(i));
                } else {
                    if (check && (maxMonth == listPBCPTT.get(i).getDate().getMonthValue())) {
                        if (i + 1 < listPBCPTT.size()) {
                            maxMonth = listPBCPTT.get(i + 1).getDate().getMonthValue();
                        }
                    } else {
                        ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                        viewVoucherNo.setReasonFail("Chứng từ đã tồn tại chứng từ phân bổ sau kỳ phân bổ này!");
                        BeanUtils.copyProperties(listPBCPTT.get(i), viewVoucherNo);
                        viewVoucherNoListFail.add(viewVoucherNo);
                        listDeletePBCPTT.remove(listPBCPTT.get(i));
                    }
                }
            }
        }
        for (int i = 0; i < listDeleteCTNVK.size(); i++) {
            if (Boolean.TRUE.equals(listDeleteCTNVK.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(listDeleteCTNVK.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(listDeleteCTNVK.get(i));
            }
        }
        if (listDeletePBCPTT.size() > 0) {
            for (GOtherVoucher item : listDeletePBCPTT) {
                listDelete.add(item);
            }
        }

        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName(typeRepository.findTypeNameByTypeID(viewVoucherNoListFail.get(i).getTypeID()));
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        if (listDelete.size() == 0 && listPBCPTT.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "gOTherVoucher", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        for (int i = 0; i < listDelete.size(); i++) {
            uuidListDelete.add(listDelete.get(i).getId());
        }
        // Gan' TypeName
        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName(typeRepository.findTypeNameByTypeID(viewVoucherNoListFail.get(i).getTypeID()));
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        if (uuidListDelete.size() > 0) {
            cpAcceptanceDetailsRepository.deleteByGOtherVoucherID(uuidListDelete);
            gOtherVoucherRepository.multiDeleteGOtherVoucher(currentUserLoginAndOrg.get().getOrg(), uuidListDelete);
            gOtherVoucherRepository.multiDeleteGOtherVoucherChild("GOtherVoucherDetail", uuidListDelete);
            gOtherVoucherRepository.multiDeleteGOtherVoucherChild("GOtherVoucherDetailTax", uuidListDelete);
            gOtherVoucherRepository.multiDeleteGOtherVoucherChild("GOtherVoucherDetailExpenseAllocation", uuidListDelete);
            gOtherVoucherRepository.multiDeleteGOtherVoucherChild("GOtherVoucherDetailExpense", uuidListDelete);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidListDelete);
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<GOtherVoucher> gOtherVouchers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(gOtherVouchers.size());
        List<GOtherVoucher> listDelete = gOtherVouchers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        for (GOtherVoucher g : gOtherVouchers) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(g.getPostedDate().toString());
            if (Boolean.FALSE.equals(g.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(g, viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(g);
            } else {
                if (Boolean.TRUE.equals(g.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.compareTo(postedDate) >= 0) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                    viewVoucherNo.setPostedDate(g.getPostedDate());
                    viewVoucherNo.setDate(g.getDate());
                    viewVoucherNo.setRefID(g.getId());
                    viewVoucherNo.setNoFBook(g.getNoFBook());
                    viewVoucherNo.setNoMBook(g.getNoMBook());
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(g);
                }
            }
        }
        List<UUID> uuidList = new ArrayList<>();
        for (GOtherVoucher g : listDelete) {
            uuidList.add(g.getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        if (uuidList.size() > 0) {
            gOtherVoucherRepository.updateUnRecord(uuidList,currentUserLoginAndOrg.get().getOrg());
            gOtherVoucherRepository.deleteRefIDInGL(uuidList, currentUserLoginAndOrg.get().getOrg());
        }
        return handlingResultDTO;
    }
}
