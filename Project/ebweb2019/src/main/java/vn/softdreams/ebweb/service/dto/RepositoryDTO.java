package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class RepositoryDTO {
    private UUID id;
    private String repositoryCode;
    private String repositoryName;

    public RepositoryDTO(UUID id, String repositoryCode, String repositoryName) {
        this.id = id;
        this.repositoryCode = repositoryCode;
        this.repositoryName = repositoryName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
