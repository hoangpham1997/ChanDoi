package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.sql.Types;
import java.util.*;

import static vn.softdreams.ebweb.service.util.Common.setParams;
import static vn.softdreams.ebweb.service.util.Common.setParamsWithPageable;

@Repository
public class UtilsRepositoryImpl implements UtilsRepository {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private GenCodeService genCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationUnitRepository organizationUnitRepository;

    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    final Integer TYPE_MC_RECEIPT = 100; // Phiếu thu
    final Integer TYPE_MC_PAYMENT = 110; // Phiếu chi
    final Integer TYPE_MC_AUDIT = 180;
    final Integer TYPE_SA_ORDER = 310;
    final Integer TYPE_G_OTHER_VOUCHER = 700;
    final Integer TYPE_LEDGER_FBOOK = 0; // Sổ tài chính
    final Integer TYPE_LEDGER_MBOOK = 1; // Sổ quản trị


    /**
     * Hautv
     *
     * @param id
     * @param typeID
     * @param isNext
     * @return
     */
    public Object findOneByRowNum(UUID id, Number typeID, Boolean isNext, SearchVoucher searchVoucher) {
        Class cl = null;
        if (typeID.intValue() == TYPE_MC_RECEIPT) {
            cl = MCReceipt.class;
        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
            cl = MCPayment.class;
        } else if (typeID.intValue() == TYPE_SA_ORDER) {
            cl = SAOrder.class;
        } else if (typeID.intValue() == TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC) {
            cl = GOtherVoucher.class;
        } else if (typeID.intValue() == TypeConstant.BAO_CO) {
            cl = MBDeposit.class;
        } else if (typeID.intValue() == TypeConstant.THE_TIN_DUNG) {
            cl = MBCreditCard.class;
        }
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String orderBy = "";
        if (phienSoLamViec == 0) {
            orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
        } else {
            orderBy = " order by Date DESC ,PostedDate DESC ,noMbook DESC";
        }
        StringBuilder sql = new StringBuilder();
        Object obj = new Object();
        Map<String, Object> params = new HashMap<>();
        if (isNext) {
            if (typeID.intValue() == TYPE_SA_ORDER) {
                sql.append("select * from (SELECT ROW_NUMBER() OVER (order by Date DESC ,No DESC ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                params.put("CompanyID", userDTO.getOrganizationUnit().getId());
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
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
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                        params.put("typeID", searchVoucher.getTypeID());
                    }
                    if (searchVoucher.getStatus() != null) {
                        sql.append("AND status = :status ");
                        params.put("status", searchVoucher.getStatus());
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                        params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND EmployeeID = :employeeID ");
                        params.put("employeeID", searchVoucher.getEmployeeID());
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        sql.append("OR No LIKE :searchValue ");
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR ContactName LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        sql.append("OR DeliveryPlace LIKE :searchValue) ");
                        params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                    }
                }
                sql.append(") b " +
                    "where (b.RowIndex = (SELECT RowIndex from (SELECT ROW_NUMBER() OVER (order by Date DESC ,No DESC ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
                        sql.append("AND currencyID = :currencyID ");
                    }
                    if (searchVoucher.getFromDate() != null) {
                        sql.append("AND Date >= :fromDate ");
                    }
                    if (searchVoucher.getToDate() != null) {
                        sql.append("AND Date <= :toDate ");
                    }
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                        params.put("typeID", searchVoucher.getTypeID());
                    }
                    if (searchVoucher.getStatus() != null) {
                        sql.append("AND status = :status ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND EmployeeID = :employeeID ");
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        sql.append("OR No LIKE :searchValue ");
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR ContactName LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        sql.append("OR DeliveryPlace LIKE :searchValue) ");
                    }
                }
                sql.append(") as r " +
                    "where r.ID = :ID) -1)");
            } else {
                sql.append("select * from (SELECT ROW_NUMBER() OVER (" + orderBy + " ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (phienSoLamViec == 0) {
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                params.put("CompanyID", userDTO.getOrganizationUnit().getId());
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
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
                        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                            sql.append("OR NoFBook LIKE :searchValue ");
                        } else {
                            sql.append("OR NoMBook LIKE :searchValue ");
                        }
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        if (typeID.intValue() == TYPE_MC_RECEIPT) {
                            sql.append("OR Payers LIKE :searchValue ");
                        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                            sql.append("OR Receiver LIKE :searchValue ");
                        }
                        sql.append("OR numberAttach LIKE :searchValue) ");
                        params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                    }
                }
                sql.append(") b " +
                    "where (b.RowIndex = (SELECT RowIndex from (SELECT ROW_NUMBER() OVER (" + orderBy + " ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 0) {
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 1) {
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
                        sql.append("AND currencyID = :currencyID ");
                    }
                    if (searchVoucher.getFromDate() != null) {
                        sql.append("AND Date >= :fromDate ");
                    }
                    if (searchVoucher.getToDate() != null) {
                        sql.append("AND Date <= :toDate ");
                    }
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                    }
                    if (searchVoucher.getStatusRecorded() != null) {
                        sql.append("AND Recorded = :recorded ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                            sql.append("OR NoFBook LIKE :searchValue ");
                        } else {
                            sql.append("OR NoMBook LIKE :searchValue ");
                        }
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        if (typeID.intValue() == TYPE_MC_RECEIPT) {
                            sql.append("OR Payers LIKE :searchValue ");
                        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                            sql.append("OR Receiver LIKE :searchValue ");
                        }
                        sql.append("OR numberAttach LIKE :searchValue) ");
                    }
                }
                sql.append(") as r " +
                    "where r.ID = :ID) -1)");
            }

        } else {
            if (typeID.intValue() == TYPE_SA_ORDER) {
                sql.append("select * from (SELECT ROW_NUMBER() OVER (order by Date DESC ,No DESC ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                params.put("CompanyID", userDTO.getOrganizationUnit().getId());
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
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
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                        params.put("typeID", searchVoucher.getTypeID());
                    }
                    if (searchVoucher.getStatus() != null) {
                        sql.append("AND status = :status ");
                        params.put("status", searchVoucher.getStatus());
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                        params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND EmployeeID = :employeeID ");
                        params.put("employeeID", searchVoucher.getEmployeeID());
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        sql.append("OR No LIKE :searchValue ");
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR CompanyTaxCode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR ContactName LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        sql.append("OR DeliveryPlace LIKE :searchValue) ");
                        params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                    }
                }
                sql.append(") b " +
                    "where (b.RowIndex = (SELECT RowIndex from (SELECT ROW_NUMBER() OVER (order by Date DESC ,No DESC ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
                        sql.append("AND currencyID = :currencyID ");
                    }
                    if (searchVoucher.getFromDate() != null) {
                        sql.append("AND Date >= :fromDate ");
                    }
                    if (searchVoucher.getToDate() != null) {
                        sql.append("AND Date <= :toDate ");
                    }
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                        params.put("typeID", searchVoucher.getTypeID());
                    }
                    if (searchVoucher.getStatus() != null) {
                        sql.append("AND status = :status ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND EmployeeID = :employeeID ");
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        sql.append("OR No LIKE :searchValue ");
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR CompanyTaxCode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR ContactName LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        sql.append("OR DeliveryPlace LIKE :searchValue) ");
                    }
                }
                sql.append(") as r " +
                    "where r.ID = :ID) +1)");
            } else {
                sql.append("select * from (SELECT ROW_NUMBER() OVER (" + orderBy + " ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 0) {
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 1) {
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                params.put("CompanyID", userDTO.getOrganizationUnit().getId());
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
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
                        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                            sql.append("OR NoFBook LIKE :searchValue ");
                        } else {
                            sql.append("OR NoMBook LIKE :searchValue ");
                        }
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        if (typeID.intValue() == TYPE_MC_RECEIPT) {
                            sql.append("OR Payers LIKE :searchValue ");
                        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                            sql.append("OR Receiver LIKE :searchValue ");
                        }
                        sql.append("OR numberAttach LIKE :searchValue) ");
                        params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                    }
                }
                sql.append(") b " +
                    "where (b.RowIndex = (SELECT RowIndex from (SELECT ROW_NUMBER() OVER (" + orderBy + " ) as RowIndex, * from ");
                sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
                if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 0) {
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData()) == 1) {
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (searchVoucher != null) {
                    if (searchVoucher.getCurrencyID() != null) {
                        sql.append("AND currencyID = :currencyID ");
                    }
                    if (searchVoucher.getFromDate() != null) {
                        sql.append("AND Date >= :fromDate ");
                    }
                    if (searchVoucher.getToDate() != null) {
                        sql.append("AND Date <= :toDate ");
                    }
                    if (searchVoucher.getTypeID() != null) {
                        sql.append("AND TypeID = :typeID ");
                    }
                    if (searchVoucher.getStatusRecorded() != null) {
                        sql.append("AND Recorded = :recorded ");
                    }
                    if (searchVoucher.getAccountingObjectID() != null) {
                        sql.append("AND accountingObjectID = :accountingObjectID ");
                    }
                    if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                        sql.append("AND (accountingObjectName LIKE :searchValue ");
                        if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                            sql.append("OR NoFBook LIKE :searchValue ");
                        } else {
                            sql.append("OR NoMBook LIKE :searchValue ");
                        }
                        sql.append("OR accountingObjectAddress LIKE :searchValue ");
                        sql.append("OR taxcode LIKE :searchValue ");
                        sql.append("OR reason LIKE :searchValue ");
                        sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                        sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                        if (typeID.intValue() == TYPE_MC_RECEIPT) {
                            sql.append("OR Payers LIKE :searchValue ");
                        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                            sql.append("OR Receiver LIKE :searchValue ");
                        }
                        sql.append("OR numberAttach LIKE :searchValue) ");
                    }
                }
                sql.append(") as r " +
                    "where r.ID = :ID) +1)");
            }
        }
