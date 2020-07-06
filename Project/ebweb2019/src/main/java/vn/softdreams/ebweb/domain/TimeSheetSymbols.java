package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A TimeSheetSymbols.
 */
@Entity
@Table(name = "timesheetsymbols")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TimeSheetSymbols implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "timesheetsymbolscode", length = 25, nullable = false)
    private String timeSheetSymbolsCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "timesheetsymbolsname", length = 512, nullable = false)
    private String timeSheetSymbolsName;

    @Column(name = "salaryrate", precision = 10, scale = 2)
    private BigDecimal salaryRate;

    @Column(name = "isdefault")
    private Boolean isDefault;

    @Column(name = "ishalfdaydefault")
    private Boolean isHalfDayDefault;

    @Column(name = "ishalfday")
    private Boolean isHalfDay;

    @Column(name = "overtimesymbol")
    private Integer overTimeSymbol;

    @Column(name = "isovertime")
    private Boolean isOverTime;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTimeSheetSymbolsCode() {
        return timeSheetSymbolsCode;
    }

    public TimeSheetSymbols timeSheetSymbolsCode(String timeSheetSymbolsCode) {
        this.timeSheetSymbolsCode = timeSheetSymbolsCode;
        return this;
    }

    public void setTimeSheetSymbolsCode(String timeSheetSymbolsCode) {
        this.timeSheetSymbolsCode = timeSheetSymbolsCode;
    }

    public String getTimeSheetSymbolsName() {
        return timeSheetSymbolsName;
    }

    public TimeSheetSymbols timeSheetSymbolsName(String timeSheetSymbolsName) {
        this.timeSheetSymbolsName = timeSheetSymbolsName;
        return this;
    }

    public void setTimeSheetSymbolsName(String timeSheetSymbolsName) {
        this.timeSheetSymbolsName = timeSheetSymbolsName;
    }

    public BigDecimal getSalaryRate() {
        return salaryRate;
    }

    public TimeSheetSymbols salaryRate(BigDecimal salaryRate) {
        this.salaryRate = salaryRate;
        return this;
    }

    public void setSalaryRate(BigDecimal salaryRate) {
        this.salaryRate = salaryRate;
    }

    public Boolean isIsDefault() {
        return isDefault;
    }

    public TimeSheetSymbols isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean isIsHalfDayDefault() {
        return isHalfDayDefault;
    }

    public TimeSheetSymbols isHalfDayDefault(Boolean isHalfDayDefault) {
        this.isHalfDayDefault = isHalfDayDefault;
        return this;
    }

    public void setIsHalfDayDefault(Boolean isHalfDayDefault) {
        this.isHalfDayDefault = isHalfDayDefault;
    }

    public Boolean isIsHalfDay() {
        return isHalfDay;
    }

    public TimeSheetSymbols isHalfDay(Boolean isHalfDay) {
        this.isHalfDay = isHalfDay;
        return this;
    }

    public void setIsHalfDay(Boolean isHalfDay) {
        this.isHalfDay = isHalfDay;
    }

    public Integer getOverTimeSymbol() {
        return overTimeSymbol;
    }

    public TimeSheetSymbols overTimeSymbol(Integer overTimeSymbol) {
        this.overTimeSymbol = overTimeSymbol;
        return this;
    }

    public void setOverTimeSymbol(Integer overTimeSymbol) {
        this.overTimeSymbol = overTimeSymbol;
    }

    public Boolean isIsOverTime() {
        return isOverTime;
    }

    public TimeSheetSymbols isOverTime(Boolean isOverTime) {
        this.isOverTime = isOverTime;
        return this;
    }

    public void setIsOverTime(Boolean isOverTime) {
        this.isOverTime = isOverTime;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public TimeSheetSymbols isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public TimeSheetSymbols isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
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
        TimeSheetSymbols timeSheetSymbols = (TimeSheetSymbols) o;
        if (timeSheetSymbols.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeSheetSymbols.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeSheetSymbols{" +
            "id=" + getId() +
            ", timeSheetSymbolsCode='" + getTimeSheetSymbolsCode() + "'" +
            ", timeSheetSymbolsName='" + getTimeSheetSymbolsName() + "'" +
            ", salaryRate=" + getSalaryRate() +
            ", isDefault='" + isIsDefault() + "'" +
            ", isHalfDayDefault='" + isIsHalfDayDefault() + "'" +
            ", isHalfDay='" + isIsHalfDay() + "'" +
            ", overTimeSymbol=" + getOverTimeSymbol() +
            ", isOverTime='" + isIsOverTime() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
