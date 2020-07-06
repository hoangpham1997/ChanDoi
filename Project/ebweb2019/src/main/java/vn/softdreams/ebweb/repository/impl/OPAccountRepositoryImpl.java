package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.repository.OPAccountRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class OPAccountRepositoryImpl implements OPAccountRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<OPAccount> insertBulk(List<OPAccount> opAccounts) {
        final List<OPAccount> savedEntities = new ArrayList<>(opAccounts.size());
        int i = 0;
        for (OPAccount t : opAccounts) {
            if (save(entityManager, t) == 1)
                savedEntities.add(t);
            i++;
            if (i > Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;
    }
    private int save(EntityManager em, OPAccount opAccount) {
        if (opAccount.getId() != null) {
            em.merge(opAccount);
        } else {
            em.persist(opAccount);
        }
        return 1;
    }
}