//        params.put("class", cl.getSimpleName());
        params.put("ID", id);
        /*if(isNext){
            params.put("isNext", "- 1");
        }else{
            params.put("isNext", "+ 1");
        }*/
        Query query = entityManager.createNativeQuery(sql.toString(), cl);
        setParams(query, params);
        Object obj_ = query.getSingleResult();
        obj = cl.cast(obj_);
        return obj;
    }

    /**
     * Hautv
     *
     * @param id
     * @param typeID
     * @return
     */
    @Override
    public List getIndexRow(UUID id, Number typeID, SearchVoucher searchVoucher) {
        Class cl = null;
        if (typeID.intValue() == TYPE_MC_RECEIPT) {
            cl = MCReceipt.class;
        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
            cl = MCPayment.class;
        } else if (typeID.intValue() == TYPE_SA_ORDER) {
            cl = SAOrder.class;
        } else if (typeID.intValue() == TypeConstant.BAO_CO) {
            cl = MBDeposit.class;
        } else if (typeID.intValue() == TypeConstant.THE_TIN_DUNG) {
            cl = MBCreditCard.class;
        } else if (typeID.intValue() == TypeConstant.CHUNG_TU_NGHIEP_VU_KHAC) {
            cl = GOtherVoucher.class;
        }
        StringBuilder sql = new StringBuilder();
        Object obj = new Object();
        Map<String, Object> params = new HashMap<>();

        /*sql.append("SELECT RowIndex " +
            "FROM (SELECT ROW_NUMBER() OVER (order by Date DESC ,PostedDate DESC ,NoFBook DESC ) as RowIndex, * from " + cl.getSimpleName() + ") a " +
            "where a.ID = :ID " +
            "UNION ALL select COUNT(*) as rc from " + cl.getSimpleName());*/
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String orderBy = "";
        if (phienSoLamViec == 0) {
            orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
        } else {
            orderBy = " order by Date DESC ,PostedDate DESC ,noMbook DESC";
        }
        if (typeID.intValue() == TYPE_SA_ORDER) {
            sql.append("SELECT RowIndex " +
                "FROM (SELECT ROW_NUMBER() OVER (order by Date DESC ,No DESC ) as RowIndex, * from ");
            sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
            params.put("CompanyID", userDTO.getOrganizationUnit().getId());
            if (searchVoucher != null) {
                if (searchVoucher.getCurrencyID() != null) {
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
                if (searchVoucher.getTypeID() != null) {
                    sql.append("AND TypeID = :typeID ");
                    params.put("typeID", searchVoucher.getTypeID());
                }
                if (searchVoucher.getStatus() != null) {
                    sql.append("AND status = :status ");
                    params.put("status", searchVoucher.getStatus());
                }
                if (searchVoucher.getAccountingObjectID() != null) {
                    sql.append("AND accountingObjectID = :accountingObjectID ");
                    params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
                }
                if (searchVoucher.getAccountingObjectID() != null) {
                    sql.append("AND EmployeeID = :employeeID ");
                    params.put("employeeID", searchVoucher.getEmployeeID());
                }
                if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                    sql.append("AND (accountingObjectName LIKE :searchValue ");
                    sql.append("OR No LIKE :searchValue ");
                    sql.append("OR accountingObjectAddress LIKE :searchValue ");
                    sql.append("OR CompanyTaxCode LIKE :searchValue ");
                    sql.append("OR reason LIKE :searchValue ");
                    sql.append("OR ContactName LIKE :searchValue ");
                    sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                    sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                    sql.append("OR DeliveryPlace LIKE :searchValue) ");
                    params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                }
            }
            sql.append(") a " +
                "where a.ID = :ID " +
                "UNION ALL select COUNT(*) as rc from ");
            sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
            if (searchVoucher != null) {
                if (searchVoucher.getCurrencyID() != null) {
                    sql.append("AND currencyID = :currencyID ");
                }
                if (searchVoucher.getFromDate() != null) {
                    sql.append("AND Date >= :fromDate ");
                }
                if (searchVoucher.getToDate() != null) {
                    sql.append("AND Date <= :toDate ");
                }
                if (searchVoucher.getTypeID() != null) {
                    sql.append("AND TypeID = :typeID ");
                }
                if (searchVoucher.getStatus() != null) {
                    sql.append("AND status = :status ");
                }
                if (searchVoucher.getAccountingObjectID() != null) {
                    sql.append("AND accountingObjectID = :accountingObjectID ");
                }
                if (searchVoucher.getAccountingObjectID() != null) {
                    sql.append("AND EmployeeID = :employeeID ");
                }
                if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                    sql.append("AND (accountingObjectName LIKE :searchValue ");
                    sql.append("OR No LIKE :searchValue ");
                    sql.append("OR accountingObjectAddress LIKE :searchValue ");
                    sql.append("OR CompanyTaxCode LIKE :searchValue ");
                    sql.append("OR reason LIKE :searchValue ");
                    sql.append("OR ContactName LIKE :searchValue ");
                    sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                    sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                    sql.append("OR DeliveryPlace LIKE :searchValue) ");
                }
            }
            params.put("ID", id);
        } else {
            sql.append("SELECT RowIndex " +
                "FROM (SELECT ROW_NUMBER() OVER (" + orderBy + " ) as RowIndex, * from ");
            sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
            if (phienSoLamViec == 0) {
                sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
            } else if (phienSoLamViec == 1) {
                sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
            }
            params.put("CompanyID", userDTO.getOrganizationUnit().getId());
            if (searchVoucher != null) {
                if (searchVoucher.getCurrencyID() != null) {
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
                    if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                        sql.append("OR NoFBook LIKE :searchValue ");
                    } else {
                        sql.append("OR NoMBook LIKE :searchValue ");
                    }
                    sql.append("OR accountingObjectAddress LIKE :searchValue ");
                    sql.append("OR taxcode LIKE :searchValue ");
                    sql.append("OR reason LIKE :searchValue ");
                    sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                    sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                    if (typeID.intValue() == TYPE_MC_RECEIPT) {
                        sql.append("OR Payers LIKE :searchValue ");
                    } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                        sql.append("OR Receiver LIKE :searchValue ");
                    }
                    sql.append("OR numberAttach LIKE :searchValue) ");
                    params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
                }
            }
            sql.append(") a " +
                "where a.ID = :ID " +
                "UNION ALL select COUNT(*) as rc from ");
            sql.append(cl.getSimpleName() + " where CompanyID = :CompanyID ");
            if (phienSoLamViec == 0) {
                sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
            } else if (phienSoLamViec == 1) {
                sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
            }
            if (searchVoucher != null) {
                if (searchVoucher.getCurrencyID() != null) {
                    sql.append("AND currencyID = :currencyID ");
                }
                if (searchVoucher.getFromDate() != null) {
                    sql.append("AND Date >= :fromDate ");
                }
                if (searchVoucher.getToDate() != null) {
                    sql.append("AND Date <= :toDate ");
                }
                if (searchVoucher.getTypeID() != null) {
                    sql.append("AND TypeID = :typeID ");
                }
                if (searchVoucher.getStatusRecorded() != null) {
                    sql.append("AND Recorded = :recorded ");
                }
                if (searchVoucher.getAccountingObjectID() != null) {
                    sql.append("AND accountingObjectID = :accountingObjectID ");
                }
                if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                    sql.append("AND (accountingObjectName LIKE :searchValue ");
                    if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData() == "0") {
                        sql.append("OR NoFBook LIKE :searchValue ");
                    } else {
                        sql.append("OR NoMBook LIKE :searchValue ");
                    }
                    sql.append("OR accountingObjectAddress LIKE :searchValue ");
                    sql.append("OR taxcode LIKE :searchValue ");
                    sql.append("OR reason LIKE :searchValue ");
                    sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                    sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                    if (typeID.intValue() == TYPE_MC_RECEIPT) {
                        sql.append("OR Payers LIKE :searchValue ");
                    } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                        sql.append("OR Receiver LIKE :searchValue ");
                    }
                    sql.append("OR numberAttach LIKE :searchValue) ");
                }
            }
            params.put("ID", id);
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        List obj_ = query.getResultList();
        return obj_;
    }

    /**
     * Hautv
     *
     * @param pageable
     * @param searchVoucher
     * @param typeID
     * @return
     */
    @Override
    public Page<Object> findAll(Pageable pageable, SearchVoucher searchVoucher, Number typeID) {
        Class cl = null;
        if (typeID.intValue() == TYPE_MC_RECEIPT) {
            cl = MCReceipt.class;
        } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
            cl = MCPayment.class;
        } else if (typeID.intValue() == TYPE_MC_AUDIT) {
            cl = MCAudit.class;
        } else if (typeID.intValue() == TYPE_SA_ORDER) {
            cl = SAOrder.class;
        }
        UserDTO userDTO = userService.getAccount();
        StringBuilder sql = new StringBuilder();
        List<Object> lstObject = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        String orderBy = "";
        if (phienSoLamViec == 0) {
            orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
        } else {
            orderBy = " order by Date DESC ,PostedDate DESC ,noMbook DESC";
        }
        if (typeID.intValue() == TYPE_SA_ORDER) {
            sql.append("FROM " + cl.getSimpleName() + " WHERE CompanyID = :CompanyID ");
            params.put("CompanyID", userDTO.getOrganizationUnit().getId());
            if (searchVoucher.getCurrencyID() != null) {
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
            if (searchVoucher.getTypeID() != null) {
                sql.append("AND TypeID = :typeID ");
                params.put("typeID", searchVoucher.getTypeID());
            }
            if (searchVoucher.getStatus() != null) {
                sql.append("AND status = :status ");
                params.put("status", searchVoucher.getStatus());
            }
            if (searchVoucher.getAccountingObjectID() != null) {
                sql.append("AND accountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
            }
            if (searchVoucher.getEmployeeID() != null) {
                sql.append("AND EmployeeID = :employeeID ");
                params.put("employeeID", searchVoucher.getEmployeeID());
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND (accountingObjectName LIKE :searchValue ");
                sql.append("OR No LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR CompanyTaxCode LIKE :searchValue ");
                sql.append("OR reason LIKE :searchValue ");
                sql.append("OR ContactName LIKE :searchValue ");
                sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                sql.append("OR DeliveryPlace LIKE :searchValue) ");
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        } else {
            String joinType = " a left join Type b on a.typeID = b.ID ";
            if (pageable == null) {
                sql.append("FROM " + cl.getSimpleName() + joinType + " WHERE CompanyID = :CompanyID ");
            } else {
                sql.append("FROM " + cl.getSimpleName() + " WHERE CompanyID = :CompanyID ");
            }
            if (phienSoLamViec == 0) {
                sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
            } else if (phienSoLamViec == 1) {
                sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
            }
            params.put("CompanyID", userDTO.getOrganizationUnit().getId());
            if (searchVoucher.getCurrencyID() != null) {
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
                if (typeID.intValue() == TYPE_MC_AUDIT) {
                    sql.append("AND (No LIKE :searchValue ");
                    sql.append("OR Description LIKE :searchValue ");
                    sql.append("OR Summary LIKE :searchValue )");
                } else if (typeID.intValue() == TYPE_MC_PAYMENT || typeID.intValue() == TYPE_MC_RECEIPT) {
                    sql.append("AND (accountingObjectName LIKE :searchValue ");
                    if (phienSoLamViec == 0) {
                        sql.append("OR NoFBook LIKE :searchValue ");
                    } else {
                        sql.append("OR NoMBook LIKE :searchValue ");
                    }
                    sql.append("OR accountingObjectAddress LIKE :searchValue ");
                    sql.append("OR taxcode LIKE :searchValue ");
                    sql.append("OR reason LIKE :searchValue ");
                    sql.append("OR EmployeeID IN (select ID from AccountingObject where AccountingObjectName LIKE :searchValue ");
                    sql.append("or AccountingObjectCode LIKE :searchValue ) ");
                    if (typeID.intValue() == TYPE_MC_RECEIPT) {
                        sql.append("OR Payers LIKE :searchValue ");
                    } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                        sql.append("OR Receiver LIKE :searchValue ");
                    }
                    sql.append("OR numberAttach LIKE :searchValue) ");
                }
                params.put("searchValue", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        String sqlSum = sql.toString();
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (typeID.intValue() == TYPE_MC_AUDIT) {
                sql.append(" order by Date DESC, AuditDate DESC, No DESC "); // sắp xếp theo ngày chứng từ, ngày ghi sổ, số chứng từ
            } else if (typeID.intValue() == TYPE_SA_ORDER) {
                sql.append(" order by Date DESC, No DESC ");
            } else {
                sql.append(orderBy);
            }
            Query query;
            if (pageable != null) {
                query = entityManager.createNativeQuery("SELECT *, null as TypeName " + sql.toString(), cl.getSimpleName() + "DTO");
                setParamsWithPageable(query, params, pageable, total);
                lstObject = query.getResultList();
                if (typeID.intValue() == TYPE_MC_RECEIPT) {
                    Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sqlSum);
                    setParams(querySum, params);
                    Number totalamount = (Number) querySum.getSingleResult();
                    ((MCReceiptDTO) lstObject.get(0)).setTotal(totalamount);
                } else if (typeID.intValue() == TYPE_MC_PAYMENT) {
                    Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sqlSum);
                    setParams(querySum, params);
                    Number totalamount = (Number) querySum.getSingleResult();
                    ((MCPaymentDTO) lstObject.get(0)).setTotal(totalamount);
                } else if (typeID.intValue() == TYPE_SA_ORDER) {
                    Query querySum = entityManager.createNativeQuery("select sum(totalAmount) " + sqlSum);
                    setParams(querySum, params);
                    Number totalamount = (Number) querySum.getSingleResult();
                    ((SAOrderDTO) lstObject.get(0)).setTotal(totalamount);
                }
            } else {
                if (typeID.intValue() == TYPE_SA_ORDER) {
                    query = entityManager.createNativeQuery("SELECT * " + sql.toString(), cl.getSimpleName() + "DTO");
                } else {
                    query = entityManager.createNativeQuery("SELECT a.*, b.TypeName " + sql.toString(), cl.getSimpleName() + "DTO");
                }
                setParams(query, params);
                lstObject = query.getResultList();
            }
        }
        if (pageable == null) {
            return new PageImpl<>(lstObject);
        }
        return new PageImpl<>(lstObject, pageable, total.longValue());
    }

    /**
     * Hautv
     *
     * @param
     * @param typeGroupID
     * @param displayOnBook
     * @return
     */
    @Override
    public boolean updateGencode(String noFBook, String noMBook, int typeGroupID, int displayOnBook) {
        try {
            // edit by anmt
            if (displayOnBook == 0) {
                Character[] arr = new Character[noFBook.length()];
                String strVal = "";
                for (int i = 0; i < noFBook.length(); i++) {
                    arr[i] = Character.valueOf(noFBook.charAt(i));
                    if (Character.isDigit(arr[i])) {
                        strVal = noFBook.substring(i);
                        break;
                    }
                }
                String strCurrentValue = strVal.replaceAll("(\\d+).*", "$1");
                String preFix = noFBook.substring(0, noFBook.indexOf(strCurrentValue));
                String subFix = noFBook.substring(noFBook.indexOf(strCurrentValue) + strCurrentValue.length());
                int noV = Integer.valueOf(strCurrentValue);
                GenCode genCode = genCodeService.findWithTypeID(typeGroupID, displayOnBook);
                if (genCode.getSuffix() == null) {
                    genCode.suffix("");
                }
                if (!preFix.equals(genCode.getPrefix()) || !subFix.equals(genCode.getSuffix())) {
                    genCode.prefix(preFix);
                    genCode.suffix(subFix);
                    genCode.length(strCurrentValue.length());
                    genCode.currentValue(noV);
                    genCodeService.save(genCode);
                }

                if (noV > genCode.getCurrentValue()) {
                    genCode.currentValue(noV);
                    if (strCurrentValue.length() != genCode.getLength()) {
                        genCode.length(strCurrentValue.length());
                    }
                    genCodeService.save(genCode);
                }
            } else if (displayOnBook == 1) {
                Character[] arr = new Character[noMBook.length()];
                String strVal = "";
                for (int i = 0; i < noMBook.length(); i++) {
                    arr[i] = Character.valueOf(noMBook.charAt(i));
                    if (Character.isDigit(arr[i])) {
                        strVal = noMBook.substring(i);
                        break;
                    }
                }
                String strCurrentValue = strVal.replaceAll("(\\d+).*", "$1");
                String preFix = noMBook.substring(0, noMBook.indexOf(strCurrentValue));
                String subFix = noMBook.substring(noMBook.indexOf(strCurrentValue) + strCurrentValue.length());
                int noV = Integer.valueOf(strCurrentValue);
                GenCode genCode = genCodeService.findWithTypeID(typeGroupID, displayOnBook);
                if (genCode.getSuffix() == null) {
                    genCode.suffix("");
                }
                if (!preFix.equals(genCode.getPrefix()) || !subFix.equals(genCode.getSuffix())) {
                    genCode.prefix(preFix);
                    genCode.suffix(subFix);
                    genCode.length(strCurrentValue.length());
                    genCode.currentValue(noV);
                    genCodeService.save(genCode);
                }

                if (noV > genCode.getCurrentValue()) {
                    genCode.currentValue(noV);
                    if (strCurrentValue.length() != genCode.getLength()) {
                        genCode.length(strCurrentValue.length());
                    }
                    genCodeService.save(genCode);
                }
            } else {
                GenCode genCode = genCodeService.findWithTypeID(typeGroupID, displayOnBook);
                if (genCode != null) {
                    Character[] arr = new Character[noFBook.length()];
                    String strVal = "";
                    for (int i = 0; i < noFBook.length(); i++) {
                        arr[i] = Character.valueOf(noFBook.charAt(i));
                        if (Character.isDigit(arr[i])) {
                            strVal = noFBook.substring(i);
                            break;
                        }
                    }
                    String strCurrentValue = strVal.replaceAll("(\\d+).*", "$1");
                    String preFix = noFBook.substring(0, noFBook.indexOf(strCurrentValue));
                    String subFix = noFBook.substring(noFBook.indexOf(strCurrentValue) + strCurrentValue.length());
                    int noV = Integer.valueOf(strCurrentValue);
                    if (genCode.getSuffix() == null) {
                        genCode.suffix("");
                    }
                    if (!preFix.equals(genCode.getPrefix()) || !subFix.equals(genCode.getSuffix())) {
                        genCode.prefix(preFix);
                        genCode.suffix(subFix);
                        genCode.length(strCurrentValue.length());
                        genCode.currentValue(noV);
                        genCodeService.save(genCode);
                    }

                    if (noV > genCode.getCurrentValue()) {
                        genCode.currentValue(noV);
                        if (strCurrentValue.length() != genCode.getLength()) {
                            genCode.length(strCurrentValue.length());
                        }
                        genCodeService.save(genCode);
                    }
                } else {
                    Character[] arr0 = new Character[noFBook.length()];
                    String strVal0 = "";
                    for (int i = 0; i < noFBook.length(); i++) {
                        arr0[i] = Character.valueOf(noFBook.charAt(i));
                        if (Character.isDigit(arr0[i])) {
                            strVal0 = noFBook.substring(i);
                            break;
                        }
                    }
                    String strCurrentValue0 = strVal0.replaceAll("(\\d+).*", "$1");
                    String preFix0 = noFBook.substring(0, noFBook.indexOf(strCurrentValue0));
                    String subFix0 = noFBook.substring(noFBook.indexOf(strCurrentValue0) + strCurrentValue0.length());
                    int noV0 = Integer.valueOf(strCurrentValue0);
                    GenCode genCode0 = genCodeService.findWithTypeID(typeGroupID, 0);
                    if (genCode0.getSuffix() == null) {
                        genCode0.suffix("");
                    }
                    if (!preFix0.equals(genCode0.getPrefix()) || !subFix0.equals(genCode0.getSuffix())) {
                        genCode0.prefix(preFix0);
                        genCode0.suffix(subFix0);
                        genCode0.length(strCurrentValue0.length());
                        genCode0.currentValue(noV0);
                        genCodeService.save(genCode0);
                    }

                    if (noV0 > genCode0.getCurrentValue()) {
                        genCode0.currentValue(noV0);
                        if (strCurrentValue0.length() != genCode0.getLength()) {
                            genCode0.length(strCurrentValue0.length());
                        }
                        genCodeService.save(genCode0);
                    }

                    Character[] arr1 = new Character[noMBook.length()];
                    String strVal1 = "";
                    for (int i = 0; i < noMBook.length(); i++) {
                        arr1[i] = Character.valueOf(noMBook.charAt(i));
                        if (Character.isDigit(arr1[i])) {
                            strVal1 = noMBook.substring(i);
                            break;
                        }
                    }
                    String strCurrentValue1 = strVal1.replaceAll("(\\d+).*", "$1");
                    String preFix1 = noMBook.substring(0, noMBook.indexOf(strCurrentValue1));
                    String subFix1 = noMBook.substring(noMBook.indexOf(strCurrentValue1) + strCurrentValue1.length());
                    int noV1 = Integer.valueOf(strCurrentValue1);
                    GenCode genCode1 = genCodeService.findWithTypeID(typeGroupID, 1);
                    if (genCode1.getSuffix() == null) {
                        genCode1.suffix("");
                    }
                    if (!preFix1.equals(genCode1.getPrefix()) || !subFix1.equals(genCode1.getSuffix())) {
                        genCode1.prefix(preFix1);
                        genCode1.suffix(subFix1);
                        genCode1.length(strCurrentValue1.length());
                        genCode1.currentValue(noV1);
                        genCodeService.save(genCode1);
                    }

                    if (noV1 > genCode1.getCurrentValue()) {
                        genCode1.currentValue(noV1);
                        if (strCurrentValue1.length() != genCode1.getLength()) {
                            genCode1.length(strCurrentValue1.length());
                        }
                        genCodeService.save(genCode1);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Hautv
     *
     * @param noFBook,      noMBook
     * @param displayOnBook
     * @return
     */
    public boolean checkDuplicateNoVoucher(String noFBook, String noMBook, int displayOnBook, UUID refID) {
        StringBuilder sql = new StringBuilder();
        List<Object> lstObject = new ArrayList<>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT Count(1) FROM ViewVoucherNo WHERE CompanyID = :CompanyID ");
        params.put("CompanyID", currentUserLoginAndOrg.get().getOrg());
        if (displayOnBook != 2) {
            sql.append("and (TypeLedger = :displayOnBook ");
            sql.append("or TypeLedger = 2) ");
            params.put("displayOnBook", displayOnBook);
            if (displayOnBook == 0) {
                sql.append("and NoFBook = :NoFBook ");
                params.put("NoFBook", noFBook);
            } else {
                sql.append("and NoMBook = :NoMBook ");
                params.put("NoMBook", noMBook);
            }
        } else {
            sql.append("and ( NoFBook = :noFBook ");
            params.put("noFBook", noFBook);
            sql.append("or NoMBook = :noMBook ) ");
            params.put("noMBook", noMBook);
        }
        if (refID != null) {
            sql.append("and RefID <> :RefID ");
            params.put("RefID", refID);
        }
        Query countQuerry = entityManager.createNativeQuery(sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() == 0) return true;
        else return false;
    }

    @Override
    public byte[] exportPdf(SearchVoucher searchVoucher, Number typeID) {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<Object> lstObject = findAll(null, searchVoucher, typeID).getContent();
        switch (typeID.intValue()) {
            case Constants.MCReceipt.TYPE_PHIEU_THU:
                if (phienSoLamViec.equals(0)) {
                    return PdfUtils.writeToFile(lstObject, ExcelConstant.MCReceipt.HEADER, ExcelConstant.MCReceipt.FIELD_NoFBook, userDTO);
                } else {
                    return PdfUtils.writeToFile(lstObject, ExcelConstant.MCReceipt.HEADER, ExcelConstant.MCReceipt.FIELD_NoMBook, userDTO);
                }
            case Constants.MCPayment.TYPE_PHIEU_CHI:
                if (phienSoLamViec.equals(0)) {
                    return PdfUtils.writeToFile(lstObject, ExcelConstant.MCPayment.HEADER, ExcelConstant.MCPayment.FIELD_NoFBook, userDTO);
                } else {
                    return PdfUtils.writeToFile(lstObject, ExcelConstant.MCPayment.HEADER, ExcelConstant.MCPayment.FIELD_NoMBook, userDTO);
                }
            case Constants.SAOrder.TYPE_DON_DAT_HANG:
                List<SAOrderDTO> saOrders = (List<SAOrderDTO>) (Object) lstObject;
                for (SAOrderDTO saOrderDTO : saOrders
                ) {
                    if (saOrderDTO.getStatus() == 0) {
                        saOrderDTO.setStatusString("Chưa thực hiện");
                    } else if (saOrderDTO.getStatus() == 1) {
                        saOrderDTO.setStatusString("Đang thực hiện");
                    } else if (saOrderDTO.getStatus() == 2) {
                        saOrderDTO.setStatusString("Đã hoàn thành");
                    } else if (saOrderDTO.getStatus() == 3) {
                        saOrderDTO.setStatusString("Đã hủy bỏ");
                    }
                }
                return PdfUtils.writeToFile(saOrders, ExcelConstant.SaOrder.HEADER, ExcelConstant.SaOrder.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher, Number typeID) {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        List<Object> lstObject = findAll(null, searchVoucher, typeID).getContent();
        switch (typeID.intValue()) {
            case Constants.MCReceipt.TYPE_PHIEU_THU:
                if (phienSoLamViec.equals(0)) {
                    return ExcelUtils.writeToFile(lstObject, ExcelConstant.MCReceipt.NAME, ExcelConstant.MCReceipt.HEADER, ExcelConstant.MCReceipt.FIELD_NoFBook, userDTO);
                } else {
                    return ExcelUtils.writeToFile(lstObject, ExcelConstant.MCReceipt.NAME, ExcelConstant.MCReceipt.HEADER, ExcelConstant.MCReceipt.FIELD_NoMBook, userDTO);
                }
            case Constants.MCPayment.TYPE_PHIEU_CHI:
                if (phienSoLamViec.equals(0)) {
                    return ExcelUtils.writeToFile(lstObject, ExcelConstant.MCPayment.NAME, ExcelConstant.MCPayment.HEADER, ExcelConstant.MCPayment.FIELD_NoFBook, userDTO);
                } else {
                    return ExcelUtils.writeToFile(lstObject, ExcelConstant.MCPayment.NAME, ExcelConstant.MCPayment.HEADER, ExcelConstant.MCPayment.FIELD_NoMBook, userDTO);
                }
            case Constants.SAOrder.TYPE_DON_DAT_HANG:
                List<SAOrderDTO> saOrders = (List<SAOrderDTO>) (Object) lstObject;
                for (SAOrderDTO saOrder : saOrders
                ) {
                    if (saOrder.getStatus() == 0) {
                        saOrder.setStatusString("Chưa thực hiện");
                    } else if (saOrder.getStatus() == 1) {
                        saOrder.setStatusString("Đang thực hiện");
                    } else if (saOrder.getStatus() == 2) {
                        saOrder.setStatusString("Đã hoàn thành");
                    } else if (saOrder.getStatus() == 3) {
                        saOrder.setStatusString("Đã hủy bỏ");
                    }
                }
                return ExcelUtils.writeToFile(saOrders, ExcelConstant.SaOrder.NAME, ExcelConstant.SaOrder.HEADER, ExcelConstant.SaOrder.FIELD, userDTO);
        }
        return null;
    }

    /**
     * @param companyID
     * @param uuid
     * @param nameColumn
     * @return Kiểm tra ràng buộc danh mục
     * true: đã được sử dụng
     * false: chưa được sử dụng
     * @Author Hautv
     */
    @Override
    public Boolean checkCatalogInUsed(UUID companyID, Object uuid, String nameColumn) {
        Boolean result = false;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        /*sql.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and "); // Hautv edit
        params.put("companyID", companyID);*/
        sql.append("SELECT Count(*) from ViewCheckConstraint where "); // Hautv bỏ check theo compayID chỉ check theo ID của danh mục
        sql.append(nameColumn);
        sql.append(" = :id");
        params.put("id", uuid);
        Query countQuerry = entityManager.createNativeQuery(sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public Boolean checkContraint(String nameTable, String nameColumn, UUID uuid) {
        Boolean result = false;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        /*sql.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and "); // Hautv edit
        params.put("companyID", companyID);*/
        sql.append("SELECT Count(*) from "); // Hautv bỏ check theo compayID chỉ check theo ID của danh mục
        sql.append(nameTable);
        sql.append(" where ");
        sql.append(nameColumn);
        sql.append(" = :id");
        params.put("id", uuid);
        Query countQuerry = entityManager.createNativeQuery(sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public Boolean checkCostSetIDUsed(UUID companyID, List<UUID> uuids, String nameColumn) {
        Boolean result = false;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        /*sql.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and "); // Hautv edit
        params.put("companyID", companyID);*/
        sql.append("SELECT Count(*) from ViewCheckConstraint where "); // Hautv bỏ check theo compayID chỉ check theo ID của danh mục
        sql.append(nameColumn);
        sql.append(" in :id");
        params.put("id", uuids);
        Query countQuerry = entityManager.createNativeQuery(sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            result = true;
        }
        return result;
    }


    /**
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumn
     * @return
     * @Author Hautv
     */
    @Override
    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, String nameColumn) {
        List<VoucherRefCatalogDTO> result = new ArrayList<>();
        StringBuilder sqlCount = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String order = " order by date desc , ";
        sqlCount.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and ");
        params.put("companyID", companyID);
        if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            order += "NoFBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 0) and ");
        } else {
            order += "NoMBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 1) and ");
        }
        sqlCount.append(nameColumn);
        sqlCount.append(" = :id");
        params.put("id", uuid);
        Query countQuerry = entityManager.createNativeQuery(sqlCount.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            sql.append("select DISTINCT a.ReferenceID as id, " +
                "       a.TypeID      as typeID, " +
                "       a.TypeGroupID      as typeGroupID, " +
                "       b.TypeName, " +
                "       date, " +
                "       a.NoFBook, " +
                "       a.NoMBook, " +
                "       a.Reason, " +
                "       a.TotalAmount, " +
                "       a.TotalAmountOriginal " +
                "from ViewCheckConstraint a  " +
                "         left join Type b on a.TypeID = b.ID  " +
                "where a.CompanyID = :companyID and ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                sql.append("(TypeLedger = 2 or TypeLedger = 0) and ");
            } else {
                sql.append("(TypeLedger = 2 or TypeLedger = 1) and ");
            }
            sql.append(nameColumn);
            sql.append(" = :id");
            sql.append(order);
            Query query = entityManager.createNativeQuery(sql.toString(), "VoucherRefCatalogDTO");
            setParams(query, params);
            result = query.getResultList();
        }
        return result;
    }

    /**
     * @param pageable
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumn
     * @return
     * @Author Hautv
     */
    @Override
    public Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, String nameColumn) {
        List<VoucherRefCatalogDTO> result = new ArrayList<>();
        StringBuilder sqlCount = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String order = " order by date desc , ";
        sqlCount.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and ");
        params.put("companyID", companyID);
        if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            order += "NoFBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 0) and ");
        } else {
            order += "NoMBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 1) and ");
        }
        sqlCount.append(nameColumn);
        sqlCount.append(" = :id");
        params.put("id", uuid);
        Query countQuerry = entityManager.createNativeQuery(sqlCount.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            sql.append("select DISTINCT a.ReferenceID as id, " +
                "       a.TypeID      as typeID, " +
                "       a.TypeGroupID      as typeGroupID, " +
                "       b.TypeName, " +
                "       date, " +
                "       a.NoFBook, " +
                "       a.NoMBook, " +
                "       a.Reason, " +
                "       a.TotalAmount, " +
                "       a.TotalAmountOriginal " +
                "from ViewCheckConstraint a  " +
                "         left join Type b on a.TypeID = b.ID  " +
                "where a.CompanyID = :companyID and ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                sql.append("(TypeLedger = 2 or TypeLedger = 0) and ");
            } else {
                sql.append("(TypeLedger = 2 or TypeLedger = 1) and ");
            }
            sql.append(nameColumn);
            sql.append(" = :id");
            sql.append(order);
            Query query = entityManager.createNativeQuery(sql.toString(), "VoucherRefCatalogDTO");
            setParamsWithPageable(query, params, pageable, total);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    /**
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumns
     * @return
     * @Author Hautv
     */
    @Override
    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns) {
        List<VoucherRefCatalogDTO> result = new ArrayList<>();
        StringBuilder sqlCount = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlCount.append("SELECT Count(1) from ViewCheckConstraint where CompanyID = :companyID and ");
        params.put("companyID", companyID);
        String order = " order by date desc , ";
        if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            order += "NoFBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 0) and (");
        } else {
            order += "NoMBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 1) and (");
        }
        int count = 0;
        for (String nameColumn : nameColumns) {
            sqlCount.append(nameColumn);
            sqlCount.append(" = :id ");
            params.put("id", uuid);
            if (count < nameColumns.size() - 1) {
                sqlCount.append(" or ");
            }
            count++;
        }
        sqlCount.append(")");
        Query countQuerry = entityManager.createNativeQuery(sqlCount.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            sql.append("select DISTINCT a.ReferenceID as id, " +
                "       a.TypeID      as typeID, " +
                "       a.TypeGroupID      as typeGroupID, " +
                "       b.TypeName, " +
                "       date, " +
                "       a.NoFBook, " +
                "       a.NoMBook, " +
                "       a.Reason, " +
                "       a.TotalAmount, " +
                "       a.TotalAmountOriginal " +
                "from ViewCheckConstraint a  " +
                "         left join Type b on a.TypeID = b.ID  " +
                "where a.CompanyID = :companyID and ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                sql.append("(TypeLedger = 2 or TypeLedger = 0) and (");
            } else {
                sql.append("(TypeLedger = 2 or TypeLedger = 1) and (");
            }
            count = 0;
            for (String nameColumn : nameColumns) {
                sql.append(nameColumn);
                sql.append(" = :id ");
                if (count < nameColumns.size() - 1) {
                    sql.append(" or ");
                }
                count++;
            }
            sql.append(")");
            sql.append(order);
            Query query = entityManager.createNativeQuery(sql.toString(), "VoucherRefCatalogDTO");
            setParams(query, params);
            result = query.getResultList();
        }
        return result;
    }


    /**
     * @param pageable
     * @param companyID
     * @param currentBook
     * @param uuid
     * @param nameColumns
     * @return
     * @Author Hautv
     */
    @Override
    public Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns) {
        List<VoucherRefCatalogDTO> result = new ArrayList<>();
        StringBuilder sqlCount = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlCount.append("Select COUNT(*) from (select DISTINCT a.ReferenceID as id, " +
            "       a.TypeID      as typeID, " +
            "       a.TypeGroupID      as TypeGroupID, " +
            "       b.TypeName, " +
            "       date, " +
            "       a.NoFBook, " +
            "       a.NoMBook, " +
            "       a.Reason, " +
            "       a.TotalAmount, " +
            "       a.TotalAmountOriginal " +
            "from ViewCheckConstraint a  " +
            "         left join Type b on a.TypeID = b.ID  " +
            "where a.CompanyID = :companyID and ");
        params.put("companyID", companyID);
        String order = " order by date desc , ";
        if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            order += "NoFBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 0) and (");
        } else {
            order += "NoMBook desc ";
            sqlCount.append("(TypeLedger = 2 or TypeLedger = 1) and (");
        }
        int count = 0;
        for (String nameColumn : nameColumns) {
            sqlCount.append(nameColumn);
            sqlCount.append(" = :id ");
            params.put("id", uuid);
            if (count < nameColumns.size() - 1) {
                sqlCount.append(" or ");
            }
            count++;
        }
        sqlCount.append(")) a");
        Query countQuerry = entityManager.createNativeQuery(sqlCount.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            sql.append("select DISTINCT a.ReferenceID as id, " +
                "       a.TypeID      as typeID, " +
                "       a.TypeGroupID      as TypeGroupID, " +
                "       b.TypeName, " +
                "       date, " +
                "       a.NoFBook, " +
                "       a.NoMBook, " +
                "       a.Reason, " +
                "       a.TotalAmount, " +
                "       a.TotalAmountOriginal " +
                "from ViewCheckConstraint a  " +
                "         left join Type b on a.TypeID = b.ID  " +
                "where a.CompanyID = :companyID and ");
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
                sql.append("(TypeLedger = 2 or TypeLedger = 0) and (");
            } else {
                sql.append("(TypeLedger = 2 or TypeLedger = 1) and (");
            }
            count = 0;
            for (String nameColumn : nameColumns) {
                sql.append(nameColumn);
                sql.append(" = :id ");
                if (count < nameColumns.size() - 1) {
                    sql.append(" or ");
                }
                count++;
            }
            sql.append(")");
            sql.append(order);
            Query query = entityManager.createNativeQuery(sql.toString(), "VoucherRefCatalogDTO");
            setParamsWithPageable(query, params, pageable, total);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, total.longValue());
    }

    @Override
    public Long checkQuantityLimitedNoVoucher(UUID org) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM ViewVoucherNoPosted WHERE CompanyID = :companyID ");
        params.put("companyID", org);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number result = (Number) countQuerry.getSingleResult();
        return result.longValue();
    }
}
