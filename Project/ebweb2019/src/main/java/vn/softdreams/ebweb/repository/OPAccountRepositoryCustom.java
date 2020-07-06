package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.OPAccount;

import java.util.List;
import java.util.UUID;


public interface OPAccountRepositoryCustom {

    List<OPAccount> insertBulk(List<OPAccount> opAccounts);
}
