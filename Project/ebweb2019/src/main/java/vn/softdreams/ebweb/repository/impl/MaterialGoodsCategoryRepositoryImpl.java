package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.MaterialGoodsCategoryRepository;
import vn.softdreams.ebweb.repository.MaterialGoodsCategoryRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialGoodsCategoryRepositoryImpl implements MaterialGoodsCategoryRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Override
    public List<String> GetListOrderFixCodeParentID(UUID id) {
        Query query = entityManager.createNativeQuery("SELECT OrderFixCode FROM MaterialGoodsCategory ", MaterialGoodsCategory.class);
        List<String> lstFixCOde = query.getResultList();
        return  lstFixCOde;
    }

    @Override
    public Page<MaterialGoodsCategory> pageableAllMaterialGoodsCategories(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoodsCategory> materialGoodsCategories = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoodsCategory WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , MaterialGoodsCategory.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoodsCategories = query.getResultList();

        }
        return new PageImpl<>(materialGoodsCategories, pageable, total.longValue());
    }

    @Override
    public List<MaterialGoodsCategory> getAllMaterialGoodsCategoryByCompanyIDAndSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  select * from MaterialGoodsCategory where 1 = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        sql.append(" order by MaterialGoodsCategoryCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), MaterialGoodsCategory.class);
        Common.setParams(query, params);
        return query.getResultList();
    }
}
