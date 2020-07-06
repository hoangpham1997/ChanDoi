package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;
import vn.softdreams.ebweb.domain.CPPeriodDetails;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EvaluateDTO {
    private UUID cPPeriodID;
    private List<CPUncompleteDTO> cPUncompletes;
    private List<GiaThanhPoPupDTO> cPExpenseLists1;
    private List<GiaThanhPoPupDTO> cPExpenseLists2;
    private List<CPPeriodDetails> cPPeriodDetails;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer method;
    private BigDecimal totalAmountCPEX0;
    private BigDecimal totalAmountCPEX1;
    private List<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails;

    public EvaluateDTO() {
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public List<GiaThanhPoPupDTO> getcPExpenseLists1() {
        return cPExpenseLists1;
    }

    public void setcPExpenseLists1(List<GiaThanhPoPupDTO> cPExpenseLists1) {
        this.cPExpenseLists1 = cPExpenseLists1;
    }

    public List<GiaThanhPoPupDTO> getcPExpenseLists2() {
        return cPExpenseLists2;
    }

    public void setcPExpenseLists2(List<GiaThanhPoPupDTO> cPExpenseLists2) {
        this.cPExpenseLists2 = cPExpenseLists2;
    }

    public List<CPPeriodDetails> getcPPeriodDetails() {
        return cPPeriodDetails;
    }

    public void setcPPeriodDetails(List<CPPeriodDetails> cPPeriodDetails) {
        this.cPPeriodDetails = cPPeriodDetails;
    }

    public List<CPUncompleteDTO> getcPUncompletes() {
        return cPUncompletes;
    }

    public void setcPUncompletes(List<CPUncompleteDTO> cPUncompletes) {
        this.cPUncompletes = cPUncompletes;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public List<CPAllocationGeneralExpenseDetails> getcPAllocationGeneralExpenseDetails() {
        return cPAllocationGeneralExpenseDetails;
    }

    public void setcPAllocationGeneralExpenseDetails(List<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails) {
        this.cPAllocationGeneralExpenseDetails = cPAllocationGeneralExpenseDetails;
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

    public BigDecimal getTotalAmountCPEX0() {
        return totalAmountCPEX0;
    }

    public void setTotalAmountCPEX0(BigDecimal totalAmountCPEX0) {
        this.totalAmountCPEX0 = totalAmountCPEX0;
    }

    public BigDecimal getTotalAmountCPEX1() {
        return totalAmountCPEX1;
    }

    public void setTotalAmountCPEX1(BigDecimal totalAmountCPEX1) {
        this.totalAmountCPEX1 = totalAmountCPEX1;
    }
}
