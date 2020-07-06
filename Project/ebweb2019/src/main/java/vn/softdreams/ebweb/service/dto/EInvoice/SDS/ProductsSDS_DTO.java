package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ProductsSDS_DTO {
    private List<ProductSDS_DTO> Product;

    @XmlElement(name = "Product")
    public List<ProductSDS_DTO> getProduct() {
        return Product;
    }

    public void setProduct(List<ProductSDS_DTO> product) {
        Product = product;
    }
}
