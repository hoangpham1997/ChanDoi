package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class LotNoDTO {
    private UUID companyID;
    private UUID materialGoodsID;
    private String lotNo;
    private LocalDate expiryDate;
    private BigDecimal totalIWQuantity;
    private BigDecimal totalOWQuantity;
    private BigDecimal totalQuantityBalance;

    public LotNoDTO() {
    }

    public LotNoDTO(UUID companyID, UUID materialGoodsID, String lotNo, LocalDate expiryDate, BigDecimal totalIWQuantity, BigDecimal totalOWQuantity, BigDecimal totalQuantityBalance) {
        this.companyID = companyID;
        this.materialGoodsID = materialGoodsID;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.totalIWQuantity = totalIWQuantity;
        this.totalOWQuantity = totalOWQuantity;
        this.totalQuantityBalance = totalQuantityBalance;
    }

    public LotNoDTO(String lotNo, LocalDate expiryDate) {
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
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

    public BigDecimal getTotalQuantityBalance() {
        return totalQuantityBalance;
    }

    public void setTotalQuantityBalance(BigDecimal totalQuantityBalance) {
        this.totalQuantityBalance = totalQuantityBalance;
    }
}
