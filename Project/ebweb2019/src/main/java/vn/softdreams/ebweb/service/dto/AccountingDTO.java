package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public interface AccountingDTO {
    UUID getId();

    String getAccountingObjectName();

    String getAccountingObjectAddress();

    String getAccountingObjectCode();

    String getTaxCode();

    String getContactName();
}
