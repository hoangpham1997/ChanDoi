package vn.softdreams.ebweb.service.impl;

import org.jfree.util.UnitType;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.scanner.Constant;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EbPackageService;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.web.rest.dto.ChangeSessionDTO;
import vn.softdreams.ebweb.web.rest.dto.ChangeSessionTableDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeTableDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.ComTypeForPackage.KE_TOAN_DICH_VU;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDSOQUANTRI;
import static vn.softdreams.ebweb.service.util.Constants.TCKHAC_SDSOQUANTRI.*;

/**
 * Service Implementation for managing OrganizationUnit.
 */
@Service
@Transactional
public class OrganizationUnitServiceImpl implements OrganizationUnitService {

    private final Logger log = LoggerFactory.getLogger(OrganizationUnitServiceImpl.class);

    private final OrganizationUnitRepository organizationUnitRepository;
    private final EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository;
    private final EbGroupRepository ebGroupRepository;
    private final EbUserGroupRepository ebUserGroupRepository;
    private final OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository;
    private final EbUserPackageRepository ebUserPackageRepository;
    private final GeneralLedgerRepository generalLedgerRepository;

    private final UserService userService;
    private final UserRepository userRepository;
    private final EbPackageService ebPackageService;
    private final EbPackageRepository ebPackageRepository;
    private final SystemOptionRepository systemOptionRepository;

    public OrganizationUnitServiceImpl(OrganizationUnitRepository organizationUnitRepository,
                                       OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository,
                                       UserService userService, UserRepository userRepository,
                                       EbGroupRepository ebGroupRepository, EbUserGroupRepository ebUserGroupRepository,
                                       EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository,
                                       EbUserPackageRepository ebUserPackageRepository,
                                       EbPackageService ebPackageService,
                                       SystemOptionRepository systemOptionRepository,
                                       EbPackageRepository ebPackageRepository, GeneralLedgerRepository generalLedgerRepository
    ) {
        this.organizationUnitRepository = organizationUnitRepository;
        this.organizationUnitOptionReportRepository = organizationUnitOptionReportRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.ebGroupRepository = ebGroupRepository;
        this.ebUserGroupRepository = ebUserGroupRepository;
        this.ebUserOrganizationUnitRepository = ebUserOrganizationUnitRepository;
        this.ebPackageService = ebPackageService;
        this.ebUserPackageRepository = ebUserPackageRepository;
        this.ebPackageRepository = ebPackageRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.generalLedgerRepository = generalLedgerRepository;
    }

