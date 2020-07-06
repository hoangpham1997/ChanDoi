package vn.softdreams.ebweb.service.dto;

import java.util.List;
import java.util.UUID;

public class ListObjectDTO {
    private List<ObjectDTO> materialGoodIDS;
    private List<ObjectDTO> repositoryIDS;

    public ListObjectDTO() {
    }

    public ListObjectDTO(List<ObjectDTO> materialGoodIDS, List<ObjectDTO> repositoryIDS) {
        this.materialGoodIDS = materialGoodIDS;
        this.repositoryIDS = repositoryIDS;
    }

    public List<ObjectDTO> getMaterialGoodIDS() {
        return materialGoodIDS;
    }

    public void setMaterialGoodIDS(List<ObjectDTO> materialGoodIDS) {
        this.materialGoodIDS = materialGoodIDS;
    }

    public List<ObjectDTO> getRepositoryIDS() {
        return repositoryIDS;
    }

    public void setRepositoryIDS(List<ObjectDTO> repositoryIDS) {
        this.repositoryIDS = repositoryIDS;
    }
}
