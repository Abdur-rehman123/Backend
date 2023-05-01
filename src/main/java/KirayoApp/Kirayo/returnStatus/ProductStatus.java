package KirayoApp.Kirayo.returnStatus;

import java.util.List;

public class ProductStatus {
    private Boolean status;
    private String message;


    private List<ProductsResponse> productsResponse;
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public List<ProductsResponse> getProductsResponse() {
        return productsResponse;
    }

    public void setProductsResponse(List<ProductsResponse> productsResponse) {
        this.productsResponse = productsResponse;
    }

}
