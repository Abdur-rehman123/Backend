package KirayoApp.Kirayo.returnStatus;

import java.util.Date;

public class ProductRequestResponse {
    private Long requestId;
    private String requestStatus;
    private Date timeStamp;
    private ProductsResponse products;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ProductsResponse getProduct() {
        return products;
    }

    public void setProduct(ProductsResponse products) {
        this.products = products;
    }
}
