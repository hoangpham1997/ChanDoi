package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.service.util.DateUtil;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OPMaterialGoods.
 */
@Service
@Transactional
public class OPMaterialGoodsServiceImpl implements OPMaterialGoodsService {

    private final Logger log = LoggerFactory.getLogger(OPMaterialGoodsService.class);

    private final OPMaterialGoodsRepository opMaterialGoodsRepository;

    private final RepositoryLedgerRepository repositoryLedgerRepository;

    private final UserService userService;

    private final GeneralLedgerService generalLedgerService;

    private final OrganizationUnitRepository organizationUnitRepository;


    private final OrganizationUnitService organizationUnitService;

    private final RepositoryRepository repositoryRepository;

    private final SystemOptionRepository systemOptionRepository;

    private final MaterialGoodsRepository materialGoodsRepository;

    private final UnitRepository unitRepository;

    private final BankAccountDetailsService bankAccountDetailsService;

    private final StatisticsCodeService statisticsCodeService;

    private final CostSetService costSetService;

    private final ExpenseItemService expenseItemService;

//    private final EMContractService emContractService;

    private final BudgetItemService budgetItemService;

    private final UtilsService utilsService;
    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;


    public OPMaterialGoodsServiceImpl(OPMaterialGoodsRepository opMaterialGoodsRepository, RepositoryLedgerRepository repositoryLedgerRepository, UserService userService,
                                      GeneralLedgerService generalLedgerService, AccountListRepository accountListRepository, CurrencyRepository currencyRepository,
                                      BankAccountDetailsRepository bankAccountDetailsRepository,
                                      StatisticsCodeRepository statisticsCodeRepository,
                                      CostSetRepository costSetRepository, ExpenseItemRepository expenseItemRepository,
                                      EMContractRepository emContractRepository,
                                      OrganizationUnitRepository organizationUnitRepository,
                                      BudgetItemRepository budgetItemRepository,
                                      OrganizationUnitService organizationUnitService, RepositoryRepository repositoryRepository,
                                      SystemOptionRepository systemOptionRepository, MaterialGoodsRepository materialGoodsRepository, UnitRepository unitRepository,
                                      BankAccountDetailsService bankAccountDetailsService,
                                      StatisticsCodeService statisticsCodeService, AccountingObjectService accountingObjectService,
                                      CostSetService costSetService, ExpenseItemService expenseItemService,
//                                      EMContractService emContractService,
                                      BudgetItemService budgetItemService, UtilsService utilsService, MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.opMaterialGoodsRepository = opMaterialGoodsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.userService = userService;
        this.generalLedgerService = generalLedgerService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.organizationUnitService = organizationUnitService;
        this.repositoryRepository = repositoryRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.unitRepository = unitRepository;
        this.bankAccountDetailsService = bankAccountDetailsService;
        this.statisticsCodeService = statisticsCodeService;
        this.costSetService = costSetService;
        this.expenseItemService = expenseItemService;
//        this.emContractService = emContractService;
        this.budgetItemService = budgetItemService;
        this.utilsService = utilsService;
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    @Override
    public List<OPMaterialGoodsDTO> findAllByAccountNumber(String accountNumber, UUID repositoryId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        UserDTO account = userService.getAccount();
//        String organizationCurrencyId = account.getOrganizationUnit().getCurrencyID();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        return currentUserLoginAndOrg.map(securityDTO -> opMaterialGoodsRepository
                .findAllByAccountNumberAndCompanyIdAndTypeLedger(accountNumber, securityDTO.getOrg(), repositoryId, typeLedger))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<OPMaterialGoodsDTO> saveOPMaterialGoods(List<OPMaterialGoodsDTO> opMaterialGoods) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Map<UUID, Repository> repositories = repositoryRepository.findAllRepository(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMKho)).stream()
            .collect(Collectors.toMap(Repository::getId, repository -> repository));
        Map<UUID, BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findAllActiveCustom().stream()
            .collect(Collectors.toMap(BankAccountDetails::getId, bankAccountDetails1 -> bankAccountDetails1));
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
        Map<UUID, MaterialGoods> materialGoodsMap = materialGoodsRepository.findAllMaterialGoodsCustom(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH)).stream()
            .map(materialGoodsDTO -> {
                MaterialGoods materialGoods = new MaterialGoods();
                materialGoods.setId(materialGoodsDTO.getId());
                materialGoods.setMaterialGoodsCode(materialGoodsDTO.getMaterialGoodsCode());
                materialGoods.setMaterialGoodsName(materialGoodsDTO.getMaterialGoodsName());
                materialGoods.setUnitID(materialGoodsDTO.getUnitID());
                return materialGoods;
            })
            .collect(Collectors.toMap(MaterialGoods::getId, materialGoods1 -> materialGoods1));
        List<OPMaterialGoods> opMaterialGoodsLst = opMaterialGoods.stream()
            .map(opMDTO -> new OPMaterialGoods(opMDTO, currentUserLoginAndOrg.get().getOrg()))
            .collect(Collectors.toList());
        LocalDate postedDate = organizationUnit.get().getStartDate().minusDays(1);
        opMaterialGoodsLst = opMaterialGoodsLst.stream().peek(opm -> {
            opm.setMainUnitId(materialGoodsMap.get(opm.getMaterialGoodsId()).getUnitID());
            if (opm.getId() == null) {
                opm.setPostedDate(postedDate);
            }
        }).collect(Collectors.toList());
        opMaterialGoodsLst = setMainProperties(opMaterialGoodsLst, userDTO);
        List<OPMaterialGoods> opMaterialGoodsDTOList =  opMaterialGoodsRepository.saveAll(opMaterialGoodsLst);

        List<UUID> uuids = opMaterialGoods.stream().filter(x -> x.getId() != null).map(m -> m.getId()).collect(Collectors.toList());
        if (uuids.size() > 0) {
            materialGoodsSpecificationsLedgerRepository.deleteByListRefID(uuids);
        }
        List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers = new ArrayList<>();
        for (int i = 0; i < opMaterialGoods.size(); i++) {
            if (opMaterialGoods.get(i).getMaterialGoodsSpecificationsLedgers() != null &&
                opMaterialGoods.get(i).getMaterialGoodsSpecificationsLedgers().size() > 0) {
                for (MaterialGoodsSpecificationsLedger item: opMaterialGoods.get(i).getMaterialGoodsSpecificationsLedgers()) {
                    item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                    item.setDate(opMaterialGoodsDTOList.get(i).getPostedDate());
                    item.setPostedDate(opMaterialGoodsDTOList.get(i).getPostedDate());
                    item.setTypeLedger(opMaterialGoodsDTOList.get(i).getTypeLedger());
                    item.setRefTypeID(opMaterialGoodsDTOList.get(i).getTypeId());
                    item.setReferenceID(opMaterialGoodsDTOList.get(i).getId());
                    item.setDetailID(opMaterialGoodsDTOList.get(i).getId());
                    item.setNoFBook("OPN");
                    item.setNoMBook("OPN");
                    item.setiWRepositoryID(opMaterialGoodsDTOList.get(i).getRepositoryId());
                    item.setoWQuantity(BigDecimal.ZERO);
                    materialGoodsSpecificationsLedgers.add(item);
                }
            }
        }
        if (materialGoodsSpecificationsLedgers.size() > 0) {
            materialGoodsSpecificationsLedgerRepository.saveAll(materialGoodsSpecificationsLedgers);
        }

        generalLedgerService.record(opMaterialGoodsDTOList.stream().peek(op -> {
            op.setRepository(repositories.get(op.getRepositoryId()));
            op.setMaterialGoods(materialGoodsMap.get(op.getMaterialGoodsId()));
            if (op.getBankAccountDetailId() != null) {
                op.setBankAccountDetails(bankAccountDetails.get(op.getBankAccountDetailId()));
            }
        }).collect(Collectors.toList()), new MessageDTO(""));
        return opMaterialGoodsDTOList.stream().map(OPMaterialGoodsDTO::new).collect(Collectors.toList());
    }


    private List<OPMaterialGoods> setMainProperties(List<OPMaterialGoods> opMaterialGoods, UserDTO userDTO) {
        if (opMaterialGoods.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, Map<UUID, UnitConvertRateDTO>> convertMap = new HashMap<>();
        Map<String, Map<UUID, UUID>> uuidMapMap = new HashMap<>();
        opMaterialGoods.forEach(opm -> {
            if (opm.getUnitId() != null) {
                if (uuidMapMap.get(opm.getRepositoryId().toString()) == null) {
                    Map<UUID, UUID> opmMap = new HashMap<>();
                    opmMap.put(opm.getMaterialGoodsId(), opm.getUnitId());
                    uuidMapMap.put(opm.getRepositoryId().toString(), opmMap);
                } else {
                    Map<UUID, UUID> opmMap = uuidMapMap.get(opm.getRepositoryId().toString());
                    opmMap.put(opm.getMaterialGoodsId(), opm.getUnitId());
                }

                List<UnitConvertRateDTO> convertRateDTOs = unitRepository.getConvertUnitInfo(uuidMapMap);
                convertRateDTOs.forEach(ucr -> {
                    if (convertMap.get(ucr.getRepositoryId().toString()) == null) {
                        Map<UUID, UnitConvertRateDTO> opmMap = new HashMap<>();
                        opmMap.put(ucr.getMaterialGoodsID(), ucr);
                        convertMap.put(ucr.getRepositoryId().toString(), opmMap);
                    } else {
                        Map<UUID, UnitConvertRateDTO> opmMap = convertMap.get(ucr.getRepositoryId().toString());
                        opmMap.put(ucr.getMaterialGoodsID(), ucr);
                    }
                });
            }
        });

        int lamTronTienVN = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_TienVND)).findAny().get().getData());
        return opMaterialGoods.stream().peek(opm -> {
            if (opm.getUnitId() == null || opm.getMainUnitId() ==  null || opm.getUnitId().equals(opm.getMainUnitId())) {
                opm.setMainConvertRate(BigDecimal.ONE);
                opm.setFormula("*");
                opm.setMainQuantity(opm.getQuantity());
                opm.setMainUnitPrice(opm.getUnitPriceOriginal());
                opm.setMainQuantity(opm.getQuantity());
                opm.setMainUnitPrice(opm.getUnitPriceOriginal());
            } else {
                Map<UUID, UnitConvertRateDTO> result = convertMap
                    .get(opm.getRepositoryId().toString());
                if (result != null) {
                    UnitConvertRateDTO convertRateDTO = result.get(opm.getMaterialGoodsId());
                    opm.setMainConvertRate(convertRateDTO.getConvertRate());
                    opm.setFormula(convertRateDTO.getFormula());
                    if (convertRateDTO.getFormula().equals("*")) {
                        opm.setMainQuantity(Utils.round(opm.getQuantity().multiply(opm.getMainConvertRate()),
                            lamTronTienVN));
                        opm.setMainUnitPrice(opm.getUnitPriceOriginal().divide(opm.getMainConvertRate(), lamTronTienVN));
                    } else {
                        opm.setMainQuantity(opm.getQuantity().divide(opm.getMainConvertRate(), lamTronTienVN));
                        opm.setMainUnitPrice(opm.getUnitPriceOriginal().multiply(opm.getMainConvertRate()));
                    }
                }
            }

        }).collect(Collectors.toList());
    }
    @Override
    public byte[] exportPdf(String accountNumber, UUID repositoryId) {
        UserDTO account = userService.getAccount();
        List<OPMaterialGoodsDTO> opMaterialGoodsDTOList = this.findAllByAccountNumber(accountNumber, repositoryId);
        return PdfUtils.writeToFile(opMaterialGoodsDTOList, ExcelConstant.OPMaterialGoods.HEADER, ExcelConstant.OPMaterialGoods.FIELD, account);
    }

    public boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public UpdateDataDTO upload(MultipartFile file, String sheetName) throws IOException {
        UserDTO userDTO = userService.getAccount();
        Optional<UserSystemOption> user = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        // init map data
        Map<String, BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findAllActiveCustom().stream()
            .collect(Collectors.toMap(BankAccountDetails::getBankAccount, bankAccountDetails1 -> bankAccountDetails1));
        Map<String, UUID> statistics = statisticsCodeService.findAllActive().stream().collect(Collectors.toMap(StatisticsCode::getStatisticsCode, StatisticsCode::getId));

        Map<String, MaterialGoods> materialGoodsMap = materialGoodsRepository.findAllMaterialGoodsCustom(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH)).stream()
            .map(materialGoodsDTO -> {
                MaterialGoods materialGoods = new MaterialGoods();
                materialGoods.setId(materialGoodsDTO.getId());
                materialGoods.setMaterialGoodsCode(materialGoodsDTO.getMaterialGoodsCode());
                materialGoods.setMaterialGoodsName(materialGoodsDTO.getMaterialGoodsName());
                return materialGoods;
            })
            .collect(Collectors.toMap(MaterialGoods::getMaterialGoodsCode, materialGoods1 -> materialGoods1));

        Map<String, Repository> repositories = repositoryRepository.findAllRepositoryCustom(systemOptionRepository
                .getAllCompanyByCompanyIdAndCode(companyId,
                        Constants.SystemOption.TCKHAC_SDDMVTHH)).stream()
            .collect(Collectors.toMap(Repository::getRepositoryCode, repository -> repository));

