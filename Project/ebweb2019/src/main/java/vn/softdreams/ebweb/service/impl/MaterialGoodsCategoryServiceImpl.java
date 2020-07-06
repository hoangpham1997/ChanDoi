package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.repository.MaterialGoodsRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsCategoryService;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.repository.MaterialGoodsCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MaterialGoodsCategory.
 */
@Service
@Transactional
public class MaterialGoodsCategoryServiceImpl implements MaterialGoodsCategoryService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsCategoryServiceImpl.class);

    private final MaterialGoodsCategoryRepository materialGoodsCategoryRepository;
    private final MaterialGoodsRepository materialGoodsRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UserService userService;
    private final SystemOptionRepository systemOptionRepository;

    public MaterialGoodsCategoryServiceImpl(MaterialGoodsCategoryRepository materialGoodsCategoryRepository,
                                            MaterialGoodsRepository materialGoodsRepository, OrganizationUnitRepository organizationUnitRepository, UserService userService, SystemOptionRepository systemOptionRepository) {
        this.materialGoodsCategoryRepository = materialGoodsCategoryRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.systemOptionRepository = systemOptionRepository;
    }

    // De quy lay ra tat ca tai khoan con cua tai khoan hien tai
    public List<MaterialGoodsCategory> getListChildAccount(List<MaterialGoodsCategory> materialGoodsCategories, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialGoodsCategory> childAccountLists = materialGoodsCategoryRepository.findByParentID(parentAccountID,
            currentUserLoginAndOrg.get().getOrg());
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                materialGoodsCategories.add(childAccountLists.get(i));
                List<MaterialGoodsCategory> childAccount = materialGoodsCategoryRepository.findByParentID
                    (childAccountLists.get(i).getParentID(), currentUserLoginAndOrg.get().getOrg());
                if (childAccount.size() > 0) {
                    getListChildAccount(materialGoodsCategories, childAccount.get(i).getId());
                }
            }
        }
        return materialGoodsCategories;
    }


    /**
     * Save a materialGoodsCategory.
     *
     * @param materialGoodsCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsCategory save(MaterialGoodsCategory materialGoodsCategory) {
        MaterialGoodsCategory savedMaterialGoodsCategory;
        log.debug("Request to save MaterialGoodsCategory : {}", materialGoodsCategory);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            materialGoodsCategory.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            List<MaterialGoodsCategory> materialGoodsCategoriesChild = new ArrayList<MaterialGoodsCategory>();
            getListChildAccount(materialGoodsCategoriesChild, materialGoodsCategory.getId());
            if (materialGoodsCategoriesChild.size() > 0) {
                for (int i = 0; i < materialGoodsCategoriesChild.size(); i++) {
                    materialGoodsCategoriesChild.get(i).setIsActive(materialGoodsCategory.getIsActive());
                }
                materialGoodsCategoryRepository.saveAll(materialGoodsCategoriesChild);
            }
            // check duplicate material- good- category
            Integer count = this.materialGoodsCategoryRepository.checkDuplicateMaterialGoodsCategory(materialGoodsCategory.getId() == null ? UUID.randomUUID() : materialGoodsCategory.getId(), currentUserLoginAndOrg.get().getOrg(), materialGoodsCategory.getMaterialGoodsCategory());
            if (materialGoodsCategory.getId() == null) {
                if (count != null && count > 0) {
                    throw new BadRequestAlertException("", "MaterialGoodsCategory", "existData");
                }
            }

            // update statisticsCode's grade and parents
            if (materialGoodsCategory.getParentID() != null) {
                Optional<MaterialGoodsCategory> parentMaterialGoodsCategory = this.materialGoodsCategoryRepository.findById(materialGoodsCategory.getParentID());
                materialGoodsCategory.setGrade(parentMaterialGoodsCategory.get().getGrade() + 1);
                parentMaterialGoodsCategory.get().setIsParentNode(true);
                this.materialGoodsCategoryRepository.save(parentMaterialGoodsCategory.get());
                if (materialGoodsCategory.getIsActive()) {
                    List<MaterialGoodsCategory> parents = new ArrayList<>();
                    if (materialGoodsCategory.getIsActive())
                        parents = this.updateAllParents(parents, materialGoodsCategory);
                    this.materialGoodsCategoryRepository.saveAll(parents);
                }
            } else {
                materialGoodsCategory.setGrade(1);
                if (materialGoodsCategory.getId() == null) materialGoodsCategory.setIsParentNode(null);
            }

            // update isActive and grade for statisticsCode's descendants
            if (materialGoodsCategory.getId() != null) {
                Optional<MaterialGoodsCategory> currentMaterialGoodsCategory = this.materialGoodsCategoryRepository.findById(materialGoodsCategory.getId());
                if (currentMaterialGoodsCategory.get().getIsActive() != materialGoodsCategory.getIsActive() || currentMaterialGoodsCategory.get().getGrade() != materialGoodsCategory.getGrade()) {
                    List<MaterialGoodsCategory> descendants = new ArrayList<>();
                    descendants = this.updateAllChildren(descendants, materialGoodsCategory);
                    this.materialGoodsCategoryRepository.saveAll(descendants);
                }
            }

            // get old parent
            MaterialGoodsCategory oldParent = null;
            if (materialGoodsCategory.getId() != null) {
                oldParent = this.materialGoodsCategoryRepository.findParentByChildID(materialGoodsCategory.getId());
            }

            // save current material goods category
            savedMaterialGoodsCategory = materialGoodsCategoryRepository.save(materialGoodsCategory);

            // update old parent if no child exists
            if (oldParent != null) {
                count = this.materialGoodsCategoryRepository.countChildrenByID(oldParent.getId());
                if (count == null || count == 0) {
                    oldParent.setIsParentNode(false);
                    this.materialGoodsCategoryRepository.save(oldParent);
                }
            }
            return savedMaterialGoodsCategory;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsCategory> updateAllParents(List<MaterialGoodsCategory> parents, MaterialGoodsCategory materialGoodsCategory) {
        if (materialGoodsCategory.getParentID() != null) {
            Optional<MaterialGoodsCategory> parent = this.materialGoodsCategoryRepository.findById(materialGoodsCategory.getParentID());
            parent.get().setIsActive(true);
            parents.add(parent.get());
            updateAllParents(parents, parent.get());
        }
        return parents;
    }

    public List<MaterialGoodsCategory> updateAllChildren(List<MaterialGoodsCategory> descendants, MaterialGoodsCategory materialGoodsCategory) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialGoodsCategory> children = this.materialGoodsCategoryRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), materialGoodsCategory.getId());
        if (children != null) {
            for (MaterialGoodsCategory child : children) {
                child.setIsActive(materialGoodsCategory.getIsActive());
                child.setGrade(materialGoodsCategory.getGrade() + 1);
                descendants.add(child);
            }
        }
        return descendants;
    }

    /**
     * Get all the materialGoodsCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsCategory> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsCategories");
        return materialGoodsCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsCategory> pageableAllMaterialGoodsCategories(Pageable pageable) {
        log.debug("Request to get all AccountingObjects");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsCategoryRepository.pageableAllMaterialGoodsCategories(pageable, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one materialGoodsCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsCategory> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsCategory : {}", id);
        return materialGoodsCategoryRepository.findById(id);
    }

    /**
     * Delete the materialGoodsCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsCategory : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<MaterialGoodsCategory> materialGoodsCategory = materialGoodsCategoryRepository.findById(id);
            if(Boolean.TRUE.equals(materialGoodsCategory.get().isIsParentNode())){
                throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con ", "", "");
            }else{
                MaterialGoods materialGoods = materialGoodsRepository.findByCompanyIDAndMaterialGoodsCategoryID(currentUserLoginAndOrg.get().getOrg(), id);
                if (materialGoods != null) {
                    throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
                } else {
                    materialGoodsCategoryRepository.deleteById(id);
                    if (materialGoodsCategory.get().getParentID() != null) {
                        int count = materialGoodsCategoryRepository.countSiblings(currentUserLoginAndOrg.get().getOrg(), materialGoodsCategory.get().getParentID());
                        //if statisticsCode no longer has children
                        if (count == 0) {
                            Optional<MaterialGoodsCategory> statisticsCodeParent = materialGoodsCategoryRepository.findById(materialGoodsCategory.get().getParentID());
                            statisticsCodeParent.get().setIsParentNode(false);
                            materialGoodsCategoryRepository.save(statisticsCodeParent.get());
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> GetListOrderFixCodeParentID(UUID id){
        log.debug("Request to get GetListOrderFixCodeParentID : {}", id);
        return materialGoodsCategoryRepository.GetListOrderFixCodeParentID(id);
    }


    @Override
    public List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyID(Boolean similarBranch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<UUID> comIds = new ArrayList<>();
            if (similarBranch != null && similarBranch) {
                comIds = organizationUnitRepository.getCompanyAndBranch(currentUserLoginAndOrg.get().getOrg()).stream().map(x -> x.getId()).collect(Collectors.toList());
            } else {
                comIds.add(currentUserLoginAndOrg.get().getOrg());
            }
            return materialGoodsCategoryRepository.getAllMaterialGoodsCategoryByCompanyID(comIds);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsCategory> findAllExceptID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<UUID> comIds = new ArrayList<>();
            comIds.add(currentUserLoginAndOrg.get().getOrg());
            List<MaterialGoodsCategory> materialGoodsCategories = materialGoodsCategoryRepository.getAllMaterialGoodsCategoryByCompanyID(comIds);
            List<MaterialGoodsCategory> materialGoodsCategoriesParent = new ArrayList<MaterialGoodsCategory>();
            recursiveMaterialGoodsCategoryParent(materialGoodsCategoriesParent, id, currentUserLoginAndOrg.get().getOrg());
            for (int i = 0; i < materialGoodsCategoriesParent.size(); i++) {
                materialGoodsCategories.remove(materialGoodsCategoriesParent.get(i));
            }
            MaterialGoodsCategory materialGoodsCategory = materialGoodsCategoryRepository.findById(id).get();
            materialGoodsCategories.remove(materialGoodsCategory);
            return materialGoodsCategories;
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMVTHH)).findAny().get().getData().equals("1");
            return materialGoodsCategoryRepository.getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMVTHH), similarBranch, checkShared);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoodsCategory> getMaterialGoodsCategoryForReport(UUID companyID, Boolean similarBranch) {
        if (companyID != null) {
            List<UUID> comIds = new ArrayList<>();
            if (similarBranch != null && similarBranch) {
                comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
            } else {
                comIds.add(companyID);
            }
            return materialGoodsCategoryRepository.getAllMaterialGoodsCategoryByCompanyID(comIds);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<MaterialGoodsCategory> recursiveMaterialGoodsCategoryParent(List<MaterialGoodsCategory> materialGoodsCategories, UUID parentID, UUID companyID) {
        List<MaterialGoodsCategory> listAccTemp = new ArrayList<>();
        List<MaterialGoodsCategory> materialGoodsCategories1 = materialGoodsCategoryRepository.findByParentID(parentID, companyID);
        if (materialGoodsCategories1.size() > 0) {
            materialGoodsCategories.addAll(materialGoodsCategories1);
            listAccTemp.addAll(materialGoodsCategories1);
            for (int i = 0; i < listAccTemp.size(); i++) {
                List<MaterialGoodsCategory> materialGoodsCategoriesList = materialGoodsCategoryRepository.findByParentID(listAccTemp.get(i).getId(), companyID);
                if (materialGoodsCategoriesList.size() > 0) {
                    materialGoodsCategories.addAll(recursiveMaterialGoodsCategoryParent(listAccTemp, materialGoodsCategories.get(i).getId(), companyID));
                }
            }
        }
        return materialGoodsCategories;
    }
}
