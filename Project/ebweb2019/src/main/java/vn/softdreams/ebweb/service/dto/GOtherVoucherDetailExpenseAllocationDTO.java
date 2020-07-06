package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class GOtherVoucherDetailExpenseAllocationDTO {
    private UUID id;
    private UUID gOtherVoucherID;
    private UUID prepaidExpenseID;
    private BigDecimal allocationAmount;
    private UUID objectID;
    private Integer objectType;
    private BigDecimal allocationRate;
    private BigDecimal amount;
    private String costAccount;
    private UUID expenseItemID;
    private UUID costSetID;
    private Integer orderPriority;
}
