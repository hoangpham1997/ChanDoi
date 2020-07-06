package vn.softdreams.ebweb.web.rest.dto;

import java.util.UUID;

public class PPOrderSearchDTO {
    private UUID accountingObject;
    private Integer status;
    private String currency;
    private UUID employee;
    private String fromDate;
    private String toDate;
    private String searchValue;

    public PPOrderSearchDTO() {
    }

    public PPOrderSearchDTO(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue) {
        this.accountingObject = accountingObject;
        this.status = status;
        this.currency = currency;
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.searchValue = searchValue;
    }

    public UUID getAccountingObject() {
        return accountingObject;
    }

    public void setAccountingObject(UUID accountingObject) {
        this.accountingObject = accountingObject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UUID getEmployee() {
        return employee;
    }

    public void setEmployee(UUID employee) {
        this.employee = employee;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
