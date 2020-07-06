package vn.softdreams.ebweb.service.dto;

import java.util.List;
import java.util.UUID;

public class AccountListAllDTO {
    private List<AccountListDTO> deductionDebitAccount;
    private List<AccountListDTO> debitAccount;
    private List<AccountListDTO> vatAccount;
    private List<AccountListDTO> discountAccount;
    private List<AccountListDTO> creditAccount;
    private List<AccountListDTO> importTaxAccount;
    private List<AccountListDTO> exportTaxAccount;
    private List<AccountListDTO> repositoryAccount;
    private List<AccountListDTO> specialConsumeTaxAccount;
    private List<AccountListDTO> costAccount;
    private String deductionDebitAccountDefault;
    private String debitAccountDefault;
    private String vatAccountDefault;
    private String discountAccountDefault;
    private String creditAccountDefault;
    private String importTaxAccountDefault;
    private String exportTaxAccountDefault;
    private String repositoryAccountDefault;
    private String specialConsumeTaxAccountDefault;
    private String costAccountDefault;

    public AccountListAllDTO() {
    }

    public AccountListAllDTO(List<AccountListDTO> deductionDebitAccount, List<AccountListDTO> debitAccount, List<AccountListDTO> vatAccount, List<AccountListDTO> discountAccount, List<AccountListDTO> creditAccount, List<AccountListDTO> importTaxAccount, List<AccountListDTO> exportTaxAccount, List<AccountListDTO> repositoryAccount, List<AccountListDTO> specialConsumeTaxAccount, List<AccountListDTO> costAccount, String deductionDebitAccountDefault, String debitAccountDefault, String vatAccountDefault, String discountAccountDefault, String creditAccountDefault, String importTaxAccountDefault, String exportTaxAccountDefault, String repositoryAccountDefault, String specialConsumeTaxAccountDefault, String costAccountDefault) {
        this.deductionDebitAccount = deductionDebitAccount;
        this.debitAccount = debitAccount;
        this.vatAccount = vatAccount;
        this.discountAccount = discountAccount;
        this.creditAccount = creditAccount;
        this.importTaxAccount = importTaxAccount;
        this.exportTaxAccount = exportTaxAccount;
        this.repositoryAccount = repositoryAccount;
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
        this.costAccount = costAccount;
        this.deductionDebitAccountDefault = deductionDebitAccountDefault;
        this.debitAccountDefault = debitAccountDefault;
        this.vatAccountDefault = vatAccountDefault;
        this.discountAccountDefault = discountAccountDefault;
        this.creditAccountDefault = creditAccountDefault;
        this.importTaxAccountDefault = importTaxAccountDefault;
        this.exportTaxAccountDefault = exportTaxAccountDefault;
        this.repositoryAccountDefault = repositoryAccountDefault;
        this.specialConsumeTaxAccountDefault = specialConsumeTaxAccountDefault;
        this.costAccountDefault = costAccountDefault;
    }

    public String getDeductionDebitAccountDefault() {
        return deductionDebitAccountDefault;
    }

    public void setDeductionDebitAccountDefault(String deductionDebitAccountDefault) {
        this.deductionDebitAccountDefault = deductionDebitAccountDefault;
    }

    public String getDebitAccountDefault() {
        return debitAccountDefault;
    }

    public void setDebitAccountDefault(String debitAccountDefault) {
        this.debitAccountDefault = debitAccountDefault;
    }

    public String getVatAccountDefault() {
        return vatAccountDefault;
    }

    public void setVatAccountDefault(String vatAccountDefault) {
        this.vatAccountDefault = vatAccountDefault;
    }

    public String getDiscountAccountDefault() {
        return discountAccountDefault;
    }

    public void setDiscountAccountDefault(String discountAccountDefault) {
        this.discountAccountDefault = discountAccountDefault;
    }

    public String getCreditAccountDefault() {
        return creditAccountDefault;
    }

    public void setCreditAccountDefault(String creditAccountDefault) {
        this.creditAccountDefault = creditAccountDefault;
    }

    public String getImportTaxAccountDefault() {
        return importTaxAccountDefault;
    }

    public void setImportTaxAccountDefault(String importTaxAccountDefault) {
        this.importTaxAccountDefault = importTaxAccountDefault;
    }

    public String getExportTaxAccountDefault() {
        return exportTaxAccountDefault;
    }

    public void setExportTaxAccountDefault(String exportTaxAccountDefault) {
        this.exportTaxAccountDefault = exportTaxAccountDefault;
    }

    public String getRepositoryAccountDefault() {
        return repositoryAccountDefault;
    }

    public void setRepositoryAccountDefault(String repositoryAccountDefault) {
        this.repositoryAccountDefault = repositoryAccountDefault;
    }

    public String getSpecialConsumeTaxAccountDefault() {
        return specialConsumeTaxAccountDefault;
    }

    public void setSpecialConsumeTaxAccountDefault(String specialConsumeTaxAccountDefault) {
        this.specialConsumeTaxAccountDefault = specialConsumeTaxAccountDefault;
    }

    public String getCostAccountDefault() {
        return costAccountDefault;
    }

    public void setCostAccountDefault(String costAccountDefault) {
        this.costAccountDefault = costAccountDefault;
    }

    public List<AccountListDTO> getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(List<AccountListDTO> deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public List<AccountListDTO> getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(List<AccountListDTO> debitAccount) {
        this.debitAccount = debitAccount;
    }

    public List<AccountListDTO> getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(List<AccountListDTO> vatAccount) {
        this.vatAccount = vatAccount;
    }

    public List<AccountListDTO> getDiscountAccount() {
        return discountAccount;
    }

    public void setDiscountAccount(List<AccountListDTO> discountAccount) {
        this.discountAccount = discountAccount;
    }

    public List<AccountListDTO> getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(List<AccountListDTO> creditAccount) {
        this.creditAccount = creditAccount;
    }

    public List<AccountListDTO> getImportTaxAccount() {
        return importTaxAccount;
    }

    public void setImportTaxAccount(List<AccountListDTO> importTaxAccount) {
        this.importTaxAccount = importTaxAccount;
    }

    public List<AccountListDTO> getExportTaxAccount() {
        return exportTaxAccount;
    }

    public void setExportTaxAccount(List<AccountListDTO> exportTaxAccount) {
        this.exportTaxAccount = exportTaxAccount;
    }

    public List<AccountListDTO> getRepositoryAccount() {
        return repositoryAccount;
    }

    public void setRepositoryAccount(List<AccountListDTO> repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
    }

    public List<AccountListDTO> getSpecialConsumeTaxAccount() {
        return specialConsumeTaxAccount;
    }

    public void setSpecialConsumeTaxAccount(List<AccountListDTO> specialConsumeTaxAccount) {
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
    }

    public List<AccountListDTO> getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(List<AccountListDTO> costAccount) {
        this.costAccount = costAccount;
    }
}
