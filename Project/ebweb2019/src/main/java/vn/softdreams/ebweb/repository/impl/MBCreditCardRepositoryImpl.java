package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.MBCreditCard;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.MBCreditCardRepositoryCustom;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardExportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

public class MBCreditCardRepositoryImpl extends Common implements MBCreditCardRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<MBCreditCardViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean NoMBook) {
        StringBuilder sql = new StringBuilder();
        List<MBCreditCardViewDTO> MBCreditCards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBCreditCard WHERE 1 = 1 ");
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

        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
            sql.append("AND (accountingObjectName LIKE :searchValue ");
            sql.append("OR accountingObjectAddress LIKE :searchValue ");
            sql.append("OR reason LIKE :searchValue ");
            if (Boolean.TRUE.equals(NoMBook)) {
                sql.append("OR NOMBook LIKE :searchValue) ");
            } else {
                sql.append("OR NOFBook LIKE :searchValue) ");
            }
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        if (Boolean.TRUE.equals(NoMBook)) {
            sql.append(" AND (TypeLedger = 1 OR TypeLedger = 2) ");
        } else {
            sql.append(" AND (TypeLedger = 0 OR TypeLedger = 2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            String selectSQL = "SELECT ID as id, TypeID as typeID, Date as date, PostedDate as postedDate, TypeLedger as typeLedger, CreditCardNumber as creditCardNumber, " +
                " NoFBook as noFBook, NoMBook as noMBook, AccountingObjectID as accountingObjectID, AccountingObjectName as accountingObjectName, " +
                "AccountingObjectAddress as accountingObjectAddress, Reason as reason, " +
                "CurrencyID as currencyID, ExchangeRate as exchangeRate, TotalAmount as totalAmount, TotalAmountOriginal as totalAmountOriginal, TotalVATAmount as totalVATAmount, " +
                "TotalVATAmountOriginal as totalVATAmountOriginal, Recorded as recorded, 0 ";
            if (Boolean.TRUE.equals(NoMBook)) {
                Query query = entityManager.createNativeQuery(selectSQL + sql.toString() + "ORDER BY date DESC,posteddate DESC ,NOMBook DESC ", "MBCreditCardViewDTO");
                setParamsWithPageable(query, params, pageable, total);
                MBCreditCards = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(selectSQL + sql.toString() + "ORDER BY date DESC,posteddate DESC ,NOFBook DESC ", "MBCreditCardViewDTO");
                setParamsWithPageable(query, params, pageable, total);
                MBCreditCards = query.getResultList();
            }
            Query querySum = entityManager.createNativeQuery("SELECT Sum(TotalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            MBCreditCards.get(0).setTotal(totalamount);
        }
        return new PageImpl<>(MBCreditCards, pageable, total.longValue());
    }

    @Override
    public MBCreditCard findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        String newSql;
        MBCreditCard mBCC = new MBCreditCard();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBCreditCard WHERE 1 = 1 AND companyID = :companyID ");
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
                sql.append("AND (accountingObjectName LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR bankName LIKE :searchValue ");
                sql.append("OR reason LIKE :searchValue ");
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
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoMBook DESC  " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            } else {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoFBook DESC  " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            }
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, MBCreditCard.class);
            setParams(query, params);
            Object obj = query.getSingleResult();
            mBCC = MBCreditCard.class.cast(obj);
        }
        return mBCC;
    }

    @Override
    public Page<MBCreditCardExportDTO> getAllMBCreditCards(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<MBCreditCardExportDTO> mbCreditCardExportDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String selectSql = "";
        if (Boolean.TRUE.equals(isNoMBook)) {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, NoMBook as noBook, " +
                "TypeID as typeID, null as typeIDInWord, CreditCardNumber as creditCardNumber, " +
                "AccountingObjectName as accountingObjectName, Reason as description, TotalAmount as totalAmount ";
        } else {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, NoFBook as noBook, " +
                "TypeID as typeID, null as typeIDInWord, CreditCardNumber as creditCardNumber, " +
                "AccountingObjectName as accountingObjectName, Reason as description, TotalAmount as totalAmount ";
        }
        sql.append("FROM MBCreditCard WHERE 1 = 1 ");
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
            sql.append("AND (accountingObjectName LIKE :searchValue ");
            sql.append("OR accountingObjectAddress LIKE :searchValue ");
            sql.append("OR bankName LIKE :searchValue ");
            sql.append("OR reason LIKE :searchValue ");
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
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC,NOMBook DESC ", "MBCreditCardExportDTO");
                setParams(query, params);
                mbCreditCardExportDTOS = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC ,NOFBook DESC ", "MBCreditCardExportDTO");
                setParams(query, params);
                mbCreditCardExportDTOS = query.getResultList();
            }
        }
        return new PageImpl<>(mbCreditCardExportDTOS);
    }


    @Override
    public UpdateDataDTO getNoBookById(UUID paymentVoucherID) {
        String sql = "select NoFBook as noFBook, NoMBook as noMBook, TypeLedger as type from MBCreditCard where ID = ?1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, paymentVoucherID);
        Object result[] = (Object[]) query.getSingleResult();
        return new UpdateDataDTO((String) result[0], (String) result[1], (Integer) result[2]);
    }

    @Override
    public List<Number> getIndexRow(UUID iD, SearchVoucher searchVoucher, UUID companyID, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        String newSql;
        List<Number> lstIndex = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBCreditCard WHERE companyID = :companyID ");
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
                sql.append("AND (accountingObjectName LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR reason LIKE :searchValue ");
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
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        Number indexRow = 0;
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoMBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD";
            } else {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoFBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD";
            }
            params.put("iD", iD);
            Query query = entityManager.createNativeQuery(newSql);
            setParams(query, params);
            indexRow = (Number) query.getSingleResult();
        }
        lstIndex.add(indexRow);
        lstIndex.add(total);
        return lstIndex;
    }

    @Override
    public void multiDeleteMBCreditCard(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM MBCreditCard WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteChildMBCreditCard(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE MBCreditCardID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }
}