//        Map<String, UUID> units = unitRepository.findAllByIsActive(utilsService.getOrgID()).stream()
//            .collect(Collectors.toMap(Unit::getUnitName, Unit::getId));
        List<Unit> unitsRaw = unitRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        Map<String, Unit> units = unitsRaw.stream()
            .collect(Collectors.toMap(x->x.getUnitName().toUpperCase(), x -> x));
        Map<String, UUID> mapUnits = unitRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData()).stream()
            .collect(Collectors.toMap(Unit::getUnitName, Unit::getId));
        Map<String, UUID> costSets = costSetService.findAllActive().stream().collect(Collectors.toMap(CostSet::getCostSetCode, CostSet::getId));
        Map<String, UUID> expenseItems = expenseItemService.findAllActive().stream().collect(Collectors.toMap(ExpenseItem::getExpenseItemCode, ExpenseItem::getId));
//        Map<String, UUID> emContract = emContractService.findAllActive().stream().collect(Collectors.toMap(typeLedger.equals(1) ? EMContract::getNoFBook : EMContract::getNoMBook, EMContract::getId));
        Map<String, UUID> organizations = organizationUnitService.findAllActive().stream().collect(Collectors.toMap(OrganizationUnit::getOrganizationUnitCode, OrganizationUnit::getId));
        Map<String, UUID> budgetItems = budgetItemService.findAllActive().stream().collect(Collectors.toMap(BudgetItem::getBudgetItemCode, BudgetItem::getId));
        Optional<OrganizationUnit> organizationUnit = organizationUnitRepository.findById(currentUserLoginAndOrg.get().getOrg());
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
            if (ExcelConstant.Header.OP_MATERIAL_GOODS.contains(ExcelUtils.getCellValue(cell).toString())) {
                headers.put(ExcelUtils.getCellValue(cell).toString(), i);
            }
            i++;
        }
        // check các cột bặt buộc phải có trong file
        for (String header : ExcelConstant.Header.OP_MATERIAL_GOODS_REQUIRED) {
            if (headers.get(header) == null) {
                isError = true;
                message = "invalidFileFormat";
                UpdateDataDTO dto = new UpdateDataDTO();
                dto.setError(isError);
                dto.setMessage(message);
                return dto;
            }
        }

        List<OPMaterialGoods> opMaterialGoodsList = new ArrayList<>();

        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            boolean checkAccountNumberAndCurrency = true;
            OPMaterialGoods opMaterialGoods = new OPMaterialGoods();
            Row row = sheet.getRow(j);
            boolean isBlankRow = true;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                String cellValueString = ExcelUtils.getCellValueString(row.getCell(k));
                if (!Strings.isNullOrEmpty(cellValueString)) {
                    isBlankRow = false;
                    break;
                }
            }
            if (isBlankRow) {
                continue;
            }
            for (String header: ExcelConstant.Header.OP_MATERIAL_GOODS) {
                ExcelUtils.removeComment(row.getCell(headers.get(header)));
            }
            String repositoryCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0))));
            String materialGoodsCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1))));
            String accountNUmber = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(15 - 1))));
            if (Strings.isNullOrEmpty(repositoryCode) && Strings.isNullOrEmpty(materialGoodsCode) && Strings.isNullOrEmpty(accountNUmber)) {
                //continue;
            }
            BigDecimal amountOriginal = null;
            try {
                amountOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(5))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(5))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(5) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(5)));
            }
            if (amountOriginal == null || amountOriginal.compareTo(BigDecimal.ZERO) == 0) {
                //continue;
            }
            if (amountOriginal instanceof BigDecimal) {
                opMaterialGoods.setAmountOriginal(amountOriginal);
                opMaterialGoods.setAmount(amountOriginal);
            } else {
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(5))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(5) + " không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(5)));
            }
            if (Strings.isNullOrEmpty(accountNUmber)) {
                isError = true;
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(15 - 1))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(15 - 1) + " không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(15 - 1)));
            } else {
                ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(15 - 1))));
                opMaterialGoods.setAccountNumber(accountNUmber);
            }
            if (Strings.isNullOrEmpty(repositoryCode)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0))), "Mã kho bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0)));
            } else {
                Repository repository = repositories.get(repositoryCode);
                if (repository == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0))), "Mã kho không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(0))));
                    opMaterialGoods.setRepository(repository);
                }

            }


            if (Strings.isNullOrEmpty(materialGoodsCode)) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1))), "Mã hàng bắt buộc nhập",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1)));
            } else {
                MaterialGoods mg = materialGoodsMap.get(materialGoodsCode);
                if (mg == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1))), "Mã hàng không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(1))));
                    opMaterialGoods.setMaterialGoods(mg);
                }
            }
            // Đơn vị tính
            String unitName = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(2))));
            if (!Strings.isNullOrEmpty(unitName)) {
                if (units.containsKey(unitName.toUpperCase())) {
                    opMaterialGoods.setUnitId(units.get(unitName.toUpperCase()).getId());
                } else {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(2))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(2) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(2)));
                }
            }
            //Số lượng
            BigDecimal quantity = null;
            try {
                quantity = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(3))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(3))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(3) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(3)));
            }
            if (quantity instanceof BigDecimal) {
                opMaterialGoods.setQuantity(quantity);
            } else {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(3))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(3) + " không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(3)));
            }
            // Đơn giá
            BigDecimal unitPriceOriginal = null;
            try {
                unitPriceOriginal = ExcelUtils.getCellValueBigDecimal(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(4))));
            } catch (NumberFormatException e) {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(4))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(4) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(4)));
            }
            if (unitPriceOriginal instanceof  BigDecimal) {
                opMaterialGoods.setUnitPriceOriginal(unitPriceOriginal);
            } else {
                checkAccountNumberAndCurrency = false;
                ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(4))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(4) + " không hợp lệ",
                    row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(4)));
            }

            String lotNo = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(6))));
            opMaterialGoods.setLotNo(lotNo);

            String expiryDateStr = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7))));
            if (!Strings.isNullOrEmpty(expiryDateStr)) {
                LocalDate expiryDate = DateUtil.getLocalDateFromString(expiryDateStr, DateUtil.C_DD_MM_YYYY);
                if (expiryDateStr == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(7) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7)));
                } else if (isThisDateValid(expiryDateStr, "dd/MM/yyyy")){
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7))));
                    opMaterialGoods.setExpiryDate(expiryDate);
                } else {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(7) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(7)));
                }

            }

            String bankAccount = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(8))));
            if (!Strings.isNullOrEmpty(bankAccount)) {
                BankAccountDetails bankAccountDetails1 =  bankAccountDetails.get(bankAccount);
                if (bankAccountDetails1 == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(8))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(8) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(8)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(8))));
                    opMaterialGoods.setBankAccountDetails(bankAccountDetails1);
                }
            }

            String expenseItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(9))));
            if (!Strings.isNullOrEmpty(expenseItemCode)) {
                UUID expenseItemID = expenseItems.get(expenseItemCode);
                if (expenseItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(9))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(9) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(9)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(9))));
                    opMaterialGoods.setExpenseItemId(expenseItemID);
                }
            }

            String costSetCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(10))));
            if (!Strings.isNullOrEmpty(costSetCode)) {
                UUID cotSetID = costSets.get(costSetCode);
                if (cotSetID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(10))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(10) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(10)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(10))));
                    opMaterialGoods.setCostSetId(cotSetID);
                }
            }

