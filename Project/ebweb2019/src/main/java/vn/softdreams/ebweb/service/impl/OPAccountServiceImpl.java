package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OPAccount.
 */
@Service
@Transactional
public class OPAccountServiceImpl implements OPAccountService {

    private final Logger log = LoggerFactory.getLogger(OPAccountService.class);

    private final OPAccountRepository opAccountRepository;

    private final AccountListRepository accountListRepository;

    private final CurrencyRepository currencyRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final UserService userService;

    private final UtilsService utilsService;

    private final OrganizationUnitService organizationUnitService;

    private final GeneralLedgerService generalLedgerService;

    private final BankAccountDetailsService bankAccountDetailsService;

    private final CreditCardService creditCardService;

    private final StatisticsCodeService statisticsCodeService;

    private final AccountingObjectService accountingObjectService;

    private final CostSetService costSetService;

    private final ExpenseItemService expenseItemService;

//    private final EMContractService emContractService;

    private final BudgetItemService budgetItemService;

    public OPAccountServiceImpl(OPAccountRepository opAccountRepository,
                                AccountListRepository accountListRepository,
                                CurrencyRepository currencyRepository,
                                OrganizationUnitRepository organizationUnitRepository,
                                UserService userService,
                                UtilsService utilsService, OrganizationUnitService organizationUnitService,
                                GeneralLedgerService generalLedgerService,
                                BankAccountDetailsService bankAccountDetailsService,
                                CreditCardService creditCardService, StatisticsCodeService statisticsCodeService,
                                AccountingObjectService accountingObjectService,
                                CostSetService costSetService,
                                ExpenseItemService expenseItemService,
//                                EMContractService emContractService,
                                BudgetItemService budgetItemService) {
        this.opAccountRepository = opAccountRepository;
        this.accountListRepository = accountListRepository;
        this.currencyRepository = currencyRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.organizationUnitService = organizationUnitService;
        this.generalLedgerService = generalLedgerService;
        this.bankAccountDetailsService = bankAccountDetailsService;
        this.creditCardService = creditCardService;
        this.statisticsCodeService = statisticsCodeService;
        this.accountingObjectService = accountingObjectService;
        this.costSetService = costSetService;
        this.expenseItemService = expenseItemService;
//        this.emContractService = emContractService;
        this.budgetItemService = budgetItemService;
    }

