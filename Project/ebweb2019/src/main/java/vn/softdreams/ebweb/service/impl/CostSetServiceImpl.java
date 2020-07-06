package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import org.yaml.snakeyaml.scanner.Constant;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CostSetService;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.repository.CostSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CostSetSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CostSet.
 */
@Service
@Transactional
public class CostSetServiceImpl implements CostSetService {

    private final Logger log = LoggerFactory.getLogger(CostSetServiceImpl.class);

    private final CostSetRepository costSetRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsRepository utilsRepository;
    private final SystemOptionRepository systemOptionRepository;

    public CostSetServiceImpl(
        CostSetRepository costSetRepository,
        UserService userService,
        UtilsService utilsService,
        OrganizationUnitRepository organizationUnitRepository,
        UtilsRepository utilsRepository, SystemOptionRepository systemOptionRepository) {
        this.costSetRepository = costSetRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsRepository = utilsRepository;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a costSet.
     *
     * @param costSet the entity to save
     * @return the persisted entity
     */
    @Override
    public CostSet save(CostSet costSet) {
        log.debug("Request to save CostSet : {}", costSet);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            costSet.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        if (costSet.getId() == null) {
            costSet.setIsActive(true);
        }
        return costSetRepository.save(costSet);
    }

    /**
     * Get all the costSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CostSet> findAll(Pageable pageable) {
        log.debug("Request to get all CostSets");
        return costSetRepository.findAll(pageable);
    }

    @Override
    public Page<CostSet> findAll() {
        return new PageImpl<CostSet>(costSetRepository.findAll());
    }


    /**
     * Get one costSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CostSet> findOne(UUID id) {
        log.debug("Request to get CostSet : {}", id);
        return costSetRepository.findById(id);
    }

    /**
     * Delete the costSet by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CostSet : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedCSetId = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "CostSetID");
        Boolean checkUsedMId = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "MaterialGoodsID");
        Integer checkCostSetMQId = costSetRepository.checkCostSetMQId(id);
        Boolean checkCostSetRelatedVoucher = costSetRepository.checkCostSetRelatedVoucher(id);
        if (checkUsedCSetId || checkUsedMId || checkCostSetRelatedVoucher || checkCostSetMQId > 0) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            costSetRepository.deleteById(id);
        }
    }

    @Override
    public Page<CostSet> getAllCostSetsActive() {
        return new PageImpl<>(costSetRepository.findAllByIsActiveTrue());
    }

    @Override
    public Page<CostSet> findAll(Pageable pageable, UUID branchID, String costSetCode, String costSetName, Integer costSetType,
                                 String description, UUID parentID, Boolean isParentNode, String orderFixCode, Integer grade, Boolean isActive) {
        return costSetRepository.findAll(pageable, branchID, costSetCode, costSetName, costSetType, description,
            parentID, isParentNode, orderFixCode, grade, isActive);
    }

    public List<CostSet> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetRepository.findAllByIsActive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP));
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<CostSet> getCostSetsByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetRepository.getCostSetsByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<CostSet> findAllCostSetByCompanyID(Pageable pageable) {
        /*log.debug("Request to get all CostSet");
        UserDTO userDTO = userService.getAccount();
        Boolean isGetAllCompany = Utils.TCKHAC_SDDMDoiTuong(userDTO) == 0;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return costSetRepository.pageableAllCostSet(pageable, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP));*/
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer test = Utils.TCKHAC_SDDMDoiTuong(userDTO);
        String isGetAllCompany = costSetRepository.checked(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP);
        log.debug("Request to get all CostSets");
        if (currentUserLoginAndOrg.isPresent()) {
            if (isGetAllCompany.equals("0")) {
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
                return costSetRepository.getAllByListCompany(pageable, listCompanyID);
            } else {
                return costSetRepository.pageableAllCostSets(pageable, currentUserLoginAndOrg.get().getOrg());
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    /*@Override
    public Page<CostSet> findAllCostSetByCompanyID(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Boolean storage = Constants.SYSTEM_OPTION_DATA.STORAGE.equals(Utils.TCKHAC_SDDMDoiTuong(userDTO));
        if (storage) {
            return costSetRepository.findAllCostSetByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
        } else {
            return costSetRepository.findAllCostSetStorageByCompanyID(pageable, userDTO.getOrganizationUnit().getId(), userDTO.getOrganizationUnit().getParentID());
        }
    }*/

    @Override
    public CostSetSaveDTO saveDTO(CostSet costSet) {
        log.debug("Request to saveDTO CostSet : {}", costSet);
        CostSet cSet = new CostSet();
        CostSetSaveDTO costSetSaveDTO = new CostSetSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (costSetRepository.findByCompanyIDAndCostSetCode(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMDTTHCP), costSet.getCostSetCode(),
                costSet.getId() == null ? new UUID(0L, 0L) : costSet.getId()) != null) {
                costSetSaveDTO.setStatus(1);
                costSetSaveDTO.setCostSet(costSet);
                return costSetSaveDTO;
            }
            costSet.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (costSet.getId() == null) {
                costSet.setIsActive(true);
            }
            cSet = costSetRepository.save(costSet);
            costSetSaveDTO.setCostSet(cSet);
            costSetSaveDTO.setStatus(0);
        }
        return costSetSaveDTO;
    }

    @Override
    public HandlingResultDTO multiDelete(List<UUID> costSets) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<UUID> failDeletedList = costSetRepository.getUsedCostSetID(costSets).stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
        handlingResultDTO.setDeletedFail(failDeletedList);
        List<UUID> deletedList = costSets.stream().filter(x -> !failDeletedList.contains(x)).collect(Collectors.toList());
        handlingResultDTO.setDeletedSuccess(deletedList);
        if (!deletedList.isEmpty()) {
            costSetRepository.deleteAllById(deletedList);
        }
        return handlingResultDTO;
    }

