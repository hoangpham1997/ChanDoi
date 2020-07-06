package vn.softdreams.ebweb.repository;

import java.util.List;
import java.util.UUID;

public interface IARegisterInvoiceRepositoryCustom {

    void multiDeleteRegisInvoice(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildRegisInvoice(String tableChildName, List<UUID> uuidList);
}
