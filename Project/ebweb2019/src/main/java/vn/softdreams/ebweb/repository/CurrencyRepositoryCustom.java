package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.SearchVoucherCurrency;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;
import vn.softdreams.ebweb.web.rest.dto.CurrencyCbbDTO;

import java.util.List;
import java.util.UUID;

public interface CurrencyRepositoryCustom {
    /**
     * @Author hieugie
     *
     * @param simpleName
     * @param org
     * @return
     */
    List<CurrencyCbbDTO> findAllDTO(String simpleName, UUID org);
    Page<Currency> pageableAllCurrency(Pageable sort, UUID org);
    Page<Currency> findAll1(Pageable sort, SearchVoucherCurrency searchVoucherCurrency, UUID companyID);
}
