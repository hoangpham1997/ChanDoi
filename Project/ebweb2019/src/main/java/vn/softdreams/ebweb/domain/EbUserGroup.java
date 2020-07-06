package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ebusergroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbUserGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @ManyToOne(optional = false)
//    @NotNull
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "groupid")
//    private EbGroup group;
//
//    @ManyToOne(optional = false)
//    @NotNull
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "userid")
//    private User user;
    @Column(name = "userid")
    private Long userID;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "groupid")
    private UUID groupID;

    @Column(name = "workingonbook")
    private Integer workingOnBook;

    @Override
    public String toString() {
        return "EbUserGroup{" +
            "id=" + id +
            '}';
    }

    //    public EbUserGroup(@NotNull EbGroup group, @NotNull User user) {
//        this.group = group;
//        this.user = user;
//    }
    public EbUserGroup(@NotNull UUID group, @NotNull Long user) {
        this.groupID = group;
        this.userID = user;
    }

    public EbUserGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public Integer getWorkingOnBook() {
        return workingOnBook;
    }

    public void setWorkingOnBook(Integer workingOnBook) {
        this.workingOnBook = workingOnBook;
    }

//    public EbGroup getGroup() {
//        return group;
//    }
//
//    public void setGroup(EbGroup group) {
//        this.group = group;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

}
