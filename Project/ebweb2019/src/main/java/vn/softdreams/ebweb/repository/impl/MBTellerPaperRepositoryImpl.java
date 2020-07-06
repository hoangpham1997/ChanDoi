package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.MBTellerPaper;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.MBTellerPaperRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class MBTellerPaperRepositoryImpl implements MBTellerPaperRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<MBTellerPaper> findAll(Pageable pageable, SearchVoucher searchVoucher, UUID companyID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        List<MBTellerPaper> lstMBT = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String joinType = " a left join Type b on a.typeID = b.ID ";
        sql.append("FROM MBTellerPaper" + joinType + "WHERE companyID = :companyID ");
        params.put("companyID", companyID);
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
            sql.append("OR AccountingObjectBankAccount LIKE :searchValue ");
            sql.append("OR AccountingObjectBankName LIKE :searchValue ");
            sql.append("OR NoFBook LIKE :searchValue) ");
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        String orderBy = "";
        if (displayOnBook == 0) {
            orderBy = " order by Date DESC, PostedDate DESC, noFbook DESC ";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2)");
        } else if (displayOnBook == 1) {
            orderBy = " order by Date DESC, PostedDate DESC, noMbook DESC ";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.*, b.TypeName  " + sql.toString() + orderBy, MBTellerPaper.class);
            setParamsWithPageable(query, params, pageable, total);
            lstMBT = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lstMBT.get(0).setTotal(totalamount);
        }
        return new PageImpl<>(lstMBT, pageable, total.longValue());
    }

    @Override
    public MBTellerPaper findOneByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        MBTellerPaper mBTP = new MBTellerPaper();
        // List<MBTellerPaper> lstMBT = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBTellerPaper WHERE companyID = :companyID ");
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
                sql.append("OR AccountingObjectBankAccount LIKE :searchValue ");
                sql.append("OR AccountingObjectBankName LIKE :searchValue ");
                sql.append("OR NoFBook LIKE :searchValue) ");
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            // String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( " + addSort(sort.getSort()) + ") row_num " + sql.toString() + ") where rownum = :rowNum";
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC, PostedDate DESC, NoFBook DESC, NoMBook DESC ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, MBTellerPaper.class);
            setParams(query, params);
            Object obj = query.getSingleResult();
            mBTP = MBTellerPaper.class.cast(obj);
        }
        return mBTP;
    }

    @Override
    public List<Number> getIndexRow(UUID iD, SearchVoucher searchVoucher, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<Number> lstIndex = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MBTellerPaper WHERE companyID = :companyID ");
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
                sql.append("OR AccountingObjectBankAccount LIKE :searchValue ");
                sql.append("OR AccountingObjectBankName LIKE :searchValue ");
                sql.append("OR NoFBook LIKE :searchValue) ");
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        Number indexRow = 0;
        if (total.longValue() > 0) {
//            String newSql = "SELECT rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC ,PostedDate DESC ,NoFBook DESC ,NoMBook DESC ) rowNum " + sql.toString() + ") a where a.ID = :iD";
            String newSql = "SELECT a.rowNum FROM (SELECT *, ROW_NUMBER() over ( order by Date DESC, PostedDate DESC, NoFBook DESC, NoMBook DESC ) as rowNum " + sql.toString() + ") as a where a.ID = :iD";
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
    public Page<MBTellerPaperDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        List<MBTellerPaperDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String joinType = " a left join Type b on a.typeID = b.ID ";
        sql.append("FROM MBTellerPaper" + joinType + "WHERE companyID = :companyID ");
        params.put("companyID", companyID);
        String orderBy = "";
        if (displayOnBook == 0) {
            orderBy = " order by Date DESC, PostedDate DESC, noFbook DESC ";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2)");
        } else if (displayOnBook == 1) {
            orderBy = " order by Date DESC, PostedDate DESC, noMbook DESC ";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue () > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.*, b.TypeName  " + sql.toString() + orderBy, "MBTellerPaperDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            if (lst.size() > 0) {
                lst.get(0).setTotal(totalamount);
            }
        }
        return new PageImpl<>(((List<MBTellerPaperDTO>) lst), pageable, total.longValue());
    }

    @Override
    public UpdateDataDTO getNoBookById(UUID paymentVoucherID) {
        String sql = "select NoFBook as noFBook, NoMBook as noMBook, TypeLedger as type from MBTellerPaper where ID = ?1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, paymentVoucherID);
        Object result[] = (Object[]) query.getSingleResult();
        return new UpdateDataDTO((String) result[0], (String) result[1], (Integer) result[2]);
    }

    @Override
    public List<MBTellerPaperDTO> findAllForExport(SearchVoucher searchVoucher, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<MBTellerPaperDTO> lstMBT = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String joinType = " a left join Type b on a.typeID = b.ID ";
        sql.append("FROM MBTellerPaper" + joinType + "WHERE companyID = :companyID ");
        params.put("companyID", org);
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
            sql.append("OR AccountingObjectBankAccount LIKE :searchValue ");
            sql.append("OR AccountingObjectBankName LIKE :searchValue ");
            sql.append("OR NoFBook LIKE :searchValue) ");
            params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.*, b.TypeName " + sql.toString() + " order by Date DESC, PostedDate DESC, NoFBook DESC, NoMBook DESC ", "MBTellerPaperDTO");
            setParams(query, params);
            lstMBT = query.getResultList();
        }
        return lstMBT;
    }

    @Override
    public Object findIDRef(UUID uuid, Integer typeID) {
        Map<String, Object> params = new HashMap<>();
        String sql = "";
        if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU)
        ) {
            sql = "select ID from PPService where PaymentVoucherID = :id";
        } else if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_HANG)
        ) {
            sql = "select ID, StoredInRepository from PPInvoice where PaymentVoucherID = :id";
        }
        Query query = entityManager.createNativeQuery(sql);
        params.put("id", uuid);
        Common.setParams(query, params);
        return query.getSingleResult();
    }

    @Override
    public List<PPInvoiceDTO> findVoucherByListPPInvoiceID(List<UUID> uuids, int voucherTypeID) {
        StringBuilder sql = new StringBuilder();
        List<PPInvoiceDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select ID, Date, NoFBook, NoMBook, DueDate from ");
        switch (voucherTypeID) {
            case TypeConstant
                .MUA_HANG_CHUA_THANH_TOAN:
                sql.append("PPInvoice where ID in :listID ;");
                break;
            case TypeConstant
                .PP_SERVICE:
                sql.append("PPService where ID in :listID ;");
                break;
            case TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC:
                sql.append("GOtherVoucher where ID in :listID ;");
                break;
            case TypeConstant
                .OPENING_BLANCE_AC:
                sql = new StringBuilder();
                sql.append("select ReferenceID as ID, Date, NoFBook, NoMBook, NULL as DueDate from GeneralLedger where ReferenceID in :listID ;");
                break;
        }
        params.put("listID", uuids);
        Query query = entityManager.createNativeQuery(sql.toString(), "ViewFromMCPaymentDTO");
        Utils.setParams(query, params);
        lst = query.getResultList();
        return lst;
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

    /**
     * @param sort
     * @return
     */
    public static String addSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null)
            return "";
        for (Sort.Order order : sort) {

            orderSql.append("ORDER BY ");
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            break;
        }
        return orderSql.toString();
    }

    public static String addMultiSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null) {
            return "";
        }
        orderSql.append("ORDER BY ");
        int i = 0;
        for (Sort.Order order : sort) {
            if (i > 0) {
                orderSql.append(", ");
            }
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            i++;
        }
        return orderSql.toString();
    }

    @Override
    public void multiDeleteMBTellerPaper(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM MBTellerPaper WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteChildMBTellerPaper(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE MBTellerPaperID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }
}
