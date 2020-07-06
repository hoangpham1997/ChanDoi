package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MaterialGoodsSpecificationsLedgerDTO {
    private UUID materialGoodsID;
    private String specification1;
    private String specification2;
    private String specification3;
    private String specification4;
    private String specification5;
    private UUID iWRepositoryID;
    private UUID oWRepositoryID;
    private String repositoryCode;
    private BigDecimal iWQuantity;
    private BigDecimal oWQuantity;
    private Integer rowIndex;

    public MaterialGoodsSpecificationsLedgerDTO(UUID materialGoodsID, String specification1, String specification2, String specification3, String specification4, String specification5, UUID iWRepositoryID, UUID oWRepositoryID, String repositoryCode, BigDecimal iWQuantity, BigDecimal oWQuantity,
                                                Integer rowIndex) {
        this.materialGoodsID = materialGoodsID;
        this.specification1 = specification1;
        this.specification2 = specification2;
        this.specification3 = specification3;
        this.specification4 = specification4;
        this.specification5 = specification5;
        this.iWRepositoryID = iWRepositoryID;
        this.oWRepositoryID = oWRepositoryID;
        this.repositoryCode = repositoryCode;
        this.iWQuantity = iWQuantity;
        this.oWQuantity = oWQuantity;
        this.rowIndex = rowIndex;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
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

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }
}
