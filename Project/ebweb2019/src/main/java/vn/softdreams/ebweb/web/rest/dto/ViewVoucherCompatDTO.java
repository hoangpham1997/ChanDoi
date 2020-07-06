package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewVoucherCompatDTO {

    private Long id;

    private Integer typeID;

    private UUID companyID;

    private String no;

    private LocalDate date;

    private LocalDate postedDate;

    private String reason;

    private Boolean recorded;

    public ViewVoucherCompatDTO(Long id, Integer typeID, UUID companyID, String no, LocalDate date, LocalDate postedDate, String reason, Boolean recorded) {
        this.id = id;
        this.typeID = typeID;
        this.companyID = companyID;
        this.date = date;
        this.no = no;
        this.postedDate = postedDate;
        this.reason = reason;
        this.recorded = recorded;
    }
}
