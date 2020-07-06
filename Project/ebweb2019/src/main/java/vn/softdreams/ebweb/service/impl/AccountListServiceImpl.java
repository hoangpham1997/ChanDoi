package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.domain.AccountList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AccountList.
 */
@Service
@Transactional
public class AccountListServiceImpl implements AccountListService {

    private final Logger log = LoggerFactory.getLogger(AccountListServiceImpl.class);

    private final AccountListRepository accountListRepository;

    private UserRepository userRepository;

    private final UtilsService utilsService;

    private final UserService userService;
    private final OrganizationUnitService organizationUnitService;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final OPAccountService opAccountService;

    private final OPMaterialGoodsService opMaterialGoodsService;

    private final SystemOptionRepository systemOptionRepository;
    private final OPAccountRepository opAccountRepository;

    public AccountListServiceImpl(AccountListRepository accountListRepository, UserRepository userRepository,
                                  UtilsService utilsService, UserService userService, OrganizationUnitService organizationUnitService, OrganizationUnitRepository organizationUnitRepository, OPAccountService opAccountService, OPMaterialGoodsService opMaterialGoodsService, SystemOptionRepository systemOptionRepository, OPAccountRepository opAccountRepository) {
        this.accountListRepository = accountListRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
        this.userService = userService;
        this.organizationUnitService = organizationUnitService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.opAccountService = opAccountService;
        this.opMaterialGoodsService = opMaterialGoodsService;
        this.systemOptionRepository = systemOptionRepository;
        this.opAccountRepository = opAccountRepository;
    }

