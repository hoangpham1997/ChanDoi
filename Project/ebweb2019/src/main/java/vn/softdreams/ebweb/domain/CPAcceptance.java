package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAcceptanceDTO;
import vn.softdreams.ebweb.service.dto.CPAcceptanceDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAcceptance.
 */
@Entity
@Table(name = "cpacceptance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAcceptanceDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAcceptanceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class)
                }
            )
        }
    )})
public class CPAcceptance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "no")
    private String no;

    @Size(max = 512)
    @Column(name = "description")
    private String description;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "totalamount")
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal")
    private BigDecimal totalAmountOriginal;

    @Column(name = "gothervoucherid")
    private UUID gOtherVoucherID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
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
        CPAcceptance cPAcceptance = (CPAcceptance) o;
        if (cPAcceptance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAcceptance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAcceptance{" +
            "id=" + getId() +
            "}";
    }
}
