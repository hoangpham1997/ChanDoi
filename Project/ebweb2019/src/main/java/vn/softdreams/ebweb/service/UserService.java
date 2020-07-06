package vn.softdreams.ebweb.service;

import com.google.common.base.Strings;
import com.sun.org.apache.xpath.internal.operations.Or;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Now;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.AuthoritiesConstants;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.security.jwt.TokenProvider;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.RandomUtil;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.vm.LoginVM;

import javax.validation.Valid;
import java.sql.Date;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static vn.softdreams.ebweb.service.util.Constants.API_CRM.Email_Title;
import static vn.softdreams.ebweb.service.util.Constants.CRMReqStatus.Cancel;
import static vn.softdreams.ebweb.service.util.Constants.CRMResStatus.*;
import static vn.softdreams.ebweb.service.util.Constants.EbPackage.*;
import static vn.softdreams.ebweb.service.util.Constants.EbUserPackage.*;
import static vn.softdreams.ebweb.service.util.Constants.OrgCode.Ma_Cong_Ty;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final EbUserGroupRepository ebUserGroupRepository;

    private final EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final EbUserPackageRepository ebUserPackageRepository;

    private final EbGroupRepository ebGroupRepository;

    private final CacheManager cacheManager;

    private final SystemOptionRepository systemOptionRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository;

    private final TokenProvider tokenProvider;

    private final EbPackageRepository ebPackageRepository;

    private final MailService mailService;

//    private final OrganizationUnitService organizationUnitService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthorityRepository authorityRepository, CacheManager cacheManager,
                       SystemOptionRepository systemOptionRepository,
                       EbGroupRepository ebGroupRepository,
                       OrganizationUnitRepository organizationUnitRepository,
                       EbUserGroupRepository ebUserGroupRepository,
                       TokenProvider tokenProvider,
                       EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository,
                       OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository,
                       EbUserPackageRepository ebUserPackageRepository,
                       EbPackageRepository ebPackageRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.ebGroupRepository = ebGroupRepository;
        this.cacheManager = cacheManager;
        this.systemOptionRepository = systemOptionRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.ebUserGroupRepository = ebUserGroupRepository;
        this.tokenProvider = tokenProvider;
        this.ebPackageRepository = ebPackageRepository;
        this.ebUserOrganizationUnitRepository = ebUserOrganizationUnitRepository;
        this.ebUserPackageRepository = ebUserPackageRepository;
        this.mailService = mailService;
        this.organizationUnitOptionReportRepository = organizationUnitOptionReportRepository;
