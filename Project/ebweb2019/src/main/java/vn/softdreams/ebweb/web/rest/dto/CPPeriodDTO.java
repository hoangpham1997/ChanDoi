package vn.softdreams.ebweb.web.rest.dto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.*;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CPPeriodDTO {
    private UUID id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String name;
    private BigDecimal totalIncompleteOpenning;
    private BigDecimal totalAmountInPeriod;
    private BigDecimal totalIncompleteClosing;
    private BigDecimal totalCost;
    private Integer type;
    private List<CPPeriodDetailDTO> cPPeriodDetails;
    private List<CPExpenseListDTO> cPExpenseList;
    private List<CPAllocationExpenseDTO> cPAllocationGeneralExpenses;
    private List<CPAllocationExpenseDetailDTO> cPAllocationGeneralExpenseDetails;
    private List<CPUncompletesDTO> cPUncompletes;
    private List<CPUncompleteDetailDTO> cPUncompleteDetails;
    private List<CPResultDTO> cPResults;
    private List<CPAllocationRateDTO> cPAllocationRates;
    private List<CPAcceptanceDTO> cPAcceptances;
    private List<CPAcceptanceDetailDTO> cPAcceptanceDetails;
    private String debitAccount;
    private String creditAccount;

    public CPPeriodDTO(UUID id, LocalDate fromDate, LocalDate toDate, String name, BigDecimal totalIncompleteOpenning, BigDecimal totalAmountInPeriod, BigDecimal totalIncompleteClosing, BigDecimal totalCost,
                       Integer type) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.totalIncompleteOpenning = totalIncompleteOpenning;
        this.totalAmountInPeriod = totalAmountInPeriod;
        this.totalIncompleteClosing = totalIncompleteClosing;
        this.totalCost = totalCost;
        this.type = type;
    }

    public CPPeriodDTO(UUID id, LocalDate fromDate, LocalDate toDate, String name, Integer type) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.type = type;
    }

    public CPPeriodDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalIncompleteOpenning() {
        return totalIncompleteOpenning;
    }

    public void setTotalIncompleteOpenning(BigDecimal totalIncompleteOpenning) {
        this.totalIncompleteOpenning = totalIncompleteOpenning;
    }

    public BigDecimal getTotalAmountInPeriod() {
        return totalAmountInPeriod;
    }

    public void setTotalAmountInPeriod(BigDecimal totalAmountInPeriod) {
        this.totalAmountInPeriod = totalAmountInPeriod;
    }

    public BigDecimal getTotalIncompleteClosing() {
        return totalIncompleteClosing;
    }

    public void setTotalIncompleteClosing(BigDecimal totalIncompleteClosing) {
        this.totalIncompleteClosing = totalIncompleteClosing;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<CPPeriodDetailDTO> getcPPeriodDetails() {
        return cPPeriodDetails;
    }

    public void setcPPeriodDetails(List<CPPeriodDetailDTO> cPPeriodDetails) {
        this.cPPeriodDetails = cPPeriodDetails;
    }

    public List<CPExpenseListDTO> getcPExpenseList() {
        return cPExpenseList;
    }

    public void setcPExpenseList(List<CPExpenseListDTO> cPExpenseList) {
        this.cPExpenseList = cPExpenseList;
    }

    public List<CPAllocationExpenseDTO> getcPAllocationGeneralExpenses() {
        return cPAllocationGeneralExpenses;
    }

    public void setcPAllocationGeneralExpenses(List<CPAllocationExpenseDTO> cPAllocationGeneralExpenses) {
        this.cPAllocationGeneralExpenses = cPAllocationGeneralExpenses;
    }

    public List<CPAllocationExpenseDetailDTO> getcPAllocationGeneralExpenseDetails() {
        return cPAllocationGeneralExpenseDetails;
    }

    public void setcPAllocationGeneralExpenseDetails(List<CPAllocationExpenseDetailDTO> cPAllocationGeneralExpenseDetails) {
        this.cPAllocationGeneralExpenseDetails = cPAllocationGeneralExpenseDetails;
    }

    public List<CPUncompletesDTO> getcPUncompletes() {
        return cPUncompletes;
    }

    public void setcPUncompletes(List<CPUncompletesDTO> cPUncompletes) {
        this.cPUncompletes = cPUncompletes;
    }

    public List<CPUncompleteDetailDTO> getcPUncompleteDetails() {
        return cPUncompleteDetails;
    }

    public void setcPUncompleteDetails(List<CPUncompleteDetailDTO> cPUncompleteDetails) {
        this.cPUncompleteDetails = cPUncompleteDetails;
    }

    public List<CPResultDTO> getcPResults() {
        return cPResults;
    }

    public void setcPResults(List<CPResultDTO> cPResults) {
        this.cPResults = cPResults;
    }

    public List<CPAllocationRateDTO> getcPAllocationRates() {
        return cPAllocationRates;
    }

    public void setcPAllocationRates(List<CPAllocationRateDTO> cPAllocationRates) {
        this.cPAllocationRates = cPAllocationRates;
    }

    public List<CPAcceptanceDTO> getcPAcceptances() {
        return cPAcceptances;
    }

    public void setcPAcceptances(List<CPAcceptanceDTO> cPAcceptances) {
        this.cPAcceptances = cPAcceptances;
    }

    public List<CPAcceptanceDetailDTO> getcPAcceptanceDetails() {
        return cPAcceptanceDetails;
    }

    public void setcPAcceptanceDetails(List<CPAcceptanceDetailDTO> cPAcceptanceDetails) {
        this.cPAcceptanceDetails = cPAcceptanceDetails;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }
}
