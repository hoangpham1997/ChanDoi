package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.InvoiceTypeService;
import vn.softdreams.ebweb.domain.InvoiceType;
import vn.softdreams.ebweb.repository.InvoiceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing InvoiceType.
 */
@Service
@Transactional
public class InvoiceTypeServiceImpl implements InvoiceTypeService {

    private final Logger log = LoggerFactory.getLogger(InvoiceTypeServiceImpl.class);

    private final InvoiceTypeRepository invoiceTypeRepository;

    public InvoiceTypeServiceImpl(InvoiceTypeRepository invoiceTypeRepository) {
        this.invoiceTypeRepository = invoiceTypeRepository;
    }

    /**
     * Save a invoiceType.
     *
     * @param invoiceType the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceType save(InvoiceType invoiceType) {
        log.debug("Request to save InvoiceType : {}", invoiceType);        return invoiceTypeRepository.save(invoiceType);
    }

    /**
     * Get all the invoiceTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceType> findAll() {
        log.debug("Request to get all InvoiceTypes");
        return invoiceTypeRepository.findAllOrderByInvoiceTypeName();
    }


    /**
     * Get one invoiceType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceType> findOne(UUID id) {
        log.debug("Request to get InvoiceType : {}", id);
        return invoiceTypeRepository.findById(id);
    }

    /**
     * Delete the invoiceType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete InvoiceType : {}", id);
        invoiceTypeRepository.deleteById(id);
    }
}