    /**
     * Save a accountList.
     *
     * @param accountList the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountList save(AccountList accountList) {
        log.debug("Request to save AccountList : {}", accountList);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> accountListsChild = new ArrayList<AccountList>();
        List<AccountList> accountListsParent = new ArrayList<AccountList>();
        Optional<OrganizationUnit> organizationUnit = organizationUnitService.findOne(currentUserLoginAndOrg.get().getOrgGetData());
        if (currentUserLoginAndOrg.isPresent()) {
            if(accountList.getId() == null){
                Integer checkExistAccount = accountListRepository.findByAccountNumberAndCompanyID(accountList.getAccountNumber(), currentUserLoginAndOrg.get().getOrgGetData());
                if(checkExistAccount > 0){
                    throw new BadRequestAlertException("Số tài khoản đã tồn tại!", "AccountList", "existAccount");
                }
            }
            accountList.setAccountingType(organizationUnit.get().getAccountingType());
            accountList.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
            getListChildAccount(accountListsChild, accountList.getId());
            if (accountList.getParentAccountID() != null) {
                AccountList parent = accountListRepository.findById(accountList.getParentAccountID()).get();
                if (accountList.getId() == null) {
                    int checkConstraint = accountListRepository.checkConstrainDelete(parent.getAccountNumber(), currentUserLoginAndOrg.get().getOrgGetData());
                    if (checkConstraint > 0) {
                        throw new BadRequestAlertException("Tài khoản cha đã có dữ liệu hạch toán, không thể tạo con!", "AccountList", "existConstraint");
                    }
                }
                if (Boolean.TRUE.equals(accountList.getIsActive())) {
                    getListParentAccount(accountListsParent, accountList.getParentAccountID());
                    for (int i = 0; i < accountListsParent.size(); i++) {
                        accountListsParent.get(i).setIsActive(accountList.getIsActive());
                    }
                }
                Optional<AccountList> acc = accountListRepository.findById(accountList.getParentAccountID());
                acc.get().setIsParentNode(true);
                if (accountListsChild.size() > 0) {
                    accountList.setIsParentNode(true);
                } else {
                    accountList.setIsParentNode(false);
                }
                accountList.setGrade(acc.get().getGrade() + 1);
                accountListRepository.save(acc.get());
            } else {
                if (accountList.getId() != null) {
                    Optional<AccountList> accountList1 = accountListRepository.findById(accountList.getId());
                    List<AccountList> accountListList = accountListRepository.findByParentAccountID(accountList1.get().getParentAccountID(), currentUserLoginAndOrg.get().getOrgGetData());
                    if (accountListList.size() == 1) {
                        Optional<AccountList> accountList2 = accountListRepository.findById(accountList1.get().getParentAccountID());
                        accountList2.get().setIsParentNode(false);
                        accountListRepository.save(accountList2.get());
                    }
                }
                List<AccountList> accountListList = accountListRepository.findByParentAccountID(accountList.getId(), currentUserLoginAndOrg.get().getOrgGetData());
                if (accountListList.size() == 0) {
                    accountList.setIsParentNode(false);
                }
                accountList.setGrade(1);
            }
            // Set isParentNode = false nếu thằng cha ko còn con
            if (accountList.getId() != null) {
                AccountList currentAccount = accountListRepository.findById(accountList.getId()).get();
                if (currentAccount.getParentAccountID() != null) {
                    AccountList parentAccount = accountListRepository.findById(currentAccount.getParentAccountID()).get();
                    List<AccountList> parentOfCurrentAccount = accountListRepository.findByParentAccountID(parentAccount.getId(), currentUserLoginAndOrg.get().getOrgGetData());
                    if (parentOfCurrentAccount.size() == 1) {
                        parentAccount.setIsParentNode(false);
                        accountListRepository.save(parentAccount);
                    }
                }
            }
            // set lại Grade cho toàn bộ thằng con của tài khoản hiện tại
            Boolean isEqualsGrade = false;
            for (int i = 0; i < accountListsChild.size(); i++) {
                if (accountListsChild.get(i).getGrade().equals(accountList.getGrade())) {
                    isEqualsGrade = true;
                }
            }
            if (accountListsChild.size() > 0) {
                for (int i = 0; i < accountListsChild.size(); i++) {
                    accountListsChild.get(i).setIsActive(accountList.getIsActive());
                    if (Boolean.TRUE.equals(isEqualsGrade)) {
                        accountListsChild.get(i).setGrade(accountListsChild.get(i).getGrade() + 1);
                    }
                }
                accountListRepository.saveAll(accountListsChild);
            }
            return accountListRepository.save(accountList);
        }
        throw new BadRequestAlertException("", "", "");
    }

    // De quy lay ra tat ca tai khoan con cua tai khoan hien tai
    public List<AccountList> getListChildAccount(List<AccountList> accountLists, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> childAccountLists = accountListRepository.findByParentAccountID(parentAccountID,
            currentUserLoginAndOrg.get().getOrgGetData());
        if (childAccountLists.size() > 0) {
            for (int i = 0; i < childAccountLists.size(); i++) {
                accountLists.add(childAccountLists.get(i));
                List<AccountList> childAccount = accountListRepository.findByParentAccountID
                    (childAccountLists.get(i).getParentAccountID(), currentUserLoginAndOrg.get().getOrgGetData());
                if (childAccount.size() > 0) {
                    getListChildAccount(accountLists, childAccount.get(i).getId());
                }
            }
        }
        return accountLists;
    }

    @Override
    public List<AccountList> findAllAccountListByOrg(UUID orgID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        OrganizationUnit organizationUnit = organizationUnitRepository.findByID(orgID);
//        UUID orgGetData;
//        if (organizationUnit != null) {
//            if(organizationUnit.getUnitType() == 0 && organizationUnit.getUserID() != null){
//
//            }
//        }
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllAccountLists(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    // De quy lay ra tat ca tai khoan cha cua tai khoan hien tai
    public List<AccountList> getListParentAccount(List<AccountList> accountLists, UUID parentAccountID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        AccountList parentAccountLists = accountListRepository.findByChildAccountID(parentAccountID,
            currentUserLoginAndOrg.get().getOrgGetData());
        if (parentAccountLists != null) {
            accountLists.add(parentAccountLists);
            if (parentAccountLists.getParentAccountID() != null) {
                getListParentAccount(accountLists, parentAccountLists.getParentAccountID());
            }
        }
        return accountLists;
    }

    /**
     * Get all the accountLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountList> findAll(Pageable pageable) {
        log.debug("Request to get all AccountLists");
        return accountListRepository.findAll(pageable);
    }


    /**
     * Get one accountList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountList> findOne(UUID id) {
        log.debug("Request to get AccountList : {}", id);
        return accountListRepository.findById(id);
    }

    /**
     * Delete the accountList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountList : {}", id);
        accountListRepository.deleteById(id);
    }

    @Override
    public Page<AccountList> findAll(Pageable pageable, String accountNumber) {
        return accountListRepository.findAll(pageable, accountNumber);
    }

    @Override
    public List<AccountListDTO> getAccountType(Integer typeID, String columnName, Boolean ppType) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        return accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, columnName, ppType);
        return null;
    }

    @Override
    public Optional<AccountListAllDTO> getAccountTypeSecond(Integer typeID, List<ColumnDTO> columnName) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        AccountListAllDTO accountListAllDTO = new AccountListAllDTO();
        columnName.forEach(item -> {
//            if (item.getColumn().toUpperCase().equals("DEDUCTIONDEBITACCOUNT")) {
//                accountListAllDTO.setDeductionDebitAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("DEBITACCOUNT")) {
//                accountListAllDTO.setDebitAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("VATACCOUNT")) {
//                accountListAllDTO.setVatAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("DISCOUNTACCOUNT")) {
//                accountListAllDTO.setDiscountAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("CREDITACCOUNT")) {
//                accountListAllDTO.setCreditAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("IMPORTTAXACCOUNT")) {
//                accountListAllDTO.setImportTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("EXPORTTAXACCOUNT")) {
//                accountListAllDTO.setExportTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("REPOSITORYACCOUNT")) {
//                accountListAllDTO.setRepositoryAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("SPECIALCONSUMETAXACCOUNT")) {
//                accountListAllDTO.setSpecialConsumeTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            } else if (item.getColumn().toUpperCase().equals("COSTACCOUNT")) {
//                accountListAllDTO.setCostAccount(accountListRepository.getAccountType(currentUserLoginAndOrg, typeID, item.getColumn(), item.getPpType()));
//            }
        });
        return Optional.of(accountListAllDTO);
    }

    public List<AccountListDTO> findByGOtherVoucher() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findByGOtherVoucher(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * @param ppServiceType 240: PP_SERVICE_UNPAID, 241: PP_SERVICE_CASH,
     *                      242: PP_SERVICE_PAYMENT_ORDER, 243: PPSERVICE_CHECK_TRANSFER
     *                      244: PP_SERVICE_CREDIT_CARD, 245: PPSERVICE_CASH_CHECK
     * @param accountType   0: debit, 1: credit
     * @return
     * @author phuonghv
     */
    @Override
    public List<AccountListDTO> getAccountListByAccountType(Integer ppServiceType, Integer accountType) {
        Optional<UserSystemOption> userSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!userSystemOption.isPresent() || !currentUserLoginAndOrg.isPresent()) {
            return new ArrayList<>();
        }
        String columnName = accountType == 0 ? Constants.AccountDefaultColumnName.DEBIT_ACCOUNT : Constants.AccountDefaultColumnName.CREDIT_ACCOUNT;
        UserSystemOption systemOption = userSystemOption.get();
        UUID companyId = currentUserLoginAndOrg.get().getOrgGetData();
        if (systemOption.getSystemOptions().stream().map(so -> {
            if (so.getData().equals("1")) return so.getCode();
            return null;
        }).collect(Collectors
            .toCollection(ArrayList::new)).contains(Constants.SystemOption.TCKHAC_HanCheTK)) {
            return accountListRepository.getAccountListByAccountTypeHasRestrictEdit(ppServiceType, columnName, companyId);
        }
        return accountListRepository.getAllAccountList();
    }

    public List<AccountListDTO> findAccountLike133() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAccountLike133(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Get all the accountLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountList> findAllByCompanyID(Pageable pageable) {
        log.debug("Request to get all AccountLists");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllByCompanyID(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> findAllExceptID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<AccountList> accountLists = accountListRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
            List<AccountList> accountListsParent = new ArrayList<AccountList>();
            recursiveAccountListParent(accountListsParent, id, currentUserLoginAndOrg.get().getOrgGetData());
            for (int i = 0; i < accountListsParent.size(); i++) {
                accountLists.remove(accountListsParent.get(i));
            }
            AccountList accountList = accountListRepository.findById(id).get();
            accountLists.remove(accountList);
            return accountLists;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> recursiveAccountListParent(List<AccountList> accountLists, UUID parentID, UUID companyID) {
        List<AccountList> listAccTemp = new ArrayList<>();
        List<AccountList> accountLists1 = accountListRepository.findByParentAccountID(parentID, companyID);
        if (accountLists1.size() > 0) {
            accountLists.addAll(accountLists1);
            listAccTemp.addAll(accountLists1);
            for (int i = 0; i < listAccTemp.size(); i++) {
                List<AccountList> accountListList = accountListRepository.findByParentAccountID(listAccTemp.get(i).getId(), companyID);
                if (accountListList.size() > 0) {
                    accountLists.addAll(recursiveAccountListParent(listAccTemp, accountLists.get(i).getId(), companyID));
                }
            }
        }
        return accountLists;
    }

    public List<AccountList> findAllActive1() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllByIsActive1(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> findAllActive2() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllByIsActive2(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> findAllForSystemOptions() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllForSystemOptions(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> findAllAccountLists() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.findAllAccountLists(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> getAccountStartWith154() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountStartWith154(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> getAccountDetailType() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountDetailType(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> getAccountDetailTypeActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountDetailTypeActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountList> getAccountTypeFromGOV() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountTypeFromGOV(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrgGetData(), Constants.SystemOption.TCKHAC_SDDMDoiTuong));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountList> getAccountListSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            UUID orgID = findTopParent(currentUserLoginAndOrg.get().getOrg());
            return accountListRepository.findAllAccountListSimilarBranch(similarBranch, orgID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    private UUID findTopParent(UUID orgID) {
        OrganizationUnit currentOrg = organizationUnitRepository.findByID(orgID);
        if (currentOrg.getParentID() != null) {
            return findTopParent(currentOrg.getParentID());
        } else {
            return orgID;
        }
    }

    @Override
    public void checkOPNAndExistData(String accountNumber) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        if (currentUserLoginAndOrg.isPresent()) {
            List<OPAccount> opAccount = opAccountRepository.findAllByAccountNumberAndCompanyIdAndTypeLedger(accountNumber, currentUserLoginAndOrg.get().getOrgGetData(), typeLedger);
            Integer count = accountListRepository.checkUse(accountNumber);
            BigDecimal sumCreditAmount = BigDecimal.ZERO;
            BigDecimal sumDebitAmount = BigDecimal.ZERO;
            for (int i = 0; i < opAccount.size(); i++) {
                sumCreditAmount = sumCreditAmount.add(opAccount.get(i).getCreditAmount());
                sumDebitAmount = sumDebitAmount.add(opAccount.get(i).getDebitAmount());
            }
            if (count > 0 || sumCreditAmount.compareTo(BigDecimal.ZERO) > 0 || sumDebitAmount.compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestAlertException("Đã phát sinh dữ liệu !", "AccountList", "checkOPNAndExistData");
            } else {
                return;
            }
        }
    }

    public List<AccountList> getAccountStartWith112() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountStartWith112(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountList> getAccountStartWith111() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountStartWith111(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<AccountForAccountDefaultDTO> getAccountForAccountDefault(String listFilterAccount) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAccountForAccountDefault(listFilterAccount, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountList> findAllAccountList() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> accountListDTOS = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            accountListDTOS = accountListRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }

        return accountListDTOS;
    }

    @Override

    public List<AccountList> getAccountForOrganizationUnit() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<AccountList> accountListDTOS = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            accountListDTOS = accountListRepository.getAccountForOrganizationUnit(currentUserLoginAndOrg.get().getOrgGetData());
        }

        return accountListDTOS;
    }

    @Override
    public List<AccountListDTO> findAllActiveForOP(String currencySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO account = userService.getAccount();
        String currencyId = account.getOrganizationUnit().getCurrencyID();
        if (currentUserLoginAndOrg.isPresent()) {
            Integer currentBook = Utils.PhienSoLamViec(account);
            List<AccountListDTO> accountListDTOS = accountListRepository.findAllByIsActiveForOP(currentUserLoginAndOrg.get().getOrg(),
                currencySearch, currentBook, currentUserLoginAndOrg.get().getOrgGetData());

            List<AccountListDTO> accountListParent = new ArrayList<>();
            for (AccountListDTO accountListDTO : accountListDTOS) {
                boolean isNewNode = true;
                for (AccountListDTO dto : accountListParent) {
                    if (dto.getAccountNumber().equals(accountListDTO.getAccountNumber()) && accountListDTO.getOpAccountDTO() != null) {
                        accountListDTO.setAccountName(dto.getAccountName());
                        dto.addOPAccountDTO(accountListDTO.getOpAccountDTO());
                        isNewNode = false;
                    }
                    if (accountListDTO.getAmountOriginal() != null) {
                        if (dto.getAccountNumber().equalsIgnoreCase(accountListDTO.getAccountNumber())) {
                            dto.setAmountOriginal(dto.getAmountOriginal().add(accountListDTO.getAmountOriginal()));
                            isNewNode = false;
                        }
                    }
                }
                if (isNewNode) {
                    AccountListDTO dto = accountListDTO;
                    if (dto.getOpAccountDTO() != null && dto.getOpAccountDTO().getId() != null) {
                        dto.getOpAccountDTO().setAccountName(dto.getAccountName());
                        dto.addOPAccountDTO(dto.getOpAccountDTO());
                        dto.setOpAccountDTO(null);
                    }
                    accountListParent.add(dto);
                }
            }
            accountListParent = accountListParent.stream().map(al -> {
                if (al.getOpAccountDTOList() != null) {
                    al.setCreditAmountOriginal(al.getOpAccountDTOList()
                        .stream()
                        .map(opa -> opa.getCurrencyId()
                            .equals(currencyId) ? opa.getCreditAmountOriginal() : opa.getCreditAmount())
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                    al.setDebitAmountOriginal(al.getOpAccountDTOList()
                        .stream()
                        .map(opa -> opa.getCurrencyId()
                            .equals(currencyId) ? opa.getDebitAmountOriginal() : opa.getDebitAmount())
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                }
                if (al.getAmountOriginal() != null && !al.getAmountOriginal().equals(BigDecimal.ZERO)) {
                    al.setDebitAmountOriginal(al.getAmountOriginal());
                }
                return al;
            }).collect(Collectors.toList());
            for (int i = accountListParent.size() - 1; i >= 0; i--) {
                if (accountListParent.get(i).getParentAccountID() != null) {
                    for (int j = 0; j < accountListParent.size(); j++) {
                        if (accountListParent.get(j).getId().equals(accountListParent.get(i).getParentAccountID())) {
                            accountListParent.get(j).setCreditAmountOriginal(accountListParent.get(j)
                                .getCreditAmountOriginal().add(accountListParent.get(i).getCreditAmountOriginal()));
                            accountListParent.get(j).setDebitAmountOriginal(accountListParent.get(j)
                                .getDebitAmountOriginal().add(accountListParent.get(i).getDebitAmountOriginal()));
                        }
                    }
                }
            }
            List<AccountListDTO> finalAccountListParent = accountListParent;

            return accountListParent
                .stream()
                .peek(x -> x.setChildren(finalAccountListParent
                    .stream()
                    .filter(y -> y.getParentAccountID() != null && y.getParentAccountID().equals(x.getId()))
                    .collect(Collectors.toList())))
                .collect(Collectors.toList())
                .stream()
                .filter(x -> x.getParentAccountID() == null)
                .collect(Collectors.toList());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public void deleteByAccountListID(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<AccountList> accountList = accountListRepository.findById(id);
            int checkConstraint = accountListRepository.checkConstrainDelete(accountList.get().getAccountNumber(), currentUserLoginAndOrg.get().getOrgGetData());
            if (checkConstraint > 0) {
                throw new BadRequestAlertException("Còn tồn tại ràng buộc, không thể xoá !", "AccountList", "existConstraint");
            }
            accountListRepository.deleteById(id);
            int countChildByParentID = accountListRepository.countChildByParentID(currentUserLoginAndOrg.get().getOrgGetData(), accountList.get().getParentAccountID());
            int countChildByID = accountListRepository.countChildByID(currentUserLoginAndOrg.get().getOrgGetData(), accountList.get().getId());
            if (countChildByID == 0) {
                if (accountList.get().getParentAccountID() != null) {
                    if (countChildByParentID == 0) {
                        Optional<AccountList> childAccountList = accountListRepository.findById(accountList.get().getParentAccountID());
                        childAccountList.get().setIsParentNode(false);
                        accountListRepository.save(childAccountList.get());
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
        }
        throw new BadRequestAlertException("Không thể xóa dữ liệu cha nếu còn tồn tại dữ liệu con", "AccountList", "errorDeleteParent");
    }

    public byte[] exportPdf(String currencySearch) {
        UserDTO account = userService.getAccount();
        List<AccountListDTO> accountListDTOS = this.findAllActiveForOP(currencySearch);
        return PdfUtils.writeToFile(accountListDTOS, ExcelConstant.OpeningBalance.HEADER, ExcelConstant.OpeningBalance.FIELD,
            account);
    }

    public Optional<AccountListAllDTO> getAccountTypeThird(Integer typeID, List<ColumnDTO> columnName) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        OrganizationUnit organizationUnitParent = new OrganizationUnit();
        Boolean checkParent;
        if (userDTO.getOrganizationUnit().getUnitType() == 1 && userDTO.getOrganizationUnit().getAccType() == 0) {
            checkParent = true; // phụ thuộc
        } else {
            checkParent = false; // không phụ thuộc
        }
        AccountListAllDTO accountListAllDTO = new AccountListAllDTO();
        columnName.forEach(item -> {
            if (item.getColumn().toUpperCase().equals("DEDUCTIONDEBITACCOUNT")) {
                accountListAllDTO.setDeductionDebitAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setDeductionDebitAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("DEBITACCOUNT")) {
                accountListAllDTO.setDebitAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setDebitAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("VATACCOUNT")) {
                accountListAllDTO.setVatAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setVatAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("DISCOUNTACCOUNT")) {
                accountListAllDTO.setDiscountAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setDiscountAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("CREDITACCOUNT")) {
                accountListAllDTO.setCreditAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setCreditAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("IMPORTTAXACCOUNT")) {
                accountListAllDTO.setImportTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setImportTaxAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("EXPORTTAXACCOUNT")) {
                accountListAllDTO.setExportTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setExportTaxAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("REPOSITORYACCOUNT")) {
                accountListAllDTO.setRepositoryAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setRepositoryAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("SPECIALCONSUMETAXACCOUNT")) {
                accountListAllDTO.setSpecialConsumeTaxAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setSpecialConsumeTaxAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            } else if (item.getColumn().toUpperCase().equals("COSTACCOUNT")) {
                accountListAllDTO.setCostAccount(accountListRepository.getAccountType(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType(), checkParent));
                accountListAllDTO.setCostAccountDefault(accountListRepository.getAccountTypeDefault(currentUserLoginAndOrg.get().getOrgGetData(), typeID, item.getColumn(), item.getPpType()));
            }
        });
        return Optional.of(accountListAllDTO);
    }

    @Override
    public List<Integer> getGradeAccount() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getGradeAccount(currentUserLoginAndOrg.get().getOrgGetData());
        }
        return null;
    }

    @Override
    public List<AccountList> getAccountForTHCPTheoKMCP() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<AccountList> accountLists = accountListRepository.getAccountForTHCPTheoKMCP(currentUserLoginAndOrg.get().getOrgGetData());
            return getAllChildAccount(accountLists, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    // De quy lay ra tat ca tai khoan con cua list tai khoan hien tai
    public List<AccountList> getAllChildAccount(List<AccountList> accountLists, UUID orgID) {
        for (int i = 0; i < accountLists.size(); i++) {
            if (Boolean.TRUE.equals(accountLists.get(i).getParentNode())) {
                List<AccountList> accountLists1 = accountListRepository.findAccountListByParentID(accountLists.get(i).getId(), orgID);
                if (accountLists1 != null && accountLists1.size() > 0) {
                    accountLists.addAll(accountLists1);
                }
            }
        }
        return accountLists;
    }

    @Override
    public List<AccountForAccountDefaultDTO> findAllAccountListActiveAndAccountingType() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountListRepository.getAllAccountListActiveAndAccountingType(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
