package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucherCurrency;
import vn.softdreams.ebweb.repository.CurrencyRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.CurrencyCbbDTO;
import vn.softdreams.ebweb.domain.Currency;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<CurrencyCbbDTO> findAllDTO(String className, UUID org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select distinct (a.currencycode), a.currencyname from Currency a inner join ");
        sql.append(className);
        sql.append(" b on b.currencyID = a.currencycode where b.CompanyID = :org");
        params.put("org", org);
        Query query = entityManager.createNativeQuery(sql.toString(), "CurrencyCbbDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public Page<Currency> pageableAllCurrency(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<Currency> currencies = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Currency WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select *, " +
                    "case " +
                    "    when CurrencyCode = 'VND' then 1 " +
                    "    when CurrencyCode = 'USD' then 2 " +
                    "    else 3 " +
                    "end as rowNumber " +
                    "from Currency where CompanyID = :org " +
                    "order by rowNumber, CurrencyCode"
                , Currency.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            currencies = query.getResultList();
        }
        return new PageImpl<>(currencies, pageable, total.longValue());
    }

    @Override
    public Page<Currency> findAll1(Pageable pageable, SearchVoucherCurrency searchVoucherCurrency, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<Currency> lstMG = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Currency WHERE companyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucherCurrency.getCurrencyCode() != null) {
            sql.append("AND CurrencyCode = :CurrencyCode ");
            params.put("CurrencyCode", searchVoucherCurrency.getCurrencyCode());
        }
        if (searchVoucherCurrency.getCurrencyName() != null) {
            sql.append("AND CurrencyName = :CurrencyName ");
            params.put("CurrencyName", searchVoucherCurrency.getCurrencyName());
        }
        if (searchVoucherCurrency.getExchangeRate() != null) {
            sql.append("AND ExchangeRate = :ExchangeRate ");
            params.put("ExchangeRate", searchVoucherCurrency.getExchangeRate());
        }
        if (!Strings.isNullOrEmpty(searchVoucherCurrency.getKeySearch())) {
            sql.append("AND (CurrencyCode LIKE :searchValue ");
            sql.append("OR CurrencyName LIKE :searchValue ");
            sql.append("OR ExchangeRate LIKE :searchValue )");
            params.put("searchValue", "%" + searchVoucherCurrency.getKeySearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), Currency.class);
            setParamsWithPageable(query, params, pageable, total);
            lstMG = query.getResultList();
        }
        return new PageImpl<>(lstMG, pageable, total.longValue());
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

}
