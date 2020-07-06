package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import vn.softdreams.ebweb.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.validation.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Instant;

/**
 * A user.
 */
@Entity
@Table(name = "ebuser")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "UserDTO",
        classes = {
            @ConstructorResult(
                targetClass = UserDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "login", type = String.class),
                    @ColumnResult(name = "fullName", type = String.class),
                    @ColumnResult(name = "job", type = String.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "PackageName", type = String.class),
                    @ColumnResult(name = "mobilePhone", type = String.class),
                    @ColumnResult(name = "OrganizationUnitName", type = String.class),
                    @ColumnResult(name = "status", type = Integer.class),
                    @ColumnResult(name = "packageId", type = UUID.class),
                    @ColumnResult(name = "companyId", type = UUID.class),
                    @ColumnResult(name = "isTotalPackage", type = Boolean.class),
                }
            )
        }
    )})
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserId")
    @SequenceGenerator(name = "UserId", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "full_name")
    private String fullName;

    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Column(name = "lang_Key", length = 20)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activationkey", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @Column(name = "job")
    private String job;

    @Column(name = "description")
    private String description;

    @Column(name = "work_On_Book")
    private Integer workOnBook;

    @Column(name = "home_Phone")
    private String homePhone;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer status;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer organizationUnitName;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer packageName;

    @Column(name = "mobile_Phone")
    private String mobilePhone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "address")
    private String address;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "id_Card")
    private String idCard;

    @Column(name = "is_System")
    private Boolean isSystem;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "parentID")
    private Long parentID;

    @Column(name = "activeddate")
    private LocalDate activedDate;

    @Column(name = "exprireddate")
    private LocalDate expriredDate;


    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID orgID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID packageID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private EbUserPackage ebUserPackage;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean isChangePassword;

    //    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "ebuserauthority",
        joinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authoritycode", referencedColumnName = "code")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @BatchSize(size = 20)
    private Set<EbAuthority> authorities = new HashSet<>();

    //    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "ebuserorganizationunit",
        joinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "orgid", referencedColumnName = "id")})
    private Set<OrganizationUnit> organizationUnits = new HashSet<>();
    // add by anmt
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "ebusergroup",
        joinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "groupid", referencedColumnName = "id")})
    private Set<EbGroup> ebGroups = new HashSet<>();

    public User() {
    }

    public User(Long id, String login, String password, String email, Boolean activated,
                String langKey, String resetKey, Instant resetDate, String job, String fullName,
                String description, Integer workOnBook, String homePhone, String mobilePhone,
                String fax, String address, byte[] photo, String country, String province,
                String city, String idCard, Boolean isSystem, String activationKey,
                Long parentID, LocalDate activedDate, LocalDate expriredDate, Integer status) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.resetKey = resetKey;
        this.resetDate = resetDate;
        this.job = job;
        this.fullName = fullName;
        this.description = description;
        this.workOnBook = workOnBook;
        this.homePhone = homePhone;
        this.mobilePhone = mobilePhone;
        this.fax = fax;
        this.address = address;
        this.photo = photo;
        this.country = country;
        this.province = province;
        this.city = city;
        this.idCard = idCard;
        this.isSystem = isSystem;
        this.activationKey = activationKey;
        this.parentID = parentID;
        this.activedDate = activedDate;
        this.expriredDate = expriredDate;
        this.status = status;
    }

    public EbUserPackage getEbUserPackage() {
        return ebUserPackage;
    }

    public void setEbUserPackage(EbUserPackage ebUserPackage) {
        this.ebUserPackage = ebUserPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<EbAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<EbAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorkOnBook() {
        return workOnBook;
    }

    public void setWorkOnBook(Integer workOnBook) {
        this.workOnBook = workOnBook;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

//    public Boolean getSystem() {
//        return isSystem;
//    }
//
//    public void setSystem(Boolean system) {
//        isSystem = system;
//    }

    public Boolean isIsSystem() {
        return isSystem;
    }

    public User isSystem(Boolean isSystem) {
        this.isSystem = isSystem;
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Set<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(Set<OrganizationUnit> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public Set<EbGroup> getEbGroups() {
        return ebGroups;
    }

    public void setEbGroups(Set<EbGroup> ebGroups) {
        this.ebGroups = ebGroups;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public LocalDate getActivedDate() {
        return activedDate;
    }

    public void setActivedDate(LocalDate activedDate) {
        this.activedDate = activedDate;
    }

    public LocalDate getExpriredDate() {
        return expriredDate;
    }

    public void setExpriredDate(LocalDate expriredDate) {
        this.expriredDate = expriredDate;
    }

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public UUID getOrgID() {
        return orgID;
    }

    public void setOrgID(UUID orgID) {
        this.orgID = orgID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(Integer organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public Integer getPackageName() {
        return packageName;
    }

    public void setPackageName(Integer packageName) {
        this.packageName = packageName;
    }

    public Boolean getIsChangePassword() {
        return isChangePassword;
    }

    public void setIsChangePassword(Boolean changePassword) {
        isChangePassword = changePassword;
    }
}