    /**
     * Save a organizationUnit.
     *
     * @param organizationUnit the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationUnitSaveDTO save(OrganizationUnit organizationUnit) {
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnit orgLogin = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
        OrganizationUnit organizationUnit1 = new OrganizationUnit();
        List<OrganizationUnit> organizationUnitChild = new ArrayList<OrganizationUnit>();
        List<OrganizationUnit> organizationUnitParent = new ArrayList<OrganizationUnit>();
        OrganizationUnitSaveDTO organizationUnitSaveDTO = new OrganizationUnitSaveDTO();
        if (currentUserLoginAndOrg.isPresent()) {
            OrganizationUnit organizationUnitCurrent = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg()).get();
            UserDTO userLogined = userService.getAccount();
            EbPackage ebPackage = ebPackageRepository.findEbPackageByUser(userLogined.getLogin());
            Integer checkPackage = ebPackageService.findOneByOrgIdAndUserId(ebPackage.getComType());
            if (organizationUnit.getId() == null) {
                if (ebPackage.getComType() == Constants.EbPackage.COMTYPE_1_NOBRANCH) {
                    if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH ||
                        organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY) {
                        throw new BadRequestAlertException("Vượt quá số lượng công ty cho phép", "", "exceedsAmountCompany");
                    }
                }
                if (ebPackage.getComType() == Constants.EbPackage.COMTYPE_2_HASBRANCH) {
                    if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY) {
                        throw new BadRequestAlertException("Vượt quá số lượng công ty cho phép", "", "exceedsAmountCompany");
                    }
                    if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH) {
                        if (checkPackage == Constants.EbPackage.HET_PACKAGE) {
                            throw new BadRequestAlertException("Vượt quá số lượng công ty cho phép", "", "exceedsAmountCompany");
                        } else if (checkPackage == Constants.EbPackage.NULL_PACKAGE) {
                            throw new BadRequestAlertException("Đơn vị/ Tổ chức chưa đăng ký gói dịch vụ! Vui lòng liên hệ với đơn vị cung cấp giải pháp phần mềm Kế toán."
                                , "", "isBuyPackage");
                        }
                    }
                }
                if (ebPackage.getComType() == Constants.EbPackage.COMTYPE_3_SERVICEACC) {
                    if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH ||
                        organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY) {
                        if (checkPackage == Constants.EbPackage.HET_PACKAGE) {
                            throw new BadRequestAlertException("Vượt quá số lượng công ty cho phép", "", "exceedsAmountCompany");
                        } else if (checkPackage == Constants.EbPackage.NULL_PACKAGE) {
                            throw new BadRequestAlertException("Đơn vị/ Tổ chức chưa đăng ký gói dịch vụ! Vui lòng liên hệ với đơn vị cung cấp giải pháp phần mềm Kế toán."
                                , "", "isBuyPackage");
                        }
                    }
                }
            }

            // trường hợp admin, goi tổng
            if (organizationUnit.getUnitType() == 0) {
                if (organizationUnit.getId() != null) {
                    OrganizationUnit organizationUnitOld = organizationUnitRepository.findById(organizationUnit.getId()).get();
                    if (organizationUnit.getStartDate() != null && organizationUnitOld.getStartDate() != null && !organizationUnit.getStartDate().equals(organizationUnitOld.getStartDate())) {
                        int countOPAccount = generalLedgerRepository.countAllOPN(currentUserLoginAndOrg.get().getOrg());
                        if (countOPAccount > 0) {
                            throw new BadRequestAlertException("Đã phát sinh số dư đầu kỳ của niên độ kế toán hiện tại, không thể sửa ngày bắt đầu hạch toán trên phần mềm này!", "", "existOPAccount");
                        }
                    }
                }
                // check đã phát sinh số dư đầu kỳ hay chưa để thay đổi ngày bắt đầu hạch toán ebw-4593
                if (userLogined.getEbPackage() != null) {
                    if (userLogined.getEbPackage().getComType() == KE_TOAN_DICH_VU) {
                        if (organizationUnit.getId() == null) {
                            organizationUnit.setUserID(userLogined.getId());
                        }
                    }
                }
            }
            if (organizationUnit.getParentID() != null) {
                if (Boolean.TRUE.equals(organizationUnit.getIsActive())) {
                    getListParentOrganizationUnit(organizationUnitParent, organizationUnit.getParentID());
                    for (int i = 0; i < organizationUnitParent.size(); i++) {
                        organizationUnitParent.get(i).setIsActive(organizationUnit.getIsActive());
                    }
                }
            }
            if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH && organizationUnit.getParentID() != null) {
                OrganizationUnit tCT = organizationUnitRepository.findByID(organizationUnit.getParentID());
                organizationUnit.setTaxCalculationMethod(tCT.getTaxCalculationMethod());
                organizationUnit.setGoodsServicePurchaseID(tCT.getGoodsServicePurchaseID());
                organizationUnit.setCareerGroupID(tCT.getCareerGroupID());
            }
            if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY && organizationUnit.getId() != null) {
                List<OrganizationUnit> organizationUnits = new ArrayList<>();
                organizationUnits.add(organizationUnit);
                getListChildOrganizationUnitUnitType1(organizationUnits, organizationUnit.getId());
                if (organizationUnit.getTaxCalculationMethod() != null && organizationUnit.getTaxCalculationMethod() == 0) {
                    for (int i = 0; i < organizationUnits.size(); i++) {
                        organizationUnits.get(i).setGoodsServicePurchaseID(organizationUnit.getGoodsServicePurchaseID());
                        organizationUnits.get(i).setCareerGroupID(null);
                        organizationUnits.get(i).setTaxCalculationMethod(organizationUnit.getTaxCalculationMethod());
                    }
                } else if (organizationUnit.getTaxCalculationMethod() != null && organizationUnit.getTaxCalculationMethod() == 1) {
                    for (int i = 0; i < organizationUnits.size(); i++) {
                        organizationUnits.get(i).setGoodsServicePurchaseID(null);
                        organizationUnits.get(i).setCareerGroupID(organizationUnit.getCareerGroupID());
                        organizationUnits.get(i).setTaxCalculationMethod(organizationUnit.getTaxCalculationMethod());
                    }
                }
                organizationUnitRepository.saveAll(organizationUnits);
            }
            if (organizationUnit.getId() == null) {
                organizationUnit.setIsActive(true);
                long count = 0;
                if (organizationUnit.getUnitType() > 0) {
                    if (organizationUnit.getParentID().equals(currentUserLoginAndOrg.get().getOrg())) {
                        count = organizationUnitRepository.checkExist(organizationUnit.getOrganizationUnitCode(), currentUserLoginAndOrg.get().getOrg());
                    } else {
                        count = organizationUnitRepository.checkExist(organizationUnit.getOrganizationUnitCode(), organizationUnit.getParentID());
                    }
                } else if (organizationUnit.getUnitType() == 0) {
                    count = organizationUnitRepository.checkExistIsTotalPackage(organizationUnit.getOrganizationUnitCode(), userLogined.getId().intValue(), currentUserLoginAndOrg.get().getOrg());
                }
                if (count > 0) {
//                    organizationUnitSaveDTO.setOrganizationUnit(organizationUnit);
//                    organizationUnitSaveDTO.setStatus(1);
//                    return organizationUnitSaveDTO;
                    throw new BadRequestAlertException("Mã cơ cấu tổ chức đã tồn tại, vui lòng kiểm tra lại!", "", "existOrganizationUnit");
                } else {
                    organizationUnit.setId(UUID.randomUUID());
                    Optional<OrganizationUnit> parentOrganizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
                    organizationUnit.setCurrencyID(parentOrganizationUnit.get().getCurrencyID());
                    if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY || (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH)) {
                        organizationUnitRepository.insertDataCategoryToDB(organizationUnit.getId(), organizationUnit.getCurrencyID(), organizationUnit.getUnitType(), organizationUnit.getParentID());
                    }
                    organizationUnit.setIsParentNode(false);
                    if (organizationUnit.getParentID() == null) {
                        organizationUnit.setGrade(1);
//                        organizationUnit.setParentID(currentUserLoginAndOrg.get().getOrg());
                    } else {
                        Optional<OrganizationUnit> organizationUnit2 = organizationUnitRepository.findById(organizationUnit.getParentID());
                        organizationUnit2.get().setIsParentNode(true);
                        organizationUnit.setGrade(organizationUnit2.get().getGrade() + 1);
                        organizationUnit.setIsParentNode(false);
                        organizationUnitRepository.save(organizationUnit2.get());
                    }
                    // Tạo user bên EbUserOrganization Unit
                    UserDTO user = userService.getAccount();
                    EbUserOrganizationUnitDTO ebUserOrganizationUnitDTO = new EbUserOrganizationUnitDTO();
                    ebUserOrganizationUnitDTO.setUserID(user.getId());
                    ebUserOrganizationUnitDTO.setOrgID(organizationUnit.getId());
                    ebUserOrganizationUnitDTO.setCurrentBook("0");
                    List<EbUserOrganizationUnitDTO> ebUserOrganizationUnitDTOS = new ArrayList<EbUserOrganizationUnitDTO>();
                    ebUserOrganizationUnitDTOS.add(ebUserOrganizationUnitDTO);
                    userRepository.insertEbUserOrganizationUnit(ebUserOrganizationUnitDTOS);
                    //
                    if (organizationUnit.getUnitType() == 0 || organizationUnit.getUnitType() == 1) {
                        organizationUnit.getOrganizationUnitOptionReport().setOrganizationUnitID(organizationUnit.getId());
                        organizationUnitOptionReportRepository.save(organizationUnit.getOrganizationUnitOptionReport());
                    }
                    organizationUnit.setCompanyID(null);
                    organizationUnitRepository.save(organizationUnit);
                    organizationUnit1 = organizationUnitRepository.save(organizationUnit);
                    organizationUnitSaveDTO.setOrganizationUnit(organizationUnit1);
                    organizationUnitSaveDTO.setStatus(0);
                    return organizationUnitSaveDTO;
                }
            } else {
                getListChildOrganizationUnit(organizationUnitChild, organizationUnit.getId());
                for (int i = 0; i < organizationUnitChild.size(); i++) {
                    organizationUnitChild.get(i).setIsActive(organizationUnit.getIsActive());
                }
                if (organizationUnit.getParentID() == null) {
                    organizationUnit.setGrade(1);
                    if (!organizationUnit.getId().equals(currentUserLoginAndOrg.get().getOrg()) && organizationUnit.getUnitType() > 0) {
                        organizationUnit.setParentID(currentUserLoginAndOrg.get().getOrg());
                    }
                } else {
                    Optional<OrganizationUnit> organizationUnit2 = organizationUnitRepository.findById(organizationUnit.getParentID());
                    organizationUnit2.get().setIsParentNode(true);
                    organizationUnit.setGrade(organizationUnit2.get().getGrade() + 1);
                    List<OrganizationUnit> organizationUnitList = organizationUnitRepository.findByParentID(organizationUnit.getId());
                    if (organizationUnitList.size() == 0) {
                        organizationUnit.setIsParentNode(false);
                    }
                    organizationUnitRepository.save(organizationUnit2.get());
                }
                organizationUnitRepository.saveAll(organizationUnitChild);
                organizationUnit.setCompanyID(null);
                organizationUnit1 = organizationUnitRepository.save(organizationUnit);
                organizationUnitSaveDTO.setOrganizationUnit(organizationUnit1);
                organizationUnitSaveDTO.setStatus(0);
                organizationUnitOptionReportRepository.save(organizationUnit.getOrganizationUnitOptionReport());

                return organizationUnitSaveDTO;
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public OrganizationUnitSaveDTO saveBigOrg(OrganizationUnit organizationUnit) {
        OrganizationUnit oldOrganizationUnit = new OrganizationUnit();
        oldOrganizationUnit = organizationUnitRepository.findByID(organizationUnit.getId());
        if (organizationUnit.getId() == null) {
            if (organizationUnitRepository.countAllByOrganizationUnitCode(organizationUnit.getOrganizationUnitCode()) > 0) {
                OrganizationUnitSaveDTO organizationUnitSaveDTO = new OrganizationUnitSaveDTO();
                organizationUnitSaveDTO.setOrganizationUnit(organizationUnit);
                organizationUnitSaveDTO.setStatus(1);
                return organizationUnitSaveDTO;
            }
        } else {
            if (oldOrganizationUnit.getOrganizationUnitCode() != null &&
                !oldOrganizationUnit.getOrganizationUnitCode().equals(organizationUnit.getOrganizationUnitCode()) &&
                organizationUnitRepository.countAllByOrganizationUnitCode(organizationUnit.getOrganizationUnitCode()) > 0) {
                OrganizationUnitSaveDTO organizationUnitSaveDTO = new OrganizationUnitSaveDTO();
                organizationUnitSaveDTO.setOrganizationUnit(organizationUnit);
                organizationUnitSaveDTO.setStatus(1);
                return organizationUnitSaveDTO;
            }
        }
        if (organizationUnit.getId() == null) {
            organizationUnit.setId(UUID.randomUUID());
            organizationUnit.setGrade(1);
            organizationUnit.setIsParentNode(false);
            if (organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY || organizationUnit.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH) {
                organizationUnitRepository.insertDataCategoryToDB(organizationUnit.getId(), organizationUnit.getCurrencyID(), 0, organizationUnit.getParentID());
            }
            organizationUnit.getOrganizationUnitOptionReport().setOrganizationUnitID(organizationUnit.getId());
            organizationUnitOptionReportRepository.save(organizationUnit.getOrganizationUnitOptionReport());
        }
//        EbUserPackage ebUserPackage1 = ebUserPackageRepository.findOneByCompanyID(organizationUnit.getId());
//        if (ebUserPackage1 == null) {
//            EbUserPackage ebUserPackage = new EbUserPackage();
//            ebUserPackage.setId(UUID.randomUUID());
//            ebUserPackage.setCompanyID(organizationUnit.getId());
//            ebUserPackage.setPackageID(organizationUnit.getPackageID());
//            ebUserPackage.setStatus(organizationUnit.getStatus());
//            ebUserPackageRepository.save(ebUserPackage);
//        }
        OrganizationUnit organizationUnit1 = organizationUnitRepository.save(organizationUnit);
        OrganizationUnitSaveDTO organizationUnitSaveDTO = new OrganizationUnitSaveDTO();
        organizationUnitSaveDTO.setOrganizationUnit(organizationUnit1);
        organizationUnitSaveDTO.setStatus(0);
        return organizationUnitSaveDTO;
    }

    // De quy lay ra tat ca co cau con cua co cau hien tai
    public List<OrganizationUnit> getListChildOrganizationUnit(List<OrganizationUnit> organizationUnits, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<OrganizationUnit> childAccountLists = organizationUnitRepository.findByParentOrganizationUnitID(parentAccountID);
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                organizationUnits.add(childAccountLists.get(i));
                List<OrganizationUnit> childAccount = organizationUnitRepository.findByParentOrganizationUnitID
                    (childAccountLists.get(i).getParentID());
                if (childAccount.size() > 0) {
                    getListChildOrganizationUnit(organizationUnits, childAccount.get(i).getId());
                }
            }
        }
        return organizationUnits;
    }

    // De quy lay ra tat ca co cau con cua co cau hien tai voi Unit Type = 0 hoac 1
    public List<OrganizationUnit> getListChildOrganizationUnitCustom(List<OrganizationUnit> organizationUnits, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<OrganizationUnit> childAccountLists = organizationUnitRepository.findByParentOrganizationUnitIDCustom(parentAccountID);
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                organizationUnits.add(childAccountLists.get(i));
                List<OrganizationUnit> childAccount = organizationUnitRepository.findByParentOrganizationUnitIDCustom
                    (childAccountLists.get(i).getParentID());
                if (childAccount.size() > 0) {
                    getListChildOrganizationUnit(organizationUnits, childAccount.get(i).getId());
                }
            }
        }
        return organizationUnits;
    }

    // De quy lay ra tat ca co cau con cua co cau hien tai voi Unit Type = 1
    public List<OrganizationUnit> getListChildOrganizationUnitUnitType1(List<OrganizationUnit> organizationUnits, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<OrganizationUnit> childAccountLists = organizationUnitRepository.findByParentOrganizationUnitAndUnitType1(parentAccountID);
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                organizationUnits.add(childAccountLists.get(i));
                List<OrganizationUnit> childAccount = organizationUnitRepository.findByParentOrganizationUnitAndUnitType1
                    (childAccountLists.get(i).getParentID());
                if (childAccount.size() > 0) {
                    getListChildOrganizationUnit(organizationUnits, childAccount.get(i).getId());
                }
            }
        }
        return organizationUnits;
    }

    // De quy lay ra tat ca co cau cha cua co cau hien tai
    public List<OrganizationUnit> getListParentOrganizationUnit(List<OrganizationUnit> organizationUnits, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        OrganizationUnit parentAccountLists = organizationUnitRepository.findByChildOrganizationUnitID(parentAccountID);
        if (parentAccountLists != null) {
            organizationUnits.add(parentAccountLists);
            if (parentAccountLists.getParentID() != null) {
                getListParentOrganizationUnit(organizationUnits, parentAccountLists.getParentID());
            }
        }
        return organizationUnits;
    }

    /**
     * Get all the organizationUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationUnit> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationUnits");
        return organizationUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationUnit> findAllBigOrg(Pageable pageable) {
        log.debug("Request to get all OrganizationUnits");
        return organizationUnitRepository.findAllBigOrg(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationUnit> findAllBigOrgSearch(Pageable pageable, OrganizationUnitSearchDTO organizationUnitSearchDTO) {
        log.debug("Request to get all OrganizationUnits");
        return organizationUnitRepository.findAllBigOrgSearch(pageable, organizationUnitSearchDTO);
    }

    @Override
    public List<OrganizationUnit> findAllBigOrg(Long userId) {
        List<OrganizationUnit> result = new ArrayList<>();
        result.addAll(organizationUnitRepository.findAllByUnitType(0));
        if (userId != null) {
            EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByUserID(userId);
            if (ebUserPackage != null) {
                OrganizationUnit organizationUnit = organizationUnitRepository.getOne(ebUserPackage.getCompanyID());
                organizationUnit.setIsHaveOrg(true);
                result.add(organizationUnit);
            }
        }
        return result;
    }

    @Override
    public List<EbUserPackage> findAllEbUSerPackage(Long userId) {
        List<EbUserPackage> result = new ArrayList<>();
        result.addAll(organizationUnitRepository.findAllUSerPackage(0));
        return result;
    }

    @Override
    public Page<OrganizationUnit> findAll() {
        return new PageImpl<OrganizationUnit>(organizationUnitRepository.findAll());
    }

    @Override
    public List<OrganizationUnit> findTotalAllBigOrg() {
        List<OrganizationUnit> result = new ArrayList<>();
        result.addAll(organizationUnitRepository.findTotalAllByUnitType());
        return result;
    }

    /**
     * Get one organizationUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationUnit> findOne(UUID id) {
        log.debug("Request to get OrganizationUnit : {}", id);
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(id);
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByCompanyID(id);
        if (ebUserPackage != null) {
//            organizationUnit.get().setUserID(ebUserPackage.getUserID());
            organizationUnit.get().setStatus(ebUserPackage.getStatus());
            organizationUnit.get().setPackageID(ebUserPackage.getPackageID());
        }
        return organizationUnit;
    }

    /**
     * Delete the organizationUnit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrganizationUnit : {}", id);
        organizationUnitRepository.deleteById(id);
    }

    @Override
    public List<String> GetListOrderFixCodeParentID(UUID id) {
        log.debug("Request to get GetListOrderFixCodeParentID : {}", id);
        return organizationUnitRepository.GetListOrderFixCodeParentID(id);
    }

    @Override
    public List<OrganizationUnit> getOuByUnitType(Long userId, Integer unitType, UUID id) {
        if (unitType == 0) {
            return organizationUnitRepository.findByUnitType(unitType, userId);
        } else {
            return organizationUnitRepository.findByUserIdAndParentID(id, userId);
        }
    }

    @Override
    public Page<OrganizationUnitDTO> getAllOrganizationUnitsActive() {
        return new PageImpl<>(organizationUnitRepository.getAllOrganizationUnitsActive());
    }

    @Override
    public Optional<OrganizationUnitDTO> getOrganizationUnitsByCompanyID() {
        UserRepository userRepository = null;
        Optional<User> userOptional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        return organizationUnitRepository.getOrganizationUnitsByCompanyID(userOptional.get().getId());
    }

    public List<OrganizationUnit> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<SystemOption> systemOption = systemOptionRepository.findAllSystemOptions(currentUserLoginAndOrg.get().getOrg());
            String check = systemOption.stream().filter(n -> n.getCode().equals("TCKHAC_SDDMDoiTuong")).findAny().get().getData();
            List<OrganizationUnit> organizationUnits = new ArrayList<OrganizationUnit>();
//            if (check.equals(Constants.TypeDungChungRieng.DUNG_CHUNG)) {
//                organizationUnits = organizationUnitRepository.findAllDepartment(currentUserLoginAndOrg.get().getOrg());
//            } else if (check.equals(Constants.TypeDungChungRieng.DUNG_RIENG)) {
                organizationUnits = organizationUnitRepository.findAllDepartment(currentUserLoginAndOrg.get().getOrg());
//            }
            return organizationUnits;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<OrganizationUnit> getAllOrganizationUnitByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            List<OrganizationUnit> organizationUnitList = new ArrayList<OrganizationUnit>();
            organizationUnitList.add(organizationUnit.get());
            recursiveOrganizationUnitByParentID(organizationUnitList);
            if (organizationUnit.get().getUnitType() > 0 && organizationUnit.get().getParentID() != null) {
                Optional<OrganizationUnit> organizationUnitParent = organizationUnitRepository.findById(organizationUnit.get().getParentID());
                organizationUnitList.add(organizationUnitParent.get());
            }
            return organizationUnitList;
        }

        throw new BadRequestAlertException("", "", "");
    }

    private List<UUID> getIDOrg(UUID orgCurrent) {
        List<UUID> result = new ArrayList<>();
        OrganizationUnit organizationUnit = organizationUnitRepository.findByID(orgCurrent);
        result.add(organizationUnit.getId());
        while (organizationUnit.getParentID() != null) {
            organizationUnit = organizationUnitRepository.findByID(organizationUnit.getParentID());
            result.add(organizationUnit.getId());
        }
        return result;
    }

    public List<OrganizationUnit> getOrganizationUnitByParentID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID topOrg = findTopParent(currentUserLoginAndOrg.get().getOrg());
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(topOrg);
            List<OrganizationUnit> organizationUnitList = new ArrayList<OrganizationUnit>();
            organizationUnitList.add(organizationUnit.get());
            OrganizationUnit parentOrg = new OrganizationUnit();
            recursiveOrganizationUnitByParentID(organizationUnitList);
            if (organizationUnit.get().getParentID() != null) {
                parentOrg = organizationUnitRepository.findByID(organizationUnit.get().getParentID());
                if (parentOrg != null) {
                    organizationUnitList.add(parentOrg);
                }
            }
            return organizationUnitList;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<OrganizationUnit> getOrganizationUnitByParentIDPopup() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            List<OrganizationUnit> organizationUnitList = new ArrayList<OrganizationUnit>();
            organizationUnitList.add(organizationUnit.get());
            return recursiveOrganizationUnitByParentIDPopup(organizationUnitList);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<OrganizationUnit> recursiveOrganizationUnitByParentID(List<OrganizationUnit> organizationUnits) {
        List<OrganizationUnit> organizationUnitList = new ArrayList<OrganizationUnit>();
        for (int i = 0; i < organizationUnits.size(); i++) {
            organizationUnitList = organizationUnitRepository.findByParentID(organizationUnits.get(i).getId());
            if (organizationUnitList.size() > 0) {
                for (int j = 0; j < organizationUnitList.size(); j++) {
                    organizationUnits.add(organizationUnitList.get(j));
                }
                recursiveOrganizationUnitByParentID(organizationUnitList);
            }
        }
        return organizationUnits;
    }

    public List<OrganizationUnit> recursiveOrganizationUnitByParentIDPopup(List<OrganizationUnit> organizationUnits) {
        List<OrganizationUnit> organizationUnitList = new ArrayList<OrganizationUnit>();
        for (int i = 0; i < organizationUnits.size(); i++) {
            organizationUnitList = organizationUnitRepository.findByParentIDCbb(organizationUnits.get(i).getId());
            if (organizationUnitList.size() > 0) {
                for (int j = 0; j < organizationUnitList.size(); j++) {
                    organizationUnits.add(organizationUnitList.get(j));
                }
                recursiveOrganizationUnitByParentID(organizationUnitList);
            }
        }
        return organizationUnits;
    }

    public List<OrganizationUnit> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return organizationUnitRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<TreeOrganizationUnitDTO> getTreeOrganizationUnit() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
            List<TreeOrganizationUnitDTO> treeOrganizationUnitDTOList = new ArrayList<TreeOrganizationUnitDTO>();
            treeOrganizationUnitDTOList.add(new TreeOrganizationUnitDTO());
            treeOrganizationUnitDTOList.get(0).setParent(organizationUnit.get());
            return recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOList);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<TreeOrganizationUnitDTO> getTreeOrganizationUnitByOfUserId() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            List<OrganizationUnit> organizationUnits = organizationUnitRepository.findAllByUserId(userDTO.getId());
            List<TreeOrganizationUnitDTO> treeOrganizationUnitDTOList = new ArrayList<TreeOrganizationUnitDTO>();
            organizationUnits.forEach(orgUnit -> {
                if (orgUnit.getUnitType() == 0 && orgUnit.getParentID() == null) {
                    TreeOrganizationUnitDTO treeOrgUnitDTO = new TreeOrganizationUnitDTO();
                    treeOrgUnitDTO.setParent(orgUnit);
                    treeOrganizationUnitDTOList.add(treeOrgUnitDTO);
                } else if (orgUnit.getUnitType() == 1 && orgUnit.getParentID() != null) {
                    List<TreeOrganizationUnitDTO> lstChildren = new ArrayList<TreeOrganizationUnitDTO>();
                    TreeOrganizationUnitDTO treeOrgUnitDTO = treeOrganizationUnitDTOList.stream()
                        .filter(n -> n.getParent().getId() == orgUnit.getParentID()).findFirst().get();
                    lstChildren.add(treeOrgUnitDTO);
                    treeOrgUnitDTO.setChildren(lstChildren);
                }
            });
            return recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOList);
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<TreeOrganizationUnitDTO> getTreeAllOrganizationUnitsByOfUserId() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        List<OrganizationUnit> organizationUnits = organizationUnitRepository.findAllByUserId(userDTO.getId());
        if (currentUserLoginAndOrg.isPresent()) {
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
            List<TreeOrganizationUnitDTO> treeOrganizationUnitDTOListParent = new ArrayList<TreeOrganizationUnitDTO>();
            treeOrganizationUnitDTOListParent.add(new TreeOrganizationUnitDTO());
            treeOrganizationUnitDTOListParent.get(0).setParent(organizationUnit);
            organizationUnits.forEach(orgUnit -> {
                if (orgUnit.getUnitType() == 0 && orgUnit.getParentID() == null) {
                    TreeOrganizationUnitDTO treeOrgUnitDTO = new TreeOrganizationUnitDTO();
                    treeOrgUnitDTO.setParent(orgUnit);
                    treeOrganizationUnitDTOListParent.add(treeOrgUnitDTO);
                } else if (orgUnit.getUnitType() == 1 && orgUnit.getParentID() != null) {
                    List<TreeOrganizationUnitDTO> lstChildren = new ArrayList<TreeOrganizationUnitDTO>();
                    TreeOrganizationUnitDTO treeOrgUnitDTO = treeOrganizationUnitDTOListParent.stream()
                        .filter(n -> n.getParent().getId() == orgUnit.getParentID()).findFirst().get();
                    lstChildren.add(treeOrgUnitDTO);
                    treeOrgUnitDTO.setChildren(lstChildren);
                }
            });
            return recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOListParent);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<OrganizationUnit> findAllByUserId() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return organizationUnitRepository.findAllByUserId(user.getId());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<TreeOrganizationUnitDTO> recursiveTreeOrganizationUnitByParentID(List<TreeOrganizationUnitDTO> treeOrganizationUnitDTOS) {
        for (int i = 0; i < treeOrganizationUnitDTOS.size(); i++) {
            List<OrganizationUnit> organizationUnitList = organizationUnitRepository.findByParentID(treeOrganizationUnitDTOS.get(i).getParent().getId());
            for (int j = 0; j < organizationUnitList.size(); j++) {
                if (j == 0) {
                    treeOrganizationUnitDTOS.get(i).setChildren(new ArrayList<TreeOrganizationUnitDTO>());
                }
                treeOrganizationUnitDTOS.get(i).getChildren().add(new TreeOrganizationUnitDTO());
                treeOrganizationUnitDTOS.get(i).getChildren().get(j).setParent(organizationUnitList.get(j));
            }
            if (treeOrganizationUnitDTOS.get(i).getChildren() != null && treeOrganizationUnitDTOS.get(i).getChildren().size() > 0) {
                recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOS.get(i).getChildren());
            }
        }
        return treeOrganizationUnitDTOS;
    }

    @Override
    public ChangeSessionDTO getOuTreeByUnitType() {
        Optional<UserSystemOption> userWithAuthorities = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthorities.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<TreeGetOrganizationUnitDTO> organizationUnitDTOS = new ArrayList<>();
            if (userWithAuthorities.get().getUser().getParentID() == null) {
                organizationUnitDTOS = userService.getTreeOrganizationUnitLogin(findTopParent(currentUserLoginAndOrg.get().getOrg()));
            } else {
                List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = organizationUnitRepository.findAllByListCompanyID(userWithAuthorities.get().getUser().getId());
                List<TreeGetOrganizationUnitDTO> organizationUnitDTOList = new ArrayList<>();
                Map<OrganizationUnit, TreeOrganizationUnitDTO> tree = new HashMap<>();
                for (OrganizationUnitCustomDTO org : organizationUnitCustomDTOS) {
                    // Trường hợp là tổng công ty
//                    if (tree.get(org.getParentID()) != null) {
//                        tree.get(org.getParentID()).getChildren().add(new OrgTreeDTO(org.getId(), org.getOrganizationUnitName(), new ArrayList<>()));
//                    } else {
                    TreeGetOrganizationUnitDTO treeOrganizationUnitDTO = new TreeGetOrganizationUnitDTO();
                    treeOrganizationUnitDTO.setParent(org);
                    organizationUnitDTOS.add(treeOrganizationUnitDTO);
                }
            }
//            organizationUnitDTOS = userService.getTreeOrganizationUnitLogin(findTopParent(currentUserLoginAndOrg.get().getOrg()));
            ChangeSessionDTO dto = new ChangeSessionDTO();
            dto.setOrgTrees(organizationUnitDTOS);
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthorities.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            dto.setBook(currentBook);
            TreeGetOrganizationUnitDTO second = findCurrentOrgLogin(organizationUnitDTOS, currentUserLoginAndOrg.get().getOrg());
            if (second != null) {
                dto.setCurrentOrgLogin(second);
            }
            return dto;
        }
        return null;
    }

    @Override
    public void deleteByOrganizationUnitID(DeleteOrganizationUnitDTO deleteOrganizationUnitDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if(Boolean.FALSE.equals(deleteOrganizationUnitDTO.getSkipExistUserOrgID())){
                int existUserUseOrg = ebUserGroupRepository.countAllUser(deleteOrganizationUnitDTO.getOrgID());
                if(existUserUseOrg > 0){
                    throw new BadRequestAlertException("Đã tồn tại người sử dụng được cấp quyền truy cập", "OrganizationUnit", "existUserUseOrg");
                }
            }
            int countVoucher = organizationUnitRepository.countVoucherNo(deleteOrganizationUnitDTO.getOrgID());
            int countDetailsVoucher = organizationUnitRepository.countDetailsVoucherNo(deleteOrganizationUnitDTO.getOrgID());
            int countExistInAccountingObject = organizationUnitRepository.countExistInAccountingObject(deleteOrganizationUnitDTO.getOrgID());
            int countBranch = organizationUnitRepository.countBranchByParentID(deleteOrganizationUnitDTO.getOrgID());
            if (countBranch > 0) {
                throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con", "OrganizationUnit", "errorDeleteParent");
            }
            if (countVoucher > 0 || countDetailsVoucher > 0 || countExistInAccountingObject > 0) {
                throw new BadRequestAlertException("Chi nhánh đã phát sinh chứng từ, không thể xoá", "OrganizationUnit", "errorExistVoucher");
            } else {
                organizationUnitRepository.deleteById(deleteOrganizationUnitDTO.getOrganizationUnit().getId());

                int countChildByParentID = organizationUnitRepository.countChildByParentID(deleteOrganizationUnitDTO.getOrganizationUnit().getParentID());
                int countChildByID = organizationUnitRepository.countChildByParentID(deleteOrganizationUnitDTO.getOrganizationUnit().getId());
                ebUserOrganizationUnitRepository.deleteByOrgID(deleteOrganizationUnitDTO.getOrgID());
                ebUserGroupRepository.deleteByOrgID(deleteOrganizationUnitDTO.getOrgID());
                if (countChildByID == 0) {
                    if (deleteOrganizationUnitDTO.getOrganizationUnit().getParentID() != null) {
                        if (countChildByParentID == 0) {
                            Optional<OrganizationUnit> childOrganizationUnit = organizationUnitRepository.findById(deleteOrganizationUnitDTO.getOrganizationUnit().getParentID());
                            childOrganizationUnit.get().setIsParentNode(false);
                            organizationUnitRepository.save(childOrganizationUnit.get());
                        }
                    }
                    organizationUnitRepository.deleteDataByCompanyID(deleteOrganizationUnitDTO.getOrgID());
                    return;
                }
            }
        }
        throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con", "OrganizationUnit", "errorDeleteParent");
    }

    @Override
    public void deleteByBigOrganizationUnitID(UUID orgId) {
        EbUserPackage ebUserPackage = ebUserPackageRepository.findOneByCompanyID(orgId);
        if (ebUserPackage != null) {
            ebUserOrganizationUnitRepository.deleteEbUserOrganizationUnitByUserId(ebUserPackage.getUserID());
        }
        organizationUnitRepository.deleteOrganizationUnitByParentId(orgId);
        ebUserPackageRepository.deleteEbUserPackageByCompanyID(orgId);
        organizationUnitRepository.deleteOrganizationUnitById(orgId);
        //organizationUnitRepository.deleteDataByCompanyID(orgId);
    }

    @Override
    public List<OrgTreeTableDTO> getAllOuTreeByUnitType() {
        Optional<UserSystemOption> userWithAuthorities = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthorities.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<OrganizationUnit> orgs = organizationUnitRepository.getAllOuTreeByUnitType();
            Map<UUID, OrgTreeTableDTO> tree = new HashMap<>();
            for (OrganizationUnit org : orgs) {
                // Trường hợp là tổng công ty
                if (org.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY) {
                    if (tree.get(org.getId()) == null) {
                        tree.put(org.getId(), new OrgTreeTableDTO(
                            org.getId(), org.getOrganizationUnitName(), org.getUnitType(), new ArrayList<>()));
                    }
                } else if (org.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH && tree.get(org.getParentID()) != null) {
                    tree.get(org.getParentID()).getChildren().add(
                        new OrgTreeTableDTO(org.getId(), org.getOrganizationUnitName(), org.getUnitType(), new ArrayList<>()));
                }
            }
            return new ArrayList<>(tree.values());
        }
        return null;
    }

    @Override
    public List<OrgTreeTableDTO> getAllOuTreeByUnitTypeByOrgID(String userLogin) {
        Optional<UserSystemOption> userWithAuthorities = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTOLogined = userService.getAccount();
        if (userWithAuthorities.isPresent() && currentUserLoginAndOrg.isPresent()) {
            Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(userLogin);
            OrganizationUnit orgUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg()).get();
            Map<UUID, OrgTreeTableDTO> tree = new HashMap<>();
            // neu la chi nhanh
            if (orgUnit.getUnitType() == 1) {
                // check co SD_SOQUANTRI?
                Optional<SystemOption> systemOpt = systemOptionRepository.findOneByUser(orgUnit.getId(), TCKHAC_SDSOQUANTRI);
                String dataSDSQT = "";
                if (systemOpt.isPresent()) {
                    dataSDSQT = systemOpt.get().getData();
                }
                // get EbUserGroup for set workingonbook
                List<EbUserGroup> listEbUG = ebUserGroupRepository.
                    findAllByUserIdAndCompanyID(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
                String crrBook = "";
                String data = "";
                if (dataSDSQT.equals(USE)) {
                    crrBook = listEbUG.size() > 0 ? listEbUG.get(0).getWorkingOnBook().toString() : Constants.TypeLedger.BOTH_BOOK.toString();
                    data = USE;
                } else {
                    crrBook = Constants.TypeLedger.FINANCIAL_BOOK.toString();
                    data = NOT_USE;
                }
                // neu co quyen dang nhap
                // add ebgroup
                Set<EbGroup> setEGs = new HashSet<>();
                orgUnit.getGroups().clear();
                List<EbGroup> lstEb = ebGroupRepository.findAllByOrgId(orgUnit.getId());
                setEGs.addAll(lstEb);
                orgUnit.setGroups(setEGs);
                orgUnit.getGroups().size();
                if (userRepository.countEbOrgUnitsByUserIdAndOrId(user.get().getId(), orgUnit.getId()) > 0) {
                    // check co vai tro trong bang EbUserGroup
                    for (EbGroup ebG : orgUnit.getGroups()) {
                        if (userRepository.countEbUserGroupsByUserIdAndOrgIdAndEbGroupId(
                            user.get().getId(), orgUnit.getId(), ebG.getId()) > 0) {
                            ebG.setCheck(true);
                        } else {
                            ebG.setCheck(false);
                        }
                    }
                    tree.put(orgUnit.getId(), new OrgTreeTableDTO(
                        orgUnit.getId(), orgUnit.getOrganizationUnitName(), orgUnit.getUnitType(), crrBook,
                        orgUnit.getGroups(), data, new ArrayList<>(), true));
                } else {
                    tree.put(orgUnit.getId(), new OrgTreeTableDTO(
                        orgUnit.getId(), orgUnit.getOrganizationUnitName(), orgUnit.getUnitType(), crrBook,
                        orgUnit.getGroups(), data, new ArrayList<>(), false));
                }
            }
            List<OrganizationUnit> orgs = new ArrayList<>();
            if (orgUnit.getUnitType() == 0 && userDTOLogined.getEbPackage().getComType() == KE_TOAN_DICH_VU) {
                // trường hợp là admin, gói tổng
                orgs = organizationUnitRepository.
                    findAllCompanyAndBranchByUserId(userDTOLogined.getId(), orgUnit.getId());
            } else if (orgUnit.getUnitType() == 0 && userDTOLogined.getEbPackage().getComType() != KE_TOAN_DICH_VU) {
                orgs = organizationUnitRepository.
                    getAllOuTreeByUnitTypeByOrgID(currentUserLoginAndOrg.get().getOrg());
            }
            for (OrganizationUnit org : orgs) {
                // check co SD_SOQUANTRI?
                Optional<SystemOption> systemOption = systemOptionRepository.findOneByUser(org.getId(), TCKHAC_SDSOQUANTRI);
                String dataSQT = "";
                if (systemOption.isPresent()) {
                    dataSQT = systemOption.get().getData();
                }
                // get EbUserGroup for set workingonbook
                List<EbUserGroup> list = ebUserGroupRepository.
                    findAllByUserIdAndCompanyID(user.get().getId(), org.getId());
                Boolean isChecked = list.stream()
                    .map(EbUserGroup::getCompanyID).collect(Collectors.toList()).contains(org.getId());
                String book = "";
                String data = "";
                if (dataSQT.equals(USE)) {
                    book = list.size() > 0 && isChecked ? list.get(0).getWorkingOnBook().toString() : Constants.TypeLedger.BOTH_BOOK.toString();
                    data = USE;
                } else {
                    book = Constants.TypeLedger.FINANCIAL_BOOK.toString();
                    data = NOT_USE;
                }
                // Trường hợp là tổng công ty
                // add ebgroup
                Set<EbGroup> sets = new HashSet<>();
                org.getGroups().clear();
                List<EbGroup> lst = ebGroupRepository.findAllByOrgId(org.getId());
                sets.addAll(lst);
                org.setGroups(sets);
                org.getGroups().size();
                if (org.getUnitType() == Constants.OrgUnitTypeConstant.TONG_CONG_TY) {
                    // neu co quyen dang nhap
                    if (userRepository.countEbOrgUnitsByUserIdAndOrId(user.get().getId(), org.getId()) > 0) {
                        // check co vai tro trong bang EbUserGroup
                        for (EbGroup ebG : org.getGroups()) {
                            Long count = userRepository.countEbUserGroupsByUserIdAndOrgIdAndEbGroupId(
                                user.get().getId(), org.getId(), ebG.getId());
                            if (count > 0) {
                                ebG.setCheck(true);
                            } else {
                                ebG.setCheck(false);
                            }
                        }
                        if (tree.get(org.getId()) == null) {
                            tree.put(org.getId(), new OrgTreeTableDTO(
                                org.getId(), org.getOrganizationUnitName(), org.getUnitType(), book,
                                org.getGroups(), data, new ArrayList<>(), true));
                        }
                    } else {
                        if (tree.get(org.getId()) == null) {
                            tree.put(org.getId(), new OrgTreeTableDTO(
                                org.getId(), org.getOrganizationUnitName(), org.getUnitType(), book,
                                org.getGroups(), data, new ArrayList<>(), false));
                        }
                    }
                } else if (org.getUnitType() == Constants.OrgUnitTypeConstant.CHI_NHANH && tree.get(org.getParentID()) != null) {
                    // neu co quyen dang nhap
                    if (userRepository.countEbOrgUnitsByUserIdAndOrId(user.get().getId(), org.getId()) > 0) {
                        // check co vai tro trong bang EbUserGroup
                        for (EbGroup ebG : org.getGroups()) {
                            if (userRepository.countEbUserGroupsByUserIdAndOrgIdAndEbGroupId(
                                user.get().getId(), org.getId(), ebG.getId()) > 0) {
                                ebG.setCheck(true);
                            } else {
                                ebG.setCheck(false);
                            }
                        }
                        tree.get(org.getParentID()).getChildren().add(
                            new OrgTreeTableDTO(org.getId(), org.getOrganizationUnitName(), org.getUnitType(), book,
                                org.getGroups(), data, new ArrayList<>(), true));
                    } else {
                        tree.get(org.getParentID()).getChildren().add(
                            new OrgTreeTableDTO(org.getId(), org.getOrganizationUnitName(), org.getUnitType(), book,
                                org.getGroups(), data, new ArrayList<>(), false));
                    }
                }
            }
            return new ArrayList<>(tree.values());
        }
        return null;
    }

    @Override
    public List<OrganizationUnit> findAllByListID(List<UUID> listID) {
        List<OrganizationUnit> listOrg = new ArrayList<>();
        listID.stream()
            .map(organizationUnitRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(listOrg::add);
        return listOrg;
    }

    @Override
    public LocalDate getPostedDate() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String postedDate = organizationUnitRepository.findStartDateById(currentUserLoginAndOrg.get().getOrg());
        return Objects.requireNonNull(DateUtil.getLocalDateFromString(postedDate, DateUtil.C_DD_MM_YYYY)).minusDays(1);
    }

    public List<OrganizationUnit> findAllExceptID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<OrganizationUnit> organizationUnits = organizationUnitRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
            List<OrganizationUnit> organizationUnitsParent = new ArrayList<OrganizationUnit>();
            recursiveOrganizationUnitParent(organizationUnitsParent, id);
            for (int i = 0; i < organizationUnitsParent.size(); i++) {
                organizationUnits.remove(organizationUnitsParent.get(i));
            }
            OrganizationUnit organizationUnit = organizationUnitRepository.findById(id).get();
            if (organizationUnit != null && organizationUnit.getParentID() != null) {
                OrganizationUnit organizationUnit1 = organizationUnitRepository.findById(organizationUnit.getParentID()).get();
                organizationUnits.add(organizationUnit1);
            }
            organizationUnits.remove(organizationUnit);
            return organizationUnits;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<OrganizationUnit> recursiveOrganizationUnitParent(List<OrganizationUnit> organizationUnits, UUID parentID) {
        List<OrganizationUnit> listAccTemp = new ArrayList<>();
        List<OrganizationUnit> organizationUnits1 = organizationUnitRepository.findAllByUnitTypeAndParentIDIsOrderByOrganizationUnitCode(parentID);
        if (organizationUnits1.size() > 0) {
            organizationUnits.addAll(organizationUnits1);
            listAccTemp.addAll(organizationUnits1);
            for (int i = 0; i < listAccTemp.size(); i++) {
                List<OrganizationUnit> accountListList = organizationUnitRepository.findAllByUnitTypeAndParentIDIsOrderByOrganizationUnitCode(listAccTemp.get(i).getId());
                if (accountListList.size() > 0) {
                    recursiveOrganizationUnitParent(organizationUnits, organizationUnits.get(i).getId());
                }
            }
        }
        return organizationUnits;
    }

    public List<TreeOrganizationUnitDTO> getAllTreeOrganizationUnitDTO() {
        List<OrganizationUnit> lstOrgParent = organizationUnitRepository.findAll().stream().
            filter(item -> item.getUnitType().equals(0)).collect(Collectors.toList());
        List<TreeOrganizationUnitDTO> treeOrganizationUnitDTOListParent = new ArrayList<TreeOrganizationUnitDTO>();
        lstOrgParent.forEach(item -> {
            TreeOrganizationUnitDTO treeDTO = new TreeOrganizationUnitDTO();
            treeDTO.setParent(item);
            treeOrganizationUnitDTOListParent.add(treeDTO);
        });
        return recursiveTreeOrganizationUnitByParentID(treeOrganizationUnitDTOListParent);
    }

    public List<OrganizationUnit> getAllOrganizationUnitByListParentID() {
        List<OrganizationUnit> lstOrgParent = organizationUnitRepository.findAll().stream().
            filter(item -> item.getUnitType().equals(0)).collect(Collectors.toList());
        return recursiveOrganizationUnitByParentID(lstOrgParent);
    }

    @Override
    public ChangeSessionDTO getChildCompanyByID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByLoginAndOrganizationUnitsId(currentUserLoginAndOrg.get().getLogin(),
            currentUserLoginAndOrg.get().getOrg());
        if (userOptional.isPresent() && currentUserLoginAndOrg.isPresent()) {
            ChangeSessionDTO dto = new ChangeSessionDTO();
            List<TreeGetOrganizationUnitDTO> organizationUnitDTOS = userService.getTreeOrganizationUnitLogin(currentUserLoginAndOrg.get().getOrg());
            dto.setOrgTrees(organizationUnitDTOS);
            Optional<TreeGetOrganizationUnitDTO> second = organizationUnitDTOS.stream().filter(item -> item.getParent().getId().compareTo(currentUserLoginAndOrg.get().getOrg()) == 0).findFirst();
            if (second.isPresent()) {
                dto.setCurrentOrgLogin(second.get());
            }
            return dto;
        }
        throw new BadRequestAlertException("", "", "");
    }


    public UUID findTopParent(UUID orgID) {
        OrganizationUnit currentOrg = organizationUnitRepository.findByID(orgID);
        if (currentOrg.getParentID() != null) {
            return findTopParent(currentOrg.getParentID());
        } else {
            return orgID;
        }
    }

    @Override
    public List<OrganizationUnit> findAllDepartments(UUID orgID, boolean isDependent) {
        return isDependent ? organizationUnitRepository.findAllDepartmentDependent(orgID) :
            organizationUnitRepository.findAllDepartment(orgID);
    }

    // đoạn này code hơi ngu ai có cách khác thì sửa
    public TreeGetOrganizationUnitDTO findCurrentOrgLogin(List<TreeGetOrganizationUnitDTO> organizationUnitDTOS, UUID orgID) {
        for (int i = 0; i < organizationUnitDTOS.size(); i++) {
            if (organizationUnitDTOS.get(i).getParent().getId().equals(orgID)) {
                return organizationUnitDTOS.get(i);
            }
            if (organizationUnitDTOS.get(i).getChildren() != null && organizationUnitDTOS.get(i).getChildren().size() > 0) {
                for (int j = 0; j < organizationUnitDTOS.get(i).getChildren().size(); j++) {
                    if (organizationUnitDTOS.get(i).getChildren().get(j).getParent().getId().equals(orgID)) {
                        return organizationUnitDTOS.get(i).getChildren().get(j);
                    }
                    if (organizationUnitDTOS.get(i).getChildren().get(j).getChildren() != null && organizationUnitDTOS.get(i).getChildren().get(j).getChildren().size() > 0) {
                        for (int k = 0; k < organizationUnitDTOS.get(i).getChildren().get(j).getChildren().size(); k++) {
                            if (organizationUnitDTOS.get(i).getChildren().get(j).getChildren().get(k).getParent().getId().equals(orgID)) {
                                return organizationUnitDTOS.get(i).getChildren().get(j).getChildren().get(k);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
