package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A PSSalaryTaxInsuranceRegulation.
 */
@Entity
@Table(name = "pssalarytaxinsuranceregulation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PSSalaryTaxInsuranceRegulation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fromdate")
    private LocalDate fromDate;

    @Column(name = "todate")
    private LocalDate toDate;

    @Column(name = "basicwage", precision = 10, scale = 2)
    private BigDecimal basicWage;

    @Column(name = "insuarancemaximumizesalary", precision = 10, scale = 2)
    private BigDecimal insuaranceMaximumizeSalary;

    @Column(name = "reduceselftaxamount", precision = 10, scale = 2)
    private BigDecimal reduceSelfTaxAmount;

    @Column(name = "reducedependtaxamount", precision = 10, scale = 2)
    private BigDecimal reduceDependTaxAmount;

    @Column(name = "workhoursinday", precision = 10, scale = 2)
    private BigDecimal workHoursInDay;

    @Column(name = "isworkingonmsaturday")
    private Boolean isWorkingOnMSaturday;

    @Column(name = "isworkingonmsunday")
    private Boolean isWorkingOnMSunday;

    @Column(name = "isworkingonnsaturday")
    private Boolean isWorkingOnNSaturday;

    @Column(name = "isworkingonnsunday")
    private Boolean isWorkingOnNSunday;

    @Column(name = "overtimedailypercent", precision = 10, scale = 2)
    private BigDecimal overtimeDailyPercent;

    @Column(name = "overtimeweekendpercent", precision = 10, scale = 2)
    private BigDecimal overtimeWeekendPercent;

    @Column(name = "overtimeholidaypercent", precision = 10, scale = 2)
    private BigDecimal overtimeHolidayPercent;

    @Column(name = "overtimeworkingdaynightpercent", precision = 10, scale = 2)
    private BigDecimal overtimeWorkingDayNightPercent;

    @Column(name = "overtimeweekenddaynightpercent", precision = 10, scale = 2)
    private BigDecimal overtimeWeekendDayNightPercent;

    @Column(name = "overtimeholidaynightpercent", precision = 10, scale = 2)
    private BigDecimal overtimeHolidayNightPercent;

    @Column(name = "companysocityinsurancepercent", precision = 10, scale = 2)
    private BigDecimal companySocityInsurancePercent;

    @Column(name = "companytaccidentinsurancepercent", precision = 10, scale = 2)
    private BigDecimal companytAccidentInsurancePercent;

    @Column(name = "companymedicalinsurancepercent", precision = 10, scale = 2)
    private BigDecimal companyMedicalInsurancePercent;

    @Column(name = "companyunemployeeinsurancepercent", precision = 10, scale = 2)
    private BigDecimal companyUnEmployeeInsurancePercent;

    @Column(name = "companytradeunioninsurancepercent", precision = 10, scale = 2)
    private BigDecimal companyTradeUnionInsurancePercent;

    @Column(name = "employeesocityinsurancepercent", precision = 10, scale = 2)
    private BigDecimal employeeSocityInsurancePercent;

    @Column(name = "employeeaccidentinsurancepercent", precision = 10, scale = 2)
    private BigDecimal employeeAccidentInsurancePercent;

    @Column(name = "employeemedicalinsurancepercent", precision = 10, scale = 2)
    private BigDecimal employeeMedicalInsurancePercent;

    @Column(name = "employeeunemployeeinsurancepercent", precision = 10, scale = 2)
    private BigDecimal employeeUnEmployeeInsurancePercent;

    @Column(name = "employeetradeunioninsurancepercent", precision = 10, scale = 2)
    private BigDecimal employeeTradeUnionInsurancePercent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public PSSalaryTaxInsuranceRegulation fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public PSSalaryTaxInsuranceRegulation toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getBasicWage() {
        return basicWage;
    }

    public PSSalaryTaxInsuranceRegulation basicWage(BigDecimal basicWage) {
        this.basicWage = basicWage;
        return this;
    }

    public void setBasicWage(BigDecimal basicWage) {
        this.basicWage = basicWage;
    }

    public BigDecimal getInsuaranceMaximumizeSalary() {
        return insuaranceMaximumizeSalary;
    }

    public PSSalaryTaxInsuranceRegulation insuaranceMaximumizeSalary(BigDecimal insuaranceMaximumizeSalary) {
        this.insuaranceMaximumizeSalary = insuaranceMaximumizeSalary;
        return this;
    }

    public void setInsuaranceMaximumizeSalary(BigDecimal insuaranceMaximumizeSalary) {
        this.insuaranceMaximumizeSalary = insuaranceMaximumizeSalary;
    }

    public BigDecimal getReduceSelfTaxAmount() {
        return reduceSelfTaxAmount;
    }

    public PSSalaryTaxInsuranceRegulation reduceSelfTaxAmount(BigDecimal reduceSelfTaxAmount) {
        this.reduceSelfTaxAmount = reduceSelfTaxAmount;
        return this;
    }

    public void setReduceSelfTaxAmount(BigDecimal reduceSelfTaxAmount) {
        this.reduceSelfTaxAmount = reduceSelfTaxAmount;
    }

    public BigDecimal getReduceDependTaxAmount() {
        return reduceDependTaxAmount;
    }

    public PSSalaryTaxInsuranceRegulation reduceDependTaxAmount(BigDecimal reduceDependTaxAmount) {
        this.reduceDependTaxAmount = reduceDependTaxAmount;
        return this;
    }

    public void setReduceDependTaxAmount(BigDecimal reduceDependTaxAmount) {
        this.reduceDependTaxAmount = reduceDependTaxAmount;
    }

    public BigDecimal getWorkHoursInDay() {
        return workHoursInDay;
    }

    public PSSalaryTaxInsuranceRegulation workHoursInDay(BigDecimal workHoursInDay) {
        this.workHoursInDay = workHoursInDay;
        return this;
    }

    public void setWorkHoursInDay(BigDecimal workHoursInDay) {
        this.workHoursInDay = workHoursInDay;
    }

    public Boolean isIsWorkingOnMSaturday() {
        return isWorkingOnMSaturday;
    }

    public PSSalaryTaxInsuranceRegulation isWorkingOnMSaturday(Boolean isWorkingOnMSaturday) {
        this.isWorkingOnMSaturday = isWorkingOnMSaturday;
        return this;
    }

    public void setIsWorkingOnMSaturday(Boolean isWorkingOnMSaturday) {
        this.isWorkingOnMSaturday = isWorkingOnMSaturday;
    }

    public Boolean isIsWorkingOnMSunday() {
        return isWorkingOnMSunday;
    }

    public PSSalaryTaxInsuranceRegulation isWorkingOnMSunday(Boolean isWorkingOnMSunday) {
        this.isWorkingOnMSunday = isWorkingOnMSunday;
        return this;
    }

    public void setIsWorkingOnMSunday(Boolean isWorkingOnMSunday) {
        this.isWorkingOnMSunday = isWorkingOnMSunday;
    }

    public Boolean isIsWorkingOnNSaturday() {
        return isWorkingOnNSaturday;
    }

    public PSSalaryTaxInsuranceRegulation isWorkingOnNSaturday(Boolean isWorkingOnNSaturday) {
        this.isWorkingOnNSaturday = isWorkingOnNSaturday;
        return this;
    }

    public void setIsWorkingOnNSaturday(Boolean isWorkingOnNSaturday) {
        this.isWorkingOnNSaturday = isWorkingOnNSaturday;
    }

    public Boolean isIsWorkingOnNSunday() {
        return isWorkingOnNSunday;
    }

    public PSSalaryTaxInsuranceRegulation isWorkingOnNSunday(Boolean isWorkingOnNSunday) {
        this.isWorkingOnNSunday = isWorkingOnNSunday;
        return this;
    }

    public void setIsWorkingOnNSunday(Boolean isWorkingOnNSunday) {
        this.isWorkingOnNSunday = isWorkingOnNSunday;
    }

    public BigDecimal getOvertimeDailyPercent() {
        return overtimeDailyPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeDailyPercent(BigDecimal overtimeDailyPercent) {
        this.overtimeDailyPercent = overtimeDailyPercent;
        return this;
    }

    public void setOvertimeDailyPercent(BigDecimal overtimeDailyPercent) {
        this.overtimeDailyPercent = overtimeDailyPercent;
    }

    public BigDecimal getOvertimeWeekendPercent() {
        return overtimeWeekendPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeWeekendPercent(BigDecimal overtimeWeekendPercent) {
        this.overtimeWeekendPercent = overtimeWeekendPercent;
        return this;
    }

    public void setOvertimeWeekendPercent(BigDecimal overtimeWeekendPercent) {
        this.overtimeWeekendPercent = overtimeWeekendPercent;
    }

    public BigDecimal getOvertimeHolidayPercent() {
        return overtimeHolidayPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeHolidayPercent(BigDecimal overtimeHolidayPercent) {
        this.overtimeHolidayPercent = overtimeHolidayPercent;
        return this;
    }

    public void setOvertimeHolidayPercent(BigDecimal overtimeHolidayPercent) {
        this.overtimeHolidayPercent = overtimeHolidayPercent;
    }

    public BigDecimal getOvertimeWorkingDayNightPercent() {
        return overtimeWorkingDayNightPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeWorkingDayNightPercent(BigDecimal overtimeWorkingDayNightPercent) {
        this.overtimeWorkingDayNightPercent = overtimeWorkingDayNightPercent;
        return this;
    }

    public void setOvertimeWorkingDayNightPercent(BigDecimal overtimeWorkingDayNightPercent) {
        this.overtimeWorkingDayNightPercent = overtimeWorkingDayNightPercent;
    }

    public BigDecimal getOvertimeWeekendDayNightPercent() {
        return overtimeWeekendDayNightPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeWeekendDayNightPercent(BigDecimal overtimeWeekendDayNightPercent) {
        this.overtimeWeekendDayNightPercent = overtimeWeekendDayNightPercent;
        return this;
    }

    public void setOvertimeWeekendDayNightPercent(BigDecimal overtimeWeekendDayNightPercent) {
        this.overtimeWeekendDayNightPercent = overtimeWeekendDayNightPercent;
    }

    public BigDecimal getOvertimeHolidayNightPercent() {
        return overtimeHolidayNightPercent;
    }

    public PSSalaryTaxInsuranceRegulation overtimeHolidayNightPercent(BigDecimal overtimeHolidayNightPercent) {
        this.overtimeHolidayNightPercent = overtimeHolidayNightPercent;
        return this;
    }

    public void setOvertimeHolidayNightPercent(BigDecimal overtimeHolidayNightPercent) {
        this.overtimeHolidayNightPercent = overtimeHolidayNightPercent;
    }

    public BigDecimal getCompanySocityInsurancePercent() {
        return companySocityInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation companySocityInsurancePercent(BigDecimal companySocityInsurancePercent) {
        this.companySocityInsurancePercent = companySocityInsurancePercent;
        return this;
    }

    public void setCompanySocityInsurancePercent(BigDecimal companySocityInsurancePercent) {
        this.companySocityInsurancePercent = companySocityInsurancePercent;
    }

    public BigDecimal getCompanytAccidentInsurancePercent() {
        return companytAccidentInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation companytAccidentInsurancePercent(BigDecimal companytAccidentInsurancePercent) {
        this.companytAccidentInsurancePercent = companytAccidentInsurancePercent;
        return this;
    }

    public void setCompanytAccidentInsurancePercent(BigDecimal companytAccidentInsurancePercent) {
        this.companytAccidentInsurancePercent = companytAccidentInsurancePercent;
    }

    public BigDecimal getCompanyMedicalInsurancePercent() {
        return companyMedicalInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation companyMedicalInsurancePercent(BigDecimal companyMedicalInsurancePercent) {
        this.companyMedicalInsurancePercent = companyMedicalInsurancePercent;
        return this;
    }

    public void setCompanyMedicalInsurancePercent(BigDecimal companyMedicalInsurancePercent) {
        this.companyMedicalInsurancePercent = companyMedicalInsurancePercent;
    }

    public BigDecimal getCompanyUnEmployeeInsurancePercent() {
        return companyUnEmployeeInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation companyUnEmployeeInsurancePercent(BigDecimal companyUnEmployeeInsurancePercent) {
        this.companyUnEmployeeInsurancePercent = companyUnEmployeeInsurancePercent;
        return this;
    }

    public void setCompanyUnEmployeeInsurancePercent(BigDecimal companyUnEmployeeInsurancePercent) {
        this.companyUnEmployeeInsurancePercent = companyUnEmployeeInsurancePercent;
    }

    public BigDecimal getCompanyTradeUnionInsurancePercent() {
        return companyTradeUnionInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation companyTradeUnionInsurancePercent(BigDecimal companyTradeUnionInsurancePercent) {
        this.companyTradeUnionInsurancePercent = companyTradeUnionInsurancePercent;
        return this;
    }

    public void setCompanyTradeUnionInsurancePercent(BigDecimal companyTradeUnionInsurancePercent) {
        this.companyTradeUnionInsurancePercent = companyTradeUnionInsurancePercent;
    }

    public BigDecimal getEmployeeSocityInsurancePercent() {
        return employeeSocityInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation employeeSocityInsurancePercent(BigDecimal employeeSocityInsurancePercent) {
        this.employeeSocityInsurancePercent = employeeSocityInsurancePercent;
        return this;
    }

    public void setEmployeeSocityInsurancePercent(BigDecimal employeeSocityInsurancePercent) {
        this.employeeSocityInsurancePercent = employeeSocityInsurancePercent;
    }

    public BigDecimal getEmployeeAccidentInsurancePercent() {
        return employeeAccidentInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation employeeAccidentInsurancePercent(BigDecimal employeeAccidentInsurancePercent) {
        this.employeeAccidentInsurancePercent = employeeAccidentInsurancePercent;
        return this;
    }

    public void setEmployeeAccidentInsurancePercent(BigDecimal employeeAccidentInsurancePercent) {
        this.employeeAccidentInsurancePercent = employeeAccidentInsurancePercent;
    }

    public BigDecimal getEmployeeMedicalInsurancePercent() {
        return employeeMedicalInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation employeeMedicalInsurancePercent(BigDecimal employeeMedicalInsurancePercent) {
        this.employeeMedicalInsurancePercent = employeeMedicalInsurancePercent;
        return this;
    }

    public void setEmployeeMedicalInsurancePercent(BigDecimal employeeMedicalInsurancePercent) {
        this.employeeMedicalInsurancePercent = employeeMedicalInsurancePercent;
    }

    public BigDecimal getEmployeeUnEmployeeInsurancePercent() {
        return employeeUnEmployeeInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation employeeUnEmployeeInsurancePercent(BigDecimal employeeUnEmployeeInsurancePercent) {
        this.employeeUnEmployeeInsurancePercent = employeeUnEmployeeInsurancePercent;
        return this;
    }

    public void setEmployeeUnEmployeeInsurancePercent(BigDecimal employeeUnEmployeeInsurancePercent) {
        this.employeeUnEmployeeInsurancePercent = employeeUnEmployeeInsurancePercent;
    }

    public BigDecimal getEmployeeTradeUnionInsurancePercent() {
        return employeeTradeUnionInsurancePercent;
    }

    public PSSalaryTaxInsuranceRegulation employeeTradeUnionInsurancePercent(BigDecimal employeeTradeUnionInsurancePercent) {
        this.employeeTradeUnionInsurancePercent = employeeTradeUnionInsurancePercent;
        return this;
    }

    public void setEmployeeTradeUnionInsurancePercent(BigDecimal employeeTradeUnionInsurancePercent) {
        this.employeeTradeUnionInsurancePercent = employeeTradeUnionInsurancePercent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PSSalaryTaxInsuranceRegulation pSSalaryTaxInsuranceRegulation = (PSSalaryTaxInsuranceRegulation) o;
        if (pSSalaryTaxInsuranceRegulation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pSSalaryTaxInsuranceRegulation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PSSalaryTaxInsuranceRegulation{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", basicWage=" + getBasicWage() +
            ", insuaranceMaximumizeSalary=" + getInsuaranceMaximumizeSalary() +
            ", reduceSelfTaxAmount=" + getReduceSelfTaxAmount() +
            ", reduceDependTaxAmount=" + getReduceDependTaxAmount() +
            ", workHoursInDay=" + getWorkHoursInDay() +
            ", isWorkingOnMSaturday='" + isIsWorkingOnMSaturday() + "'" +
            ", isWorkingOnMSunday='" + isIsWorkingOnMSunday() + "'" +
            ", isWorkingOnNSaturday='" + isIsWorkingOnNSaturday() + "'" +
            ", isWorkingOnNSunday='" + isIsWorkingOnNSunday() + "'" +
            ", overtimeDailyPercent=" + getOvertimeDailyPercent() +
            ", overtimeWeekendPercent=" + getOvertimeWeekendPercent() +
            ", overtimeHolidayPercent=" + getOvertimeHolidayPercent() +
            ", overtimeWorkingDayNightPercent=" + getOvertimeWorkingDayNightPercent() +
            ", overtimeWeekendDayNightPercent=" + getOvertimeWeekendDayNightPercent() +
            ", overtimeHolidayNightPercent=" + getOvertimeHolidayNightPercent() +
            ", companySocityInsurancePercent=" + getCompanySocityInsurancePercent() +
            ", companytAccidentInsurancePercent=" + getCompanytAccidentInsurancePercent() +
            ", companyMedicalInsurancePercent=" + getCompanyMedicalInsurancePercent() +
            ", companyUnEmployeeInsurancePercent=" + getCompanyUnEmployeeInsurancePercent() +
            ", companyTradeUnionInsurancePercent=" + getCompanyTradeUnionInsurancePercent() +
            ", employeeSocityInsurancePercent=" + getEmployeeSocityInsurancePercent() +
            ", employeeAccidentInsurancePercent=" + getEmployeeAccidentInsurancePercent() +
            ", employeeMedicalInsurancePercent=" + getEmployeeMedicalInsurancePercent() +
            ", employeeUnEmployeeInsurancePercent=" + getEmployeeUnEmployeeInsurancePercent() +
            ", employeeTradeUnionInsurancePercent=" + getEmployeeTradeUnionInsurancePercent() +
            "}";
    }
}
