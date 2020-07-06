package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.IAInvoiceTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the IAInvoiceTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IAInvoiceTemplateRepository extends JpaRepository<IAInvoiceTemplate, UUID> {

}
