package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EbPackageService;
import vn.softdreams.ebweb.domain.EbPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CRMEbPackageDTO;
import vn.softdreams.ebweb.web.rest.dto.CRMUserRespDTO;

import java.util.*;

import static vn.softdreams.ebweb.service.util.Constants.CRMResStatus.*;
import static vn.softdreams.ebweb.service.util.Constants.EbPackage.*;

/**
 * Service Implementation for managing EbPackage.
 */
@Service
@Transactional
public class EbPackageServiceImpl implements EbPackageService {

    private final Logger log = LoggerFactory.getLogger(EbPackageServiceImpl.class);
    private UserService userService;
    private final EbPackageRepository ebPackageRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UserRepository userRepository;
    private final EbUserPackageRepository ebUserPackageRepository;
    private final EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository;

    public EbPackageServiceImpl(EbPackageRepository ebPackageRepository,
                                UserService userService,
                                OrganizationUnitRepository organizationUnitRepository,
                                EbUserPackageRepository ebUserPackageRepository,
                                EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository,
                                UserRepository userRepository) {
        this.ebPackageRepository = ebPackageRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userRepository = userRepository;
        this.ebUserPackageRepository = ebUserPackageRepository;
        this.ebUserOrganizationUnitRepository = ebUserOrganizationUnitRepository;
    }

    /**
     * Save a ebPackage.
     *
     * @param ebPackage the entity to save
     * @return the persisted entity
     */
    @Override
    public EbPackage save(EbPackage ebPackage) {
        log.debug("Request to save EbPackage : {}", ebPackage);
        EbPackage oldEbPackage = new EbPackage();
        if (ebPackage.getId() == null) {
            ebPackage.setStatus(true);
        } else {
            oldEbPackage = ebPackageRepository.findOneByID(ebPackage.getId());
        }
        if (ebPackage.getId() == null) {
            if (ebPackageRepository.countPackageCode(ebPackage.getPackageCode()) > 0) {
                ebPackage.setIsSave(false);
                return ebPackage;
            }
        } else {
            if (oldEbPackage.getPackageCode() != null &&
                !oldEbPackage.getPackageCode().equals(ebPackage.getPackageCode()) &&
                ebPackageRepository.countPackageCode(ebPackage.getPackageCode()) > 0) {
                ebPackage.setIsSave(false);
                return ebPackage;
            }
        }
        ebPackage = ebPackageRepository.save(ebPackage);
        ebPackage.setIsSave(true);
        return ebPackage;
    }

