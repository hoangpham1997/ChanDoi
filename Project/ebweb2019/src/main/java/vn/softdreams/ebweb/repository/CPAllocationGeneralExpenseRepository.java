package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationGeneralExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationGeneralExpenseRepository extends JpaRepository<CPAllocationGeneralExpense, UUID>, CPAllocationGeneralExpenseRepositoryCustom {


}
