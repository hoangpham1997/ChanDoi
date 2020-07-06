package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.AccountingObjectRepositoryCustom;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

public class AccountingObjectRepositoryImpl implements AccountingObjectRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    /**
     * Hautv
     *
     * @return
     */
    @Override
    public List<AccountingObjectDTO> findAllDTO(List<UUID> companyID, Boolean isObjectType12) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObjectDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject where companyID in :companyID and IsActive = 1");
        if (isObjectType12) {
            sql.append("AND (ObjectType = 1 or ObjectType = 2) ");
        }
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY AccountingObjectCode ", "AccountingObjectDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectDTO> findAllAccountingObjectDTO(List<UUID> companyID, Boolean isObjectType12) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObjectDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject where companyID in :companyID ");
        if (isObjectType12) {
            sql.append("AND (ObjectType = 1 or ObjectType = 2) ");
        }
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY AccountingObjectCode ", "AccountingObjectDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    /***
     * @author phuonghv
     * @param taskMethod 0: mua dich vu
     * @return
     */
    @Override
    public List<AccountingObjectDTO> findAllDTO(Integer taskMethod, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject ");
        if (taskMethod != null) {
            sql.append(" where CompanyID in :org ");
            params.put("org", org);
            if (taskMethod == 0) {
                sql.append("and objectType in (0, 2) and isActive = 1");
            }
        }
        String selectQuery = "SELECT id, branchId, companyId, accountingObjectCode, accountingObjectName, contactName, " +
            "employeeBirthday, accountingObjectAddress, tel, fax, email, " +
            "website, bankName, taxCode, isEmployee, isActive , objectType ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectPPServiceDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectDTO> findAllDTO(String className, UUID org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select distinct (a.id), a.AccountingObjectCode, a.AccountingObjectName from AccountingObject a left join ");
        sql.append(className);
        sql.append(" b on b.AccountingObjectID = a.id where a.CompanyID = :org and (objectType = 0 or objectType = 1 or objectType = 2) and isactive = 1 order by AccountingObjectCode ");
        params.put("org", org);
        Query query = entityManager.createNativeQuery(sql.toString(), "AccountingObjectCbbDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     */
    @Override
    public Page<PPPayVendorDTO> getPPPayVendorByDate(String fromDate, String toDate, UUID companyID, Integer soLamViec) {
        List<PPPayVendorDTO> ppPayVendorDTOs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GetPPPayVendor] @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @TypeLedger = :soLamViec");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("soLamViec", soLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "PPPayVendorDTO");
        Common.setParams(query, params);
        ppPayVendorDTOs = query.getResultList();
        return new PageImpl<>(ppPayVendorDTOs);
    }

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     * @param accountObjectID : mã nhà cung cấp
     */
    @Override
    public Page<PPPayVendorBillDTO> getPPPayVendorBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID, UUID companyID, Integer soLamViec) {
        List<PPPayVendorBillDTO> ppPayVendorBillDTOs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GetPPPayVendorBill] @AccountingObjectID = :accountingObjectID, @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @TypeLedger = :soLamViec");
        params.put("accountingObjectID", accountObjectID);
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("soLamViec", soLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "PPPayVendorBillDTO");
        Common.setParams(query, params);
        ppPayVendorBillDTOs = query.getResultList();
        return new PageImpl<>(ppPayVendorBillDTOs);
    }

    @Override
    public Page<SAReceiptDebitDTO> getSAReceiptDebitByDate(String fromDate, String toDate, UUID org, Integer soLamViec) {
        List<SAReceiptDebitDTO> saReceiptDebitDTOs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GetReceiptDebit] @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @TypeLedger = :soLamViec");
        params.put("companyID", org);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("soLamViec", soLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "SAReceiptDebitDTO");
        Common.setParams(query, params);
        saReceiptDebitDTOs = query.getResultList();
        return new PageImpl<>(saReceiptDebitDTOs);
    }

    @Override
    public Page<SAReceiptDebitBillDTO> getSAReceiptDebitBillByAccountingObjectID(String fromDate, String toDate, UUID accountObjectID, UUID org, Integer soLamViec) {
        List<SAReceiptDebitBillDTO> saReceiptDebitBillDTOs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_GetReceiptDebitBill] @AccountingObjectID = :accountingObjectID, @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @TypeLedger = :soLamViec");
        params.put("accountingObjectID", accountObjectID);
        params.put("companyID", org);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("soLamViec", soLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "SAReceiptDebitBillDTO");
        Common.setParams(query, params);
        saReceiptDebitBillDTOs = query.getResultList();
        return new PageImpl<>(saReceiptDebitBillDTOs);
    }

    @Override
    public Page<AccountingObject> pageableAllAccountingObjects(Pageable pageable, Integer typeAccountingObject, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObject> accountingObjects = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", org);
        }
        if (typeAccountingObject == Constants.typeAccountingObject.KHACH_HANG) {
            sql.append(" AND (objectType = 1 OR objectType = 2 OR objectType = 3) ");
        }
        if (typeAccountingObject == Constants.typeAccountingObject.NHA_CUNG_CAP) {
            sql.append(" AND (objectType = 0 OR objectType = 2 OR objectType = 3) ");
        }
        if (typeAccountingObject == Constants.typeAccountingObject.NHAN_VIEN) {
            sql.append(" AND isEmployee = 1 ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by AccountingObjectCode"
                , AccountingObject.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            accountingObjects = query.getResultList();

        }
        return new PageImpl<>(accountingObjects, pageable, total.longValue());
    }

    @Override
    public Page<AccountingObject> getAllByListCompany(Pageable pageable, Integer typeAccountingObject, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObject> accountingObjects = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        if (typeAccountingObject == Constants.typeAccountingObject.KHACH_HANG) {
            sql.append(" AND (objectType = 1 OR objectType = 2) ");
        }
        if (typeAccountingObject == Constants.typeAccountingObject.NHA_CUNG_CAP) {
            sql.append(" AND (objectType = 0 OR objectType = 2) ");
        }
        if (typeAccountingObject == Constants.typeAccountingObject.NHAN_VIEN) {
            sql.append(" AND isEmployee = 1 ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by AccountingObjectCode"
                , AccountingObject.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            accountingObjects = query.getResultList();
        }
        return new PageImpl<>(accountingObjects, pageable, total.longValue());
    }

    @Override
    public Page<AccountingObject> findVoucherByAccountingObjectID(Pageable pageable, UUID id) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObject> accountingObjects = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject WHERE 1 = 1 ");
        if (id != null) {
            sql.append("AND id = :id ");
            params.put("id", id);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), AccountingObject.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            accountingObjects = query.getResultList();
        }
        return new PageImpl<>(accountingObjects, pageable, total.longValue());
    }


    /***
     * @author ductv
     * Tìm kiếm nâng cao
     */
    @Override
    public Page<AccountingObject> findAll1(Pageable pageable, Integer typeAccountingObject, SearchVoucherAccountingObjects searchVoucherAccountingObjects, List<UUID> companyID) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObject> lstMG = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject A LEFT JOIN AccountingObjectGroup B ON (A.AccountObjectGroupID = B.ID) WHERE A.companyID in :companyID ");
        params.put("companyID", companyID);
        if (searchVoucherAccountingObjects.getScaleType() != null) {
            sql.append("AND A.ScaleType = :ScaleType ");
            params.put("ScaleType", searchVoucherAccountingObjects.getScaleType());
            if (searchVoucherAccountingObjects.getObjectType() == null) {
                if (typeAccountingObject == Constants.typeAccountingObject.KHACH_HANG) {
                    sql.append(" AND (objectType = 1 OR objectType = 2 OR objectType = 3) ");
                } else if (typeAccountingObject == Constants.typeAccountingObject.NHA_CUNG_CAP) {
                    sql.append(" AND (objectType = 0 OR objectType = 2 OR objectType = 3) ");
                }
            }
        }
        if (typeAccountingObject == Constants.typeAccountingObject.KHACH_HANG) {
            if (searchVoucherAccountingObjects.getObjectType() != null) {
                sql.append("AND A.ObjectType = :ObjectType ");
                params.put("ObjectType", searchVoucherAccountingObjects.getObjectType());
            }
            if (searchVoucherAccountingObjects.getAccountingObjectGroup() != null) {
                sql.append("AND A.AccountObjectGroupID = :AccountObjectGroupID ");
                params.put("AccountObjectGroupID", searchVoucherAccountingObjects.getAccountingObjectGroup());
            }
            if (!Strings.isNullOrEmpty(searchVoucherAccountingObjects.getKeySearch())) {
                sql.append(" AND (objectType = 1 OR objectType = 2 OR objectType = 3) ");
                sql.append("AND ((B.AccountingObjectGroupName LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectCode LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectName LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectAddress LIKE :searchValue) ");
                sql.append("OR (A.Tel LIKE :searchValue) ");
                sql.append("OR (A.Fax LIKE :searchValue) ");
                sql.append("OR (A.Email LIKE :searchValue) ");
                sql.append("OR (A.Website LIKE :searchValue)) ");
                params.put("searchValue", "%" + searchVoucherAccountingObjects.getKeySearch() + "%");
            }
        }
        if (typeAccountingObject == Constants.typeAccountingObject.NHA_CUNG_CAP) {
            if (searchVoucherAccountingObjects.getObjectType() != null) {
                sql.append("AND A.ObjectType = :ObjectType ");
                params.put("ObjectType", searchVoucherAccountingObjects.getObjectType());
            }
            if (searchVoucherAccountingObjects.getAccountingObjectGroup() != null) {
                sql.append("AND A.AccountObjectGroupID in :AccountObjectGroupID ");
                params.put("AccountObjectGroupID", searchVoucherAccountingObjects.getAccountingObjectGroup());
            }
            if (!Strings.isNullOrEmpty(searchVoucherAccountingObjects.getKeySearch())) {
                sql.append(" AND (objectType = 0 OR objectType = 2 OR objectType = 3) ");
                sql.append("AND ((B.AccountingObjectGroupName LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectCode LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectName LIKE :searchValue) ");
                sql.append("OR (A.AccountingObjectAddress LIKE :searchValue) ");
                sql.append("OR (A.Tel LIKE :searchValue) ");
                sql.append("OR (A.Fax LIKE :searchValue) ");
                sql.append("OR (A.Email LIKE :searchValue) ");
                sql.append("OR (A.Website LIKE :searchValue)) ");
                params.put("searchValue", "%" + searchVoucherAccountingObjects.getKeySearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by AccountingObjectCode", AccountingObject.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lstMG = query.getResultList();
        }
        return new PageImpl<>(lstMG, pageable, total.longValue());
    }

    public Page<AccountingObject> findAllEmployee1(Pageable pageable, SearchVoucherEmployee searchVoucherEmployee, List<UUID> companyID) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObject> lstMG = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject A LEFT JOIN EbOrganizationUnit B ON (A.PaymentClauseID = B.ID) WHERE A.companyID in :companyID ");
        params.put("companyID", companyID);
        if (searchVoucherEmployee.getObjectType() != null) {
            sql.append("AND A.ObjectType = :ObjectType ");
            params.put("ObjectType", searchVoucherEmployee.getObjectType());
        }
        if (searchVoucherEmployee.getContactSex() != null) {
            sql.append("AND A.ContactSex = :ContactSex ");
            params.put("ContactSex", searchVoucherEmployee.getContactSex());
        }
        if (searchVoucherEmployee.getOrganizationUnitName() != null) {
            sql.append("AND A.DepartmentID = :DepartmentID ");
            params.put("DepartmentID", searchVoucherEmployee.getOrganizationUnitName());
        }
        if (!Strings.isNullOrEmpty(searchVoucherEmployee.getKeySearch())) {
//            sql.append("AND (A.ObjectType LIKE :searchValue) ");
//            sql.append("OR (A.ContactSex LIKE :searchValue) ");
            sql.append("AND ((B.OrganizationUnitCode LIKE :searchValue) ");
            sql.append("OR (B.OrganizationUnitName LIKE :searchValue) ");
            sql.append("OR (A.AccountingObjectCode LIKE :searchValue) ");
            sql.append("OR (A.AccountingObjectName LIKE :searchValue) ");
            sql.append("OR (A.AccountingObjectAddress LIKE :searchValue) ");
            sql.append("OR (A.Tel LIKE :searchValue) ");
            sql.append("OR (A.Fax LIKE :searchValue) ");
            sql.append("OR (A.Email LIKE :searchValue) ");
            sql.append("OR (A.Website LIKE :searchValue)) ");
            params.put("searchValue", "%" + searchVoucherEmployee.getKeySearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT A.* " + sql.toString(), AccountingObject.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lstMG = query.getResultList();
        }
        return new PageImpl<>(lstMG, pageable, total.longValue());
    }

    @Override
    public List<AccountingObject> findByIsActiveCustom(List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject where CompanyID in :org and IsActive = 1 ");
        params.put("org", org);
        String selectQuery = "SELECT id,  accountingObjectCode, accountingObjectName, accountingObjectAddress ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        return ((List<AccountingObjectDTO>) query.getResultList()).stream().map(dto -> {
            AccountingObject accountingObject = new AccountingObject();
            accountingObject.setId(dto.getId());
            accountingObject.setAccountingObjectCode(dto.getAccountingObjectCode());
            accountingObject.setAccountingObjectName(dto.getAccountingObjectName());
            accountingObject.setAccountingObjectAddress(dto.getAccountingObjectAddress());
            return accountingObject;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectByGroupID(List<UUID> org, Boolean similarBranch, Boolean checkShared, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from AccountingObject b where IsActive = 1 and ObjectType in (0, 2)");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");
            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", org);
        if (id != null) {
            sql.append(" and AccountObjectGroupID = :id");
            params.put("id", id);
        }
        sql.append(" order by AccountingObjectCode");
        String selectQuery = "SELECT id, accountingObjectCode, accountingObjectName, accountingObjectAddress ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        List<AccountingObjectDTO> rs = query.getResultList();
        return rs;
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectByGroupIDKH(List<UUID> org, Boolean similarBranch, Boolean checkShared, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AccountingObject where ObjectType in (1,2) and IsActive = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID IN :org) OR (ParentID IN :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", org);
        if (id != null) {
            sql.append(" and AccountObjectGroupID = :id");
            params.put("id", id);
        }
        sql.append(" order by AccountingObjectCode");
        String selectQuery = "SELECT id, accountingObjectCode, accountingObjectName, accountingObjectAddress ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        List<AccountingObjectDTO> rs = query.getResultList();
        return rs;
    }

    @Override
    public List<AccountingObjectDTO> findAllDTOAll(Integer taskMethod, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObject ");
        if (taskMethod != null) {
            sql.append(" where CompanyID in :org ");
            params.put("org", org);
            if (taskMethod == 0) {
                sql.append("and objectType in (0, 2) ");
            }
        }
        String selectQuery = "SELECT id, branchId, companyId, accountingObjectCode, accountingObjectName, contactName, " +
            "employeeBirthday, accountingObjectAddress, tel, fax, email, " +
            "website, bankName, taxCode, isEmployee, isActive , objectType ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectPPServiceDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectDTO> getAccountingObjectsByGroupIDSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AccountingObject where ObjectType in (0,2) and IsActive = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        if (id != null) {
            sql.append(" and AccountObjectGroupID = :id");
            params.put("id", id);
        }
        sql.append(" order by AccountingObjectCode");
        String selectQuery = "SELECT id, accountingObjectCode, accountingObjectName, accountingObjectAddress ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObject> getAccountingObjectByGroupIDKHSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared, UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AccountingObject where ObjectType in (1,2) and IsActive = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        if (id != null) {
            sql.append(" and AccountObjectGroupID = :id");
            params.put("id", id);
        }
        sql.append(" order by AccountingObjectCode");
        String selectQuery = "SELECT id, accountingObjectCode, accountingObjectName, accountingObjectAddress ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsEmployeeSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select * from AccountingObject where IsActive = 1 and IsEmployee = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        sql.append(" order by AccountingObjectCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), AccountingObject.class);
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObject> insertBulk(List<AccountingObject> accountingObjects) {
        final List<AccountingObject> savedEntities = new ArrayList<>(accountingObjects.size());
        int i = 0;
        for (AccountingObject accountingObject : accountingObjects) {
            if (save(entityManager, accountingObject) == 1) {
                savedEntities.add(accountingObject);
            }
            i++;
            if (i > vn.softdreams.ebweb.config.Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;
    }

    @Override
    public List<AccountingObject> getAllAccountingObjectsByCompanyID(UUID orgID, boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select * from AccountingObject where IsActive = 1 and ObjectType in (1,2,3) and CompanyID = :CompanyID ");
        if (isDependent) {
            sql.append(" OR CompanyID in (select ID from EbOrganizationUnit where (ParentID = :CompanyID and UnitType = 1 and IsActive = 1 and AccType = 0))");
        }
        params.put("CompanyID", orgID);
        sql.append(" order by AccountingObjectCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), AccountingObject.class);
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectDTO> getAllAccountingObjectByCompany(List<UUID> comIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" Select ID, AccountingObjectCode, AccountingObjectName, AccountingObjectAddress from AccountingObject where CompanyID in :comID and IsActive = 1 ");
        params.put("comID", comIds);
        Query query = entityManager.createNativeQuery(sql.toString(), "AccountingObjectCustom");
        Common.setParams(query, params);
        return query.getResultList();
    }

    private int save(EntityManager em, AccountingObject hisPsTbbDtl) {
        if (hisPsTbbDtl.getId() != null) {
            em.merge(hisPsTbbDtl);
        } else {
            em.persist(hisPsTbbDtl);
        }
        return 1;
    }
}
