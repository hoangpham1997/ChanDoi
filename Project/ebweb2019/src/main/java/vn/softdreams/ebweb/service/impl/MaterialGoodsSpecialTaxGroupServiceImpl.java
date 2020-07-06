package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsSpecialTaxGroupService;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;
import vn.softdreams.ebweb.repository.MaterialGoodsSpecialTaxGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialGoodsSpecialTaxGroup.
 */
@Service
@Transactional
public class MaterialGoodsSpecialTaxGroupServiceImpl implements MaterialGoodsSpecialTaxGroupService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecialTaxGroupServiceImpl.class);

    private final MaterialGoodsSpecialTaxGroupRepository materialGoodsSpecialTaxGroupRepository;
    private MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup;
    private final UtilsService utilsService;

    public MaterialGoodsSpecialTaxGroupServiceImpl(MaterialGoodsSpecialTaxGroupRepository materialGoodsSpecialTaxGroupRepository, UtilsService utilsService) {
        this.materialGoodsSpecialTaxGroupRepository = materialGoodsSpecialTaxGroupRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a materialGoodsSpecialTaxGroup.
     *
     * @param materialGoodsSpecialTaxGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsSpecialTaxGroup save(MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
        MaterialGoodsSpecialTaxGroup savedMaterialGoodsSpecialTaxGroup;
        log.debug("Request to save Material Goods Special Tax Group : {}", materialGoodsSpecialTaxGroup);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            materialGoodsSpecialTaxGroup.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
            List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroupsChild = new ArrayList<MaterialGoodsSpecialTaxGroup>();
            getListChildAccount(materialGoodsSpecialTaxGroupsChild, materialGoodsSpecialTaxGroup.getId());
            if (materialGoodsSpecialTaxGroupsChild.size() > 0) {
                for (int i = 0; i < materialGoodsSpecialTaxGroupsChild.size(); i++) {
                    materialGoodsSpecialTaxGroupsChild.get(i).setIsActive(materialGoodsSpecialTaxGroup.getIsActive());
                }
                materialGoodsSpecialTaxGroupRepository.saveAll(materialGoodsSpecialTaxGroupsChild);
            }
            // check duplicate material- good- category
            Integer count = this.materialGoodsSpecialTaxGroupRepository.checkDuplicateMaterialGoodsSpecialTaxGroup(materialGoodsSpecialTaxGroup.getId() == null ? UUID.randomUUID() : materialGoodsSpecialTaxGroup.getId(), currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.getGoodsSpecialTaxGroup());
            if (materialGoodsSpecialTaxGroup.getId() == null) {
                if (count != null && count > 0) {
                    throw new BadRequestAlertException("", "MaterialGoodsSpecialTaxGroup", "existData");
                }
            }

            // update statisticsCode's grade and parents
            if (materialGoodsSpecialTaxGroup.getParentID() != null) {
                Optional<MaterialGoodsSpecialTaxGroup> parentMaterialGoodsSpecialTaxGroup = this.materialGoodsSpecialTaxGroupRepository.findById(materialGoodsSpecialTaxGroup.getParentID());
                materialGoodsSpecialTaxGroup.setGrade(parentMaterialGoodsSpecialTaxGroup.get().getGrade() + 1);
                parentMaterialGoodsSpecialTaxGroup.get().setIsParentNode(true);
                materialGoodsSpecialTaxGroup.setIsParentNode(false);
                this.materialGoodsSpecialTaxGroupRepository.save(parentMaterialGoodsSpecialTaxGroup.get());
                if (materialGoodsSpecialTaxGroup.getIsActive()) {
                    List<MaterialGoodsSpecialTaxGroup> parents = new ArrayList<>();
                    if (materialGoodsSpecialTaxGroup.getIsActive())
                        parents = this.updateAllParents(parents, materialGoodsSpecialTaxGroup);
                    this.materialGoodsSpecialTaxGroupRepository.saveAll(parents);
                }
            } else {
                materialGoodsSpecialTaxGroup.setGrade(1);
                if (materialGoodsSpecialTaxGroup.getId() == null) materialGoodsSpecialTaxGroup.setIsParentNode(false);
            }

            // update isActive and grade for materialGoodsSpecialTaxGroupCode's descendants
            if (materialGoodsSpecialTaxGroup.getId() != null) {
                Optional<MaterialGoodsSpecialTaxGroup> currentMaterialGoodsSpecialTaxGroup = this.materialGoodsSpecialTaxGroupRepository.findById(materialGoodsSpecialTaxGroup.getId());
                if (currentMaterialGoodsSpecialTaxGroup.get().getIsActive() != materialGoodsSpecialTaxGroup.getIsActive() || currentMaterialGoodsSpecialTaxGroup.get().getGrade() != materialGoodsSpecialTaxGroup.getGrade()) {
                    List<MaterialGoodsSpecialTaxGroup> descendants = new ArrayList<>();
                    descendants = this.updateAllChildren(descendants, materialGoodsSpecialTaxGroup);
                    this.materialGoodsSpecialTaxGroupRepository.saveAll(descendants);
                }
            }

            // get old parent
            MaterialGoodsSpecialTaxGroup oldParent = null;
            if (materialGoodsSpecialTaxGroup.getId() != null) {
                oldParent = this.materialGoodsSpecialTaxGroupRepository.findParentByChildID(materialGoodsSpecialTaxGroup.getId());
            }

            // save current material goods category
            savedMaterialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupRepository.save(materialGoodsSpecialTaxGroup);

            // update old parent if no child exists
            if (oldParent != null) {
                count = this.materialGoodsSpecialTaxGroupRepository.countChildrenByID(oldParent.getId());
                if (count == null || count == 0) {
                    oldParent.setIsParentNode(false);
                    this.materialGoodsSpecialTaxGroupRepository.save(oldParent);
                }
            }
            return savedMaterialGoodsSpecialTaxGroup;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsSpecialTaxGroup> updateAllParents(List<MaterialGoodsSpecialTaxGroup> parents, MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
        if (materialGoodsSpecialTaxGroup.getParentID() != null) {
            Optional<MaterialGoodsSpecialTaxGroup> parent = this.materialGoodsSpecialTaxGroupRepository.findById(materialGoodsSpecialTaxGroup.getParentID());
            parent.get().setIsActive(true);
            parents.add(parent.get());
            updateAllParents(parents, parent.get());
        }
        return parents;
    }

    public List<MaterialGoodsSpecialTaxGroup> updateAllChildren(List<MaterialGoodsSpecialTaxGroup> descendants, MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialGoodsSpecialTaxGroup> children = this.materialGoodsSpecialTaxGroupRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.getId());
        if (children != null) {
            for (MaterialGoodsSpecialTaxGroup child : children) {
                child.setIsActive(materialGoodsSpecialTaxGroup.getIsActive());
                child.setGrade(materialGoodsSpecialTaxGroup.getGrade() + 1);
                descendants.add(child);
            }
        }
        return descendants;
    }

    // De quy lay ra tat ca tai khoan con cua tai khoan hien tai
    public List<MaterialGoodsSpecialTaxGroup> getListChildAccount(List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialGoodsSpecialTaxGroup> childAccountLists = materialGoodsSpecialTaxGroupRepository.findByParentID(parentAccountID,
            currentUserLoginAndOrg.get().getOrgGetData());
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                materialGoodsSpecialTaxGroups.add(childAccountLists.get(i));
                List<MaterialGoodsSpecialTaxGroup> childAccount = materialGoodsSpecialTaxGroupRepository.findByParentID
                    (childAccountLists.get(i).getParentID(), currentUserLoginAndOrg.get().getOrgGetData());
                if (childAccount.size() > 0) {
                    getListChildAccount(materialGoodsSpecialTaxGroups, childAccount.get(i).getId());
                }
            }
        }
        return materialGoodsSpecialTaxGroups;
    }


