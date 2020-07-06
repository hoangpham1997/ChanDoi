package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.MBDepositRepositoryCustom;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MBDepositRepositoryImpl extends Common implements MBDepositRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<MBDepositViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<MBDepositViewDTO> MBDeposits = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBDeposit WHERE 1 = 1 ");
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
            String selectSQL = "SELECT ID as id, TypeID as typeID, Date as date, PostedDate as postedDate, TypeLedger as typeLedger," +
                " NoFBook as noFBook, NoMBook as noMBook, AccountingObjectID as accountingObjectID, AccountingObjectName as accountingObjectName, " +
                "AccountingObjectAddress as accountingObjectAddress, BankAccountDetailID as bankAccountDetailID, BankName as bankName, Reason as reason, " +
                "CurrencyID as currencyID, ExchangeRate as exchangeRate, TotalAmount as totalAmount, TotalAmountOriginal as totalAmountOriginal, TotalVATAmount as totalVATAmount, " +
                "TotalVATAmountOriginal as totalVATAmountOriginal, Recorded as recorded, 0 ";
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery(selectSQL + sql.toString()
                    + "ORDER BY date DESC ,posteddate DESC,NOMBook DESC ", "MBDepositViewDTO");
                setParamsWithPageable(query, params, pageable, total);
                MBDeposits = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(selectSQL + sql.toString()
                    + "ORDER BY date DESC ,posteddate DESC,NOFBook DESC ", "MBDepositViewDTO");
                setParamsWithPageable(query, params, pageable, total);
                MBDeposits = query.getResultList();
            }
            Query querySum = entityManager.createNativeQuery("SELECT Sum(TotalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (MBDeposits.size() > 0) {
                MBDeposits.get(0).setTotal(totalamount);
            }
        }
        return new PageImpl<>(MBDeposits, pageable, total.longValue());
    }

    @Override
    public MBDeposit findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        MBDeposit mBD = new MBDeposit();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBDeposit WHERE 1 = 1 AND CompanyID = :companyID ");
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
            // String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + addSort(sort.getSort()) + ") row_num " + sql.toString() + ") where rownum = :rowNum";
            String newSql = "";
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoMBook DESC " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum ";
            } else {
                newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + "ORDER BY Date DESC, PostedDate DESC, NoFBook DESC " + ") rownum " + sql.toString() + ") a where a.rownum = :rowNum ";
            }
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, MBDeposit.class);
            setParams(query, params);
            Object obj = query.getSingleResult();
            mBD = MBDeposit.class.cast(obj);
        }
        return mBD;
    }

    @Override
    public void mutipleRecord(Optional<User> userOptional, Optional<SecurityDTO> currentUserLoginAndOrg, MutipleRecord mutipleRecord) {
        if (userOptional.isPresent()) {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("[MultipleRecord] :listID, :status, :companyID");
            String listID = "";
//            for (int i = 0; i < mutipleRecord.getListID().size(); i++) {
//                if (i == 0) {
//                    listID += "," + mutipleRecord.getListID().get(i).ge;
//                }
//                else{
//                    listID += "," + mutipleRecord.getListID().get(i).getId();
//                }
//            }
            listID += ",";
            System.out.println(listID.toUpperCase());
            System.out.println(currentUserLoginAndOrg.get().getOrg());
            String companyID = currentUserLoginAndOrg.get().getOrg().toString();
            System.out.println(companyID.toUpperCase());
            params.put("listID", listID.toUpperCase());
            params.put("status", mutipleRecord.getStatusRecorded());
            params.put("companyID", companyID.toUpperCase());

            Query countQuery = entityManager.createNativeQuery(sql.toString());
            Common.setParams(countQuery, params);
            countQuery.executeUpdate();
        }
    }

    @Override
    public void mutipleUnRecord(List<UUID> listUnRecord, UUID orgID) {
        String sql1 = "Delete GeneralLedger WHERE referenceID = ? AND CompanyID = ?;";
        jdbcTemplate.batchUpdate(sql1, listUnRecord, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Common.revertUUID(orgID).toString());
        });
    }

    @Override
    public void multiDeleteMBDeposit(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM MBDeposit WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteMBDepositChild(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE MBDepositID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteGeneralLedger(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM GeneralLedger WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ReferenceID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteSAInvoice(UUID orgID, List<UUID> listIDSAInvoice) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM SAInvoice WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :listIDSAInvoice ");
        params.put("listIDSAInvoice", listIDSAInvoice);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteSAInvoiceDetails(List<UUID> listIDSAInvoice) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM SAInvoiceDetails WHERE SAInvoiceID = :listIDSAInvoice ");
        params.put("listIDSAInvoice", listIDSAInvoice);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public List<UUID> findSAInvoiceIDByMBDepositID(UUID orgID, List<UUID> listUUID) {
        StringBuilder sql = new StringBuilder();
        List<UUID> listIDSAInvoice = new ArrayList<UUID>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM SAInvoice WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append(" AND MBDepositID IN :listUUID ");
        params.put("listUUID", listUUID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT ID " + sql.toString());
            setParams(query, params);
            listIDSAInvoice = query.getResultList();
        }
        return listIDSAInvoice;
    }


    @Override
    public Page<MBDepositExportDTO> getAllMBDeposits(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<MBDepositExportDTO> mbDepositExportDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String selectSql = "";
        if (Boolean.TRUE.equals(isNoMBook)) {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, " +
                "NoMBook as noBook, TypeID as typeID, null as typeIDInWord, AccountingObjectName as accountingObjectName," +
                "Reason as description, TotalAmount as totalAmount ";
        } else {
            selectSql = "SELECT ID as id, Date as date, PostedDate as postedDate, NoFBook as noBook, " +
                "TypeID as typeID, null as typeIDInWord, AccountingObjectName as accountingObjectName," +
                "Reason as description, TotalAmount as totalAmount ";
        }

        sql.append("FROM MBDeposit WHERE 1 = 1 ");
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
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC,NOMBook DESC ", "MBDepositExportDTO");
                setParams(query, params);
                mbDepositExportDTOS = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(selectSql.toString() + sql.toString() + " ORDER BY date DESC ,posteddate DESC ,NOFBook DESC ", "MBDepositExportDTO");
                setParams(query, params);
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
        sql.append("FROM MBDeposit WHERE companyID = :companyID ");
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
        Number indexRow = 0;
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoMBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD ";
            } else {
                newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoFBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD ";
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

//    @Override
//    public String findRefTable(List<Integer> typeID, UUID orgID) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("FROM ViewVoucherNo WHERE companyID = :orgID ");
//        params.put("orgID", orgID);
//        sql.append("AND TypeID IN :typeID ");
//        params.put("typeID", typeID);
//        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
//        setParams(countQuerry, params);
//        Number total = (Number) countQuerry.getSingleResult();
//        List<String> listTypeID = new ArrayList<String>();
//        if (total.longValue() > 0) {
//            Query query = entityManager.createNativeQuery("SELECT RefTable " + sql.toString() + " GROUP BY TypeID,RefTable");
//            setParams(query, params);
//            listTypeID = query.getResultList();
//        }
//        return "";
//    }

}
