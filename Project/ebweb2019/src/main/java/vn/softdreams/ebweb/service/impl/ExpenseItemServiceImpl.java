package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.BudgetItem;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.ExpenseItemService;
import vn.softdreams.ebweb.domain.ExpenseItem;
import vn.softdreams.ebweb.repository.ExpenseItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.DetailDelFailCategoryDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ExpenseItem.
 */
@Service
@Transactional
public class ExpenseItemServiceImpl implements ExpenseItemService {

    private final Logger log = LoggerFactory.getLogger(ExpenseItemServiceImpl.class);

    private final ExpenseItemRepository expenseItemRepository;
    private final UtilsRepository utilsRepository;
    private final OrganizationUnitRepository organizationUnitRepository;

    public ExpenseItemServiceImpl(ExpenseItemRepository expenseItemRepository, UtilsRepository utilsRepository, OrganizationUnitRepository organizationUnitRepository) {
        this.expenseItemRepository = expenseItemRepository;
        this.utilsRepository = utilsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a expenseItem.
     *
     * @param expenseItem the entity to save
     * @return the persisted entity
     */
    @Override
    public ExpenseItem save(ExpenseItem expenseItem) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        expenseItem.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (currentUserLoginAndOrg.isPresent()) {
            expenseItem.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            Integer count = this.expenseItemRepository.checkDuplicateExpenseItemCode(expenseItem.getId() == null ? UUID.randomUUID() : expenseItem.getId(), currentUserLoginAndOrg.get().getOrg(), expenseItem.getExpenseItemCode());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("", "ExpenseItem", "expenseItemCodeDuplicate");
            }

            //update grade for expense and isParentNode for expenseItemcode's parent
            if (expenseItem.getParentID() != null) {
                Optional<ExpenseItem> parentExpenseItemCode = expenseItemRepository.findById(expenseItem.getParentID());
                expenseItem.setGrade(parentExpenseItemCode.get().getGrade() + 1);
                parentExpenseItemCode.get().setIsParentNode(true);
                this.expenseItemRepository.save(parentExpenseItemCode.get());
            } else {
                expenseItem.setGrade(1);
                if (expenseItem.getId() == null) {
                    expenseItem.setIsParentNode(null);
                }
            }

            //update isActive and grade for expense's descendants
            if (expenseItem.getId() != null) {
                Optional<ExpenseItem> currentExpenseItemCode = this.expenseItemRepository.findById(expenseItem.getId());
                if (currentExpenseItemCode.get().getIsActive() != expenseItem.getIsActive() || currentExpenseItemCode.get().getGrade() != expenseItem.getGrade()) {
                    List<ExpenseItem> descendants = new ArrayList<>();
                    descendants = this.updateAllChildren(descendants, expenseItem);
                    this.expenseItemRepository.saveAll(descendants);
                }
                if (expenseItem.getParentID() != null) {
                    int countCh = expenseItemRepository.countSiblings(expenseItem.getCompanyID(), currentExpenseItemCode.get().getParentID());
                    if (countCh == 1) {
                        expenseItemRepository.setParenNode(currentExpenseItemCode.get().getParentID(), false);
                    }
                }
            }
            return expenseItemRepository.save(expenseItem);
        }
        throw new BadRequestAlertException("", "", "");
    }

    private List<ExpenseItem> updateAllChildren(List<ExpenseItem> descendants, ExpenseItem expenseItem) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<ExpenseItem> children = this.expenseItemRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), expenseItem.getId());
        if (children != null) {
            for (ExpenseItem child : children) {
                child.setIsActive(expenseItem.getIsActive());
                child.setGrade(expenseItem.getGrade() + 1);
                descendants.add(child);
                if (Boolean.TRUE.equals(child.getIsParentNode())) updateAllChildren(descendants, child);
            }
        }
        return descendants;
    }

    void unActiveAllChildrenNote(ExpenseItem expenseItem, boolean isActive) {
        expenseItemRepository.updateIsActive(expenseItem.getId(), isActive);
        if (Boolean.TRUE.equals(expenseItem.getIsParentNode())) {
            List<ExpenseItem> expenseItems = expenseItemRepository.findAllByIsActive(expenseItem.getId());
            for (ExpenseItem Ex : expenseItems) {
                unActiveAllChildrenNote(Ex, isActive);
            }
        }
    }

    /**
     * Get all the expenseItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseItem> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseItems");
        return expenseItemRepository.findAll(pageable);
    }

    @Override
    public Page<ExpenseItem> findAll() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return new PageImpl<ExpenseItem>(expenseItemRepository.findAllByAndCompanyID(currentUserLoginAndOrg.get().getOrg()));
    }

    @Override
    public List<ExpenseItem> getAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return expenseItemRepository.getAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    @Override
    public List<ExpenseItem> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return expenseItemRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    @Override
    public List<ExpenseItem> findAllByAndCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return expenseItemRepository.findAllByAndCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    /**
     * Get one expenseItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExpenseItem> findOne(UUID id) {
        log.debug("Request to get ExpenseItem : {}", id);
        return expenseItemRepository.findById(id);
    }

    /**
     * Delete the expenseItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ExpenseItem : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer check = this.expenseItemRepository.checkLicense(currentUserLoginAndOrg.get().getOrg(), id);
        if (check != null && check > 0) {
            throw new BadRequestAlertException("", "errorcheckLicense", "licenseError");
        }
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<ExpenseItem> expenseItem = expenseItemRepository.findById(id);
            this.expenseItemRepository.deleteById(id);
            if (Boolean.TRUE.equals(expenseItem.get().getIsParentNode())) {
                throw new BadRequestAlertException("", "isIsParentNode", "error");
            }
            if (expenseItem.get().getParentID() != null) {
                int count = expenseItemRepository.countSiblings(currentUserLoginAndOrg.get().getOrg(), expenseItem.get().getParentID());
                //if expenseItem no longer has children
                if (count == 0) {
                    Optional<ExpenseItem> statisticsCodeParent = expenseItemRepository.findById(expenseItem.get().getParentID());
                    statisticsCodeParent.get().setIsParentNode(false);
                    expenseItemRepository.save(statisticsCodeParent.get());
                }
            }
        }
    }

    @Override
    public Page<ExpenseItem> getExpenseItemActive() {
        return new PageImpl<>(expenseItemRepository.findByIsActiveTrue());
    }

    public List<ExpenseItem> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return expenseItemRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<ExpenseItem> getExpenseItemSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            List<UUID> listID = expenseItemRepository.getIDByCompanyIDAndSimilarBranch(companyID, similarBranch);
            return expenseItemRepository.findAllExpenseItemSimilarBranch(similarBranch, listID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<ExpenseItem> getExpenseItemsByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return expenseItemRepository.getExpenseItemsByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public HandlingResultDTO deleteMulti(List<ExpenseItem> expenseItems) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkExpenseId;
        Collections.sort(expenseItems, (o1, o2) ->{
            return o2.getGrade() - o1.getGrade();
        });
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(expenseItems.size());
        DetailDelFailCategoryDTO deleteMultiCategoryDTO;
        List<DetailDelFailCategoryDTO> deleteMultiCategoryDTOS = new ArrayList<>();
        List<ExpenseItem> listDelete = new ArrayList<>(expenseItems);
        List<UUID> listIdDelete = new ArrayList<>();
        for (ExpenseItem expenseItem : expenseItems) {
            checkExpenseId = utilsRepository.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), expenseItem.getId(), "ExpenseItemID");
            if (checkExpenseId) {
                listDelete.remove(expenseItem);
                deleteMultiCategoryDTO = new DetailDelFailCategoryDTO(expenseItem.getExpenseItemName(), expenseItem.getExpenseItemCode(), "PHAT_SINH_CHUNG_TU");
                deleteMultiCategoryDTOS.add(deleteMultiCategoryDTO);
            } else if (expenseItem.getIsParentNode() == null || !expenseItem.getIsParentNode()) {
                    if (expenseItem.getGrade() == 1) {
                        listIdDelete.add(expenseItem.getId());
                    } else if (expenseItem.getGrade() > 1) {
                        expenseItemRepository.deleteById(expenseItem.getId());
                        if (expenseItem.getParentID() != null) {
                            int countChildren = expenseItemRepository.checkChildren(expenseItem.getParentID());
                            if (countChildren == 0) {
                                expenseItemRepository.updateIsParentNode(expenseItem.getParentID(), false);
                            }
                        }
                        listDelete.remove(expenseItem);
                    }
            } else {
                int isParent = expenseItemRepository.checkIsParentNode(expenseItem.getId());
                if (isParent == 0) {
                    expenseItemRepository.deleteById(expenseItem.getId());
                    if (expenseItem.getParentID() !=null) {
                        int countChildren = expenseItemRepository.checkChildren(expenseItem.getParentID());
                        if (countChildren == 0) {
                            expenseItemRepository.setParenNode(expenseItem.getParentID(), false);
                        }
                    }
                    listDelete.remove(expenseItem);
                } else {
                    deleteMultiCategoryDTO = new DetailDelFailCategoryDTO(expenseItem.getExpenseItemName(), expenseItem.getExpenseItemCode(), "NUT_CHA");
                    deleteMultiCategoryDTOS.add(deleteMultiCategoryDTO);
                    listDelete.remove(expenseItem);
                }
            }
        }
        expenseItemRepository.deleteByIdIn(listIdDelete);
        handlingResultDTO.setCountSuccessVouchers(expenseItems.size() - deleteMultiCategoryDTOS.size());
        handlingResultDTO.setCountFailVouchers(deleteMultiCategoryDTOS.size());
        handlingResultDTO.setListFailCategory(deleteMultiCategoryDTOS);
        return handlingResultDTO;
    }

    @Override
    public List<ExpenseItem> findAllActiveByCompanyID(UUID companyID) {
        List<UUID> comID = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
        return expenseItemRepository.findAllExpenseItemByIsActive(comID);
    }

    @Override
    public List<ExpenseItem> getExpenseItemsAndIsDependent(Boolean isDependent, UUID orgID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> listOrgID = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            if (Boolean.TRUE.equals(isDependent)) {
                List<UUID> uuids = organizationUnitRepository.findAllOrgAccType0(orgID);
                listOrgID.addAll(uuids);
                return expenseItemRepository.getExpenseItemsAndIsDependent(listOrgID);
            } else {
                listOrgID.add(orgID);
                return expenseItemRepository.getExpenseItemsAndIsDependent(listOrgID);
            }
        }
        throw new BadRequestAlertException("", "", "");
    }
}
