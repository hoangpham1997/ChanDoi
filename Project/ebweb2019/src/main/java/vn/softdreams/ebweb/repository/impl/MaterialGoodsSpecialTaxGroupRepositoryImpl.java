package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;
import vn.softdreams.ebweb.repository.MaterialGoodsSpecialTaxGroupRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialGoodsSpecialTaxGroupRepositoryImpl implements MaterialGoodsSpecialTaxGroupRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<MaterialGoodsSpecialTaxGroup> pageableAllMaterialGoodsSpecialTaxGroup(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoodsSpecialTaxGroup> materialGoodsSpecialTaxGroups = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoodsSpecialTaxGroup WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , MaterialGoodsSpecialTaxGroup.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoodsSpecialTaxGroups = query.getResultList();

        }
        return new PageImpl<>(materialGoodsSpecialTaxGroups, pageable, total.longValue());
    }
}
