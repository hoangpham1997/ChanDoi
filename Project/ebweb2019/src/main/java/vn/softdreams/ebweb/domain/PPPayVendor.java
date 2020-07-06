package vn.softdreams.ebweb.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class PPPayVendor {

    private UUID accountingObjectID;

    private String accountingObjectCode;

    private String accountingObjectName;

    private BigDecimal soDuDauNam;

    private BigDecimal soPhatSinh;

    private BigDecimal soDaTra;

    private BigDecimal soConPhaiTra;

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public BigDecimal getSoDuDauNam() {
        return soDuDauNam;
    }

    public void setSoDuDauNam(BigDecimal soDuDauNam) {
        this.soDuDauNam = soDuDauNam;
    }

    public BigDecimal getSoPhatSinh() {
        return soPhatSinh;
    }

    public void setSoPhatSinh(BigDecimal soPhatSinh) {
        this.soPhatSinh = soPhatSinh;
    }

    public BigDecimal getSoDaTra() {
        return soDaTra;
    }

    public void setSoDaTra(BigDecimal soDaTra) {
        this.soDaTra = soDaTra;
    }

    public BigDecimal getSoConPhaiTra() {
        return soConPhaiTra;
    }

    public void setSoConPhaiTra(BigDecimal soConPhaiTra) {
        this.soConPhaiTra = soConPhaiTra;
    }
}
