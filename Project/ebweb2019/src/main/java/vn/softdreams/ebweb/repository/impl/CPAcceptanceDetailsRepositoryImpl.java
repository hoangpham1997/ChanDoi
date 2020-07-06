package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.CPAcceptanceDetails;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.repository.CPAcceptanceDetailsRepositoryCustom;
import vn.softdreams.ebweb.repository.CPUncompleteDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;

public class CPAcceptanceDetailsRepositoryImpl implements CPAcceptanceDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CPAcceptanceDetails> getByCosetID(String collect, LocalDate toDate, UUID org, UUID id) {
        List<CPAcceptanceDetails> cpAcceptanceDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GET_NGHIEM_THU_KY_TRUOC] :toDate, :costSetIDs, :org, :id");
        params.put("toDate", toDate);
        params.put("costSetIDs", collect);
        params.put("org", org);
        params.put("id", id == null ? "00000000-0000-0000-0000-000000000000" : id);
        Query query = entityManager.createNativeQuery(sql.toString(), CPAcceptanceDetails.class);
        Common.setParams(query, params);
        cpAcceptanceDetails = query.getResultList();
        return cpAcceptanceDetails;
    }

    @Override
    public void deleteByGOtherVoucherID(List<UUID> uuids) {
        String sql1 = "delete CPAcceptanceDetail where CPAcceptanceID in (Select c.ID from CPAcceptance c where c.GOtherVoucherID = ?);"+
            "delete CPAcceptance where GOtherVoucherID = ? ;";
        jdbcTemplate.batchUpdate(sql1, uuids, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }
}
