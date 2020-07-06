package vn.softdreams.ebweb.service.impl;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.BudgetItemService;
import vn.softdreams.ebweb.domain.BudgetItem;
import vn.softdreams.ebweb.repository.BudgetItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.BudgetItemConvertDTO;
import vn.softdreams.ebweb.service.dto.DetailDelFailCategoryDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;

/**
 * Service Implementation for managing BudgetItem.
 */
@Service
@Transactional
public class BudgetItemServiceImpl implements BudgetItemService {

    private final Logger log = LoggerFactory.getLogger(BudgetItemServiceImpl.class);
    private final BudgetItemRepository budgetItemRepository;
    private final UtilsRepository utilsRepository;

    public BudgetItemServiceImpl(BudgetItemRepository budgetItemRepository, UtilsRepository utilsRepository) {
        this.budgetItemRepository = budgetItemRepository;
        this.utilsRepository = utilsRepository;
    }

    /**
     * Save a budgetItem.
     *
     * @param budgetItem the entity to save
     * @return the persisted entity
     */
    @Override
    public BudgetItem save(BudgetItem budgetItem) {
        if (budgetItem.getId() != null) {
            return this.update(budgetItem);
        }
        log.debug("Request to save BudgetItem : {}", budgetItem);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO -> {
            budgetItem.setCompanyID(securityDTO.getOrg());
            Integer count = budgetItemRepository.checkDuplicateBudgetItem(budgetItem.getCompanyID(), budgetItem.getBudgetItemCode());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("", "BudgetItem", "duplicateBudgetItemCode");
            }
        });
        BudgetItem save = budgetItemRepository.save(budgetItem);
        budgetItemRepository.updateIsParentNode(save.getParentID(), true);
        return save;
    }

    public BudgetItem update(BudgetItem budgetItem) {
        BudgetItem save = budgetItem;
        Optional<BudgetItem> budgetItemOld = budgetItemRepository.findById(budgetItem.getId());
        boolean flag = false;
        boolean flag2 = false;
        if (budgetItemOld.isPresent()) {
            if (checkParent(budgetItem, budgetItem.getParentID())) {
                throw new BadRequestAlertException("", "", "");
            }
            if (budgetItemOld.get().isIsActive() != budgetItem.isIsActive()) {
                flag = true;
            }
            if (!budgetItem.getGrade().equals(budgetItemOld.get().getGrade())) {
                flag2 = true;
            }
            if (!budgetItemOld.get().getBudgetItemCode().equals(budgetItem.getBudgetItemCode())) {
                Integer count = budgetItemRepository.checkDuplicateBudgetItem(budgetItem.getCompanyID(), budgetItem.getBudgetItemCode());
                if (count != null && count > 0) {
                    throw new BadRequestAlertException("", "BudgetItem", "duplicateBudgetItemCode");
                }
            }
            int countChildren = budgetItemRepository.getCountChildren(budgetItemOld.get().getParentID());
            if (countChildren == 1) {
                budgetItemRepository.updateIsParentNode(budgetItemOld.get().getParentID(), false);
            }
            save = budgetItemRepository.save(budgetItem);
            budgetItemRepository.updateIsParentNode(save.getParentID(), true);
            if (Boolean.TRUE.equals(budgetItem.isIsParentNode()) && flag) {
                this.unActiveAllChildrenNote(budgetItem, budgetItem.isIsActive());
            }
            if (budgetItem.isIsActive() && flag) {
                activeAllParentNote(budgetItem, true);
            }
            if (flag2) {
                this.updateGrade(budgetItem, budgetItem.getGrade());
            }
        }
        return save;
    }

    void activeAllParentNote(BudgetItem budgetItem, boolean isActive) {
        budgetItemRepository.updateIsActive(budgetItem.getId(), isActive);
        if (budgetItem.getParentID() != null) {
            BudgetItem budget = budgetItemRepository.findById(budgetItem.getParentID()).get();
            activeAllParentNote(budget, isActive);
        }
    }

    void unActiveAllChildrenNote(BudgetItem budgetItem, boolean isActive) {
        budgetItemRepository.updateIsActive(budgetItem.getId(), isActive);
        if (Boolean.TRUE.equals(budgetItem.isIsParentNode())) {
            List<BudgetItem> budgetItems = budgetItemRepository.findAllChildren(budgetItem.getId());
            for (BudgetItem bud : budgetItems) {
                unActiveAllChildrenNote(bud, isActive);
            }
        }
    }

    boolean checkParent(BudgetItem budgetItemParent, UUID org) {
        if (Boolean.TRUE.equals(budgetItemParent.isIsParentNode())) {
            List<BudgetItem> budgetItems = budgetItemRepository.findAllChildren(budgetItemParent.getId());
            for (BudgetItem bud : budgetItems) {
                if (bud.getId().equals(org) || checkParent(bud, org)) {
                    return true;
                }
            }
        }
        return false;
    }

    void updateGrade(BudgetItem budgetItem, Integer grade) {
        budgetItemRepository.updateGrade(budgetItem.getId(), grade);
        if (Boolean.TRUE.equals(budgetItem.isIsParentNode())) {
            List<BudgetItem> budgetItems = budgetItemRepository.findAllChildren(budgetItem.getId());
            for (BudgetItem bud : budgetItems) {
                updateGrade(bud, grade + 1);
            }
        }
    }
    /**
     * Get all the budgetItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BudgetItem> findAll(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return budgetItemRepository.findAll(currentUserLoginAndOrg.get().getOrg(), pageable);
    }
    /**
     * Get one budgetItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetItem> findOne(UUID id) {
        log.debug("Request to get BudgetItem : {}", id);
        return budgetItemRepository.findById(id);
    }

    /**
     * Delete the budgetItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete BudgetItem : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkBudgetId = utilsRepository.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "BudgetItemID");
        if (checkBudgetId) {
            throw new BadRequestAlertException("", "", "");
        }
        int checkIsParentNode = budgetItemRepository.checkIsParentNode(id);
        if (checkIsParentNode == 1) {
            throw new BadRequestAlertException("", "budgetItem", "budgetItemIsParentNode");
        } else {
            BudgetItem budgetItem = budgetItemRepository.findById(id).get();
            budgetItemRepository.deleteById(id);
            int countChildren = budgetItemRepository.getCountChildren(budgetItem.getParentID());
            if (countChildren == 0) {
                budgetItemRepository.updateIsParentNode(budgetItem.getParentID(), false);
            }
        }
    }

    @Override
    public Page<BudgetItemConvertDTO> getAllBudgetItemsActive() {
        return new PageImpl<>(budgetItemRepository.findAllByIsActiveTrue());
    }

    public List<BudgetItem> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return budgetItemRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<BudgetItem> getBudgetItemsByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return budgetItemRepository.getBudgetItemsByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<BudgetItem> findAllBudgetItem() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return budgetItemRepository.findAllBudgetItem(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public HandlingResultDTO deleteMulti(List<BudgetItem> budgetItems) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkBudgetId;
        Collections.sort(budgetItems, (o1, o2) -> {
            return o2.getGrade() - o1.getGrade();
        });
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(budgetItems.size());
        DetailDelFailCategoryDTO deleteMultiCategoryDTO;
        List<DetailDelFailCategoryDTO> deleteMultiCategoryDTOS = new ArrayList<>();
        List<BudgetItem> listDelete = new ArrayList<>(budgetItems);
        List<UUID> listIdDelete = new ArrayList<>();
        for (BudgetItem budgetItem : budgetItems) {
            checkBudgetId = utilsRepository.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), budgetItem.getId(), "BudgetItemID");
            if (checkBudgetId) {
                listDelete.remove(budgetItem);
                deleteMultiCategoryDTO = new DetailDelFailCategoryDTO(budgetItem.getBudgetItemName(), budgetItem.getBudgetItemCode(), "PHAT_SINH_CHUNG_TU");
                deleteMultiCategoryDTOS.add(deleteMultiCategoryDTO);
            } else if (!budgetItem.isIsParentNode() && budgetItem.getGrade() == 1) {
                listIdDelete.add(budgetItem.getId());
                listDelete.remove(budgetItem);
            } else if (!budgetItem.isIsParentNode() && budgetItem.getGrade() > 1) {
                budgetItemRepository.deleteById(budgetItem.getId());
                int countChildren = budgetItemRepository.getCountChildren(budgetItem.getParentID());
                if (countChildren == 0) {
                    budgetItemRepository.updateIsParentNode(budgetItem.getParentID(), false);
                }
                listDelete.remove(budgetItem);
            } else {
                int isParent = budgetItemRepository.checkIsParentNode(budgetItem.getId());
                if (isParent == 0 && budgetItem.getParentID() != null) {
                    budgetItemRepository.deleteById(budgetItem.getId());
                    int countChildren = budgetItemRepository.getCountChildren(budgetItem.getParentID());
                    if (countChildren == 0) {
                        budgetItemRepository.updateIsParentNode(budgetItem.getParentID(), false);
                    }
                    listDelete.remove(budgetItem);
                } else {
                    deleteMultiCategoryDTO = new DetailDelFailCategoryDTO(budgetItem.getBudgetItemName(), budgetItem.getBudgetItemCode(), "NUT_CHA");
                    deleteMultiCategoryDTOS.add(deleteMultiCategoryDTO);
                    listDelete.remove(budgetItem);
                }
            }
        }
        budgetItemRepository.deleteByIdIn(listIdDelete);
        handlingResultDTO.setCountSuccessVouchers(budgetItems.size() - deleteMultiCategoryDTOS.size());
        handlingResultDTO.setCountFailVouchers(deleteMultiCategoryDTOS.size());
        handlingResultDTO.setListFailCategory(deleteMultiCategoryDTOS);
        return handlingResultDTO;
    }
}
