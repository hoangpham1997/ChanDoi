package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Repository;

public class RepositorySaveDTO {
    private Repository repository;

    private int status;

    public RepositorySaveDTO() {
    }

    public RepositorySaveDTO(Repository repository, int status) {
        this.repository = repository;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
