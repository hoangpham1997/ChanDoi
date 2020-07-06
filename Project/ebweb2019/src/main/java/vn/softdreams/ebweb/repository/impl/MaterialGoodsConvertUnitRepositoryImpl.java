package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.MaterialGoodsConvertUnitRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialGoodsConvertUnitRepositoryImpl implements MaterialGoodsConvertUnitRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Override
    public MaterialGoodsConvertUnit getMaterialGoodsConvertUnitPPInvoice(UUID materialGoodsId, UUID unitId) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoodsConvertUnit> materialGoodsConvertUnitList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select * FROM MaterialGoodsConvertUnit ");
        sql.append("where materialgoodsid = :materialGoodsID ");
        sql.append("and unitid = :unitId ");
        params.put("materialGoodsID", materialGoodsId);
        params.put("unitId", unitId);
        Query query = entityManager.createNativeQuery(sql.toString(), MaterialGoodsConvertUnit.class);
        Common.setParams(query, params);
        materialGoodsConvertUnitList = query.getResultList();

        return materialGoodsConvertUnitList.get(0);
    }

    @Override
    public List<MaterialGoodsConvertUnitDTO> getAllDTO(List<UUID> companyIDs) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoodsConvertUnitDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select * FROM MaterialGoodsConvertUnit where MaterialGoodsID in (select ID from MaterialGoods where CompanyID in :companyIDs ) ");
        params.put("companyIDs", companyIDs);
        Query query = entityManager.createNativeQuery(sql.toString(), "MaterialGoodsConvertUnitDTO");
        Common.setParams(query, params);
        lst = query.getResultList();
        return lst;
    }
}
