package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.GenCodeRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.UserRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.SystemOptionService;
import vn.softdreams.ebweb.domain.SystemOption;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;
import vn.softdreams.ebweb.service.dto.SaveSystemOptionsDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SystemOption.
 */
@Service
@Transactional
public class SystemOptionServiceImpl implements SystemOptionService {

    private final Logger log = LoggerFactory.getLogger(SystemOptionServiceImpl.class);

    private final SystemOptionRepository systemOptionRepository;

    private UserRepository userRepository;
    private GenCodeRepository genCodeRepository;
    private OrganizationUnitService organizationUnitService;
    private OrganizationUnitRepository organizationUnitRepository;
    private UserService userService;

    public SystemOptionServiceImpl(SystemOptionRepository systemOptionRepository, UserRepository userRepository, GenCodeRepository genCodeRepository, OrganizationUnitService organizationUnitService, OrganizationUnitRepository organizationUnitRepository, UserService userService) {
        this.systemOptionRepository = systemOptionRepository;
        this.userRepository = userRepository;
        this.genCodeRepository = genCodeRepository;
        this.organizationUnitService = organizationUnitService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
    }

    /**
     * Save a systemOption.
     *
     * @param systemOption the entity to save
     * @return the persisted entity
     */
    @Override
    public SystemOption save(SystemOption systemOption) {
        log.debug("Request to save SystemOption : {}", systemOption);
        return systemOptionRepository.save(systemOption);
    }

