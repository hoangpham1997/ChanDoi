package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.PersonalSalaryTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the PersonalSalaryTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalSalaryTaxRepository extends JpaRepository<PersonalSalaryTax, UUID> {

}
