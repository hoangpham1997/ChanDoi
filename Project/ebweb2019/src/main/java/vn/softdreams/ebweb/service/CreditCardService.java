package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CreditCard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CreditCardSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CreditCard.
 */
public interface CreditCardService {

    /**
     * Save a creditCard.
     *
     * @param creditCard the entity to save
     * @return the persisted entity
     */
    CreditCardSaveDTO save(CreditCard creditCard);

    /**
     * Get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CreditCard> findAll(Pageable pageable);
    Page<CreditCard> pageableAllCreditCard(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<CreditCard> findAll();

    Page<CreditCard> pageableAllCreditCards(Pageable pageable, Boolean isGetAllCompany);


    /**
     * Get the "id" creditCard.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CreditCard> findOne(UUID id);

    /**
     * Delete the "id" creditCard.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    CreditCard findByCreditCardNumber(String creditCardNumber);

    HandlingResultDTO deleteEmployee(List<UUID> uuids);

    List<CreditCard> findAllByCompanyID();
    List<CreditCard> findAllActiveByCompanyID();
}
