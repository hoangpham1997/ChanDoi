package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;
import vn.softdreams.ebweb.domain.Template;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;

import java.util.List;

public class SaBillSearchDTO {
    private List<AccountingObjectDTO> accountingObjects;
    private List<IaPublishInvoiceDetails> templates;
    private List<SeriDTO> seriDTOS;

    public SaBillSearchDTO() {
    }

    public SaBillSearchDTO(List<AccountingObjectDTO> accountingObjects, List<IaPublishInvoiceDetails> templates, List<SeriDTO> seriDTOS) {
        this.accountingObjects = accountingObjects;
        this.templates = templates;
        this.seriDTOS = seriDTOS;
    }

    public List<AccountingObjectDTO> getAccountingObjects() {
        return accountingObjects;
    }

    public void setAccountingObjects(List<AccountingObjectDTO> accountingObjects) {
        this.accountingObjects = accountingObjects;
    }

    public List<IaPublishInvoiceDetails> getTemplates() {
        return templates;
    }

    public void setTemplates(List<IaPublishInvoiceDetails> templates) {
        this.templates = templates;
    }

    public List<SeriDTO> getSeriDTOS() {
        return seriDTOS;
    }

    public void setSeriDTOS(List<SeriDTO> seriDTOS) {
        this.seriDTOS = seriDTOS;
    }
}


