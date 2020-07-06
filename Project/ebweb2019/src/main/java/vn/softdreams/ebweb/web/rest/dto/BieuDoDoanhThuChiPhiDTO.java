package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BieuDoDoanhThuChiPhiDTO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String name;
    private BigDecimal amount;

    public BieuDoDoanhThuChiPhiDTO(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }

    public BieuDoDoanhThuChiPhiDTO(LocalDate fromDate, LocalDate toDate, String name, BigDecimal amount) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.amount = amount;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
