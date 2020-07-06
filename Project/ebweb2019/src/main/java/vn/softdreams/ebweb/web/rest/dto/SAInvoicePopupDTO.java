package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SAInvoicePopupDTO {
    private UUID id;
    private UUID saInvoiceID;
    private LocalDate date;
    private LocalDate postedDate;
    private String noFBook;
    private String noMBook;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String reason;
    private BigDecimal total;
    private BigDecimal quantity;
    private BigDecimal returnQuantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaInvoiceID() {
        return saInvoiceID;
    }

    public void setSaInvoiceID(UUID saInvoiceID) {
        this.saInvoiceID = saInvoiceID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public SAInvoicePopupDTO(UUID id, UUID saInvoiceID, LocalDate date, LocalDate postedDate, String noFBook, String noMBook, String reason, BigDecimal total) {
        this.id = id;
        this.saInvoiceID = saInvoiceID;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.reason = reason;
        this.total = total;
    }

    public SAInvoicePopupDTO(UUID id, UUID saInvoiceID, LocalDate date, String noFBook, String noMBook, String materialGoodsCode, String materialGoodsName, BigDecimal quantity, BigDecimal returnQuantity) {
        this.id = id;
        this.saInvoiceID = saInvoiceID;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.quantity = quantity;
        this.returnQuantity = returnQuantity;
    }

    public SAInvoicePopupDTO() {
    }
}
