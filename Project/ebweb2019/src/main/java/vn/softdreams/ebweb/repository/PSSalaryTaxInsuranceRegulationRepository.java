package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.PSSalaryTaxInsuranceRegulation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the PSSalaryTaxInsuranceRegulation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PSSalaryTaxInsuranceRegulationRepository extends JpaRepository<PSSalaryTaxInsuranceRegulation, UUID> {

}
