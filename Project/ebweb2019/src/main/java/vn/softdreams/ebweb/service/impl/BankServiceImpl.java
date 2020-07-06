package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.domain.SearchVoucherBank;
import vn.softdreams.ebweb.repository.BankAccountDetailsRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.BankService;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.BankConvertDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.BankSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Bank.
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final UserService userService;
    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final UtilsService utilsService;

    public BankServiceImpl(BankRepository bankRepository,
                           UserService userService, SystemOptionRepository systemOptionRepository,
                           BankAccountDetailsRepository bankAccountDetailsRepository, UtilsService utilsService) {
        this.bankRepository = bankRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.userService = userService;
        this.utilsService = utilsService;
    }

    /**
     * Save a bank.
     *
     * @param bank the entity to save
     * @return the persisted entity
     */
    @Override
    public BankSaveDTO save(Bank bank) {
        log.debug("Request to save Currency : {}", bank);
        Bank curr = new Bank();
        BankSaveDTO bankSaveDTO = new BankSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            bank.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }

        if (bank.getId() == null) {
            int count = bankRepository.countByBankCodeIgnoreCaseAndIsActiveTrue(bank.getBankCode(), currentUserLoginAndOrg.get().getOrgGetData());
            if (count > 0) {
                bankSaveDTO.setBank(bank);
                bankSaveDTO.setStatus(count);
                return bankSaveDTO;
            } else {
                curr = bankRepository.save(bank);
                bankSaveDTO.setBank(curr);
                bankSaveDTO.setStatus(count);
                return bankSaveDTO;
            }
        } else {
            curr = bankRepository.save(bank);
            bankSaveDTO.setBank(curr);
            bankSaveDTO.setStatus(0);
            return bankSaveDTO;
        }
    }

    @Override
    public Page<Bank> findAll() {
        return new PageImpl<Bank>(bankRepository.findAll());
    }

    /**
     * Get all the banks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Bank> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bank> pageableAllBank(Pageable pageable) {
        log.debug("Request to get all Banks");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankRepository.pageableAllBank(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Get one bank by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Bank> findOne(UUID id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id);
    }

    /**
     * Delete the bank by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Bank : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        BankAccountDetails bankAccountDetails = bankAccountDetailsRepository.findByCompanyIDAndUnitID(currentUserLoginAndOrg.get().getOrgGetData(), id);
        if (bankAccountDetails != null) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            bankRepository.deleteById(id);
        }
    }

    @Override
    public HandlingResultDTO delete(List<UUID> uuids) {
        log.debug("Request to delete Bank : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = bankRepository.getIDRef(currentUserLoginAndOrg.get().getOrgGetData(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
         if (uuidListDelete.size() > 0){
            bankRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public Page<Bank> findAllBank(Pageable pageable, SearchVoucherBank searchVoucherBank) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankRepository.findAllBank
                (pageable, searchVoucherBank, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    @Override
    public Page<Bank> findAll(Pageable pageable, String bankCode, String bankName, String bankNameRepresent, String address, String description, Boolean isActive) {
        return bankRepository.findAll(pageable, bankCode, bankName, bankNameRepresent, address, description, isActive);
    }


    public List<Bank> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<Bank> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<Bank> findAllBankByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankRepository.findAllBankByCompanyIDIn(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        throw new BadRequestAlertException("", "", "");
    }
}
