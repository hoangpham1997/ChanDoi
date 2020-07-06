package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.CostSetMaterialGoodRepositoryCustom;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class CostSetMaterialGoodRepositoryImpl implements CostSetMaterialGoodRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<TheTinhGiaThanhDTO> getAllByCompanyID(List<UUID> listOrgCS, List<UUID> listOrgMG, Integer typeMethod) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String selectSql = "";
        if (typeMethod == 1 || typeMethod == 2) {
            selectSql = "SELECT a.ID as costSetID, a.CostSetCode as costSetCode, a.CostSetName as costSetName, c.ID as materialGoodsID, c.MaterialGoodsCode as materialGoodsCode, c.MaterialGoodsName as materialGoodsName";
            sql.append(" from CostSet a LEFT JOIN CostSetMaterialGoods b ON a.ID = b.CostSetID LEFT JOIN MaterialGoods c ON b.MaterialGoodsID = c.ID WHERE a.CompanyID IN :listOrgCS AND c.CompanyID IN :listOrgMG AND a.CostSetType = 2 ");
            params.put("listOrgCS", listOrgCS);
            params.put("listOrgMG", listOrgMG);
        } else {
            selectSql = "SELECT a.ID as costSetID, a.CostSetCode as costSetCode, a.CostSetName as costSetName, b.ID as materialGoodsID, b.MaterialGoodsCode as materialGoodsCode, b.MaterialGoodsName as materialGoodsName";
            sql.append(" from CostSet a LEFT JOIN MaterialGoods b ON a.MaterialGoodsID = b.ID WHERE a.CompanyID IN :listOrgCS AND b.CompanyID IN :listOrgMG AND a.CostSetType = 4 ");
            params.put("listOrgCS", listOrgCS);
            params.put("listOrgMG", listOrgMG);
        }
        Query query = entityManager.createNativeQuery(selectSql + sql.toString(), "TheTinhGiaThanhDTO");
        setParams(query, params);
        return query.getResultList();
    }
}