    @Override
    public List<OPAccountDTO> findAllByAccountNumber(String accountNumber, String currencyId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        return currentUserLoginAndOrg.map(securityDTO -> (currencyId == null ? opAccountRepository
                .findAllByAccountNumberAndCompanyIdAndTypeLedger(accountNumber, securityDTO.getOrg(), typeLedger) :
                opAccountRepository.findAllByAccountNumberAndCompanyIdAndCurrencyIdAndTypeLedger(accountNumber, securityDTO.getOrg(), currencyId, typeLedger))
                .stream().map(OPAccountDTO::new)
                .collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    @Override
    public List<OPAccountDTO> saveOPAccount(List<OPAccountDTO> opAccount) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Map<UUID, BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findAllActiveCustom().stream()
            .collect(Collectors.toMap(BankAccountDetails::getId, bankAccountDetails1 -> bankAccountDetails1));
        Map<UUID, AccountingObject> accountingObjects = accountingObjectService.getAccountingObjectsActiveCustom().stream()
            .collect(Collectors.toMap(AccountingObject::getId, accountingObject -> accountingObject));
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        LocalDate postedDate = organizationUnit.get().getStartDate().minusDays(1);
        List<OPAccount> opAccountDTOList = opAccountRepository.saveAll(opAccount.stream()
                .map(opADTO -> new OPAccount(opADTO, currentUserLoginAndOrg.get().getOrg(), postedDate)).collect(Collectors.toList()));
        generalLedgerService.record(opAccountDTOList.stream().peek(op -> {
            if (op.getBankAccountDetailId() != null) {
                op.setBankAccountDetails(bankAccountDetails.get(op.getBankAccountDetailId()));
            }
            if (op.getAccountingObjectId() != null) {
                op.setAccountingObject(accountingObjects.get(op.getAccountingObjectId()));
            }
        }).collect(Collectors.toList()), new MessageDTO(""));
        return opAccountDTOList.stream().map(OPAccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public UpdateDataDTO deleteOPAccountByIds(List<UUID> uuids) {
         try {
             generalLedgerService.unrecord(uuids, false);
             opAccountRepository.deleteByIds(uuids);
             return new UpdateDataDTO(Constants.UpdateDataDTOMessages.DELETE_SUCCESS);
         } catch (Exception e) {
             return new UpdateDataDTO(Constants.UpdateDataDTOMessages.FAIL);
         }
    }

    @Override
    public byte[] exportPdfAccountNormal(String accountNumber, String currencyId) {
        UserDTO account = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String organizationCurrencyId = account.getOrganizationUnit().getCurrencyID();
        List<OPAccountDTO> accountListDTOS = this.findAllByAccountNumber(accountNumber, currencyId);
        AccountList accountList = accountListRepository.findByAccountNumberAndCompanyIDAndIsActiveTrue(accountNumber, currentUserLoginAndOrg.get().getOrg());
        for (int i = 0; i < accountListDTOS.size(); i++) {
            accountListDTOS.get(i).setAccountName(accountList.getAccountName());
        }
        return PdfUtils.writeToFile(accountListDTOS,
                organizationCurrencyId.equals(currencyId) ?  ExcelConstant.OPAccountNormal.HEADER : ExcelConstant.OPAccountNormalForeignCurrency.HEADER,
                organizationCurrencyId.equals(currencyId) ? ExcelConstant.OPAccountNormal.FIELD : ExcelConstant.OPAccountNormalForeignCurrency.FIELD,
                account);
    }

    @Override
    public byte[] exportPdfAccountingObject(String accountNumber, String currencyId) {
        UserDTO account = userService.getAccount();
        String organizationCurrencyId = account.getOrganizationUnit().getCurrencyID();
        List<OPAccountDTO> accountListDTOS = this.findAllByAccountNumber(accountNumber, currencyId);
        return PdfUtils.writeToFile(accountListDTOS,
                organizationCurrencyId.equals(currencyId) ?  ExcelConstant.OPAccountAccountingObject.HEADER : ExcelConstant.OPAccountAccountingObjectForeignCurrency.HEADER,
                organizationCurrencyId.equals(currencyId) ? ExcelConstant.OPAccountAccountingObject.FIELD : ExcelConstant.OPAccountAccountingObjectForeignCurrency.FIELD,
                account);
    }

    @Override
    public byte[] getFileTemp(Integer type) {
        try {
            File currentDirectory = new File(new File("").getAbsolutePath());
            String reportUrl = "";
            switch (type) {
                case TypeConstant.OPENING_BLANCE_MT:
                    reportUrl = "/report/opening_blance_m.xlsx";
                    break;
                case TypeConstant.OPENING_BLANCE_AC:
                    reportUrl = "/report/opening_blance_ac.xlsx";
                    break;
                case TypeConstant.OPENING_BLANCE:
                    reportUrl = "/report/opening_blance.xlsx";
                default:
                    break;
            }
            File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + reportUrl);
            return Files.readAllBytes(reportFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public UpdateDataDTO uploadNormal(MultipartFile file, String sheetName) throws IOException {
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(companyId);
        // init map data
        Map<String, BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findAllActiveCustom().stream()
            .collect(Collectors.toMap(BankAccountDetails::getBankAccount, bankAccountDetails1 -> bankAccountDetails1));
        Map<String, AccountList> stringAccountListMap = accountListRepository.findAllByIsActiveCustom(currentUserLoginAndOrg.get().getOrgGetData()).stream().map(dto -> {
            AccountList accountList = new AccountList();
            try {
                BeanUtils.copyProperties(accountList, dto);
                accountList.setParentAccountNumber(dto.getParentAccountNumber());
                accountList.setIsForeignCurrency(dto.getIsForeignCurrency());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return accountList;
        }).collect(Collectors.toMap(AccountList::getAccountNumber, accountList -> accountList));

        Map<String, UUID> statistics = statisticsCodeService.findAllActive().stream().collect(Collectors.toMap(StatisticsCode::getStatisticsCode, StatisticsCode::getId));
        Map<String, UUID> costSets = costSetService.findAllActive().stream().collect(Collectors.toMap(CostSet::getCostSetCode, CostSet::getId));
        Map<String, UUID> expenseItems = expenseItemService.findAllActive().stream().collect(Collectors.toMap(ExpenseItem::getExpenseItemCode, ExpenseItem::getId));
//        Map<String, UUID> emContract = emContractService.findAllActive().stream().collect(Collectors.toMap(typeLedger.equals(1) ? EMContract::getNoFBook : EMContract::getNoMBook, EMContract::getId));
        Map<String, UUID> organizations = organizationUnitService.findAllActive().stream().collect(Collectors.toMap(OrganizationUnit::getOrganizationUnitCode, OrganizationUnit::getId));
        Map<String, UUID> budgetItems = budgetItemService.findAllActive().stream().collect(Collectors.toMap(BudgetItem::getBudgetItemCode, BudgetItem::getId));
//      Map<String, UUID> currencies = currencyRepository.findByIsActiveTrue(companyId).stream().collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getId));
        Map<String, Integer> normalAccounts = new HashMap<>();
        normalAccounts.put("1111", 0);
        normalAccounts.put("1121", 1);
        normalAccounts.put("1123", 2);


        List<Currency> currenciesRaw = currencyRepository.findByIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData());
        Map<String, UUID> currencies = currenciesRaw.stream().collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getId));
        boolean isError = false;
        if (!currentUserLoginAndOrg.isPresent() || !user.isPresent()) {
            throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT,
                    this.getClass().getName(),
                    Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Workbook workbook = null;
        String message = "";
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception ignore) {
            isError = true;
            message = "invalidFile";
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }
        if (sheetName == null && workbook.getNumberOfSheets() > 1) {
            UpdateDataDTO dto = new UpdateDataDTO();
            List<String> sheetNames = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetAt(i).getSheetName());
            }
            dto.setError(true);
            dto.setResult(sheetNames);
            dto.setMessage(Constants.ImportExcel.SELECT_SHEET);
            return dto;
        }
        Sheet sheet;
        if (sheetName != null) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.getSheetAt(0);
        }
        if (sheet.getLastRowNum() < 1) {
            isError = true;
            message = "rowEmpty";
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }
        // Lấy ra row đầu tiên đọc header
        Row row1 = sheet.getRow(0);
        Iterator<Cell> cellIterator = row1.cellIterator();
        Map<String, Integer> headers = new HashMap<>();
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (ExcelConstant.Header.OP_ACCOUNT_NORMAL.contains(ExcelUtils.getCellValue(cell).toString())) {
                headers.put(ExcelUtils.getCellValue(cell).toString(), i);
            } else {
                isError = true;
                message = "invalidFileFormat";
                UpdateDataDTO dto = new UpdateDataDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
            i++;
        }
        // check các cột bặt buộc phải có trong file
        for (String header : ExcelConstant.Header.OP_ACCOUNT_NORMAL_REQUIRED) {
            if (headers.get(header) == null) {
                isError = true;
                message = "invalidFileFormat";
                UpdateDataDTO dto = new UpdateDataDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
        }

        List<OPAccount> opAccounts = new ArrayList<>();

        Map<OPAccount, List<OPAccount>> sumList = new HashMap<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            // kiểm tra loại tài khoản
            boolean checkAccountNumberAndCurrency = true;
            OPAccount opAccount = new OPAccount();
            Row row = sheet.getRow(j);

            String accountNUmber = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0))));
            String currencyId = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))));
            BigDecimal exchangeRate = null;
            try {
                exchangeRate = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2)));
            }
            // Dư nợ
            BigDecimal debitAmountOriginal = null;
            try {
                debitAmountOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3)));
            }
            // Dư nợ quy đổi
            BigDecimal debitAmount = null;
            try {
                debitAmount = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4)));
            }
            // dư có nguyên tệ
            BigDecimal creditAmountOriginal = null;
            try {
                creditAmountOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5)));
            }
            // dư có quy đổi
            BigDecimal creditAmount = null;
            try {
                creditAmount = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6)));
            }
            AccountList accountList = stringAccountListMap.get(accountNUmber);
            if (Strings.isNullOrEmpty(accountNUmber) && Strings.isNullOrEmpty(currencyId) && exchangeRate == null && debitAmountOriginal == null && creditAmountOriginal == null) {
                continue;
            }
            if (accountList == null && accountNUmber != null) {
                isError = true;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0) + " không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0)));
                continue;
            }
            if (accountList.getParentAccountNumber() == null) {
                opAccount.setIndex(j);
                opAccount.setAccountNumber(accountNUmber);
                opAccount.setDebitAmountOriginal(debitAmountOriginal);
                opAccount.setDebitAmount(debitAmount);
                opAccount.setCreditAmountOriginal(creditAmountOriginal);
                opAccount.setCreditAmount(creditAmount);
                sumList.put(opAccount, new ArrayList<>());
                continue;
            }
            Integer statusAccount = accountList.getAccountGroupKind();
            if (Strings.isNullOrEmpty(accountNUmber)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0) + " bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(0))));
                opAccount.setAccountNumber(accountNUmber);

                if (statusAccount.equals(1) && debitAmountOriginal != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3))), "Tài khoản có tính chất dư có, không thể nhập dư nợ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3)));
                }
                if (statusAccount.equals(1) && debitAmount != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))), "Tài khoản có tính chất dư có, không thể nhập dư nợ quy đổi",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4)));
                }

                if (statusAccount.equals(0) && creditAmountOriginal != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5))), "Tài khoản có tính chất dư nợ, không thể nhập dư có",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5)));
                }
                if (statusAccount.equals(0) && creditAmount != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))), "Tài khoản có tính chất dư nợ, không thể nhập dư có quy đổi",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6)));
                }
            }

            if (Strings.isNullOrEmpty(currencyId)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))), "Loại tiền bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1)));
            } else {
                if (currencies.get(currencyId) == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))),
                        "Mã loại tiền không tồn tại", row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))));
                    opAccount.setCurrencyId(currencyId);
                }
            }
            boolean isForeignCurrency = Boolean.FALSE.equals(currencyId.equals(organizationUnit.get().getCurrencyID()));
            if (accountList.getIsForeignCurrency() && !isForeignCurrency) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))),
                    "Loại tiền không phải ngoại tệ", row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1)));
                continue;
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))));
            }
            if (!accountList.getIsForeignCurrency() && isForeignCurrency) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))),
                    "Loại tiền phải khác ngoại tệ", row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1)));
                continue;
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(1))));
            }
            if (exchangeRate != null) {
                if (!isForeignCurrency && exchangeRate.compareTo(BigDecimal.ONE) != 0) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2))), "Tỷ giá không khớp với loại tiền",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(2))));
                    opAccount.setExchangeRate(exchangeRate);
                }
            }

            if(!statusAccount.equals(1) && !isForeignCurrency && debitAmountOriginal != null && debitAmount != null && debitAmountOriginal.compareTo(debitAmount) != 0) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))), "Dư nợ quy đổi không khớp dư nợ",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))));
            }

            if(!statusAccount.equals(0) && !isForeignCurrency && creditAmountOriginal != null && creditAmount != null && creditAmountOriginal.compareTo(creditAmount) != 0) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))), "Dư có quy đổi không khớp dư có",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))));
            }
            //
            if (!statusAccount.equals(1) && isForeignCurrency && debitAmountOriginal != null && debitAmount == null) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))), "Dư nợ quy đổi bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))));
            }
            if (!statusAccount.equals(0) && isForeignCurrency && creditAmountOriginal != null && creditAmount == null) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))), "Dư có quy đổi bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))));
            }

            opAccount.setDebitAmountOriginal(debitAmountOriginal!= null ? debitAmountOriginal : BigDecimal.ZERO);
            opAccount.setDebitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO);
            if (!isForeignCurrency && debitAmount == null) {
                opAccount.setDebitAmount(opAccount.getDebitAmountOriginal());
            }
            // Nếu tài khoản bình thường, import chỉ có dư nợ qui đổi, không có dư nợ thì set dư nơ = dư nợ qui đổi
            if (normalAccounts.containsKey(accountNUmber)) {
                if (debitAmount != null && debitAmountOriginal == null) {
                    opAccount.setDebitAmountOriginal(opAccount.getDebitAmount());
                }
            }
            opAccount.setCreditAmountOriginal(creditAmountOriginal!= null ? creditAmountOriginal : BigDecimal.ZERO);
            opAccount.setCreditAmount(creditAmount != null ? creditAmount : BigDecimal.ZERO);
            if (!isForeignCurrency && creditAmount == null) {
                opAccount.setCreditAmount(opAccount.getCreditAmountOriginal());
            }
            String bankAccount = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))));
            if (stringAccountListMap.get(opAccount.getAccountNumber()) != null && stringAccountListMap.get(opAccount.getAccountNumber()).getDetailType() != null) {
                String[] detailType = stringAccountListMap.get(opAccount.getAccountNumber()).getDetailType().split(";");
                if (Arrays.asList(detailType).contains(TypeConstant.ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT)) {
                    if (Strings.isNullOrEmpty(bankAccount)) {
                        checkAccountNumberAndCurrency = false;
                        ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7) + " không được để trống",
                            row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7)));
                    } else {
                        ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))));
                    }
                }
            }
            if (!Strings.isNullOrEmpty(bankAccount)) {
                BankAccountDetails bankAccountDetails1 =  bankAccountDetails.get(bankAccount);
                if (bankAccountDetails1 == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))));
                    opAccount.setBankAccountDetails(bankAccountDetails1);
                }
            }

            String expenseItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(8))));
            if (!Strings.isNullOrEmpty(expenseItemCode)) {
                UUID expenseItemID = expenseItems.get(expenseItemCode);
                if (expenseItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(8))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(8) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(8)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(8))));
                    opAccount.setExpenseItemId(expenseItemID);
                }
            }

            String costSetCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(9))));
            if (!Strings.isNullOrEmpty(costSetCode)) {
                UUID cotSetID = costSets.get(costSetCode);
                if (cotSetID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(9))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(9) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(9)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(9))));
                    opAccount.setCostSetId(cotSetID);
                }
            }

