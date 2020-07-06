package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.util.ArrayList;
import java.util.List;

public class ListEInvoiceVietTel {
    private List<EInvoiceVietTel> commonInvoiceInputs = new ArrayList<>();

    public List<EInvoiceVietTel> getCommonInvoiceInputs() {
        return commonInvoiceInputs;
    }

    public void setCommonInvoiceInputs(List<EInvoiceVietTel> commonInvoiceInputs) {
        this.commonInvoiceInputs = commonInvoiceInputs;
    }
}
