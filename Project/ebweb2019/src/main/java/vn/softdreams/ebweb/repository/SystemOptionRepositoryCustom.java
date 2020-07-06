package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;

import java.util.List;
import java.util.UUID;

public interface SystemOptionRepositoryCustom {
    List<UUID> getAllCompanyByCompanyIdAndCode(UUID companyId, String code);

    void updateAllSystemOptionByCompanyID(List<OrganizationUnit> companyID, String dataTCKHAC_SDDMDoiTuong, String dataTCKHAC_SDDMVTHH,
                                                String dataTCKHAC_SDDMKho, String dataTCKHAC_SDDMCCDC, String dataTCKHAC_SDDMTSCD,
                                                String dataTCKHAC_SDDMDTTHCP, String dataTCKHAC_SDDMTKNH, String dataTCKHAC_SDDMTheTD);

    List<PrivateToGeneralUse> checkPrivateToGeneralUse(UUID companyID, String listsUUIDOrg, String listCheck);
}
