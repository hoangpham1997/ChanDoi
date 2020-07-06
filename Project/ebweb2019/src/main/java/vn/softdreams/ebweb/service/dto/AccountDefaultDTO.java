package vn.softdreams.ebweb.service.dto;

public class AccountDefaultDTO {
    private Integer typeID;
    private String typeName;
    private String debitAccount;
    private String defaultDebitAccount;
    private String creditAccount;
    private String defaultCreditAccount;

    public AccountDefaultDTO() {
    }

    public AccountDefaultDTO(Integer typeID, String typeName, String debitAccount, String defaultDebitAccount, String creditAccount, String defaultCreditAccount) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.debitAccount = debitAccount;
        this.defaultDebitAccount = defaultDebitAccount;
        this.creditAccount = creditAccount;
        this.defaultCreditAccount = defaultCreditAccount;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getDefaultDebitAccount() {
        return defaultDebitAccount;
    }

    public void setDefaultDebitAccount(String defaultDebitAccount) {
        this.defaultDebitAccount = defaultDebitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDefaultCreditAccount() {
        return defaultCreditAccount;
    }

    public void setDefaultCreditAccount(String defaultCreditAccount) {
        this.defaultCreditAccount = defaultCreditAccount;
    }


}
