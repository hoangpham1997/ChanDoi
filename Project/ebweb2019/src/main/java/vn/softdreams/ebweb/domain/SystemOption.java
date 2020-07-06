package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A SystemOption.
 */
@Entity
@Table(name = "systemoption")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PrivateToGeneralUse",
        classes = {
            @ConstructorResult(
                targetClass = PrivateToGeneralUse.class,
                columns = {
                    @ColumnResult(name = "iD", type = UUID.class),
                    @ColumnResult(name = "code", type = String.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "type", type = Integer.class),
                    @ColumnResult(name = "nameCategory", type = String.class),
                }
            )
        }
    )})
public class SystemOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "companyid")
    private UUID companyId;

    @Column(name = "branchid")
    private UUID branchId;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "type")
    private Integer type;

    @Size(max = 500)
    @Column(name = "data", length = 500)
    private String data;

    @Size(max = 500)
    @Column(name = "defaultdata", length = 500)
    private String defaultData;

    @Size(max = 500)
    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "issecurity")
    private Boolean isSecurity;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private List<UUID> companyIds = new ArrayList<>();

    public List<UUID> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<UUID> companyIds) {
        this.companyIds = companyIds;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userId;
    }

    public SystemOption userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserID(Long userId) {
        this.userId = userId;
    }

    public UUID getCompanyID() {
        return companyId;
    }

    public SystemOption companyId(UUID companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyID(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchID() {
        return branchId;
    }

    public SystemOption branchId(UUID branchId) {
        this.branchId = branchId;
        return this;
    }

    public void setBranchID(UUID branchId) {
        this.branchId = branchId;
    }

    public String getCode() {
        return code;
    }

    public SystemOption code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public SystemOption name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public SystemOption type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public SystemOption data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDefaultData() {
        return defaultData;
    }

    public SystemOption defaultData(String defaultData) {
        this.defaultData = defaultData;
        return this;
    }

    public void setDefaultData(String defaultData) {
        this.defaultData = defaultData;
    }

    public String getNote() {
        return note;
    }

    public SystemOption note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public SystemOption isSecurity(Boolean isSecurity) {
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
        SystemOption systemOption = (SystemOption) o;
        if (systemOption.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemOption.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemOption{" +
            "id=" + getId() +
            ", userId=" + getUserID() +
            ", companyId=" + getCompanyID() +
            ", branchId=" + getBranchID() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", Type=" + getType() +
            ", data='" + getData() + "'" +
            ", defaultData='" + getDefaultData() + "'" +
            ", note='" + getNote() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
