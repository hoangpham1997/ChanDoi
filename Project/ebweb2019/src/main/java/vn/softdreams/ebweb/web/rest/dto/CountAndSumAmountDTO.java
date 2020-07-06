package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CountAndSumAmountDTO {
   Number getCount();
   BigDecimal getSumAmount();
}
