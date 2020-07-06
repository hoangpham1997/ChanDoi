package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.repository.CPAllocationQuantumRepositoryCustom;
import vn.softdreams.ebweb.repository.CPUncompleteDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.Report.SoQuyTienMatDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;

public class CPUncompleteDetailsRepositoryImpl implements CPUncompleteDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public List<CPUncompleteDetails> getByCosetID(String collect, LocalDate toDate, UUID org) {
        List<CPUncompleteDetails> cpUncompleteDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GET_DO_DANG_KY_TRUOC] :toDate, :costSetIDs, :org");
        params.put("toDate", toDate);
        params.put("costSetIDs", collect);
        params.put("org", org);
        Query query = entityManager.createNativeQuery(sql.toString(), CPUncompleteDetails.class);
        Common.setParams(query, params);
        cpUncompleteDetails = query.getResultList();
        return cpUncompleteDetails;
    }
}