//            String EMContractCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(10))));
//            if (!Strings.isNullOrEmpty(EMContractCode)) {
//                UUID contractID =  emContract.get(EMContractCode);
//                if (contractID == null) {
//                    checkAccountNumberAndCurrency = false;
//                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(10))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(10) + " không hợp lệ",
//                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(10)));
//                } else {
//                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(10))));
//                    opAccount.setContractId(contractID);
//                }
//            }

            // Thêm '- 1' để bỏ trường hợp đồng, khi thêm lại chỉ cần bỏ đi là ok
            String budgetItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(11 - 1))));
            if (!Strings.isNullOrEmpty(budgetItemCode)) {
                UUID budgetItemID = budgetItems.get(budgetItemCode);
                if (budgetItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(11 - 1))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(11 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(11 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(11 - 1))));
                    opAccount.setBudgetItemId(budgetItemID);
                }
            }
            String departmentCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(12 - 1))));
            if (!Strings.isNullOrEmpty(departmentCode)) {
                UUID departmentID = organizations.get(departmentCode);
                if (departmentID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(12 - 1))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(12 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(12 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(12 - 1))));
                    opAccount.setDepartmentId(departmentID);
                }
            }

            String statisticsCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(13 - 1))));
            if (!Strings.isNullOrEmpty(statisticsCode)) {
                UUID statisticsID = statistics.get(statisticsCode);
                if (statisticsID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(13 - 1))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(13 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(13 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(13 - 1))));
                    opAccount.setStatisticsCodeId(statisticsID);
                }
            }

            if (checkAccountNumberAndCurrency) {
                opAccount.setCompanyId(companyId);
                opAccount.setTypeId(TypeConstant.OPENING_BLANCE);
                opAccount.setTypeLedger(typeLedger);
                opAccount.setOrderPriority(j);
                opAccount.setPostedDate(organizationUnit.get().getStartDate().minusDays(1));
                opAccounts.add(opAccount);
                sumList.keySet().stream()
                    .filter(x -> x.getAccountNumber().equals(accountList.getParentAccountNumber()))
                    .findFirst()
                    .ifPresent(opAF -> sumList.get(opAF).add(opAccount));
            } else {
                isError = true;
            }
        }
        for (OPAccount opA: sumList.keySet()) {
            Row row = sheet.getRow(opA.getIndex());
            String MESS = "Số dư ở tài khoản tổng hợp không bằng tổng của các tài khoản chi tiết ";
            if (sumList.get(opA).isEmpty()) {
                continue;
            }
            if (opA.getDebitAmountOriginal().compareTo(sumList.get(opA)
                .stream().map(OPAccount::getDebitAmountOriginal).reduce(BigDecimal::add).get()) != 0) {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3))), MESS,
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(3)));
                isError = true;
            }
            if (!opA.getDebitAmount().equals(BigDecimal.ZERO) && opA.getDebitAmount().compareTo(sumList.get(opA)
                .stream().map(OPAccount::getDebitAmount).reduce(BigDecimal::add).get()) != 0) {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4))), MESS,
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(4)));
                isError = true;
            }
            if (opA.getCreditAmountOriginal().compareTo(sumList.get(opA)
                .stream().map(OPAccount::getCreditAmountOriginal).reduce(BigDecimal::add).get()) != 0) {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5))), MESS,
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(5)));
                isError = true;
            }
            if (!opA.getCreditAmount().equals(BigDecimal.ZERO) && opA.getCreditAmount().compareTo(sumList.get(opA)
                .stream().map(OPAccount::getCreditAmount).reduce(BigDecimal::add).get()) != 0) {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6))), MESS,
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(6)));
                isError = true;
            }

        }

