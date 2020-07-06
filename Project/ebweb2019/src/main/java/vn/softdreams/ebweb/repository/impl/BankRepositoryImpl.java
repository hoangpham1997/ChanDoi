package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.SearchVoucherBank;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.BankRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;
import static vn.softdreams.ebweb.service.Utils.Utils.setParamsWithPageable;

public class BankRepositoryImpl implements BankRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<Bank> findAll(Pageable pageable, String bankCode, String bankName, String bankNameRepresent, String address, String description, Boolean isActive) {
        StringBuilder sql = new StringBuilder();
        List<Bank> banks = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM BANK WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(bankCode)) {
            sql.append("AND BankCode LIKE :bankCode ");
            params.put("bankCode", "%" + bankCode + "%");
        }

        if (!Strings.isNullOrEmpty(bankNameRepresent)) {
            sql.append("AND bankNameRepresent = :bankNameRepresent ");
            params.put("bankNameRepresent", bankNameRepresent);
        }
        if (!Strings.isNullOrEmpty(address)) {
            sql.append("AND address = :address ");
            params.put("address", address);
        }
        if (!Strings.isNullOrEmpty(description)) {
            sql.append("AND description = :description ");
            params.put("description", description);
        }
        if (isActive != null) {
            sql.append("AND isActive = :isActive ");
            params.put("isActive", isActive);
        }
        String sort = pageable.getSort().toString();
        StringBuilder order = new StringBuilder();
        String[] s = sort.split(":");
        for (int i = 0; i < s.length; i++) {
            order.append(s[i]);
        }

        Query countQuery = entityManager.createNativeQuery("Select Count(1)" + sql.toString());
        setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();

        if (total.longValue() > 0) {

            Query query = entityManager.createNativeQuery("Select *" + sql.toString() + " ORDER BY " + order, Bank.class);
            setParamsWithPageable(query, params, pageable, total);
            banks = query.getResultList();
        }

        return new PageImpl<>(banks, pageable, total.longValue());
    }

    @Override
    public Page<Bank> findAllBank(Pageable pageable, SearchVoucherBank searchVoucherBank, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<Bank> lstMG = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Bank WHERE companyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucherBank.getBankCode() != null) {
            sql.append("AND BankCode = :BankCode ");
            params.put("BankCode", searchVoucherBank.getBankCode());
        }
        if (searchVoucherBank.getBankName() != null) {
            sql.append("AND BankName = :BankName ");
            params.put("BankName", searchVoucherBank.getBankName());
        }
        if (searchVoucherBank.getDescription() != null) {
            sql.append("AND Description = :Description ");
            params.put("Description", searchVoucherBank.getDescription());
        }
        if (!Strings.isNullOrEmpty(searchVoucherBank.getKeySearch())) {
            sql.append("AND (BankCode LIKE :searchValue ");
            sql.append("OR BankName LIKE :searchValue ");
            sql.append("OR Description LIKE :searchValue )");
            params.put("searchValue", "%" + searchVoucherBank.getKeySearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), Bank.class);
            setParamsWithPageable(query, params, pageable, total);
            lstMG = query.getResultList();
        }
        return new PageImpl<>(lstMG, pageable, total.longValue());
    }

    @Override
    public Page<Bank> pageableAllBank(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<Bank> banks = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM BANK WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by BankCode"
                , Bank.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            banks = query.getResultList();

        }
        return new PageImpl<>(banks, pageable, total.longValue());
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
