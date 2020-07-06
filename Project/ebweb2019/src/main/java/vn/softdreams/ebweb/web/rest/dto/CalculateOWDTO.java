package vn.softdreams.ebweb.web.rest.dto;

import org.springframework.web.bind.annotation.RequestParam;
import vn.softdreams.ebweb.domain.ViewVoucherNo;

import java.util.List;
import java.util.UUID;

public class CalculateOWDTO {
    private Integer calculationMethod;
    private List<UUID> materialGoods;
    private String fromDate;
    private String toDate;

    public CalculateOWDTO() {
    }

    public Integer getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(Integer calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public List<UUID> getMaterialGoods() {
        return materialGoods;
    }

    public void setMaterialGoods(List<UUID> materialGoods) {
        this.materialGoods = materialGoods;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
