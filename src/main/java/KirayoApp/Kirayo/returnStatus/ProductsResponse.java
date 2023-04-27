package KirayoApp.Kirayo.returnStatus;

import KirayoApp.Kirayo.model.Product;
import java.util.List;

public class ProductsResponse {


    private boolean status;
    private Product product;
    private List<Long>ids;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Long> getId() {
        return ids;
    }

    public void setId(List<Long> id) {
        this.ids = id;
    }
}
