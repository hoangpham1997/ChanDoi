package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.SaReturnDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class SaReturnDetailsRepositoryImpl implements SaReturnDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<SaReturnDetailsRSInwardDTO> findBySaReturnOrderLstIDByOrderPriority(List<UUID> id, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("Select sad.*, case when :typeLedger = 0 then sa.NoFBook else sa.NoMBook end as BookSaReturn " +
            "from SaReturnDetail sad " +
            "         left join MaterialGoods mt on sad.MaterialGoodsID = mt.ID " +
            "         left join SAReturn sa on sa.ID = sad.SAReturnID " +
            "where mt.MaterialGoodsType in (0, 1, 3) " +
            " and sad.SaReturnID in :ids " +
            " and sa.TypeLedger in (2, :typeLedger) " +
            " order by OrderPriority ");
        params.put("typeLedger", currentBook);
        params.put("ids", id);
        List<SaReturnDetailsRSInwardDTO> SaReturnDetailsRSInwardDTOs = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "SaReturnDetailsRSInwardDTO");
        Common.setParams(query, params);
        SaReturnDetailsRSInwardDTOs = query.getResultList();
        return SaReturnDetailsRSInwardDTOs;
    }
}