//        this.organizationUnitService = organizationUnitService;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFullName(userDTO.getFullName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        Set<EbAuthority> authorities = new HashSet<>();
        authorityRepository.findOneByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public Boolean createUserOrg(UserIDAndOrgIdDTO userOrgID) {
        try {
            userRepository.updateEbUser(userOrgID.getOrgID(), userOrgID.getUserID());
            organizationUnitRepository.updateOrganizationUnit(userOrgID.getUserID(), userOrgID.getOrgID());
            ebUserOrganizationUnitRepository.deleteUserAndOrg(userOrgID.getUserID(), userOrgID.getOrgID());
            ebUserOrganizationUnitRepository.insertUserAndOrg(userOrgID.getUserID(), userOrgID.getOrgID());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<UserDTO> getListUser(UUID orgID) {
        List<UserDTO> listUserDTO = new ArrayList<>();
        List<User> listUser = new ArrayList<>();
        listUser = userRepository.getListUser();
        if (orgID != null) {
            EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByCompanyID(orgID);
            if (ebUserPackage != null) {
                User user = userRepository.getOne(ebUserPackage.getUserID());
                listUser.add(user);
            }
        }
        listUser.stream()
            .map(UserDTO::new)
            .forEach(listUserDTO::add);
        return listUserDTO;
    }

    public EbUserOrganizationUnit getOrgByUser(Long userId) {
        return ebUserOrganizationUnitRepository.getOrgByUser(userId);
    }

    /**
     * @param userDTO
     * @return
     * @author anmt
     */
    public UserSaveDTO createNewUser(UserDTO userDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> userLogin = userRepository.findOneWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
        EbPackage ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(userLogin.get().getId(), currentUserLoginAndOrg.get().getOrg());
        int amountUserCurrent = userRepository.countByUserId(userLogin.get().getId());
        User user = new User();
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        if (ebPackage == null) {
            throw new BadRequestAlertException("Đơn vị/ Tổ chức chưa đăng ký gói dịch vụ! Vui lòng liên hệ với đơn vị cung cấp giải pháp phần mềm Kế toán."
                , "", "isBuyPackage");
        } else if (ebPackage.getLimitedUser() != UNLIMITED_PACKAGE && amountUserCurrent >= ebPackage.getLimitedUser()) {
            throw new BadRequestAlertException("Vượt quá số lượng người dùng cho phép", "", "exceedsAmountUser");
        }
        setNewUserFC(user, userDTO);
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setIsSystem(false);
//        user.setEbGroups(userDTO.getEbGroups());
//        if (userDTO.getAuthorities() != null) {
//            Set<EbAuthority> authorities = userDTO.getAuthorities().stream()
//                .map(authorityRepository::findOneByName)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//            user.setAuthorities(authorities);
//        }
        Set<EbAuthority> authorities = new HashSet<>();
        authorityRepository.findOneByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        user.setAuthorities(authorities);
//        List<User> userList = userRepository.findAllByParentID(userLogin.get().getId());
//        Boolean isContinue = userList.stream().noneMatch(n -> n.getLogin().equals(userDTO.getLogin()));
//        if (!isContinue) {
//            userSaveDTO.setUser(user);
//            userSaveDTO.setStatus(1);
//            return userSaveDTO;
//        }
        if (userRepository.countEbUserByLogin(userDTO.getLogin().toLowerCase()) > 0) {
            userSaveDTO.setUser(user);
            userSaveDTO.setStatus(1);
            return userSaveDTO;
        }
        // set parentID
        user.setParentID(userLogin.get().getId());
        user = userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        userSaveDTO.setUser(user);
        userSaveDTO.setStatus(0);
        return userSaveDTO;
    }

    public UserSaveDTO createNewUserAdmin(UserDTO userDTO) {
        User user = new User();
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        setNewUserFCCreateAdmin(user, userDTO);
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setIsSystem(true);
//        if (userDTO.getAuthorities() != null) {
//            Set<EbAuthority> authorities = userDTO.getAuthorities().stream()
//                .map(authorityRepository::findOneByName)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//            user.setAuthorities(authorities);
//        }
        Set<EbAuthority> authorities = new HashSet<>();
        authorityRepository.findOneByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
//        authorityRepository.findOneByName(AuthoritiesConstants.ADMIN).ifPresent(authorities::add);
        user.setAuthorities(authorities);
        if (userRepository.countEbUserByLogin(userDTO.getLogin().toLowerCase()) > 0) {
            userSaveDTO.setUser(user);
            userSaveDTO.setStatus(1);
            return userSaveDTO;
        }
        User userSaved = userRepository.save(user);
        // insert EbUserAuthority
        userRepository.insertEbUserAuthorities(userSaved.getId());
        ebUserOrganizationUnitRepository.insertUserAndOrg(userSaved.getId(), userDTO.getOrgID());
        if (userDTO.getStatus() == DANG_DUNG) {
            // neu la active
            EbPackage ebPackage = ebPackageRepository.getOne(userDTO.getPackageID());
            LocalDate activedDate = LocalDate.now();
            LocalDate expriredDate = activedDate.plusMonths(ebPackage.getExpiredTime());
            userRepository.insertEbUserPackageForActive(userSaved.getId(), userDTO.getPackageID(), userDTO.getOrgID(), userDTO.getStatus(),
                activedDate,expriredDate
            );
        } else {
            userRepository.insertEbUserPackage(userSaved.getId(), userDTO.getPackageID(), userDTO.getOrgID(), userDTO.getStatus());
        }
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        userSaveDTO.setUser(user);
        userSaveDTO.setStatus(0);
        return userSaveDTO;
    }

    /**
     * @param userDTO
     * @return
     * @author anmt
     */
    public Boolean updateEbUser(UserDTO userDTO) {
        try {
            if (userDTO.getIsChangePassword()) {
                String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
                userDTO.setPassword(encryptedPassword);
            }
            // update EbUser
            userRepository.updateEbUser(userDTO.getJob(), userDTO.getFullName(), userDTO.getPassword(),
                userDTO.isActivated(), userDTO.getId()
            );
            // clear cache
            User user = userRepository.findById(userDTO.getId()).get();
            this.clearUserCaches(user);
            log.debug("Created Information for User: {}", user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * @param userDTO
     * @return
     * @author anmt
     */
    public Boolean updateForEbGroupOrg(UserDTO userDTO) {
        try {
            List<EbUserGroupDTO> listUserGr = new ArrayList<>();
            // lay list ebusergroup
            for (EbGroup ebGroup : userDTO.getEbGroups()) {
                if (ebGroup.getListOrg().size() > 0) {
                    for (int i = 0; i < ebGroup.getListOrg().size(); i++) {
                        EbUserGroupDTO ebUserGroupDTO = new EbUserGroupDTO();
//                    ebUserGroupDTO.setId(UUID.randomUUID());
                        ebUserGroupDTO.setUserID(userDTO.getId());
                        ebUserGroupDTO.setGroupID(ebGroup.getId());
                        ebUserGroupDTO.setCompanyID(ebGroup.getListOrg().get(i).getValue());
                        ebUserGroupDTO.setWorkingOnBook(Integer.valueOf(ebGroup.getListOrg().get(i).getWorkingOnBook()));
                        listUserGr.add(ebUserGroupDTO);
                    }
                } else {
                    EbUserGroupDTO ebUserGroupDTO = new EbUserGroupDTO();
                    ebUserGroupDTO.setUserID(userDTO.getId());
                    ebUserGroupDTO.setGroupID(ebGroup.getId());
                    listUserGr.add(ebUserGroupDTO);
                }
            }
            // loc lay du lieu co ComId
            List<EbUserGroupDTO> listUserGrHasComId = listUserGr.stream().
                filter(x -> x.getCompanyID() != null).collect(Collectors.toList());
            Map<UUID, List<EbUserGroupDTO>> newListUserGrHasId =
                listUserGrHasComId.stream().collect(Collectors.groupingBy(w -> w.getCompanyID()));
            List<UUID> lstComID = newListUserGrHasId.entrySet().stream().map(n -> n.getKey()).collect(Collectors.toList());
            // lay ra list ebuserorg co comid
            List<EbUserOrganizationUnitDTO> listUserOrg = new ArrayList<>();
            for (int i = 0; i < newListUserGrHasId.size(); i++) {
                EbUserOrganizationUnitDTO ebUserOrganizationUnitDTO = new EbUserOrganizationUnitDTO();
                ebUserOrganizationUnitDTO.setUserID(userDTO.getId());
                ebUserOrganizationUnitDTO.setCurrentBook("0");
                ebUserOrganizationUnitDTO.setOrgID(lstComID.get(i));
                listUserOrg.add(ebUserOrganizationUnitDTO);
            }
            Long countGr = userRepository.countEbUserGroupsByUserId(userDTO.getId());
            if (countGr > 0) {
                userRepository.deleteByUserId(userDTO.getId());
            }
            Long countOrg = userRepository.countEbOrgUnitsByUserId(userDTO.getId());
            if (countOrg > 0) {
                userRepository.deleteEbOrgUnitByUserId(userDTO.getId());
            }
            userRepository.insertUserEbGroup(listUserGr);
            userRepository.insertEbUserOrganizationUnit(listUserOrg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * @param
     * @return
     * @author anmt
     */
    public Boolean updateForEbGroupOrg(OrgTreeUserDTO orgTreeUserDTO) {
        try {
            // insert ebUserGroup
            List<EbUserGroupDTO> listUserGr = new ArrayList<>();
            for (int i = 0; i < orgTreeUserDTO.getListOrg().size(); i++) {
                for (EbGroup ebG : orgTreeUserDTO.getListOrg().get(i).getGroups()) {
                    EbUserGroupDTO ebUserGroupDTO = new EbUserGroupDTO();
//                    ebUserGroupDTO.setId(UUID.randomUUID());
                    ebUserGroupDTO.setUserID(orgTreeUserDTO.getUser().getId());
                    ebUserGroupDTO.setGroupID(ebG.getId());
                    ebUserGroupDTO.setCompanyID(orgTreeUserDTO.getListOrg().get(i).getValue());
                    ebUserGroupDTO.setWorkingOnBook(Integer.valueOf(orgTreeUserDTO.getListOrg().get(i).getWorkingOnBook()));
                    listUserGr.add(ebUserGroupDTO);
                }
            }
            // lay ra list ebuserorg co comid
            List<EbUserOrganizationUnitDTO> listUserOrg = new ArrayList<>();
            for (int i = 0; i < orgTreeUserDTO.getListOrg().size(); i++) {
                EbUserOrganizationUnitDTO ebUserOrganizationUnitDTO = new EbUserOrganizationUnitDTO();
                ebUserOrganizationUnitDTO.setUserID(orgTreeUserDTO.getUser().getId());
                String book = orgTreeUserDTO.getListOrg().get(i).getWorkingOnBook();
                if (book.equals(vn.softdreams.ebweb.service.util.Constants.TypeLedger.BOTH_BOOK.toString())) {
                    ebUserOrganizationUnitDTO.setCurrentBook(vn.softdreams.ebweb.service.util.Constants.TypeLedger.FINANCIAL_BOOK.toString());
                } else {
                    ebUserOrganizationUnitDTO.setCurrentBook(book);
                }
                ebUserOrganizationUnitDTO.setOrgID(orgTreeUserDTO.getListOrg().get(i).getValue());
                listUserOrg.add(ebUserOrganizationUnitDTO);
            }
            Long countGr = userRepository.countEbUserGroupsByUserId(orgTreeUserDTO.getUser().getId());
            if (countGr > 0) {
                userRepository.deleteByUserId(orgTreeUserDTO.getUser().getId());
            }
            Long countOrg = userRepository.countEbOrgUnitsByUserId(orgTreeUserDTO.getUser().getId());
            if (countOrg > 0) {
                userRepository.deleteEbOrgUnitByUserId(orgTreeUserDTO.getUser().getId());
            }
            userRepository.insertUserEbGroup(listUserGr);
            userRepository.insertEbUserOrganizationUnit(listUserOrg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * @param userDTO
     * @return
     * @author anmt
     */
    public Optional<UserDTO> updateInfoUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                setInfoUser(user, userDTO);
//                Set<EbAuthority> managedAuthorities = user.getAuthorities();
//                managedAuthorities.clear();
//                userDTO.getAuthorities().stream()
//                    .map(authorityRepository::findOneByName)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    private String getStatus(Integer status) {
        String result = "";
        if (status == CHUA_DUNG) {
            result = "Chưa dùng";
        } else if (status == DANG_DUNG) {
            result = "Đang dùng";
        } else if (status == vn.softdreams.ebweb.service.util.Constants.EbUserPackage.HUY) {
            result = "Hủy";
        } else if (status == vn.softdreams.ebweb.service.util.Constants.EbUserPackage.XOA) {
            result = "Xóa";
        }
        return result;
    }

    private void checkChanged(UserDTO userDTO) {
        List<EbLogHistoryDTO> listEbLogHistoryDTO = new ArrayList<>();
        User user1 = userRepository.getOne(userDTO.getId());
        Boolean isChanged = false;
        if (user1.getPackageID() != null && !user1.getPackageID().equals(userDTO.getPackageID())) {
            EbLogHistoryDTO ebLogHistoryDTO = new EbLogHistoryDTO();
            EbPackage ebPackageOld = ebPackageRepository.getOne(user1.getPackageID());
            EbPackage ebPackageNew = ebPackageRepository.getOne(userDTO.getPackageID());
            ebLogHistoryDTO.setId(UUID.randomUUID());
            ebLogHistoryDTO.setUserID(user1.getId());
            ebLogHistoryDTO.setBeforeChange(ebPackageOld.getPackageName());
            ebLogHistoryDTO.setAfterChange(ebPackageNew.getPackageName());
            listEbLogHistoryDTO.add(ebLogHistoryDTO);
            isChanged = true;
        }
        if (user1.getStatus() != null && !user1.getStatus().equals(userDTO.getStatus())) {
            EbLogHistoryDTO ebLogHistoryDTO = new EbLogHistoryDTO();
            ebLogHistoryDTO.setId(UUID.randomUUID());
            ebLogHistoryDTO.setUserID(user1.getId());
            ebLogHistoryDTO.setBeforeChange(getStatus(user1.getStatus()));
            ebLogHistoryDTO.setAfterChange(getStatus(userDTO.getStatus()));
            listEbLogHistoryDTO.add(ebLogHistoryDTO);
            isChanged = true;
        }
        if (user1.getOrgID() != null && !user1.getOrgID().equals(userDTO.getOrgID())) {
            EbLogHistoryDTO ebLogHistoryDTO = new EbLogHistoryDTO();
            OrganizationUnit organizationUnitOld = organizationUnitRepository.getOne(user1.getOrgID());
            OrganizationUnit organizationUnitNew = organizationUnitRepository.getOne(userDTO.getOrgID());
            ebLogHistoryDTO.setId(UUID.randomUUID());
            ebLogHistoryDTO.setUserID(user1.getId());
            ebLogHistoryDTO.setBeforeChange(organizationUnitOld.getOrganizationUnitName());
            ebLogHistoryDTO.setAfterChange(organizationUnitNew.getOrganizationUnitName());
            listEbLogHistoryDTO.add(ebLogHistoryDTO);
            isChanged = true;
        }
        if (isChanged) {
            userRepository.insertEbLogHistory(listEbLogHistoryDTO);
        }
    }

    public Optional<UserDTO> updateInfoAdmin(UserDTO userDTO) {
//        User oldUser = userRepository.findOneById(userDTO.getId());
//        Optional<UserDTO> user;
//        if (oldUser.getLogin() != null &&
//            !oldUser.getLogin().equals(userDTO.getLogin().toLowerCase())
//            && userRepository.countEbUserByLogin(userDTO.getLogin().toLowerCase()) > 0) {
//            user = Stream.of(oldUser).map(UserDTO::new).findAny();
//            user.get().setStatus(1);
//            return user;
//        }
        checkChanged(userDTO);
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserIDAndCompanyID(userDTO.getId(), userDTO.getOrgID());
        if (userDTO.getStatus() == DANG_DUNG) {
            // neu la status = active
            EbPackage ebPackage = ebPackageRepository.getOne(userDTO.getPackageID());
            LocalDate activedDate = LocalDate.now();
            LocalDate expriredDate = activedDate.plusMonths(ebPackage.getExpiredTime());
            userRepository.updateEbUserPackageForActive(userDTO.getPackageID(), userDTO.getStatus(), activedDate, expriredDate,
                ebUserPackage.getId()
            );
        } else {
            userRepository.updateEbUserPackageForInActive(userDTO.getPackageID(), userDTO.getStatus(), ebUserPackage.getId());
        }
        // update userAdmin
        User user = userRepository.findOneById(userDTO.getId());
        setNewUserFCCreateAdmin(user, userDTO);
        User userSaved = userRepository.save(user);
        userSaved.setStatus(0);
        //     ebUserOrganizationUnitRepository.updateOrg(userDTO.getOrgID(), userDTO.getId());
        return Optional.of(userSaved).map(UserDTO::new);
    }

    private void setInfoUser(User user, UserDTO userDTO) {
        user.setEmail(userDTO.getEmail() != null ? userDTO.getEmail().toLowerCase() : null);
        user.setFullName(userDTO.getFullName());
        user.setLogin(userDTO.getLogin().toLowerCase());
        if (userDTO.getIsChangePassword() != null && userDTO.getIsChangePassword()) {
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
        }
        user.setAddress(userDTO.getAddress());
        user.setBirthday(userDTO.getBirthday());
        user.setCountry(userDTO.getCountry());
        user.setCity(userDTO.getCity());
        user.setProvince(userDTO.getProvince());
        user.setIdCard(userDTO.getIdCard());
        user.setDescription(userDTO.getDescription());
        user.setHomePhone(userDTO.getHomePhone());
        user.setFax(userDTO.getFax());
        user.setWorkOnBook(userDTO.getWorkOnBook());
        user.setJob(userDTO.getJob());
        user.setEbGroups(userDTO.getEbGroups());
        user.setMobilePhone(userDTO.getMobilePhone());
//        user.setStatus(userDTO.getStatus());
//        user.setPackageID(userDTO.getPackageID());
//        user.setOrgID(userDTO.getOrgID());
//        if (userDTO.getStatus() == vn.softdreams.ebweb.service.util.Constants.EbUserPackage.DANG_DUNG) {
//            user.setActivedDate(LocalDate.now());
//            EbPackage ebPackage = ebPackageRepository.getOne(user.getPackageID());
//            user.setExpriredDate(LocalDate.now().plusMonths(ebPackage.getExpiredTime()));
//        }
    }

    private void setNewUserFCCreateAdmin(User user, UserDTO userDTO) {
        user.setLogin(userDTO.getLogin());
        user.setJob(userDTO.getJob());
        user.setFullName(userDTO.getFullName());
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        user.setEmail(userDTO.getEmail() != null ? userDTO.getEmail().toLowerCase() : null);
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setBirthday(userDTO.getBirthday());
        user.setCountry(userDTO.getCountry());
        user.setCity(userDTO.getCity());
        user.setProvince(userDTO.getProvince());
        user.setIdCard(userDTO.getIdCard());
        user.setDescription(userDTO.getDescription());
        user.setHomePhone(userDTO.getHomePhone());
        user.setFax(userDTO.getFax());
        user.setMobilePhone(userDTO.getMobilePhone());
//        user.setStatus(userDTO.getStatus());
//        user.setPackageID(userDTO.getPackageID());
//        user.setOrgID(userDTO.getOrgID());
    }

    private void setNewUserFC(User user, UserDTO userDTO) {
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setJob(userDTO.getJob());
        user.setFullName(userDTO.getFullName());
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<EbAuthority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findOneByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param fullName full name of user
     * @param email    email id of user
     * @param langKey  language key
     */
    public void updateUser(String fullName, String email, String langKey) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFullName(fullName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFullName(userDTO.getFullName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<EbAuthority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOneByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void deleteUserAdmin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserID(user.get().getId());
        if (ebUserPackage != null) {
            ebUserPackageRepository.deleteEbUserPackageByUserID(user.get().getId());
            ebUserOrganizationUnitRepository.deleteEbUserOrganizationUnitByUserId(user.get().getId());
        }
        userRepository.deleteByParentID(user.get().getId());
        userRepository.deleteById(user.get().getId());
        this.clearUserCaches(user.get());
        log.debug("Deleted User: {}", user);
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
        Page<UserDTO> tmp = userRepository.findAllByParentIDOrderByLogin(pageable, user.get());
        return tmp;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsersClient(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
        Page<UserDTO> tmp = userRepository.findAllClientByParentIDOrderByLogin(pageable, user.get()).map(UserDTO::new);
        return tmp;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(login);
        Set<EbGroup> ebGroupListOfUser = user.get().getEbGroups();
        for (EbGroup ebGroup : ebGroupListOfUser) {
            // lay ra dong EbUserGroup
            List<EbUserGroup> listUserGr = ebUserGroupRepository.findAllByUserIdAndGroupId(user.get().getId(), ebGroup.getId());
            List<OrgIdAndBookDTO> listOrgIdAndBook = new ArrayList<>();
            // lay ra oject(orgId,workingOnBook) of dong EbUserGroup
            for (EbUserGroup userGroup : listUserGr) {
                OrgIdAndBookDTO orgBook = new OrgIdAndBookDTO();
                // neu co ComId thi add vao ebGroup
                if (userGroup.getCompanyID() != null) {
                    orgBook.setCompanyID(userGroup.getCompanyID());
                    orgBook.setWorkingOnBook(userGroup.getWorkingOnBook());
                    listOrgIdAndBook.add(orgBook);
                }
            }
            // neu co ComId thi add vao ebGroup
            if (listOrgIdAndBook.size() > 0) {
                List<OrgTreeTableDTO> dtoList = this.getAllOuTreeByListOrgId(listOrgIdAndBook);
                ebGroup.setListOrg(dtoList);
            }
        }
        // get EbUserGroup for set workingonbook of user
        List<EbUserGroup> listEbUG = ebUserGroupRepository.
            findAllByUserIdAndCompanyID(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
        String crrBook = listEbUG.size() > 0 ? listEbUG.get(0).getWorkingOnBook().toString() : vn.softdreams.ebweb.service.util.Constants.TypeLedger.FINANCIAL_BOOK.toString();
        user.get().setWorkOnBook(Integer.valueOf(crrBook));
        user.get().setEbGroups(ebGroupListOfUser);

        // get status, packageId, OrgID of ebUserPackage
        if (user.get().getParentID() == null) {
            EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserID(user.get().getId());
            if (ebUserPackage != null) {
                user.get().setEbUserPackage(ebUserPackage);
                user.get().setStatus(ebUserPackage.getStatus());
                user.get().setPackageID(ebUserPackage.getPackageID());
                user.get().setOrgID(ebUserPackage.getCompanyID());
            }
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (currentUserLoginAndOrg.get().getOrg() != null) {
                return userRepository.findOneWithAuthoritiesByLoginAndOrganizationUnitsId(currentUserLoginAndOrg.get().getLogin(),
                    currentUserLoginAndOrg.get().getOrg());
            } else {
                return userRepository.findOneWithAuthoritiesByLogin(currentUserLoginAndOrg.get().getLogin());
            }

        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<UserSystemOption> getUserWithAuthoritiesAndSystemOption() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<User> userOptional = userRepository.findOneByLoginAndOrganizationUnitsId(currentUserLoginAndOrg.get().getLogin(),
                currentUserLoginAndOrg.get().getOrg());
            if (userOptional.isPresent()) {
                List<SystemOption> systemOption = systemOptionRepository.findByUserIdAndCompanyId(userOptional.get().getId(), currentUserLoginAndOrg.get().getOrg());
                return Optional.of(new UserSystemOption(userOptional.get(), systemOption));
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getUserSearch(Pageable pageable, UserSearchDTO userSearchDTO) {
        Page<UserDTO> tmp = userRepository.getListUserSearch(pageable, userSearchDTO);
        return tmp;
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(EbAuthority::getName).collect(Collectors.toList());
    }

    /**
     * @return a list of all the authorities
     */
    public List<EbGroup> getEbGroups() {
        Optional<User> user = getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (user.isPresent() && currentUserLoginAndOrg.isPresent()) {
            return ebGroupRepository.findAllByOrgId(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
//        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    public UserDTO getAccount() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> user = userRepository.findOneByLogin(currentUserLoginAndOrg.get().getLogin());
//        Set<String> authorities = user.get().getAuthorities().stream()
//            .map(EbAuthority::getCode)
//            .collect(Collectors.toSet());
        List<String> authorities = userRepository.getAllAuthoritiesByUserID(user.get().getId());
        if (user.get().getParentID() != null) {
            List<String> listAuth = ebUserGroupRepository.getAuthoritiesByUserIdAndCompanyId(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
            authorities.addAll(listAuth);
        }
        Set<String> auths = new HashSet<>(authorities);
        // check la admin sds
        boolean isAdminSds = ebUserOrganizationUnitRepository.countByUserId(user.get().getId()) == 0;
        // check la admin or user thi gói status = 0 or 1 moi dc đăng nhập
        EbUserPackage ebUserPackage = new EbUserPackage();
        EbPackage ebPackage = new EbPackage();
        OrganizationUnit organizationUnitLogin = new OrganizationUnit();
        Long userId = null;
        UUID orgId = null;
        boolean isAllow = false;
        if (!isAdminSds) {
            // neu la user thi get theo goi cua parent
            userId = user.get().getParentID() != null ? user.get().getParentID() : user.get().getId();
            // neu la chi nhanh thi get theo cong ty tổng
            organizationUnitLogin = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
            if (organizationUnitLogin.getUserID() != null && organizationUnitLogin.getUserID().equals(userId)) {
                // neu cty loai ke toan dich vu
                orgId = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
            } else if (organizationUnitLogin.getUserID() == null) {
                // neu la chi nhanh cua cty dc tao ra tư cty KTDV
                UUID companyIdOfPackage = ebUserPackageRepository.findOneByUserID(userId).getCompanyID();
                OrganizationUnit orgPa = organizationUnitRepository.findByID(organizationUnitLogin.getParentID());
                if (orgPa != null && orgPa.getParentID() != null && orgPa.getParentID().equals(companyIdOfPackage)) {
                    orgId = companyIdOfPackage;
                } else {
                    orgId = organizationUnitLogin.getParentID() != null ? organizationUnitLogin.getParentID() : organizationUnitLogin.getId();
                }
            }
            ebUserPackage = ebUserPackageRepository.findOneByUserIDAndCompanyID(userId, orgId);
            isAllow = ebUserPackage.getStatus() == CHUA_DUNG || ebUserPackage.getStatus() == DANG_DUNG;
        }
        if (user.isPresent() && currentUserLoginAndOrg.isPresent()
                             && currentUserLoginAndOrg.get().getOrg() != null && !isAdminSds && isAllow)
        {
            List<SystemOption> systemOption = systemOptionRepository.findByCompanyId(currentUserLoginAndOrg.get().getOrg());
            String currentBook = organizationUnitRepository.findCurrentBook(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
            OrganizationUnit organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg()).get();
//            OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportRepository.findById(organizationUnit.getId()).get();
            systemOption.stream().filter(item -> item.getCode().equalsIgnoreCase(vn.softdreams.ebweb.service.util.Constants.SystemOption.PHIEN_SoLamViec)).findFirst().ifPresent(item -> {
                item.setData(currentBook);
            });
            ebPackage = ebPackageRepository.findOneByOrgIdAndUserId(userId, orgId);
            ebPackage.setIsTotalPackage(ebUserPackage.getIsTotalPackage());
            UserDTO userDTO = new UserDTO();
            userDTO = new UserDTO(user.get(), systemOption, organizationUnit, ebPackage, ebUserPackage, auths);
//            if (ebPackage.getComType() == COMTYPE_3_SERVICEACC && userDTO.getAuthorities().contains(AuthoritiesConstants.ADMIN)) {
//                if (organizationUnitLogin.getUserID() == null) {
//                    userDTO.getAuthorities().removeIf(x -> x.equals(AuthoritiesConstants.ADMIN));
//                    userDTO.getAuthorities().add(AuthoritiesConstants.ROLE_MGT);
//                }
//            } else {
//                userDTO.getAuthorities().add(AuthoritiesConstants.ROLE_REPORT);
//            }
            userDTO.getAuthorities().add(AuthoritiesConstants.ROLE_REPORT);
            if (user.get().getParentID() == null) {
                userDTO.getAuthorities().add(AuthoritiesConstants.ROLE_PERMISSION);
            }
            return userDTO;
        } else if (authorities.contains(AuthoritiesConstants.ADMIN) && isAdminSds) {
            UserDTO userDTO = new UserDTO(user.get(), null, null, null, null, auths);
            return new UserDTO(user.get(), null, null, null, null, auths);
        } else {
            return null;
        }
    }

    public String updateSession(String currentBook, UUID org) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<String> authorities = getAuthorities();
        if (currentUserLoginAndOrg.isPresent()) {
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(org);
            UUID orgGetData;
            if (organizationUnit.getUnitType() == 1) {
                orgGetData = organizationUnit.getParentID();
            } else {
                orgGetData = organizationUnit.getId();
            }
            organizationUnitRepository.updateCurrentBook(currentUserLoginAndOrg.get().getOrg(), currentBook);
            return tokenProvider.createToken(currentUserLoginAndOrg.get().getLogin(), authorities, org, orgGetData);
        }
        return null;
    }

        public ChangeSessionDTO preLogin(@Valid LoginVM loginVM) {
            Optional<User> oneByLogin = userRepository.findOneByLogin(loginVM.getUsername());
            if (oneByLogin.isPresent() && oneByLogin.get().getActivated() && passwordEncoder.matches(loginVM.getPassword(), oneByLogin.get().getPassword())) {
                ChangeSessionDTO dto = new ChangeSessionDTO();
                if (oneByLogin.get().getParentID() == null) {
                    EbUserPackage ebUserPackage = ebUserPackageRepository.findComIDByUserID(oneByLogin.get().getId());
                    List<TreeGetOrganizationUnitDTO> orgs = getTreeOrganizationUnitLogin(ebUserPackage.getCompanyID());
                    dto.setOrgTrees(orgs);
                    return dto;
                } else {
                    List<OrganizationUnitCustomDTO> orgs = organizationUnitRepository.findAllByListCompanyID(oneByLogin.get().getId());
                    List<TreeGetOrganizationUnitDTO> organizationUnitDTOS = new ArrayList<>();
                    Map<OrganizationUnit, TreeOrganizationUnitDTO> tree = new HashMap<>();
                    for (OrganizationUnitCustomDTO org : orgs) {
                        // Trường hợp là tổng công ty
//                    if (tree.get(org.getParentID()) != null) {
//                        tree.get(org.getParentID()).getChildren().add(new OrgTreeDTO(org.getId(), org.getOrganizationUnitName(), new ArrayList<>()));
//                    } else {
                        TreeGetOrganizationUnitDTO treeOrganizationUnitDTO = new TreeGetOrganizationUnitDTO();
                        treeOrganizationUnitDTO.setParent(org);
                        organizationUnitDTOS.add(treeOrganizationUnitDTO);
//                    }
                    }
                    dto.setOrgTrees(organizationUnitDTOS);
                    return dto;
                }
            }
            throw new UserNotFoundException();
    }

    public List<TreeGetOrganizationUnitDTO> getTreeOrganizationUnitLogin(UUID id) {
        OrganizationUnitCustomDTO organizationUnit = organizationUnitRepository.findByIDCustom(id);
        if (organizationUnit != null) {
            List<TreeGetOrganizationUnitDTO> treeOrganizationUnitDTOList = new ArrayList<TreeGetOrganizationUnitDTO>();
            treeOrganizationUnitDTOList.add(new TreeGetOrganizationUnitDTO());
            treeOrganizationUnitDTOList.get(0).setParent(organizationUnit);
            treeOrganizationUnitDTOList.get(0).setValue(organizationUnit.getId());
            treeOrganizationUnitDTOList.get(0).setText(organizationUnit.getOrganizationUnitName());
            return recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOList);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<TreeGetOrganizationUnitDTO> recursiveTreeOrganizationUnitByParentID(List<TreeGetOrganizationUnitDTO> treeOrganizationUnitDTOS) {
        for (int i = 0; i < treeOrganizationUnitDTOS.size(); i++) {
            List<OrganizationUnitCustomDTO> organizationUnitList = organizationUnitRepository.findByParentIDCustomLogin(treeOrganizationUnitDTOS.get(i).getParent().getId());
            for (int j = 0; j < organizationUnitList.size(); j++) {
                if (j == 0) {
                    treeOrganizationUnitDTOS.get(i).setChildren(new ArrayList<TreeGetOrganizationUnitDTO>());
                }
                treeOrganizationUnitDTOS.get(i).getChildren().add(new TreeGetOrganizationUnitDTO());
                treeOrganizationUnitDTOS.get(i).getChildren().get(j).setParent(organizationUnitList.get(j));
                treeOrganizationUnitDTOS.get(i).getChildren().get(j).setValue(organizationUnitList.get(j).getId());
                treeOrganizationUnitDTOS.get(i).getChildren().get(j).setText(organizationUnitList.get(j).getOrganizationUnitName());
            }
            if (treeOrganizationUnitDTOS.get(i).getChildren() != null && treeOrganizationUnitDTOS.get(i).getChildren().size() > 0) {
                recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOS.get(i).getChildren());
            }
        }
        return treeOrganizationUnitDTOS;
    }

    public List<OrgTreeTableDTO> getAllOuTreeByListOrgId(List<OrgIdAndBookDTO> listOrgIdAndBook) {
        List<OrganizationUnit> orgs = new ArrayList<>();
        List<UUID> listID = listOrgIdAndBook.stream().map(OrgIdAndBookDTO::getCompanyID).collect(Collectors.toList());
        listID.stream()
            .map(organizationUnitRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(orgs::add);
        List<OrgTreeTableDTO> dtoList = new ArrayList<>();
        for (OrganizationUnit org : orgs) {
            OrgTreeTableDTO dto = new OrgTreeTableDTO();
            dto.setValue(org.getId());
            dto.setText(org.getOrganizationUnitName());
            dto.setUnitType(org.getUnitType());
            Integer book = listOrgIdAndBook.stream().
                filter(x -> x.getCompanyID().equals(org.getId())).findFirst().get().getWorkingOnBook();
            dto.setWorkingOnBook(book.toString());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public UserDTO resetPassword(String email) {
        User user = userRepository.findOneByEmail(email);
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            String pass = RandomStringUtils.randomAlphanumeric(6);
            user.setPassword(passwordEncoder.encode(pass));
            userRepository.save(user);
            mailService.sendEmailFromTemplateCustom(user, pass, "mail/passwordResetByEmail", "email.resetPassword.title");
            userDTO = Stream.of(user).map(UserDTO::new).findAny().get();
            return userDTO;
        }
        return null;
    }

    public void resetPasswordExpired(String login) {
        User user = userRepository.findOneByLoginReset(login);
        String pass = RandomStringUtils.randomAlphanumeric(6);
        user.setPassword(passwordEncoder.encode(pass));
        userRepository.save(user);
    }

    public CRMUserRespDTO getCrmUserResponse(CRMUserDTO crmUserDTO) {
        CRMUserRespDTO crmUserRespDTO = new CRMUserRespDTO();
        User user = new User();
        OrganizationUnit organizationUnit = new OrganizationUnit();
        // set value
//        String login = getLoginByFullName(crmUserDTO.getFullName());
        // set for insert user
//        if (userRepository.countEbUserByLogin(login) > 0) {
//            String loginUser = userRepository.getLoginUserByLogin(login);
//            int stt = 1;
//            if (loginUser != null) {
//                stt = Integer.parseInt(loginUser.substring(login.length())) + 1;
//            }
//            login += stt;
//        }
        if (!Strings.isNullOrEmpty(crmUserDTO.getCompanyTaxCode()) && organizationUnitRepository.countTaxCode(crmUserDTO.getCompanyTaxCode()) > 0) {
            crmUserRespDTO.setStatus(false);
            crmUserRespDTO.setSystemCode(Fail_TaxCodeExist);
            return crmUserRespDTO;
        }
        if (ebPackageRepository.countPackageCode(crmUserDTO.getServicePackage()) == 0) {
            crmUserRespDTO.setStatus(false);
            crmUserRespDTO.setSystemCode(Fail_EbPackageInvalid);
            return crmUserRespDTO;
        }
        if (userRepository.countEbUserByLogin(crmUserDTO.getEmail()) > 0) {
            // gui response
            crmUserRespDTO.setStatus(false);
            crmUserRespDTO.setSystemCode(Fail_EmailExist);
            return crmUserRespDTO;
        }
        // neu la contact moi
        user.setLogin(crmUserDTO.getEmail());
        // set pass
        String pass = RandomStringUtils.randomAlphanumeric(6);
        user.setPassword(passwordEncoder.encode(pass));
        user.setFullName(crmUserDTO.getFullName());
        user.setEmail(crmUserDTO.getEmail());
        user.setMobilePhone(crmUserDTO.getPhoneNumber());
        user.setCreatedDate(Instant.now());
        setValueUserCrm(user);
        // goi
        EbPackage ebPackage = ebPackageRepository.findOneByPackageCode(crmUserDTO.getServicePackage());
//        user.setPackageID(ebPackage.getId());
        // set for insert company
        organizationUnit.setOrganizationUnitName(crmUserDTO.getCompanyName());
        if (Strings.isNullOrEmpty(crmUserDTO.getCompanyTaxCode())) {
            generateOrganizationUnitCode(organizationUnit);
        } else {
            organizationUnit.setOrganizationUnitCode(crmUserDTO.getCompanyTaxCode());
        }
        organizationUnit.settaxCode(crmUserDTO.getCompanyTaxCode());
        organizationUnit.setAccountingType(0);
        organizationUnit.setUnitType(0);
        organizationUnit.setGrade(1);
        organizationUnit.setIsActive(true);
        organizationUnit.setParentNode(false);
        organizationUnit.setId(UUID.randomUUID());
        // insert data kế toán mặc định
        organizationUnit.setAccountingType(0);
        organizationUnit.setFinancialYear(LocalDate.now().getYear());
        organizationUnit.setFromDate(LocalDate.now().with(firstDayOfYear()));
        organizationUnit.setToDate(LocalDate.now().with(lastDayOfYear()));
        organizationUnit.setStartDate(LocalDate.now().with(firstDayOfYear()));
        organizationUnit.setCurrencyID("VND");
        organizationUnit.setTaxCalculationMethod(0);
        // lưu DB
        User userSaved = userRepository.save(user);
        // insert EbUserAuthority
        userRepository.insertEbUserAuthorities(userSaved.getId());
        OrganizationUnit orgUnitSaved = organizationUnitRepository.save(organizationUnit);
        // insert mac dinh bao cao option
        OrganizationUnitOptionReport organizationUnitOptionReport = new OrganizationUnitOptionReport();
        organizationUnitOptionReport.setHeaderSetting(0);
        organizationUnitOptionReport.setIsDisplayNameInReport(true);
        organizationUnitOptionReport.setIsDisplayAccount(true);
        organizationUnitOptionReport.setOrganizationUnitID(orgUnitSaved.getId());
        organizationUnitOptionReportRepository.save(organizationUnitOptionReport);
        // insert ebUserOrganizationUnit, userpackage
        ebUserOrganizationUnitRepository.insertUserAndOrg(userSaved.getId(), orgUnitSaved.getId());
        // insert ebUserPackage
        EbUserPackage ebUserPackage = new EbUserPackage();
        ebUserPackage.setCompanyID(orgUnitSaved.getId());
        ebUserPackage.setUserID(userSaved.getId());
        ebUserPackage.setPackageID(ebPackage.getId());
        ebUserPackage.setStatus(0);
        ebUserPackage.setIsTotalPackage(false);
        ebUserPackage.setActivedDate(LocalDate.parse(crmUserDTO.getStartDate()));
        if (!Strings.isNullOrEmpty(crmUserDTO.getEndDate())) {
            ebUserPackage.setExpriredDate(LocalDate.parse(crmUserDTO.getEndDate()));
        }
        ebUserPackageRepository.save(ebUserPackage);
        // insert Data, report
        organizationUnitRepository.insertDataCategoryToDB(orgUnitSaved.getId(), "VND", 0,null);
        // gửi mail KH
        String title = Email_Title + crmUserDTO.getCompanyName() + " - " + crmUserDTO.getCompanyTaxCode();
        mailService.sendEmailFromTemplateForInitCRM(userSaved, pass, "mail/activationEmail", title);
        // gửi response
        crmUserRespDTO.setStatus(false);
        crmUserRespDTO.setSystemCode(Done);
        crmUserRespDTO.setCompanyTaxCode(crmUserDTO.getCompanyTaxCode());
        return crmUserRespDTO;
    }

    private void generateOrganizationUnitCode(OrganizationUnit organizationUnit) {
        String companyCode = Ma_Cong_Ty;
        if (organizationUnitRepository.countByIsActiveTrueAndOrganizationUnitCodeStartingWithIgnoreCase(companyCode) == 0) {
            companyCode += "01";
            organizationUnit.setOrganizationUnitCode(companyCode);
        } else {
            OrganizationUnit orgU =
                organizationUnitRepository.
                    findFirstByIsActiveTrueAndOrganizationUnitCodeStartingWithIgnoreCaseOrderByOrganizationUnitCodeDesc(companyCode);
            if (orgU != null) {
                String strNumber = orgU.getOrganizationUnitCode().substring(companyCode.length());
                int num = Integer.parseInt(strNumber) + 1;
                if (num < 10) {
                    companyCode += "0" + num;
                } else {
                    companyCode += "" + num;
                }
                organizationUnit.setOrganizationUnitCode(companyCode);
            }
        }
    }

    private String getLoginByFullName(String name) {
        // remove accent
        String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String newName = pattern.matcher(temp).replaceAll("").
            replaceAll("Đ", "D").replace("đ", "").toLowerCase();
        // split to array
        String[] arrName = newName.trim().split(" ");
        StringBuilder login = new StringBuilder(arrName[arrName.length - 1]);
        for (int i = 0; i < arrName.length - 1; i++) {
            login.append(arrName[i].charAt(0));
        }
        return login.toString();
    }

    private void setValueUserCrm(User user) {
        user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setIsSystem(true);
        Set<EbAuthority> authorities = new HashSet<>();
        authorityRepository.findOneByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
//        authorityRepository.findOneByName(AuthoritiesConstants.ADMIN).ifPresent(authorities::add);
        user.setAuthorities(authorities);
    }

    public Boolean checkAuthentication(String username, String password, String token) {
        String encryptedMD5 = Common.getMd5(username + password);
        return token.equals(encryptedMD5 + vn.softdreams.ebweb.service.util.Constants.Salt.value);
    }

    // gia han
    public CRMUserRespDTO extensionPackage(EbUserPackage ebUserPackage, String renewEndDate) {
        CRMUserRespDTO result = new CRMUserRespDTO();
        ebUserPackage.setExpriredDate(LocalDate.parse(renewEndDate));
        ebUserPackageRepository.save(ebUserPackage);
        result.setSystemCode(Done);
        return result;
    }

    // nang cap
    public CRMUserRespDTO upgradePackage(EbUserPackage ebUserPackage, String servicePackage, String renewEndDate) {
        CRMUserRespDTO result = new CRMUserRespDTO();
        EbPackage ebPackageNew = ebPackageRepository.findOneByPackageCode(servicePackage);
        ebUserPackage.setPackageID(ebPackageNew.getId());
        ebUserPackage.setExpriredDate(LocalDate.parse(renewEndDate));
        ebUserPackageRepository.save(ebUserPackage);
        result.setSystemCode(Done);
        return result;
    }

    // update status
    public CRMUserRespDTO updateStatusUser(List<CRMUserStatusDTO> cRMUserStatusDTOs) {
        CRMUserRespDTO result = new CRMUserRespDTO();
        List<User> userList = new ArrayList<>();
        List<EbUserPackage> ebUserPackages = new ArrayList<>();
        List<OrganizationUnit> organizationUnits = new ArrayList<>();
        for (CRMUserStatusDTO cRMUserStatusDTO : cRMUserStatusDTOs) {
            User user = userRepository.findOneByLogin(cRMUserStatusDTO.getEmail()).get();
            user.setActivated(false);
            userList.add(user);
            EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserID(user.getId());
            ebUserPackage.setStatus(HUY);
            ebUserPackages.add(ebUserPackage);
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(ebUserPackage.getCompanyID());
            organizationUnit.setIsActive(false);
            organizationUnits.add(organizationUnit);
        }
        if (Integer.parseInt(cRMUserStatusDTOs.get(0).getStatus()) == Cancel) {
            userRepository.saveAll(userList);
            ebUserPackageRepository.saveAll(ebUserPackages);
            organizationUnitRepository.saveAll(organizationUnits);
        }
        result.setSystemCode(Done);
        return result;
    }
    // xóa User, Company, Package
    public CRMUserRespDTO resetUserFromCRM(CRMUserResetDTO crmUserResetDTO) {
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (userRepository.countEbUserByLogin(crmUserResetDTO.getEmail()) == 0) {
            result.setSystemCode(Fail_EmailNotFound);
            return result;
        }
        User user = userRepository.findOneByLogin(crmUserResetDTO.getEmail()).get();
        // xoa UserPackageCompany
        userRepository.deleteUserPackageCompany(user.getId());
        result.setSystemCode(Done);
        return result;
    }

    public Boolean checkAuthenticationNewUser(CRMUserDTO crmUserDTO) {
        String md5Encrypt = Common.getMd5(crmUserDTO.getCompanyTaxCode() + crmUserDTO.getStartDate() +
                                                crmUserDTO.getEndDate() + vn.softdreams.ebweb.service.util.Constants.Salt.value);
        return md5Encrypt.equals(crmUserDTO.getHash());
    }

    public Boolean checkAuthenticationEBPack(CRMEbPackageDTO cRMEbPackageDTO) {
        String md5Encrypt = Common.getMd5(cRMEbPackageDTO.getLimitedVoucher() +
                                                vn.softdreams.ebweb.service.util.Constants.Salt.value);
        return md5Encrypt.equals(cRMEbPackageDTO.getHash());
    }

    public Boolean checkIsAuthentication(String preData, String hashData) {
        String md5Encrypt = Common.getMd5(preData + vn.softdreams.ebweb.service.util.Constants.Salt.value);
        return md5Encrypt.equals(hashData);
    }

    public EbUserPackage findOneUserPackageByLogin(String email) {
        User user = userRepository.findOneByLogin(email).get();
        return ebUserPackageRepository.findOneByUserID(user.getId());
    }

    public Boolean isExtension(String servicePackage, String email) {
        User user = userRepository.findOneByLogin(email).get();
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserID(user.getId());
        return ebPackageRepository.findOneByPackageCode(servicePackage) == ebPackageRepository.findOneByID(ebUserPackage.getPackageID());
    }

    public int getCurrentBookOfUser() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        User user = userRepository.findOneByLogin(currentUserLoginAndOrg.get().getLogin()).get();
        List<EbUserGroup> ebUserGroupList = ebUserGroupRepository.findAllByUserIdAndCompanyID(user.getId(), currentUserLoginAndOrg.get().getOrg());
        int rs = ebUserGroupList.get(0).getWorkingOnBook();
        return ebUserGroupList.get(0).getWorkingOnBook();
    }
}
