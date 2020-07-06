package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.VoucherRefCatalogDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A RefVoucher.
 */
@Entity
@Table(name = "refvoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ViewVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "branchID", type = UUID.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "refTable", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewVoucherCompatDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RefVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = RefVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "refID1", type = UUID.class),
                    @ColumnResult(name = "refID2", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "attach", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RefVoucherSecondDTO",
        classes = {
            @ConstructorResult(
                targetClass = RefVoucherSecondDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "refID1", type = UUID.class),
                    @ColumnResult(name = "refID2", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "VoucherRefCatalogDTO",
        classes = {
            @ConstructorResult(
                targetClass = VoucherRefCatalogDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "UtilitiesSearchVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "refTable", type = String.class),
                }
            )
        }
    ),
})
public class RefVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "refid1")
    private UUID refID1;

    @Column(name = "refid2")
    private UUID refID2;

    @Column(name = "attach")
    private Boolean attach;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getRefID1() {
        return refID1;
    }

    public void setRefID1(UUID refID1) {
        this.refID1 = refID1;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }

    public Boolean getAttach() {
        return attach;
    }

    public void setAttach(Boolean attach) {
        this.attach = attach;
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
        RefVoucher refVoucher = (RefVoucher) o;
        if (refVoucher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), refVoucher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RefVoucher{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", refID1='" + getRefID1() + "'" +
            ", refID2='" + getRefID2() + "'" +
            "}";
    }
}
