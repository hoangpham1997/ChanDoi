package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.SearchVoucherBank;

import java.util.UUID;

@Repository
public interface BankRepositoryCustom{
    /**
     * @param pageable
     * @param bankCode
     * @param bankName
     * @param bankNameRepresent
     * @param address
     * @param description
     * @param isActive
     * @return
     */
    Page<Bank> findAll(Pageable pageable, String bankCode, String bankName, String bankNameRepresent, String address, String description, Boolean isActive);

    Page<Bank> pageableAllBank(Pageable sort, UUID org);

    Page<Bank> findAllBank(Pageable sort, SearchVoucherBank searchVoucherBank, UUID companyID);
}