    /**
     * Get all the systemOptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SystemOption> findAll(Pageable pageable) {
        log.debug("Request to get all SystemOptions");
        return systemOptionRepository.findAll(pageable);
    }


    /**
     * Get one systemOption by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SystemOption> findOne(Long id) {
        log.debug("Request to get SystemOption : {}", id);
        return systemOptionRepository.findById(id);
    }

    /**
     * Delete the systemOption by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemOption : {}", id);
        systemOptionRepository.deleteById(id);
    }

    @Override
    public Optional<SystemOption> findOneByUser(String code) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        return systemOptionRepository.findOneByUser(user.get().getId(), code);
    }

    @Override
    public void savePostedDate(String data, String dataDefault) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            systemOptionRepository.savePostedDate(data, dataDefault, currentUserLoginAndOrg.get().getOrg());

        }
    }

    @Override
    public List<PrivateToGeneralUse> saveSystemOptions(SaveSystemOptionsDTO saveSystemOptionsDTO) {
        List<OrganizationUnit> organizationUnits = new ArrayList<>();
        List<PrivateToGeneralUse> listResult = new ArrayList<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
            if (organizationUnit.getParentID() != null) {
                UUID orgID = organizationUnitService.findTopParent(currentUserLoginAndOrg.get().getOrg());
                OrganizationUnit organizationUnitParent = organizationUnitRepository.findByID(orgID);
                organizationUnits.add(organizationUnitParent);
                Integer comType = organizationUnitRepository.findComTypeByOrgID(orgID);
                if (comType == 3) {
                    List<OrganizationUnit> organizationUnitList = organizationUnitRepository.findByParentIDPopup(orgID);
                    if (organizationUnitList != null && organizationUnitList.size() > 0) {
                        for (int i = 0; i < organizationUnitList.size(); i++) {
                            Integer value = i;
                            Optional<OrganizationUnit> organizationUnit1 = organizationUnitList.stream().filter(a -> a.getId().equals((organizationUnitList.get(value).getId()))).findFirst();
                            if (!organizationUnit1.isPresent()) {
                                organizationUnits.add(organizationUnitList.get(value));
                            }
                        }
                    }
                }
                organizationUnitService.getListChildOrganizationUnitCustom(organizationUnits, orgID);
            } else {
                UserDTO userDTO = userService.getAccount();
                if (userDTO.getEbPackage() != null) {
                    if (userDTO.getEbPackage().getComType() == 3) {
                        List<OrganizationUnit> organizationUnitList = organizationUnitRepository.findByParentIDPopup(organizationUnit.getId());
                        if (organizationUnitList != null && organizationUnitList.size() > 0) {
                            for (int i = 0; i < organizationUnitList.size(); i++) {
                                Integer value = i;
                                Optional<OrganizationUnit> organizationUnit1 = organizationUnitList.stream().filter(a -> a.getId().equals((organizationUnitList.get(value).getId()))).findFirst();
                                if (!organizationUnit1.isPresent()) {
                                    organizationUnits.add(organizationUnitList.get(value));
                                }
                            }
                        }
                    } else {
                        organizationUnits.add(organizationUnit);
                    }
                } else {
                    organizationUnits.add(organizationUnit);
                }
                organizationUnitService.getListChildOrganizationUnitCustom(organizationUnits, organizationUnit.getId());
            }
            List<SystemOption> systemOptions = saveSystemOptionsDTO.getSystemOptions();
            List<SystemOption> systemOptionsOld = systemOptionRepository.findByCompanyId(currentUserLoginAndOrg.get().getOrg());
            List<UUID> listOrgCheckGeneralUse = organizationUnits.stream().filter(a -> a.getUnitType().equals(0) ||
                a.getUnitType().equals(1)).map(a -> a.getId()).collect(Collectors.toList());
            String listUUIDOrg = "";
            for (int i = 0; i < listOrgCheckGeneralUse.size(); i++) {
                listUUIDOrg += "," + Common.revertUUID(listOrgCheckGeneralUse.get(i)).toString().toUpperCase();
            }
            String listCheckGeneral = "";
            String listCheckPrivate = "";
            String dataTCKHAC_SDDMDoiTuong = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMDoiTuong")).findFirst().get().getData();
            String dataTCKHAC_SDDMVTHH = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMVTHH")).findFirst().get().getData();
            String dataTCKHAC_SDDMKho = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMKho")).findFirst().get().getData();
            String dataTCKHAC_SDDMCCDC = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMCCDC")).findFirst().get().getData();
            String dataTCKHAC_SDDMTSCD = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTSCĐ")).findFirst().get().getData();
            String dataTCKHAC_SDDMDTTHCP = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMĐTTHCP")).findFirst().get().getData();
            String dataTCKHAC_SDDMTKNH = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTKNH")).findFirst().get().getData();
            String dataTCKHAC_SDDMTheTD = systemOptions.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTheTD")).findFirst().get().getData();
            if (!(dataTCKHAC_SDDMDoiTuong
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMDoiTuong")).findFirst().get().getData()))
                && dataTCKHAC_SDDMDoiTuong.equals("0")) {
                listCheckGeneral += ",1";
            }
            if (!(dataTCKHAC_SDDMTSCD
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTSCĐ")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTSCD.equals("0")) {
                listCheckGeneral += ",2";
            }
            if (!(dataTCKHAC_SDDMCCDC
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMCCDC")).findFirst().get().getData()))
                && dataTCKHAC_SDDMCCDC.equals("0")) {
                listCheckGeneral += ",3";
            }
            if (!(dataTCKHAC_SDDMTKNH
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTKNH")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTKNH.equals("0")) {
                listCheckGeneral += ",4";
            }
            if (!(dataTCKHAC_SDDMVTHH
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMVTHH")).findFirst().get().getData()))
                && dataTCKHAC_SDDMVTHH.equals("0")) {
                listCheckGeneral += ",5";
            }
            if (!(dataTCKHAC_SDDMKho
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMKho")).findFirst().get().getData()))
                && dataTCKHAC_SDDMKho.equals("0")) {
                listCheckGeneral += ",6";
            }
            if (!(dataTCKHAC_SDDMDTTHCP
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMĐTTHCP")).findFirst().get().getData()))
                && dataTCKHAC_SDDMDTTHCP.equals("0")) {
                listCheckGeneral += ",7";
            }
            if (!(dataTCKHAC_SDDMTheTD
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTheTD")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTheTD.equals("0")) {
                listCheckGeneral += ",8";
            }
            if (!(dataTCKHAC_SDDMDoiTuong
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMDoiTuong")).findFirst().get().getData()))
                && dataTCKHAC_SDDMDoiTuong.equals("1")) {
                listCheckPrivate += ",1";
            }
            if (!(dataTCKHAC_SDDMTSCD
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTSCĐ")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTSCD.equals("1")) {
                listCheckPrivate += ",2";
            }
            if (!(dataTCKHAC_SDDMCCDC
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMCCDC")).findFirst().get().getData()))
                && dataTCKHAC_SDDMCCDC.equals("1")) {
                listCheckPrivate += ",3";
            }
            if (!(dataTCKHAC_SDDMTKNH
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTKNH")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTKNH.equals("1")) {
                listCheckPrivate += ",4";
            }
            if (!(dataTCKHAC_SDDMVTHH
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMVTHH")).findFirst().get().getData()))
                && dataTCKHAC_SDDMVTHH.equals("1")) {
                listCheckPrivate += ",5";
            }
            if (!(dataTCKHAC_SDDMKho
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMKho")).findFirst().get().getData()))
                && dataTCKHAC_SDDMKho.equals("1")) {
                listCheckPrivate += ",6";
            }
            if (!(dataTCKHAC_SDDMDTTHCP
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMĐTTHCP")).findFirst().get().getData()))
                && dataTCKHAC_SDDMDTTHCP.equals("1")) {
                listCheckPrivate += ",7";
            }
            if (!(dataTCKHAC_SDDMTheTD
                .equals(systemOptionsOld.stream().filter(a -> a.getCode().equals("TCKHAC_SDDMTheTD")).findFirst().get().getData()))
                && dataTCKHAC_SDDMTheTD.equals("1")) {
                listCheckPrivate += ",8";
            }
            listCheckGeneral += ",";
            listCheckPrivate += ",";
            listUUIDOrg += ",";
            if (!listCheckGeneral.equals(",") && !listUUIDOrg.equals(",")) {
                listResult = systemOptionRepository.checkPrivateToGeneralUse(currentUserLoginAndOrg.get().getOrg(), listUUIDOrg, listCheckGeneral);
            }
            if (listResult.size() > 0) {
                return listResult;
            }
            Integer countExist = 0;
            if (!listCheckPrivate.equals(",") && !listUUIDOrg.equals(",")) {
                countExist = systemOptionRepository.checkGeneralToPrivateUse(listUUIDOrg, listCheckPrivate);
            }
            if (countExist > 0) {
                throw new BadRequestAlertException("Còn tồn tại ràng buộc, không thể thay đổi !", "SystemOption", "errorGeneralToPrivateUse");
            }
            systemOptionRepository.updateAllSystemOptionByCompanyID(organizationUnits, dataTCKHAC_SDDMDoiTuong, dataTCKHAC_SDDMVTHH,
                dataTCKHAC_SDDMKho, dataTCKHAC_SDDMCCDC, dataTCKHAC_SDDMTSCD, dataTCKHAC_SDDMDTTHCP, dataTCKHAC_SDDMTKNH, dataTCKHAC_SDDMTheTD);
            systemOptionRepository.saveAll(saveSystemOptionsDTO.getSystemOptions());
            genCodeRepository.saveAll(saveSystemOptionsDTO.getGenCodes());
        }
        return listResult;
    }

    public List<SystemOption> findAllSystemOptions() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return systemOptionRepository.findAllSystemOptions(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<SystemOption> getSystemOptionsByCompanyID(UUID companyID) {
        return systemOptionRepository.findAllSystemOptions(companyID);
    }
}
