package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import java.util.List;
import java.util.UUID;

public interface IaPublishInviceDetailsRepositoryCustom {

    List<IAPublishInvoiceDetailDTO> getAllByCompany(UUID org);

    List<IAPublishInvoiceDetailDTO> getFollowTransferByCompany(UUID org);
}
