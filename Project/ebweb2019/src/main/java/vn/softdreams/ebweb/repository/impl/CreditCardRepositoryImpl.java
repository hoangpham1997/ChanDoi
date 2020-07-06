package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.domain.CreditCard;
import vn.softdreams.ebweb.repository.CreditCardRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class CreditCardRepositoryImpl implements CreditCardRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public CreditCard findByCreditCardNumber(String creditCardNumber, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        CreditCard creditCard = new CreditCard();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CreditCard WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(creditCardNumber)) {
            sql.append("AND CreditCardNumber = :creditCardNumber ");
            params.put("creditCardNumber", creditCardNumber);
        }
        sql.append("AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            // String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + addSort(sort.getSort()) + ") row_num " + sql.toString() + ") where rownum = :rowNum";
            String newSql = "SELECT * " + sql.toString();
            Query query = entityManager.createNativeQuery(newSql, CreditCard.class);
            setParams(query, params);
            Object obj = query.getSingleResult();
            creditCard = CreditCard.class.cast(obj);
        }
        return creditCard;
    }

    @Override
    public Page<CreditCard> pageableAllCreditCard(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<CreditCard> creditCards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CreditCard WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , CreditCard.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            creditCards = query.getResultList();

        }
        return new PageImpl<>(creditCards, pageable, total.longValue());
    }
    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    @Override
    public Page<CreditCard> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<CreditCard> creditCards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM creditCard WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by CreditCardNumber"
                , CreditCard.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            creditCards = query.getResultList();
        }
        return new PageImpl<>(creditCards, pageable, total.longValue());
    }

    @Override
    public Page<CreditCard> pageableAllCreditCards(Pageable pageable , UUID org) {
        StringBuilder sql = new StringBuilder();
        List<CreditCard> creditCards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CreditCard WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by CreditCardNumber"
                , CreditCard.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            creditCards = query.getResultList();

        }
        return new PageImpl<>(creditCards, pageable, total.longValue());
    }
}
