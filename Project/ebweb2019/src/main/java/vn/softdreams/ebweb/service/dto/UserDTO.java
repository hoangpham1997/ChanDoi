package vn.softdreams.ebweb.service.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.softdreams.ebweb.config.Constants;

import vn.softdreams.ebweb.domain.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 255)
    private String fullName;

//    @Email
//    @Size(min = 5, max = 254)
    private String email;

    private String firstName;

    private String lastName;

    private Boolean checked;

    private String confirmPassword;

    private boolean activated = false;

    @Size(min = 2, max = 6)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Integer status;

    private UUID orgID;

    private UUID packageID;

    private Set<String> authorities;

    private List<SystemOption> systemOption;

    private OrganizationUnit organizationUnit;

    private String organizationUnitName;

    private String packageName;

    // add by anmt
    private Set<EbGroup> ebGroups;
    private String address;
    private String mobilePhone;
    private List<OrganizationUnit> organizationUnits;
    private LocalDate birthday;
    private String country;
    private String city;
    private String province;
    private String idCard;
    private String description;
    private String homePhone;
    private String fax;
    private Integer workOnBook;
    private String job;
    private String password;
    private Boolean isSystem;
    private EbPackage ebPackage;
    private EbUserPackage ebUserPackage;
    private Boolean isChangePassword;
    private Boolean isEmptyIfNull;
    private Boolean isTotalPackage;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

 public UserDTO(Long id,
                String login,
                String fullName,
                String job,
                String email,
                String packageName,
                String mobilePhone,
                String organizationUnitName,
                Integer status,
                UUID packageId,
                UUID companyId,
                Boolean isTotalPackage) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.job = job;
        this.status = status;
        this.organizationUnitName = organizationUnitName;
        this.packageName = packageName;
        this.mobilePhone = mobilePhone;
        this.packageID = packageId;
        this.orgID = companyId;
        this.email = email;
        this.isTotalPackage = isTotalPackage;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream()
            .map(EbAuthority::getName)
            .collect(Collectors.toSet());
        // add by anmt
        this.ebGroups = user.getEbGroups();
        this.address = user.getAddress();
        this.mobilePhone = user.getMobilePhone();
        this.organizationUnits = new ArrayList<>(user.getOrganizationUnits());
        this.birthday = user.getBirthday();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.province = user.getProvince();
        this.idCard = user.getIdCard();
        this.description = user.getDescription();
        this.homePhone = user.getHomePhone();
        this.fax = user.getFax();
        this.workOnBook = user.getWorkOnBook();
        this.job = user.getJob();
        this.password = user.getPassword();
        this.isSystem = user.isIsSystem();
        this.status = user.getStatus();
        this.orgID = user.getOrgID();
        this.packageID = user.getPackageID();
        this.ebUserPackage = user.getEbUserPackage();
        this.isChangePassword = user.getIsChangePassword();
    }


    public UserDTO(User user, List<SystemOption> systemOption, OrganizationUnit orgUnit, EbPackage ebPackage,
                   EbUserPackage ebUserPackage, Set<String> authorities) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = authorities;
//        this.authorities = user.getAuthorities().stream()
//            .map(EbAuthority::getCode)
//            .collect(Collectors.toSet());
        // add authorities from EbGroup (filter ComID of user login)
//        if (orgUnit != null) {
//            Set<EbGroup> lst = user.getEbGroups().stream().filter(item -> item.getCompanyID().equals(orgUnit.getId())).collect(Collectors.toSet());
//            lst.forEach(ebGroup -> {
//                authorities.addAll(ebGroup.getAuthorities().stream().map(EbAuthority::getCode)
//                    .collect(Collectors.toList()));
//            });
//        }
        this.organizationUnit = orgUnit;
        this.systemOption = systemOption;
        this.ebPackage = ebPackage;
        this.ebUserPackage = ebUserPackage;
    }

    public Boolean getEmptyIfNull() {
        return isEmptyIfNull != null ? isEmptyIfNull : false;
    }

    public void setEmptyIfNull(Boolean emptyIfNull) {
        isEmptyIfNull = emptyIfNull;
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

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities != null ? authorities : new HashSet<>();
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<SystemOption> getSystemOption() {
        return systemOption;
    }

    public void setSystemOption(List<SystemOption> systemOption) {
        this.systemOption = systemOption;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", fullName='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(List<OrganizationUnit> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getWorkOnBook() {
        return workOnBook;
    }

    public void setWorkOnBook(Integer workOnBook) {
        this.workOnBook = workOnBook;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<EbGroup> getEbGroups() {
        return ebGroups;
    }

    public void setEbGroups(Set<EbGroup> ebGroups) {
        this.ebGroups = ebGroups;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean system) {
        isSystem = system;
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

    public EbPackage getEbPackage() {
        return ebPackage;
    }

    public void setEbPackage(EbPackage ebPackage) {
        this.ebPackage = ebPackage;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getIsChangePassword() {
        return isChangePassword;
    }

    public void setIsChangePassword(Boolean changePassword) {
        isChangePassword = changePassword;
    }

    public Boolean getIsTotalPackage() {
        return isTotalPackage;
    }

    public void setIsTotalPackage(Boolean totalPackage) {
        isTotalPackage = totalPackage;
    }

}
