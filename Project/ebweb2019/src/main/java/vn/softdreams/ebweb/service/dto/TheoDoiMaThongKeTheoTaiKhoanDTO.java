package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TheoDoiMaThongKeTheoTaiKhoanDTO {
    private UUID statisticsCodeID;
    private String statisticsCode;
    private String statisticsCodeName;
    private LocalDate ngayChungTu;
    private LocalDate ngayHachToan;
    private String soChungTu;
    private String dienGiai;
    private String tk;
    private String tkDoiUng;
    private BigDecimal soTienNo;
    private BigDecimal soTienCo;
    private Integer orderPriority;
    private UUID refID;
    private Integer refType;
    private BigDecimal tongNo;
    private BigDecimal tongCo;
    private String soTienNoString;
    private String soTienCoString;
    private String tongNoString;
    private String tongCoString;
    private String linkRef;
    private String ngayChungTuString;
    private String ngayHachToanString;

    public TheoDoiMaThongKeTheoTaiKhoanDTO() {
    }

    public TheoDoiMaThongKeTheoTaiKhoanDTO(String statisticsCode, String statisticsCodeName) {
        this.statisticsCode = statisticsCode;
        this.statisticsCodeName = statisticsCodeName;
    }

    public TheoDoiMaThongKeTheoTaiKhoanDTO(UUID statisticsCodeID, String statisticsCode, String statisticsCodeName, LocalDate ngayChungTu, LocalDate ngayHachToan,
                                           String soChungTu, String dienGiai, String tk, String tkDoiUng, BigDecimal soTienNo,
                                           BigDecimal soTienCo, Integer orderPriority, UUID refID, Integer refType, BigDecimal tongNo, BigDecimal tongCo) {
        this.statisticsCodeID = statisticsCodeID;
        this.statisticsCode = statisticsCode;
        this.statisticsCodeName = statisticsCodeName;
        this.ngayChungTu = ngayChungTu;
        this.ngayHachToan = ngayHachToan;
        this.soChungTu = soChungTu;
        this.dienGiai = dienGiai;
        this.tk = tk;
        this.tkDoiUng = tkDoiUng;
        this.soTienNo = soTienNo;
        this.soTienCo = soTienCo;
        this.orderPriority = orderPriority;
        this.refID = refID;
        this.refType = refType;
        this.tongNo = tongNo;
        this.tongCo = tongCo;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getStatisticsCodeName() {
        return statisticsCodeName;
    }

    public void setStatisticsCodeName(String statisticsCodeName) {
        this.statisticsCodeName = statisticsCodeName;
    }

    public LocalDate getNgayChungTu() {
        return ngayChungTu;
    }

    public void setNgayChungTu(LocalDate ngayChungTu) {
        this.ngayChungTu = ngayChungTu;
    }

    public String getSoChungTu() {
        return soChungTu;
    }

    public void setSoChungTu(String soChungTu) {
        this.soChungTu = soChungTu;
    }

    public String getDienGiai() {
        return dienGiai;
    }

    public void setDienGiai(String dienGiai) {
        this.dienGiai = dienGiai;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getTkDoiUng() {
        return tkDoiUng;
    }

    public void setTkDoiUng(String tkDoiUng) {
        this.tkDoiUng = tkDoiUng;
    }

    public BigDecimal getSoTienNo() {
        return soTienNo;
    }

    public void setSoTienNo(BigDecimal soTienNo) {
        this.soTienNo = soTienNo;
    }

    public BigDecimal getSoTienCo() {
        return soTienCo;
    }

    public void setSoTienCo(BigDecimal soTienCo) {
        this.soTienCo = soTienCo;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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

    public BigDecimal getTongNo() {
        return tongNo;
    }

    public void setTongNo(BigDecimal tongNo) {
        this.tongNo = tongNo;
    }

    public BigDecimal getTongCo() {
        return tongCo;
    }

    public void setTongCo(BigDecimal tongCo) {
        this.tongCo = tongCo;
    }

    public String getSoTienNoString() {
        return soTienNoString;
    }

    public void setSoTienNoString(String soTienNoString) {
        this.soTienNoString = soTienNoString;
    }

    public String getSoTienCoString() {
        return soTienCoString;
    }

    public void setSoTienCoString(String soTienCoString) {
        this.soTienCoString = soTienCoString;
    }

    public String getTongNoString() {
        return tongNoString;
    }

    public void setTongNoString(String tongNoString) {
        this.tongNoString = tongNoString;
    }

    public String getTongCoString() {
        return tongCoString;
    }

    public void setTongCoString(String tongCoString) {
        this.tongCoString = tongCoString;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getNgayChungTuString() {
        return ngayChungTuString;
    }

    public void setNgayChungTuString(String ngayChungTuString) {
        this.ngayChungTuString = ngayChungTuString;
    }

    public LocalDate getNgayHachToan() {
        return ngayHachToan;
    }

    public void setNgayHachToan(LocalDate ngayHachToan) {
        this.ngayHachToan = ngayHachToan;
    }

    public String getNgayHachToanString() {
        return ngayHachToanString;
    }

    public void setNgayHachToanString(String ngayHachToanString) {
        this.ngayHachToanString = ngayHachToanString;
    }
}
