package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.repository.AccountingObjectRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AccountingObjectGroupService;
import vn.softdreams.ebweb.domain.AccountingObjectGroup;
import vn.softdreams.ebweb.repository.AccountingObjectGroupRepository;
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

/**
 * Service Implementation for managing AccountingObjectGroup.
 */
@Service
@Transactional
public class AccountingObjectGroupServiceImpl implements AccountingObjectGroupService {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectGroupServiceImpl.class);

    private final AccountingObjectGroupRepository accountingObjectGroupRepository;
    private final AccountingObjectRepository accountingObjectRepository;

    private final SystemOptionRepository systemOptionRepository;
    private final UserService userService;

    public AccountingObjectGroupServiceImpl(AccountingObjectGroupRepository accountingObjectGroupRepository,
                                            AccountingObjectRepository accountingObjectRepository, SystemOptionRepository systemOptionRepository, UserService userService) {
        this.accountingObjectGroupRepository = accountingObjectGroupRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.userService = userService;
    }

    /**
     * Save a accountingObjectGroup.
     *
     * @param accountingObjectGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountingObjectGroup save(AccountingObjectGroup accountingObjectGroup) {
        AccountingObjectGroup savedAccountingObjectGroup;
        log.debug("Request to save AccountingObjectGroup : {}", accountingObjectGroup);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            accountingObjectGroup.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            List<AccountingObjectGroup> accountingObjectGroupsChild = new ArrayList<AccountingObjectGroup>();
            getListChildAccount(accountingObjectGroupsChild, accountingObjectGroup.getId());
            if (accountingObjectGroupsChild.size() > 0) {
                for (int i = 0; i < accountingObjectGroupsChild.size(); i++) {
                    accountingObjectGroupsChild.get(i).setIsActive(accountingObjectGroup.getIsActive());
                }
                accountingObjectGroupRepository.saveAll(accountingObjectGroupsChild);
            }
            // check duplicate accounting object group
            Integer count = this.accountingObjectGroupRepository.checkDuplicateAccountingObjectGroup(accountingObjectGroup.getId() == null ? UUID.randomUUID() : accountingObjectGroup.getId(), currentUserLoginAndOrg.get().getOrg(), accountingObjectGroup.getAccountingObjectGroup());
            if (accountingObjectGroup.getId() == null) {
                if (count != null && count > 0) {
                    throw new BadRequestAlertException("", "AccountingObjectGroup", "existData");
                }
            }

            // update statisticsCode's grade and parents
            if (accountingObjectGroup.getParentId() != null) {
                Optional<AccountingObjectGroup> parentAccountingObjectGroup = this.accountingObjectGroupRepository.findById(accountingObjectGroup.getParentId());
                accountingObjectGroup.setGrade(parentAccountingObjectGroup.get().getGrade() + 1);
                parentAccountingObjectGroup.get().setIsParentNode(true);
                this.accountingObjectGroupRepository.save(parentAccountingObjectGroup.get());
                if (accountingObjectGroup.getIsActive()) {
                    List<AccountingObjectGroup> parents = new ArrayList<>();
                    if (accountingObjectGroup.getIsActive())
                        parents = this.updateAllParents(parents, accountingObjectGroup);
                    this.accountingObjectGroupRepository.saveAll(parents);
                }
            } else {
                accountingObjectGroup.setGrade(1);
                if (accountingObjectGroup.getId() == null) accountingObjectGroup.setIsParentNode(false);
            }

            // update isActive and grade for statisticsCode's descendants
            if (accountingObjectGroup.getId() != null) {
                Optional<AccountingObjectGroup> currentAccountingObjectGroup = this.accountingObjectGroupRepository.findById(accountingObjectGroup.getId());
                if (currentAccountingObjectGroup.get().isIsActive() != accountingObjectGroup.isIsActive() || currentAccountingObjectGroup.get().getGrade() != accountingObjectGroup.getGrade()) {
                    List<AccountingObjectGroup> descendants = new ArrayList<>();
                    descendants = this.updateAllChildren(descendants, accountingObjectGroup);
                    this.accountingObjectGroupRepository.saveAll(descendants);
                }
            }

            // get old parent
            AccountingObjectGroup oldParent = null;
            if (accountingObjectGroup.getId() != null) {
                oldParent = this.accountingObjectGroupRepository.findParentByChildID(accountingObjectGroup.getId());
            }

            // save current material goods category
            savedAccountingObjectGroup = accountingObjectGroupRepository.save(accountingObjectGroup);

            // update old parent if no child exists
            if (oldParent != null) {
                count = this.accountingObjectGroupRepository.countChildrenByID(oldParent.getId());
                if (count == null || count == 0) {
                    oldParent.setIsParentNode(false);
                    this.accountingObjectGroupRepository.save(oldParent);
                }
            }
            return savedAccountingObjectGroup;
        }
        throw new BadRequestAlertException("", "", "");
    }

    // De quy lay ra tat ca tai khoan con cua tai khoan hien tai
    public List<AccountingObjectGroup> getListChildAccount(List<AccountingObjectGroup> accountingObjectGroups, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountingObjectGroup> childAccountLists = accountingObjectGroupRepository.findByParentID(parentAccountID,
            currentUserLoginAndOrg.get().getOrg());
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                accountingObjectGroups.add(childAccountLists.get(i));
                List<AccountingObjectGroup> childAccount = accountingObjectGroupRepository.findByParentID
                    (childAccountLists.get(i).getParentId(), currentUserLoginAndOrg.get().getOrg());
                if (childAccount.size() > 0) {
                    getListChildAccount(accountingObjectGroups, childAccount.get(i).getId());
                }
            }
        }
        return accountingObjectGroups;
    }

    public List<AccountingObjectGroup> updateAllParents(List<AccountingObjectGroup> parents, AccountingObjectGroup accountingObjectGroup) {
        if (accountingObjectGroup.getParentId() != null) {
            Optional<AccountingObjectGroup> parent = this.accountingObjectGroupRepository.findById(accountingObjectGroup.getParentId());
            parent.get().setIsActive(true);
            parents.add(parent.get());
            updateAllParents(parents, parent.get());
        }
        return parents;
    }

    public List<AccountingObjectGroup> updateAllChildren(List<AccountingObjectGroup> descendants, AccountingObjectGroup accountingObjectGroup) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountingObjectGroup> children = this.accountingObjectGroupRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), accountingObjectGroup.getId());
        if (children != null) {
            for (AccountingObjectGroup child : children) {
                child.setIsActive(accountingObjectGroup.isIsActive());
                child.setGrade(accountingObjectGroup.getGrade() + 1);
                descendants.add(child);
                if (child.isIsParentNode()) updateAllChildren(descendants, child);
            }
        }
        return descendants;
    }


    /**
     * Get all the accountingObjectGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObjectGroup> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingObjectGroups");
        return accountingObjectGroupRepository.findAll(pageable);
    }


    /**
     * Get one accountingObjectGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingObjectGroup> findOne(UUID id) {
        log.debug("Request to get AccountingObjectGroup : {}", id);
        return accountingObjectGroupRepository.findById(id);
    }

    /**
     * Delete the accountingObjectGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountingObjectGroup : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<AccountingObjectGroup> accountingObjectGroup = accountingObjectGroupRepository.findById(id);
            if (Boolean.TRUE.equals(accountingObjectGroup.get().isIsParentNode())) {
                throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con ", "", "");
            } else {
                AccountingObject accountingObject = accountingObjectRepository.findByCompanyIDAndAccountingObjectID(currentUserLoginAndOrg.get().getOrg(), id);
                if (accountingObject != null) {
                    throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
                } else {
                    accountingObjectGroupRepository.deleteById(id);
                    if (accountingObjectGroup.get().getParentId() != null) {
                        int count = accountingObjectGroupRepository.countSiblings(currentUserLoginAndOrg.get().getOrg(), accountingObjectGroup.get().getParentId());
                        //if statisticsCode no longer has children
                        if (count == 0) {
                            Optional<AccountingObjectGroup> statisticsCodeParent = accountingObjectGroupRepository.findById(accountingObjectGroup.get().getParentId());
                            statisticsCodeParent.get().setIsParentNode(false);
                            accountingObjectGroupRepository.save(statisticsCodeParent.get());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObjectGroup> findAll(Pageable pageable, String accountingobjectgroupcode) {
        return accountingObjectGroupRepository.findAll(pageable, accountingobjectgroupcode);
    }

    @Override
    public List<AccountingObjectGroup> findAllAccountingObjectGroup(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectGroupRepository.findAllAccountingObjectGroupSimilar(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountingObjectGroup> getCbxAccountingObjectGroup(UUID id) {
        List<AccountingObjectGroup> cbxAccountingObjectGroup = findAllActive();
        Optional<AccountingObjectGroup> accountingObjectGroup = this.accountingObjectGroupRepository.findById(id);
        cbxAccountingObjectGroup = filterAccountingObjectGroup(cbxAccountingObjectGroup,accountingObjectGroup.get());
        return cbxAccountingObjectGroup;
    }

    @Override
    public List<AccountingObjectGroup> loadAllAccountingObjectGroup() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectGroupRepository.loadAllAccountingObjectGroup(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountingObjectGroup> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectGroupRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }
    public List<AccountingObjectGroup> filterAccountingObjectGroup(List<AccountingObjectGroup> cbxAccountingObjectGroup, AccountingObjectGroup accountingObjectGroup) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        cbxAccountingObjectGroup.remove(accountingObjectGroup);
        List<AccountingObjectGroup> childStatisticsCodes = this.accountingObjectGroupRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), accountingObjectGroup.getId());
        if (childStatisticsCodes != null) {
            for (AccountingObjectGroup s : childStatisticsCodes) {
                this.filterAccountingObjectGroup(cbxAccountingObjectGroup, s);
            }
        }
        return cbxAccountingObjectGroup;
    }
    public List<AccountingObjectGroup> getAllAccountingObjectGroupSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectGroupRepository.getAllAccountingObjectGroupSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared);
        }
        throw new BadRequestAlertException("", "", "");

    }
}
