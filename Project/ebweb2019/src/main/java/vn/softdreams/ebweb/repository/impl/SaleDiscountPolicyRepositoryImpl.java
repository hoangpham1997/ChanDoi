package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.SaleDiscountPolicyRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;
import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleDiscountPolicyRepositoryImpl implements SaleDiscountPolicyRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<SaleDiscountPolicyDTO> findAllSaleDiscountPolicyDTO() {
        StringBuilder sql = new StringBuilder();
        List<SaleDiscountPolicyDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SaleDiscountPolicy");
        Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), "SaleDiscountPolicyDTO");
        Utils.setParams(query, params);
        return query.getResultList();
    }
}
