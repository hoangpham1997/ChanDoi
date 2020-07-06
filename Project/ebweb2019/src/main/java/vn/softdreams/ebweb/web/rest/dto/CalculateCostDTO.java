package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.domain.CPPeriodDetails;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.service.dto.CPPeriodDetailDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;

import java.time.LocalDate;
import java.util.List;

public class CalculateCostDTO {
    private List<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails;
    private List<CPUncompleteDetails> cPUncompleteDetails;
    private List<CPAllocationRate> cPAllocationRates;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<GiaThanhPoPupDTO> cPExpenseLists1;
    private List<GiaThanhPoPupDTO> cPExpenseLists2;
    private List<CPPeriodDetails> cPPeriodDetails;

    public CalculateCostDTO() {
    }

    public List<CPAllocationGeneralExpenseDetails> getcPAllocationGeneralExpenseDetails() {
        return cPAllocationGeneralExpenseDetails;
    }

    public void setcPAllocationGeneralExpenseDetails(List<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails) {
        this.cPAllocationGeneralExpenseDetails = cPAllocationGeneralExpenseDetails;
    }

    public List<CPUncompleteDetails> getcPUncompleteDetails() {
        return cPUncompleteDetails;
    }

    public void setcPUncompleteDetails(List<CPUncompleteDetails> cPUncompleteDetails) {
        this.cPUncompleteDetails = cPUncompleteDetails;
    }

    public List<CPAllocationRate> getcPAllocationRates() {
        return cPAllocationRates;
    }

    public void setcPAllocationRates(List<CPAllocationRate> cPAllocationRates) {
        this.cPAllocationRates = cPAllocationRates;
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
}
