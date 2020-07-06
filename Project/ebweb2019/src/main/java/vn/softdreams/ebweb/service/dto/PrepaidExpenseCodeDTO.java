package vn.softdreams.ebweb.service.dto;

import java.util.List;
import java.util.UUID;

public class PrepaidExpenseCodeDTO {
   private UUID id;
   private String species;
   private String code;
   private String name;
   private Integer type;

    public PrepaidExpenseCodeDTO() {
    }

    public PrepaidExpenseCodeDTO(UUID id, String species, String code, String name, Integer type) {
        this.id = id;
        this.species = species;
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
