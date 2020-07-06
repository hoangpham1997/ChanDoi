package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

/**
 * Service Implementation for managing MaterialGoods.
 */
@Service
@Transactional
public class MaterialGoodsServiceImpl implements MaterialGoodsService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsServiceImpl.class);

    private final MaterialGoodsRepository materialGoodsRepository;
    private final MaterialGoodsAssemblyRepository materialGoodsAssemblyRepository;
    private final MaterialGoodsPurchasePriceRepository materialGoodsPurchasePriceRepository;
    private final MaterialGoodsSpecificationsRepository materialGoodsSpecificationsRepository;
    private final SaleDiscountPolicyRepository saleDiscountPolicyRepository;
    private final MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;
    private final UserService userService;
    private final RepositoryRepository repositoryRepository;
    private final SystemOptionRepository systemOptionRepository;


    public MaterialGoodsServiceImpl(MaterialGoodsRepository materialGoodsRepository,
                                    MaterialGoodsAssemblyRepository materialGoodsAssemblyRepository,
                                    MaterialGoodsPurchasePriceRepository materialGoodsPurchasePriceRepository,
                                    MaterialGoodsSpecificationsRepository materialGoodsSpecificationsRepository,
                                    SaleDiscountPolicyRepository saleDiscountPolicyRepository,
                                    OrganizationUnitRepository organizationUnitRepository,
                                    UtilsService utilsService,
                                    MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository,
                                    UserService userService, RepositoryRepository repositoryRepository, SystemOptionRepository systemOptionRepository) {
        this.materialGoodsRepository = materialGoodsRepository;
        this.materialGoodsAssemblyRepository = materialGoodsAssemblyRepository;
        this.materialGoodsPurchasePriceRepository = materialGoodsPurchasePriceRepository;
        this.materialGoodsSpecificationsRepository = materialGoodsSpecificationsRepository;
        this.saleDiscountPolicyRepository = saleDiscountPolicyRepository;
        this.materialGoodsConvertUnitRepository = materialGoodsConvertUnitRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
        this.userService = userService;
        this.repositoryRepository = repositoryRepository;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a materialGoods.
     *
     * @param materialGoods the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodSaveDTO save(MaterialGoods materialGoods) {
        log.debug("Request to save MaterialGoods : {}", materialGoods);
        MaterialGoods curr = new MaterialGoods();
        MaterialGoodSaveDTO materialGoodSaveDTO = new MaterialGoodSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            materialGoods.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        if (materialGoods.getId() == null) {
            int count = materialGoodsRepository.countByMaterialGoodsCodeIgnoreCaseAndIsActiveTrue(materialGoods.getMaterialGoodsCode(),currentUserLoginAndOrg.get().getOrg());
            if (count > 0) {
                materialGoodSaveDTO.setMaterialGoods(materialGoods);
                materialGoodSaveDTO.setStatus(count);
                return materialGoodSaveDTO;
            } else {
                UUID materialGoodID = UUID.randomUUID();
                materialGoods.setId(materialGoodID);
                for (MaterialGoodsAssembly item: materialGoods.getMaterialGoodsAssembly()) {
                    item.setMaterialGoodsID(materialGoodID);
                }
                curr = materialGoodsRepository.save(materialGoods);
                materialGoodSaveDTO.setMaterialGoods(curr);
                materialGoodSaveDTO.setStatus(count);
                return materialGoodSaveDTO;
            }
        } else {
            for (MaterialGoodsAssembly item: materialGoods.getMaterialGoodsAssembly()) {
                item.setMaterialGoodsID(materialGoods.getId());
            }
            curr = materialGoodsRepository.save(materialGoods);
            materialGoodSaveDTO.setMaterialGoods(curr);
            materialGoodSaveDTO.setStatus(0);
            return materialGoodSaveDTO;
        }
    }
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        materialGoods.setCompanyID(currentUserLoginAndOrg.get().getOrg());
//        if (materialGoods.getId() == null) {
//            materialGoods.setId(UUID.randomUUID());
//        }
//        return materialGoodsRepository.save(materialGoods);
//    }

    /**
     * Get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoods> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoods");
        return materialGoodsRepository.findAll(pageable);
    }

    /**
     * Get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoods> getAllByCompanyID(Pageable pageable) {
        log.debug("Request to get all MaterialGoods");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.getAllByCompanyID(pageable, currentUserLoginAndOrg.get().getOrg());

        }
        throw new BadRequestAlertException("", "", "");

    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoods> pageableAllMaterialGoods(Pageable pageable) {
        UserDTO userDTO = userService.getAccount();
        Integer test = Utils.getTCKHAC_SDDMVTHH(userDTO);
        Boolean isGetAllCompany = Utils.getTCKHAC_SDDMVTHH(userDTO) == 0;
        log.debug("Request to get all MaterialGoods");
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
                return materialGoodsRepository.getAllByListCompany(pageable, listCompanyID);
            } else {
                return materialGoodsRepository.pageableAllMaterialGoods(pageable, currentUserLoginAndOrg.get().getOrg());
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Get one materialGoods by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoods> findOne(UUID id) {
        log.debug("Request to get MaterialGoods : {}", id);
        Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(id);
        UserDTO userDTO = userService.getAccount();
        List<String> nameColumns = new ArrayList<>();
        List<VoucherRefCatalogDTO> voucherRefCatalogDTOS = utilsService.getVoucherRefCatalogDTOByID(
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO),
            materialGoods.get().getId(),
            "MaterialGoodsID");

        materialGoods.get().setVoucherRefCatalogDTOS(voucherRefCatalogDTOS);
        return materialGoods;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoods> findVoucherByMaterialGoodsID(Pageable pageable, UUID id) {
        log.debug("Request to get MaterialGoods : {}", id);
        Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(id);
        UserDTO userDTO = userService.getAccount();
        List<String> nameColumns = new ArrayList<>();
        nameColumns.add("AccountingObjectID");
        nameColumns.add("EmployeeID");
        List<VoucherRefCatalogDTO> voucherRefCatalogDTOS = utilsService.getVoucherRefCatalogDTOByID(
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO),
            materialGoods.get().getId(),
            nameColumns);

        materialGoods.get().setVoucherRefCatalogDTOS(voucherRefCatalogDTOS);
        return materialGoodsRepository.findVoucherByMaterialGoodsID(pageable, id);
    }

    //    @Override
//    @Transactional(readOnly = true)
//    public Optional<MaterialGoods> findOne(UUID id) {
//        log.debug("Request to get MaterialGoods : {}", id);
//        return materialGoodsRepository.findById(id);
//    }


    /**
     * Delete the materialGoods by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoods : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "MaterialGoodsID");
        Boolean checkCostSet = utilsService.checkContraint("CostSet", "MaterialGoodsID", id);
        Boolean checkQuantum = utilsService.checkContraint("MaterialQuantum", "ObjectID", id);
        Boolean checkQuantumDetail = utilsService.checkContraint("MaterialQuantumDetail", "MaterialGoodsID", id);
        if (checkUsedAcc) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else if (checkCostSet) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh dữ liệu liên quan.VTHH này đã thuộc Đối tượng THCP khác! ", "", "");
        } else if (checkQuantum || checkQuantumDetail) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh dữ liệu liên quan.VTHH này đã thuộc Định mức nguyên vật liệu khác! ", "", "");
        } else {
             materialGoodsAssemblyRepository.deleteByMaterialGoodsID(id);
             materialGoodsRepository.deleteById(id);
        }
    }

    @Override
    public HandlingResultDTO deleteEmployee(List<UUID> uuids) {
        log.debug("Request to delete MaterialGoods : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = materialGoodsRepository.getIDRefEmployee(uuids);
        lstFail.addAll(materialGoodsRepository.getIDContraint(uuids));
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            materialGoodsRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }


    @Override
    public Page<MaterialGoods> findAll1(Pageable pageable, SearchVoucherMaterialGoods searchVoucherMaterialGoods) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.findAll1
                (pageable, searchVoucherMaterialGoods, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }


    @Override
    public List<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox1(List<Integer> materialsGoodsType) {
        log.debug("Request to get MG for PPOrder");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return materialGoodsRepository.getMaterialGoodsForCombobox1(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), materialsGoodsType);
    }

    public List<MaterialGoods> findByCompanyIDAndIsActiveTrue() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> materialGoodsRepository.findByCompanyIDAndIsActiveTrue(securityDTO.getOrg())).orElse(null);
    }

    @Override
    public List<MGForPPOrderDTO> getMaterialGoodsForCombobox() {
        log.debug("Request to get MG for PPOrder");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.getMaterialGoodsForCombobox(currentUserLoginAndOrg.get().getOrg().toString());
        }
        return new ArrayList<>();
    }

    @Override
    public Long getMaterialGoodsPPInvoiceQuantity(UUID id, UUID ppOrderDetailID) {
        log.debug("Request to get MG for getMaterialGoodsPPInvoiceQuantity");
        return materialGoodsRepository.getMaterialGoodsPPInvoiceQuantity(id, ppOrderDetailID);
    }

    @Override
    public List<MaterialGoodsConvertDTO> ConvertToCombobox() {
        return materialGoodsRepository.ConvertToCombobox();
    }

    public List<MaterialGoodsDTO> findAllForPPService() {
        return materialGoodsRepository.findAllForPPService();
    }

    @Override
    public List<MaterialGoodsDTO> findAllForPPInvoice() {
        return materialGoodsRepository.findAllForPPInvoice();
    }

    public Optional<MaterialGoods> getByUUID(UUID id) {
        return materialGoodsRepository.getByUUID(id.toString());
    }

    public List<MaterialGoods> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.findAllByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoods> getAllMaterialGoodsActiveCompanyIDByRepository(UUID repositoryId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.getAllMaterialGoodsActiveCompanyIdByRepository(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH), repositoryId);
        }
        throw new BadRequestAlertException("", "", "");
    }


    public List<MaterialGoods> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.findAllByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoodsDTO> findAllMaterialGoodsCustom() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialGoodsRepository.findAllMaterialGoodsCustom(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<MaterialGoodsDTO> findAllForDTO(UUID companyID, Boolean similarBranch) {
        List<UUID> comIds = new ArrayList<>();
        if (similarBranch != null && similarBranch) {
            comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
        } else {
            comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMVTHH);
        }
        return materialGoodsRepository.findAllForDTO(comIds);
    }

    @Override
    public List<MGForPPOrderConvertQuantityDTO> getQuantityExistsTest(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs, String postedDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return materialGoodsRepository.getQuantityExistsTest(materialGoodsIDs, repositoryIDs, postedDate);
    }

    @Override
    public ListObjectDTO getMaterialGoodAndRepository(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs) {
        ListObjectDTO listObjectDTO = new ListObjectDTO();
        List<ObjectDTO> materialGoodsList = materialGoodsRepository.getIDAndNameByIDS(materialGoodsIDs);
        if (materialGoodsList != null) {
            listObjectDTO.setMaterialGoodIDS(materialGoodsList);
        }
        List<ObjectDTO> repositoryList = repositoryRepository.getIDAndNameByIDS(repositoryIDs);
        if (repositoryList != null) {
            listObjectDTO.setRepositoryIDS(repositoryList);
        }
        return listObjectDTO;
    }

    @Override
    public List<MaterialGoodsDTO> findAllForDTOSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMVTHH)).findAny().get().getData().equals("1");
            return materialGoodsRepository.findAllForDTOSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMVTHH), similarBranch, checkShared);
        }
        throw new BadRequestAlertException("", "", "");
    }
}