//        if (!isError) {
//            opAccountRepository.deleteAllByTypeIdAndCompanyIdAndTypeLedger(TypeConstant.OPENING_BLANCE_AC, companyId, typeLedger);
//            opAccountRepository.insertBulk(opAccounts);
//            generalLedgerService.record(opAccounts, new MessageDTO(""));
//        }
        UpdateDataDTO dto = new UpdateDataDTO();
        if (!isError) {
            dto.setMessage("valid");
            dto.setResult(opAccounts);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            dto.setError(isError);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage(message);
        }
        workbook.close();
        return dto;
    }

    @Override
    public UpdateDataDTO uploadAccountingObject(MultipartFile file, String sheetName) throws IOException {
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        // init map data
        Map<String, BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findAllActiveCustom().stream()
            .collect(Collectors.toMap(BankAccountDetails::getBankAccount, bankAccountDetails1 -> bankAccountDetails1));
        Map<String, AccountingObject> accountingObjecs = accountingObjectService.getAccountingObjectsActiveCustom().stream()
            .collect(Collectors.toMap(AccountingObject::getAccountingObjectCode, accountingObject -> accountingObject));
        Map<String, UUID> statistics = statisticsCodeService.findAllActive().stream().collect(Collectors.toMap(StatisticsCode::getStatisticsCode, StatisticsCode::getId));
        Map<String, UUID> costSets = costSetService.findAllActive().stream().collect(Collectors.toMap(CostSet::getCostSetCode, CostSet::getId));
        Map<String, UUID> expenseItems = expenseItemService.findAllActive().stream().collect(Collectors.toMap(ExpenseItem::getExpenseItemCode, ExpenseItem::getId));
//        Map<String, UUID> emContract = emContractService.findAllActive().stream().collect(Collectors.toMap(typeLedger.equals(1) ? EMContract::getNoFBook : EMContract::getNoMBook, EMContract::getId));
        Map<String, UUID> organizations = organizationUnitService.findAllActive().stream().collect(Collectors.toMap(OrganizationUnit::getOrganizationUnitCode, OrganizationUnit::getId));
        Map<String, UUID> budgetItems = budgetItemService.findAllActive().stream().collect(Collectors.toMap(BudgetItem::getBudgetItemCode, BudgetItem::getId));
//      Map<String, UUID> currencies = currencyRepository.findByIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData()).stream().collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getId));
        List<Currency> currenciesRaw = currencyRepository.findByIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData());
        Map<String, UUID> currencies = currenciesRaw.stream().collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getId));

        Map<String, AccountList> stringAccountListMap = accountListRepository.findAllByIsActiveCustom(currentUserLoginAndOrg.get().getOrgGetData()).stream().map(dto -> {
            AccountList accountList = new AccountList();
            try {
                BeanUtils.copyProperties(accountList, dto);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return accountList;
        }).collect(Collectors.toMap(AccountList::getAccountNumber, accountList -> accountList));
        boolean isError = false;
        if (!currentUserLoginAndOrg.isPresent() || !user.isPresent()) {
            throw new BadRequestAlertException(Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT,
                    this.getClass().getName(),
                    Constants.UpdateDataDTOMessages.CURRENT_USER_IS_NOT_PRESENT);
        }
        Workbook workbook = null;
        String message = "";
        try {
            workbook = WorkbookFactory.create(file.getInputStream());

        } catch (Exception ignore) {
            isError = true;
            message = "invalidFile";
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }
        if (sheetName == null && workbook.getNumberOfSheets() > 1) {
            UpdateDataDTO dto = new UpdateDataDTO();
            List<String> sheetNames = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetAt(i).getSheetName());
            }
            dto.setError(true);
            dto.setResult(sheetNames);
            dto.setMessage(Constants.ImportExcel.SELECT_SHEET);
            return dto;
        }
        Sheet sheet;
        if (sheetName != null) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.getSheetAt(0);
        }
        if (sheet.getLastRowNum() < 1) {
            isError = true;
            message = "rowEmpty";
            UpdateDataDTO dto = new UpdateDataDTO();
            dto.setError(isError);
            dto.setMessage(message);
            return dto;
        }
        // Lấy ra row đầu tiên đọc header
        Row row1 = sheet.getRow(0);
        Iterator<Cell> cellIterator = row1.cellIterator();
        Map<String, Integer> headers = new HashMap<>();
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (ExcelConstant.Header.OP_ACCOUNT_OBJECT.contains(ExcelUtils.getCellValue(cell).toString())) {
                headers.put(ExcelUtils.getCellValue(cell).toString(), i);
            }
            i++;
        }
        // check các cột bặt buộc phải có trong file
        for (String header : ExcelConstant.Header.OP_ACCOUNT_OBJECT_REQUIRED) {
            if (headers.get(header) == null) {
                isError = true;
                message = "invalidFileFormat";
                UpdateDataDTO dto = new UpdateDataDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
        }

        List<OPAccount> opAccounts = new ArrayList<>();

        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            boolean checkAccountNumberAndCurrency = true;
            OPAccount opAccount = new OPAccount();

            Row row = sheet.getRow(j);
            // remove all comment
            for (String header: ExcelConstant.Header.OP_ACCOUNT_OBJECT) {
                ExcelUtils.removeComment(row.getCell(headers.get(header)));
            }
            String accostingObjectCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0))));
            String currencyId = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1))));
            BigDecimal exchangeRate = null;
            try {
                exchangeRate = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2)));
            }
            BigDecimal debitAmountOriginal = null;
            try {
                debitAmountOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3)));
            }
            BigDecimal debitAmount = null;
            try {
                debitAmount = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4)));
            }
            BigDecimal creditAmountOriginal = null;
            try {
                creditAmountOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5)));
            }
            BigDecimal creditAmount = null;
            try {
                creditAmount = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5)));
            }
            String accountNUmber = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1))));
            AccountList accountList = stringAccountListMap.get(accountNUmber);

            if (Strings.isNullOrEmpty(accountNUmber) && Strings.isNullOrEmpty(currencyId) && exchangeRate == null && debitAmountOriginal == null && creditAmountOriginal == null) {
                continue;
            }
            if (accountList == null && accountNUmber != null) {
                isError = true;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1) + "  không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1)));
                continue;
            }
            Integer statusAccount = accountList.getAccountGroupKind();
            if (Strings.isNullOrEmpty(accountNUmber)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1) + "  bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(14 - 1))));
                opAccount.setAccountNumber(accountNUmber);
                if (statusAccount.equals(1) && debitAmountOriginal != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3))), "Tài khoản có tính chất dư có, không thể nhập dư nợ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(3)));
                }
                if (statusAccount.equals(1) && debitAmount != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4))), "Tài khoản có tính chất dư có, không thể nhập dư nợ quy đổi",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4)));
                }

                if (statusAccount.equals(0) && creditAmountOriginal != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5))), "Tài khoản có tính chất dư nợ, không thể nhập dư có",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(5)));
                }
                if (statusAccount.equals(0) && creditAmount != null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6))), "Tài khoản có tính chất dư nợ, không thể nhập dư có quy đổi",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6)));
                }
            }
            if (Strings.isNullOrEmpty(accostingObjectCode) && Strings.isNullOrEmpty(accountNUmber) && Strings.isNullOrEmpty(currencyId) && exchangeRate == null && debitAmountOriginal == null && creditAmountOriginal == null) {
                continue;
            }

            if (Strings.isNullOrEmpty(accostingObjectCode)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0))), "Mã đối tượng  bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0)));
            } else {
                AccountingObject accountingObject = accountingObjecs.get(accostingObjectCode);
                if (accountingObject == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0))), "Mã đối tượng không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(0))));
                    opAccount.setAccountingObject(accountingObject);
                }
            }

            if (Strings.isNullOrEmpty(currencyId)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1))), "Loại tiền bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1)));
            } else {
                if (currencies.get(currencyId) == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1))),
                        "Mã loại tiền không tồn tại", row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(1))));
                    opAccount.setCurrencyId(currencyId);
                }
            }
            boolean isForeignCurrency = !currencyId.equals(organizationUnit.get().getCurrencyID());

            if (exchangeRate != null) {
                if (!isForeignCurrency &&  exchangeRate.equals(BigDecimal.ONE)) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2))), "Tỷ giá không khớp với loại tiền",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(2))));
                    opAccount.setExchangeRate(exchangeRate);
                }
            }

            if(!statusAccount.equals(1) && !isForeignCurrency && debitAmountOriginal != null && debitAmount != null & !debitAmountOriginal.equals(debitAmount)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4))), "Dư nợ quy đổi không khớp dư nợ",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4)));
            }

            if(!statusAccount.equals(0) && !isForeignCurrency && creditAmountOriginal != null && creditAmount != null && !creditAmountOriginal.equals(creditAmount)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6))), "Dư có quy đổi không khớp dư có",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6)));
            }
            //
            if (!statusAccount.equals(1) && isForeignCurrency && debitAmountOriginal != null && debitAmount == null) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(4))), "Dư nợ quy đổi bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6)));
            }
            if (!statusAccount.equals(0) && isForeignCurrency && creditAmountOriginal != null && creditAmount == null) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6))), "Dư có quy đổi bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(6)));
            }

            opAccount.setDebitAmountOriginal(debitAmountOriginal!= null ? debitAmountOriginal : BigDecimal.ZERO);
            opAccount.setDebitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO);
            opAccount.setCreditAmountOriginal(creditAmountOriginal!= null ? creditAmountOriginal : BigDecimal.ZERO);
            opAccount.setCreditAmount(creditAmount!= null ? creditAmount : BigDecimal.ZERO);

            String bankAccount = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(7))));
            String[] detailType = stringAccountListMap.get(opAccount.getAccountNumber()).getDetailType().split(";");
            if (Arrays.asList(detailType).contains(TypeConstant.ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT)) {
                if (Strings.isNullOrEmpty(bankAccount)) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7))), ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7) + " không được để trống",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_NORMAL.get(7)));
                }
            }
            if (!Strings.isNullOrEmpty(bankAccount)) {
                BankAccountDetails bankAccountDetails1 =  bankAccountDetails.get(bankAccount);
                if (bankAccountDetails1 == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(7))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(7) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(7)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(7))));
                    opAccount.setBankAccountDetails(bankAccountDetails1);
                }
            }

            String expenseItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(8))));
            if (!Strings.isNullOrEmpty(expenseItemCode)) {
                UUID expenseItemID = expenseItems.get(expenseItemCode);
                if (expenseItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(8))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(8) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(8)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(8))));
                    opAccount.setExpenseItemId(expenseItemID);
                }
            }
            String costSetCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(9))));
            if (!Strings.isNullOrEmpty(costSetCode)) {
                UUID cotSetID = costSets.get(costSetCode);
                if (cotSetID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(9))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(9) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(9)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(9))));
                    opAccount.setCostSetId(cotSetID);
                }
            }

