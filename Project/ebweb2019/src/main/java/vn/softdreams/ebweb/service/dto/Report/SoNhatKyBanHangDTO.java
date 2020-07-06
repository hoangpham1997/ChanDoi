package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoNhatKyBanHangDTO {
    private UUID detailID;
    private Integer typeID;
    private LocalDate postedDate;
    private String postedDateString;
    private LocalDate refDate;
    private String refDateString;
    private String refNo;
    private LocalDate invoiceDate;
    private String invoiceDateString;
    private String invoiceNo;
    private String description;
    private UUID refID;
    private String nameCustomer;
    private BigDecimal doanhThuHangHoa;
    private String doanhThuHangHoaString;
    private BigDecimal doanhThuThanhPham;
    private String doanhThuThanhPhamString;
    private BigDecimal doanhThuDichVu;
    private String doanhThuDichVuString;
    private BigDecimal doanhThuTroCap;
    private String doanhThuTroCapString;
    private BigDecimal doanhThuBDSDautu;
    private String doanhThuBDSDautuString;
    private BigDecimal doanhThuKhac;
    private String doanhThuKhacString;
    private BigDecimal chietKhau;
    private String chietKhauString;
    private BigDecimal giaTriTraLai;
    private String giaTriTraLaiString;
    private BigDecimal giaTriGiamGia;
    private String giaTriGiamGiaString;
    private BigDecimal sum;
    private String sumString;
    private BigDecimal doanhThuThuan;
    private String doanhThuThuanString;
    public String linkRef;

    public SoNhatKyBanHangDTO() {
    }

    public SoNhatKyBanHangDTO(UUID detailID, Integer typeID, LocalDate postedDate, LocalDate refDate, String refNo, LocalDate invoiceDate, String invoiceNo, String description, UUID refID, String nameCustomer, BigDecimal doanhThuHangHoa, BigDecimal doanhThuThanhPham, BigDecimal doanhThuDichVu, BigDecimal doanhThuTroCap, BigDecimal doanhThuBDSDautu, BigDecimal doanhThuKhac, BigDecimal chietKhau, BigDecimal giaTriTraLai, BigDecimal giaTriGiamGia, BigDecimal sum, BigDecimal doanhThuThuan) {
        this.detailID = detailID;
        this.typeID = typeID;
        this.postedDate = postedDate;
        this.refDate = refDate;
        this.refNo = refNo;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.description = description;
        this.refID = refID;
        this.nameCustomer = nameCustomer;
        this.doanhThuHangHoa = doanhThuHangHoa;
        this.doanhThuThanhPham = doanhThuThanhPham;
        this.doanhThuDichVu = doanhThuDichVu;
        this.doanhThuTroCap = doanhThuTroCap;
        this.doanhThuBDSDautu = doanhThuBDSDautu;
        this.doanhThuKhac = doanhThuKhac;
        this.chietKhau = chietKhau;
        this.giaTriTraLai = giaTriTraLai;
        this.giaTriGiamGia = giaTriGiamGia;
        this.sum = sum;
        this.doanhThuThuan = doanhThuThuan;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(String postedDateString) {
        this.postedDateString = postedDateString;
    }

    public LocalDate getRefDate() {
        return refDate;
    }

    public void setRefDate(LocalDate refDate) {
        this.refDate = refDate;
    }

    public String getRefDateString() {
        return refDateString;
    }

    public void setRefDateString(String refDateString) {
        this.refDateString = refDateString;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDateString() {
        return invoiceDateString;
    }

    public void setInvoiceDateString(String invoiceDateString) {
        this.invoiceDateString = invoiceDateString;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public BigDecimal getDoanhThuHangHoa() {
        return doanhThuHangHoa;
    }

    public void setDoanhThuHangHoa(BigDecimal doanhThuHangHoa) {
        this.doanhThuHangHoa = doanhThuHangHoa;
    }

    public String getDoanhThuHangHoaString() {
        return doanhThuHangHoaString;
    }

    public void setDoanhThuHangHoaString(String doanhThuHangHoaString) {
        this.doanhThuHangHoaString = doanhThuHangHoaString;
    }

    public BigDecimal getDoanhThuThanhPham() {
        return doanhThuThanhPham;
    }

    public void setDoanhThuThanhPham(BigDecimal doanhThuThanhPham) {
        this.doanhThuThanhPham = doanhThuThanhPham;
    }

    public String getDoanhThuThanhPhamString() {
        return doanhThuThanhPhamString;
    }

    public void setDoanhThuThanhPhamString(String doanhThuThanhPhamString) {
        this.doanhThuThanhPhamString = doanhThuThanhPhamString;
    }

    public BigDecimal getDoanhThuDichVu() {
        return doanhThuDichVu;
    }

    public void setDoanhThuDichVu(BigDecimal doanhThuDichVu) {
        this.doanhThuDichVu = doanhThuDichVu;
    }

    public String getDoanhThuDichVuString() {
        return doanhThuDichVuString;
    }

    public void setDoanhThuDichVuString(String doanhThuDichVuString) {
        this.doanhThuDichVuString = doanhThuDichVuString;
    }

    public BigDecimal getDoanhThuTroCap() {
        return doanhThuTroCap;
    }

    public void setDoanhThuTroCap(BigDecimal doanhThuTroCap) {
        this.doanhThuTroCap = doanhThuTroCap;
    }

    public String getDoanhThuTroCapString() {
        return doanhThuTroCapString;
    }

    public void setDoanhThuTroCapString(String doanhThuTroCapString) {
        this.doanhThuTroCapString = doanhThuTroCapString;
    }

    public BigDecimal getDoanhThuBDSDautu() {
        return doanhThuBDSDautu;
    }

    public void setDoanhThuBDSDautu(BigDecimal doanhThuBDSDautu) {
        this.doanhThuBDSDautu = doanhThuBDSDautu;
    }

    public String getDoanhThuBDSDautuString() {
        return doanhThuBDSDautuString;
    }

    public void setDoanhThuBDSDautuString(String doanhThuBDSDautuString) {
        this.doanhThuBDSDautuString = doanhThuBDSDautuString;
    }

    public BigDecimal getDoanhThuKhac() {
        return doanhThuKhac;
    }

    public void setDoanhThuKhac(BigDecimal doanhThuKhac) {
        this.doanhThuKhac = doanhThuKhac;
    }

    public String getDoanhThuKhacString() {
        return doanhThuKhacString;
    }

    public void setDoanhThuKhacString(String doanhThuKhacString) {
        this.doanhThuKhacString = doanhThuKhacString;
    }

    public BigDecimal getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(BigDecimal chietKhau) {
        this.chietKhau = chietKhau;
    }

    public String getChietKhauString() {
        return chietKhauString;
    }

    public void setChietKhauString(String chietKhauString) {
        this.chietKhauString = chietKhauString;
    }

    public BigDecimal getGiaTriTraLai() {
        return giaTriTraLai;
    }

    public void setGiaTriTraLai(BigDecimal giaTriTraLai) {
        this.giaTriTraLai = giaTriTraLai;
    }

    public String getGiaTriTraLaiString() {
        return giaTriTraLaiString;
    }

    public void setGiaTriTraLaiString(String giaTriTraLaiString) {
        this.giaTriTraLaiString = giaTriTraLaiString;
    }

    public BigDecimal getGiaTriGiamGia() {
        return giaTriGiamGia;
    }

    public void setGiaTriGiamGia(BigDecimal giaTriGiamGia) {
        this.giaTriGiamGia = giaTriGiamGia;
    }

    public String getGiaTriGiamGiaString() {
        return giaTriGiamGiaString;
    }

    public void setGiaTriGiamGiaString(String giaTriGiamGiaString) {
        this.giaTriGiamGiaString = giaTriGiamGiaString;
    }

    public String getDoanhThuThuanString() {
        return doanhThuThuanString;
    }

    public void setDoanhThuThuanString(String doanhThuThuanString) {
        this.doanhThuThuanString = doanhThuThuanString;
    }

    public String getSumString() {
        return sumString;
    }

    public void setSumString(String sumString) {
        this.sumString = sumString;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getDoanhThuThuan() {
        return doanhThuThuan;
    }

    public void setDoanhThuThuan(BigDecimal doanhThuThuan) {
        this.doanhThuThuan = doanhThuThuan;
    }
}