//        log.debug("Request to save MaterialGoodsSpecialTaxGroup : {}", materialGoodsSpecialTaxGroup);
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        if (currentUserLoginAndOrg.isPresent()) {
//            materialGoodsSpecialTaxGroup.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
//            if (materialGoodsSpecialTaxGroup.getParentID() != null) {
//                Optional<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroupParent = materialGoodsSpecialTaxGroupRepository.findById(materialGoodsSpecialTaxGroup.getParentID());
//                materialGoodsSpecialTaxGroup.setGrade(materialGoodsSpecialTaxGroupParent.get().getGrade() + 1);
//                materialGoodsSpecialTaxGroupParent.get().setIsParentNode(true);
//            }
//            Integer count = this.materialGoodsSpecialTaxGroupRepository.checkDuplicateMaterialGoodsSpecialTaxGroup(materialGoodsSpecialTaxGroup.getId() == null ? UUID.randomUUID() : materialGoodsSpecialTaxGroup.getId(), currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.getMaterialGoodsSpecialTaxGroupCode());
//            if (materialGoodsSpecialTaxGroup.getId() == null) {
//                if (count != null && count > 0) {
//                    throw new BadRequestAlertException("", "MaterialGoodsSpecialTaxGroup", "existData");
//                }
//            }
//        }
//        return materialGoodsSpecialTaxGroupRepository.save(materialGoodsSpecialTaxGroup);
//    }

    /**
     * Get all the materialGoodsSpecialTaxGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsSpecialTaxGroup> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsSpecialTaxGroups");
        return materialGoodsSpecialTaxGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsSpecialTaxGroup> pageableAllMaterialGoodsSpecialTaxGroup(Pageable pageable) {
        log.debug("Request to get all Banks");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsSpecialTaxGroupRepository.pageableAllMaterialGoodsSpecialTaxGroup(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one materialGoodsSpecialTaxGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsSpecialTaxGroup> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsSpecialTaxGroup : {}", id);
        return materialGoodsSpecialTaxGroupRepository.findById(id);
    }

    /**
     * Delete the materialGoodsSpecialTaxGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupRepository.findById(id);
            materialGoodsSpecialTaxGroupRepository.deleteById(id);
            int countChildByParentID = materialGoodsSpecialTaxGroupRepository.countSiblings(currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.get().getParentID());
            int countChildByID = materialGoodsSpecialTaxGroupRepository.countSiblings(currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.get().getId());
            if (countChildByID == 0) {
                if (materialGoodsSpecialTaxGroup.get().getParentID() != null) {
                    if (countChildByParentID == 0) {
                        Optional<MaterialGoodsSpecialTaxGroup> childAccountList = materialGoodsSpecialTaxGroupRepository.findById(materialGoodsSpecialTaxGroup.get().getParentID());
                        childAccountList.get().setIsParentNode(false);
                        materialGoodsSpecialTaxGroupRepository.save(childAccountList.get());
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
        }
        throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con", "MaterialGoodsSpecialTaxGroup", "errorDeleteParent");
    }

    @Override
    public List<MaterialGoodsSpecialTaxGroup> findAllMaterialGoodsSpecialTaxGroupByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsSpecialTaxGroupRepository.findAllMaterialGoodsSpecialTaxGroupkByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoodsSpecialTaxGroup> getAllMaterialGoodsSpecialTaxGroupByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsSpecialTaxGroupRepository.getAllMaterialGoodsSpecialTaxGroupByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsSpecialTaxGroup> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsSpecialTaxGroupRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsSpecialTaxGroup> findAllExceptID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<MaterialGoodsSpecialTaxGroup> accountLists = materialGoodsSpecialTaxGroupRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
            List<MaterialGoodsSpecialTaxGroup> accountListsParent = new ArrayList<MaterialGoodsSpecialTaxGroup>();
            recursiveMaterialGoodsCategoryParent(accountListsParent, id, currentUserLoginAndOrg.get().getOrgGetData());
            for (int i = 0; i < accountListsParent.size(); i++) {
                accountLists.remove(accountListsParent.get(i));
            }
            MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupRepository.findById(id).get();
            accountLists.remove(materialGoodsSpecialTaxGroup);
            return accountLists;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsSpecialTaxGroup> findAllAccountLists() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsSpecialTaxGroupRepository.findAllAccountLists(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public MaterialGoodsSpecialTaxGroup findOneExceptID (UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroupRepository.getOneMaterialGoodsSpecialTaxGroupByCompanyID(id);
            return materialGoodsSpecialTaxGroup;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsSpecialTaxGroup> recursiveMaterialGoodsCategoryParent(List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups, UUID parentID, UUID companyID) {
        List<MaterialGoodsSpecialTaxGroup> listAccTemp = new ArrayList<>();
        List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups1 = materialGoodsSpecialTaxGroupRepository.findByParentID(parentID, companyID);
        if (materialGoodsSpecialTaxGroups1.size() > 0) {
            materialGoodsSpecialTaxGroups.addAll(materialGoodsSpecialTaxGroups1);
            listAccTemp.addAll(materialGoodsSpecialTaxGroups1);
            for (int i = 0; i < listAccTemp.size(); i++) {
                List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroupList = materialGoodsSpecialTaxGroupRepository.findByParentID(listAccTemp.get(i).getId(), companyID);
                if (materialGoodsSpecialTaxGroupList.size() > 0) {
                    materialGoodsSpecialTaxGroups.addAll(recursiveMaterialGoodsCategoryParent(listAccTemp, materialGoodsSpecialTaxGroups.get(i).getId(), companyID));
                }
            }
        }
        return materialGoodsSpecialTaxGroups;
    }

    @Override
    public List<MaterialGoodsSpecialTaxGroup> findMaterialGoodsSpecialTaxGroupsOne(UUID id) {
        List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups = findAllActive();
        Optional<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroup = this.materialGoodsSpecialTaxGroupRepository.findById(id);
        materialGoodsSpecialTaxGroups = filterStatisticsCode(materialGoodsSpecialTaxGroups, materialGoodsSpecialTaxGroup.get());
        return materialGoodsSpecialTaxGroups;
    }

    public List<MaterialGoodsSpecialTaxGroup> filterStatisticsCode(List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups, MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        materialGoodsSpecialTaxGroups.remove(materialGoodsSpecialTaxGroup);
        List<MaterialGoodsSpecialTaxGroup> childStatisticsCodes = this.materialGoodsSpecialTaxGroupRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrgGetData(), materialGoodsSpecialTaxGroup.getId());
        if (childStatisticsCodes != null) {
            for (MaterialGoodsSpecialTaxGroup s : childStatisticsCodes) {
                this.filterStatisticsCode(materialGoodsSpecialTaxGroups, s);
            }
        }
        return materialGoodsSpecialTaxGroups;
    }
}
