package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CreditCardService;
import vn.softdreams.ebweb.domain.CreditCard;
import vn.softdreams.ebweb.repository.CreditCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CreditCardSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTheTD;

/**
 * Service Implementation for managing CreditCard.
 */
@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private final Logger log = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private final CreditCardRepository creditCardRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;
    private final SystemOptionRepository systemOptionRepository;

    public CreditCardServiceImpl(OrganizationUnitRepository organizationUnitRepository, CreditCardRepository creditCardRepository, UtilsService utilsService, SystemOptionRepository systemOptionRepository) {
        this.creditCardRepository = creditCardRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a creditCard.
     *
     * @param creditCard the entity to save
     * @return the persisted entity
     */
    @Override
//    public CreditCard save(CreditCard creditCard) {
//        log.debug("Request to save CreditCard : {}", creditCard);
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        if (currentUserLoginAndOrg.isPresent()) {
//            creditCard.setCompanyID(currentUserLoginAndOrg.get().getOrg());
//        }
//        return creditCardRepository.save(creditCard);
//    }

    public CreditCardSaveDTO save(CreditCard creditCard) {
        log.debug("Request to save CreditCard : {}", creditCard);
        CreditCard curr = new CreditCard();
        CreditCardSaveDTO creditCardSaveDTO = new CreditCardSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (currentUserLoginAndOrg.isPresent()) {
            creditCard.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        if (creditCard.getId() == null) {
            creditCard.setId(UUID.randomUUID());
            int count = creditCardRepository.countByCreditCardNumberIgnoreCaseAndIsActiveTrue(creditCard.getCreditCardNumber(), currentUserLoginAndOrg.get().getOrg());
            if (count > 0) {
                creditCardSaveDTO.setCreditCard(creditCard);
                creditCardSaveDTO.setStatus(count);
                return creditCardSaveDTO;
            } else {
                curr = creditCardRepository.save(creditCard);
                creditCardSaveDTO.setCreditCard(curr);
                creditCardSaveDTO.setStatus(count);
                return creditCardSaveDTO;
            }
        } else {
            curr = creditCardRepository.save(creditCard);
            creditCardSaveDTO.setCreditCard(curr);
            creditCardSaveDTO.setStatus(0);
            return creditCardSaveDTO;
        }
    }

    @Override
    public Page<CreditCard> findAll() {
        return new PageImpl<CreditCard>(creditCardRepository.findAll());
    }

    /**
     * Get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CreditCard> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCards");
        return creditCardRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCard> pageableAllCreditCard(Pageable pageable) {
        log.debug("Request to get all CreditCards");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return creditCardRepository.pageableAllCreditCard(pageable, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCard> pageableAllCreditCards(Pageable pageable, Boolean isGetAllCompany) {
        log.debug("Request to get all CreditCard");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (isGetAllCompany) {
                List<UUID> listCompanyID = new ArrayList<>();
                Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
                if (organizationUnit.get().getUnitType().equals(Constants.OrgUnitTypeConstant.TONG_CONG_TY)) {
                    listCompanyID.add(currentUserLoginAndOrg.get().getOrg());
                    List<OrganizationUnit> listCompany = organizationUnitRepository.getChildCom(currentUserLoginAndOrg.get().getOrg(), Constants.OrgUnitTypeConstant.CHI_NHANH);
                    for (OrganizationUnit item : listCompany) {
                        listCompanyID.add(item.getId());
                    }
                } else if (organizationUnit.get().getUnitType().equals(Constants.OrgUnitTypeConstant.CHI_NHANH)) {
                    listCompanyID.add(organizationUnit.get().getParentID());
                    List<OrganizationUnit> listCompany = organizationUnitRepository.getChildCom(organizationUnit.get().getParentID(), Constants.OrgUnitTypeConstant.CHI_NHANH);
                    for (OrganizationUnit item : listCompany) {
                        listCompanyID.add(item.getId());
                    }
                }
                return creditCardRepository.getAllByListCompany(pageable, listCompanyID);
            } else {
                return creditCardRepository.pageableAllCreditCards(pageable, currentUserLoginAndOrg.get().getOrg());
            }
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one creditCard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCard> findOne(UUID id) {
        log.debug("Request to get CreditCard : {}", id);
        return creditCardRepository.findById(id);
    }

    /**
     * Delete the creditCard by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CreditCard : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "CreditCardID");
        if (checkUsedAcc) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            creditCardRepository.deleteById(id);
        }
    }

    @Override
    public HandlingResultDTO deleteEmployee(List<UUID> uuids) {
        log.debug("Request to delete CreditCard : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = creditCardRepository.getIDRefEmployee(uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            creditCardRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public CreditCard findByCreditCardNumber(String creditCardNumber) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return creditCardRepository.findByCreditCardNumber(creditCardNumber, currentUserLoginAndOrg.get().getOrg());
    }

    public List<CreditCard> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return creditCardRepository.findAllByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMTheTD));
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<CreditCard> findAllActiveByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return creditCardRepository.findAllActiveByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
