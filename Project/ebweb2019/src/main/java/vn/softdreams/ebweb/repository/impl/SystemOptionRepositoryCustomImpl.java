package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.SystemOptionRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

public class SystemOptionRepositoryCustomImpl implements SystemOptionRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UUID> getAllCompanyByCompanyIdAndCode(UUID companyId, String code) {
        String sql = "exec getCompany :companyId, :code";
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("code", code);
        Query query = entityManager.createNativeQuery(sql, "UUIDDTO");
        Common.setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }

    @Override
    public void updateAllSystemOptionByCompanyID(List<OrganizationUnit> organizationUnits, String dataTCKHAC_SDDMDoiTuong, String dataTCKHAC_SDDMVTHH,
                                                       String dataTCKHAC_SDDMKho, String dataTCKHAC_SDDMCCDC, String dataTCKHAC_SDDMTSCD,
                                                       String dataTCKHAC_SDDMDTTHCP, String dataTCKHAC_SDDMTKNH, String dataTCKHAC_SDDMTheTD) {
        String sql = "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMDoiTuong + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMDoiTuong';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMVTHH + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMVTHH';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMKho + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMKho';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMCCDC + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMCCDC';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMTSCD + " WHERE CompanyID = ? AND Code =N'TCKHAC_SDDMTSCĐ';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMDTTHCP + " WHERE CompanyID = ? AND Code =N'TCKHAC_SDDMĐTTHCP';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMTKNH + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMTKNH';"
            + "UPDATE SystemOption SET Data = " + dataTCKHAC_SDDMTheTD + " WHERE CompanyID = ? AND Code ='TCKHAC_SDDMTheTD';";
        jdbcTemplate.batchUpdate(sql, organizationUnits, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail.getId()).toString());
            ps.setString(2, Common.revertUUID(detail.getId()).toString());
            ps.setString(3, Common.revertUUID(detail.getId()).toString());
            ps.setString(4, Common.revertUUID(detail.getId()).toString());
            ps.setString(5, Common.revertUUID(detail.getId()).toString());
            ps.setString(6, Common.revertUUID(detail.getId()).toString());
            ps.setString(7, Common.revertUUID(detail.getId()).toString());
            ps.setString(8, Common.revertUUID(detail.getId()).toString());
        });
    }


    @Override
    public List<PrivateToGeneralUse> checkPrivateToGeneralUse(UUID companyID, String listsUUIDOrg, String listCheck) {
        String sql = "[CheckGeneralUse] :companyID, :listsUUIDOrg, :listCheck";
        Map<String, Object> params = new HashMap<>();
        params.put("listsUUIDOrg", listsUUIDOrg);
        params.put("listCheck", listCheck);
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql, "PrivateToGeneralUse");
        Common.setParams(query, params);
        List<PrivateToGeneralUse> updateDataDTOS = query.getResultList();
        return updateDataDTOS;
    }
}
