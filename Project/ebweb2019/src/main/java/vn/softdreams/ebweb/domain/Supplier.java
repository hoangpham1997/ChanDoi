package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A SupplierService.
 */
@Entity
@Table(name = "supplierservice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Supplier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "supplierservicecode")
    private String supplierServiceCode;

    @Column(name = "supplierservicename")
    private String supplierServiceName;

    @Column(name = "pathaccess")
    private String pathAccess;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSupplierServiceCode() {
        return supplierServiceCode;
    }

    public void setSupplierServiceCode(String supplierServiceCode) {
        this.supplierServiceCode = supplierServiceCode;
    }

    public String getSupplierServiceName() {
        return supplierServiceName;
    }

    public void setSupplierServiceName(String supplierServiceName) {
        this.supplierServiceName = supplierServiceName;
    }

    public String getPathAccess() {
        return pathAccess;
    }

    public void setPathAccess(String pathAccess) {
        this.pathAccess = pathAccess;
    }
}