    /**
     * Get all the ebPackages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EbPackage> findAll(Pageable pageable) {
        log.debug("Request to get all EbPackages");
        return ebPackageRepository.findAllEbPackage(pageable);
    }

    @Override
    public List<EbPackage> findAll() {
        return ebPackageRepository.findAll();
    }

    /**
     * Get one ebPackage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EbPackage> findOne(UUID id) {
        log.debug("Request to get EbPackage : {}", id);
        return ebPackageRepository.findById(id);
    }

    /**
     * Delete the ebPackage by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete EbPackage : {}", id);
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByPackageID(id);
        if (ebUserPackage != null) {
            ebUserOrganizationUnitRepository.deleteEbUserOrganizationUnitByUserId(ebUserPackage.getUserID());
        }
        ebUserPackageRepository.deleteEbUserPackageByPackageID(id);
        ebPackageRepository.deleteById(id);
    }

    @Override
    public Integer findOneByOrgIdAndUserId(Integer comType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
        EbPackage ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
        int amountCompanyCurrent = checkAmountCompanyInNormalPackage(currentUserLoginAndOrg.get().getOrg(), comType);
        if (ebPackage != null) {
            return (amountCompanyCurrent < ebPackage.getLimitedCompany() + 1) ? Constants.EbPackage.CO_PACKAGE : Constants.EbPackage.HET_PACKAGE;
        } else {
            return Constants.EbPackage.NULL_PACKAGE;
        }
    }

    private Integer checkAmountCompanyInNormalPackage(UUID orgID, Integer comType) {
        List<OrganizationUnit> result = new ArrayList<>();
        Queue<OrganizationUnit> listOrganizationUnit = new LinkedList<>();
        OrganizationUnit organizationUnitBegin = organizationUnitRepository.findByID(orgID);
        if (organizationUnitBegin != null) {
            listOrganizationUnit.add(organizationUnitBegin);
        }
        while (!listOrganizationUnit.isEmpty()) {
            OrganizationUnit organizationUnit = listOrganizationUnit.remove();
            List<OrganizationUnit> listOrganizationUnitNew = new ArrayList<>();
            if (comType == Constants.EbPackage.COMTYPE_2_HASBRANCH) {
                listOrganizationUnitNew = organizationUnitRepository.findOrganizationUnitByBranch(organizationUnit.getId());
            }
            if (comType == COMTYPE_3_SERVICEACC) {
                listOrganizationUnitNew = organizationUnitRepository.findOrganizationUnitByBranchAndOrg(organizationUnit.getId());
            }
            listOrganizationUnit.addAll(listOrganizationUnitNew);
            result.add(organizationUnit);
        }
        return result.size();
    }

    @Override
    public Integer findOneByUserId() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
        EbPackage ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
        int amountUserCurrent = userRepository.countByUserId(user.get().getId());
        if (ebPackage != null) {
            return (amountUserCurrent < ebPackage.getLimitedUser()) ? Constants.EbPackage.CO_PACKAGE : Constants.EbPackage.HET_PACKAGE;
        } else {
            return Constants.EbPackage.NULL_PACKAGE;
        }

    }

    @Override
    public CRMUserRespDTO createNewEbPackageCrm(CRMEbPackageDTO cRMEbPackageDTO) {
        CRMUserRespDTO crmUserRespDTO = new CRMUserRespDTO();
        if (ebPackageRepository.countPackageCode(cRMEbPackageDTO.getPackageCode()) > 0) {
            crmUserRespDTO.setSystemCode(Fail_EbPackageExist);
            return crmUserRespDTO;
        }
        EbPackage ebPackage = new EbPackage();
        ebPackage.setPackageCode(cRMEbPackageDTO.getPackageCode());
        ebPackage.setPackageName(cRMEbPackageDTO.getPackageCode());
        ebPackage.setDescription(cRMEbPackageDTO.getDescription());
        ebPackage.setLimitedVoucher(Integer.parseInt(cRMEbPackageDTO.getLimitedVoucher()));
        ebPackage.setLimitedUser(Integer.parseInt(cRMEbPackageDTO.getLimitedUser()));
        ebPackage.setLimitedCompany(Integer.parseInt(cRMEbPackageDTO.getLimitedCompany()));
        ebPackage.setExpiredTime(Integer.parseInt(cRMEbPackageDTO.getExpireTime()));
        ebPackage.setComType(Integer.parseInt(cRMEbPackageDTO.getComType()));
        ebPackage.setStatus(true);
        ebPackageRepository.save(ebPackage);
        crmUserRespDTO.setSystemCode(Done);
        return crmUserRespDTO;
    }

    /**
     * @param servicePackage
     * @param companyTaxCode
     * @return true: gia hạn; false: nâng cấp
     */
    @Override
    public Boolean isExtension(String servicePackage, String companyTaxCode) {
        EbUserPackage ebUserPackage = findOneByTaxCode(companyTaxCode);
        return ebPackageRepository.findOneByPackageCode(servicePackage) == ebPackageRepository.findOneByID(ebUserPackage.getPackageID());
    }

    @Override
    public EbUserPackage findOneByTaxCode(String companyTaxCode) {
        return ebUserPackageRepository.findOneByTaxCode(companyTaxCode);
    }

    @Override
    public CRMUserRespDTO updateEbPackageCrm(CRMEbPackageDTO cRMEbPackageDTO) {
        CRMUserRespDTO crmUserRespDTO = new CRMUserRespDTO();
        EbPackage ebPackage = ebPackageRepository.findOneByPackageCode(cRMEbPackageDTO.getPackageCode());
        if (ebPackage == null) {
            crmUserRespDTO.setSystemCode(Fail_EbPackageInvalid);
            return crmUserRespDTO;
        }
        ebPackage.setPackageCode(cRMEbPackageDTO.getPackageCode());
        ebPackage.setPackageName(cRMEbPackageDTO.getPackageCode());
        ebPackage.setDescription(cRMEbPackageDTO.getDescription());
        ebPackage.setLimitedVoucher(Integer.parseInt(cRMEbPackageDTO.getLimitedVoucher()));
        ebPackage.setLimitedUser(Integer.parseInt(cRMEbPackageDTO.getLimitedUser()));
        ebPackage.setLimitedCompany(Integer.parseInt(cRMEbPackageDTO.getLimitedCompany()));
        ebPackage.setExpiredTime(Integer.parseInt(cRMEbPackageDTO.getExpireTime()));
        ebPackage.setComType(Integer.parseInt(cRMEbPackageDTO.getComType()));
        ebPackage.setStatus(true);
        ebPackageRepository.save(ebPackage);
        crmUserRespDTO.setSystemCode(Done);
        return crmUserRespDTO;
    }

}
