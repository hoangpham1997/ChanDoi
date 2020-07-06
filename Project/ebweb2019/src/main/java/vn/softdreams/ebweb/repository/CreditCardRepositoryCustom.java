package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CreditCard;

import java.util.List;
import java.util.UUID;

public interface CreditCardRepositoryCustom {
    CreditCard findByCreditCardNumber(String creditCardNumber, UUID companyID);

    Page<CreditCard> pageableAllCreditCard(Pageable sort, UUID org);

    Page<CreditCard> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID);

    Page<CreditCard> pageableAllCreditCards(Pageable sort, UUID org);
}
