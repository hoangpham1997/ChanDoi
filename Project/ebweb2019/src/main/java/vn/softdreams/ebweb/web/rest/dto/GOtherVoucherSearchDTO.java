package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class GOtherVoucherSearchDTO {
    private List<GOtherVoucherDTO> gOtherVoucherDTOS;
    private BigDecimal sumTotalAmount;

    public GOtherVoucherSearchDTO(List<GOtherVoucherDTO> gOtherVoucherDTOS, BigDecimal sumTotalAmount) {
        this.gOtherVoucherDTOS = gOtherVoucherDTOS;
        this.sumTotalAmount = sumTotalAmount;
    }

    public GOtherVoucherSearchDTO() {
    }

    public List<GOtherVoucherDTO> getgOtherVoucherDTOS() {
        return gOtherVoucherDTOS;
    }

    public void setgOtherVoucherDTOS(List<GOtherVoucherDTO> gOtherVoucherDTOS) {
        this.gOtherVoucherDTOS = gOtherVoucherDTOS;
    }

    public BigDecimal getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(BigDecimal sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }
}
