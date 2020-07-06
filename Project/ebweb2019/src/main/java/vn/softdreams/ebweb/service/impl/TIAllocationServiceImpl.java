package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.TIAllocation;
import vn.softdreams.ebweb.domain.TIAllocationDetails;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.service.util.TypeGroupConstant;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import javax.tools.Tool;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAllocation.
 */
@Service
@Transactional
public class TIAllocationServiceImpl implements TIAllocationService {

    private final Logger log = LoggerFactory.getLogger(TIAllocationServiceImpl.class);

    private final TIAllocationRepository tIAllocationRepository;

    private final UtilsService utilsService;

    private final ToolsRepository toolsRepository;

    private final UtilsRepository utilsRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final UserService userService;

    private final GenCodeService genCodeService;

    private final GeneralLedgerService generalLedgerService;

    private final RefVoucherRepository refVoucherRepository;

    public TIAllocationServiceImpl(TIAllocationRepository tIAllocationRepository, UtilsService utilsService, ToolsRepository toolsRepository, UtilsRepository utilsRepository, OrganizationUnitRepository organizationUnitRepository, UserService userService, GenCodeService genCodeService, GeneralLedgerService generalLedgerService, RefVoucherRepository refVoucherRepository) {
        this.tIAllocationRepository = tIAllocationRepository;
        this.utilsService = utilsService;
        this.toolsRepository = toolsRepository;
        this.utilsRepository = utilsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.refVoucherRepository = refVoucherRepository;
    }

    /**
     * Save a tIAllocation.
     *
     * @param tiAllocationSaveConvertDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAllocation save(TIAllocationSaveConvertDTO tiAllocationSaveConvertDTO) {
        log.debug("Request to save TIAllocation : {}", tiAllocationSaveConvertDTO.getTiAllocation());
        TIAllocation tiAllocation = tiAllocationSaveConvertDTO.getTiAllocation();
        if (tiAllocation.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }
        List<UUID> uuidList = new ArrayList<>();
        for (TIAllocationDetails item : tiAllocation.getTiAllocationDetails()) {
            uuidList.add(item.getToolsID());
        }
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            tiAllocation.typeLedger(Integer.valueOf(currentBook));
            // check sổ làm việc
            // phiên làm việc
            if (Constants.TypeLedger.BOTH_BOOK.equals(tiAllocation.getTypeLedger())) {
                if (currentBook.equals("0")) {
                    if (tiAllocation.getId() == null) {
                        tiAllocation.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CCDC, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (tiAllocation.getId() == null) {
                        tiAllocation.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CCDC, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            } else if (Constants.TypeLedger.MANAGEMENT_BOOK.equals(tiAllocation.getTypeLedger())) {
                if (tiAllocation.getNoFBook() != null) {
                    tiAllocation.setNoFBook(null);
                }
            } else if (Constants.TypeLedger.FINANCIAL_BOOK.equals(tiAllocation.getTypeLedger())) {
                if (tiAllocation.getNoMBook() != null) {
                    tiAllocation.setNoMBook(null);
                }
            }
            tiAllocation.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsRepository.checkDuplicateNoVoucher(tiAllocation.getNoFBook(), tiAllocation.getNoMBook(), Integer.valueOf(currentBook), tiAllocation.getId())) {
                throw new BadRequestAlertException("discountreturn", "voucherExists", "");
            }
            List<RefVoucher> refVouchers = tiAllocationSaveConvertDTO.getViewVouchers();
            if (refVouchers != null && refVouchers.size() > 0) {
                for (RefVoucher item : refVouchers) {
                    item.setRefID1(tiAllocation.getId());
                    item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                }
            }
            refVoucherRepository.deleteByRefID1(tiAllocation.getId());
            if (refVouchers != null && refVouchers.size() > 0) {
                refVoucherRepository.saveAll(refVouchers);
            }
            tiAllocation.setTypeID(TypeConstant.PHAN_BO_CCDC);
            tiAllocation.setRecorded(false);
            tiAllocation = tIAllocationRepository.save(tiAllocation);
            Long count = tIAllocationRepository.countToolsAllocation(uuidList, tiAllocation.getPostedDate());
            tiAllocation.setStatusSave(count > 0 ? 1 : 0);
            if (count <= 0) {
                if (tiAllocation.getRecorded() == null || tiAllocation.getRecorded()) {
                    if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                        MessageDTO msg = new MessageDTO("");
                        if (!generalLedgerService.record(tiAllocation, msg)) {
                            tiAllocation.setRecorded(false);
                        } else {
                            tiAllocation.setRecorded(true);
                        }
                    }
                }
            }
        }
        return  tIAllocationRepository.save(tiAllocation);
    }

    /**
     * Get all the tIAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAllocation> findAll(Pageable pageable) {
        log.debug("Request to get all TIAllocations");
        return tIAllocationRepository.findAll(pageable);
    }


    /**
     * Get one tIAllocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAllocation> findOne(UUID id) {
        log.debug("Request to get TIAllocation : {}", id);
        return tIAllocationRepository.findById(id);
    }

    /**
     * Delete the tIAllocation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAllocation : {}", id);
        tIAllocationRepository.deleteById(id);
    }

    @Override
    public List<TIAllocationDetailConvertDTO> getTIAllocationDetails(Integer month, Integer year, Integer date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        String checkPeriodic = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_PBCCDC)).findAny().get().getData();
        List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS = tIAllocationRepository.getTIAllocationDetails(currentUserLoginAndOrg.get().getOrg(), month, year, date, checkPeriodic);
        if (!tiAllocationDetailConvertDTOS.isEmpty()) {
            for (int i = 0; i < tiAllocationDetailConvertDTOS.size(); i++) {
                List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS = toolsRepository.getToolsDetailsByID(tiAllocationDetailConvertDTOS.get(i).getToolsID());
                tiAllocationDetailConvertDTOS.get(i).setToolsDetailsConvertDTOS(toolsDetailsConvertDTOS);
            }
        }
        return tiAllocationDetailConvertDTOS;
    }

    @Override
    public List<Tool> getTIAllocations(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return tIAllocationRepository.getTIAllocations(currentUserLoginAndOrg.get().getOrg(), month, year);
    }

    @Override
    public Long countToolsAllocation(List<UUID> uuidList, String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return null;
    }

    @Override
    public Long getTIAllocationCount(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
//        đếm số lượng chi phí hợp lệ
//        Long count = tIAllocationRepository.countAllByMonthAndYear(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
//        if (count != null && count < 0) {
//            throw new BadRequestAlertException("Invalid id", "GOtherVoucher", "countError");
//        }
        return tIAllocationRepository.countAllByMonthAndYear(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public TIAllocation getTIAllocationDuplicate(Integer month, Integer year) {

        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return tIAllocationRepository.getTIAllocationDuplicate(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public LocalDate getMaxMonth(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        Instant instant = tIAllocationRepository.getMaxMonth(id, currentBook);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public Page<TIAllocationConvertDTO> getAllTIAllocationSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIAllocationRepository.getAllTIAllocationSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }
}
