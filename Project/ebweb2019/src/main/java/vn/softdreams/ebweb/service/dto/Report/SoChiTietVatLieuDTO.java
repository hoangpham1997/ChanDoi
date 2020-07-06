package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class SoChiTietVatLieuDTO {
    private UUID refID;
    private UUID detailID;
    private Integer refType;
    private UUID repositoryID;
    private String repositoryCode;
    private String repositoryName;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private LocalDate postedDate;
    private LocalDateTime refDate;
    private String refNo;
    private String reason;
    private String correspondingAccountNumber;
    private String unitName;
    private BigDecimal unitPrice;
    private BigDecimal inwardQuantity;
    private BigDecimal inwardAmount;
    private BigDecimal outwardQuantity;
    private BigDecimal outwardAmount;
    private BigDecimal closingQuantity;
    private BigDecimal closingAmount;
    private Integer rowNum;
    private LocalDateTime iNRefOrder;
    private Integer sortOrder;
    private Boolean isShowOnReport;
    private String donGia;
    private String slNhap;
    private String tienNhap;
    private String slXuat;
    private String tienXuat;
    private String slTon;
    private String tienTon;
    private String ngayChungTu;
    private String tongSLNhap;
    private String tongTienNhap;
    private String tongSLXuat;
    private String tongTienXuat;
    private String tongSLTon;
    private String tongTienTon;
    private Integer stt;
    private Boolean loop;
    private String linkRef;


    public SoChiTietVatLieuDTO(UUID refID, UUID detailID, Integer refType, UUID repositoryID, String repositoryCode, String repositoryName, UUID materialGoodsID, String materialGoodsCode, String materialGoodsName, LocalDate postedDate, LocalDateTime refDate, String refNo, String reason, String correspondingAccountNumber, String unitName, BigDecimal unitPrice, BigDecimal inwardQuantity, BigDecimal inwardAmount, BigDecimal outwardQuantity, BigDecimal outwardAmount, BigDecimal closingQuantity, BigDecimal closingAmount, Integer rowNum, LocalDateTime iNRefOrder, Integer sortOrder, Boolean isShowOnReport) {
        this.refID = refID;
        this.detailID = detailID;
        this.refType = refType;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.repositoryName = repositoryName;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.postedDate = postedDate;
        this.refDate = refDate;
        this.refNo = refNo;
        this.reason = reason;
        this.correspondingAccountNumber = correspondingAccountNumber;
        this.unitName = unitName;
        this.unitPrice = unitPrice;
        this.inwardQuantity = inwardQuantity;
        this.inwardAmount = inwardAmount;
        this.outwardQuantity = outwardQuantity;
        this.outwardAmount = outwardAmount;
        this.closingQuantity = closingQuantity;
        this.closingAmount = closingAmount;
        this.rowNum = rowNum;
        this.iNRefOrder = iNRefOrder;
        this.sortOrder = sortOrder;
        this.isShowOnReport = isShowOnReport;
    }

    public SoChiTietVatLieuDTO(BigDecimal unitPrice, BigDecimal inwardQuantity, BigDecimal inwardAmount, BigDecimal outwardQuantity, BigDecimal outwardAmount, BigDecimal closingQuantity, BigDecimal closingAmount) {
        this.unitPrice = unitPrice;
        this.inwardQuantity = inwardQuantity;
        this.inwardAmount = inwardAmount;
        this.outwardQuantity = outwardQuantity;
        this.outwardAmount = outwardAmount;
        this.closingQuantity = closingQuantity;
        this.closingAmount = closingAmount;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
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

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDateTime getRefDate() {
        return refDate;
    }

    public void setRefDate(LocalDateTime refDate) {
        this.refDate = refDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCorrespondingAccountNumber() {
        return correspondingAccountNumber;
    }

    public void setCorrespondingAccountNumber(String correspondingAccountNumber) {
        this.correspondingAccountNumber = correspondingAccountNumber;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getInwardQuantity() {
        return inwardQuantity;
    }

    public void setInwardQuantity(BigDecimal inwardQuantity) {
        this.inwardQuantity = inwardQuantity;
    }

    public BigDecimal getInwardAmount() {
        return inwardAmount;
    }

    public void setInwardAmount(BigDecimal inwardAmount) {
        this.inwardAmount = inwardAmount;
    }

    public BigDecimal getOutwardQuantity() {
        return outwardQuantity;
    }

    public void setOutwardQuantity(BigDecimal outwardQuantity) {
        this.outwardQuantity = outwardQuantity;
    }

    public BigDecimal getOutwardAmount() {
        return outwardAmount;
    }

    public void setOutwardAmount(BigDecimal outwardAmount) {
        this.outwardAmount = outwardAmount;
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

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public LocalDateTime getiNRefOrder() {
        return iNRefOrder;
    }

    public void setiNRefOrder(LocalDateTime iNRefOrder) {
        this.iNRefOrder = iNRefOrder;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getShowOnReport() {
        return isShowOnReport;
    }

    public void setShowOnReport(Boolean showOnReport) {
        isShowOnReport = showOnReport;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
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

    public String getNgayChungTu() {
        return ngayChungTu;
    }

    public void setNgayChungTu(String ngayChungTu) {
        this.ngayChungTu = ngayChungTu;
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

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}