//            String EMContractCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(10))));
//            if (!Strings.isNullOrEmpty(EMContractCode)) {
//                UUID contractID = emContract.get(EMContractCode);
//                if (contractID == null) {
//                    checkAccountNumberAndCurrency = false;
//                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(10))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(10) + " không hợp lệ",
//                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(10)));
//                } else {
//                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(10))));
//                    opAccount.setContractId(contractID);
//                }
//            }

            String budgetItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(11 - 1))));
            if (!Strings.isNullOrEmpty(budgetItemCode)) {
                UUID budgetItemID = budgetItems.get(budgetItemCode);
                if (budgetItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(11 - 1))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(11 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(11 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(11 - 1))));
                    opAccount.setBudgetItemId(budgetItemID);
                }
            }

            String departmentCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(12 - 1))));
            if (!Strings.isNullOrEmpty(departmentCode)) {
                UUID departmentID = organizations.get(departmentCode);
                if (departmentID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(12 - 1))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(12 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(12 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(12 - 1))));
                    opAccount.setDepartmentId(departmentID);
                }
            }

            String statisticsCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(13 - 1))));
            if (!Strings.isNullOrEmpty(statisticsCode)) {
                UUID statisticsID = statistics.get(statisticsCode);
                if (statisticsID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(13 - 1))), ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(13 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(13 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_ACCOUNT_OBJECT.get(13 - 1))));
                    opAccount.setStatisticsCodeId(statisticsID);
                }
            }

            if (checkAccountNumberAndCurrency) {
                opAccount.setCompanyId(companyId);
                opAccount.setTypeId(TypeConstant.OPENING_BLANCE_AC);
                opAccount.setTypeLedger(typeLedger);
                opAccount.setOrderPriority(j);
                opAccount.setPostedDate(organizationUnit.get().getStartDate().minusDays(1));
                opAccounts.add(opAccount);
            } else {
                isError = true;
            }
        }
