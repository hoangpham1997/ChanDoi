package vn.softdreams.ebweb.domain;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewLotNo implements Serializable {

    private UUID companyID;
    private UUID materialGoodsID;
    private String lotNo;
    private LocalDate expiryDate;
    private BigDecimal totalIWQuantity;
    private BigDecimal totalOWQuantity;
    private Integer totalQuantityBalance;

    public ViewLotNo() {
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getTotalIWQuantity() {
        return totalIWQuantity;
    }

    public void setTotalIWQuantity(BigDecimal totalIWQuantity) {
        this.totalIWQuantity = totalIWQuantity;
    }

    public BigDecimal getTotalOWQuantity() {
        return totalOWQuantity;
    }

    public void setTotalOWQuantity(BigDecimal totalOWQuantity) {
        this.totalOWQuantity = totalOWQuantity;
    }

    public Integer getTotalQuantityBalance() {
        return totalQuantityBalance;
    }

    public void setTotalQuantityBalance(Integer totalQuantityBalance) {
        this.totalQuantityBalance = totalQuantityBalance;
    }
}
