package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PPInvoiceConvert2DTO {
    private LocalDate Date;
    private String NoMBook;
    private String NoFBook;
    private String MaterialGoodsCode;
    private BigDecimal quantityRollBack;
    private PPDiscountReturnDetails ppDiscountReturnDetails;


    public PPInvoiceConvert2DTO() {
    }

    public PPInvoiceConvert2DTO(LocalDate date, String noMBook, String noFBook, String materialGoodsCode, BigDecimal quantityRollBack, PPDiscountReturnDetails ppDiscountReturnDetails) {
        Date = date;
        NoMBook = noMBook;
        NoFBook = noFBook;
        MaterialGoodsCode = materialGoodsCode;
        this.quantityRollBack = quantityRollBack;
        this.ppDiscountReturnDetails = ppDiscountReturnDetails;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public String getNoMBook() {
        return NoMBook;
    }

    public void setNoMBook(String noMBook) {
        NoMBook = noMBook;
    }

    public String getNoFBook() {
        return NoFBook;
    }

    public void setNoFBook(String noFBook) {
        NoFBook = noFBook;
    }

    public String getMaterialGoodsCode() {
        return MaterialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        MaterialGoodsCode = materialGoodsCode;
    }

    public BigDecimal getQuantityRollBack() {
        return quantityRollBack;
    }

    public void setQuantityRollBack(BigDecimal quantityRollBack) {
        this.quantityRollBack = quantityRollBack;
    }

    public PPDiscountReturnDetails getPpDiscountReturnDetails() {
        return ppDiscountReturnDetails;
    }

    public void setPpDiscountReturnDetails(PPDiscountReturnDetails ppDiscountReturnDetails) {
        this.ppDiscountReturnDetails = ppDiscountReturnDetails;
    }
}
