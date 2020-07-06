package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.BankAccountDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

public class BankAccountDetailsRepositoryImpl implements BankAccountDetailsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<BankAccountDetails> pageableAllBankAccountDetails(Pageable pageable, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        List<BankAccountDetails> bankAccountDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM BankAccountDetail WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , BankAccountDetails.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            bankAccountDetails = query.getResultList();

        }
        return new PageImpl<>(bankAccountDetails, pageable, total.longValue());
    }

    @Override
    public Page<BankAccountDetails> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<BankAccountDetails> bankAccountDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM BankAccountDetail WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , BankAccountDetails.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            bankAccountDetails = query.getResultList();
        }
        return new PageImpl<>(bankAccountDetails, pageable, total.longValue());
    }

    @Override
    public List<BankAccountDetails> findAllByIsActiveCustom(List<UUID> orgTKNH, List<UUID> orgTTD, UUID parentID) {
        Map<String, Object> params = new HashMap<>();
        String sqlSelectBankAccountDetail = "SELECT ID as id, BankAccount as bankAccount, BankName as bankName ";
        String sqlCreditCard = "SELECT a.ID as id, a.CreditCardNumber as bankAccount, b.BankName as bankName ";
        params.put("orgTKNH", orgTKNH);
        params.put("orgTTD", orgTTD);
        params.put("parentID", parentID);
        Query query = entityManager.createNativeQuery( "SELECT id as id, bankAccount as bankAccount, bankName as bankName FROM ( "
                + sqlSelectBankAccountDetail + " FROM BankAccountDetail WHERE CompanyID in :orgTKNH AND isActive = 1 " + " UNION ALL " +
                sqlCreditCard + " FROM CreditCard a LEFT JOIN Bank b ON a.BankIDIssueCard = b.ID WHERE a.CompanyID in :orgTTD AND a.IsActive = 1 AND b.CompanyID = :parentID AND b.IsActive = 1 "
                + " ) as #RS ORDER BY bankAccount", "bankAccountDetailDTO");
        Common.setParams(query, params);

        return ((List<BankAccountDetailDTO>) query.getResultList()).stream().map(bankAccountDetailDTO -> {
            BankAccountDetails bankAccountDetails = new BankAccountDetails();
            bankAccountDetails.setId(bankAccountDetailDTO.getId());
            bankAccountDetails.setBankAccount(bankAccountDetailDTO.getBankAccount());
            bankAccountDetails.setBankName(bankAccountDetailDTO.getBankName());
            return bankAccountDetails;
        }).collect(Collectors.toList());
    }

    @Override
    public BankAccountDetailDTO getByPaymentVoucherId(UUID id, Integer typeId) {
        List<BankAccountDetailDTO> dtos = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        if (typeId.equals(Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG)) {
            sql.append("select bad.Id as Id, bad.bankaccount as bankaccount, bad.bankname as bankname from BankAccountDetail bad inner join MBCreditCard mcc on bad.id = mcc.CreditCardID where mcc.id = :id");
        } else {
            sql.append("select bad.Id as Id, bad.bankaccount as bankaccount, bad.bankname as bankname from BankAccountDetail bad inner join MBTellerPaper mtp on bad.id = mtp.BankAccountDetailID where mtp.id = :id");
        }
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "bankAccountDetailDTO");
        Common.setParams(query, params);
        dtos = query.getResultList();
        if (dtos.size() > 0) {
            return dtos.get(0);
        }
        return null;
    }

    @Override
    public List<BankAccountDetails> getAllBankAccountDetailsByOrgID(List<UUID> orgTKNH, List<UUID> orgTTD, UUID parentID) {
        Map<String, Object> params = new HashMap<>();
        String sqlSelectBankAccountDetail = "SELECT ID as id, BankAccount as bankAccount, BankName as bankName ";
        String sqlCreditCard = "SELECT a.ID as id, a.CreditCardNumber as bankAccount, b.BankName as bankName ";
        params.put("orgTKNH", orgTKNH);
        params.put("orgTTD", orgTTD);
        params.put("parentID", parentID);
        Query query = entityManager.createNativeQuery( "SELECT id as id, bankAccount as bankAccount, bankName as bankName FROM ( "
                + sqlSelectBankAccountDetail + " FROM BankAccountDetail WHERE CompanyID in :orgTKNH AND isActive = 1 " + " UNION ALL " +
                sqlCreditCard + " FROM CreditCard a LEFT JOIN Bank b ON a.BankIDIssueCard = b.ID WHERE a.CompanyID in :orgTTD AND a.IsActive = 1 AND b.CompanyID = :parentID AND b.IsActive = 1 "
                + " ) as #RS ORDER BY bankAccount", "bankAccountDetailDTO");
        Common.setParams(query, params);

        return ((List<BankAccountDetailDTO>) query.getResultList()).stream().map(bankAccountDetailDTO -> {
            BankAccountDetails bankAccountDetails = new BankAccountDetails();
            bankAccountDetails.setId(bankAccountDetailDTO.getId());
            bankAccountDetails.setBankAccount(bankAccountDetailDTO.getBankAccount());
            bankAccountDetails.setBankName(bankAccountDetailDTO.getBankName());
            return bankAccountDetails;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ComboboxBankAccountDetailDTO> findAllByIsActive(List<UUID> companyIDTKNH, List<UUID> companyIDTTD, UUID orgID) {
        List<ComboboxBankAccountDetailDTO> comboboxBankAccountDetailDTOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String sqlSelectBankAccountDetail = "SELECT ID as id, BankAccount as bankAccount, BankName as bankName, 0 as type ";
        String sqlCreditCard = "SELECT a.ID as id, a.CreditCardNumber as bankAccount, b.BankName as bankName, 1 as type ";
        sql.append(" FROM BankAccountDetail WHERE CompanyID in :companyIDTKNH AND isActive = 1 ");
        sql2.append(" FROM CreditCard a LEFT JOIN Bank b ON a.BankIDIssueCard = b.ID WHERE a.CompanyID in :companyIDTTD AND a.IsActive = 1 AND b.CompanyID = :orgID AND b.IsActive = 1 ");
        params.put("companyIDTKNH", companyIDTKNH);
        params.put("companyIDTTD", companyIDTTD);
        params.put("orgID", orgID);
        Query query = entityManager.createNativeQuery( "SELECT id as id, bankAccount as bankAccount, bankName as bankName FROM ( " + sqlSelectBankAccountDetail + sql.toString() + " UNION ALL " + sqlCreditCard + sql2.toString() + " ) as #RS ORDER BY bankAccount", "ComboboxBankAccountDetailDTO");
        Common.setParams(query, params);
        comboboxBankAccountDetailDTOList = query.getResultList();
        return comboboxBankAccountDetailDTOList;
    }

    @Override
    public List<ComboboxBankAccountDetailDTO> findAllForAccType(UUID org, UUID org2) {
        List<ComboboxBankAccountDetailDTO> comboboxBankAccountDetailDTOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String sqlSelectBankAccountDetail = "SELECT ID as id, BankAccount as bankAccount, BankName as bankName ";
        String sqlCreditCard = "SELECT a.ID as id, a.CreditCardNumber as bankAccount, b.BankName as bankName ";
        sql.append(" FROM BankAccountDetail WHERE CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID = :org) OR (ParentID = :org AND AccType = 0 AND UnitType = 1)) AND isActive = 1 ");
        sql2.append(" FROM CreditCard a LEFT JOIN Bank b ON a.BankIDIssueCard = b.ID WHERE a.CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID = :org) OR (ParentID = :org AND AccType = 0 AND UnitType = 1)) AND a.IsActive = 1 AND b.CompanyID = :org2 AND b.IsActive = 1 ");
        params.put("org", org);
        params.put("org2", org2);
        Query query = entityManager.createNativeQuery( "SELECT id as id, bankAccount as bankAccount, bankName as bankName FROM ( " + sqlSelectBankAccountDetail + sql.toString() + " UNION ALL " + sqlCreditCard + sql2.toString() + " ) as #RS ORDER BY bankAccount", "ComboboxBankAccountDetailDTO");
        Common.setParams(query, params);
        comboboxBankAccountDetailDTOList = query.getResultList();
        return comboboxBankAccountDetailDTOList;
    }
}
