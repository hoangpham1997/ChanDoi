package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.BankAccountDetailsService;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.repository.BankAccountDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.BankAccountDetailsSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.*;

/**
 * Service Implementation for managing BankAccountDetails.
 */
@Service
@Transactional
public class BankAccountDetailsServiceImpl implements BankAccountDetailsService {

    private final Logger log = LoggerFactory.getLogger(BankAccountDetailsServiceImpl.class);

    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;
    private final SystemOptionRepository systemOptionRepository;

    public BankAccountDetailsServiceImpl(BankAccountDetailsRepository bankAccountDetailsRepository,
                                         OrganizationUnitRepository organizationUnitRepository,
                                         UtilsService utilsService, SystemOptionRepository systemOptionRepository) {
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a bankAccountDetails.
     *
     * @param bankAccountDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public BankAccountDetailsSaveDTO save(BankAccountDetails bankAccountDetails) {
        log.debug("Request to save BankAccountDetails : {}", bankAccountDetails);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        BankAccountDetails bankAccountDetails1 = new BankAccountDetails();
        BankAccountDetailsSaveDTO bankAccountDetailsSaveDTO = new BankAccountDetailsSaveDTO();
        if (currentUserLoginAndOrg.isPresent()) {
            if (bankAccountDetailsRepository.findByCompanyIDAndBankAccount(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(),
                        TCKHAC_SDDMTKNH), bankAccountDetails.getBankAccount(),
                bankAccountDetails.getId() == null ? new UUID(0L, 0L): bankAccountDetails.getId()) != null) {
                bankAccountDetailsSaveDTO.setStatus(1);
                bankAccountDetailsSaveDTO.setBankAccountDetails(bankAccountDetails);
                return bankAccountDetailsSaveDTO;
            }
            bankAccountDetails.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (bankAccountDetails.getId() == null) {
                bankAccountDetails.setIsActive(true);
            }
            bankAccountDetails1 = bankAccountDetailsRepository.save(bankAccountDetails);
            bankAccountDetailsSaveDTO.setBankAccountDetails(bankAccountDetails1);
            bankAccountDetailsSaveDTO.setStatus(0);
        }
        return bankAccountDetailsSaveDTO;
    }
    /**
     * Get all the bankAccountDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BankAccountDetails> findAll(Pageable pageable) {
        log.debug("Request to get all BankAccountDetails");
        return bankAccountDetailsRepository.findAll(pageable);
    }

    @Override
    public Page<BankAccountDetails> findAll() {
        return new PageImpl<BankAccountDetails>(bankAccountDetailsRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankAccountDetails> pageableAllBankAccountDetails(Pageable pageable) {
        log.debug("Request to get all BankAccountDetails");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean check = systemOptionRepository.findByCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH) == 1;
        if (!check) {
            return bankAccountDetailsRepository.pageableAllBankAccountDetails(pageable, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH));
        } else {
            return bankAccountDetailsRepository.pageableAllBankAccountDetails(pageable, Stream.of(currentUserLoginAndOrg.get().getOrg()).collect(Collectors.toList()));
        }
    }

    /**
     * Get one bankAccountDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BankAccountDetails> findOne(UUID id) {
        log.debug("Request to get BankAccountDetails : {}", id);
        return bankAccountDetailsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankAccountDetails> findID(UUID id) {
        log.debug("Request to get BankAccountDetails : {}", id);
        return bankAccountDetailsRepository.findById(id);
    }

    /**
     * Delete the bankAccountDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete BankAccountDetails : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "BankAccountDetailID");
        if (checkUsedAcc) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            bankAccountDetailsRepository.deleteById(id);
        }
    }

    public List<ComboboxBankAccountDetailDTO> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<UUID> listCompanyIDTKNH = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH);
            List<UUID> listCompanyIDTTD = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTheTD);
            return bankAccountDetailsRepository.findAllByIsActive(listCompanyIDTKNH, listCompanyIDTTD, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<ComboboxBankAccountDetailDTO> findAllForAccType() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.findAllForAccType(currentUserLoginAndOrg.get().getOrg(), currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<BankAccountDetails> getAllBankAccountDetailsByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.getAllBankAccountDetailsByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<BankAccountDetails> findAllActiveCustom() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.findAllByIsActiveCustom(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH), systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTheTD), currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<BankAccountDetails> getAllBankAccountDetailsByOrgID(UUID orgID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.getAllBankAccountDetailsByOrgID(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(orgID, TCKHAC_SDDMTKNH), systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(orgID, TCKHAC_SDDMTheTD), currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<BankAccountDetails> allBankAccountDetails() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.allBankAccountDetails(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public HandlingResultDTO deleteBank(List<UUID> uuids) {
        log.debug("Request to delete BankAccountDetails : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = bankAccountDetailsRepository.getIDRef(currentUserLoginAndOrg.get().getOrg(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            bankAccountDetailsRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    public List<BankAccountDetails> getAllBankAccountDetailsActiveCompanyIDNotCreditCard() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return bankAccountDetailsRepository.getAllBankAccountDetailsActiveCompanyIDNotCreditCard(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMTKNH));
        }
        throw new BadRequestAlertException("", "", "");
    }
}
