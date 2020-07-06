package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A PrepaidExpenseVoucher.
 */
@Entity
@Table(name = "prepaidexpensevoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrepaidExpenseVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "prepaidexpenseid")
    private UUID prepaidExpenseID;

    @Column(name = "refid", precision = 10, scale = 2)
    private UUID refID;

    @Column(name = "typeid")
    private Integer typeID;

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }
}
