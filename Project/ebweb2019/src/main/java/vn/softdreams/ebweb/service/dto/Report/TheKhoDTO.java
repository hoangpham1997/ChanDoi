package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TheKhoDTO {
    private UUID refID;
    private Integer refType;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private LocalDate postedDate;
    private LocalDateTime refDate;
    private String refNo;
    private String reason;
    private String unitName;
    private BigDecimal inwardQuantity;
    private BigDecimal outwardQuantity;
    private BigDecimal closingQuantity;
    private Integer rowNum;
    private String slNhap;
    private String slXuat;
    private String slTon;
    private String ngayChungTu;
    private String ngayHachToan;
    private String tongSLNhap;
    private String tongSLXuat;
    private String tongSLTon;
    private String noNK;
    private String noXK;
    private Integer stt;
    private Boolean loop;
    private String linkRef;

    public TheKhoDTO(UUID refID, Integer refType, UUID materialGoodsID, String materialGoodsCode, String materialGoodsName, LocalDate postedDate, LocalDateTime refDate, String refNo, String reason, String unitName, BigDecimal inwardQuantity, BigDecimal outwardQuantity, BigDecimal closingQuantity, Integer rowNum) {
        this.refID = refID;
        this.refType = refType;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.postedDate = postedDate;
        this.refDate = refDate;
        this.refNo = refNo;
        this.reason = reason;
        this.unitName = unitName;
        this.inwardQuantity = inwardQuantity;
        this.outwardQuantity = outwardQuantity;
        this.closingQuantity = closingQuantity;
        this.rowNum = rowNum;
    }

    public TheKhoDTO(BigDecimal inwardQuantity, BigDecimal outwardQuantity, BigDecimal closingQuantity) {
        this.inwardQuantity = inwardQuantity;
        this.outwardQuantity = outwardQuantity;
        this.closingQuantity = closingQuantity;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getInwardQuantity() {
        return inwardQuantity;
    }

    public void setInwardQuantity(BigDecimal inwardQuantity) {
        this.inwardQuantity = inwardQuantity;
    }

    public BigDecimal getOutwardQuantity() {
        return outwardQuantity;
    }

    public void setOutwardQuantity(BigDecimal outwardQuantity) {
        this.outwardQuantity = outwardQuantity;
    }

    public BigDecimal getClosingQuantity() {
        return closingQuantity;
    }

    public void setClosingQuantity(BigDecimal closingQuantity) {
        this.closingQuantity = closingQuantity;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getSlNhap() {
        return slNhap;
    }

    public void setSlNhap(String slNhap) {
        this.slNhap = slNhap;
    }

    public String getSlXuat() {
        return slXuat;
    }

    public void setSlXuat(String slXuat) {
        this.slXuat = slXuat;
    }

    public String getSlTon() {
        return slTon;
    }

    public void setSlTon(String slTon) {
        this.slTon = slTon;
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

    public String getTongSLXuat() {
        return tongSLXuat;
    }

    public void setTongSLXuat(String tongSLXuat) {
        this.tongSLXuat = tongSLXuat;
    }

    public String getTongSLTon() {
        return tongSLTon;
    }

    public void setTongSLTon(String tongSLTon) {
        this.tongSLTon = tongSLTon;
    }

    public String getNoNK() {
        return noNK;
    }

    public void setNoNK(String noNK) {
        this.noNK = noNK;
    }

    public String getNoXK() {
        return noXK;
    }

    public void setNoXK(String noXK) {
        this.noXK = noXK;
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

    public String getNgayHachToan() {
        return ngayHachToan;
    }

    public void setNgayHachToan(String ngayHachToan) {
        this.ngayHachToan = ngayHachToan;
    }
}
