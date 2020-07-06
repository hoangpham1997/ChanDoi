package vn.softdreams.ebweb.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class SAReceiptDebit {

    private UUID accountingObjectID;

    private String accountingObjectCode;

    private String accountingObjectName;

    private BigDecimal soDuDauNam;

    private BigDecimal soPhatSinh;

    private BigDecimal soDaThu;

    private BigDecimal soConPhaiThu;

    public SAReceiptDebit(UUID accountingObjectID, String accountingObjectCode, String accountingObjectName, BigDecimal soDuDauNam, BigDecimal soPhatSinh, BigDecimal soDaThu, BigDecimal soConPhaiThu) {
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.soDuDauNam = soDuDauNam;
        this.soPhatSinh = soPhatSinh;
        this.soDaThu = soDaThu;
        this.soConPhaiThu = soConPhaiThu;
    }

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

    public BigDecimal getSoDaThu() {
        return soDaThu;
    }

    public void setSoDaThu(BigDecimal soDaThu) {
        this.soDaThu = soDaThu;
    }

    public BigDecimal getSoConPhaiThu() {
        return soConPhaiThu;
    }

    public void setSoConPhaiThu(BigDecimal soConPhaiThu) {
        this.soConPhaiThu = soConPhaiThu;
    }
}
