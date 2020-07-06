package vn.softdreams.ebweb.service.impl;

import org.springframework.beans.BeanUtils;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.PrepaidExpenseAllocationService;
import vn.softdreams.ebweb.service.PrepaidExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PrepaidExpense.
 */
@Service
@Transactional
public class PrepaidExpenseServiceImpl implements PrepaidExpenseService {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseServiceImpl.class);

    private final PrepaidExpenseRepository prepaidExpenseRepository;

    private final PrepaidExpenseVoucherRepository prepaidExpenseVoucherRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final PrepaidExpenseAllocationRepository prepaidExpenseAllocationRepository;

    private UserService userService;

    private final UtilsService utilsService;

    private final SystemOptionRepository systemOptionRepository;

    public PrepaidExpenseServiceImpl(PrepaidExpenseRepository prepaidExpenseRepository, PrepaidExpenseVoucherRepository prepaidExpenseVoucherRepository, OrganizationUnitRepository organizationUnitRepository, PrepaidExpenseAllocationService prepaidExpenseAllocationService, PrepaidExpenseAllocationRepository prepaidExpenseAllocationRepository, UserService userService, UtilsService utilsService, SystemOptionRepository systemOptionRepository) {
        this.prepaidExpenseRepository = prepaidExpenseRepository;
        this.prepaidExpenseVoucherRepository = prepaidExpenseVoucherRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.prepaidExpenseAllocationRepository = prepaidExpenseAllocationRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a prepaidExpense.
     *
     * @param prepaidExpenseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrepaidExpense save(PrepaidExpense prepaidExpenseDTO) {
        log.debug("Request to save PrepaidExpense : {}", prepaidExpenseDTO);
//        PrepaidExpenseDTO prepaidExpenseDTO1 = new PrepaidExpenseDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        PrepaidExpense prepaidExpense = prepaidExpenseDTO.getPrepaidExpense();
//        List<PrepaidExpenseVoucher> prepaidExpenseVoucher = null;
//        if (!prepaidExpenseDTO.getPrepaidExpenseVoucher().isEmpty()) {
//            prepaidExpenseVoucher = prepaidExpenseDTO.getPrepaidExpenseVoucher();
//        }
        prepaidExpenseDTO.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (prepaidExpenseDTO.getPrepaidExpenseCode() != null) {
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Long count = prepaidExpenseRepository.countAllByPrepaidExpenseCode(prepaidExpenseDTO.getPrepaidExpenseCode(), currentUserLoginAndOrg.get().getOrg(), prepaidExpenseDTO.getId(), currentBook);
            if (count > 0) {
                throw new BadRequestAlertException("prepaidExpense", "duplicatePrepaidExpenseCode", "idexists");
            }
        } else {
            throw new BadRequestAlertException("prepaidExpense", "nullPrepaidExpenseCode", "idexists");
        }
//        if (prepaidExpense.getTypeExpense().equals('0')) {
//            prepaidExpense.setAllocatedPeriod(null);
//            prepaidExpense.setAllocationAmount(null);
//        }
//        if (prepaidExpenseVoucher != null && prepaidExpenseVoucher.size() > 0) {
//            prepaidExpenseDTO1.setPrepaidExpenseVoucher(prepaidExpenseVoucherRepository.saveAll(prepaidExpenseVoucher));
//        }
//        prepaidExpenseDTO1.setPrepaidExpense(prepaidExpenseRepository.save(prepaidExpenseDTO));
        return prepaidExpenseRepository.save(prepaidExpenseDTO);

    }

    /**m
     * Get all the prepaidExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrepaidExpense> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaidExpenses");
        return prepaidExpenseRepository.findAll(pageable);
    }


    /**
     * Get one prepaidExpense by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaidExpense> findOne(UUID id) {
        log.debug("Request to get PrepaidExpense : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.findOneById(id, currentBook, currentUserLoginAndOrg.get().getOrg());
    }

    /**
     * Delete the prepaidExpense by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PrepaidExpense : {}", id);
        prepaidExpenseVoucherRepository.deleteAllByRefID(id);
        prepaidExpenseRepository.deleteById(id);
    }

    @Override
    public List<PrepaidExpenseCodeDTO> getPrepaidExpenseCode() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
         Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.getPrepaidExpenseCode(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP), currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public List<AccountList> getCostAccounts() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return prepaidExpenseRepository.getCostAccounts(currentUserLoginAndOrg.get().getOrgGetData());
    }

    @Override
    public Page<PrepaidExpenseAllDTO> getAll(Pageable pageable, Integer typeExpense, String fromDate, String toDate, String textSearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.getAll(pageable, typeExpense, fromDate, toDate, textSearch,currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public List<RefVoucherSecondDTO> findPrepaidExpenseByID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.findPrepaidExpenseByID(id, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public PrepaidExpenseObjectConvertDTO getPrepaidExpenseAllocation(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        PrepaidExpenseObjectConvertDTO prepaidExpenseObjectConvertDTO = new PrepaidExpenseObjectConvertDTO();
        List<PrepaidExpenseConvertDTO> prepaidExpenseConvertDTOS = prepaidExpenseRepository.getPrepaidExpenseAllocation(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
//        lấy danh sách prepaidExpenseid để lấy ra các detail
        List<UUID> listID = new ArrayList<>();
        if (prepaidExpenseConvertDTOS != null && prepaidExpenseConvertDTOS.size() > 0) {
            for (int i = 0; i < prepaidExpenseConvertDTOS.size(); i++) {
                listID.add(prepaidExpenseConvertDTOS.get(i).getPrepaidExpenseID());
            }
        }
        prepaidExpenseObjectConvertDTO.setPrepaidExpenseConvertDTOS(prepaidExpenseConvertDTOS);
        prepaidExpenseObjectConvertDTO.setPrepaidExpenseAllocations(prepaidExpenseAllocationRepository.findAllByExpenseListItemID(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook, listID));
        return prepaidExpenseObjectConvertDTO;
    }

    @Override
    public Long getPrepaidExpenseAllocationCount(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.countAllByMonthAndYear(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public GOtherVoucher getPrepaidExpenseAllocationDuplicate(Integer month, Integer year) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.getPrepaidExpenseAllocationDuplicate(month, year, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public Long countByPrepaidExpenseID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.countByPrepaidExpenseID(id, currentBook);
    }

    @Override
    public List<PrepaidExpenseAllocation> getPrepaidExpenseAllocationByID(UUID id) {
        return prepaidExpenseAllocationRepository.findByPrepaidExpenseID(id);
    }

    @Override
    public Page<PrepaidExpense> getPrepaidExpenseByCurrentBookToModal(Pageable pageable, String fromDate, String toDate, Integer typeExpense) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return prepaidExpenseAllocationRepository.getPrepaidExpenseByCurrentBookToModal(pageable, fromDate, toDate, typeExpense, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    public List<PrepaidExpense> getPrepaidExpensesByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return prepaidExpenseRepository.getPrepaidExpensesByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public HandlingResultDTO multiDelete(List<PrepaidExpense> prepaidExpense) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(prepaidExpense.size());
        List<PrepaidExpense> listDelete = prepaidExpense.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();

        UserDTO user = userService.getAccount();
        String currentBook = Utils.PhienSoLamViec(user).toString();
        int dem = 0;
        for (int i = 0; i < prepaidExpense.size(); i++) {
            // check  chứng từ Đã tồn tại chứng từ phân bổ sau kỳ phân bổ này
            Long count = prepaidExpenseRepository.countByPrepaidExpenseID(prepaidExpense.get(i).getId(), currentBook);
            if (count > 0) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Không thể xóa chi phí vì đã phát sinh chứng từ phân bổ!");
                BeanUtils.copyProperties(prepaidExpense.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(prepaidExpense.get(i));
            }

        }
        List<UUID> listID = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            listID.add(listDelete.get(i).getId());
        }
        if (listID != null && listID.size() > 0) {
            prepaidExpenseRepository.deleteByIdIn(listID);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

    @Override
    public void updateIsActive(UUID id) {
        prepaidExpenseRepository.updateIsActive(id);
    }

    @Override
    public List<PrepaidExpenseCodeDTO> getPrepaidExpenseCodeCanActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return prepaidExpenseRepository.getPrepaidExpenseCodeCanActive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP), currentUserLoginAndOrg.get().getOrg(), currentBook);
    }
}
