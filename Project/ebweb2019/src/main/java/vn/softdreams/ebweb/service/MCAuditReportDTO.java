package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.MCAuditDetails;
import vn.softdreams.ebweb.domain.OrganizationUnit;

import java.util.UUID;

public class MCAuditReportDTO  {

    private String accountingObjectName;

    private String accountingObjectTitle;

    private String quantityString;

    private String amountString;

    private String valueOfMoneyString;

    public String getValueOfMoneyString() {
        return valueOfMoneyString;
    }

    public void setValueOfMoneyString(String valueOfMoney) {
        this.valueOfMoneyString = valueOfMoney;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectTitle() {
        return accountingObjectTitle;
    }

    public void setAccountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
    }
}