//        if (!isError) {
//            opAccountRepository.deleteAllByTypeIdAndCompanyIdAndTypeLedger(TypeConstant.OPENING_BLANCE_AC, companyId, typeLedger);
//            opAccountRepository.insertBulk(opAccounts);
//            generalLedgerService.record(opAccounts, new MessageDTO(""));
//        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        UpdateDataDTO dto = new UpdateDataDTO();
        if (!isError) {
            dto.setMessage("valid");
            dto.setResult(opAccounts);
        } else {
            dto.setError(isError);
            workbook.write(bos);
            dto.setExcelFile(bos.toByteArray());
            dto.setMessage(message);
        }
        workbook.close();
        return dto;
    }

    @Override
    public Boolean getCheckHaveData(Integer typeId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        return opAccountRepository.existsByCompanyIdAndTypeLedgerAndTypeId(companyId, typeLedger, typeId);
    }

    @Override
    public Boolean acceptedOPAccount(List<OPAccount> opAccounts, Integer type) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        List<UUID> uuids = opAccountRepository.findAllUUIDForCompanyIdAndTypeLedgerAndType(companyId, typeLedger, type);
        if (!uuids.isEmpty()) {
            generalLedgerService.unrecord(uuids, false);
        }
        opAccountRepository.deleteAllByTypeIdAndCompanyIdAndTypeLedger(type, companyId, typeLedger);
        opAccountRepository.insertBulk(opAccounts);
        generalLedgerService.record(opAccounts, new MessageDTO(""));
        return true;
    }

    @Override
    public UpdateDataDTO checkReferenceData(List<UUID> uuid) {
        if (uuid.isEmpty()) {
            return new UpdateDataDTO().withMessage(
                    Constants.UpdateDataDTOMessages.SUCCESS);
        }
        return new UpdateDataDTO().withMessage(opAccountRepository.checkReferenceData(uuid) > 0 ?
                Constants.UpdateDataDTOMessages.HAD_REFERENCE : Constants.UpdateDataDTOMessages.SUCCESS);
    }
}
