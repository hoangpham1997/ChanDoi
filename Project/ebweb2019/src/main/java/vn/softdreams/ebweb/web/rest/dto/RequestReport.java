package vn.softdreams.ebweb.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Author Hautv
 */
public class RequestReport {
    private String typeReport;
    private LocalDate fromDate; // từ ngày
    private LocalDate toDate; // đến ngày
    private LocalDate createDate;
    private Boolean groupTheSameItem; // Cộng gộp các bút toán giống nhau
    private Boolean showAccumAmount; // Hiển thị số lũy kế kỳ trước chuyển sang
    private UUID companyID;
    private Boolean option;
    private List<String> accountList;
    private List<UUID> listCostSetID;
    private List<UUID> listExpenseItemID;
    private List<UUID> listMaterialGoods;
    private List<UUID> listCostSets;
    private List<UUID> listExpenseItems;
    private String accountNumber;
    private String bankAccountDetail;
    private UUID repositoryID;
    private Integer unitType;
    private String currencyID; // loại tiền
    private Boolean isSimilarSum;
    private Boolean isDependent;
    private Boolean isBill;
    private UUID materialGoodsCategoryID;
    private UUID employeeID;
    private List<UUID> accountingObjects;
    private String timeLineVoucher;
    private String fileName;
    private List<UUID> tools;
    private List<UUID> departments;
    private Boolean typeShowCurrency;
    private List<UUID> statisticsCodes;
    private List<UUID> expenseItems;

    private Integer grade;
    private Integer optionReport; // hóa đơn điện tử
    private UUID bankAccountDetailID;
    private Boolean getAmountOriginal;
    private UUID cPPeriodID;
    private Integer typeMethod;

    public List<UUID> getListExpenseItemID() {
        return listExpenseItemID;
    }

    public void setListExpenseItemID(List<UUID> listExpenseItemID) {
        this.listExpenseItemID = listExpenseItemID;
    }

    public Boolean getDependent() {
        return isDependent;
    }

    public List<UUID> getListCostSetID() {
        return listCostSetID;
    }

    public void setListCostSetID(List<UUID> listCostSetID) {
        this.listCostSetID = listCostSetID;
    }

    public void setDependent(Boolean dependent) {
        isDependent = dependent;
    }

    public Boolean getBill() {
        return isBill;
    }

    public void setBill(Boolean bill) {
        isBill = bill;
    }

    public Boolean getSimilarSum() {
        return isSimilarSum;
    }

    public void setSimilarSum(Boolean similarSum) {
        isSimilarSum = similarSum;
    }

    public String getBankAccountDetail() {
        return bankAccountDetail;
    }

    public void setBankAccountDetail(String bankAccountDetail) {
        this.bankAccountDetail = bankAccountDetail;
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Boolean getOption() {
        return option;
    }

    public void setOption(Boolean option) {
        this.option = option;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean getGroupTheSameItem() {
        return groupTheSameItem;
    }

    public void setGroupTheSameItem(Boolean groupTheSameItem) {
        this.groupTheSameItem = groupTheSameItem;
    }

    public Boolean getShowAccumAmount() {
        return showAccumAmount;
    }

    public void setShowAccumAmount(Boolean showAccumAmount) {
        this.showAccumAmount = showAccumAmount;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public List<UUID> getListMaterialGoods() {
        return listMaterialGoods;
    }

    public void setListMaterialGoods(List<UUID> listMaterialGoods) {
        this.listMaterialGoods = listMaterialGoods;
    }

    public Integer getOptionReport() {
        return optionReport;
    }

    public void setOptionReport(Integer optionReport) {
        this.optionReport = optionReport;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public UUID getMaterialGoodsCategoryID() {
        return materialGoodsCategoryID;
    }

    public void setMaterialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public List<UUID> getAccountingObjects() {
        return accountingObjects;
    }

    public void setAccountingObjects(List<UUID> accountingObjects) {
        this.accountingObjects = accountingObjects;
    }

    public String getTimeLineVoucher() {
        return timeLineVoucher;
    }

    public void setTimeLineVoucher(String timeLineVoucher) {
        this.timeLineVoucher = timeLineVoucher;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<UUID> getTools() {
        return tools;
    }

    public void setTools(List<UUID> tools) {
        this.tools = tools;
    }

    public Boolean getTypeShowCurrency() {
        return typeShowCurrency;
    }

    public void setTypeShowCurrency(Boolean typeShowCurrency) {
        this.typeShowCurrency = typeShowCurrency;
    }

    public Boolean getGetAmountOriginal() {
        return getAmountOriginal;
    }

    public void setGetAmountOriginal(Boolean getAmountOriginal) {
        this.getAmountOriginal = getAmountOriginal;
    }

    public List<UUID> getListExpenseItems() {
        return listExpenseItems;
    }

    public void setListExpenseItems(List<UUID> listExpenseItems) {
        this.listExpenseItems = listExpenseItems;
    }

    public List<UUID> getDepartments() {
        return departments;
    }

    public void setDepartments(List<UUID> departments) {
        this.departments = departments;
    }

    public List<UUID> getListCostSets() {
        return listCostSets;
    }

    public void setListCostSets(List<UUID> listCostSets) {
        this.listCostSets = listCostSets;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public Integer getTypeMethod() {
        return typeMethod;
    }

    public void setTypeMethod(Integer typeMethod) {
        this.typeMethod = typeMethod;
    }

    public List<UUID> getStatisticsCodes() {
        return statisticsCodes;
    }

    public void setStatisticsCodes(List<UUID> statisticsCodes) {
        this.statisticsCodes = statisticsCodes;
    }

    public List<UUID> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<UUID> expenseItems) {
        this.expenseItems = expenseItems;
    }
}
