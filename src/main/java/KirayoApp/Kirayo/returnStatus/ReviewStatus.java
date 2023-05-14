package KirayoApp.Kirayo.returnStatus;

import java.util.List;

public class ReviewStatus {
    private Boolean status;
    private String message;


    private List<ReviewResponse> reviewResponses;
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
    public List<ReviewResponse> getProductsResponse() {
        return reviewResponses;
    }

    public void setProductsResponse(List<ReviewResponse> reviewResponses) {
        this.reviewResponses = reviewResponses;
    }
}
