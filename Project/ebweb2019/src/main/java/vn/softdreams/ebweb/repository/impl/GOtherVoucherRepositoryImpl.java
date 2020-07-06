package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.GOtherVoucherRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.GOtherVoucherDetailKcDTO;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDsDTO;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.GOtherVoucherExportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDataKcDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class GOtherVoucherRepositoryImpl implements GOtherVoucherRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<GOtherVoucherViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherViewDTO> gOtherVouchers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        } else {
            sql.append("AND CompanyID = null ");
        }
        if (searchVoucher.getTypeID() != null) {
            sql.append("AND TypeID = :typeID ");
            params.put("typeID", searchVoucher.getTypeID());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getCurrencyID())) {
            sql.append("AND currencyID = :currencyID ");
            params.put("currencyID", searchVoucher.getCurrencyID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (Boolean.TRUE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 1 ");
        } else if (Boolean.FALSE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 0 ");
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
            sql.append("AND (reason LIKE :searchValue ");
            if (Boolean.TRUE.equals(isNoMBook)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append(" AND (TypeLedger = 1 OR TypeLedger = 2)");
        } else {
            sql.append(" AND (TypeLedger = 0 OR TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            String selectSQL = "SELECT ID as id, TypeID as typeID, Date as date, PostedDate as postedDate, " +
                " TypeLedger as typeLedger, NoFBook as noFBook, NoMBook as noMBook, " +
                " Reason as reason, " +
                "CurrencyID as currencyID, ExchangeRate as exchangeRate, TotalAmount as totalAmount," +
                " TotalAmountOriginal as totalAmountOriginal, Recorded as recorded, 0  ";
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "ORDER BY Date DESC,PostedDate DESC ,NOMBook DESC ", "GOtherVoucherViewDTO");
                Common.setParamsWithPageable(query, params, pageable, total);
                gOtherVouchers = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "ORDER BY Date DESC,PostedDate DESC ,NOFBook DESC ", "GOtherVoucherViewDTO");
                Common.setParamsWithPageable(query, params, pageable, total);
                gOtherVouchers = query.getResultList();
            }
            Query querySum = entityManager.createNativeQuery("SELECT Sum(TotalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (gOtherVouchers.size() > 0) {
                gOtherVouchers.get(0).setTotal(totalamount);
            }
        }
        return new PageImpl<>(gOtherVouchers, pageable, total.longValue());
    }

    @Override
    public GOtherVoucher findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        String newSql;
        GOtherVoucher gOtherVoucher = new GOtherVoucher();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE 1 = 1 AND CompanyID = :org ");
        params.put("org", org);
        if (searchVoucher != null) {
            if (searchVoucher.getCurrencyID() != null) {
                sql.append("AND currencyID = :currencyID ");
                params.put("currencyID", searchVoucher.getCurrencyID());
            }
            if (searchVoucher.getFromDate() != null) {
                sql.append("AND date >= :fromDate ");
                params.put("fromDate", searchVoucher.getFromDate());
            }
            if (searchVoucher.getToDate() != null) {
                sql.append("AND date <= :toDate ");
                params.put("toDate", searchVoucher.getToDate());
            }
            if (searchVoucher.getTypeID() != null) {
                sql.append("AND TypeID = :typeID ");
                params.put("typeID", searchVoucher.getTypeID());
            }
            if (searchVoucher.getStatusRecorded() != null) {
                sql.append("AND Recorded = :recorded ");
                params.put("recorded", searchVoucher.getStatusRecorded() ? 1 : 0);
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND (reason LIKE :searchValue ");
                if (Boolean.TRUE.equals(isNoMBook)) {
                    sql.append("OR NOMBook LIKE :searchValue) ");
                } else {
                    sql.append("OR NOFBook LIKE :searchValue) ");
                }
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append("AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append("AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoMBook DESC " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum ";
            } else {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoFBook DESC " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum ";
            }
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, GOtherVoucher.class);
            Common.setParams(query, params);
            Object obj = query.getSingleResult();
            gOtherVoucher = GOtherVoucher.class.cast(obj);
        }
        return gOtherVoucher;
    }

    @Override
    public Page<GOtherVoucherExportDTO> getAllGOtherVouchers(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherExportDTO> mbDepositExportDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String selectSql = "";
        if (Boolean.TRUE.equals(isNoMBook)) {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, " +
                "NoMBook as noBook, TypeID as typeID, null as typeIDInWord, " +
                "Reason as description, TotalAmount as totalAmount ";
        } else {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, NoFBook as noBook, " +
                "TypeID as typeID, null as typeIDInWord, " +
                "Reason as description, TotalAmount as totalAmount ";
        }

        sql.append("FROM GOtherVoucher WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        } else {
            sql.append("AND CompanyID = null ");
        }
        if (searchVoucher.getTypeID() != null) {
            sql.append("AND TypeID = :typeID ");
            params.put("typeID", searchVoucher.getTypeID());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getCurrencyID())) {
            sql.append("AND currencyID = :currencyID ");
            params.put("currencyID", searchVoucher.getCurrencyID());
        }
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (Boolean.TRUE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 1 ");
        } else if (Boolean.FALSE.equals(searchVoucher.getStatusRecorded())) {
            sql.append("AND Recorded = 0 ");
        } else {
        }
        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
            sql.append("AND (reason LIKE :searchValue ");
            if (Boolean.TRUE.equals(isNoMBook)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append(" AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append(" AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC,NOMBook DESC ", "GOtherVoucherExportDTO");
                Common.setParams(query, params);
                mbDepositExportDTOS = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC ,NOFBook DESC ", "GOtherVoucherExportDTO");
                Common.setParams(query, params);
                mbDepositExportDTOS = query.getResultList();
            }
        }
        return new PageImpl<>(mbDepositExportDTOS);
    }

    @Override
    public List<Number> getIndexRow(UUID iD, SearchVoucher searchVoucher, UUID companyID, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        String newSql;
        List<Number> lstIndex = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE companyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucher != null) {
            if (searchVoucher.getCurrencyID() != null) {
                sql.append("AND currencyID = :currencyID ");
                params.put("currencyID", searchVoucher.getCurrencyID());
            }
            if (searchVoucher.getFromDate() != null) {
                sql.append("AND date >= :fromDate ");
                params.put("fromDate", searchVoucher.getFromDate());
            }
            if (searchVoucher.getToDate() != null) {
                sql.append("AND date <= :toDate ");
                params.put("toDate", searchVoucher.getToDate());
            }
            if (searchVoucher.getTypeID() != null) {
                sql.append("AND TypeID = :typeID ");
                params.put("typeID", searchVoucher.getTypeID());
            }
            if (searchVoucher.getStatusRecorded() != null) {
                sql.append("AND Recorded = :recorded ");
                params.put("recorded", searchVoucher.getStatusRecorded() ? 1 : 0);
            }
            if (searchVoucher.getAccountingObjectID() != null) {
                sql.append("AND accountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND ((reason LIKE :searchValue) ");
                if (Boolean.TRUE.equals(isNoMBook)) {
                    sql.append("OR NOMBook LIKE :searchValue) ");
                } else {
                    sql.append("OR NOFBook LIKE :searchValue) ");
                }
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append("AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append("AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        Number indexRow = 0;
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoMBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD ";
            } else {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoFBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD ";
            }
            params.put("iD", iD);
            Query query = entityManager.createNativeQuery(newSql);
            Common.setParams(query, params);
            indexRow = (Number) query.getSingleResult();
        }
        lstIndex.add(indexRow);
        lstIndex.add(total);
        return lstIndex;
    }

    @Override
    public Page<GOtherVoucherKcDsDTO> searchGOtherVoucher(Pageable pageable, String fromDate, String toDate, Integer status, String keySearch, boolean isNoMBook, UUID companyID) {
        List<GOtherVoucherKcDsDTO> otherVoucherDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(status, fromDate, toDate, keySearch, sql, companyID, isNoMBook, params, null);
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT ");
        sqlSelect.append("go.id, ");
        sqlSelect.append("go.date, ");
        sqlSelect.append("go.postedDate, ");
        if (isNoMBook) {
            sqlSelect.append("go.noMBook as no, ");
        } else {
            sqlSelect.append("go.noFBook as no, ");
        }
        sqlSelect.append("go.recorded, ");
        sqlSelect.append("go.reason, ");
        sqlSelect.append("go.currencyId, ");
        sqlSelect.append("go.totalAmount ");

        if (total.longValue() > 0) {
            Query sumQuery = entityManager.createNativeQuery(new StringBuilder("Select sum(a.totalAmount) from (select go.totalAmount ")
                .append(sql).append(") a").toString());
            Common.setParams(sumQuery, params);

            BigDecimal sumTotalAmount = (BigDecimal) sumQuery.getSingleResult();

            Query query = entityManager.createNativeQuery(sqlSelect.toString() + sql.toString() + " order by Date desc, NoFbook DESC, NoMbook DESC ", "GOtherVoucherSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            otherVoucherDTOS = query.getResultList();
            if (otherVoucherDTOS.size() > 0) {
                otherVoucherDTOS.get(0).setSumTotalAmount(sumTotalAmount);
            }
        }
        if (pageable == null) {
            return new PageImpl<>(otherVoucherDTOS);
        }
        return new PageImpl<>(otherVoucherDTOS, pageable, total.longValue());
    }

    @Override
    public List<GOtherVoucherDetailKcDTO> getGOtherVoucherDetailByGOtherVoucherId(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<GOtherVoucherDetailKcDTO> gOtherVoucherDetailKcDTOS = new ArrayList<>();
        params.put("id", id);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("god.id                                               , ");
        sqlBuilder.append("god.description                                      , ");
        sqlBuilder.append("god.debitAccount                                     , ");
        sqlBuilder.append("god.creditAccount                                    , ");
        sqlBuilder.append("god.amount                                             ");
        sqlBuilder.append("FROM GOtherVoucherDetail god ");

        sqlBuilder.append("WHERE god.gOtherVoucherID = :id ");
        sqlBuilder.append("order by god.OrderPriority asc ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "GOtherVoucherDetailKcDTO");
        Common.setParams(query, params);
        gOtherVoucherDetailKcDTOS = query.getResultList();

        return gOtherVoucherDetailKcDTOS;
    }

    private Number getQueryString(Integer status, String fromDate, String toDate, String searchValue, StringBuilder sql, UUID companyID, boolean isNoMBook, Map<String, Object> params, String str) {
        sql.append("FROM GOtherVoucher go ");
        if (!Strings.isNullOrEmpty(str)) {
            sql.append(str);
        }
        sql.append("WHERE 1 = 1 ");
        sql.append(" and go.TYPEID = :typeId ");
        sql.append(" and go.CompanyID = :companyID ");
        params.put("companyID", companyID);
        params.put("typeId", Constants.TypeId.TYPE_G_OTHER_VOUCHER_KC);

        if (isNoMBook) {
            sql.append(" and go.typeLedger in (1) ");
        } else {
            sql.append(" and go.typeLedger in (0) ");
        }

        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("AND go.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("AND go.Date <= :toDate ");
            params.put("toDate", toDate);
        }

        if (status != null) {
            sql.append("AND go.Recorded = :status ");
            params.put("status", status);
        }

        if (!Strings.isNullOrEmpty(searchValue)) {
            sql.append("AND (go.NoFBook LIKE :searchValue ");
            sql.append("OR go.NoMBook LIKE :searchValue ");
            sql.append("OR go.Reason LIKE :searchValue) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    @Override
    public GOtherVoucherKcDTO getGOtherVoucherById(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<GOtherVoucherKcDTO> gOtherVoucherKcDTOS = new ArrayList<>();
        params.put("id", id);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("id, ");
        sqlBuilder.append("typeID, ");
        sqlBuilder.append("currencyID, ");
        sqlBuilder.append("exchangeRate, ");
        sqlBuilder.append("typeLedger, ");
        sqlBuilder.append("reason, ");
        sqlBuilder.append("noMBook, ");
        sqlBuilder.append("noFBook, ");
        sqlBuilder.append("date, ");
        sqlBuilder.append("postedDate, ");
        sqlBuilder.append("totalAmount, ");
        sqlBuilder.append("totalAmountOriginal, ");
        sqlBuilder.append("recorded ");

        sqlBuilder.append("FROM GOtherVoucher ");

        sqlBuilder.append("WHERE id = :id ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "GOtherVoucherKcDTO");
        Common.setParams(query, params);
        gOtherVoucherKcDTOS = query.getResultList();

        return gOtherVoucherKcDTOS.get(0);
    }

    @Override
    public GOtherVoucher findIdByRowNumKc(Pageable pageable, Integer status, String fromDate, String toDate, String searchValue, Integer rowNum, UUID org, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        GOtherVoucher gOtherVoucher = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(status, fromDate, toDate, searchValue, sql, org, isNoMBook, params, null);
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (ORDER BY date DESC , NoFbook DESC, NoMbook DESC ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, GOtherVoucher.class);
            Common.setParams(query, params);
            gOtherVoucher = (GOtherVoucher) query.getSingleResult();
        }
        return gOtherVoucher;
    }

    @Override
    public Page<GOtherVoucherDTO> searchAllPB(Pageable pageable, String fromDate, String toDate, String textSearch, UUID org, Integer currentBook, boolean checkPDF) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDTO> gOtherVoucherDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE companyID = :companyID  and TypeLedger in (:typeLedger, 2) and TypeID = 709 ");
        params.put("companyID", org);
        params.put("typeLedger", currentBook);
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        if (!Strings.isNullOrEmpty(textSearch)) {
            sql.append("AND (reason LIKE :searchValue ");
            if (currentBook.equals(1)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + textSearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        Query sumQuery = entityManager.createNativeQuery("SELECT sum(ISNULL(totalAmountOriginal, 0)) sumTotal " + sql.toString());
        Common.setParams(sumQuery, params);
        BigDecimal sum = (BigDecimal) sumQuery.getSingleResult();
        if (count.longValue() > 0) {
            sql.append(" order by date desc, postedDate desc ");
            Query query = null;
            query = entityManager.createNativeQuery("select ID ID, " +
                "       companyid companyID, " +
                "       branchid branchID, " +
                "       NoFBook, NoMBook," +
                "       typeid typeID, " +
                "       currencyid currencyID, " +
                "       exchangerate exchangeRate, " +
                "       posteddate postedDate, " +
                "       date date, " +
                "       typeledger typeLedger, " +
                "       case :typeLedger when 0 then NoFBook when 1 then NoMBook end noBook, " +
                "       reason reason, " +
                "       totalamount totalAmount, " +
                "       totalamountoriginal totalAmountOriginal, " +
                "       recorded recorded, " +
                "       templateid templateID " + sql.toString(), "GOtherVoucherDTO");
            if (checkPDF) {
                Common.setParams(query, params);
            } else {
                Common.setParamsWithPageable(query, params, pageable, 0);
            }
            gOtherVoucherDTOS = query.getResultList();
            gOtherVoucherDTOS.get(0).setSumAmount(sum);
        }
        return new PageImpl<>(gOtherVoucherDTOS, pageable, count.longValue());
    }

    @Override
    public GOtherVoucher findOneByRowNumPB(String fromDate, String toDate, String textSearch, Integer rowNum, UUID org, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        GOtherVoucher gOtherVoucher = new GOtherVoucher();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE companyID = :companyID  and TypeLedger in (:typeLedger, 2) and TypeID = 709 ");
        params.put("companyID", org);
        params.put("typeLedger", currentBook);
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        if (!Strings.isNullOrEmpty(textSearch)) {
            sql.append("AND (reason LIKE :searchValue ");
            if (currentBook.equals(1)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + textSearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
//        Query sumQuery = entityManager.createNativeQuery("SELECT sum(ISNULL(totalamount, 0)) sumTotal " + sql.toString());
//        Common.setParams(sumQuery, params);
//        BigDecimal sum = (BigDecimal) sumQuery.getSingleResult();
        if (count.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select * from (select *, row_number() over (order by Date) rowNum " + sql.toString() + " ) a where a.rowNum = :rowNum ", GOtherVoucher.class);
            params.put("rowNum", rowNum);
            Common.setParams(query, params);
            gOtherVoucher = (GOtherVoucher) query.getSingleResult();
        }
        return gOtherVoucher;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataKcDiff(String postDate, UUID org, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select Description, ");
        sql.append("IIF(DebitAccount = 1, ToAccount, FromAccount)  as debitAccount, ");
        sql.append("IIF(CreditAccount = 1, ToAccount, FromAccount) as creditAccount, ");
        sql.append("(SELECT sum(COALESCE(DebitAmount, 0) - COALESCE(CreditAmount, 0)) ");
        sql.append("FROM GeneralLedger ");
        sql.append("where Account like :account911p ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("and CompanyID = :companyID and postedDate <= :postedDate) as amount, ");
        sql.append("fromAccountData ");

        sql.append("from AccountTransfer ");
        sql.append("where CompanyID = :companyID ");
        sql.append("and (FromAccount = :account4212 or ToAccount = :account4212) ");
        sql.append("order by AccountTransferOrder ");

        params.put("companyID", org);
        params.put("postedDate", postDate);
        params.put("account4212", Constants.GOtherVoucher.ACCOUNT_4212);
        params.put("account911p", Constants.GOtherVoucher.ACCOUNT_911 + "%");

        Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailDataKcDiffDTO");
        Common.setParams(query, params);
        gOtherVoucherDetails = query.getResultList();
        return gOtherVoucherDetails;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataAccountSpecial(String postDate, UUID org, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select Description, ");
        sql.append("IIF(DebitAccount = 1, ToAccount, FromAccount)  as debitAccount, ");
        sql.append("IIF(CreditAccount = 1, ToAccount, FromAccount) as creditAccount, ");
        sql.append(" case ");
        sql.append("     when FromAccountData = 1 then ");
        sql.append("     (COALESCE((SELECT sum(COALESCE(creditamount, 0)) ");
        sql.append("         FROM GeneralLedger ");
        sql.append("         where account like :account4131p ");
        sql.append("     and AccountCorresponding not like :account635p ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("     and CompanyID = :companyID and postedDate <= :postedDate ");
        sql.append("     and creditamount != 0 ), 0) ");
        sql.append("     - COALESCE((SELECT sum(COALESCE(DebitAmount, 0)) ");
        sql.append("         FROM GeneralLedger ");
        sql.append("         where account like :account4131p ");
        sql.append("     and AccountCorresponding like :account515p ");
        sql.append("     and CompanyID = :companyID and postedDate <= :postedDate ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("     and DebitAmount != 0), 0)) ");
        sql.append("     when FromAccountData = 0 then ");
        sql.append("     (COALESCE((SELECT sum(COALESCE(DebitAmount, 0)) ");
        sql.append("         FROM GeneralLedger ");
        sql.append("         where account like :account4131p ");
        sql.append("     and AccountCorresponding not like :account515p ");
        sql.append("     and CompanyID = :companyID and postedDate <= :postedDate ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("     and DebitAmount != 0), 0) ");
        sql.append("     - COALESCE((SELECT sum(COALESCE(CreditAmount, 0)) ");
        sql.append("         FROM GeneralLedger ");
        sql.append("         where account like :account4131p ");
        sql.append("     and AccountCorresponding like :account635p ");
        sql.append("     and CompanyID = :companyID and postedDate <= :postedDate ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("     and creditamount != 0), 0)) ");
        sql.append("     end                                        as amount, ");
        sql.append("fromAccountData ");
        sql.append("     from AccountTransfer ");
        sql.append("     where CompanyID = :companyID ");
        sql.append("     and (FromAccount = :account4131 or ToAccount = :account4131) ");

        sql.append("     order by AccountTransferOrder ");

        params.put("companyID", org);
        params.put("postedDate", postDate);

        params.put("account4131", Constants.GOtherVoucher.ACCOUNT_4131);
        params.put("account4131p", Constants.GOtherVoucher.ACCOUNT_4131 + "%");
        params.put("account635p", Constants.GOtherVoucher.ACCOUNT_635 + "%");
        params.put("account515p", Constants.GOtherVoucher.ACCOUNT_515 + "%");

        Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailDataKcDiffDTO");
        Common.setParams(query, params);
        gOtherVoucherDetails = query.getResultList();
        return gOtherVoucherDetails;
    }

    @Override
    public List<GOtherVoucherDetailDataKcDTO> getDataKc(String postDate, UUID org, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailDataKcDTO> gOtherVoucherDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select b.Description  as description, ");
        sql.append("IIF(b.DebitAccount = 0, Account, debit)  as debitAccount, ");
        sql.append("IIF(b.DebitAccount = 1, Account, credit) as creditAccount, ");
        sql.append("b.amount as amount, ");
        sql.append("b.FromAccountData as fromAccountData, ");
        sql.append("b.FromAccount as fromAccount, ");
        sql.append("b.DebitAccount as isDebit ");
        sql.append("from ( ");
        sql.append("        select * ");
        sql.append("        from ( ");
        sql.append("        select acct.Description, ");
        sql.append("        acct.AccountTransferOrder, ");
        sql.append("        acct.debit, ");
        sql.append("        acct.credit, ");
        sql.append("        acct.FromAccountData, ");
        sql.append("        gl.Account, ");
        sql.append("            case ");
        sql.append("        when acct.FromAccountData = 0 then (select sum(COALESCE(DebitAmount, 0)) ");
        sql.append("from GeneralLedger ");
        sql.append("where account = gl.Account ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("and CompanyID = :companyID and postedDate <= :postedDate) ");
        sql.append("when acct.FromAccountData = 1 then (select sum(COALESCE(CreditAmount, 0)) ");
        sql.append("from GeneralLedger ");
        sql.append("where account = gl.Account ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("and CompanyID = :companyID and postedDate <= :postedDate) ");
        sql.append("when acct.FromAccountData = 2 ");
        sql.append("then (select sum(COALESCE(DebitAmount, 0) - COALESCE(CreditAmount, 0)) ");
        sql.append("from GeneralLedger ");
        sql.append("where account = gl.Account ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("and CompanyID = :companyID and postedDate <= :postedDate) ");
        sql.append("when acct.FromAccountData = 3 ");
        sql.append("then (select sum(COALESCE(CreditAmount, 0) - COALESCE(DebitAmount, 0)) ");
        sql.append("from GeneralLedger ");
        sql.append("where account = gl.Account ");
        if (isNoMBook) {
            sql.append("and GeneralLedger.TypeLedger in (1, 2) ");
        } else {
            sql.append("and GeneralLedger.TypeLedger in (0, 2) ");
        }
        sql.append("and CompanyID = :companyID and postedDate <= :postedDate) ");
        sql.append("end as amount, ");
        sql.append("acct.DebitAccount, ");
        sql.append("acct.CreditAccount, ");
        sql.append("acct.FromAccount ");
        sql.append("from (select Description, ");
        sql.append("      AccountTransferCode, ");
        sql.append("      AccountTransferOrder, ");
        sql.append("      FromAccount, ");
        sql.append("      ToAccount, ");
        sql.append("      FromAccountData, ");
        sql.append("      DebitAccount, ");
        sql.append("      CreditAccount, ");
        sql.append("      IIF(DebitAccount = 1, ToAccount, FromAccount)  as debit, ");
        sql.append("      IIF(CreditAccount = 1, ToAccount, FromAccount) as credit ");
        sql.append("from AccountTransfer ");
        sql.append("where CompanyID = :companyID ");
        sql.append("and FromAccount not in :listAccount ");
        sql.append("and ToAccount not in :listAccount ");
        sql.append(") acct ");
        sql.append("inner join GeneralLedger GL on (gl.Account is not null and gl.Account like (acct.FromAccount + '%')) ");
        sql.append("group by acct.Description, acct.AccountTransferOrder, acct.debit, acct.credit, acct.FromAccountData, gl.Account, acct.DebitAccount, acct.CreditAccount, acct.FromAccount ");
        sql.append(" ) a ");
        sql.append("where a.amount != 0 ");
        sql.append("    ) b order by b.AccountTransferOrder ");

        params.put("companyID", org);
        params.put("postedDate", postDate);
        params.put("listAccount", Constants.GOtherVoucher.LIST_ACCOUNT);

        Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailDataKcDTO");
        Common.setParams(query, params);
        gOtherVoucherDetails = query.getResultList();
        return gOtherVoucherDetails;
    }

    public Integer findRowNumByID(String fromDate, String toDate, String textSearch, UUID id, UUID org, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM GOtherVoucher WHERE companyID = :companyID  and TypeLedger in (:typeLedger, 2) and TypeID = 709 ");
        params.put("companyID", org);
        params.put("typeLedger", currentBook);
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, sql, "date", "date");
        }

        if (!Strings.isNullOrEmpty(textSearch)) {
            sql.append("AND (reason LIKE :searchValue ");
            if (currentBook.equals(1)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + textSearch + "%");
        }
        Number rowNum = null;
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + sql.toString());
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.longValue() > 0) {
            String newSql = "SELECT rowNum FROM (SELECT id, ROW_NUMBER() over (ORDER BY date DESC) rownum " + sql.toString() + ") a where a.id = :id";
            params.put("id", id);
            Query query = entityManager.createNativeQuery(newSql);
            Common.setParams(query, params);
            rowNum = (Number) query.getSingleResult();
        }
        return rowNum != null ? rowNum.intValue() : -1;
    }

    @Override
    public Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select COUNT(*)" +
            " from GOtherVoucher pr" +
            " where" +
            "   pr.TypeLedger in (:currentBook, 2)" +
            "   and pr.CompanyID = :companyID" +
            "   and TypeID = 709" +
            "   and MONTH(pr.Date) = :month" +
            "   and YEAR(pr.Date) = :year ");
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        params.put("month", month);
        params.put("year", year);
        Query countQuery = entityManager.createNativeQuery(sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.longValue();
    }

    @Override
    public List<UUID> getAllByGOtherVoucherID(UUID gOtherVoucherID, List<UUID> listID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select PrepaidExpenseID from GotherVoucherDetailExpense where GOtherVoucherID = :gOtherVoucherID and PrepaidExpenseID not in :ids");
        params.put("gOtherVoucherID", gOtherVoucherID);
        params.put("ids", listID);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        List<UUID> list = query.getResultList();
        return list;
    }


    @Override
    public void multiDeleteGOtherVoucher(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM GOtherVoucher WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteGOtherVoucherChild(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE GOtherVoucherID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        Common.setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update GOtherVoucher SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public void deleteByCPPeriodID(List<UUID> uuids) {
        String sql1 = "delete GeneralLedger where ReferenceID in (Select c.GOtherVoucherID from CPAcceptance c where c.CPPeriodID = ?);"+
            "delete GOtherVoucherDetail where GOtherVoucherID in (Select c.GOtherVoucherID from CPAcceptance c where c.CPPeriodID = ?);" +
            "delete GOtherVoucher where ID in (Select c.GOtherVoucherID from CPAcceptance c where c.CPPeriodID = ?);";
        jdbcTemplate.batchUpdate(sql1, uuids, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
        });
    }

}
