package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TongHopTonKhoDTO {
    private UUID repositoryID;
    private String repositoryCode;
    private String repositoryName;
    private UUID materialGoodsID;
    private String unitName;
    private String materialGoodsCode;
    private String materialGoodsName;
    private BigDecimal openingQuantity;
    private BigDecimal openingAmount;
    private BigDecimal iwQuantity;
    private BigDecimal iwAmount;
    private BigDecimal owQuantity;
    private BigDecimal owAmount;
    private BigDecimal closingQuantity;
    private BigDecimal closingAmount;
    private String slDauKy;
    private String tienDauKy;
    private String slNhap;
    private String tienNhap;
    private String slXuat;
    private String tienXuat;
    private String slTon;
    private String tienTon;
    private String tongSLDauKy;
    private String tongTienDauKy;
    private String tongSLNhap;
    private String tongTienNhap;
    private String tongSLXuat;
    private String tongTienXuat;
    private String tongSLTon;
    private String tongTienTon;
    private Boolean loop;

    public TongHopTonKhoDTO(UUID repositoryID, String repositoryCode, String repositoryName, UUID materialGoodsID, String unitName, String materialGoodsCode, String materialGoodsName, BigDecimal openingQuantity, BigDecimal openingAmount, BigDecimal iwQuantity, BigDecimal iwAmount, BigDecimal owQuantity, BigDecimal owAmount, BigDecimal closingQuantity, BigDecimal closingAmount) {
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.repositoryName = repositoryName;
        this.materialGoodsID = materialGoodsID;
        this.unitName = unitName;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.openingQuantity = openingQuantity;
        this.openingAmount = openingAmount;
        this.iwQuantity = iwQuantity;
        this.iwAmount = iwAmount;
        this.owQuantity = owQuantity;
        this.owAmount = owAmount;
        this.closingQuantity = closingQuantity;
        this.closingAmount = closingAmount;
    }

    public TongHopTonKhoDTO(BigDecimal openingQuantity, BigDecimal openingAmount, BigDecimal iwQuantity, BigDecimal iwAmount, BigDecimal owQuantity, BigDecimal owAmount, BigDecimal closingQuantity, BigDecimal closingAmount) {
        this.openingQuantity = openingQuantity;
        this.openingAmount = openingAmount;
        this.iwQuantity = iwQuantity;
        this.iwAmount = iwAmount;
        this.owQuantity = owQuantity;
        this.owAmount = owAmount;
        this.closingQuantity = closingQuantity;
        this.closingAmount = closingAmount;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public BigDecimal getOpeningQuantity() {
        return openingQuantity;
    }

    public void setOpeningQuantity(BigDecimal openingQuantity) {
        this.openingQuantity = openingQuantity;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public BigDecimal getIwQuantity() {
        return iwQuantity;
    }

    public void setIwQuantity(BigDecimal iwQuantity) {
        this.iwQuantity = iwQuantity;
    }

    public BigDecimal getIwAmount() {
        return iwAmount;
    }

    public void setIwAmount(BigDecimal iwAmount) {
        this.iwAmount = iwAmount;
    }

    public BigDecimal getOwQuantity() {
        return owQuantity;
    }

    public void setOwQuantity(BigDecimal owQuantity) {
        this.owQuantity = owQuantity;
    }

    public BigDecimal getOwAmount() {
        return owAmount;
    }

    public void setOwAmount(BigDecimal owAmount) {
        this.owAmount = owAmount;
    }

    public BigDecimal getClosingQuantity() {
        return closingQuantity;
    }

    public void setClosingQuantity(BigDecimal closingQuantity) {
        this.closingQuantity = closingQuantity;
    }

    public BigDecimal getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(BigDecimal closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getSlDauKy() {
        return slDauKy;
    }

    public void setSlDauKy(String slDauKy) {
        this.slDauKy = slDauKy;
    }

    public String getTienDauKy() {
        return tienDauKy;
    }

    public void setTienDauKy(String tienDauKy) {
        this.tienDauKy = tienDauKy;
    }

    public String getSlNhap() {
        return slNhap;
    }

    public void setSlNhap(String slNhap) {
        this.slNhap = slNhap;
    }

    public String getTienNhap() {
        return tienNhap;
    }

    public void setTienNhap(String tienNhap) {
        this.tienNhap = tienNhap;
    }

    public String getSlXuat() {
        return slXuat;
    }

    public void setSlXuat(String slXuat) {
        this.slXuat = slXuat;
    }

    public String getTienXuat() {
        return tienXuat;
    }

    public void setTienXuat(String tienXuat) {
        this.tienXuat = tienXuat;
    }

    public String getSlTon() {
        return slTon;
    }

    public void setSlTon(String slTon) {
        this.slTon = slTon;
    }

    public String getTienTon() {
        return tienTon;
    }

    public void setTienTon(String tienTon) {
        this.tienTon = tienTon;
    }

    public String getTongSLDauKy() {
        return tongSLDauKy;
    }

    public void setTongSLDauKy(String tongSLDauKy) {
        this.tongSLDauKy = tongSLDauKy;
    }

    public String getTongTienDauKy() {
        return tongTienDauKy;
    }

    public void setTongTienDauKy(String tongTienDauKy) {
        this.tongTienDauKy = tongTienDauKy;
    }

    public String getTongSLNhap() {
        return tongSLNhap;
    }

    public void setTongSLNhap(String tongSLNhap) {
        this.tongSLNhap = tongSLNhap;
    }

    public String getTongTienNhap() {
        return tongTienNhap;
    }

    public void setTongTienNhap(String tongTienNhap) {
        this.tongTienNhap = tongTienNhap;
    }

    public String getTongSLXuat() {
        return tongSLXuat;
    }

    public void setTongSLXuat(String tongSLXuat) {
        this.tongSLXuat = tongSLXuat;
    }

    public String getTongTienXuat() {
        return tongTienXuat;
    }

    public void setTongTienXuat(String tongTienXuat) {
        this.tongTienXuat = tongTienXuat;
    }

    public String getTongSLTon() {
        return tongSLTon;
    }

    public void setTongSLTon(String tongSLTon) {
        this.tongSLTon = tongSLTon;
    }

    public String getTongTienTon() {
        return tongTienTon;
    }

    public void setTongTienTon(String tongTienTon) {
        this.tongTienTon = tongTienTon;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }
}
