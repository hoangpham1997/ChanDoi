package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeTableDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A EbGroup.
 */
@Entity
@Table(name = "ebgroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 20)
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "issystem")
    private Boolean isSystem;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Boolean check;

    //    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "ebgroupauth",
        joinColumns = {@JoinColumn(name = "groupid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authorityid", referencedColumnName = "id")})
/*    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)*/
    private Set<EbAuthority> authorities = new HashSet<>();

    // add by anmt
//    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "ebusergroup",
        joinColumns = {@JoinColumn(name = "groupid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")})
/*    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)*/
    private Set<User> users = new HashSet<>();

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private List<OrgTreeTableDTO> listOrg = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public EbGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public EbGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean system) {
        isSystem = system;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EbGroup ebGroup = (EbGroup) o;
        if (ebGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ebGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public EbGroup() {
    }

    public EbGroup(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EbGroup{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", code='" + code + '\'' +
            ", description='" + description + '\'' +
            ", isSystem=" + isSystem +
            ", check=" + check +
            '}';
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<EbAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<EbAuthority> authorities) {
        this.authorities = authorities;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public List<OrgTreeTableDTO> getListOrg() {
        return listOrg;
    }

    public void setListOrg(List<OrgTreeTableDTO> listOrg) {
        this.listOrg = listOrg;
    }

}