    @Override
    public List<CostSet> findCostSetList(UUID companyID, Boolean dependent) {
        List<UUID> comIds = new ArrayList<>();
        if (dependent != null && dependent) {
            comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
        } else {
            comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDTTHCP);
        }
        return costSetRepository.findAllByIsActive(comIds);
    }

    @Override
    public List<CostSet> findAllByOrgID(UUID orgID, Boolean isDependent) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Boolean isDungChungDTTHCP;
        isDungChungDTTHCP = (userDTO.getSystemOption().stream().filter(a -> a.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDTTHCP)).findFirst()).get().getData().equals("0");
        List<UUID> listOrgID = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            if (Boolean.TRUE.equals(isDungChungDTTHCP)) {
                return costSetRepository.findAllByOrgID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(orgID, Constants.SystemOption.TCKHAC_SDDMDTTHCP));
            } else {
                if (Boolean.TRUE.equals(isDependent)) {
                    List<UUID> uuids = organizationUnitRepository.findAllOrgAccType0(orgID);
                    listOrgID.addAll(uuids);
                    return costSetRepository.findAllByOrgID(listOrgID);
                } else {
                    listOrgID.add(orgID);
                    return costSetRepository.findAllByOrgID(listOrgID);
                }

            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<CostSet> getCostSetsByTypeRaTio(Pageable pageable, Integer type) {
        log.debug("Request to get all CostSet");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return costSetRepository.getCostSetsByTypeRaTio(pageable, type, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP));

    }

    @Override
    public List<CostSetMaterialGoodsDTO> getCostSetByListID(List<UUID> uuids) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetRepository.getCostSetByListID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP), systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH), uuids);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<CostSetDTO> findRevenueByCostSetID(CostSetDTO costSetDTO) {
        return costSetRepository.getCostSetAndRevenueAmount(costSetDTO.getCostSetIDs(), costSetDTO.getFromDate(), costSetDTO.getToDate());
    }

    @Override
    public List<CostSet> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetRepository.getCostSetsByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP));
        }
        throw new BadRequestAlertException("", "", "");
    }
}
