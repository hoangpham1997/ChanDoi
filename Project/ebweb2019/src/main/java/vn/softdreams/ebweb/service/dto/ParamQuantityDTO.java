package vn.softdreams.ebweb.service.dto;

import java.util.List;
import java.util.UUID;

public class ParamQuantityDTO {

    private List<UUID> materialGoodsIDs;
    private List<UUID> repositoryIDs;
    private String postedDate;

    public ParamQuantityDTO(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs, String postedDate) {
        this.materialGoodsIDs = materialGoodsIDs;
        this.repositoryIDs = repositoryIDs;
        this.postedDate = postedDate;
    }

    public ParamQuantityDTO() {
    }

    public List<UUID> getMaterialGoodsIDs() {
        return materialGoodsIDs;
    }

    public void setMaterialGoodsIDs(List<UUID> materialGoodsIDs) {
        this.materialGoodsIDs = materialGoodsIDs;
    }

    public List<UUID> getRepositoryIDs() {
        return repositoryIDs;
    }

    public void setRepositoryIDs(List<UUID> repositoryIDs) {
        this.repositoryIDs = repositoryIDs;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }
}
