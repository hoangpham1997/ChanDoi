package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TemplateService;
import vn.softdreams.ebweb.domain.Template;
import vn.softdreams.ebweb.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Template.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * Save a template.
     *
     * @param template the entity to save
     * @return the persisted entity
     */
    @Override
    public Template save(Template template) {
        log.debug("Request to save Template : {}", template);        return templateRepository.save(template);
    }

    /**
     * Get all the templates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Template> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        return templateRepository.findAll(pageable);
    }


    /**
     * Get one template by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Template> findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        return templateRepository.findById(id);
    }

    /**
     * Delete the template by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.deleteById(id);
    }
}