//            String EMContractCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(11))));
//            if (!Strings.isNullOrEmpty(EMContractCode)) {
//                UUID contractID = emContract.get(EMContractCode);
//                if (contractID == null) {
//                    checkAccountNumberAndCurrency = false;
//                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(11))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(11) + " không hợp lệ",
//                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(11)));
//                } else {
//                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(11))));
//                    opMaterialGoods.setContractId(contractID);
//                }
//            }

            String budgetItemCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(12 - 1))));
            if (!Strings.isNullOrEmpty(budgetItemCode)) {
                UUID budgetItemID = budgetItems.get(budgetItemCode);
                if (budgetItemID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(12 - 1))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(12 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(12 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(12 - 1))));
                    opMaterialGoods.setBudgetItemId(budgetItemID);
                }
            }

            String departmentCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(13 - 1))));
            if (!Strings.isNullOrEmpty(departmentCode)) {
                UUID departmentID = organizations.get(departmentCode);
                if (departmentID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(13 - 1))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(13 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(13 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(13 - 1))));
                    opMaterialGoods.setDepartmentId(departmentID);
                }
            }

            String statisticsCode = ExcelUtils.getCellValueString(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(14 - 1))));
            if (!Strings.isNullOrEmpty(statisticsCode)) {
                UUID statisticsID = statistics.get(statisticsCode);
                if (statisticsID == null) {
                    checkAccountNumberAndCurrency = false;
                    ExcelUtils.setComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(14 - 1))), ExcelConstant.Header.OP_MATERIAL_GOODS.get(14 - 1) + " không hợp lệ",
                        row, headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(14 - 1)));
                } else {
                    ExcelUtils.removeComment(row.getCell(headers.get(ExcelConstant.Header.OP_MATERIAL_GOODS.get(14 - 1))));
                    opMaterialGoods.setStatisticsCodeId(statisticsID);
                }
            }

            if (checkAccountNumberAndCurrency) {
                opMaterialGoods.setCompanyId(companyId);
                opMaterialGoods.setTypeId(TypeConstant.OPENING_BLANCE_MT);
                opMaterialGoods.setTypeLedger(typeLedger);
                opMaterialGoods.setCurrencyId(organizationUnit.get().getCurrencyID());
                opMaterialGoods.setExchangeRate(BigDecimal.ONE);
                opMaterialGoods.setOrderPriority(j);
                opMaterialGoods.setPostedDate(organizationUnit.get().getStartDate().minusDays(1));
                opMaterialGoodsList.add(opMaterialGoods);
            } else {
                isError = true;
            }
        }
        UpdateDataDTO dto = new UpdateDataDTO();
        if (!isError) {
            opMaterialGoodsList = opMaterialGoodsList.stream().peek(opm -> opm.setMainUnitId(materialGoodsMap.get(opm.getMaterialGoods().getMaterialGoodsCode()).getUnitID())).collect(Collectors.toList());
            opMaterialGoodsList = setMainProperties(opMaterialGoodsList, userDTO);
//            opMaterialGoodsRepository.deleteAllByTypeIdAndCompanyIdAndTypeLedger(TypeConstant.OPENING_BLANCE_MT, companyId, typeLedger);
//            opMaterialGoodsRepository.insertBulk(opMaterialGoodsList);
//            generalLedgerService.record(opMaterialGoodsList, new MessageDTO(""));
            dto.setMessage("valid");
            dto.setResult(opMaterialGoodsList);
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
    public UpdateDataDTO deleteOPAccountByIds(List<UUID> uuids) {
        try {
            generalLedgerService.unrecord(uuids, false);
            repositoryLedgerRepository.deleteAllByReferenceID(uuids);
            opMaterialGoodsRepository.deleteByIds(uuids);
            return new UpdateDataDTO(Constants.UpdateDataDTOMessages.DELETE_SUCCESS);
        } catch (Exception e) {
            return new UpdateDataDTO(Constants.UpdateDataDTOMessages.FAIL);
        }
    }

    @Override
    public Boolean getCheckHaveData() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        return opMaterialGoodsRepository.existsByCompanyIdAndTypeLedger(companyId, typeLedger);
    }

    @Override
    public Boolean acceptedOPMaterialGoods(List<OPMaterialGoods> opMaterialGoodsList) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyId = currentUserLoginAndOrg.get().getOrg();
        Integer typeLedger = Utils.PhienSoLamViec(userService.getAccount());
        List<UUID> uuids = opMaterialGoodsRepository.findAllUUIDForCompanyIDAndTypeLedger(companyId, typeLedger);
        if (!uuids.isEmpty()) {
            generalLedgerService.unrecord(uuids, true);
        }
        opMaterialGoodsRepository.deleteAllByTypeIdAndCompanyIdAndTypeLedger(TypeConstant.OPENING_BLANCE_MT, companyId, typeLedger);
        opMaterialGoodsRepository.insertBulk(opMaterialGoodsList);
        generalLedgerService.record(opMaterialGoodsList, new MessageDTO(""));
        return true;
    }
}
