package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class SucKhoeTaiChinhDoanhNghiepDTO {
    private String chiSo;
    private BigDecimal giaTri;

    public SucKhoeTaiChinhDoanhNghiepDTO(String chiSo, BigDecimal giaTri) {
        this.chiSo = chiSo;
        this.giaTri = giaTri;
    }

    public String getChiSo() {
        return chiSo;
    }

    public void setChiSo(String chiSo) {
        this.chiSo = chiSo;
    }

    public BigDecimal getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(BigDecimal giaTri) {
        this.giaTri = giaTri;
    }
}
