package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TongHopChiTietVatLieuDTO {
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String account;
    private BigDecimal amountOpening;
    private BigDecimal iWAmount;
    private BigDecimal oWAmount;
    private BigDecimal amountClosing;
    private String tienDauKy;
    private String tienNhap;
    private String tienXuat;
    private String tienTon;
    private String tongTienDauKy;
    private String tongTienNhap;
    private String tongTienXuat;
    private String tongTienTon;
    private Integer stt;
    private Boolean loop;

    public TongHopChiTietVatLieuDTO(UUID materialGoodsID, String materialGoodsCode, String materialGoodsName, String account, BigDecimal amountOpening, BigDecimal iWAmount, BigDecimal oWAmount, BigDecimal amountClosing) {
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.account = account;
        this.amountOpening = amountOpening;
        this.iWAmount = iWAmount;
        this.oWAmount = oWAmount;
        this.amountClosing = amountClosing;
    }

    public TongHopChiTietVatLieuDTO(BigDecimal amountOpening, BigDecimal iWAmount, BigDecimal oWAmount, BigDecimal amountClosing) {
        this.amountOpening = amountOpening;
        this.iWAmount = iWAmount;
        this.oWAmount = oWAmount;
        this.amountClosing = amountClosing;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAmountOpening() {
        return amountOpening;
    }

    public void setAmountOpening(BigDecimal amountOpening) {
        this.amountOpening = amountOpening;
    }

    public BigDecimal getiWAmount() {
        return iWAmount;
    }

    public void setiWAmount(BigDecimal iWAmount) {
        this.iWAmount = iWAmount;
    }

    public BigDecimal getoWAmount() {
        return oWAmount;
    }

    public void setoWAmount(BigDecimal oWAmount) {
        this.oWAmount = oWAmount;
    }

    public BigDecimal getAmountClosing() {
        return amountClosing;
    }

    public void setAmountClosing(BigDecimal amountClosing) {
        this.amountClosing = amountClosing;
    }

    public String getTienDauKy() {
        return tienDauKy;
    }

    public void setTienDauKy(String tienDauKy) {
        this.tienDauKy = tienDauKy;
    }

    public String getTienNhap() {
        return tienNhap;
    }

    public void setTienNhap(String tienNhap) {
        this.tienNhap = tienNhap;
    }

    public String getTienXuat() {
        return tienXuat;
    }

    public void setTienXuat(String tienXuat) {
        this.tienXuat = tienXuat;
    }

    public String getTienTon() {
        return tienTon;
    }

    public void setTienTon(String tienTon) {
        this.tienTon = tienTon;
    }

    public String getTongTienDauKy() {
        return tongTienDauKy;
    }

    public void setTongTienDauKy(String tongTienDauKy) {
        this.tongTienDauKy = tongTienDauKy;
    }

    public String getTongTienNhap() {
        return tongTienNhap;
    }

    public void setTongTienNhap(String tongTienNhap) {
        this.tongTienNhap = tongTienNhap;
    }

    public String getTongTienXuat() {
        return tongTienXuat;
    }

    public void setTongTienXuat(String tongTienXuat) {
        this.tongTienXuat = tongTienXuat;
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
}
