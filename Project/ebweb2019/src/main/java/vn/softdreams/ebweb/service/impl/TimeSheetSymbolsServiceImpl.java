package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TimeSheetSymbolsService;
import vn.softdreams.ebweb.domain.TimeSheetSymbols;
import vn.softdreams.ebweb.repository.TimeSheetSymbolsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TimeSheetSymbols.
 */
@Service
@Transactional
public class TimeSheetSymbolsServiceImpl implements TimeSheetSymbolsService {

    private final Logger log = LoggerFactory.getLogger(TimeSheetSymbolsServiceImpl.class);

    private final TimeSheetSymbolsRepository timeSheetSymbolsRepository;

    public TimeSheetSymbolsServiceImpl(TimeSheetSymbolsRepository timeSheetSymbolsRepository) {
        this.timeSheetSymbolsRepository = timeSheetSymbolsRepository;
    }

    /**
     * Save a timeSheetSymbols.
     *
     * @param timeSheetSymbols the entity to save
     * @return the persisted entity
     */
    @Override
    public TimeSheetSymbols save(TimeSheetSymbols timeSheetSymbols) {
        log.debug("Request to save TimeSheetSymbols : {}", timeSheetSymbols);        return timeSheetSymbolsRepository.save(timeSheetSymbols);
    }

    /**
     * Get all the timeSheetSymbols.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TimeSheetSymbols> findAll(Pageable pageable) {
        log.debug("Request to get all TimeSheetSymbols");
        return timeSheetSymbolsRepository.findAll(pageable);
    }


    /**
     * Get one timeSheetSymbols by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TimeSheetSymbols> findOne(UUID id) {
        log.debug("Request to get TimeSheetSymbols : {}", id);
        return timeSheetSymbolsRepository.findById(id);
    }

    /**
     * Delete the timeSheetSymbols by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TimeSheetSymbols : {}", id);
        timeSheetSymbolsRepository.deleteById(id);
    }
}
