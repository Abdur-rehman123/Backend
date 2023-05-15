package KirayoApp.Kirayo.returnStatus;

import java.util.List;

public class ProductRequestStatus {
    private Boolean status;
    private String message;
    private List<ProductRequestResponse> productRequestResponses;

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

    public List<ProductRequestResponse> getProductRequestResponses() {
        return productRequestResponses;
    }

    public void setProductRequestResponses(List<ProductRequestResponse> productRequestResponses) {
        this.productRequestResponses = productRequestResponses;
    }
}
