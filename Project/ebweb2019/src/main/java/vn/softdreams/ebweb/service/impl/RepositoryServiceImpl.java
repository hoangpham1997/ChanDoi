package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.MaterialGoodsRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.RepositoryService;
import vn.softdreams.ebweb.domain.Repository;
import vn.softdreams.ebweb.repository.RepositoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RepositorySaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing Repository.
 */
@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    private final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    private final RepositoryRepository repositoryRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final MaterialGoodsRepository materialGoodsRepository;
    private final SystemOptionRepository systemOptionRepository;

    public RepositoryServiceImpl(RepositoryRepository repositoryRepository,
                                 OrganizationUnitRepository organizationUnitRepository,
                                 UserService userService,
                                 UtilsService utilsService,
                                 MaterialGoodsRepository materialGoodsRepository,
                                 SystemOptionRepository systemOptionRepository
    ) {
        this.repositoryRepository = repositoryRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.materialGoodsRepository = materialGoodsRepository;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a repository.
     *
     * @param repository the entity to save
     * @return the persisted entity
     */
    @Override
    public RepositorySaveDTO save(Repository repository) {
        log.debug("Request to save Repository : {}", repository);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Repository un = new Repository();
        RepositorySaveDTO repositorySaveDTO = new RepositorySaveDTO();
        if (currentUserLoginAndOrg.isPresent()) {
            if (repositoryRepository.findByCompanyIDAndRepositoryCode(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMKho), repository.getRepositoryCode(),
                repository.getId() == null ? new UUID(0L, 0L): repository.getId()) != null) {
                repositorySaveDTO.setStatus(1);
                repositorySaveDTO.setRepository(repository);
                return repositorySaveDTO;
            }
            repository.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (repository.getId() == null) {
                repository.setIsActive(true);
            }
            un = repositoryRepository.save(repository);
            repositorySaveDTO.setRepository(un);
            repositorySaveDTO.setStatus(0);
        }
        return repositorySaveDTO;
    }

    /**
     * Get all the repositories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Repository> findAll(Pageable pageable) {
        log.debug("Request to get all Repositories");
        return repositoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Repository> pageableAllRepositories(Pageable pageable) {
        log.debug("Request to get all Repository");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean check = systemOptionRepository.findByCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho) == 1;
        if (!check) {
            return repositoryRepository.pageableAllRepositories(pageable, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho));
        } else {
            return repositoryRepository.pageableAllRepositories(pageable, Stream.of(currentUserLoginAndOrg.get().getOrg()).collect(Collectors.toList()));
        }
    }

    /**
     * Get one repository by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Repository> findOne(UUID id) {
        log.debug("Request to get Repository : {}", id);
        return repositoryRepository.findById(id);
    }

    /**
     * Delete the repository by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Repository : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "RepositoryID");
        MaterialGoods materialGoods = materialGoodsRepository.findByRepositoryIDAndCompanyID(currentUserLoginAndOrg.get().getOrg(), id);
        if (checkUsedAcc) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else if (materialGoods != null) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        }
            else{
                repositoryRepository.deleteById(id);
            }
        }

    public HandlingResultDTO deleteRepository(List<UUID> uuids) {
        log.debug("Request to delete Repository : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = repositoryRepository.getIDRef(currentUserLoginAndOrg.get().getOrg(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            repositoryRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public List<Repository> getRepositoryReport(UUID companyID, Boolean similarBranch) {
        List<UUID> comIds = new ArrayList<>();
        if (companyID != null) {
            if (similarBranch != null && similarBranch) {
                comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
            } else {
                comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMKho);
            }
            return repositoryRepository.findAllRepository(comIds);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<Repository> getRepositoryComboboxGetAll() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return repositoryRepository.getRepositoryComboboxGetAll(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<Repository> getRepositoryCombobox(Boolean similarBranch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> comIds = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            if (similarBranch != null && similarBranch) {
                comIds = organizationUnitRepository.getCompanyAndBranch(currentUserLoginAndOrg.get().getOrg()).stream().map(x -> x.getId()).collect(Collectors.toList());
            } else {
                comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho);
            }
            return repositoryRepository.findAllRepository(comIds);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<Repository> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return repositoryRepository.findAllRepository(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<Repository> listAllRepositories(Boolean isGetAllCompany) {
        log.debug("Request to get all Repository");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (isGetAllCompany) {
                List<UUID> listCompanyID = new ArrayList<>();
                Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
                if (organizationUnit.get().getUnitType().equals(Constants.OrgUnitTypeConstant.TONG_CONG_TY)) {
                    listCompanyID.add(currentUserLoginAndOrg.get().getOrg());
                    List<OrganizationUnit> listCompany = organizationUnitRepository.getChildCom(currentUserLoginAndOrg.get().getOrg(), Constants.OrgUnitTypeConstant.CHI_NHANH);
                    for (OrganizationUnit item: listCompany) {
                        listCompanyID.add(item.getId());
                    }
                } else if (organizationUnit.get().getUnitType().equals(Constants.OrgUnitTypeConstant.CHI_NHANH)) {
                    listCompanyID.add(organizationUnit.get().getParentID());
                    List<OrganizationUnit> listCompany = organizationUnitRepository.getChildCom(organizationUnit.get().getParentID(), Constants.OrgUnitTypeConstant.CHI_NHANH);
                    for (OrganizationUnit item: listCompany) {
                        listCompanyID.add(item.getId());
                    }
                }
                return repositoryRepository.getAllByListCompanyCBB(listCompanyID);
            } else {
                return repositoryRepository.pageableAllRepositoriesCBB(currentUserLoginAndOrg.get().getOrg());
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

}
