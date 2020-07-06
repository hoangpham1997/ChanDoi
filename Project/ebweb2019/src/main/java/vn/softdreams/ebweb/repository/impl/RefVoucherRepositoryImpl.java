package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.RefVoucherRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class RefVoucherRepositoryImpl implements RefVoucherRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    public Page<RefVoucherDTO> getAllRefViewVoucher(Pageable pageable, Integer typeGroup, String no, String invoiceNo, Boolean recorded,
                                                    String fromDate, String toDate, boolean isNoMBook, UUID companyID, Integer typeSearch, Integer status) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<RefVoucherDTO> result = new ArrayList<>();
        sql.append("SELECT null id, null refID1, a.id refID2, companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, date, 103) date, convert(varchar, PostedDate, 103) PostedDate, Reason, recorded, TypeID TypeID, b.typegroupid, null as attach from (  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MCReceipt  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MCPayment  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MBTellerPaper  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MBCreditCard  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MBDeposit  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from MBInternalTransfer  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Description Reason, 0 recorded from MCAudit  ");
        sql.append("union  ");
        sql.append("select id, TypeID, 2 typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Reason, 0 recorded from PPOrder  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from PPInvoice  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from PPService  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from PPDiscountReturn  ");
        sql.append("union  ");
        sql.append("select id, TypeID, 2 typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Reason, 0 recorded from SAQuote  ");
        sql.append("union  ");
        sql.append("select id, TypeID, 2 typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Reason, 0 recorded from SAOrder  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from SAInvoice  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, invoiceNo NoMBook, invoiceNo NoFBook, InvoiceDate date, InvoiceDate PostedDate, Reason, 0 recorded from SABill  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from SAReturn  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from RSInwardOutward  ");
        sql.append("union  ");
        sql.append("select id, TypeID, 2 typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Reason, 0 recorded from RSAssemblyDismantlement  ");
        sql.append("union  ");
        sql.append("select id, TypeID, 2 typeLedger, CompanyID, no NoMBook, no NoFBook, date, null PostedDate, Reason, 0 recorded from RSProductionOrder  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, MobilizationOrderFor Reason, recorded from RSTransfer  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, NULL as PostedDate, Reason, recorded from TIIncrement  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, null PostedDate, Reason, recorded from TIDecrement  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from TIAdjustment  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, null PostedDate, Reason, recorded from TITransfer  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, null PostedDate, Description Reason, 0 recorded from TIAudit  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from TIAllocation  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, NULL as PostedDate, Reason, recorded from FAIncrement  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from FADecrement  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from FAAdjustment  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, null PostedDate, Reason, recorded from FATransfer  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, null PostedDate, Description Reason, 0 recorded from FAAudit  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from FADepreciation  ");
        sql.append("union  ");
        sql.append("select id, TypeID, typeLedger, CompanyID, NoMBook, NoFBook, date, PostedDate, Reason, recorded from GOtherVoucher ) a ");
        sql.append("left join type b on a.typeid = b.id where a.companyID = :companyID ");
        params.put("companyID", companyID);
        if (typeSearch != null) {
            if (typeGroup != null && typeSearch == 1) {
                sql.append("and b.typegroupid = :typeGroup ");
                params.put("typeGroup", typeGroup);
            } else if (!Strings.isNullOrEmpty(no) && typeSearch == 2) {
                if (isNoMBook) {
                    sql.append("and a.NoMBook like :no ");
                } else {
                    sql.append("and a.NoFBook like :no ");
                }
                params.put("no", "%" + no + "%");
            } else if (!Strings.isNullOrEmpty(invoiceNo) && typeSearch == 3) {
                sql.append("and a.invoiceNo like :invoiceNo ");
                params.put("invoiceNo", "%" + invoiceNo + "%");
            }
        }

        if (isNoMBook) {
            sql.append("and a.typeLedger in (1, 2) ");
        } else {
            sql.append("and a.typeLedger in (0, 2) ");
        }

        if (recorded != null) {
            sql.append("and a.Recorded = :recorded ");
            params.put("recorded", recorded);
        }

        Common.addDateSearch(fromDate, toDate, params, sql, "date");

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString() + "order by ISNULL(date, '1900-06-05T23:59:00') desc, ISNULL(posteddate, '1900-06-05T23:59:00') desc, " + (isNoMBook ? "noMBook desc" : "noFBook desc"), "RefVoucherDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    @Override
    public List<RefVoucherDTO> getRefViewVoucher(UUID id, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, date, 103) date, convert(varchar, PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid, NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid2 = a.id ");
        sql.append("Where c.refid1 = :id ");

        sql.append("union all ");
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, date, 103) date, convert(varchar, PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid , NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid1 = a.id ");
        sql.append("Where c.refid2 = :id ");

        sql.append("union all ");
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, date, 103) date, convert(varchar, PostedDate, 103) PostedDate, Reason, recorded, typeID typeID, b.typegroupid, c.attach from (  ");
        sql.append("select id, TypeID, CompanyID, invoiceNo NoMBook, invoiceNo NoFBook, InvoiceDate date, InvoiceDate PostedDate, Reason, 0 recorded from SABill) a ");
        sql.append("left join type b on a.typeid = b.id ");
        sql.append("left join refvoucher c on c.refid2 = a.id ");
        sql.append("Where c.refid1 = :id ");

        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RefVoucherDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    /**
     * @param id
     * @param isNoMBook
     * @return
     * @Author Hautv
     */
    @Override
    public List<RefVoucherDTO> getRefViewVoucherByMCReceiptID(UUID id, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid, NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid2 = a.id ");
        sql.append("Where c.refid1 = (select ID from SAInvoice where MCReceiptID =:id) ");

        sql.append("union all ");
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid , NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid1 = a.id ");
        sql.append("Where c.refid2 = (select ID from SAInvoice where MCReceiptID =:id)");

        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RefVoucherDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    /**
     * @param id
     * @param isNoMBook
     * @return
     * @Author Hautv
     */
    @Override
    public List<RefVoucherDTO> getRefVouchersByMBDepositID(UUID id, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid, NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid2 = a.id ");
        sql.append("Where c.refid1 = (select ID from SAInvoice where MBDepositID =:id) ");

        sql.append("union all ");
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid , NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid1 = a.id ");
        sql.append("Where c.refid2 = (select ID from SAInvoice where MBDepositID =:id)");

        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RefVoucherDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<RefVoucherDTO> getRefViewVoucherByPaymentVoucherID(Integer typeID, UUID id, boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid, NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid2 = a.id ");
        if (typeID.equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU) || typeID.equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU)) {
            sql.append("Where c.refid1 = (select ID from PPService where PaymentVoucherID = :id) ");
        } else if (typeID.equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG) || typeID.equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG)) {
            sql.append("Where c.refid1 = (select ID from PPInvoice where PaymentVoucherID = :id) ");
        } else if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU)
        ) {
            sql.append("Where c.refid1 = (select ID from PPService where PaymentVoucherID = :id) ");
        } else if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_HANG)
        ) {
            sql.append("Where c.refid1 = (select ID from PPInvoice where PaymentVoucherID = :id) ");
        }

        sql.append("union all ");
        sql.append("SELECT c.id id, c.refID1 refID1, a.id refID2, a.companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, a.date, 103) date, convert(varchar, a.PostedDate, 103) PostedDate, Reason, recorded, a.typeID typeID, a.typegroupid, c.attach from (  ");
        sql.append("select RefID as id, TypeID, CompanyID, typegroupid , NoMBook, NoFBook, date, PostedDate, Reason, recorded from ViewVoucherNo ) a ");
        sql.append("left join refvoucher c on c.refid1 = a.id ");
        if (typeID.equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU) || typeID.equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU)) {
            sql.append("Where c.refid2 = (select ID from PPService where PaymentVoucherID = :id) ");
        } else if (typeID.equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG) || typeID.equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG)) {
            sql.append("Where c.refid2 = (select ID from PPInvoice where PaymentVoucherID = :id) ");
        } else if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_DICH_VU) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU)
        ) {
            sql.append("Where c.refid2 = (select ID from PPService where PaymentVoucherID = :id) ");
        } else if (
            typeID.equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_HANG) ||
                typeID.equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_HANG)
        ) {
            sql.append("Where c.refid2 = (select ID from PPInvoice where PaymentVoucherID = :id) ");
        }
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RefVoucherDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public Page<RefVoucherSecondDTO> getViewVoucherToModal(Pageable pageable, Integer typeGroup, String fromDate, String toDate, boolean isNoMBook, UUID org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<RefVoucherSecondDTO> result = new ArrayList<>();
        sql.append("SELECT id                                id, " +
            "       RefID                             refID1, " +
            "       RefID                                refID2, " +
            "       companyID, ");
        if (isNoMBook) {
            sql.append("NoMBook no, ");
        } else {
            sql.append("NoFBook no, ");
        }
        sql.append("convert(varchar, date, 103)       date, " +
            "       convert(varchar, PostedDate, 103) PostedDate, " +
            "       Reason, " +
            "       recorded, " +
            "       TypeID                            TypeID, " +
            "       typegroupid, " +
            "       TotalAmount, TotalAmountOriginal from ViewVoucherNo where companyID = :companyID and TypeLedger in (:typeLedger, 2) ");
        params.put("companyID", org);
        params.put("typeLedger", isNoMBook ? Constants.TypeLedger.MANAGEMENT_BOOK : Constants.TypeLedger.FINANCIAL_BOOK);
        if (typeGroup != null) {
            sql.append(" and TypeGroupID = :typeGroupID ");
            params.put("typeGroupID", typeGroup);
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearch(fromDate, toDate, params, sql, "date");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) from (" + sql.toString() + ") a");
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString() + "order by ISNULL(date, '1900-06-05T23:59:00') desc, ISNULL(posteddate, '1900-06-05T23:59:00') desc, " + (isNoMBook ? "noMBook desc" : "noFBook desc"), "RefVoucherSecondDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }
}
