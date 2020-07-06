package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.AccountingObjectBankAccountRepository;
import vn.softdreams.ebweb.repository.AccountingObjectRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AccountingObjectService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing AccountingObject.
 */
@Service
@Transactional
public class AccountingObjectServiceImpl implements AccountingObjectService {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectServiceImpl.class);

    private final AccountingObjectRepository accountingObjectRepository;
    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final SystemOptionRepository systemOptionRepository;

    AccountingObjectServiceImpl(AccountingObjectRepository accountingObjectRepository,
                                AccountingObjectBankAccountRepository accountingObjectBankAccountRepository,
                                OrganizationUnitRepository organizationUnitRepository,
                                UserService userService,
                                UtilsService utilsService, SystemOptionRepository systemOptionRepository) {
        this.accountingObjectRepository = accountingObjectRepository;
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a accountingObject.
     *
     * @param accountingObject the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountingObjectSaveDTO save(AccountingObject accountingObject) {
        log.debug("Request to save AccountingObject : {}", accountingObject);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        AccountingObject accountingObject1 = new AccountingObject();
        AccountingObjectSaveDTO accountingObjectSaveDTO = new AccountingObjectSaveDTO();
        if (currentUserLoginAndOrg.isPresent()) {
            if (accountingObjectRepository.findByCompanyIDAndAccountingObjectCode(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(),
                        Constants.SystemOption.TCKHAC_SDDMDoiTuong), accountingObject.getAccountingObjectCode(),
                accountingObject.getId() == null ? new UUID(0L, 0L) : accountingObject.getId()) != null) {
                accountingObjectSaveDTO.setStatus(1);
                accountingObjectSaveDTO.setAccountingObject(accountingObject);
                return accountingObjectSaveDTO;
            }
            if (accountingObject.getId() == null) {
                accountingObject.setCompanyId(currentUserLoginAndOrg.get().getOrg());
            }
            if (accountingObject.getId() == null) {
                accountingObject.setIsActive(true);
            }
            accountingObject1 = accountingObjectRepository.save(accountingObject);
            accountingObjectSaveDTO.setAccountingObject(accountingObject1);
            accountingObjectSaveDTO.setStatus(0);
        }
        return accountingObjectSaveDTO;
    }

    /**
     * Get all the accountingObjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObject> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingObjects");
        return accountingObjectRepository.findAll(pageable);
    }

    /**
     * Get all the accountingObjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObject> pageableAllAccountingObjects(Pageable pageable, Integer typeAccountingObject) {
        log.debug("Request to get all AccountingObjects");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean check = systemOptionRepository.findByCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong) == 1;
        if (!check) {
            return accountingObjectRepository.pageableAllAccountingObjects(pageable, typeAccountingObject, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        } else {
            return accountingObjectRepository.pageableAllAccountingObjects(pageable, typeAccountingObject, Stream.of(currentUserLoginAndOrg.get().getOrg()).collect(Collectors.toList()));
        }

    }


    /**
     * add by namnh
     *
     * @return
     */
    @Override
    public Page<AccountingObject> findAll() {
        return new PageImpl<AccountingObject>(accountingObjectRepository.findAll());
    }

    @Override
    public Page<AccountingObject> findAll1(Pageable pageable, Integer typeAccountingObject, SearchVoucherAccountingObjects searchVoucherAccountingObjects) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        List<UUID> listOrg =  organizationUnitRepository.findAllOrg();
        return accountingObjectRepository.findAll1(pageable, typeAccountingObject, searchVoucherAccountingObjects, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    @Override
    public Page<AccountingObject> findAllEmloyee1(Pageable pageable, SearchVoucherEmployee searchVoucherEmployee) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        List<UUID> listOrg =  organizationUnitRepository.findAllOrg();
        return accountingObjectRepository.findAllEmployee1(pageable, searchVoucherEmployee, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    /**
     * Get one accountingObject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingObject> findOne(UUID id) {
        log.debug("Request to get AccountingObject : {}", id);
        Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(id);
        UserDTO userDTO = userService.getAccount();
        List<String> nameColumns = new ArrayList<>();
        nameColumns.add("AccountingObjectID");
        nameColumns.add("EmployeeID");
        List<VoucherRefCatalogDTO> voucherRefCatalogDTOS = utilsService.getVoucherRefCatalogDTOByID(
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO),
            accountingObject.get().getId(),
            nameColumns);

        accountingObject.get().setVoucherRefCatalogDTOS(voucherRefCatalogDTOS);
        return accountingObject;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingObject> findOneWithPageable(Pageable pageable, UUID accID) {
        log.debug("Request to get AccountingObject : {}", accID);
        Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(accID);
        /*UserDTO userDTO = userService.getAccount();
        List<String> nameColumns = new ArrayList<>();
        nameColumns.add("AccountingObjectID");
        nameColumns.add("EmployeeID");
        Page<VoucherRefCatalogDTO> voucherRefCatalogDTOS = utilsService.getVoucherRefCatalogDTOByID(
            pageable,
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO),
            accountingObject.get().getId(),
            nameColumns);

        accountingObject.get().setVoucherRefCatalogDTOS(voucherRefCatalogDTOS.getContent());*/
        return accountingObject;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObject> findVoucherByAccountingObjectID(Pageable pageable, UUID id) {
        log.debug("Request to get AccountingObject : {}", id);
        Optional<AccountingObject> accountingObject = accountingObjectRepository.findById(id);
        UserDTO userDTO = userService.getAccount();
        List<String> nameColumns = new ArrayList<>();
        nameColumns.add("AccountingObjectID");
        nameColumns.add("EmployeeID");
        List<VoucherRefCatalogDTO> voucherRefCatalogDTOS = utilsService.getVoucherRefCatalogDTOByID(
            userDTO.getOrganizationUnit().getId(),
            Utils.PhienSoLamViec(userDTO),
            accountingObject.get().getId(),
            nameColumns);

        accountingObject.get().setVoucherRefCatalogDTOS(voucherRefCatalogDTOS);
        return accountingObjectRepository.findVoucherByAccountingObjectID(pageable, id);
    }

    /**
     * Delete the accountingObject by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountingObject : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "AccountingObjectID");
        Boolean checkUsedEmpl = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "EmployeeID");
        if (checkUsedAcc || checkUsedEmpl) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            accountingObjectRepository.deleteById(id);
        }
    }

    @Override
    public HandlingResultDTO delete(List<UUID> uuids) {
        log.debug("Request to delete AccountingObject : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = accountingObjectRepository.getIDRef(currentUserLoginAndOrg.get().getOrg(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            accountingObjectRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO deleteEmployee(List<UUID> uuids) {
        log.debug("Request to delete AccountingObject : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = accountingObjectRepository.getIDRefEmployee(currentUserLoginAndOrg.get().getOrg(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            accountingObjectRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findByObjecttypeAndIsactive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    @Override
    public List<AccountingObject> getByAllAccountingObjectsActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findByObjecttypeAndIsActived(currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsEmployee() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findByObjecttypeAndIEmployee(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    public List<AccountingObject> getAccountingObjectsForProvider() {
        log.debug("REST Lấy accounting object phần đơn mua hàng");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsForProvider(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAccountingObjectsForEmployee() {
        log.debug("REST Lấy accounting object phần nhân viên thực hiện");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsForEmployee(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAccountingObjectsForEmployees() {
        log.debug("REST Lấy accounting object phần nhân viên thực hiện");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsForEmployees(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectsByGroupID(UUID id, Boolean branch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if(currentUserLoginAndOrg.isPresent()) {
            List<UUID> comIds;
//            if (similarBranch != null && similarBranch) {
//                comIds = organizationUnitRepository.getCompanyAndBranch(currentUserLoginAndOrg.get().getOrg())
//                        .stream().map(OrganizationUnit::getId).collect(Collectors.toList());
//            } else {
//                comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong);
//            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            // tham số :?1 companyid ?2 check có lấy chi nhánh phụ thược không ?3 check dùng chung dùng riêng ?4 id group nhóm đối tượng
            return accountingObjectRepository.getAccountingObjectByGroupID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), branch, checkShared, id);
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObjectDTO> findAllDTO(Boolean isObjectType12) {
        log.debug("Request to get AccountingObjectsDTO");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findAllDTO(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong), isObjectType12);
    }

    @Override
    public List<AccountingObjectDTO> findAllAccountingObjectDTO(Boolean isObjectType12) {
        log.debug("Request to get AccountingObjectsDTO");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findAllAccountingObjectDTO(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong), isObjectType12);
    }

    @Override
    public List<AccountingObject> getAccountingObjectActiveByReportRequest(RequestReport requestReport) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.getAccountingObjectActiveByReportRequest(requestReport.getCompanyID(), requestReport.getDependent());
    }

    public List<AccountingObjectDTO> findAllDTO(Integer taskMethod) {
        log.debug("Request to get AccountingObjectsDTO with objectType and isActive");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findAllDTO(taskMethod, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    @Override
    public Page<PPPayVendorDTO> getPPPayVendorByDate(String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        return accountingObjectRepository.getPPPayVendorByDate(fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public Page<PPPayVendorBillDTO> getPPPayVendorBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        return accountingObjectRepository.getPPPayVendorBillByAccountingObjectID(fromDate, toDate, accountObjectID, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public List<AccountingObject> getAccountingObjectsActive() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.findByIsActive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAccountingObjectsActiveCustom() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.findByIsActiveCustom(systemOptionRepository
                    .getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public Page<SAReceiptDebitDTO> getSAReceiptDebitByDate(String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        return accountingObjectRepository.getSAReceiptDebitByDate(fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public Page<SAReceiptDebitBillDTO> getSAReceiptDebitBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        return accountingObjectRepository.getSAReceiptDebitBillByAccountingObjectID(fromDate, toDate, accountObjectID, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public List<AccountingObject> findAllAccountingObjectByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.findAllAccountingObjectByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountingObject> getAccountingObjectsActiveForRSInwardOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsActiveForRSInwardOutward(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsForRSInwardOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAllAccountingObjectsForRSInwardOutward(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAccountingObjectsRSOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsRSOutward(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsRSOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAllAccountingObjectsRSOutward(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectByGroupIDKH(UUID id, Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
//            List<UUID> comIds;
//            if (similarBranch != null && similarBranch) {
//                comIds = organizationUnitRepository.getCompanyAndBranch(currentUserLoginAndOrg.get().getOrg())
//                        .stream().map(OrganizationUnit::getId).collect(Collectors.toList());
//            } else {
//                comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong);
//            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectRepository.getAccountingObjectByGroupIDKH(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared, id);
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObject> getAccountingObjectsRSTransfer() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.getAccountingObjectsRSTransfer(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectsByGroupIDSimilarBranch(UUID id, Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectRepository.getAccountingObjectsByGroupIDSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared, id);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountingObject> getAccountingObjectByGroupIDKHSimilarBranch(UUID id, Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectRepository.getAccountingObjectByGroupIDKHSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared, id);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsEmployeeSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            UserDTO userDTO = userService.getAccount();
            Boolean checkShared = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData().equals("1");
            return accountingObjectRepository.getAllAccountingObjectsEmployeeSimilarBranch(systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong), similarBranch, checkShared);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsByCompanyID(UUID orgID, boolean isDependent) {
        return accountingObjectRepository.getAllAccountingObjectsByCompanyID(orgID, isDependent);
    }

    @Override
    public List<AccountingObjectDTO> findAllDTOAll(Integer taskMethod) {
        log.debug("Request to get AccountingObjectsDTO with objectType and isActive");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return accountingObjectRepository.findAllDTOAll(taskMethod, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
    }

    @Override
    public List<AccountingObject> getAccountingObjectsTransferActive() {
        log.debug("REST Lấy accounting object hoạt động");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountingObjectRepository.findByTransferIsActive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        return new ArrayList<>();
    }

    @Override
    public List<AccountingObjectDTO> findAllAccountingObjectByCompany(UUID companyID, Boolean dependent) {
        List<UUID> comIds = new ArrayList<>();
        if (dependent != null && dependent) {
            comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
        } else {
            comIds = systemOptionRepository.getAllCompanyByCompanyIdAndCode(companyID, Constants.SystemOption.TCKHAC_SDDMDoiTuong);
        }
        return accountingObjectRepository.getAllAccountingObjectByCompany(comIds);
    }
}
