package vn.softdreams.ebweb.repository;

import java.util.List;
import java.util.UUID;

public interface IAReportRepositoryCustom {
    void multiDeleteReportInvoice(UUID org, List<UUID> uuidList_ktmhd);
}
