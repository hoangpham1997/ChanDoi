package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public interface ExpenseItemConvertDTO {
    UUID getId();
    String getExpenseItemCode();
    String getExpenseItemName();
}
