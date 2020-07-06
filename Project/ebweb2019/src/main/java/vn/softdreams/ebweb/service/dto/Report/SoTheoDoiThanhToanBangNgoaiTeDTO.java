package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheoDoiThanhToanBangNgoaiTeDTO {
    private String account;
    private Integer idGroup;
    private String accountingObjectID;
    private String accountingObjectCode;
    private String accountingObjectName;
    private LocalDate ngayHoachToan;
    private LocalDate ngayChungTu;
    private String soChungTu;
    private String dienGiai;
    private String tKDoiUng;
    private BigDecimal tyGiaHoiDoai;
    private BigDecimal pSNSoTien;
    private BigDecimal pSNQuyDoi;
    private BigDecimal pSCSoTien;
    private BigDecimal pSCQuyDoi;
    private BigDecimal duNoSoTien;
    private BigDecimal duNoQuyDoi;
    private BigDecimal duCoSoTien;
    private BigDecimal duCoQuyDoi;
    private BigDecimal sDDKSoTien;
    private BigDecimal sDDKQuyDoi;
    private UUID refID;
    private Integer refType;

    private String tgHoiDoai;
    private String postedDate;
    private String date;
    private String psnSotien;
    private String psnQuyDoi;
    private String pscSotien;
    private String pscQuyDoi;
    private String dnSotien;
    private String dnQuyDoi;
    private String dcSotien;
    private String dcQuyDoi;
    private String soDuCoDauKySoTien;
    private String soDuCoDauKyQuyDoi;
    private String soDuNoDauKySoTien;
    private String soDuNoDauKyQuyDoi;
    private String linkRef;
    private String totalpsnSotien;
    private String totalpsnQuyDoi;
    private String totalpscSotien;
    private String totalpscQuyDoi;
    private String totalsdnSotien;
    private String totalsdnQuyDoi;
    private String totalsdcSotien;
    private String totalsdcQuyDoi;

    public SoTheoDoiThanhToanBangNgoaiTeDTO() {
    }

    public SoTheoDoiThanhToanBangNgoaiTeDTO(String psnSotien, String pscQuyDoi) {
        this.psnSotien = psnSotien;
        this.pscQuyDoi = pscQuyDoi;
    }

    public SoTheoDoiThanhToanBangNgoaiTeDTO(BigDecimal pSNSoTien, BigDecimal pSNQuyDoi, BigDecimal pSCSoTien, BigDecimal pSCQuyDoi) {
        this.pSNSoTien = pSNSoTien;
        this.pSNQuyDoi = pSNQuyDoi;
        this.pSCSoTien = pSCSoTien;
        this.pSCQuyDoi = pSCQuyDoi;
    }

    public SoTheoDoiThanhToanBangNgoaiTeDTO(String account, String accountingObjectCode, String accountingObjectName, String soChungTu, String dienGiai, String tKDoiUng, String tgHoiDoai, String postedDate, String date, String psnSotien, String psnQuyDoi, String pscSotien, String pscQuyDoi, String dnSotien, String dnQuyDoi, String dcSotien, String dcQuyDoi, String linkRef, String totalpsnSotien, String totalpsnQuyDoi, String totalpscSotien, String totalpscQuyDoi, String totalsdnSotien, String totalsdnQuyDoi, String totalsdcSotien, String totalsdcQuyDoi) {
        this.account = account;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.soChungTu = soChungTu;
        this.dienGiai = dienGiai;
        this.tKDoiUng = tKDoiUng;
        this.tgHoiDoai = tgHoiDoai;
        this.postedDate = postedDate;
        this.date = date;
        this.psnSotien = psnSotien;
        this.psnQuyDoi = psnQuyDoi;
        this.pscSotien = pscSotien;
        this.pscQuyDoi = pscQuyDoi;
        this.dnSotien = dnSotien;
        this.dnQuyDoi = dnQuyDoi;
        this.dcSotien = dcSotien;
        this.dcQuyDoi = dcQuyDoi;
        this.linkRef = linkRef;
        this.totalpsnSotien = totalpsnSotien;
        this.totalpsnQuyDoi = totalpsnQuyDoi;
        this.totalpscSotien = totalpscSotien;
        this.totalpscQuyDoi = totalpscQuyDoi;
        this.totalsdnSotien = totalsdnSotien;
        this.totalsdnQuyDoi = totalsdnQuyDoi;
        this.totalsdcSotien = totalsdcSotien;
        this.totalsdcQuyDoi = totalsdcQuyDoi;
    }

    public SoTheoDoiThanhToanBangNgoaiTeDTO(String account, Integer idGroup, String accountingObjectID, String accountingObjectCode, String accountingObjectName, LocalDate ngayHoachToan, LocalDate ngayChungTu, String soChungTu, String dienGiai, String tKDoiUng, BigDecimal tyGiaHoiDoai, BigDecimal pSNSoTien, BigDecimal pSNQuyDoi, BigDecimal pSCSoTien, BigDecimal pSCQuyDoi, BigDecimal duNoSoTien, BigDecimal duNoQuyDoi, BigDecimal duCoSoTien, BigDecimal duCoQuyDoi, BigDecimal sDDKSoTien, BigDecimal sDDKQuyDoi, UUID refID, Integer refType) {
        this.account = account;
        this.idGroup = idGroup;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.ngayHoachToan = ngayHoachToan;
        this.ngayChungTu = ngayChungTu;
        this.soChungTu = soChungTu;
        this.dienGiai = dienGiai;
        this.tKDoiUng = tKDoiUng;
        this.tyGiaHoiDoai = tyGiaHoiDoai;
        this.pSNSoTien = pSNSoTien;
        this.pSNQuyDoi = pSNQuyDoi;
        this.pSCSoTien = pSCSoTien;
        this.pSCQuyDoi = pSCQuyDoi;
        this.duNoSoTien = duNoSoTien;
        this.duNoQuyDoi = duNoQuyDoi;
        this.duCoSoTien = duCoSoTien;
        this.duCoQuyDoi = duCoQuyDoi;
        this.sDDKSoTien = sDDKSoTien;
        this.sDDKQuyDoi = sDDKQuyDoi;
        this.refID = refID;
        this.refType = refType;
    }

    public String getSoDuCoDauKySoTien() {
        return soDuCoDauKySoTien;
    }

    public void setSoDuCoDauKySoTien(String soDuCoDauKySoTien) {
        this.soDuCoDauKySoTien = soDuCoDauKySoTien;
    }

    public String getSoDuCoDauKyQuyDoi() {
        return soDuCoDauKyQuyDoi;
    }

    public void setSoDuCoDauKyQuyDoi(String soDuCoDauKyQuyDoi) {
        this.soDuCoDauKyQuyDoi = soDuCoDauKyQuyDoi;
    }

    public String getSoDuNoDauKySoTien() {
        return soDuNoDauKySoTien;
    }

    public void setSoDuNoDauKySoTien(String soDuNoDauKySoTien) {
        this.soDuNoDauKySoTien = soDuNoDauKySoTien;
    }

    public String getSoDuNoDauKyQuyDoi() {
        return soDuNoDauKyQuyDoi;
    }

    public void setSoDuNoDauKyQuyDoi(String soDuNoDauKyQuyDoi) {
        this.soDuNoDauKyQuyDoi = soDuNoDauKyQuyDoi;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public BigDecimal getsDDKSoTien() {
        return sDDKSoTien;
    }

    public void setsDDKSoTien(BigDecimal sDDKSoTien) {
        this.sDDKSoTien = sDDKSoTien;
    }

    public BigDecimal getsDDKQuyDoi() {
        return sDDKQuyDoi;
    }

    public void setsDDKQuyDoi(BigDecimal sDDKQuyDoi) {
        this.sDDKQuyDoi = sDDKQuyDoi;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public String getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public LocalDate getNgayHoachToan() {
        return ngayHoachToan;
    }

    public void setNgayHoachToan(LocalDate ngayHoachToan) {
        this.ngayHoachToan = ngayHoachToan;
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

    public String gettKDoiUng() {
        return tKDoiUng;
    }

    public void settKDoiUng(String tKDoiUng) {
        this.tKDoiUng = tKDoiUng;
    }

    public BigDecimal getTyGiaHoiDoai() {
        return tyGiaHoiDoai;
    }

    public void setTyGiaHoiDoai(BigDecimal tyGiaHoiDoai) {
        this.tyGiaHoiDoai = tyGiaHoiDoai;
    }

    public BigDecimal getpSNSoTien() {
        return pSNSoTien;
    }

    public void setpSNSoTien(BigDecimal pSNSoTien) {
        this.pSNSoTien = pSNSoTien;
    }

    public BigDecimal getpSNQuyDoi() {
        return pSNQuyDoi;
    }

    public void setpSNQuyDoi(BigDecimal pSNQuyDoi) {
        this.pSNQuyDoi = pSNQuyDoi;
    }

    public BigDecimal getpSCSoTien() {
        return pSCSoTien;
    }

    public void setpSCSoTien(BigDecimal pSCSoTien) {
        this.pSCSoTien = pSCSoTien;
    }

    public BigDecimal getpSCQuyDoi() {
        return pSCQuyDoi;
    }

    public void setpSCQuyDoi(BigDecimal pSCQuyDoi) {
        this.pSCQuyDoi = pSCQuyDoi;
    }

    public BigDecimal getDuNoSoTien() {
        return duNoSoTien;
    }

    public void setDuNoSoTien(BigDecimal duNoSoTien) {
        this.duNoSoTien = duNoSoTien;
    }

    public BigDecimal getDuNoQuyDoi() {
        return duNoQuyDoi;
    }

    public void setDuNoQuyDoi(BigDecimal duNoQuyDoi) {
        this.duNoQuyDoi = duNoQuyDoi;
    }

    public BigDecimal getDuCoSoTien() {
        return duCoSoTien;
    }

    public void setDuCoSoTien(BigDecimal duCoSoTien) {
        this.duCoSoTien = duCoSoTien;
    }

    public BigDecimal getDuCoQuyDoi() {
        return duCoQuyDoi;
    }

    public void setDuCoQuyDoi(BigDecimal duCoQuyDoi) {
        this.duCoQuyDoi = duCoQuyDoi;
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

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPsnSotien() {
        return psnSotien;
    }

    public void setPsnSotien(String psnSotien) {
        this.psnSotien = psnSotien;
    }

    public String getPsnQuyDoi() {
        return psnQuyDoi;
    }

    public void setPsnQuyDoi(String psnQuyDoi) {
        this.psnQuyDoi = psnQuyDoi;
    }

    public String getPscSotien() {
        return pscSotien;
    }

    public void setPscSotien(String pscSotien) {
        this.pscSotien = pscSotien;
    }

    public String getPscQuyDoi() {
        return pscQuyDoi;
    }

    public void setPscQuyDoi(String pscQuyDoi) {
        this.pscQuyDoi = pscQuyDoi;
    }

    public String getDnSotien() {
        return dnSotien;
    }

    public void setDnSotien(String dnSotien) {
        this.dnSotien = dnSotien;
    }

    public String getDnQuyDoi() {
        return dnQuyDoi;
    }

    public void setDnQuyDoi(String dnQuyDoi) {
        this.dnQuyDoi = dnQuyDoi;
    }

    public String getDcSotien() {
        return dcSotien;
    }

    public void setDcSotien(String dcSotien) {
        this.dcSotien = dcSotien;
    }

    public String getDcQuyDoi() {
        return dcQuyDoi;
    }

    public void setDcQuyDoi(String dcQuyDoi) {
        this.dcQuyDoi = dcQuyDoi;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getTotalpsnSotien() {
        return totalpsnSotien;
    }

    public void setTotalpsnSotien(String totalpsnSotien) {
        this.totalpsnSotien = totalpsnSotien;
    }

    public String getTotalpsnQuyDoi() {
        return totalpsnQuyDoi;
    }

    public void setTotalpsnQuyDoi(String totalpsnQuyDoi) {
        this.totalpsnQuyDoi = totalpsnQuyDoi;
    }

    public String getTotalpscSotien() {
        return totalpscSotien;
    }

    public void setTotalpscSotien(String totalpscSotien) {
        this.totalpscSotien = totalpscSotien;
    }

    public String getTotalpscQuyDoi() {
        return totalpscQuyDoi;
    }

    public void setTotalpscQuyDoi(String totalpscQuyDoi) {
        this.totalpscQuyDoi = totalpscQuyDoi;
    }

    public String getTotalsdnSotien() {
        return totalsdnSotien;
    }

    public void setTotalsdnSotien(String totalsdnSotien) {
        this.totalsdnSotien = totalsdnSotien;
    }

    public String getTotalsdnQuyDoi() {
        return totalsdnQuyDoi;
    }

    public void setTotalsdnQuyDoi(String totalsdnQuyDoi) {
        this.totalsdnQuyDoi = totalsdnQuyDoi;
    }

    public String getTotalsdcSotien() {
        return totalsdcSotien;
    }

    public void setTotalsdcSotien(String totalsdcSotien) {
        this.totalsdcSotien = totalsdcSotien;
    }

    public String getTotalsdcQuyDoi() {
        return totalsdcQuyDoi;
    }

    public void setTotalsdcQuyDoi(String totalsdcQuyDoi) {
        this.totalsdcQuyDoi = totalsdcQuyDoi;
    }

    public String getTgHoiDoai() {
        return tgHoiDoai;
    }

    public void setTgHoiDoai(String tgHoiDoai) {
        this.tgHoiDoai = tgHoiDoai;
    }
}
