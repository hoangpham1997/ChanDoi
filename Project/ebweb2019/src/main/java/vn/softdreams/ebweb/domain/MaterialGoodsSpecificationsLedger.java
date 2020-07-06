package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A MaterialGoodsSpecificationsLedger.
 */
@Entity
@Table(name = "materialgoodsspecificationsledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MaterialGoodsSpecificationsLedgerDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialGoodsSpecificationsLedgerDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "specification1", type = String.class),
                    @ColumnResult(name = "specification2", type = String.class),
                    @ColumnResult(name = "specification3", type = String.class),
                    @ColumnResult(name = "specification4", type = String.class),
                    @ColumnResult(name = "specification5", type = String.class),
                    @ColumnResult(name = "iWRepositoryID", type = UUID.class),
                    @ColumnResult(name = "oWRepositoryID", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "iWQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "oWQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "rowIndex", type = Integer.class)
                }
            )
        }
    )
})
public class MaterialGoodsSpecificationsLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "referenceid")
    private UUID referenceID;

    @Column(name = "detailid")
    private UUID detailID;

    @Column(name = "reftypeid")
    private Integer refTypeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "materialGoodsid")
    private UUID materialGoodsID;

    @Column(name = "specification1")
    private String specification1;

    @Column(name = "specification2")
    private String specification2;

    @Column(name = "specification3")
    private String specification3;

    @Column(name = "specification4")
    private String specification4;

    @Column(name = "specification5")
    private String specification5;

    @Column(name = "iwrepositoryid")
    private UUID iWRepositoryID;

    @Column(name = "owrepositoryid")
    private UUID oWRepositoryID;

    @Column(name = "iwquantity")
    private BigDecimal iWQuantity;

    @Column(name = "owquantity")
    private BigDecimal oWQuantity;

    @Column(name = "confrontid")
    private UUID confrontID;

    @Column(name = "confrontdetailid")
    private UUID confrontDetailID;

    @Column(name = "confronttypeid")
    private Integer confrontTypeID;

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

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public Integer getRefTypeID() {
        return refTypeID;
    }

    public void setRefTypeID(Integer refTypeID) {
        this.refTypeID = refTypeID;
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

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getSpecification1() {
        return specification1;
    }

    public void setSpecification1(String specification1) {
        this.specification1 = specification1;
    }

    public String getSpecification2() {
        return specification2;
    }

    public void setSpecification2(String specification2) {
        this.specification2 = specification2;
    }

    public String getSpecification3() {
        return specification3;
    }

    public void setSpecification3(String specification3) {
        this.specification3 = specification3;
    }

    public String getSpecification4() {
        return specification4;
    }

    public void setSpecification4(String specification4) {
        this.specification4 = specification4;
    }

    public String getSpecification5() {
        return specification5;
    }

    public void setSpecification5(String specification5) {
        this.specification5 = specification5;
    }

    public UUID getiWRepositoryID() {
        return iWRepositoryID;
    }

    public void setiWRepositoryID(UUID iWRepositoryID) {
        this.iWRepositoryID = iWRepositoryID;
    }

    public UUID getoWRepositoryID() {
        return oWRepositoryID;
    }

    public void setoWRepositoryID(UUID oWRepositoryID) {
        this.oWRepositoryID = oWRepositoryID;
    }

    public BigDecimal getiWQuantity() {
        return iWQuantity;
    }

    public void setiWQuantity(BigDecimal iWQuantity) {
        this.iWQuantity = iWQuantity;
    }

    public BigDecimal getoWQuantity() {
        return oWQuantity;
    }

    public void setoWQuantity(BigDecimal oWQuantity) {
        this.oWQuantity = oWQuantity;
    }

    public UUID getConfrontID() {
        return confrontID;
    }

    public void setConfrontID(UUID confrontID) {
        this.confrontID = confrontID;
    }

    public UUID getConfrontDetailID() {
        return confrontDetailID;
    }

    public void setConfrontDetailID(UUID confrontDetailID) {
        this.confrontDetailID = confrontDetailID;
    }

    public Integer getConfrontTypeID() {
        return confrontTypeID;
    }

    public void setConfrontTypeID(Integer confrontTypeID) {
        this.confrontTypeID = confrontTypeID;
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
        MaterialGoodsSpecificationsLedger materialGoodsSpecificationsLedger = (MaterialGoodsSpecificationsLedger) o;
        if (materialGoodsSpecificationsLedger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsSpecificationsLedger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsSpecificationsLedger{" +
            "id=" + getId() +
            "}";
    }
}
