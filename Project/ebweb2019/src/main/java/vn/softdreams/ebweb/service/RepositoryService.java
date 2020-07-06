package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositorySaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Repository.
 */
public interface RepositoryService {

    /**
     * Save a repository.
     *
     * @param repository the entity to save
     * @return the persisted entity
     */
    RepositorySaveDTO save(Repository repository);
    Page<Repository> pageableAllRepositories(Pageable pageable);

    /**
     * Get all the repositories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Repository> findAll(Pageable pageable);


    /**
     * Get the "id" repository.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Repository> findOne(UUID id);

    /**
     * Delete the "id" repository.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<Repository> getRepositoryCombobox(Boolean similarBranch);

    List<Repository> findAllByCompanyID();

    List<Repository> listAllRepositories(Boolean isGetAllCompany);

    HandlingResultDTO deleteRepository(List<UUID> uuids);

    List<Repository> getRepositoryReport(UUID companyID, Boolean similarBranch);

    List<Repository> getRepositoryComboboxGetAll();
}
